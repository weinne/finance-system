package com.weinne.finance_system.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.weinne.finance_system.infrastructure.multitenancy.annotation.TenantDependent;

@Data
@Entity
@Table
@TenantDependent
public class Expense {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDate date;

    private String supplier;  // Ex: "Construtora ABC"

    @Enumerated(EnumType.STRING)
    private ExpenseCategory category;

    @Enumerated(EnumType.STRING)
    private ApprovalStatus status = ApprovalStatus.PENDENTE;

    @Lob
    @Column(columnDefinition = "BYTEA")
    private byte[] attachment;

    public void setAttachment(byte[] attachment) {
        if (attachment != null && attachment.length > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("Attachment size exceeds the maximum limit of 5MB");
        }
        this.attachment = attachment;
    }
}
