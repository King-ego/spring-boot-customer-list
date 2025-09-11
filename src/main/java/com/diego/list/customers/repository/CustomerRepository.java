package com.diego.list.customers.repository;

import com.diego.list.customers.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT c FROM customers c WHERE c.email = :email")
    Optional<Customer> findByEmail(String email);
}
