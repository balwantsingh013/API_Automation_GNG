package com.gng.api.pages.marketerSwitch.ServiceOrdersPages.SaveEnrollmentPage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.request.GetEligiblePlansAndOffersRequest;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.response.Plans;
import com.gng.api.pojo.ServiceOrdersPojo.SaveEnrollment.SaveEnrollmentRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.marketerSwitch.ServiceOrdersSteps.GetEligiblePlansAndOffers.GetEligiblePlansAndOffersApiLabel;
import com.gng.api.steps.marketerSwitch.ServiceOrdersSteps.SaveEnrollment.SaveEnrollmentApiLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import static com.gng.api.pages.poc.CreateAccountNotePage.CreateAccountNoteHelper.assertRowsMatchTokenCount;

@Slf4j
public class SaveEnrollmentHelper {

    private final TestContext testContext;

    public SaveEnrollmentHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    private String aglcAccountNumber = "";

    SaveEnrollmentRequest preparePayload(SaveEnrollmentApiLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        String jsonFileName = apiLabel.equals(SaveEnrollmentApiLabel.save_enrollment)
                ? SaveEnrollmentApiLabel.save_enrollment.toString()
                : null;
        return BasePage.deserializeJsonToPojo(jsonFileName, SaveEnrollmentRequest.class);
    }

    public void setSupportingDefaultParameters(SaveEnrollmentRequest payload) {
        payload.setRequestID(FakerDataGenerator.generateString(12));
        payload.setTransactionType(GlobalEnums.TransactionType.MKSW.getValue());
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

    public void setParametersBasedOnTypeExternal(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel testCondition) {
        setSupportingDefaultParameters(payload);
        Plans plan;

        switch (testCondition) {
            case  GE_MRK_SW_RS_NEW_CC_YES_UC65_TC22 -> {
                setParametersFromGetEligiblePlansAndOffersResponse(payload, testCondition);
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.SAVE_INCOMPLETE.getValue());
                payload.setAglcServiceOrderNumber(null);
                payload.setSplitConnectionFeeIndicator(Boolean.FALSE);
            }
            case  GE_MRK_SW_RS_CRDS_CC_YES_DEPOSIT_BILLED_VALUE110_TC24 -> {
                setParametersFromGetEligiblePlansAndOffersResponse(payload, testCondition);
                payload.setAglcServiceOrderNumber(null);
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.DEPOSIT_REQUIRED.getValue());
                payload.setAglcServiceOrderNumber(null);
                payload.setSplitConnectionFeeIndicator(Boolean.FALSE);
                aglcAccountNumber = payload.getAglcAccountNumber();
            }
            case GE_MRK_SW_CM_CRDS_CC_YES_PROMO_DEPOSIT_REQUIRED_VALUE210_CREDIT0_49_TC32,
                 GE_MRK_SW_CM_CRDS_CC_YES_DEPOSIT_REQUIRED_BUSINESS_NAME_POPULATED_UC42_TC33 -> {
                setParametersFromGetEligiblePlansAndOffersResponse(payload, testCondition);
                payload.setAglcServiceOrderNumber(null);
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.DEPOSIT_REQUIRED.getValue());
                payload.setAglcServiceOrderNumber(null);
                payload.setSplitConnectionFeeIndicator(Boolean.FALSE);
                payload.setCurrentMarketerCode(null);
                aglcAccountNumber = payload.getAglcAccountNumber();
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
            case  GE_MRK_SW_RS_NEW_CC_YES_UC65_TC22 -> {
                setSecondCallParametersFromGetEligiblePlansAndOffersResponse(payload, testCondition);
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.REFUSED_PREPAY.getValue());
                payload.setSplitConnectionFeeIndicator(Boolean.FALSE);
                payload.setBillingPlan(null);
            }
            case  GE_MRK_SW_RS_CRDS_CC_YES_DEPOSIT_BILLED_VALUE110_TC24 -> {
                setSecondCallParametersFromGetEligiblePlansAndOffersResponse(payload, testCondition);
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.DEPOSIT_BILLED.getValue());
                payload.setPaymentConfirmationNumber(FakerDataGenerator.getRandomNumericString(7));
                payload.setSplitConnectionFeeIndicator(Boolean.FALSE);

                payload.setAglcServiceOrderNumber(null);
                payload.setAglcAccountNumber(aglcAccountNumber);
            }

            default -> { }
        }
    }

    public void setParametersFromGetEligiblePlansAndOffersResponse(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel testCondition) {
        var data  = testContext.getGetEligiblePlansAndOffersResponse().getData();
        var plans = data.getPlans();

        payload.setCustomerCode(data.getCustomerCode());
        payload.setPremisesCode(data.getPremisesCode());
        payload.setTransactionID(data.getTransactionID());
        Plans plan = choosePlanForCondition(testCondition, plans);
        payload.setPlanCode(plan.getPlanCode());
        String promo = Optional.ofNullable(plan.getPromotion1Code()).orElse("");
        payload.setPromotionCode(promo);

        String etod = normalizeTurnOnDate(data.getEarliestPossibleTurnOnDate());

        if (etod != null) {
            payload.setRequestedTurnOnDate(etod);
            payload.setCustomerRequestedServiceDate(etod);
        } else {
            payload.setRequestedTurnOnDate("");
            payload.setCustomerRequestedServiceDate("");
        }
        if (data.getAglcAccountNumber() != null && !data.getAglcAccountNumber().isEmpty()) {
            payload.setAglcAccountNumber(data.getAglcAccountNumber());
            payload.setAglcServiceOrderNumber(FakerDataGenerator.getRandomNumericString(9));
        } else {
            payload.setAglcAccountNumber("");
            payload.setAglcServiceOrderNumber("");
        }
    }

    public void setSecondCallParametersFromGetEligiblePlansAndOffersResponse(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel testCondition) {
        var data  = testContext.getGetEligiblePlansAndOffersResponse().getData();
        var plans = data.getPlans();

        payload.setCustomerCode(data.getCustomerCode());
        payload.setPremisesCode(data.getPremisesCode());
        payload.setTransactionID(data.getTransactionID());
        Plans plan = chooseSecondCallPlanForCondition(testCondition, plans);
        payload.setPlanCode(plan.getPlanCode());
        String promo = Optional.ofNullable(plan.getPromotion1Code()).orElse("");
        payload.setPromotionCode(promo);

        String etod = normalizeTurnOnDate(data.getEarliestPossibleTurnOnDate());

        if (etod != null) {
            payload.setRequestedTurnOnDate(etod);
            payload.setCustomerRequestedServiceDate(etod);
        } else {
            payload.setRequestedTurnOnDate("");
            payload.setCustomerRequestedServiceDate("");
        }
        if (data.getAglcAccountNumber() != null && !data.getAglcAccountNumber().isEmpty()) {
            payload.setAglcAccountNumber(data.getAglcAccountNumber());
            payload.setAglcServiceOrderNumber(FakerDataGenerator.getRandomNumericString(9));
        } else {
            payload.setAglcAccountNumber("");
            payload.setAglcServiceOrderNumber("");
        }
    }
    private String normalizeTurnOnDate(String rawEtod) {
        if (rawEtod == null) {
            return null;
        }

        String value = rawEtod.trim();
        DateTimeFormatter ETOD_FORMATTER = DateTimeFormatter.BASIC_ISO_DATE;
        if (value.isEmpty() || value.equalsIgnoreCase("null")) {
            return null;
        }
        try {
            LocalDate.parse(value, ETOD_FORMATTER);
        } catch (DateTimeParseException ex) {
            throw new AssertionError("Unexpected earliestPossibleTurnOnDate format: '" + rawEtod + "'", ex);
        }

        return value;
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

    private Plans choosePlanForCondition(SaveEnrollmentApiLabel testCondition, List<Plans> plans) {
        return switch (testCondition) {
            case GE_MRK_SW_RS_NEW_CC_YES_UC65_TC22
                    -> findPlanByCode(plans, GlobalEnums.PlanCode.MVS.getValue());
            case GE_MRK_SW_RS_CRDS_CC_YES_DEPOSIT_BILLED_VALUE110_TC24
                    -> findPlanByCode(plans, GlobalEnums.PlanCode.VML.getValue());
            case GE_MRK_SW_CM_CRDS_CC_YES_PROMO_DEPOSIT_REQUIRED_VALUE210_CREDIT0_49_TC32
                    -> findPlanByCode(plans, GlobalEnums.PlanCode.CVS.getValue());
            case GE_MRK_SW_CM_CRDS_CC_YES_DEPOSIT_REQUIRED_BUSINESS_NAME_POPULATED_UC42_TC33
                    -> findPlanByCode(plans, GlobalEnums.PlanCode.CMI.getValue());
            default -> plans.getFirst();
        };
    }

    private Plans chooseSecondCallPlanForCondition(SaveEnrollmentApiLabel testCondition, List<Plans> plans) {
        return switch (testCondition) {
            case GE_MRK_SW_RS_NEW_CC_YES_UC65_TC22
                    -> findPlanByCode(plans, GlobalEnums.PlanCode.PGB.getValue());
            case GE_MRK_SW_RS_CRDS_CC_YES_DEPOSIT_BILLED_VALUE110_TC24
                    -> findPlanByCode(plans, GlobalEnums.PlanCode.VML.getValue());
            default -> plans.getFirst();
        };
    }

}
