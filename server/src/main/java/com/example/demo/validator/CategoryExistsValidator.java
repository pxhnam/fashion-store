package com.example.demo.validator;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.demo.annotation.ValidCategory;
import com.example.demo.repository.CategoryRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CategoryExistsValidator implements ConstraintValidator<ValidCategory, String> {
    private final CategoryRepository categoryRepository;

    @Override
    public boolean isValid(String id, ConstraintValidatorContext context) {
        try {
            UUID uuid = UUID.fromString(id);
            return categoryRepository.existsById(uuid);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
