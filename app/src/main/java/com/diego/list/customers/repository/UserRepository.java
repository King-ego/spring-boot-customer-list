package com.diego.list.customers.repository;

import com.diego.list.customers.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
/*    Optional<User> findByUsername(String name);*/
    Optional<User> findByEmail(String email);/*
    boolean existsByUsername(String name);*/
    boolean existsByEmail(String email);
    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<User> findByNameOrEmailContaining(@Param("searchTerm") String searchTerm);

/*    @Query("SELECT u.usualTimezone FROM User u WHERE u.id = :userId")
    Optional<String> findUsualTimezoneByUserId(@Param("userId") String userId);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);

    @Query("SELECT u.usualTimezone FROM User u WHERE u.id = :userId")
    Optional<String> findUsualTimezoneByUserId(@Param("userId") String userId);

    List<User> findByNameContainingIgnoreCase(String name);

    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<User> findByNameOrEmailContaining(@Param("searchTerm") String searchTerm);

    long countByNameContainingIgnoreCase(String name);

    boolean existsByEmail(String email);*/
}