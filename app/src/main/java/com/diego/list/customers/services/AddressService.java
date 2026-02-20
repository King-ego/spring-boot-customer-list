package com.diego.list.customers.services;

import com.diego.list.customers.application.command.address.CreateAddressCommand;
import com.diego.list.customers.application.command.address.UpdateAddressCommand;
import com.diego.list.customers.application.validation.AddressValidator;
import com.diego.list.customers.application.validation.UserValidator;
import com.diego.list.customers.model.Address;
import com.diego.list.customers.model.User;
import com.diego.list.customers.repository.AddressRepository;
import com.diego.list.customers.repository.UserRepository;
import com.diego.list.customers.application.usecase.address.AddressDefaultUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final AddressRepository addressRepository;
    private final AddressDefaultUseCase addressDefaultUseCase;

    public void CreateAddress(CreateAddressCommand createAddressCommand) {
        Optional<User> existUser = userRepository.findById(createAddressCommand.getUser_id());

        UserValidator.validateUserNotFound(existUser.isEmpty());

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

        Address newAddress = addressRepository.save(address);

        addressDefaultUseCase.validate(createAddressCommand.getIsDefault(), createAddressCommand.getUser_id(), newAddress.getId());
    }

    public void UpdateAddress(UUID addressId, UpdateAddressCommand updateAddressCommand){
        Optional<Address> optionalAddress = addressRepository.findById(addressId);

        AddressValidator.validateAddressNotFound(optionalAddress.isEmpty());

        Address address = optionalAddress.get();

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
        Optional<User> user =  userRepository.findById(userId);

        UserValidator.validateUserNotFound(user.isEmpty());

        return addressRepository.findByUserId(userId);
    }

    public void DeleteAddress(UUID addressId){
        Optional <Address> optionalAddress = addressRepository.findById(addressId);

        AddressValidator.validateAddressNotFound(optionalAddress.isEmpty());

        Address address = optionalAddress.get();

        AddressValidator.validateDefaultAddress(address.getIsDefault());

        addressRepository.deleteById(addressId);
    }


}
