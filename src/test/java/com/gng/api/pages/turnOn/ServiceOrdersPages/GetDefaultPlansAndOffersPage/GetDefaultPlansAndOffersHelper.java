package com.gng.api.pages.turnOn.ServiceOrdersPages.GetDefaultPlansAndOffersPage;
import com.gng.api.pojo.TestContext.TestContext;
import lombok.extern.slf4j.Slf4j;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.ServiceOrdersPojo.GetDefaultPlansAndOffers.GetDefaultPlansAndOffersRequest;
import com.gng.api.steps.turnOn.GetDefaultPlansAndOffers.GetDefaultPlansAndOffersApiLabel;

import static com.gng.api.steps.turnOn.GetDefaultPlansAndOffers.GetDefaultPlansAndOffersApiLabel.GET_DEFAULT_PLANS_AND_OFFERS_TC_154;

@Slf4j
public class GetDefaultPlansAndOffersHelper {

    private final TestContext testContext;

    public GetDefaultPlansAndOffersHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    GetDefaultPlansAndOffersRequest preparePayload(GetDefaultPlansAndOffersApiLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        String jsonFileName = apiLabel.equals(GetDefaultPlansAndOffersApiLabel.get_default_plans_and_offers)
                ? GetDefaultPlansAndOffersApiLabel.get_default_plans_and_offers.toString()
                : GetDefaultPlansAndOffersApiLabel.get_default_plans_and_offers_mandatory.toString();
        return BasePage.deserializeJsonToPojo(jsonFileName, GetDefaultPlansAndOffersRequest.class);
    }

    public void setAccountType(GetDefaultPlansAndOffersRequest payload, GetDefaultPlansAndOffersApiLabel accountType){
        switch (accountType){
            case RESIDENTIAL:
                payload.setCustomerType("RS");
                break;
            case COMMERCIAL:
                payload.setCustomerType("CM");
                break;
            case INVALID:
                payload.setCustomerType("IN");
                break;
        }
    }

    public void setPromotionCode(GetDefaultPlansAndOffersRequest payload, GetDefaultPlansAndOffersApiLabel promotionCode){
        switch (promotionCode){
            case DEALS:
                payload.setMarketingPromotionCode("DEALS");
                break;
            case SAVE100:
                payload.setMarketingPromotionCode("SAVE100");
                break;
            case AAA:
                payload.setMarketingPromotionCode("AAA");
                break;
        }
    }

    public void setEnrollmentSource(GetDefaultPlansAndOffersRequest payload, GetDefaultPlansAndOffersApiLabel enrollmentSource){

        switch (enrollmentSource){
            case ALL_CONNECT:
                payload.setEnrollmentSource("ALLCONNECT");
                break;
            case CIM_BUILDER_TURN_ON:
                payload.setEnrollmentSource("CIM BUILDER TURN ON");
                break;
            case CORRESPONDENCE:
                payload.setEnrollmentSource("CORRESPONDENCE");
                break;
            case EMAIL:
                payload.setEnrollmentSource("EMAIL");
                break;
            case ENERGY_SHOP:
                payload.setEnrollmentSource("ENERGYSHOP");
                break;
            case FAX:
                payload.setEnrollmentSource("FAX");
                break;
            case GEORGIA_GAS_SAVINGS:
                payload.setEnrollmentSource("GEORGIAGASSAVINGS");
                break;
            case GNG_HUB:
                payload.setEnrollmentSource("GNGHUB");
                break;
            case MAIL:
                payload.setEnrollmentSource("MAIL");
                break;
            case MOOVE_GURU:
                payload.setEnrollmentSource("MOOVEGURU");
                break;
            case ONE_SOURCE:
                payload.setEnrollmentSource("ONESOURCE");
                break;
            case PHONE_CALL:
                payload.setEnrollmentSource("PHONE CALL");
                break;
            case VIV_INT:
                payload.setEnrollmentSource("VIVINT");
                break;
            case WEB:
                payload.setEnrollmentSource("WEB");
                break;
        }
    }

    public void setRequestParams(GetDefaultPlansAndOffersRequest payload, GetDefaultPlansAndOffersApiLabel accountType, GetDefaultPlansAndOffersApiLabel promotionCode, GetDefaultPlansAndOffersApiLabel enrollmentSource,
                                 GetDefaultPlansAndOffersApiLabel testCondition) {

        setAccountType(payload, accountType);
        if (promotionCode != null) {
            setPromotionCode(payload, promotionCode);
        }
        setEnrollmentSource(payload, enrollmentSource);

        if (testCondition.equals(GET_DEFAULT_PLANS_AND_OFFERS_TC_154))
        {
            payload.setLoginID("acncsr");
        }
//        switch (testCondition) {
//            case GET_DEFAULT_PLANS_AND_OFFERS_TC_122:
//                break;
//            case GET_DEFAULT_PLANS_AND_OFFERS_TC_154:
//                payload.setLoginID("acncsr");
//                break;
//            default:
//        }
    }
}
