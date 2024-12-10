package com.gng.api.steps.ServiceOrdersSteps.GetEligiblePlansAndOffers;


import com.gng.api.pages.ServiceOrdersPages.GetEligiblePlansAndOffersPage.GetEligiblePlansAndOffersApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.ServiceOrdersSteps.SaveEnrollment.SaveEnrollmentApiLabel;
import io.cucumber.java.en.When;

import static com.gng.api.steps.ServiceOrdersSteps.GetEligiblePlansAndOffers.GetEligiblePlansAndOffersApiLabel.*;
import static com.gng.api.steps.ServiceOrdersSteps.SaveEnrollment.SaveEnrollmentApiLabel.save_enrollment_mandatory;

public class GetEligiblePlansAndOffersApiSteps {

    private final TestContext testContext;
    private final GetEligiblePlansAndOffersApiPage getEligiblePlansAndOffersApiPage;

    public GetEligiblePlansAndOffersApiSteps(TestContext testContext, GetEligiblePlansAndOffersApiPage getEligiblePlansAndOffersApiPage ) {
        this.testContext = testContext;
        this.getEligiblePlansAndOffersApiPage = getEligiblePlansAndOffersApiPage;
        testContext.setGetEligiblePlansAndOffersApiPage(getEligiblePlansAndOffersApiPage);
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api")
    public void a_request_is_made_to_the_get_account_info_api() {
        getEligiblePlansAndOffersApiPage.sendGetEligiblePlansAndOffersRequest(get_eligible_plans_and_offers);
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with {string}")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with(String requestID)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidRequestIDCases(get_eligible_plans_and_offers_mandatory, GetEligiblePlansAndOffersApiLabel.valueOf(requestID));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with login {string} ID")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_login_ID (String loginID)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidLoginIDCases(get_eligible_plans_and_offers_mandatory, GetEligiblePlansAndOffersApiLabel.valueOf(loginID));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with  transaction {string} Type")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_transaction_type (String transactionType)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidTransactionTypeCases(get_eligible_plans_and_offers_mandatory, GetEligiblePlansAndOffersApiLabel.valueOf(transactionType));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with  customer {string} Type")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_with_customer(String customerTYPE)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidCustomerTypeCases(get_eligible_plans_and_offers_mandatory, GetEligiblePlansAndOffersApiLabel.valueOf(customerTYPE));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with enrollment {string} Sources")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_enrollment_sources (String enrollmentSources)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidEnrollmentSourcesCases(get_eligible_plans_and_offers_mandatory, GetEligiblePlansAndOffersApiLabel.valueOf(enrollmentSources));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with customer {string} LastName")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_customer_last_name (String customerLastName)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidEnrollmentSourcesCases(get_eligible_plans_and_offers_mandatory, GetEligiblePlansAndOffersApiLabel.valueOf(customerLastName));
    }

}
