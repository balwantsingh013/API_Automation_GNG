package com.gng.api.pages.serviceTransfer.ServiceOrdersPages.SaveEnrollmentPage;


import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.ServiceOrdersPojo.SaveEnrollment.SaveEnrollmentRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.serviceTransfer.ServiceOrdersSteps.SaveEnrollment.SaveEnrollmentApiLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

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
                : null;

        return BasePage.deserializeJsonToPojo(jsonFileName, SaveEnrollmentRequest.class);
    }

    public void setSupportingDefaultParameters(SaveEnrollmentRequest payload){
        payload.setRequestID(FakerDataGenerator.generateString(12));
        payload.setTransactionType(GlobalEnums.TransactionType.TRANSFER.getValue());
        payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.SAVE_INCOMPLETE.getValue());
        payload.setPromotionCode("");
        payload.setCustomerRequestedServiceDate("");
        payload.setSplitConnectionFeeIndicator(true);
        payload.setServiceTransferReward(false);
        payload.setServiceTransferCurrentPricePlan(false);
        payload.setServiceTransferOfferRemainder(false);
        payload.setEstimatedBudgetAmount(null);
        payload.setAglcServiceOrderNumber(FakerDataGenerator.getRandomNumericString(8));
        payload.setSspParticipantCode(null);
        payload.setCurrentMarketerCode(null);
        payload.setMarketerReferenceData(String.valueOf(testContext.getMarketerReferenceData()));
    }

    public void setParametersBasedOnTypeNegative(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel testCondition) {
        setSupportingDefaultParameters(payload);
        setPremisesCodeAndTransactionIdBasedOnType(payload, testCondition);

        switch (testCondition) {
            case ST_SE_MISSING_TRANSACTION_TYPE_TC229 ->
                    payload.setTransactionType(null);

            case ST_SE_INVALID_TRANSACTION_TYPE_LENGTH_TC230 ->
                    payload.setTransactionType(FakerDataGenerator.generateAlphanumeric(32));

            case ST_SE_INVALID_TRANSACTION_TYPE_VALUE_TC231 ->
                    payload.setTransactionType(GlobalEnums.InvalidValues.INVALID_TRANSACTION_TYPE.getValue());

            case ST_SE_MISSING_ENROLLMENT_STATUS_TC232 ->
                    payload.setEnrollmentStatus(null);

            case ST_SE_INVALID_ENROLLMENT_STATUS_LENGTH_TC233 ->
                    payload.setEnrollmentStatus(FakerDataGenerator.generateAlphanumeric(16));

            case ST_SE_INVALID_ENROLLMENT_STATUS_VALUE_TC234 -> {
                setTurnOnDate(payload);
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.DEPOSIT_REQUIRED.getValue());
            }

            case ST_SE_INVALID_ES_PAYMENT_CONFIRMATION_REQUIRED_TC235 -> {
                setTurnOnDate(payload);
                payload.setServiceTransferOfferRemainder(false);
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.DEPOSIT_PAID.getValue());
            }

            case ST_SE_INVALID_PAYMENT_CONFIRMATION_TC236 -> {
                payload.setPaymentConfirmationNumber(FakerDataGenerator.getRandomNumericString(8));
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.SAVE_INCOMPLETE.getValue());
            }

            case ST_SE_INVALID_SSP_PARTICIPANT_CODE_VALUE_TC237 -> {
                payload.setSeasonalSavingsProgramResult(GlobalEnums.SeasonSavingsProgramResult.ENROLLED.getValue());
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.SAVE_INCOMPLETE.getValue());
            }

            case ST_SE_SSP_PARTICIPANT_CODE_NOT_REQUIRED_TC238 ->
                payload.setSspParticipantCode(FakerDataGenerator.getRandomNumericString(6));

            case ST_SE_MISSING_MARKETER_REFERENCE_CE_TRAN_TC239 -> {
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.COMPLETE.getValue());
                payload.setMarketerReferenceData(null);
            }

            case ST_SE_DUPLICATE_MARKETER_REFERENCE_DATA_TC240 ->{
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.COMPLETE.getValue());
                payload.setMarketerReferenceData(GlobalEnums.InvalidValues.DUPLICATE_MARKETER_REFERENCE_NUMBER.getValue());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                LocalDate turnOnDate = LocalDate.now();
                String turnOnDateString = turnOnDate.plusDays(1).format(formatter);
                payload.setCustomerRequestedServiceDate(turnOnDateString);
            }

            case ST_SE_MARKETER_REFERENCE_DATA_INVALID_TYPE_TC242 ->
                    payload.setMarketerReferenceData(FakerDataGenerator.generateAlphanumeric(10));

            case ST_SE_MARKETER_REFERENCE_DATA_TOO_LONG_TC243 ->
                    payload.setMarketerReferenceData(FakerDataGenerator.getRandomNumericString(16));

            case ST_SE_MARKETER_REFERENCE_DATA_TOO_SHORT_TC244 ->
                    payload.setMarketerReferenceData(FakerDataGenerator.getRandomNumericString(10));

            case ST_SE_CURRENT_MARKETER_CODE_PROVIDED_TC245 -> payload.setCurrentMarketerCode(GlobalEnums.CurrentMarketerCode.FIRE.getValue());

            case ST_SE_REQUESTED_TURN_ON_DATE_PROVIDED_TC246 ->  setTurnOnDate(payload);

            case ST_SE_SERVICE_TRANSFER_REWARD_BOOLEAN_ONLY_TC247 ->
                    payload.setServiceTransferReward(FakerDataGenerator.generateString(4));

            case ST_SE_CURRENT_PRICE_PLAN_FIXED_BOOLEAN_ONLY_TC248 ->
                    payload.setServiceTransferCurrentPricePlan(FakerDataGenerator.generateString(2));

            case ST_SE_CURRENT_PRICE_PLAN_CEILING_BOOLEAN_ONLY_TC249 -> {
                payload.setPlanCode(GlobalEnums.PlanCode.GPP.getValue());
                payload.setServiceTransferCurrentPricePlan(FakerDataGenerator.generateString(4));
            }

            case ST_SE_CURRENT_PRICE_PLAN_APPLICABLE_FIXED_OR_CEILING_ONLY_TC250 -> {
                payload.setPlanCode(GlobalEnums.PlanCode.MVS.getValue());
                payload.setServiceTransferCurrentPricePlan(true);
                payload.setPaymentConfirmationNumber(null);
            }

            default -> { }
        }

    }

    public void setTurnOnDate(SaveEnrollmentRequest payload){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate turnOnDate = LocalDate.now();
        String turnOnDateString = turnOnDate.plusDays(1).format(formatter);
        payload.setRequestedTurnOnDate(turnOnDateString);
    }
    public void setParametersFromGetEligiblePlansAndOffersResponse(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel testCondition){
        payload.setCustomerCode(testContext.getGetEligiblePlansAndOffersResponse().getData().getCustomerCode());
        payload.setPremisesCode(testContext.getGetEligiblePlansAndOffersResponse().getData().getPremisesCode());
        payload.setTransactionID(testContext.getGetEligiblePlansAndOffersResponse().getData().getTransactionID());
        payload.setAglcAccountNumber(testContext.getSearchAccountsResponse().getData().getAccounts().getFirst().getAglcAccountNumber());
        payload.setPlanCode(testContext.getGetEligiblePlansAndOffersResponse().getData().getPlans().getFirst().getPlanCode());
    }

    public void setPremisesCodeAndTransactionIdBasedOnType(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel testCondition) {
        payload.setRequestID(FakerDataGenerator.generateString(10));

        switch (testCondition) {
            case ST_SE_INVALID_ENROLLMENT_STATUS_VALUE_TC234,
                 ST_SE_INVALID_ES_PAYMENT_CONFIRMATION_REQUIRED_TC235,
                 ST_SE_INVALID_PAYMENT_CONFIRMATION_TC236,
                 ST_SE_INVALID_SSP_PARTICIPANT_CODE_VALUE_TC237,
                 ST_SE_SSP_PARTICIPANT_CODE_NOT_REQUIRED_TC238,
                 ST_SE_MISSING_MARKETER_REFERENCE_CE_TRAN_TC239,
                 ST_SE_DUPLICATE_MARKETER_REFERENCE_DATA_TC240,
                 ST_SE_MARKETER_REFERENCE_DATA_INVALID_TYPE_TC242,
                 ST_SE_MARKETER_REFERENCE_DATA_TOO_LONG_TC243,
                 ST_SE_MARKETER_REFERENCE_DATA_TOO_SHORT_TC244,
                 ST_SE_CURRENT_MARKETER_CODE_PROVIDED_TC245,
                 ST_SE_REQUESTED_TURN_ON_DATE_PROVIDED_TC246,
                 ST_SE_SERVICE_TRANSFER_REWARD_BOOLEAN_ONLY_TC247,
                 ST_SE_CURRENT_PRICE_PLAN_CEILING_BOOLEAN_ONLY_TC249,
                 ST_SE_CURRENT_PRICE_PLAN_APPLICABLE_FIXED_OR_CEILING_ONLY_TC250 ->
                    setParametersFromGetEligiblePlansAndOffersResponse(payload, testCondition);

            default -> {
                Map<String, Object> uzrrcotRecord = ApplicationContext.get()
                        .getDbAction()
                        .getLatestUZRRCOTRecord();
                payload.setCustomerCode(uzrrcotRecord.get("UZRRCOT_CUST_CODE"));
                payload.setPremisesCode((String) uzrrcotRecord.get("UZRRCOT_PREM_CODE"));
                payload.setTransactionID(uzrrcotRecord.get("UZRRCOT_TRANSACTION_ID"));
            }
        }
    }

    public void setParametersBasedOnTypeExternal(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel testCondition) {
        payload.setRequestID(FakerDataGenerator.generateString(12));

        switch (testCondition) {
                case ST_GE_ENROLLMENT_STATE_INVALID_FOR_TRAN_NEG_TC198
                     -> {
                    setParametersFromGetEligiblePlansAndOffersResponse(payload, testCondition);
                    payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.SAVE_INCOMPLETE.getValue());
                    payload.setTransactionType(GlobalEnums.TransactionType.TRANSFER.getValue());
                    payload.setServiceTransferReward(false);
                    payload.setServiceTransferOfferRemainder(false);
                    payload.setPlanCode(GlobalEnums.PlanCode.GPP.getValue());
                    payload.setServiceTransferCurrentPricePlan(false);
                    payload.setPaymentConfirmationNumber(null);
                    payload.setPromotionCode(testContext.getGetEligiblePlansAndOffersResponse().getData().getPlans().getFirst().getPromotion1Code());
                    payload.setMarketerReferenceData(testContext.getMarketerReferenceData());
                }
            default -> { }
        }
    }
}
