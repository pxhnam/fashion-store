package com.example.demo.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.ImageService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("images")
@RestController
public class ImageController {
    private final ImageService imageService;

    @DeleteMapping("{id}")
    public String delete(@PathVariable UUID id) {
        imageService.deleteById(id);
        return "deleted";
    }
}
