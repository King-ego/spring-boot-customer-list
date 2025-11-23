package com.diego.list.customers.controller;

import com.diego.list.customers.dto.*;
import com.diego.list.customers.model.Session;
import com.diego.list.customers.services.AuthService;
import com.diego.list.customers.services.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final SessionService sessionService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request,
                                              HttpServletRequest httpRequest) {
        AuthResponse response = authService.login(request, httpRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-mfa")
    public ResponseEntity<AuthResponse> verifyMFA(@Valid @RequestBody MFAVerificationRequest request,
                                                  HttpServletRequest httpRequest,
                                                  HttpServletResponse response) {
        AuthResponse authResponse = authService.verifyMFA(request, httpRequest);

        // Configura cookie de sessão
        if (authResponse.isSuccess()) {
            ResponseCookie cookie = createSessionCookie(authResponse.getSessionId());
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        }

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@CookieValue(name = "session_id", required = false) String sessionId,
                                              HttpServletResponse response) {
        if (sessionId != null) {
            sessionService.revokeSession(sessionId, "user", "Logout voluntário");
        }

        ResponseCookie cookie = ResponseCookie.from("session_id", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(new ApiResponse("Logout realizado com sucesso"));
    }

    @PostMapping("/logout-all")
    public ResponseEntity<ApiResponse> logoutAll(@CookieValue(name = "session_id") String sessionId) {
        Session session = sessionService.getSession(sessionId);
        if (session != null) {
            int revoked = sessionService.revokeAllUserSessions(
                    session.getUserId(), sessionId, "user");

            return ResponseEntity.ok(new ApiResponse(
                    revoked + " sessões revogadas", revoked));
        }

        return ResponseEntity.badRequest().body(new ApiResponse("Sessão não encontrada 2"));
    }

    private ResponseCookie createSessionCookie(String sessionId) {
        return ResponseCookie.from("session_id", sessionId)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofMinutes(30))
                .sameSite("Strict")
                .build();
    }
}