package com.weinne.finance_system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weinne.finance_system.model.Donation;
import com.weinne.finance_system.service.DonationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/donations")
@RequiredArgsConstructor
public class DonationController {

    private final DonationService donationService;

    @PostMapping
    public ResponseEntity<Donation> createDonation(
        @RequestHeader("X-Tenant-ID") String tenantId,
        @RequestBody Donation donation
    ) {
        return ResponseEntity.ok(donationService.createDonation(donation, tenantId));
    }

    @GetMapping
    public ResponseEntity<Iterable<Donation>> listDonations(
        @RequestHeader("X-Tenant-ID") String tenantId
    ) {
        return ResponseEntity.ok(donationService.listDonations(tenantId));
    }
}
