package com.diego.list.customers.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductCommand {
    private String name;

    private BigDecimal price;

    private Integer quantity_in_stock;

    private String description;

    private String category;
}
