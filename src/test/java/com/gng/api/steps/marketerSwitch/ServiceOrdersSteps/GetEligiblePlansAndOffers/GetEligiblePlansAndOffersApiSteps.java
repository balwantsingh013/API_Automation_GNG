package com.gng.api.steps.marketerSwitch.ServiceOrdersSteps.GetEligiblePlansAndOffers;

import com.gng.api.pages.marketerSwitch.ServiceOrdersPages.GetEligiblePlansAndOffersPage.GetEligiblePlansAndOffersApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static com.gng.api.steps.marketerSwitch.ServiceOrdersSteps.GetEligiblePlansAndOffers.GetEligiblePlansAndOffersApiLabel.get_eligible_plans_and_offers;
import static com.gng.api.steps.marketerSwitch.ServiceOrdersSteps.GetEligiblePlansAndOffers.GetEligiblePlansAndOffersApiLabel.valueOf;

public class GetEligiblePlansAndOffersApiSteps {

    private final TestContext testContext;
    private final GetEligiblePlansAndOffersApiPage getEligiblePlansAndOffersApiPage;

    public GetEligiblePlansAndOffersApiSteps(TestContext testContext, GetEligiblePlansAndOffersApiPage getEligiblePlansAndOffersApiPage) {
        this.testContext = testContext;
        this.getEligiblePlansAndOffersApiPage = getEligiblePlansAndOffersApiPage;
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api for marketer switch from SearchAccounts response for external cases for {string} condition")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_Negative_Marketer_Switch_External_for_condition(String testCondition) {
        getEligiblePlansAndOffersApiPage.seedNegativeExternalTestConditions(get_eligible_plans_and_offers, GetEligiblePlansAndOffersApiLabel.valueOf(testCondition));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api from customer file marketerSwitch to seed data for {string} condition")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_Customer_file_MarketerSwitch_Seed_data_for_condition(String testCondition) {
        getEligiblePlansAndOffersApiPage.seedCustomerFileSourceTestConditions(get_eligible_plans_and_offers, GetEligiblePlansAndOffersApiLabel.valueOf(testCondition));
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api negative marketerSwitch for {string} condition")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_Negative_Marketer_Switch_for_condition(String testCondition) {
        getEligiblePlansAndOffersApiPage.validateNegativeTestConditions(get_eligible_plans_and_offers, GetEligiblePlansAndOffersApiLabel.valueOf(testCondition));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api marketerSwitch for {string} condition")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_Positive_Market_Switch_for_condition(String testCondition) {
        getEligiblePlansAndOffersApiPage.validatePositiveTestConditions(get_eligible_plans_and_offers, GetEligiblePlansAndOffersApiLabel.valueOf(testCondition));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api second call marketerSwitch for {string} condition")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_Second_Call_Positive_Market_Switch_for_condition(String testCondition) {
        getEligiblePlansAndOffersApiPage.validatePositiveSecondCallTestConditions(get_eligible_plans_and_offers, GetEligiblePlansAndOffersApiLabel.valueOf(testCondition));
    }

    @Then("the marketer switch response should contain the expected plans")
    public void verifyMeterSetEligibleResponsePlans() {
        getEligiblePlansAndOffersApiPage.verifyResponsePlans();
    }

    @And("verify the marketer switch response does not contain disallowed plans for {string} condition")
    public void verifyDisallowedPlans(String testCondition){
        getEligiblePlansAndOffersApiPage.verifyResponseDoesNotContainPlans(valueOf(testCondition));
    }

    @Then("verify the marketer switch enrollment record for {string} condition")
    public void verifyEnrollmentRecord(String testCondition){
        getEligiblePlansAndOffersApiPage.verifyEnrollmentRecord(valueOf(testCondition));

    }

}
