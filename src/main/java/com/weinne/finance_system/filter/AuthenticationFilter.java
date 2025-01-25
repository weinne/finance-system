package com.weinne.finance_system.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weinne.finance_system.dto.LoginRequest;
import com.weinne.finance_system.security.JwtService;
import com.weinne.finance_system.repos.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) 
            throws AuthenticationException {
        try {
            LoginRequest loginRequest = new ObjectMapper()
                .readValue(request.getInputStream(), LoginRequest.class);

            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword());

            return this.getAuthenticationManager().authenticate(authRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(
        HttpServletRequest request, 
        HttpServletResponse response,
        FilterChain chain,
        Authentication auth
    ) throws IOException {
        String userEmail = auth.getName();
        String tenantId = userRepository.findByEmail(userEmail)
            .map(user -> user.getChurch().getSchemaName())
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        String token = jwtService.generateToken(auth, tenantId);
        
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json");
        response.getWriter().write(String.format(
            "{\"token\":\"%s\",\"tenantId\":\"%s\",\"message\":\"Autenticação realizada com sucesso\"}", 
            token, tenantId));
    }

    @Override
    protected void unsuccessfulAuthentication(
        HttpServletRequest request, 
        HttpServletResponse response,
        AuthenticationException failed
    ) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"Credenciais inválidas\"}");
    }
}
