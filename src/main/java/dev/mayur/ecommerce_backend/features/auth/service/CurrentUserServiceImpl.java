package dev.mayur.ecommerce_backend.features.auth.service;

import dev.mayur.ecommerce_backend.core.exception.custom.ResourceNotFoundException;
import dev.mayur.ecommerce_backend.features.auth.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CurrentUserServiceImpl implements CurrentUserService {

    @Override
    public Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return Optional.of(((CustomUserDetails) principal).user());
        } else if (principal instanceof User) {
            return Optional.of((User) principal);
        }
        return Optional.empty();
    }

    @Override
    public User getCurrentUserOrThrow() {
        return getCurrentUser()
                .orElseThrow(() -> new ResourceNotFoundException("No authenticated user session found"));
    }

    @Override
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() 
                && !(authentication.getPrincipal() instanceof String && authentication.getPrincipal().equals("anonymousUser"));
    }
}
