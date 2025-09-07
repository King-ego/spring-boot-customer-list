package com.diego.list.customers.controller;

import com.diego.list.customers.command.CreateProductCommand;
import com.diego.list.customers.dto.CreateProductDto;
import com.diego.list.customers.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;


    @PostMapping
    public String createProduct(@Valid @RequestBody CreateProductDto product) {
        System.out.println(product);

        CreateProductCommand create = new CreateProductCommand(
                product.getName(),
                product.getPrice(),
                product.getQuantity_in_stock(),
                product.getDescription(),
                product.getCategory()
        );

        productService.createProduct(create);

        return ResponseEntity.status(HttpStatus.CREATED).toString();
    }
}
