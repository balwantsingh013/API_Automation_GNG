package com.gng.api.pages.csi.UpdatePaperlessCommunicationsPage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.UpdatePaperlessCommunications.UpdatePaperlessCommunicationsRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.UpdatePaperlessCommunications.UpdatePaperlessCommunicationsLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class UpdatePaperlessCommunicationsApiHelper {

    private final TestContext testContext;

    public UpdatePaperlessCommunicationsApiHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    UpdatePaperlessCommunicationsRequest preparePayload(UpdatePaperlessCommunicationsLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        String jsonFileName = apiLabel.equals(UpdatePaperlessCommunicationsLabel.update_paperless_communications)
                ? UpdatePaperlessCommunicationsLabel.update_paperless_communications.toString()
                : UpdatePaperlessCommunicationsLabel.update_paperless_communications_mandatory.toString();
        return BasePage.deserializeJsonToPojo(jsonFileName, UpdatePaperlessCommunicationsRequest.class);
    }

    public void preparePayloadForTestCondition(UpdatePaperlessCommunicationsRequest payload, UpdatePaperlessCommunicationsLabel testCondition) {
        Map<String, Object> accountData = null;

        switch (testCondition) {

            // ---------------- NEGATIVE TEST CASES ----------------

            case TC_53__Negative__Missing_Request_ID_:
                payload.setRequestID("");
                break;

            case TC_54__Negative__Invalid_Request_ID_Length_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(33));
                break;

            case TC_55__Negative__Duplicate_Request_ID_:
                payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());
                break;

            case TC_56__Negative__Missing_customerCode_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode("");
                break;

            case TC_57__Negative__Invalid_customerCode_Length_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateDigits(10));
                break;

            case TC_58__Negative__Invalid_customerCode_Format_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateString(6));
                break;

            case TC_59__Negative__Missing_premisesCode_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPremisesCode("");
                break;

            case TC_60__Negative__Invalid_premisesCode_Length_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(8));
                break;

            case TC_61__Negative__Invalid_premisesCode_Format_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPremisesCode(FakerDataGenerator.generateString(6));
                break;

            case TC_62__Negative__Invalid_Account_Number__Invalid_Account_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode("9988776");
                payload.setPremisesCode(FakerDataGenerator.generateDigits(6));
                break;

            case TC_63__Negative__Invalid_Bill_Preference_Value_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActivePaperlessEligibleAccount();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                payload.setUpdateBillDeliveryOption("X");
                break;

            case TC_64__Negative__Invalid_Correspondence_Preference_Value_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActivePaperlessEligibleAccount();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                payload.setUpdateCorrDeliveryOption("X");
                break;

            case TC_65__Negative__Mixed_Enrollment_and_Unenrollment_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActivePaperlessEligibleAccount();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                payload.setUpdateBillDeliveryOption("E");
                payload.setUpdateCorrDeliveryOption("P");
                payload.setEmailAddress("test@test.com");
                break;

            case TC_66__Negative__Missing_Email_for_Enrollment_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActivePaperlessEligibleAccount();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                payload.setUpdateBillDeliveryOption("E");
                payload.setEmailAddress("");
                break;

            case TC_67__Negative__Invalid_Email_Format_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActivePaperlessEligibleAccount();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                payload.setUpdateBillDeliveryOption("E");
                payload.setEmailAddress("invalidemail");
                break;

//            case TC_68__Negative__Bill_Enrollment_Ineligible_:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction().getBillEnrollmentIneligibleAccount();
//                payload.setCustomerCode(accountData.get("customerCode").toString());
//                payload.setPremisesCode(accountData.get("premisesCode").toString());
//                payload.setUpdateBillDeliveryOption("E");
//                payload.setEmailAddress("test@test.com");
//                break;
//
//            case TC_69__Negative__Correspondence_Enrollment_Ineligible_:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction().getCorrEnrollmentIneligibleAccount();
//                payload.setCustomerCode(accountData.get("customerCode").toString());
//                payload.setPremisesCode(accountData.get("premisesCode").toString());
//                payload.setUpdateCorrDeliveryOption("E");
//                payload.setEmailAddress("test@test.com");
//                break;
//
//            case TC_70__Negative__Both_Channels_Enrollment_Ineligible_:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction().getBothChannelsIneligibleAccount();
//                payload.setCustomerCode(accountData.get("customerCode").toString());
//                payload.setPremisesCode(accountData.get("premisesCode").toString());
//                payload.setUpdateBillDeliveryOption("E");
//                payload.setUpdateCorrDeliveryOption("E");
//                payload.setEmailAddress("test@test.com");
//                break;
//
//            case TC_71__Negative__Atomic_Eligibility_Failure_:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction().getOneChannelIneligibleAccount();
//                payload.setCustomerCode(accountData.get("customerCode").toString());
//                payload.setPremisesCode(accountData.get("premisesCode").toString());
//                payload.setUpdateBillDeliveryOption("E");
//                payload.setUpdateCorrDeliveryOption("E");
//                payload.setEmailAddress("test@test.com");
//                break;
//
//            case TC_72__Negative__Unenrollment_Not_Applicable_:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction().getAccountNotEnrolledInPaperless();
//                payload.setCustomerCode(accountData.get("customerCode").toString());
//                payload.setPremisesCode(accountData.get("premisesCode").toString());
//                payload.setUpdateBillDeliveryOption("P");
//                break;
//
//            case TC_73__Negative__Cannot_Unenroll_Initiated_State_:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction().getAccountInInitiatedPaperlessState();
//                payload.setCustomerCode(accountData.get("customerCode").toString());
//                payload.setPremisesCode(accountData.get("premisesCode").toString());
//                payload.setUpdateBillDeliveryOption("P");
//                break;
//
//            case TC_74__Negative__Enrollment_Email_Failure_Rollback__ACTIVE_:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction().getActivePaperlessEligibleAccountWithEmailFailure();
//                payload.setCustomerCode(accountData.get("customerCode").toString());
//                payload.setPremisesCode(accountData.get("premisesCode").toString());
//                payload.setUpdateBillDeliveryOption("E");
//                payload.setEmailAddress(accountData.get("emailAddress").toString());
//                break;
//
//            case TC_75__Negative__Enrollment_Email_Failure_Rollback__NEW_:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction().getNewPaperlessEligibleAccountWithEmailFailure();
//                payload.setCustomerCode(accountData.get("customerCode").toString());
//                payload.setPremisesCode(accountData.get("premisesCode").toString());
//                payload.setUpdateBillDeliveryOption("E");
//                payload.setEmailAddress(accountData.get("emailAddress").toString());
//                break;
//
//            case TC_76__Negative__Email_Ignored_for_P_Requests__Not_Enrolled_:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction().getAccountNotEnrolledInPaperless();
//                payload.setCustomerCode(accountData.get("customerCode").toString());
//                payload.setPremisesCode(accountData.get("premisesCode").toString());
//                payload.setUpdateBillDeliveryOption("P");
//                payload.setUpdateCorrDeliveryOption("P");
//                payload.setEmailAddress("invalidemail@@malformed");
//                break;

            // ---------------- POSITIVE TEST CASES ----------------

            case TC_77__Positive__UpdatePaperlessCommunications_accountType_Format__ACTIVE_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActivePaperlessEligibleAccount();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                payload.setUpdateBillDeliveryOption("E");
                payload.setEmailAddress(accountData.get("emailAddress").toString());
                break;

            case TC_78__Positive__UpdatePaperlessCommunications_accountType_Format__NEW_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getNewPaperlessEligibleAccount();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                payload.setUpdateBillDeliveryOption("E");
                payload.setUpdateCorrDeliveryOption("E");
                payload.setEmailAddress(accountData.get("emailAddress").toString());
                break;

            case TC_79__Positive__billDeliveryOptionStatus_Format__INITIATED_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActivePaperlessEligibleAccount();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                payload.setUpdateBillDeliveryOption("E");
                payload.setEmailAddress(accountData.get("emailAddress").toString());
                break;

            case TC_80__Positive__billDeliveryOptionStatus_Format__UPDATED_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActiveAccountEnrolledInPaperlessBill();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                payload.setUpdateBillDeliveryOption("P");
                break;

            case TC_81__Positive__billDeliveryOptionStatus_Format__NO_CHANGE_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActivePaperlessEligibleAccount();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                payload.setUpdateCorrDeliveryOption("E");
                payload.setEmailAddress(accountData.get("emailAddress").toString());
                break;

            case TC_82__Positive__corrDeliveryOptionStatus_Format__INITIATED_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActivePaperlessEligibleAccount();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                payload.setUpdateBillDeliveryOption("E");
                payload.setUpdateCorrDeliveryOption("E");
                payload.setEmailAddress(accountData.get("emailAddress").toString());
                break;

//            case TC_83__Positive__corrDeliveryOptionStatus_Format__UPDATED_:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction().getActiveAccountEnrolledInPaperlessCorr();
//                payload.setCustomerCode(accountData.get("customerCode").toString());
//                payload.setPremisesCode(accountData.get("premisesCode").toString());
//                payload.setUpdateCorrDeliveryOption("P");
//                break;

            case TC_84__Positive__corrDeliveryOptionStatus_Format__NO_CHANGE_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActivePaperlessEligibleAccount();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                payload.setUpdateBillDeliveryOption("E");
                payload.setEmailAddress(accountData.get("emailAddress").toString());
                break;

            case TC_85__Positive__linkExpiryDateTime_Format_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActivePaperlessEligibleAccount();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                payload.setUpdateBillDeliveryOption("E");
                payload.setEmailAddress(accountData.get("emailAddress").toString());
                break;

            case TC_86__Positive__linkExpiryDateTime_Null_When_No_Enrollment_Processed_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActiveAccountEnrolledInPaperlessBill();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                payload.setUpdateBillDeliveryOption("P");
                break;

//            case TC_87__Positive__linkCreated_Format__true_:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction().getActivePaperlessEligibleAccountWithNoToken();
//                payload.setCustomerCode(accountData.get("customerCode").toString());
//                payload.setPremisesCode(accountData.get("premisesCode").toString());
//                payload.setUpdateBillDeliveryOption("E");
//                payload.setEmailAddress(accountData.get("emailAddress").toString());
//                break;
//
//            case TC_88__Positive__linkCreated_Format__false_:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction().getActivePaperlessEligibleAccountWithValidToken();
//                payload.setCustomerCode(accountData.get("customerCode").toString());
//                payload.setPremisesCode(accountData.get("premisesCode").toString());
//                payload.setUpdateBillDeliveryOption("E");
//                payload.setEmailAddress(accountData.get("emailAddress").toString());
//                break;

            case TC_89__Positive__linkCreated_Null_When_No_Enrollment_Processed_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActiveAccountEnrolledInPaperlessBill();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                payload.setUpdateBillDeliveryOption("P");
                break;

            case TC_90__Positive__emailUpdated_Format__false_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActivePaperlessEligibleAccount();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                payload.setUpdateBillDeliveryOption("E");
                payload.setEmailAddress(accountData.get("emailAddress").toString());
                break;

            case TC_91__Positive__emailUpdated_Format__true_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActivePaperlessEligibleAccount();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                payload.setUpdateBillDeliveryOption("E");
                payload.setEmailAddress("newemail_" + FakerDataGenerator.generateAlphanumeric(5) + "@test.com");
                break;

//            case TC_92__Positive__Email_Ignored_for_P_Requests__Enrolled_:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction().getActiveAccountEnrolledInPaperlessBillAndCorr();
//                payload.setCustomerCode(accountData.get("customerCode").toString());
//                payload.setPremisesCode(accountData.get("premisesCode").toString());
//                payload.setUpdateBillDeliveryOption("P");
//                payload.setUpdateCorrDeliveryOption("P");
//                payload.setEmailAddress("invalidemail@@malformed");
//                break;

            case TC_93__Positive__Active_Account_Bill_Enrollment_Initiated_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActivePaperlessEligibleAccount();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                payload.setUpdateBillDeliveryOption("E");
                payload.setEmailAddress(accountData.get("emailAddress").toString());
                break;

            case TC_94__Positive__New_Account_Bill_Enrollment_Initiated_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getNewPaperlessEligibleAccount();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                payload.setUpdateBillDeliveryOption("E");
                payload.setEmailAddress(accountData.get("emailAddress").toString());
                break;

            case TC_95__Positive__Active_Account_Both_Channels_Enrollment_Initiated_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActivePaperlessEligibleAccount();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                payload.setUpdateBillDeliveryOption("E");
                payload.setUpdateCorrDeliveryOption("E");
                payload.setEmailAddress(accountData.get("emailAddress").toString());
                break;

            case TC_96__Positive__New_Account_Both_Channels_Enrollment_Initiated_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getNewPaperlessEligibleAccount();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                payload.setUpdateBillDeliveryOption("E");
                payload.setUpdateCorrDeliveryOption("E");
                payload.setEmailAddress(accountData.get("emailAddress").toString());
                break;

            case TC_97__Positive__Active_Account_Bill_Unenrollment_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActiveAccountEnrolledInPaperlessBill();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                payload.setUpdateBillDeliveryOption("P");
                break;

//            case TC_98__Positive__New_Account_Bill_Unenrollment_:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction().getNewAccountWithEnrolledPendingBillPref();
//                payload.setCustomerCode(accountData.get("customerCode").toString());
//                payload.setPremisesCode(accountData.get("premisesCode").toString());
//                payload.setUpdateBillDeliveryOption("P");
//                break;
//
//            case TC_99__Positive__Active_Account_Both_Channels_Unenrollment_:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction().getActiveAccountEnrolledInPaperlessBillAndCorr();
//                payload.setCustomerCode(accountData.get("customerCode").toString());
//                payload.setPremisesCode(accountData.get("premisesCode").toString());
//                payload.setUpdateBillDeliveryOption("P");
//                payload.setUpdateCorrDeliveryOption("P");
//                break;
//
//            case TC_100__Positive__New_Account_Both_Channels_Unenrollment_:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction().getNewAccountWithEnrolledPendingBillAndCorrPref();
//                payload.setCustomerCode(accountData.get("customerCode").toString());
//                payload.setPremisesCode(accountData.get("premisesCode").toString());
//                payload.setUpdateBillDeliveryOption("P");
//                payload.setUpdateCorrDeliveryOption("P");
//                break;
//
//            case TC_101__Positive__Active_Account_Reuse_Existing_Valid_Token_:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction().getActivePaperlessEligibleAccountWithValidToken();
//                payload.setCustomerCode(accountData.get("customerCode").toString());
//                payload.setPremisesCode(accountData.get("premisesCode").toString());
//                payload.setUpdateBillDeliveryOption("E");
//                payload.setEmailAddress(accountData.get("emailAddress").toString());
//                break;
//
//            case TC_102__Positive__New_Account_Reuse_Existing_Valid_Token_:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction().getNewPaperlessEligibleAccountWithValidToken();
//                payload.setCustomerCode(accountData.get("customerCode").toString());
//                payload.setPremisesCode(accountData.get("premisesCode").toString());
//                payload.setUpdateBillDeliveryOption("E");
//                payload.setEmailAddress(accountData.get("emailAddress").toString());
//                break;
//
//            case TC_103__Positive__Active_Account_New_Token_When_Existing_Token_Invalid_:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction().getActivePaperlessEligibleAccountWithExpiredToken();
//                payload.setCustomerCode(accountData.get("customerCode").toString());
//                payload.setPremisesCode(accountData.get("premisesCode").toString());
//                payload.setUpdateBillDeliveryOption("E");
//                payload.setEmailAddress(accountData.get("emailAddress").toString());
//                break;
//
//            case TC_104__Positive__New_Account_New_Token_When_Existing_Token_Invalid_:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction().getNewPaperlessEligibleAccountWithExpiredToken();
//                payload.setCustomerCode(accountData.get("customerCode").toString());
//                payload.setPremisesCode(accountData.get("premisesCode").toString());
//                payload.setUpdateBillDeliveryOption("E");
//                payload.setEmailAddress(accountData.get("emailAddress").toString());
//                break;
//
//            case TC_105__Positive__Email_Updated_During_Enrollment_:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction().getActivePaperlessEligibleAccount();
//                payload.setCustomerCode(accountData.get("customerCode").toString());
//                payload.setPremisesCode(accountData.get("premisesCode").toString());
//                payload.setUpdateBillDeliveryOption("E");
//                payload.setEmailAddress("newemail_" + FakerDataGenerator.generateAlphanumeric(5) + "@test.com");
//                break;
//
//            case TC_106__Positive__New_Token_Required_When_Email_Changes_:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction().getActivePaperlessEligibleAccountWithValidToken();
//                payload.setCustomerCode(accountData.get("customerCode").toString());
//                payload.setPremisesCode(accountData.get("premisesCode").toString());
//                payload.setUpdateBillDeliveryOption("E");
//                payload.setEmailAddress("newemail_" + FakerDataGenerator.generateAlphanumeric(5) + "@test.com");
//                break;
//
//            case TC_107__Positive__Cross_Channel_Aggregation_:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction().getActivePaperlessEligibleAccount();
//                payload.setCustomerCode(accountData.get("customerCode").toString());
//                payload.setPremisesCode(accountData.get("premisesCode").toString());
//                payload.setUpdateCorrDeliveryOption("E");
//                payload.setEmailAddress(accountData.get("emailAddress").toString());
//                break;
//
//            case TC_108__Positive__Same_Channel_Update__Last_Value_Wins_:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction().getActivePaperlessEligibleAccountWithPendingBillEnrollment();
//                payload.setCustomerCode(accountData.get("customerCode").toString());
//                payload.setPremisesCode(accountData.get("premisesCode").toString());
//                payload.setUpdateBillDeliveryOption("E");
//                payload.setEmailAddress(accountData.get("emailAddress").toString());
//                break;
//
//            case TC_109__Positive__PPER_Cleanup_on_Active_Unenrollment_:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction().getActiveAccountEnrolledInPaperlessBillWithActivePPER();
//                payload.setCustomerCode(accountData.get("customerCode").toString());
//                payload.setPremisesCode(accountData.get("premisesCode").toString());
//                payload.setUpdateBillDeliveryOption("P");
//                break;
//
//            case TC_110__Positive__Token_Expired_on_New_Account_Unenrollment_:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction().getNewAccountWithEnrolledPendingBillPrefAndValidToken();
//                payload.setCustomerCode(accountData.get("customerCode").toString());
//                payload.setPremisesCode(accountData.get("premisesCode").toString());
//                payload.setUpdateBillDeliveryOption("P");
//                break;
//
//            case TC_111__Positive__Latest_Aggregated_State_Wins_:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction().getActivePaperlessEligibleAccountWithPendingEnrollment();
//                payload.setCustomerCode(accountData.get("customerCode").toString());
//                payload.setPremisesCode(accountData.get("premisesCode").toString());
//                payload.setUpdateBillDeliveryOption("E");
//                payload.setUpdateCorrDeliveryOption("E");
//                payload.setEmailAddress(accountData.get("emailAddress").toString());
//                break;
//
//            case TC_112__Positive__Enrollment_Finalized_on_Confirmation_:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction().getActivePaperlessEligibleAccount();
//                payload.setCustomerCode(accountData.get("customerCode").toString());
//                payload.setPremisesCode(accountData.get("premisesCode").toString());
//                payload.setUpdateBillDeliveryOption("E");
//                payload.setEmailAddress(accountData.get("emailAddress").toString());
//                break;
//
//            case TC_113__Positive__Prior_Link_Invalid_After_Email_Change_:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction().getActivePaperlessEligibleAccountWithValidToken();
//                payload.setCustomerCode(accountData.get("customerCode").toString());
//                payload.setPremisesCode(accountData.get("premisesCode").toString());
//                payload.setUpdateBillDeliveryOption("E");
//                payload.setEmailAddress("newemail_" + FakerDataGenerator.generateAlphanumeric(5) + "@test.com");
//                break;
//
//            case TC_114__Positive__Passive_Invalidation_After_External_Email_Change_:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction().getActivePaperlessEligibleAccountWithExternalEmailChange();
//                payload.setCustomerCode(accountData.get("customerCode").toString());
//                payload.setPremisesCode(accountData.get("premisesCode").toString());
//                payload.setUpdateBillDeliveryOption("E");
//                payload.setEmailAddress(accountData.get("emailAddress").toString());
//                break;

            default:
                log.warn("Unhandled test condition: {}", testCondition);
                break;
        }
    }
}