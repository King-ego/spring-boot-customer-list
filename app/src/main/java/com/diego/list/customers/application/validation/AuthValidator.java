package com.diego.list.customers.application.validation;

import org.springframework.stereotype.Component;

@Component
public class AuthValidator {
    public static Boolean isRiskyIP(String ip) {
        return ip.startsWith("185.") || ip.startsWith("104.");
    }
}
