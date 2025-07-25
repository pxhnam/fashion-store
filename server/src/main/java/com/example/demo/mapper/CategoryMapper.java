package com.example.demo.mapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.demo.dto.response.CategoryResponse;
import com.example.demo.entity.Category;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CategoryMapper {
    public CategoryResponse toResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .imageUrl(category.getImageUrl())
                .name(category.getName())
                .slug(category.getSlug())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }

    public List<CategoryResponse> toResponseList(List<Category> categories) {
        return categories.stream()
                .filter(Objects::nonNull)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Category toEntity(String name, String slug, String imageUrl) {
        return Category.builder()
                .name(name)
                .slug(slug)
                .imageUrl(imageUrl)
                .build();
    }
}
