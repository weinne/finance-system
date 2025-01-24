package com.weinne.finance_system.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weinne.finance_system.config.TenantContext;
import com.weinne.finance_system.model.Donation;
import com.weinne.finance_system.repos.DonationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final DonationRepository donationRepository;

    @Transactional
	public Donation createDonation(Donation donation, String tenantId) {
        // Set the tenant context
        TenantContext.setCurrentTenant(tenantId);

        // Save the donation
        Donation savedDonation = donationRepository.save(donation);

        // Clear the tenant context
        TenantContext.clear();

        return savedDonation;
	}

    @Transactional(readOnly = true)
    public Iterable<Donation> listDonations(String tenantId) {
        // Set the tenant context
        TenantContext.setCurrentTenant(tenantId);

        // List the donations
        Iterable<Donation> donations = donationRepository.findAll();

        // Clear the tenant context
        TenantContext.clear();

        return donations;
    }

}
