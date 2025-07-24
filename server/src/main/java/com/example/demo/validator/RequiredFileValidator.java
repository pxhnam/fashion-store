package com.example.demo.validator;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.annotation.RequiredFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RequiredFileValidator implements ConstraintValidator<RequiredFile, MultipartFile> {
    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        return file != null && !file.isEmpty();
    }
}
