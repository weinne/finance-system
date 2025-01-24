package com.weinne.finance_system.service;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TenantMigrationService {

    private final DataSource dataSource;

    public void migrateTenantSchema(String schemaName) {
        Flyway flyway = Flyway.configure()
            .dataSource(dataSource)
            .schemas(schemaName)          // Schema do tenant
            .locations("classpath:db/migration/tenant")  // Migrações específicas
            .baselineOnMigrate(true)      // Cria schema se não existir
            .load();

        flyway.migrate();
    }
}