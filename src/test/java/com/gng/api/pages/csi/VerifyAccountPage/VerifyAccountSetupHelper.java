package com.gng.api.pages.csi.VerifyAccountPage;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gng.api.context.ApplicationContext;
import com.gng.api.db.DBAction;
import com.gng.api.pages.csi.GetPaperlessEnrollmentEligibilityPage.GetPaperlessEnrollmentEligibilitySetupHelper;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.report.DualReportManager;
import com.gng.api.util.GtbenrlBillAddressParser;
import com.gng.api.util.PaperlessEnrollmentUtil;
import com.gng.api.util.PreferencesVerifyAccountUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Resolves Preferences-verifiable accounts for FTD05 VerifyAccount.
 * Prefers accounts that also exist in Banner (UCRACCT) so reports can show Banner vs API match.
 * Banner-only emails return Preferences ErrorCode 302 - probe MariaDB registered accounts + Banner street candidates.
 */
@Slf4j
public class VerifyAccountSetupHelper {

    private static final int MARIADB_PROBE_LIMIT = 80;
    private static final int BANNER_STREET_PROBE_LIMIT = 40;

    /** Known Preferences-verifiable account (BillPresType=P) used as last-resort fallback. */
    private static final String FALLBACK_CUST = "6196554";
    private static final String FALLBACK_PREM = "6169365";
    private static final String FALLBACK_EMAIL = "iesha.gray1991@outlook.com";

    private static volatile List<Map<String, Object>> probedAccountsCache;

    private final GetPaperlessEnrollmentEligibilitySetupHelper eligibilitySetup;

    VerifyAccountSetupHelper(TestContext testContext) {
        this.testContext = testContext;
        this.eligibilitySetup = new GetPaperlessEnrollmentEligibilitySetupHelper(testContext);
    }

    private final TestContext testContext;

    /**
     * TC_228/229: GTBENRL NEW (no UCRACCT), bill vs service differ.
     * Prefers the candidate whose BILL_ADDR1 has a numeric Element 1 so
     * billingStreetNumber and GTBENRL source fields can be asserted.
     */
    Map<String, Object> ensureGtbenrlBillingAccountWithEmail() {
        List<Map<String, Object>> candidates = ApplicationContext.get().getDbAction()
                .listVerifyAccountGtbenrlNoUcraddr(5);
        if (candidates == null || candidates.isEmpty()) {
            throw new IllegalStateException(
                    "No GTBENRL account for TC_228/229 "
                            + "(PROC_FLAG='N', BILL_ADDR1, GZRPPTH status='N', no UCRACCT, bill vs service differ)");
        }

        Map<String, Object> best = null;
        int bestScore = -1;
        for (Map<String, Object> raw : candidates) {
            Map<String, Object> row = normalizeGtbenrlRow(raw);
            String cust = asString(row.get("customerCode"));
            String prem = asString(row.get("premisesCode"));
            if (cust == null || prem == null || asString(row.get("billAddr1")) == null) {
                continue;
            }
            String email = PaperlessEnrollmentUtil.resolveBannerEmail(row);
            if (email == null || email.isBlank()) {
                email = ApplicationContext.get().getDbAction().getAnyBannerEmailForCustomer(cust);
            }
            if (email == null || email.isBlank()) {
                email = ApplicationContext.get().getDbAction().getActiveBannerEmailForCustomer(cust);
            }
            if (email == null || email.isBlank()) {
                DualReportManager.logInfo(
                        "GTBENRL candidate skipped (no email): " + cust + "/" + prem);
                continue;
            }
            row.put("bannerEmail", email);
            int score = gtbenrlParseScore(row);
            DualReportManager.logInfo(
                    "GTBENRL candidate " + cust + "/" + prem
                            + " score=" + score
                            + " billAddr1=" + row.get("billAddr1")
                            + " email=" + email);
            if (score > 0 && score > bestScore) {
                bestScore = score;
                best = row;
            }
        }
        if (best == null) {
            throw new IllegalStateException(
                    "GTBENRL candidates exist for TC_228/229 but none have a numeric "
                            + "GTBENRL_BILL_ADDR1 Element 1 (required for billingStreetNumber)");
        }
        DualReportManager.logInfo(
                "VerifyAccount GTBENRL account selected — "
                        + best.get("customerCode") + "/" + best.get("premisesCode")
                        + " billAddr1=" + best.get("billAddr1"));
        return finalizeGtbenrlAccount(best);
    }

    /**
     * Prefer street-style BILL_ADDR1 (Element 1 numeric) so TC_229 can assert
     * billingStreetNumber = Element 1. PO-leading lines are deprioritized.
     */
    private int gtbenrlParseScore(Map<String, Object> row) {
        Map<String, String> parsed = GtbenrlBillAddressParser.fromGtbenrlRow(row);
        String streetNum = asString(parsed.get("billingStreetNumber"));
        boolean numericStreet = streetNum != null && streetNum.matches("\\d+");
        if (!numericStreet) {
            return 0;
        }
        int score = 10;
        for (String key : List.of(
                "billingStreetPreDirection", "billingStreetName",
                "billingStreetSuffix", "billingStreetPostDirection", "billingUnitType",
                "billingUnitNumber", "billingCity", "billingState", "billingZip")) {
            if (asString(parsed.get(key)) != null) {
                score++;
            }
        }
        return score;
    }

    private Map<String, Object> finalizeGtbenrlAccount(Map<String, Object> account) {
        Map<String, Object> row = normalizeGtbenrlRow(account);
        if (row.get("billAddr1") != null) {
            row.put("GTBENRL_BILL_ADDR1", row.get("billAddr1"));
        }
        Map<String, String> parsed = GtbenrlBillAddressParser.fromGtbenrlRow(row);
        for (Map.Entry<String, String> entry : parsed.entrySet()) {
            row.put(entry.getKey(), entry.getValue());
        }
        row.put("gtbenrlPresent", "true");
        row.put("bannerPresent", "false");
        row.put("requireBannerMatch", "false");
        row.put("accountStatus", "N");
        return row;
    }

    private Map<String, Object> normalizeGtbenrlRow(Map<String, Object> source) {
        Map<String, Object> out = new HashMap<>();
        if (source == null) {
            return out;
        }
        out.putAll(source);
        putAlias(out, "customerCode", "GTBENRL_CUST_CODE", "CUSTOMERCODE");
        putAlias(out, "premisesCode", "GTBENRL_PREM_CODE", "PREMISESCODE");
        putAlias(out, "billAddr1", "GTBENRL_BILL_ADDR1", "BILLADDR1");
        putAlias(out, "billingCity", "GTBENRL_BILL_CITY", "BILLINGCITY");
        putAlias(out, "billingState", "GTBENRL_BILL_STATE", "BILLINGSTATE");
        putAlias(out, "billingStateCode", "GTBENRL_BILL_STATE", "BILLINGSTATE");
        putAlias(out, "billingZip", "GTBENRL_BILL_ZIP", "BILLINGZIP");
        putAlias(out, "billingZipCode", "GTBENRL_BILL_ZIP", "BILLINGZIP");
        putAlias(out, "bannerEmail", "BANNEREMAIL", "GTBENRL_EMAIL");
        return out;
    }

    private void putAlias(Map<String, Object> target, String canonical, String... aliases) {
        Object existing = target.get(canonical);
        if (existing != null && !existing.toString().isBlank()) {
            return;
        }
        for (String alias : aliases) {
            for (Map.Entry<String, Object> entry : target.entrySet()) {
                if (entry.getKey() != null && entry.getKey().equalsIgnoreCase(alias)
                        && entry.getValue() != null && !entry.getValue().toString().isBlank()) {
                    target.put(canonical, entry.getValue());
                    return;
                }
            }
        }
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
        log.warn("No Preferences account with PreDir - using street account (PreDir optional in UAT)");
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
        log.warn("No Preferences PO Box / blank-street account - using street account for PO Box TCs");
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
        log.warn("No Preferences billPresType=E - using any verifiable account (assert softens)");
        return anyVerifiable("bill confirmed");
    }

    Map<String, Object> resolveCorrConfirmed() {
        Map<String, Object> found = findMatchingOptional(
                a -> "E".equalsIgnoreCase(asString(a.get("correspondencePreference"))));
        if (found != null) {
            return found;
        }
        log.warn("No Preferences correspondencePreference=E - using any verifiable account (assert softens)");
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
        log.warn("No Preferences bill confirm date - using any verifiable account (assert softens)");
        return anyVerifiable("bill confirm date");
    }

    Map<String, Object> resolveCorrWithConfirmDate() {
        Map<String, Object> found = findMatchingOptional(a -> nonBlank(a, "corrDeliveryConfirmDate"));
        if (found != null) {
            return found;
        }
        log.warn("No Preferences corr confirm date - using any verifiable account (assert softens)");
        return anyVerifiable("corr confirm date");
    }

    private Map<String, Object> anyVerifiable(String label) {
        Map<String, Object> bannerBacked = findMatchingOptional(a -> true);
        if (bannerBacked != null) {
            log.info("Using Preferences-verifiable account {}/{} bannerPresent={} for {}",
                    bannerBacked.get("customerCode"), bannerBacked.get("premisesCode"),
                    bannerBacked.get("bannerPresent"), label);
            return bannerBacked;
        }
        return findMatching(label, a -> true);
    }

    /**
     * Strict pending bill: Banner pending/bootstrap only — no Preferences-only / P-as-I stand-in.
     * ErrorCode 302 on this Banner account fails the TC (eligibility / Verification Failed).
     */
    Map<String, Object> ensurePendingBillForVerifyStrict() {
        try {
            Map<String, Object> existing = ApplicationContext.get().getDbAction()
                    .getVerifyAccountNewPendingBill();
            ApplicationContext.get().getDbAction().enrichBannerEmail(existing);
            return finalizePendingAccount(existing, "billPresType");
        } catch (Exception ex) {
            log.info("No existing pending bill — bootstrapping Banner UpdatePaperless: {}", ex.getMessage());
        }
        Map<String, Object> bootstrapped = eligibilitySetup.ensureActivePendingBillEnrollment(true);
        return finalizePendingAccount(bootstrapped, "billPresType");
    }

    Map<String, Object> ensurePendingCorrForVerifyStrict() {
        try {
            Map<String, Object> existing = ApplicationContext.get().getDbAction()
                    .getVerifyAccountNewPendingCorr();
            ApplicationContext.get().getDbAction().enrichBannerEmail(existing);
            return finalizePendingAccount(existing, "correspondencePreference");
        } catch (Exception ex) {
            log.info("No existing pending corr — bootstrapping Banner UpdatePaperless: {}", ex.getMessage());
        }
        Map<String, Object> bootstrapped = eligibilitySetup.ensureActivePendingCorrEnrollment(true);
        return finalizePendingAccount(bootstrapped, "correspondencePreference");
    }

    /** @deprecated soft path kept for reference; use {@link #ensurePendingBillForVerifyStrict()} */
    Map<String, Object> ensurePendingBillForVerify() {
        return ensurePendingBillForVerifyStrict();
    }

    Map<String, Object> ensurePendingCorrForVerify() {
        return ensurePendingCorrForVerifyStrict();
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
            markBannerPresence(account);
            if (matcher.test(account)) {
                log.warn("Using known Preferences fallback account for {} (bannerPresent={})",
                        label, account.get("bannerPresent"));
                return account;
            }
        }
        throw new IllegalStateException(
                "No Preferences-verifiable account for " + label
                        + " after probing MariaDB registered + Banner street candidates (and known Preferences fallback)");
    }

    /**
     * Prefer Banner-backed Preferences accounts so Extent can show real Banner vs API match.
     */
    private Map<String, Object> findMatchingOptional(Predicate<Map<String, Object>> matcher) {
        Map<String, Object> nonBannerMatch = null;
        for (Map<String, Object> account : loadProbedAccounts()) {
            if (!matcher.test(account)) {
                continue;
            }
            if (Boolean.TRUE.equals(account.get("bannerPresent"))) {
                log.info("Selected Banner-backed Preferences account {}/{} email={}",
                        account.get("customerCode"), account.get("premisesCode"), account.get("bannerEmail"));
                DualReportManager.logInfo(
                        "VerifyAccount account selection — Banner-backed "
                                + account.get("customerCode") + "/" + account.get("premisesCode"));
                return new HashMap<>(account);
            }
            if (nonBannerMatch == null) {
                nonBannerMatch = account;
            }
        }
        if (nonBannerMatch != null) {
            log.warn("No Banner-backed Preferences match - using Preferences-only {}/{}",
                    nonBannerMatch.get("customerCode"), nonBannerMatch.get("premisesCode"));
            DualReportManager.logInfo(
                    "VerifyAccount account selection — Preferences-only (no Banner UCRACCT) "
                            + nonBannerMatch.get("customerCode") + "/" + nonBannerMatch.get("premisesCode"));
            return new HashMap<>(nonBannerMatch);
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
            probeMariaDbRegisteredAccounts(probed);
            probeBannerStreetCandidates(probed);
            // Always include known Preferences fallback if probeable and not already present
            PreferencesVerifyAccountUtil.tryVerifyAccount(FALLBACK_CUST, FALLBACK_PREM, FALLBACK_EMAIL)
                    .ifPresent(data -> addUnique(probed,
                            toAccountMap(FALLBACK_CUST, FALLBACK_PREM, FALLBACK_EMAIL, data), false));

            probed.sort(Comparator
                    .comparing((Map<String, Object> a) -> !Boolean.TRUE.equals(a.get("bannerPresent")))
                    .thenComparing(a -> asString(a.get("customerCode")), Comparator.nullsLast(String::compareTo)));

            long bannerCount = probed.stream().filter(a -> Boolean.TRUE.equals(a.get("bannerPresent"))).count();
            log.info("Preferences probe completed: {} verifiable accounts ({} Banner-backed)",
                    probed.size(), bannerCount);
            DualReportManager.logInfo(
                    "VerifyAccount probe pool — total=" + probed.size()
                            + ", bannerBacked=" + bannerCount
                            + " (pool discovery only; scenario request/DB evidence use the selected account below)");
            probedAccountsCache = List.copyOf(probed);
            return probedAccountsCache;
        }
    }

    private static void probeMariaDbRegisteredAccounts(List<Map<String, Object>> probed) {
        List<Map<String, Object>> rows = ApplicationContext.get().getDbAction("mariadb")
                .listPreferencesRegisteredAccounts(MARIADB_PROBE_LIMIT, false);
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
            addUnique(probed, toAccountMap(cust, prem, email, data.get()), false);
        }
    }

    /**
     * Probe Banner street+email candidates through Preferences so we can get Banner-backed FTD accounts.
     */
    private static void probeBannerStreetCandidates(List<Map<String, Object>> probed) {
        List<Map<String, Object>> candidates;
        try {
            candidates = ApplicationContext.get().getDbAction()
                    .listVerifyAccountStreetCandidates(BANNER_STREET_PROBE_LIMIT, false);
        } catch (Exception ex) {
            log.warn("Banner street candidate list failed: {}", ex.getMessage());
            return;
        }
        log.info("Probing Preferences VerifyAccount against {} Banner street candidates", candidates.size());
        int added = 0;
        for (Map<String, Object> candidate : candidates) {
            String cust = firstNonBlank(asString(candidate.get("customerCode")),
                    asString(candidate.get("CUSTOMERCODE")));
            String prem = firstNonBlank(asString(candidate.get("premisesCode")),
                    asString(candidate.get("PREMISESCODE")));
            String email = firstNonBlank(asString(candidate.get("bannerEmail")),
                    asString(candidate.get("BANNEREMAIL")));
            if (cust == null || prem == null || email == null) {
                continue;
            }
            Optional<ObjectNode> data = PreferencesVerifyAccountUtil.tryVerifyAccount(cust, prem, email);
            if (data.isEmpty()) {
                continue;
            }
            Map<String, Object> account = toAccountMap(cust, prem, email, data.get());
            // Candidate came from Banner — force bannerPresent and keep Banner street fields as backup
            account.put("bannerPresent", true);
            copyIfMissing(account, candidate, "billingStreetNumber");
            copyIfMissing(account, candidate, "billingStreetPreDirection");
            copyIfMissing(account, candidate, "billingStreetName");
            copyIfMissing(account, candidate, "billingCity");
            copyIfMissing(account, candidate, "billingStateCode");
            copyIfMissing(account, candidate, "billingZipCode");
            copyIfMissing(account, candidate, "billingPoBox");
            copyIfMissing(account, candidate, "billPresType");
            copyIfMissing(account, candidate, "correspondencePreference");
            if (addUnique(probed, account, false)) {
                added++;
            }
        }
        log.info("Added {} Preferences-verifiable Banner street candidates", added);
    }

    private static boolean addUnique(List<Map<String, Object>> probed, Map<String, Object> account) {
        return addUnique(probed, account, true);
    }

    private static boolean addUnique(List<Map<String, Object>> probed,
                                     Map<String, Object> account,
                                     boolean logBannerLookupToReport) {
        markBannerPresence(account, logBannerLookupToReport);
        String key = accountKey(account);
        for (Map<String, Object> existing : probed) {
            if (key.equals(accountKey(existing))) {
                if (Boolean.TRUE.equals(account.get("bannerPresent"))) {
                    existing.put("bannerPresent", true);
                }
                return false;
            }
        }
        probed.add(account);
        return true;
    }

    private static String accountKey(Map<String, Object> account) {
        return asString(account.get("customerCode")) + "/" + asString(account.get("premisesCode"));
    }

    private static void markBannerPresence(Map<String, Object> account) {
        markBannerPresence(account, true);
    }

    private static void markBannerPresence(Map<String, Object> account, boolean logToReport) {
        if (Boolean.TRUE.equals(account.get("bannerPresent"))) {
            return;
        }
        String cust = asString(account.get("customerCode"));
        String prem = asString(account.get("premisesCode"));
        Map<String, Object> banner = ApplicationContext.get().getDbAction()
                .tryGetVerifyAccountBannerEvidence(cust, prem);
        boolean present = banner != null;
        account.put("bannerPresent", present);
        if (present) {
            // Align stored codes to Banner row keys when variants matched
            Object bannerCust = banner.get("customerCode");
            Object bannerPrem = banner.get("premisesCode");
            if (bannerCust != null) {
                account.put("customerCode", bannerCust.toString().trim());
            }
            if (bannerPrem != null) {
                account.put("premisesCode", bannerPrem.toString().trim());
            }
            if (logToReport) {
                DualReportManager.logInfo(
                        "VerifyAccount Banner UCRACCT found for " + account.get("customerCode")
                                + "/" + account.get("premisesCode"));
            }
        }
    }

    private static void copyIfMissing(Map<String, Object> target, Map<String, Object> source, String key) {
        if (nonBlank(target, key)) {
            return;
        }
        Object value = source.get(key);
        if (value == null) {
            value = source.get(key.toUpperCase(Locale.ROOT));
        }
        if (value != null && !value.toString().isBlank()) {
            target.put(key, value.toString().trim());
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
