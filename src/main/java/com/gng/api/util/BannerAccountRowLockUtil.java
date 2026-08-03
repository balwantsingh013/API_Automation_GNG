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
 * Holds a transactional {@code SELECT ... FOR UPDATE} lock on Banner {@code GZBEMCP} email row(s)
 * so ConfirmPaperlessEnrollment preference / confirmation-date update fails (TC_131/132 → 40293).
 * Always pair {@link #lockAccountRow(String, String)} with {@link #release()} in {@code finally}.
 */
@Slf4j
public final class BannerAccountRowLockUtil {

    private static final ThreadLocal<Connection> LOCKED_CONNECTION = new ThreadLocal<>();
    private static final ThreadLocal<String> LOCKED_ACCOUNT = new ThreadLocal<>();

    private BannerAccountRowLockUtil() {
    }

    /**
     * Locks GZBEMCP email row(s) for {@code customerCode}.
     * {@code premisesCode} is kept for account labeling / logging only.
     */
    public static void lockAccountRow(String customerCode, String premisesCode) {
        if (customerCode == null || customerCode.isBlank()
                || premisesCode == null || premisesCode.isBlank()) {
            throw new IllegalStateException(
                    "Cannot lock GZBEMCP rows: customerCode/premisesCode blank (token setup must set them)");
        }
        if (LOCKED_CONNECTION.get() != null) {
            throw new IllegalStateException(
                    "Banner account row lock already held on this thread; release before locking again");
        }

        String accountLabel = customerCode.trim() + "/" + premisesCode.trim();
        DualReportManager.logInfo(
                "Banner GZBEMCP — ROW LOCK ACQUIRE (force Paperless Preference Update Failed / 40293) for "
                        + accountLabel);

        String lockSql = DBQuery.LOCK_UCRACCT_ROW_FOR_UPDATE
                + "\n-- bind: GZBEMCP_CUST_CODE = '" + customerCode.trim() + "'";

        EnvConfig envConfig = ApplicationContext.get().getEnvConfig();
        DriverManagerDataSource dataSource = DBConnection.dbConnection().getDataSource(envConfig, "oracle");
        Connection connection = null;
        long start = System.currentTimeMillis();
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            int lockedRows;
            try (PreparedStatement ps = connection.prepareStatement(DBQuery.LOCK_UCRACCT_ROW_FOR_UPDATE)) {
                ps.setString(1, customerCode.trim());
                try (ResultSet rs = ps.executeQuery()) {
                    lockedRows = 0;
                    while (rs.next()) {
                        lockedRows++;
                    }
                }
            }
            long elapsed = System.currentTimeMillis() - start;
            if (lockedRows == 0) {
                connection.rollback();
                connection.close();
                DualReportManager.logDatabaseQuery(
                        lockSql,
                        null,
                        elapsed,
                        false,
                        "No GZBEMCP row to lock for " + accountLabel);
                throw new IllegalStateException(
                        "No GZBEMCP row to lock for " + accountLabel);
            }
            LOCKED_CONNECTION.set(connection);
            LOCKED_ACCOUNT.set(accountLabel);
            DualReportManager.logDatabaseQuery(
                    lockSql,
                    "Locked " + lockedRows + " GZBEMCP row(s) FOR UPDATE for " + accountLabel
                            + ". Hold until Confirm returns, then RELEASE (rollback).",
                    elapsed);
            log.info("Locked GZBEMCP row(s) FOR UPDATE for {}", accountLabel);
        } catch (SQLException ex) {
            DualReportManager.logDatabaseQuery(
                    lockSql,
                    null,
                    System.currentTimeMillis() - start,
                    false,
                    ex.getMessage());
            closeQuietly(connection);
            throw new IllegalStateException(
                    "Failed to lock GZBEMCP for " + accountLabel + ": " + ex.getMessage(),
                    ex);
        }
    }

    /** Releases the FOR UPDATE lock (rollback + close). Safe if lock was never taken. */
    public static void release() {
        Connection connection = LOCKED_CONNECTION.get();
        if (connection == null) {
            return;
        }
        String accountLabel = LOCKED_ACCOUNT.get();
        LOCKED_CONNECTION.remove();
        LOCKED_ACCOUNT.remove();

        DualReportManager.logInfo(
                "Banner GZBEMCP — ROW LOCK RELEASE"
                        + (accountLabel != null ? " for " + accountLabel : ""));

        long start = System.currentTimeMillis();
        String releaseSql = "ROLLBACK -- release GZBEMCP FOR UPDATE lock"
                + (accountLabel != null ? " for " + accountLabel : "");
        try {
            if (!connection.isClosed()) {
                connection.rollback();
                DualReportManager.logDatabaseQuery(
                        releaseSql,
                        "Released GZBEMCP row lock"
                                + (accountLabel != null ? " for " + accountLabel : "")
                                + " (transaction rolled back; connection closed).",
                        System.currentTimeMillis() - start);
                log.info("Released Banner GZBEMCP row lock{}",
                        accountLabel != null ? " for " + accountLabel : "");
            }
        } catch (SQLException ex) {
            DualReportManager.logDatabaseQuery(
                    releaseSql,
                    null,
                    System.currentTimeMillis() - start,
                    false,
                    ex.getMessage());
            log.error("Failed to rollback Banner GZBEMCP row lock connection", ex);
            throw new IllegalStateException("Failed to release Banner GZBEMCP row lock: " + ex.getMessage(), ex);
        } finally {
            closeQuietly(connection);
        }
    }

    private static void closeQuietly(Connection connection) {
        if (connection == null) {
            return;
        }
        try {
            connection.close();
        } catch (SQLException ignored) {
            // best-effort
        }
    }
}
