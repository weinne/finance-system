package com.weinne.finance_system.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "gastos")
public class Expense {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDate date;

    private String supplier;  // Ex: "Construtora ABC"

    private String category;

    @Enumerated(EnumType.STRING)
    private ApprovalStatus status = ApprovalStatus.PENDENTE;

    @Column(name = "invoice_url")
    private String invoiceUrl;  // URL do S3/Azure Blob

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "church_id", nullable = false)
    private Church church;
}
