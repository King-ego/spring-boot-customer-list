package com.diego.list.customers.services.execute;

import com.diego.list.customers.command.createUser.CreateCustomerCommand;
import com.diego.list.customers.command.createUser.CreateSellerCommand;
import com.diego.list.customers.model.Customer;
import com.diego.list.customers.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class CreateTypeAccount {
    private final CustomerRepository customerRepository;

    public void createRoleCustomer(CreateCustomerCommand command){
        Customer customer = Customer.builder()
                        .user(command.getUser())
                        .document(command.getDocument())
                        .totalOrders(0)
                        .totalSpent(0.0)
                        .build();

        customerRepository.save(customer);
        log.info("Creating role customer {}", command);
    }

    public void createRoleSeller(CreateSellerCommand command){
        log.info("Updating role seller {}", command);
    }
}
