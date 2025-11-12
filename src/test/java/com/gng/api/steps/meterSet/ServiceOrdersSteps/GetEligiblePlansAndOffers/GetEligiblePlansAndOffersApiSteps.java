package com.gng.api.steps.meterSet.ServiceOrdersSteps.GetEligiblePlansAndOffers;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.pages.meterSet.ServiceOrdersPages.GetEligiblePlansAndOffersPage.GetEligiblePlansAndOffersApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static com.gng.api.steps.meterSet.ServiceOrdersSteps.GetEligiblePlansAndOffers.GetEligiblePlansAndOffersApiLabel.get_eligible_plans_and_offers;
import static com.gng.api.steps.meterSet.ServiceOrdersSteps.GetEligiblePlansAndOffers.GetEligiblePlansAndOffersApiLabel.valueOf;

public class GetEligiblePlansAndOffersApiSteps {

    private final TestContext testContext;
    private final GetEligiblePlansAndOffersApiPage getEligiblePlansAndOffersApiPage;

    public GetEligiblePlansAndOffersApiSteps(TestContext testContext, GetEligiblePlansAndOffersApiPage getEligiblePlansAndOffersApiPage) {
        this.testContext = testContext;
        this.getEligiblePlansAndOffersApiPage = getEligiblePlansAndOffersApiPage;
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api negative meterSet to seed data for {string} condition")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_Negative_MeterSet_Seed_data_for_condition(String testCondition) {
        getEligiblePlansAndOffersApiPage.seedNegativeTestConditions(get_eligible_plans_and_offers, GetEligiblePlansAndOffersApiLabel.valueOf(testCondition));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api from customer file meterSet to seed data for {string} condition")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_Customer_file_MeterSet_Seed_data_for_condition(String testCondition) {
        getEligiblePlansAndOffersApiPage.seedCustomerFileSourceTestConditions(get_eligible_plans_and_offers, GetEligiblePlansAndOffersApiLabel.valueOf(testCondition));
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api negative meterSet for {string} condition")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_Negative_MeterSet_for_condition(String testCondition) {
        getEligiblePlansAndOffersApiPage.validateNegativeTestConditions(get_eligible_plans_and_offers, GetEligiblePlansAndOffersApiLabel.valueOf(testCondition));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api meterSet for {string} condition")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_Positive_MeterSet_for_condition(String testCondition) {
        getEligiblePlansAndOffersApiPage.validatePositiveTestConditions(get_eligible_plans_and_offers, GetEligiblePlansAndOffersApiLabel.valueOf(testCondition));
    }

    @Then("the meter set response should contain the expected plans")
    public void verifyMeterSetEligibleResponsePlans() {
        getEligiblePlansAndOffersApiPage.verifyResponsePlans();
    }

    @And("verify the response does not contain disallowed plans for {string} condition")
    public void verifyDisallowedPlans(String testCondition){
        getEligiblePlansAndOffersApiPage.verifyResponseDoesNotContainPlans(valueOf(testCondition));
    }
}
