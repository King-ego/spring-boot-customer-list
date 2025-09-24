package com.diego.list.customers.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public class CreateMethodPaymentDto {
    @NotBlank(message = "customerId is required")
    private UUID customerId;

    @NotBlank(message = "type is required")
    private String type;

    @NotBlank(message = "method_payment_id is required")
    private String method_payment_id;

    @NotBlank(message = "isDefault is required")
    private boolean isDefault;

    @NotBlank(message = "brand is required")
    private String brand;

    @NotBlank(message = "exp_month is required")
    private String exp_month;

    private String exp_year;

    private String last4;

    private String funding;
}
