package com.diego.list.customers.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "security_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecurityLog {
    @Id
    @GeneratedValue
    private UUID id;

    private String userId;
    private String sessionId;
    private String eventType;
    private String description;

    private String ipAddress;
    private String userAgent;
    private String deviceFingerprint;

    private boolean success;
    private int riskScore;

    @Column(columnDefinition = "JSON")
    private String metadata;

    private LocalDateTime timestamp = LocalDateTime.now();

}

enum SecurityEventType {
    LOGIN_SUCCESS,
    LOGIN_FAILURE,
    LOGOUT,
    SESSION_REVOKED,
    MFA_VERIFICATION,
    PASSWORD_CHANGE,
    SUSPICIOUS_ACTIVITY,
    ACCOUNT_LOCKED
}
