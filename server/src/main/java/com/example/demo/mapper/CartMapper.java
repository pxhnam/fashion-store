package com.example.demo.mapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.demo.dto.response.CartResponse;
import com.example.demo.entity.Cart;
import com.example.demo.entity.Image;
import com.example.demo.entity.Product;

@Component
public class CartMapper {
    public CartResponse toResponse(Cart cart) {

        Product product = cart.getVariant().getProduct();

        String imageUrl = product.getImages().stream()
                .filter(Image::isPrimary)
                .findFirst()
                .or(() -> product.getImages().stream().findFirst())
                .map(Image::getImageUrl)
                .orElse(null);

        return CartResponse.builder()
                .id(cart.getId())
                .variantId(cart.getVariant().getId())
                .imageUrl(imageUrl)
                .name(cart.getVariant().getProduct().getName())
                .slug(cart.getVariant().getProduct().getSlug())
                .size(cart.getVariant().getSize())
                .color(cart.getVariant().getColor())
                .price(cart.getVariant().getPrice())
                .quantity(cart.getQuantity())
                .build();
    }

    public List<CartResponse> toResponseList(List<Cart> carts) {
        return carts.stream()
                .filter(Objects::nonNull)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
