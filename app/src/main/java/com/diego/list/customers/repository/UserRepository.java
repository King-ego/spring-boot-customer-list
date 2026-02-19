package com.diego.list.customers.repository;

import com.diego.list.customers.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);/*
    boolean existsByUsername(String name);*/
    boolean existsByEmail(String email);
    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<User> findByNameOrEmailContaining(@Param("searchTerm") String searchTerm);

    @Modifying
    @Query("""
        UPDATE User u SET
            u.name = COALESCE(:name, u.name),
            u.birthDate = COALESCE(:birthDate, u.birthDate),
            u.phone = COALESCE(:phone, u.phone),
            u.password = COALESCE(:password, u.password)
        WHERE u.id = :id
        """)
    void updateParse(
            UUID id,
            String name,
            LocalDateTime birthDate,
            String phone,
            String password
    );

    @Modifying
    @Query("UPDATE User u SET u.enabled = true WHERE u.id = :userId")
    void enabledParse(UUID userId);
}
