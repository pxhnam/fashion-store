package com.example.demo.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.demo.validator.UniqueVariantsValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueVariantsValidator.class)
@Documented
public @interface UniqueVariants {
    String message() default "Variants must have unique (size, color) combinations";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
