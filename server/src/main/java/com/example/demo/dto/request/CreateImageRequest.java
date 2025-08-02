package com.example.demo.dto.request;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.annotation.FileValidation;
import com.example.demo.annotation.RequiredFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateImageRequest {
    @RequiredFile
    @FileValidation(maxSize = 5_000_000, allowedTypes = { "image/jpeg", "image/png", "image/webp" })
    private MultipartFile file;

    @Builder.Default
    private Boolean isPrimary = false;
}
