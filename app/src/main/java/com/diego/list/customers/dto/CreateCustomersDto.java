package com.diego.list.customers.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomersDto {
    @NotBlank(message = "O nome do cliente é obrigatório")
    private String name;

    @NotNull(message = "O Email do cliente é obrigatória")
    private String email;

    @NotNull(message = "O Endereço do cliente é obrigatória")
    private String address;

    @NotNull(message = "O Telefone do cliente é obrigatória")
    private String phone;
}