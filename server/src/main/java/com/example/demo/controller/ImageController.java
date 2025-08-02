package com.example.demo.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.BadRequestException;
import com.example.demo.service.ImageService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("images")
@RestController
public class ImageController {
    private final ImageService imageService;

    @DeleteMapping("{id}")
    public String delete(@PathVariable String id) {
        try {
            UUID uuid = UUID.fromString(id);
            imageService.deleteById(uuid);
            return "deleted";
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid UUID format: " + id);
        }
    }
}
