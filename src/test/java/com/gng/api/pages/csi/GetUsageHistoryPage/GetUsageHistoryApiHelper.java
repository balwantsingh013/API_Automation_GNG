package com.gng.api.pages.csi.GetUsageHistoryPage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.GetUsageHistory.GetUsageHistoryRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.GetUsageHistory.GetUsageHistoryLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class GetUsageHistoryApiHelper {

    private final TestContext testContext;

    public GetUsageHistoryApiHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    public GetUsageHistoryRequest preparePayload(GetUsageHistoryLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);

        // Base JSON template for GetUsageHistory
        String jsonFileName = GetUsageHistoryLabel.get_usage_history.toString();

        return BasePage.deserializeJsonToPojo(jsonFileName, GetUsageHistoryRequest.class);
    }

    public void preparePayloadForTestCondition(GetUsageHistoryRequest payload,
                                               GetUsageHistoryLabel testCondition) {

        Map<String, Object> accountInfo;
        List<Map<String, Object>> accountsInfo = null;


        switch (testCondition) {

            // ---------------------------------------------------------
            // NEGATIVE TEST CASES
            // ---------------------------------------------------------

            case TC_01__Negative__Missing_Request_ID:
                payload.setRequestID("");
                break;

            case TC_02__Negative__Invalid_Request_ID__Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(33));
                break;

            case TC_03__Negative__Duplicate_Request_ID:
                payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());
                break;

            case TC_04__Negative__Missing_customerCode:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode("");
                break;

            case TC_05__Negative__Invalid_customerCode__Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateDigits(10));
                break;

            case TC_06__Negative__Invalid_customerCode__Format:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateAlphanumeric(6));
                break;

            case TC_07__Negative__Missing_premisesCode:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPremisesCode("");
                break;

            case TC_08__Negative__Invalid_premisesCode__Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(8));
                break;

            case TC_09__Negative__Invalid_premisesCode__Format:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPremisesCode(FakerDataGenerator.generateAlphanumeric(6));
                break;

            case TC_10__Negative__Invalid_Account_Number:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode("9988776");
                Map<String, Object> userInfo = ApplicationContext.get().getDbAction().getUserAccountInfo("9988776");
                payload.setCustomerCode("9988776");
                payload.setPremisesCode(FakerDataGenerator.generateDigits(6));
                break;

            case TC_11__Negative__Missing_Number_of_Months:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setNumberOfMonths(null);
                break;

            case TC_12__Negative__Invalid_Number_of_Months__Format__Not_a_Number:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setNumberOfMonths("ABC");
                break;

            case TC_13__Negative__Invalid_Number_of_Months__Format__Zero_Value:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setNumberOfMonths("0");
                break;

            case TC_14__Negative__Invalid_Number_of_Months__Format__Negative_Number:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setNumberOfMonths("-5");
                break;

            case TC_15__Negative__Invalid_Number_of_Months__Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setNumberOfMonths("999");
                break;

            // ---------------------------------------------------------
            // POSITIVE TEST CASES (Usage History Format Validations)
            // ---------------------------------------------------------
            case TC_16__Positive__Usage_History_Service_Number_Format:
                accountInfo = ApplicationContext.get().getDbAction().getValidUsageHistoryAccount();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setNumberOfMonths("12");
                testContext.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                testContext.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                accountsInfo= ApplicationContext.get().getDbAction().getServiceNumber(testContext.getCustomerCode(), testContext.getPremisesCode());
                break;

            case TC_17__Positive__Usage_History_Bill_Date_Format:
                accountInfo = ApplicationContext.get().getDbAction().getValidUsageHistoryAccount();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setNumberOfMonths("12");
                testContext.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                testContext.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                accountsInfo= ApplicationContext.get().getDbAction().getBillDate(testContext.getCustomerCode(), testContext.getPremisesCode());
                break;

            case TC_18__Positive__Usage_History_Usage_From_Date_Format:
                accountInfo = ApplicationContext.get().getDbAction().getValidUsageHistoryAccount();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setNumberOfMonths("12");
                testContext.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                testContext.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                accountsInfo= ApplicationContext.get().getDbAction().getFromDate(testContext.getCustomerCode(), testContext.getPremisesCode());
                break;

            case TC_19__Positive__Usage_History_Usage_To_Date_Format:
                accountInfo = ApplicationContext.get().getDbAction().getValidUsageHistoryAccount();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setNumberOfMonths("12");
                testContext.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                testContext.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                accountsInfo= ApplicationContext.get().getDbAction().getToDate(testContext.getCustomerCode(), testContext.getPremisesCode());
                break;

            case TC_20__Positive__Usage_History_Average_Daily_Actual_Consumption_Format:
                accountInfo = ApplicationContext.get().getDbAction().getValidUsageHistoryAccount();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setNumberOfMonths("12");
                testContext.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                testContext.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                accountsInfo= ApplicationContext.get().getDbAction().getActualConsumption(testContext.getCustomerCode(), testContext.getPremisesCode());
                break;

            case TC_21__Positive__Usage_History_Average_Daily_Billed_Consumption_Format:
                accountInfo = ApplicationContext.get().getDbAction().getValidUsageHistoryAccount();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setNumberOfMonths("12");
                testContext.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                testContext.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                accountsInfo= ApplicationContext.get().getDbAction().getAverageDailyBilledConsumption(testContext.getCustomerCode(), testContext.getPremisesCode());
                break;


            case TC_22__Positive__Usage_History_Total_Billed_Consumption_Format:
                accountInfo = ApplicationContext.get().getDbAction().getValidUsageHistoryAccount();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setNumberOfMonths("12");
                testContext.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                testContext.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                accountsInfo= ApplicationContext.get().getDbAction().getTotalBilledConsumption(testContext.getCustomerCode(), testContext.getPremisesCode());
                break;

            case TC_23__Positive__Usage_History_Days_Of_Service_Format:
                accountInfo = ApplicationContext.get().getDbAction().getValidUsageHistoryAccount();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setNumberOfMonths("12");
                testContext.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                testContext.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                accountsInfo= ApplicationContext.get().getDbAction().getDaysOfService(testContext.getCustomerCode(), testContext.getPremisesCode());
                break;

            case TC_24__Positive__Usage_History_Reading_Format:
                accountInfo = ApplicationContext.get().getDbAction().getValidUsageHistoryAccount();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setNumberOfMonths("12");
                testContext.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                testContext.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                accountsInfo= ApplicationContext.get().getDbAction().getReading(testContext.getCustomerCode(), testContext.getPremisesCode());
                break;

            case TC_28__Positive__Usage_History_Read_Date_Format:
                accountInfo = ApplicationContext.get().getDbAction().getValidUsageHistoryAccount();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setNumberOfMonths("12");
                testContext.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                testContext.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                accountsInfo= ApplicationContext.get().getDbAction().getReadDate(testContext.getCustomerCode(), testContext.getPremisesCode());
                break;

            case TC_29__Positive__Usage_History_Average_Temperature_Format:
                accountInfo = ApplicationContext.get().getDbAction().getValidUsageHistoryAccount();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setNumberOfMonths("12");
                testContext.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                testContext.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                accountsInfo= ApplicationContext.get().getDbAction().getAverageTemperatureFormat(testContext.getCustomerCode(), testContext.getPremisesCode());
                break;

            case TC_30__Positive__Usage_History_Heating_Degree_Days_Format:
                accountInfo = ApplicationContext.get().getDbAction().getValidUsageHistoryAccount();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setNumberOfMonths("12");
                testContext.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                testContext.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                accountsInfo= ApplicationContext.get().getDbAction().getHeatingDegreeDays(testContext.getCustomerCode(), testContext.getPremisesCode());
                break;

            case TC_31__Positive__Usage_History_Bill_History_Transaction_Format:
                accountInfo = ApplicationContext.get().getDbAction().getValidUsageHistoryAccount();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setNumberOfMonths("12");
                testContext.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                testContext.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                accountsInfo= ApplicationContext.get().getDbAction().getBillHistoryTransaction(testContext.getCustomerCode(), testContext.getPremisesCode());
                break;

            case TC_32__Positive__No_Usage_History_New:
                accountInfo = ApplicationContext.get().getDbAction().getNewAccountOnly();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setNumberOfMonths("12");
                break;


            case TC_38__Positive__Usage_History_Equals_Requested_Months:
                accountInfo = ApplicationContext.get().getDbAction().getUsageHitstoryEqualToMonthsRequested();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("CUSTOMER_CODE").toString());
                payload.setPremisesCode(accountInfo.get("PREMISES_CODE").toString());
                payload.setNumberOfMonths("12");
                testContext.setCustomerCode(accountInfo.get("CUSTOMER_CODE").toString());
                testContext.setPremisesCode(accountInfo.get("PREMISES_CODE").toString());
                List<Map<String, Object>> dbRows3 = ApplicationContext.get()
                        .getDbAction()
                        .getUsageHistory(accountInfo.get("CUSTOMER_CODE").toString(), accountInfo.get("PREMISES_CODE").toString());
                break;

            case TC_25__Positive__Usage_History_Read_Type_Format__Actual:
                accountInfo = ApplicationContext.get().getDbAction().getAccountWithActualReading();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("PREM_CODE").toString());
                payload.setNumberOfMonths("12");
                testContext.setCustomerCode(accountInfo.get("CUST_CODE").toString());
                testContext.setPremisesCode(accountInfo.get("PREM_CODE").toString());
                accountsInfo= ApplicationContext.get().getDbAction().getReadType(testContext.getCustomerCode(), testContext.getPremisesCode());
                break;

            case TC_26__Positive__Usage_History_Read_Type_Format__Zero_Consumption:
                accountInfo = ApplicationContext.get().getDbAction().getAccountWithZeroReading();

                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("PREM_CODE").toString());
                payload.setNumberOfMonths("12");
                testContext.setCustomerCode(accountInfo.get("CUST_CODE").toString());
                testContext.setPremisesCode(accountInfo.get("PREM_CODE").toString());
                accountsInfo= ApplicationContext.get().getDbAction().getReadType(testContext.getCustomerCode(), testContext.getPremisesCode());
                break;


            case TC_27__Positive__Usage_History_Read_Type_Format__Estimated:
                accountInfo = ApplicationContext.get().getDbAction().getAccountWithEstimatedReading();

                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("PREM_CODE").toString());
                payload.setNumberOfMonths("24");
                testContext.setCustomerCode(accountInfo.get("CUST_CODE").toString());
                testContext.setPremisesCode(accountInfo.get("PREM_CODE").toString());
                accountsInfo= ApplicationContext.get().getDbAction().getReadType(testContext.getCustomerCode(), testContext.getPremisesCode());
                break;

            case TC_33__Positive__No_Usage_History_Active:
                accountInfo = ApplicationContext.get().getDbAction().getNoUsageHistoryActiveAccount();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setNumberOfMonths("12");
                break;

            case TC_34__Positive__No_Usage_History_Final:
                accountInfo = ApplicationContext.get().getDbAction().getNoUsageHistoryFinalAccount();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setNumberOfMonths("12");
                break;

            case TC_35__Positive__No_Usage_History_Inactive:
                accountInfo = ApplicationContext.get().getDbAction().getNoUsageHistoryInactiveAccount();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setNumberOfMonths("12");
                break;

            case TC_36__Positive__Usage_History_Too_Old_Valid_Number_of_Months:
                accountInfo = ApplicationContext.get().getDbAction().getUsageHistoryOld();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("customer_code").toString());
                payload.setPremisesCode(accountInfo.get("premises_code").toString());
                payload.setNumberOfMonths("12");
                List<Map<String, Object>> dbRows = ApplicationContext.get()
                        .getDbAction()
                        .getUsageHistory(accountInfo.get("customer_code").toString(), accountInfo.get("premises_code").toString());
                break;

            case TC_37__Positive__Usage_History_Less_Than_Requested_Months:
                accountInfo = ApplicationContext.get().getDbAction().getUsageHistoryLess();
            payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("customer_code").toString());
                payload.setPremisesCode(accountInfo.get("premises_code").toString());
            payload.setNumberOfMonths("24");
                List<Map<String, Object>> dbRows2 = ApplicationContext.get()
                        .getDbAction()
                        .getUsageHistory2(accountInfo.get("customer_code").toString(), accountInfo.get("premises_code").toString());
                break;

            case TC_39__Positive__Usage_History_Greater_Than_Requested_Months:

                accountInfo = ApplicationContext.get().getDbAction().getUsageHistoryGreater();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("customer_code").toString());
                payload.setPremisesCode(accountInfo.get("premises_code").toString());
                payload.setNumberOfMonths("18");
                List<Map<String, Object>> dbRows4 = ApplicationContext.get()
                        .getDbAction()
                        .getUsageHistory2(accountInfo.get("customer_code").toString(), accountInfo.get("premises_code").toString());
                break;

            default:
                log.warn("Unhandled test condition: {}", testCondition);
                break;
        }
    }
}
