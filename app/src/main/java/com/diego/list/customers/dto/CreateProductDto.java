package com.diego.list.customers.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductDto {
    @NotBlank(message = "O nome do produto é obrigatório")
    private String name;

    @NotNull(message = "O preço do produto é obrigatório")
    private BigDecimal price;

    @NotNull(message = "A quantidade em estoque é obrigatória")
    private Integer quantityInStock;

    private String description;

    @NotNull(message = "A categoria do produto é obrigatória")
    private String category;
}