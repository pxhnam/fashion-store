package com.example.demo.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VariantResponse {
    private UUID id;
    private String size;
    private String color;
    private BigDecimal price;
    private Integer quantity;
}
