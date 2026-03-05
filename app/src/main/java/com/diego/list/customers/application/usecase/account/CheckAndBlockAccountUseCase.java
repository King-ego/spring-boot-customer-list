package com.diego.list.customers.application.usecase.account;

import com.diego.list.customers.repository.SecurityLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CheckAndBlockAccountUseCase {
    private final SecurityLogRepository securityLogRepository;

    public void execute(UUID userId, HttpServletRequest request) {
        long failureCount = securityLogRepository.countRecentFailures(userId, LocalDateTime.now().minusMinutes(15));

        if (failureCount >= 5) {

        }
    }
}
