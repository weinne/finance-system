package com.weinne.finance_system.infrastructure.multitenancy.config;

import java.sql.Connection;
import java.sql.SQLException;

import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * SchemaPerTenantConnectionProvider is a MultiTenantConnectionProvider implementation
 * that provides connections to different schemas based on the tenant identifier.
 * 
 * This class is annotated with @Component to be managed by Spring's dependency injection.
 * It uses a DataSource to obtain connections and sets the schema for each connection
 * based on the provided tenant identifier.
 * 
 * @param dataSource the DataSource used to obtain connections
 */
@Component
public class SchemaPerTenantConnectionProvider implements MultiTenantConnectionProvider<String> {
    
    private final DataSource dataSource;

    public SchemaPerTenantConnectionProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.setSchema(tenantIdentifier);
        return connection;
    }

    // Outros métodos obrigatórios (implementação básica)
    @Override
    public Connection getAnyConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public boolean isUnwrappableAs(Class<?> unwrapType) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        return null;
    }
}