package com.diego.list.customers.application.command.address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAddressCommand {
    private String recipient;

    private String zipCode;

    private String streetAddress;

    private String streetNumber;

    private String neighborhood;

    private String city;

    private String state;

    private String complement;

    private String phoneNumber;

    private Boolean isDefault;
}