package com.diego.list.customers.controller;

import org.springframework.web.bind.annotation.PostMapping;

public class ProductController {
    @PostMapping("/products")
    public String createProduct() {
        return "Produto criado com sucesso!";
    }
}
