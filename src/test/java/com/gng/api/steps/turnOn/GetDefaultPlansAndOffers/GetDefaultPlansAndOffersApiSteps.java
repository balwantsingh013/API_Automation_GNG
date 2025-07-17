package com.gng.api.steps.turnOn.GetDefaultPlansAndOffers;
import com.gng.api.pages.turnOn.ServiceOrdersPages.GetDefaultPlansAndOffersPage.GetDefaultPlansAndOffersApiPage;
import com.gng.api.pojo.ServiceOrdersPojo.GetDefaultPlansAndOffers.GetDefaultPlansAndOffersResponse;
import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.java.en.When;

import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Then;
import java.io.InputStream;
import java.util.List;

import static com.gng.api.steps.turnOn.GetDefaultPlansAndOffers.GetDefaultPlansAndOffersApiLabel.get_default_plans_and_offers;
import static org.testng.AssertJUnit.*;


public class GetDefaultPlansAndOffersApiSteps {

    private final Map<String, PlanData> expectedPlansMap;
    private final TestContext testContext;
    private final GetDefaultPlansAndOffersApiPage getDefaultPlansAndOffersApiPage;

    public GetDefaultPlansAndOffersApiSteps(TestContext testContext, GetDefaultPlansAndOffersApiPage getDefaultPlansAndOffersApiPage){
        this.testContext = testContext;
        this.getDefaultPlansAndOffersApiPage = getDefaultPlansAndOffersApiPage;
        testContext.setGetDefaultPlansAndOffersApiPage(getDefaultPlansAndOffersApiPage);
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = getClass().getClassLoader().getResourceAsStream("testDataFiles/DefaultPlans.json");
        try {
            expectedPlansMap = mapper.readValue(is, mapper.getTypeFactory().constructMapType(Map.class, String.class, PlanData.class));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static class PlanData {
        public int numberOfMatches;
        public List<GetDefaultPlansAndOffersResponse.Plan> plans;
    }

    @When("a request is made to the GetDefaultPlansAndOffers Api with {string} account type {string} promotion code {string} enrollment source {string} condition")
    public void aRequestIsMadeToTheGetDefaultPlansAndOffersApiWithAccountTypePromotionCodeCondition(
            String accountType,
            String promotionCode,
            String enrollmentSource,
            String testCondition) {

        GetDefaultPlansAndOffersApiLabel promoLabel = null;
        if (promotionCode != null && !promotionCode.equalsIgnoreCase("<promotionCode>") && !promotionCode.trim().isEmpty()) {
            promoLabel = GetDefaultPlansAndOffersApiLabel.valueOf(promotionCode);
        }

        GetDefaultPlansAndOffersApiLabel conditionLabel = GetDefaultPlansAndOffersApiLabel.valueOf(testCondition);

        getDefaultPlansAndOffersApiPage.setRequestParams(
                get_default_plans_and_offers,
                accountType,
                promoLabel,
                GetDefaultPlansAndOffersApiLabel.valueOf(enrollmentSource),
                conditionLabel
        );
    }

    @Then("the response should contain the expected plans for {string}")
    public void theResponseShouldContainTheExpectedPlansFor(String testCondition) {
        PlanData expected = expectedPlansMap.get(testCondition);
        assertNotNull("No expected plans found for test condition: " + testCondition, expected);

        List<GetDefaultPlansAndOffersResponse.Plan> actualPlans = getPlansFromApiResponse();

        assertEquals("Mismatch in number of plans", expected.numberOfMatches, actualPlans.size());

        for (GetDefaultPlansAndOffersResponse.Plan expectedPlan : expected.plans) {
            boolean found = actualPlans.stream().anyMatch(actual ->
                    expectedPlan.getPlanCode().equals(actual.getPlanCode()) &&
                            expectedPlan.getPlanDescription().equals(actual.getPlanDescription()) &&
                            expectedPlan.getPromotion1Code().equals(actual.getPromotion1Code()) &&
                            expectedPlan.getPromotion1Description().equals(actual.getPromotion1Description())
            );
            assertTrue("Expected plan not found: " + expectedPlan.getPlanCode(), found);
        }
    }

    private List<GetDefaultPlansAndOffersResponse.Plan> getPlansFromApiResponse() {

        return testContext.getGetDefaultPlansAndOffersResponse().getData().getPlans();
    }


}

