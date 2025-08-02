package com.example.demo.validator;

import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.annotation.FileValidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class FileValidator implements ConstraintValidator<FileValidation, MultipartFile> {
    private long maxSize;
    private Set<String> allowedTypes;

    @Override
    public void initialize(FileValidation annotation) {
        this.maxSize = annotation.maxSize();
        this.allowedTypes = Set.of(annotation.allowedTypes());
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return true;
        }

        if (file.getSize() > maxSize) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    String.format("File size %d bytes exceeds maximum %d bytes",
                            file.getSize(), maxSize))
                    .addConstraintViolation();
            return false;
        }

        if (!allowedTypes.isEmpty() && !allowedTypes.contains(file.getContentType())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    String.format("File type %s not allowed. Allowed types: %s",
                            file.getContentType(), allowedTypes))
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
