package com.diego.list.customers.services;

import com.diego.list.customers.application.command.CreateProductCommand;
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
import java.util.UUID;

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
                .quantityInStock(command.getQuantityInStock())
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

    public void deleteProduct(UUID productId) {
        productRepository.findById(productId)
                .orElseThrow(() -> new CustomException("Product not found", HttpStatus.NOT_FOUND));

        productRepository.deleteById(productId);
    }
}

