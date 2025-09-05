package com.gng.api.steps.turnOn.ServiceOrdersSteps.GetPrepayPlanRequote;
import com.gng.api.constants.GlobalEnums;
import com.gng.api.pages.turnOn.ServiceOrdersPages.GetPrepayPlanRequotePage.GetPrepayPlansRequoteApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class GetPrepayPlansRequoteApiSteps {
    private final TestContext testContext;
    private final GetPrepayPlansRequoteApiPage getPrepayPlansRequoteApiPage;


    public GetPrepayPlansRequoteApiSteps(TestContext testContext, GetPrepayPlansRequoteApiPage getPrepayPlansRequoteApiPage) {
        this.testContext = testContext;
        this.getPrepayPlansRequoteApiPage = getPrepayPlansRequoteApiPage;
    }

    @When("a request is made to the GetPrepayPlansRequote Api for {string} condition")
    public void callPrepayPlansRequote(String testCondition) {
        getPrepayPlansRequoteApiPage.verifyPrepayPlanRequote(GetPrepayPlansRequoteApiLabel.get_prepay_plans_requote, GetPrepayPlansRequoteApiLabel.valueOf(testCondition));
    }

    @Then("the response should contain the expected prepay plans and {string} planCode")
    public void verifyPrepayResponsePlans(String planCode) {
        getPrepayPlansRequoteApiPage.verifyResponsePlans(GlobalEnums.PlanCode.valueOf(planCode));
    }
}
