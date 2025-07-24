package com.example.demo.dto.request;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.annotation.RequiredFile;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCategoryRequest {
    @NotBlank
    private String name;

    @RequiredFile
    private MultipartFile file;
}
