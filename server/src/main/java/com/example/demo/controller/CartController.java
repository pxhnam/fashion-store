package com.example.demo.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.annotation.CurrentUser;
import com.example.demo.dto.request.CreateCartRequest;
import com.example.demo.dto.response.CartResponse;
import com.example.demo.security.UserPrincipal;
import com.example.demo.service.CartService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("carts")
@RestController
public class CartController {
    private final CartService cartService;

    @GetMapping
    public List<CartResponse> index(@CurrentUser UserPrincipal user) {
        return cartService.findByUserId(user.getId());
    }

    @PostMapping
    public String add(@CurrentUser UserPrincipal user, @Valid @RequestBody CreateCartRequest request) {
        cartService.add(user.getId(), request);
        return "Cart item added successfully";
    }

    @PutMapping("{id}")
    public String update(@CurrentUser UserPrincipal user,
            @PathVariable UUID id,
            @RequestParam Integer quantity) {
        cartService.update(user.getId(), id, quantity);
        return "Cart item updated successfully";
    }

    @DeleteMapping("{id}")
    public String delete(@CurrentUser UserPrincipal user, @PathVariable UUID id) {
        cartService.delete(user.getId(), id);
        return "Cart item deleted successfully";
    }

    @DeleteMapping
    public String clear(@CurrentUser UserPrincipal user) {
        cartService.clear(user.getId());
        return "Cart cleared successfully";
    }
}
