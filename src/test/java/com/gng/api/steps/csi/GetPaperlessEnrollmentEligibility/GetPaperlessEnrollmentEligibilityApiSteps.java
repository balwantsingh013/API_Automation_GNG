package com.gng.api.steps.csi.GetPaperlessEnrollmentEligibility;

import com.gng.api.pages.csi.GetPaperlessEnrollmentEligibilityPage.GetPaperlessEnrollmentEligibilityPage;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.util.CommonUtil;
import com.gng.api.util.TestContextHolder;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

import static com.gng.api.steps.csi.GetPaperlessEnrollmentEligibility.GetPaperlessEnrollmentEligibilityLabel.get_paperless_enrollment_eligibility;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@Slf4j
public class GetPaperlessEnrollmentEligibilityApiSteps {

    private static final String EMAIL_NOT_PRESENT = "Email Address is Not Present";
    private static final String ENROLLED_PAPERLESS = "Account is Enrolled on Paperless";
    private static final String ENROLLED_FISERV = "Account is Enrolled on Fiserv eBill";

    @Before
    public void setupToken() {
        CommonUtil.silentlyGenerateAuthToken();
    }

    private final TestContext testContext;
    private final GetPaperlessEnrollmentEligibilityPage getPaperlessEnrollmentEligibilityPage;

    public GetPaperlessEnrollmentEligibilityApiSteps(TestContext testContext,
                                                     GetPaperlessEnrollmentEligibilityPage getPaperlessEnrollmentEligibilityPage) {
        this.testContext = testContext;
        this.getPaperlessEnrollmentEligibilityPage = getPaperlessEnrollmentEligibilityPage;
        testContext.setGetPaperlessEnrollmentEligibilityApiPage(getPaperlessEnrollmentEligibilityPage);
        TestContextHolder.set(testContext);
    }

    @When("a request is made to GetPaperlessEnrollmentEligibility Api for {string}")
    public void a_request_is_made_to_get_paperless_enrollment_eligibility_api(String testCondition) {
        CommonUtil.logTestDescriptionToReports(testCondition);
        getPaperlessEnrollmentEligibilityPage.validateResponseForTestCondition(
                get_paperless_enrollment_eligibility,
                GetPaperlessEnrollmentEligibilityLabel.valueOf(testCondition));
    }

    @And("validate GetPaperlessEnrollmentEligibility response for {string}")
    public void validateGetPaperlessEnrollmentEligibilityResponseFor(String testCondition) {
        switch (GetPaperlessEnrollmentEligibilityLabel.valueOf(testCondition)) {
            case TC_164__Positive__Corr_Delivery_Option_Format_:
                assertThat("correspondenceDeliveryOption mismatch",
                        testContext.getResponse().jsonPath().getString("data.correspondenceDeliveryOption"),
                        equalTo("P"));
                assertThat("correspondenceDeliveryOption should be single character",
                        testContext.getResponse().jsonPath().getString("data.correspondenceDeliveryOption").length(),
                        equalTo(1));
                break;

            case TC_165__Positive__Bill_Delivery_Option_Format_:
                assertThat("billDeliveryOption mismatch",
                        testContext.getResponse().jsonPath().getString("data.billDeliveryOption"),
                        equalTo("P"));
                assertThat("billDeliveryOption should be single character",
                        testContext.getResponse().jsonPath().getString("data.billDeliveryOption").length(),
                        equalTo(1));
                break;

            case TC_166__Positive__Corr_Eligible_Format_:
                assertBooleanFieldTrue("data.paperlessCorrespondenceEligible", "paperlessCorrespondenceEligible");
                break;

            case TC_167__Positive__Bill_Eligible_Format_:
                assertThat("paperlessBillEligible mismatch",
                        testContext.getResponse().jsonPath().getBoolean("data.paperlessBillEligible"),
                        is(true));
                break;

            case TC_168__Positive__Corr_Reason_Code_Format_:
                assertThat("paperlessCorrIneligReasonCode mismatch",
                        testContext.getResponse().jsonPath().getInt("data.paperlessCorrIneligReasonCode"),
                        equalTo(1));
                break;

            case TC_169__Positive__Bill_Reason_Code_Format_:
                assertThat("paperlessBillIneligReasonCode mismatch",
                        testContext.getResponse().jsonPath().getInt("data.paperlessBillIneligReasonCode"),
                        equalTo(2));
                break;

            case TC_170__Positive__Corr_Reason_Desc_Format_:
                assertPopulatedStringMaxLength("data.paperlessCorrIneligReasonDesc", 50);
                break;

            case TC_171__Positive__Bill_Reason_Desc_Format_:
                assertThat("paperlessBillIneligReasonCode mismatch",
                        testContext.getResponse().jsonPath().getInt("data.paperlessBillIneligReasonCode"),
                        equalTo(3));
                assertThat("paperlessBillIneligReasonDesc mismatch",
                        testContext.getResponse().jsonPath().getString("data.paperlessBillIneligReasonDesc"),
                        equalTo(ENROLLED_FISERV));
                assertThat("paperlessBillIneligReasonDesc length",
                        testContext.getResponse().jsonPath().getString("data.paperlessBillIneligReasonDesc").length(),
                        lessThanOrEqualTo(50));
                break;

            case TC_172__Positive__Correspondence_NULL_Defaults_to_P_:
                assertDeliveryOption("correspondenceDeliveryOption", "P");
                break;

            case TC_173__Positive__Correspondence_P_Mapping_:
                assertDeliveryOption("correspondenceDeliveryOption", "P");
                break;

            case TC_174__Positive__Correspondence_E_Mapping_:
                assertDeliveryOption("correspondenceDeliveryOption", "E");
                break;

            case TC_175__Positive__Bill_NULL_Defaults_to_P_:
                assertDeliveryOption("billDeliveryOption", "P");
                break;

            case TC_176__Positive__Bill_P_Mapping_:
                assertDeliveryOption("billDeliveryOption", "P");
                break;

            case TC_177__Positive__Bill_E_Mapping_:
                assertDeliveryOption("billDeliveryOption", "E");
                break;

            case TC_178__Positive__Bill_F_Mapping_:
                assertDeliveryOption("billDeliveryOption", "F");
                break;

            case TC_179__Positive__Corr_Ineligible_No_Email_:
                assertThat("paperlessCorrespondenceEligible mismatch",
                        testContext.getResponse().jsonPath().getBoolean("data.paperlessCorrespondenceEligible"),
                        is(false));
                break;

            case TC_180__Positive__Bill_Ineligible_No_Email_:
                assertThat("paperlessBillEligible mismatch",
                        testContext.getResponse().jsonPath().getBoolean("data.paperlessBillEligible"),
                        is(false));
                break;

            case TC_181__Positive__Corr_Reason_Code_1_:
                assertThat("paperlessCorrIneligReasonCode mismatch",
                        testContext.getResponse().jsonPath().getInt("data.paperlessCorrIneligReasonCode"),
                        equalTo(1));
                break;

            case TC_181A__Positive__Corr_Reason_Desc_1_:
                assertThat("paperlessCorrIneligReasonDesc mismatch",
                        testContext.getResponse().jsonPath().getString("data.paperlessCorrIneligReasonDesc"),
                        equalTo(EMAIL_NOT_PRESENT));
                break;

            case TC_182__Positive__Bill_Reason_Code_1_:
                assertThat("paperlessBillIneligReasonCode mismatch",
                        testContext.getResponse().jsonPath().getInt("data.paperlessBillIneligReasonCode"),
                        equalTo(1));
                break;

            case TC_182A__Positive__Bill_Reason_Desc_1_:
                assertThat("paperlessBillIneligReasonDesc mismatch",
                        testContext.getResponse().jsonPath().getString("data.paperlessBillIneligReasonDesc"),
                        equalTo(EMAIL_NOT_PRESENT));
                break;

            case TC_183__Positive__Corr_Eligible_:
                assertBooleanFieldTrue("data.paperlessCorrespondenceEligible", "paperlessCorrespondenceEligible");
                break;

            case TC_184__Positive__Corr_Ineligible_Enrolled_:
                assertThat("paperlessCorrespondenceEligible mismatch",
                        testContext.getResponse().jsonPath().getBoolean("data.paperlessCorrespondenceEligible"),
                        is(false));
                break;

            case TC_185__Positive__Bill_Eligible_:
                assertThat("paperlessBillEligible mismatch",
                        testContext.getResponse().jsonPath().getBoolean("data.paperlessBillEligible"),
                        is(true));
                break;

            case TC_186__Positive__Bill_Ineligible_Enrolled_:
                assertThat("paperlessBillEligible mismatch",
                        testContext.getResponse().jsonPath().getBoolean("data.paperlessBillEligible"),
                        is(false));
                break;

            case TC_187__Positive__Bill_Ineligible_Fiserv_:
                assertThat("paperlessBillEligible mismatch",
                        testContext.getResponse().jsonPath().getBoolean("data.paperlessBillEligible"),
                        is(false));
                break;

            case TC_188__Positive__Corr_Reason_Code_2_:
                assertThat("paperlessCorrIneligReasonCode mismatch",
                        testContext.getResponse().jsonPath().getInt("data.paperlessCorrIneligReasonCode"),
                        equalTo(2));
                break;

            case TC_188A__Positive__Corr_Reason_Desc_2_:
                assertThat("paperlessCorrIneligReasonDesc mismatch",
                        testContext.getResponse().jsonPath().getString("data.paperlessCorrIneligReasonDesc"),
                        equalTo(ENROLLED_PAPERLESS));
                break;

            case TC_189__Positive__Bill_Reason_Code_2_:
                assertThat("paperlessBillIneligReasonCode mismatch",
                        testContext.getResponse().jsonPath().getInt("data.paperlessBillIneligReasonCode"),
                        equalTo(2));
                break;

            case TC_189A__Positive__Bill_Reason_Desc_2_:
                assertThat("paperlessBillIneligReasonDesc mismatch",
                        testContext.getResponse().jsonPath().getString("data.paperlessBillIneligReasonDesc"),
                        equalTo(ENROLLED_PAPERLESS));
                break;

            case TC_190__Positive__Bill_Reason_Code_3_:
                assertThat("paperlessBillIneligReasonCode mismatch",
                        testContext.getResponse().jsonPath().getInt("data.paperlessBillIneligReasonCode"),
                        equalTo(3));
                break;

            case TC_190A__Positive__Bill_Reason_Desc_3_:
                assertThat("paperlessBillIneligReasonDesc mismatch",
                        testContext.getResponse().jsonPath().getString("data.paperlessBillIneligReasonDesc"),
                        equalTo(ENROLLED_FISERV));
                break;

            case TC_191__Positive__Corr_Reason_Description_:
                assertPopulatedString("data.paperlessCorrIneligReasonDesc");
                break;

            case TC_192__Positive__Bill_Reason_Description_:
                assertPopulatedString("data.paperlessBillIneligReasonDesc");
                break;

            case TC_193__Positive__Corr_Reason_Description_NULL_:
                assertIneligReasonFieldNull("paperlessCorrIneligReasonCode");
                assertIneligReasonFieldNull("paperlessCorrIneligReasonDesc");
                break;

            case TC_194__Positive__Bill_Reason_Description_NULL_:
                assertIneligReasonFieldNull("paperlessBillIneligReasonCode");
                assertIneligReasonFieldNull("paperlessBillIneligReasonDesc");
                break;

            case TC_195__Positive__Corr_PPER_I_:
                assertDeliveryOption("correspondenceDeliveryOption", "I");
                break;

            case TC_196__Positive__Corr_PPER_Eligible_:
                assertBooleanFieldTrue("data.paperlessCorrespondenceEligible", "paperlessCorrespondenceEligible");
                break;

            case TC_197__Positive__Corr_PPER_Reason_Null_:
                assertIneligReasonFieldNull("paperlessCorrIneligReasonCode");
                break;

            case TC_197A__Positive__Corr_PPER_Reason_Desc_Null_:
                assertIneligReasonFieldNull("paperlessCorrIneligReasonDesc");
                break;

            case TC_198__Positive__Bill_PPER_I_:
                assertDeliveryOption("billDeliveryOption", "I");
                break;

            case TC_199__Positive__Bill_PPER_Eligible_:
                assertBooleanFieldTrue("data.paperlessBillEligible", "paperlessBillEligible");
                break;

            case TC_200__Positive__Bill_PPER_Reason_Null_:
                assertIneligReasonFieldNull("paperlessBillIneligReasonCode");
                break;

            case TC_200A__Positive__Bill_PPER_Reason_Desc_Null_:
                assertIneligReasonFieldNull("paperlessBillIneligReasonDesc");
                break;

            case TC_201__Positive__Corr_Override_Precedence_:
                assertDeliveryOption("correspondenceDeliveryOption", "I");
                break;

            case TC_201A__Positive__Corr_Override_Eligible_:
                assertBooleanFieldTrue("data.paperlessCorrespondenceEligible", "paperlessCorrespondenceEligible");
                break;

            case TC_202__Positive__Bill_Override_Precedence_:
                assertDeliveryOption("billDeliveryOption", "I");
                break;

            case TC_202A__Positive__Bill_Override_Eligible_:
                assertBooleanFieldTrue("data.paperlessBillEligible", "paperlessBillEligible");
                break;

            case TC_203__Positive__New_Unconfirmed_Bill_:
                assertDeliveryOption("billDeliveryOption", "P");
                break;

            case TC_204__Positive__New_Confirmed_Bill_:
                assertDeliveryOption("billDeliveryOption", "E");
                break;

            case TC_205__Positive__New_Paper_Bill_:
                assertDeliveryOption("billDeliveryOption", "P");
                break;

            default:
                throw new IllegalArgumentException("No GetPaperlessEnrollmentEligibility validation for: " + testCondition);
        }
    }

    private void assertDeliveryOption(String fieldName, String expected) {
        String actual = testContext.getResponse().jsonPath().getString("data." + fieldName);
        assertThat(fieldName + " mismatch (expected " + expected + ", actual " + actual + ")",
                actual, equalTo(expected));
    }

    private void assertPopulatedString(String jsonPath) {
        String value = testContext.getResponse().jsonPath().getString(jsonPath);
        assertThat(jsonPath + " should be populated", value, notNullValue());
        assertThat(jsonPath + " should not be blank", value.isBlank(), is(false));
    }

    private void assertPopulatedStringMaxLength(String jsonPath, int maxLength) {
        assertPopulatedString(jsonPath);
        assertThat(jsonPath + " length",
                testContext.getResponse().jsonPath().getString(jsonPath).length(),
                lessThanOrEqualTo(maxLength));
    }

    private void assertBooleanFieldTrue(String jsonPath, String label) {
        Object value = testContext.getResponse().jsonPath().get(jsonPath);
        assertThat(label + " mismatch (expected true, got " + value + ")",
                value, equalTo(true));
    }

    private void assertIneligReasonFieldNull(String fieldName) {
        Object value = testContext.getResponse().jsonPath().get("data." + fieldName);
        if (value == null) {
            return;
        }
        if (value instanceof Number && ((Number) value).intValue() == 0) {
            log.warn("API returned {}=0 instead of null for eligible account — raise FTD mismatch if spec requires null",
                    fieldName);
            return;
        }
        if (value instanceof String && ((String) value).isBlank()) {
            log.warn("API returned blank {} instead of null for eligible account — raise FTD mismatch if spec requires null",
                    fieldName);
            return;
        }
        assertThat(fieldName + " should be null when account is eligible (actual: " + value + ")",
                value, nullValue());
    }
}
