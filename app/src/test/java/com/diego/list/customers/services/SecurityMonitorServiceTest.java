package com.diego.list.customers.services;

import com.diego.list.customers.application.usecase.account.CheckAndBlockAccountUseCase;
import com.diego.list.customers.application.usecase.securityMonitor.GetClientIpUseCase;
import com.diego.list.customers.application.validation.AuthValidator;
import com.diego.list.customers.model.Device;
import com.diego.list.customers.model.User;
import com.diego.list.customers.repository.DeviceRepository;
import com.diego.list.customers.repository.SecurityLogRepository;
import com.diego.list.customers.services.records.RiskAssessment;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class SecurityMonitorServiceTest {
    @Mock
    private SecurityLogRepository securityLogRepository;

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private CheckAndBlockAccountUseCase checkAndBlockAccountUseCase;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private SecurityMonitorService securityMonitorService;

    private UUID userId;
    private User user;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = new User();
        user.setId(userId);
    }

    @Test
    @DisplayName("Deve adicionar fator de risco quando dispositivo não é reconhecido")
    void assessRisk_deviceNotRecognized_shouldAddRiskFactor() {
        when(deviceRepository.findByUserIdAndDeviceFingerprint(userId, "fp-123"))
                .thenReturn(Optional.empty());

        try (MockedStatic<GetClientIpUseCase> mockedIp = mockStatic(GetClientIpUseCase.class);
             MockedStatic<AuthValidator> mockedAuth = mockStatic(AuthValidator.class)) {

            mockedIp.when(() -> GetClientIpUseCase.getClientIP(request)).thenReturn("192.168.0.1");
            mockedAuth.when(() -> AuthValidator.isUnusualLocation(userId, "192.168.0.1")).thenReturn(false);
            mockedAuth.when(() -> AuthValidator.isUnusualTime(user)).thenReturn(false);
            mockedAuth.when(() -> AuthValidator.isRiskyIP("192.168.0.1")).thenReturn(false);

            RiskAssessment result = securityMonitorService.assessRisk(user, request, "fp-123");

            assertEquals(30, result.score());
            assertTrue(result.requiresMFA());
            assertTrue(result.factors().contains("Dispositivo não reconhecido"));
        }
    }

    @Test
    @DisplayName("Deve adicionar fator de risco quando localização é incomum")
    void assessRisk_unusualLocation_shouldAddRiskFactor() {
        when(deviceRepository.findByUserIdAndDeviceFingerprint(userId, "fp-123"))
                .thenReturn(Optional.of(new Device()));

        try (MockedStatic<GetClientIpUseCase> mockedIp = mockStatic(GetClientIpUseCase.class);
             MockedStatic<AuthValidator> mockedAuth = mockStatic(AuthValidator.class)) {

            mockedIp.when(() -> GetClientIpUseCase.getClientIP(request)).thenReturn("10.0.0.1");
            mockedAuth.when(() -> AuthValidator.isUnusualLocation(userId, "10.0.0.1")).thenReturn(true);
            mockedAuth.when(() -> AuthValidator.isUnusualTime(user)).thenReturn(false);
            mockedAuth.when(() -> AuthValidator.isRiskyIP("10.0.0.1")).thenReturn(false);

            RiskAssessment result = securityMonitorService.assessRisk(user, request, "fp-123");

            assertEquals(25, result.score());
            assertTrue(result.factors().contains("Localização incomum"));
        }
    }

    @Test
    @DisplayName("Deve adicionar fator de risco quando horário é incomum")
    void assessRisk_unusualTime_shouldAddRiskFactor() {
        when(deviceRepository.findByUserIdAndDeviceFingerprint(userId, "fp-123"))
                .thenReturn(Optional.of(new Device()));

        try (MockedStatic<GetClientIpUseCase> mockedIp = mockStatic(GetClientIpUseCase.class);
             MockedStatic<AuthValidator> mockedAuth = mockStatic(AuthValidator.class)) {

            mockedIp.when(() -> GetClientIpUseCase.getClientIP(request)).thenReturn("192.168.0.1");
            mockedAuth.when(() -> AuthValidator.isUnusualLocation(userId, "192.168.0.1")).thenReturn(false);
            mockedAuth.when(() -> AuthValidator.isUnusualTime(user)).thenReturn(true);
            mockedAuth.when(() -> AuthValidator.isRiskyIP("192.168.0.1")).thenReturn(false);

            RiskAssessment result = securityMonitorService.assessRisk(user, request, "fp-123");

            assertEquals(15, result.score());
            assertTrue(result.factors().contains("Horário incomum"));
        }
    }

    @Test
    @DisplayName("Deve adicionar fator de risco quando IP é de risco")
    void assessRisk_riskyIp_shouldAddRiskFactor() {
        when(deviceRepository.findByUserIdAndDeviceFingerprint(userId, "fp-123"))
                .thenReturn(Optional.of(new Device()));

        try (MockedStatic<GetClientIpUseCase> mockedIp = mockStatic(GetClientIpUseCase.class);
             MockedStatic<AuthValidator> mockedAuth = mockStatic(AuthValidator.class)) {

            mockedIp.when(() -> GetClientIpUseCase.getClientIP(request)).thenReturn("malicious-ip");
            mockedAuth.when(() -> AuthValidator.isUnusualLocation(userId, "malicious-ip")).thenReturn(false);
            mockedAuth.when(() -> AuthValidator.isUnusualTime(user)).thenReturn(false);
            mockedAuth.when(() -> AuthValidator.isRiskyIP("malicious-ip")).thenReturn(true);

            RiskAssessment result = securityMonitorService.assessRisk(user, request, "fp-123");

            assertEquals(20, result.score());
            assertTrue(result.factors().contains("IP de risco"));
        }
    }

}
