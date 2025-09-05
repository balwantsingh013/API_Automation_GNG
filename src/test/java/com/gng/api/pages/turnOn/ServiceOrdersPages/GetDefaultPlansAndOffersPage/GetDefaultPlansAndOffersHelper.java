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
