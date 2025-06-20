package com.gng.api.steps.turnOff.Common.GetReasonsForLeaving;

import com.gng.api.pages.turnOff.CommonPages.GetReasonsForLeavingPage.GetReasonsForLeavingApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.When;

import static com.gng.api.steps.turnOff.Common.GetReasonsForLeaving.GetReasonsForLeavingLabel.get_reasons_for_leaving;

public class GetReasonsForLeavingApiSteps {

    private final TestContext testContext;
    private final GetReasonsForLeavingApiPage reasonsForLeavingApiPage;

    @ParameterType("true|false")
    public Boolean booleanValueOf(String value) {
        return Boolean.valueOf(value);
    }

    public GetReasonsForLeavingApiSteps(TestContext testContext, GetReasonsForLeavingApiPage reasonsForLeavingApiPage) {
        this.testContext = testContext;
        this.reasonsForLeavingApiPage = reasonsForLeavingApiPage;
        testContext.setGetReasonsForLeavingApiPage(reasonsForLeavingApiPage);
    }

    @When("a request is made to the GetReasonsForLeaving Api with Valid parameters and etcExists flag is {string}")
    public void verify_GetReasons_For_Leaving_With_ETC_Exists(String etcExistsValue) {
        reasonsForLeavingApiPage.requestToGetReasonsForLeavingWithETCFlag(get_reasons_for_leaving, booleanValueOf(etcExistsValue));
    }
}
