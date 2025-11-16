package com.gng.api.pages.meterSet.ServiceOrdersPages.SaveEnrollmentPage;




import com.gng.api.constants.GlobalEnums;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.response.Plans;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.pojo.ServiceOrdersPojo.SaveEnrollment.SaveEnrollmentRequest;
import com.gng.api.steps.meterSet.ServiceOrdersSteps.SaveEnrollment.SaveEnrollmentApiLabel;

import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


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
        payload.setTransactionType(GlobalEnums.TransactionType.METER_SET.getValue());
        payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.SAVE_INCOMPLETE.getValue());
        payload.setPromotionCode("");
        payload.setCustomerRequestedServiceDate("");
        payload.setSplitConnectionFeeIndicator(true);
        payload.setServiceTransferReward(false);
        payload.setServiceTransferCurrentPricePlan(false);
        payload.setServiceTransferOfferRemainder(false);
        payload.setEstimatedBudgetAmount(null);
        payload.setAglcServiceOrderNumber(FakerDataGenerator.getRandomNumericString(8)); // remove?
        payload.setSspParticipantCode(null);
        payload.setCurrentMarketerCode(null);
        payload.setMarketerReferenceData(testContext.getMarketerReferenceData());
    }

    public void setTurnOnDate(SaveEnrollmentRequest payload){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate turnOnDate = LocalDate.now();
        String turnOnDateString = turnOnDate.plusDays(120).format(formatter);
        payload.setRequestedTurnOnDate(turnOnDateString);
    }

    public void setInvalidTurnOnDate(SaveEnrollmentRequest payload){
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate earliestTurnOnDate = LocalDate.parse(
                testContext.getGetEligiblePlansAndOffersResponse().getData().getEarliestPossibleTurnOnDate(),
                inputFormatter
        );

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMddyyyy");
        String turnOnDateString = earliestTurnOnDate.plusDays(120).format(outputFormatter);

        payload.setRequestedTurnOnDate(turnOnDateString);
    }

    public void setCustomerRequestedServiceDate(SaveEnrollmentRequest payload){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate customerRequestedServiceDate = LocalDate.now();
        String customerRequestedServiceDateString = customerRequestedServiceDate.plusDays(1).format(formatter);
        payload.setCustomerRequestedServiceDate(customerRequestedServiceDateString);
    }
    public void setParametersFromGetEligiblePlansAndOffersResponse(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel testCondition){
        //service transfer params need to be null
        payload.setServiceTransferReward(null);
        payload.setServiceTransferCurrentPricePlan(null);
        payload.setServiceTransferOfferRemainder(null);

        var data = testContext.getGetEligiblePlansAndOffersResponse().getData();
        Plans plan;
//        plan = findPlanByCode(
//                testContext.getGetEligiblePlansAndOffersResponse().getData().getPlans(),
//                GlobalEnums.PlanCode.MVS.getValue());
        plan = testContext.getGetEligiblePlansAndOffersResponse().getData().getPlans().getFirst();

        payload.setCustomerCode(data.getCustomerCode());
        payload.setPremisesCode(data.getPremisesCode());
        payload.setTransactionID(data.getTransactionID());
        payload.setPlanCode(data.getPlans().getFirst().getPlanCode());
        payload.setEnrollmentStatus(data.getPlans().getFirst().getEnrollmentStatus().getFirst().getCode());


        switch (testCondition){
            case MS_GE_RS_ACN_LAND_BYPASS_CREDIT_TC_022,
                 MS_GE_RS_INCL_TIER_5_TC_023, MS_RS_MULTIPLE_PREM_TC_024,
                 MS_CM_CRDS_EN_CREDIT_CHECK_YES_COMM_DEP_PROSP_TC_033,
                 MS_CM_CRDS_EN_CREDIT_CHECK_YES_BUSINESS_NAME_TC_034 -> payload.setRequestedTurnOnDate(data.getEarliestPossibleTurnOnDate());
            case MS_SE_BUDGET_BILLING_NON_PRP_MISSING_AMOUNT_TC_044_3,
                 MS_SE_BUDGET_BILLING_NON_PRP_AMOUNT_TOO_LONG_TC_044_4 -> {
                plan = findPlanByNotCode(
                    testContext.getGetEligiblePlansAndOffersResponse().getData().getPlans(),
                    GlobalEnums.PlanCode.PRP.getValue());
                payload.setPlanCode(plan.getPlanCode());
                payload.setEnrollmentStatus(plan.getEnrollmentStatus().getFirst().getCode());

            }
            case MS_SE_BUDGET_BILLING_PRP_NULL_AMOUNT_NOT_ALLOWED_TC_044_1,
                 MS_SE_BUDGET_BILLING_PRP_AMOUNT_PROVIDED_NOT_ALLOWED_TC_044_2,
                 MS_SE_BUDGET_BILLING_NON_PRP_DECIMAL_AMOUNT_INVALID_TC_044_5,
                 MS_SE_BUDGET_BILLING_PLAN_CODE_MISSING_TC_044_6, MS_SE_RS_R_ENROLLMENT_STATUS_PC_REQUOTE_TC_047,
                 MS_SE_RS_NA_ENROLLMENT_STATUS_RP_PRP_TC_050A, MS_SE_RS_PRP_ENROLLMENT_STATUS_PR_TC_051, MS_SE_RS_PRP_ENROLLMENT_STATUS_CP_TC_054
                  ->{
                plan = findPlanByCode(
                        testContext.getGetEligiblePlansAndOffersResponse().getData().getPlans(),
                        GlobalEnums.PlanCode.PRP.getValue());
                        payload.setPlanCode(plan.getPlanCode());
                        payload.setEnrollmentStatus(plan.getEnrollmentStatus().getFirst().getCode());
                        payload.setCustomerRequestedServiceDate(data.getEarliestPossibleTurnOnDate());
                        //check < 54 for makPromo
                        payload.setPromotionCode(plan.getPromotion1Code());

            }
            case MS_SE_RS_PRP_ENROLLMENT_STATUS_DB_TC_048, MS_SE_RS_R_ENROLLMENT_STATUS_DR_TC_050 -> {
                plan = findPlanByCode(
                        testContext.getGetEligiblePlansAndOffersResponse().getData().getPlans(),
                        GlobalEnums.PlanCode.VML.getValue());
                payload.setPlanCode(plan.getPlanCode());
                payload.setEnrollmentStatus(plan.getEnrollmentStatus().getFirst().getCode());
                payload.setPromotionCode(plan.getPromotion1Code());
            }

            case MS_SE_CM_R_ENROLLMENT_STATUS_RD_PROMO_TC_053 -> {
                plan = findPlanByCode(
                        testContext.getGetEligiblePlansAndOffersResponse().getData().getPlans(),
                        GlobalEnums.PlanCode.CCV.getValue());
                payload.setPlanCode(plan.getPlanCode());
                payload.setEnrollmentStatus(plan.getEnrollmentStatus().getFirst().getCode());
                payload.setPromotionCode(plan.getPromotion1Code());
            }

            case MS_SE_RS_B_ENROLLMENT_STATUS_CE_TC_045 -> {
                plan = findPlanByCode(
                        testContext.getGetEligiblePlansAndOffersResponse().getData().getPlans(),
                        GlobalEnums.PlanCode.MVS.getValue());
                payload.setPlanCode(plan.getPlanCode());
                payload.setEnrollmentStatus(plan.getEnrollmentStatus().getFirst().getCode());
                payload.setPromotionCode(plan.getPromotion1Code());
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.COMPLETE.getValue());

            }
            case MS_SE_CM_R_ENROLLMENT_STATUS_SI_TC_049 -> {
                plan = findPlanByCode(
                        testContext.getGetEligiblePlansAndOffersResponse().getData().getPlans(),
                        GlobalEnums.PlanCode.CVS.getValue());
                payload.setPlanCode(plan.getPlanCode());
                payload.setEnrollmentStatus(plan.getEnrollmentStatus().getFirst().getCode());
                payload.setPromotionCode(plan.getPromotion1Code());
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.SAVE_INCOMPLETE.getValue());
            }

            default -> {
//                plan = findPlanByCode(
//                        testContext.getGetEligiblePlansAndOffersResponse().getData().getPlans(),
//                        GlobalEnums.PlanCode.MVS.getValue());
//                payload.setEnrollmentStatus(plan.getEnrollmentStatus().getFirst().getCode());
            }
        }

        if (data.getAglcAccountNumber() != null && !data.getAglcAccountNumber().isEmpty()) {
            payload.setAglcAccountNumber(data.getAglcAccountNumber());
            payload.setAglcServiceOrderNumber(FakerDataGenerator.getRandomNumericString(9));
        }
        else{
            payload.setAglcAccountNumber("");
            payload.setAglcServiceOrderNumber("");
        }
        payload.setRequestedTurnOnDate(data.getEarliestPossibleTurnOnDate());

    }

    public void setPrePayFieldsByCondition(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel testCondition) {
        switch (testCondition){
            case MS_SE_BUDGET_BILLING_PRP_NULL_AMOUNT_NOT_ALLOWED_TC_044_1,
                 MS_SE_BUDGET_BILLING_PRP_AMOUNT_PROVIDED_NOT_ALLOWED_TC_044_2,
                 MS_SE_BUDGET_BILLING_NON_PRP_MISSING_AMOUNT_TC_044_3,
                 MS_SE_BUDGET_BILLING_NON_PRP_AMOUNT_TOO_LONG_TC_044_4,
                 MS_SE_BUDGET_BILLING_NON_PRP_DECIMAL_AMOUNT_INVALID_TC_044_5,
                 MS_SE_BUDGET_BILLING_PLAN_CODE_MISSING_TC_044_6,
                 MS_SE_RS_B_ENROLLMENT_STATUS_CE_TC_045-> {
                payload.setEstimatedBudgetAmount(FakerDataGenerator.generateDigits(2));
                payload.setCustomerRequestedServiceDate(payload.getRequestedTurnOnDate());
                payload.setPaymentConfirmationNumber(FakerDataGenerator.getRandomNumericString(6));
                payload.setBillingPlan(GlobalEnums.BillingPlan.BUDGET.getValue());
            }
        }
    }

    public void setParametersBasedOnTypePositive(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel testCondition) {
        setSupportingDefaultParameters(payload);
        setParametersFromGetEligiblePlansAndOffersResponse(payload, testCondition);
        // only sets Budget fields for the NEGATIVE 044.x cases; positives handled below explicitly
        // setPrePayFieldsByCondition(payload, testCondition);

        var geData  = testContext.getGetEligiblePlansAndOffersResponse().getData();
        var plans   = geData.getPlans();
        Plans plan;

        switch (testCondition) {
            // 045: Residential + Budget, finalize as COMPLETE
            case MS_SE_RS_B_ENROLLMENT_STATUS_CE_TC_045 -> {
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.COMPLETE.getValue());
                payload.setBillingPlan(GlobalEnums.BillingPlan.BUDGET.getValue());
                payload.setEstimatedBudgetAmount(FakerDataGenerator.generateDigits(3));
                payload.setCustomerRequestedServiceDate(geData.getEarliestPossibleTurnOnDate());
                payload.setRequestedTurnOnDate(geData.getEarliestPossibleTurnOnDate());
                payload.setSplitConnectionFeeIndicator(Boolean.FALSE);
            }

            // 046: Commercial, Deposit Required
            case MS_SE_CM_R_ENROLLMENT_STATUS_DP_TC_046 -> {
                payload.setCustomerCode(GlobalEnums.CustomerType.COMMERCIAL);
                plan = findPlanByCode(plans, GlobalEnums.PlanCode.CVS.getValue());
                payload.setPlanCode(plan.getPlanCode());
                payload.setPromotionCode(""); // no promo by default
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.DEPOSIT_REQUIRED.getValue());
               // setTurnOnDate(payload);
                //setCustomerRequestedServiceDate(payload);
                payload.setCustomerRequestedServiceDate(geData.getEarliestPossibleTurnOnDate());
                payload.setSplitConnectionFeeIndicator(Boolean.FALSE);
            }

            case MS_SE_RS_R_ENROLLMENT_STATUS_PC_REQUOTE_TC_047 -> {
                payload.setBillingPlan(null);
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.PREPAY_REQUIRED.getValue());
                //payload.setRequestedTurnOnDate(geData.getEarliestPossibleTurnOnDate());
                payload.setCustomerRequestedServiceDate(geData.getEarliestPossibleTurnOnDate());
                payload.setRequestedTurnOnDate(geData.getEarliestPossibleTurnOnDate());
                payload.setSplitConnectionFeeIndicator(Boolean.FALSE);
            }

            // 048: Residential PRP, Deposit Billed
            case MS_SE_RS_PRP_ENROLLMENT_STATUS_DB_TC_048 -> {
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.DEPOSIT_REQUIRED.getValue());
                payload.setPaymentConfirmationNumber(FakerDataGenerator.getRandomNumericString(7));
                payload.setRequestedTurnOnDate(geData.getEarliestPossibleTurnOnDate());
                payload.setCustomerRequestedServiceDate("");
                payload.setSplitConnectionFeeIndicator(Boolean.FALSE);
            }

            // 049: Commercial, Save Incomplete
            case MS_SE_CM_R_ENROLLMENT_STATUS_SI_TC_049 -> {
//                plan = findPlanByCode(plans, GlobalEnums.PlanCode.CVS.getValue());
//                payload.setPlanCode(plan.getPlanCode());
                payload.setPromotionCode("");
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.SAVE_INCOMPLETE.getValue());
                payload.setSplitConnectionFeeIndicator(Boolean.FALSE);
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
                payload.setCustomerRequestedServiceDate(geData.getEarliestPossibleTurnOnDate());
                payload.setSplitConnectionFeeIndicator(Boolean.FALSE);
            }

            case MS_SE_CM_R_ENROLLMENT_STATUS_RD_PROMO_TC_053 -> {
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.REFUSED_DEPOSIT.getValue());
                payload.setPromotionCode("");
                payload.setCustomerRequestedServiceDate(null);
                payload.setSplitConnectionFeeIndicator(Boolean.FALSE);
            }

            case MS_SE_RS_PRP_ENROLLMENT_STATUS_CP_TC_054 -> {
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.PREPAY_REQUIRED.getValue());
                payload.setBillingPlan(null);
                payload.setCustomerRequestedServiceDate(null);
                payload.setSplitConnectionFeeIndicator(Boolean.FALSE);
            }

            case MS_SE_RS_B_ENROLLMENT_STATUS_BD_BUDGET_TC_055 -> {
                // keep selected residential plan; enable budget billing successfully
                payload.setBillingPlan(GlobalEnums.BillingPlan.BUDGET.getValue());
                payload.setEstimatedBudgetAmount(FakerDataGenerator.generateDigits(2));
                payload.setCustomerRequestedServiceDate(geData.getEarliestPossibleTurnOnDate());
                payload.setRequestedTurnOnDate(geData.getEarliestPossibleTurnOnDate());
                payload.setSplitConnectionFeeIndicator(Boolean.FALSE);
                // leave enrollmentStatus as seeded (happy path)
            }

            // 056: Commercial, CE with notes – choose CVS and finalize as COMPLETE
            case MS_SE_CM_R_CE_WITH_NOTES_TC_056 -> {
                plan = findPlanByCode(plans, GlobalEnums.PlanCode.CVS.getValue());
                payload.setPlanCode(plan.getPlanCode());
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.COMPLETE.getValue());
                payload.setCustomerRequestedServiceDate(payload.getRequestedTurnOnDate());
                payload.setPromotionCode(""); // explicit none
                payload.setSplitConnectionFeeIndicator(Boolean.FALSE);
                // If your SaveEnrollmentRequest has a notes/comments field, set it here.
                // e.g., payload.setCsrComments("Automation: CE with notes");
            }

            // (default) nothing extra – rely on GE-seeded fields
            default -> { /* no-op */ }
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
            }
            case MS_SE_RS_PRP_ENROLLMENT_STATUS_DB_TC_048 -> {
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.DEPOSIT_BILLED.getValue());
                payload.setPaymentConfirmationNumber(FakerDataGenerator.getRandomNumericString(8));
                payload.setSplitConnectionFeeIndicator(Boolean.FALSE);
                payload.setAglcAccountNumber("");
                payload.setAglcServiceOrderNumber("");
            }

            default -> { /* no-op */ }
        }
    }


    public void setParametersBasedOnTypeNegative(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel testCondition) {
        setSupportingDefaultParameters(payload);
        setParametersFromGetEligiblePlansAndOffersResponse(payload, testCondition);
        setPrePayFieldsByCondition(payload, testCondition);
        switch (testCondition) {
            case MS_GE_RS_INCL_TIER_5_TC_023 -> {
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.SAVE_INCOMPLETE.getValue());
                // payload.setPlanCode(GlobalEnums.PlanCode.MVS.getValue());
                // setTurnOnDate(payload);
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
            // 038–042: fields that MUST be null (send non-null to trigger “should be null”)
            case MS_SE_SSP_RESULT_SHOULD_BE_NULL_TC_038 -> {
                //payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.REFUSED_DEPOSIT.getValue());
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

            // 043–044: Requested Turn On Date rules
            case MS_SE_MISSING_REQUESTED_TURN_ON_DATE_TC_043 -> {
                payload.setRequestedTurnOnDate(null);          // missing
                payload.setSplitConnectionFeeIndicator(Boolean.TRUE);
            }
            case MS_SE_REQUESTED_TURN_ON_DATE_INVALID_FORMAT_TC_044 -> {
                setInvalidTurnOnDate(payload);
                payload.setSplitConnectionFeeIndicator(Boolean.TRUE);
            }

            // 044.1–044.6: Budget Billing rules
            // PRP customers not eligible for Budget Billing (amount null)
            case MS_SE_BUDGET_BILLING_PRP_NULL_AMOUNT_NOT_ALLOWED_TC_044_1 -> {
                payload.setEstimatedBudgetAmount(null);
                payload.setSplitConnectionFeeIndicator(Boolean.FALSE);
            }
            // PRP customers not eligible for Budget Billing (amount provided)
            case MS_SE_BUDGET_BILLING_PRP_AMOUNT_PROVIDED_NOT_ALLOWED_TC_044_2 -> {
                payload.setPlanCode(GlobalEnums.PlanCode.PRP.getValue());
                payload.setSplitConnectionFeeIndicator(Boolean.TRUE);
            }
            // Non-PRP: missing estimated amount
            case MS_SE_BUDGET_BILLING_NON_PRP_MISSING_AMOUNT_TC_044_3 -> {
//                payload.setPlanCode(GlobalEnums.PlanCode.MVS.getValue()); // any non-PRP
                payload.setPromotionCode("");
                payload.setEstimatedBudgetAmount(null);
                payload.setSplitConnectionFeeIndicator(Boolean.TRUE);
            }
            // Non-PRP: amount too long (max length 5)
            case MS_SE_BUDGET_BILLING_NON_PRP_AMOUNT_TOO_LONG_TC_044_4 -> {
//                payload.setPlanCode(GlobalEnums.PlanCode.MVS.getValue());
                payload.setPromotionCode("");
                payload.setEstimatedBudgetAmount(FakerDataGenerator.getRandomNumericString(7));            // 6 digits
                payload.setSplitConnectionFeeIndicator(Boolean.TRUE);
            }
            // Non-PRP: decimal/invalid integer
            case MS_SE_BUDGET_BILLING_NON_PRP_DECIMAL_AMOUNT_INVALID_TC_044_5 -> {
                //ayload.setPlanCode(GlobalEnums.PlanCode.MVS.getValue());
                payload.setEstimatedBudgetAmount(FakerDataGenerator.generateDigits(2) + "." +FakerDataGenerator.generateDigits(2));
                payload.setSplitConnectionFeeIndicator(Boolean.TRUE);
            }
            // Missing plan code
            case MS_SE_BUDGET_BILLING_PLAN_CODE_MISSING_TC_044_6 -> {
                payload.setPlanCode(null);
                payload.setEstimatedBudgetAmount("100");              // value present; plan missing
                payload.setSplitConnectionFeeIndicator(Boolean.TRUE);
            }

            default -> { /* no-op */ }
        }
    }


    public void setPremisesCodeAndTransactionIdBasedOnType(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel testCondition) {

        switch (testCondition) {
            case MS_GE_RS_INCL_TIER_5_TC_023 ->
                    setParametersFromGetEligiblePlansAndOffersResponse(payload, testCondition);

            default -> {
                setParametersFromGetEligiblePlansAndOffersResponse(payload, testCondition);

            }
        }
    }
    private static Plans findPlanByCode(Collection<Plans> plans, String planCode) {
        return plans.stream()
                .filter(p -> Objects.equals(p.getPlanCode(), planCode))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Plan " + planCode + " not found"));
    }

    private static Plans findPlanByNotCode(Collection<Plans> plans, String planCode) {
        return plans.stream()
                .filter(p -> !Objects.equals(p.getPlanCode(), planCode))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Plan " + planCode + " not found"));
    }

    public void setParametersBasedOnTypeExternal(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel testCondition) {
        setSupportingDefaultParameters(payload);
       Plans plan;
        switch (testCondition) {
            case MS_GE_RS_ACN_LAND_BYPASS_CREDIT_TC_022, MS_GE_RS_INCL_TIER_5_TC_023,
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
                payload.setServiceTransferReward(null);
                payload.setServiceTransferCurrentPricePlan(null);
                payload.setServiceTransferOfferRemainder(null);
                payload.setSplitConnectionFeeIndicator(Boolean.FALSE);
            }
            case MS_RS_CRDS_ENROLLMENT_CREDIT_CHECK_TC_025 -> {
                plan = findPlanByCode(
                        testContext.getGetEligiblePlansAndOffersResponse().getData().getPlans(),
                        GlobalEnums.PlanCode.VML.getValue());
                setParametersFromGetEligiblePlansAndOffersResponse(payload, testCondition);
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.DEPOSIT_REQUIRED.getValue());
                payload.setPlanCode(plan.getPlanCode());
                setTurnOnDate(payload);
                setCustomerRequestedServiceDate(payload);
                payload.setPromotionCode(plan.getPromotion1Code());
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
               //remove?
                setTurnOnDate(payload);
                setCustomerRequestedServiceDate(payload);
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
                //remove?
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
                payload.setCustomerRequestedServiceDate(testContext.getGetEligiblePlansAndOffersResponse().getData().getEarliestPossibleTurnOnDate());
                payload.setRequestedTurnOnDate(testContext.getGetEligiblePlansAndOffersResponse().getData().getEarliestPossibleTurnOnDate());
                payload.setPaymentConfirmationNumber(FakerDataGenerator.getRandomNumericString(7));
            }
            default -> { }
        }
    }
}
