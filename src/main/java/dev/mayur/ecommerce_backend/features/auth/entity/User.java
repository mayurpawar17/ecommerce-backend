package dev.mayur.ecommerce_backend.features.auth.entity;

import dev.mayur.ecommerce_backend.core.utils.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean enabled = true;

    // Stores uploaded avatar image URL/path
    @Column(name = "avatar_url")
    private String avatarUrl;
}
