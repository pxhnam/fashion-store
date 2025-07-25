package com.example.demo.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.CreateCategoryRequest;
import com.example.demo.dto.request.UpdateCategoryRequest;
import com.example.demo.dto.response.CategoryResponse;
import com.example.demo.service.CategoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("categories")
@RestController
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryResponse> index() {
        return categoryService.findAll();
    }

    @GetMapping("{slug}")
    public CategoryResponse slug(@PathVariable String slug) {
        return categoryService.findBySlug(slug);
    }

    @PostMapping
    public CategoryResponse create(@Valid @ModelAttribute CreateCategoryRequest request) {
        return categoryService.create(request);
    }

    @PutMapping("{id}")
    public CategoryResponse update(@PathVariable UUID id,
            @Valid @ModelAttribute UpdateCategoryRequest request) {
        return categoryService.update(id, request);
    }
}
