package com.example.demo.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
    List<Cart> findByUserId(UUID id);

    Optional<Cart> findByUserIdAndVariantId(UUID userId, UUID variantId);

    void deleteByUserIdAndVariantId(UUID userId, UUID variantId);

    void deleteAllByUserId(UUID userId);

}
