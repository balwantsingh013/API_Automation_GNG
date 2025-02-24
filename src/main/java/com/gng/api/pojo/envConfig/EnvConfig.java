package com.gng.api.pojo.envConfig;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EnvConfig {
    private String baseUri;
    private String authUri;
    private String vendorId;
    private String vendorSecret;
    private String driverClassName;
    private String serverName;
    private String userName;
    private String password;
    private Boolean enableLogsOnPass;
    private Boolean enableLogsOnFail;
    private String aesBaseUri;
}
