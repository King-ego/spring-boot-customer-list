package com.diego.list.customers.dto;

import com.diego.list.customers.model.DeviceInfo;

import java.time.LocalDateTime;

public record SessionInfo(
        String sessionId,
        DeviceInfo deviceInfo,
        String ipAddress,
        LocalDateTime createdAt,
        LocalDateTime lastActivity,
        boolean isCurrent
) {}