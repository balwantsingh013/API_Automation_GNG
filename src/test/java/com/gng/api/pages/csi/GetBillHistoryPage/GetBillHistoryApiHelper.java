package com.gng.api.pages.csi.GetBillHistoryPage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.GetBillHistory.GetBillHistoryRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.GetBillHistory.GetBillHistoryLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
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

            case TC_60__Negative__Missing_Request_ID:
                payload.setRequestID("");
                break;

            case TC_61__Negative__Invalid_Request_ID_Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(33));
                break;

            case TC_62__Negative__Duplicate_Request_ID:
                payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());
                break;

            case TC_63__Negative__Missing_customerCode:
                payload.setCustomerCode("");
                break;

            case TC_64__Negative__Invalid_customerCode_Length:
                payload.setCustomerCode(FakerDataGenerator.generateDigits(10));
                break;

            case TC_65__Negative__Invalid_customerCode_Format:
                payload.setCustomerCode(FakerDataGenerator.generateAlphanumeric(6));
                break;

            case TC_66__Negative__Missing_premisesCode:
                payload.setPremisesCode("");
                break;

            case TC_67__Negative__Invalid_premisesCode_Length:
                payload.setPremisesCode(FakerDataGenerator.generateDigits(8));
                break;

            case TC_68__Negative__Invalid_premisesCode_Format:
                payload.setPremisesCode("A"+FakerDataGenerator.generateAlphanumeric(5));
                break;

            case TC_69__Negative__Invalid_Account_Number:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode("9988776");
                Map<String, Object> userInfo = ApplicationContext.get().getDbAction().getUserAccountInfo("9988776");
                payload.setCustomerCode("9988776");
                payload.setPremisesCode(FakerDataGenerator.generateDigits(6));
                break;

            case TC_70__Negative__Missing_Number_of_Months:
                payload.setNumberOfMonths(null);
                break;

            case TC_71__Negative__Invalid_Number_of_Months_Format_NotANumber:
                payload.setNumberOfMonths("ABC");
                break;

            case TC_72__Negative__Invalid_Number_of_Months_Zero:
                payload.setNumberOfMonths("0");
                break;

            case TC_73__Negative__Invalid_Number_of_Months_Negative:
                payload.setNumberOfMonths("-5");
                break;

            case TC_74__Negative__Invalid_Number_of_Months_Length:
                payload.setNumberOfMonths("1234");
                break;

            // ---------------- POSITIVE TEST CASES ----------------

            case TC_75__Positive__Usage_History_BillDate_Format:
            case TC_76__Positive__Usage_History_UsageFromDate_Format:
            case TC_77__Positive__Usage_History_UsageToDate_Format:
            case TC_78__Positive__Usage_History_DaysOfService_Format:
            case TC_79__Positive__Usage_History_HeatingDegreeDays_Format:
            case TC_80__Positive__Usage_History_TotalBilledConsumption_Format:
            case TC_81__Positive__Usage_History_BalanceBroughtForward_Format:
            case TC_82__Positive__Usage_History_GasServiceCharges_Format:
            case TC_83__Positive__Usage_History_OtherCharges_Format:
            case TC_84__Positive__Usage_History_PromotionalDiscounts_Format:
            case TC_85__Positive__Usage_History_Taxes_Format:
            case TC_86__Positive__Usage_History_BudgetBillingAmount_Format:
            case TC_87__Positive__Usage_History_NotBudgetBillingAccount:
            case TC_88__Positive__Usage_History_TotalBillAmount_Format:
            case TC_89__Positive__Usage_History_BillHistoryTransactionNumber_Format:
                accountInfo = ApplicationContext.get().getDbAction().getAccountWithBillHistory();
                payload.setCustomerCode(accountInfo.get("UBBBHST_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UBBBHST_prem_code").toString());
                payload.setNumberOfMonths("24");
                testContext.setCustomerCode(accountInfo.get("UBBBHST_cust_code").toString());
                testContext.setPremisesCode(accountInfo.get("UBBBHST_prem_code").toString());
                List<Map<String, Object>> dbRows4 = ApplicationContext.get()
                        .getDbAction()
                        .getBillHistory(accountInfo.get("UBBBHST_cust_code").toString(), accountInfo.get("UBBBHST_prem_code").toString());

                break;

            case TC_90__Positive__No_Usage_History_Active:
                accountInfo = ApplicationContext.get().getDbAction().getNoUsageHistoryActiveAccount();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setNumberOfMonths("24");
                break;

            case TC_91__Positive__No_Usage_History_Final:
            accountInfo = ApplicationContext.get().getDbAction().getNoUsageHistoryFinalAccount();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setNumberOfMonths("24");
                break;

            case TC_92__Positive__No_Usage_History_Inactive:
                accountInfo = ApplicationContext.get().getDbAction().getNoUsageHistoryInactiveAccount();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setNumberOfMonths("24");
                break;

            case TC_93__Positive__Valid_NumberOfMonths_BillHistory_Too_Old:
             accountInfo = ApplicationContext.get().getDbAction().getAccountWithActualReading2();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("urrshis_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("urrshis_prem_code").toString());
                payload.setNumberOfMonths("2");
                List<Map<String, Object>> dbRows = ApplicationContext.get()
                        .getDbAction()
                        .getBillHistory(accountInfo.get("urrshis_cust_code").toString(), accountInfo.get("urrshis_prem_code").toString());
                break;

            case TC_94__Positive__Usage_History_Less_Than_Requested_Months:
                accountInfo = ApplicationContext.get().getDbAction().getAccountWithActualReading2();

                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("urrshis_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("urrshis_prem_code").toString());
                payload.setNumberOfMonths("24");
                testContext.setCustomerCode(accountInfo.get("urrshis_cust_code").toString());
                testContext.setPremisesCode(accountInfo.get("urrshis_prem_code").toString());
                List<Map<String, Object>> dbRows2 = ApplicationContext.get()
                        .getDbAction()
                        .getBillHistory(accountInfo.get("urrshis_cust_code").toString(), accountInfo.get("urrshis_prem_code").toString());
                break;

            case TC_95__Positive__Usage_History_Equals_Requested_Months:
              accountInfo = ApplicationContext.get().getDbAction().getAccountWithActualReading2();

                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("urrshis_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("urrshis_prem_code").toString());
                payload.setNumberOfMonths("24");
                break;

            case TC_96__Positive__Usage_History_Greater_Than_Requested_Months:
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
