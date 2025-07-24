package com.example.demo.validator;

import java.util.regex.Pattern;

import com.example.demo.annotation.StrongPassword;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {

    private static final String STRONG_PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&^#\\-_=+]).{8,}$";

    private final Pattern pattern = Pattern.compile(STRONG_PASSWORD_REGEX);

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null)
            return false;
        return pattern.matcher(password).matches();
    }
}
