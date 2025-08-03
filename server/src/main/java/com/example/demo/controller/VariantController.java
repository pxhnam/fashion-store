package com.example.demo.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.VariantService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("variants")
@RestController
public class VariantController {
    private final VariantService variantService;

    @DeleteMapping("{id}")
    public String delete(@PathVariable String id) {
        UUID uuid = UUID.fromString(id);
        variantService.deleteById(uuid);
        return "deleted";
    }
}
