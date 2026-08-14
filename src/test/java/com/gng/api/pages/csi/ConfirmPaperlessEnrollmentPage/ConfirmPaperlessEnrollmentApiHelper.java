package com.gng.api.pages.csi.ConfirmPaperlessEnrollmentPage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.ConfirmPaperlessEnrollment.ConfirmPaperlessEnrollmentRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.ConfirmPaperlessEnrollment.ConfirmPaperlessEnrollmentLabel;
import com.gng.api.steps.csi.UpdatePaperlessCommunications.UpdatePaperlessCommunicationsLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConfirmPaperlessEnrollmentApiHelper {

    private final ConfirmPaperlessEnrollmentTokenSetupHelper tokenSetupHelper;

    public ConfirmPaperlessEnrollmentApiHelper(TestContext testContext) {
        this.tokenSetupHelper = new ConfirmPaperlessEnrollmentTokenSetupHelper(testContext);
    }

    ConfirmPaperlessEnrollmentRequest preparePayload(ConfirmPaperlessEnrollmentLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        return BasePage.deserializeJsonToPojo(
                ConfirmPaperlessEnrollmentLabel.confirm_paperless_enrollment.toString(),
                ConfirmPaperlessEnrollmentRequest.class);
    }

    public void preparePayloadForTestCondition(ConfirmPaperlessEnrollmentRequest payload,
                                               ConfirmPaperlessEnrollmentLabel testCondition) {
        switch (testCondition) {

            // ---------------- NEGATIVE TEST CASES ----------------

            case TC_120__Negative__Missing_Request_ID_:
                payload.setRequestID("");
                payload.setToken("placeholderToken");
                break;

            case TC_121__Negative__Invalid_Request_ID_Length_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(33));
                payload.setToken("placeholderToken");
                break;

            case TC_122__Negative__Duplicate_Request_ID_:
                payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());
                payload.setToken("placeholderToken");
                break;

            case TC_122A__Negative__Invalid_Origin_:
                // Valid unused token + non-allowed origin (FTD: Macquarium is valid → expect 10085).
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setToken(tokenSetupHelper.bootstrapEnrollmentAndGetToken(
                        UpdatePaperlessCommunicationsLabel.TC_98__Positive__Active_Account_Bill_Enrollment_Initiated_));
                payload.setOrigin(GlobalEnums.InvalidValues.INVALID_ORIGIN.getValue());
                break;

            case TC_123__Negative__Missing_Token_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setToken("");
                break;

            case TC_124__Negative__Token_Validation_Failed_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setToken("InvalidTokenNotDecryptable");
                break;

            case TC_125__Negative__Token_Not_Found__No_Stored_Token_Record_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setToken(tokenSetupHelper.getTokenNotFoundNoStoredRecordToken(
                        UpdatePaperlessCommunicationsLabel.TC_98__Positive__Active_Account_Bill_Enrollment_Initiated_));
                break;

            case TC_126__Negative__Token_Expired__Stored_Expiry_Passed_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setToken(tokenSetupHelper.getExpiredTokenForNegativeTest());
                break;

            case TC_127__Negative__Token_Expired__Passive_Email_Change_Invalidation_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setToken(tokenSetupHelper.getPassiveEmailInvalidationToken(
                        UpdatePaperlessCommunicationsLabel.TC_98__Positive__Active_Account_Bill_Enrollment_Initiated_));
                break;

            case TC_128__Negative__Token_Already_Used_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setToken(tokenSetupHelper.resolveUsedTokenForNegativeTest(
                        UpdatePaperlessCommunicationsLabel.TC_98__Positive__Active_Account_Bill_Enrollment_Initiated_));
                break;

            case TC_129__Negative__Token_Account_Mismatch_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setToken(tokenSetupHelper.getAccountMismatchTokenForNegativeTest(
                        UpdatePaperlessCommunicationsLabel.TC_98__Positive__Active_Account_Bill_Enrollment_Initiated_));
                break;

            case TC_130__Negative__Token_Not_Found__No_Active_Pending_PPER_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setToken(tokenSetupHelper.getNoActivePendingPperToken(
                        UpdatePaperlessCommunicationsLabel.TC_98__Positive__Active_Account_Bill_Enrollment_Initiated_));
                break;

            case TC_131__Negative__Paperless_Preference_Update_Failed__ACTIVE_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setToken(tokenSetupHelper.getActivePreferenceUpdateFailedToken());
                break;

            case TC_132__Negative__Paperless_Preference_Update_Failed__NEW_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setToken(tokenSetupHelper.getNewPreferenceUpdateFailedToken());
                break;

            case TC_133__Negative__Confirmation_Notification_Failure_Rollback_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setToken(tokenSetupHelper.getConfirmationNotificationFailureToken());
                break;

            // ---------------- POSITIVE TEST CASES ----------------

            case TC_134__Positive__ConfirmPaperlessEnrollment_accountType_Format__ACTIVE_:
            case TC_136__Positive__billDeliveryOptionStatus_Format__CONFIRMED_:
            case TC_139__Positive__corrDeliveryOptionStatus_Format__NO_CHANGE_:
            case TC_140__Positive__confirmationDateTime_Format_:
            case TC_141__Positive__Active_Account_Bill_Confirmation_:
            case TC_151__Positive__Token_Marked_Used_and_PPER_Archived_After_Successful_Active_Finalization_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setToken(tokenSetupHelper.bootstrapEnrollmentAndGetToken(
                        UpdatePaperlessCommunicationsLabel.TC_98__Positive__Active_Account_Bill_Enrollment_Initiated_));
                break;

            case TC_137__Positive__billDeliveryOptionStatus_Format__NO_CHANGE_:
            case TC_138__Positive__corrDeliveryOptionStatus_Format__CONFIRMED_:
            case TC_145__Positive__Active_Account_Correspondence_Confirmation_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setToken(tokenSetupHelper.bootstrapEnrollmentAndGetToken(
                        UpdatePaperlessCommunicationsLabel.TC_86__Positive__billDeliveryOptionStatus_Format__NO_CHANGE_));
                break;

            case TC_135__Positive__ConfirmPaperlessEnrollment_accountType_Format__NEW_:
            case TC_142__Positive__New_Account_Bill_Confirmation_:
            case TC_152__Positive__Token_Marked_Used_and_PPER_Archived_After_Successful_New_Finalization_:
            case TC_153__Positive__New_Account_Finalization_Uses_Confirmation_Dates_Rather_Than_Final_Preferences_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setToken(tokenSetupHelper.bootstrapEnrollmentAndGetToken(
                        UpdatePaperlessCommunicationsLabel.TC_99__Positive__New_Account_Bill_Enrollment_Initiated_));
                break;

            case TC_143__Positive__Active_Account_Both_Channels_Confirmation_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setToken(tokenSetupHelper.bootstrapEnrollmentAndGetToken(
                        UpdatePaperlessCommunicationsLabel.TC_100__Positive__Active_Account_Both_Channels_Enrollment_Initiated_));
                break;

            case TC_144__Positive__New_Account_Both_Channels_Confirmation_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setToken(tokenSetupHelper.bootstrapEnrollmentAndGetToken(
                        UpdatePaperlessCommunicationsLabel.TC_101__Positive__New_Account_Both_Channels_Enrollment_Initiated_));
                break;

            case TC_146__Positive__New_Account_Correspondence_Confirmation_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setToken(tokenSetupHelper.bootstrapNewCorrEnrollmentAndGetToken());
                break;

            case TC_147__Positive__Account_Transitions_from_NEW_to_ACTIVE_Before_Confirmation_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setToken(tokenSetupHelper.bootstrapTc147NewToActiveTransitionAndGetToken());
                break;

            case TC_148__Positive__Current_Aggregated_PPER_State_Confirmed_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setToken(tokenSetupHelper.bootstrapTc148AggregatedPperAndGetToken());
                break;

            case TC_149__Positive__No_Email_on_File_Does_Not_Prevent_Success_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setToken(tokenSetupHelper.bootstrapTc149ClearBannerEmailBeforeConfirmAndGetToken());
                break;

            case TC_150__Positive__Confirmation_Email_Failure_Does_Not_Prevent_Success_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setToken(tokenSetupHelper.bootstrapTc150ConfirmationEmailFailureAndGetToken());
                break;

            default:
                log.warn("Unhandled test condition: {}", testCondition);
                break;
        }
    }
}
