package com.gng.api.steps.csi.GetPaymentArrangementInfo;

import com.gng.api.pages.csi.GetPaymentArrangementInfoPage.GetPaymentArrangementInfoPage;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.util.CommonUtil;
import com.gng.api.util.TestContextHolder;
import io.cucumber.java.Before;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

import static com.gng.api.steps.csi.GetPaymentArrangementInfo.GetPaymentArrangementInfoLabel.get_payment_arrangement_info;

@Slf4j
public class GetPaymentArrangementInfoApiSteps {

    @Before
    public void setupToken() {
        CommonUtil.silentlyGenerateAuthToken(); // No Allure logging
    }

    private final TestContext testContext;
    private final GetPaymentArrangementInfoPage getPaymentArrangementInfoPage;

    public GetPaymentArrangementInfoApiSteps(TestContext testContext,
                                             GetPaymentArrangementInfoPage getPaymentArrangementInfoPage) {

        this.testContext = testContext;
        this.getPaymentArrangementInfoPage = getPaymentArrangementInfoPage;

        // Register page object in TestContext
        testContext.setGetPaymentArrangementInfoApiPage(getPaymentArrangementInfoPage);

        // Set globally for utility access
        TestContextHolder.set(testContext);
    }

    @When("a request is made to GetPaymentArrangementInfo Api for {string}")
    public void a_request_is_made_to_GetPaymentArrangementInfo_Api_for(String testCondition) {

        CommonUtil.logTestDescriptionToReports(testCondition);

        // Delegate to page layer for execution
        getPaymentArrangementInfoPage.validateResponseForTestCondition(
                get_payment_arrangement_info,
                GetPaymentArrangementInfoLabel.valueOf(testCondition)
        );
    }
}
