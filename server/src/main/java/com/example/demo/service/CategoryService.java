package com.example.demo.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Category;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Category findById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));
    }

    public Category findBySlug(String slug) {
        return categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Category not found"));
    }

    public void create() {
    }

    public void update() {
    }
}
