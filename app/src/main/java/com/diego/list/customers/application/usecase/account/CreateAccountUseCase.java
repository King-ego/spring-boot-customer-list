package com.diego.list.customers.application.usecase.account;

import com.diego.list.customers.command.createUser.CreateCustomerCommand;
import com.diego.list.customers.command.createUser.CreateSellerCommand;
import com.diego.list.customers.model.Customer;
import com.diego.list.customers.model.Seller;
import com.diego.list.customers.repository.CustomerRepository;
import com.diego.list.customers.repository.SellerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class CreateAccountUseCase {
    private final CustomerRepository customerRepository;
    private final SellerRepository sellerRepository;

    public void createRoleCustomer(CreateCustomerCommand command){
        Customer customer = Customer.builder()
                        .user(command.getUser())
                        .document(command.getDocument())
                        .build();

        customerRepository.save(customer);
        log.info("Creating role customer {}", command);
    }

    public void createRoleSeller(CreateSellerCommand command){
        Seller seller = Seller.builder()
                        .user(command.getUser())
                        .storeName(command.getStoreName())
                        .documentNumber(command.getDocumentNumber())
                        .storeDescription(command.getStoreDescription())
                        .build();

        sellerRepository.save(seller);
        log.info("Updating role seller {}", command);
    }
}
