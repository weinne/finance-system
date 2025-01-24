package com.weinne.finance_system.config;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayGlobalConfig {

    @Bean
    public Flyway flyway(DataSource dataSource) {
        Flyway flyway = Flyway.configure()
            .dataSource(dataSource)
            .schemas("public")  // Schema global
            .locations("classpath:db/migration/global")
            .load();
        
        flyway.migrate();
        return flyway;
    }
}
