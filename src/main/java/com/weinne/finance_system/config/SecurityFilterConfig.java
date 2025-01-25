package com.weinne.finance_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.weinne.finance_system.filter.AuthenticationFilter;
import com.weinne.finance_system.filter.JwtAuthFilter;
import com.weinne.finance_system.security.JwtService;

@Configuration
public class SecurityFilterConfig {



    @Bean 
    public JwtAuthFilter jwtAuthFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        return new JwtAuthFilter(jwtService, userDetailsService);
    }

    @Bean
    public AuthenticationFilter authenticationFilter(
        JwtService jwtService,
        AuthenticationManager authenticationManager
    ) {
        AuthenticationFilter filter = new AuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager);
        filter.setFilterProcessesUrl("/api/auth/login");
        return filter;
    }
}
