package com.diego.list.customers.controller;

import com.diego.list.customers.dto.CreateProductDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {
    @PostMapping
    public String createProduct(@Valid @RequestBody CreateProductDto product) {
        System.out.println(product);
        return "Produto criado com sucesso!";
    }
}
