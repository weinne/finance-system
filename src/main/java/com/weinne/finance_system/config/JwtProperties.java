package com.weinne.finance_system.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@ConfigurationProperties(prefix = "application.security.jwt")
@Component
@Data
public class JwtProperties {
    private String secretKey = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private long expiration = 86400000; // 1 dia
    private String prefix = "Bearer ";
    private String tenantId;
}
