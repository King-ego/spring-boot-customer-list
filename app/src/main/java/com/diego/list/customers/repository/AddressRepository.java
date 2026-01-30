package com.diego.list.customers.repository;

import com.diego.list.customers.model.Address;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
    @Query("SELECT a FROM Address a WHERE a.user.id = :userId")
    List<Address> findByUserId(UUID userId);
    @Modifying
    @Transactional
    @Query("UPDATE Address a SET a.isDefault = :isDefault WHERE a.id = :addressId")
    void updateParse(UUID addressId, boolean isDefault);
}
