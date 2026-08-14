package com.gng.api.util;

import com.gng.api.context.ApplicationContext;
import com.gng.api.db.DBConnection;
import com.gng.api.db.DBQuery;
import com.gng.api.pojo.envConfig.EnvConfig;
import com.gng.api.report.DualReportManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Temporarily sets Banner {@code uzrpsto.NEW_TEST_EMAIL_ADDR} so paperless email behavior can be forced:
 * <ul>
 *   <li>{@link #clearForEmailNotificationFailure()} — space sentinel → email <b>creation</b> fails
 *       (Update TC_77/78 → 40281; Confirm TC_133 → 40321 on UAT)</li>
 *   <li>{@link #redirectToUndeliverableForSoftDeliveryFailure(String)} — undeliverable address →
 *       Confirm TC_150 soft delivery failure that must <b>not</b> block success</li>
 * </ul>
 * Always pair override methods with {@link #restore()} in {@code finally}.
 */
@Slf4j
public final class BannerTestEmailOverrideUtil {

    /** UAT1 default when the stored override cannot be read. */
    public static final String UAT1_DEFAULT_TEST_EMAIL = "uattestemail@test.com";

    /** Space sentinel (column is NOT NULL; Oracle treats '' as NULL). */
    public static final String CLEARED_VALUE = " ";

    /** Default undeliverable routing address for Confirm TC_150 soft delivery failure. */
    public static final String DEFAULT_UNDELIVERABLE_ADDRESS = "confirm-email-fail@invalid.test";

    private static final ThreadLocal<String> ORIGINAL_VALUE = new ThreadLocal<>();
    private static final ThreadLocal<Integer> DEPTH = ThreadLocal.withInitial(() -> 0);

    private BannerTestEmailOverrideUtil() {
    }

    /** Sets override to a space (nested calls clear once; restore when depth returns to 0). */
    public static void clearForEmailNotificationFailure() {
        applyOverride(CLEARED_VALUE,
                "Banner NEW_TEST_EMAIL_ADDR — OVERRIDE CLEAR (force notification/email failure: Update 40281 / Confirm 40321)",
                true);
    }

    /**
     * TC_150: route confirmation email to an undeliverable address so delivery fails after
     * preference update, without blanking the override (blanking triggers TC_133 / 40321 rollback).
     */
    public static void redirectToUndeliverableForSoftDeliveryFailure(String undeliverableAddress) {
        String address = (undeliverableAddress == null || undeliverableAddress.isBlank())
                ? DEFAULT_UNDELIVERABLE_ADDRESS
                : undeliverableAddress.trim();
        if (!address.contains("@")) {
            throw new IllegalArgumentException(
                    "Undeliverable address must look like an email, got: '" + address + "'");
        }
        applyOverride(address,
                "Banner NEW_TEST_EMAIL_ADDR — OVERRIDE REDIRECT to undeliverable '"
                        + address
                        + "' (Confirm TC_150: soft confirmation-email delivery failure must not block success)",
                false);
    }

    private static void applyOverride(String overrideValue, String reportLabel, boolean requireBlankAfterClear) {
        int depth = DEPTH.get();
        if (depth == 0) {
            DualReportManager.logInfo(reportLabel);

            long readStart = System.currentTimeMillis();
            String current = readParmValueRaw();
            DualReportManager.logDatabaseQuery(
                    DBQuery.SELECT_BANNER_NEW_TEST_EMAIL_ADDR,
                    "NEW_TEST_EMAIL_ADDR before override = '" + displayParm(current) + "'",
                    System.currentTimeMillis() - readStart);

            String toRestore = (current == null || current.isBlank())
                    ? UAT1_DEFAULT_TEST_EMAIL
                    : current.trim();
            ORIGINAL_VALUE.set(toRestore);

            long writeStart = System.currentTimeMillis();
            try {
                writeParmValue(overrideValue);
                String after = readParmValueRaw();
                DualReportManager.logDatabaseQuery(
                        DBQuery.RESTORE_BANNER_NEW_TEST_EMAIL_ADDR
                                + "\n-- bind: uzrpsto_parm_value = '" + displayParm(overrideValue) + "'",
                        "NEW_TEST_EMAIL_ADDR after override = '" + displayParm(after)
                                + "'. Will restore to '" + toRestore + "'",
                        System.currentTimeMillis() - writeStart);
                log.info("Set Banner NEW_TEST_EMAIL_ADDR to '{}' (will restore to '{}')",
                        displayParm(after), toRestore);
                if (requireBlankAfterClear && after != null && after.contains("@")) {
                    throw new IllegalStateException(
                            "NEW_TEST_EMAIL_ADDR still looks like an email after clear: '" + after
                                    + "'. Banner would not see a blank override.");
                }
            } catch (RuntimeException ex) {
                DualReportManager.logDatabaseQuery(
                        DBQuery.RESTORE_BANNER_NEW_TEST_EMAIL_ADDR,
                        null,
                        System.currentTimeMillis() - writeStart,
                        false,
                        ex.getMessage());
                throw ex;
            }
        }
        DEPTH.set(depth + 1);
    }

    /** Restores the saved override. Safe if clear was never invoked. */
    public static void restore() {
        Integer depth = DEPTH.get();
        if (depth == null || depth <= 0) {
            return;
        }
        int next = depth - 1;
        DEPTH.set(next);
        if (next > 0) {
            return;
        }
        String original = ORIGINAL_VALUE.get();
        ORIGINAL_VALUE.remove();
        DEPTH.remove();
        if (original == null || original.isBlank()) {
            original = UAT1_DEFAULT_TEST_EMAIL;
        }

        DualReportManager.logInfo(
                "Banner NEW_TEST_EMAIL_ADDR — OVERRIDE RESTORE to '" + original + "'");

        long writeStart = System.currentTimeMillis();
        String restoreSql = DBQuery.RESTORE_BANNER_NEW_TEST_EMAIL_ADDR
                + "\n-- bind: uzrpsto_parm_value = '" + original + "'";
        try {
            writeParmValue(original);
            String after = readParmValueRaw();
            DualReportManager.logDatabaseQuery(
                    restoreSql,
                    "NEW_TEST_EMAIL_ADDR restored. Current value = '" + displayParm(after) + "'",
                    System.currentTimeMillis() - writeStart);
            log.info("Restored Banner NEW_TEST_EMAIL_ADDR to '{}'", original);
        } catch (RuntimeException ex) {
            DualReportManager.logDatabaseQuery(
                    restoreSql,
                    null,
                    System.currentTimeMillis() - writeStart,
                    false,
                    ex.getMessage());
            log.error("Failed to restore Banner NEW_TEST_EMAIL_ADDR to '{}'. "
                            + "Manual fix: UPDATE uzrpsto SET uzrpsto_parm_value = '{}' "
                            + "WHERE uzrpsto_object IN ( 'WHTCNTS_TEST_WEB_SERV_ACCESS' , 'UZPSEND')"
                            + "AND uzrpsto_parm_name IN ( 'NEW_TEST_EMAIL_ADDR','TEST_EMAIL_ADDR')",
                    original, original, ex);
            throw ex;
        }
    }

    /**
     * Current {@code NEW_TEST_EMAIL_ADDR} value (for TC_150 failed-send evidence before restore).
     */
    public static String readCurrentOverrideValue() {
        return readParmValueRaw();
    }

    private static String displayParm(String value) {
        if (value == null) {
            return "null";
        }
        if (value.isBlank()) {
            return "(blank/space, length=" + value.length() + ")";
        }
        return value;
    }

    private static String readParmValueRaw() {
        EnvConfig envConfig = ApplicationContext.get().getEnvConfig();
        DriverManagerDataSource dataSource = DBConnection.dbConnection().getDataSource(envConfig, "oracle");
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(DBQuery.SELECT_BANNER_NEW_TEST_EMAIL_ADDR);
             ResultSet rs = ps.executeQuery()) {
            if (!rs.next()) {
                return null;
            }
            return rs.getString(1);
        } catch (SQLException ex) {
            throw new IllegalStateException("Failed to read NEW_TEST_EMAIL_ADDR: " + ex.getMessage(), ex);
        }
    }

    private static void writeParmValue(String value) {
        EnvConfig envConfig = ApplicationContext.get().getEnvConfig();
        DriverManagerDataSource dataSource = DBConnection.dbConnection().getDataSource(envConfig, "oracle");
        try (Connection connection = dataSource.getConnection()) {
            boolean previousAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);
            try (PreparedStatement ps = connection.prepareStatement(DBQuery.RESTORE_BANNER_NEW_TEST_EMAIL_ADDR)) {
                ps.setString(1, value);
                int updated = ps.executeUpdate();
                connection.commit();
                if (updated != 2) {
                    throw new IllegalStateException(
                            "Expected 1 row updated for NEW_TEST_EMAIL_ADDR, got " + updated);
                }
            } catch (SQLException ex) {
                try {
                    connection.rollback();
                } catch (SQLException ignored) {
                    // best-effort
                }
                throw ex;
            } finally {
                try {
                    connection.setAutoCommit(previousAutoCommit);
                } catch (SQLException ignored) {
                    // best-effort
                }
            }
        } catch (SQLException ex) {
            throw new IllegalStateException(
                    "Failed to update NEW_TEST_EMAIL_ADDR to '" + value + "': " + ex.getMessage(), ex);
        }
    }
}
