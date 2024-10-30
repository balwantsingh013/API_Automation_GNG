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
import java.io.InputStream;
import java.util.Locale;
import java.util.Optional;

import static com.gng.api.constants.TestConstant.PATH_CONFIG;

@Getter
@Slf4j
public class RunContext {

    private static final String DEFAULT_ENV_VALUE = "qa";
    private static final String ENV_SYS_PROP_NAME = "env";
    private static String envConfigFileName;
    private static RunContext context;
    private AuthPayload authPayload;
    private EnvConfig envConfig;
    private DBAction dbAction;

    public static RunContext get() {
        context = Optional.ofNullable(context).orElseGet(RunContext::new);
        return context;
    }

    public String getEnvironment() {
        return System.getProperty(ENV_SYS_PROP_NAME, DEFAULT_ENV_VALUE).toLowerCase(Locale.ROOT);
    }

    public String getEnvConfigFile() {
        return Optional.ofNullable(envConfigFileName).orElseGet(() -> PATH_CONFIG + "envconfig-" + getEnvironment() + ".yml");
    }

    public void loadEnvConfig() {
        try {
            InputStream inputStream = new FileInputStream(getEnvConfigFile());
            Yaml yaml = new Yaml();
            envConfig = yaml.loadAs(inputStream, EnvConfig.class);
        } catch (IOException e) {
            throw new IllegalStateException(String.format("The file '%s' cannot be read", getEnvConfigFile()), e);
        }
        log.debug("The env configs are: {}", envConfig);
    }

    public void setAuthApiPayload() {
        authPayload = new AuthPayload();
        authPayload.setVendorId(envConfig.getVendorId());
        authPayload.setVendorSecret(envConfig.getVendorSecret());
    }

    public DBAction getDbAction() {
        dbAction = Optional.ofNullable(dbAction).orElseGet(() -> DBConnection.dbConnection().createDatabaseConnection(envConfig));
        return dbAction;
    }
}