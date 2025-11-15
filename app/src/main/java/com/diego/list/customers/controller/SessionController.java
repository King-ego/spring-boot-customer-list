package com.diego.list.customers.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @GetMapping("/active")
    public ResponseEntity<List<SessionInfo>> getActiveSessions(@CookieValue("session_id") String sessionId) {
        Session currentSession = sessionService.getSession(sessionId);
        if (currentSession == null) {
            return ResponseEntity.badRequest().build();
        }

        List<SessionInfo> sessions = sessionService.getActiveUserSessions(currentSession.getUserId())
                .stream()
                .map(session -> new SessionInfo(
                        session.getSessionId(),
                        session.getDeviceInfo(),
                        session.getIpAddress(),
                        session.getCreatedAt(),
                        session.getLastActivity(),
                        session.getSessionId().equals(sessionId)
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(sessions);
    }

    @PostMapping("/revoke/{targetSessionId}")
    public ResponseEntity<ApiResponse> revokeSession(@PathVariable String targetSessionId,
                                                     @CookieValue("session_id") String currentSessionId) {
        Session currentSession = sessionService.getSession(currentSessionId);
        Session targetSession = sessionService.getSession(targetSessionId);

        if (currentSession == null || targetSession == null) {
            return ResponseEntity.badRequest().body(new ApiResponse("Sessão não encontrada"));
        }

        // Verifica se a sessão pertence ao usuário (exceto para admins)
        if (!currentSession.getUserId().equals(targetSession.getUserId()) &&
                !currentSession.getPermissions().contains("ADMIN")) {
            return ResponseEntity.status(403).body(new ApiResponse("Sem permissão para revogar esta sessão"));
        }

        sessionService.revokeSession(targetSessionId, currentSession.getUserId(), "Revogação manual");
        return ResponseEntity.ok(new ApiResponse("Sessão revogada com sucesso"));
    }

    @GetMapping("/stats")
    public ResponseEntity<SessionStats> getSessionStats(@CookieValue("session_id") String sessionId) {
        Session session = sessionService.getSession(sessionId);
        if (session == null) {
            return ResponseEntity.badRequest().build();
        }

        SessionStats stats = sessionService.getUserSessionStats(session.getUserId());
        return ResponseEntity.ok(stats);
    }
}