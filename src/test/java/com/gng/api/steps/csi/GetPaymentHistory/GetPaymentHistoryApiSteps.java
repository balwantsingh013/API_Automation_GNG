package com.gng.api.steps.csi.GetPaymentHistory;

import com.gng.api.pages.csi.GetPaymentHistoryPage.GetPaymentHistoryPage;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.util.CommonUtil;
import com.gng.api.util.TestContextHolder;
import io.cucumber.java.Before;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

import static com.gng.api.steps.csi.GetPaymentHistory.GetPaymentHistoryLabel.get_payment_history;

@Slf4j
public class GetPaymentHistoryApiSteps {

    @Before
    public void setupToken() {
        CommonUtil.silentlyGenerateAuthToken(); // No Allure logging
    }

    private final TestContext testContext;
    private final GetPaymentHistoryPage getPaymentHistoryPage;

    public GetPaymentHistoryApiSteps(TestContext testContext,
                                     GetPaymentHistoryPage getPaymentHistoryPage) {

        this.testContext = testContext;
        this.getPaymentHistoryPage = getPaymentHistoryPage;

        // Register page object in TestContext
        testContext.setGetPaymentHistoryApiPage(getPaymentHistoryPage);

        // Set globally for utility access
        TestContextHolder.set(testContext);
    }

    @When("a request is made to GetPaymentHistory Api for {string}")
    public void a_request_is_made_to_GetPaymentHistory_Api_for(String testCondition) {

        CommonUtil.logTestDescriptionToReports(testCondition);

        // Delegate to page layer for execution
        getPaymentHistoryPage.validateResponseForTestCondition(
                get_payment_history,
                GetPaymentHistoryLabel.valueOf(testCondition)
        );
    }
}
