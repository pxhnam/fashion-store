package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.request.RegisterRequest;
import com.example.demo.dto.response.LoginResponse;
import com.example.demo.dto.response.TokenResponse;
import com.example.demo.mapper.AuthMapper;
import com.example.demo.record.ClientInfo;
import com.example.demo.service.AuthService;
import com.example.demo.util.CookieUtil;
import com.example.demo.util.UserAgentUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("auth")
@RestController
public class AuthController {
    private final AuthService authService;
    private final CookieUtil cookieUtil;
    private final UserAgentUtil userAgentUtil;
    private final AuthMapper authMapper;

    @GetMapping("google")
    public String google() {
        return "developing";
    }

    @GetMapping("facebook")
    public String facebook() {
        return "developing";
    }

    @PostMapping("login")
    public LoginResponse authenticateUser(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse response) {
        ClientInfo client = userAgentUtil.parse(httpRequest);
        LoginResponse result = authService.login(request, client);
        cookieUtil.saveToken(response, result.getRefreshToken());
        return authMapper.toResponse(result.getName(), result.getEmail(), result.getAccessToken());
    }

    @PostMapping("register")
    public String register(
            @Valid @RequestBody RegisterRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse response) {
        authService.register(request);
        return "Registration successful! Please check your email to verify your account.";
    }

    @GetMapping("verify")
    public String verifyAccount(@RequestParam String token) {
        authService.verify(token);
        return "Account verified successfully";
    }

    @PostMapping("resend-token")
    public String resend(@RequestParam String email) {
        authService.resendToken(email);
        return "Verification email resent";
    }

    @PostMapping("refresh-token")
    public TokenResponse refreshToken(HttpServletRequest request) {
        return authService.refreshToken(request.getCookies());
    }
}
