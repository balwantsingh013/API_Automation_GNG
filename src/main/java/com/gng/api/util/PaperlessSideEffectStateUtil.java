package com.gng.api.util;

import com.gng.api.context.ApplicationContext;
import com.gng.api.db.DBAction;
import com.gng.api.db.DBQuery;
import com.gng.api.report.DualReportManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.testng.Assert;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Captures MariaDB {@code custadv_email_verification_status} and Banner enrollment state
 * before/after UpdatePaperlessCommunications for review evidence.
 */
@Slf4j
public final class PaperlessSideEffectStateUtil {

    private PaperlessSideEffectStateUtil() {
    }

    public static final class Snapshot {
        private final String customerCode;
        private final String premisesCode;
        private final long maxVerificationId;
        private final Map<String, String> custAdvFields;
        private final Map<String, String> enrollmentFields;

        Snapshot(String customerCode,
                 String premisesCode,
                 long maxVerificationId,
                 Map<String, String> custAdvFields,
                 Map<String, String> enrollmentFields) {
            this.customerCode = customerCode;
            this.premisesCode = premisesCode;
            this.maxVerificationId = maxVerificationId;
            this.custAdvFields = custAdvFields;
            this.enrollmentFields = enrollmentFields;
        }

        public String summary() {
            return "account=" + customerCode + "/" + premisesCode
                    + " | maxVerificationId=" + maxVerificationId
                    + " | custadv={" + custAdvFields + "}"
                    + " | enrollment={" + enrollmentFields + "}";
        }
    }

    public static Snapshot capture(String customerCode, String premisesCode, String phaseLabel) {
        Objects.requireNonNull(customerCode, "customerCode");
        Objects.requireNonNull(premisesCode, "premisesCode");

        DBAction custAdvDb = ApplicationContext.get().getDbAction("mariadb");
        DBAction bannerDb = ApplicationContext.get().getDbAction();

        long maxId = custAdvDb.getMaxCustAdvPaperlessVerificationId(customerCode, premisesCode);
        Map<String, Object> custAdvRow = tryAnyLatestCustAdvRow(custAdvDb, customerCode, premisesCode);
        Map<String, String> custAdvFields = extractCustAdvFields(custAdvRow);

        Map<String, Object> account = bannerDb.tryLookupUcracctAccount(customerCode, premisesCode);
        String bannerEmail = bannerDb.getActiveBannerEmailForCustomer(customerCode);
        Map<String, Object> ocsepci = bannerDb.tryGetLatestOcsepciForAccount(customerCode, premisesCode);
        Map<String, String> enrollmentFields = extractEnrollmentFields(account, bannerEmail, ocsepci);

        Snapshot snapshot = new Snapshot(customerCode, premisesCode, maxId, custAdvFields, enrollmentFields);

        DualReportManager.logInfo(phaseLabel + " — " + snapshot.summary());

        String accountNumber = DBAction.buildCustAdvAccountNumber(customerCode, premisesCode);
        DualReportManager.logDatabaseQuery(
                annotateSqlForReport(
                        DBQuery.SELECT_CUSTADV_ANY_LATEST_PAPERLESS_TOKEN_FOR_ACCOUNT,
                        phaseLabel,
                        "account_number = '" + accountNumber + "' "
                                + "(customerCode='" + customerCode + "', premisesCode='" + premisesCode + "')",
                        "The '?' inside SUBSTRING_INDEX(..., '?', -1) is a URL delimiter; "
                                + "'t=' is stripped so Token is the hex confirm value (not 't=&lt;hex&gt;'). "
                                + "JDBC '?' placeholders above are filled at runtime."),
                custAdvRow == null ? "(no custadv_email_verification_status row)" : custAdvRow.toString(),
                0L);
        DualReportManager.logDatabaseQuery(
                annotateSqlForReport(
                        DBQuery.SELECT_UCRACCT_ACCOUNT_SUMMARY,
                        phaseLabel,
                        "UCRACCT_CUST_CODE = '" + customerCode + "', UCRACCT_PREM_CODE = '" + premisesCode + "'",
                        null),
                "bill=" + enrollmentFields.get("billDeliveryOption")
                        + ", corr=" + enrollmentFields.get("corrDeliveryOption")
                        + ", bannerEmail=" + enrollmentFields.get("bannerEmail")
                        + ", pendingOcsepciToken=" + enrollmentFields.get("pendingOcsepciTokenId")
                        + ", pendingBill=" + enrollmentFields.get("pendingBillType")
                        + ", pendingCorr=" + enrollmentFields.get("pendingCorrType")
                        + ", ocsepciCompleted=" + enrollmentFields.get("tokenCompletedDate"),
                0L);

        log.info("{} state for {}/{}: {}", phaseLabel, customerCode, premisesCode, snapshot.summary());
        return snapshot;
    }

    /**
     * Report-only annotation: keeps JDBC {@code ?} placeholders in the SQL text and documents bound values
     * for GNG reviewers. Does not change how the query is executed.
     */
    private static String annotateSqlForReport(String sql,
                                               String phaseLabel,
                                               String boundValues,
                                               String extraNote) {
        StringBuilder annotated = new StringBuilder(sql.trim());
        annotated.append("\n-- phase=").append(phaseLabel);
        annotated.append("\n-- Bound values (JDBC '?' placeholders above are filled at runtime): ");
        annotated.append(boundValues);
        if (extraNote != null && !extraNote.isBlank()) {
            annotated.append("\n-- Note: ").append(extraNote);
        }
        return annotated.toString();
    }

    public static void assertUnchanged(Snapshot before, Snapshot after, String context) {
        Assert.assertNotNull(before, context + ": missing BEFORE snapshot");
        Assert.assertNotNull(after, context + ": missing AFTER snapshot");
        Assert.assertEquals(after.customerCode, before.customerCode, context + ": customerCode mismatch");
        Assert.assertEquals(after.premisesCode, before.premisesCode, context + ": premisesCode mismatch");

        Assert.assertEquals(
                after.maxVerificationId,
                before.maxVerificationId,
                context + ": custadv max email_verification_status_id changed (unexpected new/removed token row). "
                        + "BEFORE=" + before.maxVerificationId + " AFTER=" + after.maxVerificationId);

        for (String key : before.custAdvFields.keySet()) {
            Assert.assertEquals(
                    after.custAdvFields.get(key),
                    before.custAdvFields.get(key),
                    context + ": custadv_email_verification_status." + key + " changed. "
                            + "BEFORE=" + before.custAdvFields.get(key)
                            + " AFTER=" + after.custAdvFields.get(key));
        }
        for (String key : after.custAdvFields.keySet()) {
            Assert.assertTrue(
                    before.custAdvFields.containsKey(key),
                    context + ": unexpected new custadv field after call: " + key);
        }

        for (String key : before.enrollmentFields.keySet()) {
            Assert.assertEquals(
                    after.enrollmentFields.get(key),
                    before.enrollmentFields.get(key),
                    context + ": enrollment state." + key + " changed. "
                            + "BEFORE=" + before.enrollmentFields.get(key)
                            + " AFTER=" + after.enrollmentFields.get(key));
        }

        DualReportManager.logInfo(
                context + " — PASSED: custadv_email_verification_status and enrollment state verified unchanged. "
                        + "BEFORE=[" + before.summary() + "] AFTER=[" + after.summary() + "]");
    }

    /**
     * Report-only BEFORE/AFTER comparison for ConfirmPaperlessEnrollment (and similar).
     * When {@code expectUnchanged} is true, also hard-asserts no side effects.
     */
    public static void logBeforeAfter(Snapshot before,
                                      Snapshot after,
                                      String context,
                                      boolean expectUnchanged) {
        Assert.assertNotNull(before, context + ": missing BEFORE snapshot");
        Assert.assertNotNull(after, context + ": missing AFTER snapshot");
        DualReportManager.logInfo(context + " — PRE: " + before.summary());
        DualReportManager.logInfo(context + " — POST: " + after.summary());

        boolean sameCustAdv = Objects.equals(before.custAdvFields, after.custAdvFields)
                && before.maxVerificationId == after.maxVerificationId;
        boolean sameEnrollment = Objects.equals(before.enrollmentFields, after.enrollmentFields);
        DualReportManager.logInfo(context + " — PRE vs POST: custadvUnchanged=" + sameCustAdv
                + ", enrollmentUnchanged=" + sameEnrollment
                + (expectUnchanged
                ? " (expected unchanged — no email/token/PPER update)"
                : " (update/rollback expected — compare PRE vs POST fields)"));

        if (expectUnchanged) {
            assertUnchanged(before, after, context);
        }
    }

    /** Explicit DURING snapshot for rollback scenarios (before/during/after). */
    public static void logDuring(Snapshot during, String context) {
        Assert.assertNotNull(during, context + ": missing DURING snapshot");
        DualReportManager.logInfo(context + " — DURING: " + during.summary());
    }

    private static Map<String, Object> tryAnyLatestCustAdvRow(DBAction custAdvDb,
                                                              String customerCode,
                                                              String premisesCode) {
        try {
            return custAdvDb.getAnyLatestConfirmPaperlessTokenFromCustAdv(customerCode, premisesCode, false);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    private static Map<String, String> extractCustAdvFields(Map<String, Object> row) {
        Map<String, String> fields = new LinkedHashMap<>();
        if (row == null) {
            fields.put("rowPresent", "false");
            return fields;
        }
        fields.put("rowPresent", "true");
        fields.put("email_verification_status_id", stringVal(row, "email_verification_status_id"));
        fields.put("verification_status", stringVal(row, "verification_status"));
        fields.put("tokenIdentifier", PaperlessConfirmationTokenUtil.normalizeTokenPlaintext(firstNonBlank(
                stringVal(row, "tokenIdentifier"),
                stringVal(row, "Token"),
                stringVal(row, "token"))));
        fields.put("date_time_link_confirmed", stringVal(row, "date_time_link_confirmed"));
        fields.put("date_time_link_expired", stringVal(row, "date_time_link_expired"));
        fields.put("bill_delivery_type", stringVal(row, "bill_delivery_type"));
        fields.put("correspondence_delivery_type", stringVal(row, "correspondence_delivery_type"));
        fields.put("email_sent_to", stringVal(row, "email_sent_to"));
        return fields;
    }

    private static Map<String, String> extractEnrollmentFields(Map<String, Object> account,
                                                               String bannerEmail,
                                                               Map<String, Object> ocsepci) {
        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("billDeliveryOption", account == null ? null : stringVal(account, "billDeliveryOption"));
        fields.put("corrDeliveryOption", account == null ? null : stringVal(account, "corrDeliveryOption"));
        fields.put("accountStatus", account == null ? null : stringVal(account, "accountStatus"));
        fields.put("bannerEmail", normalize(bannerEmail));
        fields.put("pendingOcsepciTokenId", ocsepci == null ? null : stringVal(ocsepci, "tokenIdentifier"));
        fields.put("pendingBillType", ocsepci == null ? null : stringVal(ocsepci, "pendingBillType"));
        fields.put("pendingCorrType", ocsepci == null ? null : stringVal(ocsepci, "pendingCorrType"));
        fields.put("tokenCompletedDate", ocsepci == null ? null : stringVal(ocsepci, "tokenCompletedDate"));
        return fields;
    }

    private static String stringVal(Map<String, Object> row, String key) {
        if (row == null) {
            return null;
        }
        Object value = row.get(key);
        if (value == null) {
            for (Map.Entry<String, Object> entry : row.entrySet()) {
                if (entry.getKey() != null && entry.getKey().equalsIgnoreCase(key)) {
                    value = entry.getValue();
                    break;
                }
            }
        }
        return normalize(value == null ? null : value.toString());
    }

    private static String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private static String firstNonBlank(String... values) {
        if (values == null) {
            return null;
        }
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }
}
