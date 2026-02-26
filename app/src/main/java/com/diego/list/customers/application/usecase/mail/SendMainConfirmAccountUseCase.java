package com.diego.list.customers.application.usecase.mail;

import com.diego.list.customers.fila.aws.SnsProducer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SendMainConfirmAccountUseCase {
    private final SnsProducer snsProducer;
    public void snsSendMessage(Object message) {

    }
}
