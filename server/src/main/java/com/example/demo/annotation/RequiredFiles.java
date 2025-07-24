package com.example.demo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.demo.validator.RequiredFileListValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RequiredFileListValidator.class)
public @interface RequiredFiles {
    String message() default "The file list must not be empty";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
