package com.weinne.finance_system.infrastructure.multitenancy.schema;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.MetadataSources;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.springframework.stereotype.Component;

import com.weinne.finance_system.infrastructure.multitenancy.annotation.TenantDependent;

import org.springframework.jdbc.core.JdbcTemplate;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.dialect.Dialect;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.EnumSet;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class MultiTenantSchemaManager {
    
    private final EntityManagerFactory entityManagerFactory;
    private final JdbcTemplate jdbcTemplate;

    // Método para obter o dialeto do Hibernate
    public Dialect getDialect() {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            SessionFactoryImplementor sessionFactory = em.unwrap(Session.class)
                .getSessionFactory()
                .unwrap(SessionFactoryImplementor.class);
            return sessionFactory.getJdbcServices().getDialect();
        } finally {
            em.close();
        }
    }
    
    public void generateSchema(String schemaName) {
        log.info("Gerando schema para tenant: {}", schemaName);
        
        EntityManager em = entityManagerFactory.createEntityManager();
        Session session = em.unwrap(Session.class);
        
        try {
            SessionFactoryImplementor sessionFactory = session.getSessionFactory()
                .unwrap(SessionFactoryImplementor.class);
                
            // Cria um novo StandardServiceRegistry
            StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();
            
            // Copia as propriedades do SessionFactory atual
            Map<String, Object> props = sessionFactory.getProperties();
            registryBuilder.applySettings(props);
            
            StandardServiceRegistry standardRegistry = registryBuilder.build();
            
            // Configura MetadataSources com o StandardServiceRegistry
            MetadataSources metadataSources = new MetadataSources(standardRegistry);
            
            // Adiciona apenas as entidades marcadas com @TenantEntity
            for (EntityType<?> entity : em.getMetamodel().getEntities()) {
                Class<?> javaType = entity.getJavaType();
                if (javaType.isAnnotationPresent(TenantDependent.class)) {
                    metadataSources.addAnnotatedClass(javaType);
                    log.debug("Adicionando entidade {} ao schema do tenant", javaType.getSimpleName());
                }
            }
            
            // Configura o schema
            MetadataBuilder metadataBuilder = metadataSources.getMetadataBuilder();
            Metadata metadata = metadataBuilder.build();
            
            // Gera o schema
            SchemaExport schemaExport = new SchemaExport()
                .setOutputFile("schema-" + schemaName + ".sql")
                .setDelimiter(";")
                .setFormat(true);
                
            schemaExport.create(EnumSet.of(TargetType.DATABASE), metadata);
            
            log.info("Schema {} gerado com sucesso", schemaName);
            
        } catch (Exception e) {
            log.error("Erro ao gerar schema {}: {}", schemaName, e.getMessage());
            throw new RuntimeException("Falha ao gerar schema do tenant", e);
        } finally {
            em.close();
        }
    }

    public void createSchema(String schemaName) {
        if (schemaName == null || schemaName.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do schema é obrigatório");
        }

        String[] createCommands = getDialect().getCreateSchemaCommand(schemaName);
        for (String createCommand : createCommands) {
            jdbcTemplate.execute(createCommand);
        }
        generateSchema(schemaName);
    }

    public void dropSchema(String schemaName) {
        if (schemaName == null || schemaName.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do schema é obrigatório");
        }

        String[] dropCommands = getDialect().getDropSchemaCommand(schemaName);
        for (String dropCommand : dropCommands) {
            jdbcTemplate.execute(dropCommand);
        }
        log.debug("Comandos de drop executados para o schema: {}", schemaName);
    }

}