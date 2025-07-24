package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.dto.request.CreateProductRequest;
import com.example.demo.entity.Product;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;

    public Product findBySlug(String slug) {
        return productRepository
                .findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Product not found"));
    }

    public String create(CreateProductRequest dto) {
        return "create product in service";
    }

}
