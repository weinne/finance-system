package com.weinne.finance_system.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weinne.finance_system.model.Donation;

public interface DonationRepository extends JpaRepository<Donation, Long> {
    
}
