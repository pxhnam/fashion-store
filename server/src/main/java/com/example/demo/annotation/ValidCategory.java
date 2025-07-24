package com.example.demo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.demo.validator.CategoryExistsValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CategoryExistsValidator.class)
public @interface ValidCategory {
    String message() default "Invalid category ID";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
