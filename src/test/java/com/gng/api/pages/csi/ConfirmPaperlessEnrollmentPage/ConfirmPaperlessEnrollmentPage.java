package com.gng.api.pages.csi.ConfirmPaperlessEnrollmentPage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.ConfirmPaperlessEnrollment.ConfirmPaperlessEnrollmentRequest;
import com.gng.api.pojo.CSIPojo.ConfirmPaperlessEnrollment.ConfirmPaperlessEnrollmentResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.report.DualReportManager;
import com.gng.api.steps.csi.ConfirmPaperlessEnrollment.ConfirmPaperlessEnrollmentLabel;
import com.gng.api.util.BannerAccountRowLockUtil;
import com.gng.api.util.BannerTestEmailOverrideUtil;
import com.gng.api.util.PaperlessSideEffectStateUtil;
import com.gng.api.util.PaperlessTokenPperEvidenceUtil;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.CONFIRM_PAPERLESS_ENROLLMENT;

/**
 * ConfirmPaperlessEnrollment page object.
 * Logs BEFORE/AFTER (and DURING for rollback) custadv + Banner/PPER state for David review evidence.
 */
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

        boolean preferenceFailAccountLock =
                testCondition == ConfirmPaperlessEnrollmentLabel.TC_131__Negative__Paperless_Preference_Update_Failed__ACTIVE_
                        || testCondition == ConfirmPaperlessEnrollmentLabel.TC_132__Negative__Paperless_Preference_Update_Failed__NEW_;
        boolean notificationFailClearOverride =
                testCondition == ConfirmPaperlessEnrollmentLabel.TC_133__Negative__Confirmation_Notification_Failure_Rollback_;
        boolean expectUnchanged = expectsNoSideEffectUpdate(testCondition);
        boolean isRollback = notificationFailClearOverride;

        String customerCode = firstNonBlank(testContext.getCustomerCode());
        String premisesCode = firstNonBlank(testContext.getPremisesCode());
        boolean canCaptureState = customerCode != null && premisesCode != null;
        if (!canCaptureState) {
            DualReportManager.logInfo(testCondition.name()
                    + " — no customerCode/premisesCode on context (request-validation TC); "
                    + "skipping custadv/Banner BEFORE/AFTER evidence");
        }

        PaperlessSideEffectStateUtil.Snapshot beforeState = null;
        PaperlessSideEffectStateUtil.Snapshot duringState = null;
        String beforeTokenPreview = null;

        try {
            if (canCaptureState) {
                beforeState = PaperlessSideEffectStateUtil.capture(
                        customerCode, premisesCode, testCondition.name() + " BEFORE");
                beforeTokenPreview = PaperlessTokenPperEvidenceUtil.tryAnyCustAdvToken(customerCode, premisesCode);
                DualReportManager.logInfo(testCondition.name() + " — TOKEN BEFORE: "
                        + PaperlessTokenPperEvidenceUtil.displayToken(beforeTokenPreview));
                DualReportManager.logInfo(testCondition.name() + " — CONFIRM request token preview: "
                        + PaperlessTokenPperEvidenceUtil.displayToken(payload.getToken()));
            }

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

            if (canCaptureState) {
                if (isRollback) {
                    duringState = PaperlessSideEffectStateUtil.capture(
                            customerCode, premisesCode, testCondition.name() + " DURING (after Confirm response)");
                    PaperlessSideEffectStateUtil.logDuring(duringState, testCondition.name());
                }

                PaperlessSideEffectStateUtil.Snapshot afterState = PaperlessSideEffectStateUtil.capture(
                        customerCode, premisesCode, testCondition.name() + " AFTER");
                String afterTokenPreview = PaperlessTokenPperEvidenceUtil.tryAnyCustAdvToken(customerCode, premisesCode);
                DualReportManager.logInfo(testCondition.name() + " — TOKEN AFTER: "
                        + PaperlessTokenPperEvidenceUtil.displayToken(afterTokenPreview));
                PaperlessTokenPperEvidenceUtil.logTokenBeforeAfter(
                        testCondition.name(), beforeTokenPreview, afterTokenPreview);
                PaperlessTokenPperEvidenceUtil.logRecentPperRecords(testCondition.name(), customerCode, premisesCode);

                PaperlessSideEffectStateUtil.logBeforeAfter(
                        beforeState, afterState, testCondition.name() + " BEFORE/AFTER", expectUnchanged);

                DualReportManager.logInfo(testCondition.name() + " — API result: success="
                        + response.jsonPath().get("success")
                        + ", errorCode=" + response.jsonPath().get("errorCode")
                        + ", errorMessage=" + response.jsonPath().getString("errorMessage")
                        + ", accountType=" + response.jsonPath().getString("data.accountType")
                        + ", billStatus=" + response.jsonPath().getString("data.billDeliveryOptionStatus")
                        + ", corrStatus=" + response.jsonPath().getString("data.corrDeliveryOptionStatus"));
            }
        } finally {
            if (preferenceFailAccountLock) {
                BannerAccountRowLockUtil.release();
            }
            if (notificationFailClearOverride) {
                BannerTestEmailOverrideUtil.restore();
                ConfirmPaperlessEnrollmentTokenSetupHelper.restoreNotificationFailureBannerEmail(
                        testContext.getCustomerCode());
                if (canCaptureState && beforeState != null) {
                    PaperlessSideEffectStateUtil.Snapshot afterRestore = PaperlessSideEffectStateUtil.capture(
                            customerCode, premisesCode, testCondition.name() + " AFTER RESTORE");
                    DualReportManager.logInfo(testCondition.name()
                            + " — ROLLBACK evidence (BEFORE vs AFTER RESTORE)");
                    PaperlessSideEffectStateUtil.logBeforeAfter(
                            beforeState,
                            afterRestore,
                            testCondition.name() + " ROLLBACK BEFORE/AFTER RESTORE",
                            true);
                }
            }
        }
    }

    /**
     * Negatives where Confirm must not update email/token/PPER/enrollment (or rollback restores prior state).
     * Positives expect enrollment/token/PPER changes — log PRE vs POST without asserting unchanged.
     */
    private static boolean expectsNoSideEffectUpdate(ConfirmPaperlessEnrollmentLabel testCondition) {
        return switch (testCondition) {
            case TC_120__Negative__Missing_Request_ID_,
                 TC_121__Negative__Invalid_Request_ID_Length_,
                 TC_122__Negative__Duplicate_Request_ID_,
                 TC_123__Negative__Missing_Token_,
                 TC_124__Negative__Token_Validation_Failed_,
                 TC_125__Negative__Token_Not_Found__No_Stored_Token_Record_,
                 TC_126__Negative__Token_Expired__Stored_Expiry_Passed_,
                 TC_127__Negative__Token_Expired__Passive_Email_Change_Invalidation_,
                 TC_128__Negative__Token_Already_Used_,
                 TC_129__Negative__Token_Account_Mismatch_,
                 TC_130__Negative__Token_Not_Found__No_Active_Pending_PPER_,
                 TC_131__Negative__Paperless_Preference_Update_Failed__ACTIVE_,
                 TC_132__Negative__Paperless_Preference_Update_Failed__NEW_ -> true;
            // TC_133 rolls back — final AFTER RESTORE should match BEFORE
            case TC_133__Negative__Confirmation_Notification_Failure_Rollback_ -> false;
            default -> false;
        };
    }

    private static String firstNonBlank(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
