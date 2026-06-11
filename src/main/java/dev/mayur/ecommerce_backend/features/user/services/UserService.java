package dev.mayur.ecommerce_backend.features.user.services;


import dev.mayur.ecommerce_backend.features.user.dto.UserResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    UserResponseDTO uploadAvatar(Long userId, MultipartFile file);
}
