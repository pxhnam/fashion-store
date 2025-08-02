package com.example.demo.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Variant;

@Repository
public interface VariantRepository extends JpaRepository<Variant, UUID> {
}
