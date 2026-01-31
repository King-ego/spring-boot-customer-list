package com.diego.list.customers.controller;

import com.diego.list.customers.application.command.CreateProductCommand;
import com.diego.list.customers.dto.CreateProductDto;
import com.diego.list.customers.dto.FilterProductDto;
import com.diego.list.customers.model.Product;
import com.diego.list.customers.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public String createProduct(@Valid @RequestBody CreateProductDto product) {

        CreateProductCommand create = new CreateProductCommand(
                product.getName(),
                product.getPrice(),
                product.getQuantityInStock(),
                product.getDescription(),
                product.getCategory()
        );

        productService.createProduct(create);

        return ResponseEntity.status(HttpStatus.CREATED).toString();
    }

    @GetMapping
    public List<Product> index(@ModelAttribute FilterProductDto query){
        return productService.getAllProducts();
    }

    @GetMapping("/search-identity/{identifier}")
    public List<Product> searchByItemIdentity(@Valid @RequestParam String identifier) {
        return productService.searchByItemIdentity(identifier);
    }

    @GetMapping("/{slug}")
    public String getBySlug(@PathVariable String slug) {
        return "Test" + slug;
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@Valid @PathVariable UUID productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok("Deleted Product Successfully");
    }
}