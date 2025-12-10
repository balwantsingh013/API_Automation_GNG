package com.gng.api.steps.marketerSwitch.ServiceOrdersSteps.GetDefaultPlansAndOffers;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.pages.marketerSwitch.ServiceOrdersPages.GetDefaultPlansAndOffersPage.GetDefaultPlansAndOffersApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static com.gng.api.steps.marketerSwitch.ServiceOrdersSteps.GetDefaultPlansAndOffers.GetDefaultPlansAndOffersApiLabel.get_default_plans_and_offers;

public class GetDefaultPlansAndOffersApiSteps {
    private final GetDefaultPlansAndOffersApiPage getDefaultPlansAndOffersApiPage;

    public GetDefaultPlansAndOffersApiSteps(TestContext testContext, GetDefaultPlansAndOffersApiPage getDefaultPlansAndOffersApiPage) {
        this.getDefaultPlansAndOffersApiPage = getDefaultPlansAndOffersApiPage;
        testContext.setGetDefaultPlansAndOffersApiPage(getDefaultPlansAndOffersApiPage);
    }


    @When("a request is made to the GetDefaultPlansAndOffers Api marketer switch with an invalid params for {string}")
    public void a_request_is_made_to_the_GetDefaultPlansAndOffers_Api_marketer_switch_with_invalid_params_condition(String testCondition) {
        getDefaultPlansAndOffersApiPage.validateNegativeConditions(get_default_plans_and_offers, GetDefaultPlansAndOffersApiLabel.valueOf(testCondition));

    }

//    @Then("the response should contain the expected default plans")
//    public void verifyEligibleResponsePlans() {
//        getDefaultPlansAndOffersApiPage.verifyResponsePlans();
//    }


}

