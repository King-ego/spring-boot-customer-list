package com.diego.list.customers.application.command.users;

import com.diego.list.customers.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomerCommand {
    private User user;

    private String document;

    private LocalDateTime birthDate;
}
