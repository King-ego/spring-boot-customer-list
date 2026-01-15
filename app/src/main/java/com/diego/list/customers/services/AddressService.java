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
                .street_address(createAddressCommand.getStreet_address())
                .complement(createAddressCommand.getComplement())
                .street_number(createAddressCommand.getStreet_number())
                .recipient(createAddressCommand.getRecipient())
                .neighborhood(createAddressCommand.getNeighborhood())
                .state(createAddressCommand.getState())
                .zip_code(createAddressCommand.getZip_code())
                .phone_number(createAddressCommand.getPhone_number())
                .is_default(createAddressCommand.getIs_default())
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
        address.setStreet_address(updateAddressCommand.getStreet_address());
        address.setComplement(updateAddressCommand.getComplement());
        address.setStreet_number(updateAddressCommand.getStreet_number());
        address.setRecipient(updateAddressCommand.getRecipient());
        address.setNeighborhood(updateAddressCommand.getNeighborhood());
        address.setState(updateAddressCommand.getState());
        address.setZip_code(updateAddressCommand.getZip_code());
        address.setPhone_number(updateAddressCommand.getPhone_number());
        address.setIs_default(updateAddressCommand.getIs_default());
        
        addressRepository.save(address);

    }

    public List<Address> GetAddressesByUserId(UUID userId){
        if (!userRepository.existsById(userId)) {
            throw new CustomException("User not found", HttpStatus.NOT_FOUND);
        }

        return addressRepository.findByUserId(userId);
    }


}
