package com.gng.api.steps.practice.GetPaymentHistory;

import com.gng.api.pages.practice.GetPaymentHistoryPage.GetPaymentHistoryPage;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.util.CommonUtil;
import com.gng.api.util.TestContextHolder;
import io.cucumber.java.Before;
import io.cucumber.java.en.When;

import static com.gng.api.steps.practice.GetPaymentHistory.GetPaymentHistoryLabel.practice_get_payment_history;

public class GetPaymentHistoryApiSteps {

    @Before
    public void setupToken() {
        CommonUtil.silentlyGenerateAuthToken();
    }

    private final GetPaymentHistoryPage getPaymentHistoryPage;

    public GetPaymentHistoryApiSteps(TestContext testContext, GetPaymentHistoryPage getPaymentHistoryPage) {
        this.getPaymentHistoryPage = getPaymentHistoryPage;
        testContext.setGetPaymentHistoryApiPage(getPaymentHistoryPage);
        TestContextHolder.set(testContext);
    }

    @When("a practice request is made to GetPaymentHistory Api for {string}")
    public void a_practice_request_is_made_to_get_payment_history_api(String testCondition) {
        CommonUtil.logTestDescriptionToReports(testCondition);
        getPaymentHistoryPage.validateResponseForTestCondition(
                practice_get_payment_history,
                GetPaymentHistoryLabel.valueOf(testCondition));
    }
}
