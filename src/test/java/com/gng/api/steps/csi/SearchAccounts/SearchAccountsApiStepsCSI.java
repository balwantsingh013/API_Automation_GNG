package com.gng.api.steps.csi.SearchAccounts;

import com.gng.api.pages.csi.SearchAccountsPage.SearchAccountsPageCSI;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.util.CommonUtil;
import com.gng.api.util.TestContextHolder;
import io.cucumber.java.Before;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

import static com.gng.api.steps.csi.SearchAccounts.SearchAccountsLabelCSI.search_accounts_csi;

@Slf4j
public class SearchAccountsApiStepsCSI {

    @Before
    public void setupToken() {
        CommonUtil.silentlyGenerateAuthToken(); // No Allure logging
    }

    private final TestContext testContext;
    private final SearchAccountsPageCSI searchAccountsPageCSI;

    public SearchAccountsApiStepsCSI(TestContext testContext, SearchAccountsPageCSI searchAccountsPageCSI) {
        this.testContext = testContext;
        this.searchAccountsPageCSI = searchAccountsPageCSI;
        testContext.setSearchAccountsApiPage(searchAccountsPageCSI);
        // Set globally for utility access
        TestContextHolder.set(testContext);
    }

    @When("a request is made to SearchAccounts Api for {string}")
    public void a_request_is_made_to_the_SearchAccounts_Api_for(String testCondition) {
        CommonUtil.logTestDescriptionToReports(testCondition);
        searchAccountsPageCSI.validateResponseForTestConditions(
                search_accounts_csi,
                SearchAccountsLabelCSI.valueOf(testCondition)
        );
    }
}
