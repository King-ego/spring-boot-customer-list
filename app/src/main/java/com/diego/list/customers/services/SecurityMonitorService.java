package com.diego.list.customers.services;

import com.diego.list.customers.model.*;
import com.diego.list.customers.repository.DeviceRepository;
import com.diego.list.customers.repository.SecurityLogRepository;
import com.diego.list.customers.services.records.RiskAssessment;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class SecurityMonitorService {

    private final SecurityLogRepository securityLogRepository;
    private final DeviceRepository deviceRepository;

    public RiskAssessment assessRisk(User user, HttpServletRequest request, String deviceFingerprint) {
        int riskScore = 0;
        List<String> riskFactors = new ArrayList<>();

        // 1. Verifica dispositivo
        if (!isDeviceRecognized(user.getId(), deviceFingerprint)) {
            riskScore += 30;
            riskFactors.add("Dispositivo não reconhecido");
        }

        // 2. Verifica localização (simulado)
        String ip = getClientIP(request);
        if (isUnusualLocation(user.getId(), ip)) {
            riskScore += 25;
            riskFactors.add("Localização incomum");
        }

        // 3. Verifica horário
        if (isUnusualTime(user)) {
            riskScore += 15;
            riskFactors.add("Horário incomum");
        }

        // 4. Verifica IP de risco (simulado)
        if (isRiskyIP(ip)) {
            riskScore += 20;
            riskFactors.add("IP de risco");
        }

        return new RiskAssessment(riskScore, riskScore >= 30, riskFactors);
    }

    public void logAuthAttempt(UUID userId, boolean success, HttpServletRequest request, String description) {
        SecurityLog logEntry = new SecurityLog();
        logEntry.setUserId(userId);
        logEntry.setEventType(success ? SecurityEventType.LOGIN_SUCCESS : SecurityEventType.LOGIN_FAILURE);
        logEntry.setDescription(description);
        logEntry.setIpAddress(getClientIP(request));
        logEntry.setUserAgent(request.getHeader("User-Agent"));
        logEntry.setSuccess(success);
        logEntry.setRiskScore(0);

        securityLogRepository.save(logEntry);

        if (!success) {
            checkAndBlockAccount(userId, request);
        }
    }

    public void logMFAAttempt(UUID userId, boolean success, HttpServletRequest request) {
        SecurityLog logEntry = new SecurityLog();
        logEntry.setUserId(userId);
        logEntry.setEventType(SecurityEventType.MFA_VERIFICATION);
        logEntry.setDescription(success ? "MFA verificado com sucesso" : "Falha na verificação MFA");
        logEntry.setIpAddress(getClientIP(request));
        logEntry.setSuccess(success);

        securityLogRepository.save(logEntry);
    }

    public void logSessionRevocation(Session session, String revokedBy, String reason) {
        SecurityLog logEntry = new SecurityLog();
        logEntry.setUserId(session.getUserId());
        logEntry.setSessionId(session.getSessionId());
        logEntry.setEventType(SecurityEventType.SESSION_REVOKED);
        logEntry.setDescription("Sessão revogada: " + reason);
        logEntry.setIpAddress(session.getIpAddress());
        logEntry.setRevokedBy(revokedBy);
        logEntry.setSuccess(true);

        securityLogRepository.save(logEntry);
    }

    public void logSuspiciousActivity(UUID userId, String activity, HttpServletRequest request) {
        SecurityLog logEntry = new SecurityLog();
        logEntry.setUserId(userId);
        logEntry.setEventType(SecurityEventType.SUSPICIOUS_ACTIVITY);
        logEntry.setDescription(activity);
        logEntry.setIpAddress(getClientIP(request));
        logEntry.setSuccess(false);
        logEntry.setRiskScore(80);

        securityLogRepository.save(logEntry);
        log.warn("Atividade suspeita detectada para usuário {}: {}", userId, activity);
    }

    private boolean isDeviceRecognized(UUID userId, String deviceFingerprint) {
        return deviceRepository.findByUserIdAndDeviceFingerprint(userId, deviceFingerprint).isPresent();
    }

    private boolean isUnusualLocation(UUID userId, String ip) {
        // Simulado - integrar com serviço de GeoIP
        // Verificar se o IP está em localização incomum para o usuário
        return false;
    }

    private boolean isUnusualTime(User user) {
        // Simulado - verificar se é horário incomum para o usuário
        int currentHour = LocalDateTime.now().getHour();
        int startHour = 8;
        int endHour = 20;
        return currentHour < startHour || currentHour > endHour; // Fora do horário comercial
    }

    private boolean isRiskyIP(String ip) {
        // Simulado - integrar com lista de IPs de risco
        return ip.startsWith("185.") || ip.startsWith("104."); // Exemplo
    }

    private void checkAndBlockAccount(UUID userId, HttpServletRequest request) {
        long failureCount = securityLogRepository.countRecentFailures(userId, LocalDateTime.now().minusMinutes(15));

        if (failureCount >= 5) {
            log.warn("Conta bloqueada devido a múltiplas tentativas falhas: {}", userId);

            SecurityLog blockLog = new SecurityLog();
            blockLog.setUserId(userId);
            blockLog.setEventType(SecurityEventType.ACCOUNT_LOCKED);
            blockLog.setIpAddress(getClientIP(request));
            blockLog.setDescription("Conta bloqueada devido a múltiplas tentativas falhas");
            securityLogRepository.save(blockLog);
        }
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null) {
            return xfHeader.split(",")[0];
        }
        return request.getRemoteAddr();
    }
}
