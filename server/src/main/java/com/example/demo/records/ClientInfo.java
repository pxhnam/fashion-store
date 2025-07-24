package com.example.demo.records;

public record ClientInfo(
        String ip,
        String device,
        String browser,
        String location,
        String userAgent) {
}
