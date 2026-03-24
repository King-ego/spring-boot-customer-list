package com.diego.list.customers.application.validation;

import com.diego.list.customers.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class AuthValidator {
    public static Boolean isRiskyIP(String ip) {
        return ip.startsWith("185.") || ip.startsWith("104.");
    }

    public static Boolean isUnusualTime(User user) {
        int currentHour = LocalDateTime.now().getHour();
        int startHour = 8;
        int endHour = 20;
        return currentHour < startHour || currentHour > endHour;
    }

    public static Boolean isUnusualLocation(UUID userId, String ip) {
        return false;
    }
}