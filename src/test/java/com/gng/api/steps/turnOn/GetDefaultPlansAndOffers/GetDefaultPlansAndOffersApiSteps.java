package com.gng.api.steps.turnOn.GetDefaultPlansAndOffers;
import com.gng.api.constants.GlobalEnums;
import com.gng.api.pages.turnOn.ServiceOrdersPages.GetDefaultPlansAndOffersPage.GetDefaultPlansAndOffersApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.pojo.shared.PlansAndOffers;
import io.cucumber.java.en.When;
import java.util.*;

import static com.gng.api.steps.turnOn.GetDefaultPlansAndOffers.GetDefaultPlansAndOffersApiLabel.get_default_plans_and_offers;

public class GetDefaultPlansAndOffersApiSteps {
    private Map<String, PlansAndOffers.DefaultPlanData> expectedPlansMap;
    private final TestContext testContext;
    private final GetDefaultPlansAndOffersApiPage getDefaultPlansAndOffersApiPage;

    public GetDefaultPlansAndOffersApiSteps(TestContext testContext, GetDefaultPlansAndOffersApiPage getDefaultPlansAndOffersApiPage){
        this.testContext = testContext;
        this.getDefaultPlansAndOffersApiPage = getDefaultPlansAndOffersApiPage;
        testContext.setGetDefaultPlansAndOffersApiPage(getDefaultPlansAndOffersApiPage);
    }

    @When("a request is made to the GetDefaultPlansAndOffers Api with {string} customer type {string} promotion code {string} enrollment source {string} condition")
    public void PositiveDefaultPlansAndOffersApi(
            String customerType,
            String promotionCode,
            String enrollmentSource,
            String testCondition) {

        GlobalEnums.EnrollmentSource.valueOf(enrollmentSource);
        GetDefaultPlansAndOffersApiLabel conditionLabel = GetDefaultPlansAndOffersApiLabel.valueOf(testCondition);

        GlobalEnums.PromotionCode promo = null;
        if (promotionCode != null && !promotionCode.equalsIgnoreCase("<promotionCode>") && !promotionCode.trim().isEmpty()) {
            promo = GlobalEnums.PromotionCode.valueOf(promotionCode);
        }
        getDefaultPlansAndOffersApiPage.setRequestParams(
                get_default_plans_and_offers,
                GlobalEnums.CustomerType.valueOf(customerType),
                promo,
                GlobalEnums.EnrollmentSource.valueOf(enrollmentSource),
                conditionLabel
        );
    }
}

