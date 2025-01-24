package com.weinne.finance_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Permite acesso público aos endpoints necessários
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, "/api/churches").permitAll() // Permite POST sem autenticação
                .anyRequest().authenticated() // Demais endpoints exigem autenticação
            )
            // Desabilita CSRF e CORS para APIs REST
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.disable())
            // Desabilita proteções extras (opcional para desenvolvimento)
            .headers(headers -> headers
                .frameOptions(frame -> frame.disable())
                .xssProtection(xss -> xss.disable())
            );

        return http.build();
    }
}