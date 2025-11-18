package com.gng.api.steps.meterSet.ServiceOrdersSteps.GetDefaultPlansAndOffers;

import com.gng.api.pages.meterSet.ServiceOrdersPages.GetDefaultPlansAndOffersPage.GetDefaultPlansAndOffersApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.java.en.When;

import static com.gng.api.steps.meterSet.ServiceOrdersSteps.GetDefaultPlansAndOffers.GetDefaultPlansAndOffersApiLabel.get_default_plans_and_offers;

public class GetDefaultPlansAndOffersApiSteps {

    private final GetDefaultPlansAndOffersApiPage getDefaultPlansAndOffersApiPage;

    public GetDefaultPlansAndOffersApiSteps(TestContext testContext, GetDefaultPlansAndOffersApiPage getDefaultPlansAndOffersApiPage) {
        this.getDefaultPlansAndOffersApiPage = getDefaultPlansAndOffersApiPage;
        testContext.setGetDefaultPlansAndOffersApiPage(getDefaultPlansAndOffersApiPage);
    }

    @When("a request is made to the GetDefaultPlansAndOffers Api MeterSet with an invalid params for {string}")
    public void a_request_is_made_to_the_GetDefaultPlansAndOffers_Api_with_invalid_RequestId_condition(String testCondition) {
        getDefaultPlansAndOffersApiPage.validateNegativeCases(get_default_plans_and_offers, GetDefaultPlansAndOffersApiLabel.valueOf(testCondition));
    }
}
