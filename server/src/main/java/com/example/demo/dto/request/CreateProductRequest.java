package com.example.demo.dto.request;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.annotation.RequiredFile;
import com.example.demo.annotation.ValidCategory;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
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

    private String description;

    @ValidCategory
    private UUID categoryId;

    @Valid
    @NotEmpty
    private List<FileRequest> files;

    @Valid
    @NotEmpty
    private List<VariantRequest> variants;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FileRequest {
        @RequiredFile
        private MultipartFile image;

        @Builder.Default
        private boolean isPrimary = false;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VariantRequest {
        @NotBlank
        private String size;

        @NotBlank
        private String color;

        @NotNull
        @DecimalMin("0.00")
        private BigDecimal price;

        @Min(0)
        private Integer quantity;
    }
}
