package com.gng.api.pages.turnOn.ServiceOrdersPages.GetDefaultPlansAndOffersPage;
import com.gng.api.constants.GlobalEnums;
import com.gng.api.pojo.TestContext.TestContext;
import lombok.extern.slf4j.Slf4j;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.ServiceOrdersPojo.GetDefaultPlansAndOffers.GetDefaultPlansAndOffersRequest;
import com.gng.api.steps.turnOn.GetDefaultPlansAndOffers.GetDefaultPlansAndOffersApiLabel;

import static com.gng.api.steps.turnOn.GetDefaultPlansAndOffers.GetDefaultPlansAndOffersApiLabel.GET_DEFAULT_PLANS_AND_OFFERS_TC_154;

@Slf4j
public class GetDefaultPlansAndOffersHelper {

    private final TestContext testContext;
    private final String acnLogin;

    public GetDefaultPlansAndOffersHelper(TestContext testContext) {
        this.testContext = testContext;
        acnLogin = "acncsr";
    }

    GetDefaultPlansAndOffersRequest preparePayload(GetDefaultPlansAndOffersApiLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        String jsonFileName = apiLabel.equals(GetDefaultPlansAndOffersApiLabel.get_default_plans_and_offers)
                ? GetDefaultPlansAndOffersApiLabel.get_default_plans_and_offers.toString()
                : GetDefaultPlansAndOffersApiLabel.get_default_plans_and_offers_mandatory.toString();
        return BasePage.deserializeJsonToPojo(jsonFileName, GetDefaultPlansAndOffersRequest.class);
    }

    public void setRequestParams(GetDefaultPlansAndOffersRequest payload, GlobalEnums.CustomerType customerType, String promotionCode, GlobalEnums.EnrollmentSource enrollmentSource,
                                 GetDefaultPlansAndOffersApiLabel testCondition) {
        payload.setCustomerType(customerType.getValue());
        GlobalEnums.PromotionCode parsedPromotionCode = null;

        if (promotionCode != null && !promotionCode.equalsIgnoreCase("<promotionCode>") && !promotionCode.trim().isEmpty()) {
            parsedPromotionCode = GlobalEnums.PromotionCode.valueOf(promotionCode);
        }
        if (parsedPromotionCode != null) {
            payload.setMarketingPromotionCode(parsedPromotionCode.getValue());
        }
        payload.setEnrollmentSource(enrollmentSource.getValue());
        if (testCondition.equals(GET_DEFAULT_PLANS_AND_OFFERS_TC_154))
        {
            payload.setLoginID(acnLogin);
        }
    }
}
