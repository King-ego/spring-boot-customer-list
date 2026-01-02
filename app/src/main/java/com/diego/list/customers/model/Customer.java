package com.diego.list.customers.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "customers")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private UUID userId;

    private String document;

    private Boolean newsletterSubscribed;

    private Integer totalOrders;

    private Double totalSpent;

    private LocalDateTime lastPurchaseAt;


    /*@Column()
    private String stripeCustomerId;*/

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        totalOrders = 0;
        totalSpent = 0.0;
        newsletterSubscribed = false;
    }
}
