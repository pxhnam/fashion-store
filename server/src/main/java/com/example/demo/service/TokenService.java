package com.example.demo.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Token;
import com.example.demo.entity.User;
import com.example.demo.enums.EToken;
import com.example.demo.records.ClientInfo;
import com.example.demo.repository.TokenRepository;
import com.example.demo.security.jwt.Jwt;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final TokenRepository tokenRepository;
    private final Jwt jwt;

    public String hash(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    public boolean compare(String raw, String hashed) {
        return hash(raw).equals(hashed);
    }

    public boolean validate(String token) {
        try {
            if (token == null || token.trim().isEmpty())
                return false;
            String hashedToken = hash(token);
            boolean exists = tokenRepository.findByToken(hashedToken).isPresent();
            if (!exists)
                return false;
            return jwt.validate(token, EToken.REFRESH);
        } catch (Exception e) {
            return false;
        }
    }

    public void create(User user, String token, ClientInfo client) {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("Token must not be null or empty");
        }
        String hashedToken = hash(token);
        Token tokenEntity = Token.builder()
                .user(user)
                .token(hashedToken)
                .ip(client.ip())
                .device(client.device())
                .browser(client.browser())
                .location(client.location())
                .userAgent(client.userAgent())
                .build();
        tokenRepository.save(tokenEntity);
    }

    public void delete(String token) {
        String hashedToken = hash(token);
        tokenRepository.deleteByToken(hashedToken);
    }

}
