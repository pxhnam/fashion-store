package com.example.demo.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateVariantRequest {
    @NotBlank
    private String size;

    @NotBlank
    private String color;

    @NotNull
    @DecimalMin("0.00")
    private BigDecimal price;

    @Builder.Default
    @Min(0)
    private Integer quantity = 0;
}
