package com.diego.list.customers.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RedisHash(value = "Session", timeToLive = 1800)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Session {
    @Id
    private String sessionId;

    private UUID userId;

    private String deviceFingerprint;

    private String ipAddress;

    private String userAgent;

    @Embedded
    private DeviceInfo deviceInfo;

    private LocalDateTime createdAt;
    private LocalDateTime lastActivity;
    private LocalDateTime expiresAt;

    private boolean isActive = true;
    private boolean mfaVerified = false;

    private Set<String> permissions = new HashSet<>();

    private LocalDateTime revokedAt;
    private UUID revokedBy;
    private String revocationReason;

}

