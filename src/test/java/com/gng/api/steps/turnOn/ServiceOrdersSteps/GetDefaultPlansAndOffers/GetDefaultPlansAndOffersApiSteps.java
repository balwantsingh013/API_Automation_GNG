package com.gng.api.steps.turnOn.ServiceOrdersSteps.GetDefaultPlansAndOffers;
import com.gng.api.constants.GlobalEnums;
import com.gng.api.pages.turnOn.ServiceOrdersPages.GetDefaultPlansAndOffersPage.GetDefaultPlansAndOffersApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static com.gng.api.steps.turnOn.ServiceOrdersSteps.GetDefaultPlansAndOffers.GetDefaultPlansAndOffersApiLabel.get_default_plans_and_offers;

public class GetDefaultPlansAndOffersApiSteps {
    private final GetDefaultPlansAndOffersApiPage getDefaultPlansAndOffersApiPage;

    public GetDefaultPlansAndOffersApiSteps(TestContext testContext, GetDefaultPlansAndOffersApiPage getDefaultPlansAndOffersApiPage) {
        this.getDefaultPlansAndOffersApiPage = getDefaultPlansAndOffersApiPage;
        testContext.setGetDefaultPlansAndOffersApiPage(getDefaultPlansAndOffersApiPage);
    }

    @When("a request is made to the GetDefaultPlansAndOffers Api with {string} customer type {string} promotion code {string} enrollment source {string} condition")
    public void PositiveDefaultPlansAndOffersApi(String customerType, String promotionCode, String enrollmentSource, String testCondition) {
        getDefaultPlansAndOffersApiPage.validatePositiveTestConditionsFromExcelData(get_default_plans_and_offers, GlobalEnums.CustomerType.valueOf(customerType), promotionCode, GlobalEnums.EnrollmentSource.valueOf(enrollmentSource), GetDefaultPlansAndOffersApiLabel.valueOf(testCondition));
    }

    @Then("the response should contain the expected default plans")
    public void verifyEligibleResponsePlans() {
        getDefaultPlansAndOffersApiPage.verifyResponsePlans();
    }

    @When("a request is made to the GetDefaultPlansAndOffers Api with an invalid requestID for {string}")
    public void a_request_is_made_to_the_GetDefaultPlansAndOffers_Api_with_invalid_RequestId_condition(String testCondition) {
        getDefaultPlansAndOffersApiPage.validateInvalidRequestIDCases(get_default_plans_and_offers, GetDefaultPlansAndOffersApiLabel.valueOf(testCondition));
    }

    @When("a request is made to the GetDefaultPlansAndOffers Api with an invalid loginId for {string}")
    public void a_request_is_made_to_the_GetDefaultPlansAndOffers_Api_with_invalid_LoginId_condition(String testCondition) {
        getDefaultPlansAndOffersApiPage.validateInvalidLoginIDCases(get_default_plans_and_offers, GetDefaultPlansAndOffersApiLabel.valueOf(testCondition));
    }

    @When("a request is made to the GetDefaultPlansAndOffers Api with an invalid customerType for {string}")
    public void a_request_is_made_to_the_GetDefaultPlansAndOffers_Api_with_invalid_customerType_condition(String testCondition) {
        getDefaultPlansAndOffersApiPage.validateInvalidCustomerTypeCases(get_default_plans_and_offers, GetDefaultPlansAndOffersApiLabel.valueOf(testCondition));
    }

    @When("a request is made to the GetDefaultPlansAndOffers Api with an invalid transactionType for {string}")
    public void a_request_is_made_to_the_GetDefaultPlansAndOffers_Api_with_invalid_transactionType_condition(String testCondition) {
        getDefaultPlansAndOffersApiPage.validateInvalidTransactionTypeCases(get_default_plans_and_offers, GetDefaultPlansAndOffersApiLabel.valueOf(testCondition));
    }

    @When("a request is made to the GetDefaultPlansAndOffers Api with an invalid enrollmentSource for {string}")
    public void a_request_is_made_to_the_GetDefaultPlansAndOffers_Api_with_invalid_enrollmentSource_condition(String testCondition) {
        getDefaultPlansAndOffersApiPage.validateInvalidEnrollmentSourceCases(get_default_plans_and_offers, GetDefaultPlansAndOffersApiLabel.valueOf(testCondition));
    }

    @When("a request is made to the GetDefaultPlansAndOffers Api with an invalid promotionCode for {string}")
    public void a_request_is_made_to_the_GetDefaultPlansAndOffers_Api_with_invalid_promotionCode_condition(String testCondition) {
        getDefaultPlansAndOffersApiPage.validateInvalidPromotionCodeCases(get_default_plans_and_offers, GetDefaultPlansAndOffersApiLabel.valueOf(testCondition));
    }

}

