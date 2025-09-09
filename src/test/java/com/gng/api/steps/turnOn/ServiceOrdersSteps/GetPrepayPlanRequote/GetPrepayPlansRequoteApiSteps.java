package com.gng.api.steps.turnOn.ServiceOrdersSteps.GetPrepayPlanRequote;
import com.gng.api.constants.GlobalEnums;
import com.gng.api.pages.turnOn.ServiceOrdersPages.GetPrepayPlanRequotePage.GetPrepayPlansRequoteApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static com.gng.api.steps.turnOn.ServiceOrdersSteps.GetPrepayPlanRequote.GetPrepayPlansRequoteApiLabel.get_prepay_plans_requote;

public class GetPrepayPlansRequoteApiSteps {
    private final TestContext testContext;
    private final GetPrepayPlansRequoteApiPage getPrepayPlansRequoteApiPage;


    public GetPrepayPlansRequoteApiSteps(TestContext testContext, GetPrepayPlansRequoteApiPage getPrepayPlansRequoteApiPage) {
        this.testContext = testContext;
        this.getPrepayPlansRequoteApiPage = getPrepayPlansRequoteApiPage;
    }

    @When("a request is made to the GetPrepayPlansRequote Api for {string} condition")
    public void callPrepayPlansRequote(String testCondition) {
        getPrepayPlansRequoteApiPage.verifyPrepayPlanRequote(get_prepay_plans_requote, GetPrepayPlansRequoteApiLabel.valueOf(testCondition));
    }

    @Then("the response should contain the expected prepay plans and {string} planCode")
    public void verifyPrepayResponsePlans(String planCode) {
        getPrepayPlansRequoteApiPage.verifyResponsePlans(GlobalEnums.PlanCode.valueOf(planCode));
    }

    @When("a request is made to the GetPrepayPlansRequote Api with an invalid requestID for {string} condition")
    public void a_request_is_made_to_the_GetPrepayPlansRequote_Api_with_invalid_RequestId_condition(String testCondition) {
        getPrepayPlansRequoteApiPage.validateInvalidRequestIDCases(get_prepay_plans_requote, GetPrepayPlansRequoteApiLabel.valueOf(testCondition));
    }
    @When("a request is made to the GetPrepayPlansRequote Api with an invalid loginID for {string} condition")
    public void a_request_is_made_to_the_GetPrepayPlansRequote_Api_with_invalid_LoginId_condition(String testCondition) {
        getPrepayPlansRequoteApiPage.validateInvalidLoginIDCases(get_prepay_plans_requote, GetPrepayPlansRequoteApiLabel.valueOf(testCondition));
    }
    @When("a request is made to the GetPrepayPlansRequote Api for {string} negative condition")
    public void a_request_is_made_to_the_GetPrepayPlansRequote_Api_negative_condition(String testCondition) {
        getPrepayPlansRequoteApiPage.validateNegativeTestCases(get_prepay_plans_requote, GetPrepayPlansRequoteApiLabel.valueOf(testCondition));
    }
    @When("a request is made to the GetPrepayPlansRequote Api with complete flow for negative {string} condition")
    public void callPrepayPlansRequoteNegative(String testCondition) {
        getPrepayPlansRequoteApiPage.validateNegativePrepayPlanRequoteCompleteFlow(get_prepay_plans_requote, GetPrepayPlansRequoteApiLabel.valueOf(testCondition));
    }

}
