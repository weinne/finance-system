package com.weinne.finance_system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weinne.finance_system.model.User;
import com.weinne.finance_system.repos.UserRepository;
import com.weinne.finance_system.model.Church;
import com.weinne.finance_system.dto.ChurchRegistrationDTO;
import com.weinne.finance_system.service.ChurchService;

import lombok.RequiredArgsConstructor;

// AuthController.java
@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final ChurchService churchService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody ChurchRegistrationDTO registration) {
        // 1. Criar a igreja
        Church church = new Church();
        church.setName(registration.getChurchName());
        church.setCnpj(registration.getCnpj());
        church.setAddress(registration.getAddress());
        church.setSchemaName("church_" + registration.getCnpj().replaceAll("[^0-9]", ""));
        
        Church savedChurch = churchService.createChurch(church);

        // 2. Criar o usuário
        User user = new User();
        user.setEmail(registration.getEmail());
        user.setPassword(passwordEncoder.encode(registration.getPassword()));
        user.setRole(registration.getRole());
        user.setChurch(savedChurch);

        return ResponseEntity.ok(userRepository.save(user));
    }
}
