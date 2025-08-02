package com.example.demo.validator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.example.demo.annotation.UniqueVariants;
import com.example.demo.dto.request.CreateVariantRequest;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class UniqueVariantsValidator implements ConstraintValidator<UniqueVariants, List<CreateVariantRequest>> {
    @Override
    public boolean isValid(List<CreateVariantRequest> variants, ConstraintValidatorContext context) {
        if (variants == null || variants.isEmpty()) {
            return true;
        }

        Set<String> seen = new HashSet<>();
        for (CreateVariantRequest variant : variants) {
            String key = variant.getSize().toLowerCase().trim() + "_" + variant.getColor().toLowerCase().trim();
            if (!seen.add(key)) {
                return false;
            }
        }
        return true;
    }
}
