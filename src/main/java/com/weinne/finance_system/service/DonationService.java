package com.weinne.finance_system.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weinne.finance_system.exception.ResourceNotFoundException;
import com.weinne.finance_system.model.Donation;
import com.weinne.finance_system.repos.DonationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final DonationRepository donationRepository;

    @Transactional
    public Donation createDonation(Donation donation) {
        return donationRepository.save(donation);
    }

    @Transactional(readOnly = true)
    public List<Donation> listDonations() {
        return donationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Donation getDonationById(Long id) {
        return donationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Doação não encontrada"));
    }

    @Transactional
    public void deleteDonation(Long id) {
        donationRepository.deleteById(id);
    }

}
