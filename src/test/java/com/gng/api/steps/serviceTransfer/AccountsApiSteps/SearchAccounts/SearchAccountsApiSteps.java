package com.gng.api.steps.serviceTransfer.AccountsApiSteps.SearchAccounts;

import com.gng.api.pages.serviceTransfer.AccountsApiPages.SearchAccounts.SearchAccountsApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import static com.gng.api.steps.serviceTransfer.AccountsApiSteps.SearchAccounts.SearchAccountsApiLabel.search_accounts_mandatory;
import static com.gng.api.steps.serviceTransfer.AccountsApiSteps.SearchAccounts.SearchAccountsApiLabel.search_accounts;

@Slf4j
public class SearchAccountsApiSteps {

    private final TestContext testContext;
    private final SearchAccountsApiPage searchAccountsApiPage;

    public SearchAccountsApiSteps(TestContext testContext, SearchAccountsApiPage searchAccountsApiPage) {
        this.testContext = testContext;
        this.searchAccountsApiPage = searchAccountsApiPage;
    }

    @When("a request is made to the SearchAccounts Api for {string}")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_condition(String testCondition) {
        searchAccountsApiPage.validateNegativeCases(search_accounts_mandatory, SearchAccountsApiLabel.valueOf(testCondition));
    }

    @When("a request is made to the SearchAccounts Api ServiceTransfer with transactionType for {string} condition")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_transactionType(String testCondition) {
        searchAccountsApiPage.searchForExternalCases(search_accounts, SearchAccountsApiLabel.valueOf(testCondition));
    }
}