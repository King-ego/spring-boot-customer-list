package com.diego.list.customers.services;

import com.diego.list.customers.command.CreateCustomerCommand;
import com.diego.list.customers.model.Customer;
import com.diego.list.customers.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public void create(CreateCustomerCommand command){

        Optional<Customer> existCustomer = customerRepository.findByEmail(command.getEmail());

        if (existCustomer.isPresent()) {
            log.warn("El cliente con email {} ya existe", command.getEmail());
            throw new IllegalArgumentException("Customer exist");
        }

        Customer customer = Customer.builder()
                .name(command.getName())
                .email(command.getEmail())
                .phone(command.getPhone())
                .address(command.getAddress())
                .build();

        customerRepository.save(customer);
    }
}
