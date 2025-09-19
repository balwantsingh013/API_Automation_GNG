package com.gng.api.pages.turnOn.ServiceOrdersPages.GetDefaultPlansAndOffersPage;
import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pojo.ServiceOrdersPojo.GetDefaultPlansAndOffers.GetDefaultPlansAndOffersResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.ServiceOrdersPojo.GetDefaultPlansAndOffers.GetDefaultPlansAndOffersRequest;
import com.gng.api.steps.turnOn.ServiceOrdersSteps.GetDefaultPlansAndOffers.GetDefaultPlansAndOffersApiLabel;
import org.testng.Assert;
import java.util.List;
import java.util.Map;
import static com.gng.api.constants.GlobalEnums.CustomerType.COMMERCIAL;
import static com.gng.api.constants.GlobalEnums.CustomerType.RESIDENTIAL;
import static com.gng.api.constants.GlobalEnums.EnrollmentSource.MAIL;
import static com.gng.api.steps.turnOn.ServiceOrdersSteps.GetDefaultPlansAndOffers.GetDefaultPlansAndOffersApiLabel.GET_DEFAULT_PLANS_AND_OFFERS_TC_154;

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
        payload.setRequestID(FakerDataGenerator.generateString(10));
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

    public void setRequestIDBasedOnTestCondition(GetDefaultPlansAndOffersRequest payload, GetDefaultPlansAndOffersApiLabel testCondition) {
        payload.setTransactionType(GlobalEnums.TransactionType.TURN_ON.getValue());
        switch (testCondition) {
            case GET_DEFAULT_PLANS_AND_OFFERS_MISSING_REQUEST_ID_TC_129:
                payload.setRequestID(null);
                break;
            case GET_DEFAULT_PLANS_AND_OFFERS_INVALID_REQUEST_ID_TC_130:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(33));
                break;
            case GET_DEFAULT_PLANS_AND_OFFERS_DUPLICATE_REQUEST_ID_TC_128:
                payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());
                break;
            default:
                payload.setRequestID(FakerDataGenerator.generateString(10));
        }
    }

    public void setLoginIDBasedOnTestCondition(GetDefaultPlansAndOffersRequest payload, GetDefaultPlansAndOffersApiLabel testCondition) {
        payload.setTransactionType(GlobalEnums.TransactionType.TURN_ON.getValue());
        payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
        switch (testCondition) {
            case GET_DEFAULT_PLANS_AND_OFFERS_MISSING_LOGIN_ID_TC_131:
                payload.setLoginID(null);
                break;

            case GET_DEFAULT_PLANS_AND_OFFERS_INVALID_LOGIN_ID_TC_132:
                payload.setLoginID(FakerDataGenerator.generateAlphanumeric(31));
                break;

            case GET_DEFAULT_PLANS_AND_OFFERS_NON_ALPHANUMERIC_ID_TC_133:
                payload.setLoginID(FakerDataGenerator.generateString(8));
                break;
        }
    }


    public void setCustomerTypeBasedOnTestCondition(GetDefaultPlansAndOffersRequest payload, GetDefaultPlansAndOffersApiLabel testCondition) {
        payload.setTransactionType(GlobalEnums.TransactionType.TURN_ON.getValue());
        payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
        switch (testCondition) {
            case GET_DEFAULT_PLANS_AND_OFFERS_MISSING_CUSTOMER_TYPE_ID_TC_134:
                payload.setCustomerType(null);
                break;

            case GET_DEFAULT_PLANS_AND_OFFERS_INVALID_LENGTH_CUSTOMER_TYPE_TC_135:
                payload.setCustomerType(FakerDataGenerator.generateUpperCaseString(3));
                break;

            case GET_DEFAULT_PLANS_AND_OFFERS_INVALID_CUSTOMER_TYPE_TC_136:
                payload.setCustomerType(GlobalEnums.InvalidValues.INVALID_CUSTOMER_TYPE.getValue());
                break;

        }
    }

    public void setTransactionTypeBasedOnTestCondition(GetDefaultPlansAndOffersRequest payload, GetDefaultPlansAndOffersApiLabel testCondition) {
        payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
        switch (testCondition) {
            case GET_DEFAULT_PLANS_AND_OFFERS_MISSING_TRANSACTION_TYPE_TC_137:
                payload.setTransactionType(null);
                break;

            case GET_DEFAULT_PLANS_AND_OFFERS_INVALID_LENGTH_TRANSACTION_TYPE_TC_138:
                payload.setTransactionType(FakerDataGenerator.generateUpperCaseString(5));
                break;

            case GET_DEFAULT_PLANS_AND_OFFERS_INVALID_TRANSACTION_TYPE_TC_139:
                payload.setTransactionType(FakerDataGenerator.generateUpperCaseString(4));
                break;

        }
    }

        public void setEnrollmentSourceBasedOnTestCondition(GetDefaultPlansAndOffersRequest payload, GetDefaultPlansAndOffersApiLabel testCondition) {
        payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
        switch (testCondition) {
            case GET_DEFAULT_PLANS_AND_OFFERS_MISSING_ENROLLMENT_SOURCE_TC_140:
                payload.setEnrollmentSource("");
                break;

            case GET_DEFAULT_PLANS_AND_OFFERS_INVALID_LENGTH_ENROLLMENT_SOURCE_TC_141:
                payload.setEnrollmentSource(FakerDataGenerator.generateUpperCaseString(36));
                break;

            case GET_DEFAULT_PLANS_AND_OFFERS_INVALID_ENROLLMENT_SOURCE_TC_142:
                payload.setEnrollmentSource(FakerDataGenerator.generateUpperCaseString(5));
                break;
        }
    }

    public void setPromotionCodeBasedOnTestCondition(GetDefaultPlansAndOffersRequest payload, GetDefaultPlansAndOffersApiLabel testCondition) {
        payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
        switch (testCondition) {
            case INVALID_MARKETING_PROMOTION_CODE_TC_143:
                payload.setMarketingPromotionCode(FakerDataGenerator.generateAlphanumeric(46));
                break;

            case INVALID_PROMOTION_CODE_FOR_ALLCONNECT_TC_144_1:
                payload.setMarketingPromotionCode(GlobalEnums.PromotionCode.AAA.getValue());
                payload.setEnrollmentSource(GlobalEnums.EnrollmentSource.ALLCONNECT.getValue());
                break;

            case INVALID_PROMOTION_CODE_FOR_ENERGYSHOP_TC_144_2:
                payload.setMarketingPromotionCode(GlobalEnums.PromotionCode.AAA.getValue());
                payload.setEnrollmentSource(GlobalEnums.EnrollmentSource.ENERGYSHOP.getValue());
                break;

            case INVALID_PROMOTION_CODE_FOR_GEORGIAGASSAVINGS_TC_144_3:
                payload.setMarketingPromotionCode(GlobalEnums.PromotionCode.AAA.getValue());
                payload.setEnrollmentSource(GlobalEnums.EnrollmentSource.GEORGIAGASSAVINGS.getValue());
                break;

            case INVALID_PROMOTION_CODE_FOR_ONESOURCE_TC_144_4:
                payload.setMarketingPromotionCode(GlobalEnums.PromotionCode.AAA.getValue());
                payload.setEnrollmentSource(GlobalEnums.EnrollmentSource.ONESOURCE.getValue());
                break;

            case INVALID_PROMOTION_CODE_FOR_TC_145:
                payload.setMarketingPromotionCode(FakerDataGenerator.generateString(4));
                payload.setEnrollmentSource(MAIL.getValue());
                break;

            case INVALID_PROMOTION_CODE_FOR_CM_CUSTOMER_TC_146:
                payload.setCustomerType(COMMERCIAL.getValue());
                payload.setEnrollmentSource(MAIL.getValue());
                payload.setMarketingPromotionCode(GlobalEnums.MarketingPromotionCodes.PROMOTION_CODE_RESIDENTIAL.getValue());
                break;

            case INVALID_PROMOTION_CODE_FOR_NEW_CUSTOMER_TC_147:
                payload.setCustomerType(COMMERCIAL.getValue());
                payload.setEnrollmentSource(MAIL.getValue());
                payload.setMarketingPromotionCode(GlobalEnums.MarketingPromotionCodes.PROMOTION_CODE_FOR_EXISTING_CUSTOMER.getValue());
                break;

            case EXPIRED_PROMOTION_CODE_TC_148:
                payload.setCustomerType(COMMERCIAL.getValue());
                payload.setEnrollmentSource(MAIL.getValue());
                payload.setMarketingPromotionCode(GlobalEnums.MarketingPromotionCodes.EXPIRED_MARKETING_PROMOTION_CODE.getValue());
                break;

            case INVALID_PROMOTION_CODE_FOR_RS_CUSTOMER_TC_149:
                payload.setCustomerType(RESIDENTIAL.getValue());
                payload.setEnrollmentSource(MAIL.getValue());
                payload.setMarketingPromotionCode(GlobalEnums.MarketingPromotionCodes.PROMOTION_CODE_COMMERCIAL.getValue());
                break;

            case INVALID_PROMOTION_CODE_FOR_CHANNEL_SOURCE_TC_150:
                payload.setCustomerType(RESIDENTIAL.getValue());
                payload.setEnrollmentSource(MAIL.getValue());
                payload.setMarketingPromotionCode(GlobalEnums.MarketingPromotionCodes.PROMOTION_CODE_GREEN_LIFE.getValue());
                break;

        }
    }
    public void verifyResidentialDefaultPlansReceivedAgainstDatabase() {
        Map<String, Object> controlNumberResult = ApplicationContext.get().getDbAction().getControlNumber();
        String controlNum = controlNumberResult.get("UZTCOTT_CONTROL_NUM").toString();
        List<Map<String, Object>> eligiblePlansList = ApplicationContext.get().getDbAction().getValidationPlansAndOffers(controlNum);
        GetDefaultPlansAndOffersResponse response = testContext.getGetDefaultPlansAndOffersResponse();

        for (Map<String, Object> eligiblePlan : eligiblePlansList) {
            comparePlanFields(eligiblePlan, response.getData().getPlans());
        }
    }

    public static void comparePlanFields(Map<String, Object> eligiblePlan, List<GetDefaultPlansAndOffersResponse.Plan> plans) {
        String dbPlanCode = String.valueOf(eligiblePlan.get("planCode")).trim();
        String dbPlanDescription = String.valueOf(eligiblePlan.get("planDescription")).trim();
        String dbPromo1Code = normalize(eligiblePlan.get("promotion1Code"));
        String dbPromo1Desc = normalize(eligiblePlan.get("promotion1Description"));
        boolean matchFound = false;

        for (GetDefaultPlansAndOffersResponse.Plan plan : plans) {
            if (plan.getPlanCode() != null && plan.getPlanCode().trim().equals(dbPlanCode)) {
                matchFound = true;

                String apiPlanCode = plan.getPlanCode().trim();
                String apiPlanDescription = normalize(plan.getPlanDescription());
                String apiPromo1Code = normalize(plan.getPromotion1Code());
                String apiPromo1Desc = normalize(plan.getPromotion1Description());

                Assert.assertEquals(apiPlanCode, dbPlanCode, "Plan code mismatch");
                Assert.assertEquals(apiPlanDescription, dbPlanDescription, "Plan description mismatch for planCode: " + dbPlanCode);
                Assert.assertEquals(apiPromo1Code, dbPromo1Code, "Promotion1 code mismatch for planCode: " + dbPlanCode);
                Assert.assertEquals(apiPromo1Desc, dbPromo1Desc, "Promotion1 description mismatch for planCode: " + dbPlanCode);
                break;
            }
        }
        Assert.assertTrue(matchFound, "No matching planCode found in API response for: " + dbPlanCode);
    }

    private static String normalize(Object value) {
        return value == null ? "" : value.toString().trim();
    }
}
