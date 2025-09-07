package com.diego.list.customers.services;

import com.diego.list.customers.command.CreateProductCommand;
import com.diego.list.customers.model.Product;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ProductServices {
    public ProductServices(){
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

        System.out.println("Produto Criado com sucesso" + product);

        return "Produto " + command.getName() + " com preço " + command.getPrice() + " criado com sucesso!";
    }


}
