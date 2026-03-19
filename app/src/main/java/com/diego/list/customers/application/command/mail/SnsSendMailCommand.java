package com.diego.list.customers.application.command.mail;

import com.diego.list.customers.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SnsSendMailCommand {
    private String email;
    private String name;
    private String message;
    private User receiver;
}
