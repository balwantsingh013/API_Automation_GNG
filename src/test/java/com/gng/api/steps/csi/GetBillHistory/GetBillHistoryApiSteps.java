package com.gng.api.steps.csi.GetBillHistory;

import com.gng.api.pages.csi.GetBillHistoryPage.GetBillHistoryPage;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.util.CommonUtil;
import com.gng.api.util.TestContextHolder;
import io.cucumber.java.Before;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

import static com.gng.api.steps.csi.GetBillHistory.GetBillHistoryLabel.get_bill_history;

@Slf4j
public class GetBillHistoryApiSteps {

    @Before
    public void setupToken() {
        CommonUtil.silentlyGenerateAuthToken(); // No Allure logging
    }

    private final TestContext testContext;
    private final GetBillHistoryPage getBillHistoryPage;

    public GetBillHistoryApiSteps(TestContext testContext, GetBillHistoryPage getBillHistoryPage) {
        this.testContext = testContext;
        this.getBillHistoryPage = getBillHistoryPage;

        // Store page reference in test context
        testContext.setGetBillHistoryApiPage(getBillHistoryPage);

        // Set globally for utility access
        TestContextHolder.set(testContext);
    }

    @When("a request is made to GetBillHistory Api for {string}")
    public void a_request_is_made_to_GetBillHistory_Api(String testCondition) {
        CommonUtil.logTestDescriptionToReports(testCondition);

        getBillHistoryPage.validateResponseForTestConditions(
                get_bill_history,
                GetBillHistoryLabel.valueOf(testCondition)
        );
    }
}
