package com.example.sinitto.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "dummy")
public record DummyProperties(
        String devRedirectUri,
        String redirectUri,
        String password
) {
}
