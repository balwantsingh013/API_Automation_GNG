package com.gng.api.context;

import com.gng.api.auth.AuthPayload;
import com.gng.api.config.EnvConfig;
import com.gng.api.db.DBAction;
import com.gng.api.db.DBConnection;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;

import static com.gng.api.constants.TestConstant.PATH_CONFIG;

@Getter
@Slf4j
public class RunContext {

    private static final String DEFAULT_ENV = "qa";
    private static final String ENV_PROPERTY = "env";
    private static RunContext instance;

    private AuthPayload authPayload;
    private EnvConfig envConfig;
    private DBAction dbAction;

    public RunContext() {
        loadEnvConfig();
    }

    public static RunContext get() {
        if (instance == null) {
            instance = new RunContext();
        }
        return instance;
    }

    public String getEnvironment() {
        return System.getProperty(ENV_PROPERTY, DEFAULT_ENV).toLowerCase();
    }

    public String getEnvConfigFile() {
        return PATH_CONFIG + "envconfig-" + getEnvironment() + ".yml";
    }

    public void loadEnvConfig() {
        try (FileInputStream inputStream = new FileInputStream(getEnvConfigFile())) {
            envConfig = new Yaml().loadAs(inputStream, EnvConfig.class);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read config file: " + getEnvConfigFile(), e);
        }
        log.debug("Loaded environment configuration: {}", envConfig);
    }

    public void setAuthApiPayload() {
        authPayload = new AuthPayload();
        authPayload.setVendorId(envConfig.getVendorId());
        authPayload.setVendorSecret(envConfig.getVendorSecret());
    }

    public DBAction getDbAction() {
        if (dbAction == null) {
            dbAction = DBConnection.dbConnection().createDatabaseConnection(envConfig);
        }
        return dbAction;
    }
}
