package com.diego.list.customers.services;

import com.diego.list.customers.model.User;
import com.diego.list.customers.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersServices {

    @Autowired
    private UserRepository userRepository;

    public UsersServices() {
        System.out.println("UsersServices inicializado!");
    }

    public String getServiceName() {
        return "User Service";
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public List<User> searchUsersByName(String name) {
        return userRepository.findByNameContainingIgnoreCase(name);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> searchUsers(String searchTerm) {
        return userRepository.findByNameOrEmailContaining(searchTerm);
    }

    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

}