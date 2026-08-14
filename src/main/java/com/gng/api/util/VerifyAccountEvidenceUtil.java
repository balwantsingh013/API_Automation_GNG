package com.gng.api.util;

import com.gng.api.context.ApplicationContext;
import com.gng.api.db.DBAction;
import com.gng.api.db.DBQuery;
import com.gng.api.report.DualReportManager;
import io.restassured.path.json.JsonPath;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Report-only Banner/PPER vs Preferences VerifyAccount API evidence for FTD review.
 * Does not change account selection or soft-assert behavior.
 */
@Slf4j
public final class VerifyAccountEvidenceUtil {

    private VerifyAccountEvidenceUtil() {
    }

    /**
     * Captures Banner UCRACCT/UCRADDR/UCBPREM + latest OCSEPCI (PPER) and logs SQL evidence.
     */
    public static Map<String, String> captureBannerAndPper(String customerCode,
                                                           String premisesCode,
                                                           String phaseLabel) {
        Objects.requireNonNull(customerCode, "customerCode");
        Objects.requireNonNull(premisesCode, "premisesCode");

        DBAction bannerDb = ApplicationContext.get().getDbAction();
        Map<String, Object> banner = bannerDb.tryGetVerifyAccountBannerEvidence(customerCode, premisesCode);
        Map<String, Object> ocsepci = bannerDb.tryGetLatestOcsepciForAccount(customerCode, premisesCode);

        Map<String, String> snapshot = new LinkedHashMap<>();
        snapshot.put("customerCode", customerCode);
        snapshot.put("premisesCode", premisesCode);
        snapshot.put("bannerPresent", banner == null ? "false" : "true");

        if (banner == null) {
            DualReportManager.logInfo(phaseLabel
                    + " — Banner UCRACCT: (no row for " + customerCode + "/" + premisesCode + ")");
            DualReportManager.logDatabaseQuery(
                    annotateSql(DBQuery.SELECT_VERIFY_ACCOUNT_BANNER_EVIDENCE, phaseLabel,
                            "UCRACCT_CUST_CODE='" + customerCode + "', UCRACCT_PREM_CODE='" + premisesCode + "'"),
                    "(no Banner UCRACCT row)",
                    0L);
        } else {
            put(snapshot, "accountStatus", banner, "accountStatus");
            put(snapshot, "billPresType", banner, "billPresType");
            put(snapshot, "correspondencePreference", banner, "correspondencePreference");
            put(snapshot, "billingStreetNumber", banner, "billingStreetNumber");
            put(snapshot, "billingStreetPreDirection", banner, "billingStreetPreDirection");
            put(snapshot, "billingStreetName", banner, "billingStreetName");
            put(snapshot, "billingCity", banner, "billingCity");
            put(snapshot, "billingStateCode", banner, "billingStateCode");
            put(snapshot, "billingZipCode", banner, "billingZipCode");
            put(snapshot, "billingPoBox", banner, "billingPoBox");
            put(snapshot, "premisesStreetNumber", banner, "premisesStreetNumber");
            put(snapshot, "premisesCity", banner, "premisesCity");
            put(snapshot, "premisesStateCode", banner, "premisesStateCode");
            put(snapshot, "premisesZipCode", banner, "premisesZipCode");
            put(snapshot, "billDeliveryConfirmDate", banner, "billDeliveryConfirmDate");
            put(snapshot, "corrDeliveryConfirmDate", banner, "corrDeliveryConfirmDate");

            DualReportManager.logInfo(phaseLabel + " — Banner snapshot: " + summarizeBanner(snapshot));
            DualReportManager.logDatabaseQuery(
                    annotateSql(DBQuery.SELECT_VERIFY_ACCOUNT_BANNER_EVIDENCE, phaseLabel,
                            "UCRACCT_CUST_CODE='" + customerCode + "', UCRACCT_PREM_CODE='" + premisesCode + "'"),
                    banner.toString(),
                    0L);
        }

        if (ocsepci == null) {
            snapshot.put("pperPresent", "false");
            DualReportManager.logInfo(phaseLabel + " — PPER/OCSEPCI: (none)");
        } else {
            snapshot.put("pperPresent", "true");
            put(snapshot, "pendingBillType", ocsepci, "pendingBillType");
            put(snapshot, "pendingCorrType", ocsepci, "pendingCorrType");
            put(snapshot, "ocsepciTokenId", ocsepci, "tokenIdentifier");
            put(snapshot, "tokenCompletedDate", ocsepci, "tokenCompletedDate");
            put(snapshot, "tokenExpirationDate", ocsepci, "tokenExpirationDate");
            put(snapshot, "confStatus", ocsepci, "confStatus");
            DualReportManager.logInfo(phaseLabel + " — PPER/OCSEPCI latest: " + ocsepci);
            DualReportManager.logDatabaseQuery(
                    annotateSql(DBQuery.SELECT_LATEST_OCSEPCI_FOR_ACCOUNT, phaseLabel,
                            "OCSEPCI_CUST_CODE='" + customerCode + "', OCSEPCI_PREM_CODE='" + premisesCode + "'"),
                    ocsepci.toString(),
                    0L);
        }

        log.info("{} VerifyAccount evidence {}/{}: {}", phaseLabel, customerCode, premisesCode, snapshot);
        return snapshot;
    }

    public static void logBeforeAfter(String context,
                                      Map<String, String> before,
                                      Map<String, String> after) {
        DualReportManager.logInfo(context + " — PRE-UPDATE: " + before);
        DualReportManager.logInfo(context + " — POST-UPDATE: " + after);
        DualReportManager.logInfo(context + " — PRE vs POST match="
                + Objects.equals(before, after)
                + " (false means Banner/PPER changed as expected for UpdatePaperless bootstrap)");
    }

    /**
     * Logs table evidence that paperless enrollment is Initiated / In Progress for GetAccountInfo
     * TC_13 (bill) / TC_14 (corr): confirmation link not expired and not completed.
     * <p>
     * Primary source on UAT1 is MariaDB {@code custadv_email_verification_status}
     * ({@code date_time_link_confirmed IS NULL}, {@code date_time_link_expired} null/future).
     * Banner {@code OCSEPCI} is logged as supplemental when present.
     * Throws if neither source proves In Progress for the channel.
     */
    public static Map<String, Object> captureInitiatedDeliveryInProgressEvidence(String customerCode,
                                                                                 String premisesCode,
                                                                                 boolean billChannel,
                                                                                 String phaseLabel) {
        Objects.requireNonNull(customerCode, "customerCode");
        Objects.requireNonNull(premisesCode, "premisesCode");

        String channelLabel = billChannel ? "bill" : "correspondence";
        String accountNumber = DBAction.buildCustAdvAccountNumber(customerCode, premisesCode);

        DBAction custAdvDb = ApplicationContext.get().getDbAction("mariadb");
        String custAdvSql = billChannel
                ? DBQuery.SELECT_CUSTADV_IN_PROGRESS_BILL_TOKEN_FOR_ACCOUNT
                : DBQuery.SELECT_CUSTADV_IN_PROGRESS_CORR_TOKEN_FOR_ACCOUNT;
        Map<String, Object> custAdv = billChannel
                ? custAdvDb.tryGetCustAdvInProgressBillTokenForAccount(customerCode, premisesCode)
                : custAdvDb.tryGetCustAdvInProgressCorrTokenForAccount(customerCode, premisesCode);
        if (custAdv == null) {
            custAdv = custAdvDb.tryGetCustAdvInProgressTokenForAccount(customerCode, premisesCode);
            if (custAdv != null) {
                DualReportManager.logInfo(phaseLabel
                        + " — custadv channel-typed In Progress row not found; using any unused/not-expired link row");
                custAdvSql = DBQuery.SELECT_CUSTADV_IN_PROGRESS_TOKEN_FOR_ACCOUNT;
            }
        }

        String custAdvBind = "account_number='" + accountNumber + "'";
        boolean custAdvProvesInProgress = false;
        if (custAdv == null) {
            DualReportManager.logInfo(phaseLabel
                    + " — custadv In Progress proof (" + channelLabel
                    + "): (none — link expired/completed or no matching type E)");
            DualReportManager.logDatabaseQuery(
                    annotateSql(custAdvSql, phaseLabel, custAdvBind),
                    "(no In Progress custadv row)",
                    0L);
        } else {
            String linkNotExpired = stringVal(custAdv.get("linkNotExpired"));
            String linkNotCompleted = stringVal(custAdv.get("linkNotCompleted"));
            DualReportManager.logInfo(phaseLabel + " — custadv In Progress proof (" + channelLabel + "): "
                    + "enrollmentStatus=" + stringVal(custAdv.get("enrollmentStatus"))
                    + ", linkNotExpired=" + linkNotExpired
                    + " (date_time_link_expired="
                    + display(stringVal(custAdv.get("date_time_link_expired"))) + ")"
                    + ", linkNotCompleted=" + linkNotCompleted
                    + " (date_time_link_confirmed="
                    + display(stringVal(custAdv.get("date_time_link_confirmed"))) + ")"
                    + ", bill_delivery_type=" + stringVal(custAdv.get("bill_delivery_type"))
                    + ", correspondence_delivery_type="
                    + stringVal(custAdv.get("correspondence_delivery_type"))
                    + ", email_verification_status_id="
                    + stringVal(custAdv.get("email_verification_status_id")));
            DualReportManager.logDatabaseQuery(
                    annotateSql(custAdvSql, phaseLabel, custAdvBind), custAdv.toString(), 0L);
            custAdvProvesInProgress = "Y".equalsIgnoreCase(linkNotExpired)
                    && "Y".equalsIgnoreCase(linkNotCompleted);
        }

        DBAction bannerDb = ApplicationContext.get().getDbAction();
        String ocsepciSql = billChannel
                ? DBQuery.SELECT_ACTIVE_PENDING_BILL_PPER_FOR_ACCOUNT
                : DBQuery.SELECT_ACTIVE_PENDING_CORR_PPER_FOR_ACCOUNT;
        Map<String, Object> ocsepci = billChannel
                ? bannerDb.tryGetActivePendingBillPperForAccount(customerCode, premisesCode)
                : bannerDb.tryGetActivePendingCorrPperForAccount(customerCode, premisesCode);
        String ocsepciBind = "OCSEPCI_CUST_CODE='" + customerCode
                + "', OCSEPCI_PREM_CODE='" + premisesCode + "'";
        boolean ocsepciProvesInProgress = false;
        if (ocsepci == null) {
            DualReportManager.logInfo(phaseLabel
                    + " — OCSEPCI In Progress proof (" + channelLabel
                    + "): (none — supplemental Banner PPER not present)");
            DualReportManager.logDatabaseQuery(
                    annotateSql(ocsepciSql, phaseLabel, ocsepciBind),
                    "(no active pending " + channelLabel + " OCSEPCI row)",
                    0L);
        } else {
            String pendingTypeKey = billChannel ? "pendingBillType" : "pendingCorrType";
            String pendingType = stringVal(ocsepci.get(pendingTypeKey));
            String linkNotExpired = stringVal(ocsepci.get("linkNotExpired"));
            String linkNotCompleted = stringVal(ocsepci.get("linkNotCompleted"));
            DualReportManager.logInfo(phaseLabel + " — OCSEPCI In Progress proof (" + channelLabel + "): "
                    + "enrollmentStatus=" + stringVal(ocsepci.get("enrollmentStatus"))
                    + ", " + pendingTypeKey + "=" + pendingType
                    + ", linkNotExpired=" + linkNotExpired
                    + " (EMAIL_EXP_DATE=" + stringVal(ocsepci.get("tokenExpirationDate")) + ")"
                    + ", linkNotCompleted=" + linkNotCompleted
                    + " (EMAIL_COMP_DATE="
                    + display(stringVal(ocsepci.get("tokenCompletedDate"))) + ")"
                    + ", tokenIdentifier=" + stringVal(ocsepci.get("tokenIdentifier")));
            DualReportManager.logDatabaseQuery(
                    annotateSql(ocsepciSql, phaseLabel, ocsepciBind), ocsepci.toString(), 0L);
            ocsepciProvesInProgress = "E".equalsIgnoreCase(pendingType)
                    && "Y".equalsIgnoreCase(linkNotExpired)
                    && "Y".equalsIgnoreCase(linkNotCompleted);
        }

        if (!custAdvProvesInProgress && !ocsepciProvesInProgress) {
            throw new IllegalStateException(phaseLabel
                    + ": no In Progress confirmation-link proof for " + customerCode + "/" + premisesCode
                    + " (" + channelLabel
                    + " — need custadv date_time_link_confirmed IS NULL and not expired,"
                    + " and/or OCSEPCI EMAIL_COMP_DATE IS NULL and EMAIL_EXP_DATE > SYSDATE)");
        }

        DualReportManager.logInfo(phaseLabel + " — In Progress evidence accepted: custadv="
                + custAdvProvesInProgress + ", OCSEPCI=" + ocsepciProvesInProgress);

        Map<String, Object> evidence = new LinkedHashMap<>();
        evidence.put("customerCode", customerCode);
        evidence.put("premisesCode", premisesCode);
        evidence.put("channel", channelLabel);
        evidence.put("custadvProvesInProgress", custAdvProvesInProgress);
        evidence.put("ocsepciProvesInProgress", ocsepciProvesInProgress);
        if (custAdv != null) {
            evidence.put("custadv", custAdv);
        }
        if (ocsepci != null) {
            evidence.put("ocsepci", ocsepci);
        }
        log.info("{} In Progress evidence {}/{}: {}", phaseLabel, customerCode, premisesCode, evidence);
        return evidence;
    }

    private static String stringVal(Object value) {
        return value == null ? null : value.toString().trim();
    }

    /**
     * Logs DB vs API values for the fields under test in each VerifyAccount TC.
     */
    public static void logEvidenceForScenario(String testCondition,
                                              String customerCode,
                                              String premisesCode,
                                              JsonPath jsonPath,
                                              Map<String, String> expectedAccount) {
        Map<String, String> banner = captureBannerAndPper(
                customerCode, premisesCode, testCondition + " DB evidence");

        DualReportManager.logInfo(testCondition + " — Preferences API data (normalized): "
                + summarizeApiData(jsonPath));
        if (expectedAccount != null && !expectedAccount.isEmpty()) {
            DualReportManager.logInfo(testCondition + " — Expected account data: " + expectedAccount);
        }

        switch (testCondition) {
            case "TC_206__Positive__Billing_Street_Number_Value_":
            case "TC_207__Positive__Billing_Street_Number_Format_":
                logFieldWithExpected(testCondition, "billingStreetNumber",
                        resolveAddressDb(banner, "billingStreetNumber", "premisesStreetNumber"),
                        expected(expectedAccount, "billingStreetNumber"),
                        api(jsonPath, "data.billingStreetNumber"), false);
                break;
            case "TC_208__Positive__Billing_PreDir_Value_":
            case "TC_209__Positive__Billing_PreDir_Format_":
                logFieldWithExpected(testCondition, "billingStreetPreDirection",
                        blankToNull(banner.get("billingStreetPreDirection")),
                        expected(expectedAccount, "billingStreetPreDirection"),
                        api(jsonPath, "data.billingStreetPreDirection"), true);
                break;
            case "TC_210__Positive__Billing_Street_Name_Value_":
            case "TC_211__Positive__Billing_Street_Name_Format_":
                logFieldWithExpected(testCondition, "billingStreetName",
                        blankToNull(banner.get("billingStreetName")),
                        expected(expectedAccount, "billingStreetName"),
                        api(jsonPath, "data.billingStreetName"), false);
                break;
            case "TC_212__Positive__Billing_Street_Suffix_Value_":
            case "TC_213__Positive__Billing_Street_Suffix_Format_":
                logFieldWithExpected(testCondition, "billingStreetSuffix",
                        blankToNull(banner.get("billingStreetSuffix")),
                        expected(expectedAccount, "billingStreetSuffix"),
                        api(jsonPath, "data.billingStreetSuffix"), true);
                break;
            case "TC_214__Positive__Billing_PostDir_Value_":
            case "TC_215__Positive__Billing_PostDir_Format_":
                logFieldWithExpected(testCondition, "billingStreetPostDirection",
                        blankToNull(banner.get("billingStreetPostDirection")),
                        expected(expectedAccount, "billingStreetPostDirection"),
                        api(jsonPath, "data.billingStreetPostDirection"), true);
                break;
            case "TC_216__Positive__Billing_Unit_Type_Value_":
            case "TC_217__Positive__Billing_Unit_Type_Format_":
                logFieldWithExpected(testCondition, "billingUnitType",
                        blankToNull(banner.get("billingUnitType")),
                        expected(expectedAccount, "billingUnitType"),
                        api(jsonPath, "data.billingUnitType"), true);
                break;
            case "TC_218__Positive__Billing_Unit_Number_Value_":
            case "TC_219__Positive__Billing_Unit_Number_Format_":
                logFieldWithExpected(testCondition, "billingUnitNumber",
                        blankToNull(banner.get("billingUnitNumber")),
                        expected(expectedAccount, "billingUnitNumber"),
                        api(jsonPath, "data.billingUnitNumber"), true);
                break;
            case "TC_220__Positive__Billing_City_Value_":
            case "TC_221__Positive__Billing_City_Format_":
                logFieldWithExpected(testCondition, "billingCity",
                        resolveAddressDb(banner, "billingCity", "premisesCity"),
                        expected(expectedAccount, "billingCity"),
                        api(jsonPath, "data.billingCity"), false);
                break;
            case "TC_222__Positive__Billing_State_Value_":
            case "TC_223__Positive__Billing_State_Format_":
                logFieldWithExpected(testCondition, "billingState",
                        resolveAddressDb(banner, "billingStateCode", "premisesStateCode"),
                        firstNonBlank(expected(expectedAccount, "billingStateCode"),
                                expected(expectedAccount, "billingState")),
                        firstNonBlank(api(jsonPath, "data.billingState"),
                                api(jsonPath, "data.billingStateCode")), false);
                break;
            case "TC_224__Positive__Billing_ZIP_Value_":
            case "TC_225__Positive__Billing_ZIP_Format_":
                logZipWithExpected(testCondition,
                        resolveAddressDb(banner, "billingZipCode", "premisesZipCode"),
                        firstNonBlank(expected(expectedAccount, "billingZipCode"),
                                expected(expectedAccount, "billingZip")),
                        firstNonBlank(api(jsonPath, "data.billingZip"),
                                api(jsonPath, "data.billingZipCode")));
                break;
            case "TC_226__Positive__Billing_PO_Box_Value_":
            case "TC_227__Positive__Billing_PO_Box_Format_":
                logFieldWithExpected(testCondition, "billingPoBox",
                        blankToNull(banner.get("billingPoBox")),
                        expected(expectedAccount, "billingPoBox"),
                        firstNonBlank(api(jsonPath, "data.billingPoBox"),
                                api(jsonPath, "data.BillingPoBox")), true);
                break;
            case "TC_228__Positive__GTBENRL_Billing_Address_Source_":
            case "TC_229__Positive__GTBENRL_Billing_Address_Parsing_":
                DualReportManager.logInfo(testCondition + " — GTBENRL expected data: " + expectedAccount);
                logFieldWithExpected(testCondition, "billingStreetNumber",
                        expected(expectedAccount, "billingStreetNumber"),
                        expected(expectedAccount, "billingStreetNumber"),
                        api(jsonPath, "data.billingStreetNumber"), true);
                logFieldWithExpected(testCondition, "billingCity",
                        expected(expectedAccount, "billingCity"),
                        expected(expectedAccount, "billingCity"),
                        api(jsonPath, "data.billingCity"), false);
                break;
            case "TC_230__Positive__Bill_Delivery_Confirmation_Date_Value_":
            case "TC_231__Positive__Bill_Delivery_Confirmation_Date_Format_":
                logFieldWithExpected(testCondition, "billDeliveryConfirmDate",
                        blankToNull(banner.get("billDeliveryConfirmDate")),
                        expected(expectedAccount, "billDeliveryConfirmDate"),
                        api(jsonPath, "data.billDeliveryConfirmDate"), true);
                break;
            case "TC_232__Positive__Corr_Delivery_Confirmation_Date_Value_":
            case "TC_233__Positive__Corr_Delivery_Confirmation_Date_Format_":
                logFieldWithExpected(testCondition, "corrDeliveryConfirmDate",
                        blankToNull(banner.get("corrDeliveryConfirmDate")),
                        expected(expectedAccount, "corrDeliveryConfirmDate"),
                        api(jsonPath, "data.corrDeliveryConfirmDate"), true);
                break;
            case "TC_234__Positive__Bill_Delivery_Preference_Confirmed_":
            case "TC_236__Positive__Bill_Delivery_Preference_Initiated_":
            case "TC_238__Positive__Bill_Delivery_Preference_Paper_":
            case "TC_240__Positive__Bill_Delivery_Preference_Expired_":
                logPreference(testCondition, "billPresType",
                        blankToNull(banner.get("billPresType")),
                        blankToNull(banner.get("pendingBillType")),
                        firstNonBlank(expected(expectedAccount, "billPresType"),
                                expected(expectedAccount, "billDeliveryOption")),
                        firstNonBlank(api(jsonPath, "data.billPresType"),
                                api(jsonPath, "data.billDeliveryOption")));
                break;
            case "TC_235__Positive__Correspondence_Delivery_Preference_Confirmed_":
            case "TC_237__Positive__Correspondence_Delivery_Preference_Initiated_":
            case "TC_239__Positive__Correspondence_Delivery_Preference_Paper_":
            case "TC_241__Positive__Correspondence_Delivery_Preference_Expired_":
                logPreference(testCondition, "correspondencePreference",
                        blankToNull(banner.get("correspondencePreference")),
                        blankToNull(banner.get("pendingCorrType")),
                        firstNonBlank(expected(expectedAccount, "correspondencePreference"),
                                expected(expectedAccount, "corrDeliveryOption")),
                        firstNonBlank(api(jsonPath, "data.correspondencePreference"),
                                api(jsonPath, "data.correspondenceDeliveryOption")));
                break;
            default:
                DualReportManager.logInfo(testCondition
                        + " — no field-specific DB/API match matrix (success path only)");
                break;
        }
    }

    private static void logPreference(String context,
                                      String field,
                                      String bannerValue,
                                      String pperPendingValue,
                                      String expectedValue,
                                      String apiValue) {
        DualReportManager.logInfo(context + " — DB(Banner UCRACCT." + field + ")="
                + display(bannerValue)
                + " | DB(PPER pending)=" + display(pperPendingValue)
                + " | Expected=" + display(expectedValue)
                + " | API=" + display(apiValue)
                + " | matchBanner=" + matchStatus(bannerValue, apiValue, false)
                + " | matchPperOrBanner=" + matchStatus(
                firstNonBlank(pperPendingValue, bannerValue), apiValue, false)
                + " | matchExpected=" + matchStatus(expectedValue, apiValue, false));
    }

    private static void logFieldWithExpected(String context,
                                         String field,
                                         String bannerDbValue,
                                         String expectedValue,
                                         String apiValue,
                                         boolean optional) {
        DualReportManager.logInfo(context + " — field=" + field
                + " | DB(Banner)=" + display(bannerDbValue)
                + " | Expected=" + display(expectedValue)
                + " | API=" + display(apiValue)
                + " | matchBanner=" + matchStatus(bannerDbValue, apiValue, optional)
                + " | matchExpected=" + matchStatus(expectedValue, apiValue, optional));
    }

    private static void logZipWithExpected(String context,
                                       String bannerDbValue,
                                       String expectedValue,
                                       String apiValue) {
        DualReportManager.logInfo(context + " — field=billingZipCode"
                + " | DB(Banner)=" + display(bannerDbValue)
                + " | Expected=" + display(expectedValue)
                + " | API=" + display(apiValue)
                + " | matchBanner=" + (isBlank(bannerDbValue)
                ? "N/A (no Banner value — Preferences-only account or column null)"
                : (zipMatches(bannerDbValue, apiValue) ? "YES" : "NO"))
                + " | matchExpected=" + (isBlank(expectedValue)
                ? "N/A (no expected value)"
                : (zipMatches(expectedValue, apiValue) ? "YES" : "NO")));
    }

    private static String expected(Map<String, String> expectedAccount, String key) {
        if (expectedAccount == null) {
            return null;
        }
        return blankToNull(expectedAccount.get(key));
    }

    private static String matchStatus(String dbValue, String apiValue, boolean optional) {
        if (isBlank(dbValue) && isBlank(apiValue)) {
            return optional ? "N/A (both empty)" : "N/A (both null)";
        }
        if (isBlank(dbValue)) {
            return "N/A (no Banner value — Preferences-only account or column null)";
        }
        if (isBlank(apiValue)) {
            return optional ? "N/A (API empty/optional)" : "NO (API empty)";
        }
        return dbValue.trim().equalsIgnoreCase(apiValue.trim()) ? "YES" : "NO";
    }

    private static boolean zipMatches(String dbValue, String apiValue) {
        if (isBlank(dbValue) || isBlank(apiValue)) {
            return false;
        }
        String expected = dbValue.trim();
        String actual = apiValue.trim();
        String actualBase = actual.replaceAll("-.*", "");
        return actual.equalsIgnoreCase(expected)
                || actual.regionMatches(true, 0, expected, 0, expected.length())
                || expected.regionMatches(true, 0, actualBase, 0, actualBase.length());
    }

    private static String resolveAddressDb(Map<String, String> banner,
                                           String billingKey,
                                           String premisesKey) {
        String billing = blankToNull(banner.get(billingKey));
        if (billing != null) {
            return billing;
        }
        return blankToNull(banner.get(premisesKey));
    }

    private static String summarizeBanner(Map<String, String> snapshot) {
        return "status=" + snapshot.get("accountStatus")
                + ", bill=" + snapshot.get("billPresType")
                + ", corr=" + snapshot.get("correspondencePreference")
                + ", street#=" + snapshot.get("billingStreetNumber")
                + ", city=" + snapshot.get("billingCity")
                + ", state=" + snapshot.get("billingStateCode")
                + ", zip=" + snapshot.get("billingZipCode")
                + ", premStreet#=" + snapshot.get("premisesStreetNumber");
    }

    private static String summarizeApiData(JsonPath jsonPath) {
        Map<String, String> api = new LinkedHashMap<>();
        api.put("billingStreetNumber", api(jsonPath, "data.billingStreetNumber"));
        api.put("billingStreetPreDirection", api(jsonPath, "data.billingStreetPreDirection"));
        api.put("billingStreetName", api(jsonPath, "data.billingStreetName"));
        api.put("billingCity", api(jsonPath, "data.billingCity"));
        api.put("billingStateCode", firstNonBlank(
                api(jsonPath, "data.billingStateCode"), api(jsonPath, "data.billingState")));
        api.put("billingZipCode", api(jsonPath, "data.billingZipCode"));
        api.put("billingPoBox", firstNonBlank(
                api(jsonPath, "data.billingPoBox"), api(jsonPath, "data.BillingPoBox")));
        api.put("billPresType", firstNonBlank(
                api(jsonPath, "data.billPresType"), api(jsonPath, "data.billDeliveryOption")));
        api.put("correspondencePreference", firstNonBlank(
                api(jsonPath, "data.correspondencePreference"),
                api(jsonPath, "data.correspondenceDeliveryOption")));
        api.put("billDeliveryConfirmDate", api(jsonPath, "data.billDeliveryConfirmDate"));
        api.put("corrDeliveryConfirmDate", api(jsonPath, "data.corrDeliveryConfirmDate"));
        api.put("accountStatus", api(jsonPath, "data.accountStatus"));
        return api.toString();
    }

    private static String annotateSql(String sql, String phaseLabel, String boundValues) {
        return sql.trim()
                + "\n-- phase=" + phaseLabel
                + "\n-- Bound values (JDBC '?' placeholders above are filled at runtime): "
                + boundValues;
    }

    private static void put(Map<String, String> target, String key, Map<String, Object> source, String sourceKey) {
        Object value = source.get(sourceKey);
        target.put(key, value == null ? null : value.toString().trim());
    }

    private static String api(JsonPath jsonPath, String path) {
        try {
            String value = jsonPath.getString(path);
            return blankToNull(value);
        } catch (Exception ex) {
            return null;
        }
    }

    private static String display(String value) {
        return value == null || value.isBlank() ? "(null)" : value;
    }

    private static String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private static String firstNonBlank(String... values) {
        if (values == null) {
            return null;
        }
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
        }
        return null;
    }
}
