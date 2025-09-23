package com.gng.api.steps.serviceTransfer.Common.GetReasonsForLeaving;
import com.gng.api.pages.serviceTransfer.CommonPages.GetReasonsForLeavingPage.GetReasonsForLeavingApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;

import static com.gng.api.steps.serviceTransfer.Common.GetReasonsForLeaving.GetReasonsForLeavingApiLabel.get_reasons_for_leaving;

public class GetReasonsForLeavingApiSteps {

    private final TestContext testContext;
    private final GetReasonsForLeavingApiPage getReasonsForLeavingApiPage;


    public GetReasonsForLeavingApiSteps(TestContext testContext,
                                        GetReasonsForLeavingApiPage getReasonsForLeavingApiPage) {
        this.testContext = testContext;
        this.getReasonsForLeavingApiPage = getReasonsForLeavingApiPage;
        testContext.setGetReasonsForLeavingApiPage(getReasonsForLeavingApiPage);
    }

    @When("a request is made to the GetReasonsForLeaving Api with invalid parameters for {string} condition")
    public void a_request_is_made_to_the_GetReasonsForLeaving_Api_with(String testCondition) {
        getReasonsForLeavingApiPage.validateInvalidRequestParameters(get_reasons_for_leaving, GetReasonsForLeavingApiLabel.valueOf(testCondition)
        );
    }

    @When("a request is made to the GetReasonsForLeaving Api with valid parameters for {string} condition")
    public void validRequestByTestCondition(String testCondition) {
       getReasonsForLeavingApiPage.validRequestByTestCondition(get_reasons_for_leaving, GetReasonsForLeavingApiLabel.valueOf(testCondition));

    }

    @And("the response should contain the {string} turnOffReason SubReason")
    public void verifyErrorCodeAndReasons(String turnOffReason) {
        getReasonsForLeavingApiPage.verifyGetReasonsForLeavingResponse(turnOffReason);
    }

}
