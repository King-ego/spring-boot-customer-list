package com.diego.list.customers.services;

import com.diego.list.customers.application.command.users.CreateUserCommand;
import com.diego.list.customers.application.usecase.account.CreateAccountUseCase;
import com.diego.list.customers.model.Session;
import com.diego.list.customers.model.User;
import com.diego.list.customers.model.UserRole;
import com.diego.list.customers.repository.UserRepository;
import com.diego.list.customers.security.SessionAuthentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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

        user.setEnabled(true);

        Session session = mock(Session.class);
        when(session.getUserId()).thenReturn(userId);

        SessionAuthentication auth = mock(SessionAuthentication.class);
        when(auth.getSession()).thenReturn(session);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);

        SecurityContextHolder.setContext(securityContext);
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

    @Test
    @DisplayName("Save user customer with valid data")
    void testSaveUser_ValidData() {
        CreateUserCommand command = mock(CreateUserCommand.class);

        // Mock do customerDetails para não ser null
        CreateUserCommand.CustomerDetails customerDetails = mock(CreateUserCommand.CustomerDetails.class);
        when(customerDetails.getDocument()).thenReturn("123.456.789-00");


        when(command.getEmail()).thenReturn("test@email.com");
        when(command.getRole()).thenReturn(UserRole.CUSTOMER);

        User savedUser = new User();

        savedUser.setId(UUID.randomUUID());

        when(userRepository.findByEmail(command.getEmail())).thenReturn(java.util.Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        doNothing().when(createAccountUseCase).createAccount(any(CreateUserCommand.class), any(User.class));

        User result = usersServices.saveUser(command);

        assertEquals(savedUser.getId(), result.getId());
        verify(userRepository, times(1)).save(any(User.class));
        verify(createAccountUseCase, times(1)).createAccount(any(CreateUserCommand.class), any(User.class));

    }

}
