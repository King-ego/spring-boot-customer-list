package com.diego.list.customers.services;

import com.diego.list.customers.dto.*;
import com.diego.list.customers.model.Session;
import com.diego.list.customers.model.User;
import com.diego.list.customers.repository.UserRepository;
import com.diego.list.customers.services.records.RiskAssessment;
import com.diego.list.customers.services.records.TempTokenData;
import com.diego.list.customers.utils.KeyInRedisUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final SessionService sessionService;
    private final MFAService mfaService;
    private final SecurityMonitorService securityMonitor;
    private final DeviceFingerprintService fingerprintService;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final KeyInRedisUtils keyInRedisUtils;

    public AuthService(
            UserRepository userRepository,
            SessionService sessionService,
            MFAService mfaService,
            SecurityMonitorService securityMonitorService,
            DeviceFingerprintService deviceFingerprintService,
            PasswordEncoder passwordEncoder,
            RedisTemplate<String, Object> redisTemplate,
            ObjectMapper objectMapper,
            KeyInRedisUtils keyInRedisUtils
    ){
        this.userRepository = userRepository;
        this.sessionService = sessionService;
        this.mfaService = mfaService;
        this.securityMonitor = securityMonitorService;
        this.fingerprintService=deviceFingerprintService;
        this.passwordEncoder = passwordEncoder;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.keyInRedisUtils = keyInRedisUtils;
    }

    public AuthResponse login(LoginRequest request, HttpServletRequest httpRequest) {

        var validated = isRateLimited(request.getEmail(), httpRequest.getRemoteAddr());
        if (validated) {
            log.error("Too many login attempts");
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many login attempts. Please try again later.");
        }

        User user = this.userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas"));

        if (!this.passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.error("Invalid credentials for user");
            this.securityMonitor.logAuthAttempt(user.getId(), false, httpRequest, "Invalid credentials");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        if (!user.isEnabled() || !user.isAccountNonLocked()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Account is disabled or locked");
        }

        String deviceFingerprint = this.fingerprintService.generateFingerprint(httpRequest);

        RiskAssessment risk = this.securityMonitor.assessRisk(user, httpRequest, deviceFingerprint);

        if (risk.isRequiresMFA()) {
            String mfaId = this.mfaService.generateMFACode(user.getId());
            this.mfaService.sendMFACode(user, mfaId);

            String tempToken = generateTempToken(user.getId());

            return AuthResponse.builder()
                    .requiresMFA(true)
                    .mfaId(mfaId)
                    .tempToken(tempToken)
                    .riskScore(risk.getScore())
                    .riskFactors(risk.getFactors())
                    .build();
        }

        return createSessionAndRespond(user, httpRequest, deviceFingerprint);
    }

    public AuthResponse verifyMFA(MFAVerificationRequest request, HttpServletRequest httpRequest) {
        TempTokenData tempData = verifyTempToken(request.getTempToken());
        User user = this.userRepository.findById(tempData.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token"));

        if (!mfaService.verifyMFACode(request.getMfaId(), request.getCode())) {
            this.securityMonitor.logMFAAttempt(user.getId(), false, httpRequest);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid MFA code");
        }

        String deviceFingerprint = fingerprintService.generateFingerprint(httpRequest);
        securityMonitor.logMFAAttempt(user.getId(), true, httpRequest);

        return createSessionAndRespond(user, httpRequest, deviceFingerprint);
    }

    private AuthResponse createSessionAndRespond(User user, HttpServletRequest httpRequest, String deviceFingerprint) {
        Session session = this.sessionService.createSession(user, httpRequest, deviceFingerprint);
        this.sessionService.registerDeviceIfNew(user.getId(), deviceFingerprint, httpRequest);

        this.securityMonitor.logAuthAttempt(user.getId(), true, httpRequest, "Successful login");
        user.setLastLogin(LocalDateTime.now());
        this.userRepository.save(user);

        this.sessionService.cleanupInactiveSessions(user.getId());

        return AuthResponse.builder()
                .success(true)
                .sessionId(session.getSessionId())
                .user(user.toUserInfo())
                .mfaVerified(true)
                .build();
    }

    private boolean isRateLimited(String username, String ip) {
        String ipKey = "rate_limit:ip:" + ip;
        String userKey = "rate_limit:user:" + username;

        Long ipCount = this.redisTemplate.opsForValue().increment(ipKey, 1);
        Long userCount = this.redisTemplate.opsForValue().increment(userKey, 1);

        if (Objects.equals(ipCount, 1L)) {
            this.redisTemplate.expire(ipKey, Duration.ofMinutes(15));
        }
        if (Objects.equals(userCount, 1L)) {
            this.redisTemplate.expire(userKey, Duration.ofMinutes(15));
        }

        long ipCnt = ipCount == null ? 0L : ipCount;
        long userCnt = userCount == null ? 0L : userCount;

        return ipCnt > 10L || userCnt > 5L;
    }

    private String generateTempToken(UUID userId) {
        String token = UUID.randomUUID().toString();
        TempTokenData tempData = new TempTokenData(userId, LocalDateTime.now().plusMinutes(5));

        /*try {
            redisTemplate.opsForValue().set("tempToken:" + token, objectMapper.writeValueAsString(tempData), Duration.ofMinutes(5));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Generate temp token error", e);
        }*/

        this.keyInRedisUtils.punishKey("tempToken:" + token, tempData, Duration.ofMinutes(5));

        return token;
    }

    private TempTokenData verifyTempToken(String token) {
        try {
            String data = (String) this.redisTemplate.opsForValue().get("tempToken:" + token);
            log.info("token data: {}", data);
            if (data == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Expired or invalid token");
            }
            return this.objectMapper.readValue(data, TempTokenData.class);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token", e);
        }
    }
}
