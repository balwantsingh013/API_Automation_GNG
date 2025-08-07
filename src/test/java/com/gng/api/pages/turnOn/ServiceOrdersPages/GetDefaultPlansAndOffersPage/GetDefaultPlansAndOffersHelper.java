package com.gng.api.pages.turnOn.ServiceOrdersPages.GetDefaultPlansAndOffersPage;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pojo.ServiceOrdersPojo.GetDefaultPlansAndOffers.GetDefaultPlansAndOffersResponse;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.response.GetEligiblePlansAndOffersResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.ServiceOrdersPojo.GetDefaultPlansAndOffers.GetDefaultPlansAndOffersRequest;
import com.gng.api.steps.turnOn.GetDefaultPlansAndOffers.GetDefaultPlansAndOffersApiLabel;

import java.util.List;

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
    public List<GetDefaultPlansAndOffersResponse.Plan> getValidationDefaultPlansAndOffers() {
        String controlNum = ApplicationContext.get().getDbAction().getControlNumber();
        Object rawResult = ApplicationContext.get().getDbAction().getValidationPlansAndOffers(controlNum);

        return convertObjectToPojo(rawResult, new TypeReference<>() {});
    }

    public static <T> T convertObjectToPojo(Object source, TypeReference<T> typeRef) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            return mapper.convertValue(source, typeRef);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Failed to map object: " + e.getMessage(), e);
        }
    }
}
