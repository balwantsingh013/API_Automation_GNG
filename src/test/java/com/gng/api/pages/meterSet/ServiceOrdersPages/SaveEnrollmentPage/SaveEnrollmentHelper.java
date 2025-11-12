package com.gng.api.pages.meterSet.ServiceOrdersPages.SaveEnrollmentPage;




import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.response.Plans;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.pojo.ServiceOrdersPojo.SaveEnrollment.SaveEnrollmentRequest;
import com.gng.api.pojo.ServiceOrdersPojo.SaveEnrollment.SaveEnrollmentResponse;
import com.gng.api.steps.meterSet.ServiceOrdersSteps.SaveEnrollment.SaveEnrollmentApiSteps;
import com.gng.api.steps.meterSet.ServiceOrdersSteps.SaveEnrollment.SaveEnrollmentApiLabel;

import com.gng.api.util.FakerDataGenerator;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpPost;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


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

    public void setCustomerRequestedServiceDate(SaveEnrollmentRequest payload){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate customerRequestedServiceDate = LocalDate.now();
        String customerRequestedServiceDateString = customerRequestedServiceDate.plusDays(1).format(formatter);
        payload.setCustomerRequestedServiceDate(customerRequestedServiceDateString);
    }
    public void setParametersFromGetEligiblePlansAndOffersResponse(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel testCondition){
        var data = testContext.getGetEligiblePlansAndOffersResponse().getData();
        payload.setCustomerCode(data.getCustomerCode());
        payload.setPremisesCode(data.getPremisesCode());
        payload.setTransactionID(data.getTransactionID());
        payload.setPlanCode(data.getPlans().getFirst().getPlanCode());

        switch (testCondition){
            case MS_GE_RS_ACN_LAND_BYPASS_CREDIT_TC_022,
                 MS_GE_RS_INCL_TIER_5_TC_023, MS_RS_MULTIPLE_PREM_TC_024,
                 MS_CM_CRDS_EN_CREDIT_CHECK_YES_COMM_DEP_PROSP_TC_033,
                 MS_CM_CRDS_EN_CREDIT_CHECK_YES_BUSINESS_NAME_TC_034-> payload.setRequestedTurnOnDate(data.getEarliestPossibleTurnOnDate());
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
