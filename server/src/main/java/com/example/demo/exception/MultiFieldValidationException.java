package com.example.demo.exception;

import java.util.Map;

public class MultiFieldValidationException extends RuntimeException{
    private final Map<String, String> fieldErrors;

    public MultiFieldValidationException(Map<String, String> fieldErrors) {
        super("Multiple validation errors");
        this.fieldErrors = fieldErrors;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }

}