package com.weinne.finance_system.config;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

@Component
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver<String> {

    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenant = TenantContext.getCurrentTenant();
        return tenant != null ? tenant : "public"; // Schema padrão para operações globais
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return false; // Não valida sessões existentes (melhor performance)
    }
}