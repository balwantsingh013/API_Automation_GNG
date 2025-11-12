package com.gng.api.steps.serviceTransfer.AccountsApiSteps.SearchAccounts;

import com.gng.api.pages.serviceTransfer.AccountsApiPages.SearchAccounts.SearchAccountsServiceTransferApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

import static com.gng.api.steps.serviceTransfer.AccountsApiSteps.SearchAccounts.SearchAccountsApiLabel.search_accounts;
import static com.gng.api.steps.serviceTransfer.AccountsApiSteps.SearchAccounts.SearchAccountsApiLabel.search_accounts_mandatory;

@Slf4j
public class SearchAccountsApiSteps {

    private final TestContext testContext;
    private final SearchAccountsServiceTransferApiPage searchAccountsApiPage;

    public SearchAccountsApiSteps(TestContext testContext, SearchAccountsServiceTransferApiPage searchAccountsApiPage) {
        this.testContext = testContext;
        this.searchAccountsApiPage = searchAccountsApiPage;
    }

    @When("a request is made to the SearchAccounts Api for {string}")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_condition(String testCondition) {
        searchAccountsApiPage.validateNegativeCases(search_accounts_mandatory, SearchAccountsApiLabel.valueOf(testCondition));
    }

    @When("customer code, premises code and transaction id is fetched from db for {string}")
    public void db_search_for_cus_prem_code(String testCondition) {
        searchAccountsApiPage.searchCustCodePremCodeFromDB(SearchAccountsApiLabel.valueOf(testCondition));
    }

    @When("request is made to the SearchAccountsApi for {string}")
    public void search_accounts_e2e(String testCondition) {
        searchAccountsApiPage.searchAccountsE2E(search_accounts, SearchAccountsApiLabel.valueOf(testCondition));
    }



}