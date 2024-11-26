package com.gng.api.steps;

import com.gng.api.context.RunContext;
import com.gng.api.context.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static com.gng.api.config.LogConfig.logError;
import static com.gng.api.config.LogConfig.logInfo;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public class AuthApiSteps {

    private final TestContext testContext;

    public AuthApiSteps(TestContext testContext) {
        this.testContext = testContext;
    }

    @When("a request is made to generate authentication token")
    public void requestToGenerateAuthToken() {
        logInfo("Generating Auth Token...");
        try {
            RunContext runContext = RunContext.get();
            runContext.setAuthApiPayload();
            Response response = executeAuthRequest(runContext);
            storeAuthToken(response);
        } catch (Exception e) {
            handleException(e);
        }
    }

    private Response executeAuthRequest(RunContext runContext) {
        return given()
                .relaxedHTTPSValidation()
                .contentType(ContentType.JSON)
                .baseUri(runContext.getEnvConfig().getBaseUri())
                .body(runContext.getAuthPayload())
                .post(runContext.getEnvConfig().getAuthUri())
                .then().extract().response();
    }

    private void storeAuthToken(Response response) {
        testContext.setResponse(response);
        testContext.setAuthToken(response.jsonPath().getString("token"));
    }

    private void handleException(Exception e) {
        logError("Error occurred: " + e.getMessage());
        String responseDetails = testContext.getResponse() != null ? testContext.getResponse().prettyPrint() : "No response received";
        throw new IllegalStateException(e.getMessage() + "\n" + responseDetails, e);
    }

    @Then("verify Authentication Token Api response status code is {int}")
    public void verifyResponseStatusCode(int statusCode) {
        testContext.getResponse().then().statusCode(statusCode);
    }

    @Then("a valid token is received in response")
    public void validateTokenReceived() {
        assertThat("Authentication token should not be null", testContext.getAuthToken(), notNullValue());
        logInfo("Token generated successfully: " + testContext.getAuthToken());
    }
}
