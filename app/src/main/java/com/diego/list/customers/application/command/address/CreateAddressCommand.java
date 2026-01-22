package com.diego.list.customers.application.command.address;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateAddressCommand {
    @NotNull
    private String recipient;

    @NotNull
    private String zipCode;

    @NotNull
    private String streetAddress;

    @NotNull
    private String streetNumber;

    @NotNull
    private String neighborhood;

    @NotNull
    private String city;

    @NotNull
    private String state;

    private String complement;

    @NotNull
    private String phoneNumber;

    @NotNull
    private UUID user_id;

    @NotNull
    private Boolean isDefault;
}
