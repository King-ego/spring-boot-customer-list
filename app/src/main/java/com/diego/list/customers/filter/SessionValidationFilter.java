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

@Component
@Order(1)
public class SessionValidationFilter extends OncePerRequestFilter {

    private final SessionService sessionService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (isPublicEndpoint(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String sessionId = getSessionIdFromRequest(request);
        if (sessionId == null) {
            sendError(response, "Sessão não encontrada", HttpStatus.UNAUTHORIZED);
            return;
        }

        try {
            Session session = sessionService.getSession(sessionId);
            if (session == null || !sessionService.validateSession(session, request)) {
                sendError(response, "Sessão inválida ou expirada", HttpStatus.UNAUTHORIZED);
                return;
            }

            // Adiciona sessão ao contexto da requisição
            SessionRequestWrapper wrappedRequest = new SessionRequestWrapper(request, session);
            filterChain.doFilter(wrappedRequest, response);

        } catch (Exception e) {
            log.error("Erro na validação da sessão", e);
            sendError(response, "Erro de autenticação", HttpStatus.INTERNAL_SERVER_ERROR);
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
        return path.startsWith("/api/auth/login") ||
                path.startsWith("/api/auth/register") ||
                path.startsWith("/public/");
    }
}

// Wrapper para adicionar sessão ao request
class SessionRequestWrapper extends HttpServletRequestWrapper {
    private final Session session;

    public SessionRequestWrapper(HttpServletRequest request, Session session) {
        super(request);
        this.session = session;
    }

    public Session getSession() {
        return session;
    }
}