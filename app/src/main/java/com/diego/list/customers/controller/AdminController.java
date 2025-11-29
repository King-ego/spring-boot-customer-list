package com.diego.list.customers.controller;

import com.diego.list.customers.controller.records.UserSessionsResponse;
import com.diego.list.customers.dto.ApiResponse;
import com.diego.list.customers.model.SecurityLog;
import com.diego.list.customers.model.Session;
import com.diego.list.customers.repository.SecurityLogRepository;
import com.diego.list.customers.services.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final SessionService sessionService;
    private final SecurityLogRepository securityLogRepository;

    @GetMapping("/user-sessions/{userId}")
    public ResponseEntity<UserSessionsResponse> getUserSessions(@PathVariable UUID userId,
                                                                @CookieValue("session_id") String adminSessionId) {
        Session adminSession = sessionService.getSession(adminSessionId);
        if (adminSession == null || !adminSession.getPermissions().contains("ADMIN")) {
            return ResponseEntity.status(403).build();
        }

        List<Session> sessions = sessionService.getAllUserSessions(userId);
        UserSessionsResponse response = new UserSessionsResponse(userId, sessions);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/revoke-user-sessions/{userId}")
    public ResponseEntity<ApiResponse> revokeUserSessions(@PathVariable UUID userId,
                                                          @CookieValue("session_id") String adminSessionId) {
        Session adminSession = sessionService.getSession(adminSessionId);
        if (adminSession == null || !adminSession.getPermissions().contains("ADMIN")) {
            return ResponseEntity.status(403).body(new ApiResponse("Acesso negado"));
        }

        int revoked = sessionService.revokeAllUserSessions(userId, null, "admin");
        return ResponseEntity.ok(new ApiResponse(revoked + " sessões revogadas", revoked));
    }

    @GetMapping("/security-logs")
    public ResponseEntity<List<SecurityLog>> getSecurityLogs(
            @RequestParam(defaultValue = "24") int hours,
            @CookieValue("session_id") String adminSessionId) {

        Session adminSession = sessionService.getSession(adminSessionId);
        if (adminSession == null || !adminSession.getPermissions().contains("ADMIN")) {
            return ResponseEntity.status(403).build();
        }

        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        List<SecurityLog> logs = securityLogRepository.findRecentLogs(since);

        return ResponseEntity.ok(logs);
    }
}