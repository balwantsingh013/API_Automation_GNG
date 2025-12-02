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



    public void setPrePayFieldsByCondition(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel testCondition) {
        switch (testCondition) {
//            case  -> {
//                payload.setEstimatedBudgetAmount(FakerDataGenerator.generateDigits(2));
//                payload.setCustomerRequestedServiceDate(payload.getRequestedTurnOnDate());
//                payload.setPaymentConfirmationNumber(FakerDataGenerator.getRandomNumericString(6));
//                payload.setBillingPlan(GlobalEnums.BillingPlan.BUDGET.getValue());
//            }
            default -> {  }
        }
    }

    public void setParametersBasedOnTypePositive(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel testCondition) {
        setSupportingDefaultParameters(payload);
        setParametersFromGetEligiblePlansAndOffersResponse(payload, testCondition);

        var geData  = testContext.getGetEligiblePlansAndOffersResponse().getData();

        switch (testCondition) {
//            case GE_MRK_SW_RS_NEW_CC_YES_UC65_TC22 -> {
//                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.SAVE_INCOMPLETE.getValue());
//                payload.setCustomerRequestedServiceDate(geData.getEarliestPossibleTurnOnDate());
//                payload.setRequestedTurnOnDate(geData.getEarliestPossibleTurnOnDate());
//               // payload.setSplitConnectionFeeIndicator(Boolean.FALSE);
//            }
            default -> { }
        }
    }

    public void setSecondRequestParametersBasedOnTypePositive(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel testCondition) {
        setSupportingDefaultParameters(payload);
        setParametersFromGetEligiblePlansAndOffersResponse(payload, testCondition);

        switch (testCondition) {
//            case GE_MRK_SW_RS_NEW_CC_YES_UC65_TC22 -> {
//                payload.setBillingPlan(null);
//                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.SAVE_INCOMPLETE.getValue());
//                payload.setSplitConnectionFeeIndicator(Boolean.FALSE);
//                payload.setAglcServiceOrderNumber(null;
//            }
            default -> {  }
        }
    }

    public void setParametersBasedOnTypePositiveWithNote(SaveEnrollmentRequest payload, String noteText, SaveEnrollmentApiLabel testCondition) {
        setSupportingDefaultParameters(payload);
        setParametersFromGetEligiblePlansAndOffersResponse(payload, testCondition);

        var geData = testContext.getGetEligiblePlansAndOffersResponse().getData();

        switch (testCondition) {
//            case  -> {
//                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.COMPLETE.getValue());
//                payload.setCustomerRequestedServiceDate(payload.getRequestedTurnOnDate());
//                payload.setPaymentConfirmationNumber(FakerDataGenerator.getRandomNumericString(7));
//                payload.setPromotionCode("");
//                payload.setSplitConnectionFeeIndicator(Boolean.FALSE);
//                payload.setNotes(noteText);
//            }
        }
    }

    public void setParametersBasedOnTypeNegative(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel testCondition) {
        setSupportingDefaultParameters(payload);
        setParametersFromGetEligiblePlansAndOffersResponse(payload, testCondition);
        setPrePayFieldsByCondition(payload, testCondition);

        switch (testCondition) {

            default -> {  }
        }
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
                //override to null
                payload.setAglcServiceOrderNumber(null);
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.DEPOSIT_REQUIRED.getValue());
                payload.setAglcServiceOrderNumber(null);
                payload.setSplitConnectionFeeIndicator(Boolean.FALSE);
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
                //payload.setPlanCode(GlobalEnums.PlanCode.TRD.getValue());
                payload.setSplitConnectionFeeIndicator(Boolean.FALSE);
                payload.setBillingPlan(null);
            }
            case  GE_MRK_SW_RS_CRDS_CC_YES_DEPOSIT_BILLED_VALUE110_TC24 -> {
                //loadCustomerData(payload, testCondition);
                setSecondCallParametersFromGetEligiblePlansAndOffersResponse(payload, testCondition);
                //adding aglcAcc#?
                //setParamsFromSearchAccountsResponse(payload, testCondition);
                payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.DEPOSIT_BILLED.getValue());
                payload.setPaymentConfirmationNumber(FakerDataGenerator.getRandomNumericString(7));
                payload.setSplitConnectionFeeIndicator(Boolean.FALSE);

                //testing
                payload.setAglcServiceOrderNumber(null);
                payload.setAglcAccountNumber(aglcAccountNumber);
            }

            default -> { }
        }
    }
    public void setParamsFromSearchAccountsResponse(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel testCondition){
        String geTransactionIdRaw = String.valueOf(
                testContext.getGetEligiblePlansAndOffersResponse()
                        .getData()
                        .getTransactionID()
        );

        int geTransactionId = parseIntOrAssert(geTransactionIdRaw, "SaveEnrollmentResponse.data.transactionID");

        var matchingAccount = testContext.getSearchAccountsResponse()
                .getData()
                .getAccounts()
                .stream()
                .filter(acct -> {
                    String acctTxRaw = String.valueOf(acct.getTransactionID());
                    int acctTxId = parseIntOrAssert(acctTxRaw,  "SearchAccountsResponse.data.accounts[].transactionID (customerCode=" + acct.getCustomerCode() + ")");
                    return acctTxId == geTransactionId;
                })
                .findFirst()
                .orElse(null);

        Assert.assertNotNull(matchingAccount,"No SearchAccounts account found for transactionId: " + geTransactionId
        );

        switch (testCondition){
            case GE_MRK_SW_RS_NEW_CC_YES_UC65_TC22, GE_MRK_SW_RS_CRDS_CC_YES_DEPOSIT_BILLED_VALUE110_TC24 -> {
                payload.setCustomerCode(matchingAccount.getCustomerCode());
                payload.setPremisesCode(matchingAccount.getPremisesCode());
                payload.setAglcAccountNumber(matchingAccount.getAglcAccountNumber());
                payload.setTransactionID(geTransactionId);
            }
        }
    }

    private int parseIntOrAssert(String value, String fieldName) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            throw new AssertionError("Unable to parse int from " + fieldName + " value: [" + value + "]", ex);
        }
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
            // Either leave them unset (null) or explicitly set to ""
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
            // Either leave them unset (null) or explicitly set to ""
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
        // Treat "null" and empty as no date
        if (value.isEmpty() || value.equalsIgnoreCase("null")) {
            return null;
        }
        // Validate format
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
            case GE_MRK_SW_RS_NEW_CC_YES_UC65_TC22
                    -> findPlanByCode(plans, GlobalEnums.PlanCode.MVS.getValue());
            case GE_MRK_SW_RS_CRDS_CC_YES_DEPOSIT_BILLED_VALUE110_TC24
                    -> findPlanByCode(plans, GlobalEnums.PlanCode.VML.getValue());
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
