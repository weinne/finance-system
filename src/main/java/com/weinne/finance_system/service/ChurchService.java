package com.weinne.finance_system.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weinne.finance_system.model.Church;
import com.weinne.finance_system.repos.ChurchRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChurchService {

    private final ChurchRepository churchRepository;
    private final TenantMigrationService tenantMigrationService;

    @Transactional
    public Church createChurch(Church church) {
        if (churchRepository.existsBySchemaName(church.getSchemaName())) {
            throw new IllegalArgumentException("Schema já existe");
        }

        Church savedChurch = churchRepository.save(church);
        
        // Cria schema e aplica migrações
        tenantMigrationService.migrateTenantSchema(church.getSchemaName());
        
        return savedChurch;
    }
}