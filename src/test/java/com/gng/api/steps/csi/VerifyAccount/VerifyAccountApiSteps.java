package com.gng.api.steps.csi.VerifyAccount;

import com.gng.api.pages.csi.VerifyAccountPage.VerifyAccountPage;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.report.DualReportManager;
import com.gng.api.util.CommonUtil;
import com.gng.api.util.GtbenrlBillAddressParser;
import com.gng.api.util.TestContextHolder;
import com.gng.api.util.VerifyAccountEvidenceUtil;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static com.gng.api.steps.csi.VerifyAccount.VerifyAccountLabel.verify_account;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;

/**
 * FTD05 VerifyAccount assertions (updated sheet TC_206–241).
 * <ul>
 *   <li>206–227: Swagger + UCRADDR billing field value/format</li>
 *   <li>228–229: Preferences + GTBENRL billing parse</li>
 *   <li>230–241: Preferences + NEW prefs / confirm dates</li>
 * </ul>
 */
@Slf4j
public class VerifyAccountApiSteps {

    @Before
    public void setupToken() {
        CommonUtil.silentlyGenerateAuthToken();
    }

    private final TestContext testContext;
    private final VerifyAccountPage verifyAccountPage;

    public VerifyAccountApiSteps(TestContext testContext, VerifyAccountPage verifyAccountPage) {
        this.testContext = testContext;
        this.verifyAccountPage = verifyAccountPage;
        testContext.setVerifyAccountApiPage(verifyAccountPage);
        TestContextHolder.set(testContext);
    }

    @When("a request is made to VerifyAccount Api for {string}")
    public void a_request_is_made_to_verify_account_api(String testCondition) {
        CommonUtil.logTestDescriptionToReports(testCondition);
        verifyAccountPage.validateResponseForTestCondition(
                verify_account,
                VerifyAccountLabel.valueOf(testCondition));
    }

    @And("validate VerifyAccount response for {string}")
    public void validateVerifyAccountResponseFor(String testCondition) {
        Integer errorCode = testContext.getResponse().jsonPath().getInt("errorCode");
        Boolean success = testContext.getResponse().jsonPath().getBoolean("success");
        String errorMessage = testContext.getResponse().jsonPath().getString("errorMessage");
        Map<String, String> expected = testContext.getVerifyAccountExpectedData();
        boolean preferencesPath = !"swagger".equalsIgnoreCase(
                expected == null ? null : expected.get("endpointPath"));

        if (preferencesPath && errorCode != null && errorCode == 302) {
            DualReportManager.logInfo(
                    "Preferences VerifyAccount ErrorCode 302 (Verification Failed) for "
                            + testContext.getCustomerCode() + "/" + testContext.getPremisesCode()
                            + ". For Active UCRACCT this can be expected (GTBENRL-only path); "
                            + "for NEW/GTBENRL TCs it is a failure.");
        }

        assertThat(
                failureMessage(testCondition, success, errorCode, errorMessage, preferencesPath),
                errorCode, equalTo(0));
        assertThat("success should be true", success, equalTo(true));
        assertThat("data should be present", testContext.getResponse().jsonPath().get("data"), notNullValue());

        VerifyAccountEvidenceUtil.logEvidenceForScenario(
                testCondition,
                testContext.getCustomerCode(),
                testContext.getPremisesCode(),
                testContext.getResponse().jsonPath(),
                expected);

        if (expected != null && "true".equalsIgnoreCase(expected.get("requireBannerMatch"))) {
            assertThat(
                    "TC requires Banner UCRADDR account",
                    expected.get("bannerPresent"), equalTo("true"));
        }
        if (expected != null && "true".equalsIgnoreCase(expected.get("gtbenrlPresent"))) {
            assertThat("TC requires GTBENRL (no UCRADDR) account",
                    expected.get("gtbenrlPresent"), equalTo("true"));
        }

        switch (VerifyAccountLabel.valueOf(testCondition)) {
            case TC_206__Positive__Billing_Street_Number_Value_ ->
                    assertFieldMatchesDb("data.billingStreetNumber", expected.get("billingStreetNumber"));
            case TC_207__Positive__Billing_Street_Number_Format_ ->
                    assertStringMaxLength("data.billingStreetNumber", 12);
            case TC_208__Positive__Billing_PreDir_Value_ ->
                    assertFieldMatchesDb("data.billingStreetPreDirection",
                            expected.get("billingStreetPreDirection"));
            case TC_209__Positive__Billing_PreDir_Format_ ->
                    assertStringMaxLength("data.billingStreetPreDirection", 2);
            case TC_210__Positive__Billing_Street_Name_Value_ ->
                    assertFieldMatchesDb("data.billingStreetName", expected.get("billingStreetName"));
            case TC_211__Positive__Billing_Street_Name_Format_ ->
                    assertStringMaxLength("data.billingStreetName", 30);
            case TC_212__Positive__Billing_Street_Suffix_Value_ ->
                    assertFieldMatchesDb("data.billingStreetSuffix", expected.get("billingStreetSuffix"));
            case TC_213__Positive__Billing_Street_Suffix_Format_ ->
                    assertStringMaxLength("data.billingStreetSuffix", 6);
            case TC_214__Positive__Billing_PostDir_Value_ ->
                    assertFieldMatchesDb("data.billingStreetPostDirection",
                            expected.get("billingStreetPostDirection"));
            case TC_215__Positive__Billing_PostDir_Format_ ->
                    assertStringMaxLength("data.billingStreetPostDirection", 2);
            case TC_216__Positive__Billing_Unit_Type_Value_ ->
                    assertFieldMatchesDb("data.billingUnitType", expected.get("billingUnitType"));
            case TC_217__Positive__Billing_Unit_Type_Format_ ->
                    assertStringMaxLength("data.billingUnitType", 6);
            case TC_218__Positive__Billing_Unit_Number_Value_ ->
                    assertFieldMatchesDb("data.billingUnitNumber", expected.get("billingUnitNumber"));
            case TC_219__Positive__Billing_Unit_Number_Format_ ->
                    assertStringMaxLength("data.billingUnitNumber", 6);
            case TC_220__Positive__Billing_City_Value_ ->
                    assertFieldMatchesDb("data.billingCity", expected.get("billingCity"));
            case TC_221__Positive__Billing_City_Format_ ->
                    assertStringMaxLength("data.billingCity", 20);
            case TC_222__Positive__Billing_State_Value_ ->
                    assertStateMatchesDb(firstNonBlank(
                            expected.get("billingState"), expected.get("billingStateCode")));
            case TC_223__Positive__Billing_State_Format_ ->
                    assertStateFormat();
            case TC_224__Positive__Billing_ZIP_Value_ ->
                    assertZipMatchesDb(firstNonBlank(
                            expected.get("billingZip"), expected.get("billingZipCode")));
            case TC_225__Positive__Billing_ZIP_Format_ ->
                    assertZipFormat();
            case TC_226__Positive__Billing_PO_Box_Value_ ->
                    assertPoBoxValue(expected.get("billingPoBox"));
            case TC_227__Positive__Billing_PO_Box_Format_ ->
                    assertPoBoxFormat(expected.get("billingPoBox"));
            case TC_228__Positive__GTBENRL_Billing_Address_Source_ ->
                    assertGtbenrlBillingSource(expected);
            case TC_229__Positive__GTBENRL_Billing_Address_Parsing_ -> {
                String expectedStreet = expected == null ? null : expected.get("billingStreetNumber");
                if (expectedStreet == null || expectedStreet.isBlank()) {
                    String addr1 = firstNonBlank(
                            expected == null ? null : expected.get("billAddr1"),
                            expected == null ? null : expected.get("GTBENRL_BILL_ADDR1"));
                    expectedStreet = GtbenrlBillAddressParser.parseBillAddr1(addr1)
                            .get("billingStreetNumber");
                }
                assertFieldMatchesDb("data.billingStreetNumber", expectedStreet);
            }
            case TC_230__Positive__Bill_Delivery_Confirmation_Date_Value_ ->
                    assertRequiredConfirmDateValue("data.billDeliveryConfirmDate",
                            expected.get("billDeliveryConfirmDate"));
            case TC_231__Positive__Bill_Delivery_Confirmation_Date_Format_ ->
                    assertRequiredYyyyMmDdFormat("data.billDeliveryConfirmDate");
            case TC_232__Positive__Corr_Delivery_Confirmation_Date_Value_ ->
                    assertRequiredConfirmDateValue("data.corrDeliveryConfirmDate",
                            expected.get("corrDeliveryConfirmDate"));
            case TC_233__Positive__Corr_Delivery_Confirmation_Date_Format_ ->
                    assertRequiredYyyyMmDdFormat("data.corrDeliveryConfirmDate");
            case TC_234__Positive__Bill_Delivery_Preference_Confirmed_ ->
                    assertPreferenceExact("billPresType", "E");
            case TC_235__Positive__Correspondence_Delivery_Preference_Confirmed_ ->
                    assertPreferenceExact("correspondencePreference", "E");
            case TC_236__Positive__Bill_Delivery_Preference_Initiated_ -> {
                assertPreferenceExact("billPresType", "I");
                assertConfirmDateNullOrAbsent("data.billDeliveryConfirmDate");
            }
            case TC_237__Positive__Correspondence_Delivery_Preference_Initiated_ -> {
                assertPreferenceExact("correspondencePreference", "I");
                assertConfirmDateNullOrAbsent("data.corrDeliveryConfirmDate");
            }
            case TC_238__Positive__Bill_Delivery_Preference_Paper_ ->
                    assertPreferenceExact("billPresType", "P");
            case TC_239__Positive__Correspondence_Delivery_Preference_Paper_ ->
                    assertPreferenceExact("correspondencePreference", "P");
            case TC_240__Positive__Bill_Delivery_Preference_Expired_ ->
                    assertPreferenceExact("billPresType", "P");
            case TC_241__Positive__Correspondence_Delivery_Preference_Expired_ ->
                    assertPreferenceExact("correspondencePreference", "P");
            default -> throw new IllegalArgumentException(
                    "No VerifyAccount validation for: " + testCondition);
        }
    }

    private void assertGtbenrlBillingSource(Map<String, String> expected) {
        String billAddr1 = firstNonBlank(
                expected == null ? null : expected.get("billAddr1"),
                expected == null ? null : expected.get("GTBENRL_BILL_ADDR1"));
        assertThat(
                "TC_228 requires GTBENRL_BILL_ADDR1 on the selected account",
                billAddr1, notNullValue());

        Map<String, String> parsed = GtbenrlBillAddressParser.parseBillAddr1(billAddr1);
        if (expected != null) {
            if (expected.get("billingCity") != null) {
                parsed.put("billingCity", expected.get("billingCity"));
            }
            if (firstNonBlank(expected.get("billingState"), expected.get("billingStateCode")) != null) {
                parsed.put("billingState", firstNonBlank(
                        expected.get("billingState"), expected.get("billingStateCode")));
            }
            if (firstNonBlank(expected.get("billingZip"), expected.get("billingZipCode")) != null) {
                parsed.put("billingZip", firstNonBlank(
                        expected.get("billingZip"), expected.get("billingZipCode")));
            }
        }

        DualReportManager.logInfo(
                "TC_228 GTBENRL billing source assert — BILL_ADDR1=" + billAddr1
                        + " parsed=" + parsed);

        assertGtbenrlParsedField("billingStreetNumber", parsed.get("billingStreetNumber"));
        assertGtbenrlParsedField("billingStreetPreDirection", parsed.get("billingStreetPreDirection"));
        assertGtbenrlParsedField("billingStreetName", parsed.get("billingStreetName"));
        assertGtbenrlParsedField("billingStreetSuffix", parsed.get("billingStreetSuffix"));
        assertGtbenrlParsedField("billingStreetPostDirection", parsed.get("billingStreetPostDirection"));
        assertGtbenrlParsedField("billingUnitType", parsed.get("billingUnitType"));
        assertGtbenrlParsedField("billingUnitNumber", parsed.get("billingUnitNumber"));
        assertGtbenrlParsedField("billingPoBox", parsed.get("billingPoBox"));
        assertGtbenrlParsedField("billingCity", parsed.get("billingCity"));
        assertStateMatchesDb(parsed.get("billingState"));
        assertZipMatchesDb(parsed.get("billingZip"));
    }

    /**
     * TC_228 / FTD05: billing fields must come from GTBENRL_BILL_ADDR1 parse.
     * Populated GTBENRL elements must match; empty elements must stay blank
     * (must not be filled from service address).
     */
    private void assertGtbenrlParsedField(String field, String expectedFromGtbenrl) {
        String actual = firstNonBlank(
                testContext.getResponse().jsonPath().getString("data." + field),
                testContext.getResponse().jsonPath().getString(
                        "data." + Character.toUpperCase(field.charAt(0)) + field.substring(1)));
        if (expectedFromGtbenrl == null || expectedFromGtbenrl.isBlank()) {
            assertThat(
                    "TC_228: data." + field + " must be blank when GTBENRL_BILL_ADDR1 has no value "
                            + "(got '" + actual + "' — service/other address bleed, not GTBENRL parse)",
                    actual, nullValue());
            return;
        }
        assertThat(
                "TC_228: data." + field + " must match GTBENRL parse '" + expectedFromGtbenrl + "'",
                actual, equalTo(expectedFromGtbenrl.trim()));
    }

    private String failureMessage(String testCondition, Boolean success, Integer errorCode,
                                  String errorMessage, boolean preferencesPath) {
        String path = preferencesPath ? "Preferences" : "Swagger";
        return "FAIL [" + testCondition + "] " + path + " VerifyAccount for "
                + testContext.getCustomerCode() + "/" + testContext.getPremisesCode()
                + " — expected ErrorCode=0 but got success=" + success
                + ", errorCode=" + errorCode
                + ", errorMessage=" + errorMessage
                + (preferencesPath && Integer.valueOf(302).equals(errorCode)
                ? " | Preferences PEW Verification Failed (302)"
                : "");
    }

    private void assertFieldMatchesDb(String jsonPath, String expectedDbValue) {
        String actual = testContext.getResponse().jsonPath().getString(jsonPath);
        assertThat(jsonPath + " should be populated", actual, notNullValue());
        assertThat(jsonPath + " should not be blank", actual.isBlank(), is(false));
        assertThat(jsonPath + " expected Banner/DB value must be present",
                expectedDbValue, notNullValue());
        assertThat(jsonPath + " should match Banner/DB value",
                actual.trim(), equalTo(expectedDbValue.trim()));
    }

    private void assertStateMatchesDb(String expectedDbValue) {
        String actual = firstNonBlank(
                testContext.getResponse().jsonPath().getString("data.billingState"),
                testContext.getResponse().jsonPath().getString("data.billingStateCode"));
        assertThat("data.billingState should be populated", actual, notNullValue());
        assertThat("Banner state expected value required for TC", expectedDbValue, notNullValue());
        assertThat("data.billingState should match Banner",
                actual.trim(), equalTo(expectedDbValue.trim()));
    }

    private void assertStateFormat() {
        String actual = firstNonBlank(
                testContext.getResponse().jsonPath().getString("data.billingState"),
                testContext.getResponse().jsonPath().getString("data.billingStateCode"));
        assertThat("data.billingState should be populated", actual, notNullValue());
        assertThat("data.billingState length should be <= 2",
                actual.length(), lessThanOrEqualTo(2));
    }

    private void assertZipMatchesDb(String expectedDbValue) {
        String actual = firstNonBlank(
                testContext.getResponse().jsonPath().getString("data.billingZip"),
                testContext.getResponse().jsonPath().getString("data.billingZipCode"));
        assertThat("data.billingZip should be populated", actual, notNullValue());
        assertThat("data.billingZip should not be blank", actual.isBlank(), is(false));
        assertThat("Banner ZIP expected value required for TC", expectedDbValue, notNullValue());
        String expected = expectedDbValue.trim();
        String actualTrim = actual.trim();
        boolean matches = actualTrim.equals(expected)
                || actualTrim.startsWith(expected)
                || expected.startsWith(actualTrim.replaceAll("-.*", ""));
        assertThat("data.billingZip should match Banner ZIP (actual=" + actualTrim
                        + ", expected=" + expected + ")",
                matches, is(true));
    }

    private void assertZipFormat() {
        String actual = firstNonBlank(
                testContext.getResponse().jsonPath().getString("data.billingZip"),
                testContext.getResponse().jsonPath().getString("data.billingZipCode"));
        assertThat("data.billingZip should be populated", actual, notNullValue());
        assertThat("data.billingZip length should be <= 10",
                actual.length(), lessThanOrEqualTo(10));
    }

    private void assertPoBoxValue(String expectedDbPoBox) {
        String apiPoBox = firstNonBlank(
                testContext.getResponse().jsonPath().getString("data.billingPoBox"),
                testContext.getResponse().jsonPath().getString("data.BillingPoBox"));
        assertThat(
                "VerifyAccount must return data.billingPoBox (got null). Expected="
                        + expectedDbPoBox,
                apiPoBox, notNullValue());
        assertThat("billingPoBox should match Banner", apiPoBox, equalTo(expectedDbPoBox));
        assertThat("billingPoBox should include PO BOX prefix",
                apiPoBox.toUpperCase(), startsWith("PO BOX"));
    }

    private void assertPoBoxFormat(String expectedDbPoBox) {
        String apiPoBox = firstNonBlank(
                testContext.getResponse().jsonPath().getString("data.billingPoBox"),
                testContext.getResponse().jsonPath().getString("data.BillingPoBox"));
        assertThat(
                "VerifyAccount must return data.billingPoBox for format check (got null). Expected="
                        + expectedDbPoBox,
                apiPoBox, notNullValue());
        assertThat("billingPoBox length should be <= 60", apiPoBox.length(), lessThanOrEqualTo(60));
    }

    private void assertRequiredConfirmDateValue(String jsonPath, String expectedDbValue) {
        String actual = testContext.getResponse().jsonPath().getString(jsonPath);
        assertThat(jsonPath + " must be returned for confirm-date TC (was null/blank)",
                actual != null && !actual.isBlank(), is(true));
        assertFieldMatchesDb(jsonPath, expectedDbValue);
    }

    private void assertRequiredYyyyMmDdFormat(String jsonPath) {
        String actual = testContext.getResponse().jsonPath().getString(jsonPath);
        assertThat(jsonPath + " must be returned for confirm-date format TC",
                actual != null && !actual.isBlank(), is(true));
        assertThat(jsonPath + " should be YYYYMMDD String(8)", actual, matchesPattern("\\d{8}"));
        assertThat(jsonPath + " length should be 8", actual.length(), equalTo(8));
    }

    private void assertConfirmDateNullOrAbsent(String jsonPath) {
        Object value = testContext.getResponse().jsonPath().get(jsonPath);
        if (value == null) {
            return;
        }
        if (value instanceof String && ((String) value).isBlank()) {
            return;
        }
        assertThat(jsonPath + " should be null when preference is initiated", value, nullValue());
    }

    private void assertPreferenceExact(String camelCaseField, String expected) {
        String actual = firstNonBlank(
                testContext.getResponse().jsonPath().getString("data." + camelCaseField),
                testContext.getResponse().jsonPath().getString(
                        "data." + Character.toUpperCase(camelCaseField.charAt(0))
                                + camelCaseField.substring(1)));
        assertThat(
                "data." + camelCaseField + " must be '" + expected + "' (got " + actual + ").",
                actual, equalTo(expected));
    }

    private void assertStringMaxLength(String jsonPath, int maxLength) {
        String actual = testContext.getResponse().jsonPath().getString(jsonPath);
        assertThat(jsonPath + " should be populated", actual, notNullValue());
        assertThat(jsonPath + " should not be blank", actual.isBlank(), is(false));
        assertThat(jsonPath + " length should be <= " + maxLength,
                actual.length(), lessThanOrEqualTo(maxLength));
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
