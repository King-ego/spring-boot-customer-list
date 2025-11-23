package com.diego.list.customers.services;

import com.diego.list.customers.dto.SessionStats;
import com.diego.list.customers.model.*;
import com.diego.list.customers.repository.DeviceRepository;
import com.diego.list.customers.redis.SessionRedisRepository;
import com.diego.list.customers.repository.UserRepository;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SessionService {

    private final SessionRedisRepository sessionRedisRepository;
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final SecurityMonitorService securityMonitor;
    private final DeviceFingerprintService fingerprintService;
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
        sessionRedisRepository.save(session);

        // Adiciona à lista de sessões do usuário
        String userSessionsKey = "user_sessions:" + user.getId();
        redisTemplate.opsForSet().add(userSessionsKey, sessionId);
        redisTemplate.expire(userSessionsKey, SESSION_DURATION);

        log.info("Sessão criada para usuário: {}, sessão: {}", user.getName(), sessionId);
        return session;
    }

    public Session getSession(String sessionId) {
        return sessionRedisRepository.findById(sessionId).orElse(null);
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
            securityMonitor.logSuspiciousActivity(session.getUserId(), "Mismatch de fingerprint", request);
            revokeSession(session.getSessionId(), "system", "Dispositivo alterado");
            return false;
        }

        // Atualiza última atividade
        updateLastActivity(session.getSessionId());

        return true;
    }

    public void revokeSession(String sessionId, String revokedBy, String reason) {
        Session session = sessionRedisRepository.findById(sessionId).orElse(null);
        if (session == null) return;

        session.setActive(false);
        session.setRevokedAt(LocalDateTime.now());
        session.setRevokedBy(revokedBy);
        session.setRevocationReason(reason);

        sessionRedisRepository.save(session);

        // Remove da lista de sessões ativas do usuário
        String userSessionsKey = "user_sessions:" + session.getUserId();
        redisTemplate.opsForSet().remove(userSessionsKey, sessionId);

        // Log de segurança
        securityMonitor.logSessionRevocation(session, revokedBy, reason);

        log.info("Sessão revogada: {}, motivo: {}, por: {}", sessionId, reason, revokedBy);
    }

    public int revokeAllUserSessions(UUID userId, String excludeSessionId, String revokedBy) {
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

        log.info("Revogadas {} sessões do usuário: {}", revokedCount, userId);
        return revokedCount;
    }

    public void registerDeviceIfNew(UUID userId, String deviceFingerprint, HttpServletRequest request) {
        var existingDevice = deviceRepository.findByUserIdAndDeviceFingerprint(userId, deviceFingerprint);

        if (existingDevice.isEmpty()) {
            Device device = new Device();
            device.setUser(userRepository.findById(userId).orElse(null));
            device.setDeviceFingerprint(deviceFingerprint);
            device.setUserAgent(request.getHeader("User-Agent"));
            device.setIpAddress(getClientIP(request));
            device.setFirstSeen(LocalDateTime.now());
            device.setLastSeen(LocalDateTime.now());
            device.setTrusted(false);

            deviceRepository.save(device);
            log.info("Novo dispositivo registrado para usuário: {}", userId);
        } else {
            Device device = existingDevice.get();
            device.setLastSeen(LocalDateTime.now());
            deviceRepository.save(device);
        }
    }

    private void updateLastActivity(String sessionId) {
        Session session = sessionRedisRepository.findById(sessionId).orElse(null);
        if (session != null) {
            session.setLastActivity(LocalDateTime.now());
            sessionRedisRepository.save(session);
        }
    }

    private String generateSecureSessionId() {
        return UUID.randomUUID().toString().replace("-", "") +
                System.currentTimeMillis();
    }

    private DeviceInfo extractDeviceInfo(HttpServletRequest request) {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));

        return new DeviceInfo(
                userAgent.getBrowser().getName(),
                userAgent.getOperatingSystem().getName(),
                request.getHeader("Screen-Resolution"),
                request.getHeader("Timezone"),
                request.getHeader("Accept-Language"),
                "Brasil", // Simulado - integrar com serviço de GeoIP
                "São Paulo", // Simulado
                userAgent.getOperatingSystem().isMobileDevice(),
                false, // Simplificado
                true // Simplificado
        );
    }

    private Set<String> getUserPermissions(User user) {
        Set<String> permissions = new HashSet<>();
        permissions.add("READ");

        if (user.getRole() == UserRole.ADMIN || user.getRole() == UserRole.SUPER_ADMIN) {
            permissions.add("WRITE");
            permissions.add("ADMIN");
        }

        return permissions;
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null) {
            return xfHeader.split(",")[0];
        }
        return request.getRemoteAddr();
    }

    public List<Session> getAllUserSessions(UUID userId) {
        return sessionRedisRepository.findByUserId(userId);
    }

    public List<Session> getActiveUserSessions(UUID userId) {
        List<Session> allSessions = getAllUserSessions(userId);
        return allSessions.stream()
                .filter(Session::isActive)
                .collect(Collectors.toList());
    }

    public SessionStats getUserSessionStats(UUID userId) {
        List<Session> allSessions = getAllUserSessions(userId);

        long activeSessions = allSessions.stream()
                .filter(Session::isActive)
                .count();

        Set<String> uniqueDevices = allSessions.stream()
                .map(Session::getDeviceFingerprint)
                .collect(Collectors.toSet());

        return new SessionStats(
                (int) activeSessions,
                uniqueDevices.size(),
                allSessions.size()
        );
    }


}