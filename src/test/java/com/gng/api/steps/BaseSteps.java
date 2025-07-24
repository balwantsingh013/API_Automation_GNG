package com.gng.api.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.response.Plans;
import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.testng.AssertJUnit.*;
import static org.hamcrest.Matchers.hasItem;

@Slf4j
public class BaseSteps {
    private final TestContext testContext;
    ObjectMapper mapper = new ObjectMapper();
    InputStream is = getClass().getClassLoader().getResourceAsStream("testDataFiles/EligiblePlansAndOffers.json");
    private final Map<String, PlanData> expectedPlansMap;

    {
        try {
            expectedPlansMap = mapper.readValue(is, mapper.getTypeFactory().constructMapType(Map.class, String.class, PlanData.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @ParameterType("true|false")
    public Boolean booleanVal(String value) {
        return Boolean.valueOf(value);
    }

    public BaseSteps(TestContext testContext) {
        this.testContext = testContext;
    }

    public static class PlanData {
        public int numberOfMatches;
        public List<Plans> plans;
    }

    @Then("verify response code of {string} Api is {int}")
    public void verify_response_code_of_api_is(String apiName, Integer statusCode) {
        verifyResponseCode(apiName, statusCode);
    }

    @And("response should have ErrorCode {int} and ErrorMessage {string}")
    public void responseShouldHaveErrorCodeAndErrorMessage(int errorCode, String errorMessage) {
        verifyErrorCodeAndMessage(errorCode, errorMessage);
    }

    @Then("the response should contain the expected plans for {string} condition")
    public void verifyResponsePlans(String testCondition) {
        PlanData expected = expectedPlansMap.get(testCondition);
        assertNotNull("No expected plans found for test condition: " + testCondition, expected);
        List<Plans> actualPlans =  testContext.getGetEligiblePlansAndOffersResponse().getData().getPlans();

        assertEquals("Mismatch in number of plans", expected.numberOfMatches, actualPlans.size());

        for (Plans expectedPlan : expected.plans) {
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

    @And("response should return numberOfMatches as {int}")
    public void responseShouldReturnNumberOfMatchesAs(int numberOfMatches) {
        verifyNumberOfMatches(numberOfMatches);
    }

    @And("response should have {string} to {string}")
    public void responseShouldHavePaymentFieldsAs(String field, String value){
        verifyPaymentFields(field,value);
    }

    @And("response should have {string} as {string}")
    public void enrollmentStateresponseShouldHaveFieldAs(String field, String value){
        verifyFieldInResponse(field, value);
    }

    @And("response should have {string} flag as {string}")
    public void responseShouldHaveFlagAs(String flag, String value){
        verifyFlagValueInResponse(flag, booleanVal(value));
    }

    @And("response should have {int} turnOffReasons")
    public void responseShouldHaveTurnOffReasons(int count) {
        verifyNumberOfTurnOffReasons(count);
    }

    @And("the response should contain the following turnOffReasons:")
    public void verifyTurnOffReasons(DataTable dataTable) {
        List<Map<String, String>> expectedReasons = dataTable.asMaps(String.class, String.class);
        validateTurnOffReasons(expectedReasons);
    }

    @And("the response should contain the following reasonForTurnOffAlerts:")
    public void verifyReasonForTurnOffAlerts(DataTable dataTable) {
        List<Map<String, String>> expectedAlerts = dataTable.asMaps(String.class, String.class);
        validateReasonForTurnOffAlerts(expectedAlerts);
    }

    @And("the response should contain the following plans:")
    public void verifyPlans(DataTable dataTable) {
        List<Map<String, String>> expectedPlans = dataTable.asMaps(String.class, String.class);
        validatePlans(expectedPlans);
    }

    @And("response should have role with ID {string} and description {string}")
    public void responseShouldHaveRoleWithIDAndDescription(String roleID, String roleDescription) {
        verifyRoleDetails(roleID, roleDescription);
    }

    private void verifyRoleDetails(String expectedRoleID, String expectedRoleDescription) {
        Response response = testContext.getResponse();
        List<Map<String, String>> roles = response.jsonPath().getList("data.roles");

        boolean roleFound = roles.stream()
                .anyMatch(role -> expectedRoleID.equals(role.get("roleID")) &&
                                  expectedRoleDescription.equals(role.get("roleDescription")));

        assertThat("Expected role with ID and description not found", roleFound);
    }

    private void validatePlans(List<Map<String, String>> expectedPlans) {
        Response response = testContext.getResponse();
        List<Map<String, Object>> actualPlans = response.jsonPath().getList("data.plans");

        for (Map<String, String> expected : expectedPlans) {
            String expectedCode = normalize(expected.get("planCode"));
            String expectedDesc = normalize(expected.get("planDescription"));
            String expectedPromo1Code = normalize(expected.get("promotion1Code"));
            String expectedPromo1Desc = normalize(expected.get("promotion1Description"));

            boolean matchFound = actualPlans.stream().anyMatch(plan -> {
                String actualCode = normalize(plan.get("planCode"));
                String actualDesc = normalize(plan.get("planDescription"));
                String actualPromo1Code = normalize(plan.get("promotion1Code"));
                String actualPromo1Description = normalize(plan.get("promotion1Description"));
                return expectedCode.equals(actualCode) && expectedDesc.equals(actualDesc)
                        && expectedPromo1Code.equals(actualPromo1Code) && expectedPromo1Desc.equals(actualPromo1Description);
            });

            assertThat("Plan not found: code=" + expectedCode + ", description=" + expectedDesc + ", promotion 1 code="
                    + expectedPromo1Desc + ", promotion 1 description=" + expectedPromo1Desc, matchFound);
        }
    }

    private void validateReasonForTurnOffAlerts(List<Map<String, String>> expectedAlerts) {
        Response response = testContext.getResponse();
        List<Map<String, Object>> actualReasons = response.jsonPath().getList("data.turnOffReasons");

        for (Map<String, String> expected : expectedAlerts) {
            String reason = normalize(expected.get("reasonForTurnOff"));
            String expectedAlert = normalize(expected.get("reasonForTurnOffAlert"));

            String actualAlert = actualReasons.stream()
                    .filter(r -> reason.equals(normalize(r.get("reasonForTurnOff"))))
                    .map(r -> normalize(r.get("reasonForTurnOffAlert")))
                    .findFirst()
                    .orElse("");

            assertThat("Unexpected reasonForTurnOffAlert for: " + reason, actualAlert, equalTo(expectedAlert));
        }
    }

    private void verifyNumberOfTurnOffReasons(int count) {
        Response response = testContext.getResponse();
        assertThat("Unexpected number of turnOffReasons returned", response.jsonPath().getList("data.turnOffReasons").size(), equalTo(count));
    }

    private String normalize(Object value) {
        return Optional.ofNullable(value)
                .map(Object::toString)
                .map(s -> s.replaceAll("\\u00A0", " "))
                .map(String::trim)
                .orElse("");
    }

    private void validateTurnOffReasons(List<Map<String, String>> expectedReasons) {
        Response response = testContext.getResponse();
        List<Map<String, Object>> actualReasons = response.jsonPath().getList("data.turnOffReasons");

        for (Map<String, String> expected : expectedReasons) {
            String expectedReason = normalize(expected.get("reasonForTurnOff"));
            String expectedSubReason = normalize(expected.get("subReasonForTurnOff"));

            boolean matchFound = actualReasons.stream().anyMatch(actual -> {
                String actualReason = normalize(actual.get("reasonForTurnOff"));
                String actualSubReason = normalize(actual.get("subReasonForTurnOff"));
                return expectedReason.equals(actualReason) && expectedSubReason.equals(actualSubReason);
            });

            assertThat("Expected turnOffReason not found: " + expectedReason + " / " + expectedSubReason, matchFound);
        }
    }

    private void verifyFlagValueInResponse(String flag, Boolean expectedValue) {
        Response response = testContext.getResponse();

        List<Boolean> flagValues = response.jsonPath().getList("data.accounts." + flag, Boolean.class);
        assertThat("Expected at least one account with " + flag + " = " + expectedValue,
                flagValues, hasItem(expectedValue));
    }

    private void verifyFieldInResponse(String field, String value) {
        Response response = testContext.getResponse();
        List<Map<String, Object>> accounts = response.jsonPath().getList("data.accounts");

        boolean matchFound = accounts.stream()
                .anyMatch(account -> value.equals(String.valueOf(account.get(field))));

        assertThat("Expected value not found in any account for field: " + field, matchFound, is(true));
    }

    private void verifyResponseCode(String apiName, Integer statusCode) {
        assertThat("Invalid Response Code for API: " + apiName,
                testContext.getResponse().statusCode(),
                equalTo(statusCode));
    }

    private void verifyErrorCodeAndMessage(int errorCode, String errorMessage) {
        Response response = testContext.getResponse();

        assertThat("Incorrect ErrorCode returned",
                response.jsonPath().getInt("errorCode"),
                equalTo(errorCode));

        assertThat("Incorrect ErrorMessage returned",
                response.jsonPath().getString("errorMessage"),
                equalTo(errorMessage));
    }

    private void verifyNumberOfMatches(int expectedMatches) {
        Response response = testContext.getResponse();

        assertThat("Unexpected number of matches returned",
                response.jsonPath().getInt("data.numberOfMatches"),
                equalTo(expectedMatches));
    }

    private void verifyPaymentFields(String field, String value){
        Response response = testContext.getResponse();
        switch (value.toLowerCase()) {
            case "exist" -> {
                assertTrue("Unexpected " + field + " amount",
                        response.jsonPath().getDouble("data.accounts[0]." + field) > 0);
            }
            case "not empty" -> {
                assertNotNull("Unexpected " + field + " is null", response.jsonPath().getString("data.accounts[0]." + field));
                assertFalse("Unexpected " + field + " is empty", response.jsonPath().getString("data.accounts[0]." + field).trim().isEmpty());
            }
            case "empty" ->{
                assertTrue("Unexpected " + field + " is empty", response.jsonPath().getString("data.accounts[0]." + field).trim().isEmpty());
            }
            case "null" -> {
                assertNull("Unexpected " + field + " is not null", response.jsonPath().getString("data.accounts[0]." + field));
            }
            case "not exist" -> {
                assertEquals("Unexpected " + field + " amount", 0.0,
                        response.jsonPath().getDouble("data.accounts[0]." + field));
            }
        }
    }
}
