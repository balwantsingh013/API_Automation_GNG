package com.gng.api.pages.csi.ReportPaymentPage;

import com.gng.api.context.ApplicationContext;
import com.gng.api.db.DBAction;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * ReportPayment TC_37–TC_48 only.
 * Temporarily widens NOTIFY_PAYMENT.DELINQUENCY_LOOKBACK_DAYS from the newest PREC date,
 * then restores the original value. Does not change other APIs or permanent config.
 */
@Slf4j
public final class ReportPaymentDelinquencyLookbackSupport {

    private static final int BUFFER_DAYS = 7;
    private static final int MIN_LOOKBACK_DAYS = 5;
    private static final int MAX_LOOKBACK_DAYS = 180;

    private static final AtomicBoolean APPLIED = new AtomicBoolean(false);
    private static volatile String originalValue;
    private static volatile int activeLookbackDays = MIN_LOOKBACK_DAYS;

    private ReportPaymentDelinquencyLookbackSupport() {
    }

    public static int getActiveLookbackDays() {
        return activeLookbackDays;
    }

    public static boolean isApplied() {
        return APPLIED.get();
    }

    /**
     * Expand lookback so existing UAT1 PREC rows qualify for TC_37–TC_48.
     * Safe to call repeatedly; applies once per JVM run until restored.
     */
    public static synchronized void ensureForTc37To48() {
        if (APPLIED.get()) {
            return;
        }
        DBAction db = ApplicationContext.get().getDbAction();
        originalValue = db.getNotifyPaymentDelinquencyLookbackDays();
        if (originalValue == null || originalValue.isBlank()) {
            throw new IllegalStateException(
                    "NOTIFY_PAYMENT.DELINQUENCY_LOOKBACK_DAYS is missing in UZRPSTO");
        }
        try {
            activeLookbackDays = Integer.parseInt(originalValue.trim());
        } catch (NumberFormatException ex) {
            activeLookbackDays = MIN_LOOKBACK_DAYS;
        }

        LocalDate newestPrec = db.getNewestActivePrecOccurrenceDate();
        if (newestPrec == null) {
            throw new IllegalStateException(
                    "No PREC credit-history rows found on active accounts for ReportPayment TC_37–TC_48");
        }

        long daysSincePrec = ChronoUnit.DAYS.between(newestPrec, LocalDate.now());
        if (daysSincePrec < 0) {
            daysSincePrec = 0;
        }
        int required = (int) daysSincePrec + BUFFER_DAYS;
        if (required < MIN_LOOKBACK_DAYS) {
            required = MIN_LOOKBACK_DAYS;
        }
        if (required > MAX_LOOKBACK_DAYS) {
            required = MAX_LOOKBACK_DAYS;
        }

        if (required > activeLookbackDays) {
            log.info("ReportPayment TC_37–TC_48: newest PREC={}, raising DELINQUENCY_LOOKBACK_DAYS {} -> {}",
                    newestPrec, activeLookbackDays, required);
            db.updateNotifyPaymentDelinquencyLookbackDays(String.valueOf(required));
            activeLookbackDays = required;
        } else {
            log.info("ReportPayment TC_37–TC_48: newest PREC={}, current lookback {} already covers it",
                    newestPrec, activeLookbackDays);
        }
        APPLIED.set(true);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                restoreIfChanged();
            } catch (Exception ignored) {
                // best-effort restore if the JVM exits before TC_48 After hook
            }
        }, "reportpayment-lookback-restore"));
    }

    /** Restore original UZRPSTO value after TC_37–TC_48. */
    public static synchronized void restoreIfChanged() {
        if (!APPLIED.get()) {
            return;
        }
        if (originalValue != null) {
            try {
                ApplicationContext.get().getDbAction()
                        .updateNotifyPaymentDelinquencyLookbackDays(originalValue);
                log.info("ReportPayment TC_37–TC_48: restored DELINQUENCY_LOOKBACK_DAYS to {}", originalValue);
            } catch (Exception ex) {
                log.error("Failed to restore DELINQUENCY_LOOKBACK_DAYS to {}", originalValue, ex);
            }
        }
        activeLookbackDays = MIN_LOOKBACK_DAYS;
        originalValue = null;
        APPLIED.set(false);
    }
}
