package dev.mayur.ecommerce_backend.core.utils.security;

import dev.mayur.ecommerce_backend.features.auth.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public class SecurityUtils {

    public static User getCurrentUser() {
        return (User) Objects.requireNonNull(
                SecurityContextHolder.getContext().getAuthentication()
        ).getPrincipal();
    }
}
