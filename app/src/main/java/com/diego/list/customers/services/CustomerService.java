package com.diego.list.customers.services;

import com.diego.list.customers.command.CreateCustomerCommand;
import com.diego.list.customers.errors.CustomException;
import com.diego.list.customers.model.Customer;
import com.diego.list.customers.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final StripeServices stripeServices;

    public void create(CreateCustomerCommand command){

        Optional<Customer> existCustomer = customerRepository.findByEmail(command.getEmail());

        if (existCustomer.isPresent()) {
            throw new CustomException("Customer exist", HttpStatus.CONFLICT);
        }

        String stripeId = stripeServices.createCustomer(command.getEmail(), command.getName());

        Customer customer = Customer.builder()
                .name(command.getName())
                .email(command.getEmail())
                .phone(command.getPhone())
                .address(command.getAddress())
                .stripeCustomerId(stripeId)
                .build();

        customerRepository.save(customer);
    }

    public Optional<Customer> customerById(UUID customerId) {
        return customerRepository.findById(customerId);
    }
}
