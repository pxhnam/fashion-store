package com.example.demo.dto.request;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.annotation.FileValidation;
import com.example.demo.annotation.IsUUID;

import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateImageRequest {
    @IsUUID
    private String id;

    private String imageUrl;

    @FileValidation(maxSize = 5_000_000, allowedTypes = { "image/jpeg", "image/png", "image/webp" })
    private MultipartFile file;

    @Builder.Default
    private Boolean isPrimary = false;

    @AssertTrue(message = "Either file or imageUrl must be provided")
    public boolean isValid() {
        boolean hasFile = file != null && !file.isEmpty();
        boolean hasUrl = imageUrl != null && !imageUrl.trim().isEmpty();

        if (id == null || id.isBlank()) {
            return hasFile;
        } else {
            return hasFile || hasUrl;
        }
    }
}
