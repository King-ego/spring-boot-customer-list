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

    private UUID userId;
    private String sessionId;
    private SecurityEventType eventType;
    private String description;

    private String ipAddress;
    private String userAgent;
    private String deviceFingerprint;

    private boolean success;
    private int riskScore;

    private String revokedBy;

    @Column(columnDefinition = "TEXT")
    private String metadata;

    private LocalDateTime timestamp = LocalDateTime.now();

}

