package com.diego.list.customers.controller;

import com.diego.list.customers.command.CreateCustomerCommand;
import com.diego.list.customers.dto.CreateCustomersDto;
import com.diego.list.customers.services.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public void createCustomer(@Valid @RequestBody CreateCustomersDto createCustomersDto){
        CreateCustomerCommand customer = new CreateCustomerCommand(
                createCustomersDto.getName(),
                createCustomersDto.getEmail(),
                createCustomersDto.getPhone(),
                createCustomersDto.getAddress()
        );

        customerService.create(customer);
    }

    @GetMapping
    public String CustomerById(@PathVariable UUID id){
        return "Hello World";
    }
}
