package com.example.demo.mapper;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.demo.dto.response.ImageResponse;
import com.example.demo.dto.response.ProductDetailResponse;
import com.example.demo.dto.response.ProductResponse;
import com.example.demo.dto.response.VariantResponse;
import com.example.demo.entity.Image;
import com.example.demo.entity.Product;
import com.example.demo.entity.Variant;

@Component
public class ProductMapper {
    public List<ProductResponse> toResponseList(List<Product> products) {
        return products.stream().map(product -> {
            String imageUrl = product.getImages().stream()
                    .filter(Image::isPrimary)
                    .map(Image::getImageUrl)
                    .findFirst()
                    .or(() -> product.getImages().stream()
                            .map(Image::getImageUrl)
                            .findFirst())
                    .orElse(null);

            String price = product.getVariants().stream()
                    .map(Variant::getPrice)
                    .min(Comparator.naturalOrder())
                    .map(BigDecimal::toPlainString)
                    .orElse("0");

            return ProductResponse.builder()
                    .id(product.getId())
                    .imageUrl(imageUrl)
                    .name(product.getName())
                    .slug(product.getSlug())
                    .price(price)
                    .description(product.getDescription())
                    .status(product.getStatus())
                    .createdAt(product.getCreatedAt())
                    .updatedAt(product.getUpdatedAt())
                    .build();
        }).toList();
    }

    public ProductDetailResponse toResponse(Product product) {
        return ProductDetailResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .slug(product.getSlug())
                .description(product.getDescription())
                .status(product.getStatus())
                .images(product.getImages().stream()
                        .map(image -> ImageResponse.builder()
                                .id(image.getId())
                                .imageUrl(image.getImageUrl())
                                .isPrimary(image.isPrimary())
                                .build())
                        .collect(Collectors.toList()))
                .variants(product.getVariants().stream()
                        .map(variant -> VariantResponse.builder()
                                .id(variant.getId())
                                .size(variant.getSize())
                                .color(variant.getColor())
                                .price(variant.getPrice())
                                .quantity(variant.getQuantity())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
