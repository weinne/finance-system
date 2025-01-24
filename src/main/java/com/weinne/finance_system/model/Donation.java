package com.weinne.finance_system.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "doacoes")
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

    private String category;  // Ex: "Missões", "Construção"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "church_id", nullable = false)
    private Church church;
}