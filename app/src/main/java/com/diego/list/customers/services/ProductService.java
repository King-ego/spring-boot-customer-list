package com.diego.list.customers.services;

import com.diego.list.customers.command.CreateProductCommand;
import com.diego.list.customers.errors.CustomException;
import com.diego.list.customers.model.Product;
import com.diego.list.customers.repository.ProductRepository;
import com.diego.list.customers.utils.SlugUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(CreateProductCommand command){

        Optional<Product> existProduct = productRepository.findByName(command.getName());

        if (existProduct.isPresent()) {
            throw new CustomException("Product exist", HttpStatus.CONFLICT);
        }

        String slug = SlugUtil.toSlug(command.getName());

        Product product = Product.builder()
                .name(command.getName())
                .price(command.getPrice())
                .quantity_in_stock(command.getQuantity_in_stock())
                .description(command.getDescription())
                .category(command.getCategory())
                .slug(slug)
                .build();

        log.info("Creando produto: {}", product);

        productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> searchByItemIdentity(String identifier) {
        return productRepository.findByIdentity(identifier);
    }

}