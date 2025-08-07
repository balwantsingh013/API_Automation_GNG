package com.gng.api.steps.turnOn.ServiceOrdersSteps.GetPrepayPlanRequote;
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

    @When("a request is made to the GetPrepayPlansRequote Api with {string} pricePlan for {string} condition")
    public void callPrepayPlansRequote(String pricePlan, String testCondition) {
        getPrepayPlansRequoteApiPage.verifyPrepayPlanRequote(GetPrepayPlansRequoteApiLabel.get_prepay_plans_requote, GetPrepayPlansRequoteApiLabel.valueOf(testCondition));
    }

    @Then("the response should contain the expected prepay plans")
    public void verifyPrepayResponsePlans() {
        getPrepayPlansRequoteApiPage.verifyResponsePlans();
    }
}
