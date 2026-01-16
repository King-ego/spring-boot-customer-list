package com.diego.list.customers.controller;

import com.diego.list.customers.command.createUser.CreateUserCommand;
import com.diego.list.customers.http.responses.ReturnUsers;
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
    public ResponseEntity<ReturnUsers> getUserById(@PathVariable UUID id) {
        Optional<User> user = usersServices.getUserById(id);

        return user.map(ReturnUsers::from)
                       .map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());

    }

    @PostMapping("/register")
    public User createUser(@RequestBody CreateUserCommand user) {
        return usersServices.saveUser(user);
    }

    /*@PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @RequestBody User userDetails) {
        Optional<User> optionalUser = usersServices.getUserById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setName(userDetails.getName());
            user.setEmail(userDetails.getEmail());
            return ResponseEntity.ok(usersServices.saveUser(user));
        }
        return ResponseEntity.notFound().build();
    }*/

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        if (usersServices.getUserById(id).isPresent()) {
            usersServices.deleteUser(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    /*@GetMapping("/search")
    public ResponseEntity<List<ListUsers>> searchUsers(@RequestParam String term) {
        List<User> users = usersServices.searchUsers(term);

        List<ListUsers> response = users.stream()
                .map(ListUsers::from)
                .toList();

        return ResponseEntity.ok(response);
    }
*/
    @GetMapping("/by-email")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        Optional<User> user = usersServices.getUserByEmail(email);
        return user.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }
}