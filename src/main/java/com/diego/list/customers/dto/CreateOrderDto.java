package com.diego.list.customers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderDto {
    private String customerId;
    private List<OrderItemDto> order;
}
