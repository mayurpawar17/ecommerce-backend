package dev.mayur.ecommerce_backend.features.user.controller;

import dev.mayur.ecommerce_backend.core.utils.dto.ApiResponse;
import dev.mayur.ecommerce_backend.features.auth.entity.User;
import dev.mayur.ecommerce_backend.features.auth.service.CustomUserDetails;
import dev.mayur.ecommerce_backend.features.product.dto.ProductResponseDTO;
import dev.mayur.ecommerce_backend.features.user.dto.UserResponseDTO;
import dev.mayur.ecommerce_backend.features.user.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Upload profile avatar
    @PutMapping("/me/avatar")
    public ResponseEntity<ApiResponse<UserResponseDTO>> uploadAvatar(@RequestParam("file") MultipartFile file, Authentication authentication) {

        // Get logged-in user email
//        String email = customUserDetails.getUser().getEmail();

        //Get logged-in user details
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();


        //Get user id directly
        Long userId = userDetails.getId();

        UserResponseDTO userResponseDTO = userService.uploadAvatar(userId, file);
        ApiResponse<UserResponseDTO> body = ApiResponse.success("Photo upload successfully!", userResponseDTO);
        return ResponseEntity.status(HttpStatus.OK).body(body);

    }
}
