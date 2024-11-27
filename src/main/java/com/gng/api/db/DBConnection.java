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

    public DBAction createDatabaseConnection(EnvConfig envConfig) {
        log.info("Create Database Connection");
        DataSource dataSource = getDataSource(envConfig);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        // Set log level to DEBUG for JdbcTemplate
        jdbcTemplate.setResultsMapCaseInsensitive(true);
        return new DBAction(jdbcTemplate);
    }

    private DriverManagerDataSource getDataSource(EnvConfig envConfig) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(envConfig.getServerName());
        dataSource.setUsername(envConfig.getUserName());
        dataSource.setPassword(envConfig.getPassword());
        dataSource.setDriverClassName(envConfig.getDriverClassName());
        return dataSource;
    }
}
