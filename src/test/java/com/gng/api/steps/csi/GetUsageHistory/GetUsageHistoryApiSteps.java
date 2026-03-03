package com.gng.api.steps.csi.GetUsageHistory;

import com.gng.api.pages.csi.GetUsageHistoryPage.GetUsageHistoryPage;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.util.CommonUtil;
import com.gng.api.util.TestContextHolder;
import io.cucumber.java.Before;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

import static com.gng.api.steps.csi.GetUsageHistory.GetUsageHistoryLabel.get_usage_history;

@Slf4j
public class GetUsageHistoryApiSteps {

    @Before
    public void setupToken() {
        CommonUtil.silentlyGenerateAuthToken(); // No Allure logging
    }

    private final TestContext testContext;
    private final GetUsageHistoryPage getUsageHistoryPage;

    public GetUsageHistoryApiSteps(TestContext testContext, GetUsageHistoryPage getUsageHistoryPage) {
        this.testContext = testContext;
        this.getUsageHistoryPage = getUsageHistoryPage;

        // Register page object in TestContext
        testContext.setGetUsageHistoryApiPage(getUsageHistoryPage);

        // Set globally for utility access
        TestContextHolder.set(testContext);
    }

    @When("a request is made to GetUsageHistory Api for {string}")
    public void a_request_is_made_to_GetUsageHistory_Api_for(String testCondition) {
        CommonUtil.logTestDescriptionToReports(testCondition);

        // Delegate to page layer for execution
        getUsageHistoryPage.validateResponseForTestCondition(
                get_usage_history,
                GetUsageHistoryLabel.valueOf(testCondition)
        );
    }
}
