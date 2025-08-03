package com.example.demo.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.request.CreateUserRequest;
import com.example.demo.dto.request.UpdateUserRequest;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.User;
import com.example.demo.exception.ConflictException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserResponse> get() {
        return userMapper.toResponseList(userRepository.findAll());
    }

    private User findByIdOrThrow(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + id));
    }

    private UserResponse save(User user) {
        return userMapper.toResponse(userRepository.save(user));
    }

    public UserResponse getById(UUID id) {
        User user = findByIdOrThrow(id);
        return userMapper.toResponse(user);
    }

    public UserResponse getByEmail(String email) {
        return userRepository.findByEmail(email).map(user -> userMapper.toResponse(user))
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));
    }

    public UserResponse create(CreateUserRequest request) {
        User user = userMapper.toEntity(request);
        return save(user);
    }

    public UserResponse update(UUID id, UpdateUserRequest request) {
        User user = findByIdOrThrow(id);
        userRepository.findByEmail(request.getEmail())
                .ifPresent(u -> {
                    if (!u.getId().equals(id)) {
                        throw new ConflictException("Username đã tồn tại: " + u.getEmail());
                    }
                });
        userMapper.toEntity(user, request);
        return save(user);
    }

    public UserResponse delete(UUID id) {
        User user = findByIdOrThrow(id);
        userRepository.delete(user);
        return userMapper.toResponse(user);
    }
}
