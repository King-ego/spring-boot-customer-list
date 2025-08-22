package com.diego.list.customers.controller;

import com.diego.list.customers.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UsersController {
    @GetMapping("/users")
    public String Users() {
        var users = List.of(
                new User(1, "Diego", ""),
                new User(1, "Diego", ""),
                new User(1, "Diego", "")
        );
        return "Users endpoint is working!";
    }
}
