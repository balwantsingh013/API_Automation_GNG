package com.gng.api.pages.csi.UpdatePaperlessCommunicationsPage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.db.DBAction;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.UpdatePaperlessCommunications.UpdatePaperlessCommunicationsRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.UpdatePaperlessCommunications.UpdatePaperlessCommunicationsLabel;
import com.gng.api.util.FakerDataGenerator;
import com.gng.api.util.PaperlessEnrollmentAccountRegistry;
import com.gng.api.util.PaperlessEnrollmentUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class UpdatePaperlessCommunicationsApiHelper {

    private final TestContext testContext;

    /** Shared across suite: TC_83 account must not be reused by TC_84. */
    private static Map<String, Object> tc83AccountWithCorrEnrollment;

    /** Shared: TC_99/96 enroll → TC_103/107 unenroll or token reuse. */
    private static Map<String, Object> tc96NewAccountWithPendingBill;

    /** Shared: TC_101 enroll → TC_105 both-channels unenroll. */
    private static Map<String, Object> tc98NewAccountWithPendingBothChannels;

    /** Shared: TC_98 enroll → TC_114 PPER cleanup unenroll. */
    private static Map<String, Object> tc111ActiveAccountWithPendingBill;

    /** Shared: confirmed E/E account for TC_97 / TC_104 unenroll setup. */
    private static Map<String, Object> tc101ActiveAccountBothChannelsEnrolled;

    private Map<String, Object> tc90ResolvedAccount;
    private boolean tc90RequiresPriorEnrollment;

    /** Shared: TC_92 enroll → TC_93/106 token reuse. */
    private static Map<String, Object> tc89AccountWithPendingToken;

    private Map<String, Object> tc104ResolvedAccount;
    private boolean tc104RequiresPriorEnrollment;

    void storeAccountForTc101(String customerCode, String premisesCode) {
        tc101ActiveAccountBothChannelsEnrolled = Map.of(
                "customerCode", customerCode,
                "premisesCode", premisesCode);
    }

    void clearAccountForTc101() {
        tc101ActiveAccountBothChannelsEnrolled = null;
    }

    void clearTc96NewAccountWithPendingBill() {
        tc96NewAccountWithPendingBill = null;
    }

    Map<String, Object> getTc96NewAccountWithPendingBill() {
        if (tc96NewAccountWithPendingBill == null) {
            throw new IllegalStateException("NEW account with pending bill enrollment was not established");
        }
        return tc96NewAccountWithPendingBill;
    }

    void clearTc98NewAccountWithPendingBothChannels() {
        tc98NewAccountWithPendingBothChannels = null;
    }

    void clearTc111ActiveAccountWithPendingBill() {
        tc111ActiveAccountWithPendingBill = null;
    }

    void storeTc111ActiveAccountWithPendingBill(Map<String, Object> accountData) {
        tc111ActiveAccountWithPendingBill = accountData;
    }

    Map<String, Object> reserveFreshActiveAccountForEnrollment() {
        return reserveActiveAccountForFreshBillEnrollment();
    }

    /** ACTIVE P/P account with no pending OCSEPCI for TC_112/113 cross-channel flows. */
    Map<String, Object> reserveActiveAccountForCrossChannelEnrollment() {
        DBAction dbAction = ApplicationContext.get().getDbAction();
        for (Map<String, Object> candidate : dbAction.listActiveBothChannelsEnrollmentCandidates()) {
            if (PaperlessEnrollmentAccountRegistry.tryReserve(candidate)) {
                log.info("Reserved cross-channel enrollment account {}/{}",
                        candidate.get("customerCode"), candidate.get("premisesCode"));
                return candidate;
            }
        }
        Map<String, Object> fallback = dbAction.getActiveAccountForTc97();
        if (PaperlessEnrollmentAccountRegistry.tryReserve(fallback)) {
            log.info("Reserved cross-channel enrollment account {}/{} (TC_97 fallback)",
                    fallback.get("customerCode"), fallback.get("premisesCode"));
            return fallback;
        }
        throw new IllegalStateException(
                "No unreserved ACTIVE account with bill=P and corr=P for cross-channel aggregation. "
                        + "All TC_97 candidates are already reserved in this suite run.");
    }

    void prepareTc90Account() {
        tc90ResolvedAccount = null;
        tc90RequiresPriorEnrollment = false;
        try {
            tc90ResolvedAccount = ApplicationContext.get().getDbAction().getActiveAccountForTc90();
        } catch (IllegalStateException ex) {
            if (tc89AccountWithPendingToken != null) {
                tc90ResolvedAccount = tc89AccountWithPendingToken;
                log.info("TC_90 reusing TC_89 account {}/{} (pending token from prior TC_89 run)",
                        tc90ResolvedAccount.get("customerCode"), tc90ResolvedAccount.get("premisesCode"));
            } else {
                tc90ResolvedAccount = ApplicationContext.get().getDbAction().getActiveAccountForTc89();
                tc90RequiresPriorEnrollment = true;
                log.info("TC_90: no account with valid unused token; running prior enroll on {}/{}",
                        tc90ResolvedAccount.get("customerCode"), tc90ResolvedAccount.get("premisesCode"));
            }
        }
    }

    Map<String, Object> getTc90ResolvedAccount() {
        if (tc90ResolvedAccount == null) {
            throw new IllegalStateException("TC_90 account not resolved; call prepareTc90Account() first");
        }
        return tc90ResolvedAccount;
    }

    boolean isTc90PriorEnrollmentRequired() {
        return tc90RequiresPriorEnrollment;
    }

    /** Resolves NEW account for TC_107 token reuse; prior-enrolls when no unused token is visible. */
    void prepareTc104Account() {
        tc104ResolvedAccount = null;
        tc104RequiresPriorEnrollment = false;

        if (tc96NewAccountWithPendingBill != null) {
            String customerCode = getDbString(tc96NewAccountWithPendingBill, "customerCode");
            String premisesCode = getDbString(tc96NewAccountWithPendingBill, "premisesCode");
            String visibleToken = ApplicationContext.get().getDbAction()
                    .tryGetLatestValidTokenIdentifierQuiet(customerCode, premisesCode);
            if (visibleToken != null) {
                tc104ResolvedAccount = tc96NewAccountWithPendingBill;
                log.info("TC_107: reusing TC_99 NEW account {}/{} with unused token (single-call reuse)",
                        customerCode, premisesCode);
                return;
            }
            log.warn("TC_107: TC_99 account {}/{} no longer has an unused token; seeding a fresh NEW account",
                    customerCode, premisesCode);
        }

        // Prefer prior-enroll over Banner-only OCSEPCI "valid token" matches (UAT1 flaky INITIATED).
        tc104ResolvedAccount = ApplicationContext.get().getDbAction().getActiveAccountForTc96();
        tc104RequiresPriorEnrollment = true;
        log.info("TC_107: will prior-enroll fresh NEW account {}/{} then reuse token",
                getDbString(tc104ResolvedAccount, "customerCode"),
                getDbString(tc104ResolvedAccount, "premisesCode"));
    }

    Map<String, Object> getTc104ResolvedAccount() {
        if (tc104ResolvedAccount == null) {
            throw new IllegalStateException("TC_104 account not resolved; call prepareTc104Account() first");
        }
        return tc104ResolvedAccount;
    }

    boolean isTc104PriorEnrollmentRequired() {
        return tc104RequiresPriorEnrollment;
    }

    private Map<String, Object> tc105ResolvedAccount;
    private boolean tc105RequiresExpiredTokenSetup;

    private Map<String, Object> tc106ResolvedAccount;
    private boolean tc106RequiresExpiredTokenSetup;

    private Map<String, Object> tc116ResolvedAccount;

    void prepareTc105Account() {
        if (tc105ResolvedAccount != null) {
            return;
        }
        tc105RequiresExpiredTokenSetup = false;
        try {
            tc105ResolvedAccount = ApplicationContext.get().getDbAction().getActiveAccountForTc105();
        } catch (IllegalStateException ex) {
            tc105ResolvedAccount = ApplicationContext.get().getDbAction().getActiveAccountForFreshBillEnrollmentStrict();
            tc105RequiresExpiredTokenSetup = true;
            log.info("TC_105: no account with expired/used token in Banner; will create pending enrollment then expire token");
        }
    }

    Map<String, Object> getTc105ResolvedAccount() {
        prepareTc105Account();
        return tc105ResolvedAccount;
    }

    boolean isTc105ExpiredTokenSetupRequired() {
        prepareTc105Account();
        return tc105RequiresExpiredTokenSetup;
    }

    void resetTc106AccountState() {
        tc106ResolvedAccount = null;
        tc106RequiresExpiredTokenSetup = false;
    }

    void prepareTc106Account() {
        if (tc106ResolvedAccount != null) {
            return;
        }
        tc106RequiresExpiredTokenSetup = false;
        try {
            tc106ResolvedAccount = ApplicationContext.get().getDbAction().getNewAccountForTc106();
        } catch (IllegalStateException ex) {
            tc106ResolvedAccount = ApplicationContext.get().getDbAction().getNewPaperlessEligibleAccountWithNoToken();
            tc106RequiresExpiredTokenSetup = true;
            log.info("TC_106: no NEW account with expired/used token in Banner; will create pending enrollment then expire token");
        }
    }

    Map<String, Object> getTc106ResolvedAccount() {
        prepareTc106Account();
        return tc106ResolvedAccount;
    }

    boolean isTc106ExpiredTokenSetupRequired() {
        prepareTc106Account();
        return tc106RequiresExpiredTokenSetup;
    }

    void prepareTc116Account() {
        if (tc116ResolvedAccount != null) {
            return;
        }
        try {
            tc116ResolvedAccount = ApplicationContext.get().getDbAction().getActiveAccountForTc116();
        } catch (IllegalStateException ex) {
            tc116ResolvedAccount = ApplicationContext.get().getDbAction().getActiveAccountForFreshBillEnrollmentStrict();
            log.warn("TC_116: no email-mismatch pending token in Banner; using fresh enrollment account {}/{}",
                    tc116ResolvedAccount.get("customerCode"), tc116ResolvedAccount.get("premisesCode"));
        }
    }

    Map<String, Object> getTc116ResolvedAccount() {
        prepareTc116Account();
        return tc116ResolvedAccount;
    }

    static String generateNewTestEmail() {
        return "newemail_" + FakerDataGenerator.generateAlphanumeric(5) + "@test.com";
    }

    static Map<String, Object> accountFromPayload(UpdatePaperlessCommunicationsRequest payload) {
        return Map.of(
                "customerCode", payload.getCustomerCode(),
                "premisesCode", payload.getPremisesCode(),
                "bannerEmail", payload.getEmailAddress());
    }

    void populateBillEnrollmentPayload(UpdatePaperlessCommunicationsRequest payload,
                                               Map<String, Object> accountData) {
        payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
        payload.setCustomerCode(getDbString(accountData, "customerCode"));
        payload.setPremisesCode(getDbString(accountData, "premisesCode"));
        payload.setUpdateBillDeliveryOption("E");
        payload.setUpdateCorrDeliveryOption(null);
        payload.setEmailAddress(resolveBannerEmail(accountData));
    }

    void populateCorrOnlyEnrollmentPayload(UpdatePaperlessCommunicationsRequest payload,
                                           Map<String, Object> accountData) {
        payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
        payload.setCustomerCode(getDbString(accountData, "customerCode"));
        payload.setPremisesCode(getDbString(accountData, "premisesCode"));
        payload.setUpdateBillDeliveryOption(null);
        payload.setUpdateCorrDeliveryOption("E");
        payload.setEmailAddress(resolveBannerEmail(accountData));
    }

    void populateBothChannelsEnrollmentPayload(UpdatePaperlessCommunicationsRequest payload,
                                               Map<String, Object> accountData) {
        payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
        payload.setCustomerCode(getDbString(accountData, "customerCode"));
        payload.setPremisesCode(getDbString(accountData, "premisesCode"));
        payload.setUpdateBillDeliveryOption("E");
        payload.setUpdateCorrDeliveryOption("E");
        payload.setEmailAddress(resolveBannerEmail(accountData));
    }

    void populateBillUnenrollPayload(UpdatePaperlessCommunicationsRequest payload,
                                     Map<String, Object> accountData) {
        payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
        payload.setCustomerCode(getDbString(accountData, "customerCode"));
        payload.setPremisesCode(getDbString(accountData, "premisesCode"));
        payload.setUpdateBillDeliveryOption("P");
        payload.setUpdateCorrDeliveryOption(null);
        payload.setEmailAddress(null);
    }

    static String getDbString(Map<String, Object> row, String key) {
        if (row == null) {
            throw new IllegalStateException("DB row is null for key: " + key);
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

    static String resolveBannerEmail(Map<String, Object> accountData) {
        String email = PaperlessEnrollmentUtil.resolveBannerEmail(accountData);
        if (email == null) {
            throw new IllegalStateException("No banner email in account data: " + accountData.keySet());
        }
        return email;
    }

    static String resolveBannerEmailOrDefault(Map<String, Object> accountData, String fallback) {
        String email = PaperlessEnrollmentUtil.resolveBannerEmail(accountData);
        return email != null ? email : fallback;
    }

    public UpdatePaperlessCommunicationsApiHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    UpdatePaperlessCommunicationsRequest preparePayload(UpdatePaperlessCommunicationsLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        String jsonFileName = apiLabel.equals(UpdatePaperlessCommunicationsLabel.update_paperless_communications)
                ? UpdatePaperlessCommunicationsLabel.update_paperless_communications.toString()
                : UpdatePaperlessCommunicationsLabel.update_paperless_communications_mandatory.toString();
        return BasePage.deserializeJsonToPojo(jsonFileName, UpdatePaperlessCommunicationsRequest.class);
    }

    public void preparePayloadForTestCondition(UpdatePaperlessCommunicationsRequest payload, UpdatePaperlessCommunicationsLabel testCondition) {
        Map<String, Object> accountData = null;

        switch (testCondition) {

            case TC_53__Negative__Missing_Request_ID_:
                payload.setRequestID("");
                break;

            case TC_54__Negative__Invalid_Request_ID_Length_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(33));
                break;

            case TC_55__Negative__Duplicate_Request_ID_:
                payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());
                break;

            case TC_56__Negative__Missing_customerCode_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode("");
                break;

            case TC_57__Negative__Invalid_customerCode_Length_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateDigits(10));
                break;

            case TC_58__Negative__Invalid_customerCode_Format_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateString(6));
                break;

            case TC_59__Negative__Missing_premisesCode_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPremisesCode("");
                break;

            case TC_60__Negative__Invalid_premisesCode_Length_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(8));
                break;

            case TC_61__Negative__Invalid_premisesCode_Format_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPremisesCode(FakerDataGenerator.generateString(6));
                break;

            case TC_62__Negative__Invalid_Account_Number__Invalid_Account_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode("9988776");
                ApplicationContext.get().getDbAction().getUserAccountInfo("9988776");
                payload.setPremisesCode(FakerDataGenerator.generateDigits(6));
                break;

            case TC_63__Negative__Missing_Both_Preference_Values_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUpdateBillDeliveryOption(null);
                payload.setUpdateCorrDeliveryOption(null);
                break;

            case TC_64__Negative__Invalid_Bill_Preference_Value_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUpdateBillDeliveryOption("X");
                break;

            case TC_65__Negative__Invalid_Correspondence_Preference_Value_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUpdateCorrDeliveryOption("X");
                break;

            case TC_66__Negative__Mixed_Enrollment_and_Unenrollment_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActivePaperlessEligibleAccount();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                payload.setUpdateBillDeliveryOption("E");
                payload.setUpdateCorrDeliveryOption("P");
                payload.setEmailAddress("test@test.com");
                break;

            case TC_67__Negative__Missing_Email_for_Enrollment_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActivePaperlessEligibleAccount();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                payload.setUpdateBillDeliveryOption("E");
                payload.setEmailAddress("");
                break;

            case TC_68__Negative__Invalid_Email_Format_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActivePaperlessEligibleAccount();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                payload.setUpdateBillDeliveryOption("E");
                payload.setEmailAddress("invalidemail");
                break;

            case TC_69__Negative__Bill_Enrollment_Ineligible__Not_Fiserv_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getBillEnrollmentIneligibleAccount();
                payload.setCustomerCode(getDbString(accountData, "customerCode"));
                payload.setPremisesCode(getDbString(accountData, "premisesCode"));
                payload.setUpdateBillDeliveryOption("E");
                payload.setUpdateCorrDeliveryOption(null);
                payload.setEmailAddress(resolveBannerEmailOrDefault(accountData, "test@vertexone.net"));
                break;

            case TC_70__Negative__Bill_Enrollment_Ineligible__Fiserv_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getFiservBillAccount();
                payload.setCustomerCode(getDbString(accountData, "customerCode"));
                payload.setPremisesCode(getDbString(accountData, "premisesCode"));
                payload.setUpdateBillDeliveryOption("E");
                payload.setUpdateCorrDeliveryOption(null);
                payload.setEmailAddress(resolveBannerEmail(accountData));
                break;

            case TC_71__Negative__Correspondence_Enrollment_Ineligible_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getCorrEnrollmentIneligibleAccount();
                payload.setCustomerCode(getDbString(accountData, "customerCode"));
                payload.setPremisesCode(getDbString(accountData, "premisesCode"));
                payload.setUpdateBillDeliveryOption(null);
                payload.setUpdateCorrDeliveryOption("E");
                payload.setEmailAddress(resolveBannerEmailOrDefault(accountData, "test@vertexone.net"));
                break;

            case TC_72__Negative__Both_Channels_Enrollment_Ineligible_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getBothChannelsIneligibleAccount();
                payload.setCustomerCode(getDbString(accountData, "customerCode"));
                payload.setPremisesCode(getDbString(accountData, "premisesCode"));
                payload.setUpdateBillDeliveryOption("E");
                payload.setUpdateCorrDeliveryOption("E");
                payload.setEmailAddress("test@vertexone.net");
                break;

            case TC_73__Negative__Atomic_Eligibility_Failure_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getOneChannelIneligibleAccount();
                payload.setCustomerCode(getDbString(accountData, "customerCode"));
                payload.setPremisesCode(getDbString(accountData, "premisesCode"));
                payload.setUpdateBillDeliveryOption("E");
                payload.setUpdateCorrDeliveryOption("E");
                payload.setEmailAddress(resolveBannerEmailOrDefault(accountData, "test@vertexone.net"));
                break;

            case TC_74__Negative__Unenrollment_Not_Applicable__Not_Enrolled_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActiveAccountWithPaperBillDeliveryAndBannerEmail();
                payload.setCustomerCode(getDbString(accountData, "customerCode"));
                payload.setPremisesCode(getDbString(accountData, "premisesCode"));
                payload.setUpdateBillDeliveryOption("P");
                payload.setUpdateCorrDeliveryOption(null);
                payload.setEmailAddress(null);
                break;

            case TC_75__Negative__Fiserv_Bill_Unenrollment_Not_Allowed_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActiveAccountWithFiservBillDelivery();
                payload.setCustomerCode(getDbString(accountData, "customerCode"));
                payload.setPremisesCode(getDbString(accountData, "premisesCode"));
                payload.setUpdateBillDeliveryOption("P");
                payload.setUpdateCorrDeliveryOption(null);
                payload.setEmailAddress(null);
                break;

            case TC_76__Negative__Cannot_Unenroll_Initiated_State_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getAccountInInitiatedPaperlessState();
                payload.setCustomerCode(getDbString(accountData, "customerCode"));
                payload.setPremisesCode(getDbString(accountData, "premisesCode"));
                payload.setUpdateBillDeliveryOption("P");
                payload.setUpdateCorrDeliveryOption(null);
                payload.setEmailAddress(null);
                break;

            case TC_77__Negative__Enrollment_Email_Failure_Rollback__ACTIVE_:
                // Valid Banner email in payload; Page blanks NEW_TEST_EMAIL_ADDR → 40281
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActivePaperlessEligibleAccountWithEmailFailure();
                populateBillEnrollmentPayload(payload, accountData);
                break;

            case TC_78__Negative__Enrollment_Email_Failure_Rollback__NEW_:
                // Valid Banner email in payload; Page blanks NEW_TEST_EMAIL_ADDR → 40281
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getNewPaperlessEligibleAccountWithEmailFailure();
                populateBillEnrollmentPayload(payload, accountData);
                break;

            case TC_79__Negative__Email_Ignored_for_P_Requests__Not_Enrolled_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActiveAccountWithPaperBillDeliveryAndBannerEmail();
                payload.setCustomerCode(getDbString(accountData, "customerCode"));
                payload.setPremisesCode(getDbString(accountData, "premisesCode"));
                payload.setUpdateBillDeliveryOption("P");
                payload.setUpdateCorrDeliveryOption("P");
                payload.setEmailAddress(null);
                break;

            case TC_80__Negative__Valid_Email_Address_Not_Allowed_on_Unenrollment_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActiveAccountWithPaperBillDeliveryAndBannerEmail();
                payload.setCustomerCode(getDbString(accountData, "customerCode"));
                payload.setPremisesCode(getDbString(accountData, "premisesCode"));
                payload.setUpdateBillDeliveryOption("P");
                payload.setUpdateCorrDeliveryOption("P");
                payload.setEmailAddress("test@vertexone.net");
                break;

            case TC_81__Negative__Invalid_Email_Address_Not_Allowed_on_Unenrollment_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActiveAccountWithPaperBillDeliveryAndBannerEmail();
                payload.setCustomerCode(getDbString(accountData, "customerCode"));
                payload.setPremisesCode(getDbString(accountData, "premisesCode"));
                payload.setUpdateBillDeliveryOption("P");
                payload.setUpdateCorrDeliveryOption("P");
                payload.setEmailAddress("invalidemail");
                break;

            case TC_82__Positive__UpdatePaperlessCommunications_accountType_Format__ACTIVE_:
                accountData = reserveActiveAccountForFreshBillEnrollment();
                populateBillEnrollmentPayload(payload, accountData);
                break;

            case TC_83__Positive__UpdatePaperlessCommunications_accountType_Format__NEW_:
                accountData = reserveNewAccountForBothChannelsEnrollment();
                populateNewBothChannelsEnrollmentPayload(payload, accountData);
                break;

            case TC_84__Positive__billDeliveryOptionStatus_Format__INITIATED_:
                accountData = reserveActiveAccountForFreshBillEnrollment();
                populateBillEnrollmentPayload(payload, accountData);
                break;

            case TC_85__Positive__billDeliveryOptionStatus_Format__UPDATED_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActiveAccountEnrolledInPaperlessBill();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                payload.setUpdateBillDeliveryOption("P");
                payload.setUpdateCorrDeliveryOption(null);
                payload.setEmailAddress(null);
                break;

            case TC_86__Positive__billDeliveryOptionStatus_Format__NO_CHANGE_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActiveAccountForTc83();
                tc83AccountWithCorrEnrollment = accountData;
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                payload.setUpdateBillDeliveryOption(null);
                payload.setUpdateCorrDeliveryOption("E");
                payload.setEmailAddress(resolveBannerEmail(accountData));
                break;

            case TC_87__Positive__corrDeliveryOptionStatus_Format__INITIATED_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = getActiveAccountForTc84();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                payload.setUpdateBillDeliveryOption("E");
                payload.setUpdateCorrDeliveryOption("E");
                payload.setEmailAddress(resolveBannerEmail(accountData));
                break;

            case TC_88__Positive__corrDeliveryOptionStatus_Format__UPDATED_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActiveAccountEnrolledInPaperlessCorr();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                payload.setUpdateBillDeliveryOption(null);
                payload.setUpdateCorrDeliveryOption("P");
                payload.setEmailAddress(null);
                break;

            case TC_89__Positive__corrDeliveryOptionStatus_Format__NO_CHANGE_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActiveAccountForTc86();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                payload.setUpdateBillDeliveryOption("E");
                payload.setUpdateCorrDeliveryOption(null);
                payload.setEmailAddress(resolveBannerEmail(accountData));
                break;

            case TC_90__Positive__linkExpiryDateTime_Format_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActiveAccountForTc87();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                payload.setUpdateBillDeliveryOption("E");
                payload.setUpdateCorrDeliveryOption(null);
                payload.setEmailAddress(resolveBannerEmail(accountData));
                break;

            case TC_91__Positive__linkExpiryDateTime_Null_When_No_Enrollment_Processed_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActiveAccountEnrolledInPaperlessBill();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                payload.setUpdateBillDeliveryOption("P");
                payload.setUpdateCorrDeliveryOption(null);
                payload.setEmailAddress(null);
                break;

            case TC_92__Positive__linkCreated_Format__true_:
                if (tc90RequiresPriorEnrollment && tc90ResolvedAccount != null) {
                    accountData = tc90ResolvedAccount;
                } else {
                    accountData = reserveActiveAccountForFreshBillEnrollment();
                }
                populateBillEnrollmentPayload(payload, accountData);
                tc89AccountWithPendingToken = accountData;
                break;

            case TC_93__Positive__linkCreated_Format__false_:
                if (tc90ResolvedAccount == null) {
                    prepareTc90Account();
                }
                populateBillEnrollmentPayload(payload, tc90ResolvedAccount);
                break;

            case TC_94__Positive__linkCreated_Null_When_No_Enrollment_Processed_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActiveAccountEnrolledInPaperlessBill();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                payload.setUpdateBillDeliveryOption("P");
                payload.setUpdateCorrDeliveryOption(null);
                payload.setEmailAddress(null);
                break;

            case TC_95__Positive__emailUpdated_Format__false_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActivePaperlessEligibleAccountWithNoToken();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                payload.setUpdateBillDeliveryOption("E");
                payload.setUpdateCorrDeliveryOption(null);
                payload.setEmailAddress(resolveBannerEmail(accountData));
                break;

            case TC_96__Positive__emailUpdated_Format__true_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActivePaperlessEligibleAccountWithNoToken();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                payload.setUpdateBillDeliveryOption("E");
                payload.setUpdateCorrDeliveryOption(null);
                payload.setEmailAddress("newemail_" + FakerDataGenerator.generateAlphanumeric(5) + "@test.com");
                break;

            case TC_97__Positive__Email_Ignored_for_P_Requests__Enrolled_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                if (tc101ActiveAccountBothChannelsEnrolled != null) {
                    accountData = tc101ActiveAccountBothChannelsEnrolled;
                    log.info("TC_94 using prior-enrolled account {}/{}",
                            accountData.get("customerCode"), accountData.get("premisesCode"));
                } else {
                    accountData = ApplicationContext.get().getDbAction().getActiveAccountForTc94();
                }
                payload.setCustomerCode(getDbString(accountData, "customerCode"));
                payload.setPremisesCode(getDbString(accountData, "premisesCode"));
                payload.setUpdateBillDeliveryOption("P");
                payload.setUpdateCorrDeliveryOption("P");
                payload.setEmailAddress(null);
                break;

            case TC_98__Positive__Active_Account_Bill_Enrollment_Initiated_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActivePaperlessEligibleAccountWithNoToken();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                payload.setUpdateBillDeliveryOption("E");
                payload.setUpdateCorrDeliveryOption(null);
                payload.setEmailAddress(resolveBannerEmail(accountData));
                break;

            case TC_99__Positive__New_Account_Bill_Enrollment_Initiated_:
                // TC_107 prior enroll must reuse tc104ResolvedAccount (not a fresh ORA_HASH pick)
                if (tc104RequiresPriorEnrollment && tc104ResolvedAccount != null) {
                    accountData = tc104ResolvedAccount;
                    log.info("TC_99 prior-enroll for TC_107/104 reuse using resolved NEW account {}/{}",
                            getDbString(accountData, "customerCode"), getDbString(accountData, "premisesCode"));
                } else {
                    accountData = ApplicationContext.get().getDbAction().getActiveAccountForTc96();
                    tc96NewAccountWithPendingBill = accountData;
                }
                populateBillEnrollmentPayload(payload, accountData);
                break;

            case TC_100__Positive__Active_Account_Both_Channels_Enrollment_Initiated_:
                // TC_84 never-enrolled ACTIVE P/P (avoid hottest TC_97 accounts → 40291)
                accountData = getActiveAccountForTc84();
                populateBothChannelsEnrollmentPayload(payload, accountData);
                break;

            case TC_101__Positive__New_Account_Both_Channels_Enrollment_Initiated_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getNewAccountForTc98();
                tc98NewAccountWithPendingBothChannels = accountData;
                payload.setCustomerCode(getDbString(accountData, "customerCode"));
                payload.setPremisesCode(getDbString(accountData, "premisesCode"));
                payload.setUpdateBillDeliveryOption("E");
                payload.setUpdateCorrDeliveryOption("E");
                payload.setEmailAddress(resolveBannerEmail(accountData));
                break;

            case TC_102__Positive__Active_Account_Bill_Unenrollment_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActiveAccountEnrolledInPaperlessBill();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                payload.setUpdateBillDeliveryOption("P");
                payload.setUpdateCorrDeliveryOption(null);
                payload.setEmailAddress(null);
                break;

            case TC_103__Positive__New_Account_Bill_Unenrollment_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                if (tc96NewAccountWithPendingBill != null) {
                    accountData = tc96NewAccountWithPendingBill;
                    log.info("TC_100 reusing TC_96 account {}/{} with pending bill enrollment",
                            accountData.get("customerCode"), accountData.get("premisesCode"));
                } else {
                    accountData = ApplicationContext.get().getDbAction().getNewAccountWithEnrolledPendingBillPref();
                }
                payload.setCustomerCode(getDbString(accountData, "customerCode"));
                payload.setPremisesCode(getDbString(accountData, "premisesCode"));
                payload.setUpdateBillDeliveryOption("P");
                payload.setUpdateCorrDeliveryOption(null);
                payload.setEmailAddress(null);
                break;

            case TC_104__Positive__Active_Account_Both_Channels_Unenrollment_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                if (tc101ActiveAccountBothChannelsEnrolled != null) {
                    accountData = tc101ActiveAccountBothChannelsEnrolled;
                    log.info("TC_101 reusing prior-enrolled account {}/{}",
                            accountData.get("customerCode"), accountData.get("premisesCode"));
                } else {
                    accountData = ApplicationContext.get().getDbAction().getActiveAccountEnrolledInPaperlessBillAndCorr();
                }
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                payload.setUpdateBillDeliveryOption("P");
                payload.setUpdateCorrDeliveryOption("P");
                payload.setEmailAddress(null);
                break;

            case TC_105__Positive__New_Account_Both_Channels_Unenrollment_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                if (tc98NewAccountWithPendingBothChannels != null) {
                    accountData = tc98NewAccountWithPendingBothChannels;
                    log.info("TC_102 reusing TC_98 account {}/{} with pending both-channels enrollment",
                            accountData.get("customerCode"), accountData.get("premisesCode"));
                } else {
                    throw new IllegalStateException(
                            "TC_102 requires a TC_98 account with pending bill and corr enrollment");
                }
                payload.setCustomerCode(getDbString(accountData, "customerCode"));
                payload.setPremisesCode(getDbString(accountData, "premisesCode"));
                payload.setUpdateBillDeliveryOption("P");
                payload.setUpdateCorrDeliveryOption("P");
                payload.setEmailAddress(null);
                break;

            case TC_106__Positive__Active_Account_Reuse_Existing_Valid_Token_:
                if (tc90ResolvedAccount == null) {
                    prepareTc90Account();
                }
                populateBillEnrollmentPayload(payload, tc90ResolvedAccount);
                break;

            case TC_107__Positive__New_Account_Reuse_Existing_Valid_Token_:
                if (tc104ResolvedAccount == null) {
                    prepareTc104Account();
                }
                populateBillEnrollmentPayload(payload, tc104ResolvedAccount);
                break;

            case TC_108__Positive__Active_Account_New_Token_When_Existing_Token_Invalid_:
                prepareTc105Account();
                populateBillEnrollmentPayload(payload, tc105ResolvedAccount);
                break;

            case TC_109__Positive__New_Account_New_Token_When_Existing_Token_Invalid_:
                prepareTc106Account();
                populateBillEnrollmentPayload(payload, tc106ResolvedAccount);
                break;

            case TC_110__Positive__Email_Updated_During_Enrollment_:
                accountData = reserveActiveAccountForFreshBillEnrollment();
                populateBillEnrollmentPayload(payload, accountData);
                payload.setEmailAddress(generateNewTestEmail());
                break;

            case TC_111__Positive__New_Token_Required_When_Email_Changes_:
                prepareTc90Account();
                populateBillEnrollmentPayload(payload, tc90ResolvedAccount);
                payload.setEmailAddress(generateNewTestEmail());
                break;

            case TC_112__Positive__Cross_Channel_Aggregation_:
                break;

            case TC_113__Positive__Same_Channel_Update__Last_Value_Wins_:
                break;

            case TC_114__Positive__PPER_Cleanup_on_Active_Unenrollment_:
                if (tc111ActiveAccountWithPendingBill != null) {
                    accountData = tc111ActiveAccountWithPendingBill;
                    log.info("TC_111 reusing account {}/{} with pending bill enrollment",
                            accountData.get("customerCode"), accountData.get("premisesCode"));
                } else {
                    throw new IllegalStateException(
                            "TC_111 requires prior bill enrollment setup in the same test flow");
                }
                populateBillUnenrollPayload(payload, accountData);
                break;

            case TC_115__Positive__Token_Expired_on_New_Account_Unenrollment_:
                if (tc96NewAccountWithPendingBill != null) {
                    accountData = tc96NewAccountWithPendingBill;
                    log.info("TC_112 reusing NEW account {}/{} with pending bill enrollment",
                            accountData.get("customerCode"), accountData.get("premisesCode"));
                } else {
                    throw new IllegalStateException(
                            "TC_112 requires prior NEW bill enrollment setup in the same test flow");
                }
                populateBillUnenrollPayload(payload, accountData);
                break;

            case TC_116__Positive__Latest_Aggregated_State_Wins_:
                break;

            case TC_117__Positive__Enrollment_Finalized_on_Confirmation_:
                // Full E2E (enroll → Confirm CONFIRMED) is in
                // UpdatePaperlessCommunicationsPage.validateTc117EnrollmentFinalizedOnConfirmation.
                accountData = reserveActiveAccountForFreshBillEnrollment();
                populateBillEnrollmentPayload(payload, accountData);
                break;

            case TC_118__Positive__Prior_Link_Invalid_After_Email_Change_:
                // Full E2E (enroll → Update email change → prior Confirm rejected) is in
                // UpdatePaperlessCommunicationsPage.validateTc118PriorLinkInvalidAfterEmailChange.
                accountData = reserveActiveAccountForFreshBillEnrollment();
                populateBillEnrollmentPayload(payload, accountData);
                break;

            case TC_119__Positive__Passive_Invalidation_After_External_Email_Change_:
                // Full E2E (enroll → external Banner email change → Confirm 10413) is in
                // UpdatePaperlessCommunicationsPage.validateTc119PassiveInvalidationAfterExternalEmailChange.
                accountData = reserveActiveAccountForFreshBillEnrollment();
                populateBillEnrollmentPayload(payload, accountData);
                break;

            default:
                log.warn("Unhandled test condition: {}", testCondition);
                break;
        }
    }

    void configureTc106Account(Map<String, Object> account, boolean expiredTokenSetupRequired) {
        tc106ResolvedAccount = account;
        tc106RequiresExpiredTokenSetup = expiredTokenSetupRequired;
    }

    Map<String, Object> reserveNewAccountForFreshBillEnrollment() {
        DBAction dbAction = ApplicationContext.get().getDbAction();
        String excludeCustomer = null;
        String excludePremises = null;

        for (int attempt = 0; attempt < 25; attempt++) {
            Map<String, Object> candidate;
            try {
                candidate = excludeCustomer == null
                        ? dbAction.getActiveAccountForTc96()
                        : dbAction.getNewPaperlessEligibleAccountWithNoToken();
            } catch (IllegalStateException ex) {
                candidate = dbAction.getNewPaperlessEligibleAccountWithNoToken();
            }
            if (PaperlessEnrollmentAccountRegistry.tryReserve(candidate)) {
                log.info("Reserved NEW fresh bill enrollment account {}/{}",
                        candidate.get("customerCode"), candidate.get("premisesCode"));
                return candidate;
            }
            excludeCustomer = getDbString(candidate, "customerCode");
            excludePremises = getDbString(candidate, "premisesCode");
            log.info("NEW fresh bill enrollment account {}/{} already reserved; selecting another",
                    excludeCustomer, excludePremises);
        }

        Map<String, Object> fallback = dbAction.getNewPaperlessEligibleAccountWithNoToken();
        PaperlessEnrollmentAccountRegistry.tryReserve(fallback);
        return fallback;
    }

    private Map<String, Object> reserveActiveAccountForFreshBillEnrollment() {
        DBAction dbAction = ApplicationContext.get().getDbAction();
        String excludeCustomer = null;
        String excludePremises = null;

        for (int attempt = 0; attempt < 25; attempt++) {
            Map<String, Object> candidate = excludeCustomer == null
                    ? dbAction.getActiveAccountForFreshBillEnrollmentStrict()
                    : dbAction.getActiveAccountForFreshBillEnrollmentStrictExcluding(
                            excludeCustomer, excludePremises);
            if (PaperlessEnrollmentAccountRegistry.tryReserve(candidate)) {
                log.info("Reserved fresh bill enrollment account {}/{}",
                        candidate.get("customerCode"), candidate.get("premisesCode"));
                return candidate;
            }
            excludeCustomer = getDbString(candidate, "customerCode");
            excludePremises = getDbString(candidate, "premisesCode");
            log.info("Fresh bill enrollment account {}/{} already reserved; selecting another",
                    excludeCustomer, excludePremises);
        }

        for (Map<String, Object> candidate : dbAction.listFreshBillEnrollmentCandidates()) {
            if (PaperlessEnrollmentAccountRegistry.tryReserve(candidate)) {
                log.info("Reserved fresh bill enrollment account {}/{} from candidate list",
                        candidate.get("customerCode"), candidate.get("premisesCode"));
                return candidate;
            }
        }

        throw new IllegalStateException(
                "No unreserved ACTIVE account for fresh bill enrollment. "
                        + "All strict candidates are already reserved by other scenarios in this suite run.");
    }

    private Map<String, Object> reserveNewAccountForBothChannelsEnrollment() {
        DBAction dbAction = ApplicationContext.get().getDbAction();
        String excludeCustomer = null;
        String excludePremises = null;

        for (int attempt = 0; attempt < 25; attempt++) {
            Map<String, Object> candidate = excludeCustomer == null
                    ? dbAction.getNewAccountForBothChannelsEnrollmentStrict()
                    : dbAction.getNewAccountForBothChannelsEnrollmentStrictExcluding(
                            excludeCustomer, excludePremises);
            if (PaperlessEnrollmentAccountRegistry.tryReserve(candidate)) {
                log.info("Reserved NEW both-channels enrollment account {}/{}",
                        candidate.get("customerCode"), candidate.get("premisesCode"));
                return candidate;
            }
            excludeCustomer = getDbString(candidate, "customerCode");
            excludePremises = getDbString(candidate, "premisesCode");
            log.info("NEW both-channels enrollment account {}/{} already reserved; selecting another",
                    excludeCustomer, excludePremises);
        }

        for (Map<String, Object> candidate : dbAction.listNewBothChannelsEnrollmentCandidates()) {
            if (PaperlessEnrollmentAccountRegistry.tryReserve(candidate)) {
                log.info("Reserved NEW both-channels enrollment account {}/{} from candidate list",
                        candidate.get("customerCode"), candidate.get("premisesCode"));
                return candidate;
            }
        }

        throw new IllegalStateException(
                "No unreserved NEW account for both-channels first-time enrollment. "
                        + "All strict candidates are already reserved by other scenarios in this suite run.");
    }

    void populateNewBothChannelsEnrollmentPayload(UpdatePaperlessCommunicationsRequest payload,
                                                  Map<String, Object> accountData) {
        payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
        payload.setCustomerCode(getDbString(accountData, "customerCode"));
        payload.setPremisesCode(getDbString(accountData, "premisesCode"));
        payload.setUpdateBillDeliveryOption("E");
        payload.setUpdateCorrDeliveryOption("E");
        payload.setEmailAddress(resolveBannerEmail(accountData));
    }

    private Map<String, Object> getActiveAccountForTc84() {
        if (tc83AccountWithCorrEnrollment != null) {
            return ApplicationContext.get().getDbAction().getActiveAccountForTc84Excluding(
                    getDbString(tc83AccountWithCorrEnrollment, "customerCode"),
                    getDbString(tc83AccountWithCorrEnrollment, "premisesCode"));
        }
        return ApplicationContext.get().getDbAction().getActiveAccountForTc84();
    }

}