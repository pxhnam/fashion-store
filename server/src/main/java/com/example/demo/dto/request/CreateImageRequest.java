package com.example.demo.dto.request;

import org.springframework.web.multipart.MultipartFile;

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
    private MultipartFile image;

    @Builder.Default
    private Boolean isPrimary = false;

}
