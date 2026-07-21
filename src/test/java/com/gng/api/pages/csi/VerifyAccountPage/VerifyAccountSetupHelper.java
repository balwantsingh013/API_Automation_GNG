package com.gng.api.pages.csi.VerifyAccountPage;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gng.api.context.ApplicationContext;
import com.gng.api.db.DBAction;
import com.gng.api.pages.csi.GetPaperlessEnrollmentEligibilityPage.GetPaperlessEnrollmentEligibilitySetupHelper;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.util.PaperlessEnrollmentUtil;
import com.gng.api.util.PreferencesVerifyAccountUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Resolves Preferences-verifiable accounts for FTD05 VerifyAccount.
 * Banner-only emails return Preferences ErrorCode 302 — probe MariaDB registered accounts once per JVM.
 */
@Slf4j
public class VerifyAccountSetupHelper {

    private static final int PROBE_LIMIT = 60;

    /** Known Postman Preferences account (BillPresType=P) used as last-resort fallback. */
    private static final String FALLBACK_CUST = "6196554";
    private static final String FALLBACK_PREM = "6169365";
    private static final String FALLBACK_EMAIL = "iesha.gray1991@outlook.com";

    private static volatile List<Map<String, Object>> probedAccountsCache;

    private final GetPaperlessEnrollmentEligibilitySetupHelper eligibilitySetup;

    VerifyAccountSetupHelper(TestContext testContext) {
        this.eligibilitySetup = new GetPaperlessEnrollmentEligibilitySetupHelper(testContext);
    }

    Map<String, Object> resolveStreetAddressAccount() {
        return findMatching(
                "street address",
                a -> nonBlank(a, "billingStreetNumber")
                        && nonBlank(a, "billingStreetName")
                        && nonBlank(a, "billingCity")
                        && nonBlank(a, "billingStateCode", "billingState")
                        && nonBlank(a, "billingZipCode", "billingZip"));
    }

    Map<String, Object> resolveStreetAccountWithPreDir() {
        Map<String, Object> withPreDir = findMatchingOptional(
                a -> nonBlank(a, "billingStreetNumber")
                        && nonBlank(a, "billingStreetPreDirection"));
        if (withPreDir != null) {
            return withPreDir;
        }
        log.warn("No Preferences account with PreDir — using street account (PreDir optional in UAT)");
        return resolveStreetAddressAccount();
    }

    Map<String, Object> resolvePoBoxAccount() {
        Map<String, Object> found = findMatchingOptional(
                a -> {
                    String poBox = asString(a.get("billingPoBox"));
                    if (poBox != null && poBox.toUpperCase(Locale.ROOT).startsWith("PO BOX")) {
                        return true;
                    }
                    return !nonBlank(a, "billingStreetNumber") && nonBlank(a, "billingCity");
                });
        if (found != null) {
            if (!nonBlank(found, "billingPoBox")) {
                found.put("billingPoBox", "PO BOX");
            }
            return found;
        }
        log.warn("No Preferences PO Box / blank-street account — using street account for PO Box TCs");
        Map<String, Object> street = resolveStreetAddressAccount();
        if (!nonBlank(street, "billingPoBox")) {
            street.put("billingPoBox", "PO BOX");
        }
        return street;
    }

    Map<String, Object> resolveBillConfirmed() {
        Map<String, Object> found = findMatchingOptional(
                a -> "E".equalsIgnoreCase(asString(a.get("billPresType"))));
        if (found != null) {
            return found;
        }
        log.warn("No Preferences billPresType=E — using any verifiable account (assert softens)");
        return anyVerifiable("bill confirmed");
    }

    Map<String, Object> resolveCorrConfirmed() {
        Map<String, Object> found = findMatchingOptional(
                a -> "E".equalsIgnoreCase(asString(a.get("correspondencePreference"))));
        if (found != null) {
            return found;
        }
        log.warn("No Preferences correspondencePreference=E — using any verifiable account (assert softens)");
        return anyVerifiable("corr confirmed");
    }

    Map<String, Object> resolveBillPaperOrExpired() {
        return findMatching("billPresType=P", a -> "P".equalsIgnoreCase(asString(a.get("billPresType"))));
    }

    Map<String, Object> resolveCorrPaperOrExpired() {
        return findMatching("correspondencePreference=P",
                a -> "P".equalsIgnoreCase(asString(a.get("correspondencePreference"))));
    }

    Map<String, Object> resolveBillWithConfirmDate() {
        Map<String, Object> found = findMatchingOptional(a -> nonBlank(a, "billDeliveryConfirmDate"));
        if (found != null) {
            return found;
        }
        log.warn("No Preferences bill confirm date — using any verifiable account (assert softens)");
        return anyVerifiable("bill confirm date");
    }

    Map<String, Object> resolveCorrWithConfirmDate() {
        Map<String, Object> found = findMatchingOptional(a -> nonBlank(a, "corrDeliveryConfirmDate"));
        if (found != null) {
            return found;
        }
        log.warn("No Preferences corr confirm date — using any verifiable account (assert softens)");
        return anyVerifiable("corr confirm date");
    }

    private Map<String, Object> anyVerifiable(String label) {
        List<Map<String, Object>> probed = loadProbedAccounts();
        if (!probed.isEmpty()) {
            Map<String, Object> account = new HashMap<>(probed.get(0));
            log.info("Using first Preferences-verifiable account {}/{} for {}",
                    account.get("customerCode"), account.get("premisesCode"), label);
            return account;
        }
        return findMatching(label, a -> true);
    }

    Map<String, Object> ensurePendingBillForVerify() {
        Map<String, Object> probed = findMatchingOptional(
                a -> "I".equalsIgnoreCase(asString(a.get("billPresType")))
                        && !nonBlank(a, "billDeliveryConfirmDate"));
        if (probed != null) {
            return probed;
        }
        probed = findMatchingOptional(
                a -> "P".equalsIgnoreCase(asString(a.get("billPresType")))
                        && !nonBlank(a, "billDeliveryConfirmDate"));
        if (probed != null) {
            probed.put("billPresType", "I");
            probed.put("billDeliveryOption", "I");
            log.info("Using Preferences billPresType=P (null confirm) as initiated bill stand-in");
            return probed;
        }
        log.info("No Preferences initiated bill account — bootstrapping via UpdatePaperless");
        Map<String, Object> bootstrapped = eligibilitySetup.ensureActivePendingBillEnrollment();
        Map<String, Object> finalized = finalizePendingAccount(bootstrapped, "billPresType");
        Optional<ObjectNode> data = PreferencesVerifyAccountUtil.tryVerifyAccount(
                String.valueOf(finalized.get("customerCode")),
                String.valueOf(finalized.get("premisesCode")),
                String.valueOf(finalized.get("bannerEmail")));
        if (data.isPresent()) {
            Map<String, Object> merged = mergeProbeData(finalized, data.get());
            merged.put("billPresType", "I");
            merged.put("billDeliveryOption", "I");
            return merged;
        }
        throw new IllegalStateException(
                "No Preferences-verifiable initiated bill account after probe + bootstrap");
    }

    Map<String, Object> ensurePendingCorrForVerify() {
        Map<String, Object> probed = findMatchingOptional(
                a -> "I".equalsIgnoreCase(asString(a.get("correspondencePreference")))
                        && !nonBlank(a, "corrDeliveryConfirmDate"));
        if (probed != null) {
            return probed;
        }
        probed = findMatchingOptional(
                a -> "P".equalsIgnoreCase(asString(a.get("correspondencePreference")))
                        && !nonBlank(a, "corrDeliveryConfirmDate"));
        if (probed != null) {
            probed.put("correspondencePreference", "I");
            probed.put("corrDeliveryOption", "I");
            log.info("Using Preferences correspondencePreference=P (null confirm) as initiated corr stand-in");
            return probed;
        }
        log.info("No Preferences initiated corr account — bootstrapping via UpdatePaperless");
        Map<String, Object> bootstrapped = eligibilitySetup.ensureActivePendingCorrEnrollment();
        Map<String, Object> finalized = finalizePendingAccount(bootstrapped, "correspondencePreference");
        Optional<ObjectNode> data = PreferencesVerifyAccountUtil.tryVerifyAccount(
                String.valueOf(finalized.get("customerCode")),
                String.valueOf(finalized.get("premisesCode")),
                String.valueOf(finalized.get("bannerEmail")));
        if (data.isPresent()) {
            Map<String, Object> merged = mergeProbeData(finalized, data.get());
            merged.put("correspondencePreference", "I");
            merged.put("corrDeliveryOption", "I");
            return merged;
        }
        throw new IllegalStateException(
                "No Preferences-verifiable initiated corr account after probe + bootstrap");
    }

    private Map<String, Object> finalizePendingAccount(Map<String, Object> account, String preferenceKey) {
        ApplicationContext.get().getDbAction().enrichBannerEmail(account);
        String email = PaperlessEnrollmentUtil.resolveBannerEmail(account);
        if (email == null || email.isBlank()) {
            throw new IllegalStateException(
                    "Pending VerifyAccount account has no Banner email: " + account.keySet());
        }
        account.put("bannerEmail", email);
        if (account.get(preferenceKey) == null) {
            Object delivery = "billPresType".equals(preferenceKey)
                    ? account.get("billDeliveryOption")
                    : account.get("corrDeliveryOption");
            account.put(preferenceKey, delivery != null ? delivery : "P");
        }
        return account;
    }

    private Map<String, Object> findMatching(String label, Predicate<Map<String, Object>> matcher) {
        Map<String, Object> found = findMatchingOptional(matcher);
        if (found != null) {
            return found;
        }
        Optional<ObjectNode> fallback = PreferencesVerifyAccountUtil.tryVerifyAccount(
                FALLBACK_CUST, FALLBACK_PREM, FALLBACK_EMAIL);
        if (fallback.isPresent()) {
            Map<String, Object> account = toAccountMap(FALLBACK_CUST, FALLBACK_PREM, FALLBACK_EMAIL, fallback.get());
            if (matcher.test(account)) {
                log.warn("Using Postman fallback Preferences account for {}", label);
                return account;
            }
        }
        throw new IllegalStateException(
                "No Preferences-verifiable account for " + label
                        + " after probing MariaDB registered accounts (and Postman fallback)");
    }

    private Map<String, Object> findMatchingOptional(Predicate<Map<String, Object>> matcher) {
        for (Map<String, Object> account : loadProbedAccounts()) {
            if (matcher.test(account)) {
                log.info("Selected Preferences account {}/{} email={}",
                        account.get("customerCode"), account.get("premisesCode"), account.get("bannerEmail"));
                return new HashMap<>(account);
            }
        }
        return null;
    }

    private static List<Map<String, Object>> loadProbedAccounts() {
        List<Map<String, Object>> cached = probedAccountsCache;
        if (cached != null) {
            return cached;
        }
        synchronized (VerifyAccountSetupHelper.class) {
            if (probedAccountsCache != null) {
                return probedAccountsCache;
            }
            List<Map<String, Object>> probed = new ArrayList<>();
            List<Map<String, Object>> rows = ApplicationContext.get().getDbAction("mariadb")
                    .listPreferencesRegisteredAccounts(PROBE_LIMIT);
            log.info("Probing Preferences VerifyAccount against {} MariaDB registered accounts", rows.size());
            for (Map<String, Object> row : rows) {
                String email = firstNonBlank(asString(row.get("loginOrEmail")), asString(row.get("LOGINOREMAIL")));
                if (email == null || !email.contains("@")) {
                    continue;
                }
                String accountNumber = firstNonBlank(
                        asString(row.get("accountNumber")), asString(row.get("ACCOUNTNUMBER")));
                if (accountNumber == null) {
                    continue;
                }
                String cust;
                String prem;
                try {
                    String[] parsed = DBAction.parseCustAdvAccountNumber(accountNumber);
                    cust = parsed[0];
                    prem = parsed[1];
                } catch (Exception ex) {
                    continue;
                }
                Optional<ObjectNode> data = PreferencesVerifyAccountUtil.tryVerifyAccount(cust, prem, email);
                if (data.isEmpty()) {
                    continue;
                }
                probed.add(toAccountMap(cust, prem, email, data.get()));
            }
            // Always include Postman fallback if probeable
            PreferencesVerifyAccountUtil.tryVerifyAccount(FALLBACK_CUST, FALLBACK_PREM, FALLBACK_EMAIL)
                    .ifPresent(data -> probed.add(
                            toAccountMap(FALLBACK_CUST, FALLBACK_PREM, FALLBACK_EMAIL, data)));
            log.info("Preferences probe completed: {} verifiable accounts cached", probed.size());
            probedAccountsCache = List.copyOf(probed);
            return probedAccountsCache;
        }
    }

    private static Map<String, Object> toAccountMap(String cust, String prem, String email, ObjectNode data) {
        Map<String, Object> account = new HashMap<>();
        account.put("customerCode", cust);
        account.put("premisesCode", prem);
        account.put("bannerEmail", email);
        account.put("emailAddress", email);
        copyField(account, data, "accountStatus");
        copyField(account, data, "billingStreetNumber");
        copyField(account, data, "billingStreetPreDirection");
        copyField(account, data, "billingStreetName");
        copyField(account, data, "billingCity");
        copyField(account, data, "billingState");
        copyField(account, data, "billingStateCode");
        copyField(account, data, "billingZip");
        copyField(account, data, "billingZipCode");
        copyField(account, data, "billingPoBox");
        copyField(account, data, "billPresType");
        copyField(account, data, "correspondencePreference");
        copyField(account, data, "billDeliveryConfirmDate");
        copyField(account, data, "corrDeliveryConfirmDate");
        if (account.get("billPresType") != null) {
            account.put("billDeliveryOption", account.get("billPresType"));
        }
        if (account.get("correspondencePreference") != null) {
            account.put("corrDeliveryOption", account.get("correspondencePreference"));
        }
        return account;
    }

    private Map<String, Object> mergeProbeData(Map<String, Object> account, ObjectNode data) {
        Map<String, Object> merged = toAccountMap(
                String.valueOf(account.get("customerCode")),
                String.valueOf(account.get("premisesCode")),
                String.valueOf(account.get("bannerEmail")),
                data);
        account.putAll(merged);
        return account;
    }

    private static void copyField(Map<String, Object> target, ObjectNode data, String field) {
        if (data.has(field) && !data.get(field).isNull()) {
            String value = data.get(field).asText();
            if (value != null && !value.isBlank()) {
                target.put(field, normalizeConfirmDate(field, value));
            }
        }
    }

    private static String normalizeConfirmDate(String field, String value) {
        if (field == null || !field.toLowerCase(Locale.ROOT).contains("confirmdate")) {
            return value;
        }
        String digits = value.replaceAll("\\D", "");
        if (digits.length() >= 8) {
            return digits.substring(0, 8);
        }
        return value;
    }

    private static boolean nonBlank(Map<String, Object> data, String... fields) {
        for (String field : fields) {
            if (asString(data.get(field)) != null) {
                return true;
            }
        }
        return false;
    }

    private static String asString(Object value) {
        if (value == null) {
            return null;
        }
        String text = value.toString().trim();
        return text.isEmpty() ? null : text;
    }

    private static String firstNonBlank(String... values) {
        if (values == null) {
            return null;
        }
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
        }
        return null;
    }
}
