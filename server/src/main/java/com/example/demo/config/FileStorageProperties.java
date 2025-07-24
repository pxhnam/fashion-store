package com.example.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "file")
@Data
public class FileStorageProperties {
    private String uploadDir = "uploads";

    public String getUrlPattern() {
        return "/" + uploadDir + "/**";
    }

    public String getFileSystemPath() {
        return "file:" + uploadDir + "/";
    }
}
