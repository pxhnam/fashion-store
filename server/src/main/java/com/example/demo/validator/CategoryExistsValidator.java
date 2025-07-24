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
public class CategoryExistsValidator implements ConstraintValidator<ValidCategory, UUID> {
    private final CategoryRepository categoryRepository;

    @Override
    public boolean isValid(UUID id, ConstraintValidatorContext context) {
        return id != null && categoryRepository.existsById(id);
    }
}
