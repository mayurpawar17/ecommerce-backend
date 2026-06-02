package dev.mayur.ecommerce_backend.features.auth.service;

import dev.mayur.ecommerce_backend.core.utils.enums.Role;
import dev.mayur.ecommerce_backend.features.auth.dto.AuthResponse;
import dev.mayur.ecommerce_backend.features.auth.dto.LoginRequest;
import dev.mayur.ecommerce_backend.features.auth.dto.RegisterRequest;
import dev.mayur.ecommerce_backend.features.auth.entity.RefreshToken;
import dev.mayur.ecommerce_backend.features.auth.entity.User;
import dev.mayur.ecommerce_backend.features.auth.jwt.JwtUtil;
import dev.mayur.ecommerce_backend.features.auth.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    // Register
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ROLE_USER); // Production practice: Default to lowest privilege (ROLE_USER)
        user.setEnabled(true);

        userRepository.save(user);

        // Generate access token with role claim
        String accessToken = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        // Generate refresh token
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return new AuthResponse(
                accessToken,
                refreshToken.getToken(),
                user.getEmail()
        );
    }

    // Login
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Compare hashed password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        
        // Generate access token with role claim
        String accessToken = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return new AuthResponse(accessToken, refreshToken.getToken(), user.getEmail());
    }

    // Logout
    public void logout(User user) {
        refreshTokenService.deleteByUser(user);
    }
}
