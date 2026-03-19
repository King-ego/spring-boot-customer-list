package com.diego.list.customers.application.usecase.mail;

import com.diego.list.customers.application.command.mail.SnsSendMailCommand;
import com.diego.list.customers.fila.aws.SnsProducer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@AllArgsConstructor
public class SendMainConfirmAccountUseCase {
    private final SnsProducer snsProducer;
    private final TemplateEngine templateEngine;

    public void snsSendMessage(SnsSendMailCommand inputs) {
        validateInput(inputs.getEmail(), inputs.getName());

        Object payload = createMessage(inputs.getEmail(), inputs.getName());
        
        snsProducer.convertAndSend(payload);

    }

    private Object createMessage(String email, String name) {
        String link = "https://seusite.com/confirm?token=abc123";
        String htmlBody = this.buildConfirmAccountEmail(name, link);

        return SnsSendMailCommand.builder()
                .email(email)
                .name(name)
                .message(htmlBody)
                .build();
        /*
                .message("Welcome to our service! Your account has been successfully created.")
                */
    }

    private void validateInput(String email, String name) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
    }

    private String buildConfirmAccountEmail(String name, String confirmationLink) {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("confirmationLink", confirmationLink);
        return templateEngine.process("confirm-account", context);
    }
}
