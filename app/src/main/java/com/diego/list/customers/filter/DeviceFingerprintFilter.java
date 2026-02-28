package com.diego.list.customers.filter;

import com.diego.list.customers.application.usecase.securityMonitor.GetClientIpUseCase;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@Order(2)
public class DeviceFingerprintFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String userAgent = request.getHeader("User-Agent");
        String ip = GetClientIpUseCase.getClientIP(request);

        log.debug("Request from IP: {}, User-Agent: {}", ip, userAgent);

        filterChain.doFilter(request, response);
    }

}
