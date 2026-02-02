package com.diego.list.customers.fila.sqs;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;

@Service
@AllArgsConstructor
public class SqsProducerProducer {

    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;


    public void send(String queueUrl, String message) {
        sqsClient.sendMessage(r -> r
                .queueUrl(queueUrl)
                .messageBody(message)
        );
    }

    public void convertAndSend(String queueUrl, Object event) {
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

