package com.gng.api.steps.marketerSwitch.Common.GetMarketerCodes;

import com.gng.api.pages.marketerSwitch.CommonPages.GetMarkterCodes.GetMarketerCodesApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.java.en.When;

import static com.gng.api.steps.marketerSwitch.Common.GetMarketerCodes.GetMarketerCodesApiLabel.get_marketer_codes;
import static com.gng.api.steps.marketerSwitch.Common.GetMarketerCodes.GetMarketerCodesApiLabel.valueOf;

public class GetMarketerCodesApiSteps {

    private final TestContext testContext;
    private final GetMarketerCodesApiPage getMarketerCodesApiPage;

    public GetMarketerCodesApiSteps(TestContext testContext, GetMarketerCodesApiPage getMarketerCodesApiPage) {
        this.testContext = testContext;
        this.getMarketerCodesApiPage = getMarketerCodesApiPage;
    }


    @When("a request is made to the GetMarketerCodes Api negative marketerSwitch for {string} condition")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_Negative_Marketer_Switch_for_condition(String testCondition) {
        getMarketerCodesApiPage.validateNegativeTestConditions(get_marketer_codes, GetMarketerCodesApiLabel.valueOf(testCondition));
    }

    @When("a request is made to the GetMarketerCodes Api marketerSwitch for {string} condition")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_Positive_Market_Switch_for_condition(String testCondition) {
        getMarketerCodesApiPage.validatePositiveTestConditions(get_marketer_codes, GetMarketerCodesApiLabel.valueOf(testCondition));
    }





}
