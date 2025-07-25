package com.example.demo.record;

public record ClientInfo(
        String ip,
        String device,
        String browser,
        String location,
        String userAgent) {
}
