package com.example.sinitto.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "admin")
public record AdminProperties(
        String adminEmail,
        String adminPassword
) {
}