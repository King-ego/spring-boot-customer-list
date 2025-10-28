package com.diego.list.customers.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderCommand {
    private UUID customerId;

    private List<OrderItemCommand> orders;
}
