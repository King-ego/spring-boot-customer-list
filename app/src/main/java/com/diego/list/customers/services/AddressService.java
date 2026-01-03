package com.diego.list.customers.services;

import com.diego.list.customers.command.CreateAddressCommand;
import com.diego.list.customers.repository.AddressRepository;
import com.diego.list.customers.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class AddressService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public void CreateAddress(CreateAddressCommand createAddressCommand) {

    }
}
