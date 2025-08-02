package com.example.demo.security.jwt;

import java.io.IOException;
import java.time.LocalDateTime;

import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.example.demo.dto.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    private final ObjectMapper mapper;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception)
            throws IOException, ServletException {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now().toString())
                .status(HttpServletResponse.SC_UNAUTHORIZED)
                .error("Unauthorized")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .traceId(MDC.get("traceId"))
                .build();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        mapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
