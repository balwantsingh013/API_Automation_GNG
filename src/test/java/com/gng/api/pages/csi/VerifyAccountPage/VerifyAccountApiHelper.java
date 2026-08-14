package com.gng.api.pages.csi.VerifyAccountPage;

import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.VerifyAccount.SwaggerVerifyAccountRequest;
import com.gng.api.pojo.CSIPojo.VerifyAccount.VerifyAccountRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.report.DualReportManager;
import com.gng.api.steps.csi.VerifyAccount.VerifyAccountLabel;
import com.gng.api.util.FakerDataGenerator;
import com.gng.api.util.GtbenrlBillAddressParser;
import com.gng.api.util.PaperlessEnrollmentUtil;
import com.gng.api.util.PreferencesVerifyAccountUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Builds VerifyAccount payloads per FTD05:
 * <ul>
 *   <li>TC_206–227 — Swagger {@code /Accounts/VerifyAccount} + UCRADDR billing</li>
 *   <li>TC_228–229 — Preferences broker + GTBENRL (no UCRADDR)</li>
 *   <li>TC_230–241 — Preferences broker + NEW prefs / confirm dates</li>
 * </ul>
 */
@Slf4j
public class VerifyAccountApiHelper {

    private final TestContext testContext;
    private final VerifyAccountSetupHelper setupHelper;

    public VerifyAccountApiHelper(TestContext testContext) {
        this.testContext = testContext;
        this.setupHelper = new VerifyAccountSetupHelper(testContext);
    }

    static boolean isSwaggerTestCondition(VerifyAccountLabel testCondition) {
        return switch (testCondition) {
            case TC_206__Positive__Billing_Street_Number_Value_,
                 TC_207__Positive__Billing_Street_Number_Format_,
                 TC_208__Positive__Billing_PreDir_Value_,
                 TC_209__Positive__Billing_PreDir_Format_,
                 TC_210__Positive__Billing_Street_Name_Value_,
                 TC_211__Positive__Billing_Street_Name_Format_,
                 TC_212__Positive__Billing_Street_Suffix_Value_,
                 TC_213__Positive__Billing_Street_Suffix_Format_,
                 TC_214__Positive__Billing_PostDir_Value_,
                 TC_215__Positive__Billing_PostDir_Format_,
                 TC_216__Positive__Billing_Unit_Type_Value_,
                 TC_217__Positive__Billing_Unit_Type_Format_,
                 TC_218__Positive__Billing_Unit_Number_Value_,
                 TC_219__Positive__Billing_Unit_Number_Format_,
                 TC_220__Positive__Billing_City_Value_,
                 TC_221__Positive__Billing_City_Format_,
                 TC_222__Positive__Billing_State_Value_,
                 TC_223__Positive__Billing_State_Format_,
                 TC_224__Positive__Billing_ZIP_Value_,
                 TC_225__Positive__Billing_ZIP_Format_,
                 TC_226__Positive__Billing_PO_Box_Value_,
                 TC_227__Positive__Billing_PO_Box_Format_ -> true;
            default -> false;
        };
    }

    VerifyAccountRequest preparePreferencesPayload(VerifyAccountLabel apiLabel) {
        log.info("Preparing Preferences requestbroker VerifyAccount payload for {}", apiLabel);
        return BasePage.deserializeJsonToPojo(
                VerifyAccountLabel.verify_account.toString(),
                VerifyAccountRequest.class);
    }

    SwaggerVerifyAccountRequest prepareSwaggerPayload() {
        log.info("Preparing Swagger Accounts/VerifyAccount payload");
        return BasePage.deserializeJsonToPojo(
                VerifyAccountLabel.verify_account_swagger.toString(),
                SwaggerVerifyAccountRequest.class);
    }

    public void prepareSwaggerPayloadForTestCondition(SwaggerVerifyAccountRequest payload,
                                                      VerifyAccountLabel testCondition) {
        Map<String, Object> accountData = switch (testCondition) {
            case TC_208__Positive__Billing_PreDir_Value_,
                 TC_209__Positive__Billing_PreDir_Format_ -> resolveBannerStreetWithField(
                    "billingStreetPreDirection", "TC_208/209 PreDir");
            case TC_212__Positive__Billing_Street_Suffix_Value_,
                 TC_213__Positive__Billing_Street_Suffix_Format_ -> resolveBannerStreetWithField(
                    "billingStreetSuffix", "TC_212/213 Street Suffix");
            case TC_214__Positive__Billing_PostDir_Value_,
                 TC_215__Positive__Billing_PostDir_Format_ -> resolveBannerStreetWithField(
                    "billingStreetPostDirection", "TC_214/215 PostDir");
            case TC_216__Positive__Billing_Unit_Type_Value_,
                 TC_217__Positive__Billing_Unit_Type_Format_ -> resolveBannerStreetWithField(
                    "billingUnitType", "TC_216/217 Unit Type");
            case TC_218__Positive__Billing_Unit_Number_Value_,
                 TC_219__Positive__Billing_Unit_Number_Format_ -> resolveBannerStreetWithField(
                    "billingUnitNumber", "TC_218/219 Unit Number");
            case TC_226__Positive__Billing_PO_Box_Value_,
                 TC_227__Positive__Billing_PO_Box_Format_ ->
                    ApplicationContext.get().getDbAction().getVerifyAccountWithUcraddrPoBox();
            default -> resolveBannerStreetAccount();
        };
        applySwaggerAccount(payload, accountData, testCondition);
    }

    public void preparePreferencesPayloadForTestCondition(VerifyAccountRequest payload,
                                                          VerifyAccountLabel testCondition) {
        switch (testCondition) {
            case TC_228__Positive__GTBENRL_Billing_Address_Source_,
                 TC_229__Positive__GTBENRL_Billing_Address_Parsing_ ->
                    applyGtbenrlAccount(payload,
                            setupHelper.ensureGtbenrlBillingAccountWithEmail(), testCondition);

            case TC_230__Positive__Bill_Delivery_Confirmation_Date_Value_,
                 TC_231__Positive__Bill_Delivery_Confirmation_Date_Format_,
                 TC_234__Positive__Bill_Delivery_Preference_Confirmed_ ->
                    applyBannerAccount(payload,
                            ApplicationContext.get().getDbAction().getVerifyAccountNewConfirmedBill(),
                            testCondition);

            case TC_232__Positive__Corr_Delivery_Confirmation_Date_Value_,
                 TC_233__Positive__Corr_Delivery_Confirmation_Date_Format_,
                 TC_235__Positive__Correspondence_Delivery_Preference_Confirmed_ ->
                    applyBannerAccount(payload,
                            ApplicationContext.get().getDbAction().getVerifyAccountNewConfirmedCorr(),
                            testCondition);

            case TC_236__Positive__Bill_Delivery_Preference_Initiated_ ->
                    applyBannerAccount(payload,
                            ApplicationContext.get().getDbAction().getVerifyAccountNewPendingBill(),
                            testCondition);

            case TC_237__Positive__Correspondence_Delivery_Preference_Initiated_ ->
                    applyBannerAccount(payload,
                            ApplicationContext.get().getDbAction().getVerifyAccountNewPendingCorr(),
                            testCondition);

            case TC_238__Positive__Bill_Delivery_Preference_Paper_ ->
                    applyBannerAccount(payload,
                            ApplicationContext.get().getDbAction().getVerifyAccountNewPaperBill(),
                            testCondition);

            case TC_239__Positive__Correspondence_Delivery_Preference_Paper_ ->
                    applyBannerAccount(payload,
                            ApplicationContext.get().getDbAction().getVerifyAccountNewPaperCorr(),
                            testCondition);

            case TC_240__Positive__Bill_Delivery_Preference_Expired_ ->
                    applyBannerAccount(payload,
                            ApplicationContext.get().getDbAction().getVerifyAccountNewExpiredBill(),
                            testCondition);

            case TC_241__Positive__Correspondence_Delivery_Preference_Expired_ ->
                    applyBannerAccount(payload,
                            ApplicationContext.get().getDbAction().getVerifyAccountNewExpiredCorr(),
                            testCondition);

            default -> log.warn("Unhandled Preferences VerifyAccount test condition: {}", testCondition);
        }
    }

    private Map<String, Object> resolveBannerStreetAccount() {
        List<Map<String, Object>> candidates = ApplicationContext.get().getDbAction()
                .listVerifyAccountStreetCandidates(10, true);
        if (candidates == null || candidates.isEmpty()) {
            return ApplicationContext.get().getDbAction().getVerifyAccountWithUcraddrStreet();
        }
        return candidates.get(0);
    }

    private Map<String, Object> resolveBannerStreetWithField(String field, String label) {
        List<Map<String, Object>> candidates = ApplicationContext.get().getDbAction()
                .listVerifyAccountStreetCandidates(40, true);
        for (Map<String, Object> row : candidates) {
            Object value = firstNonNull(row.get(field), row.get(field.toUpperCase()));
            if (value != null && !value.toString().isBlank()) {
                return row;
            }
        }
        throw new IllegalStateException(
                "TC " + label + " requires Banner UCRADDR " + field
                        + " — no street candidate with that field found");
    }

    private Map<String, Object> normalizeAccountKeys(Map<String, Object> source) {
        Map<String, Object> out = new HashMap<>();
        if (source == null) {
            return out;
        }
        for (Map.Entry<String, Object> entry : source.entrySet()) {
            if (entry.getKey() == null) {
                continue;
            }
            out.put(entry.getKey(), entry.getValue());
            // Oracle often returns uppercase aliases
            String lowerCamel = toLowerCamel(entry.getKey());
            if (!out.containsKey(lowerCamel) || out.get(lowerCamel) == null) {
                out.put(lowerCamel, entry.getValue());
            }
        }
        // Common aliases from raw Banner column names
        copyAlias(out, "customerCode", "CUSTOMERCODE", "GTBENRL_CUST_CODE");
        copyAlias(out, "premisesCode", "PREMISESCODE", "GTBENRL_PREM_CODE");
        copyAlias(out, "bannerEmail", "BANNEREMAIL", "GZBEMCP_EMAIL_ADDR");
        copyAlias(out, "ocsepciEmail", "OCSEPCIEMAIL", "OCSEPCI_EMAIL_ADDR");
        copyAlias(out, "gtbenrlEmail", "GTBENRLEMAIL", "GTBENRL_EMAIL");
        copyAlias(out, "billAddr1", "BILLADDR1", "GTBENRL_BILL_ADDR1");
        copyAlias(out, "billingCity", "BILLINGCITY", "GTBENRL_BILL_CITY");
        copyAlias(out, "billingState", "BILLINGSTATE", "GTBENRL_BILL_STATE");
        copyAlias(out, "billingStateCode", "BILLINGSTATECODE", "GTBENRL_BILL_STATE");
        copyAlias(out, "billingZip", "BILLINGZIP", "GTBENRL_BILL_ZIP");
        copyAlias(out, "billingZipCode", "BILLINGZIPCODE", "GTBENRL_BILL_ZIP");
        copyAlias(out, "hasActiveUcraddr", "HASACTIVEUCRADDR");
        out.putIfAbsent("accountStatus", "N");
        return out;
    }

    private void copyAlias(Map<String, Object> target, String canonical, String... aliases) {
        Object existing = target.get(canonical);
        if (existing != null && !existing.toString().isBlank()) {
            return;
        }
        for (String alias : aliases) {
            Object value = null;
            for (Map.Entry<String, Object> entry : target.entrySet()) {
                if (entry.getKey() != null && entry.getKey().equalsIgnoreCase(alias)
                        && entry.getValue() != null && !entry.getValue().toString().isBlank()) {
                    value = entry.getValue();
                    break;
                }
            }
            if (value != null) {
                target.put(canonical, value);
                return;
            }
        }
    }

    private void applySwaggerAccount(SwaggerVerifyAccountRequest payload,
                                     Map<String, Object> accountData,
                                     VerifyAccountLabel testCondition) {
        ApplicationContext.get().getDbAction().enrichBannerEmail(accountData);
        String email = PaperlessEnrollmentUtil.resolveBannerEmail(accountData);
        if (email == null || email.isBlank()) {
            throw new IllegalStateException(
                    "Swagger VerifyAccount account has no email for "
                            + asString(accountData, "customerCode") + "/"
                            + asString(accountData, "premisesCode"));
        }
        accountData.put("bannerEmail", email);
        accountData.put("bannerPresent", true);
        accountData.put("requireBannerMatch", true);
        accountData.put("endpointPath", "swagger");

        DualReportManager.logInfo(
                "VerifyAccount account (Swagger + UCRADDR) for " + testCondition + " — "
                        + asString(accountData, "customerCode") + "/"
                        + asString(accountData, "premisesCode")
                        + " email=" + email);

        payload.setVerifyType("email");
        payload.setCustomerCode(asString(accountData, "customerCode"));
        payload.setPremisesCode(asString(accountData, "premisesCode"));
        payload.setEmailAddress(email);
        payload.setOrigin("Macquarium");
        payload.setLoginID("autotester");
        payload.setRequestID(FakerDataGenerator.generateAlphanumeric(32));

        testContext.setCustomerCode(payload.getCustomerCode());
        testContext.setPremisesCode(payload.getPremisesCode());
        testContext.setVerifyAccountExpectedData(toExpectedMap(accountData));
    }

    private void applyBannerAccount(VerifyAccountRequest payload,
                                    Map<String, Object> accountData,
                                    VerifyAccountLabel testCondition) {
        accountData = normalizeAccountKeys(accountData);
        ApplicationContext.get().getDbAction().enrichBannerEmail(accountData);
        String email = PaperlessEnrollmentUtil.resolveBannerEmail(accountData);
        if (email == null || email.isBlank()) {
            String cust = asString(accountData, "customerCode");
            if (cust != null) {
                email = ApplicationContext.get().getDbAction().getAnyBannerEmailForCustomer(cust);
            }
        }
        // Preference-state accounts may lack GZBEMCP; use PPER/GTBENRL email when present,
        // otherwise still call VerifyAccount and report the API result.
        if (email == null || email.isBlank()) {
            email = firstNonBlank(
                    asString(accountData, "ocsepciEmail"),
                    asString(accountData, "OCSEPCIEMAIL"),
                    asString(accountData, "gtbenrlEmail"),
                    asString(accountData, "GTBENRLEMAIL"));
        }
        boolean missingGzbemcp = email == null || email.isBlank();
        if (missingGzbemcp) {
            DualReportManager.logInfo(
                    testCondition + " — no GZBEMCP/PPER/GTBENRL email on seed "
                            + asString(accountData, "customerCode") + "/"
                            + asString(accountData, "premisesCode")
                            + "; calling Preferences VerifyAccount without Banner email");
            email = "";
        }
        accountData.put("bannerEmail", missingGzbemcp ? null : email);
        accountData.put("bannerPresent", true);
        // GZRPPTH NEW — Preferences pref TCs do not require UCRADDR match
        accountData.put("requireBannerMatch", "false");
        accountData.put("accountSource", "gzrppth");
        accountData.put("endpointPath", "preferences");

        DualReportManager.logInfo(
                "VerifyAccount GZRPPTH account (Preferences) for " + testCondition + " — "
                        + asString(accountData, "customerCode") + "/"
                        + asString(accountData, "premisesCode")
                        + " email=" + (missingGzbemcp ? "(none)" : email)
                        + " billPresType=" + asString(accountData, "billPresType")
                        + " billConfirmDate=" + asString(accountData, "billDeliveryConfirmDate"));

        fillPreferencesPayload(payload, accountData, email);
        testContext.setVerifyAccountExpectedData(toExpectedMap(accountData));
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

    private void applyGtbenrlAccount(VerifyAccountRequest payload,
                                     Map<String, Object> accountData,
                                     VerifyAccountLabel testCondition) {
        accountData = normalizeAccountKeys(accountData);
        if (mapValue(accountData, "billAddr1") != null) {
            accountData.put("GTBENRL_BILL_ADDR1", mapValue(accountData, "billAddr1"));
        }
        ApplicationContext.get().getDbAction().enrichBannerEmail(accountData);
        String email = PaperlessEnrollmentUtil.resolveBannerEmail(accountData);
        if (email == null || email.isBlank()) {
            String cust = asString(accountData, "customerCode");
            if (cust != null) {
                email = ApplicationContext.get().getDbAction().getAnyBannerEmailForCustomer(cust);
            }
        }
        if (email == null || email.isBlank()) {
            Object custRaw = firstNonNull(
                    accountData.get("GTBENRL_CUST_CODE"),
                    accountData.get("gtbenrlCustCode"),
                    mapValue(accountData, "customerCode"),
                    mapValue(accountData, "GTBENRL_CUST_CODE"));
            Object premRaw = firstNonNull(
                    accountData.get("GTBENRL_PREM_CODE"),
                    accountData.get("gtbenrlPremCode"),
                    mapValue(accountData, "premisesCode"),
                    mapValue(accountData, "GTBENRL_PREM_CODE"));
            throw new IllegalStateException(
                    "GTBENRL VerifyAccount account has no email for "
                            + custRaw + "/" + premRaw
                            + " keys=" + accountData.keySet()
                            + " billAddr1=" + mapValue(accountData, "billAddr1")
                            + " rowSample=" + accountData);
        }

        Map<String, String> parsed = GtbenrlBillAddressParser.fromGtbenrlRow(accountData);
        for (Map.Entry<String, String> entry : parsed.entrySet()) {
            accountData.put(entry.getKey(), entry.getValue());
        }
        accountData.put("bannerEmail", email);
        accountData.put("bannerPresent", "false");
        accountData.put("gtbenrlPresent", "true");
        accountData.put("requireBannerMatch", "false");
        accountData.put("endpointPath", "preferences");

        DualReportManager.logInfo(
                "VerifyAccount account (Preferences + GTBENRL, no UCRADDR) for " + testCondition
                        + " — " + asString(accountData, "customerCode") + "/"
                        + asString(accountData, "premisesCode")
                        + " email=" + email
                        + " billAddr1=" + mapValue(accountData, "billAddr1"));

        fillPreferencesPayload(payload, accountData, email);
        testContext.setVerifyAccountExpectedData(toExpectedMap(accountData));
    }

    private void fillPreferencesPayload(VerifyAccountRequest payload,
                                        Map<String, Object> accountData,
                                        String email) {
        payload.setAcctSearchType("email");
        payload.setActions("VerifyAccount");
        payload.setCustCode(asString(accountData, "customerCode"));
        payload.setPremCode(asString(accountData, "premisesCode"));
        payload.setEmailAddress(email);
        payload.setModule("PEW");
        payload.setAuthenticationToken(PreferencesVerifyAccountUtil.requirePreferencesAuthToken());
        payload.setRequestID(FakerDataGenerator.generateAlphanumeric(32));

        testContext.setCustomerCode(payload.getCustCode());
        testContext.setPremisesCode(payload.getPremCode());
    }

    private Map<String, String> toExpectedMap(Map<String, Object> accountData) {
        Map<String, String> expected = new HashMap<>();
        putIfPresent(expected, accountData, "billAddr1");
        putIfPresent(expected, accountData, "GTBENRL_BILL_ADDR1");
        putIfPresent(expected, accountData, "billingStreetNumber");
        putIfPresent(expected, accountData, "billingStreetPreDirection");
        putIfPresent(expected, accountData, "billingStreetName");
        putIfPresent(expected, accountData, "billingStreetSuffix");
        putIfPresent(expected, accountData, "billingStreetPostDirection");
        putIfPresent(expected, accountData, "billingUnitType");
        putIfPresent(expected, accountData, "billingUnitNumber");
        putIfPresent(expected, accountData, "billingCity");
        putIfPresent(expected, accountData, "billingStateCode");
        putIfPresent(expected, accountData, "billingState");
        putIfPresent(expected, accountData, "billingZipCode");
        putIfPresent(expected, accountData, "billingZip");
        putIfPresent(expected, accountData, "billingPoBox");
        putIfPresent(expected, accountData, "billDeliveryConfirmDate");
        putIfPresent(expected, accountData, "corrDeliveryConfirmDate");
        putIfPresent(expected, accountData, "billPresType");
        putIfPresent(expected, accountData, "correspondencePreference");
        putIfPresent(expected, accountData, "billDeliveryOption");
        putIfPresent(expected, accountData, "corrDeliveryOption");
        putIfPresent(expected, accountData, "accountStatus");
        putIfPresent(expected, accountData, "endpointPath");
        putIfPresent(expected, accountData, "gtbenrlPresent");
        Object bannerPresent = mapValue(accountData, "bannerPresent");
        expected.put("bannerPresent",
                bannerPresent == null ? "false" : String.valueOf(bannerPresent));
        Object requireBannerMatch = mapValue(accountData, "requireBannerMatch");
        expected.put("requireBannerMatch",
                requireBannerMatch == null ? "false" : String.valueOf(requireBannerMatch));
        return expected;
    }

    private void putIfPresent(Map<String, String> target, Map<String, Object> source, String key) {
        Object value = mapValue(source, key);
        if (value != null && !value.toString().isBlank()) {
            target.put(key, value.toString().trim());
        }
    }

    private Object firstNonNull(Object... values) {
        if (values == null) {
            return null;
        }
        for (Object value : values) {
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    private Object mapValue(Map<String, Object> source, String key) {
        if (source == null || key == null) {
            return null;
        }
        Object direct = source.get(key);
        if (direct != null && !direct.toString().isBlank()) {
            return direct;
        }
        Object upper = source.get(key.toUpperCase(Locale.ROOT));
        if (upper != null && !upper.toString().isBlank()) {
            return upper;
        }
        Object lower = source.get(key.toLowerCase(Locale.ROOT));
        if (lower != null && !lower.toString().isBlank()) {
            return lower;
        }
        Object found = null;
        for (Map.Entry<String, Object> entry : source.entrySet()) {
            if (entry.getKey() != null && entry.getKey().equalsIgnoreCase(key)
                    && entry.getValue() != null && !entry.getValue().toString().isBlank()) {
                return entry.getValue();
            }
            if (found == null && entry.getKey() != null && entry.getKey().equalsIgnoreCase(key)) {
                found = entry.getValue();
            }
        }
        return found;
    }

    private String toLowerCamel(String key) {
        if (key == null || key.isBlank()) {
            return key;
        }
        // ALLCAPS token (e.g. CUSTOMERCODE) → lowercase
        if (key.equals(key.toUpperCase(Locale.ROOT)) && !key.contains("_")) {
            return key.toLowerCase(Locale.ROOT);
        }
        if (!key.contains("_") && Character.isUpperCase(key.charAt(0))) {
            return key.substring(0, 1).toLowerCase(Locale.ROOT) + key.substring(1);
        }
        if (!key.contains("_")) {
            return key;
        }
        String[] parts = key.toLowerCase(Locale.ROOT).split("_");
        StringBuilder sb = new StringBuilder(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            if (parts[i].isEmpty()) {
                continue;
            }
            sb.append(Character.toUpperCase(parts[i].charAt(0))).append(parts[i].substring(1));
        }
        return sb.toString();
    }

    private String asString(Object value) {
        return value == null ? null : value.toString().trim();
    }

    private String asString(Map<String, Object> source, String key) {
        return asString(mapValue(source, key));
    }
}
