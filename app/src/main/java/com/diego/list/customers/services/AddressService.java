package com.diego.list.customers.services;

import com.diego.list.customers.command.address.CreateAddressCommand;
import com.diego.list.customers.command.address.UpdateAddressCommand;
import com.diego.list.customers.errors.CustomException;
import com.diego.list.customers.model.Address;
import com.diego.list.customers.model.User;
import com.diego.list.customers.repository.AddressRepository;
import com.diego.list.customers.repository.UserRepository;
import com.diego.list.customers.services.execute.OnlyAddressDefault;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class AddressService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;/*
    private final OnlyAddressDefault onlyAddressDefault;*/

    public void CreateAddress(CreateAddressCommand createAddressCommand) {
        Optional<User> existUser = userRepository.findById(createAddressCommand.getUser_id());

        if (existUser.isEmpty()) {
            throw new CustomException("User not found", HttpStatus.NOT_FOUND);
        }

        Address address = Address.builder()
                .city(createAddressCommand.getCity())
                .streetAddress(createAddressCommand.getStreetAddress())
                .complement(createAddressCommand.getComplement())
                .streetNumber(createAddressCommand.getStreetNumber())
                .recipient(createAddressCommand.getRecipient())
                .neighborhood(createAddressCommand.getNeighborhood())
                .state(createAddressCommand.getState())
                .zipCode(createAddressCommand.getZipCode())
                .phoneNumber(createAddressCommand.getPhoneNumber())
                .isDefault(createAddressCommand.getIsDefault())
                .user(existUser.get())
                .build();

        addressRepository.save(address);
/*
        onlyAddressDefault.validate(createAddressCommand.getIs_default(), createAddressCommand.getUser_id());*/
    }

    public void UpdateAddress(UUID addressId, UpdateAddressCommand updateAddressCommand){
        Optional <Address> existingAddress = addressRepository.findById(addressId);

        if (existingAddress.isEmpty()) {
            throw new CustomException("Address not found", HttpStatus.NOT_FOUND);
        }

        Address address = existingAddress.get();

        address.setCity(updateAddressCommand.getCity());
        address.setStreetAddress(updateAddressCommand.getStreetAddress());
        address.setComplement(updateAddressCommand.getComplement());
        address.setStreetNumber(updateAddressCommand.getStreetNumber());
        address.setRecipient(updateAddressCommand.getRecipient());
        address.setNeighborhood(updateAddressCommand.getNeighborhood());
        address.setState(updateAddressCommand.getState());
        address.setZipCode(updateAddressCommand.getZipCode());
        address.setPhoneNumber(updateAddressCommand.getPhoneNumber());
        address.setIsDefault(updateAddressCommand.getIsDefault());
        
        addressRepository.save(address);

    }

    public List<Address> GetAddressesByUserId(UUID userId){
        if (!userRepository.existsById(userId)) {
            throw new CustomException("User not found", HttpStatus.NOT_FOUND);
        }

        return addressRepository.findByUserId(userId);
    }


}
