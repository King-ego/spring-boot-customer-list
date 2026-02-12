package com.diego.list.customers.application.command.users;

import com.diego.list.customers.model.UserRole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserCommand {
    private String name;

    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private String phone;

    private  String country;

    private String birthDate;

    private CreateCustomerCommand customerDetails;

    private CreateSellerCommand sellerDetails;
}
