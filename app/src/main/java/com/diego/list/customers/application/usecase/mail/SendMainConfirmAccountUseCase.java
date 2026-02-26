package com.diego.list.customers.application.usecase.mail;

import com.diego.list.customers.application.command.mail.SnsSendMailCommand;
import com.diego.list.customers.fila.aws.SnsProducer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SendMainConfirmAccountUseCase {
    private final SnsProducer snsProducer;
    public void snsSendMessage(SnsSendMailCommand inputs) {
        validateInput(inputs.getEmail(), inputs.getName());
        Object payload = createMessage(inputs.getEmail(), inputs.getName());
        snsProducer.convertAndSend(payload);

    }

    private Object createMessage(String email, String name) {
        return SnsSendMailCommand.builder()
                .email(email)
                .name(name)
                .message("Welcome to our service! Your account has been successfully created.")
                .build();
    }

    private void validateInput(String email, String name) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
    }
}
