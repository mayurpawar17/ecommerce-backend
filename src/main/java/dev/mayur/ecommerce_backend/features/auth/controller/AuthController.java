package dev.mayur.ecommerce_backend.features.auth.controller;

import dev.mayur.ecommerce_backend.core.utils.dto.ApiResponse;
import dev.mayur.ecommerce_backend.core.utils.security.SecurityUtils;
import dev.mayur.ecommerce_backend.features.auth.dto.AuthResponse;
import dev.mayur.ecommerce_backend.features.auth.dto.LoginRequest;
import dev.mayur.ecommerce_backend.features.auth.dto.RefreshRequest;
import dev.mayur.ecommerce_backend.features.auth.dto.RegisterRequest;
import dev.mayur.ecommerce_backend.features.auth.entity.RefreshToken;
import dev.mayur.ecommerce_backend.features.auth.entity.User;
import dev.mayur.ecommerce_backend.features.auth.jwt.JwtUtil;
import dev.mayur.ecommerce_backend.features.auth.service.AuthService;
import dev.mayur.ecommerce_backend.features.auth.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@RequestBody RegisterRequest request) {
        AuthResponse data = authService.register(request);
        return ResponseEntity.ok(ApiResponse.success("User registered successfully", data));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest request) {
        AuthResponse data = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("User Login successfully", data));
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getCurrentUser() {
        User user = SecurityUtils.getCurrentUser();

        Map<String, Object> data = Map.of("id", user.getId(), "email", user.getEmail());
        return ResponseEntity.ok(ApiResponse.success("User Login successfully", data));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(@RequestBody RefreshRequest request) {

        RefreshToken refreshToken = refreshTokenService.verifyToken(request.getRefreshToken());

        String newAccessToken = jwtUtil.generateToken(refreshToken.getUser().getEmail());

        AuthResponse data = new AuthResponse(newAccessToken, refreshToken.getToken(), refreshToken.getUser().getEmail());

        return ResponseEntity.ok(ApiResponse.success("Token refreshed successfully", data));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        User user = SecurityUtils.getCurrentUser();

        authService.logout(user);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Logged out successfully", null));
//        return ResponseEntity.ok("Logged out successfully");
    }
}
