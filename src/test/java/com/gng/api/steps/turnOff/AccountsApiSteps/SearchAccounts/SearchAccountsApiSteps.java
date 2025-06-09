package com.gng.api.steps.turnOff.AccountsApiSteps.SearchAccounts;

import com.gng.api.pages.turnOff.AccountsApiPages.SearchAccounts.SearchAccountsApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

import static com.gng.api.steps.turnOn.AccountsApiSteps.SearchAccounts.SearchAccountsApiLabel.search_accounts;

@Slf4j
public class SearchAccountsApiSteps {

    private final TestContext testContext;
    private final SearchAccountsApiPage searchAccountsApiPage;

    public SearchAccountsApiSteps(TestContext testContext, SearchAccountsApiPage searchAccountsApiPage) {
        this.testContext = testContext;
        this.searchAccountsApiPage = searchAccountsApiPage;
        testContext.setGetAccountInfoApiPage(searchAccountsApiPage);
    }

    @When("a request is made to the SearchAccounts Api with invalid Banner Account Number parameter with transactionType As TOFF TC_74")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_invalid_Banner_Account_Number_parameter_TC_74 () {
        searchAccountsApiPage.validateResponseForInvalidAccountParamtersTC74(search_accounts);
    }


    @When("a request is made to the SearchAccounts Api with valid Banner Account Number parameter with transactionType As TOFF For a {string} Active Account")
    public void  a_request_is_made_to_the_SearchAccounts_Api_with_valid_Banner_Account_Number_parameter_with_transactionType_As_TOFF_TC_75_76 (String account_type) {
        searchAccountsApiPage.validateResponseForvalidAccountParamtersForTC75_76(search_accounts, account_type );
    }


}
