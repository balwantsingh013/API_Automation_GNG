package com.gng.api.pages.turnOn.ServiceOrdersPages.SaveEnrollmentPage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.response.Plans;
import com.gng.api.pojo.ServiceOrdersPojo.SaveEnrollment.SaveEnrollmentRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.turnOn.ServiceOrdersSteps.SaveEnrollment.SaveEnrollmentApiLabel;
import com.gng.api.util.FakerDataGenerator;
import io.cucumber.datatable.DataTable;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static com.gng.api.constants.GlobalEnums.EnrollMentStatus.*;
import static com.gng.api.constants.GlobalEnums.TransactionType.TURN_ON;

@Slf4j
public class SaveEnrollmentHelper {

    private final TestContext testContext;

    public SaveEnrollmentHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    SaveEnrollmentRequest preparePayload(SaveEnrollmentApiLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        String jsonFileName = apiLabel.equals(SaveEnrollmentApiLabel.save_enrollment)
                ? SaveEnrollmentApiLabel.save_enrollment.toString()
                : SaveEnrollmentApiLabel.save_enrollment_mandatory.toString();
        return BasePage.deserializeJsonToPojo(jsonFileName, SaveEnrollmentRequest.class);
    }

    public void setRequestIDBasedOnType(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel requestID) {
        switch (requestID) {
            case SAVE_ENROLLMENT_INVALID_EMPTY_REQUEST_ID_TC376:
                payload.setRequestID("");
                break;
            case SAVE_ENROLLMENT_INVALID_LONG_REQUEST_ID_TC377:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(200));
                break;
            case SAVE_ENROLLMENT_INVALID_DUPLICATE_REQUEST_ID_TC378:
                payload.setRequestID("123");
                break;
            default:
                payload.setRequestID(FakerDataGenerator.generateString(10));
        }
    }

    public void setCustomerCodeBasedOnType(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel testCondition) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        Map<String, Object> uzrrcotRecord = ApplicationContext.get()
                .getDbAction()
                .getLatestUZRRCOTRecord();
        payload.setCustomerCode(uzrrcotRecord.get("UZRRCOT_CUST_CODE"));
        payload.setPremisesCode((String) uzrrcotRecord.get("UZRRCOT_PREM_CODE"));
        payload.setTransactionID(uzrrcotRecord.get("UZRRCOT_TRANSACTION_ID"));
        switch (testCondition) {
            case INVALID_CUSTOMER_CODE_EMPTY_TC388:
                payload.setCustomerCode("");
                break;
            case INVALID_CUSTOMER_CODE_MAX_LENGTH_TC389:
                payload.setCustomerCode(FakerDataGenerator.generateNumber(1111000000, 1118900000));
                break;
            case INVALID_CUSTOMER_CODE_ALPHA_NUM_TC390:
                payload.setCustomerCode(FakerDataGenerator.generateAlphanumeric( 9));
                break;
            case INVALID_CUSTOMER_CODE_DOES_NOT_EXIST_TC390a:
                payload.setCustomerCode(0);
                break;
            default:
                payload.setCustomerCode(FakerDataGenerator.generateNumber(0, 9));
        }
    }

    public void setValuesBasedOnGetEligiblePlansAndOffersResponse(SaveEnrollmentRequest payload){
        int customerCode = Integer.parseInt(testContext.getGetEligiblePlansAndOffersResponse().getData().getCustomerCode());
        String premisesCode = testContext.getGetEligiblePlansAndOffersResponse().getData().getPremisesCode();
        int transactionID = Integer.parseInt(testContext.getGetEligiblePlansAndOffersResponse().getData().getTransactionID());
        String aglcAccountNumber = testContext.getGetEligiblePlansAndOffersResponse().getData().getAglcAccountNumber();
        String aglcServiceOrderNumber = testContext.getGetEligiblePlansAndOffersResponse().getData().getAglcServiceLocationID();
        payload.setTransactionType(TURN_ON.getValue());
        payload.setCustomerCode(customerCode);
        payload.setPremisesCode(premisesCode);
        payload.setTransactionID(transactionID);
        payload.setAglcAccountNumber(aglcAccountNumber);
        payload.setAglcServiceOrderNumber(aglcServiceOrderNumber);
    }

    public void setValuesBasedOnGetPrepayPlanRequoteResponse(SaveEnrollmentRequest payload){
        int customerCode = Integer.parseInt(testContext.getGetPrepayPlansRequoteResponse().getData().getCustomerCode());
        String premisesCode = testContext.getGetPrepayPlansRequoteResponse().getData().getPremisesCode();
        int transactionID = Integer.parseInt(testContext.getGetPrepayPlansRequoteResponse().getData().getTransactionID());
        payload.setTransactionType(TURN_ON.getValue());
        payload.setCustomerCode(customerCode);
        payload.setPremisesCode(premisesCode);
        payload.setTransactionID(transactionID);

    }

    public void setSaveEnrollmentAfterRequote(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel testCondition, String planCode){
        setMarketerReferenceData(payload, testContext.getMarketerReferenceData());
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setPlanCode(planCode);
        payload.setPromotionCode("");
        payload.setSspParticipantCode(null);
        String aglcAccountNumber = testContext.getGetEligiblePlansAndOffersResponse().getData().getAglcAccountNumber();
        String aglcServiceOrderNumber = testContext.getGetEligiblePlansAndOffersResponse().getData().getAglcServiceLocationID();
        payload.setAglcAccountNumber(aglcAccountNumber);
        payload.setAglcServiceOrderNumber(aglcServiceOrderNumber);
        payload.setBillingPlan("");
        setValuesBasedOnGetPrepayPlanRequoteResponse(payload);

        switch(testCondition){
            case GET_PREPAY_PLANS_REQUOTE_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_448:
            case GET_PREPAY_PLANS_REQUOTE_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_449:
                payload.setEnrollmentStatus(CANCEL_PREPAY.getValue());
                break;

            case GET_PREPAY_PLANS_REQUOTE_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_429:
            case GET_PREPAY_PLANS_REQUOTE_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_430:
                payload.setEnrollmentStatus(PAYMENT_COMPLETE.getValue());
                payload.setPaymentConfirmationNumber(FakerDataGenerator.generateDigits(5));
                payload.setAglcAccountNumber(FakerDataGenerator.generateDigits(7));
                break;
        }
    }


    public void setSaveEnrollmentRequestForPreviouslySavedEnrollment(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel testCondition, String planCode, String promotionCode){
        setMarketerReferenceData(payload, testContext.getMarketerReferenceData());
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setPlanCode(planCode);
        payload.setPromotionCode(promotionCode);
        setValuesBasedOnGetEligiblePlansAndOffersResponse(payload);
        String sspParticipantCode="";
        switch(testCondition) {
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_424,
                 GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_CE_TC_505:
                payload.setEnrollmentStatus(COMPLETE.getValue());
                payload.setAglcAccountNumber(FakerDataGenerator.generateDigits(7));
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_426:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_426_1,
                 GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_506:
                payload.setEnrollmentStatus(DEPOSIT_PAID.getValue());
                payload.setPaymentConfirmationNumber(FakerDataGenerator.generateDigits(6));
                payload.setAglcAccountNumber(FakerDataGenerator.generateDigits(7));
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_450:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_451:
                payload.setEnrollmentStatus(CANCEL_PREPAY.getValue());
                payload.setBillingPlan("");
                payload.setAglcAccountNumber(FakerDataGenerator.generateDigits(7));
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_453:
                payload.setEnrollmentStatus(BILL_DEPOSIT.getValue());
                payload.setAglcAccountNumber(FakerDataGenerator.generateDigits(7));
                break;

            case SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_479:
                payload.setEnrollmentStatus(COMPLETE.getValue());
                payload.setCustomerCode(null);
                payload.setAglcAccountNumber(FakerDataGenerator.generateDigits(7));
                break;

            case SSP_VALIDATION_SSP_PARTICIPANT_CODE_MISSING_TC_491:
                payload.setEnrollmentStatus(COMPLETE.getValue());
                payload.setPaymentConfirmationNumber(FakerDataGenerator.generateDigits(6));
                payload.setAglcAccountNumber(FakerDataGenerator.generateDigits(7));
                payload.setSspParticipantCode(null);
                payload.setSeasonalSavingsProgramResult("ENROLLED");
                break;

            case SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_481:
                payload.setEnrollmentStatus(SAVE_FOR_FALL_SSP.getValue());
                payload.setCustomerCode(null);
                payload.setAglcAccountNumber(FakerDataGenerator.generateDigits(7));
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_SSP_TC_455:
                payload.setEnrollmentStatus(SAVE_FOR_FALL_SSP.getValue());
                payload.setAglcAccountNumber(FakerDataGenerator.generateDigits(7));
                break;

            case SSP_VALIDATION_SSP_PARTICIPANT_CODE_MISSING_TC_493:
            case SSP_VALIDATION_SSP_PARTICIPANT_CODE_MISSING_TC_495:
            case SSP_VALIDATION_PREMISES_CODE_MISSING_TC_492:
                payload.setEnrollmentStatus(COMPLETE.getValue());
                payload.setSspParticipantCode(null);
                payload.setAglcAccountNumber(FakerDataGenerator.generateDigits(7));
                break;

            case SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_483:
                sspParticipantCode= testContext.getSearchAccountsResponse().getData().getAccounts().getFirst().getSspParticipantCode();
                payload.setEnrollmentStatus(SAVE_INCOMPLETE.getValue());
                payload.setSspParticipantCode(sspParticipantCode);
                payload.setCustomerCode(null);
                payload.setAglcAccountNumber(FakerDataGenerator.generateDigits(7));
                break;

            case SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_487:
                payload.setEnrollmentStatus(SAVE_FOR_FALL_SSP.getValue());
                payload.setSspParticipantCode(null);
                payload.setCustomerCode(null);
                payload.setAglcAccountNumber(FakerDataGenerator.generateDigits(7));
                break;

            case SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_482:
            case SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_483_2:
            case SSP_VALIDATION_SSP_PARTICIPANT_CODE_MISSING_TC_494:
            case SSP_VALIDATION_SSP_PARTICIPANT_CODE_MISSING_TC_495_2:
                sspParticipantCode= testContext.getSearchAccountsResponse().getData().getAccounts().getFirst().getSspParticipantCode();
                payload.setEnrollmentStatus(SAVE_INCOMPLETE.getValue());
                payload.setSspParticipantCode(sspParticipantCode);
                payload.setAglcAccountNumber(FakerDataGenerator.generateDigits(7));
                break;

            case SSP_VALIDATION_PREMISES_CODE_MISSING_TC_488:
                sspParticipantCode= testContext.getSearchAccountsResponse().getData().getAccounts().getFirst().getSspParticipantCode();
                payload.setEnrollmentStatus(SAVE_INCOMPLETE.getValue());
                payload.setSspParticipantCode(sspParticipantCode);
                payload.setAglcAccountNumber(null);
                payload.setAglcServiceOrderNumber(null);
                payload.setMarketerReferenceData(null);
                payload.setPromotionCode(null);
                payload.setCustomerRequestedServiceDate(null);
                break;

            case SSP_VALIDATION_PAYMENT_CONFIRMATION_NUMBER_MISSING_TC_496:
                sspParticipantCode= testContext.getGetEligiblePlansAndOffersResponse().getData().getSspParticipantCode().toString();
                payload.setEnrollmentStatus(COMPLETE.getValue());
                payload.setSspParticipantCode(sspParticipantCode);
                payload.setPaymentConfirmationNumber("");
                payload.setSeasonalSavingsProgramResult("ENROLLED");
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_444:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_445:
                payload.setEnrollmentStatus(REFUSED_PREPAY.getValue());
                payload.setBillingPlan("");
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_447:
                payload.setEnrollmentStatus(REFUSED_DEPOSIT.getValue());
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_427:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_428:
                setEnrollmentStatusPCAndPaymentConfirmationNumber(payload);
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_434:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_SI_TC_503,
                 GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_507:
                payload.setEnrollmentStatus(SAVE_INCOMPLETE.getValue());
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_436:
                payload.setEnrollmentStatus(DEPOSIT_REQUIRED.getValue());
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_440:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_441:
                payload.setEnrollmentStatus(PREPAY_REQUIRED.getValue());
                payload.setBillingPlan("");
                payload.setRequestID(FakerDataGenerator.generateString(9));
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_437:
                payload.setEnrollmentStatus(DEPOSIT_BILLED.getValue());
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_CE_TC_502:
                payload.setEnrollmentStatus(COMPLETE.getValue());
                payload.setAglcAccountNumber(FakerDataGenerator.generateDigits(7));
                payload.setSeasonalSavingsProgramResult("ENROLLED");
                payload.setSspParticipantCode(Integer.parseInt(testContext.getGetEligiblePlansAndOffersResponse().getData().getCustomerCode()));
                break;
        }
    }

    public void databaseValidationPostEnrollment(DataTable dataTable) {
        Map<String, String> params = dataTable.asMaps(String.class, String.class).get(0); // ✅ Use first row only

        String cycleCode = params.get("cycleCode");
        String reasonCode = params.get("reasonCode");
        String enrollmentStatus = params.get("enrollmentStatus");
        String accountStatusIdicator = params.get("accountStatusIdicator");
        String paymentArrear = params.get("paymentArrear");
        String badDebtExemptIndicator = params.get("badDebtExemptIndicator");
        String NCOAProtectIndicator = params.get("NCOAProtectIndicator");
        String feedbackIndicator = params.get("feedbackIndicator");
        String contactDirection = params.get("contactDirection");
        String referredIndicator = params.get("referredIndicator");
        String OCRCDETStatus = params.get("OCRCDETStatus");
        String OCRCTIMAutomaticIndicator = params.get("OCRCTIMAutomaticIndicator");
        String contactType = params.get("contactType");
        SaveEnrollmentApiLabel testCondition= SaveEnrollmentApiLabel.valueOf(params.get("testCondition"));

        String customerCode = testContext.getGetEligiblePlansAndOffersResponse().getData().getCustomerCode();
        String premisesCode = testContext.getGetEligiblePlansAndOffersResponse().getData().getPremisesCode();
        Map<String, Object> enrollmentRecord= null;

        switch (testCondition){
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_SI_TC_433:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_RP_TC_443:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_RD_TC_446:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_SF_TC_454:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_434:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_SF_TC_501:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_SI_TC_503:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_SI_TC_504,
                 GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_507,
                 GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_444,
                 GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_445,
                 GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_447,
                 GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_RP_TC_442:

                enrollmentRecord = ApplicationContext.get()
                        .getDbAction()
                        .validateAllTheTablesAfterEnrollmentForIncompleteEnrollment(
                                customerCode,
                                premisesCode,
                                enrollmentStatus,
                                feedbackIndicator,
                                contactDirection,
                                reasonCode,
                                referredIndicator,
                                OCRCDETStatus,
                                OCRCTIMAutomaticIndicator,
                                contactType
                        );
                break;

            default:
                enrollmentRecord = ApplicationContext.get()
                        .getDbAction()
                        .validateAllTheTablesAfterEnrollment(
                                customerCode,
                                premisesCode,
                                cycleCode,
                                reasonCode,
                                enrollmentStatus,
                                accountStatusIdicator,
                                paymentArrear,
                                badDebtExemptIndicator,
                                NCOAProtectIndicator,
                                feedbackIndicator,
                                contactDirection,
                                referredIndicator,
                                OCRCDETStatus,
                                OCRCTIMAutomaticIndicator,
                                contactType
                        );
                break;


        }
        Assert.assertEquals(enrollmentRecord.get("UZBENRO_CUST_CODE").toString(), customerCode);
    }

    public void setSaveEnrollmentRequestParametersAsPerTestCondition(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel testCondition, String planCode, String promotionCode){
        setMarketerReferenceData(payload, testContext.getMarketerReferenceData());
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setPlanCode(planCode);
        payload.setPromotionCode(promotionCode);
        setValuesBasedOnGetEligiblePlansAndOffersResponse(payload);
        String sspParticipantCode="";
        switch(testCondition) {
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_NOTES_CE_TC_498:
                payload.setEnrollmentStatus(COMPLETE.getValue());
                break;

            case SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_478:
            case SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_479:
            case SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_482:
            case SSP_VALIDATION_SSP_PARTICIPANT_CODE_MISSING_TC_494:
            case SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_483:
            case SSP_VALIDATION_SSP_PARTICIPANT_CODE_MISSING_TC_495:
            case SSP_VALIDATION_SSP_PARTICIPANT_CODE_MISSING_TC_490:
                payload.setEnrollmentStatus(COMPLETE.getValue());
                payload.setAglcAccountNumber(FakerDataGenerator.generateDigits(7));
                break;

            case SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_480:
            case SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_481, GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_SF_TC_454:
            case SSP_VALIDATION_PREMISES_CODE_MISSING_TC_492:
            case SSP_VALIDATION_SSP_PARTICIPANT_CODE_MISSING_TC_493,
                 GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_SF_TC_501,
                 SAVE_ENROLLMENT_INVALID_SSP_EMPTY_TC412,
                 SAVE_ENROLLMENT_INVALID_SSP_RESULT_VALUE_TC414,
                 SAVE_ENROLLMENT_INVALID_SSP_SPLIT_FEE_EMPTY_TC415,
                 SAVE_ENROLLMENT_INVALID_SSP_SPLIT_FEE_VALUE_TC416,
                 SAVE_ENROLLMENT_INVALID_SPLIT_FEE_VALUE_TC417a,
                 SSP_FALL_TURN_ON_SEARCH_TC_112:
                payload.setEnrollmentStatus(SAVE_FOR_FALL_SSP.getValue());
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_NOTES_PC_TC_499:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_NOTES_PC_TC_500A:
                setEnrollmentStatusPCAndPaymentConfirmationNumber(payload);
                break;

            case SSP_VALIDATION_PAYMENT_CONFIRMATION_NUMBER_MISSING_TC_496:
                setEnrollmentStatusPCAndPaymentConfirmationNumber(payload);
                payload.setAglcAccountNumber(FakerDataGenerator.generateDigits(6));
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_NOTES_PC_TC_500B:
                setEnrollmentStatusPCAndPaymentConfirmationNumber(payload);
                payload.setNotes(FakerDataGenerator.generateString(10));
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_424:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_434:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_SSP_TC_455:
                payload.setEnrollmentStatus(SAVE_INCOMPLETE.getValue());
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_CE_TC_423:
                payload.setNotes(FakerDataGenerator.generateString(10));
                payload.setEnrollmentStatus(COMPLETE.getValue());
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_DP_TC_425, SSP_VALIDATION_PREMISES_CODE_MISSING_TC_484,
                 SSP_VALIDATION_PREMISES_CODE_MISSING_TC_488, SSP_VALIDATION_SSP_PARTICIPANT_CODE_MISSING_TC_491:
                payload.setEnrollmentStatus(DEPOSIT_PAID.getValue());
                payload.setPaymentConfirmationNumber(FakerDataGenerator.generateDigits(6));
                payload.setAglcAccountNumber(FakerDataGenerator.generateDigits(7));
                break;

            case SSP_VALIDATION_INVALID_SSP_RESULT_TC_497B:
                payload.setEnrollmentStatus(COMPLETE.getValue());
                payload.setSspParticipantCode(null);
                payload.setSeasonalSavingsProgramResult("ENROLLED");
                payload.setAglcAccountNumber(FakerDataGenerator.generateDigits(7));
                break;

            case SSP_VALIDATION_INVALID_SSP_CODE_TC_497C:
                payload.setEnrollmentStatus(COMPLETE.getValue());
                payload.setSspParticipantCode(FakerDataGenerator.generateDigits(5));
                payload.setSeasonalSavingsProgramResult("");
                payload.setAglcAccountNumber(FakerDataGenerator.generateDigits(7));
                break;

            case SSP_VALIDATION_PREMISES_CODE_MISSING_TC_485:
                payload.setEnrollmentStatus(DEPOSIT_PAID.getValue());
                payload.setPaymentConfirmationNumber(FakerDataGenerator.generateDigits(6));
                payload.setAglcAccountNumber(FakerDataGenerator.generateDigits(7));
                payload.setPremisesCode(null);
                payload.setSspParticipantCode(null);
                break;

            case SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_486:
                payload.setSspParticipantCode(null);
                payload.setEnrollmentStatus(SAVE_FOR_FALL_SSP.getValue());
                break;

            case SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_487:
                sspParticipantCode= testContext.getSearchAccountsResponse().getData().getAccounts().getFirst().getSspParticipantCode();
                payload.setSspParticipantCode(sspParticipantCode);
                payload.setEnrollmentStatus(SAVE_FOR_FALL_SSP.getValue());
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_426:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_426_1:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_RD_TC_446:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_436:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_437:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_453:
                payload.setEnrollmentStatus(REFUSED_DEPOSIT.getValue());
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_DR_TC_435:
            case INVALID_TRANSACTION_ID_ENROLLMENT_STATE_CRDS_TC_176:
            case INVALID_CUSTOMER_CODE_ENROLLMENT_STATE_CRDS_TC_178:
            case INVALID_PREMISES_CODE_ENROLLMENT_STATE_CRDS_TC_180:
            case INVALID_COMBINATION_OF_CUSTOMER_AND_PREMISES_CODE_CRDS_TC_182:
                payload.setEnrollmentStatus(DEPOSIT_REQUIRED.getValue());
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PR_TC_438:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PR_TC_439:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_427:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_428:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_450:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_451:
                payload.setEnrollmentStatus(PREPAY_REQUIRED.getValue());
                payload.setBillingPlan("");
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_RP_TC_442:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_RP_TC_443:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_440:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_441:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_444:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_445:
                payload.setEnrollmentStatus(REFUSED_PREPAY.getValue());
                payload.setBillingPlan("");
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_BD_TC_452:
                payload.setEnrollmentStatus(BILL_DEPOSIT.getValue());
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_SI_TC_433:
            case INVALID_TRANSACTION_ID_ENROLLMENT_STATE_INCL_TC_175:
            case INVALID_CUSTOMER_CODE_ENROLLMENT_STATE_INCL_TC_177:
            case INVALID_CUSTOMER_CODE_ENROLLMENT_STATE_INCL_TC_187:
            case INVALID_PREMISES_CODE_ENROLLMENT_STATE_INCL_TC_179:
            case INVALID_COMBINATION_OF_CUSTOMER_AND_PREMISES_CODE_INCL_TC_181:
            case INVALID_LENGTH_CUSTOMER_CODE_ENROLLMENT_STATE_INCL_TC_186:
            case INVALID_CUSTOMER_CODE_LESS_THAN_0_ENROLLMENT_STATE_INCL_TC_186A:
            case INVALID_CUSTOMER_CODE_EMPTY_ENROLLMENT_STATE_INCL_TC_186B:
            case INVALID_PREMISES_CODE_LENGTH_ENROLLMENT_STATE_INCL_TC_188:
            case INVALID_PREMISES_CODE_NON_NUMERIC_ENROLLMENT_STATE_INCL_TC_188A:
            case INVALID_PREMISES_CODE_ENROLLMENT_STATE_INCL_TC_189:
                payload.setEnrollmentStatus(SAVE_INCOMPLETE.getValue());
                payload.setNotes(FakerDataGenerator.generateString(10));
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PC_TC_431:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PC_TC_432:
                setEnrollmentStatusPCAndPaymentConfirmationNumber(payload);
                payload.setAglcAccountNumber(FakerDataGenerator.generateDigits(7));
                break;
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_CE_TC_502:
                payload.setEnrollmentStatus(SAVE_FOR_FALL_SSP.getValue());
                payload.setPromotionCode("");
                break;
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_SI_TC_503:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_CE_TC_505,
                 GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_507:
                payload.setEnrollmentStatus(SAVE_INCOMPLETE.getValue());
                payload.setPromotionCode("");
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_SI_TC_504,
                 GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_506,
                 GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_447:
                payload.setEnrollmentStatus(REFUSED_DEPOSIT.getValue());
                payload.setAglcServiceOrderNumber(null);
                payload.setPromotionCode("");
                break;
            case SAVE_ENROLLMENT_INVALID_VALUE_BILLING_PLAN_PREPAY_TC406a:
                payload.setAglcAccountNumber(FakerDataGenerator.generateDigits(8));
                payload.setAglcServiceOrderNumber(FakerDataGenerator.generateDigits(8));
                payload.setEnrollmentStatus(PREPAY_REQUIRED.getValue());
                payload.setBillingPlan("B");
                break;
            case ST_SE_INVALID_ES_PAYMENT_CONFIRMATION_REQUIRED_TC235:
                payload.setEnrollmentStatus(SAVE_INCOMPLETE.getValue());
                payload.setTransactionType(GlobalEnums.TransactionType.TRANSFER.getValue());
        }
    }

    public void setEnrollmentStatusPCAndPaymentConfirmationNumber(SaveEnrollmentRequest payload){
        payload.setEnrollmentStatus(PAYMENT_COMPLETE.getValue());
        payload.setPaymentConfirmationNumber(FakerDataGenerator.generateDigits(6));
        payload.setBillingPlan("");
    }

    public void setPremisesCodeBasedOnType(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel testCondition) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        Map<String, Object> uzrrcotRecord = ApplicationContext.get()
                .getDbAction()
                .getLatestUZRRCOTRecord();
        payload.setCustomerCode(uzrrcotRecord.get("UZRRCOT_CUST_CODE"));
        payload.setPremisesCode((String) uzrrcotRecord.get("UZRRCOT_PREM_CODE"));
        payload.setTransactionID(uzrrcotRecord.get("UZRRCOT_TRANSACTION_ID"));
        switch (testCondition) {
            case INVALID_PREMISES_CODE_EMPTY_TC391:
                payload.setPremisesCode("");
                break;
            case SPECIAL_CHARS_PREMISES_CODE:
                payload.setPremisesCode(FakerDataGenerator.generateAlphanumericWithSpecialChars(10));
                break;
            case INVALID_PREMISES_CODE_MAX_LENGTH_TC392:
                payload.setPremisesCode(FakerDataGenerator.generateAlphanumeric(36));
                break;
            case INVALID_PREMISES_CODE_NOT_EXISTS_TC392a:
                payload.setPremisesCode("0");
                break;
            case INVALID_PREMISES_CODE_CUSTOMER_CODE_NOT_EXISTS_TC393:
                payload.setPremisesCode("12");
                payload.setCustomerCode("10");
                break;
            default:
                payload.setPremisesCode(FakerDataGenerator.generateString(10));
        }
    }

    public void setTransactionIDBasedOnType(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel transactionID) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        switch (transactionID) {
            case MISSING_TRANSACTION_ID_TC382:
                payload.setTransactionID("");
                break;
            case INVALID_NON_INTEGER_TRANSACTION_ID_TC383:
                payload.setTransactionID(FakerDataGenerator.generateString( 4));
                break;
            case INVALID_DOES_NOT_MATCH_TRANSACTION_ID_TC384:
                payload.setTransactionID(FakerDataGenerator.generateNumber(4, 9));
                break;
            default:
                payload.setTransactionID(FakerDataGenerator.generateNumber(0, 10));
        }
    }

    public void setTransactionTypeBasedOnType(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel testCondition) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        switch (testCondition) {
            case INVALID_TRANSACTION_TYPE_EMPTY_TC385:
                payload.setTransactionType("");
                break;
            case INVALID_TRANSACTION_TYPE_MAX_LENGTH_TC386:
                payload.setTransactionType(FakerDataGenerator.generateString(10));
                break;
            case INVALID_TRANSACTION_TYPE_NOT_EXISTS_TC387:
                Map<String, Object> uzrrcotRecord = ApplicationContext.get()
                        .getDbAction()
                        .getLatestUZRRCOTRecord();
                payload.setCustomerCode(uzrrcotRecord.get("UZRRCOT_CUST_CODE"));
                payload.setPremisesCode((String) uzrrcotRecord.get("UZRRCOT_PREM_CODE"));
                payload.setTransactionID(uzrrcotRecord.get("UZRRCOT_TRANSACTION_ID"));
                payload.setTransactionType("TNOF");
                break;
            default:
                payload.setTransactionType(FakerDataGenerator.generateUpperCaseString(4));
        }
    }

    public void setPlanCodeBasedOnType(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel testCondition) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        switch (testCondition) {
            case INVALID_PLAN_CODE_EMPTY_TC394:
                payload.setPlanCode("");
                break;
            case INVALID_PLAN_CODE_MAX_LENGTH_TC395:
                payload.setPlanCode(FakerDataGenerator.generateString(7));
                break;
            case INVALID_PLAN_CODE_NOT_EXISTS_TC396:
                Map<String, Object> uzrrcotRecord = ApplicationContext.get()
                        .getDbAction()
                        .getLatestUZRRCOTRecord();
                payload.setCustomerCode(uzrrcotRecord.get("UZRRCOT_CUST_CODE"));
                payload.setPremisesCode((String) uzrrcotRecord.get("UZRRCOT_PREM_CODE"));
                payload.setTransactionID(uzrrcotRecord.get("UZRRCOT_TRANSACTION_ID"));
                payload.setPlanCode("ZZZ");
                break;
            case INVALID_PLAN_CODE_PRIME_STATUS_TC396a:
                payload.setMarketerReferenceData(String.valueOf(testContext.getMarketerReferenceData()));
                Map<String, Object> uzrrcotRecord1 = ApplicationContext.get()
                        .getDbAction()
                        .getLatestUZRRCOTRecord();
                payload.setCustomerCode(uzrrcotRecord1.get("UZRRCOT_CUST_CODE"));
                payload.setPremisesCode((String) uzrrcotRecord1.get("UZRRCOT_PREM_CODE"));
                payload.setTransactionID(uzrrcotRecord1.get("UZRRCOT_TRANSACTION_ID"));
                payload.setPlanCode("PGB");
                payload.setEnrollmentStatus(PAYMENT_COMPLETE.getValue());
                payload.setTransactionType(TURN_ON.getValue());
                break;
            case ALPHANUMERIC_PLAN_CODE:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setPlanCode(FakerDataGenerator.generateAlphanumeric(2));
                break;
            default:
                payload.setPlanCode(FakerDataGenerator.generateUpperCaseString(3));
        }
    }

    public void setPromotionCodeBasedOnType(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel testCondition) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        switch (testCondition) {
            case INVALID_PROMOTION_CODE_MAX_LENGTH_TC397:
                payload.setPromotionCode(FakerDataGenerator.generateString(39));
                break;
            case SAVE_ENROLLMENT_INVALID_PROMOTION_CODE_NO_MATCH_TC398:
                payload.setPlanCode(testContext.getGetEligiblePlansAndOffersResponse().getData().getPlans().getFirst().getPlanCode());
                payload.setEnrollmentStatus(testContext.getGetEligiblePlansAndOffersResponse().getData().getPlans().getFirst().getEnrollmentStatus().getFirst().getCode());
                payload.setPromotionCode("0 CENTS FOR 12 MONTHS");
                payload.setTransactionID(testContext.getGetEligiblePlansAndOffersResponse().getData().getTransactionID());
                payload.setCustomerCode(testContext.getGetEligiblePlansAndOffersResponse().getData().getCustomerCode());
                payload.setPremisesCode(testContext.getGetEligiblePlansAndOffersResponse().getData().getPremisesCode());
                payload.setNotes("test");
                break;
            default:
                payload.setPromotionCode(FakerDataGenerator.generateUpperCaseString(3));
        }
    }

    public void setLoginIDBasedOnType(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel loginID) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        switch (loginID) {
            case SAVE_ENROLLMENT_INVALID_EMPTY_LOGIN_ID_TC379:
                payload.setLoginID("");
                break;
            case SAVE_ENROLLMENT_INVALID_MAX_LENGTH_LOGIN_ID_TC380:
                payload.setLoginID(FakerDataGenerator.getRandomNumericString(39));
                break;
            case INVALID_LOGIN_ID_NOT_PRESENT_USER_TABLE_TC381:
                payload.setLoginID(FakerDataGenerator.getRandomString(10));
                break;
            default:
                payload.setLoginID(FakerDataGenerator.generateLowerCaseString(5));
        }
    }

    public void setEnrollmentStatusBasedOnType(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel testCondition) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        switch (testCondition) {
            case SAVE_ENROLLMENT_INVALID_ENROLLMENT_STATUS_MAX_LENGTH_TC400:
                payload.setEnrollmentStatus(FakerDataGenerator.getRandomString(10));
                break;
            case SAVE_ENROLLMENT_INVALID_ENROLLMENT_STATUS_NO_MATCH_TC401:
                payload.setTransactionType(TURN_ON.getValue());
                payload.setPlanCode(testContext.getGetEligiblePlansAndOffersResponse().getData().getPlans().getFirst().getPlanCode());
                payload.setEnrollmentStatus(PREPAY_REQUIRED.getValue());
                payload.setPromotionCode(testContext.getGetEligiblePlansAndOffersResponse().getData().getPlans().getFirst().getPromotion1Code());
                payload.setTransactionID(testContext.getGetEligiblePlansAndOffersResponse().getData().getTransactionID());
                payload.setCustomerCode(testContext.getGetEligiblePlansAndOffersResponse().getData().getCustomerCode());
                payload.setPremisesCode(testContext.getGetEligiblePlansAndOffersResponse().getData().getPremisesCode());
                break;
            case SAVE_ENROLLMENT_INVALID_ENROLLMENT_STATUS_MISSING_CONFIRMATION_TC402:
                payload.setMarketerReferenceData(String.valueOf(testContext.getMarketerReferenceData()));
                payload.setTransactionType(TURN_ON.getValue());
                payload.setPlanCode(GlobalEnums.PlanCode.PGB.getValue());
                payload.setEnrollmentStatus(PAYMENT_COMPLETE.getValue());
                payload.setTransactionID(testContext.getGetEligiblePlansAndOffersResponse().getData().getTransactionID());
                payload.setCustomerCode(testContext.getGetEligiblePlansAndOffersResponse().getData().getCustomerCode());
                payload.setPremisesCode(testContext.getGetEligiblePlansAndOffersResponse().getData().getPremisesCode());
                break;
            case LOWERCASE_ENROLLMENT_STATUS:
                payload.setEnrollmentStatus(FakerDataGenerator.generateUpperCaseString(4));
                break;
            case MAX_LENGTH_ENROLLMENT_STATUS:
                payload.setEnrollmentStatus(FakerDataGenerator.getRandomNumericString(3));
                break;
            case WHITESPACE_CONTAINS_ENROLLMENT_STATUS:
                payload.setEnrollmentStatus("S I");
                break;
            default:
                payload.setEnrollmentStatus(FakerDataGenerator.generateUpperCaseString(2));
        }
    }
    public void setPaymentConfirmationNumberBasedOnType(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel testCondition) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setMarketerReferenceData(String.valueOf(testContext.getMarketerReferenceData()));
        payload.setTransactionType(TURN_ON.getValue());
        payload.setPlanCode(testContext.getGetEligiblePlansAndOffersResponse().getData().getPlans().getFirst().getPlanCode());
        payload.setEnrollmentStatus(DEPOSIT_PAID.getValue());
        payload.setPromotionCode(testContext.getGetEligiblePlansAndOffersResponse().getData().getPlans().getFirst().getPromotion1Code());
        payload.setTransactionID(testContext.getGetEligiblePlansAndOffersResponse().getData().getTransactionID());
        payload.setCustomerCode(testContext.getGetEligiblePlansAndOffersResponse().getData().getCustomerCode());
        payload.setPremisesCode(testContext.getGetEligiblePlansAndOffersResponse().getData().getPremisesCode());
        payload.setPaymentConfirmationNumber(FakerDataGenerator.generateDigits(29));
    }

    public void setMarketerReferenceData(SaveEnrollmentRequest payload, long marketerReferenceData){
        payload.setMarketerReferenceData(marketerReferenceData);
    }

    public void setRequestParamsForBillPlanFromEligiblePlansAndOffers(SaveEnrollmentRequest payload){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate serviceDate = LocalDate.now();
        String serviceDateString = serviceDate.minusDays(1).format(formatter);
        payload.setMarketerReferenceData(String.valueOf(testContext.getMarketerReferenceData()));
        payload.setTransactionType(TURN_ON.getValue());
        payload.setPlanCode(testContext.getGetEligiblePlansAndOffersResponse().getData().getPlans().getFirst().getPlanCode());
        payload.setEnrollmentStatus(testContext.getGetEligiblePlansAndOffersResponse().getData().getPlans().getFirst().getEnrollmentStatus().getFirst().getCode());
        payload.setPromotionCode(testContext.getGetEligiblePlansAndOffersResponse().getData().getPlans().getFirst().getPromotion1Code());
        payload.setTransactionID(testContext.getGetEligiblePlansAndOffersResponse().getData().getTransactionID());
        payload.setCustomerCode(testContext.getGetEligiblePlansAndOffersResponse().getData().getCustomerCode());
        payload.setPremisesCode(testContext.getGetEligiblePlansAndOffersResponse().getData().getPremisesCode());
        payload.setCustomerRequestedServiceDate(serviceDateString);
    }
    public void setBillingPlanBasedOnType(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel billingPlan) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        switch (billingPlan) {
            case SAVE_ENROLLMENT_INVALID_BILLING_PLAN_EMPTY_TC404:
                setRequestParamsForBillPlanFromEligiblePlansAndOffers(payload);
                payload.setAglcAccountNumber(FakerDataGenerator.generateDigits(8));
                payload.setAglcServiceOrderNumber(FakerDataGenerator.generateDigits(8));
                payload.setBillingPlan("");
                break;
            case SAVE_ENROLLMENT_INVALID_MAX_LENGTH_BILLING_PLAN_TC405:
                payload.setBillingPlan(FakerDataGenerator.getRandomNumericString(4));
                break;
            case SAVE_ENROLLMENT_INVALID_VALUE_BILLING_PLAN_TC406:
                setRequestParamsForBillPlanFromEligiblePlansAndOffers(payload);
                payload.setAglcAccountNumber(FakerDataGenerator.generateDigits(8));
                payload.setAglcServiceOrderNumber(FakerDataGenerator.generateDigits(8));
                payload.setBillingPlan("Z");
                break;
            case SAVE_ENROLLMENT_INVALID_BILLING_PLAN_EMPTY_BUDGET_AMOUNT_TC407:
                setRequestParamsForBillPlanFromEligiblePlansAndOffers(payload);
                payload.setAglcAccountNumber(FakerDataGenerator.generateDigits(8));
                payload.setAglcServiceOrderNumber(FakerDataGenerator.generateDigits(8));
                payload.setBillingPlan("B");
                break;
            case SPECIAL_CHARS_BILLING_PLAN:
                payload.setBillingPlan(FakerDataGenerator.generateAlphanumericWithSpecialChars(7));
                break;
            case LOWERCASE_BILLING_PLAN:
                payload.setBillingPlan(FakerDataGenerator.generateUpperCaseString(4));
                break;
            default:
                payload.setEnrollmentStatus(FakerDataGenerator.generateUpperCaseString(1));
        }
    }

    public void setEstimatedBudgetAmountBasedOnType(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel billingPlan) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        setRequestParamsForBillPlanFromEligiblePlansAndOffers(payload);
        payload.setAglcAccountNumber(FakerDataGenerator.generateDigits(8));
        payload.setAglcServiceOrderNumber(FakerDataGenerator.generateDigits(8));
        payload.setBillingPlan("B");
        switch (billingPlan) {
            case SAVE_ENROLLMENT_INVALID_BUDGET_AMOUNT_MAX_LENGTH_TC408:
                payload.setEstimatedBudgetAmount(FakerDataGenerator.generateDigits(9));
                break;
            case SAVE_ENROLLMENT_INVALID_VALUE_BUDGET_AMOUNT_TC409:
                payload.setEstimatedBudgetAmount(FakerDataGenerator.generateAlphanumeric(3));
                break;
            default:
                payload.setEnrollmentStatus(FakerDataGenerator.generateUpperCaseString(1));
        }
    }

    public void setServiceDateBasedOnType(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel billingPlan) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        setRequestParamsForBillPlanFromEligiblePlansAndOffers(payload);
        payload.setAglcAccountNumber(FakerDataGenerator.generateDigits(8));
        payload.setAglcServiceOrderNumber(FakerDataGenerator.generateDigits(8));
        switch (billingPlan) {
            case SAVE_ENROLLMENT_INVALID_SERVICE_DATE_EMPTY_TC410:
                payload.setCustomerRequestedServiceDate("");
                break;
            case SAVE_ENROLLMENT_INVALID_SERVICE_DATE_FORMAT_TC411:
                payload.setCustomerRequestedServiceDate(FakerDataGenerator.getRandomString(8));
                break;
            case SAVE_ENROLLMENT_INVALID_SERVICE_DATE_FORMAT_TC411a:
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
                LocalDate serviceDate = LocalDate.now();
                String invaliidServiceDateString = serviceDate.minusDays(1).format(formatter);
                payload.setCustomerRequestedServiceDate(invaliidServiceDateString);
                break;
        }
    }

    public void setInvalidParametersBasedOnType(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel billingPlan) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        setRequestParamsForBillPlanFromEligiblePlansAndOffers(payload);
        payload.setAglcAccountNumber(FakerDataGenerator.generateDigits(8));
        payload.setAglcServiceOrderNumber(FakerDataGenerator.generateDigits(8));
        switch (billingPlan) {
            case SAVE_ENROLLMENT_INVALID_SSP_EMPTY_TC412:
                payload.setEnrollmentStatus(COMPLETE.getValue());
                break;
            case SAVE_ENROLLMENT_INVALID_SSP_RESULT_MAX_LENGTH_TC413:
                payload.setEnrollmentStatus(COMPLETE.getValue());
                payload.setSeasonalSavingsProgramResult(FakerDataGenerator.generateString(19));
                break;
            case SAVE_ENROLLMENT_INVALID_SSP_RESULT_VALUE_TC414:
                payload.setEnrollmentStatus(COMPLETE.getValue());
                payload.setSeasonalSavingsProgramResult("EXC");
                payload.setPromotionCode("");
                payload.setSspParticipantCode(testContext.getGetEligiblePlansAndOffersResponse().getData().getCustomerCode());
                break;
            case SAVE_ENROLLMENT_INVALID_SSP_SPLIT_FEE_EMPTY_TC415,
                 SAVE_ENROLLMENT_INVALID_SSP_SPLIT_FEE_VALUE_TC416:
                payload.setEnrollmentStatus(COMPLETE.getValue());
                payload.setPromotionCode("");
                payload.setSspParticipantCode(testContext.getGetEligiblePlansAndOffersResponse().getData().getCustomerCode());
                payload.setSplitConnectionFeeIndicator(null);
                break;
            case SAVE_ENROLLMENT_INVALID_SPLIT_FEE_VALUE_TC417a:
                payload.setEnrollmentStatus(PREPAY_REQUIRED.getValue());
                payload.setPromotionCode("");
                payload.setPlanCode("PGB");
                payload.setSplitConnectionFeeIndicator(true);
                break;
            case SAVE_ENROLLMENT_INVALID_AGLC_ACCOUNT_EMPTY_TC418:
                payload.setEnrollmentStatus(COMPLETE.getValue());
                payload.setPromotionCode("");
                payload.setAglcAccountNumber("");
                break;
            case SAVE_ENROLLMENT_INVALID_AGLC_ACCOUNT_MAX_LENGTH_TC419:
                payload.setEnrollmentStatus(COMPLETE.getValue());
                payload.setPromotionCode("");
                payload.setAglcAccountNumber(FakerDataGenerator.generateDigits(22));
                break;
            case SAVE_ENROLLMENT_INVALID_AGLC_SERVICE_ORDER_EMPTY_TC420:
                payload.setEnrollmentStatus(COMPLETE.getValue());
                payload.setPromotionCode("");
                payload.setAglcServiceOrderNumber("");
                break;
            case SAVE_ENROLLMENT_INVALID_AGLC_SERVICE_ORDER_MAX_LENGTH_TC421:
                payload.setEnrollmentStatus(COMPLETE.getValue());
                payload.setPromotionCode("");
                payload.setAglcServiceOrderNumber(FakerDataGenerator.generateDigits(12));
                break;
            case SAVE_ENROLLMENT_INVALID_NOTES_MAX_LENGTH_TC422:
                payload.setEnrollmentStatus(COMPLETE.getValue());
                payload.setPromotionCode("");
                payload.setNotes(FakerDataGenerator.generateDigits(6005));
                break;
            case SAVE_ENROLLMENT_INVALID_SSP_PC_MAX_LENGTH_TC422a:
                payload.setEnrollmentStatus(COMPLETE.getValue());
                payload.setPromotionCode("");
                payload.setSspParticipantCode(FakerDataGenerator.generateDigits(15));
                break;
            case SAVE_ENROLLMENT_INVALID_SSP_PC_VALUE_TC422b:
                payload.setEnrollmentStatus(COMPLETE.getValue());
                payload.setPromotionCode("");
                payload.setSspParticipantCode(FakerDataGenerator.generateString(3));
                break;
        }
    }

    public void setRequestParamsByPlanCode(SaveEnrollmentRequest payload, GlobalEnums.PlanCode planCode, SaveEnrollmentApiLabel testCondition){

        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setTransactionType(testContext.getGetEligiblePlansAndOffersResponse().getData().getTransactionType());
        payload.setBillingPlan("");
        payload.setTransactionID(Integer.parseInt(testContext.getGetEligiblePlansAndOffersResponse().getData().getTransactionID()));
        payload.setCustomerCode(Integer.parseInt(testContext.getGetEligiblePlansAndOffersResponse().getData().getCustomerCode()));
        payload.setPremisesCode(testContext.getGetEligiblePlansAndOffersResponse().getData().getPremisesCode());

        var plans = testContext.getGetEligiblePlansAndOffersResponse()
                .getData()
                .getPlans();
        Plans prepayPlan =
                plans.stream()
                        //.filter(p -> Boolean.TRUE.equals(p.getPrepayPlanIndicator())
                        .filter(p -> Objects.equals(p.getPlanCode(), planCode.getValue())
                       )
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("No prepay plan found in EligiblePlans response"));

        payload.setPlanCode(prepayPlan.getPlanCode());
        payload.setPromotionCode(prepayPlan.getPromotion1Code());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate serviceDate = LocalDate.parse(prepayPlan.getPrepayCustomerPayByDate(),formatter);
        String serviceDateString = serviceDate.minusDays(1).format(formatter);
        payload.setCustomerRequestedServiceDate(serviceDateString);
        payload.setMarketerReferenceData(String.valueOf(testContext.getMarketerReferenceData()));

        switch (testCondition) {
            case GET_PREPAY_PLANS_REQUOTE_POSITIVE_TC_456:
            case GET_PREPAY_PLANS_REQUOTE_POSITIVE_TC_471:
            case GET_PREPAY_PLANS_REQUOTE_POSITIVE_TC_472:
            case GET_PREPAY_PLANS_REQUOTE_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_448:
            case GET_PREPAY_PLANS_REQUOTE_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_449:
            case GET_PREPAY_PLANS_REQUOTE_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_429:
            case GET_PREPAY_PLANS_REQUOTE_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_430,
                 GET_PREPAY_PLANS_REQUOTE_NEGATIVE_TC_469,
                 GET_PREPAY_PLANS_REQUOTE_EXISTING_QUOTE_NOT_EXPIRED_NEGATIVE_TC_470,
                 GET_PREPAY_PLANS_REQUOTE_PRP_ENROLLED_NEGATIVE_TC_471:
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.PREPAY_REQUIRED.getValue());
                break;
            default:
                payload.setEnrollmentStatus(SAVE_INCOMPLETE.getValue());
        }
    }



}
