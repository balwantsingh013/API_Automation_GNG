package com.gng.api.pojo.envConfig;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class EnvConfig {
    private String baseUri;
    private String authUri;
    private String vendorId;
    private String vendorSecret;
    private Boolean enableLogsOnPass;
    private Boolean enableLogsOnFail;
    private String aesBaseUri;
    private String aesEncryptPath;
    private String aesDecryptPath;

    // 👇 New fields for multi-database support
    private String defaultDatabase; // e.g., "oracle" or "mariadb"
    private Map<String, DatabaseConfig> databases;

    @Setter
    @Getter
    public static class DatabaseConfig {
        private String driverClassName;
        private String serverName;
        private String userName;
        private String password;
    }
}
