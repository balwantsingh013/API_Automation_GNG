package com.gng.api.steps;

import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Slf4j
public class BaseSteps {
    private final TestContext testContext;

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
                response.jsonPath().getInt("numberOfMatches"),
                equalTo(expectedMatches));
    }

}
