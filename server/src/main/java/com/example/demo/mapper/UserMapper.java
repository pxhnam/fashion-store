package com.example.demo.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.example.demo.dto.request.CreateUserRequest;
import com.example.demo.dto.request.UpdateUserRequest;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class UserMapper {
    private final PasswordEncoder encoder;

    public List<UserResponse> toResponseList(List<User> users) {
        return users.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public User toEntity(CreateUserRequest dto) {
        return User.builder()
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .build();
    }

    public void toEntity(User user, UpdateUserRequest dto) {
        if (StringUtils.hasText(dto.getEmail())) {
            user.setEmail(dto.getEmail());
        }
    }

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
