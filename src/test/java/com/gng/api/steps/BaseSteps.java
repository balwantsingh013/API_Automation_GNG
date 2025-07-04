package com.gng.api.steps;

import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import java.util.List;
import java.util.Map;
import static org.hamcrest.Matchers.equalTo;
import static org.testng.AssertJUnit.*;

@Slf4j
public class BaseSteps {
    private final TestContext testContext;

    @ParameterType("true|false")
    public Boolean booleanVal(String value) {
        return Boolean.valueOf(value);
    }

    public BaseSteps(TestContext testContext) {
        this.testContext = testContext;
    }

    @Then("verify response code of {string} Api is {int}")
    public void verify_response_code_of_api_is(String apiName, Integer statusCode) {
        verifyResponseCode(apiName, statusCode);
    }

    @And("response should have ErrorCode {int} and ErrorMessage {string}")
    public void responseShouldHaveErrorCodeAndErrorMessage(int errorCode, String errorMessage) {
        verifyErrorCodeAndMessage(errorCode, errorMessage);
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
    public void responseShouldHaveFieldAs(String field, String value){
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

    @And("response should have turnOffReason with reasonForTurnOff as {string} and subReasonForTurnOff as {string}")
    public void responseShouldHaveTurnOffReason(String reasonForTurnOff, String subReasonForTurnOff) {
        verifyTurnOffReason(reasonForTurnOff, subReasonForTurnOff);
    }

    @And("response should have reasonForTurnOffAlert as {string} for reasonForTurnOff {string}")
    public void responseShouldHaveReasonForTurnOffAlert(String alert, String reasonForTurnOff) {
        verifyReasonForTurnOffAlert(alert, reasonForTurnOff);
    }

    @And("response should have plan with code {string} and description {string}")
    public void responseShouldHavePlanWithCodeAndDescription(String planCode, String planDescription) {
        verifyPlanDetails(planCode, planDescription);
    }

    private void verifyPlanDetails(String expectedPlanCode, String expectedPlanDescription) {
        Response response = testContext.getResponse();
        List<Map<String, String>> plans = response.jsonPath().getList("data.plans");

        boolean planFound = plans.stream()
                .anyMatch(plan -> expectedPlanCode.equals(plan.get("planCode")) &&
                        expectedPlanDescription.equals(plan.get("planDescription")));

        assertThat("Expected plan with code and description not found", planFound);
    }


    private void verifyReasonForTurnOffAlert(String alert, String reasonForTurnOff) {
        Response response = testContext.getResponse();
        List<Map<String, String>> turnOffReasons = response.jsonPath().getList("data.turnOffReasons");
        String actualAlert = turnOffReasons.stream()
                .filter(reason -> reason.get("reasonForTurnOff").equals(reasonForTurnOff))
                .findFirst()
                .map(reason -> reason.get("reasonForTurnOffAlert"))
                .orElse(null);
        assertThat("Unexpected reasonForTurnOffAlert returned", actualAlert, equalTo(alert));
    }

    private void verifyNumberOfTurnOffReasons(int count) {
        Response response = testContext.getResponse();
        assertThat("Unexpected number of turnOffReasons returned", response.jsonPath().getList("data.turnOffReasons").size(), equalTo(count));
    }

    private void verifyTurnOffReason(String reasonForTurnOff, String subReasonForTurnOff) {
        Response response = testContext.getResponse();
        List<Map<String, String>> turnOffReasons = response.jsonPath().getList("data.turnOffReasons");
        boolean found = turnOffReasons.stream()
                .anyMatch(reason -> reason.get("reasonForTurnOff").equals(reasonForTurnOff) &&
                        reason.get("subReasonForTurnOff").equals(subReasonForTurnOff));
        assertThat("Expected turnOffReason not found", found);
    }

    private void verifyFlagValueInResponse(String flag, Boolean value) {
        Response response = testContext.getResponse();

        assertThat("Unexpected "+flag+" returned",
                response.jsonPath().getBoolean("data.accounts[0]."+flag),
                equalTo(value));
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
            case "not exist" -> {
                assertEquals("Unexpected " + field + " amount", 0.0,
                        response.jsonPath().getDouble("data.accounts[0]." + field));
            }
        }
    }
}
