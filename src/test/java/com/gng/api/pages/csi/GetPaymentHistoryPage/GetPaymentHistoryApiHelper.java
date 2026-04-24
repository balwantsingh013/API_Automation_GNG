package com.gng.api.pages.csi.GetPaymentHistoryPage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.GetPaymentHistory.GetPaymentHistoryRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.GetPaymentHistory.GetPaymentHistoryLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class GetPaymentHistoryApiHelper {

    private final TestContext testContext;

    public GetPaymentHistoryApiHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    GetPaymentHistoryRequest preparePayload(GetPaymentHistoryLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);

        String jsonFileName = apiLabel.equals(GetPaymentHistoryLabel.get_payment_history)
                ? GetPaymentHistoryLabel.get_payment_history.toString()
                : GetPaymentHistoryLabel.get_payment_history_mandatory.toString();

        return BasePage.deserializeJsonToPojo(jsonFileName, GetPaymentHistoryRequest.class);
    }

    public void preparePayloadForTestCondition(GetPaymentHistoryRequest payload,
                                               GetPaymentHistoryLabel testCondition) {

        Map<String, Object> accountInfo = null;

        switch (testCondition) {

            // ---------------- NEGATIVE TEST CASES ----------------

            case TC_126__Negative__Missing_Request_ID:
                payload.setRequestID("");
                break;

            case TC_127__Negative__Invalid_Request_ID_Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(40));
                break;

            case TC_128__Negative__Duplicate_Request_ID:
                payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());
                break;

            case TC_129__Negative__Missing_customerCode:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode("");
                break;

            case TC_130__Negative__Invalid_customerCode_Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateDigits(12));
                break;

            case TC_131__Negative__Invalid_customerCode_Format:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateAlphanumeric(6));
                break;

            case TC_132__Negative__Missing_premisesCode:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPremisesCode("");
                break;

            case TC_133__Negative__Invalid_premisesCode_Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(10));
                break;

            case TC_134__Negative__Invalid_premisesCode_Format:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPremisesCode(FakerDataGenerator.generateAlphanumeric(6));
                break;

            case TC_135__Negative__Invalid_Account_Number_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode("9988776");
                Map<String, Object> userInfo = ApplicationContext.get().getDbAction().getUserAccountInfo("9988776");
                payload.setCustomerCode("9988776");
                payload.setPremisesCode(FakerDataGenerator.generateDigits(6));
                break;

            case TC_136__Negative__Missing_Number_of_Months:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setNumberOfMonths(null);
                break;

            case TC_137__Negative__Invalid_Number_of_Months_Format__Not_a_Number:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setNumberOfMonths("ABC");
                break;

            case TC_138__Negative__Invalid_Number_of_Months_Format__Zero_Value:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setNumberOfMonths("0");
                break;

            case TC_139__Negative__Invalid_Number_of_Months_Format__Negative_Number:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setNumberOfMonths("-5");
                break;

            case TC_140__Negative__Invalid_Number_of_Months_Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setNumberOfMonths("999");
                break;


            // ---------------- POSITIVE TEST CASES ----------------

            case TC_141__Positive__Payment_Date_Format:
            case TC_142__Positive__Payment_Amount_Format:
            case TC_143__Positive__Payment_Code_Format:
            case TC_144__Positive__Payment_Description_Format:
                accountInfo = ApplicationContext.get().getDbAction().getAccountWithPaymentHistory();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(10));
                payload.setCustomerCode(accountInfo.get("cust_code").toString());
                payload.setPremisesCode(accountInfo.get("prem_code").toString());
                payload.setNumberOfMonths("24");
                testContext.setCustomerCode(accountInfo.get("cust_code").toString());
                testContext.setPremisesCode(accountInfo.get("prem_code").toString());
                List<Map<String, Object>> dbRows1 = ApplicationContext.get()
                        .getDbAction()
                        .getPaymentHistory(accountInfo.get("cust_code").toString(), accountInfo.get("prem_code").toString());
                break;

            case TC_145__Positive__No_Payment_History_New:
                accountInfo = ApplicationContext.get().getDbAction().getNewAccountNoPaymentHistory();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(10));
                payload.setCustomerCode(accountInfo.get("cust_code").toString());
                payload.setPremisesCode(accountInfo.get("prem_code").toString());
                payload.setNumberOfMonths("12");
                testContext.setCustomerCode(accountInfo.get("cust_code").toString());
                testContext.setPremisesCode(accountInfo.get("prem_code").toString());
                List<Map<String, Object>> dbRows2 = ApplicationContext.get()
                        .getDbAction()
                        .getPaymentHistory(accountInfo.get("cust_code").toString(), accountInfo.get("prem_code").toString());
                break;

            case TC_146__Positive__No_Payment_History_Active_Final_Inactive:
                accountInfo = ApplicationContext.get().getDbAction().getActiveAccountNoPaymentHistory();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(10));
                payload.setCustomerCode(accountInfo.get("cust_code").toString());
                payload.setPremisesCode(accountInfo.get("prem_code").toString());
                payload.setNumberOfMonths("12");
                testContext.setCustomerCode(accountInfo.get("cust_code").toString());
                testContext.setPremisesCode(accountInfo.get("prem_code").toString());
                List<Map<String, Object>> dbRows3 = ApplicationContext.get()
                        .getDbAction()
                        .getPaymentHistory(accountInfo.get("cust_code").toString(), accountInfo.get("prem_code").toString());
                break;

            case TC_147__Positive__Valid_NumberOfMonths_PaymentHistory_Too_Old:
                accountInfo = ApplicationContext.get().getDbAction().getPaymentHistoryTooOld();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(10));
                payload.setCustomerCode(accountInfo.get("cust_code").toString());
                payload.setPremisesCode(accountInfo.get("prem_code").toString());
                payload.setNumberOfMonths("12");
                testContext.setCustomerCode(accountInfo.get("cust_code").toString());
                testContext.setPremisesCode(accountInfo.get("prem_code").toString());
                List<Map<String, Object>> dbRows4 = ApplicationContext.get()
                        .getDbAction()
                        .getPaymentHistory(accountInfo.get("cust_code").toString(), accountInfo.get("prem_code").toString());
                break;

            case TC_149__Positive__Valid_NumberOfMonths_Equals_Requested:
                accountInfo = ApplicationContext.get().getDbAction().getPaymentHistoryEqual();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(10));
                payload.setCustomerCode(accountInfo.get("customer_code").toString());
                payload.setPremisesCode(accountInfo.get("premises_code").toString());
                payload.setNumberOfMonths("12");
                testContext.setCustomerCode(accountInfo.get("customer_code").toString());
                testContext.setPremisesCode(accountInfo.get("premises_code").toString());
                List<Map<String, Object>> dbRows5 = ApplicationContext.get()
                        .getDbAction()
                        .getPaymentHistory(accountInfo.get("customer_code").toString(), accountInfo.get("premises_code").toString());
                break;

            case TC_148__Positive__Valid_NumberOfMonths_Less_Than_Requested:
                accountInfo = ApplicationContext.get().getDbAction().getPaymentHistoryLess();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(10));
                payload.setCustomerCode(accountInfo.get("customer_code").toString());
                payload.setPremisesCode(accountInfo.get("premises_code").toString());
                payload.setNumberOfMonths("12");
                testContext.setCustomerCode(accountInfo.get("customer_code").toString());
                testContext.setPremisesCode(accountInfo.get("premises_code").toString());
                List<Map<String, Object>> dbRows6 = ApplicationContext.get()
                        .getDbAction()
                        .getPaymentHistory(accountInfo.get("customer_code").toString(), accountInfo.get("premises_code").toString());
                break;

            case TC_150__Positive__Valid_NumberOfMonths_Greater_Than_Requested:
                accountInfo = ApplicationContext.get().getDbAction().getPaymentHistoryEqual();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(10));
                payload.setCustomerCode(accountInfo.get("customer_code").toString());
                payload.setPremisesCode(accountInfo.get("premises_code").toString());
                payload.setNumberOfMonths("8");
                testContext.setCustomerCode(accountInfo.get("customer_code").toString());
                testContext.setPremisesCode(accountInfo.get("premises_code").toString());
                List<Map<String, Object>> dbRows7 = ApplicationContext.get()
                        .getDbAction()
                        .getPaymentHistory(accountInfo.get("customer_code").toString(), accountInfo.get("premises_code").toString());
                break;

            case TC_151__Positive__Posted_Reversal_Payment:
                accountInfo = ApplicationContext.get().getDbAction().getPaymentHistoryPostedReversal();
                String custCode1=accountInfo.get("customer_code").toString();
                String premCode1=accountInfo.get("premises_code").toString();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(10));
                payload.setCustomerCode(custCode1);
                payload.setPremisesCode(premCode1);
                payload.setNumberOfMonths("12");
                testContext.setCustomerCode(custCode1);
                testContext.setPremisesCode(premCode1);
                accountInfo = ApplicationContext.get().getDbAction().getPaymentHistoryNotPostedReversal(custCode1, premCode1);
                List<Map<String, Object>> dbRows8 = ApplicationContext.get()
                        .getDbAction()
                        .getPaymentHistory(custCode1, premCode1);
                break;

            case TC_152__Positive__Not_Posted_Reversal_Payment:
                accountInfo = ApplicationContext.get().getDbAction().getPaymentHistoryNoReversal();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(10));
                payload.setCustomerCode(accountInfo.get("cust_code").toString());
                payload.setPremisesCode(accountInfo.get("prem_code").toString());
                payload.setNumberOfMonths("12");
                testContext.setCustomerCode(accountInfo.get("cust_code").toString());
                testContext.setPremisesCode(accountInfo.get("prem_code").toString());
                List<Map<String, Object>> dbRows9 = ApplicationContext.get()
                        .getDbAction()
                        .getPaymentHistory(accountInfo.get("cust_code").toString(), accountInfo.get("prem_code").toString());
                break;

            case TC_153__Positive__Posted_Payments:
                accountInfo = ApplicationContext.get().getDbAction().getPaymentHistoryPostedPayments();
                String custCode2=accountInfo.get("customer_code").toString();
                String premCode2=accountInfo.get("premises_code").toString();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(10));
                payload.setCustomerCode(custCode2);
                payload.setPremisesCode(premCode2);
                payload.setNumberOfMonths("12");
                testContext.setCustomerCode(custCode2);
                testContext.setPremisesCode(premCode2);
                accountInfo = ApplicationContext.get().getDbAction().getPaymentHistoryNonPosted(custCode2, premCode2);
                List<Map<String, Object>> dbRows15 = ApplicationContext.get()
                        .getDbAction()
                        .getPaymentHistory(custCode2, premCode2);
                break;

            case TC_154__Positive__Pending_Payments:
                accountInfo = ApplicationContext.get().getDbAction().getPaymentHistoryPendingPayments();
                String custCode3=accountInfo.get("customer_code").toString();
                String premCode3=accountInfo.get("premises_code").toString();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(10));
                payload.setCustomerCode(custCode3);
                payload.setPremisesCode(premCode3);
                payload.setNumberOfMonths("12");
                testContext.setCustomerCode(custCode3);
                testContext.setPremisesCode(premCode3);
                accountInfo = ApplicationContext.get().getDbAction().getPaymentHistoryPosted2(custCode3, premCode3);
                List<Map<String, Object>> dbRows16 = ApplicationContext.get()
                        .getDbAction()
                        .getPaymentHistory(custCode3, premCode3);
                break;

            case TC_155__Positive__Posted_and_Pending_Payments:
                accountInfo = ApplicationContext.get().getDbAction().getPaymentHistoryPostedAndPendingPayments();
                String custCode4=accountInfo.get("customer_code").toString();
                String premCode4=accountInfo.get("premises_code").toString();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(10));
                payload.setCustomerCode(custCode4);
                payload.setPremisesCode(premCode4);
                payload.setNumberOfMonths("12");
                testContext.setCustomerCode(custCode4);
                testContext.setPremisesCode(premCode4);
                accountInfo = ApplicationContext.get().getDbAction().getPaymentHistoryNotPosted2(custCode4, premCode4);
                List<Map<String, Object>> dbRows17 = ApplicationContext.get()
                        .getDbAction()
                        .getPaymentHistory(custCode4, premCode4);
                break;

            default:
                log.warn("Unhandled test condition: {}", testCondition);
                break;
        }
    }
}
