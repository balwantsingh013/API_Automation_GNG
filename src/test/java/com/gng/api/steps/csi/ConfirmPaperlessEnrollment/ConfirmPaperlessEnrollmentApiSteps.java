package com.gng.api.steps.csi.ConfirmPaperlessEnrollment;

import com.gng.api.pages.csi.ConfirmPaperlessEnrollmentPage.ConfirmPaperlessEnrollmentPage;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.report.DualReportManager;
import com.gng.api.util.CommonUtil;
import com.gng.api.util.TestContextHolder;
import io.cucumber.java.Before;
import io.cucumber.java.en.When;
import org.testng.SkipException;

import static com.gng.api.steps.csi.ConfirmPaperlessEnrollment.ConfirmPaperlessEnrollmentLabel.confirm_paperless_enrollment;

public class ConfirmPaperlessEnrollmentApiSteps {

    private static final String BANNER_COORDINATION_SKIP_REASON =
            "Blocked per FTD: needs coordination with Banner Devs to fail at the right time to trigger errorCode 40293 "
                    + "(Paperless Preference Update Failed).";

    @Before
    public void setupToken() {
        CommonUtil.silentlyGenerateAuthToken();
    }

    private final ConfirmPaperlessEnrollmentPage confirmPaperlessEnrollmentPage;

    public ConfirmPaperlessEnrollmentApiSteps(TestContext testContext,
                                              ConfirmPaperlessEnrollmentPage confirmPaperlessEnrollmentPage) {
        this.confirmPaperlessEnrollmentPage = confirmPaperlessEnrollmentPage;
        testContext.setConfirmPaperlessEnrollmentApiPage(confirmPaperlessEnrollmentPage);
        TestContextHolder.set(testContext);
    }

    @When("a request is made to ConfirmPaperlessEnrollment Api for {string}")
    public void a_request_is_made_to_confirm_paperless_enrollment_api(String testCondition) {
        CommonUtil.logTestDescriptionToReports(testCondition);
        skipIfBannerCoordinationRequired(testCondition);
        confirmPaperlessEnrollmentPage.validateResponseForTestCondition(
                confirm_paperless_enrollment,
                ConfirmPaperlessEnrollmentLabel.valueOf(testCondition));
    }

    private static void skipIfBannerCoordinationRequired(String testCondition) {
        if (testCondition.equals(
                ConfirmPaperlessEnrollmentLabel.TC_131__Negative__Paperless_Preference_Update_Failed__ACTIVE_.name())
                || testCondition.equals(
                ConfirmPaperlessEnrollmentLabel.TC_132__Negative__Paperless_Preference_Update_Failed__NEW_.name())) {
            String reason = testCondition + " — " + BANNER_COORDINATION_SKIP_REASON;
            DualReportManager.logInfo("⏭️ SKIPPED: " + reason);
            throw new SkipException(reason);
        }
    }
}
