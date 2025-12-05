package com.gng.api.steps.marketerSwitch.ServiceOrdersSteps.GetPrepayPlanRequote;
import com.gng.api.constants.GlobalEnums;
import com.gng.api.pages.marketerSwitch.ServiceOrdersPages.GetPrepayPlanRequotePage.GetPrepayPlansRequoteApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static com.gng.api.steps.marketerSwitch.ServiceOrdersSteps.GetPrepayPlanRequote.GetPrepayPlansRequoteApiLabel.get_prepay_plans_requote;

public class GetPrepayPlansRequoteApiSteps {
    private final TestContext testContext;
    private final GetPrepayPlansRequoteApiPage getPrepayPlansRequoteApiPage;


    public GetPrepayPlansRequoteApiSteps(TestContext testContext, GetPrepayPlansRequoteApiPage getPrepayPlansRequoteApiPage) {
        this.testContext = testContext;
        this.getPrepayPlansRequoteApiPage = getPrepayPlansRequoteApiPage;
    }

    @When("a request is made to the GetPrepayPlansRequote Api marketer switch with an invalid params for {string} condition")
    public void callPrepayPlansRequoteNegative(String testCondition) {
        getPrepayPlansRequoteApiPage.validateNegativePrepayPlanRequote(get_prepay_plans_requote, GetPrepayPlansRequoteApiLabel.valueOf(testCondition));
    }


}
