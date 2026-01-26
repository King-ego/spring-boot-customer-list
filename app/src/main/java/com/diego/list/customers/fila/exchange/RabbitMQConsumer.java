package com.diego.list.customers.fila.exchange;

import com.diego.list.customers.application.usecase.order.OrderUpdateUseCase;
import com.diego.list.customers.fila.exchange.config.PaymentQueueConfig;
import com.diego.list.customers.fila.exchange.event.PaymentProcessedEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class RabbitMQConsumer {
    private final OrderUpdateUseCase orderUpdateUseCase;
    @RabbitListener(queues = PaymentQueueConfig.PAYMENT_RESULT_QUEUE)
    private void receivePaymentResult(PaymentProcessedEvent event) {
        log.info("Processing order {} for customer {} with total: {} and status: {}",
                event.getGroupId(),
                event.getCustomerId(),
                event.getPaymentId(),
                event.getStatus());

        log.info("Order {} processed successfully", event);

    }


}
