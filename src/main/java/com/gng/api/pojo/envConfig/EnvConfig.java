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

    /**
     * Preferences / PEW base URL from Postman {@code PreferencesBaseUrl} (should end with {@code /}).
     * Example: {@code https://pref-uat1.gng.vertexna.net/}
     */
    private String preferencesBaseUri;
    /**
     * Optional static Preferences auth token (Postman {@code PreferencesAuthToken}).
     * If blank, automation fetches via SOAP AuthenticatePreferences ({@code preferencesTokenUri}).
     */
    private String preferencesAuthToken;
    /** Postman AuthenticatePreferences URL. */
    private String preferencesTokenUri;
    /** Basic Auth username for Preferences token SOAP. */
    private String preferencesTokenUsername;
    /** Basic Auth password for Preferences token SOAP. */
    private String preferencesTokenPassword;
    /** SOAP VendorSource (e.g. GAGASSAVE). */
    private String preferencesVendorSource;
    /** SOAP Application (e.g. Preferences). */
    private String preferencesApplication;

    /** SMTP bounce address for TC_77/78 email-failure rollback tests (optional). */
    private String paperlessEmailFailureAddress;

    /** TC_131: ACTIVE account for preference-update failure (optional envconfig). */
    private PaperlessConfirmFailureAccount confirmPaperlessPreferenceFailureActive;
    /** TC_132: NEW account for preference-update failure (optional envconfig). */
    private PaperlessConfirmFailureAccount confirmPaperlessPreferenceFailureNew;
    /**
     * TC_133: account where Confirm preference update succeeds but Banner cannot create the
     * confirmation email notification → UAT 40321 (Customer Email Creation Failed).
     */
    private PaperlessConfirmFailureAccount confirmPaperlessNotificationFailure;

    @Setter
    @Getter
    public static class PaperlessConfirmFailureAccount {
        private String customerCode;
        private String premisesCode;
    }

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
