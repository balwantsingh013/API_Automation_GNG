package com.gng.api.steps.turnOn.GetDefaultPlansAndOffers;
import com.gng.api.pages.turnOn.ServiceOrdersPages.GetDefaultPlansAndOffersPage.GetDefaultPlansAndOffersApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.java.en.When;
import static com.gng.api.steps.turnOn.GetDefaultPlansAndOffers.GetDefaultPlansAndOffersApiLabel.get_default_plans_and_offers;

public class GetDefaultPlansAndOffersApiSteps {

    private final TestContext testContext;
    private final GetDefaultPlansAndOffersApiPage getDefaultPlansAndOffersApiPage;

    public GetDefaultPlansAndOffersApiSteps(TestContext testContext, GetDefaultPlansAndOffersApiPage getDefaultPlansAndOffersApiPage) {
        this.testContext = testContext;
        this.getDefaultPlansAndOffersApiPage = getDefaultPlansAndOffersApiPage;
        testContext.setGetDefaultPlansAndOffersApiPage(getDefaultPlansAndOffersApiPage);
    }


    @When("a request is made to the GetDefaultPlansAndOffers Api with {string} account type {string} promotion code {string} enrollment source {string} condition")
    public void aRequestIsMadeToTheGetDefaultPlansAndOffersApiWithAccountTypePromotionCodeCondition(
            String accountType,
            String promotionCode,
            String enrollmentSource,
            String testCondition) {

        GetDefaultPlansAndOffersApiLabel promoLabel = null;
        if (promotionCode != null && !promotionCode.equalsIgnoreCase("<promotionCode>") && !promotionCode.trim().isEmpty()) {
            promoLabel = GetDefaultPlansAndOffersApiLabel.valueOf(promotionCode);
        }

        GetDefaultPlansAndOffersApiLabel conditionLabel = GetDefaultPlansAndOffersApiLabel.valueOf(testCondition);

        getDefaultPlansAndOffersApiPage.setRequestParams(
                get_default_plans_and_offers,
                accountType,
                promoLabel,
                GetDefaultPlansAndOffersApiLabel.valueOf(enrollmentSource),
                conditionLabel
        );
    }
}

