package com.example.demo.mapper;

import org.springframework.stereotype.Component;

import com.example.demo.dto.request.RegisterRequest;
import com.example.demo.dto.response.LoginResponse;
import com.example.demo.dto.response.TokenResponse;
import com.example.demo.entity.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class AuthMapper {
    public User toEntity(RegisterRequest dto) {
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();
    }

    public LoginResponse toResponse(
            String name,
            String email,
            String accessToken,
            String refreshToken) {
        return LoginResponse.builder()
                .name(name)
                .email(email)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public LoginResponse toResponse(
            String name,
            String email,
            String token) {
        return LoginResponse.builder()
                .name(name)
                .email(email)
                .token(token)
                .build();
    }

    public TokenResponse toResponse(String token) {
        return TokenResponse.builder().token(token).build();
    }
}
