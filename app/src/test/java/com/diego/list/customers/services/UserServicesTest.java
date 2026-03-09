package com.diego.list.customers.services;

import com.diego.list.customers.application.usecase.account.CreateAccountUseCase;
import com.diego.list.customers.model.User;
import com.diego.list.customers.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    @DisplayName("Test getUserById with valid user ID")
    void testGetUserById_ValidId() {
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));

        var result = usersServices.getUserById(userId);

        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getId());
        verify(userRepository, times(1)).findById(userId);
    }

}
