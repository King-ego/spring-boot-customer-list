package com.diego.list.customers.services;

import com.diego.list.customers.application.command.users.CreateCustomerCommand;
import com.diego.list.customers.application.command.users.CreateSellerCommand;
import com.diego.list.customers.application.command.users.CreateUserCommand;
import com.diego.list.customers.application.validation.UserValidator;
import com.diego.list.customers.dto.UpdateUserDto;
import com.diego.list.customers.errors.CustomException;
import com.diego.list.customers.model.Session;
import com.diego.list.customers.model.User;
import com.diego.list.customers.model.UserRole;
import com.diego.list.customers.repository.UserRepository;
import com.diego.list.customers.security.SessionAuthentication;
import com.diego.list.customers.application.usecase.account.CreateAccountUseCase;
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

    private final CreateAccountUseCase createAccountUseCase;
    private final PasswordEncoder passwordEncoder;

    /*@PersistenceContext
    private EntityManager entityManager;*/

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(UUID id) {
        SessionAuthentication auth = (SessionAuthentication) SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getSession() == null) {
            throw new CustomException("Session Expire or Invalid", HttpStatus.UNAUTHORIZED);
        }

        Session session = auth.getSession();

        if (!session.getUserId().equals(id)) {
            throw new CustomException("Not Access", HttpStatus.FORBIDDEN);
        }

        Optional <User> user =  userRepository.findById(id);

        Boolean disabledUser = user.isPresent() && !user.get().isEnabled();

        UserValidator.exceptionDisabledUser(disabledUser);

        return user;

    }

    public User saveUser(CreateUserCommand user) {
        Optional<User> existUser = userRepository.findByEmail(user.getEmail());

        UserValidator.exceptionUserExists(existUser.isPresent());

        boolean isCustomerWithoutDetail = user.getRole() == UserRole.CUSTOMER && user.getCustomerDetails() == null;

        UserValidator.exceptionCustomerDetails(isCustomerWithoutDetail);

        boolean isSellerWithoutDetail = user.getRole() == UserRole.SELLER && user.getSellerDetails() == null;

        UserValidator.exceptionSellerDetails(isSellerWithoutDetail);

        String randomPassword = UUID.randomUUID().toString();

        String password_encoder = passwordEncoder.encode(randomPassword);

        User userBuilder = User.builder()
                .name(user.getName())
                .email(user.getEmail())
                .password(password_encoder)
                .role(user.getRole())
                .enabled(false)
                .build();

        var create_user = userRepository.save(userBuilder);

        Map<UserRole, Runnable> validatedCreateRole = Map.of(
                UserRole.CUSTOMER, () -> {
                    CreateCustomerCommand customerCommand = CreateCustomerCommand.builder()
                            .user(create_user)
                            .document(user.getCustomerDetails().getDocument())
                            .build();
                    createAccountUseCase.createRoleCustomer(customerCommand);
                },
                UserRole.SELLER, () -> {
                    CreateSellerCommand sellerCommand = CreateSellerCommand.builder()
                            .user(create_user)
                            .storeName(user.getSellerDetails().getStoreName())
                            .documentNumber(user.getSellerDetails().getDocumentNumber())
                            .storeDescription(user.getSellerDetails().getStoreDescription())
                            .build();
                    createAccountUseCase.createRoleSeller(sellerCommand);
                }
        );

        Runnable action = validatedCreateRole.get(user.getRole());
        if (action != null) {
            action.run();
        }

        return  create_user;
    }

    public void deleteUser(UUID id) {
        Optional<User> user = userRepository.findById(id);

        UserValidator.exceptionUserNotFound(user.isEmpty());

        userRepository.deleteById(id);
    }


    public User getUserByEmail(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        UserValidator.exceptionUserNotFound(userOpt.isEmpty());

        User user = userOpt.get();

        Boolean disabledUser =  !user.isEnabled();

        UserValidator.exceptionDisabledUser(disabledUser);

        return user;
    }

    public void updateUserPartial(
            UUID id,
            UpdateUserDto userDetails
    ) {
        Optional<User> user = userRepository.findById(id);

        UserValidator.exceptionUserNotFound(user.isEmpty());

        Boolean disabledUser = !user.get().isEnabled();

        UserValidator.exceptionDisabledUser(disabledUser);

        String encodedPassword = user.get().getPassword();

        if (userDetails.getPassword() != null) {
            encodedPassword = passwordEncoder.encode(userDetails.getPassword());
        }

        userRepository.updateParse(
                id,
                userDetails.getName(),
                userDetails.getBirthDate(),
                userDetails.getPhone(),
                encodedPassword
        );
    }

    public void enabledUser(UUID userId) {
        Optional<User> user = userRepository.findById(userId);

        UserValidator.exceptionUserNotFound(user.isEmpty());

        userRepository.enabledParse(userId);
    }

}
