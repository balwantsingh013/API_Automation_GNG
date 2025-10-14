package com.gng.api.steps.poc.GetAccountInfo;

import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.pages.poc.GetAccountInfoPage.GetAccountInfoApiPage;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

import static com.gng.api.steps.poc.GetAccountInfo.GetAccountInfoApiLabel.get_account_info;

@Slf4j
public class GetAccountInfoApiSteps {

    private final TestContext testContext;
    private final GetAccountInfoApiPage getAccountInfoApiPage;

    public GetAccountInfoApiSteps(TestContext testContext, GetAccountInfoApiPage getAccountInfoApiPage) {
        this.testContext = testContext;
        this.getAccountInfoApiPage = getAccountInfoApiPage;
        testContext.setGetAccountInfoApiPage(getAccountInfoApiPage);
    }

    @Then("verify the account information in the response should match the information in the database")
    public void verify_the_account_information_in_the_response_should_match_the_information_in_the_database() {
        getAccountInfoApiPage.verifyAccountInformationWithDatabase();
    }

    @When("a request is made to the GetAccountInfo Api with invalid parameters for {string} condition")
    public void aRequestIsMadeToTheGetAccountInfoApiWithInvalidParametersForCondition(String testCondition) {
        getAccountInfoApiPage.validateInvalidParameters(get_account_info, GetAccountInfoApiLabel.valueOf(testCondition));
    }

    @When("a request is made to the GetAccountInfo Api with valid parameters for {string} condition")
    public void aRequestIsMadeToTheGetAccountInfoApiWithValidParametersForCondition(String testCondition) {
        getAccountInfoApiPage.validatePositiveConditions(get_account_info, GetAccountInfoApiLabel.valueOf(testCondition));
    }
}
