package com.example.demo.filter;

import java.io.IOException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class HttpLogFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        filterChain.doFilter(request, response);
        String method = request.getMethod();
        Integer status = response.getStatus();
        String uri = request.getRequestURI();
        String query = request.getQueryString();
        long duration = System.currentTimeMillis() - startTime;
        String fullUri = uri + (query != null ? ("?" + query) : "");
        logger.info(method + " " + status + " " + fullUri + " - " + duration + "ms");
    }
}
