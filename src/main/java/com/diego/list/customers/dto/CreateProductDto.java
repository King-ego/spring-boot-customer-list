package com.diego.list.customers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductDto {
    private String name;
    private Integer price;
    private Integer quantity_in_stock;
    private String description;
    private String category;
}
