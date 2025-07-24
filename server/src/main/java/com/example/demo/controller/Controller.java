package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.annotation.CurrentUser;
import com.example.demo.security.UserPrincipal;

@RestController
public class Controller {
    @GetMapping("me")
    public ResponseEntity<?> getCurrentUser(@CurrentUser UserPrincipal user) {
        return ResponseEntity.ok(user);
    }
}
