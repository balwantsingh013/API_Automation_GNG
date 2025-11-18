package com.gng.api.steps.meterSet.ServiceOrdersSteps.GetPrepayPlansRequote;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.pages.meterSet.ServiceOrdersPages.GetPrepayPlansRequotePage.GetPrepayPlansRequoteApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.meterSet.ServiceOrdersSteps.GetPrepayPlansRequote.GetPrepayPlansRequoteApiLabel;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static com.gng.api.steps.meterSet.ServiceOrdersSteps.GetPrepayPlansRequote.GetPrepayPlansRequoteApiLabel.get_prepay_plans_requote;

public class GetPrepayPlansRequoteApiSteps {
    private final TestContext testContext;
    private final GetPrepayPlansRequoteApiPage getPrepayPlansRequoteApiPage;


    public GetPrepayPlansRequoteApiSteps(TestContext testContext, GetPrepayPlansRequoteApiPage getPrepayPlansRequoteApiPage) {
        this.testContext = testContext;
        this.getPrepayPlansRequoteApiPage = getPrepayPlansRequoteApiPage;
    }

    @When("a request is made to the GetPrepayPlansRequote Api meterSet for {string} condition")
    public void callPrepayPlansRequote(String testCondition) {
        getPrepayPlansRequoteApiPage.verifyPrepayPlanRequote(get_prepay_plans_requote, GetPrepayPlansRequoteApiLabel.valueOf(testCondition));
    }

    @When("a request is made to the GetPrepayPlansRequote Api meterSet for {string} negative condition")
    public void a_request_is_made_to_the_GetPrepayPlansRequote_Api_negative_condition(String testCondition) {
        getPrepayPlansRequoteApiPage.validateNegativeTestCases(get_prepay_plans_requote, GetPrepayPlansRequoteApiLabel.valueOf(testCondition));
    }
    @Then("the response should contain the expected meterSet prepay plans and {string} planCode")
    public void verifyPrepayResponsePlans(String planCode) {
        getPrepayPlansRequoteApiPage.verifyResponsePlans(GlobalEnums.PlanCode.valueOf(planCode));
    }
}
