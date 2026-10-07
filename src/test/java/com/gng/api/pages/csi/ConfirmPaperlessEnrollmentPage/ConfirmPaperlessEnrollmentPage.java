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

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

import static com.gng.api.constants.ApiEndPoint.CONFIRM_PAPERLESS_ENROLLMENT;

/**
 * ConfirmPaperlessEnrollment page object.
 * Logs BEFORE/AFTER (and DURING for rollback) custadv + Banner/PPER state for report evidence.
 */
@Slf4j
public class ConfirmPaperlessEnrollmentPage extends BasePage {

    /** Expected UTC format: YYYY-MM-DDTHH:MM:SSZ. */
    private static final Pattern CONFIRMATION_DATE_TIME_ISO8601_UTC =
            Pattern.compile("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z$");

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
                        "TC_150 — REVIEWER NOTE: an invalid/fake email ('" + failureAddress
                                + "') is used so the confirmation email cannot be sent/delivered. "
                                + "Confirm must still succeed; a failed email does not fail enrollment.");
                DualReportManager.logInfo(
                        "TC_150 — setup: routing confirmation email to '" + failureAddress
                                + "' (previous NEW_TEST_EMAIL_ADDR='" + beforeOverride + "')");
                BannerTestEmailOverrideUtil.redirectToUndeliverableForSoftDeliveryFailure(failureAddress);
                String activeOverride = BannerTestEmailOverrideUtil.readCurrentOverrideValue();
                DualReportManager.logInfo(
                        "TC_150 — invalid/fake email now active: '" + activeOverride + "'");
                Assert.assertEquals(
                        activeOverride == null ? null : activeOverride.trim(),
                        failureAddress,
                        "TC_150 requires invalid/fake email '" + failureAddress
                                + "' to be active before Confirm");
            }
            if (tc149MidClear) {
                // Banner email must still be present for token validation. Clearing it before
                // Confirm returns 40287, so it is removed shortly after Confirm starts.
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
                        "TC_149 — REVIEWER NOTE: Banner email is present when the token is validated. "
                                + "It will then be removed so there is no Banner email on file. "
                                + "Confirm must still succeed; the confirmation email is skipped and "
                                + "that does not fail the test.");
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
                ConfirmPaperlessEnrollmentTokenSetupHelper.logTc149BannerRemovalEvidence(
                        customerCode, tc149EmailCleared.get());
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
                        testCondition, response, afterState, tc149EmailCleared.get(),
                        customerCode, premisesCode);
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
                        "TC_150 — restored email routing after Confirm");
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
                    "TC_149 — REVIEWER NOTE: Banner email is present when the token is validated "
                            + "(Banner email='" + bannerEmail + "'). It will then be removed so there "
                            + "is no Banner email on file. Confirm still succeeds; the confirmation "
                            + "email is skipped and that does not fail the test.");
        } else if (testCondition == ConfirmPaperlessEnrollmentLabel
                .TC_150__Positive__Confirmation_Email_Failure_Does_Not_Prevent_Success_) {
            DualReportManager.logInfo(
                    "TC_150 — REVIEWER NOTE: an invalid/fake email is used so the confirmation "
                            + "email cannot be sent/delivered. Confirm still succeeds; a failed email "
                            + "does not fail enrollment. Token unused="
                            + (firstNonBlank(beforeState.custAdv("date_time_link_confirmed")) == null)
                            + ", pending bill='" + beforeState.enrollment("pendingBillType")
                            + "', pending corr='" + beforeState.enrollment("pendingCorrType") + "'.");
        }
    }

    private static void assertTcSpecificPostConfirmEvidence(ConfirmPaperlessEnrollmentLabel testCondition,
                                                            Response response,
                                                            PaperlessSideEffectStateUtil.Snapshot afterState,
                                                            boolean tc149EmailCleared,
                                                            String customerCode,
                                                            String premisesCode) {
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
            String confirmationDateTime = response.jsonPath().getString("data.confirmationDateTime");
            String accountType = response.jsonPath().getString("data.accountType");
            String billStatus = response.jsonPath().getString("data.billDeliveryOptionStatus");
            String corrStatus = response.jsonPath().getString("data.corrDeliveryOptionStatus");
            DualReportManager.logInfo(
                    "TC_149 — REVIEWER NOTE: Banner email was present when the token was validated, "
                            + "then there was no Banner email on file (removed=" + tc149EmailCleared
                            + "). Confirm succeeded; the confirmation email was skipped and that "
                            + "does not fail the test. "
                            + "success=" + response.jsonPath().get("success")
                            + ", errorCode=" + response.jsonPath().get("errorCode")
                            + ", errorMessage='" + nullToEmpty(response.jsonPath().getString("errorMessage")) + "'"
                            + ", accountType='" + accountType
                            + "', bill='" + billStatus
                            + "', corr='" + corrStatus
                            + "', confirmationDateTime='" + confirmationDateTime + "'.");
            assertSuccessfulConfirmResponse(response, "TC_149");
            assertActiveAccountType(accountType, "TC_149");
            assertConfirmedOrNoChange(billStatus, "TC_149 billDeliveryOptionStatus");
            assertConfirmedOrNoChange(corrStatus, "TC_149 corrDeliveryOptionStatus");
            assertAtLeastOneChannelConfirmed(billStatus, corrStatus, "TC_149");
            assertConfirmationDateTimeIso8601(confirmationDateTime, "TC_149");
            Assert.assertTrue(tc149EmailCleared,
                    "TC_149 expects no Banner email on file after token validation "
                            + "(confirmation email skipped)");
        } else if (testCondition == ConfirmPaperlessEnrollmentLabel
                .TC_150__Positive__Confirmation_Email_Failure_Does_Not_Prevent_Success_) {
            String failureAddress = ConfirmPaperlessEnrollmentTokenSetupHelper.getTc150FailureEmailAddress();
            String overrideAfterConfirm = BannerTestEmailOverrideUtil.readCurrentOverrideValue();
            String confirmationDateTime = response.jsonPath().getString("data.confirmationDateTime");
            String accountType = response.jsonPath().getString("data.accountType");
            String billStatus = response.jsonPath().getString("data.billDeliveryOptionStatus");
            String corrStatus = response.jsonPath().getString("data.corrDeliveryOptionStatus");
            DualReportManager.logInfo(
                    "TC_150 — REVIEWER NOTE: invalid/fake email '" + failureAddress
                            + "' was used so the confirmation email cannot be sent/delivered "
                            + "(still active='" + overrideAfterConfirm + "'). Confirm succeeded; "
                            + "a failed email does not fail enrollment. "
                            + "success=" + response.jsonPath().get("success")
                            + ", errorCode=" + response.jsonPath().get("errorCode")
                            + ", errorMessage='" + nullToEmpty(response.jsonPath().getString("errorMessage")) + "'"
                            + ", accountType='" + accountType
                            + "', bill='" + billStatus
                            + "', corr='" + corrStatus
                            + "', confirmationDateTime='" + confirmationDateTime + "'.");
            Assert.assertEquals(
                    overrideAfterConfirm == null ? null : overrideAfterConfirm.trim(),
                    failureAddress,
                    "TC_150 expects invalid/fake email '" + failureAddress
                            + "' to remain active during Confirm");
            assertSuccessfulConfirmResponse(response, "TC_150");
            assertActiveAccountType(accountType, "TC_150");
            assertConfirmedOrNoChange(billStatus, "TC_150 billDeliveryOptionStatus");
            assertConfirmedOrNoChange(corrStatus, "TC_150 corrDeliveryOptionStatus");
            assertAtLeastOneChannelConfirmed(billStatus, corrStatus, "TC_150");
            assertConfirmationDateTimeIso8601(confirmationDateTime, "TC_150");
            assertTokenUsedAndPperArchived(afterState, customerCode, premisesCode, "TC_150");
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

    private static void assertSuccessfulConfirmResponse(Response response, String tcId) {
        Assert.assertTrue(Boolean.TRUE.equals(response.jsonPath().getBoolean("success")),
                tcId + " expects success=true; got errorCode="
                        + response.jsonPath().get("errorCode")
                        + " errorMessage=" + response.jsonPath().getString("errorMessage"));
        Assert.assertEquals(response.jsonPath().getInt("errorCode"), 0,
                tcId + " expects errorCode=0");
        String errorMessage = nullToEmpty(response.jsonPath().getString("errorMessage"));
        Assert.assertEquals(errorMessage, "",
                tcId + " expects empty errorMessage; got '" + errorMessage + "'");
        Assert.assertNotNull(response.jsonPath().get("data"),
                tcId + " expects data to contain a ConfirmPaperlessEnrollment object");
    }

    private static void assertActiveAccountType(String accountType, String tcId) {
        Assert.assertTrue("ACTIVE".equals(accountType) || "A".equals(accountType),
                tcId + " expects accountType=ACTIVE; got '" + accountType + "'");
    }

    private static void assertConfirmedOrNoChange(String status, String fieldLabel) {
        Assert.assertTrue("CONFIRMED".equals(status) || "NO_CHANGE".equals(status),
                fieldLabel + " expects CONFIRMED or NO_CHANGE; got '" + status + "'");
    }

    private static void assertAtLeastOneChannelConfirmed(String billStatus, String corrStatus, String tcId) {
        Assert.assertTrue("CONFIRMED".equals(billStatus) || "CONFIRMED".equals(corrStatus),
                tcId + " expects at least one channel CONFIRMED; "
                        + "bill='" + billStatus + "', corr='" + corrStatus + "'");
    }

    private static void assertConfirmationDateTimeIso8601(String confirmationDateTime, String tcId) {
        Assert.assertNotNull(confirmationDateTime,
                tcId + " expects confirmationDateTime populated");
        Assert.assertFalse(confirmationDateTime.isBlank(),
                tcId + " expects confirmationDateTime populated, not blank");
        Assert.assertTrue(
                CONFIRMATION_DATE_TIME_ISO8601_UTC.matcher(confirmationDateTime.trim()).matches(),
                tcId + " expects confirmationDateTime in YYYY-MM-DDTHH:MM:SSZ (UTC); got '"
                        + confirmationDateTime + "'");
    }

    private static void assertTokenUsedAndPperArchived(PaperlessSideEffectStateUtil.Snapshot afterState,
                                                       String customerCode,
                                                       String premisesCode,
                                                       String tcId) {
        String linkConfirmed = firstNonBlank(afterState.custAdv("date_time_link_confirmed"));
        DualReportManager.logInfo(
                tcId + " — token marked used: date_time_link_confirmed='" + linkConfirmed + "'");
        Assert.assertNotNull(linkConfirmed,
                tcId + " expects the token to be marked used (date_time_link_confirmed populated)");

        Map<String, String> afterPper = PaperlessTokenPperEvidenceUtil.capturePperSnapshot(
                customerCode, premisesCode);
        Map<String, Object> pendingBill = ApplicationContext.get().getDbAction()
                .tryGetActivePendingBillPperForAccount(customerCode, premisesCode);
        String completedDate = firstNonBlank(afterPper.get("tokenCompletedDate"));
        boolean pendingPperGone = pendingBill == null;
        DualReportManager.logInfo(
                tcId + " — PPER archived: completedDate='" + completedDate
                        + "', pending bill PPER still active=" + !pendingPperGone
                        + ", pendingBill='" + afterPper.get("pendingBillType")
                        + "', pendingCorr='" + afterPper.get("pendingCorrType") + "'");
        Assert.assertTrue(completedDate != null || pendingPperGone,
                tcId + " expects the associated PPER record to be archived");
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    private static String firstNonBlank(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
