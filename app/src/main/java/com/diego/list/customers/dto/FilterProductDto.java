package com.diego.list.customers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterProductDto {
    private String name;

    private Integer minPrice;

    private  Integer maxPrice;

    private String category;
}