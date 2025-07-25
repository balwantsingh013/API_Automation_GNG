package com.gng.api.pages.turnOn.ServiceOrdersPages.GetDefaultPlansAndOffersPage;
import com.gng.api.constants.GlobalEnums;
import com.gng.api.pojo.TestContext.TestContext;
import lombok.extern.slf4j.Slf4j;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.ServiceOrdersPojo.GetDefaultPlansAndOffers.GetDefaultPlansAndOffersRequest;
import com.gng.api.steps.turnOn.GetDefaultPlansAndOffers.GetEligiblePlansAndOffersApiLabel;

import static com.gng.api.steps.turnOn.GetDefaultPlansAndOffers.GetEligiblePlansAndOffersApiLabel.GET_DEFAULT_PLANS_AND_OFFERS_TC_154;

@Slf4j
public class GetDefaultPlansAndOffersHelper {

    private final TestContext testContext;

    public GetDefaultPlansAndOffersHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    GetDefaultPlansAndOffersRequest preparePayload(GetEligiblePlansAndOffersApiLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        String jsonFileName = apiLabel.equals(GetEligiblePlansAndOffersApiLabel.get_default_plans_and_offers)
                ? GetEligiblePlansAndOffersApiLabel.get_default_plans_and_offers.toString()
                : GetEligiblePlansAndOffersApiLabel.get_default_plans_and_offers_mandatory.toString();
        return BasePage.deserializeJsonToPojo(jsonFileName, GetDefaultPlansAndOffersRequest.class);
    }

    public void setCustomerType(GetDefaultPlansAndOffersRequest payload, GlobalEnums.CustomerType customerType){
       payload.setCustomerType(customerType.getValue());
    }

    public void setPromotionCode(GetDefaultPlansAndOffersRequest payload, GlobalEnums.PromotionCode promotionCode){
        payload.setMarketingPromotionCode(promotionCode.getValue());
    }

    public void setEnrollmentSource(GetDefaultPlansAndOffersRequest payload, GlobalEnums.EnrollmentSource enrollmentSource){
        payload.setEnrollmentSource(enrollmentSource.getValue());
    }

    public void setRequestParams(GetDefaultPlansAndOffersRequest payload, GlobalEnums.CustomerType customerType, GlobalEnums.PromotionCode promotionCode, GlobalEnums.EnrollmentSource enrollmentSource,
                                 GetEligiblePlansAndOffersApiLabel testCondition) {

        String acnLogin = "acncsr";

       // setCustomerType(payload, customerType);
        payload.setCustomerType(customerType.getValue());

        if (promotionCode != null) {
           // setPromotionCode(payload, promotionCode);
            payload.setMarketingPromotionCode(promotionCode.getValue());

        }
        //setEnrollmentSource(payload, enrollmentSource);
        payload.setEnrollmentSource(enrollmentSource.getValue());

        if (testCondition.equals(GET_DEFAULT_PLANS_AND_OFFERS_TC_154))
        {
            payload.setLoginID(acnLogin);
        }
    }
}
