package com.gng.api.pages.csi.GetPaperlessEnrollmentEligibilityPage;

import com.gng.api.context.ApplicationContext;
import com.gng.api.db.DBAction;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.GetPaperlessEnrollmentEligibility.GetPaperlessEnrollmentEligibilityRequest;
import com.gng.api.pojo.CSIPojo.UpdatePaperlessCommunications.UpdatePaperlessCommunicationsRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.GetPaperlessEnrollmentEligibility.GetPaperlessEnrollmentEligibilityLabel;
import com.gng.api.steps.csi.UpdatePaperlessCommunications.UpdatePaperlessCommunicationsLabel;
import com.gng.api.util.FakerDataGenerator;
import com.gng.api.util.PaperlessConfirmationTokenUtil;
import com.gng.api.util.PaperlessEnrollmentAccountRegistry;
import com.gng.api.util.PaperlessEnrollmentUtil;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static com.gng.api.constants.ApiEndPoint.GET_PAPERLESS_ENROLLMENT_ELIGIBILITY;
import static com.gng.api.constants.ApiEndPoint.UPDATE_PAPERLESS_COMMUNICATIONS;

@Slf4j
public class GetPaperlessEnrollmentEligibilitySetupHelper {

    private static final int BOOTSTRAP_MAX_ATTEMPTS = 3;

    private final TestContext testContext;

    public GetPaperlessEnrollmentEligibilitySetupHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    public Map<String, Object> ensureActivePendingCorrEnrollment() {
        DBAction dbAction = ApplicationContext.get().getDbAction();
        Map<String, Object> account = dbAction.tryGetActiveAccountWithPendingCorrEnrollment();
        if (account != null && probeDeliveryOption(account, "correspondenceDeliveryOption", "I")) {
            log.info("Using ACTIVE pending corr PPER account {}/{}",
                    account.get("customerCode"), account.get("premisesCode"));
            return account;
        }

        for (int attempt = 1; attempt <= BOOTSTRAP_MAX_ATTEMPTS; attempt++) {
            account = reserveFreshCorrAccount(dbAction);
            initiateCorrEnrollment(account);
            if (probeDeliveryOption(account, "correspondenceDeliveryOption", "I")) {
                log.info("Bootstrapped ACTIVE pending corr PPER for {}/{} (attempt {})",
                        account.get("customerCode"), account.get("premisesCode"), attempt);
                return account;
            }
            log.warn("Pending corr bootstrap attempt {}: expected correspondenceDeliveryOption=I for {}/{}",
                    attempt, account.get("customerCode"), account.get("premisesCode"));
        }
        throw new IllegalStateException(
                "Could not resolve ACTIVE account with pending corr PPER returning correspondenceDeliveryOption=I.");
    }

    public Map<String, Object> ensureActivePendingBillEnrollment() {
        DBAction dbAction = ApplicationContext.get().getDbAction();
        Map<String, Object> account = dbAction.tryGetActiveAccountWithPendingBillEnrollment();
        if (account != null && probeDeliveryOption(account, "billDeliveryOption", "I")) {
            log.info("Using ACTIVE pending bill PPER account {}/{}",
                    account.get("customerCode"), account.get("premisesCode"));
            return account;
        }

        for (int attempt = 1; attempt <= BOOTSTRAP_MAX_ATTEMPTS; attempt++) {
            account = reserveFreshBillAccount(dbAction);
            initiateBillEnrollment(account);
            if (probeDeliveryOption(account, "billDeliveryOption", "I")) {
                log.info("Bootstrapped ACTIVE pending bill PPER for {}/{} (attempt {})",
                        account.get("customerCode"), account.get("premisesCode"), attempt);
                return account;
            }
            log.warn("Pending bill bootstrap attempt {}: expected billDeliveryOption=I for {}/{}",
                    attempt, account.get("customerCode"), account.get("premisesCode"));
        }
        throw new IllegalStateException(
                "Could not resolve ACTIVE account with pending bill PPER returning billDeliveryOption=I.");
    }

    Map<String, Object> ensureActiveNoEmailPendingBillEnrollment() {
        DBAction dbAction = ApplicationContext.get().getDbAction();

        Map<String, Object> account = dbAction.tryGetActiveAccountWithNoEmailAndPendingBillEnrollment();
        if (account != null && probeReasonCode(account, "paperlessBillIneligReasonCode", 3)) {
            return account;
        }

        for (int attempt = 1; attempt <= BOOTSTRAP_MAX_ATTEMPTS; attempt++) {
            account = reserveFreshBillAccount(dbAction);
            initiateBillEnrollment(account);
            dbAction.expireActiveBannerEmailsQuiet(readDbString(account, "customerCode"));
            if (probeReasonCode(account, "paperlessBillIneligReasonCode", 3)) {
                log.info("Bootstrapped no-email ACTIVE pending bill PPER for {}/{} (attempt {})",
                        account.get("customerCode"), account.get("premisesCode"), attempt);
                return account;
            }
            log.warn("No-email pending bill bootstrap attempt {}: expected paperlessBillIneligReasonCode=3 for {}/{}",
                    attempt, account.get("customerCode"), account.get("premisesCode"));
        }
        throw new IllegalStateException(
                "Could not resolve ACTIVE no-email account with pending bill PPER returning "
                        + "paperlessBillIneligReasonCode=3.");
    }

    Map<String, Object> ensureNewUnconfirmedBillPreference() {
        DBAction dbAction = ApplicationContext.get().getDbAction();

        // Prefer expired/unconfirmed over active PPER (active returns billDeliveryOption=I)
        for (Map<String, Object> candidate : dbAction.listNewAccountsWithExpiredUnconfirmedBillEnrollment(25, true)) {
            if (probeDeliveryOption(candidate, "billDeliveryOption", "P")) {
                log.info("TC_204 using NEW expired unconfirmed bill account {}/{}",
                        candidate.get("customerCode"), candidate.get("premisesCode"));
                return candidate;
            }
        }

        Map<String, Object> account = dbAction.tryGetNewAccountWithExpiredUnconfirmedBillPreference();
        if (account != null && probeDeliveryOption(account, "billDeliveryOption", "P")) {
            log.info("TC_204 using NEW expired unconfirmed bill preference account {}/{}",
                    account.get("customerCode"), account.get("premisesCode"));
            return account;
        }

        for (int attempt = 1; attempt <= BOOTSTRAP_MAX_ATTEMPTS; attempt++) {
            account = reserveNewBillAccount(dbAction);
            initiateNewUnconfirmedBillEnrollment(account);
            expireActivePaperlessEnrollment(account);
            if (probeDeliveryOption(account, "billDeliveryOption", "P")) {
                log.info("TC_204 bootstrapped NEW unconfirmed bill preference for {}/{} (attempt {})",
                        account.get("customerCode"), account.get("premisesCode"), attempt);
                return account;
            }
            log.warn("TC_204 bootstrap attempt {}: eligibility probe expected bill=P for {}/{}",
                    attempt, account.get("customerCode"), account.get("premisesCode"));
        }

        throw new IllegalStateException(
                "Could not resolve NEW account with unconfirmed bill preference returning billDeliveryOption=P. "
                        + "Active PPER enrollment returns I (reason 3); expire OCSEPCI/custadv after enroll or seed "
                        + "expired unconfirmed bill preference in UAT.");
    }

    private void expireActivePaperlessEnrollment(Map<String, Object> account) {
        String customerCode = readDbString(account, "customerCode");
        String premisesCode = readDbString(account, "premisesCode");
        ApplicationContext.get().getDbAction()
                .expireValidConfirmationTokensForAccount(customerCode, premisesCode);
        log.info("Expired active PPER tokens for NEW unconfirmed bill setup {}/{}", customerCode, premisesCode);
    }

    private boolean isNewAccount(Map<String, Object> account) {
        return "N".equalsIgnoreCase(readDbStringOptional(account, "accountStatus"));
    }

    private void initiateNewUnconfirmedBillEnrollment(Map<String, Object> accountData) {
        UpdatePaperlessCommunicationsRequest payload = buildEnrollmentPayload(accountData);
        payload.setUpdateBillDeliveryOption("E");
        payload.setUpdateCorrDeliveryOption(null);
        postEnrollment(payload, "Bootstrap NEW unconfirmed bill preference (TC_97 pattern)");
    }

    private boolean probeDeliveryOption(Map<String, Object> account, String fieldName, String expected) {
        String actual = probeEligibilityField(account, fieldName);
        if (actual == null) {
            return false;
        }
        boolean matches = expected.equalsIgnoreCase(actual);
        if (!matches) {
            log.debug("Eligibility probe {}/{}: {}={} (expected {})",
                    account.get("customerCode"), account.get("premisesCode"), fieldName, actual, expected);
        }
        return matches;
    }

    private boolean probeReasonCode(Map<String, Object> account, String fieldName, int expected) {
        Response response = callGetPaperlessEnrollmentEligibility(account);
        if (response.jsonPath().getInt("errorCode") != 0) {
            return false;
        }
        Integer actual = response.jsonPath().get("data." + fieldName);
        return actual != null && actual == expected;
    }

    private String probeEligibilityField(Map<String, Object> account, String fieldName) {
        Response response = callGetPaperlessEnrollmentEligibility(account);
        if (response.jsonPath().getInt("errorCode") != 0) {
            return null;
        }
        return response.jsonPath().getString("data." + fieldName);
    }

    private Response callGetPaperlessEnrollmentEligibility(Map<String, Object> account) {
        GetPaperlessEnrollmentEligibilityRequest payload = BasePage.deserializeJsonToPojo(
                GetPaperlessEnrollmentEligibilityLabel.get_paperless_enrollment_eligibility.toString(),
                GetPaperlessEnrollmentEligibilityRequest.class);
        payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
        payload.setCustomerCode(readDbString(account, "customerCode"));
        payload.setPremisesCode(readDbString(account, "premisesCode"));

        String uri = ApplicationContext.get().getEnvConfig().getBaseUri() + GET_PAPERLESS_ENROLLMENT_ELIGIBILITY;
        return io.restassured.RestAssured.given()
                .relaxedHTTPSValidation()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + testContext.getAuthToken())
                .body(payload)
                .post(uri);
    }

    private Map<String, Object> reserveFreshCorrAccount(DBAction dbAction) {
        for (int attempt = 0; attempt < 25; attempt++) {
            Map<String, Object> candidate = dbAction.getActiveAccountForFreshCorrEnrollmentStrict();
            if (PaperlessEnrollmentAccountRegistry.tryReserve(candidate)) {
                return candidate;
            }
        }
        throw new IllegalStateException("No unreserved ACTIVE account for fresh correspondence enrollment setup.");
    }

    private Map<String, Object> reserveFreshBillAccount(DBAction dbAction) {
        for (int attempt = 0; attempt < 25; attempt++) {
            Map<String, Object> candidate = dbAction.getActiveAccountForFreshBillEnrollmentStrict();
            if (PaperlessEnrollmentAccountRegistry.tryReserve(candidate)) {
                return candidate;
            }
        }
        throw new IllegalStateException("No unreserved ACTIVE account for fresh bill enrollment setup.");
    }

    private Map<String, Object> reserveNewBillAccount(DBAction dbAction) {
        for (int attempt = 0; attempt < 25; attempt++) {
            Map<String, Object> candidate;
            try {
                candidate = dbAction.getActiveAccountForTc96();
            } catch (IllegalStateException ex) {
                candidate = dbAction.getNewPaperlessEligibleAccountWithNoToken();
            }
            if (!isNewAccount(candidate)) {
                continue;
            }
            if (PaperlessEnrollmentAccountRegistry.tryReserve(candidate)) {
                return candidate;
            }
        }
        Map<String, Object> fallback = dbAction.getNewPaperlessEligibleAccountWithNoToken();
        if (!isNewAccount(fallback)) {
            throw new IllegalStateException(
                    "TC_204 requires NEW account but resolved status="
                            + readDbStringOptional(fallback, "accountStatus"));
        }
        PaperlessEnrollmentAccountRegistry.tryReserve(fallback);
        return fallback;
    }

    private void initiateCorrEnrollment(Map<String, Object> accountData) {
        UpdatePaperlessCommunicationsRequest payload = buildEnrollmentPayload(accountData);
        payload.setUpdateBillDeliveryOption(null);
        payload.setUpdateCorrDeliveryOption("E");
        postEnrollment(payload, "Bootstrap pending correspondence enrollment");
    }

    private void initiateBillEnrollment(Map<String, Object> accountData) {
        UpdatePaperlessCommunicationsRequest payload = buildEnrollmentPayload(accountData);
        payload.setUpdateBillDeliveryOption("E");
        payload.setUpdateCorrDeliveryOption(null);
        postEnrollment(payload, "Bootstrap pending bill enrollment");
    }

    private UpdatePaperlessCommunicationsRequest buildEnrollmentPayload(Map<String, Object> accountData) {
        UpdatePaperlessCommunicationsRequest payload = BasePage.deserializeJsonToPojo(
                UpdatePaperlessCommunicationsLabel.update_paperless_communications.toString(),
                UpdatePaperlessCommunicationsRequest.class);
        payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
        payload.setCustomerCode(readDbString(accountData, "customerCode"));
        payload.setPremisesCode(readDbString(accountData, "premisesCode"));
        payload.setEmailAddress(resolveBannerEmail(accountData));
        return payload;
    }

    private void postEnrollment(UpdatePaperlessCommunicationsRequest enrollPayload, String label) {
        String customerCode = enrollPayload.getCustomerCode();
        String premisesCode = enrollPayload.getPremisesCode();
        long baseline = PaperlessConfirmationTokenUtil.getBaselineVerificationId(customerCode, premisesCode);

        String uri = ApplicationContext.get().getEnvConfig().getBaseUri() + UPDATE_PAPERLESS_COMMUNICATIONS;
        Response enrollResponse = io.restassured.RestAssured.given()
                .relaxedHTTPSValidation()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + testContext.getAuthToken())
                .body(enrollPayload)
                .post(uri);
        log.info("{} returned HTTP {} for {}/{}", label, enrollResponse.statusCode(), customerCode, premisesCode);
        int enrollErrorCode = enrollResponse.jsonPath().getInt("errorCode");
        if (enrollErrorCode != 0) {
            throw new IllegalStateException(
                    label + " failed with errorCode " + enrollErrorCode + ": " + enrollResponse.asString());
        }

        PaperlessConfirmationTokenUtil.waitForTokenCreatedAfterEnroll(customerCode, premisesCode, baseline);
    }

    private static String resolveBannerEmail(Map<String, Object> accountData) {
        String email = PaperlessEnrollmentUtil.resolveBannerEmail(accountData);
        if (email == null) {
            throw new IllegalStateException("No banner email in account data: " + accountData.keySet());
        }
        return email;
    }

    private static String readDbString(Map<String, Object> row, String key) {
        if (row == null) {
            return null;
        }
        Object direct = row.get(key);
        if (direct != null) {
            return direct.toString();
        }
        for (Map.Entry<String, Object> entry : row.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(key) && entry.getValue() != null) {
                return entry.getValue().toString();
            }
        }
        throw new IllegalStateException("Key '" + key + "' not found in DB row keys: " + row.keySet());
    }

    private static String readDbStringOptional(Map<String, Object> row, String key) {
        if (row == null) {
            return null;
        }
        Object direct = row.get(key);
        if (direct != null) {
            return direct.toString();
        }
        for (Map.Entry<String, Object> entry : row.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(key)) {
                return entry.getValue() != null ? entry.getValue().toString() : null;
            }
        }
        return null;
    }
}
