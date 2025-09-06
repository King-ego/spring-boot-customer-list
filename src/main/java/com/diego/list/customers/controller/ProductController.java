package com.diego.list.customers.controller;

import com.diego.list.customers.dto.CreateProductDto;
import org.springframework.web.bind.annotation.PostMapping;

public class ProductController {
    @PostMapping("/products")
    public String createProduct(CreateProductDto product) {
        System.out.println(product);
        return "Produto criado com sucesso!";
    }
}
