package com.example.demo.validator;

import org.springframework.stereotype.Component;

import com.example.demo.annotation.UniqueEmail;
import com.example.demo.repository.UserRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    private final UserRepository userRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null)
            return true;
        return !userRepository.existsByEmail(email);
    }
}
