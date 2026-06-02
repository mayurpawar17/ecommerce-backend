package dev.mayur.ecommerce_backend.features.auth.service;

import dev.mayur.ecommerce_backend.features.auth.entity.User;
import java.util.Optional;

public interface CurrentUserService {
    Optional<User> getCurrentUser();
    User getCurrentUserOrThrow();
    boolean isAuthenticated();
}
