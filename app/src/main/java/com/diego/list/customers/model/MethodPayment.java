package com.diego.list.customers.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Table(name = "method_payments")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MethodPayment {
    @Id
    @GeneratedValue
    private UUID id;

    private UUID customerId;

    private String type;

    private String method_payment_id;

    private boolean isDefault;

    private String brand;

    private String exp_month;

    private String exp_year;

    private String last4;

    private String funding;
}
