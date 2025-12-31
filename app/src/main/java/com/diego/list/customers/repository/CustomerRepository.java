package com.diego.list.customers.repository;

import com.diego.list.customers.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    @Query("SELECT c FROM Customer c WHERE c.userId = ?1")
    Optional<Customer> findByUserId(UUID userId);
}
