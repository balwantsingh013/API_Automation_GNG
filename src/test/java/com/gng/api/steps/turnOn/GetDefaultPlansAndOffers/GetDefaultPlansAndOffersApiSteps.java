package com.gng.api.steps.turnOn.GetDefaultPlansAndOffers;
import com.gng.api.pages.turnOn.ServiceOrdersPages.GetDefaultPlansAndOffers.GetDefaultPlansAndOffersApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.turnOn.ServiceOrdersSteps.GetEligiblePlansAndOffers.GetEligiblePlansAndOffersApiLabel;
import io.cucumber.java.en.When;
import static com.gng.api.steps.turnOn.GetDefaultPlansAndOffers.GetDefaultPlansAndOffersApiLabel.get_default_plans_and_offers;

public class GetDefaultPlansAndOffersApiSteps {

    private final TestContext testContext;
    private final GetDefaultPlansAndOffersApiPage getDefaultPlansAndOffersApiPage;

    public GetDefaultPlansAndOffersApiSteps(TestContext testContext, GetDefaultPlansAndOffersApiPage getDefaultPlansAndOffersApiPage) {
        this.testContext = testContext;
        this.getDefaultPlansAndOffersApiPage = getDefaultPlansAndOffersApiPage;
        testContext.setGetEligiblePlansAndOffersApiPage(getDefaultPlansAndOffersApiPage);
    }

    @When("a request is made to the GetDefaultPlansAndOffers Api with {string} account type {string} promotion code {string} condition")
    public void aRequestIsMadeToTheGetDefaultPlansAndOffersApiWithAccountTypePromotionCodeCondition(String accountType, String promotionCode, String testCondition) {
        getDefaultPlansAndOffersApiPage.setRequestParams(get_default_plans_and_offers, accountType, GetDefaultPlansAndOffersApiLabel.valueOf(promotionCode), GetDefaultPlansAndOffersApiLabel.valueOf(testCondition));
    }
}
