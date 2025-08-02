package com.example.demo.dto.request;

import java.util.List;

import com.example.demo.annotation.UniqueVariants;
import com.example.demo.annotation.ValidCategory;
import com.example.demo.enums.ProductStatus;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    @ValidCategory
    private String categoryId;

    @Builder.Default
    private ProductStatus status = ProductStatus.INACTIVE;

    @Valid
    @NotEmpty
    private List<CreateImageRequest> images;

    @Valid
    @NotEmpty
    @UniqueVariants
    private List<CreateVariantRequest> variants;
}
