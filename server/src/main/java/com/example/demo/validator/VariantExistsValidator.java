package com.example.demo.validator;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.demo.annotation.ValidVariant;
import com.example.demo.repository.VariantRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class VariantExistsValidator implements ConstraintValidator<ValidVariant, String> {
    private final VariantRepository variantRepository;

    @Override
    public boolean isValid(String id, ConstraintValidatorContext context) {
        try {
            UUID uuid = UUID.fromString(id);
            return variantRepository.existsById(uuid);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
