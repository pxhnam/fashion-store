package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.CreateProductRequest;
import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("products")
@RestController
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public String index() {
        return "get product";
    }

    @GetMapping("{slug}")
    public Product slug(@PathVariable String slug) {
        return productService.findBySlug(slug);
    }

    @PostMapping
    public String create(@Valid @ModelAttribute CreateProductRequest request) {
        return productService.create(request);
    }
}
