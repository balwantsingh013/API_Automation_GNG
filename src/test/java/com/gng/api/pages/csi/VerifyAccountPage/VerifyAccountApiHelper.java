package com.gng.api.pages.csi.VerifyAccountPage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.VerifyAccount.VerifyAccountRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.VerifyAccount.VerifyAccountLabel;
import com.gng.api.util.PreferencesVerifyAccountUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class VerifyAccountApiHelper {

    private final TestContext testContext;
    private final VerifyAccountSetupHelper setupHelper;

    public VerifyAccountApiHelper(TestContext testContext) {
        this.testContext = testContext;
        this.setupHelper = new VerifyAccountSetupHelper(testContext);
    }

    VerifyAccountRequest preparePayload(VerifyAccountLabel apiLabel) {
        log.info("Preparing Preferences requestbroker VerifyAccount payload for {}", apiLabel);
        return BasePage.deserializeJsonToPojo(
                VerifyAccountLabel.verify_account.toString(),
                VerifyAccountRequest.class);
    }

    public void preparePayloadForTestCondition(VerifyAccountRequest payload,
                                               VerifyAccountLabel testCondition) {
        switch (testCondition) {
            case TC_206__Positive__Billing_Street_Number_Value_:
            case TC_207__Positive__Billing_Street_Number_Format_:
            case TC_210__Positive__Billing_Street_Name_Value_:
            case TC_211__Positive__Billing_Street_Name_Format_:
            case TC_212__Positive__Billing_City_Value_:
            case TC_213__Positive__Billing_City_Format_:
            case TC_214__Positive__Billing_State_Value_:
            case TC_215__Positive__Billing_State_Format_:
            case TC_216__Positive__Billing_ZIP_Value_:
            case TC_217__Positive__Billing_ZIP_Format_:
                applyAccount(payload, setupHelper.resolveStreetAddressAccount());
                break;

            case TC_208__Positive__Billing_PreDir_Value_:
            case TC_209__Positive__Billing_PreDir_Format_:
                applyAccount(payload, setupHelper.resolveStreetAccountWithPreDir());
                break;

            case TC_218__Positive__Billing_PO_Box_Value_:
            case TC_219__Positive__Billing_PO_Box_Format_:
                applyAccount(payload, setupHelper.resolvePoBoxAccount());
                break;

            case TC_220__Positive__Bill_Delivery_Confirmation_Date_Value_:
            case TC_221__Positive__Bill_Delivery_Confirmation_Date_Format_:
                applyAccount(payload, setupHelper.resolveBillWithConfirmDate());
                break;

            case TC_222__Positive__Corr_Delivery_Confirmation_Date_Value_:
            case TC_223__Positive__Corr_Delivery_Confirmation_Date_Format_:
                applyAccount(payload, setupHelper.resolveCorrWithConfirmDate());
                break;

            case TC_224__Positive__Bill_Delivery_Preference_Confirmed_:
                applyAccount(payload, setupHelper.resolveBillConfirmed());
                break;

            case TC_225__Positive__Correspondence_Delivery_Preference_Confirmed_:
                applyAccount(payload, setupHelper.resolveCorrConfirmed());
                break;

            case TC_226__Positive__Bill_Delivery_Preference_Initiated_:
                applyAccount(payload, setupHelper.ensurePendingBillForVerify());
                break;

            case TC_227__Positive__Correspondence_Delivery_Preference_Initiated_:
                applyAccount(payload, setupHelper.ensurePendingCorrForVerify());
                break;

            case TC_228__Positive__Bill_Delivery_Preference_Paper_:
            case TC_230__Positive__Bill_Delivery_Preference_Expired_:
                applyAccount(payload, setupHelper.resolveBillPaperOrExpired());
                break;

            case TC_229__Positive__Correspondence_Delivery_Preference_Paper_:
            case TC_231__Positive__Correspondence_Delivery_Preference_Expired_:
                applyAccount(payload, setupHelper.resolveCorrPaperOrExpired());
                break;

            default:
                log.warn("Unhandled VerifyAccount test condition: {}", testCondition);
                break;
        }
    }

    private void applyAccount(VerifyAccountRequest payload, Map<String, Object> accountData) {
        String email = firstNonBlank(
                asString(accountData.get("bannerEmail")),
                asString(accountData.get("emailAddress")));
        if (email == null || email.isBlank()) {
            throw new IllegalStateException(
                    "Preferences VerifyAccount requires email for customer "
                            + accountData.get("customerCode"));
        }

        payload.setAcctSearchType("email");
        payload.setActions("VerifyAccount");
        payload.setCustCode(asString(accountData.get("customerCode")));
        payload.setPremCode(asString(accountData.get("premisesCode")));
        payload.setEmailAddress(email);
        payload.setModule("PEW");
        payload.setAuthenticationToken(PreferencesVerifyAccountUtil.requirePreferencesAuthToken());
        payload.setRequestID(UUID.randomUUID().toString());

        testContext.setCustomerCode(payload.getCustCode());
        testContext.setPremisesCode(payload.getPremCode());
        testContext.setVerifyAccountExpectedData(toExpectedMap(accountData));
    }

    private Map<String, String> toExpectedMap(Map<String, Object> accountData) {
        Map<String, String> expected = new HashMap<>();
        putIfPresent(expected, accountData, "billingStreetNumber");
        putIfPresent(expected, accountData, "billingStreetPreDirection");
        putIfPresent(expected, accountData, "billingStreetName");
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
        return expected;
    }

    private void putIfPresent(Map<String, String> target, Map<String, Object> source, String key) {
        Object value = source.get(key);
        if (value != null) {
            target.put(key, value.toString().trim());
        }
    }

    private String asString(Object value) {
        return value == null ? null : value.toString().trim();
    }

    private String firstNonBlank(String... values) {
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
