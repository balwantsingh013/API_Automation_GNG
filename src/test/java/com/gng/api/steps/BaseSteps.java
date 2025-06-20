package com.gng.api.steps;

import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

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

    @And("response should have pastDueAmount as {int}")
    public void responseShouldShowPastDueAmountAs(int pastDue){
        verifyPastDueAmount(pastDue);
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

        assertThat("Unexpected account status returned",
                response.jsonPath().getString("data.accounts[0]."+field),
                equalTo(value));
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

    private void verifyPastDueAmount(int pastDueAmount){
        Response response = testContext.getResponse();

        assertThat("Unexpected past due amount",
                response.jsonPath().getInt("data.accounts[0].pastDueAmount"),
                equalTo(pastDueAmount));
    }


}
