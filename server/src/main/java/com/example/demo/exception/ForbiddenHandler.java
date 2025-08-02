package com.example.demo.exception;

import java.io.IOException;
import java.time.LocalDateTime;

import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.example.demo.dto.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ForbiddenHandler implements AccessDeniedHandler {
    private final ObjectMapper mapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now().toString())
                .status(HttpServletResponse.SC_FORBIDDEN)
                .error("Forbidden")
                .message("Access Denied")
                .path(request.getRequestURI())
                .traceId(MDC.get("traceId"))
                .build();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        mapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
