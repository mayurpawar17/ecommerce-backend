package dev.mayur.ecommerce_backend.features.user.services;

import dev.mayur.ecommerce_backend.core.exception.custom.UserNotFoundException;
import dev.mayur.ecommerce_backend.features.auth.entity.User;
import dev.mayur.ecommerce_backend.features.auth.repo.UserRepository;
import dev.mayur.ecommerce_backend.features.file.services.FileStorageService;
import dev.mayur.ecommerce_backend.features.user.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final FileStorageService fileStorageService;

    @Override
    public UserResponseDTO uploadAvatar(Long userId, MultipartFile file) {

        //Get logged-in user
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

        //Delete old avatar if exists
        if (user.getAvatarUrl() != null) {
            fileStorageService.deleteFile(user.getAvatarUrl());
        }

        //Upload new avatar
        String avatarUrl = fileStorageService.uploadFile(file);

        //Save avatar path
        user.setAvatarUrl(avatarUrl);

        userRepository.save(user);

        //Return response
        return UserResponseDTO.builder().id(user.getId()).name(user.getName()).email(user.getEmail()).avatarUrl(user.getAvatarUrl()).build();
    }
}
