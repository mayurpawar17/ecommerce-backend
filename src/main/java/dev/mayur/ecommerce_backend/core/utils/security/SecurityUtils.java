package dev.mayur.ecommerce_backend.core.utils.security;

import dev.mayur.ecommerce_backend.features.auth.entity.User;
import dev.mayur.ecommerce_backend.features.auth.service.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).user();
        } else if (principal instanceof User) {
            return (User) principal;
        }
        return null;
    }
}
