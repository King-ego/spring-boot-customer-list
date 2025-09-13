package com.diego.list.customers.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
    @NotNull(message = "O Id do produto é obrigatório")
    private UUID productId;

    @NotNull(message = "A quantidade do produto é obrigatória")
    private Integer amount;
}