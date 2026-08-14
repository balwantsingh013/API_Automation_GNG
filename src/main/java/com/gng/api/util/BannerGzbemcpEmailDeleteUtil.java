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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * TC_131/132: snapshot + delete {@code GZBEMCP} rows for a customer before Confirm so Banner
 * preference update fails quickly (expect 40293), then restore in {@code finally}.
 * Do not use {@link BannerAccountRowLockUtil} for this — a held lock always times out (HTTP 504).
 */
@Slf4j
public final class BannerGzbemcpEmailDeleteUtil {

    private static final ThreadLocal<List<Map<String, Object>>> SAVED_ROWS = new ThreadLocal<>();
    private static final ThreadLocal<String> SAVED_CUSTOMER = new ThreadLocal<>();

    private BannerGzbemcpEmailDeleteUtil() {
    }

    /**
     * Snapshot then delete all GZBEMCP rows for {@code customerCode}.
     * Always pair with {@link #restore()} in {@code finally}.
     */
    public static void deleteRowsForCustomer(String customerCode) {
        if (customerCode == null || customerCode.isBlank()) {
            throw new IllegalStateException(
                    "Cannot delete GZBEMCP rows: customerCode is blank (token setup must set it)");
        }
        if (SAVED_ROWS.get() != null) {
            throw new IllegalStateException(
                    "GZBEMCP delete snapshot already held on this thread; restore before deleting again");
        }

        String cust = customerCode.trim();
        DualReportManager.logInfo(
                "Banner GZBEMCP — DELETE rows for customer " + cust
                        + " (force Paperless Preference Update Failed / 40293; no row lock)");

        EnvConfig envConfig = ApplicationContext.get().getEnvConfig();
        DriverManagerDataSource dataSource = DBConnection.dbConnection().getDataSource(envConfig, "oracle");

        long start = System.currentTimeMillis();
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            List<Map<String, Object>> rows = readRows(connection, cust);
            DualReportManager.logDatabaseQuery(
                    DBQuery.SELECT_GZBEMCP_ROWS_FOR_CUSTOMER
                            + "\n-- bind: GZBEMCP_CUST_CODE = '" + cust + "'",
                    "Snapshot " + rows.size() + " GZBEMCP row(s) for customer " + cust + " before delete",
                    System.currentTimeMillis() - start);

            if (rows.isEmpty()) {
                connection.rollback();
                throw new IllegalStateException(
                        "No GZBEMCP rows to delete for customer " + cust
                                + " (need email rows present after enroll, then delete before Confirm)");
            }

            long deleteStart = System.currentTimeMillis();
            int deleted;
            try (PreparedStatement ps = connection.prepareStatement(DBQuery.DELETE_GZBEMCP_ROWS_FOR_CUSTOMER)) {
                ps.setString(1, cust);
                deleted = ps.executeUpdate();
            }
            connection.commit();

            SAVED_ROWS.set(rows);
            SAVED_CUSTOMER.set(cust);
            DualReportManager.logDatabaseQuery(
                    DBQuery.DELETE_GZBEMCP_ROWS_FOR_CUSTOMER
                            + "\n-- bind: GZBEMCP_CUST_CODE = '" + cust + "'",
                    "Deleted " + deleted + " GZBEMCP row(s) for customer " + cust
                            + ". Will restore snapshot after Confirm.",
                    System.currentTimeMillis() - deleteStart);
            log.info("Deleted {} GZBEMCP row(s) for customer {} (snapshot size={})",
                    deleted, cust, rows.size());
        } catch (SQLException ex) {
            DualReportManager.logDatabaseQuery(
                    DBQuery.DELETE_GZBEMCP_ROWS_FOR_CUSTOMER,
                    null,
                    System.currentTimeMillis() - start,
                    false,
                    ex.getMessage());
            throw new IllegalStateException(
                    "Failed to delete GZBEMCP for customer " + cust + ": " + ex.getMessage(), ex);
        }
    }

    /** Restores snapshot rows. Safe if delete was never invoked. */
    public static void restore() {
        List<Map<String, Object>> rows = SAVED_ROWS.get();
        String cust = SAVED_CUSTOMER.get();
        SAVED_ROWS.remove();
        SAVED_CUSTOMER.remove();
        if (rows == null || rows.isEmpty()) {
            return;
        }

        DualReportManager.logInfo(
                "Banner GZBEMCP — RESTORE rows for customer " + cust + " (" + rows.size() + " row(s))");

        EnvConfig envConfig = ApplicationContext.get().getEnvConfig();
        DriverManagerDataSource dataSource = DBConnection.dbConnection().getDataSource(envConfig, "oracle");
        long start = System.currentTimeMillis();
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            // Clear any rows that may have been re-created during Confirm, then re-insert snapshot.
            try (PreparedStatement del = connection.prepareStatement(DBQuery.DELETE_GZBEMCP_ROWS_FOR_CUSTOMER)) {
                del.setString(1, cust);
                del.executeUpdate();
            }
            insertRows(connection, rows);
            connection.commit();
            DualReportManager.logDatabaseQuery(
                    "INSERT INTO GZBEMCP ... (restore snapshot)",
                    "Restored " + rows.size() + " GZBEMCP row(s) for customer " + cust,
                    System.currentTimeMillis() - start);
            log.info("Restored {} GZBEMCP row(s) for customer {}", rows.size(), cust);
        } catch (SQLException ex) {
            DualReportManager.logDatabaseQuery(
                    "INSERT INTO GZBEMCP ... (restore snapshot)",
                    null,
                    System.currentTimeMillis() - start,
                    false,
                    ex.getMessage());
            throw new IllegalStateException(
                    "Failed to restore GZBEMCP for customer " + cust + ": " + ex.getMessage(), ex);
        }
    }

    private static List<Map<String, Object>> readRows(Connection connection, String customerCode)
            throws SQLException {
        List<Map<String, Object>> rows = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(DBQuery.SELECT_GZBEMCP_ROWS_FOR_CUSTOMER)) {
            ps.setString(1, customerCode);
            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                int columnCount = meta.getColumnCount();
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.put(meta.getColumnName(i), rs.getObject(i));
                    }
                    rows.add(row);
                }
            }
        }
        return rows;
    }

    private static void insertRows(Connection connection, List<Map<String, Object>> rows)
            throws SQLException {
        if (rows.isEmpty()) {
            return;
        }
        List<String> columns = new ArrayList<>(rows.getFirst().keySet());
        StringJoiner colJoiner = new StringJoiner(", ");
        StringJoiner phJoiner = new StringJoiner(", ");
        for (String column : columns) {
            colJoiner.add(column);
            phJoiner.add("?");
        }
        String insertSql = "INSERT INTO GZBEMCP (" + colJoiner + ") VALUES (" + phJoiner + ")";
        try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
            for (Map<String, Object> row : rows) {
                int index = 1;
                for (String column : columns) {
                    Object value = row.get(column);
                    if (value == null) {
                        ps.setNull(index, Types.NULL);
                    } else {
                        ps.setObject(index, value);
                    }
                    index++;
                }
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
}
