package com.diego.list.customers.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Table(name = "method_payments")
@NoArgsConstructor
@AllArgsConstructor
public class MethodPayment {
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    private UUID customerId;

    private String type;

    private String token_method_payment;

    private boolean isDefault;
}
