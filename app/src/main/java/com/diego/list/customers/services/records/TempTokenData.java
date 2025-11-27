package com.diego.list.customers.services.records;

import java.time.LocalDateTime;
import java.util.UUID;

public record TempTokenData(UUID userId, LocalDateTime expiresAt) {
    // Adicione os getters
    public UUID getUserId() {
        return userId;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
}
