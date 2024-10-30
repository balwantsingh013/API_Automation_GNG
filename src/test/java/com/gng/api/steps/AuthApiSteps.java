package com.gng.api.steps;

import com.gng.api.context.RunContext;
import com.gng.api.context.TestContext;
import com.gng.api.steps.base.BaseStep;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static com.gng.api.util.LogUtil.logError;
import static com.gng.api.util.LogUtil.logInfo;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public class AuthApiSteps extends BaseStep {
    TestContext testContext;

    public AuthApiSteps(TestContext testContext) {
        super();
        this.testContext = testContext;
    }

    @When("a request is made to generate authentication token")
    public void a_request_is_made_to_generate_authentication_token() {
        try {
            logInfo("Generating Auth Token");
            RunContext.get().setAuthApiPayload();
            Response response = given().relaxedHTTPSValidation()
                    .contentType(ContentType.JSON)
                    .baseUri(RunContext.get().getEnvConfig().getBaseUri())
                    .body(RunContext.get().getAuthPayload())
                    .when().post(RunContext.get().getEnvConfig().getAuthUri())
                    .then().extract().response();
            testContext.setResponse(response);
            testContext.setAuthToken(response.jsonPath().get("token"));
        } catch (Exception e) {
            logError(e.getMessage());
            throw new IllegalStateException(e.getMessage() + "\n" + testContext.getResponse().prettyPrint());
        }
    }

    @Then("verify Authentication Token Api response status code is {int}")
    public void verify_authentication_token_api_response_status_code_is(int statusCode) {
        testContext.getResponse().then().statusCode(statusCode);
    }

    @Then("a valid token is received in response")
    public void a_valid_token_is_received_in_response() {
        assertThat(testContext.getAuthToken(), notNullValue());
        logInfo("Token Generated Successfully");
    }

}
