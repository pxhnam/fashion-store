package com.example.demo.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.demo.enums.ProductStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private UUID id;
    private String imageUrl;
    private String name;
    private String slug;
    private String price;
    private String description;
    private ProductStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
