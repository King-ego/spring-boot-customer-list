package com.diego.list.customers.dto;

public record SessionStats(
        int activeSessions,
        int uniqueDevices,
        int totalSessions
) {}