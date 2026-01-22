package com.diego.list.customers.application.usecase.address;

import com.diego.list.customers.model.Address;
import com.diego.list.customers.repository.AddressRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class AddressDefaultUseCase {
    private final AddressRepository addressRepository;

    public void validate(boolean isDefault, UUID userId, UUID addressId) {
        if (!isDefault) {
            return;
        }

        List<Address> addresses = addressRepository.findByUserId(userId);

        for (Address address : addresses) {
            if (!address.getId().equals(addressId) && address.getIsDefault()) {
                addressRepository.updateParse(address.getId(), false);
            }
        }

        log.info("One address set as default for user");
    }

}
