package com.diego.list.customers.services;

import com.diego.list.customers.dto.*;
import com.diego.list.customers.model.Session;
import com.diego.list.customers.model.User;
import com.diego.list.customers.repository.UserRepository;
import com.diego.list.customers.services.records.RiskAssessment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final SessionService sessionService;
    private final MFAService mfaService;
    private final SecurityMonitorService securityMonitor;
    private final DeviceFingerprintService fingerprintService;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public AuthResponse login(LoginRequest request, HttpServletRequest httpRequest) {
        // Rate limiting
        if (isRateLimited(request.getEmail(), httpRequest.getRemoteAddr())) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Muitas tentativas. Tente novamente mais tarde.");
        }

        // Busca usuário
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas"));

        // Verifica senha
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            securityMonitor.logAuthAttempt(user.getId(), false, httpRequest, "Senha incorreta");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas");
        }

        // Verifica se conta está ativa
        if (!user.isEnabled() || !user.isAccountNonLocked()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Conta bloqueada ou desativada");
        }

        // Gera fingerprint do dispositivo
        String deviceFingerprint = fingerprintService.generateFingerprint(httpRequest);

        // Calcula risco e decide se precisa de MFA
        RiskAssessment risk = securityMonitor.assessRisk(user, httpRequest, deviceFingerprint);

        if (risk.isRequiresMFA()) {
            String mfaId = mfaService.generateMFACode(user.getId());
            mfaService.sendMFACode(user, mfaId);

            String tempToken = generateTempToken(user.getId());

            return AuthResponse.builder()
                    .requiresMFA(true)
                    .mfaId(mfaId)
                    .tempToken(tempToken)
                    .riskScore(risk.getScore())
                    .riskFactors(risk.getFactors())
                    .build();
        }

        // Login direto - cria sessão
        return createSessionAndRespond(user, httpRequest, deviceFingerprint);
    }

    public AuthResponse verifyMFA(MFAVerificationRequest request, HttpServletRequest httpRequest) {
        TempTokenData tempData = verifyTempToken(request.getTempToken());
        User user = userRepository.findById(tempData.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido"));

        if (!mfaService.verifyMFACode(request.getMfaId(), request.getCode())) {
            securityMonitor.logMFAAttempt(user.getId(), false, httpRequest);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Código inválido");
        }

        String deviceFingerprint = fingerprintService.generateFingerprint(httpRequest);
        securityMonitor.logMFAAttempt(user.getId(), true, httpRequest);

        return createSessionAndRespond(user, httpRequest, deviceFingerprint);
    }

    private AuthResponse createSessionAndRespond(User user, HttpServletRequest httpRequest, String deviceFingerprint) {
        Session session = sessionService.createSession(user, httpRequest, deviceFingerprint);
        sessionService.registerDeviceIfNew(user.getId(), deviceFingerprint, httpRequest);

        securityMonitor.logAuthAttempt(user.getId(), true, httpRequest, "Login bem-sucedido");
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

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

        Long ipCount = redisTemplate.opsForValue().increment(ipKey, 1);
        Long userCount = redisTemplate.opsForValue().increment(userKey, 1);

        if (Objects.equals(ipCount, 1L)) {
            redisTemplate.expire(ipKey, Duration.ofMinutes(15));
        }
        if (Objects.equals(userCount, 1L)) {
            redisTemplate.expire(userKey, Duration.ofMinutes(15));
        }

        long ipCnt = ipCount == null ? 0L : ipCount;
        long userCnt = userCount == null ? 0L : userCount;

        return ipCnt > 10L || userCnt > 5L;
    }

    private String generateTempToken(UUID userId) {
        String token = UUID.randomUUID().toString();
        TempTokenData tempData = new TempTokenData(userId, LocalDateTime.now().plusMinutes(5));

        try {
            redisTemplate.opsForValue().set("temp_token:" + token, objectMapper.writeValueAsString(tempData), Duration.ofMinutes(5));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao gerar token temporário", e);
        }

        return token;
    }

    private TempTokenData verifyTempToken(String token) {
        try {
            String data = (String) redisTemplate.opsForValue().get("temp_token:" + token);
            if (data == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token expirado ou inválido");
            }
            return objectMapper.readValue(data, TempTokenData.class);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido");
        }
    }
}

record TempTokenData(UUID userId, LocalDateTime expiresAt) {
    // Adicione os getters
    public UUID getUserId() {
        return userId;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
}
