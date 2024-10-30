package com.gng.api.db;

import com.gng.api.config.EnvConfig;
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
        return new DBAction(jdbcTemplate);
    }

    public DriverManagerDataSource getDataSource(EnvConfig envConfig) {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource(envConfig.getServerName(),
                envConfig.getUserName(), envConfig.getPassword());
        driverManagerDataSource.setDriverClassName(envConfig.getDriverClassName());
        return driverManagerDataSource;
    }
}
