package com.justanalytics.publishapi.repositories;

import java.util.List;
import java.util.Map;

import com.justanalytics.publishapi.dataModels.DatasetSchema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DataRepository {
    
    @Autowired
    @Qualifier("databricksJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Cacheable(value = "schemas")
    public DatasetSchema getDatasetSchema(String database, String table) {
        String query = String.format("DESC %s.%s", database, table);
        Map<String, String> schema = jdbcTemplate.query(query, new DatasetSchemaExtractor());
        return new DatasetSchema(database, table, schema);
    }

    @Cacheable(value = "tables")
    public List<String> getDatasets(String database) {
        String query = String.format("SHOW TABLES IN %s", database);
        return jdbcTemplate.query(query, (rs, rowNum) -> rs.getString("tableName"));
    }

    public List<String> getDatabases() {
        String query = "SHOW DATABASES";
        List<String> databases = jdbcTemplate.query(query, (rs, rowNum) -> rs.getString("namespace"));
        return databases;
    }

    public Long getCount(String database, String table) {
        String query = String.format("SELECT COUNT(*) FROM %s.%s", database, table);
        return jdbcTemplate.queryForObject(query, Long.class);
    }

    public List<Map<String, Object>> getData(String query) {
        return jdbcTemplate.queryForList(query);
    }
}
