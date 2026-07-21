package com.gng.api.util;

import com.gng.api.context.ApplicationContext;
import com.gng.api.db.DBAction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Map;

/**
 * Resolves ConfirmPaperlessEnrollment tokens from MariaDB custadv (primary) with OCSEPCI fallback.
 * After enroll, wait uses {@code email_verification_status_id > baseline} for the new row.
 */
@Slf4j
public final class PaperlessConfirmationTokenUtil {

    private static final int MIN_CUSTADV_TOKEN_LENGTH = 32;
    private static final int DEFAULT_MAX_ATTEMPTS = 15;
    private static final long DEFAULT_DELAY_MS = 2000L;

    private PaperlessConfirmationTokenUtil() {
    }

    public static long getBaselineVerificationId(String customerCode, String premisesCode) {
        return ApplicationContext.get().getDbAction("mariadb")
                .getMaxCustAdvPaperlessVerificationId(customerCode, premisesCode);
    }

    public static Map<String, Object> getLatestTokenData(String customerCode, String premisesCode) {
        try {
            return ApplicationContext.get().getDbAction("mariadb")
                    .getLatestConfirmPaperlessTokenFromCustAdv(customerCode, premisesCode);
        } catch (EmptyResultDataAccessException ex) {
            log.warn("No custadv paperless token for {}/{}; falling back to OCSEPCI",
                    customerCode, premisesCode);
            return ApplicationContext.get().getDbAction()
                    .getLatestValidConfirmationTokenFromOcsepci(customerCode, premisesCode);
        }
    }

    public static String tryGetLatestTokenIdentifierFromCustAdvQuiet(String customerCode, String premisesCode) {
        return ApplicationContext.get().getDbAction("mariadb")
                .tryGetLatestConfirmPaperlessTokenFromCustAdv(customerCode, premisesCode);
    }

    public static String tryGetLatestTokenIdentifier(String customerCode, String premisesCode) {
        String custAdvToken = tryGetLatestTokenIdentifierFromCustAdvQuiet(customerCode, premisesCode);
        if (custAdvToken != null) {
            return custAdvToken;
        }
        return ApplicationContext.get().getDbAction()
                .tryGetLatestOcsepciTokenIdentifierQuiet(customerCode, premisesCode);
    }

    /**
     * After enroll, wait for a new custadv row (id &gt; baseline) so stale unused rows are not reused.
     */
    public static String waitForTokenCreatedAfterEnroll(String customerCode,
                                                      String premisesCode,
                                                      long baselineVerificationId) {
        return waitForTokenCreatedAfterEnroll(
                customerCode, premisesCode, baselineVerificationId, DEFAULT_MAX_ATTEMPTS, DEFAULT_DELAY_MS);
    }

    public static String waitForTokenCreatedAfterEnroll(String customerCode,
                                                      String premisesCode,
                                                      long baselineVerificationId,
                                                      int maxAttempts,
                                                      long delayMs) {
        String accountNumber = DBAction.buildCustAdvAccountNumber(customerCode, premisesCode);
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            String token = ApplicationContext.get().getDbAction("mariadb")
                    .tryGetUnusedConfirmPaperlessTokenAfterId(customerCode, premisesCode, baselineVerificationId);
            if (token != null) {
                log.info("New custadv paperless token for {} after baseline {} on attempt {}/{}",
                        accountNumber, baselineVerificationId, attempt, maxAttempts);
                return token;
            }
            log.debug("No new custadv token yet for {} after baseline {} (attempt {}/{})",
                    accountNumber, baselineVerificationId, attempt, maxAttempts);
            if (attempt < maxAttempts) {
                sleepQuietly(delayMs);
            }
        }
        log.warn("No new custadv token after {} attempts for {} (baseline {}); trying latest valid unused row",
                maxAttempts, accountNumber, baselineVerificationId);
        return tryGetLatestTokenIdentifierFromCustAdvQuiet(customerCode, premisesCode);
    }

    public static String waitForLatestTokenIdentifier(String customerCode,
                                                      String premisesCode,
                                                      int maxAttempts,
                                                      long delayMs) {
        long baseline = getBaselineVerificationId(customerCode, premisesCode);
        return waitForTokenCreatedAfterEnroll(customerCode, premisesCode, baseline, maxAttempts, delayMs);
    }

    public static String resolveTokenIdentifier(Map<String, Object> tokenData) {
        return DBAction.resolveTokenIdentifierFromRow(tokenData);
    }

    /**
     * Plaintext token id sent to AES encryption (strip {@code confirm?} / URL query segment).
     */
    public static String normalizeTokenPlaintext(String token) {
        if (token == null || token.isBlank()) {
            return token;
        }
        String value = token.trim();
        if (value.startsWith("confirm?")) {
            return value.substring("confirm?".length());
        }
        int queryStart = value.lastIndexOf('?');
        if (queryStart >= 0 && queryStart < value.length() - 1) {
            return value.substring(queryStart + 1);
        }
        return value;
    }

    public static boolean isCustAdvTokenIdentifier(String token) {
        String plaintext = normalizeTokenPlaintext(token);
        return plaintext != null
                && plaintext.length() >= MIN_CUSTADV_TOKEN_LENGTH
                && plaintext.matches("(?i)[0-9a-f]+");
    }

    /** Prefers custadv email_link token over short OCSEPCI id. */
    public static String resolveConfirmToken(Map<String, Object> tokenData) {
        String direct = resolveTokenIdentifier(tokenData);
        if (isCustAdvTokenIdentifier(direct)) {
            return normalizeTokenPlaintext(direct);
        }
        String customerCode = readRowValue(tokenData, "customerCode");
        String premisesCode = readRowValue(tokenData, "premisesCode");
        if (customerCode != null && premisesCode != null) {
            String custAdvToken = ApplicationContext.get().getDbAction("mariadb")
                    .tryGetLatestConfirmPaperlessTokenFromCustAdv(customerCode, premisesCode);
            if (custAdvToken != null) {
                log.info("Resolved valid unused custadv confirm token for {}/{}", customerCode, premisesCode);
                return normalizeTokenPlaintext(custAdvToken);
            }
        }
        if (direct != null) {
            log.warn("Using non-custadv token identifier for confirm: {}", direct);
            return normalizeTokenPlaintext(direct);
        }
        throw new IllegalStateException("No confirm token in DB row keys: " + tokenData.keySet());
    }

    public static Map<String, Object> getUsedTokenData() {
        try {
            return ApplicationContext.get().getDbAction("mariadb").getCustAdvUsedConfirmationToken();
        } catch (RuntimeException custAdvEx) {
            log.warn("custadv used token query failed ({}); locating used OCSEPCI account",
                    custAdvEx.getMessage());
        }
        Map<String, Object> oracleRow = ApplicationContext.get().getDbAction().getUsedConfirmationToken();
        return resolveCustAdvTokenRowForOracleAccount(oracleRow);
    }

    public static Map<String, Object> getEmailMismatchTokenData() {
        Map<String, Object> oracleRow = ApplicationContext.get().getDbAction().getEmailMismatchConfirmationToken();
        return resolveCustAdvTokenRowForOracleAccount(oracleRow);
    }

    private static Map<String, Object> resolveCustAdvTokenRowForOracleAccount(Map<String, Object> oracleRow) {
        String customerCode = readRowValue(oracleRow, "customerCode");
        String premisesCode = readRowValue(oracleRow, "premisesCode");
        if (customerCode == null || premisesCode == null) {
            throw new IllegalStateException("Oracle token row missing customer/premises: " + oracleRow.keySet());
        }
        try {
            return ApplicationContext.get().getDbAction("mariadb")
                    .getLatestConfirmPaperlessTokenFromCustAdv(customerCode, premisesCode);
        } catch (EmptyResultDataAccessException ex) {
            return ApplicationContext.get().getDbAction("mariadb")
                    .getAnyLatestConfirmPaperlessTokenFromCustAdv(customerCode, premisesCode);
        }
    }

    private static void sleepQuietly(long delayMs) {
        try {
            Thread.sleep(delayMs);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    private static String readRowValue(Map<String, Object> row, String key) {
        if (row == null) {
            return null;
        }
        Object direct = row.get(key);
        if (direct != null && !direct.toString().isBlank()) {
            return direct.toString();
        }
        for (Map.Entry<String, Object> entry : row.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(key) && entry.getValue() != null) {
                return entry.getValue().toString();
            }
        }
        return null;
    }
}
