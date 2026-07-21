package com.gng.api.pages.csi.ConfirmPaperlessEnrollmentPage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.ConfirmPaperlessEnrollment.ConfirmPaperlessEnrollmentRequest;
import com.gng.api.pojo.CSIPojo.ConfirmPaperlessEnrollment.ConfirmPaperlessEnrollmentResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.ConfirmPaperlessEnrollment.ConfirmPaperlessEnrollmentLabel;
import com.gng.api.util.BannerAccountRowLockUtil;
import com.gng.api.util.BannerTestEmailOverrideUtil;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.CONFIRM_PAPERLESS_ENROLLMENT;

@Slf4j
public class ConfirmPaperlessEnrollmentPage extends BasePage {

    private final ConfirmPaperlessEnrollmentApiHelper helper;

    public ConfirmPaperlessEnrollmentPage(TestContext testContext) {
        super(testContext);
        this.helper = new ConfirmPaperlessEnrollmentApiHelper(testContext);
    }

    public void validateResponseForTestCondition(ConfirmPaperlessEnrollmentLabel apiLabel,
                                                 ConfirmPaperlessEnrollmentLabel testCondition) {
        ConfirmPaperlessEnrollmentRequest payload = helper.preparePayload(apiLabel);
        helper.preparePayloadForTestCondition(payload, testCondition);
        payload.setToken(ConfirmPaperlessEnrollmentTokenSetupHelper.prepareTokenForConfirmApi(payload.getToken()));

        // TC_131/132: GZBEMCP FOR UPDATE during Confirm (UAT often returns HTTP 504 instead of 40293).
        boolean preferenceFailAccountLock =
                testCondition == ConfirmPaperlessEnrollmentLabel.TC_131__Negative__Paperless_Preference_Update_Failed__ACTIVE_
                        || testCondition == ConfirmPaperlessEnrollmentLabel.TC_132__Negative__Paperless_Preference_Update_Failed__NEW_;
        // TC_133: blank NEW_TEST_EMAIL_ADDR around Confirm → UAT 40321.
        boolean notificationFailClearOverride =
                testCondition == ConfirmPaperlessEnrollmentLabel.TC_133__Negative__Confirmation_Notification_Failure_Rollback_;

        try {
            if (preferenceFailAccountLock) {
                BannerAccountRowLockUtil.lockAccountRow(
                        testContext.getCustomerCode(), testContext.getPremisesCode());
            }
            if (notificationFailClearOverride) {
                BannerTestEmailOverrideUtil.clearForEmailNotificationFailure();
            }
            setRequestSpecification(payload, testContext.getAuthToken());
            Response response = sendRequest(HttpPost.METHOD_NAME, CONFIRM_PAPERLESS_ENROLLMENT, 200);
            ConfirmPaperlessEnrollmentResponse confirmPaperlessEnrollmentResponse =
                    deserializeResponseToPojo(response, ConfirmPaperlessEnrollmentResponse.class);
            testContext.setConfirmPaperlessEnrollmentResponse(confirmPaperlessEnrollmentResponse);
            testContext.setResponse(response);
        } finally {
            if (preferenceFailAccountLock) {
                BannerAccountRowLockUtil.release();
            }
            if (notificationFailClearOverride) {
                BannerTestEmailOverrideUtil.restore();
                ConfirmPaperlessEnrollmentTokenSetupHelper.restoreNotificationFailureBannerEmail(
                        testContext.getCustomerCode());
            }
        }
    }
}
