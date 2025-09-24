package com.diego.list.customers.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public class CreateMethodPaymentDto {
    @NotBlank(message = "customerId is required")
    private UUID customerId;

    @NotBlank(message = "type is required")
    private String type;

    private String method_payment_id;

    private boolean isDefault;

    private String brand;

    private String exp_month;

    private String exp_year;

    private String last4;

    private String funding;
}
