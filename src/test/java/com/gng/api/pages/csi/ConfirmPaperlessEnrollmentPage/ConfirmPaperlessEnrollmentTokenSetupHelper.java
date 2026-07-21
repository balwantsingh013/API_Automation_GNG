package com.gng.api.pages.csi.ConfirmPaperlessEnrollmentPage;

import com.gng.api.context.ApplicationContext;
import com.gng.api.db.DBAction;
import com.gng.api.db.DBQuery;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.ConfirmPaperlessEnrollment.ConfirmPaperlessEnrollmentRequest;
import com.gng.api.pojo.CSIPojo.UpdatePaperlessCommunications.UpdatePaperlessCommunicationsRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.report.DualReportManager;
import com.gng.api.steps.csi.ConfirmPaperlessEnrollment.ConfirmPaperlessEnrollmentLabel;
import com.gng.api.steps.csi.UpdatePaperlessCommunications.UpdatePaperlessCommunicationsLabel;
import com.gng.api.steps.AesEncryption.AesEncryptionSteps;
import com.gng.api.util.FakerDataGenerator;
import com.gng.api.util.PaperlessConfirmationTokenUtil;
import com.gng.api.util.PaperlessEnrollmentAccountRegistry;
import com.gng.api.util.PaperlessEnrollmentUtil;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import java.util.Map;

import static com.gng.api.constants.ApiEndPoint.CONFIRM_PAPERLESS_ENROLLMENT;
import static com.gng.api.constants.ApiEndPoint.UPDATE_PAPERLESS_COMMUNICATIONS;

@Slf4j
public class ConfirmPaperlessEnrollmentTokenSetupHelper {

    private final TestContext testContext;

    ConfirmPaperlessEnrollmentTokenSetupHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    /**
     * Prepares Confirm token: custadv email_link hashes are sent as-is;
     * short OCSEPCI ids are AES-encrypted when needed.
     */
    public static String prepareTokenForConfirmApi(String token) {
        if (token == null || token.isBlank()) {
            return token;
        }
        if ("InvalidTokenNotDecryptable".equals(token) || "placeholderToken".equals(token)) {
            return token;
        }
        String plaintext = PaperlessConfirmationTokenUtil.normalizeTokenPlaintext(token);
        if (PaperlessConfirmationTokenUtil.isCustAdvTokenIdentifier(plaintext) || plaintext.length() > 64) {
            log.info("Using custadv confirm token as-is (length {})", plaintext.length());
            return plaintext;
        }
        return AesEncryptionSteps.encryptData(plaintext);
    }

    /** @deprecated use {@link #prepareTokenForConfirmApi(String)} */
    @Deprecated
    public static String encryptForConfirmApi(String token) {
        return prepareTokenForConfirmApi(token);
    }

    String bootstrapEnrollmentAndGetToken(UpdatePaperlessCommunicationsLabel enrollCase) {
        UpdatePaperlessCommunicationsRequest enrollPayload = BasePage.deserializeJsonToPojo(
                UpdatePaperlessCommunicationsLabel.update_paperless_communications.toString(),
                UpdatePaperlessCommunicationsRequest.class);
        prepareEnrollmentPayload(enrollPayload, enrollCase);
        return enrollAndWaitForNewToken(enrollPayload, "Bootstrap UpdatePaperlessCommunications for " + enrollCase);
    }

    /** TC_146: NEW account with correspondence-only pending enrollment (bill not pending). */
    String bootstrapNewCorrEnrollmentAndGetToken() {
        UpdatePaperlessCommunicationsRequest enrollPayload = BasePage.deserializeJsonToPojo(
                UpdatePaperlessCommunicationsLabel.update_paperless_communications.toString(),
                UpdatePaperlessCommunicationsRequest.class);
        Map<String, Object> accountData =
                ApplicationContext.get().getDbAction().getNewPaperlessEligibleAccountWithNoToken();
        enrollPayload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
        enrollPayload.setCustomerCode(accountData.get("customerCode").toString());
        enrollPayload.setPremisesCode(accountData.get("premisesCode").toString());
        enrollPayload.setUpdateBillDeliveryOption(null);
        enrollPayload.setUpdateCorrDeliveryOption("E");
        enrollPayload.setEmailAddress(resolveEmail(accountData));
        return enrollAndWaitForNewToken(enrollPayload,
                "Bootstrap NEW correspondence enrollment for TC_146");
    }

    String getUsedTokenFromDatabase() {
        Map<String, Object> tokenData = PaperlessConfirmationTokenUtil.getUsedTokenData();
        return getTokenFromDbRecord(tokenData);
    }

    String getTokenFromDbRecord(Map<String, Object> tokenData) {
        String token = PaperlessConfirmationTokenUtil.resolveConfirmToken(tokenData);
        testContext.setPaperlessConfirmationToken(token);
        String customerCode = readRowString(tokenData, "customerCode");
        String premisesCode = readRowString(tokenData, "premisesCode");
        if (customerCode != null) {
            testContext.setCustomerCode(customerCode);
        }
        if (premisesCode != null) {
            testContext.setPremisesCode(premisesCode);
        }
        return token;
    }

    /**
     * TC_126: unused-token lookup + UAT1 link-expired rows + bootstrap expire fallback.
     */
    String getExpiredTokenForNegativeTest() {
        DBAction oracleDb = ApplicationContext.get().getDbAction();
        DBAction custAdvDb = ApplicationContext.get().getDbAction("mariadb");

        try {
            Map<String, Object> custAdvExpired = custAdvDb.getCustAdvExpiredConfirmationToken();
            String token = tryExpiredTokenFromRow(custAdvExpired, custAdvDb);
            if (token != null) {
                return token;
            }
        } catch (RuntimeException custAdvEx) {
            log.debug("custadv expired-column query unavailable ({}); trying date_time_link_expired",
                    custAdvEx.getMessage());
        }

        Map<String, Object> linkExpiredRow = custAdvDb.tryGetCustAdvExpiredByLinkExpiredToken();
        if (linkExpiredRow != null) {
            String token = tryExpiredTokenFromRow(linkExpiredRow, custAdvDb);
            if (token != null) {
                return token;
            }
        }

        String token = findExpiredTokenAmongCustAdvRows(
                custAdvDb.listCustAdvExpiredByLinkExpiredTokens(100, false), custAdvDb);
        if (token != null) {
            return token;
        }

        token = findExpiredTokenAmongCustAdvRows(
                custAdvDb.listRecentUnusedPaperlessTokensFromCustAdv(100, false), custAdvDb);
        if (token != null) {
            return token;
        }

        for (Map<String, Object> oracleRow : oracleDb.listExpiredConfirmationTokenAccounts(50, false)) {
            String customerCode = normalizeNumericCode(readRowString(oracleRow, "customerCode"));
            String premisesCode = normalizeNumericCode(readRowString(oracleRow, "premisesCode"));
            if (customerCode == null || premisesCode == null) {
                continue;
            }
            token = tryExpiredTokenForAccount(customerCode, premisesCode, custAdvDb);
            if (token != null) {
                return token;
            }
        }

        log.info("TC_126: no pre-seeded expired token; bootstrapping enroll + custadv/OCSEPCI expire");
        return bootstrapEnrollmentExpireTokenForTc124();
    }

    private String tryExpiredTokenForAccount(String customerCode, String premisesCode, DBAction custAdvDb) {
        try {
            Map<String, Object> tokenData = custAdvDb.getLatestConfirmPaperlessTokenFromCustAdv(
                    customerCode, premisesCode, false);
            String token = tryExpiredTokenFromRow(tokenData, custAdvDb);
            if (token != null) {
                return token;
            }
        } catch (EmptyResultDataAccessException ex) {
            log.debug("TC_126: no custadv unused token for {}/{}", customerCode, premisesCode);
        }
        try {
            Map<String, Object> anyTokenData = custAdvDb.getAnyLatestConfirmPaperlessTokenFromCustAdv(
                    customerCode, premisesCode);
            return tryExpiredTokenFromRow(anyTokenData, custAdvDb);
        } catch (EmptyResultDataAccessException ex) {
            log.debug("TC_126: no custadv token row for {}/{}", customerCode, premisesCode);
            return null;
        }
    }

    private String bootstrapEnrollmentExpireTokenForTc124() {
        String token = bootstrapEnrollmentAndGetToken(
                UpdatePaperlessCommunicationsLabel.TC_98__Positive__Active_Account_Bill_Enrollment_Initiated_);
        String customerCode = testContext.getCustomerCode();
        String premisesCode = testContext.getPremisesCode();
        ApplicationContext.get().getDbAction().expireValidConfirmationTokensForAccount(customerCode, premisesCode);

        int probeErrorCode = probeConfirmErrorCode(token);
        if (probeErrorCode != 10413) {
            throw new IllegalStateException(
                    "TC_126 bootstrap expire expected Confirm errorCode 10413, got " + probeErrorCode);
        }

        DBAction custAdvDb = ApplicationContext.get().getDbAction("mariadb");
        Map<String, Object> tokenData = custAdvDb.getLatestConfirmPaperlessTokenFromCustAdv(
                customerCode, premisesCode, false);
        custAdvDb.logConfirmPaperlessTokenLookupResult(customerCode, premisesCode, tokenData);
        log.info("TC_126: bootstrap expired token for {}/{}", customerCode, premisesCode);
        return tokenFromCustAdvRow(tokenData);
    }

    private String findExpiredTokenAmongCustAdvRows(List<Map<String, Object>> rows, DBAction custAdvDb) {
        for (Map<String, Object> tokenData : rows) {
            String token = tryExpiredTokenFromRow(tokenData, custAdvDb);
            if (token != null) {
                return token;
            }
        }
        return null;
    }

    private String tryExpiredTokenFromRow(Map<String, Object> tokenData, DBAction custAdvDb) {
        String token = PaperlessConfirmationTokenUtil.resolveTokenIdentifier(tokenData);
        if (!PaperlessConfirmationTokenUtil.isCustAdvTokenIdentifier(token)) {
            return null;
        }
        String customerCode = normalizeNumericCode(readRowString(tokenData, "customerCode"));
        String premisesCode = normalizeNumericCode(readRowString(tokenData, "premisesCode"));
        if (customerCode == null || premisesCode == null) {
            return null;
        }
        int probeErrorCode = probeConfirmErrorCode(token);
        if (probeErrorCode != 10413) {
            log.debug("TC_126: token for {}/{} returned errorCode {}, skipping",
                    customerCode, premisesCode, probeErrorCode);
            return null;
        }
        custAdvDb.logConfirmPaperlessTokenLookupResult(customerCode, premisesCode, tokenData);
        log.info("TC_126: expired token for {}/{}", customerCode, premisesCode);
        return tokenFromCustAdvRow(tokenData);
    }

    private String tokenFromCustAdvRow(Map<String, Object> tokenData) {
        String token = PaperlessConfirmationTokenUtil.normalizeTokenPlaintext(
                PaperlessConfirmationTokenUtil.resolveTokenIdentifier(tokenData));
        if (token == null || token.isBlank()) {
            throw new IllegalStateException("No Token in custadv row for TC_126: " + tokenData.keySet());
        }
        testContext.setPaperlessConfirmationToken(token);
        String customerCode = readRowString(tokenData, "customerCode");
        String premisesCode = readRowString(tokenData, "premisesCode");
        if (customerCode != null) {
            testContext.setCustomerCode(customerCode);
        }
        if (premisesCode != null) {
            testContext.setPremisesCode(premisesCode);
        }
        return token;
    }

    private int probeConfirmErrorCode(String token) {
        ConfirmPaperlessEnrollmentRequest confirmPayload = BasePage.deserializeJsonToPojo(
                ConfirmPaperlessEnrollmentLabel.confirm_paperless_enrollment.toString(),
                ConfirmPaperlessEnrollmentRequest.class);
        confirmPayload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
        confirmPayload.setToken(prepareTokenForConfirmApi(token));
        String uri = ApplicationContext.get().getEnvConfig().getBaseUri() + CONFIRM_PAPERLESS_ENROLLMENT;
        Response confirmResponse = io.restassured.RestAssured.given()
                .relaxedHTTPSValidation()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + testContext.getAuthToken())
                .body(confirmPayload)
                .post(uri);
        return confirmResponse.jsonPath().getInt("errorCode");
    }

    private static String normalizeNumericCode(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        int dot = trimmed.indexOf('.');
        return dot >= 0 ? trimmed.substring(0, dot) : trimmed;
    }

    /**
     * TC_127: PPER email ≠ Banner email (passive invalidation) → Confirm 10413.
     * Tries Oracle email-mismatch accounts first; otherwise enrolls then changes Banner email outside enroll API.
     */
    String getPassiveEmailInvalidationToken(UpdatePaperlessCommunicationsLabel enrollCase) {
        DBAction oracleDb = ApplicationContext.get().getDbAction();
        DBAction custAdvDb = ApplicationContext.get().getDbAction("mariadb");

        String token = findPassiveEmailInvalidationTokenAmongAccounts(
                oracleDb.listEmailMismatchConfirmationTokenAccounts(25, false), custAdvDb);
        if (token != null) {
            return token;
        }

        log.info("TC_127: no pre-seeded email-mismatch account; using bootstrap passive Banner email change");
        return bootstrapPassiveEmailInvalidationAndGetStaleToken(enrollCase);
    }

    private String findPassiveEmailInvalidationTokenAmongAccounts(List<Map<String, Object>> oracleAccounts,
                                                                  DBAction custAdvDb) {
        for (Map<String, Object> oracleRow : oracleAccounts) {
            String customerCode = normalizeNumericCode(readRowString(oracleRow, "customerCode"));
            String premisesCode = normalizeNumericCode(readRowString(oracleRow, "premisesCode"));
            if (customerCode == null || premisesCode == null) {
                continue;
            }
            try {
                Map<String, Object> tokenData = custAdvDb.getLatestConfirmPaperlessTokenFromCustAdv(
                        customerCode, premisesCode, false);
                String token = tryPassiveEmailInvalidationToken(tokenData, custAdvDb);
                if (token != null) {
                    return token;
                }
            } catch (EmptyResultDataAccessException ex) {
                log.debug("TC_127: no custadv unused token for email-mismatch account {}/{}",
                        customerCode, premisesCode);
            }
        }
        return null;
    }

    private String tryPassiveEmailInvalidationToken(Map<String, Object> tokenData, DBAction custAdvDb) {
        String token = PaperlessConfirmationTokenUtil.resolveTokenIdentifier(tokenData);
        if (!PaperlessConfirmationTokenUtil.isCustAdvTokenIdentifier(token)) {
            return null;
        }
        String customerCode = normalizeNumericCode(readRowString(tokenData, "customerCode"));
        String premisesCode = normalizeNumericCode(readRowString(tokenData, "premisesCode"));
        if (customerCode == null || premisesCode == null) {
            return null;
        }
        int probeErrorCode = probeConfirmErrorCode(token);
        if (probeErrorCode != 10413) {
            log.debug("TC_127: token for {}/{} returned errorCode {}, skipping",
                    customerCode, premisesCode, probeErrorCode);
            return null;
        }
        custAdvDb.logConfirmPaperlessTokenLookupResult(customerCode, premisesCode, tokenData);
        log.info("TC_127: passive email invalidation token for {}/{}", customerCode, premisesCode);
        return tokenFromCustAdvRow(tokenData);
    }

    String bootstrapEnrollmentEmailChangeAndGetStaleToken(UpdatePaperlessCommunicationsLabel enrollCase) {
        UpdatePaperlessCommunicationsRequest firstPayload = BasePage.deserializeJsonToPojo(
                UpdatePaperlessCommunicationsLabel.update_paperless_communications.toString(),
                UpdatePaperlessCommunicationsRequest.class);
        prepareEnrollmentPayload(firstPayload, enrollCase);
        long baseline = PaperlessConfirmationTokenUtil.getBaselineVerificationId(
                firstPayload.getCustomerCode(), firstPayload.getPremisesCode());
        postEnrollment(firstPayload, "TC_125/127 initial bill enrollment");
        String staleToken = PaperlessConfirmationTokenUtil.waitForTokenCreatedAfterEnroll(
                firstPayload.getCustomerCode(), firstPayload.getPremisesCode(), baseline);
        if (staleToken == null) {
            throw new IllegalStateException(
                    "No custadv token after initial enroll for "
                            + firstPayload.getCustomerCode() + "/" + firstPayload.getPremisesCode());
        }

        UpdatePaperlessCommunicationsRequest secondPayload = BasePage.deserializeJsonToPojo(
                UpdatePaperlessCommunicationsLabel.update_paperless_communications.toString(),
                UpdatePaperlessCommunicationsRequest.class);
        secondPayload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
        secondPayload.setCustomerCode(firstPayload.getCustomerCode());
        secondPayload.setPremisesCode(firstPayload.getPremisesCode());
        secondPayload.setUpdateBillDeliveryOption("E");
        secondPayload.setUpdateCorrDeliveryOption(null);
        secondPayload.setEmailAddress(generateNewTestEmail());
        postEnrollment(secondPayload, "TC_125 stale token after re-enroll with new email");

        DBAction custAdvDb = ApplicationContext.get().getDbAction("mariadb");
        Map<String, Object> tokenData = Map.of(
                "customerCode", firstPayload.getCustomerCode(),
                "premisesCode", firstPayload.getPremisesCode(),
                "tokenIdentifier", staleToken,
                "Token", staleToken);
        custAdvDb.logConfirmPaperlessTokenLookupResult(
                firstPayload.getCustomerCode(), firstPayload.getPremisesCode(), tokenData);

        testContext.setPaperlessConfirmationToken(staleToken);
        testContext.setCustomerCode(firstPayload.getCustomerCode());
        testContext.setPremisesCode(firstPayload.getPremisesCode());
        return staleToken;
    }

    /**
     * TC_127: enroll once, then change Banner email outside UpdatePaperlessCommunications (TC_119 passive flow).
     * PPER/OCSEPCI keep the original email; Confirm should return 10413 Token Expired.
     */
    String bootstrapPassiveEmailInvalidationAndGetStaleToken(UpdatePaperlessCommunicationsLabel enrollCase) {
        UpdatePaperlessCommunicationsRequest enrollPayload = BasePage.deserializeJsonToPojo(
                UpdatePaperlessCommunicationsLabel.update_paperless_communications.toString(),
                UpdatePaperlessCommunicationsRequest.class);
        prepareEnrollmentPayload(enrollPayload, enrollCase);
        long baseline = PaperlessConfirmationTokenUtil.getBaselineVerificationId(
                enrollPayload.getCustomerCode(), enrollPayload.getPremisesCode());
        postEnrollment(enrollPayload, "TC_127 initial bill enrollment");
        String staleToken = PaperlessConfirmationTokenUtil.waitForTokenCreatedAfterEnroll(
                enrollPayload.getCustomerCode(), enrollPayload.getPremisesCode(), baseline);
        if (staleToken == null) {
            throw new IllegalStateException(
                    "No custadv token after TC_127 initial enroll for "
                            + enrollPayload.getCustomerCode() + "/" + enrollPayload.getPremisesCode());
        }

        String newBannerEmail = PaperlessEnrollmentUtil.normalizeBannerEmail(generateNewTestEmail());
        ApplicationContext.get().getDbAction().updateActiveBannerEmailForCustomer(
                enrollPayload.getCustomerCode(), newBannerEmail);

        DBAction custAdvDb = ApplicationContext.get().getDbAction("mariadb");
        Map<String, Object> tokenData = Map.of(
                "customerCode", enrollPayload.getCustomerCode(),
                "premisesCode", enrollPayload.getPremisesCode(),
                "tokenIdentifier", staleToken,
                "Token", staleToken);
        int probeErrorCode = probeConfirmErrorCode(staleToken);
        if (probeErrorCode != 10413) {
            throw new IllegalStateException(
                    "TC_127 passive email setup expected Confirm errorCode 10413, got " + probeErrorCode);
        }
        custAdvDb.logConfirmPaperlessTokenLookupResult(
                enrollPayload.getCustomerCode(), enrollPayload.getPremisesCode(), tokenData);
        return tokenFromCustAdvRow(tokenData);
    }

    /** TC_130: valid custadv token but no active pending PPER before confirm → 10411. */
    String getNoActivePendingPperToken(UpdatePaperlessCommunicationsLabel enrollCase) {
        String token = findProbedTokenAmongCustAdvRows(
                ApplicationContext.get().getDbAction("mariadb")
                        .listRecentUnusedPaperlessTokensFromCustAdv(25, false),
                10411);
        if (token != null) {
            return token;
        }
        log.info("TC_130: no pre-seeded 10411 token; using stale-token flow (UAT1 cancel-pending still confirms)");
        return bootstrapEnrollmentEmailChangeAndGetStaleToken(enrollCase);
    }

    /**
     * TC_129: token decrypts to account A but custadv/PPER record points at account B → Confirm 10417.
     */
    String getAccountMismatchTokenForNegativeTest(UpdatePaperlessCommunicationsLabel enrollCase) {
        String token = findAccountMismatchTokenAmongCustAdvRows(
                ApplicationContext.get().getDbAction("mariadb")
                        .listRecentUnusedPaperlessTokensFromCustAdv(25, false));
        if (token != null) {
            return token;
        }
        return bootstrapAccountMismatchAndGetToken(enrollCase);
    }

    /**
     * TC_131: unused Confirm token for an ACTIVE account with pending bill PPER.
     * Confirm page locks GZBEMCP email row(s) FOR UPDATE so Banner preference apply fails → 40293
     * (UAT often returns HTTP 504 instead). Do not probe Confirm here (keeps unused token).
     */
    String getActivePreferenceUpdateFailedToken() {
        DBAction custAdvDb = ApplicationContext.get().getDbAction("mariadb");

        String configuredToken = tryConfiguredUnusedConfirmToken(
                ApplicationContext.get().getEnvConfig().getConfirmPaperlessPreferenceFailureActive(),
                custAdvDb,
                "TC_131");
        if (configuredToken != null) {
            return configuredToken;
        }

        log.info("TC_131: bootstrapping ACTIVE bill enrollment before GZBEMCP row lock");
        String bootstrapped = bootstrapEnrollmentAndGetToken(
                UpdatePaperlessCommunicationsLabel.TC_98__Positive__Active_Account_Bill_Enrollment_Initiated_);
        if (bootstrapped != null && !bootstrapped.isBlank()) {
            return bootstrapped;
        }

        throw new IllegalStateException(
                "TC_131 could not obtain an unused Confirm token for an ACTIVE account. "
                        + "Enroll first, or set confirmPaperlessPreferenceFailureActive in envconfig.");
    }

    /**
     * TC_132: unused Confirm token for a NEW account with pending bill preference.
     * Confirm page locks GZBEMCP email row(s) FOR UPDATE so confirmation-date apply fails → 40293
     * (UAT often returns HTTP 504 instead).
     */
    String getNewPreferenceUpdateFailedToken() {
        DBAction custAdvDb = ApplicationContext.get().getDbAction("mariadb");

        String configuredToken = tryConfiguredUnusedConfirmToken(
                ApplicationContext.get().getEnvConfig().getConfirmPaperlessPreferenceFailureNew(),
                custAdvDb,
                "TC_132");
        if (configuredToken != null) {
            return configuredToken;
        }

        log.info("TC_132: bootstrapping NEW bill enrollment before GZBEMCP row lock");
        String bootstrapped = bootstrapEnrollmentAndGetToken(
                UpdatePaperlessCommunicationsLabel.TC_99__Positive__New_Account_Bill_Enrollment_Initiated_);
        if (bootstrapped != null && !bootstrapped.isBlank()) {
            return bootstrapped;
        }

        throw new IllegalStateException(
                "TC_132 could not obtain an unused Confirm token for a NEW account. "
                        + "Enroll first, or set confirmPaperlessPreferenceFailureNew in envconfig.");
    }

    private static final ThreadLocal<String> TC133_ORIGINAL_BANNER_EMAIL = new ThreadLocal<>();

    /**
     * TC_133: unused pending Confirm token. Prefer envconfig account; otherwise enroll first.
     * Confirm page clears {@code NEW_TEST_EMAIL_ADDR} (same as Update TC_77/78) → UAT 40321.
     */
    String getConfirmationNotificationFailureToken() {
        DBAction custAdvDb = ApplicationContext.get().getDbAction("mariadb");

        String configuredToken = tryConfiguredUnusedConfirmToken(
                ApplicationContext.get().getEnvConfig().getConfirmPaperlessNotificationFailure(),
                custAdvDb,
                "TC_133");
        if (configuredToken != null) {
            return configuredToken;
        }

        log.info("TC_133: bootstrapping ACTIVE bill enrollment while Banner test-email override is active");
        String bootstrapped = bootstrapEnrollmentAndGetToken(
                UpdatePaperlessCommunicationsLabel.TC_98__Positive__Active_Account_Bill_Enrollment_Initiated_);
        if (bootstrapped != null && !bootstrapped.isBlank()) {
            return bootstrapped;
        }

        throw new IllegalStateException(
                "TC_133 could not obtain an unused Confirm token. Enroll first while NEW_TEST_EMAIL_ADDR "
                        + "is set, or set confirmPaperlessNotificationFailure.customerCode/premisesCode.");
    }

    /** Restores Banner email after TC_133 optional seed. Safe if setup never ran. */
    static void restoreNotificationFailureBannerEmail(String customerCode) {
        String original = TC133_ORIGINAL_BANNER_EMAIL.get();
        TC133_ORIGINAL_BANNER_EMAIL.remove();
        if (customerCode == null || customerCode.isBlank() || original == null || original.isBlank()) {
            return;
        }
        DualReportManager.logInfo(
                "TC_133 — restore Banner GZBEMCP email for customer " + customerCode
                        + " to '" + original + "'");
        long start = System.currentTimeMillis();
        String sql = DBQuery.UPDATE_ACTIVE_BANNER_EMAIL_FOR_CUSTOMER
                + "\n-- bind: email='" + original + "', customerCode='" + customerCode + "'";
        try {
            ApplicationContext.get().getDbAction()
                    .updateActiveBannerEmailForCustomer(customerCode, original);
            DualReportManager.logDatabaseQuery(
                    sql,
                    "Restored active Banner email for customer " + customerCode
                            + " to '" + original + "'",
                    System.currentTimeMillis() - start);
            log.info("Restored Banner email for customer {} after TC_133", customerCode);
        } catch (RuntimeException ex) {
            DualReportManager.logDatabaseQuery(
                    sql, null, System.currentTimeMillis() - start, false, ex.getMessage());
            log.warn("Could not restore Banner email for customer {}: {}", customerCode, ex.getMessage());
        }
    }

    /** Unused custadv Confirm token for an envconfig account (no Confirm probe). */
    private String tryConfiguredUnusedConfirmToken(
            com.gng.api.pojo.envConfig.EnvConfig.PaperlessConfirmFailureAccount configured,
            DBAction custAdvDb,
            String label) {
        if (configured == null
                || configured.getCustomerCode() == null || configured.getCustomerCode().isBlank()
                || configured.getPremisesCode() == null || configured.getPremisesCode().isBlank()) {
            return null;
        }
        String customerCode = configured.getCustomerCode().trim();
        String premisesCode = configured.getPremisesCode().trim();
        log.info("{}: using envconfig unused-token account {}/{}", label, customerCode, premisesCode);

        Map<String, Object> tokenData;
        try {
            tokenData = custAdvDb.getLatestConfirmPaperlessTokenFromCustAdv(customerCode, premisesCode, false);
        } catch (EmptyResultDataAccessException ex) {
            throw new IllegalStateException(
                    label + " envconfig account " + customerCode + "/" + premisesCode
                            + " has no unused custadv confirmation token. Enroll first, then re-run.");
        }

        String token = PaperlessConfirmationTokenUtil.resolveTokenIdentifier(tokenData);
        if (!PaperlessConfirmationTokenUtil.isCustAdvTokenIdentifier(token)) {
            throw new IllegalStateException(
                    label + " envconfig account " + customerCode + "/" + premisesCode
                            + " token is not a usable custadv identifier");
        }
        custAdvDb.logConfirmPaperlessTokenLookupResult(customerCode, premisesCode, tokenData);
        testContext.setCustomerCode(customerCode);
        testContext.setPremisesCode(premisesCode);
        return tokenFromCustAdvRow(tokenData);
    }

    private String findProbedTokenAmongCustAdvRows(List<Map<String, Object>> rows, int expectedErrorCode) {
        DBAction custAdvDb = ApplicationContext.get().getDbAction("mariadb");
        for (Map<String, Object> tokenData : rows) {
            String token = PaperlessConfirmationTokenUtil.resolveTokenIdentifier(tokenData);
            if (!PaperlessConfirmationTokenUtil.isCustAdvTokenIdentifier(token)) {
                continue;
            }
            String customerCode = normalizeNumericCode(readRowString(tokenData, "customerCode"));
            String premisesCode = normalizeNumericCode(readRowString(tokenData, "premisesCode"));
            if (customerCode == null || premisesCode == null) {
                continue;
            }
            if (probeConfirmErrorCode(token) != expectedErrorCode) {
                continue;
            }
            custAdvDb.logConfirmPaperlessTokenLookupResult(customerCode, premisesCode, tokenData);
            log.info("TC probe {}: token for {}/{}", expectedErrorCode, customerCode, premisesCode);
            return tokenFromCustAdvRow(tokenData);
        }
        return null;
    }

    private String findAccountMismatchTokenAmongCustAdvRows(List<Map<String, Object>> rows) {
        DBAction custAdvDb = ApplicationContext.get().getDbAction("mariadb");
        for (Map<String, Object> tokenData : rows) {
            String token = tryAccountMismatchToken(tokenData, custAdvDb);
            if (token != null) {
                return token;
            }
        }
        return null;
    }

    private String tryAccountMismatchToken(Map<String, Object> tokenData, DBAction custAdvDb) {
        String token = PaperlessConfirmationTokenUtil.resolveTokenIdentifier(tokenData);
        if (!PaperlessConfirmationTokenUtil.isCustAdvTokenIdentifier(token)) {
            return null;
        }
        String customerCode = normalizeNumericCode(readRowString(tokenData, "customerCode"));
        String premisesCode = normalizeNumericCode(readRowString(tokenData, "premisesCode"));
        if (customerCode == null || premisesCode == null) {
            return null;
        }
        if (probeConfirmErrorCode(token) != 10417) {
            return null;
        }
        custAdvDb.logConfirmPaperlessTokenLookupResult(customerCode, premisesCode, tokenData);
        log.info("TC_129: account mismatch token for {}/{}", customerCode, premisesCode);
        return tokenFromCustAdvRow(tokenData);
    }

    private String bootstrapAccountMismatchAndGetToken(UpdatePaperlessCommunicationsLabel enrollCase) {
        UpdatePaperlessCommunicationsRequest enrollPayload = BasePage.deserializeJsonToPojo(
                UpdatePaperlessCommunicationsLabel.update_paperless_communications.toString(),
                UpdatePaperlessCommunicationsRequest.class);
        prepareEnrollmentPayload(enrollPayload, enrollCase);
        long baseline = PaperlessConfirmationTokenUtil.getBaselineVerificationId(
                enrollPayload.getCustomerCode(), enrollPayload.getPremisesCode());
        postEnrollment(enrollPayload, "TC_129 initial bill enrollment");
        String token = PaperlessConfirmationTokenUtil.waitForTokenCreatedAfterEnroll(
                enrollPayload.getCustomerCode(), enrollPayload.getPremisesCode(), baseline);
        if (token == null) {
            throw new IllegalStateException(
                    "No custadv token after TC_129 enroll for "
                            + enrollPayload.getCustomerCode() + "/" + enrollPayload.getPremisesCode());
        }

        Map<String, Object> tokenData = ApplicationContext.get().getDbAction("mariadb")
                .getUnusedConfirmPaperlessTokenAfterId(
                        enrollPayload.getCustomerCode(), enrollPayload.getPremisesCode(), baseline);
        long verificationId = readVerificationId(tokenData);
        Map<String, Object> otherAccount = ApplicationContext.get().getDbAction()
                .getActivePaperlessEligibleAccountWithNoTokenExcluding(
                        enrollPayload.getCustomerCode(), enrollPayload.getPremisesCode());
        String mismatchedAccountNumber = DBAction.buildCustAdvAccountNumber(
                readRowString(otherAccount, "customerCode"), readRowString(otherAccount, "premisesCode"));
        ApplicationContext.get().getDbAction("mariadb").updateCustAdvPaperlessTokenAccountNumberQuiet(
                verificationId, mismatchedAccountNumber);

        int probeErrorCode = probeConfirmErrorCode(token);
        if (probeErrorCode != 10417) {
            throw new IllegalStateException(
                    "TC_129 account-mismatch setup expected Confirm errorCode 10417, got " + probeErrorCode);
        }

        DBAction custAdvDb = ApplicationContext.get().getDbAction("mariadb");
        custAdvDb.logConfirmPaperlessTokenLookupResult(
                enrollPayload.getCustomerCode(), enrollPayload.getPremisesCode(), tokenData);
        return tokenFromCustAdvRow(tokenData);
    }

    private static long readVerificationId(Map<String, Object> tokenData) {
        Object id = tokenData.get("email_verification_status_id");
        if (id == null) {
            id = tokenData.get("EMAIL_VERIFICATION_STATUS_ID");
        }
        if (id instanceof Number number) {
            return number.longValue();
        }
        if (id != null) {
            return Long.parseLong(id.toString());
        }
        throw new IllegalStateException("custadv token row missing email_verification_status_id: " + tokenData.keySet());
    }

    String resolveUsedTokenForNegativeTest(UpdatePaperlessCommunicationsLabel enrollCase) {
        try {
            return getUsedTokenFromDatabase();
        } catch (RuntimeException ex) {
            log.warn("No used custadv token in DB ({}); creating one via bootstrap confirm", ex.getMessage());
            return bootstrapEnrollmentConfirmOnceAndGetUsedToken(enrollCase);
        }
    }

    private String bootstrapEnrollmentConfirmOnceAndGetUsedToken(UpdatePaperlessCommunicationsLabel enrollCase) {
        String token = bootstrapEnrollmentAndGetToken(enrollCase);
        postConfirmEnrollment(token, "TC_128 mark token used");
        return token;
    }

    private String enrollAndWaitForNewToken(UpdatePaperlessCommunicationsRequest enrollPayload, String label) {
        long baseline = PaperlessConfirmationTokenUtil.getBaselineVerificationId(
                enrollPayload.getCustomerCode(), enrollPayload.getPremisesCode());
        postEnrollment(enrollPayload, label);
        String token = PaperlessConfirmationTokenUtil.waitForTokenCreatedAfterEnroll(
                enrollPayload.getCustomerCode(), enrollPayload.getPremisesCode(), baseline);
        if (token == null) {
            throw new IllegalStateException(
                    "No new paperless confirmation token in custadv for "
                            + enrollPayload.getCustomerCode() + "/" + enrollPayload.getPremisesCode());
        }
        DBAction custAdvDb = ApplicationContext.get().getDbAction("mariadb");
        Map<String, Object> tokenData = custAdvDb.getLatestConfirmPaperlessTokenFromCustAdv(
                enrollPayload.getCustomerCode(), enrollPayload.getPremisesCode(), false);
        custAdvDb.logConfirmPaperlessTokenLookupResult(
                enrollPayload.getCustomerCode(), enrollPayload.getPremisesCode(), tokenData);
        testContext.setPaperlessConfirmationToken(token);
        testContext.setCustomerCode(enrollPayload.getCustomerCode());
        testContext.setPremisesCode(enrollPayload.getPremisesCode());
        return token;
    }

    private void postEnrollment(UpdatePaperlessCommunicationsRequest enrollPayload, String label) {
        String uri = ApplicationContext.get().getEnvConfig().getBaseUri() + UPDATE_PAPERLESS_COMMUNICATIONS;
        Response enrollResponse = io.restassured.RestAssured.given()
                .relaxedHTTPSValidation()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + testContext.getAuthToken())
                .body(enrollPayload)
                .post(uri);
        log.info("{} returned HTTP {}", label, enrollResponse.statusCode());
        int enrollErrorCode = enrollResponse.jsonPath().getInt("errorCode");
        if (enrollErrorCode != 0) {
            throw new IllegalStateException(
                    label + " failed with errorCode " + enrollErrorCode + ": " + enrollResponse.asString());
        }
    }

    private void postConfirmEnrollment(String token, String label) {
        ConfirmPaperlessEnrollmentRequest confirmPayload = BasePage.deserializeJsonToPojo(
                ConfirmPaperlessEnrollmentLabel.confirm_paperless_enrollment.toString(),
                ConfirmPaperlessEnrollmentRequest.class);
        confirmPayload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
        confirmPayload.setToken(prepareTokenForConfirmApi(token));
        String uri = ApplicationContext.get().getEnvConfig().getBaseUri() + CONFIRM_PAPERLESS_ENROLLMENT;
        Response confirmResponse = io.restassured.RestAssured.given()
                .relaxedHTTPSValidation()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + testContext.getAuthToken())
                .body(confirmPayload)
                .post(uri);
        log.info("{} returned HTTP {}", label, confirmResponse.statusCode());
        int confirmErrorCode = confirmResponse.jsonPath().getInt("errorCode");
        if (confirmErrorCode != 0) {
            throw new IllegalStateException(
                    label + " failed with errorCode " + confirmErrorCode + ": " + confirmResponse.asString());
        }
    }

    private static String generateNewTestEmail() {
        return "newemail_" + FakerDataGenerator.generateAlphanumeric(5) + "@test.com";
    }

    private static String readRowString(Map<String, Object> row, String key) {
        Object direct = row.get(key);
        if (direct != null) {
            return direct.toString();
        }
        for (Map.Entry<String, Object> entry : row.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(key) && entry.getValue() != null) {
                return entry.getValue().toString();
            }
        }
        return null;
    }

    private Map<String, Object> reserveFreshBillEnrollmentAccount() {
        DBAction dbAction = ApplicationContext.get().getDbAction();
        String excludeCustomer = null;
        String excludePremises = null;

        for (int attempt = 0; attempt < 25; attempt++) {
            Map<String, Object> candidate = excludeCustomer == null
                    ? dbAction.getActiveAccountForFreshBillEnrollmentStrict()
                    : dbAction.getActiveAccountForFreshBillEnrollmentStrictExcluding(
                            excludeCustomer, excludePremises);
            if (PaperlessEnrollmentAccountRegistry.tryReserve(candidate)) {
                log.info("Reserved confirm test account {}/{}",
                        candidate.get("customerCode"), candidate.get("premisesCode"));
                return candidate;
            }
            excludeCustomer = readRowString(candidate, "customerCode");
            excludePremises = readRowString(candidate, "premisesCode");
        }

        for (Map<String, Object> candidate : dbAction.listFreshBillEnrollmentCandidates()) {
            if (PaperlessEnrollmentAccountRegistry.tryReserve(candidate)) {
                return candidate;
            }
        }

        throw new IllegalStateException("No unreserved ACTIVE account for confirm paperless bootstrap.");
    }

    private void prepareEnrollmentPayload(UpdatePaperlessCommunicationsRequest payload,
                                          UpdatePaperlessCommunicationsLabel enrollCase) {
        payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
        Map<String, Object> accountData;

        switch (enrollCase) {
            case TC_98__Positive__Active_Account_Bill_Enrollment_Initiated_:
                accountData = reserveFreshBillEnrollmentAccount();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                payload.setUpdateBillDeliveryOption("E");
                payload.setUpdateCorrDeliveryOption(null);
                payload.setEmailAddress(resolveEmail(accountData));
                break;

            case TC_99__Positive__New_Account_Bill_Enrollment_Initiated_:
                accountData = ApplicationContext.get().getDbAction().getNewPaperlessEligibleAccountWithNoToken();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                payload.setUpdateBillDeliveryOption("E");
                payload.setUpdateCorrDeliveryOption(null);
                payload.setEmailAddress(resolveEmail(accountData));
                break;

            case TC_100__Positive__Active_Account_Both_Channels_Enrollment_Initiated_:
                // Same as Update TC_100: never-enrolled ACTIVE P/P (avoid hottest TC_97 → 40291)
                accountData = ApplicationContext.get().getDbAction().getActiveAccountForTc84();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                payload.setUpdateBillDeliveryOption("E");
                payload.setUpdateCorrDeliveryOption("E");
                payload.setEmailAddress(resolveEmail(accountData));
                break;

            case TC_101__Positive__New_Account_Both_Channels_Enrollment_Initiated_:
                accountData = ApplicationContext.get().getDbAction().getNewAccountForTc98();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                payload.setUpdateBillDeliveryOption("E");
                payload.setUpdateCorrDeliveryOption("E");
                payload.setEmailAddress(resolveEmail(accountData));
                break;

            case TC_86__Positive__billDeliveryOptionStatus_Format__NO_CHANGE_:
                accountData = ApplicationContext.get().getDbAction().getActiveAccountForTc83();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                payload.setUpdateBillDeliveryOption(null);
                payload.setUpdateCorrDeliveryOption("E");
                payload.setEmailAddress(resolveEmail(accountData));
                break;

            default:
                throw new IllegalStateException("Unsupported bootstrap enrollment case: " + enrollCase);
        }
    }

    private static String resolveEmail(Map<String, Object> accountData) {
        String email = PaperlessEnrollmentUtil.resolveBannerEmail(accountData);
        if (email != null) {
            return email;
        }
        throw new IllegalStateException("No email found in account data: " + accountData.keySet());
    }
}
