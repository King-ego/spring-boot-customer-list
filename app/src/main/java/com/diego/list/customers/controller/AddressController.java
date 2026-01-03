package com.diego.list.customers.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressController {
    @PostMapping("")
    public void createAddress() {

    }
}
