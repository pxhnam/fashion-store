package com.example.demo.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.CreateProductRequest;
import com.example.demo.dto.request.UpdateProductRequest;
import com.example.demo.dto.response.ProductDetailResponse;
import com.example.demo.dto.response.ProductResponse;
import com.example.demo.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("products")
@RestController
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public List<ProductResponse> index() {
        return productService.findAll();
    }

    @GetMapping("{slug}")
    public ProductDetailResponse slug(@PathVariable String slug) {
        return productService.findBySlug(slug);
    }

    @PostMapping
    public ProductDetailResponse create(@Valid @ModelAttribute CreateProductRequest request) {
        return productService.create(request);
    }

    @PutMapping("{id}")
    public ProductDetailResponse update(
            @PathVariable String id,
            @Valid @ModelAttribute UpdateProductRequest request) {
        UUID uuid = UUID.fromString(id);
        return productService.update(uuid, request);
    }

    @DeleteMapping("{id}")
    public String delete(@PathVariable String id) {
        UUID uuid = UUID.fromString(id);
        return "deleted" + uuid;
    }
}
