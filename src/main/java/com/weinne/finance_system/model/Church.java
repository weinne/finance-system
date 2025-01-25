package com.weinne.finance_system.model;

import com.weinne.finance_system.infrastructure.multitenancy.annotation.TenantClassEntity;
import com.weinne.finance_system.infrastructure.multitenancy.interfaces.TenantAware;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(schema="public")
@Data
@TenantClassEntity(schemaNameField = "schemaName", tenantIdField = "id")
public class Church implements TenantAware<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String cnpj;

    private String address;

    @Column(nullable = false, unique = true)
    private String schemaName;
}