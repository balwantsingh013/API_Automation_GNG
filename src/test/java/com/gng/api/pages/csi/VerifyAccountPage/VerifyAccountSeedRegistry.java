package com.gng.api.pages.csi.VerifyAccountPage;

import com.gng.api.report.DualReportManager;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Deferred undo log for the DB seeds FTD05 TC_236/237 need.
 * <p>
 * Reaching Preferences {@code billPresType/correspondencePreference = I} requires mutating the
 * account's GZRPPTH row, and the seeded state has to survive until the scenario's assertions have
 * run — so the revert cannot be inline. Setup records the restore here; a {@code @VerifyAccount}
 * -scoped {@code @After} hook drains it once the scenario ends.
 * <p>
 * Without this the seeds were permanent: every run demoted more accounts out of the confirmed
 * (TC_230-235) and paper (TC_238/239) pools that the neighbouring TCs select from.
 */
@Slf4j
public final class VerifyAccountSeedRegistry {

    private static final ThreadLocal<List<PendingRestore>> PENDING =
            ThreadLocal.withInitial(ArrayList::new);

    private VerifyAccountSeedRegistry() {
    }

    private record PendingRestore(String label, Runnable action) {
    }

    /** Queues a revert for the current scenario. */
    public static void record(String label, Runnable restore) {
        if (restore == null) {
            return;
        }
        PENDING.get().add(new PendingRestore(label, restore));
        DualReportManager.logInfo("VerifyAccount seed recorded for restore — " + label);
    }

    /**
     * Runs every queued revert in reverse order, then clears the log.
     * A failing revert is logged and the remainder still run — teardown must never mask
     * the scenario's own result.
     */
    public static void restoreAll() {
        List<PendingRestore> pending = PENDING.get();
        if (pending.isEmpty()) {
            PENDING.remove();
            return;
        }
        for (int i = pending.size() - 1; i >= 0; i--) {
            PendingRestore restore = pending.get(i);
            try {
                restore.action().run();
                DualReportManager.logInfo("VerifyAccount seed restored — " + restore.label());
            } catch (Exception ex) {
                log.warn("VerifyAccount seed restore failed for {}: {}",
                        restore.label(), ex.getMessage());
                DualReportManager.logInfo(
                        "VerifyAccount seed restore FAILED — " + restore.label()
                                + " : " + ex.getMessage());
            }
        }
        pending.clear();
        PENDING.remove();
    }

    /** Drops queued reverts without running them. */
    public static void discard() {
        PENDING.get().clear();
        PENDING.remove();
    }
}
