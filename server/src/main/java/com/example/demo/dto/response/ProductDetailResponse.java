package com.example.demo.dto.response;

import java.time.Instant;
import java.util.List;
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
public class ProductDetailResponse {
    private UUID id;
    private String name;
    private String slug;
    private String description;
    private ProductStatus status;
    List<ImageResponse> images;
    List<VariantResponse> variants;
    private Instant updatedAt;
    private Instant createdAt;
}
