package com.diego.list.customers.services;

import com.diego.list.customers.application.usecase.account.CheckAndBlockAccountUseCase;
import com.diego.list.customers.model.User;
import com.diego.list.customers.repository.DeviceRepository;
import com.diego.list.customers.repository.SecurityLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

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

}
