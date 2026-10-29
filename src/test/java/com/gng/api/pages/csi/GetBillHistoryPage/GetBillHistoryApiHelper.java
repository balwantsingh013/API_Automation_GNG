package com.gng.api.pages.csi.GetBillHistoryPage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.db.DBQuery;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.GetBillHistory.GetBillHistoryRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.report.DualReportManager;
import com.gng.api.steps.csi.GetBillHistory.GetBillHistoryLabel;
import com.gng.api.util.FakerDataGenerator;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.testng.SkipException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.gng.api.constants.ApiEndPoint.GET_BILL_HISTORY;

@Slf4j
public class GetBillHistoryApiHelper {

    private final TestContext testContext;

    public GetBillHistoryApiHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    GetBillHistoryRequest preparePayload(GetBillHistoryLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        String jsonFileName = apiLabel.equals(GetBillHistoryLabel.get_bill_history)
                ? GetBillHistoryLabel.get_bill_history.toString()
                : GetBillHistoryLabel.get_bill_history_mandatory.toString();

        return BasePage.deserializeJsonToPojo(jsonFileName, GetBillHistoryRequest.class);
    }

    public void preparePayloadForTestCondition(GetBillHistoryRequest payload,
                                               GetBillHistoryLabel testCondition) {

        payload.setRequestID(FakerDataGenerator.generateAlphanumeric(10));
        // loginID null — template User20 has hung some UAT accounts.
        payload.setLoginID(null);
        Map<String, Object> accountInfo;

        switch (testCondition) {

            // ---------------- NEGATIVE TEST CASES ----------------

            case TC_60__Negative__Missing_Request_ID:
                payload.setRequestID("");
                break;

            case TC_61__Negative__Invalid_Request_ID_Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(33));
                break;

            case TC_62__Negative__Duplicate_Request_ID:
                payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());
                break;

            case TC_63__Negative__Missing_customerCode:
                payload.setCustomerCode("");
                break;

            case TC_64__Negative__Invalid_customerCode_Length:
                payload.setCustomerCode(FakerDataGenerator.generateDigits(10));
                break;

            case TC_65__Negative__Invalid_customerCode_Format:
                payload.setCustomerCode(FakerDataGenerator.generateAlphanumeric(6));
                break;

            case TC_66__Negative__Missing_premisesCode:
                payload.setPremisesCode("");
                break;

            case TC_67__Negative__Invalid_premisesCode_Length:
                payload.setPremisesCode(FakerDataGenerator.generateDigits(8));
                break;

            case TC_68__Negative__Invalid_premisesCode_Format:
                payload.setPremisesCode("A"+FakerDataGenerator.generateAlphanumeric(5));
                break;

            case TC_69__Negative__Invalid_Account_Number:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode("9988776");
                Map<String, Object> userInfo = ApplicationContext.get().getDbAction().getUserAccountInfo("9988776");
                payload.setCustomerCode("9988776");
                payload.setPremisesCode(FakerDataGenerator.generateDigits(6));
                break;

            case TC_70__Negative__Missing_Number_of_Months:
                payload.setNumberOfMonths(null);
                break;

            case TC_71__Negative__Invalid_Number_of_Months_Format_NotANumber:
                payload.setNumberOfMonths("ABC");
                break;

            case TC_72__Negative__Invalid_Number_of_Months_Zero:
                payload.setNumberOfMonths("0");
                break;

            case TC_73__Negative__Invalid_Number_of_Months_Negative:
                payload.setNumberOfMonths("-5");
                break;

            case TC_74__Negative__Invalid_Number_of_Months_Length:
                payload.setNumberOfMonths("1234");
                break;

            // ---------------- POSITIVE TEST CASES ----------------

            case TC_75__Positive__Usage_History_BillDate_Format:
            case TC_76__Positive__Usage_History_UsageFromDate_Format:
            case TC_77__Positive__Usage_History_UsageToDate_Format:
            case TC_78__Positive__Usage_History_DaysOfService_Format:
            case TC_79__Positive__Usage_History_HeatingDegreeDays_Format:
            case TC_80__Positive__Usage_History_TotalBilledConsumption_Format:
            case TC_81__Positive__Usage_History_BalanceBroughtForward_Format:
            case TC_82__Positive__Usage_History_GasServiceCharges_Format:
            case TC_85__Positive__Usage_History_Taxes_Format:
            case TC_88__Positive__Usage_History_TotalBillAmount_Format:
            case TC_89__Positive__Usage_History_BillHistoryTransactionNumber_Format:
            case SP1_02__Positive__Budget_Billing_Actual_Charges_Null:
            case SP1_04__Positive__Budget_Billing_Monthly_Variance_Null:
            case SP1_06__Positive__Budget_Billing_Total_Variance_Null:
            case SP1_08__Positive__Guaranteed_Bill_Amount_Null:
                // SELECT_VALID_BILL_HISTORY_ACCOUNT excludes BB seed 2955/2568 (slow Banner lookback).
                accountInfo = ApplicationContext.get().getDbAction().getAccountWithBillHistory();
                payload.setCustomerCode(accountInfo.get("UBBBHST_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UBBBHST_prem_code").toString());
                payload.setNumberOfMonths(24);
                testContext.setCustomerCode(accountInfo.get("UBBBHST_cust_code").toString());
                testContext.setPremisesCode(accountInfo.get("UBBBHST_prem_code").toString());
                break;

            case TC_87__Positive__Usage_History_NotBudgetBillingAccount:
                // No active UABBUDG row (uabbudg_status_ind = 'A').
                accountInfo = ApplicationContext.get().getDbAction().getNonBudgetBillHistoryAccount();
                payload.setCustomerCode(accountInfo.get("UBBBHST_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UBBBHST_prem_code").toString());
                payload.setNumberOfMonths(24);
                testContext.setCustomerCode(accountInfo.get("UBBBHST_cust_code").toString());
                testContext.setPremisesCode(accountInfo.get("UBBBHST_prem_code").toString());
                break;

            case TC_86__Positive__Usage_History_BudgetBillingAmount_Format:
            case SP1_01__Positive__Budget_Billing_Actual_Charges_Format:
            case SP1_03__Positive__Budget_Billing_Monthly_Variance_Format:
            case SP1_05__Positive__Budget_Billing_Total_Variance_Format:
            case SP1_09__Positive__Budget_Billing_Historical_Values:
                // UAT BB seed 2955/2568 (loginID null, numberOfMonths 24).
                // Do not pre-fetch getBillHistory — unused and previously stalled the suite.
                applyBudgetBillingBillHistorySeed(payload);
                break;

            case SP1_07__Positive__Guaranteed_Bill_Amount_Format:
            case SP1_10__Positive__Guaranteed_Bill_Historical_Values:
                applyGuaranteedBillHistorySeedOrSkip(payload, testCondition.name());
                break;

            case SP1_11__Positive__Standard_Bill_History_Values:
            case SP1_12__Positive__Consumption_Values:
            case SP1_13__Positive__Balance_Brought_Forward_Values:
            case SP1_14__Positive__Promotional_Discount_Values:
                applyTwoMonthBannerValueSeed(payload, testCondition);
                break;


            case TC_83__Positive__Usage_History_OtherCharges_Format:
                accountInfo = ApplicationContext.get().getDbAction().getAccountWithActualReading2();
                payload.setCustomerCode(accountInfo.get("urrshis_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("urrshis_prem_code").toString());
                payload.setNumberOfMonths("24");
                testContext.setCustomerCode(accountInfo.get("urrshis_cust_code").toString());
                testContext.setPremisesCode(accountInfo.get("urrshis_prem_code").toString());
                List<Map<String, Object>> dbRows5 = ApplicationContext.get()
                        .getDbAction()
                        .getBillHistory(accountInfo.get("urrshis_cust_code").toString(), accountInfo.get("urrshis_prem_code").toString());

                break;

            case TC_84__Positive__Usage_History_PromotionalDiscounts_Format:
                // Use a known bill-history account; getUsageHistoryGreater often returns no rows
                // when Banner has no account with exactly 12 distinct printed dates in 12 months.
                accountInfo = ApplicationContext.get().getDbAction().getAccountWithBillHistory();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("UBBBHST_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UBBBHST_prem_code").toString());
                payload.setNumberOfMonths("24");
                testContext.setCustomerCode(accountInfo.get("UBBBHST_cust_code").toString());
                testContext.setPremisesCode(accountInfo.get("UBBBHST_prem_code").toString());
                break;

            case TC_90__Positive__No_Usage_History_New:
                accountInfo = ApplicationContext.get().getDbAction().getNewAccountOnly();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setNumberOfMonths("24");
                break;

            case TC_90A__Positive__No_Usage_History_Active:
                accountInfo = ApplicationContext.get().getDbAction().getNoUsageHistoryActiveAccount();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setNumberOfMonths("24");
                break;

            case TC_91__Positive__No_Usage_History_Final:
            accountInfo = ApplicationContext.get().getDbAction().getNoUsageHistoryFinalAccount();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setNumberOfMonths("24");
                break;

            case TC_92__Positive__No_Usage_History_Inactive:
                accountInfo = ApplicationContext.get().getDbAction().getNoUsageHistoryInactiveAccount();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setNumberOfMonths("24");
                break;

            case TC_93__Positive__Valid_NumberOfMonths_BillHistory_Too_Old:
                try {
                    accountInfo = ApplicationContext.get().getDbAction().getUsageHistoryOld();
                } catch (Exception e) {
                    skipWhenNoAutomatableSeed(testCondition.name(),
                            "No automatable too-old bill-history account; "
                                    + "manual account creation is not permitted for automation.");
                    break;
                }
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("customer_code").toString());
                payload.setPremisesCode(accountInfo.get("premises_code").toString());
                payload.setNumberOfMonths(12);
                testContext.setCustomerCode(accountInfo.get("customer_code").toString());
                testContext.setPremisesCode(accountInfo.get("premises_code").toString());
                break;

            case TC_94__Positive__Usage_History_Less_Than_Requested_Months: {
                Map<String, Object> resolved94 = resolveBillHistoryLessThanRequested();
                String cust94 = resolved94.get("ubbbhst_cust_code").toString();
                String prem94 = resolved94.get("ubbbhst_prem_code").toString();
                int lookback94 = ((Number) resolved94.get("requestedMonths")).intValue();
                int expected94 = ((Number) resolved94.get("expectedMatches")).intValue();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(cust94);
                payload.setPremisesCode(prem94);
                payload.setNumberOfMonths(lookback94);
                testContext.setCustomerCode(cust94);
                testContext.setPremisesCode(prem94);
                testContext.setRequestedNumberOfMonths(lookback94);
                testContext.setExpectedNumberOfMatches(expected94);
                break;
            }

            case TC_95__Positive__Usage_History_Equals_Requested_Months: {
                // numberOfMonths = lookback window (months), not bill count.
                Map<String, Object> resolved95 = resolveBillHistoryCandidate(true);
                String cust95 = resolved95.get("ubbbhst_cust_code").toString();
                String prem95 = resolved95.get("ubbbhst_prem_code").toString();
                int lookback95 = ((Number) resolved95.get("requestedMonths")).intValue();
                int expected95 = ((Number) resolved95.get("expectedMatches")).intValue();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(cust95);
                payload.setPremisesCode(prem95);
                payload.setNumberOfMonths(String.valueOf(lookback95));
                testContext.setCustomerCode(cust95);
                testContext.setPremisesCode(prem95);
                testContext.setRequestedNumberOfMonths(lookback95);
                testContext.setExpectedNumberOfMatches(expected95);
                break;
            }

            case TC_96__Positive__Usage_History_Greater_Than_Requested_Months: {
                // Seed a wider lookback, then request a shorter lookback.
                // Bills in the shorter window != the lookback month value (e.g. 9 months → 4 bills).
                Map<String, Object> resolved96 = resolveBillHistoryCandidate(false);
                String cust96 = resolved96.get("ubbbhst_cust_code").toString();
                String prem96 = resolved96.get("ubbbhst_prem_code").toString();
                int lookback96 = ((Number) resolved96.get("requestedMonths")).intValue();
                int expected96 = ((Number) resolved96.get("expectedMatches")).intValue();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(cust96);
                payload.setPremisesCode(prem96);
                payload.setNumberOfMonths(String.valueOf(lookback96));
                testContext.setCustomerCode(cust96);
                testContext.setPremisesCode(prem96);
                testContext.setRequestedNumberOfMonths(lookback96);
                testContext.setExpectedNumberOfMatches(expected96);
                break;
            }

            default:
                log.warn("Unhandled test condition: {}", testCondition);
                break;
        }
    }

    /**
     * Budget-billing GetBillHistory seed for 2955/2568.
     * Payload: loginID null, numberOfMonths as integer 24 (not a pre-API DB lookback).
     */
    private void applyBudgetBillingBillHistorySeed(GetBillHistoryRequest payload) {
        final String cust = "2955";
        final String prem = "2568";
        final int lookbackMonths = 24;
        payload.setLoginID(null);
        payload.setCustomerCode(cust);
        payload.setPremisesCode(prem);
        payload.setNumberOfMonths(lookbackMonths);
        testContext.setCustomerCode(cust);
        testContext.setPremisesCode(prem);
        testContext.setRequestedNumberOfMonths(lookbackMonths);
        DualReportManager.logInfo(
                "GetBillHistory BB seed " + cust + "/" + prem
                        + " (loginID=null, numberOfMonths=" + lookbackMonths + ")");
    }

    /**
     * TC_94: request a lookback wider than available bill history (matches &gt; 0 and &lt; lookback).
     */
    private Map<String, Object> resolveBillHistoryLessThanRequested() {
        List<Map<String, Object>> candidates = ApplicationContext.get().getDbAction()
                .listBillHistoryTc9596Candidates();
        if (candidates == null || candidates.isEmpty()) {
            skipWhenNoAutomatableSeed(
                    GetBillHistoryLabel.TC_94__Positive__Usage_History_Less_Than_Requested_Months.name(),
                    "No automatable less-than-requested bill-history account; "
                            + "manual account creation is not permitted for automation.");
        }
        final int lookbackMonths = 24;
        for (Map<String, Object> candidate : candidates) {
            String cust = firstNonNull(candidate, "ubbbhst_cust_code", "UBBBHST_CUST_CODE").toString();
            String prem = firstNonNull(candidate, "ubbbhst_prem_code", "UBBBHST_PREM_CODE").toString();
            ProbeResult probe = probeOk(cust, prem, lookbackMonths);
            if (probe != null && probe.matches > 0 && probe.matches < lookbackMonths) {
                DualReportManager.logInfo(
                        "TC_94 seed " + cust + "/" + prem
                                + " lookbackMonths=" + lookbackMonths
                                + " billMatches=" + probe.matches);
                return resolvedAccount(cust, prem, probe.matches, lookbackMonths);
            }
        }
        skipWhenNoAutomatableSeed(
                GetBillHistoryLabel.TC_94__Positive__Usage_History_Less_Than_Requested_Months.name(),
                "No probe-validated account with bill history less than requested months; "
                        + "manual account creation is not permitted for automation.");
        return null;
    }

    /**
     * SP1_07 / SP1_10: guaranteed-bill rate codes (CGBP, RGBP, SGBP, RGB6, SGB6, RPGC, SPGC)
     * with a positive billed charge. Skip if the API never returns a non-null guaranteedBillAmount.
     */
    private void applyGuaranteedBillHistorySeedOrSkip(GetBillHistoryRequest payload, String testCondition) {
        List<Map<String, Object>> candidates = new java.util.ArrayList<>();
        try {
            candidates.addAll(ApplicationContext.get().getDbAction().listBillHistoryGuaranteedBillSeeds());
        } catch (Exception e) {
            log.warn("Guaranteed bill seed query failed: {}", e.getMessage());
        }

        Set<String> seen = new HashSet<>();
        int probed = 0;
        final int maxProbes = 10;
        for (Map<String, Object> candidate : candidates) {
            if (candidate == null) {
                continue;
            }
            if (probed >= maxProbes) {
                break;
            }
            String cust;
            String prem;
            try {
                cust = firstNonNull(candidate,
                        "uabopen_cust_code", "UABOPEN_CUST_CODE",
                        "ubbbhst_cust_code", "UBBBHST_CUST_CODE").toString();
                prem = firstNonNull(candidate,
                        "uabopen_prem_code", "UABOPEN_PREM_CODE",
                        "ubbbhst_prem_code", "UBBBHST_PREM_CODE").toString();
            } catch (Exception e) {
                continue;
            }
            if (!seen.add(cust + "/" + prem)) {
                continue;
            }
            probed++;
            Response response = probeGetBillHistory(cust, prem, 24);
            if (response.statusCode() != 200) {
                continue;
            }
            Object err = response.jsonPath().get("errorCode");
            if (err == null) {
                err = response.jsonPath().get("ErrorCode");
            }
            Integer errorCode = err instanceof Number ? ((Number) err).intValue() : null;
            if (errorCode == null || errorCode != 0) {
                continue;
            }
            List<Object> amounts = response.jsonPath().getList("data.billHistory.guaranteedBillAmount");
            boolean hasGuaranteed = amounts != null && amounts.stream().anyMatch(v -> v != null);
            if (!hasGuaranteed) {
                continue;
            }
            final int lookbackMonths = 24;
            payload.setLoginID(null);
            payload.setCustomerCode(cust);
            payload.setPremisesCode(prem);
            payload.setNumberOfMonths(lookbackMonths);
            testContext.setCustomerCode(cust);
            testContext.setPremisesCode(prem);
            testContext.setRequestedNumberOfMonths(lookbackMonths);
            DualReportManager.logInfo(
                    testCondition + " Guaranteed Bill seed " + cust + "/" + prem
                            + " (loginID=null, numberOfMonths=" + lookbackMonths + ")");
            return;
        }

        skipWhenNoAutomatableSeed(testCondition,
                "No automatable Guaranteed Bill GetBillHistory seed; "
                        + "manual account creation is not permitted for automation.");
    }

    /**
     * SP1.11–SP1.14 request two months of history. numberOfMatches is the bill
     * count in that window, which can be greater than two when one month has two bills.
     */
    private void applyTwoMonthBannerValueSeed(GetBillHistoryRequest payload, GetBillHistoryLabel testCondition) {
        final int lookbackMonths = 2;
        List<Map<String, Object>> candidates = twoMonthCandidates(testCondition);
        if (candidates.isEmpty()) {
            skipWhenNoAutomatableSeed(testCondition.name(),
                    "No account with more than one qualifying bill in the last 2 months; "
                            + "manual account creation is not permitted for automation.");
        }

        int probed = 0;
        final int maxProbes = 20;
        for (Map<String, Object> candidate : candidates) {
            if (candidate == null || probed >= maxProbes) {
                break;
            }
            String cust;
            String prem;
            try {
                cust = firstNonNull(candidate, "ubbbhst_cust_code", "UBBBHST_CUST_CODE").toString();
                prem = firstNonNull(candidate, "ubbbhst_prem_code", "UBBBHST_PREM_CODE").toString();
            } catch (Exception e) {
                continue;
            }
            probed++;
            List<Map<String, Object>> rows = ApplicationContext.get().getDbAction()
                    .getBillHistory2(cust, prem, lookbackMonths);
            if (rows == null || rows.size() < 2 || !matchesTwoMonthValueCondition(testCondition, rows)) {
                continue;
            }
            payload.setLoginID(null);
            payload.setCustomerCode(cust);
            payload.setPremisesCode(prem);
            payload.setNumberOfMonths(lookbackMonths);
            testContext.setCustomerCode(cust);
            testContext.setPremisesCode(prem);
            testContext.setRequestedNumberOfMonths(lookbackMonths);
            testContext.setExpectedNumberOfMatches(rows.size());
            DualReportManager.logInfo(
                    testCondition.name() + " two-month seed " + cust + "/" + prem
                            + " lookbackMonths=" + lookbackMonths
                            + " billMatches=" + rows.size());
            return;
        }

        skipWhenNoAutomatableSeed(testCondition.name(),
                "No 2-month bill history account satisfied " + testCondition.name()
                        + "; manual account creation is not permitted for automation.");
    }

    private List<Map<String, Object>> twoMonthCandidates(GetBillHistoryLabel testCondition) {
        List<Map<String, Object>> candidates = new java.util.ArrayList<>();
        try {
            if (testCondition == GetBillHistoryLabel.SP1_13__Positive__Balance_Brought_Forward_Values) {
                candidates.addAll(ApplicationContext.get().getDbAction().listBillHistoryTwoMonthBalanceCandidates());
            } else if (testCondition == GetBillHistoryLabel.SP1_14__Positive__Promotional_Discount_Values) {
                candidates.addAll(ApplicationContext.get().getDbAction().listBillHistoryTwoMonthPromoCandidates());
            }
            if (candidates.isEmpty()
                    && testCondition != GetBillHistoryLabel.SP1_14__Positive__Promotional_Discount_Values) {
                candidates.addAll(ApplicationContext.get().getDbAction().listBillHistoryTwoMonthCandidates());
            }
        } catch (Exception e) {
            log.warn("Two-month bill history candidate query failed: {}", e.getMessage());
        }
        return candidates;
    }

    private static boolean matchesTwoMonthValueCondition(GetBillHistoryLabel testCondition,
                                                         List<Map<String, Object>> rows) {
        boolean everyBillHasServiceDates = rows.stream().allMatch(row ->
                present(column(row, "BILL_FROM_DATE"))
                        && present(column(row, "BILL_TO_DATE"))
                        && column(row, "DAYS_OF_SERVICE") != null);
        if (!everyBillHasServiceDates) {
            return false;
        }
        if (testCondition == GetBillHistoryLabel.SP1_12__Positive__Consumption_Values) {
            return rows.stream().anyMatch(row ->
                    greaterThanZero(column(row, "DAYS_OF_SERVICE"))
                            && greaterThanZero(column(row, "HEATING_DEGREE_DAYS"))
                            && greaterThanZero(column(row, "TOTAL_BILLED_CONSUMPTION")));
        }
        if (testCondition == GetBillHistoryLabel.SP1_13__Positive__Balance_Brought_Forward_Values) {
            return rows.stream().anyMatch(row -> notZero(column(row, "BALANCE_BROUGHT_FORWARD")));
        }
        if (testCondition == GetBillHistoryLabel.SP1_14__Positive__Promotional_Discount_Values) {
            return rows.stream().anyMatch(row -> notZero(column(row, "PROMOTIONAL_DISCOUNTS")));
        }
        return true;
    }

    private static Object column(Map<String, Object> row, String name) {
        Object value = row.get(name);
        if (value == null) {
            value = row.get(name.toLowerCase());
        }
        return value;
    }

    private static boolean present(Object value) {
        return value != null && !String.valueOf(value).isBlank();
    }

    private static boolean greaterThanZero(Object value) {
        return value != null && new java.math.BigDecimal(String.valueOf(value)).compareTo(java.math.BigDecimal.ZERO) > 0;
    }

    private static boolean notZero(Object value) {
        return value != null && new java.math.BigDecimal(String.valueOf(value)).compareTo(java.math.BigDecimal.ZERO) != 0;
    }

    private static void skipWhenNoAutomatableSeed(String testCondition, String reason) {
        String message = testCondition + " — " + reason;
        DualReportManager.logInfo("SKIPPED: " + message);
        throw new SkipException(message);
    }

    /**
     * Resolves an account for TC_95/TC_96.
     * {@code numberOfMonths} is a date lookback window ({@code ADD_MONTHS(SYSDATE, -N)}),
     * not a bill count. Multiple bills can fall in one calendar month, so bill count ≠ months.
     * {@code expectedMatches} is the bill count returned for that lookback window.
     */
    private Map<String, Object> resolveBillHistoryCandidate(boolean equalsMode) {
        String seedQuery = equalsMode
                ? DBQuery.SELECT_BILL_HISTORY_TC95_96_ACCOUNT
                : DBQuery.SELECT_BILL_HISTORY_TC96_ACCOUNT;
        List<Map<String, Object>> candidates = equalsMode
                ? ApplicationContext.get().getDbAction().listBillHistoryTc9596Candidates()
                : ApplicationContext.get().getDbAction().listBillHistoryTc96Candidates();
        if (candidates == null || candidates.isEmpty()) {
            throw new IllegalStateException(
                    equalsMode
                            ? "TC_95: no SP_GET_BILL_HISTORY candidate accounts in last 12 months."
                            : "TC_96: no SP_GET_BILL_HISTORY candidate accounts in last 9 months.");
        }

        final int tc96LookbackMonths = 9;

        for (Map<String, Object> candidate : candidates) {
            String cust = firstNonNull(candidate, "ubbbhst_cust_code", "UBBBHST_CUST_CODE").toString();
            String prem = firstNonNull(candidate, "ubbbhst_prem_code", "UBBBHST_PREM_CODE").toString();

            if (equalsMode) {
                ProbeResult at12 = probeOk(cust, prem, 12);
                if (at12 == null) {
                    continue;
                }
                log.info("Resolved TC_95 candidate {}/{} lookbackMonths=12 billMatches={}",
                        cust, prem, at12.matches);
                ApplicationContext.get().getDbAction()
                        .logSelectedBillHistoryRequestAccount(seedQuery, cust, prem, at12.matches);
                return resolvedAccount(cust, prem, at12.matches, 12);
            }

            ProbeResult at9 = probeOk(cust, prem, tc96LookbackMonths);
            if (at9 == null) {
                continue;
            }
            ProbeResult at12 = probeOk(cust, prem, 12);
            if (at12 == null || at12.matches <= at9.matches) {
                log.info("Skipping TC_96 candidate {}/{}: billsAt9={} billsAt12={}",
                        cust, prem, at9.matches, at12 == null ? null : at12.matches);
                continue;
            }

            log.info("Resolved TC_96 candidate {}/{} lookbackMonths={} billMatches={} billsAt12={}",
                    cust, prem, tc96LookbackMonths, at9.matches, at12.matches);
            ApplicationContext.get().getDbAction()
                    .logSelectedBillHistoryRequestAccount(seedQuery, cust, prem, at9.matches);
            return resolvedAccount(cust, prem, at9.matches, tc96LookbackMonths);
        }

        throw new IllegalStateException(
                equalsMode
                        ? "TC_95: no candidate returned HTTP 200 with bill history for a lookback window"
                        : "TC_96: no candidate with bill history beyond a 9-month lookback window");
    }

    private Map<String, Object> resolvedAccount(String cust, String prem, int expectedMatches, int requestedMonths) {
        Map<String, Object> resolved = new HashMap<>();
        resolved.put("ubbbhst_cust_code", cust);
        resolved.put("ubbbhst_prem_code", prem);
        resolved.put("expectedMatches", expectedMatches);
        resolved.put("requestedMonths", requestedMonths);
        return resolved;
    }

    private ProbeResult probeOk(String cust, String prem, int numberOfMonths) {
        Response response = probeGetBillHistory(cust, prem, numberOfMonths);
        int status = response.statusCode();
        Object err = response.jsonPath().get("errorCode");
        if (err == null) {
            err = response.jsonPath().get("ErrorCode");
        }
        Integer errorCode = err instanceof Number ? ((Number) err).intValue() : null;
        Integer matches = response.jsonPath().get("data.numberOfMatches");
        if (status != 200 || errorCode == null || errorCode != 0 || matches == null || matches <= 0) {
            log.info("Skipping bill-history candidate {}/{} months={}: status={} errorCode={} matches={}",
                    cust, prem, numberOfMonths, status, errorCode, matches);
            return null;
        }
        return new ProbeResult(matches);
    }

    private static final class ProbeResult {
        private final int matches;

        private ProbeResult(int matches) {
            this.matches = matches;
        }
    }

    private Response probeGetBillHistory(String customerCode, String premisesCode, int numberOfMonths) {
        GetBillHistoryRequest probe = BasePage.deserializeJsonToPojo(
                GetBillHistoryLabel.get_bill_history.toString(),
                GetBillHistoryRequest.class);
        probe.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
        probe.setLoginID(null);
        probe.setCustomerCode(customerCode);
        probe.setPremisesCode(premisesCode);
        probe.setNumberOfMonths(numberOfMonths);

        String uri = ApplicationContext.get().getEnvConfig().getBaseUri() + GET_BILL_HISTORY;
        return io.restassured.RestAssured.given()
                .relaxedHTTPSValidation()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + testContext.getAuthToken())
                .body(probe)
                .post(uri);
    }

    private static Object firstNonNull(Map<String, Object> map, String... keys) {
        for (String key : keys) {
            Object value = map.get(key);
            if (value != null) {
                return value;
            }
        }
        throw new IllegalStateException("None of keys found in DB result: " + String.join(", ", keys));
    }
}
