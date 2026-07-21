package com.gng.api.steps.csi.ConfirmPaperlessEnrollment;

import com.gng.api.pages.csi.ConfirmPaperlessEnrollmentPage.ConfirmPaperlessEnrollmentPage;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.util.CommonUtil;
import com.gng.api.util.TestContextHolder;
import io.cucumber.java.Before;
import io.cucumber.java.en.When;

import static com.gng.api.steps.csi.ConfirmPaperlessEnrollment.ConfirmPaperlessEnrollmentLabel.confirm_paperless_enrollment;

public class ConfirmPaperlessEnrollmentApiSteps {

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
        confirmPaperlessEnrollmentPage.validateResponseForTestCondition(
                confirm_paperless_enrollment,
                ConfirmPaperlessEnrollmentLabel.valueOf(testCondition));
    }
}
