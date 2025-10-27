package com.gng.api.steps.meterSet.ServiceOrdersSteps.GetEligiblePlansAndOffers;

import com.gng.api.pages.meterSet.ServiceOrdersPages.GetEligiblePlansAndOffersPage.GetEligiblePlansAndOffersApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.java.en.When;

import static com.gng.api.steps.meterSet.ServiceOrdersSteps.GetEligiblePlansAndOffers.GetEligiblePlansAndOffersApiLabel.get_eligible_plans_and_offers;

public class GetEligiblePlansAndOffersApiSteps {

    private final TestContext testContext;
    private final GetEligiblePlansAndOffersApiPage getEligiblePlansAndOffersApiPage;

    public GetEligiblePlansAndOffersApiSteps(TestContext testContext, GetEligiblePlansAndOffersApiPage getEligiblePlansAndOffersApiPage) {
        this.testContext = testContext;
        this.getEligiblePlansAndOffersApiPage = getEligiblePlansAndOffersApiPage;
    }


    @When("a request is made to the GetEligiblePlansAndOffers Api meterSet for {string} condition")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_MeterSet_for_condition(String testCondition) {
        getEligiblePlansAndOffersApiPage.validateNegativeTestConditions(get_eligible_plans_and_offers, GetEligiblePlansAndOffersApiLabel.valueOf(testCondition));
    }
}
