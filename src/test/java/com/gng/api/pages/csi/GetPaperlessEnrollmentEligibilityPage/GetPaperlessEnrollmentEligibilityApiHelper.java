package com.gng.api.pages.csi.GetPaperlessEnrollmentEligibilityPage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.GetPaperlessEnrollmentEligibility.GetPaperlessEnrollmentEligibilityRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.GetPaperlessEnrollmentEligibility.GetPaperlessEnrollmentEligibilityLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class GetPaperlessEnrollmentEligibilityApiHelper {

    private final GetPaperlessEnrollmentEligibilitySetupHelper setupHelper;

    public GetPaperlessEnrollmentEligibilityApiHelper(TestContext testContext) {
        this.setupHelper = new GetPaperlessEnrollmentEligibilitySetupHelper(testContext);
    }

    GetPaperlessEnrollmentEligibilityRequest preparePayload(GetPaperlessEnrollmentEligibilityLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        return BasePage.deserializeJsonToPojo(
                GetPaperlessEnrollmentEligibilityLabel.get_paperless_enrollment_eligibility.toString(),
                GetPaperlessEnrollmentEligibilityRequest.class);
    }

    public void preparePayloadForTestCondition(GetPaperlessEnrollmentEligibilityRequest payload,
                                               GetPaperlessEnrollmentEligibilityLabel testCondition) {
        Map<String, Object> accountData;

        switch (testCondition) {

            case TC_154__Negative__Missing_Request_ID_:
                payload.setRequestID("");
                break;

            case TC_155__Negative__Invalid_Request_ID_Length_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(33));
                break;

            case TC_156__Negative__Duplicate_Request_ID_:
                payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());
                break;

            case TC_157__Negative__Missing_customerCode_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode("");
                break;

            case TC_158__Negative__Invalid_customerCode_Length_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateDigits(10));
                break;

            case TC_159__Negative__Invalid_customerCode_Format_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateString(6));
                break;

            case TC_160__Negative__Missing_premisesCode_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPremisesCode("");
                break;

            case TC_161__Negative__Invalid_premisesCode_Length_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(8));
                break;

            case TC_162__Negative__Invalid_premisesCode_Format_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPremisesCode(FakerDataGenerator.generateString(6));
                break;

            case TC_163__Negative__Invalid_Account_Number__Invalid_Account_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode("9988776");
                ApplicationContext.get().getDbAction().getUserAccountInfo("9988776");
                payload.setPremisesCode(FakerDataGenerator.generateDigits(6));
                break;

            case TC_164__Positive__Corr_Delivery_Option_Format_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActiveAccountForFreshCorrEnrollmentStrict();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                break;

            case TC_165__Positive__Bill_Delivery_Option_Format_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActiveAccountWithPaperBillDeliveryAndBannerEmail();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                break;

            case TC_166__Positive__Corr_Eligible_Format_:
                applyAccount(payload, ApplicationContext.get().getDbAction().getActiveAccountForFreshCorrEnrollmentStrict());
                break;

            case TC_167__Positive__Bill_Eligible_Format_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActiveAccountWithPaperBillDeliveryAndBannerEmail();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                break;

            case TC_168__Positive__Corr_Reason_Code_Format_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActiveAccountWithNoBannerEmail();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                break;

            case TC_169__Positive__Bill_Reason_Code_Format_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActiveAccountEnrolledInPaperlessBill();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                break;

            case TC_170__Positive__Corr_Reason_Desc_Format_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActiveAccountEnrolledInPaperlessCorr();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                break;

            case TC_171__Positive__Bill_Reason_Desc_Format_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActiveAccountWithFiservBillDelivery();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                break;

            case TC_172__Positive__Correspondence_NULL_Defaults_to_P_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActiveAccountWithNullCorrPreference();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                break;

            case TC_173__Positive__Correspondence_P_Mapping_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActiveAccountForFreshCorrEnrollmentStrict();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                break;

            case TC_174__Positive__Correspondence_E_Mapping_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActiveAccountEnrolledInPaperlessCorr();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                break;

            case TC_175__Positive__Bill_NULL_Defaults_to_P_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActiveAccountWithNullBillPreference();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                break;

            case TC_176__Positive__Bill_P_Mapping_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActiveAccountWithPaperBillDeliveryAndBannerEmail();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                break;

            case TC_177__Positive__Bill_E_Mapping_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActiveAccountEnrolledInPaperlessBill();
                payload.setCustomerCode(accountData.get("customerCode").toString());
                payload.setPremisesCode(accountData.get("premisesCode").toString());
                break;

            case TC_178__Positive__Bill_F_Mapping_:
                applyAccount(payload, ApplicationContext.get().getDbAction().getActiveAccountWithFiservBillDelivery());
                break;

            case TC_179__Positive__Corr_Ineligible_No_Email_:
                applyAccount(payload, ApplicationContext.get().getDbAction().getActiveAccountWithNoBannerEmail());
                break;

            case TC_180__Positive__Bill_Ineligible_No_Email_:
                applyAccount(payload, ApplicationContext.get().getDbAction().getActiveAccountWithNoBannerEmail());
                break;

            case TC_181__Positive__Corr_Reason_Code_1_:
                applyAccount(payload, ApplicationContext.get().getDbAction().getActiveAccountWithNoBannerEmail());
                break;

            case TC_182__Positive__Bill_Reason_Code_1_:
                applyAccount(payload, ApplicationContext.get().getDbAction().getActiveAccountWithNoBannerEmail());
                break;

            case TC_183__Positive__Corr_Eligible_:
                applyAccount(payload, ApplicationContext.get().getDbAction().getActiveAccountForFreshCorrEnrollmentStrict());
                break;

            case TC_184__Positive__Corr_Ineligible_Enrolled_:
                applyAccount(payload, ApplicationContext.get().getDbAction().getActiveAccountEnrolledInPaperlessCorr());
                break;

            case TC_185__Positive__Bill_Eligible_:
                applyAccount(payload, ApplicationContext.get().getDbAction().getActiveAccountWithPaperBillDeliveryAndBannerEmail());
                break;

            case TC_186__Positive__Bill_Ineligible_Enrolled_:
                applyAccount(payload, ApplicationContext.get().getDbAction().getActiveAccountEnrolledInPaperlessBill());
                break;

            case TC_187__Positive__Bill_Ineligible_Fiserv_:
                applyAccount(payload, ApplicationContext.get().getDbAction().getActiveAccountWithFiservBillDelivery());
                break;

            case TC_188__Positive__Corr_Reason_Code_2_:
                applyAccount(payload, ApplicationContext.get().getDbAction().getActiveAccountEnrolledInPaperlessCorr());
                break;

            case TC_189__Positive__Bill_Reason_Code_2_:
                applyAccount(payload, ApplicationContext.get().getDbAction().getActiveAccountEnrolledInPaperlessBill());
                break;

            case TC_190__Positive__Bill_Reason_Code_4_:
                applyAccount(payload, ApplicationContext.get().getDbAction().getActiveAccountWithFiservBillDelivery());
                break;

            case TC_191__Positive__Corr_Reason_Description_:
                applyAccount(payload, ApplicationContext.get().getDbAction().getActiveAccountWithNoBannerEmail());
                break;

            case TC_192__Positive__Bill_Reason_Description_:
                applyAccount(payload, ApplicationContext.get().getDbAction().getActiveAccountEnrolledInPaperlessBill());
                break;

            case TC_193__Positive__Corr_Reason_Description_NULL_:
                applyAccount(payload, ApplicationContext.get().getDbAction().getActiveAccountForFreshCorrEnrollmentStrict());
                break;

            case TC_194__Positive__Bill_Reason_Description_NULL_:
                applyAccount(payload, ApplicationContext.get().getDbAction().getActiveAccountWithPaperBillDeliveryAndBannerEmail());
                break;

            case TC_195__Positive__Corr_Override_I_:
                applyAccount(payload, setupHelper.ensureActivePendingCorrEnrollment());
                break;

            case TC_196__Positive__Corr_Override_Ineligible_:
                applyAccount(payload, setupHelper.ensureActivePendingCorrEnrollment());
                break;

            case TC_197__Positive__Corr_Override_Reason_3_:
                applyAccount(payload, setupHelper.ensureActivePendingCorrEnrollment());
                break;

            case TC_198__Positive__Bill_Override_I_:
                applyAccount(payload, setupHelper.ensureActivePendingBillEnrollment());
                break;

            case TC_199__Positive__Bill_Override_Ineligible_:
                applyAccount(payload, setupHelper.ensureActivePendingBillEnrollment());
                break;

            case TC_200__Positive__Bill_Override_Reason_3_:
                applyAccount(payload, setupHelper.ensureActivePendingBillEnrollment());
                break;

            case TC_201__Positive__Corr_Override_Precedence_:
                // Pending corr PPER → correspondenceDeliveryOption=I
                applyAccount(payload, setupHelper.ensureActivePendingCorrEnrollment());
                break;

            case TC_202__Positive__Bill_Override_Precedence_:
                // Pending bill PPER → billDeliveryOption=I
                applyAccount(payload, setupHelper.ensureActivePendingBillEnrollment());
                break;

            case TC_203__Positive__PPER_Overrides_Email_Rule_:
                applyAccount(payload, setupHelper.ensureActiveNoEmailPendingBillEnrollment());
                break;

            case TC_204__Positive__New_Unconfirmed_Bill_:
                applyAccount(payload, setupHelper.ensureNewUnconfirmedBillPreference());
                break;

            case TC_205__Positive__New_Confirmed_Bill_:
                applyAccount(payload, ApplicationContext.get().getDbAction().getNewAccountWithConfirmedBillPreference());
                break;

            default:
                log.warn("Unhandled test condition: {}", testCondition);
                break;
        }
    }

    private void applyAccount(GetPaperlessEnrollmentEligibilityRequest payload, Map<String, Object> accountData) {
        payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
        payload.setCustomerCode(accountData.get("customerCode").toString());
        payload.setPremisesCode(accountData.get("premisesCode").toString());
    }
}
