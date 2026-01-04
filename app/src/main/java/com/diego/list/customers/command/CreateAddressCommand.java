package com.diego.list.customers.command;

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
    private Number zip_code;

    @NotNull
    private String street_address;

    @NotNull
    private Number street_number;

    @NotNull
    private String neighborhood;

    @NotNull
    private String city;

    @NotNull
    private String state;

    private String complement;

    @NotNull
    private String phone_number;

    @NotNull
    private UUID user_id;

    @NotNull
    private Boolean is_default;
}
