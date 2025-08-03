package com.example.demo.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.demo.dto.request.CreateCategoryRequest;
import com.example.demo.dto.request.UpdateCategoryRequest;
import com.example.demo.dto.response.CategoryResponse;
import com.example.demo.entity.Category;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.util.SlugUtil;
import com.example.demo.util.UploadUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UploadUtil uploadUtil;
    private final SlugUtil slugUtil;
    private final CategoryMapper categoryMapper;

    @Transactional(readOnly = true)
    public List<CategoryResponse> findAll() {
        List<Category> categories = categoryRepository.findAll();
        return categoryMapper.toResponseList(categories);
    }

    public Category findById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));
    }

    @Transactional(readOnly = true)
    public CategoryResponse findBySlug(String slug) {
        return categoryMapper.toResponse(categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Category not found")));
    }

    @Transactional
    public CategoryResponse create(CreateCategoryRequest dto) {
        String slug = makeSlug(null, slugUtil.generate(dto.getName()));
        String imageUrl = uploadUtil.save(dto.getFile());
        Category category = categoryMapper.toEntity(dto.getName(), slug, imageUrl);
        categoryRepository.save(category);
        return categoryMapper.toResponse(category);
    }

    @Transactional
    public CategoryResponse update(UUID id, UpdateCategoryRequest dto) {
        Category category = findById(id);
        if (StringUtils.hasText(dto.getName())) {
            category.setName(dto.getName());
            String slug = makeSlug(id, slugUtil.generate(dto.getName()));
            category.setSlug(slug);
        }

        if (dto.getFile() != null && !dto.getFile().isEmpty()) {
            String imageUrl = uploadUtil.save(dto.getFile());
            category.setImageUrl(imageUrl);
        }
        categoryRepository.save(category);
        return categoryMapper.toResponse(category);
    }

    private String makeSlug(UUID id, String slug) {
        return categoryRepository
                .findBySlug(slug)
                .filter(c -> !c.getId().equals(id))
                .map(c -> {
                    String suffix = UUID.randomUUID()
                            .toString()
                            .substring(0, 8);
                    return makeSlug(id, slug + "-" + suffix);
                })
                .orElse(slug);
    }
}
