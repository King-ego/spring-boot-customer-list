package com.diego.list.customers.services;

import com.diego.list.customers.application.usecase.account.CreateAccountUseCase;
import com.diego.list.customers.model.User;
import com.diego.list.customers.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

@ExtendWith(SpringExtension.class)
public class UserServicesTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private CreateAccountUseCase createAccountUseCase;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsersServices usersServices;

    private UUID userId;
    private User user;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = new User();
        user.setId(userId);
    }


}
