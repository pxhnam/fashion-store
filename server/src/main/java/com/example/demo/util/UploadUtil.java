package com.example.demo.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.config.FileStorageProperties;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.InternalServerErrorException;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UploadUtil {
    private final FileStorageProperties fileStorageProperties;

    @PostConstruct
    public void init() {
        try {
            Path uploadPath = Paths.get(fileStorageProperties.getUploadDir());
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
        } catch (IOException e) {
            throw new InternalServerErrorException("Could not create upload directory!", e);
        }
    }

    public String save(MultipartFile file) {
        try {
            // check file is empty
            if (file.isEmpty()) {
                throw new BadRequestException("Uploaded file is empty.");
            }

            // check file type (only allow image)
            String contentType = file.getContentType();
            if (!isImageFile(contentType)) {
                throw new BadRequestException("Only image files are allowed.");
            }

            String originalFilename = file.getOriginalFilename();
            String fileExtension = getFileExtension(originalFilename);

            // check file name is valid
            if (originalFilename == null || originalFilename.trim().isEmpty()) {
                throw new BadRequestException("Invalid file name.");
            }
            // create name file unique
            String uniqueFilename = System.currentTimeMillis() + "_" +
                    UUID.randomUUID().toString() + fileExtension;

            // save file
            Path uploadPath = Paths.get(fileStorageProperties.getUploadDir());
            Path filePath = uploadPath.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/" + uniqueFilename;
        } catch (IOException e) {
            throw new InternalServerErrorException("Failed to save uploaded file.", e);
        }
    }

    private boolean isImageFile(String contentType) {
        return contentType != null && (contentType.startsWith("image/jpeg") ||
                contentType.startsWith("image/jpg") ||
                contentType.startsWith("image/png") ||
                contentType.startsWith("image/gif") ||
                contentType.startsWith("image/webp"));
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }
}
