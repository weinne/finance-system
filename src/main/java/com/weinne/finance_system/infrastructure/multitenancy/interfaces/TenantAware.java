package com.weinne.finance_system.infrastructure.multitenancy.interfaces;

public interface TenantAware<T> {
    String getSchemaName();
    T getId();
}
