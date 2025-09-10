package com.diego.list.customers.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderDto {
    @NotNull(message = "O Id do cliente é obrigatório")
    private UUID customerId;

    @NotNull(message = "A lista de itens do pedido é obrigatória")
    private List<OrderItemDto> order;
}
