package com.gng.api.pages.csi.GetBillHistoryPage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.GetBillHistory.GetBillHistoryRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.GetBillHistory.GetBillHistoryLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class GetBillHistoryApiHelper {

    private final TestContext testContext;

    public GetBillHistoryApiHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    GetBillHistoryRequest preparePayload(GetBillHistoryLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        String jsonFileName = apiLabel.equals(GetBillHistoryLabel.get_bill_history)
                ? GetBillHistoryLabel.get_bill_history.toString()
                : GetBillHistoryLabel.get_bill_history_mandatory.toString();

        return BasePage.deserializeJsonToPojo(jsonFileName, GetBillHistoryRequest.class);
    }

    public void preparePayloadForTestCondition(GetBillHistoryRequest payload,
                                               GetBillHistoryLabel testCondition) {

        payload.setRequestID(FakerDataGenerator.generateAlphanumeric(10));
        Map<String, Object> accountInfo;

        switch (testCondition) {

            // ---------------- NEGATIVE TEST CASES ----------------

            case TC_11__Negative__Missing_Request_ID:
                payload.setRequestID("");
                break;

            case TC_12__Negative__Invalid_Request_ID_Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(33));
                break;

            case TC_13__Negative__Duplicate_Request_ID:
                payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());
                break;

            case TC_14__Negative__Missing_customerCode:
                payload.setCustomerCode("");
                break;

            case TC_15__Negative__Invalid_customerCode_Length:
                payload.setCustomerCode(FakerDataGenerator.generateDigits(10));
                break;

            case TC_16__Negative__Invalid_customerCode_Format:
                payload.setCustomerCode(FakerDataGenerator.generateAlphanumeric(6));
                break;

            case TC_17__Negative__Missing_premisesCode:
                payload.setPremisesCode("");
                break;

            case TC_18__Negative__Invalid_premisesCode_Length:
                payload.setPremisesCode(FakerDataGenerator.generateDigits(8));
                break;

            case TC_19__Negative__Invalid_premisesCode_Format:
                payload.setPremisesCode("A"+FakerDataGenerator.generateAlphanumeric(5));
                break;

            case TC_20__Negative__Invalid_Account_Number:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode("9988776");
                Map<String, Object> userInfo = ApplicationContext.get().getDbAction().getUserAccountInfo("9988776");
                payload.setCustomerCode("9988776");
                payload.setPremisesCode(FakerDataGenerator.generateDigits(6));
                break;

            case TC_21__Negative__Missing_Number_of_Months:
                payload.setNumberOfMonths(null);
                break;

            case TC_22__Negative__Invalid_Number_of_Months_Format_NotANumber:
                payload.setNumberOfMonths("ABC");
                break;

            case TC_23__Negative__Invalid_Number_of_Months_Zero:
                payload.setNumberOfMonths("0");
                break;

            case TC_24__Negative__Invalid_Number_of_Months_Negative:
                payload.setNumberOfMonths("-5");
                break;

            case TC_25__Negative__Invalid_Number_of_Months_Length:
                payload.setNumberOfMonths("1234");
                break;

            case TC_26__Negative__New_Account_Not_Allowed:
                accountInfo = ApplicationContext.get().getDbAction().getNewAccountOnly();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setNumberOfMonths("3");
                break;


            // ---------------- POSITIVE TEST CASES ----------------

            case TC_27__Positive__Usage_History_BillDate_Format:
            case TC_28__Positive__Usage_History_UsageFromDate_Format:
            case TC_29__Positive__Usage_History_UsageToDate_Format:
            case TC_30__Positive__Usage_History_DaysOfService_Format:
            case TC_31__Positive__Usage_History_HeatingDegreeDays_Format:
            case TC_32__Positive__Usage_History_TotalBilledConsumption_Format:
            case TC_33__Positive__Usage_History_BalanceBroughtForward_Format:
            case TC_34__Positive__Usage_History_GasServiceCharges_Format:
            case TC_35__Positive__Usage_History_OtherCharges_Format:
            case TC_36__Positive__Usage_History_PromotionalDiscounts_Format:
            case TC_37__Positive__Usage_History_Taxes_Format:
            case TC_38__Positive__Usage_History_BudgetBillingAmount_Format:
            case TC_39__Positive__Usage_History_NotBudgetBillingAccount:
            case TC_40__Positive__Usage_History_TotalBillAmount_Format:
            case TC_41__Positive__Usage_History_BillHistoryTransactionNumber_Format:
                accountInfo = ApplicationContext.get().getDbAction().getAccountWithBillHistory();
                payload.setCustomerCode(accountInfo.get("UBBBHST_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UBBBHST_prem_code").toString());
                payload.setNumberOfMonths("24");
                break;

            case TC_42__Positive__No_Usage_History_Active:
                accountInfo = ApplicationContext.get().getDbAction().getNoUsageHistoryActiveAccount();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setNumberOfMonths("24");
                break;

            case TC_43__Positive__No_Usage_History_Final:
            accountInfo = ApplicationContext.get().getDbAction().getNoUsageHistoryFinalAccount();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setNumberOfMonths("24");
                break;

            case TC_44__Positive__No_Usage_History_Inactive:
                accountInfo = ApplicationContext.get().getDbAction().getNoUsageHistoryInactiveAccount();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setNumberOfMonths("24");
                break;

            case TC_45__Positive__Valid_NumberOfMonths_BillHistory_Too_Old:
             accountInfo = ApplicationContext.get().getDbAction().getAccountWithActualReading2();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("urrshis_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("urrshis_prem_code").toString());
                payload.setNumberOfMonths("2");
                break;

            case TC_46__Positive__Usage_History_Less_Than_Requested_Months:
                accountInfo = ApplicationContext.get().getDbAction().getAccountWithActualReading3();

                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("urrshis_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("urrshis_prem_code").toString());
                payload.setNumberOfMonths("24");
                break;

            case TC_47__Positive__Usage_History_Equals_Requested_Months:
              accountInfo = ApplicationContext.get().getDbAction().getAccountWithActualReading2();

                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("urrshis_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("urrshis_prem_code").toString());
                payload.setNumberOfMonths("24");
                break;

            case TC_48__Positive__Usage_History_Greater_Than_Requested_Months:
              accountInfo = ApplicationContext.get().getDbAction().getAccountWithActualReading2();

                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("urrshis_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("urrshis_prem_code").toString());
                payload.setNumberOfMonths("14");
                break;

            default:
                log.warn("Unhandled test condition: {}", testCondition);
                break;
        }
    }
}
