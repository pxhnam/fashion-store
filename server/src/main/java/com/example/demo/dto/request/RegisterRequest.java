package com.example.demo.dto.request;

import com.example.demo.annotation.StrongPassword;
import com.example.demo.annotation.UniqueEmail;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    @Email
    @UniqueEmail
    private String email;

    @NotNull
    @NotBlank
    @StrongPassword
    private String password;
}
