package com.diego.list.customers.controller;

import com.diego.list.customers.dto.CreateProductDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {
    @PostMapping
    public String createProduct(CreateProductDto product) {
        System.out.println(product);
        return "Produto criado com sucesso!";
    }
}
