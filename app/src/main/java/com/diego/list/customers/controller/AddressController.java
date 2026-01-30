package com.diego.list.customers.controller;

import com.diego.list.customers.application.command.address.CreateAddressCommand;
import com.diego.list.customers.application.command.address.UpdateAddressCommand;
import com.diego.list.customers.model.Address;
import com.diego.list.customers.services.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.ReturnedType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @PostMapping
    public String createAddress(@RequestBody CreateAddressCommand createAddressCommand) {
        addressService.CreateAddress(createAddressCommand);
        return "Create";
    }

    @PatchMapping("/{addressId}")
    public String updateAddress(@PathVariable UUID addressId, @RequestBody UpdateAddressCommand updateAddressCommand) {
        addressService.UpdateAddress(addressId, updateAddressCommand);
        return "Teste";
    }

    @GetMapping("/{userId}")
    public List<Address> getAddressesByUserId(@PathVariable UUID userId) {
        return addressService.GetAddressesByUserId(userId);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<String> deleteAddress(@Valid @PathVariable UUID addressId) {
        addressService.DeleteAddress(addressId);
        return ResponseEntity.ok("Deleted");
    }
}
