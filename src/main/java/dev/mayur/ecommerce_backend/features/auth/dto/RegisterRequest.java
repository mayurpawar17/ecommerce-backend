package dev.mayur.ecommerce_backend.features.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(@NotBlank String username, @Email String email, @Size(min = 6) @NotBlank String password) {
}


