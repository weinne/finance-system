package com.weinne.finance_system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weinne.finance_system.model.Church;
import com.weinne.finance_system.service.ChurchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/churches")
@RequiredArgsConstructor
public class ChurchController {

    private final ChurchService churchService;

    @PostMapping
    public ResponseEntity<Church> createChurch(@RequestBody Church church) {
        return ResponseEntity.ok(churchService.createChurch(church));
    }
}