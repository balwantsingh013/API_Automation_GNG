package com.gng.api.steps.turnOn.GetDefaultPlansAndOffers;
import com.gng.api.constants.CustomerType;
import com.gng.api.constants.EnrollmentSource;
import com.gng.api.constants.PromotionCode;
import com.gng.api.pages.turnOn.ServiceOrdersPages.GetDefaultPlansAndOffersPage.GetDefaultPlansAndOffersApiPage;
import com.gng.api.pojo.ServiceOrdersPojo.GetDefaultPlansAndOffers.GetDefaultPlansAndOffersResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.pojo.shared.PlansAndOffers;
import io.cucumber.java.en.When;
import java.io.IOException;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Then;
import java.io.InputStream;
import java.util.List;
import static com.gng.api.steps.turnOn.GetDefaultPlansAndOffers.GetEligiblePlansAndOffersApiLabel.get_default_plans_and_offers;
import static org.testng.AssertJUnit.*;

public class GetDefaultPlansAndOffersApiSteps {
    private Map<String, PlansAndOffers.DefaultPlanData> expectedPlansMap;
    private final TestContext testContext;
    private final GetDefaultPlansAndOffersApiPage getDefaultPlansAndOffersApiPage;

    public GetDefaultPlansAndOffersApiSteps(TestContext testContext, GetDefaultPlansAndOffersApiPage getDefaultPlansAndOffersApiPage){
        this.testContext = testContext;
        this.getDefaultPlansAndOffersApiPage = getDefaultPlansAndOffersApiPage;
        testContext.setGetDefaultPlansAndOffersApiPage(getDefaultPlansAndOffersApiPage);
    }

    public void loadEligiblePlans() {
        //customerData for request
        if (expectedPlansMap == null) {
            try {
                //api plan results
                ObjectMapper mapper = new ObjectMapper();
                InputStream is = getClass().getClassLoader().getResourceAsStream("testDataFiles/DefaultPlans.json");
                this.expectedPlansMap = mapper.readValue(is, mapper.getTypeFactory().constructMapType(Map.class, String.class, PlansAndOffers.DefaultPlanData.class));

            } catch (IOException e) {
                throw new RuntimeException("Failed to load plans to validate data", e);
            }
        }
    }

    @When("a request is made to the GetDefaultPlansAndOffers Api with {string} customer type {string} promotion code {string} enrollment source {string} condition")
    public void PositiveDefaultPlansAndOffersApi(
            String customerType,
            String promotionCode,
            String enrollmentSource,
            String testCondition) {

        EnrollmentSource.valueOf(enrollmentSource);
        GetEligiblePlansAndOffersApiLabel conditionLabel = GetEligiblePlansAndOffersApiLabel.valueOf(testCondition);

        PromotionCode promo = null;
        if (promotionCode != null && !promotionCode.equalsIgnoreCase("<promotionCode>") && !promotionCode.trim().isEmpty()) {
            promo = PromotionCode.valueOf(promotionCode);
        }

        getDefaultPlansAndOffersApiPage.setRequestParams(
                get_default_plans_and_offers,
                CustomerType.valueOf(customerType),
                promo,
                EnrollmentSource.valueOf(enrollmentSource),
                conditionLabel
        );
    }

    @Then("the response should contain the expected plans for {string} condition")
    public void verifyResponsePlans(String testCondition) {
        loadEligiblePlans();
        PlansAndOffers.DefaultPlanData expected = expectedPlansMap.get(testCondition);
        assertNotNull("No expected plans found for test condition: " + testCondition, expected);
        List<GetDefaultPlansAndOffersResponse.Plan> actualPlans =  testContext.getGetDefaultPlansAndOffersResponse().getData().getPlans();

        assertEquals("Mismatch in number of plans", expected.getNumberOfMatches(), actualPlans.size());

        for (GetDefaultPlansAndOffersResponse.Plan expectedPlan : expected.getPlans()) {
            boolean found = actualPlans.stream().anyMatch(actual ->
                    expectedPlan.getPlanCode().equals(actual.getPlanCode()) &&
                    expectedPlan.getPlanDescription().equals(actual.getPlanDescription()) &&
                    expectedPlan.getPromotion1Code().equals(actual.getPromotion1Code()) &&
                    expectedPlan.getPromotion1Description().equals(actual.getPromotion1Description()
                    )
            );
            assertTrue("Expected plan not found: " + expectedPlan.getPlanCode(), found);
        }
    }
}

