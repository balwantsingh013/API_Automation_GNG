package com.gng.api.steps;

import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.pages.GetAccountInfoApiPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Slf4j
public class CommonApiSteps {

    private final TestContext testContext;

    public CommonApiSteps(TestContext testContext, GetAccountInfoApiPage getAccountInfoApiPage) {
        super();
        this.testContext = testContext;
        testContext.setGetAccountInfoApiPage(getAccountInfoApiPage);
    }

    @Then("verify response code of {string} Api is <{int}>")
    public void verify_response_code_of_api_is(String apiName, Integer statusCode) {
        assertThat("Invalid " + apiName + " Response Code for Api - " + apiName, testContext.getResponse().statusCode(), equalTo(statusCode));
    }

    @And("response should have ErrorCode {int} and ErrorMessage {string}")
    public void responseShouldHaveErrorCodeAndErrorMessage(int errorCode, String errorMessage) {
        assertThat("Incorrect ErrorCode returned", testContext.getResponse().jsonPath().get("errorCode"), equalTo(errorCode));
        assertThat("Incorrect ErrorMessage returned", testContext.getResponse().jsonPath().get("errorMessage"), equalTo(errorMessage));
    }
}
