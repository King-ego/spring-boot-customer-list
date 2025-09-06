package com.diego.list.customers.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductDto {
    @NotBlank(message = "O nome do produto é obrigatório")
    private String name;

    @NotNull(message = "O preço do produto é obrigatório")
    private Integer price;

    @NotNull(message = "A quantidade em estoque é obrigatória")
    private Integer quantity_in_stock;


    private String description;

    @NotNull(message = "A categoria do produto é obrigatória")
    private String category;
}
