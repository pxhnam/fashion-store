package com.example.demo.dto.request;

import com.example.demo.annotation.ValidVariant;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCartRequest {

    @NotNull
    @ValidVariant
    private String variantId;

    @Min(1)
    @Builder.Default
    private Integer quantity = 1;
}
