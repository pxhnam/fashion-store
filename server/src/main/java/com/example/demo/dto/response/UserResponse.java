package com.example.demo.dto.response;

import java.time.Instant;
import java.util.UUID;

import com.example.demo.enums.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private UUID id;
    private String email;
    private UserStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}
