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
    private UUID orderId;
    private UUID customerId;
    private String paymentStatus;
    private BigDecimal amountPaid;
    private LocalDateTime paymentDate;
    private String paymentId;
}
