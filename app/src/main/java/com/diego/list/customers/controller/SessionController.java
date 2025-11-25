package com.diego.list.customers.controller;

import com.diego.list.customers.dto.*;
import com.diego.list.customers.model.*;
import com.diego.list.customers.services.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
@Slf4j
public class SessionController {

    private final SessionService sessionService;

    @PostMapping("/revoke/{targetSessionId}")
    public ResponseEntity<ApiResponse> revokeSession(@PathVariable String targetSessionId,
                                                     @CookieValue("session_id") String currentSessionId) {
        Session currentSession = sessionService.getSession(currentSessionId);
        Session targetSession = sessionService.getSession(targetSessionId);

        if (currentSession == null || targetSession == null) {
            return ResponseEntity.badRequest().body(new ApiResponse("Sessão não encontrada 3"));
        }

        if (!currentSession.getUserId().equals(targetSession.getUserId()) &&
                !currentSession.getPermissions().contains("ADMIN")) {
            return ResponseEntity.status(403).body(new ApiResponse("Sem permissão para revogar esta sessão"));
        }

        sessionService.revokeSession(targetSessionId, currentSession.getRevokedBy(), "Revogação manual");
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