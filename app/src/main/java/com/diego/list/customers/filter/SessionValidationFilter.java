package com.diego.list.customers.filter;

/*import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.service.checkout.SessionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;*/

import com.diego.list.customers.model.Session;
import com.diego.list.customers.security.SessionAuthentication;
import com.diego.list.customers.services.SessionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Order(1)
@Slf4j
@RequiredArgsConstructor
public class SessionValidationFilter extends OncePerRequestFilter {

    private final SessionService sessionService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        log.info("Request 1: {}", request);
        log.info("Response 2: {}", request);
        log.info("FilterChain 3: {}", filterChain);
        if (isPublicEndpoint(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String sessionId = getSessionIdFromRequest(request);

        log.info("Session ID: {}", sessionId);
        if (sessionId == null) {
            sendError(response, "Sessão não encontrada", HttpStatus.BAD_REQUEST);
            return;
        }

        try {
            Session session = sessionService.getSession(sessionId);
            log.info("Session: {}", session);
            if (session == null || !sessionService.validateSession(session, request)) {
                sendError(response, "Sessão inválida ou expirada", HttpStatus.NOT_FOUND);
                return;
            }

            List<SimpleGrantedAuthority> authorities = session.getPermissions().stream()
                    .map(perm -> new SimpleGrantedAuthority("ROLE_" + perm))
                    .toList();

            SessionAuthentication authentication = new SessionAuthentication(session, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.info("✅ Sessão validada e autenticada no Spring Security");
            SessionRequestWrapper wrappedRequest = new SessionRequestWrapper(request, session);
            filterChain.doFilter(wrappedRequest, response);
            log.info("✅ Retornou do controller");

        } catch (Exception e) {
            log.error("Erro na validação da sessão", e);
            sendError(response, "Erro de autenticação", HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            SecurityContextHolder.clearContext(); // Limpa após a requisição
        }
    }

    private String getSessionIdFromRequest(HttpServletRequest request) {
        // Tenta do cookie primeiro
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("session_id".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        // Tenta do header Authorization
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Session ")) {
            return authHeader.substring(8);
        }

        return null;
    }

    private void sendError(HttpServletResponse response, String message, HttpStatus status) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");

        Map<String, Object> error = new HashMap<>();
        error.put("error", status.getReasonPhrase());
        error.put("message", message);
        error.put("timestamp", Instant.now().toString());

        objectMapper.writeValue(response.getWriter(), error);
    }

    private boolean isPublicEndpoint(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/login") ||
                path.startsWith("/health") ||
                path.startsWith("/public/") ||
                path.startsWith("/verify-mfa") ||
                path.equals("/users/register");
    }
}