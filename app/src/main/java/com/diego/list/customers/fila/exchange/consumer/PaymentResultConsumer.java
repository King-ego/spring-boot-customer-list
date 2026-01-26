package com.diego.list.customers.fila.exchange.consumer;

import com.diego.list.customers.application.usecase.order.OrderUpdateUseCase;
import com.diego.list.customers.fila.exchange.config.PaymentQueueConfig;
import com.diego.list.customers.fila.exchange.event.PaymentProcessedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentResultConsumer {
    private final OrderUpdateUseCase orderUpdateUseCase;

    @RabbitListener(queues = PaymentQueueConfig.QUEUE_NAME)
    public void receivePaymentResult(PaymentProcessedEvent event) {
        try{
            orderUpdateUseCase.updateOrderStatus(
                    event.getOrderId(),
                    event.getPaymentStatus(),
                    event.getPaymentId(),
                    event.getPaymentDate()
            );

            log.info("Received PaymentResult Event: {}", event);
        } catch (Exception exc){
            log.error("Failed to process payment for orderId: {}. Error: {}",
                    event.getOrderId(), exc.getMessage(), exc);
            throw exc;
        }

    }
}
