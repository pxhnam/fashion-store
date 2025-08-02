package com.example.demo.dto.request;

import java.math.BigDecimal;

import com.example.demo.annotation.IsUUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateVariantRequest {
    @IsUUID
    private String id;

    @NotBlank
    @Size(min = 1, max = 10)
    private String size;

    @NotBlank
    @Size(min = 1, max = 50)
    private String color;

    @NotNull
    @DecimalMin("0.00")
    private BigDecimal price;

    @Builder.Default
    @Min(0)
    private Integer quantity = 0;
}
