package com.gng.api.steps.csi.GetAccountInfo;

import com.gng.api.pages.csi.GetAccountInfoPage.GetAccountInfoPage;
import com.gng.api.pages.csi.GetAccountInfoPage.GetAccountInfoPlanValidationHelper;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.util.CommonUtil;
import com.gng.api.util.TestContextHolder;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

import static com.gng.api.steps.csi.GetAccountInfo.GetAccountInfoLabel.csi_get_account_info;

@Slf4j
public class GetAccountInfoApiSteps {

    @Before
    public void setupToken() {
        CommonUtil.silentlyGenerateAuthToken(); // No Allure logging
    }

    private final TestContext testContext;
    private final GetAccountInfoPage getAccountInfoPage;

    public GetAccountInfoApiSteps(TestContext testContext, GetAccountInfoPage getAccountInfoPage) {
        this.testContext = testContext;
        this.getAccountInfoPage = getAccountInfoPage;
        testContext.setGetAccountInfoApiPage(getAccountInfoPage);
        // Set globally for utility access
        TestContextHolder.set(testContext);
    }

    @When("a request is made to GetAccountInfo Api for {string}")
    public void a_request_is_made_to_the_GetAccountInfo_Api_with_test_condition(String testCondition) {
        CommonUtil.logTestDescriptionToReports(testCondition);
        getAccountInfoPage.validateResponseForTestConditions(csi_get_account_info, GetAccountInfoLabel.valueOf(testCondition));
    }

    @And("validate GetAccountInfo price plan response for {string}")
    public void validateGetAccountInfoPricePlanResponseFor(String testCondition) {
        switch (GetAccountInfoLabel.valueOf(testCondition)) {
            case TC_1__Positive__Account_Info_Returned___Renewal_Indicator_Format____ ->
                    GetAccountInfoPlanValidationHelper.validateRenewalIndicatorFormat(testContext.getResponse());
            case TC_2__Positive__Account_Info_Returned___Renewal_Indicator_equals_Y____ ->
                    GetAccountInfoPlanValidationHelper.validateRenewalIndicatorValue(testContext.getResponse(), "Y");
            case TC_3__Positive__Account_Info_Returned___Renewal_Indicator_equals_N____ ->
                    GetAccountInfoPlanValidationHelper.validateRenewalIndicatorValue(testContext.getResponse(), "N");
            case TC_4__Positive__Account_Info_Returned___Renewal_Indicator_equals_Dash____ ->
                    GetAccountInfoPlanValidationHelper.validateRenewalIndicatorValue(testContext.getResponse(), "-");
            case TC_5__Positive__Account_Info_Returned___Guaranteed_Plan_Pricing_Model____ ->
                    GetAccountInfoPlanValidationHelper.validateGuaranteedPlanPricingModel(testContext.getResponse());
            case TC_6__Positive__Account_Info_Returned___Non__Guaranteed_Plan_Pricing_Model____ ->
                    GetAccountInfoPlanValidationHelper.validateNonGuaranteedPlanPricingModel(testContext.getResponse());
            case TC_7__Positive__Account_Info_Returned___Plan_Description_Format____ ->
                    GetAccountInfoPlanValidationHelper.validatePlanDescriptionFormat(testContext.getResponse());
            case TC_8__Positive__Account_Info_Returned___Plan_Description_Value____ ->
                    GetAccountInfoPlanValidationHelper.validatePlanDescriptionValue(testContext.getResponse());
            default -> throw new IllegalArgumentException("Unsupported price plan test condition: " + testCondition);
        }
    }
}
