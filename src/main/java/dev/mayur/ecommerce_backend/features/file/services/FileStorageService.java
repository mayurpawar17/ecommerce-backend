package dev.mayur.ecommerce_backend.features.file.services;


import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    // Upload file and return stored file path/url
    String uploadFile(MultipartFile file);

    // Delete old file
    void deleteFile(String filePath);
}
