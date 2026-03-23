package com.diego.list.customers.application.usecase.account;

import com.diego.list.customers.repository.SecurityLogRepository;
import com.diego.list.customers.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class CheckAndBlockAccountUseCase {
    private final SecurityLogRepository securityLogRepository;
    private final UserRepository userRepository;

    public CheckAndBlockAccountUseCase (SecurityLogRepository securityLogRepository, UserRepository userRepository) {
        this.securityLogRepository = securityLogRepository;
        this.userRepository = userRepository;
    }

    public void execute(UUID userId, HttpServletRequest request) {
        long failureCount = securityLogRepository.countRecentFailures(userId, LocalDateTime.now().minusMinutes(15));

        if (failureCount >= 5) {
            blockAccount(userId);
        }
    }

    private void blockAccount(UUID userId) {
        log.warn("Conta bloqueada devido a múltiplas tentativas falhas: {}", userId);
        userRepository.accountNonLockedParse(userId, false);
    }
}
