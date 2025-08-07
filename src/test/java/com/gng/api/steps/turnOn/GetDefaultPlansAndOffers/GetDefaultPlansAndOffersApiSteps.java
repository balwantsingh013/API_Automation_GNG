package com.gng.api.steps.turnOn.GetDefaultPlansAndOffers;
import com.gng.api.constants.GlobalEnums;
import com.gng.api.pages.turnOn.ServiceOrdersPages.GetDefaultPlansAndOffersPage.GetDefaultPlansAndOffersApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static com.gng.api.steps.BaseSteps.verifyResponsePlans;
import static com.gng.api.steps.turnOn.GetDefaultPlansAndOffers.GetDefaultPlansAndOffersApiLabel.get_default_plans_and_offers;

public class GetDefaultPlansAndOffersApiSteps {
    private final GetDefaultPlansAndOffersApiPage getDefaultPlansAndOffersApiPage;

    public GetDefaultPlansAndOffersApiSteps(TestContext testContext, GetDefaultPlansAndOffersApiPage getDefaultPlansAndOffersApiPage){
        this.getDefaultPlansAndOffersApiPage = getDefaultPlansAndOffersApiPage;
        testContext.setGetDefaultPlansAndOffersApiPage(getDefaultPlansAndOffersApiPage);
    }

    @When("a request is made to the GetDefaultPlansAndOffers Api with {string} customer type {string} promotion code {string} enrollment source {string} condition")
    public void PositiveDefaultPlansAndOffersApi(String customerType, String promotionCode, String enrollmentSource, String testCondition) {
        getDefaultPlansAndOffersApiPage.validatePositiveTestConditionsFromExcelData(get_default_plans_and_offers, GlobalEnums.CustomerType.valueOf(customerType), promotionCode, GlobalEnums.EnrollmentSource.valueOf(enrollmentSource), GetDefaultPlansAndOffersApiLabel.valueOf(testCondition)
        );
    }

    @Then("the response should contain the expected default plans")
    public void verifyEligibleResponsePlans() {
       // verifyResponsePlans(testContext.getGetValidationDefaultPlansAndOffersPlans(), testContext.getGetDefaultPlansAndOffersResponse().getData().getPlans())
        getDefaultPlansAndOffersApiPage.verifyResponsePlans();
    }
}

