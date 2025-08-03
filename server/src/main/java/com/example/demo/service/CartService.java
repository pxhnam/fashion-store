package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.request.CreateCartRequest;
import com.example.demo.dto.response.CartResponse;
import com.example.demo.entity.Cart;
import com.example.demo.entity.Variant;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.CartMapper;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final VariantService variantService;
    private final CartMapper cartMapper;

    @Transactional(readOnly = true)
    public List<CartResponse> findByUserId(UUID userId) {
        return cartMapper.toResponseList(cartRepository.findByUserId(userId));
    }

    public Cart findById(UUID id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cart not found"));
    }

    @Transactional
    public void add(UUID userId, CreateCartRequest dto) {
        UUID variantId = UUID.fromString(dto.getVariantId());
        Variant variant = variantService.findById(variantId);

        if (variant.getQuantity() < dto.getQuantity()) {
            throw new BadRequestException("Not enough stock available");
        }

        Optional<Cart> cart = cartRepository.findByUserIdAndVariantId(userId, variantId);
        Cart cartEntity;
        if (cart.isPresent()) {
            cartEntity = cart.get();
            int newQuantity = cartEntity.getQuantity() + dto.getQuantity();

            if (newQuantity > variant.getQuantity()) {
                throw new BadRequestException("Not enough stock to increase quantity");
            }

            cartEntity.setQuantity(newQuantity);
        } else {
            cartEntity = Cart.builder()
                    .user(userRepository.getReferenceById(userId))
                    .variant(variant)
                    .quantity(dto.getQuantity())
                    .build();
        }

        cartRepository.save(cartEntity);

    }

    public void update(UUID userId, UUID cartId, int quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new NotFoundException("Cart item not found"));

        if (!cart.getUser().getId().equals(userId)) {
            return;
        }

        Variant variant = cart.getVariant();
        if (quantity <= 0) {
            delete(userId, cartId);
            return;
        }
        if (quantity > variant.getQuantity()) {
            throw new BadRequestException("Not enough stock available");
        }

        cart.setQuantity(quantity);
        cartRepository.save(cart);
    }

    public void delete(UUID userId, UUID cartId) {
        Cart cart = findById(cartId);
        if (cart.getUser().getId().equals(userId)) {
            cartRepository.delete(cart);
        }
    }

    @Transactional
    public void clear(UUID userId) {
        cartRepository.deleteAllByUserId(userId);
    }
}
