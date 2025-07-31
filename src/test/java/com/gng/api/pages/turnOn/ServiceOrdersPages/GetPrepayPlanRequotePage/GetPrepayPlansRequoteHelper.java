package com.gng.api.pages.turnOn.ServiceOrdersPages.GetPrepayPlanRequotePage;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;

import com.gng.api.pojo.ServiceOrdersPojo.GetDefaultPlansAndOffers.GetDefaultPlansAndOffersRequest;
import com.gng.api.pojo.ServiceOrdersPojo.GetPrepayPlansRequote.GetPrepayPlansRequoteRequest;
import com.gng.api.pojo.ServiceOrdersPojo.GetPrepayPlansRequote.GetPrepayPlansRequoteResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.turnOn.ServiceOrdersSteps.GetPrepayPlanRequote.GetPrepayPlansRequoteApiLabel;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Slf4j
public class GetPrepayPlansRequoteHelper {
    private final TestContext testContext;

    public GetPrepayPlansRequoteHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    GetPrepayPlansRequoteRequest preparePayload(GetPrepayPlansRequoteApiLabel apiLabel) {
       log.info("Preparing payload for {}", apiLabel);
       String jsonFileName = apiLabel.equals(GetPrepayPlansRequoteApiLabel.get_prepay_plans_requote)
               ? GetPrepayPlansRequoteApiLabel.get_prepay_plans_requote.toString()
               : GetPrepayPlansRequoteApiLabel.get_prepay_plans_requote_mandatory.toString();
       return BasePage.deserializeJsonToPojo(jsonFileName, GetPrepayPlansRequoteRequest.class);
   }

    public void setRequestParams(GetPrepayPlansRequoteRequest payload,
                                 GetPrepayPlansRequoteApiLabel apiLabel) {
        // loginID can come from TestConstant or test data
        //payload.setLoginID(TestConstant.LOGIN_ID);

        // Retrieve the prepay transaction ID from TestContext or DB
       // String tranId = (String) testContext.get("transactionID");
      //  payload.setTransactionID(tranId);

        // Set transaction type from enum name (e.g., TNON)
      //  payload.setTransactionType(GlobalEnums.TransactionType.TNON.name());

        // Save payload for later use (e.g., duplicate check)
       // testContext.put("prepayRequestPayload", payload);
    }

//    public GetPrepayPlansRequoteRequest getOriginalPayload() {
//        // Return the payload saved in context
//        return (GetPrepayPlansRequoteRequest) testContext.get("prepayRequestPayload");
//    }

    /**
     * Validate the database for prepay requote results.  For example,
     * confirm that UZRRCOT has a record for the returned requestID.
     */
//    public void validateRequoteInDb(GetPrepayPlansRequoteResponse response) {
//        // Example logic:
//        String requestID = response.getRequestID();
//        boolean recordExists = DBUtil.exists(
//                "SELECT 1 FROM UZRRCOT WHERE REQUEST_ID = ?", requestID);
//        assert recordExists : "Requote record missing in UZRRCOT table";
//    }
//    private final TestContext testContext;
//    private final String acnLogin;
//
//    public GetPrepayPlansRequoteHelper(TestContext testContext) {
//        this.testContext = testContext;
//        acnLogin = "acncsr";
//    }
//
//    GetPrepayPlansRequoteRequest preparePayload(GetPrepayPlansRequoteApiLabel apiLabel) {
//        log.info("Preparing payload for {}", apiLabel);
//        String jsonFileName = apiLabel.equals(GetPrepayPlansRequoteApiLabel.get_default_plans_and_offers)
//                ? GetPrepayPlansRequoteApiLabel.get_default_plans_and_offers.toString()
//                : GetPrepayPlansRequoteApiLabel.get_default_plans_and_offers_mandatory.toString();
//        return BasePage.deserializeJsonToPojo(jsonFileName, GetDefaultPlansAndOffersRequest.class);
//    }
//
//    public void setRequestParams(GetDefaultPlansAndOffersRequest payload, GlobalEnums.CustomerType customerType, String promotionCode, GlobalEnums.EnrollmentSource enrollmentSource,
//                                 GetPrepayPlansRequoteApiLabel testCondition) {
//        payload.setCustomerType(customerType.getValue());
//        GlobalEnums.PromotionCode parsedPromotionCode = null;
//
//        if (promotionCode != null && !promotionCode.equalsIgnoreCase("<promotionCode>") && !promotionCode.trim().isEmpty()) {
//            parsedPromotionCode = GlobalEnums.PromotionCode.valueOf(promotionCode);
//        }
//        if (parsedPromotionCode != null) {
//            payload.setMarketingPromotionCode(parsedPromotionCode.getValue());
//        }
//        payload.setEnrollmentSource(enrollmentSource.getValue());
//        if (testCondition.equals(GET_DEFAULT_PLANS_AND_OFFERS_TC_154))
//        {
//            payload.setLoginID(acnLogin);
//        }
//    }
//    public List<GetDefaultPlansAndOffersResponse.Plan> getValidationDefaultPlansAndOffers() {
//        String controlNum = ApplicationContext.get().getDbAction().getControlNumber();
//        Object rawResult = ApplicationContext.get().getDbAction().getValidationPlansAndOffers(controlNum);
//
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//
//        try {
//            return mapper.convertValue(
//                    rawResult,
//                    new TypeReference<>() {}
//            );
//        } catch (IllegalArgumentException e) {
//            throw new RuntimeException("Failed to convert rawResult to List<Plan>: " + e.getMessage(), e);
//        }
//    }

}
