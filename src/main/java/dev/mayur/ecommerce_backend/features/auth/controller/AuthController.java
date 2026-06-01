//package dev.mayur.ecommerce_backend.features.auth.controller;
//
//import dev.mayur.ecommerce_backend.core.utils.dto.ApiResponse;
//import dev.mayur.ecommerce_backend.features.auth.dto.RegisterRequest;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.time.LocalDateTime;
//
//@RestController
//@RequestMapping("/api/v1/auth")
//@RequiredArgsConstructor
//public class AuthController {
//    private final AuthService authService;
//
//    @PostMapping("/register")
//    public ResponseEntity<ApiResponse<AuthResponse>> register(
//            @Valid @RequestBody RegisterRequest request) {
//
//        return ResponseEntity.ok(
//                ApiResponse.<AuthResponse>builder()
//                        .success(true)
//                        .message("User Registered")
//                        .data(authService.register(request))
//                        .timestamp(LocalDateTime.now())
//                        .build()
//        );
//    }
//}
