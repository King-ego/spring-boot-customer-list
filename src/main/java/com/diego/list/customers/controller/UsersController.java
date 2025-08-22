package com.diego.list.customers.controller;

import com.diego.list.customers.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UsersController {
    @GetMapping("/users")
    public List<User> Users() {
        return List.of(
                new User(1, "Diego", ""),
                new User(1, "Diego", ""),
                new User(1, "Diego", "")
        );
    }
}
