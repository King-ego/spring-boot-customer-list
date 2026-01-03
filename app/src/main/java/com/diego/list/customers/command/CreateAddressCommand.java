package com.diego.list.customers.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateAddressCommand {
    private String recipient;

    private Number zip_code;

    private String street_address;

    private Number street_number;

    private String neighborhood;

    private String city;

    private String state;

    private String complement;

    private String phone_number;

    private String user_id;
}
