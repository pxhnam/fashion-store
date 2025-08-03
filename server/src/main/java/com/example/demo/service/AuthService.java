package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.request.RegisterRequest;
import com.example.demo.dto.response.LoginResponse;
import com.example.demo.dto.response.TokenResponse;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.enums.ERole;
import com.example.demo.enums.EToken;
import com.example.demo.enums.UserStatus;
import com.example.demo.exception.BadRequestException;
import com.example.demo.mapper.AuthMapper;
import com.example.demo.record.ClientInfo;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.jwt.Jwt;
import com.example.demo.util.MailUtil;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class AuthService {
    private final AuthenticationManager manager;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final RoleService roleService;
    private final Jwt jwt;
    private final PasswordEncoder encoder;
    private final MailUtil mailUtil;
    private final AuthMapper authMapper;

    public LoginResponse login(LoginRequest dto, ClientInfo client) {
        manager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BadCredentialsException(""));
        String errorMessage = getErrorMessageByStatus(user.getStatus());
        if (errorMessage != null) {
            throw new BadRequestException(errorMessage);
        }

        String accessToken = jwt.generate(user.getId());
        String refreshToken = jwt.generate(user.getId(), EToken.REFRESH);
        tokenService.create(user, refreshToken, client);
        return authMapper.toResponse(user.getName(), user.getEmail(), accessToken, refreshToken);
    }

    public void register(RegisterRequest dto) {
        User user = authMapper.toEntity(dto);
        user.setPassword(encoder.encode(user.getPassword()));
        Role userRole = roleService.findByName(ERole.USER);
        user.setRoles(userRole);

        user.setVerifyToken(genToken());
        user.setTokenExp(genTokenExp());
        sendVerificationEmail(user.getEmail(), user.getVerifyToken());

        userRepository.save(user);
    }

    public void verify(String token) {
        User user = userRepository.findByVerifyToken(token)
                .orElseThrow(() -> new BadRequestException("Invalid verification token"));

        if (user.getVerifiedAt() != null) {
            throw new BadRequestException("Account is already verified");
        }

        if (user.getTokenExp() == null || user.getTokenExp().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Verification token has expired");
        }

        user.setStatus(UserStatus.ACTIVE);
        user.setVerifiedAt(LocalDateTime.now());
        user.setVerifyToken(null);
        user.setTokenExp(null);

        userRepository.save(user);
    }

    public void resendToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Account with this email does not exist"));

        if (user.getVerifiedAt() != null) {
            throw new BadRequestException("Account is already verified");
        }

        user.setVerifyToken(genToken());
        user.setTokenExp(genTokenExp());
        userRepository.save(user);

        sendVerificationEmail(user.getEmail(), user.getVerifyToken());
    }

    public TokenResponse refreshToken(Cookie[] cookies) {
        if (cookies == null || cookies.length == 0) {
            throw new BadCredentialsException("");
        }
        String accessToken = Arrays.stream(cookies)
                .filter(cookie -> jwt.getCookieName().equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .map(token -> {
                    if (!tokenService.validate(token)) {
                        throw new BadCredentialsException("");
                    }
                    UUID id = jwt.getSubject(token, EToken.REFRESH);
                    return jwt.generate(id);
                })
                .orElseThrow(() -> new BadCredentialsException(""));

        return authMapper.toResponse(accessToken);
    }

    private String genToken() {
        return UUID.randomUUID().toString();
    }

    private LocalDateTime genTokenExp() {
        return LocalDateTime.now().plusMinutes(15);
    }

    private void sendVerificationEmail(String email, String token) {
        String verifyLink = "http://localhost:3000/api/auth/verify?token=" + token;
        String html = "<h1>Confirm your email address</h1><p>Click <a href=\"" + verifyLink
                + "\">here</a> to activate your account.</p>";
        mailUtil.send("Verify your account", email, html);
    }

    private String getErrorMessageByStatus(UserStatus status) {
        return switch (status) {
            case PENDING -> "Your account has not been verified yet.";
            case DISABLED -> "Your account has been disabled. Please contact support.";
            case REMOVED -> "This account has been removed.";
            default -> null;
        };
    }
}
