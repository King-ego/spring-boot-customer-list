package com.diego.list.customers.controller;

import com.diego.list.customers.dto.CreateCustomersDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {
    @PostMapping
    public void createCustomer(@Valid @RequestBody CreateCustomersDto createCustomersDto){
        log.info("Creating customer...");
    }
}
