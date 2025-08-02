package com.example.demo.advice;

import java.time.LocalDateTime;

import org.slf4j.MDC;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.BaseResponse;
import com.example.demo.dto.response.ErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(@NonNull MethodParameter returnType,
            @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            @Nullable Object body,
            @NonNull MethodParameter returnType,
            @NonNull MediaType selectedContentType,
            @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response) {

        if (body instanceof BaseResponse || body instanceof ApiResponse || body instanceof ErrorResponse) {
            return body;
        }

        ApiResponse<Object> apiResponse = buildResponse(body, request, response);

        if (returnType.getParameterType().equals(String.class)) {
            try {
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                return new ObjectMapper().writeValueAsString(apiResponse);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to convert ApiResponse to JSON string", e);
            }
        }

        return apiResponse;
    }

    private ApiResponse<Object> buildResponse(Object body, ServerHttpRequest request, ServerHttpResponse response) {
        int status = HttpStatus.OK.value();
        String message = HttpStatus.OK.getReasonPhrase();
        String path = "";

        if (response instanceof ServletServerHttpResponse servletResponse) {
            status = servletResponse.getServletResponse().getStatus();
            HttpStatus httpStatus = HttpStatus.resolve(status);
            message = httpStatus != null ? httpStatus.getReasonPhrase() : "Unknown";
        }

        if (request instanceof ServletServerHttpRequest servletRequest) {
            path = servletRequest.getServletRequest().getRequestURI();
        }

        return ApiResponse.builder()
                .timestamp(LocalDateTime.now().toString())
                .path(path)
                .status(status)
                .traceId(MDC.get("traceId"))
                .message(body instanceof String ? body.toString() : message)
                .data(body instanceof String ? null : body)
                .build();
    }
}
