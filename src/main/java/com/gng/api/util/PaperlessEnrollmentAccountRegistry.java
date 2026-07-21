package com.gng.api.util;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Reserves customer/premises pairs across paperless enrollment scenarios in the same
 * suite run so parallel or sequential tests do not reuse the same Banner account.
 */
public final class PaperlessEnrollmentAccountRegistry {

    private static final Set<String> RESERVED = ConcurrentHashMap.newKeySet();

    private PaperlessEnrollmentAccountRegistry() {
    }

    public static boolean tryReserve(Map<String, Object> account) {
        if (account == null) {
            return false;
        }
        return RESERVED.add(toKey(account));
    }

    public static boolean isReserved(Map<String, Object> account) {
        return account != null && RESERVED.contains(toKey(account));
    }

    public static String toKey(Map<String, Object> account) {
        return readAccountField(account, "customerCode") + "|" + readAccountField(account, "premisesCode");
    }

    private static String readAccountField(Map<String, Object> account, String key) {
        Object direct = account.get(key);
        if (direct != null) {
            return direct.toString();
        }
        for (Map.Entry<String, Object> entry : account.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(key) && entry.getValue() != null) {
                return entry.getValue().toString();
            }
        }
        return "";
    }
}
