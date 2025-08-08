package com.gng.api.pages.turnOn.ServiceOrdersPages.SaveEnrollmentPage;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gng.api.constants.GlobalEnums;
import com.gng.api.constants.TestConstant;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.AccountsPojo.SearchAccounts.SearchAccountsRequest;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.request.GetEligiblePlansAndOffersRequest;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.response.GetEligiblePlansAndOffersResponse;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.response.Plans;
import com.gng.api.pojo.ServiceOrdersPojo.SaveEnrollment.SaveEnrollmentRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.turnOn.ServiceOrdersSteps.SaveEnrollment.SaveEnrollmentApiLabel;
import com.gng.api.util.ExcelReader;
import com.gng.api.util.FakerDataGenerator;
import io.cucumber.datatable.DataTable;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;

import java.io.IOException;
import java.util.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import static com.gng.api.constants.GlobalEnums.CreditCheckOption.YES;
import static com.gng.api.constants.GlobalEnums.EnrollMentStatus.*;
import static com.gng.api.constants.GlobalEnums.EnrollmentSource.PHONECALL;
import static com.gng.api.constants.GlobalEnums.TransactionType.TURN_ON;
import static com.gng.api.constants.TestConstant.CUSTOMER_DATA;
import static com.gng.api.constants.TestConstant.CUSTOMER_SHEET_NAME;
import static com.gng.api.steps.AesEncryption.AesEncryptionSteps.encryptData;

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
            case EMPTY_REQUEST_ID:
                payload.setRequestID("");
                break;
            case DUPLICATE_REQUEST_ID:
                payload.setRequestID("123");
                break;

            case LONG_REQUEST_ID:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(200));
                break;

            default:
                payload.setRequestID(FakerDataGenerator.generateString(10));
        }
    }

    public void setCustomerCodeBasedOnType(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel customerCODE) {
        switch (customerCODE) {
            case DUPLICATE_CUSTOMER_CODE:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setCustomerCode(12356);
                break;
            case MAX_LENGTH_CUSTOMER_CODE:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                int customerCode = FakerDataGenerator.generateNumber(1, 11);
                payload.setCustomerCode(customerCode);
                break;
            case UNICODE_CHARS_CUSTOMER_CODE:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setCustomerCode(FakerDataGenerator.generateNumber(0, 15));
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

    public void setSaveEnrollmentRequestForPreviouslySavedEnrollment(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel testCondition, String planCode, String promotionCode){
        setMarketerReferenceData(payload, testContext.getMarketerReferenceData());
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setPlanCode(planCode);
        payload.setPromotionCode(promotionCode);
        setValuesBasedOnGetEligiblePlansAndOffersResponse(payload);
        switch(testCondition) {
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_424:
                payload.setEnrollmentStatus(COMPLETE.getValue());
                payload.setAglcAccountNumber(FakerDataGenerator.generateDigits(7));
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_426:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_426_1:
                payload.setEnrollmentStatus(DEPOSIT_PAID.getValue());
                payload.setPaymentConfirmationNumber(FakerDataGenerator.generateDigits(6));
                payload.setAglcAccountNumber(FakerDataGenerator.generateDigits(7));
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_427:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_428:
                setEnrollmentStatusPCAndPaymentConfirmationNumber(payload);
                    break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_434:
                payload.setEnrollmentStatus(SAVE_INCOMPLETE.getValue());
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_436:
                payload.setEnrollmentStatus(DEPOSIT_REQUIRED.getValue());
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_440:
                payload.setEnrollmentStatus(PREPAY_REQUIRED.getValue());
                payload.setBillingPlan("");
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
        switch(testCondition) {
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_NOTES_CE_TC_498:
                payload.setEnrollmentStatus(COMPLETE.getValue());
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_NOTES_PC_TC_499:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_NOTES_PC_TC_500A:
                setEnrollmentStatusPCAndPaymentConfirmationNumber(payload);
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_NOTES_PC_TC_500B:
                setEnrollmentStatusPCAndPaymentConfirmationNumber(payload);
                payload.setNotes(FakerDataGenerator.generateString(10));
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_424:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_434:
                payload.setEnrollmentStatus(SAVE_INCOMPLETE.getValue());
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_CE_TC_423:
                payload.setNotes(FakerDataGenerator.generateString(10));
                payload.setEnrollmentStatus(COMPLETE.getValue());
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_DP_TC_425:
                payload.setEnrollmentStatus(DEPOSIT_PAID.getValue());
                payload.setPaymentConfirmationNumber(FakerDataGenerator.generateDigits(6));
                payload.setAglcAccountNumber(FakerDataGenerator.generateDigits(7));
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_426:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_426_1:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_RD_TC_446:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_436:
                payload.setEnrollmentStatus(REFUSED_DEPOSIT.getValue());
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_DR_TC_435:
                payload.setEnrollmentStatus(DEPOSIT_REQUIRED.getValue());
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PR_TC_438:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PR_TC_439:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_427:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_428:
                payload.setEnrollmentStatus(PREPAY_REQUIRED.getValue());
                payload.setBillingPlan("");
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_RP_TC_442:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_RP_TC_443:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_440:
                payload.setEnrollmentStatus(REFUSED_PREPAY.getValue());
                payload.setBillingPlan("");
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_BD_TC_452:
                payload.setEnrollmentStatus(BILL_DEPOSIT.getValue());
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_SF_TC_454:
                payload.setEnrollmentStatus(SAVE_FOR_FALL_SSP.getValue());
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_SI_TC_433:
                payload.setEnrollmentStatus(SAVE_INCOMPLETE.getValue());
                payload.setNotes(FakerDataGenerator.generateString(10));
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PC_TC_431:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PC_TC_432:
                setEnrollmentStatusPCAndPaymentConfirmationNumber(payload);
                payload.setAglcAccountNumber(FakerDataGenerator.generateDigits(7));
                break;

        }
    }

    public void setEnrollmentStatusPCAndPaymentConfirmationNumber(SaveEnrollmentRequest payload){
        payload.setEnrollmentStatus(PAYMENT_COMPLETE.getValue());
        payload.setPaymentConfirmationNumber(FakerDataGenerator.generateDigits(6));
        payload.setBillingPlan("");
    }

    public void setPremisesCodeBasedOnType(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel premisesCode) {
        switch (premisesCode) {
            case EMPTY_PREMISES_CODE:
                payload.setPremisesCode("");
                break;
            case DUPLICATE_PREMISES_CODE:
                payload.setPremisesCode("25 CENTS FOR 12 MONTH");
                break;
            case SPECIAL_CHARS_PREMISES_CODE:
                payload.setPremisesCode(FakerDataGenerator.generateAlphanumericWithSpecialChars(10));
                break;
            case MAX_LENGTH_PREMISES_CODE:
                payload.setPremisesCode(FakerDataGenerator.generateAlphanumeric(36));
                break;
            case MIN_LENGTH_PREMISES_CODE:
                payload.setPremisesCode(FakerDataGenerator.generateString(2));
                break;
            default:
                payload.setPremisesCode(FakerDataGenerator.generateString(10));
        }
    }

    public void setTransactionIDBasedOnType(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel transactionID) {
        switch (transactionID) {
            case MIN_LENGTH_TRANSACTION_ID:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setTransactionID(FakerDataGenerator.generateNumber(0, 0));
                break;
            case WHITESPACE_BETWEEN_TRANSACTION_ID:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setTransactionID(FakerDataGenerator.generateNumber(4, 9));
                break;
            default:
                payload.setTransactionID(FakerDataGenerator.generateNumber(0, 10));
        }
    }

    public void setTransactionTypeBasedOnType(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel transactionType) {
        switch (transactionType) {
            case EMPTY_TRANSACTION_TYPE:
                payload.setTransactionType("");
                break;
            case NUMERIC_TRANSACTION_TYPE:
                payload.setTransactionType(FakerDataGenerator.generateDigits(10));
                break;
            case UPPERCASE_TRANSACTION_TYPE:
                payload.setTransactionType(FakerDataGenerator.generateUpperCaseString(36));
                break;
            case LOWERCASE_TRANSACTION_TYPE:
                payload.setTransactionType(FakerDataGenerator.generateLowerCaseString(4));
                break;

            case ALPHANUMERIC_TRANSACTION_TYPE:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setTransactionType(FakerDataGenerator.generateAlphanumeric(2));
                break;
            default:
                payload.setTransactionType(FakerDataGenerator.generateUpperCaseString(4));
        }


    }

    public void setPlanCodeBasedOnType(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel planCode) {
        switch (planCode) {
            case NUMERIC_PLAN_CODE:
                payload.setPlanCode(FakerDataGenerator.generateDigits(7));
                break;
            case SPECIAL_CHARS_PLAN_CODE:
                payload.setPlanCode(FakerDataGenerator.generateAlphanumericWithSpecialChars(7));
                break;
            case EMPTY_PLAN_CODE:
                payload.setPlanCode("");
                break;
            case UPPERCASE_PLAN_CODE:
                payload.setPlanCode(FakerDataGenerator.generateUpperCaseString(4));
                break;
            case ALPHANUMERIC_PLAN_CODE:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setPlanCode(FakerDataGenerator.generateAlphanumeric(2));
                break;
            default:
                payload.setPlanCode(FakerDataGenerator.generateUpperCaseString(3));
        }
    }

    public void setLoginIDBasedOnType(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel loginID) {
        switch (loginID) {
            case MIN_LENGTH_LOGIN_ID:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(FakerDataGenerator.getRandomNumericString(1));
                break;
            case SPECIAL_CHARS_LOGIN_ID:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(FakerDataGenerator.generateAlphanumericWithSpecialChars(7));
                break;
            case EMPTY_LOGIN_ID:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("");
                break;
            case UPPERCASE_LOGIN_ID:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(FakerDataGenerator.generateUpperCaseString(4));
                break;
            case ALPHANUMERIC_LOGIN_ID:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(FakerDataGenerator.generateAlphanumeric(2));
                break;
            case MAX_LENGTH_LOGIN_ID:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(FakerDataGenerator.getRandomNumericString(9));
                break;
            default:
                payload.setLoginID(FakerDataGenerator.generateLowerCaseString(5));
        }
    }

    public void setEnrollmentStatusBasedOnType(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel enrollmentStatus) {
        switch (enrollmentStatus) {
            case MIN_LENGTH_ENROLLMENT_STATUS:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setEnrollmentStatus(FakerDataGenerator.getRandomNumericString(1));
                break;
            case SPECIAL_CHARS_ENROLLMENT_STATUS:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setEnrollmentStatus(FakerDataGenerator.generateAlphanumericWithSpecialChars(7));
                break;
            case EMPTY_ENROLLMENT_STATUS:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setEnrollmentStatus("");
                break;
            case LOWERCASE_ENROLLMENT_STATUS:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setEnrollmentStatus(FakerDataGenerator.generateUpperCaseString(4));
                break;
            case MAX_LENGTH_ENROLLMENT_STATUS:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setEnrollmentStatus(FakerDataGenerator.getRandomNumericString(3));
                break;
            case WHITESPACE_CONTAINS_ENROLLMENT_STATUS:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setEnrollmentStatus("S I");
                break;
            default:
                payload.setEnrollmentStatus(FakerDataGenerator.generateUpperCaseString(2));
        }
    }

    public void setMarketerReferenceData(SaveEnrollmentRequest payload, long marketerReferenceData){
        payload.setMarketerReferenceData(marketerReferenceData);
    }

    public void setBillingPlanBasedOnType(SaveEnrollmentRequest payload, SaveEnrollmentApiLabel billingPlan) {
        switch (billingPlan) {
            case MAX_LENGTH_BILLING_PLAN:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setBillingPlan(FakerDataGenerator.getRandomNumericString(4));
                break;
            case SPECIAL_CHARS_BILLING_PLAN:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setBillingPlan(FakerDataGenerator.generateAlphanumericWithSpecialChars(7));
                break;
            case EMPTY_BILLING_PLAN:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setBillingPlan("");
                break;
            case LOWERCASE_BILLING_PLAN:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setBillingPlan(FakerDataGenerator.generateUpperCaseString(4));
                break;
            default:
                payload.setEnrollmentStatus(FakerDataGenerator.generateUpperCaseString(1));
        }
    }

    public void setPrePayRequestParams(SaveEnrollmentRequest payload, GlobalEnums.PlanCode planCode, SaveEnrollmentApiLabel testCondition){

        Map<String, String> customerData = loadRowFromExcelToCustomerData(CUSTOMER_DATA, CUSTOMER_SHEET_NAME, testCondition);

        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setEnrollmentStatus(GlobalEnums.EnrollMentStatus.PREPAY_REQUIRED.getValue());
        payload.setTransactionType(testContext.getGetEligiblePlansAndOffersResponse().getData().getTransactionType());
        payload.setBillingPlan("");
        payload.setTransactionID(Integer.parseInt(testContext.getGetEligiblePlansAndOffersResponse().getData().getTransactionID()));
        payload.setCustomerCode(Integer.parseInt(testContext.getGetEligiblePlansAndOffersResponse().getData().getCustomerCode()));
        payload.setPremisesCode(testContext.getGetEligiblePlansAndOffersResponse().getData().getPremisesCode());

        // Select a plan from the EligiblePlansAndOffers response where the plan is pre-pay
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
        payload.setMarketerReferenceNumber(String.valueOf(testContext.getMarketerReferenceData()));
    }

    public void getCustomerDetails(SaveEnrollmentRequest payload, Map<String, String> data ){
        payload.setAglcAccountNumber(FakerDataGenerator.generateDigits(8));

    }
    public static <E extends Enum<E>> Map<String, String> loadRowFromExcelToCustomerData(
            String excelPath,
            String sheetName,
            E testLabel) {

        try {
            ExcelReader reader = new ExcelReader(excelPath);
            List<Map<String, String>> sheetData = reader.getSheetData(sheetName);

            // Find the first row where the "testConditions" column matches the enum name
            return sheetData.stream()
                    .filter(row -> testLabel.name().equalsIgnoreCase(row.get("testCondition")))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException(
                            "No matching testConditions found for: " + testLabel.name()));

        } catch (IOException e) {
            throw new RuntimeException("Failed to load data from Excel", e);
        }
    }

}
