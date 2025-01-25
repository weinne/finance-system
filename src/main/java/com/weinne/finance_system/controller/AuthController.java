package com.weinne.finance_system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weinne.finance_system.service.AuthService;
import com.weinne.finance_system.dto.RegisterRequest;
import com.weinne.finance_system.dto.RegisterResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

// AuthController.java
@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
}
