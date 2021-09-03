package com.justanalytics.publishapi.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DatasourceConfig {
    
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "audit-datasource")
    public DataSource getDataSource() {
        return DataSourceBuilder.create().build();
    }
}
