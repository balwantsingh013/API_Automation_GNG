package com.gng.api.pages.csi.ConfirmPaperlessEnrollmentPage;

import com.gng.api.context.ApplicationContext;
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
import org.testng.Assert;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.gng.api.constants.ApiEndPoint.CONFIRM_PAPERLESS_ENROLLMENT;

/**
 * ConfirmPaperlessEnrollment page object.
 * Logs BEFORE/AFTER (and DURING for rollback) custadv + Banner/PPER state for report evidence.
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
        boolean softConfirmEmailDeliveryFailure =
                testCondition == ConfirmPaperlessEnrollmentLabel.TC_150__Positive__Confirmation_Email_Failure_Does_Not_Prevent_Success_;
        boolean tc147Restore =
                testCondition == ConfirmPaperlessEnrollmentLabel.TC_147__Positive__Account_Transitions_from_NEW_to_ACTIVE_Before_Confirmation_;
        boolean tc149MidClear =
                testCondition == ConfirmPaperlessEnrollmentLabel.TC_149__Positive__No_Email_on_File_Does_Not_Prevent_Success_;
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
        ExecutorService tc149ClearExecutor = null;
        Future<?> tc149ClearFuture = null;
        AtomicBoolean tc149EmailCleared = new AtomicBoolean(false);

        try {
            if (canCaptureState) {
                beforeState = PaperlessSideEffectStateUtil.capture(
                        customerCode, premisesCode, testCondition.name() + " BEFORE");
                beforeTokenPreview = PaperlessTokenPperEvidenceUtil.tryAnyCustAdvToken(customerCode, premisesCode);
                DualReportManager.logInfo(testCondition.name() + " — TOKEN BEFORE: "
                        + PaperlessTokenPperEvidenceUtil.displayToken(beforeTokenPreview));
                DualReportManager.logInfo(testCondition.name() + " — CONFIRM request token preview: "
                        + PaperlessTokenPperEvidenceUtil.displayToken(payload.getToken()));
                logTcSpecificPreConfirmEvidence(testCondition, customerCode, premisesCode, beforeState);
            }

            if (preferenceFailAccountLock) {
                BannerAccountRowLockUtil.lockAccountRow(
                        testContext.getCustomerCode(), testContext.getPremisesCode());
            }
            if (notificationFailClearOverride) {
                BannerTestEmailOverrideUtil.clearForEmailNotificationFailure();
            }
            if (softConfirmEmailDeliveryFailure) {
                String failureAddress = ConfirmPaperlessEnrollmentTokenSetupHelper.getTc150FailureEmailAddress();
                String beforeOverride = BannerTestEmailOverrideUtil.readCurrentOverrideValue();
                DualReportManager.logInfo(
                        "TC_150 — FAILED SEND SETUP: NEW_TEST_EMAIL_ADDR before override='"
                                + beforeOverride + "'");
                DualReportManager.logInfo(
                        "TC_150 — FAILED SEND SETUP: forcing confirmation-email send failure by setting "
                                + "NEW_TEST_EMAIL_ADDR='" + failureAddress
                                + "' (NOT blank — blank is TC_133 / 40321 rollback)");
                BannerTestEmailOverrideUtil.redirectToUndeliverableForSoftDeliveryFailure(failureAddress);
                String activeOverride = BannerTestEmailOverrideUtil.readCurrentOverrideValue();
                DualReportManager.logInfo(
                        "TC_150 — FAILED SEND VALIDATION (pre-Confirm): NEW_TEST_EMAIL_ADDR active='"
                                + activeOverride + "'");
                Assert.assertEquals(
                        activeOverride == null ? null : activeOverride.trim(),
                        failureAddress,
                        "TC_150 requires NEW_TEST_EMAIL_ADDR='" + failureAddress
                                + "' active before Confirm so failed send is forced and visible");
            }
            if (tc149MidClear) {
                // FTD: email present during token validation, cleared before confirmation email.
                // Clearing before the API call returns 40287; clear shortly after Confirm starts.
                final String midClearCustomer = customerCode;
                tc149ClearExecutor = Executors.newSingleThreadExecutor(r -> {
                    Thread t = new Thread(r, "tc149-banner-email-mid-clear");
                    t.setDaemon(true);
                    return t;
                });
                tc149ClearFuture = tc149ClearExecutor.submit(() -> {
                    try {
                        Thread.sleep(350L);
                        tc149EmailCleared.set(
                                ConfirmPaperlessEnrollmentTokenSetupHelper.clearBannerEmailMidConfirm(
                                        midClearCustomer));
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        DualReportManager.logInfo("TC_149 — mid-Confirm Banner email clear interrupted");
                    } catch (RuntimeException ex) {
                        DualReportManager.logInfo(
                                "TC_149 — mid-Confirm Banner email clear failed: " + ex.getMessage());
                        throw ex;
                    }
                });
                DualReportManager.logInfo(
                        "TC_149 — scheduled mid-Confirm Banner email clear (~350ms after Confirm start) "
                                + "so token validates with email present, then confirmation email is skipped");
            }
            setRequestSpecification(payload, testContext.getAuthToken());
            Response response = sendRequest(HttpPost.METHOD_NAME, CONFIRM_PAPERLESS_ENROLLMENT, 200);
            ConfirmPaperlessEnrollmentResponse confirmPaperlessEnrollmentResponse =
                    deserializeResponseToPojo(response, ConfirmPaperlessEnrollmentResponse.class);
            testContext.setConfirmPaperlessEnrollmentResponse(confirmPaperlessEnrollmentResponse);
            testContext.setResponse(response);

            if (tc149ClearFuture != null) {
                try {
                    tc149ClearFuture.get(10, TimeUnit.SECONDS);
                } catch (Exception ex) {
                    DualReportManager.logInfo(
                            "TC_149 — waiting for mid-Confirm clear finished with: " + ex.getMessage());
                }
            }

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

                assertTcSpecificPostConfirmEvidence(
                        testCondition, response, afterState, tc149EmailCleared.get());
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
            if (softConfirmEmailDeliveryFailure) {
                BannerTestEmailOverrideUtil.restore();
                ConfirmPaperlessEnrollmentTokenSetupHelper.clearTc150FailureEmailAddress();
                DualReportManager.logInfo(
                        "TC_150 — restored NEW_TEST_EMAIL_ADDR after failed-send Confirm");
            }
            if (tc147Restore) {
                ConfirmPaperlessEnrollmentTokenSetupHelper.restoreTc147AccountStatus(customerCode, premisesCode);
            }
            if (tc149MidClear) {
                if (tc149ClearFuture != null) {
                    tc149ClearFuture.cancel(true);
                }
                if (tc149ClearExecutor != null) {
                    tc149ClearExecutor.shutdownNow();
                }
                ConfirmPaperlessEnrollmentTokenSetupHelper.restoreTc149BannerEmail();
            }
        }
    }

    private static void logTcSpecificPreConfirmEvidence(ConfirmPaperlessEnrollmentLabel testCondition,
                                                        String customerCode,
                                                        String premisesCode,
                                                        PaperlessSideEffectStateUtil.Snapshot beforeState) {
        if (testCondition == ConfirmPaperlessEnrollmentLabel
                .TC_147__Positive__Account_Transitions_from_NEW_to_ACTIVE_Before_Confirmation_) {
            DualReportManager.logInfo(
                    "TC_147 — PRE-CONFIRM: account must already be ACTIVE (transitioned after NEW enroll). "
                            + beforeState.summary());
        } else if (testCondition == ConfirmPaperlessEnrollmentLabel
                .TC_148__Positive__Current_Aggregated_PPER_State_Confirmed_) {
            DualReportManager.logInfo(
                    "TC_148 — PRE-CONFIRM: aggregated PPER should be latest E/E from multi-update setup. "
                            + beforeState.summary());
        } else if (testCondition == ConfirmPaperlessEnrollmentLabel
                .TC_149__Positive__No_Email_on_File_Does_Not_Prevent_Success_) {
            String bannerEmail = ApplicationContext.get().getDbAction()
                    .getActiveBannerEmailForCustomer(customerCode);
            DualReportManager.logInfo(
                    "TC_149 — PRE-CONFIRM evidence: Banner email still present for token validation "
                            + "(bannerEmail='" + bannerEmail
                            + "'). Mid-Confirm clear will run after validation so confirmation email is skipped.");
        } else if (testCondition == ConfirmPaperlessEnrollmentLabel
                .TC_150__Positive__Confirmation_Email_Failure_Does_Not_Prevent_Success_) {
            DualReportManager.logInfo(
                    "TC_150 — PRE-CONFIRM: token unused + Banner email on file (preference path ready). "
                            + beforeState.summary());
        }
    }

    private static void assertTcSpecificPostConfirmEvidence(ConfirmPaperlessEnrollmentLabel testCondition,
                                                            Response response,
                                                            PaperlessSideEffectStateUtil.Snapshot afterState,
                                                            boolean tc149EmailCleared) {
        if (testCondition == ConfirmPaperlessEnrollmentLabel
                .TC_147__Positive__Account_Transitions_from_NEW_to_ACTIVE_Before_Confirmation_) {
            String accountType = response.jsonPath().getString("data.accountType");
            DualReportManager.logInfo(
                    "TC_147 — POST-CONFIRM evidence: accountType='" + accountType
                            + "' (must be ACTIVE because Banner transitioned NEW→ACTIVE before Confirm)");
            Assert.assertEquals(accountType, "ACTIVE",
                    "TC_147 expects accountType=ACTIVE after NEW→ACTIVE transition before Confirm");
        } else if (testCondition == ConfirmPaperlessEnrollmentLabel
                .TC_148__Positive__Current_Aggregated_PPER_State_Confirmed_) {
            String billStatus = response.jsonPath().getString("data.billDeliveryOptionStatus");
            String corrStatus = response.jsonPath().getString("data.corrDeliveryOptionStatus");
            DualReportManager.logInfo(
                    "TC_148 — POST-CONFIRM evidence: billStatus='" + billStatus
                            + "', corrStatus='" + corrStatus
                            + "' (latest aggregated E/E must win — both channels confirmed/updated, "
                            + "not prior bill-only state)");
            Assert.assertEquals(billStatus, "CONFIRMED",
                    "TC_148 expects billDeliveryOptionStatus=CONFIRMED from latest E/E aggregate");
            Assert.assertEquals(corrStatus, "CONFIRMED",
                    "TC_148 expects corrDeliveryOptionStatus=CONFIRMED from latest E/E aggregate");
        } else if (testCondition == ConfirmPaperlessEnrollmentLabel
                .TC_149__Positive__No_Email_on_File_Does_Not_Prevent_Success_) {
            DualReportManager.logInfo(
                    "TC_149 — STEP 4/5 POST-CONFIRM evidence: midClearSucceeded=" + tc149EmailCleared
                            + "; enrollment must succeed with confirmation email skipped. AFTER: "
                            + afterState.summary());
            Assert.assertTrue(Boolean.TRUE.equals(response.jsonPath().getBoolean("success")),
                    "TC_149 expects success=true when Banner email cleared after token validation "
                            + "(mid-Confirm); got errorCode=" + response.jsonPath().get("errorCode")
                            + " errorMessage=" + response.jsonPath().getString("errorMessage"));
            Assert.assertEquals(response.jsonPath().getInt("errorCode"), 0,
                    "TC_149 expects errorCode=0 when Banner email cleared after token validation");
            Assert.assertTrue(tc149EmailCleared,
                    "TC_149 expects Banner email cleared mid-Confirm (confirmation email skipped evidence)");
        } else if (testCondition == ConfirmPaperlessEnrollmentLabel
                .TC_150__Positive__Confirmation_Email_Failure_Does_Not_Prevent_Success_) {
            String failureAddress = ConfirmPaperlessEnrollmentTokenSetupHelper.getTc150FailureEmailAddress();
            String overrideAfterConfirm = BannerTestEmailOverrideUtil.readCurrentOverrideValue();
            String billStatus = response.jsonPath().getString("data.billDeliveryOptionStatus");
            DualReportManager.logInfo(
                    "TC_150 — FAILED SEND VALIDATION (post-Confirm, before restore):"
                            + " NEW_TEST_EMAIL_ADDR still='" + overrideAfterConfirm + "'"
                            + " (forced failure address='" + failureAddress + "');"
                            + " API success=" + response.jsonPath().get("success")
                            + ", errorCode=" + response.jsonPath().get("errorCode")
                            + ", billStatus=" + billStatus
                            + "; AFTER state: " + afterState.summary());
            Assert.assertEquals(
                    overrideAfterConfirm == null ? null : overrideAfterConfirm.trim(),
                    failureAddress,
                    "TC_150 failed-send validation requires NEW_TEST_EMAIL_ADDR still='"
                            + failureAddress + "' after Confirm (override must have been active during send)");
            Assert.assertTrue(Boolean.TRUE.equals(response.jsonPath().getBoolean("success")),
                    "TC_150 expects success=true despite forced confirmation-email send failure");
            Assert.assertEquals(response.jsonPath().getInt("errorCode"), 0,
                    "TC_150 expects errorCode=0 despite forced confirmation-email send failure");
            Assert.assertEquals(billStatus, "CONFIRMED",
                    "TC_150 expects enrollment still finalized (bill CONFIRMED) when confirmation email send fails");
            DualReportManager.logInfo(
                    "TC_150 — FAILED SEND VALIDATED: confirmation email was forced to undeliverable '"
                            + failureAddress
                            + "' via NEW_TEST_EMAIL_ADDR during Confirm; enrollment still succeeded "
                            + "(success=true, errorCode=0, billDeliveryOptionStatus=CONFIRMED). "
                            + "This is distinct from TC_133 blank override → 40321 rollback.");
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
                 TC_122A__Negative__Invalid_Origin_,
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
