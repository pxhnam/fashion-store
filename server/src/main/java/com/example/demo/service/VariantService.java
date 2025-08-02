package com.example.demo.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.repository.VariantRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class VariantService {
    private final VariantRepository variantRepository;

    public void deleteById(UUID id) {
        variantRepository.deleteById(id);
    }
}
