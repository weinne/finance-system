package com.weinne.finance_system.infrastructure.multitenancy.schema;

import java.time.LocalDateTime;
import java.lang.reflect.Field;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weinne.finance_system.exception.TenantCreationException;
import com.weinne.finance_system.infrastructure.multitenancy.annotation.TenantClassEntity;
import com.weinne.finance_system.model.SchemaChangeLog;
import com.weinne.finance_system.model.Church;
import com.weinne.finance_system.repos.SchemaChangeLogRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for managing tenant schemas in the finance system.
 * This service provides methods to create and drop schemas for different tenants (churches).
 * 
 * Dependencies:
 * - {@link SchemaChangeLogRepository} - Repository for logging schema changes.
 * - {@link MultiTenantSchemaManager} - Utility for generating schemas and tables using Hibernate.
 * 
 * Methods:
 * - {@link #createTenantSchema(Church)} - Creates a new schema for the given church, logs the operation, and configures permissions.
 * - {@link #dropTenantSchema(String)} - Drops the schema with the given name.
 * 
 * Usage:
 * {@code
 * TenantMigrationService tenantMigrationService = new TenantMigrationService(dataSource, schemaChangeLogRepository, schemaGenerator);
 * tenantMigrationService.createTenantSchema(church);
 * tenantMigrationService.dropTenantSchema(schemaName);
 * }
 * 
 * Annotations:
 * - {@link Slf4j} - For logging.
 * - {@link Service} - Marks this class as a Spring service.
 * - {@link RequiredArgsConstructor} - Generates a constructor with required arguments.
 * - {@link Transactional} - Ensures methods are executed within a transaction.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TenantSchemaService {

    private final SchemaChangeLogRepository schemaChangeLogRepository;
    private final MultiTenantSchemaManager tenantSchemaGenerator;

    public String getSchemaName(Object tenantObject) {
    TenantClassEntity annotation = tenantObject.getClass().getAnnotation(TenantClassEntity.class);
    if (annotation != null) {
        try {
            Field field = tenantObject.getClass().getDeclaredField(annotation.schemaNameField());
            field.setAccessible(true);
            return (String) field.get(tenantObject);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao obter schema name", e);
        }
    }
    throw new IllegalArgumentException("Objeto não é uma tenant válida");
}

    @Transactional
    public void createTenantSchema(Object tenant) {
        if (tenant == null) {
            throw new IllegalArgumentException("Tenant não pode ser nulo");
        }

        try {
            String schemaName = getSchemaName(tenant);

            tenantSchemaGenerator.createSchema(schemaName);
            
            SchemaChangeLog schemaLog = new SchemaChangeLog();
            schemaLog.setSchemaName(schemaName);
            schemaLog.setOperation("CREATE");
            schemaLog.setCreatedAt(LocalDateTime.now());
            schemaLog.setStatus("SUCCESS");
            schemaChangeLogRepository.save(schemaLog);
            
            log.info("Schema {} criado com sucesso", schemaName);
        } catch (Exception e) {
            log.error("Erro ao criar schema do tenant: {}", e.getMessage());
            throw new TenantCreationException("Falha ao criar schema do tenant", e);
        }
    }

    @Transactional
    public void dropTenantSchema(String schemaName) {
        if (schemaName == null || schemaName.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do schema é obrigatório");
        }

        try {
            tenantSchemaGenerator.dropSchema(schemaName);

            SchemaChangeLog schemaLog = new SchemaChangeLog();
            schemaLog.setSchemaName(schemaName);
            schemaLog.setOperation("DROP");
            schemaLog.setCreatedAt(LocalDateTime.now());
            schemaLog.setStatus("SUCCESS");
            schemaChangeLogRepository.save(schemaLog);

            log.info("Schema {} removido com sucesso", schemaName);

        } catch (Exception e) {
            log.error("Erro ao remover schema do tenant: {}", e.getMessage());
            throw new TenantCreationException("Falha ao remover schema do tenant", e);
        }
    }
}