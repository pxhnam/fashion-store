package com.example.demo.util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.stereotype.Component;

import com.example.demo.records.ClientInfo;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class UserAgentUtil {

    public ClientInfo parse(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null)
            userAgent = "Unknown";

        String ip = getClientIp(request);
        String browser = detectBrowser(userAgent);
        String device = detectDevice(userAgent);
        String location = resolveLocation(ip);

        return new ClientInfo(ip, device, browser, location, userAgent);
    }

    public String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isBlank()) {
            ip = request.getRemoteAddr();
        } else {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    public String detectBrowser(String userAgent) {
        userAgent = userAgent.toLowerCase();
        if (userAgent.contains("postman"))
            return "Postman";
        if (userAgent.contains("chrome"))
            return "Chrome";
        if (userAgent.contains("firefox"))
            return "Firefox";
        if (userAgent.contains("safari") && !userAgent.contains("chrome"))
            return "Safari";
        if (userAgent.contains("edge"))
            return "Edge";
        if (userAgent.contains("msie") || userAgent.contains("trident"))
            return "Internet Explorer";
        return "Unknown";
    }

    public String detectDevice(String userAgent) {
        String ua = userAgent.toLowerCase();
        if (ua.contains("mobile"))
            return "Mobile";
        if (ua.contains("tablet"))
            return "Tablet";
        return "Desktop";
    }

    public String resolveLocation(String ip) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://ipapi.co/" + ip + "/city/"))
                    .GET()
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            String city = response.body().trim();
            return city.isEmpty() ? "Unknown" : city;
        } catch (IOException | InterruptedException e) {
            return "Unknown";
        }
    }
}
