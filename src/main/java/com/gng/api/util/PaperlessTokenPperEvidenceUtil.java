package com.gng.api.util;

import com.gng.api.context.ApplicationContext;
import com.gng.api.db.DBAction;
import com.gng.api.db.DBQuery;
import com.gng.api.report.DualReportManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Report-only evidence for token replacement and PPER (OCSEPCI) before/after state.
 * Does not change API/enrollment behavior — logging helpers for Extent review.
 */
@Slf4j
public final class PaperlessTokenPperEvidenceUtil {

    private static final int TOKEN_HEAD = 16;
    private static final int TOKEN_TAIL = 12;

    private PaperlessTokenPperEvidenceUtil() {
    }

    /** Truncates long custadv tokens for readable report output. */
    public static String displayToken(String token) {
        if (token == null || token.isBlank()) {
            return "(none)";
        }
        String value = token.trim();
        if (value.length() <= TOKEN_HEAD + TOKEN_TAIL + 3) {
            return value;
        }
        return value.substring(0, TOKEN_HEAD) + "..." + value.substring(value.length() - TOKEN_TAIL)
                + " (len=" + value.length() + ")";
    }

    public static String tryAnyCustAdvToken(String customerCode, String premisesCode) {
        return ApplicationContext.get().getDbAction("mariadb")
                .tryGetAnyConfirmPaperlessTokenFromCustAdv(customerCode, premisesCode);
    }

    public static String tryUnusedCustAdvToken(String customerCode, String premisesCode) {
        return ApplicationContext.get().getDbAction("mariadb")
                .tryGetLatestConfirmPaperlessTokenFromCustAdv(customerCode, premisesCode, false);
    }

    public static Map<String, String> capturePperSnapshot(String customerCode, String premisesCode) {
        Map<String, String> snapshot = new LinkedHashMap<>();
        snapshot.put("customerCode", customerCode);
        snapshot.put("premisesCode", premisesCode);

        Map<String, Object> ocsepci = ApplicationContext.get().getDbAction()
                .tryGetLatestOcsepciForAccount(customerCode, premisesCode);
        if (ocsepci == null) {
            snapshot.put("pperPresent", "false");
            snapshot.put("ocsepciTokenId", null);
            snapshot.put("pendingBillType", null);
            snapshot.put("pendingCorrType", null);
            snapshot.put("pendingEmail", null);
            snapshot.put("tokenExpirationDate", null);
            snapshot.put("tokenCompletedDate", null);
            snapshot.put("confStatus", null);
        } else {
            snapshot.put("pperPresent", "true");
            snapshot.put("ocsepciTokenId", stringVal(ocsepci, "tokenIdentifier"));
            snapshot.put("pendingBillType", stringVal(ocsepci, "pendingBillType"));
            snapshot.put("pendingCorrType", stringVal(ocsepci, "pendingCorrType"));
            snapshot.put("pendingEmail", stringVal(ocsepci, "pendingEmail"));
            snapshot.put("tokenExpirationDate", stringVal(ocsepci, "tokenExpirationDate"));
            snapshot.put("tokenCompletedDate", stringVal(ocsepci, "tokenCompletedDate"));
            snapshot.put("confStatus", stringVal(ocsepci, "confStatus"));
        }

        String custAdvToken = tryAnyCustAdvToken(customerCode, premisesCode);
        snapshot.put("custadvTokenPreview", displayToken(custAdvToken));
        try {
            Map<String, Object> custAdvRow = ApplicationContext.get().getDbAction("mariadb")
                    .getAnyLatestConfirmPaperlessTokenFromCustAdv(customerCode, premisesCode, false);
            snapshot.put("custadvVerificationId", stringVal(custAdvRow, "email_verification_status_id"));
            snapshot.put("custadvBillType", stringVal(custAdvRow, "bill_delivery_type"));
            snapshot.put("custadvCorrType", stringVal(custAdvRow, "correspondence_delivery_type"));
            snapshot.put("custadvEmailSentTo", stringVal(custAdvRow, "email_sent_to"));
            snapshot.put("custadvLinkConfirmed", stringVal(custAdvRow, "date_time_link_confirmed"));
        } catch (EmptyResultDataAccessException ex) {
            snapshot.put("custadvVerificationId", null);
            snapshot.put("custadvBillType", null);
            snapshot.put("custadvCorrType", null);
            snapshot.put("custadvEmailSentTo", null);
            snapshot.put("custadvLinkConfirmed", null);
        }
        return snapshot;
    }

    public static void logTokenBeforeAfter(String context, String beforeToken, String afterToken) {
        DualReportManager.logInfo(
                context + " — TOKEN BEFORE/AFTER: before=" + displayToken(beforeToken)
                        + " | after=" + displayToken(afterToken)
                        + " | replaced=" + Boolean.toString(
                        beforeToken != null && afterToken != null && !beforeToken.equals(afterToken)));
    }

    public static void logPperBeforeAfter(String context,
                                          Map<String, String> before,
                                          Map<String, String> after) {
        DualReportManager.logInfo(
                context + " — PPER/OCSEPCI BEFORE: " + before);
        DualReportManager.logInfo(
                context + " — PPER/OCSEPCI AFTER: " + after);
        DualReportManager.logDatabaseQuery(
                DBQuery.SELECT_LATEST_OCSEPCI_FOR_ACCOUNT
                        + "\n-- phase=" + context + " BEFORE/AFTER PPER comparison"
                        + "\n-- account=" + before.get("customerCode") + "/" + before.get("premisesCode"),
                "BEFORE=" + before + " | AFTER=" + after,
                0L);
    }

    /** Logs recent OCSEPCI rows so reviewers can see old vs new PPER after a state change. */
    public static void logRecentPperRecords(String context, String customerCode, String premisesCode) {
        List<Map<String, Object>> rows = ApplicationContext.get().getDbAction()
                .listRecentOcsepciForAccount(customerCode, premisesCode, 5);
        if (rows == null || rows.isEmpty()) {
            DualReportManager.logInfo(context + " — recent PPER/OCSEPCI records: (none visible)");
            DualReportManager.logDatabaseQuery(
                    DBQuery.SELECT_RECENT_OCSEPCI_FOR_ACCOUNT
                            + "\n-- phase=" + context
                            + "\n-- Bound values: UCRACCT/OCSEPCI cust='" + customerCode
                            + "', prem='" + premisesCode + "', maxRows=5",
                    "(no OCSEPCI/PPER rows)",
                    0L);
            return;
        }
        StringBuilder summary = new StringBuilder();
        int i = 1;
        for (Map<String, Object> row : rows) {
            if (summary.length() > 0) {
                summary.append(" || ");
            }
            summary.append("#").append(i++)
                    .append("{id=").append(stringVal(row, "tokenIdentifier"))
                    .append(", bill=").append(stringVal(row, "pendingBillType"))
                    .append(", corr=").append(stringVal(row, "pendingCorrType"))
                    .append(", email=").append(stringVal(row, "pendingEmail"))
                    .append(", exp=").append(stringVal(row, "tokenExpirationDate"))
                    .append(", completed=").append(stringVal(row, "tokenCompletedDate"))
                    .append(", activity=").append(stringVal(row, "activityDate"))
                    .append("}");
        }
        DualReportManager.logInfo(context + " — recent PPER/OCSEPCI records: " + summary);
        DualReportManager.logDatabaseQuery(
                DBQuery.SELECT_RECENT_OCSEPCI_FOR_ACCOUNT
                        + "\n-- phase=" + context
                        + "\n-- Bound values: cust='" + customerCode + "', prem='" + premisesCode + "', maxRows=5",
                summary.toString(),
                0L);
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
        if (value == null) {
            return null;
        }
        String text = value.toString().trim();
        return text.isEmpty() ? null : text;
    }
}
