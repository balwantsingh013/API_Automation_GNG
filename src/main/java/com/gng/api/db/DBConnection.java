package com.gng.api.db;

import com.gng.api.pojo.envConfig.EnvConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Slf4j
public class DBConnection {

    public static DBConnection dbConnection() {
        return new DBConnection();
    }

    /**
     * Backward-compatible default connection using envConfig.defaultDatabase
     */
    public DBAction createDatabaseConnection(EnvConfig envConfig) {
        return createDatabaseConnection(envConfig, "default");
    }

    /**
     * Create connection using a specific key (e.g., "oracle", "mariadb")
     */
    public DBAction createDatabaseConnection(EnvConfig envConfig, String connectionKey) {
        log.info("Create Database Connection for: {}", connectionKey);
        DataSource dataSource = getDataSource(envConfig, connectionKey);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);
        return new DBAction(jdbcTemplate);
    }

    /**
     * Resolve the correct database config based on connectionKey
     */
    private DriverManagerDataSource getDataSource(EnvConfig envConfig, String connectionKey) {
        String key = connectionKey.equals("default") ? envConfig.getDefaultDatabase() : connectionKey;
        EnvConfig.DatabaseConfig dbConfig = envConfig.getDatabases().get(key);

        if (dbConfig == null) {
            throw new IllegalArgumentException("No database configuration found for key: " + key);
        }

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(dbConfig.getServerName());
        dataSource.setUsername(dbConfig.getUserName());
        dataSource.setPassword(dbConfig.getPassword());
        dataSource.setDriverClassName(dbConfig.getDriverClassName());

        log.debug("DataSource configured with driver: {}", dbConfig.getDriverClassName());
        return dataSource;
    }
}
