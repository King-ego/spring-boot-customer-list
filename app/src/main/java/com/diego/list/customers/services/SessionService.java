package com.diego.list.customers.services;

import com.diego.list.customers.model.DeviceInfo;
import com.diego.list.customers.model.Session;
import com.diego.list.customers.model.User;
import com.diego.list.customers.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final SecurityMonitorService securityMonitor;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final Duration SESSION_DURATION = Duration.ofMinutes(30);
    private static final Duration INACTIVITY_LIMIT = Duration.ofMinutes(15);

    public Session createSession(User user, HttpServletRequest request, String deviceFingerprint) {
        String sessionId = generateSecureSessionId();

        DeviceInfo deviceInfo = extractDeviceInfo(request);

        Session session = new Session();
        session.setSessionId(sessionId);
        session.setUserId(user.getId());
        session.setDeviceFingerprint(deviceFingerprint);
        session.setIpAddress(getClientIP(request));
        session.setUserAgent(request.getHeader("User-Agent"));
        session.setDeviceInfo(deviceInfo);
        session.setCreatedAt(LocalDateTime.now());
        session.setLastActivity(LocalDateTime.now());
        session.setExpiresAt(LocalDateTime.now().plus(SESSION_DURATION));
        session.setActive(true);
        session.setMfaVerified(true);
        session.setPermissions(getUserPermissions(user));

        // Salva no Redis
        sessionRepository.save(session);

        // Adiciona à lista de sessões do usuário
        String userSessionsKey = "user_sessions:" + user.getId();
        redisTemplate.opsForSet().add(userSessionsKey, sessionId);
        redisTemplate.expire(userSessionsKey, SESSION_DURATION);

        return session;
    }

    public void revokeSession(String sessionId, String revokedBy, String reason) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionException("Sessão não encontrada"));

        session.setActive(false);
        session.setRevokedAt(LocalDateTime.now());
        session.setRevokedBy(revokedBy);
        session.setRevocationReason(reason);

        sessionRepository.save(session);

        // Remove da lista de sessões ativas do usuário
        String userSessionsKey = "user_sessions:" + session.getUserId();
        redisTemplate.opsForSet().remove(userSessionsKey, sessionId);

        // Log de segurança
        securityMonitor.logSessionRevocation(session, revokedBy, reason);

        // Notifica via WebSocket se necessário
        notifySessionRevocation(session, reason);
    }

    public int revokeAllUserSessions(String userId, String excludeSessionId, String revokedBy) {
        String userSessionsKey = "user_sessions:" + userId;
        Set<Object> sessionIds = redisTemplate.opsForSet().members(userSessionsKey);

        if (sessionIds == null) return 0;

        int revokedCount = 0;
        for (Object sessionIdObj : sessionIds) {
            String sessionId = (String) sessionIdObj;
            if (!sessionId.equals(excludeSessionId)) {
                revokeSession(sessionId, revokedBy, "Revogação em massa");
                revokedCount++;
            }
        }

        return revokedCount;
    }

    public boolean validateSession(Session session, HttpServletRequest request) {
        // Verifica se está ativa
        if (!session.isActive()) {
            return false;
        }

        // Verifica expiração
        if (LocalDateTime.now().isAfter(session.getExpiresAt())) {
            revokeSession(session.getSessionId(), "system", "Sessão expirada");
            return false;
        }

        // Verifica inatividade
        if (Duration.between(session.getLastActivity(), LocalDateTime.now()).compareTo(INACTIVITY_LIMIT) > 0) {
            revokeSession(session.getSessionId(), "system", "Inatividade prolongada");
            return false;
        }

        // Verifica fingerprint do dispositivo
        String currentFingerprint = fingerprintService.generateFingerprint(request);
        if (!session.getDeviceFingerprint().equals(currentFingerprint)) {
            securityMonitor.logSuspiciousActivity(session.getUserId(),
                    "Mismatch de fingerprint", request);
            revokeSession(session.getSessionId(), "system", "Dispositivo alterado");
            return false;
        }

        // Atualiza última atividade
        updateLastActivity(session.getSessionId());

        return true;
    }

    private void updateLastActivity(String sessionId) {
        Session session = sessionRepository.findById(sessionId).orElse(null);
        if (session != null) {
            session.setLastActivity(LocalDateTime.now());
            sessionRepository.save(session);
        }
    }

    private String generateSecureSessionId() {
        return UUID.randomUUID().toString().replace("-", "") +
                Instant.now().toEpochMilli();
    }

    private DeviceInfo extractDeviceInfo(HttpServletRequest request) {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));

        return new DeviceInfo(
                userAgent.getBrowser().getName(),
                userAgent.getOperatingSystem().getName(),
                request.getHeader("Screen-Resolution"),
                request.getHeader("Timezone"),
                request.getHeader("Accept-Language"),
                getCountryFromIP(getClientIP(request)),
                getCityFromIP(getClientIP(request)),
                userAgent.getOperatingSystem().isMobileDevice(),
                userAgent.getOperatingSystem().getDeviceType() == DeviceType.TABLET,
                userAgent.getOperatingSystem().getDeviceType() == DeviceType.COMPUTER
        );
    }
}