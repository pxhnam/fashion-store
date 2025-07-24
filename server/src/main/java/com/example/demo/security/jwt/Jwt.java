package com.example.demo.security.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.demo.config.JwtProperties;
import com.example.demo.enums.EToken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class Jwt {
    private final JwtProperties jwt;

    private JwtProperties.JwtConfig getConfigByType(EToken type) {
        return switch (type) {
            case ACCESS -> jwt.getAccess();
            case REFRESH -> jwt.getRefresh();
        };
    }

    public String getCookieName() {
        return jwt.getName();
    }

    public String getHeader() {
        return jwt.getHeader();
    }

    public String getPrefix() {
        return jwt.getPrefix();
    }

    private Key getSigningKey(String secret) {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    private Claims extract(String secret, String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(secret))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generate(UUID id, EToken type) {
        var config = getConfigByType(type);
        Instant now = Instant.now();
        Instant exp = now.plus(config.getExpires());
        return Jwts.builder()
                .setSubject(id.toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(getSigningKey(config.getSecret()), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generate(UUID id) {
        return generate(id, EToken.ACCESS);
    }

    public UUID getSubject(String token, EToken type) {
        var config = getConfigByType(type);
        String subject = extract(config.getSecret(), token).getSubject();
        return UUID.fromString(subject);
    }

    public UUID getSubject(String token) {
        return getSubject(token, EToken.ACCESS);
    }

    public boolean validate(String token, EToken type) {
        try {
            var config = getConfigByType(type);
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey(config.getSecret()))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public boolean validate(String token) {
        return validate(token, EToken.ACCESS);
    }
}
