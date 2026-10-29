package com.gng.api.steps.csi.ReportPayment;

import com.gng.api.pages.csi.ReportPaymentPage.ReportPaymentDelinquencyLookbackSupport;
import com.gng.api.pages.csi.ReportPaymentPage.ReportPaymentPage;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.util.CommonUtil;
import com.gng.api.util.TestContextHolder;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

import static com.gng.api.steps.csi.ReportPayment.ReportPaymentLabel.report_payment;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@Slf4j
public class ReportPaymentApiSteps {

    @Before
    public void setupToken() {
        CommonUtil.silentlyGenerateAuthToken();
    }

    @After
    public void restoreDelinquencyLookbackAfterTc48(Scenario scenario) {
        String name = scenario == null ? "" : scenario.getName();
        // Keep the widened window for the whole TC_37–TC_48 block; restore after TC_48.
        if (name != null && name.contains("TC_48__")) {
            ReportPaymentDelinquencyLookbackSupport.restoreIfChanged();
        }
    }

    @AfterAll
    public static void restoreDelinquencyLookbackAfterAll() {
        ReportPaymentDelinquencyLookbackSupport.restoreIfChanged();
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

    @And("response success is {string} and data is {string}")
    public void responseSuccessAndData(String success, String data) {
        Response response = testContext.getResponse();
        assertThat("Incorrect success flag",
                response.jsonPath().getBoolean("success"),
                is(Boolean.parseBoolean(success)));
        Object responseData = response.jsonPath().get("data");
        if ("null".equalsIgnoreCase(data)) {
            assertThat("data should be null", responseData, nullValue());
        } else {
            assertThat("data should be populated", responseData, notNullValue());
        }
    }

    @And("ReportPayment paCreated is {string} paAmount is {string} and paDueDate is {string}")
    public void reportPaymentHoldOutcome(String paCreated, String paAmount, String paDueDate) {
        Response response = testContext.getResponse();
        assertThat("Incorrect paCreated",
                response.jsonPath().getBoolean("data.paCreated"),
                is(Boolean.parseBoolean(paCreated)));
        assertAmount(response.jsonPath().get("data.paAmount"), paAmount);
        assertDueDate(response.jsonPath().get("data.paDueDate"), paDueDate);
    }

    private void assertAmount(Object actual, String expected) {
        if ("null".equalsIgnoreCase(expected)) {
            assertThat("paAmount should be null", actual, nullValue());
            return;
        }
        assertThat("paAmount should be populated", actual, notNullValue());
        if ("matchesPastDue".equalsIgnoreCase(expected)) {
            BigDecimal actualAmount = new BigDecimal(actual.toString());
            BigDecimal expectedAmount = new BigDecimal(testContext.getReportPaymentExpectedPaAmount());
            assertThat("paAmount should equal the net past-due balance used for the request",
                    actualAmount.compareTo(expectedAmount), is(0));
        }
    }

    private void assertDueDate(Object actual, String expected) {
        if ("null".equalsIgnoreCase(expected)) {
            assertThat("paDueDate should be null", actual, nullValue());
            return;
        }
        assertThat("paDueDate should be a YYYYMMDD date",
                actual != null && actual.toString().matches("\\d{8}"),
                is(true));
    }
}
