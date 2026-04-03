package com.diego.list.customers.application.usecase.securityMonitor;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class GetClientIpUseCase {
    public static String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null && isTrustedProxy(request.getRemoteAddr())) {
            return xfHeader.split(",")[0].trim();
        }
        return request.getRemoteAddr();
}
