package com.gng.api.steps.AccountsApiSteps.SearchAccounts;

import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.pages.AccountsApiPages.SearchAccounts.SearchAccountsApiPage;
import com.gng.api.steps.UsersApiSteps.GetUserRoles.GetUserRolesApiLabel;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

import static com.gng.api.steps.AccountsApiSteps.SearchAccounts.SearchAccountsApiLabel.search_accounts;
import static com.gng.api.steps.UsersApiSteps.GetUserRoles.GetUserRolesApiLabel.get_user_roles;

@Slf4j
    public class SearchAccountsApiSteps {

        private final TestContext testContext;
        private final SearchAccountsApiPage searchAccountsApiPage;

        public SearchAccountsApiSteps(TestContext testContext, SearchAccountsApiPage searchAccountsApiPage) {
            this.testContext = testContext;
            this.searchAccountsApiPage = searchAccountsApiPage;
            testContext.setGetAccountInfoApiPage(searchAccountsApiPage);
        }
    @When("a request is made to the SearchAccounts Api with {string}TC42_TC44")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC42_TC44(String requestID) {
        searchAccountsApiPage.validateInvalidRequestIDCasesTC42_TC44(search_accounts,SearchAccountsApiLabel.valueOf(requestID));
    }
    @When("a request is made to the SearchAccounts Api with {string}TC45_TC48")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC45_TC48(String loginID) {
        searchAccountsApiPage.validateInvalidLoginIDCasesTC45_TC48(search_accounts, SearchAccountsApiLabel.valueOf(loginID));
    }
    @When("a request is made to the SearchAccounts Api with {string}TC49")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC49(String customerCode) {
        searchAccountsApiPage.validateInvalidCustomerCodeCasesTC49(search_accounts, SearchAccountsApiLabel.valueOf(customerCode));
    }
    @When("a request is made to the SearchAccounts Api with {string}TC50")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC50(String premisesCode) {
        searchAccountsApiPage.validateInvalidPremisesCodeCasesTC50(search_accounts, SearchAccountsApiLabel.valueOf(premisesCode));
    }
    @When("a request is made to the SearchAccounts Api with {string}TC51_TC52")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC51_TC52(String transactionType) {
        searchAccountsApiPage.validateInvalidTransactionTypeCasesTC51_TC52(search_accounts, SearchAccountsApiLabel.valueOf(transactionType));
    }
    @When("a request is made to the SearchAccounts Api with {string}TC53")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC53(String businessName) {
        searchAccountsApiPage.validateInvalidBusinessNameCasesTC53(search_accounts, SearchAccountsApiLabel.valueOf(businessName));
    }
}
