package com.gng.api.steps.marketerSwitch.AccountsApiSteps.SearchAccounts;


import com.gng.api.pages.marketerSwitch.AccountsApiPages.SearchAccounts.SearchAccountsApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

import static com.gng.api.steps.marketerSwitch.AccountsApiSteps.SearchAccounts.SearchAccountsApiLabel.search_accounts_mandatory;

@Slf4j
public class SearchAccountsApiSteps {

    private final TestContext testContext;
    private final SearchAccountsApiPage searchAccountsApiPage;

    public SearchAccountsApiSteps(TestContext testContext, SearchAccountsApiPage searchAccountsApiPage) {
        this.testContext = testContext;
        this.searchAccountsApiPage = searchAccountsApiPage;
    }

    @When("a request is made to the MarketerSwitch SearchAccounts Api for {string} condition")
    public void aRequestIsMadeToTheMarketerSwitchSearchAccountsApiFor(String testCondition) {
        searchAccountsApiPage.validatePositiveCases(search_accounts_mandatory, SearchAccountsApiLabel.valueOf(testCondition));

    }

    @Then("a request is made to the MarketerSwitch SearchAccounts Api for external cases for {string} condition")
    public void aRequestIsMadeToTheMarketerSwitchSearchAccountsApiForExternalCases(String testCondition) {
        searchAccountsApiPage.validateExternalCases(search_accounts_mandatory, SearchAccountsApiLabel.valueOf(testCondition));

    }
    @Then("a request is made to the MarketerSwitch SearchAccounts Api from GetEligiblePlansAndOffers API for external cases for {string} condition")
    public void aRequestIsMadeToTheMarketerSwitchSearchAccountsApiFromGetEligibleForExternalCases(String testCondition) {
        searchAccountsApiPage.searchFromGetEligibleExternalCases(search_accounts_mandatory, SearchAccountsApiLabel.valueOf(testCondition));

    }

}
