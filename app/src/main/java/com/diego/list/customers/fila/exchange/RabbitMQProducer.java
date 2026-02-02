package com.diego.list.customers.fila.exchange;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RabbitMQProducer {
    private final RabbitTemplate rabbitTemplate;
    private final String orderExchange;
    private final String orderRoutingKey;

    public RabbitMQProducer(RabbitTemplate rabbitTemplate,
                            @Value("${rabbitmq.exchange.order.name}") String orderExchange,
                            @Value("${rabbitmq.routing-key.order.created}") String orderRoutingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.orderExchange = orderExchange;
        this.orderRoutingKey = orderRoutingKey;
    }

    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(orderExchange, orderRoutingKey, message);

        log.info("Sent message to exchange: {}", orderExchange);
    }

    public void sendEvent(Object event) {
        rabbitTemplate.convertAndSend(orderExchange, orderRoutingKey, event);

        log.info("Sent event to exchange: {}", orderExchange);
    }
}