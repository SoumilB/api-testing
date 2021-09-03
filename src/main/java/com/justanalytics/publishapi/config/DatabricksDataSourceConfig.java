package com.justanalytics.publishapi.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DatabricksDataSourceConfig {

    @Bean(name = "databricksDataSource")
    @ConfigurationProperties(prefix = "databricks")
    public DataSource getDatabricksDataSource() {
        return (DataSource) DataSourceBuilder.create().build();
    }

    @Bean(name = "databricksJdbcTemplate")
    public JdbcTemplate getDatabricksJdbcTemplate(@Qualifier("databricksDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}
