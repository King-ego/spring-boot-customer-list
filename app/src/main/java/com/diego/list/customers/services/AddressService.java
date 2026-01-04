package com.diego.list.customers.services;

import com.diego.list.customers.command.CreateAddressCommand;
import com.diego.list.customers.errors.CustomException;
import com.diego.list.customers.model.Address;
import com.diego.list.customers.model.User;
import com.diego.list.customers.repository.AddressRepository;
import com.diego.list.customers.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class AddressService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public void CreateAddress(CreateAddressCommand createAddressCommand) {
        Optional<User> existUser = userRepository.findById(createAddressCommand.getUser_id());

        if (existUser.isEmpty()) {
            throw new CustomException("User not found", HttpStatus.NOT_FOUND);
        }

        Address address = Address.builder()
                .city(createAddressCommand.getCity())
                .street_address(createAddressCommand.getStreet_address())
                .complement(createAddressCommand.getComplement())
                .street_number(createAddressCommand.getStreet_number())
                .recipient(createAddressCommand.getRecipient())
                .neighborhood(createAddressCommand.getNeighborhood())
                .state(createAddressCommand.getState())
                .zip_code(createAddressCommand.getZip_code())
                .phone_number(createAddressCommand.getPhone_number())
                .is_default(createAddressCommand.getIs_default())
                .build();

        addressRepository.save(address);
    }
}
