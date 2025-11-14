package com.diego.list.customers.model;

import com.stripe.model.issuing.Token;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    private boolean enabled = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean accountNonExpired = true;

    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
    private String usualTimezone;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Token.NetworkData.Device> trustedDevices = new ArrayList<>();
}