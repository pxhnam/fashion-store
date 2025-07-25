package com.example.demo.dto.request;

import java.util.List;
import java.util.UUID;

import com.example.demo.annotation.ValidCategory;
import com.example.demo.enums.ProductStatus;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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

    private String description;

    @ValidCategory
    private UUID categoryId;

    @Builder.Default
    private ProductStatus status = ProductStatus.ACTIVE;

    @Valid
    @NotEmpty
    private List<CreateImageRequest> files;

    @Valid
    @NotEmpty
    private List<CreateVariantRequest> variants;
}
