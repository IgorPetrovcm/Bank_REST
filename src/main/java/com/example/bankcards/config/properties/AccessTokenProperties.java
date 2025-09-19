package com.example.bankcards.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.security.access-token")
@Getter
@Setter
public class AccessTokenProperties {
    private String secret;
    private int expirationInMinutes;
}