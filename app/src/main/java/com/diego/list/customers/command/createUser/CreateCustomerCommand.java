package com.diego.list.customers.command.createUser;

import com.diego.list.customers.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomerCommand {
    private User user;

    private String document; // CPF

    private LocalDateTime birthDate;
}
