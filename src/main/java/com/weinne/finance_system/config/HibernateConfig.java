package com.weinne.finance_system.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.weinne.finance_system.infrastructure.multitenancy.config.SchemaPerTenantConnectionProvider;

/**
 * Configuration class for setting up Hibernate with multi-tenancy support.
 * This class configures the EntityManagerFactory and initializes the database schema.
 *
 * Annotations:
 * - @Configuration: Indicates that this class contains Spring configuration.
 * - @EnableTransactionManagement: Enables Spring's annotation-driven transaction management.
 * - @EntityScan: Specifies the packages to scan for JPA entities.
 * - @EnableJpaRepositories: Enables JPA repositories and specifies the packages to scan for repository interfaces.
 *
 * Methods:
 * - entityManagerFactory(DataSource dataSource, SchemaPerTenantConnectionProvider schemaPerTenantConnectionProvider, CurrentTenantIdentifierResolver<String> tenantResolver):
 *   Configures the LocalContainerEntityManagerFactoryBean with multi-tenancy support and Hibernate properties.
 *   - @param dataSource: The DataSource to be used by the EntityManagerFactory.
 *   - @param schemaPerTenantConnectionProvider: The connection provider for multi-tenancy.
 *   - @param tenantResolver: The tenant identifier resolver.
 *   - @return LocalContainerEntityManagerFactoryBean: The configured EntityManagerFactory bean.
 *
 * - initDatabase(DataSource dataSource):
 *   Initializes the database schema by creating the "public" schema if it does not exist.
 *   - @param dataSource: The DataSource to be used for executing the schema creation.
 *   - @PostConstruct: Indicates that this method should be executed after the bean's initialization.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "com.weinne.finance_system.repos",
    entityManagerFactoryRef = "entityManagerFactory",
    transactionManagerRef = "transactionManager"
)
@EntityScan("com.weinne.finance_system.model")
public class HibernateConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
        DataSource dataSource,
        SchemaPerTenantConnectionProvider schemaPerTenantConnectionProvider,
        CurrentTenantIdentifierResolver<String> tenantResolver) {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.weinne.finance_system.model");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true); // Habilita geração de DDL
        vendorAdapter.setShowSql(true);     // Mostra SQL no console
        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.multiTenancy", "SCHEMA");
        properties.put("hibernate.multi_tenant_connection_provider", schemaPerTenantConnectionProvider);
        properties.put("hibernate.tenant_identifier_resolver", tenantResolver);
        properties.put("hibernate.hbm2ddl.auto", "update"); // Adiciona essa linha
        properties.put("hibernate.default_schema", "public"); // Define schema padrão

        em.setJpaPropertyMap(properties);
        return em;
    }

    @Primary
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    @PostConstruct
    public void initDatabase() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS public");
    }
}
