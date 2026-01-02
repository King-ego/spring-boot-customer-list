package com.diego.list.customers.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @Column(unique = true)
    private String email;

    private String phone;

    private UserRole role;

    private String password;

    private boolean enabled;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean accountNonExpired;

    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
    private String usualTimezone;

    private String country;
    private LocalDateTime birthDate;


    public UserInfo toUserInfo() {
        return new UserInfo(this.id, this.email, this.name, this.role);
    }

    @PrePersist
    public void preCreate() {
        createdAt = LocalDateTime.now();
        accountNonExpired = true;
        credentialsNonExpired = true;
        accountNonLocked = true;
        enabled = true;
        role = UserRole.CUSTOMER;
    }
}
