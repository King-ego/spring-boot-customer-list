package com.diego.list.customers.services;

import com.diego.list.customers.command.createUser.CreateCustomerCommand;
import com.diego.list.customers.command.createUser.CreateSellerCommand;
import com.diego.list.customers.command.createUser.CreateUserCommand;
import com.diego.list.customers.errors.CustomException;
import com.diego.list.customers.model.Session;
import com.diego.list.customers.model.User;
import com.diego.list.customers.model.UserRole;
import com.diego.list.customers.repository.UserRepository;
import com.diego.list.customers.security.SessionAuthentication;
import com.diego.list.customers.services.execute.CreateTypeAccount;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class UsersServices {

    @Autowired
    private UserRepository userRepository;

    private final CreateTypeAccount createTypeAccount;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(UUID id) {
        SessionAuthentication auth = (SessionAuthentication) SecurityContextHolder.getContext().getAuthentication();

        log.info("auth: {}", auth);

        if (auth == null || auth.getSession() == null) {
            throw new CustomException("Sessão inválida ou expirada", HttpStatus.UNAUTHORIZED);
        }

        Session session = auth.getSession();

        if (!session.getUserId().equals(id)) {
            throw new CustomException("Você só pode acessar seus próprios dados", HttpStatus.FORBIDDEN);
        }

        return userRepository.findById(id);

    }

    public User saveUser(CreateUserCommand createUser) {
        Optional<User> existUser = userRepository.findByEmail(createUser.getEmail());

        if (existUser.isPresent()) {
            throw new CustomException("Email already in use", HttpStatus.CONFLICT);
        }

        if (createUser.getRole() == UserRole.CUSTOMER && createUser.getCustomerDetails() == null) {
            throw new CustomException("Customer details are required", HttpStatus.BAD_REQUEST);
        }

        if (createUser.getRole() == UserRole.SELLER && createUser.getSellerDetails() == null) {
            throw new CustomException("Seller details are required", HttpStatus.BAD_REQUEST);
        }

        String password_encoder = passwordEncoder.encode(createUser.getPassword());

        User user = User.builder()
                .name(createUser.getName())
                .email(createUser.getEmail())
                .password(password_encoder)
                .role(createUser.getRole())
                .build();

        var create_user = userRepository.save(user);

        Map<UserRole, Runnable> validatedCreateRole = Map.of(
                UserRole.CUSTOMER, () -> {
                    CreateCustomerCommand customerCommand = CreateCustomerCommand.builder()
                            .user(create_user)
                            .document(createUser.getCustomerDetails().getDocument())
                            .build();
                    createTypeAccount.createRoleCustomer(customerCommand);
                },
                UserRole.SELLER, () -> {
                    CreateSellerCommand sellerCommand = CreateSellerCommand.builder()
                            .user(create_user)
                            .storeName(createUser.getSellerDetails().getStoreName())
                            .documentNumber(createUser.getSellerDetails().getDocumentNumber())
                            .storeDescription(createUser.getSellerDetails().getStoreDescription())
                            .build();
                    createTypeAccount.createRoleSeller(sellerCommand);
                }
        );

        Runnable action = validatedCreateRole.get(createUser.getRole());
        if (action != null) {
            action.run();
        }

        return  create_user;
    }

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }


    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
/*
    public List<User> searchUsers(String searchTerm) {
        return userRepository.findByNameOrEmailContaining(searchTerm);
    }*/
}