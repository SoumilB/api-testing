package com.justanalytics.publishapi.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class DatasetSchemaExtractor implements ResultSetExtractor<Map<String, String>> {

    @Override
    public Map<String, String> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<String, String> schema = new HashMap<>();
        while(rs.next()) {
            String columnName = rs.getString("col_name");
            if(columnName.isBlank())
                break;
            String dataType = rs.getString("data_type");
            schema.put(columnName, dataType);
        }
        return schema;
    }
    
}
