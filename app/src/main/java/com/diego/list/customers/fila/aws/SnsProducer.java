package com.diego.list.customers.fila.aws;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;

@Service
public class SnsProducer {
    private final SnsClient snsClient;
    private final ObjectMapper objectMapper;

    @Value("${aws.sns.topic-arn}")
    private String topicArn;
    public SnsProducer(SnsClient snsClient, ObjectMapper objectMapper) {
        this.snsClient = snsClient;
        this.objectMapper = objectMapper;
    }

    public void sendMessage(String message){
        snsClient.publish(r -> r
                .topicArn(topicArn)
                .message(message)
        );
    }

    public void convertAndSend(Object event) {
        try {
            String messageBody = objectMapper.writeValueAsString(event);
            snsClient.publish(r -> r
                    .topicArn(topicArn)
                    .message(messageBody)
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert event to JSON", e);
        }
    }
}
