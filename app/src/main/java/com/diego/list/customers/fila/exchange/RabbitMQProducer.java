package com.diego.list.customers.fila.exchange;

import com.diego.list.customers.fila.exchange.config.PaymentQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RabbitMQProducer {
    private final RabbitTemplate rabbitTemplate;

    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(PaymentQueueConfig.ORDER_EXCHANGE, PaymentQueueConfig.ORDER_ROUTING_KEY, message);

        log.info("Sent message to exchange: {}", PaymentQueueConfig.ORDER_EXCHANGE);
    }

    public void sendEvent(Object event) {
        rabbitTemplate.convertAndSend(PaymentQueueConfig.ORDER_EXCHANGE, PaymentQueueConfig.ORDER_ROUTING_KEY, event);

        log.info("Sent event to exchange: {}", PaymentQueueConfig.ORDER_EXCHANGE);
    }
}