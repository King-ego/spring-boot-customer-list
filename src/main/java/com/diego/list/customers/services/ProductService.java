package com.diego.list.customers.services;

import com.diego.list.customers.command.CreateProductCommand;
import com.diego.list.customers.model.Product;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
public class ProductService {
    public ProductService(){
        System.out.println("Service de criação de produto iniciado");
    }

    public String createProduct(CreateProductCommand command){
        Product product = Product.builder()
                .name(command.getName())
                .price(command.getPrice())
                .quantity_in_stock(command.getQuantity_in_stock())
                .description(command.getDescription())
                .category(command.getCategory())
                .build();

        log.info("Creando produto: {}", product);

        return "Produto " + command.getName() + " com preço " + command.getPrice() + " criado com sucesso!";
    }


}
