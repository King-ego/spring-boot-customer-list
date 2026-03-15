package com.diego.list.customers.services;

import com.diego.list.customers.application.usecase.account.CheckAndBlockAccountUseCase;
import com.diego.list.customers.application.usecase.securityMonitor.GetClientIpUseCase;
import com.diego.list.customers.application.validation.AuthValidator;
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
    private final CheckAndBlockAccountUseCase checkAndBlockAccountUseCase;

    public RiskAssessment assessRisk(User user, HttpServletRequest request, String deviceFingerprint) {
        int riskScore = 0;
        List<String> riskFactors = new ArrayList<>();

        if (!isDeviceRecognized(user.getId(), deviceFingerprint)) {
            riskScore += 30;
            riskFactors.add("Dispositivo não reconhecido");
        }

        String ip = GetClientIpUseCase.getClientIP(request);
        if (AuthValidator.isUnusualLocation(user.getId(), ip)) {
            riskScore += 25;
            riskFactors.add("Localização incomum");
        }

        if (AuthValidator.isUnusualTime(user)) {
            riskScore += 15;
            riskFactors.add("Horário incomum");
        }

        boolean isRiskyIP = AuthValidator.isRiskyIP(ip);

        if (isRiskyIP) {
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
        logEntry.setIpAddress(GetClientIpUseCase.getClientIP(request));
        logEntry.setUserAgent(request.getHeader("User-Agent"));
        logEntry.setSuccess(success);
        logEntry.setRiskScore(0);

        securityLogRepository.save(logEntry);

        if (!success) {
            checkAndBlockAccountUseCase.execute(userId, request);
        }
    }

    public void logMFAAttempt(UUID userId, boolean success, HttpServletRequest request) {
        SecurityLog logEntry = new SecurityLog();
        logEntry.setUserId(userId);
        logEntry.setEventType(SecurityEventType.MFA_VERIFICATION);
        logEntry.setDescription(success ? "MFA verificado com sucesso" : "Falha na verificação MFA");
        logEntry.setIpAddress(GetClientIpUseCase.getClientIP(request));
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
        logEntry.setIpAddress(GetClientIpUseCase.getClientIP(request));
        logEntry.setSuccess(false);
        logEntry.setRiskScore(80);

        securityLogRepository.save(logEntry);
        log.warn("Atividade suspeita detectada para usuário {}: {}", userId, activity);
    }

    private boolean isDeviceRecognized(UUID userId, String deviceFingerprint) {
        return deviceRepository.findByUserIdAndDeviceFingerprint(userId, deviceFingerprint).isPresent();
    }
}
