package com.diego.list.customers.controller;

import com.diego.list.customers.command.address.CreateAddressCommand;
import com.diego.list.customers.services.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public void updateAddress(@PathVariable Long addressId, @RequestBody CreateAddressCommand updateAddressCommand) {
        addressService.updateAddress(addressId, updateAddressCommand);
    }
}
