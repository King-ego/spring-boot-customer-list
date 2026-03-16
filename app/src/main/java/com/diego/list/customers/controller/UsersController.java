package com.diego.list.customers.controller;

import com.diego.list.customers.application.command.users.CreateUserCommand;
import com.diego.list.customers.dto.UpdateUserDto;
import com.diego.list.customers.controller.response.users.UsersResponse;
import com.diego.list.customers.model.User;
import com.diego.list.customers.services.UsersServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersServices usersServices;

    @GetMapping
    public List<User> getAllUsers() {
        return usersServices.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsersResponse> getUserById(@PathVariable UUID id) {
        Optional<User> user = usersServices.getUserById(id);

        return user.map(UsersResponse::from)
                       .map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());

    }

    @PostMapping("/register")
    public User createUser(@RequestBody CreateUserCommand user) {
        return usersServices.saveUser(user);
    }

    @PatchMapping("/enabled/user/{id}")
    public void UserEnabled(@PathVariable UUID id) {
        usersServices.enabledUser(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        if (usersServices.getUserById(id).isPresent()) {
            usersServices.deleteUser(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/by-email")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        Optional<User> userOpt = usersServices.getUserByEmail(email);
        return userOpt
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok().build());

    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Void> updateUserPartially(
            @PathVariable UUID userId,
            @RequestBody UpdateUserDto updates) {
        try {
            usersServices.updateUserPartial(userId, updates);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
