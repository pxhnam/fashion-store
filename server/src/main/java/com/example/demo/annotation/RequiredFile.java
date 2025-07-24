package com.example.demo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.demo.validator.RequiredFileValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RequiredFileValidator.class)
public @interface RequiredFile {
    String message() default "File is required";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
