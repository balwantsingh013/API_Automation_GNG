package com.gng.api.pages.meterSet.ServiceOrdersPages.SaveEnrollmentPage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.response.Plans;
import com.gng.api.pojo.ServiceOrdersPojo.SaveEnrollment.SaveEnrollmentRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.meterSet.ServiceOrdersSteps.SaveEnrollment.SaveEnrollmentApiLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.gng.api.pages.poc.CreateAccountNotePage.CreateAccountNoteHelper.assertRowsMatchTokenCount;

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

    public void setSupportingDefaultParameters(SaveEnrollmentRequest payload) {
        payload.setRequestID(FakerDataGenerator.generateString(12));
        payload.setTransactionType(GlobalEnums.TransactionType.METER_SET.getValue());

        payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.SAVE_INCOMPLETE.getValue());

        payload.setPromotionCode("");
        payload.setCustomerRequestedServiceDate("");
        payload.setSplitConnectionFeeIndicator(true);

        payload.setEstimatedBudgetAmount(null);

        payload.setAglcServiceOrderNumber(FakerDataGenerator.getRandomNumericString(8));

        payload.setSspParticipantCode(null);
        payload.setCurrentMarketerCode(null);
        payload.setServiceTransferReward(null);
        payload.setServiceTransferCurrentPricePlan(null);
        payload.setServiceTransferOfferRemainder(null);
        payload.setMarketerReferenceData(testContext.getMarketerReferenceData());
    }


    public void setTurnOnDate(SaveEnrollmentRequest payload) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate turnOnDate = LocalDate.now();
        payload.setRequestedTurnOnDate(turnOnDate.plusDays(120).format(formatter));
    }

    public void setInvalidTurnOnDate(SaveEnrollmentRequest payload) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate earliestTurnOnDate = LocalDate.parse(
                testContext.getGetEligiblePlansAndOffersResponse().getData().getEarliestPossibleTurnOnDate(),
                inputFormatter
        );
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMddyyyy");
        payload.setRequestedTurnOnDate(earliestTurnOnDate.plusDays(120).format(outputFormatter));
    }


    public void setParametersFromGetEligiblePlansAndOffersResponse(
            SaveEnrollmentRequest payload, SaveEnrollmentApiLabel testCondition) {


        var data  = testContext.getGetEligiblePlansAndOffersResponse().getData();
        var plans = data.getPlans();

        payload.setCustomerCode(data.getCustomerCode());
        payload.setPremisesCode(data.getPremisesCode());
        payload.setTransactionID(data.getTransactionID());
        Plans plan = choosePlanForCondition(testCondition, plans);

        payload.setPlanCode(plan.getPlanCode());

        String promo = Optional.ofNullable(plan.getPromotion1Code()).orElse("");
        payload.setPromotionCode(promo);

        String etod = data.getEarliestPossibleTurnOnDate();
        payload.setRequestedTurnOnDate(etod);

        payload.setCustomerRequestedServiceDate(etod);


        if (data.getAglcAccountNumber() != null && !data.getAglcAccountNumber().isEmpty()) {
            payload.setAglcAccountNumber(data.getAglcAccountNumber());
            payload.setAglcServiceOrderNumber(FakerDataGenerator.getRandomNumericString(9));
        } else {
            payload.setAglcAccountNumber("");
            payload.setAglcServiceOrderNumber("");
        }
    }

    private static Plans findPlanByCode(Collection<Plans> plans, String planCode) {
        return plans.stream()
                .filter(p -> Objects.equals(p.getPlanCode(), planCode))
                .findFirst()
                .orElseThrow(() -> {
                    String codes = plans.stream().map(Plans::getPlanCode).sorted().toList().toString();
                    return new AssertionError("Plan " + planCode + " not found. Available: " + codes);
                });
    }

    private static Plans findPlanByNotCode(Collection<Plans> plans, String planCode) {
        return plans.stream()
                .filter(p -> !Objects.equals(p.getPlanCode(), planCode))
                .findFirst()
                .orElseThrow(() -> {
                    String codes = plans.stream().map(Plans::getPlanCode).sorted().toList().toString();
                    return new AssertionError("Non-" + planCode + " plan not found. Available: " + codes);
                });
    }

    private Plans choosePlanForCondition(SaveEnrollmentApiLabel testCondition, List<Plans> plans) {
        return switch (testCondition) {
            case MS_RS_CRDS_ENROLLMENT_CREDIT_CHECK_TC_025,
                 MS_SE_RS_PRP_ENROLLMENT_STATUS_DB_TC_048,
                 MS_SE_RS_R_ENROLLMENT_STATUS_DR_TC_050,
                 MS_SE_RS_B_ENROLLMENT_STATUS_BD_BUDGET_TC_055
                    -> findPlanByCode(plans, GlobalEnums.PlanCode.VML.getValue());

            case MS_SE_BUDGET_BILLING_PRP_NULL_AMOUNT_NOT_ALLOWED_TC_044_1,
                 MS_SE_BUDGET_BILLING_PRP_AMOUNT_PROVIDED_NOT_ALLOWED_TC_044_2,
                 MS_SE_BUDGET_BILLING_NON_PRP_DECIMAL_AMOUNT_INVALID_TC_044_5,
                 MS_SE_BUDGET_BILLING_PLAN_CODE_MISSING_TC_044_6,
                 MS_SE_RS_R_ENROLLMENT_STATUS_PC_REQUOTE_TC_047,
                 MS_SE_RS_NA_ENROLLMENT_STATUS_RP_PRP_TC_050A,
                 MS_SE_RS_PRP_ENROLLMENT_STATUS_PR_TC_051,
                 MS_SE_RS_PRP_ENROLLMENT_STATUS_CP_TC_054
                    -> findPlanByCode(plans, GlobalEnums.PlanCode.PRP.getValue());

            case MS_SE_CM_R_ENROLLMENT_STATUS_SI_TC_049,
                 MS_SE_CM_R_CE_WITH_NOTES_TC_056
                    -> findPlanByCode(plans, GlobalEnums.PlanCode.CVS.getValue());

            case MS_SE_CM_R_ENROLLMENT_STATUS_RD_PROMO_TC_053
                    -> findPlanByCode(plans, GlobalEnums.PlanCode.CCV.getValue());

            case MS_SE_BUDGET_BILLING_NON_PRP_MISSING_AMOUNT_TC_044_3,
                 MS_SE_BUDGET_BILLING_NON_PRP_AMOUNT_TOO_LONG_TC_044_4
                    -> findPlanByNotCode(plans, GlobalEnums.PlanCode.PRP.getValue());

            default -> plans.getFirst();
        };
    }


    public void setPrePayFieldsByCondition(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel testCondition) {
        switch (testCondition) {
            case MS_SE_BUDGET_BILLING_PRP_NULL_AMOUNT_NOT_ALLOWED_TC_044_1,
                 MS_SE_BUDGET_BILLING_PRP_AMOUNT_PROVIDED_NOT_ALLOWED_TC_044_2,
                 MS_SE_BUDGET_BILLING_NON_PRP_MISSING_AMOUNT_TC_044_3,
                 MS_SE_BUDGET_BILLING_NON_PRP_AMOUNT_TOO_LONG_TC_044_4,
                 MS_SE_BUDGET_BILLING_NON_PRP_DECIMAL_AMOUNT_INVALID_TC_044_5,
                 MS_SE_BUDGET_BILLING_PLAN_CODE_MISSING_TC_044_6,
                 MS_SE_RS_B_ENROLLMENT_STATUS_CE_TC_045 -> {
                payload.setEstimatedBudgetAmount(FakerDataGenerator.generateDigits(2));
                payload.setCustomerRequestedServiceDate(payload.getRequestedTurnOnDate());
                payload.setPaymentConfirmationNumber(FakerDataGenerator.getRandomNumericString(6));
                payload.setBillingPlan(GlobalEnums.BillingPlan.BUDGET.getValue());
            }
            default -> {  }
        }
    }


    public void setParametersBasedOnTypePositive(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel testCondition) {
        setSupportingDefaultParameters(payload);
        setParametersFromGetEligiblePlansAndOffersResponse(payload, testCondition);

        var geData  = testContext.getGetEligiblePlansAndOffersResponse().getData();

        switch (testCondition) {
            case MS_SE_RS_B_ENROLLMENT_STATUS_CE_TC_045 -> {
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.COMPLETE.getValue());
                payload.setBillingPlan(GlobalEnums.BillingPlan.BUDGET.getValue());
                payload.setEstimatedBudgetAmount(FakerDataGenerator.generateDigits(3));
                payload.setCustomerRequestedServiceDate(geData.getEarliestPossibleTurnOnDate());
                payload.setRequestedTurnOnDate(geData.getEarliestPossibleTurnOnDate());
                payload.setSplitConnectionFeeIndicator(Boolean.FALSE);
            }

            case MS_SE_CM_R_ENROLLMENT_STATUS_DP_TC_046 -> {
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.DEPOSIT_REQUIRED.getValue());
                payload.setCustomerRequestedServiceDate(geData.getEarliestPossibleTurnOnDate());
                payload.setPromotionCode("");
                payload.setSplitConnectionFeeIndicator(Boolean.FALSE);
            }

            case MS_SE_RS_R_ENROLLMENT_STATUS_PC_REQUOTE_TC_047 -> {
                payload.setBillingPlan(null);
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.PREPAY_REQUIRED.getValue());
                payload.setCustomerRequestedServiceDate(geData.getEarliestPossibleTurnOnDate());
                payload.setRequestedTurnOnDate(geData.getEarliestPossibleTurnOnDate());
                payload.setPromotionCode("");
                payload.setSplitConnectionFeeIndicator(Boolean.FALSE);
            }

            case MS_SE_RS_PRP_ENROLLMENT_STATUS_DB_TC_048 -> {
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.DEPOSIT_REQUIRED.getValue());
                payload.setPaymentConfirmationNumber(FakerDataGenerator.getRandomNumericString(7));
                payload.setCustomerRequestedServiceDate("");
                payload.setPromotionCode("");
                payload.setSplitConnectionFeeIndicator(Boolean.FALSE);
            }

            case MS_SE_CM_R_ENROLLMENT_STATUS_SI_TC_049 -> {
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.SAVE_INCOMPLETE.getValue());
                payload.setPromotionCode("");
            }

            case MS_SE_RS_R_ENROLLMENT_STATUS_DR_TC_050 -> {
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.DEPOSIT_REQUIRED.getValue());
                payload.setPromotionCode("");
                payload.setCustomerRequestedServiceDate("");
                payload.setSplitConnectionFeeIndicator(Boolean.FALSE);
            }

            case MS_SE_RS_NA_ENROLLMENT_STATUS_RP_PRP_TC_050A -> {
                payload.setBillingPlan(null);
                payload.setPromotionCode("");
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.REFUSED_PREPAY.getValue());
                payload.setRequestedTurnOnDate(geData.getEarliestPossibleTurnOnDate());
                payload.setSplitConnectionFeeIndicator(Boolean.FALSE);
            }

            case MS_SE_RS_PRP_ENROLLMENT_STATUS_PR_TC_051 -> {
                payload.setBillingPlan(null);
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.PREPAY_REQUIRED.getValue());
                payload.setPaymentConfirmationNumber(FakerDataGenerator.getRandomNumericString(7));
                payload.setRequestedTurnOnDate(geData.getEarliestPossibleTurnOnDate());
                payload.setSplitConnectionFeeIndicator(Boolean.FALSE);
                payload.setPromotionCode("");
            }

            case MS_SE_CM_R_ENROLLMENT_STATUS_RD_PROMO_TC_053 -> {
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.REFUSED_DEPOSIT.getValue());
                payload.setPromotionCode("");
                payload.setCustomerRequestedServiceDate("");
                payload.setSplitConnectionFeeIndicator(Boolean.FALSE);
            }

            case MS_SE_RS_PRP_ENROLLMENT_STATUS_CP_TC_054 -> {
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.PREPAY_REQUIRED.getValue());
                payload.setBillingPlan(null);
                payload.setCustomerRequestedServiceDate(null);
                payload.setPromotionCode("");
                payload.setSplitConnectionFeeIndicator(Boolean.FALSE);
            }

            case MS_SE_RS_B_ENROLLMENT_STATUS_BD_BUDGET_TC_055 -> {
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.BILL_DEPOSIT.getValue());
                payload.setBillingPlan(GlobalEnums.BillingPlan.BUDGET.getValue());
                payload.setEstimatedBudgetAmount(FakerDataGenerator.generateDigits(3));
                payload.setCustomerRequestedServiceDate(geData.getEarliestPossibleTurnOnDate());
                payload.setRequestedTurnOnDate(geData.getEarliestPossibleTurnOnDate());
                payload.setSplitConnectionFeeIndicator(Boolean.FALSE);
            }
            default -> { }
        }
    }

    public void setSecondRequestParametersBasedOnTypePositive(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel testCondition) {
        setSupportingDefaultParameters(payload);
        setParametersFromGetEligiblePlansAndOffersResponse(payload, testCondition);

        switch (testCondition) {
            case MS_SE_RS_R_ENROLLMENT_STATUS_PC_REQUOTE_TC_047 -> {
                payload.setBillingPlan(null);
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.PAYMENT_COMPLETE.getValue());
                payload.setPaymentConfirmationNumber(FakerDataGenerator.getRandomNumericString(8));
                payload.setSplitConnectionFeeIndicator(Boolean.FALSE);
                payload.setAglcAccountNumber("");
                payload.setAglcServiceOrderNumber("");
                payload.setPromotionCode("");
            }
            case MS_SE_RS_PRP_ENROLLMENT_STATUS_DB_TC_048 -> {
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.DEPOSIT_BILLED.getValue());
                payload.setPaymentConfirmationNumber(FakerDataGenerator.getRandomNumericString(8));
                payload.setSplitConnectionFeeIndicator(Boolean.FALSE);
                payload.setAglcAccountNumber("");
                payload.setAglcServiceOrderNumber("");
                payload.setPromotionCode("");
            }
            default -> {  }
        }
    }

    public void setParametersBasedOnTypePositiveWithNote(SaveEnrollmentRequest payload, String noteText, SaveEnrollmentApiLabel testCondition) {
        setSupportingDefaultParameters(payload);
        setParametersFromGetEligiblePlansAndOffersResponse(payload, testCondition);

        var geData = testContext.getGetEligiblePlansAndOffersResponse().getData();

        switch (testCondition) {
            case MS_SE_CM_R_CE_WITH_NOTES_TC_056 -> {
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.COMPLETE.getValue());
                payload.setCustomerRequestedServiceDate(payload.getRequestedTurnOnDate());
                payload.setPaymentConfirmationNumber(FakerDataGenerator.getRandomNumericString(7));
                payload.setPromotionCode("");
                payload.setSplitConnectionFeeIndicator(Boolean.FALSE);
                payload.setNotes(noteText);
            }
        }
    }

    public void setParametersBasedOnTypeNegative(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel testCondition) {
        setSupportingDefaultParameters(payload);
        setParametersFromGetEligiblePlansAndOffersResponse(payload, testCondition);
        setPrePayFieldsByCondition(payload, testCondition);

        switch (testCondition) {
            case MS_GE_RS_INCL_TIER_5_TC_023 -> {
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.SAVE_INCOMPLETE.getValue());
            }

            case MS_SE_MISSING_TRANSACTION_TYPE_TC_035 -> {
                payload.setTransactionType(null);
                payload.setSplitConnectionFeeIndicator(Boolean.TRUE);
            }
            case MS_SE_TRANSACTION_TYPE_MAX_LENGTH_TC_036 -> {
                payload.setTransactionType(FakerDataGenerator.generateUpperCaseString(5));
                payload.setSplitConnectionFeeIndicator(Boolean.TRUE);
            }
            case MS_SE_TRANSACTION_TYPE_INVALID_VALUE_TC_037 -> {
                payload.setTransactionType(GlobalEnums.InvalidValues.INVALID_TRANSACTION_TYPE.getValue());
                payload.setSplitConnectionFeeIndicator(Boolean.TRUE);
            }

            case MS_SE_SSP_RESULT_SHOULD_BE_NULL_TC_038 -> {
                payload.setSeasonalSavingsProgramResult(FakerDataGenerator.generateString(4));
                payload.setSplitConnectionFeeIndicator(Boolean.TRUE);
                payload.setAglcAccountNumber("");
                payload.setAglcServiceOrderNumber("");
            }
            case MS_SE_AGLC_ACCOUNT_NUMBER_SHOULD_BE_NULL_TC_039 -> {
                payload.setAglcAccountNumber(FakerDataGenerator.getRandomNumericString(9));
                payload.setSplitConnectionFeeIndicator(Boolean.TRUE);
            }
            case MS_SE_AGLC_SERVICE_ORDER_NUMBER_SHOULD_BE_NULL_TC_040 -> {
                payload.setAglcServiceOrderNumber(FakerDataGenerator.getRandomNumericString(8));
                payload.setSplitConnectionFeeIndicator(Boolean.TRUE);
            }
            case MS_SE_SSP_PARTICIPANT_CODE_SHOULD_BE_NULL_TC_041 -> {
                payload.setSspParticipantCode(testContext.getGetEligiblePlansAndOffersResponse().getData().getCustomerCode());
                payload.setSplitConnectionFeeIndicator(Boolean.TRUE);
            }
            case MS_SE_CURRENT_MARKETER_CODE_SHOULD_BE_NULL_TC_042 -> {
                payload.setCurrentMarketerCode(FakerDataGenerator.generateUpperCaseString(3));
                payload.setSplitConnectionFeeIndicator(Boolean.TRUE);
            }

            case MS_SE_MISSING_REQUESTED_TURN_ON_DATE_TC_043 -> {
                payload.setRequestedTurnOnDate(null);
                payload.setSplitConnectionFeeIndicator(Boolean.TRUE);
            }
            case MS_SE_REQUESTED_TURN_ON_DATE_INVALID_FORMAT_TC_044 -> {
                setInvalidTurnOnDate(payload);
                payload.setSplitConnectionFeeIndicator(Boolean.TRUE);
            }

            case MS_SE_BUDGET_BILLING_PRP_NULL_AMOUNT_NOT_ALLOWED_TC_044_1 -> {
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.PAYMENT_COMPLETE.getValue());
                payload.setEstimatedBudgetAmount(null);
                payload.setSplitConnectionFeeIndicator(Boolean.TRUE);
            }
            case MS_SE_BUDGET_BILLING_PRP_AMOUNT_PROVIDED_NOT_ALLOWED_TC_044_2 -> {
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.PAYMENT_COMPLETE.getValue());
                payload.setPlanCode(GlobalEnums.PlanCode.PRP.getValue());
                payload.setSplitConnectionFeeIndicator(Boolean.TRUE);
            }
            case MS_SE_BUDGET_BILLING_NON_PRP_MISSING_AMOUNT_TC_044_3 -> {
                payload.setPromotionCode("");
                payload.setEstimatedBudgetAmount(null);
                payload.setSplitConnectionFeeIndicator(Boolean.TRUE);
            }
            case MS_SE_BUDGET_BILLING_NON_PRP_AMOUNT_TOO_LONG_TC_044_4 -> {
                payload.setPromotionCode("");
                payload.setEstimatedBudgetAmount(FakerDataGenerator.getRandomNumericString(7));
                payload.setSplitConnectionFeeIndicator(Boolean.TRUE);
            }
            case MS_SE_BUDGET_BILLING_NON_PRP_DECIMAL_AMOUNT_INVALID_TC_044_5 -> {
                payload.setEstimatedBudgetAmount(FakerDataGenerator.generateDigits(2) + "." + FakerDataGenerator.generateDigits(2));
                payload.setSplitConnectionFeeIndicator(Boolean.TRUE);
            }
            case MS_SE_BUDGET_BILLING_PLAN_CODE_MISSING_TC_044_6 -> {
                payload.setPlanCode(null);
                payload.setEstimatedBudgetAmount("100");
                payload.setSplitConnectionFeeIndicator(Boolean.TRUE);
            }

            default -> {  }
        }
    }

    public void setParametersBasedOnTypeExternal(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel testCondition) {
        setSupportingDefaultParameters(payload);
        Plans plan;

        switch (testCondition) {
            case MS_GE_RS_ACN_LAND_BYPASS_CREDIT_TC_022,
                 MS_GE_RS_INCL_TIER_5_TC_023,
                 MS_RS_MULTIPLE_PREM_TC_024 -> {
                plan = findPlanByCode(
                        testContext.getGetEligiblePlansAndOffersResponse().getData().getPlans(),
                        GlobalEnums.PlanCode.MVS.getValue());
                setParametersFromGetEligiblePlansAndOffersResponse(payload, testCondition);
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.SAVE_INCOMPLETE.getValue());
                payload.setPlanCode(plan.getPlanCode());
                payload.setPromotionCode("");
                payload.setAglcAccountNumber("");
                payload.setAglcServiceOrderNumber(null);
                payload.setSplitConnectionFeeIndicator(Boolean.FALSE);
            }

            case MS_RS_CRDS_ENROLLMENT_CREDIT_CHECK_TC_025 -> {
                setParametersFromGetEligiblePlansAndOffersResponse(payload, testCondition);
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.DEPOSIT_REQUIRED.getValue());
                payload.setAglcAccountNumber("");
                payload.setAglcServiceOrderNumber(null);
                payload.setServiceTransferReward(null);
                payload.setServiceTransferCurrentPricePlan(null);
                payload.setServiceTransferOfferRemainder(null);
                payload.setSplitConnectionFeeIndicator(Boolean.FALSE);
            }

            case MS_CM_CRDS_EN_CREDIT_CHECK_YES_COMM_DEP_PROSP_TC_033,
                 MS_CM_CRDS_EN_CREDIT_CHECK_YES_BUSINESS_NAME_TC_034 -> {
                plan = findPlanByCode(
                        testContext.getGetEligiblePlansAndOffersResponse().getData().getPlans(),
                        GlobalEnums.PlanCode.CVS.getValue());
                setParametersFromGetEligiblePlansAndOffersResponse(payload, testCondition);
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.DEPOSIT_REQUIRED.getValue());
                payload.setPlanCode(plan.getPlanCode());
               // setTurnOnDate(payload);
                payload.setPromotionCode("");
                payload.setAglcAccountNumber("");
                payload.setAglcServiceOrderNumber(null);
                payload.setServiceTransferReward(null);
                payload.setServiceTransferCurrentPricePlan(null);
                payload.setServiceTransferOfferRemainder(null);
                payload.setSplitConnectionFeeIndicator(Boolean.FALSE);
            }

            default -> { }
        }
    }

    public void setParametersSecondCallTypeExternal(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel testCondition) {
        setSupportingDefaultParameters(payload);
        payload.setAglcAccountNumber("");
        payload.setAglcServiceOrderNumber(null);
        payload.setServiceTransferReward(null);
        payload.setServiceTransferCurrentPricePlan(null);
        payload.setServiceTransferOfferRemainder(null);

        Plans plan;
        switch (testCondition) {
            case MS_GE_RS_INCL_TIER_5_TC_023 -> {
                plan = findPlanByNotCode(
                        testContext.getGetEligiblePlansAndOffersResponse().getData().getPlans(),
                        GlobalEnums.PlanCode.MVS.getValue());
                setParametersFromGetEligiblePlansAndOffersResponse(payload, testCondition);
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.SAVE_INCOMPLETE.getValue());
                payload.setPlanCode(plan.getPlanCode());
                payload.setPromotionCode(plan.getPromotion1Code());
                setTurnOnDate(payload);
                payload.setSplitConnectionFeeIndicator(Boolean.FALSE);
            }

            case MS_RS_CRDS_ENROLLMENT_CREDIT_CHECK_TC_025 -> {
                plan = findPlanByCode(
                        testContext.getGetEligiblePlansAndOffersResponse().getData().getPlans(),
                        GlobalEnums.PlanCode.VML.getValue());
                setParametersFromGetEligiblePlansAndOffersResponse(payload, testCondition);
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.DEPOSIT_BILLED.getValue());
                payload.setPlanCode(plan.getPlanCode());
                payload.setPromotionCode("");
                payload.setSplitConnectionFeeIndicator(Boolean.FALSE);
                var geData = testContext.getGetEligiblePlansAndOffersResponse().getData();
                payload.setCustomerRequestedServiceDate(geData.getEarliestPossibleTurnOnDate());
                payload.setRequestedTurnOnDate(geData.getEarliestPossibleTurnOnDate());
                payload.setPaymentConfirmationNumber(FakerDataGenerator.getRandomNumericString(7));
                payload.setAglcAccountNumber("");
                payload.setAglcServiceOrderNumber("");
            }

            default -> { }
        }
    }

    public void validateNoteCreation(String noteText, String testCondition) {
        String expected = noteText.replace("\\|~", "|~");
        //getNoteByCustomerCode();
        List<String> storedValues = getNoteByCustomerCode();
        assertRowsMatchTokenCount(expected, storedValues);

    }

    public List<String> getNoteByCustomerCode(){
        String custCode = testContext.getGetEligiblePlansAndOffersResponse().getData().getCustomerCode();

        List<Map<String, Object>> rows = ApplicationContext.get()
                .getDbAction()
                .getNoteByCustomerCode(custCode);

        if (rows == null || rows.isEmpty()) {
            throw new IllegalStateException("No DB rows returned for customer code " + custCode );
        }

        String seq = rows.getFirst().get("UCBNOTE_SEQ_NUMBER").toString();

        Map<String, Object> noteRow = ApplicationContext.get()
                .getDbAction()
                .getNoteBySequenceNumber(seq);

        if (noteRow == null || noteRow.isEmpty()) {
            throw new IllegalStateException("No DB rows returned for note sequence " + seq);
        }

        final String VALUE_KEY = firstExistingKey(rows,"UCBNOTE_SEQ_NUMBER");

        return rows.stream()
                .map(r -> String.valueOf(r.get(VALUE_KEY)))
                .toList();
    }

    private static String firstExistingKey(List<Map<String, Object>> rows, String... candidates) {
        for (String k : candidates) {
            if (rows.getFirst().containsKey(k)) return k;
        }
        throw new IllegalStateException("Could not find note-value column in DB rows.");
    }

    public void setPremisesCodeAndTransactionIdBasedOnType(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel testCondition) {
        setParametersFromGetEligiblePlansAndOffersResponse(payload, testCondition);
    }
}
