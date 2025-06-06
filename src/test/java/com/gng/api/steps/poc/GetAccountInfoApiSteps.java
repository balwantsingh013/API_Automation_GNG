package com.gng.api.steps.poc;

import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.pages.poc.GetAccountInfoPage.GetAccountInfoApiPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetAccountInfoApiSteps {

    private final TestContext testContext;
    private final GetAccountInfoApiPage getAccountInfoApiPage;

    public GetAccountInfoApiSteps(TestContext testContext, GetAccountInfoApiPage getAccountInfoApiPage) {
        this.testContext = testContext;
        this.getAccountInfoApiPage = getAccountInfoApiPage;
        testContext.setGetAccountInfoApiPage(getAccountInfoApiPage);
    }

    @When("a request is made to the GetAccountInfo Api")
    public void a_request_is_made_to_the_get_account_info_api() {
        getAccountInfoApiPage.sendGetAccountInfoRequest();
    }

    @When("a request is made to the GetAccountInfo Api with missing param {string}")
    public void a_request_is_made_to_the_get_account_info_api_with_missing_param(String missingParam) {
        getAccountInfoApiPage.sendGetAccountInfoRequestWithMissingParam(missingParam);
    }

    @When("a request is made to the GetAccountInfo Api with invalid param {string} length")
    public void a_request_is_made_to_the_get_account_info_api_with_invalid_param_length(String param) {
        getAccountInfoApiPage.sendGetAccountInfoRequestWithInvalidParamLength(param);
    }

    @When("a request is made to the GetAccountInfo Api with duplicate requestID")
    public void a_request_is_made_to_the_get_account_info_api_with_duplicate_request_id() {
        getAccountInfoApiPage.sendGetAccountInfoRequestWithDuplicateRequestId();
    }

    @When("a request is made to the GetAccountInfo Api with non-existent combination of custCode and premCode")
    public void a_request_is_made_to_the_get_account_info_api_with_non_existent_combination_of_cust_code_and_prem_code() {
        getAccountInfoApiPage.sendGetAccountInfoRequestWithNonExistentCombination();
    }

    @Then("verify response code of GetAccountInfo Api with missing param {string} is {int}")
    public void verify_response_code_of_getAccountInfoApi_with_missing_param_is(String missingParam, int statusCode) {
        getAccountInfoApiPage.verifyResponseCodeForMissingParam(missingParam, statusCode);
    }

    @Then("verify response code of GetAccountInfo Api with invalid param {string} length is {int}")
    public void verify_response_code_of_get_account_info_api_with_invalid_param_length_is(String param, Integer statusCode) {
        getAccountInfoApiPage.verifyResponseCodeForInvalidParamLength(param, statusCode);
    }

    @Then("verify the account information in the response should match the information in the database")
    public void verify_the_account_information_in_the_response_should_match_the_information_in_the_database() {
        getAccountInfoApiPage.verifyAccountInformationWithDatabase();
    }


}
