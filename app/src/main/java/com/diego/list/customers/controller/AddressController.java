package com.diego.list.customers.controller;

import com.diego.list.customers.command.address.*;
import com.diego.list.customers.services.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @PostMapping
    public String createAddress(@RequestBody CreateAddressCommand createAddressCommand) {
        addressService.CreateAddress(createAddressCommand);
        return ResponseEntity.status(HttpStatus.CREATED).toString();
    }

    @PatchMapping("/{addressId}")
    public String updateAddress(@PathVariable UUID addressId, @RequestBody UpdateAddressCommand updateAddressCommand) {
        addressService.UpdateAddress(addressId, updateAddressCommand);
        return ResponseEntity.status(HttpStatus.CREATED).toString();
    }
}
