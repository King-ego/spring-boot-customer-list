package com.diego.list.customers.controller;

import com.diego.list.customers.command.address.*;
import com.diego.list.customers.services.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @PostMapping("")
    public void createAddress(@RequestBody CreateAddressCommand createAddressCommand) {
        addressService.CreateAddress(createAddressCommand);
    }

    @PatchMapping("/{addressId}")
    public void updateAddress(@PathVariable UUID addressId, @RequestBody UpdateAddressCommand updateAddressCommand) {
        addressService.UpdateAddress(addressId, updateAddressCommand);
    }
}
