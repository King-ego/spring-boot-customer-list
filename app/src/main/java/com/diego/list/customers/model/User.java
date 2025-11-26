package com.diego.list.customers.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    private String passwordHash;

    private boolean enabled = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean accountNonExpired = true;

    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
    private String usualTimezone;


    public UserInfo toUserInfo() {
        return new UserInfo(this.id, this.email, this.name, this.role);
    }
}
