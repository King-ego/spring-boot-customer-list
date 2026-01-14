package com.diego.list.customers.repository;

import com.diego.list.customers.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
    @Query("SELECT a FROM Address a WHERE a.user.id = :userId")
    List<Address> findByUserId(UUID userId);
}
