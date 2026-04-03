package com.diego.list.customers.application.usecase.securityMonitor;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class GetClientIpUseCase {
    private static final Set<String> TRUSTED_PROXIES = Set.of(
            "127.0.0.1",       // localhost (dev)
            "10.0.0.1",        // seu load balancer interno
            "172.16.0.1"       // outro proxy interno, se houver
    );

    public static String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null && isTrustedProxy(request.getRemoteAddr())) {
            return xfHeader.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private static boolean isTrustedProxy(String remoteAddr) {
        return TRUSTED_PROXIES.contains(remoteAddr);
    }
}


