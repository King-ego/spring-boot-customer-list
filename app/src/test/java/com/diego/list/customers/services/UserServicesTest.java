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

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

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
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        var result = usersServices.getUserById(userId);

        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getId());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Test getUserById with invalid session should throw exception")
    void testGetUserById_InvalidSession() {
        SecurityContextHolder.clearContext();

        assertThrows(Exception.class, () -> usersServices.getUserById(userId));
    }

    /*@Test
    @DisplayName("Save user CUSTOMER with valid data")
    void testSaveUser_CustomerValidData() {
        CreateUserCommand command = mock(CreateUserCommand.class);


        when(command.getEmail()).thenReturn("customer@email.com");
        when(command.getName()).thenReturn("Customer Test");
        when(command.getRole()).thenReturn(UserRole.CUSTOMER);

        User savedUser = new User();
        savedUser.setId(UUID.randomUUID());

        when(userRepository.findByEmail("customer@email.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        *//*doNothing().when(createAccountUseCase).createAccount(any(CreateUserCommand.class), any(User.class));
*//*
        User result = usersServices.saveUser(command);

        assertNotNull(result);
        assertEquals(savedUser.getId(), result.getId());
        verify(userRepository, times(1)).save(any(User.class));
        verify(createAccountUseCase, times(1)).createAccount(any(CreateUserCommand.class), any(User.class));
    }
    */
    @Test
    @DisplayName("Save user should throw exception when email already exists")
    void testSaveUser_EmailAlreadyExists() {
        CreateUserCommand command = mock(CreateUserCommand.class);
        when(command.getEmail()).thenReturn("existing@email.com");

        when(userRepository.findByEmail("existing@email.com")).thenReturn(Optional.of(user));

        assertThrows(Exception.class, () -> usersServices.saveUser(command));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Delete user with valid ID")
    void testDeleteUser_ValidId() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(userId);

        assertDoesNotThrow(() -> usersServices.deleteUser(userId));
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    @DisplayName("Delete user should throw exception when user not found")
    void testDeleteUser_NotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> usersServices.deleteUser(userId));
        verify(userRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Get user by email with valid email")
    void testGetUserByEmail_ValidEmail() {
        when(userRepository.findByEmail("customer@email.com")).thenReturn(Optional.of(user));

        var result = usersServices.getUserByEmail("customer@email.com");

        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getId());
        verify(userRepository, times(1)).findByEmail("customer@email.com");
    }
}
