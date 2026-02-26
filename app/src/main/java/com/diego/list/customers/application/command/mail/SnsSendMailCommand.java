package com.diego.list.customers.application.command.mail;

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
}
