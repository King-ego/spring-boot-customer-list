package com.diego.list.customers.fila.exchange;

import com.diego.list.customers.fila.exchange.config.PaymentQueueConfig;
import com.diego.list.customers.fila.exchange.event.OderCreateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RabbitMQConsumer {

    @RabbitListener(queues = PaymentQueueConfig.PAYMENT_RESULT_QUEUE)
    private void receivePaymentResult(OderCreateEvent event) {
        log.info("Processing order {} for customer {} with total: {}",
                event.getGroupId(),
                event.getCustomerId(),
                event.getOrderTotal());

        log.info("Order {} processed successfully", event.getGroupId());
    }


}
