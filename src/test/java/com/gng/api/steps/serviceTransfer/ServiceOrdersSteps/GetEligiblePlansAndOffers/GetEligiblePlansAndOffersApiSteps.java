package com.gng.api.steps.serviceTransfer.ServiceOrdersSteps.GetEligiblePlansAndOffers;
import com.gng.api.pages.serviceTransfer.ServiceOrdersPages.GetEligiblePlansAndOffersPage.GetEligiblePlansAndOffersApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.java.en.When;

import static com.gng.api.steps.serviceTransfer.ServiceOrdersSteps.GetEligiblePlansAndOffers.GetEligiblePlansAndOffersApiLabel.get_eligible_plans_and_offers;

public class GetEligiblePlansAndOffersApiSteps {

    private final TestContext testContext;
    private final GetEligiblePlansAndOffersApiPage getEligiblePlansAndOffersApiPage;

    public GetEligiblePlansAndOffersApiSteps(TestContext testContext, GetEligiblePlansAndOffersApiPage getEligiblePlansAndOffersApiPage) {
        this.testContext = testContext;
        this.getEligiblePlansAndOffersApiPage = getEligiblePlansAndOffersApiPage;
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api to seed data for serviceTransfer for {string} condition")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_Seed_Data_ServiceTransfer_for_condition(String testCondition) {
        getEligiblePlansAndOffersApiPage.seedDataNegativeTestConditions(get_eligible_plans_and_offers, GetEligiblePlansAndOffersApiLabel.valueOf(testCondition));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api serviceTransfer for {string} condition")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_ServiceTransfer_for_condition(String testCondition) {
        getEligiblePlansAndOffersApiPage.validateNegativeTestConditions(get_eligible_plans_and_offers, GetEligiblePlansAndOffersApiLabel.valueOf(testCondition));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api from SearchAccounts response for external cases for {string} condition")
    public void SendEligiblePlansAndOffersForExternalCasesFromSearchAccountsResponse(String testCondition) {
        getEligiblePlansAndOffersApiPage.sendGetEligiblePlansAndOffersForExternalCasesFromSearchAccountsResponse(GetEligiblePlansAndOffersApiLabel.get_eligible_plans_and_offers, GetEligiblePlansAndOffersApiLabel.valueOf(testCondition));
    }
}
