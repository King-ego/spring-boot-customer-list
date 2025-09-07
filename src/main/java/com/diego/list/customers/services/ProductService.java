package com.diego.list.customers.services;

import com.diego.list.customers.command.CreateProductCommand;
import com.diego.list.customers.model.Product;
import com.diego.list.customers.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(CreateProductCommand command){
        Product product = Product.builder()
                .name(command.getName())
                .price(command.getPrice())
                .quantity_in_stock(command.getQuantity_in_stock())
                .description(command.getDescription())
                .category(command.getCategory())
                .build();

        log.info("Creando produto: {}", product);

        productRepository.save(product);
    }


}
