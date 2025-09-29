package com.diego.list.customers.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "products")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @Column(precision =  10, scale = 2)
    private BigDecimal price;

    private Integer quantity_in_stock;

    private String description;

    private String category;

    @Column(unique = true, nullable = false)
    private String slug;
}
