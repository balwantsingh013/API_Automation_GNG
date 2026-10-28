package com.gng.api.steps.csi.ReportPayment;

import com.gng.api.pages.csi.ReportPaymentPage.ReportPaymentPage;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.util.CommonUtil;
import com.gng.api.util.TestContextHolder;
import io.cucumber.java.Before;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

import static com.gng.api.steps.csi.ReportPayment.ReportPaymentLabel.report_payment;

@Slf4j
public class ReportPaymentApiSteps {

    @Before
    public void setupToken() {
        CommonUtil.silentlyGenerateAuthToken();
    }

    private final TestContext testContext;
    private final ReportPaymentPage reportPaymentPage;

    public ReportPaymentApiSteps(TestContext testContext, ReportPaymentPage reportPaymentPage) {
        this.testContext = testContext;
        this.reportPaymentPage = reportPaymentPage;
        testContext.setReportPaymentApiPage(reportPaymentPage);
        TestContextHolder.set(testContext);
    }

    @When("a request is made to ReportPayment Api for {string}")
    public void a_request_is_made_to_ReportPayment_Api(String testCondition) {
        CommonUtil.logTestDescriptionToReports(testCondition);
        reportPaymentPage.validateResponseForTestConditions(
                report_payment,
                ReportPaymentLabel.valueOf(testCondition)
        );
    }
}
