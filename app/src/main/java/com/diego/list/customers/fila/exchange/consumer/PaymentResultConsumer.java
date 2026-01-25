package com.diego.list.customers.fila.exchange.consumer;

import com.diego.list.customers.application.usecase.order.OrderUpdateUseCase;
import com.diego.list.customers.fila.exchange.config.PaymentQueueConfig;
import com.diego.list.customers.fila.exchange.event.PaymentProcessedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PaymentResultConsumer {
    private OrderUpdateUseCase orderUpdateUseCase;

    @RabbitListener(queues = PaymentQueueConfig.QUEUE_NAME)
    public void receivePaymentResult(PaymentProcessedEvent event) {

        orderUpdateUseCase.updateOrderStatus(
                event.getOrderId(),
                event.getPaymentStatus(),
                event.getPaymentId(),
                event.getPaymentDate()
        );

        log.info("Received PaymentResult Event: {}", event);
    }
}
