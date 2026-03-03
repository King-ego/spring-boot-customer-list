package com.diego.list.customers.fila.exchange.config;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class PaymentQueueConfig {
    @Value("${rabbitmq.queue.order.name}")
    private String ORDER_QUEUE;

    @Value("${rabbitmq.exchange.order.name}")
    private String ORDER_EXCHANGE;

    @Value("${rabbitmq.routing-key.order.created}")
    private String ORDER_ROUTING_KEY;

    @Value("${rabbitmq.queue.payment-result.name}")
    private String PAYMENT_RESULT_QUEUE;

    @Value("${rabbitmq.exchange.payment-result.name}")
    private String PAYMENT_RESULT_EXCHANGE;

    @Value("${rabbitmq.routing-key.payment.processed}")
    private String PAYMENT_RESULT_ROUTING_KEY;

    @Value("${rabbitmq.queue.dlq.name}")
    private String DLQ_NAME;

    @Value("${rabbitmq.exchange.dlx.name}")
    private String DLX_NAME;

    @Value("${rabbitmq.routing-key.dlq}")
    private String DLQ_ROUTING_KEY;

    @Bean
    public Queue orderQueue() {
        return QueueBuilder.durable(ORDER_QUEUE)
                .withArgument("x-dead-letter-exchange", DLX_NAME)
                .withArgument("x-dead-letter-routing-key", DLQ_ROUTING_KEY)
                .build();
    }

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Binding orderBinding() {
        return BindingBuilder.bind(orderQueue()).to(orderExchange()).with(ORDER_ROUTING_KEY);
    }

    @Bean
    public Queue paymentResultQueue() {
        return new Queue(PAYMENT_RESULT_QUEUE, true);
    }

    @Bean
    public TopicExchange paymentResultExchange() {
        return new TopicExchange(PAYMENT_RESULT_EXCHANGE);
    }

    @Bean
    public Binding paymentResultBinding() {
        return BindingBuilder.bind(paymentResultQueue())
                .to(paymentResultExchange())
                .with(PAYMENT_RESULT_ROUTING_KEY);
    }

    @Bean
    public Queue deadLetterQueue() {
        return new Queue(DLQ_NAME, true);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DLX_NAME);
    }

    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with(DLQ_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        factory.setDefaultRequeueRejected(false);
        return factory;
    }
}