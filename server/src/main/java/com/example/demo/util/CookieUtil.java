package com.example.demo.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import com.example.demo.config.JwtProperties;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CookieUtil {
    private final JwtProperties jwtProps;

    public void saveToken(HttpServletResponse response, String token) {
        ResponseCookie accessCookie = ResponseCookie.from(jwtProps.getName(), token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("None")
                .maxAge(jwtProps.getRefresh().getExpires())
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
    }

    public void clearToken(HttpServletResponse response) {
        ResponseCookie accessCookie = ResponseCookie.from(jwtProps.getName(), "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("None")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
    }
}
