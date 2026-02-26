package com.diego.list.customers.application.usecase.mail;

import com.diego.list.customers.application.command.mail.SnsSendMailCommand;
import com.diego.list.customers.fila.aws.SnsProducer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SendMainConfirmAccountUseCase {
    private final SnsProducer snsProducer;
    public void snsSendMessage(SnsSendMailCommand message) {
        if (message == null) return;
        if (message.getEmail().isEmpty()) return;
        snsProducer.convertAndSend(message);

    }
}
