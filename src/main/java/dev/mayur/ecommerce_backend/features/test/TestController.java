package dev.mayur.ecommerce_backend.features.test;

import dev.mayur.ecommerce_backend.core.utils.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<String>> testUserEndpoint() {
        return ResponseEntity.ok(ApiResponse.success("Successfully accessed user endpoint!", "User data"));
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> testAdminEndpoint() {
        return ResponseEntity.ok(ApiResponse.success("Successfully accessed admin endpoint!", "Admin data"));
    }
}
