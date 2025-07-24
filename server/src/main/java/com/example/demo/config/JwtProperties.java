package com.example.demo.config;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String name = "_token";
    private JwtConfig access;
    private JwtConfig refresh;
    private String header = "Authorization";
    private String prefix = "Bearer";

    @Data
    public static class JwtConfig {
        private String secret;
        private Duration expires;
    }
}
