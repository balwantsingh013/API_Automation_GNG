package com.gng.api.pages.turnOn.ServiceOrdersPages.GetDefaultPlansAndOffersPage;
import com.gng.api.pojo.TestContext.TestContext;
import lombok.extern.slf4j.Slf4j;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.ServiceOrdersPojo.GetDefaultPlansAndOffers.GetDefaultPlansAndOffersRequest;
import com.gng.api.steps.turnOn.GetDefaultPlansAndOffers.GetEligiblePlansAndOffersApiLabel;
import com.gng.api.constants.CustomerType;
import com.gng.api.constants.EnrollmentSource;
import com.gng.api.constants.PromotionCode;

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

    public void setAccountType(GetDefaultPlansAndOffersRequest payload, CustomerType customerType){
        switch (customerType){
            case RESIDENTIAL:
                payload.setCustomerType(CustomerType.RESIDENTIAL.getValue());
                break;
            case COMMERCIAL:
                payload.setCustomerType(CustomerType.COMMERCIAL.getValue());
                break;
            case INVALID:
                payload.setCustomerType(CustomerType.INVALID.getValue());
                break;
        }
    }

    public void setPromotionCode(GetDefaultPlansAndOffersRequest payload, PromotionCode promotionCode){
        switch (promotionCode){
            case DEALS:
                payload.setMarketingPromotionCode(PromotionCode.DEALS.getValue());
                break;
            case SAVE100:
                payload.setMarketingPromotionCode(PromotionCode.SAVE100.getValue());
                break;
            case AAA:
                payload.setMarketingPromotionCode(PromotionCode.AAA.getValue());
                break;
        }
    }

    public void setEnrollmentSource(GetDefaultPlansAndOffersRequest payload, EnrollmentSource enrollmentSource){

        switch (enrollmentSource) {
            case ALL_CONNECT:
                payload.setEnrollmentSource(EnrollmentSource.ALL_CONNECT.getValue());
                break;
            case CIM_BUILDER_TURN_ON:
                payload.setEnrollmentSource(EnrollmentSource.CIM_BUILDER_TURN_ON.getValue());
                break;
            case CORRESPONDENCE:
                payload.setEnrollmentSource(EnrollmentSource.CORRESPONDENCE.getValue());
                break;
            case EMAIL:
                payload.setEnrollmentSource(EnrollmentSource.EMAIL.getValue());
                break;
            case ENERGY_SHOP:
                payload.setEnrollmentSource(EnrollmentSource.ENERGY_SHOP.getValue());
                break;
            case FAX:
                payload.setEnrollmentSource(EnrollmentSource.FAX.getValue());
                break;
            case GEORGIA_GAS_SAVINGS:
                payload.setEnrollmentSource(EnrollmentSource.GEORGIA_GAS_SAVINGS.getValue());
                break;
            case GNG_HUB:
                payload.setEnrollmentSource(EnrollmentSource.GNG_HUB.getValue());
                break;
            case MAIL:
                payload.setEnrollmentSource(EnrollmentSource.MAIL.getValue());
                break;
            case MOOVE_GURU:
                payload.setEnrollmentSource(EnrollmentSource.MOOVE_GURU.getValue());
                break;
            case ONE_SOURCE:
                payload.setEnrollmentSource(EnrollmentSource.ONE_SOURCE.getValue());
                break;
            case PHONE_CALL:
                payload.setEnrollmentSource(EnrollmentSource.PHONE_CALL.getValue());
                break;
            case VIV_INT:
                payload.setEnrollmentSource(EnrollmentSource.VIV_INT.getValue());
                break;
            case WEB:
                payload.setEnrollmentSource(EnrollmentSource.WEB.getValue());
                break;
        }
    }

    public void setRequestParams(GetDefaultPlansAndOffersRequest payload, CustomerType customerType, PromotionCode promotionCode, EnrollmentSource enrollmentSource,
                                 GetEligiblePlansAndOffersApiLabel testCondition) {

        String acnLogin = "acncsr";

        setAccountType(payload, customerType);
        if (promotionCode != null) {
            setPromotionCode(payload, promotionCode);
        }
        setEnrollmentSource(payload, enrollmentSource);

        if (testCondition.equals(GET_DEFAULT_PLANS_AND_OFFERS_TC_154))
        {
            payload.setLoginID(acnLogin);
        }
    }
}
