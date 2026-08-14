package com.gng.api.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Parses GTBENRL_BILL_ADDR1 (~ delimited) per Preferences SP VerifyAccount mapping (FTD05 TC_228/229).
 */
public final class GtbenrlBillAddressParser {

    private GtbenrlBillAddressParser() {
    }

    /**
     * Element order in GTBENRL_BILL_ADDR1: 1 street#, 2 preDir, 3 name, 4 suffix, 5 postDir, 6 unitType, 7 unit#.
     */
    public static Map<String, String> parseBillAddr1(String billAddr1) {
        Map<String, String> out = new HashMap<>();
        String[] parts = splitTilde(billAddr1);
        put(out, "billingStreetNumber", element(parts, 1));
        put(out, "billingStreetPreDirection", element(parts, 2));
        put(out, "billingStreetName", element(parts, 3));
        put(out, "billingStreetSuffix", element(parts, 4));
        put(out, "billingStreetPostDirection", element(parts, 5));
        put(out, "billingUnitType", element(parts, 6));
        put(out, "billingUnitNumber", element(parts, 7));

        String poBox = derivePoBox(billAddr1, parts);
        if (poBox != null) {
            out.put("billingPoBox", poBox);
        }
        return out;
    }

    public static Map<String, String> fromGtbenrlRow(Map<String, Object> row) {
        Map<String, String> out = parseBillAddr1(asString(row.get("billAddr1"), row.get("GTBENRL_BILL_ADDR1")));
        put(out, "billingCity", asString(row.get("billingCity"), row.get("GTBENRL_BILL_CITY")));
        put(out, "billingState", asString(row.get("billingState"), row.get("GTBENRL_BILL_STATE")));
        put(out, "billingStateCode", asString(row.get("billingState"), row.get("GTBENRL_BILL_STATE")));
        String zip = asString(row.get("billingZip"), row.get("GTBENRL_BILL_ZIP"));
        if (zip != null) {
            String trimmed = zip.replaceAll("\\D", "");
            if (trimmed.length() >= 5) {
                trimmed = trimmed.substring(0, 5);
            }
            out.put("billingZip", trimmed);
            out.put("billingZipCode", trimmed);
        }
        return out;
    }

    private static String derivePoBox(String raw, String[] parts) {
        if (raw == null) {
            return null;
        }
        String upper = raw.toUpperCase(Locale.ROOT);
        if (!upper.contains("PO") || !upper.contains("BOX")) {
            return null;
        }
        // Common pending format: PO~~BOX~~~~<number>~
        for (int i = 0; i < parts.length; i++) {
            if ("BOX".equalsIgnoreCase(parts[i]) && i + 1 < parts.length) {
                for (int j = i + 1; j < parts.length; j++) {
                    if (parts[j] != null && !parts[j].isBlank()) {
                        return "PO BOX " + parts[j].trim();
                    }
                }
            }
        }
        String digits = raw.replaceAll("\\D", "");
        return digits.isBlank() ? "PO BOX" : "PO BOX " + digits;
    }

    private static String[] splitTilde(String value) {
        if (value == null) {
            return new String[0];
        }
        // Keep empty segments so element indexes match SP
        return value.split("~", -1);
    }

    private static String element(String[] parts, int oneBased) {
        int idx = oneBased - 1;
        if (idx < 0 || idx >= parts.length) {
            return null;
        }
        String v = parts[idx];
        return v == null || v.isBlank() ? null : v.trim();
    }

    private static void put(Map<String, String> target, String key, String value) {
        if (value != null && !value.isBlank()) {
            target.put(key, value.trim());
        }
    }

    private static String asString(Object... values) {
        if (values == null) {
            return null;
        }
        for (Object value : values) {
            if (value != null && !value.toString().isBlank()) {
                return value.toString().trim();
            }
        }
        return null;
    }
}
