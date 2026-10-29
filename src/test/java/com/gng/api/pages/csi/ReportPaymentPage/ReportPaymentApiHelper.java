package com.gng.api.pages.csi.ReportPaymentPage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.db.DBQuery;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.ReportPayment.ReportPaymentRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.ReportPayment.ReportPaymentLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class ReportPaymentApiHelper {

    private static final DateTimeFormatter YYYYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final String DEFAULT_PAYMENT_TYPE = "OT";
    private static final String DEFAULT_PAYMENT_AMOUNT = "10.00";
    private static final String NO_RECENT_PREC =
            "No active account has a PREC credit history entry inside the active "
                    + "NOTIFY_PAYMENT.DELINQUENCY_LOOKBACK_DAYS window";

    private final TestContext testContext;
    private List<Map<String, Object>> recentPrecAccounts;

    public ReportPaymentApiHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    ReportPaymentRequest preparePayload(ReportPaymentLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        String jsonFileName = apiLabel.equals(ReportPaymentLabel.report_payment)
                ? ReportPaymentLabel.report_payment.toString()
                : ReportPaymentLabel.report_payment_mandatory.toString();
        return BasePage.deserializeJsonToPojo(jsonFileName, ReportPaymentRequest.class);
    }

    public void preparePayloadForTestCondition(ReportPaymentRequest payload,
                                               ReportPaymentLabel testCondition) {
        switch (testCondition) {

            // ---------------- NEGATIVE TEST CASES ----------------

            case TC_1__Negative__Missing_Request_ID__ReportPayment_:
                payload.setRequestID("");
                break;

            case TC_2__Negative__Invalid_Request_ID_Length__ReportPayment_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(33));
                break;

            case TC_3__Negative__Duplicate_Request_ID__ReportPayment_:
                payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());
                break;

            case TC_4__Negative__Missing_customerCode__ReportPayment_:
                applyFreshRequestDefaults(payload);
                payload.setCustomerCode("");
                break;

            case TC_5__Negative__Invalid_customerCode_Length__ReportPayment_:
                applyFreshRequestDefaults(payload);
                payload.setCustomerCode(FakerDataGenerator.generateDigits(10));
                break;

            case TC_6__Negative__Invalid_customerCode_Format__ReportPayment_:
                applyFreshRequestDefaults(payload);
                payload.setCustomerCode(FakerDataGenerator.generateString(6));
                break;

            case TC_7__Negative__Missing_premisesCode__ReportPayment_:
                applyFreshRequestDefaults(payload);
                payload.setPremisesCode("");
                break;

            case TC_8__Negative__Invalid_premisesCode_Length__ReportPayment_:
                applyFreshRequestDefaults(payload);
                payload.setPremisesCode(FakerDataGenerator.generateDigits(8));
                break;

            case TC_9__Negative__Invalid_premisesCode_Format__ReportPayment_:
                applyFreshRequestDefaults(payload);
                payload.setPremisesCode(FakerDataGenerator.generateString(6));
                break;

            case TC_10__Negative__Invalid_Account_Number__Invalid_Account__ReportPayment_:
                applyFreshRequestDefaults(payload);
                payload.setCustomerCode("9988776");
                payload.setPremisesCode(FakerDataGenerator.generateDigits(6));
                break;

            case TC_11__Negative__Missing_Payment_Date_:
                applyActiveAccount(payload);
                payload.setPaymentDate("");
                break;

            case TC_12__Negative__Invalid_Payment_Date_Format_:
                applyActiveAccount(payload);
                payload.setPaymentDate("2026-01-01");
                break;

            case TC_13__Negative__Missing_Payment_Amount_:
                applyActiveAccount(payload);
                payload.setPaymentAmount("");
                break;

            case TC_14__Negative__Invalid_Payment_Amount_Format_NonNumeric_:
                applyActiveAccount(payload);
                payload.setPaymentAmount("ABC");
                break;

            case TC_15__Negative__Invalid_Payment_Amount_Format_DecimalPlaces_:
                applyActiveAccount(payload);
                payload.setPaymentAmount("10.123");
                break;

            case TC_16__Negative__Missing_Payment_Type_:
                applyActiveAccount(payload);
                payload.setPaymentType("");
                break;

            case TC_17__Negative__Invalid_Payment_Type_Format_:
                applyActiveAccount(payload);
                payload.setPaymentType(FakerDataGenerator.generateAlphanumeric(21));
                break;

            case TC_18__Negative__Invalid_Payment_Confirmation_Number_Format_:
                applyActiveAccount(payload);
                payload.setConfirmationNumber("123");
                break;

            case TC_19__Negative__Missing_Origin_:
                applyActiveAccount(payload);
                payload.setOrigin("");
                break;

            case TC_20__Negative__Invalid_Origin_:
                applyActiveAccount(payload);
                payload.setOrigin(GlobalEnums.InvalidValues.INVALID_ORIGIN.getValue());
                break;

            case TC_21__Negative__Open_SONP_Service_Order_:
                applyAccountFromMap(payload, account(DBQuery.SELECT_RP_OPEN_SONP_ACCOUNT, "open SONP service order"));
                break;

            case TC_22__Negative__SONP_Credit_History_Entry_:
                applyAccountFromMap(payload, account(DBQuery.SELECT_RP_SONP_CREDIT_HISTORY_ACCOUNT, "SONP credit history"));
                break;

            case TC_23__Negative__Unresolved_SONP_Transaction_:
                applyAccountFromMap(payload, account(DBQuery.SELECT_RP_UNRESOLVED_SONP_ACCOUNT, "unresolved SONP transaction"));
                break;

            case TC_24__Negative__Past_Due_Balance_Exceeds_Threshold_:
                applyAccountFromMap(payload, ApplicationContext.get().getDbAction().getReportPaymentPastDueOverThreshold());
                payload.setPaymentAmount("1.00");
                break;

            case TC_25__Negative__Invalid_Payment_Type_:
                applyActiveAccount(payload);
                payload.setPaymentType("ZZ");
                break;

            case TC_26__Negative__Invalid_Payment_Date_Future_Date_:
                applyActiveAccount(payload);
                payload.setPaymentDate(LocalDate.now().plusDays(7).format(YYYYMMDD));
                break;

            case TC_27__Negative__Invalid_Payment_Date_Outside_Allowed_Backdate_Window_:
                applyActiveAccount(payload);
                payload.setPaymentDate(LocalDate.now().minusYears(2).format(YYYYMMDD));
                break;

            case TC_28__Negative__Payment_Amount_Equals_Zero_:
                applyActiveAccount(payload);
                payload.setPaymentAmount("0.00");
                break;

            case TC_29__Negative__Payment_Amount_Less_Than_Zero_:
                applyActiveAccount(payload);
                payload.setPaymentAmount("-1.00");
                break;

            case TC_30__Negative__Payment_Amount_Exceeds_Maximum_Allowed_:
                applyActiveAccount(payload);
                payload.setPaymentAmount("99999999.99");
                break;

            case TC_31__Negative__WS_Missing_Confirmation_Number_:
                applyActiveAccount(payload);
                payload.setPaymentType("WS");
                payload.setConfirmationNumber(null);
                break;

            case TC_32__Negative__PH_Missing_Confirmation_Number_:
                applyActiveAccount(payload);
                payload.setPaymentType("PH");
                payload.setConfirmationNumber(null);
                break;

            case TC_33__Negative__WI_Missing_Confirmation_Number_:
                applyActiveAccount(payload);
                payload.setPaymentType("WI");
                payload.setConfirmationNumber(null);
                break;

            // ---------------- POSITIVE TEST CASES ----------------

            case TC_34__Positive__OT_Payment_Type_Without_Confirmation_Number_:
                applyActiveAccount(payload);
                payload.setPaymentType(DEFAULT_PAYMENT_TYPE);
                payload.setConfirmationNumber(null);
                break;

            case TC_35__Positive__Payment_Amount_Less_Than_Past_Due_Balance_:
                applyPartialPayment(payload, account(
                        DBQuery.SELECT_RP_PAST_DUE_BELOW_THRESHOLD, "past due below 10000"));
                break;

            case TC_36__Positive__No_Recent_PREC_:
                applyCoveringPayment(payload, account(
                        DBQuery.SELECT_RP_PAST_DUE_BELOW_THRESHOLD, "past due with no recent PREC"));
                break;

            case TC_37__Positive__Net_Past_Due_Balance_Zero_or_Negative_:
                ensureDynamicLookbackForTc37To48();
                applyCoveringPayment(payload, recentPrecAccount((account, due) -> due.signum() <= 0, "net past due <= 0"));
                break;

            case TC_38__Positive__Existing_Payment_Arrangement_:
                ensureDynamicLookbackForTc37To48();
                applyCoveringPayment(payload, recentPrecAccount(this::hasActivePaymentArrangement, "active payment arrangement"));
                break;

            case TC_39__Positive__Active_Bank_Draft_:
                ensureDynamicLookbackForTc37To48();
                applyCoveringPayment(payload, recentPrecAccount(this::hasActiveOrPendingBankDraft, "active bank draft"));
                break;

            case TC_40__Positive__Active_Budget_Billing_:
                ensureDynamicLookbackForTc37To48();
                applyCoveringPayment(payload, recentPrecAccount(this::hasActiveBudgetBilling, "active budget billing"));
                break;

            case TC_41__Positive__Home_Warranty_:
                ensureDynamicLookbackForTc37To48();
                applyCoveringPayment(payload, recentPrecAccount(this::hasHomeWarranty, "home warranty"));
                break;

            case TC_42__Positive__Recurring_Credit_Card_:
                ensureDynamicLookbackForTc37To48();
                applyCoveringPayment(payload, recentPrecAccount(this::hasRecurringCreditCard, "recurring credit card"));
                break;

            case TC_43__Positive__MPAY_Defaulted_Payment_Arrangement_History_:
                ensureDynamicLookbackForTc37To48();
                applyCoveringPayment(payload, recentPrecAccount(this::hasRecentMpay, "MPAY history"));
                break;

            case TC_44__Positive__Delinquency_Stage_Update_Rule_:
            case TC_45__Positive__Hold_Payment_Arrangement_Created_:
            case TC_46__Positive__Hold_Response_Amount_:
            case TC_47__Positive__Hold_Response_Due_Date_:
            case TC_48__Positive__Payment_Arrangement_Enrollment_Note_Created_:
                ensureDynamicLookbackForTc37To48();
                applyCoveringPayment(payload, nextHoldEligibleAccount());
                break;

            default:
                log.warn("Unhandled ReportPayment test condition: {}", testCondition);
                break;
        }
    }

    private void ensureDynamicLookbackForTc37To48() {
        boolean alreadyApplied = ReportPaymentDelinquencyLookbackSupport.isApplied();
        ReportPaymentDelinquencyLookbackSupport.ensureForTc37To48();
        if (!alreadyApplied) {
            recentPrecAccounts = null;
            holdEligibleAccounts.clear();
            holdEligibleLoaded = false;
        }
    }

    private Map<String, Object> account(String query, String label) {
        return ApplicationContext.get().getDbAction().getReportPaymentAccount(query, label);
    }

    private void applyPartialPayment(ReportPaymentRequest payload, Map<String, Object> accountInfo) {
        applyAccountFromMap(payload, accountInfo);
        payload.setPaymentAmount("1.00");
    }

    private void applyCoveringPayment(ReportPaymentRequest payload, Map<String, Object> accountInfo) {
        applyAccountFromMap(payload, accountInfo);
        BigDecimal due = pastDue(accountInfo);
        if (due.signum() < 0) {
            due = BigDecimal.ZERO;
        }
        String amount = due.setScale(2, RoundingMode.HALF_UP).toPlainString();
        if (new BigDecimal(amount).compareTo(new BigDecimal("10000")) > 0) {
            throw new IllegalStateException("Past due " + amount + " exceeds the 10000 payment maximum");
        }
        if (new BigDecimal(amount).signum() == 0) {
            amount = DEFAULT_PAYMENT_AMOUNT;
        }
        payload.setPaymentAmount(amount);
        testContext.setReportPaymentExpectedPaAmount(amount);
    }

    private Map<String, Object> recentPrecAccount(PrecMatch match, String label) {
        for (Map<String, Object> account : recentPrecAccounts()) {
            if (match.matches(account, pastDue(account))) {
                return account;
            }
        }
        throw new IllegalStateException(NO_RECENT_PREC + " for " + label);
    }

    private final List<Map<String, Object>> holdEligibleAccounts = new ArrayList<>();
    private boolean holdEligibleLoaded;

    private Map<String, Object> nextHoldEligibleAccount() {
        if (!holdEligibleLoaded) {
            for (Map<String, Object> account : recentPrecAccounts()) {
                BigDecimal due = pastDue(account);
                if (due.signum() > 0
                        && due.compareTo(new BigDecimal("10000")) <= 0
                        && !hasActivePaymentArrangement(account, due)
                        && !hasActiveOrPendingBankDraft(account, due)
                        && !hasRecurringCreditCard(account, due)
                        && !hasActiveBudgetBilling(account, due)
                        && !hasRecentMpay(account, due)
                        && !hasHomeWarranty(account, due)
                        && !hasOpenSonp(account)
                        && !hasSonpCreditHistory(account)
                        && !hasUnresolvedSonp(account)) {
                    holdEligibleAccounts.add(account);
                }
            }
            holdEligibleLoaded = true;
        }
        if (holdEligibleAccounts.isEmpty()) {
            throw new IllegalStateException(NO_RECENT_PREC + " that is eligible for a Hold Payment Arrangement");
        }
        return holdEligibleAccounts.remove(0);
    }

    private List<Map<String, Object>> recentPrecAccounts() {
        if (recentPrecAccounts == null) {
            int lookback = ReportPaymentDelinquencyLookbackSupport.getActiveLookbackDays();
            recentPrecAccounts = new ArrayList<>(ApplicationContext.get().getDbAction()
                    .getReportPaymentAccounts(
                            DBQuery.SELECT_RP_RECENT_PREC_ACCOUNTS,
                            "recent PREC accounts (lookback=" + lookback + ")",
                            lookback));
            if (recentPrecAccounts.isEmpty()) {
                throw new IllegalStateException(NO_RECENT_PREC + " (lookback=" + lookback + ")");
            }
        }
        return recentPrecAccounts;
    }

    private boolean hasActivePaymentArrangement(Map<String, Object> account, BigDecimal pastDue) {
        return count(account, """
                SELECT COUNT(*) FROM uabpyar
                WHERE uabpyar_cust_code = ? AND uabpyar_prem_code = ? AND uabpyar_status = 'A'
                """) > 0 && pastDue != null;
    }

    private boolean hasActiveOrPendingBankDraft(Map<String, Object> account, BigDecimal pastDue) {
        return count(account, """
                SELECT COUNT(*) FROM ucracct
                WHERE ucracct_cust_code = ? AND ucracct_prem_code = ?
                  AND NVL(ucracct_draft_acct_status, ' ') IN ('A', 'P')
                """) > 0 && pastDue != null;
    }

    private boolean hasActiveBudgetBilling(Map<String, Object> account, BigDecimal pastDue) {
        return count(account, """
                SELECT COUNT(*) FROM uabbudg
                WHERE uabbudg_cust_code = ? AND uabbudg_prem_code = ? AND uabbudg_status_ind = 'A'
                """) > 0 && pastDue != null;
    }

    private boolean hasHomeWarranty(Map<String, Object> account, BigDecimal pastDue) {
        return count(account, """
                SELECT COUNT(*) FROM dual
                WHERE GCISMGR.F_DOES_ACTIVE_HOME_SOL_EXIST(?, ?) = 'Y'
                """) > 0 && pastDue != null;
    }

    private boolean hasRecurringCreditCard(Map<String, Object> account, BigDecimal pastDue) {
        return count(account, """
                SELECT COUNT(*) FROM dual
                WHERE GCISMGR.F_DOES_WU_CREDIT_CARD_EXIST(?, ?) = 'Y'
                """) > 0 && pastDue != null;
    }

    private boolean hasRecentMpay(Map<String, Object> account, BigDecimal pastDue) {
        return count(account, """
                SELECT COUNT(*) FROM ucrcrhs
                WHERE ucrcrhs_cust_code = ? AND ucrcrhs_prem_code = ?
                  AND ucrcrhs_ccat_code = 'MPAY'
                  AND ucrcrhs_occurance_date >= TRUNC(SYSDATE) - 30
                """) > 0 && pastDue != null;
    }

    private boolean hasOpenSonp(Map<String, Object> account) {
        return count(account, """
                SELECT COUNT(*) FROM ucbsvco
                WHERE ucbsvco_cust_code = ? AND ucbsvco_prem_code = ?
                  AND ucbsvco_sotp_code = 'SONP' AND ucbsvco_stus_code = 'O'
                  AND ucbsvco_date_created >= TRUNC(SYSDATE) - 30
                """) > 0;
    }

    private boolean hasSonpCreditHistory(Map<String, Object> account) {
        return count(account, """
                SELECT COUNT(*) FROM ucrcrhs
                WHERE ucrcrhs_cust_code = ? AND ucrcrhs_prem_code = ?
                  AND ucrcrhs_ccat_code = 'SONP'
                  AND ucrcrhs_occurance_date >= TRUNC(SYSDATE) - 30
                """) > 0;
    }

    private boolean hasUnresolvedSonp(Map<String, Object> account) {
        return count(account, """
                SELECT COUNT(*) FROM gtbtrnh
                WHERE gtbtrnh_cust_code = ? AND gtbtrnh_prem_code = ?
                  AND gtbtrnh_tran_type = 'SONP'
                  AND gtbtrnh_tran_date >= TRUNC(SYSDATE) - 30
                """) > 0;
    }

    private int count(Map<String, Object> account, String sql) {
        return ApplicationContext.get().getDbAction().countReportPaymentCondition(
                sql,
                resolveAccountField(account, "UCRACCT_CUST_CODE", "ucracct_cust_code"),
                resolveAccountField(account, "UCRACCT_PREM_CODE", "ucracct_prem_code"));
    }

    private static BigDecimal pastDue(Map<String, Object> account) {
        Object value = null;
        for (Map.Entry<String, Object> entry : account.entrySet()) {
            if (entry.getKey() != null && entry.getKey().equalsIgnoreCase("past_due")) {
                value = entry.getValue();
                break;
            }
        }
        if (value == null) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(value.toString());
    }

    @FunctionalInterface
    private interface PrecMatch {
        boolean matches(Map<String, Object> account, BigDecimal pastDue);
    }

    private void applyFreshRequestDefaults(ReportPaymentRequest payload) {
        payload.setRequestID(FakerDataGenerator.generateAlphanumeric(32));
        payload.setPaymentDate(today());
        payload.setPaymentAmount(DEFAULT_PAYMENT_AMOUNT);
        payload.setPaymentType(DEFAULT_PAYMENT_TYPE);
    }

    private void applyActiveAccount(ReportPaymentRequest payload) {
        applyAccountFromMap(payload, ApplicationContext.get().getDbAction().getActiveAccountOnly());
    }

    private void applyBudgetBillingAccount(ReportPaymentRequest payload) {
        List<Map<String, Object>> accounts = ApplicationContext.get().getDbAction().getBudgetBillingAccount();
        if (accounts == null || accounts.isEmpty()) {
            throw new IllegalStateException("No Budget Billing account seed available for ReportPayment");
        }
        applyAccountFromMap(payload, accounts.get(0));
    }

    private void applyAccountFromMap(ReportPaymentRequest payload, Map<String, Object> accountInfo) {
        if (accountInfo == null || accountInfo.isEmpty()) {
            throw new IllegalStateException("No account seed available for ReportPayment test condition");
        }
        payload.setRequestID(FakerDataGenerator.generateAlphanumeric(32));
        payload.setCustomerCode(resolveAccountField(accountInfo,
                "UCRACCT_CUST_CODE", "customer_code", "CUSTOMER_CODE", "ubbbhst_cust_code"));
        payload.setPremisesCode(resolveAccountField(accountInfo,
                "UCRACCT_PREM_CODE", "premises_code", "PREMISES_CODE", "ubbbhst_prem_code"));
        payload.setPaymentDate(today());
        payload.setPaymentAmount(DEFAULT_PAYMENT_AMOUNT);
        payload.setPaymentType(DEFAULT_PAYMENT_TYPE);
        payload.setConfirmationNumber(FakerDataGenerator.generateDigits(10));
        payload.setOrigin("CSI");

        testContext.setCustomerCode(payload.getCustomerCode());
        testContext.setPremisesCode(payload.getPremisesCode());
    }

    private static String resolveAccountField(Map<String, Object> accountInfo, String... keys) {
        for (String key : keys) {
            if (accountInfo.containsKey(key) && accountInfo.get(key) != null) {
                return accountInfo.get(key).toString();
            }
            for (Map.Entry<String, Object> entry : accountInfo.entrySet()) {
                if (entry.getKey() != null
                        && entry.getKey().equalsIgnoreCase(key)
                        && entry.getValue() != null) {
                    return entry.getValue().toString();
                }
            }
        }
        throw new IllegalStateException("Could not resolve account field from keys: " + String.join(",", keys)
                + " in " + accountInfo.keySet());
    }

    private static String today() {
        return LocalDate.now().format(YYYYMMDD);
    }
}
