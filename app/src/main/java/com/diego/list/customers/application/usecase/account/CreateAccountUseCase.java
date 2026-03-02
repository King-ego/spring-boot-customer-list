package com.diego.list.customers.application.usecase.account;

import com.diego.list.customers.application.command.users.CreateCustomerCommand;
import com.diego.list.customers.application.command.users.CreateSellerCommand;
import com.diego.list.customers.application.command.users.CreateUserCommand;
import com.diego.list.customers.model.Customer;
import com.diego.list.customers.model.Seller;
import com.diego.list.customers.model.User;
import com.diego.list.customers.model.UserRole;
import com.diego.list.customers.repository.CustomerRepository;
import com.diego.list.customers.repository.SellerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class CreateAccountUseCase {
    private final CustomerRepository customerRepository;
    private final SellerRepository sellerRepository;

    public void createAccount(CreateUserCommand command, User create_user) {
        Map<UserRole, Runnable> validatedCreateRole = Map.of(
                UserRole.CUSTOMER, () -> {
                    CreateCustomerCommand customerCommand = CreateCustomerCommand.builder()
                            .user(create_user)
                            .document(command.getCustomerDetails().getDocument())
                            .build();
                    this.createRoleCustomer(customerCommand);
                },
                UserRole.SELLER, () -> {
                    CreateSellerCommand sellerCommand = CreateSellerCommand.builder()
                            .user(create_user)
                            .storeName(command.getSellerDetails().getStoreName())
                            .documentNumber(command.getSellerDetails().getDocumentNumber())
                            .storeDescription(command.getSellerDetails().getStoreDescription())
                            .build();
                    this.createRoleSeller(sellerCommand);
                }
        );

        Runnable action = validatedCreateRole.get(command.getRole());
        if (action != null) {
            action.run();
        }

    }

    public void createRoleCustomer(CreateCustomerCommand command){
        Customer customer = Customer.builder()
                        .user(command.getUser())
                        .document(command.getDocument())
                        .build();

        customerRepository.save(customer);
        log.info("Creating role customer {}", command);
    }

    public void createRoleSeller(CreateSellerCommand command){
        Seller seller = Seller.builder()
                        .user(command.getUser())
                        .storeName(command.getStoreName())
                        .documentNumber(command.getDocumentNumber())
                        .storeDescription(command.getStoreDescription())
                        .build();

        sellerRepository.save(seller);
        log.info("Updating role seller {}", command);
    }
}
