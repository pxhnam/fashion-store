package com.example.demo.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Variant;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.VariantRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class VariantService {
    private final VariantRepository variantRepository;

    public Variant findById(UUID id) {
        return variantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Variant not found"));
    }

    public void deleteById(UUID id) {
        variantRepository.deleteById(id);
    }
}
