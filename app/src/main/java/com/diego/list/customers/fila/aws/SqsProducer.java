package com.diego.list.customers.fila.aws;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;

@Service
public class SqsProducer {

    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;

    @Value("${aws.sqs.queue-url}")
    private String queueUrl;

    public SqsProducer(SqsClient sqsClient, ObjectMapper objectMapper) {
        this.sqsClient = sqsClient;
        this.objectMapper = objectMapper;
    }

    public void send(String message) {
        sqsClient.sendMessage(r -> r
                .queueUrl(queueUrl)
                .messageBody(message)
        );
    }
    public void convertAndSend(Object event) {
        try {
            String messageBody = objectMapper.writeValueAsString(event);
            sqsClient.sendMessage(r -> r
                    .queueUrl(queueUrl)
                    .messageBody(messageBody)
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert event to JSON", e);
        }

    }
}