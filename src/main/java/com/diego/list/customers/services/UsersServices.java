package com.diego.list.customers.services;
import org.springframework.stereotype.Service;

@Service
public class UsersServices {
    public  UsersServices(String name) {
        System.out.println("Hello, " + name + "!");
    }
}
