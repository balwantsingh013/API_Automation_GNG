package com.gng.api.util;



import java.util.Locale;

import java.util.Map;



/** Banner email helpers for paperless enrollment (normalize casing for custadv match). */

public final class PaperlessEnrollmentUtil {



    private PaperlessEnrollmentUtil() {

    }



    public static String normalizeBannerEmail(String email) {

        if (email == null || email.isBlank()) {

            return email;

        }

        return email.trim().toLowerCase(Locale.ROOT);

    }



    public static String resolveBannerEmail(Map<String, Object> accountData) {

        if (accountData == null) {

            return null;

        }

        for (String key : new String[]{"bannerEmail", "BANNEREMAIL", "emailAddress", "EMAILADDRESS"}) {

            Object value = accountData.get(key);

            if (value != null && !value.toString().isBlank()) {

                return normalizeBannerEmail(value.toString());

            }

        }

        for (Map.Entry<String, Object> entry : accountData.entrySet()) {

            for (String key : new String[]{"bannerEmail", "BANNEREMAIL", "emailAddress", "EMAILADDRESS"}) {

                if (entry.getKey().equalsIgnoreCase(key) && entry.getValue() != null

                        && !entry.getValue().toString().isBlank()) {

                    return normalizeBannerEmail(entry.getValue().toString());

                }

            }

        }

        return null;

    }

}


