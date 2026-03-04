package com.diego.list.customers.application.validation;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AuthValidator {
    public static Boolean isRiskyIP(String ip) {
        return ip.startsWith("185.") || ip.startsWith("104.");
    }

    public static Boolean isUnusualTime(){
        int currentHour = LocalDateTime.now().getHour();
        int startHour = 8;
        int endHour = 20;
        return currentHour < startHour || currentHour > endHour;
    }
}
