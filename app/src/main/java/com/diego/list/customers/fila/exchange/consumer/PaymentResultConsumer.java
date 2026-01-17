package com.diego.list.customers.fila.exchange.consumer;

import com.diego.list.customers.fila.exchange.config.PaymentQueueConfig;
import com.diego.list.customers.fila.exchange.event.PaymentProcessedEvent;
import com.diego.list.customers.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PaymentResultConsumer {
    @Autowired
    private OrderRepository orderRepository;

    @RabbitListener(queues = PaymentQueueConfig.QUEUE_NAME)
    public void receivePaymentResult(PaymentProcessedEvent event) {
        log.info("Received PaymentResult Event: {}", event);
    }
}
