package com.example.demo.validator;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.annotation.RequiredFiles;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RequiredFileListValidator implements ConstraintValidator<RequiredFiles, List<MultipartFile>> {

    @Override
    public boolean isValid(List<MultipartFile> files, ConstraintValidatorContext context) {
        if (files == null || files.isEmpty())
            return false;
        for (MultipartFile file : files) {
            if (file == null || file.isEmpty())
                return false;
        }
        return true;
    }

}
