package com.example.demo.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.dto.request.CreateProductRequest;
import com.example.demo.dto.request.UpdateProductRequest;
import com.example.demo.dto.response.ProductDetailResponse;
import com.example.demo.dto.response.ProductResponse;
import com.example.demo.entity.Category;
import com.example.demo.entity.Image;
import com.example.demo.entity.Product;
import com.example.demo.entity.Variant;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.repository.ProductRepository;
import com.example.demo.util.SlugUtil;
import com.example.demo.util.UploadUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final UploadUtil uploadUtil;
    private final SlugUtil slugUtil;
    private final ProductMapper productMapper;

    public List<ProductResponse> findAll() {
        return productMapper.toResponseList(productRepository.findAll());
    }

    public Product findById(UUID id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
    }

    public ProductDetailResponse findBySlug(String slug) {
        return productMapper.toResponse(productRepository
                .findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Product not found")));
    }

    @Transactional
    public ProductDetailResponse create(CreateProductRequest dto) {
        Category category = categoryService.findById(dto.getCategoryId());

        Product product = Product.builder()
                .name(dto.getName())
                .slug(makeSlug(null, slugUtil.generate(dto.getName())))
                .description(dto.getDescription())
                .category(category)
                .build();

        List<Image> images = dto.getFiles().stream()
                .map(file -> Image.builder()
                        .imageUrl(uploadUtil.save(file.getImage()))
                        .isPrimary(Boolean.TRUE.equals(file.getIsPrimary()))
                        .product(product)
                        .build())
                .collect(Collectors.toList());

        List<Variant> variants = dto.getVariants().stream()
                .map(variant -> Variant.builder()
                        .product(product)
                        .size(variant.getSize())
                        .color(variant.getColor())
                        .price(variant.getPrice())
                        .quantity(variant.getQuantity())
                        .build())
                .collect(Collectors.toList());

        product.setImages(images);
        product.setVariants(variants);
        productRepository.save(product);
        return productMapper.toResponse(product);
    }

    public ProductDetailResponse update(UUID id, UpdateProductRequest dto) {
        Product product = findById(id);
        // do something
        return productMapper.toResponse(product);
    }

    private String makeSlug(UUID id, String slug) {
        return productRepository
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
