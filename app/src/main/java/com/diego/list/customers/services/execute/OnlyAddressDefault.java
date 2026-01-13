package com.diego.list.customers.services.execute;

import com.diego.list.customers.model.Address;
import com.diego.list.customers.repository.AddressRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OnlyAddressDefault {
    private final AddressRepository addressRepository;

    public void validate(boolean isDefault, UUID userId) {
        if (!isDefault) {
            return;
        }

        List<Address> addresses = addressRepository.findByUserId(userId);

        System.out.println("addresses: " + addresses);
    }

}
