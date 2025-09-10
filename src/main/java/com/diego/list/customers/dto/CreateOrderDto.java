package com.diego.list.customers.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderDto {
    @NotNull(message = "O Id do cliente é obrigatório")
    private String customerId;

    @NotNull(message = "A lista de itens do pedido é obrigatória")
    private List<OrderItemDto> order;
}
