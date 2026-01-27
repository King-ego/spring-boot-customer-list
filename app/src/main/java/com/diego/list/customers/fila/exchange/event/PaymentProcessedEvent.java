package com.diego.list.customers.fila.exchange.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentProcessedEvent {
    private UUID groupId;
    private UUID customerId;
    private String status;
    private LocalDateTime paymentDate;
    private String paymentId;
}
