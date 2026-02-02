package com.diego.list.customers.services;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;

@Service
public class SqsProducerService {

    private final SqsClient sqsClient;

    public SqsProducerService(SqsClient sqsClient) {
        this.sqsClient = sqsClient;
    }

    public void send(String queueUrl, String message) {
        sqsClient.sendMessage(r -> r
                .queueUrl(queueUrl)
                .messageBody(message)
        );
    }
}

