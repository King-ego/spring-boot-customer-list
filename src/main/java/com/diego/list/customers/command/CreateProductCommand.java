package com.diego.list.customers.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductCommand {
    private String name;

    private Integer price;

    private Integer quantity_in_stock;

    private String description;

    private String category;
}
