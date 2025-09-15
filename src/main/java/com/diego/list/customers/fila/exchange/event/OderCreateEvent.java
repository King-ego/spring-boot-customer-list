package com.diego.list.customers.fila.exchange.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OderCreateEvent {
    private UUID orderId;
    private UUID customerId;
    private String orderStatus;
    private Float orderTotal;
}
