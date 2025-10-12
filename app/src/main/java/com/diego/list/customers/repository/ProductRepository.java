package com.diego.list.customers.repository;

import com.diego.list.customers.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Query("SELECT p FROM Product p WHERE p.name = :name")
    Optional<Product> findByName(String name);

    @Query("SELECT p FROM Product p WHERE p.name LIKE :identifier% OR p.category LIKE :identifier% OR p.description LIKE :identifier%")
    List<Product> findByIdentity(String identifier);
}