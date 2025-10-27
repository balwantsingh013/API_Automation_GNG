package com.gng.api.steps.meterSet.AccountsApiSteps.SearchAccounts;


import com.gng.api.pages.meterSet.AccountsApiPages.SearchAccounts.SearchAccountsApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import static com.gng.api.steps.meterSet.AccountsApiSteps.SearchAccounts.SearchAccountsApiLabel.search_accounts_mandatory;

@Slf4j
public class SearchAccountsApiSteps {

    private final TestContext testContext;
    private final SearchAccountsApiPage searchAccountsApiPage;

    public SearchAccountsApiSteps(TestContext testContext, SearchAccountsApiPage searchAccountsApiPage) {
        this.testContext = testContext;
        this.searchAccountsApiPage = searchAccountsApiPage;
    }

    @When("a request is made to the MeterSet SearchAccounts Api for {string} condition")
    public void aRequestIsMadeToTheMeterSetSearchAccountsApiFor(String testCondition) {
        searchAccountsApiPage.validatePositiveCases(search_accounts_mandatory, SearchAccountsApiLabel.valueOf(testCondition));

    }
}
