package dev.mayur.ecommerce_backend.features.file.services;


import dev.mayur.ecommerce_backend.core.exception.custom.FailedToDeleteException;
import dev.mayur.ecommerce_backend.core.exception.custom.InvalidFileException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    //Folder where files will be stored
    private final Path uploadPath = Paths.get("uploads/avatars");

    public FileStorageServiceImpl() {

        try {
            // Create folders if not exists
            Files.createDirectories(uploadPath);

        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory");
        }
    }

    @Override
    public String uploadFile(MultipartFile file) {

        //Validate empty file
        if (file.isEmpty()) {
            throw new InvalidFileException("File is empty");
        }

        //Validate file type
        String contentType = file.getContentType();

        if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png") && !contentType.equals("image/webp"))) {

            throw new InvalidFileException("Only JPG, PNG, WEBP allowed");
        }

        //Validate file size (5MB)
        long maxSize = 5 * 1024 * 1024;

        if (file.getSize() > maxSize) {
            throw new InvalidFileException("File size exceeds 5MB");
        }

        try {

            // Generate unique filename
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());

            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

            String fileName = UUID.randomUUID() + extension;

            // Final storage location
            Path targetLocation = uploadPath.resolve(fileName);

            //Copy file to uploads folder
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);


            //Return relative file path
            return "/uploads/avatars/" + fileName;

        } catch (IOException e) {
            throw new InvalidFileException("Failed to upload file");
        }
    }

    @Override
    public void deleteFile(String filePath) {

        try {

            // Remove "/uploads/avatars/"
            String fileName = filePath.replace("/uploads/avatars/", "");

            Path targetPath = uploadPath.resolve(fileName);

            Files.deleteIfExists(targetPath);

        } catch (IOException e) {
            throw new FailedToDeleteException("Failed to delete file");
        }
    }
}
