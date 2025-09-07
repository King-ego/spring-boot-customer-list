package com.diego.list.customers.services;

import com.diego.list.customers.command.CreateProductCommand;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ProductServices {
    public ProductServices(){
        System.out.println("Service de criação de produto iniciado");
    }

    public String createProduct(CreateProductCommand productCommand){
        return "Produto " + productCommand.getName() + " com preço " + productCommand.getPrice() + " criado com sucesso!";
    }


}
