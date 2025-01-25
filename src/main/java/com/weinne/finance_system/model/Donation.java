package com.weinne.finance_system.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.weinne.finance_system.infrastructure.multitenancy.annotation.TenantDependent;

@Data
@Entity
@Table(name = "doacoes")
@TenantDependent
public class Donation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;  // Enum: PIX, DINHEIRO, CARTAO

    @Enumerated(EnumType.STRING)
    private DonationCategory category;  // Ex: "Missões", "Construção"
}