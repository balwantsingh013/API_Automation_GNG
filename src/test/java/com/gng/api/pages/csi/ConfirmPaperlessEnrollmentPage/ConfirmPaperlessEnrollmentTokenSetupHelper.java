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
import com.gng.api.util.BannerTestEmailOverrideUtil;
import com.gng.api.util.PaperlessConfirmationTokenUtil;
import com.gng.api.util.PaperlessEnrollmentAccountRegistry;
import com.gng.api.util.PaperlessEnrollmentUtil;
import com.gng.api.util.PaperlessTokenPperEvidenceUtil;
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

    /**
     * TC_125: token identifier does not match any stored token record → Confirm 10411.
     * <p>Prior green Confirm report used email-change stale-token (custadv row still present) and got
     * 10411. Current UAT1 often returns 10413 for that path — fall back to enroll+delete.
     */
    String getTokenNotFoundNoStoredRecordToken(UpdatePaperlessCommunicationsLabel enrollCase) {
        return resolveTokenNotFound10411(enrollCase, "TC_125");
    }

    /**
     * TC_130: otherwise-issued token with no active PendingConfirmation PPER → Confirm 10411.
     * <p>Same prior-green setup as TC_125 (email-change stale token → 10411 with
     * {@code rowPresent=true} / {@code newemail_*}). Falls back to enroll+delete when UAT returns 10413.
     */
    String getNoActivePendingPperToken(UpdatePaperlessCommunicationsLabel enrollCase) {
        return resolveTokenNotFound10411(enrollCase, "TC_130");
    }

    /**
     * Prefer prior-green email-change stale-token path; if UAT now classifies it as 10413, force
     * Token Not Found by removing the stored paperless token record for a freshly issued token.
     */
    private String resolveTokenNotFound10411(UpdatePaperlessCommunicationsLabel enrollCase, String label) {
        log.info("{}: trying email-change stale-token flow (prior green Confirm report)", label);
        String staleToken = bootstrapEnrollmentEmailChangeAndGetStaleToken(enrollCase);
        int probeErrorCode = probeConfirmErrorCode(staleToken);
        if (probeErrorCode == 10411) {
            log.info("{}: email-change stale token returned 10411", label);
            return staleToken;
        }
        log.warn("{}: email-change returned errorCode {} (prior report was 10411); "
                        + "falling back to enroll+delete stored paperless token for Token Not Found",
                label, probeErrorCode);
        return enrollAndDeleteStoredPaperlessToken(enrollCase, label);
    }

    /** Enroll once, delete stored custadv (+ pending OCSEPCI), probe must be 10411. */
    private String enrollAndDeleteStoredPaperlessToken(UpdatePaperlessCommunicationsLabel enrollCase,
                                                       String label) {
        UpdatePaperlessCommunicationsRequest enrollPayload = BasePage.deserializeJsonToPojo(
                UpdatePaperlessCommunicationsLabel.update_paperless_communications.toString(),
                UpdatePaperlessCommunicationsRequest.class);
        prepareEnrollmentPayload(enrollPayload, enrollCase);
        long baseline = PaperlessConfirmationTokenUtil.getBaselineVerificationId(
                enrollPayload.getCustomerCode(), enrollPayload.getPremisesCode());
        postEnrollment(enrollPayload, label + " enroll before delete-token");
        String token = PaperlessConfirmationTokenUtil.waitForTokenCreatedAfterEnroll(
                enrollPayload.getCustomerCode(), enrollPayload.getPremisesCode(), baseline);
        if (token == null) {
            throw new IllegalStateException(
                    "No custadv token after " + label + " enroll for "
                            + enrollPayload.getCustomerCode() + "/" + enrollPayload.getPremisesCode());
        }

        String normalizedToken = PaperlessConfirmationTokenUtil.normalizeTokenPlaintext(token);
        DBAction custAdvDb = ApplicationContext.get().getDbAction("mariadb");
        int deletedByToken = custAdvDb.deleteCustAdvUnusedPaperlessTokenByTokenQuiet(normalizedToken);
        if (deletedByToken == 0) {
            Map<String, Object> tokenData = custAdvDb.getUnusedConfirmPaperlessTokenAfterId(
                    enrollPayload.getCustomerCode(), enrollPayload.getPremisesCode(), baseline);
            custAdvDb.deleteCustAdvPaperlessTokenByIdQuiet(readVerificationId(tokenData));
        }
        custAdvDb.deleteCustAdvUnusedPaperlessTokensForAccountQuiet(
                enrollPayload.getCustomerCode(), enrollPayload.getPremisesCode());
        ApplicationContext.get().getDbAction().deletePendingOcsepciRowsForAccountQuiet(
                enrollPayload.getCustomerCode(), enrollPayload.getPremisesCode());

        int probeErrorCode = probeConfirmErrorCode(normalizedToken);
        if (probeErrorCode != 10411) {
            throw new IllegalStateException(
                    label + " delete-token setup expected Confirm errorCode 10411, got " + probeErrorCode);
        }

        testContext.setPaperlessConfirmationToken(normalizedToken);
        testContext.setCustomerCode(enrollPayload.getCustomerCode());
        testContext.setPremisesCode(enrollPayload.getPremisesCode());
        log.info("{}: removed stored paperless token for {}/{} (Confirm probe=10411)",
                label, enrollPayload.getCustomerCode(), enrollPayload.getPremisesCode());
        return normalizedToken;
    }

    String bootstrapEnrollmentEmailChangeAndGetStaleToken(UpdatePaperlessCommunicationsLabel enrollCase) {
        UpdatePaperlessCommunicationsRequest firstPayload = BasePage.deserializeJsonToPojo(
                UpdatePaperlessCommunicationsLabel.update_paperless_communications.toString(),
                UpdatePaperlessCommunicationsRequest.class);
        prepareEnrollmentPayload(firstPayload, enrollCase);
        long baseline = PaperlessConfirmationTokenUtil.getBaselineVerificationId(
                firstPayload.getCustomerCode(), firstPayload.getPremisesCode());
        postEnrollment(firstPayload, "email-change initial bill enrollment");
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
        postEnrollment(secondPayload, "email-change re-enroll with new email");

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
     * Do not probe Confirm here (keeps unused token). Confirm page locks GZBEMCP → 40293.
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
     * Confirm page locks GZBEMCP → 40293.
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
            String token = getUsedTokenFromDatabase();
            int probeErrorCode = probeConfirmErrorCode(token);
            if (probeErrorCode == 10415) {
                return token;
            }
            log.warn("DB used token returned Confirm errorCode {}; bootstrapping a fresh used token",
                    probeErrorCode);
        } catch (RuntimeException ex) {
            log.warn("No used custadv token in DB ({}); creating one via bootstrap confirm", ex.getMessage());
        }
        return bootstrapEnrollmentConfirmOnceAndGetUsedToken(enrollCase);
    }

    private String bootstrapEnrollmentConfirmOnceAndGetUsedToken(UpdatePaperlessCommunicationsLabel enrollCase) {
        String token = bootstrapEnrollmentAndGetToken(enrollCase);
        postConfirmEnrollment(token, "TC_128 mark token used");
        int probeErrorCode = probeConfirmErrorCode(token);
        if (probeErrorCode != 10415) {
            throw new IllegalStateException(
                    "TC_128 used-token setup expected Confirm errorCode 10415, got " + probeErrorCode);
        }
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

    private static final ThreadLocal<String> TC147_ORIGINAL_STATUS = new ThreadLocal<>();
    private static final ThreadLocal<String> TC149_SAVED_BANNER_EMAIL = new ThreadLocal<>();
    private static final ThreadLocal<String> TC149_CUSTOMER_FOR_RESTORE = new ThreadLocal<>();

    /**
     * TC_147: enroll while Banner account is NEW, then transition UCRACCT status to ACTIVE
     * before Confirm so response accountType follows the ACTIVE confirmation path.
     */
    String bootstrapTc147NewToActiveTransitionAndGetToken() {
        DualReportManager.logInfo(
                "TC_147 — STEP 1: enroll while accountStatus=NEW (token created on NEW account)");
        String token = bootstrapEnrollmentAndGetToken(
                UpdatePaperlessCommunicationsLabel.TC_99__Positive__New_Account_Bill_Enrollment_Initiated_);
        String customerCode = testContext.getCustomerCode();
        String premisesCode = testContext.getPremisesCode();
        DBAction bannerDb = ApplicationContext.get().getDbAction();

        Map<String, Object> before = bannerDb.tryLookupUcracctAccount(customerCode, premisesCode);
        String statusBefore = readRowString(before, "accountStatus");
        DualReportManager.logInfo(
                "TC_147 — STEP 1 evidence: account=" + customerCode + "/" + premisesCode
                        + " accountStatus at token create = '" + statusBefore + "' (expected N/NEW)");
        if (statusBefore == null || !(statusBefore.equalsIgnoreCase("N") || statusBefore.equalsIgnoreCase("NEW"))) {
            throw new IllegalStateException(
                    "TC_147 requires NEW account at enrollment; got accountStatus='" + statusBefore
                            + "' for " + customerCode + "/" + premisesCode);
        }

        TC147_ORIGINAL_STATUS.set(statusBefore);
        DualReportManager.logInfo(
                "TC_147 — STEP 2: transition Banner UCRACCT_STATUS_IND from '"
                        + statusBefore + "' to 'A' (ACTIVE) before Confirm");
        long start = System.currentTimeMillis();
        try {
            bannerDb.updateUcracctStatusInd(customerCode, premisesCode, "A");
            DualReportManager.logDatabaseQuery(
                    DBQuery.UPDATE_UCRACCT_STATUS_IND_FOR_ACCOUNT
                            + "\n-- bind: status='A', customerCode='" + customerCode
                            + "', premisesCode='" + premisesCode + "'",
                    "Updated UCRACCT_STATUS_IND to A for " + customerCode + "/" + premisesCode,
                    System.currentTimeMillis() - start);
        } catch (RuntimeException ex) {
            DualReportManager.logDatabaseQuery(
                    DBQuery.UPDATE_UCRACCT_STATUS_IND_FOR_ACCOUNT,
                    null,
                    System.currentTimeMillis() - start,
                    false,
                    ex.getMessage());
            throw ex;
        }

        Map<String, Object> after = bannerDb.tryLookupUcracctAccount(customerCode, premisesCode);
        String statusAfter = readRowString(after, "accountStatus");
        DualReportManager.logInfo(
                "TC_147 — STEP 2 evidence: accountStatus after transition = '"
                        + statusAfter + "' (expected A/ACTIVE) before Confirm API call");
        if (statusAfter == null || !(statusAfter.equalsIgnoreCase("A") || statusAfter.equalsIgnoreCase("ACTIVE"))) {
            throw new IllegalStateException(
                    "TC_147 status transition failed; expected A after update, got '" + statusAfter + "'");
        }
        DualReportManager.logInfo(
                "TC_147 — STEP 3: Confirm will run with token from NEW enrollment on now-ACTIVE account "
                        + "(expect accountType=ACTIVE)");
        return token;
    }

    /** Restores TC_147 Banner status if setup flipped N→A. Safe if setup never ran. */
    static void restoreTc147AccountStatus(String customerCode, String premisesCode) {
        String original = TC147_ORIGINAL_STATUS.get();
        TC147_ORIGINAL_STATUS.remove();
        if (customerCode == null || customerCode.isBlank()
                || premisesCode == null || premisesCode.isBlank()
                || original == null || original.isBlank()) {
            return;
        }
        String restoreStatus = original.equalsIgnoreCase("NEW") ? "N" : original;
        DualReportManager.logInfo(
                "TC_147 — restore UCRACCT_STATUS_IND for " + customerCode + "/" + premisesCode
                        + " to '" + restoreStatus + "'");
        try {
            ApplicationContext.get().getDbAction()
                    .updateUcracctStatusInd(customerCode, premisesCode, restoreStatus);
        } catch (RuntimeException ex) {
            log.warn("TC_147 could not restore account status for {}/{}: {}",
                    customerCode, premisesCode, ex.getMessage());
        }
    }

    /**
     * TC_148: multiple Update enrollments on same account — null/null → E/null → E/E —
     * then Confirm applies the latest aggregated PPER state (last state wins).
     */
    String bootstrapTc148AggregatedPperAndGetToken() {
        DualReportManager.logInfo(
                "TC_148 — STEP 1: reserve ACTIVE account and capture baseline PPER (expect null/null pending)");
        Map<String, Object> account = reserveFreshBillEnrollmentAccount();
        String customerCode = account.get("customerCode").toString();
        String premisesCode = account.get("premisesCode").toString();
        String email = resolveEmail(account);
        testContext.setCustomerCode(customerCode);
        testContext.setPremisesCode(premisesCode);

        var baselinePper = PaperlessTokenPperEvidenceUtil.capturePperSnapshot(customerCode, premisesCode);
        PaperlessTokenPperEvidenceUtil.logRecentPperRecords(
                "TC_148 STEP1 baseline (null/null)", customerCode, premisesCode);
        DualReportManager.logInfo(
                "TC_148 — STEP 1 evidence: pendingBill=" + baselinePper.get("pendingBillType")
                        + ", pendingCorr=" + baselinePper.get("pendingCorrType"));

        DualReportManager.logInfo("TC_148 — STEP 2: Update enroll bill-only (null/null → E/null)");
        UpdatePaperlessCommunicationsRequest billPayload = BasePage.deserializeJsonToPojo(
                UpdatePaperlessCommunicationsLabel.update_paperless_communications.toString(),
                UpdatePaperlessCommunicationsRequest.class);
        billPayload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
        billPayload.setCustomerCode(customerCode);
        billPayload.setPremisesCode(premisesCode);
        billPayload.setUpdateBillDeliveryOption("E");
        billPayload.setUpdateCorrDeliveryOption(null);
        billPayload.setEmailAddress(email);
        String tokenAfterBill = enrollAndWaitForNewToken(billPayload, "TC_148 bill-only enroll E/null");
        var afterBillPper = PaperlessTokenPperEvidenceUtil.capturePperSnapshot(customerCode, premisesCode);
        PaperlessTokenPperEvidenceUtil.logPperBeforeAfter("TC_148 STEP2 bill-only", baselinePper, afterBillPper);
        PaperlessTokenPperEvidenceUtil.logRecentPperRecords(
                "TC_148 STEP2 after E/null", customerCode, premisesCode);
        DualReportManager.logInfo(
                "TC_148 — STEP 2 evidence: pendingBill=" + afterBillPper.get("pendingBillType")
                        + ", pendingCorr=" + afterBillPper.get("pendingCorrType")
                        + " (expect E/null)");

        DualReportManager.logInfo(
                "TC_148 — STEP 3: Update enroll both channels on same account (E/null → E/E); last state wins");
        UpdatePaperlessCommunicationsRequest bothPayload = BasePage.deserializeJsonToPojo(
                UpdatePaperlessCommunicationsLabel.update_paperless_communications.toString(),
                UpdatePaperlessCommunicationsRequest.class);
        bothPayload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
        bothPayload.setCustomerCode(customerCode);
        bothPayload.setPremisesCode(premisesCode);
        bothPayload.setUpdateBillDeliveryOption("E");
        bothPayload.setUpdateCorrDeliveryOption("E");
        bothPayload.setEmailAddress(email);
        postEnrollment(bothPayload, "TC_148 aggregated both-channels enroll E/E");

        String token = PaperlessConfirmationTokenUtil.tryGetLatestTokenIdentifier(customerCode, premisesCode);
        if (token == null || token.isBlank()) {
            token = tokenAfterBill;
        }
        testContext.setPaperlessConfirmationToken(token);

        var afterBothPper = PaperlessTokenPperEvidenceUtil.capturePperSnapshot(customerCode, premisesCode);
        PaperlessTokenPperEvidenceUtil.logPperBeforeAfter("TC_148 STEP3 aggregated E/E", afterBillPper, afterBothPper);
        PaperlessTokenPperEvidenceUtil.logRecentPperRecords(
                "TC_148 STEP3 after E/E (latest aggregated)", customerCode, premisesCode);
        DualReportManager.logInfo(
                "TC_148 — STEP 3 evidence: pendingBill=" + afterBothPper.get("pendingBillType")
                        + ", pendingCorr=" + afterBothPper.get("pendingCorrType")
                        + " (expect E/E — Confirm must apply this latest aggregated state)");
        DualReportManager.logInfo(
                "TC_148 — STEP 4: Confirm with current token; expect bill+corr confirmation from E/E aggregate");
        return token;
    }

    /**
     * TC_149: Banner email present and matching PPER at token create.
     * Banner email is cleared mid-Confirm (after token validation) by the page — clearing
     * before the API call returns 40287 (Email Address is Not Present).
     */
    String bootstrapTc149ClearBannerEmailBeforeConfirmAndGetToken() {
        DualReportManager.logInfo(
                "TC_149 — STEP 1: enroll ACTIVE bill with Banner email present (token create)");
        String token = bootstrapEnrollmentAndGetToken(
                UpdatePaperlessCommunicationsLabel.TC_98__Positive__Active_Account_Bill_Enrollment_Initiated_);
        String customerCode = testContext.getCustomerCode();
        DBAction bannerDb = ApplicationContext.get().getDbAction();

        String bannerEmailAtTokenCreate = bannerDb.getActiveBannerEmailForCustomer(customerCode);
        var pperAtCreate = PaperlessTokenPperEvidenceUtil.capturePperSnapshot(
                customerCode, testContext.getPremisesCode());
        String pperEmail = pperAtCreate.get("pendingEmail");
        DualReportManager.logInfo(
                "TC_149 — STEP 1 evidence (token create): bannerEmail='" + bannerEmailAtTokenCreate
                        + "', PPER email='" + pperEmail + "'");
        if (bannerEmailAtTokenCreate == null || bannerEmailAtTokenCreate.isBlank()) {
            throw new IllegalStateException(
                    "TC_149 requires Banner email present when token is created for " + customerCode);
        }
        DualReportManager.logInfo(
                "TC_149 — STEP 2: Banner email exists and matches PPER at token create. "
                        + "STEP 3 (clear Banner email) runs mid-Confirm after token validation "
                        + "so confirmation email is skipped without 40287.");
        TC149_SAVED_BANNER_EMAIL.set(bannerEmailAtTokenCreate);
        TC149_CUSTOMER_FOR_RESTORE.set(customerCode);
        return token;
    }

    /**
     * TC_149 STEP 3: expire Banner email during Confirm (after token validation).
     * Returns true when email is absent after expire.
     */
    static boolean clearBannerEmailMidConfirm(String customerCode) {
        if (customerCode == null || customerCode.isBlank()) {
            return false;
        }
        DBAction bannerDb = ApplicationContext.get().getDbAction();
        String before = bannerDb.getActiveBannerEmailForCustomer(customerCode);
        DualReportManager.logInfo(
                "TC_149 — STEP 3: clear Banner email DURING Confirm (after token validation). "
                        + "bannerEmail before clear='" + before + "'");
        long start = System.currentTimeMillis();
        try {
            bannerDb.expireActiveBannerEmailsQuiet(customerCode);
            DualReportManager.logDatabaseQuery(
                    DBQuery.UPDATE_EXPIRE_ACTIVE_BANNER_EMAIL_FOR_CUSTOMER
                            + "\n-- bind: customerCode='" + customerCode + "'",
                    "Expired active Banner GZBEMCP email(s) mid-Confirm for customer " + customerCode
                            + " (was '" + before + "')",
                    System.currentTimeMillis() - start);
        } catch (RuntimeException ex) {
            DualReportManager.logDatabaseQuery(
                    DBQuery.UPDATE_EXPIRE_ACTIVE_BANNER_EMAIL_FOR_CUSTOMER,
                    null,
                    System.currentTimeMillis() - start,
                    false,
                    ex.getMessage());
            throw ex;
        }
        String after = bannerDb.getActiveBannerEmailForCustomer(customerCode);
        DualReportManager.logInfo(
                "TC_149 — STEP 3 evidence: bannerEmail after mid-Confirm clear='"
                        + after + "' (expect null/blank → confirmation email skipped)");
        return after == null || after.isBlank();
    }

    /** Restores TC_149 Banner email after expire. Safe if setup never ran. */
    static void restoreTc149BannerEmail() {
        String customerCode = TC149_CUSTOMER_FOR_RESTORE.get();
        String savedEmail = TC149_SAVED_BANNER_EMAIL.get();
        TC149_CUSTOMER_FOR_RESTORE.remove();
        TC149_SAVED_BANNER_EMAIL.remove();
        if (customerCode == null || customerCode.isBlank()) {
            return;
        }
        DualReportManager.logInfo(
                "TC_149 — restore Banner email for customer " + customerCode
                        + (savedEmail == null ? "" : " (saved='" + savedEmail + "')"));
        DBAction bannerDb = ApplicationContext.get().getDbAction();
        try {
            int unexpired = bannerDb.unexpireRecentlyExpiredBannerEmailsQuiet(customerCode);
            DualReportManager.logInfo(
                    "TC_149 — unexpired " + unexpired + " recently-expired GZBEMCP row(s) for " + customerCode);
            if (savedEmail != null && !savedEmail.isBlank()
                    && bannerDb.getActiveBannerEmailForCustomer(customerCode) == null) {
                bannerDb.updateActiveBannerEmailForCustomer(customerCode, savedEmail);
                DualReportManager.logInfo(
                        "TC_149 — re-applied Banner email '" + savedEmail + "' for " + customerCode);
            }
        } catch (RuntimeException ex) {
            log.warn("TC_149 could not restore Banner email for {}: {}", customerCode, ex.getMessage());
        }
    }

    /**
     * TC_150: unused Confirm token for ACTIVE bill enrollment.
     * Failed confirmation-email send is forced deterministically at Confirm time by setting
     * {@code NEW_TEST_EMAIL_ADDR} to a known undeliverable address (not blank — blank is TC_133 / 40321).
     */
    String bootstrapTc150ConfirmationEmailFailureAndGetToken() {
        DualReportManager.logInfo(
                "TC_150 — STEP 1: enroll ACTIVE bill with matching Banner email "
                        + "(token valid; preference update path must succeed on Confirm)");
        String token = bootstrapEnrollmentAndGetToken(
                UpdatePaperlessCommunicationsLabel.TC_98__Positive__Active_Account_Bill_Enrollment_Initiated_);
        String customerCode = testContext.getCustomerCode();
        String bannerEmail = ApplicationContext.get().getDbAction().getActiveBannerEmailForCustomer(customerCode);
        String failureAddress = resolveTc150FailureEmailAddress();
        TC150_FAILURE_EMAIL.set(failureAddress);
        DualReportManager.logInfo(
                "TC_150 — STEP 1 evidence: unused token ready; bannerEmail='" + bannerEmail
                        + "'; forced failure address for Confirm='" + failureAddress + "'");
        DualReportManager.logInfo(
                "TC_150 — STEP 2 (at Confirm): set NEW_TEST_EMAIL_ADDR to '" + failureAddress
                        + "' so Paperless Enrollment Confirmation email send is forced to fail, "
                        + "while enrollment must still return success=true / errorCode=0 "
                        + "(distinct from TC_133 blank override → 40321 rollback)");
        return token;
    }

    private static final ThreadLocal<String> TC150_FAILURE_EMAIL = new ThreadLocal<>();

    static String resolveTc150FailureEmailAddress() {
        String configured = ApplicationContext.get().getEnvConfig().getPaperlessEmailFailureAddress();
        if (configured != null && !configured.isBlank()) {
            return configured.trim();
        }
        return BannerTestEmailOverrideUtil.DEFAULT_UNDELIVERABLE_ADDRESS;
    }

    static String getTc150FailureEmailAddress() {
        String forced = TC150_FAILURE_EMAIL.get();
        if (forced != null && !forced.isBlank()) {
            return forced;
        }
        return resolveTc150FailureEmailAddress();
    }

    static void clearTc150FailureEmailAddress() {
        TC150_FAILURE_EMAIL.remove();
    }
}
