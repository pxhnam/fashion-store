package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.dto.request.CreateProductRequest;
import com.example.demo.dto.request.UpdateImageRequest;
import com.example.demo.dto.request.UpdateProductRequest;
import com.example.demo.dto.request.UpdateVariantRequest;
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
        UUID uuid = UUID.fromString(dto.getCategoryId());
        Category category = categoryService.findById(uuid);

        Product product = Product.builder()
                .name(dto.getName())
                .slug(makeSlug(null, slugUtil.generate(dto.getName())))
                .description(dto.getDescription())
                .category(category)
                .build();

        List<Image> images = dto.getImages().stream()
                .map(image -> Image.builder()
                        .imageUrl(uploadUtil.save(image.getFile()))
                        .isPrimary(Boolean.TRUE.equals(image.getIsPrimary()))
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

        ensurePrimaryImage(images);
        product.setImages(images);
        product.setVariants(variants);
        productRepository.save(product);
        return productMapper.toResponse(product);
    }

    @Transactional
    public ProductDetailResponse update(UUID id, UpdateProductRequest dto) {
        Product product = findById(id);
        if (!dto.getName().equals(product.getName())) {
            product.setName(dto.getName());
            String slug = makeSlug(id, slugUtil.generate(dto.getName()));
            product.setSlug(slug);
        }
        product.setDescription(dto.getDescription());
        product.setStatus(dto.getStatus());
        UUID newCategoryId = UUID.fromString(dto.getCategoryId());
        if (!newCategoryId.equals(product.getCategory().getId())) {
            Category category = categoryService.findById(newCategoryId);
            product.setCategory(category);
        }
        updateImages(product, dto.getImages());
        updateVariants(product, dto.getVariants());
        productRepository.save(product);
        return productMapper.toResponse(product);
    }

    private void updateImages(Product product, List<UpdateImageRequest> imagesDTO) {
        Map<UUID, Image> currentImages = product.getImages().stream()
                .collect(Collectors.toMap(Image::getId, img -> img));

        List<Image> updatedImages = new ArrayList<>();

        for (UpdateImageRequest dto : imagesDTO) {
            Image image;
            UUID uuid = parseUUID(dto.getId());
            if (uuid != null && currentImages.containsKey(uuid)) {
                image = currentImages.get(uuid);
                if (dto.getFile() != null && !dto.getFile().isEmpty()) {
                    String newUrl = uploadUtil.save(dto.getFile());
                    image.setImageUrl(newUrl);
                } else if (dto.getImageUrl() != null) {
                    image.setImageUrl(dto.getImageUrl());
                }
            } else {
                image = new Image();
                image.setProduct(product);
                if (dto.getFile() != null && !dto.getFile().isEmpty()) {
                    String url = uploadUtil.save(dto.getFile());
                    image.setImageUrl(url);
                } else {
                    image.setImageUrl(dto.getImageUrl());
                }
            }

            image.setPrimary(Boolean.TRUE.equals(dto.getIsPrimary()));
            updatedImages.add(image);
        }

        ensurePrimaryImage(updatedImages);
        product.getImages().clear();
        product.getImages().addAll(updatedImages);
    }

    private void updateVariants(Product product, List<UpdateVariantRequest> variantsDTO) {
        Map<UUID, Variant> currentVariants = product.getVariants().stream()
                .collect(Collectors.toMap(Variant::getId, v -> v));

        List<Variant> updatedVariants = new ArrayList<>();

        for (UpdateVariantRequest dto : variantsDTO) {
            Variant variant;
            UUID uuid = parseUUID(dto.getId());
            if (uuid != null && currentVariants.containsKey(uuid)) {
                variant = currentVariants.get(uuid);
            } else {
                variant = new Variant();
                variant.setProduct(product);
            }

            variant.setSize(dto.getSize());
            variant.setColor(dto.getColor());
            variant.setPrice(dto.getPrice());
            variant.setQuantity(dto.getQuantity());

            updatedVariants.add(variant);
        }

        product.getVariants().clear();
        product.getVariants().addAll(updatedVariants);
    }

    private void ensurePrimaryImage(List<Image> images) {
        if (images.isEmpty()) {
            return;
        }

        boolean hasPrimary = images.stream().anyMatch(Image::isPrimary);
        if (!hasPrimary) {
            images.get(0).setPrimary(true);
        } else {
            boolean foundFirst = false;
            for (Image image : images) {
                if (image.isPrimary()) {
                    if (foundFirst) {
                        image.setPrimary(false);
                    } else {
                        foundFirst = true;
                    }
                }
            }
        }
    }

    private UUID parseUUID(String uuidString) {
        if (uuidString == null || uuidString.trim().isEmpty()) {
            return null;
        }
        try {
            return UUID.fromString(uuidString);
        } catch (IllegalArgumentException e) {
            return null;
        }
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
