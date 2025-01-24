package com.weinne.finance_system.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.config.Customizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.weinne.finance_system.model.User;
import com.weinne.finance_system.repos.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Permite acesso público aos endpoints necessários
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/api/churches").hasRole("ADMIN")
                .requestMatchers("/api/donations").hasAnyRole("TESOUREIRO", "ADMIN")
                .requestMatchers("/api/expenses").hasRole("TESOUREIRO")
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults()) // Autenticação básica
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

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
            return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
            );
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}