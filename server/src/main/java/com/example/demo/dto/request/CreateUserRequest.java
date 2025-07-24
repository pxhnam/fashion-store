package com.example.demo.dto.request;

import com.example.demo.annotation.UniqueEmail;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    @NotNull
    @NotBlank
    @Size(min = 6)
    @UniqueEmail
    @Email
    private String email;

    @NotNull
    @NotBlank
    @Size(min = 8)
    private String password;
}
