package com.diego.list.customers.application.usecase.mail;

import com.diego.list.customers.application.command.mail.SnsSendMailCommand;
import com.diego.list.customers.fila.aws.SnsProducer;
import com.diego.list.customers.model.User;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class SendMainConfirmAccountUseCase {
    private final SnsProducer snsProducer;
    private final TemplateEngine templateEngine;

    public SendMainConfirmAccountUseCase(SnsProducer snsProducer, TemplateEngine templateEngine) {
        this.snsProducer = snsProducer;
        this.templateEngine = templateEngine;
    }

    public void snsSendMessage(User user) {
        validateInput(user.getEmail(), user.getName());

        Object payload = createMessage(user.getEmail(), user.getName(), user);

        snsProducer.convertAndSend(payload);
    }

    private Object createMessage(String email, String name, User user) {
        String link = "https://seusite.com/confirm?id=" + user.getId();
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