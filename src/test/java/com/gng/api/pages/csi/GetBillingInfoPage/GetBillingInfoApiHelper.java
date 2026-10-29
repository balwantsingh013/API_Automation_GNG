package com.gng.api.pages.csi.GetBillingInfoPage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.GetBillingInfo.GetBillingInfoRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.report.DualReportManager;
import com.gng.api.steps.csi.GetBillingInfo.GetBillingInfoLabel;
import com.gng.api.util.FakerDataGenerator;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.testng.SkipException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static com.gng.api.constants.ApiEndPoint.GET_BILLING_INFO;

@Slf4j
public class GetBillingInfoApiHelper {

    private final TestContext testContext;

    public GetBillingInfoApiHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    GetBillingInfoRequest preparePayload(GetBillingInfoLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        String jsonFileName = apiLabel.equals(GetBillingInfoLabel.get_billing_info)
                ? GetBillingInfoLabel.get_billing_info.toString()
                : GetBillingInfoLabel.get_billing_info_mandatory.toString();
        return BasePage.deserializeJsonToPojo(jsonFileName, GetBillingInfoRequest.class);
    }

    public void preparePayloadForTestCondition(GetBillingInfoRequest payload, GetBillingInfoLabel testCondition) {
        Map<String, Object> accountData = null;

        switch (testCondition) {

            // ---------------- NEGATIVE TEST CASES ----------------

            case TC_1__Negative__Missing_Request_ID_:
                payload.setRequestID("");
                break;

            case TC_2__Negative__Invalid_Request_ID_Length_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(33));
                break;

            case TC_3__Negative__Duplicate_Request_ID_:
                payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());
                break;

            case TC_4__Negative__Missing_customerCode_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode("");
                break;

            case TC_5__Negative__Invalid_customerCode_Length_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateDigits(10));
                break;

            case TC_6__Negative__Invalid_customerCode_Format_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateString(6));
                break;

            case TC_7__Negative__Missing_premisesCode_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPremisesCode("");
                break;

            case TC_8__Negative__Invalid_premisesCode_Length_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(8));
                break;

            case TC_9__Negative__Invalid_premisesCode_Format_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPremisesCode(FakerDataGenerator.generateString(6));
                break;

            case TC_10__Negative__Invalid_Account_Number__Invalid_Account_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode("9988776");
                Map<String, Object> userInfo = ApplicationContext.get().getDbAction().getUserAccountInfo("9988776");
                payload.setCustomerCode("9988776");
                payload.setPremisesCode(FakerDataGenerator.generateDigits(6));
                break;


            case TC_11__Positive__Valid_Account_with_No_Billing_History:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getNoUsageHistoryActiveAccount();
                payload.setCustomerCode(accountData.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountData.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_12__Positive__Valid_Account_with_1_Month_Billing_History:
                accountData = resolveListedSeed(
                        ApplicationContext.get().getDbAction().listBillingInfoTc12Seeds(),
                        "TC_12",
                        "no account with exactly one bill in the last 24 months",
                        response -> response.jsonPath().get("data") != null);
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                break;

            case TC_13__Positive__Valid_Account_with_More_Than_1_Month_Billing_History:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getUsageHistoryMoreThanOne();
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                List<Map<String, Object>> dbRows2 = ApplicationContext.get()
                        .getDbAction()
                        .getBillInfo(accountData.get("ubbbhst_cust_code").toString(), accountData.get("ubbbhst_prem_code").toString());
                break;

            case TC_14__Positive__Billing_Info_Bill_Date_Format:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getUsageHistoryMoreThanOne();
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                List<Map<String, Object>> dbRows3 = ApplicationContext.get()
                        .getDbAction()
                        .getBillInfo(accountData.get("ubbbhst_cust_code").toString(), accountData.get("ubbbhst_prem_code").toString());
                break;

            case TC_15__Positive__Billing_Info_Bill_From_Date_Format:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getUsageHistoryMoreThanOne();
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                List<Map<String, Object>> dbRows4 = ApplicationContext.get()
                        .getDbAction()
                        .getBillInfo(accountData.get("ubbbhst_cust_code").toString(), accountData.get("ubbbhst_prem_code").toString());
                break;
//
            case TC_16__Positive__Billing_Info_Bill_To_Date_Format:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getUsageHistoryMoreThanOne();
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                List<Map<String, Object>> dbRows5 = ApplicationContext.get()
                        .getDbAction()
                        .getBillInfo(accountData.get("ubbbhst_cust_code").toString(), accountData.get("ubbbhst_prem_code").toString());
                break;
//
            case TC_17__Positive__Billing_Info_Past_Due_Amount_Format__Past_Due_Amount_Greater_Than_0:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = resolveListedSeed(
                        ApplicationContext.get().getDbAction().listBillingInfoTc17Seeds(),
                        "TC_17",
                        "no account with past due amount greater than 0",
                        response -> isPositive(response.jsonPath().get("data.pastDueAmount")));
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                List<Map<String, Object>> dbRows6 = ApplicationContext.get()
                        .getDbAction()
                        .getBillInfo(accountData.get("ubbbhst_cust_code").toString(), accountData.get("ubbbhst_prem_code").toString());
                break;
//
            case TC_18__Positive__Billing_Info_Past_Due_Amount_Format__Past_Due_Amount_Equals_0:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = resolveOneMonthBillingCandidate("data.pastDueAmount");
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                List<Map<String, Object>> dbRows7 = ApplicationContext.get()
                        .getDbAction()
                        .getBillInfo(accountData.get("ubbbhst_cust_code").toString(), accountData.get("ubbbhst_prem_code").toString());
                break;
//
            case TC_19__Positive__Billing_Info_Past_Due_Date_Format:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getAccountForBillInfo("147670", "151572");
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                List<Map<String, Object>> dbRows8 = ApplicationContext.get()
                        .getDbAction()
                        .getBillInfo(accountData.get("ubbbhst_cust_code").toString(), accountData.get("ubbbhst_prem_code").toString());
                break;
//
            case TC_20__Positive__Billing_Info_Previous_Bill_Amount_Format__Previous_Bill_Amount_Greater_Than_0:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getUsageHistoryMoreThanOne();
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                List<Map<String, Object>> dbRows9 = ApplicationContext.get()
                        .getDbAction()
                        .getBillInfo(accountData.get("ubbbhst_cust_code").toString(), accountData.get("ubbbhst_prem_code").toString());
                break;

            case TC_21__Positive__Billing_Info_Previous_Bill_Amount_Format__Previous_Bill_Amount_Equals_0:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = resolveListedSeed(
                        ApplicationContext.get().getDbAction().listBillingInfoTc21Seeds(),
                        "TC_21",
                        "no recent bill with previous balance 0",
                        response -> isZero(response.jsonPath().get("data.previousBillAmount")));
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                List<Map<String, Object>> dbRows10 = ApplicationContext.get()
                        .getDbAction()
                        .getBillInfo(accountData.get("ubbbhst_cust_code").toString(), accountData.get("ubbbhst_prem_code").toString());
                break;

            case TC_22__Positive__Billing_Info_Payments_Applied_Amount_Format__Payments_Applied_Amount_Greater_Than_0:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getUsageHistoryMoreThanOne();
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                List<Map<String, Object>> dbRows11 = ApplicationContext.get()
                        .getDbAction()
                        .getBillInfo(accountData.get("ubbbhst_cust_code").toString(), accountData.get("ubbbhst_prem_code").toString());
                break;

            case TC_23__Positive__Billing_Info_Payments_Applied_Amount_Format__Payments_Applied_Amount_Equals_0:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = resolveOneMonthBillingCandidate("data.paymentsApplied");
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                List<Map<String, Object>> dbRows12 = ApplicationContext.get()
                        .getDbAction()
                        .getBillInfo(accountData.get("ubbbhst_cust_code").toString(), accountData.get("ubbbhst_prem_code").toString());
                break;

            case TC_24__Positive__Balance_Brought_Forward_Greater_Than_0_Format:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getAccountForBillInfo("99342", "101785");
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                List<Map<String, Object>> dbRows13 = ApplicationContext.get()
                        .getDbAction()
                        .getBillInfo(accountData.get("ubbbhst_cust_code").toString(), accountData.get("ubbbhst_prem_code").toString());
                break;
//
            case TC_25__Positive__Balance_Brought_Forward_Equals_0_Format:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getUsageHistoryMoreThanOne();
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                List<Map<String, Object>> dbRows14 = ApplicationContext.get()
                        .getDbAction()
                        .getBillInfo(accountData.get("ubbbhst_cust_code").toString(), accountData.get("ubbbhst_prem_code").toString());
                break;
//
            case TC_26__Positive__Balance_Brought_Forward_Less_Than_0_Format:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = resolveListedSeed(
                        ApplicationContext.get().getDbAction().listBillingInfoTc26Seeds(),
                        "TC_26",
                        "no recent bill with balance brought forward less than 0",
                        response -> isNegative(response.jsonPath().get("data.balanceBroughtForward")));
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                List<Map<String, Object>> dbRows15 = ApplicationContext.get()
                        .getDbAction()
                        .getBillInfo(accountData.get("ubbbhst_cust_code").toString(), accountData.get("ubbbhst_prem_code").toString());
                break;
//
            case TC_27__Positive__Current_Charges_Format:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getUsageHistoryMoreThanOne();
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                List<Map<String, Object>> dbRows16 = ApplicationContext.get()
                        .getDbAction()
                        .getBillInfo(accountData.get("ubbbhst_cust_code").toString(), accountData.get("ubbbhst_prem_code").toString());
                break;

            case TC_28__Positive__Total_Amount_Due_Format:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getUsageHistoryMoreThanOne();
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                 List<Map<String, Object>> dbRows17 = ApplicationContext.get()
                    .getDbAction()
                    .getBillInfo(accountData.get("ubbbhst_cust_code").toString(), accountData.get("ubbbhst_prem_code").toString());
            break;

            case TC_29__Positive__Bill_Due_Date_Format:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getUsageHistoryMoreThanOne();
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                List<Map<String, Object>> dbRows18 = ApplicationContext.get()
                        .getDbAction()
                        .getBillInfo(accountData.get("ubbbhst_cust_code").toString(), accountData.get("ubbbhst_prem_code").toString());
                break;

            case TC_30__Positive__Payments_Since_Last_Bill_Greater_Than_0_Format:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getAccountForBillInfo("3147", "2760");
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                List<Map<String, Object>> dbRows19 = ApplicationContext.get()
                        .getDbAction()
                        .getBillInfo(accountData.get("ubbbhst_cust_code").toString(), accountData.get("ubbbhst_prem_code").toString());
                break;

            case TC_31__Positive__Payments_Since_Last_Bill_Equals_0_Format:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getAccountForBillInfo("6073939", "6047368");
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                List<Map<String, Object>> dbRows20 = ApplicationContext.get()
                        .getDbAction()
                        .getBillInfo(accountData.get("ubbbhst_cust_code").toString(), accountData.get("ubbbhst_prem_code").toString());
                break;
//
            case TC_32__Positive__Current_Balance_Format:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = resolveListedSeed(
                        ApplicationContext.get().getDbAction().listBillingInfoTc32Seeds(),
                        "TC_32",
                        "no account with a non-zero current balance",
                        response -> isNonZero(response.jsonPath().get("data.currentBalance")));
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                List<Map<String, Object>> dbRows21 = ApplicationContext.get()
                        .getDbAction()
                        .getBillInfo(accountData.get("ubbbhst_cust_code").toString(), accountData.get("ubbbhst_prem_code").toString());
                break;
//
            case TC_33__Positive__Budget_Billing_Amount_not_Budget_Billing_Account_Format:
            case SP1_06__Positive__Budget_Billing_True_Up_Date_not_Budget_Billing_Account_Format_:
            case SP1_08__Positive__Budget_Billing_Actual_Charges_not_Budget_Billing_Account_Format_:
            case SP1_10__Positive__Guaranteed_Bill_Amount_not_Guaranteed_Bill_Account_Format_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getUsageHistoryMoreThanOne();
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                List<Map<String, Object>> dbRows22 = ApplicationContext.get()
                        .getDbAction()
                        .getBillInfo(accountData.get("ubbbhst_cust_code").toString(), accountData.get("ubbbhst_prem_code").toString());
                break;

            case TC_34__Positive__Budget_Billing_TotalVariance_not_Budget_Billing_Account_Format:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getUsageHistoryMoreThanOne();
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                List<Map<String, Object>> dbRows23 = ApplicationContext.get()
                        .getDbAction()
                        .getBillInfo(accountData.get("ubbbhst_cust_code").toString(), accountData.get("ubbbhst_prem_code").toString());
                break;

            case TC_35__Positive__Budget_Billing_Start_Date_not_Budget_Billing_Account_Format:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getUsageHistoryMoreThanOne();
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                List<Map<String, Object>> dbRows24 = ApplicationContext.get()
                        .getDbAction()
                        .getBillInfo(accountData.get("ubbbhst_cust_code").toString(), accountData.get("ubbbhst_prem_code").toString());
                break;

            case TC_36__Positive__Budget_Billing_Amount_Budget_Billing_Account_Format:
            case SP1_05__Positive__Budget_Billing_True_Up_Date_Budget_Billing_Account_Format_:
            case SP1_07__Positive__Budget_Billing_Actual_Charges_Budget_Billing_Account_Format_:
            case SP1_11__Positive__Budget_Billing_Current_Values_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getAccountForBillInfo("2984", "2597");
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                List<Map<String, Object>> dbRows25 = ApplicationContext.get()
                        .getDbAction()
                        .getBillInfo(accountData.get("ubbbhst_cust_code").toString(), accountData.get("ubbbhst_prem_code").toString());
                break;

            case TC_37__Positive__Budget_Billing_TotalVariance_Budget_Billing_Account_Format:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getAccountForBillInfo("2984", "2597");
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                List<Map<String, Object>> dbRows26 = ApplicationContext.get()
                        .getDbAction()
                        .getBillInfo(accountData.get("ubbbhst_cust_code").toString(), accountData.get("ubbbhst_prem_code").toString());
                break;

            case TC_38__Positive__Budget_Billing_Start_Date_Budget_Billing_Account_Format:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getAccountForBillInfo("2984", "2597");
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                List<Map<String, Object>> dbRows27 = ApplicationContext.get()
                        .getDbAction()
                        .getBillInfo(accountData.get("ubbbhst_cust_code").toString(), accountData.get("ubbbhst_prem_code").toString());
                break;

            case SP1_01__Positive__Adjustments_Applied_Amount_Greater_Than_0_Format_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = resolveListedSeed(
                        ApplicationContext.get().getDbAction().listBillingInfoSp101Seeds(),
                        "SP1_01",
                        "no account with adjustments applied greater than 0",
                        response -> isPositive(response.jsonPath().get("data.adjustmentsApplied")));
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                break;

            case SP1_03__Positive__Adjustments_Since_Last_Bill_Greater_Than_0_Format_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = resolveListedSeed(
                        ApplicationContext.get().getDbAction().listBillingInfoSp103Seeds(),
                        "SP1_03",
                        "no account with adjustments since last bill greater than 0",
                        response -> isPositive(response.jsonPath().get("data.adjustmentsSinceLastBill")));
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                break;

            case SP1_02__Positive__Adjustments_Applied_Amount_Equals_0_:
            case SP1_04__Positive__Adjustments_Since_Last_Bill_Equals_0_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getUsageHistoryMoreThanOne();
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                break;

            // Guaranteed Bill: probe TC_5 GB pools via GetBillingInfo; run if guaranteedBillAmount present, else Skip
            case SP1_09__Positive__Guaranteed_Bill_Amount_Guaranteed_Bill_Account_Format_:
            case SP1_12__Positive__Guaranteed_Bill_Current_Values_:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = resolveGuaranteedBillBillingInfoCandidate(testCondition.name());
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                testContext.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                testContext.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                break;


            case TC_39__Positive__Bill_Line_Items_Code_Format:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getUsageHistoryMoreThanOne();
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                List<Map<String, Object>> dbRows28 = ApplicationContext.get()
                        .getDbAction()
                        .getBillInfo(accountData.get("ubbbhst_cust_code").toString(), accountData.get("ubbbhst_prem_code").toString());
                break;

            case TC_40__Positive__Bill_Line_Items_Description_Format:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getUsageHistoryMoreThanOne();
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                List<Map<String, Object>> dbRows29 = ApplicationContext.get()
                        .getDbAction()
                        .getBillInfo(accountData.get("ubbbhst_cust_code").toString(), accountData.get("ubbbhst_prem_code").toString());
                break;

            case TC_41__Positive__Bill_Line_Items_Amount_Format:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getUsageHistoryMoreThanOne();
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                List<Map<String, Object>> dbRows30 = ApplicationContext.get()
                        .getDbAction()
                        .getBillInfo(accountData.get("ubbbhst_cust_code").toString(), accountData.get("ubbbhst_prem_code").toString());
                break;

            case TC_42__Positive__Bill_Line_Items_Category_Format:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getUsageHistoryMoreThanOne();
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                List<Map<String, Object>> dbRows31 = ApplicationContext.get()
                        .getDbAction()
                        .getBillInfo(accountData.get("ubbbhst_cust_code").toString(), accountData.get("ubbbhst_prem_code").toString());
                break;

            case TC_49__Positive__Bill_Line_Items_Reward_Order:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = resolveListedSeed(
                        ApplicationContext.get().getDbAction().listBillingInfoTc49Seeds(),
                        "TC_49",
                        "no account whose current bill contains a reward line",
                        response -> hasCategory(response, "REWARD"));
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                List<Map<String, Object>> dbRows32 = ApplicationContext.get()
                        .getDbAction()
                        .getBillInfo(accountData.get("ubbbhst_cust_code").toString(), accountData.get("ubbbhst_prem_code").toString());
                break;

            case TC_43__Positive__Bill_Line_Items_Base_Charge_Order:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getAccountForBillInfo("6073939", "6047368");
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                List<Map<String, Object>> dbRows33 = ApplicationContext.get()
                        .getDbAction()
                        .getBillInfo(accountData.get("ubbbhst_cust_code").toString(), accountData.get("ubbbhst_prem_code").toString());
                break;

            case TC_44__Positive__Bill_Line_Items_Customer_Service_Charge_Order:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getAccountForBillInfo("6073939", "6047368");
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                List<Map<String, Object>> dbRows34 = ApplicationContext.get()
                        .getDbAction()
                        .getBillInfo(accountData.get("ubbbhst_cust_code").toString(), accountData.get("ubbbhst_prem_code").toString());
                break;

            case TC_45__Positive__Bill_Line_Items_Natural_Gas_Charge_Order:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getAccountForBillInfo("6073939", "6047368");
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                List<Map<String, Object>> dbRows35 = ApplicationContext.get()
                        .getDbAction()
                        .getBillInfo(accountData.get("ubbbhst_cust_code").toString(), accountData.get("ubbbhst_prem_code").toString());
                break;

            case TC_46__Positive__Bill_Line_Items_Bill_Guarantee_Order:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getAccountForBillInfo("2968", "2581");
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                List<Map<String, Object>> dbRows36 = ApplicationContext.get()
                        .getDbAction()
                        .getBillInfo(accountData.get("ubbbhst_cust_code").toString(), accountData.get("ubbbhst_prem_code").toString());
                break;

            case TC_47__Positive__Bill_Line_Items_Interstate_Pipeline_Capacity_Charge_Order:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getUsageHistoryINTERSTATE();
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                List<Map<String, Object>> dbRows37 = ApplicationContext.get()
                        .getDbAction()
                        .getBillInfo(accountData.get("ubbbhst_cust_code").toString(), accountData.get("ubbbhst_prem_code").toString());
                break;

            case TC_48__Positive__Bill_Line_Items_Promotional_Discount_Charge_Order:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getAccountForBillInfo("3147", "2760");
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                List<Map<String, Object>> dbRows38 = ApplicationContext.get()
                        .getDbAction()
                        .getBillInfo(accountData.get("ubbbhst_cust_code").toString(), accountData.get("ubbbhst_prem_code").toString());
                break;

            case TC_50__Positive__Bill_Line_Items_Miscellaneous_Charge_Order:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getAccountForBillInfo("2984", "2597");
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                List<Map<String, Object>> dbRows39 = ApplicationContext.get()
                        .getDbAction()
                        .getBillInfo(accountData.get("ubbbhst_cust_code").toString(), accountData.get("ubbbhst_prem_code").toString());
                break;

            case TC_51__Positive__Bill_Line_Items_Miscellaneous_Credit_Order:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = resolveListedSeed(
                        ApplicationContext.get().getDbAction().listBillingInfoTc51Seeds(),
                        "TC_51",
                        "no account whose current bill contains a miscellaneous credit line",
                        response -> hasCategory(response, "MISCCREDIT"));
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                List<Map<String, Object>> dbRows40 = ApplicationContext.get()
                        .getDbAction()
                        .getBillInfo(accountData.get("ubbbhst_cust_code").toString(), accountData.get("ubbbhst_prem_code").toString());
                break;

            case TC_52__Positive__Bill_Line_Items_Taxes_Order:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getUsageHistoryMoreThanOne();
                payload.setCustomerCode(accountData.get("ubbbhst_cust_code").toString());
                payload.setPremisesCode(accountData.get("ubbbhst_prem_code").toString());
                List<Map<String, Object>> dbRows41 = ApplicationContext.get()
                        .getDbAction()
                        .getBillInfo(accountData.get("ubbbhst_cust_code").toString(), accountData.get("ubbbhst_prem_code").toString());
                break;

            default:
                log.warn("Unhandled test condition: {}", testCondition);
                break;
        }
    }

    /**
     * SP1-09 / SP1-12 seed from guaranteed-bill rate codes. Skip only when none of those
     * accounts return a non-null guaranteedBillAmount.
     */
    private Map<String, Object> resolveGuaranteedBillBillingInfoCandidate(String testCondition) {
        List<Map<String, Object>> candidates = new ArrayList<>();
        try {
            candidates.addAll(ApplicationContext.get().getDbAction().listBillingInfoGuaranteedBillSeeds());
        } catch (Exception e) {
            log.warn("Guaranteed bill seed query failed: {}", e.getMessage());
        }

        int probed = 0;
        final int maxProbes = 10;
        for (Map<String, Object> candidate : candidates) {
            if (candidate == null) {
                continue;
            }
            if (probed >= maxProbes) {
                break;
            }
            String cust;
            String prem;
            try {
                cust = firstNonNull(candidate,
                        "ucracct_cust_code", "UCRACCT_CUST_CODE",
                        "ubbbhst_cust_code", "UBBBHST_CUST_CODE").toString();
                prem = firstNonNull(candidate,
                        "ucracct_prem_code", "UCRACCT_PREM_CODE",
                        "ubbbhst_prem_code", "UBBBHST_PREM_CODE").toString();
            } catch (Exception e) {
                continue;
            }
            probed++;
            Map<String, Object> probeAccount = Map.of(
                    "ubbbhst_cust_code", cust,
                    "ubbbhst_prem_code", prem);
            Response response = callGetBillingInfo(probeAccount);
            if (response.statusCode() != 200) {
                continue;
            }
            Object err = response.jsonPath().get("errorCode");
            if (err == null) {
                err = response.jsonPath().get("ErrorCode");
            }
            Integer errorCode = err instanceof Number ? ((Number) err).intValue() : null;
            if (errorCode == null || errorCode != 0) {
                continue;
            }
            Object guaranteedBillAmount = response.jsonPath().get("data.guaranteedBillAmount");
            if (guaranteedBillAmount == null) {
                continue;
            }
            DualReportManager.logInfo(
                    testCondition + " Guaranteed Bill seed " + cust + "/" + prem
                            + " (guaranteedBillAmount=" + guaranteedBillAmount + ")");
            return probeAccount;
        }

        String reason = testCondition + " — No automatable Guaranteed Bill GetBillingInfo seed; "
                + "manual account creation is not permitted for automation.";
        DualReportManager.logInfo("SKIPPED: " + reason);
        throw new SkipException(reason);
    }

    private static Object firstNonNull(Map<String, Object> map, String... keys) {
        for (String key : keys) {
            Object value = map.get(key);
            if (value != null) {
                return value;
            }
        }
        throw new IllegalStateException("None of keys found in DB result: " + String.join(", ", keys));
    }

    private Map<String, Object> resolveOneMonthBillingCandidate(String zeroAmountField) {
        List<Map<String, Object>> candidates;
        try {
            candidates = ApplicationContext.get().getDbAction().listUsageHistoryOneCandidates();
        } catch (Exception e) {
            String reason = "TC_12 — one-month billing candidate query failed/unavailable; "
                    + "manual account creation is not permitted for automation. " + e.getMessage();
            DualReportManager.logInfo("SKIPPED: " + reason);
            throw new SkipException(reason, e);
        }
        if (candidates == null || candidates.isEmpty()) {
            String reason = "TC_12 — no automatable one-month billing history account; "
                    + "manual account creation is not permitted for automation.";
            DualReportManager.logInfo("SKIPPED: " + reason);
            throw new SkipException(reason);
        }
        for (Map<String, Object> candidate : candidates) {
            if (matchesBillingCandidate(candidate, zeroAmountField)) {
                log.info("Resolved one-month billing candidate via probe {}/{}",
                        candidate.get("ubbbhst_cust_code"), candidate.get("ubbbhst_prem_code"));
                return candidate;
            }
        }

        String reason = "TC_12 — no probe-validated one-month billing history account; "
                + "manual account creation is not permitted for automation.";
        DualReportManager.logInfo("SKIPPED: " + reason);
        throw new SkipException(reason);
    }

    private boolean matchesBillingCandidate(Map<String, Object> accountData, String zeroAmountField) {
        if (accountData == null) {
            return false;
        }
        Response response = callGetBillingInfo(accountData);
        if (response.jsonPath().getInt("errorCode") != 0 || !Boolean.TRUE.equals(response.jsonPath().getBoolean("success"))) {
            return false;
        }
        if (zeroAmountField == null) {
            return true;
        }
        Object value = response.jsonPath().get(zeroAmountField);
        if (!(value instanceof Number)) {
            return false;
        }
        return Math.abs(((Number) value).doubleValue()) < 0.0001d;
    }

    private Response callGetBillingInfo(Map<String, Object> accountData) {
        GetBillingInfoRequest payload = BasePage.deserializeJsonToPojo(
                GetBillingInfoLabel.get_billing_info.toString(),
                GetBillingInfoRequest.class);
        payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
        payload.setCustomerCode(firstNonNull(accountData,
                "ubbbhst_cust_code", "UBBBHST_CUST_CODE",
                "ucracct_cust_code", "UCRACCT_CUST_CODE").toString());
        payload.setPremisesCode(firstNonNull(accountData,
                "ubbbhst_prem_code", "UBBBHST_PREM_CODE",
                "ucracct_prem_code", "UCRACCT_PREM_CODE").toString());

        String uri = ApplicationContext.get().getEnvConfig().getBaseUri() + GET_BILLING_INFO;
        return io.restassured.RestAssured.given()
                .relaxedHTTPSValidation()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + testContext.getAuthToken())
                .body(payload)
                .post(uri);
    }

    private Map<String, Object> resolveListedSeed(List<Map<String, Object>> candidates,
                                                   String label,
                                                   String emptyReason,
                                                   Predicate<Response> accept) {
        if (candidates == null || candidates.isEmpty()) {
            skipListed(label, emptyReason);
        }
        int probed = 0;
        for (Map<String, Object> candidate : candidates) {
            if (candidate == null || probed >= 25) {
                if (probed >= 25) {
                    break;
                }
                continue;
            }
            String cust;
            String prem;
            try {
                cust = firstNonNull(candidate, "ubbbhst_cust_code", "UBBBHST_CUST_CODE").toString();
                prem = firstNonNull(candidate, "ubbbhst_prem_code", "UBBBHST_PREM_CODE").toString();
            } catch (Exception e) {
                continue;
            }
            probed++;
            Map<String, Object> account = new HashMap<>();
            account.put("ubbbhst_cust_code", cust);
            account.put("ubbbhst_prem_code", prem);
            Response response = callGetBillingInfo(account);
            if (response.statusCode() != 200) {
                continue;
            }
            Object err = response.jsonPath().get("errorCode");
            if (!(err instanceof Number) || ((Number) err).intValue() != 0) {
                continue;
            }
            if (accept != null && !accept.test(response)) {
                continue;
            }
            if (!matchesBillInfoV14(cust, prem, response)) {
                continue;
            }
            DualReportManager.logInfo(label + " seed " + cust + "/" + prem);
            return account;
        }
        skipListed(label, "no probe-validated account; " + emptyReason);
        return null;
    }

    private boolean matchesBillInfoV14(String customerCode, String premisesCode, Response response) {
        try {
            GetBillingInfoRequest probe = new GetBillingInfoRequest();
            probe.setCustomerCode(customerCode);
            probe.setPremisesCode(premisesCode);
            assertApiMatchesBillInfoV14(probe, response);
            return true;
        } catch (AssertionError e) {
            log.info("Rejected seed {}/{}: {}", customerCode, premisesCode, e.getMessage());
            return false;
        }
    }

    private static void skipListed(String label, String reason) {
        String message = label + " — " + reason
                + "; manual account creation is not permitted for automation.";
        DualReportManager.logInfo("SKIPPED: " + message);
        throw new SkipException(message);
    }

    private static boolean isZero(Object value) {
        return value instanceof Number && Math.abs(((Number) value).doubleValue()) < 0.0001d;
    }

    private static boolean isNegative(Object value) {
        return value instanceof Number && ((Number) value).doubleValue() < 0d;
    }

    private static boolean isPositive(Object value) {
        return value instanceof Number && ((Number) value).doubleValue() > 0.0001d;
    }

    private static boolean isNonZero(Object value) {
        return value instanceof Number && Math.abs(((Number) value).doubleValue()) > 0.0001d;
    }

    private static boolean hasCategory(Response response, String category) {
        List<Map<String, Object>> lines = response.jsonPath().getList("data.billLineItems");
        if (lines == null) {
            return false;
        }
        for (Map<String, Object> line : lines) {
            Object value = line.get("category");
            if (value != null && category.equalsIgnoreCase(String.valueOf(value))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Fails the scenario when the chosen account does not actually contain the value under test.
     */
    public void assertScenarioEvidence(GetBillingInfoLabel testCondition, Response response) {
        if (testCondition == null || response == null) {
            return;
        }
        switch (testCondition) {
            case TC_17__Positive__Billing_Info_Past_Due_Amount_Format__Past_Due_Amount_Greater_Than_0:
                requireEvidence(isPositive(response.jsonPath().get("data.pastDueAmount")),
                        "TC17 pastDueAmount must be greater than 0");
                break;
            case TC_32__Positive__Current_Balance_Format:
                requireEvidence(isNonZero(response.jsonPath().get("data.currentBalance")),
                        "TC32 currentBalance must be non-zero");
                break;
            case TC_49__Positive__Bill_Line_Items_Reward_Order:
                requireEvidence(hasCategory(response, "REWARD"),
                        "TC49 bill line items must include category REWARD");
                break;
            case TC_51__Positive__Bill_Line_Items_Miscellaneous_Credit_Order:
                requireEvidence(hasCategory(response, "MISCCREDIT"),
                        "TC51 bill line items must include category MISCCREDIT");
                break;
            case SP1_01__Positive__Adjustments_Applied_Amount_Greater_Than_0_Format_:
                requireEvidence(isPositive(response.jsonPath().get("data.adjustmentsApplied")),
                        "SP1-01 adjustmentsApplied must be greater than 0");
                break;
            case SP1_03__Positive__Adjustments_Since_Last_Bill_Greater_Than_0_Format_:
                requireEvidence(isPositive(response.jsonPath().get("data.adjustmentsSinceLastBill")),
                        "SP1-03 adjustmentsSinceLastBill must be greater than 0");
                break;
            default:
                break;
        }
    }

    private static void requireEvidence(boolean proven, String message) {
        if (!proven) {
            throw new AssertionError(message);
        }
    }

    /**
     * Compares a successful GetBillingInfo payload to the V1.4 Banner query.
     * Negative responses and empty data are left to the existing error-code checks.
     */
    public void assertApiMatchesBillInfoV14(GetBillingInfoRequest payload, Response response) {
        if (payload == null || payload.getCustomerCode() == null || payload.getCustomerCode().isBlank()
                || payload.getPremisesCode() == null || payload.getPremisesCode().isBlank()) {
            return;
        }
        Object errorCode = response.jsonPath().get("errorCode");
        if (!(errorCode instanceof Number) || ((Number) errorCode).intValue() != 0) {
            return;
        }
        if (response.jsonPath().get("data") == null) {
            return;
        }

        List<Map<String, Object>> dbRows = ApplicationContext.get().getDbAction()
                .getBillInfoV14(payload.getCustomerCode(), payload.getPremisesCode());
        if (dbRows == null || dbRows.isEmpty()) {
            throw new AssertionError("V1.4 GetBillingInfo returned no current bill for "
                    + payload.getCustomerCode() + "/" + payload.getPremisesCode());
        }

        Map<String, Object> header = dbRows.get(0);
        String row = payload.getCustomerCode() + "/" + payload.getPremisesCode();
        assertText(row, "billDate", response.jsonPath().get("data.billDate"), col(header, "billDate"));
        assertText(row, "billFromDate", response.jsonPath().get("data.billFromDate"), col(header, "billFromDate"));
        assertText(row, "billToDate", response.jsonPath().get("data.billToDate"), col(header, "billToDate"));
        assertText(row, "pastDueDate", response.jsonPath().get("data.pastDueDate"), col(header, "pastDueDate"));
        assertText(row, "billDueDate", response.jsonPath().get("data.billDueDate"), col(header, "billDueDate"));
        assertText(row, "budgetBillingStartDate", response.jsonPath().get("data.budgetBillingStartDate"), col(header, "budgetBillingStartDate"));
        assertText(row, "budgetBillingTrueUpDate", response.jsonPath().get("data.budgetBillingTrueUpDate"), col(header, "budgetBillingTrueUpDate"));
        assertMoney(row, "pastDueAmount", response.jsonPath().get("data.pastDueAmount"), col(header, "pastDueAmount"));
        assertMoney(row, "previousBillAmount", response.jsonPath().get("data.previousBillAmount"), col(header, "previousBillAmount"));
        assertMoney(row, "paymentsApplied", response.jsonPath().get("data.paymentsApplied"), col(header, "paymentsApplied"));
        assertMoney(row, "adjustmentsApplied", response.jsonPath().get("data.adjustmentsApplied"), col(header, "adjustmentsApplied"));
        assertMoney(row, "balanceBroughtForward", response.jsonPath().get("data.balanceBroughtForward"), col(header, "balanceBroughtForward"));
        assertMoney(row, "currentCharges", response.jsonPath().get("data.currentCharges"), col(header, "currentCharges"));
        assertMoney(row, "paymentsSinceLastBill", response.jsonPath().get("data.paymentsSinceLastBill"), col(header, "paymentsSinceLastBill"));
        assertMoney(row, "adjustmentsSinceLastBill", response.jsonPath().get("data.adjustmentsSinceLastBill"), col(header, "adjustmentsSinceLastBill"));
        assertMoney(row, "currentBalance", response.jsonPath().get("data.currentBalance"), col(header, "currentBalance"));
        assertMoney(row, "totalAmountDue", response.jsonPath().get("data.totalAmountDue"), col(header, "totalAmountDue"));
        assertMoney(row, "budgetBillingAmount", response.jsonPath().get("data.budgetBillingAmount"), col(header, "budgetBillingAmount"));
        assertMoney(row, "budgetBillingActualCharges", response.jsonPath().get("data.budgetBillingActualCharges"), col(header, "budgetBillingActualCharges"));
        assertMoney(row, "budgetBillingTotalVariance", response.jsonPath().get("data.budgetBillingTotalVariance"), col(header, "budgetBillingTotalVariance"));
        assertMoney(row, "guaranteedBillAmount", response.jsonPath().get("data.guaranteedBillAmount"), col(header, "guaranteedBillAmount"));

        List<Map<String, Object>> apiLines = response.jsonPath().getList("data.billLineItems");
        List<Map<String, Object>> dbLines = new ArrayList<>();
        for (Map<String, Object> dbRow : dbRows) {
            if (col(dbRow, "code") != null) {
                dbLines.add(dbRow);
            }
        }
        int apiSize = apiLines == null ? 0 : apiLines.size();
        if (apiSize != dbLines.size()) {
            throw new AssertionError(row + " billLineItems size api=" + apiSize + " db=" + dbLines.size());
        }
        for (int i = 0; i < dbLines.size(); i++) {
            Map<String, Object> apiLine = apiLines.get(i);
            Map<String, Object> dbLine = dbLines.get(i);
            String line = row + " line[" + i + "]";
            assertText(line, "code", apiLine.get("code"), col(dbLine, "code"));
            assertText(line, "description", apiLine.get("description"), col(dbLine, "description"));
            assertText(line, "category", apiLine.get("category"), col(dbLine, "category"));
            assertMoney(line, "amount", apiLine.get("amount"), col(dbLine, "amount"));
        }
    }

    private static Object col(Map<String, Object> row, String name) {
        if (row == null || name == null) {
            return null;
        }
        if (row.containsKey(name)) {
            return row.get(name);
        }
        String upper = name.toUpperCase();
        if (row.containsKey(upper)) {
            return row.get(upper);
        }
        for (Map.Entry<String, Object> entry : row.entrySet()) {
            if (entry.getKey() != null && entry.getKey().equalsIgnoreCase(name)) {
                return entry.getValue();
            }
        }
        return null;
    }

    private static String textOrNull(Object value) {
        if (value == null) {
            return null;
        }
        String text = String.valueOf(value).trim();
        if (text.isEmpty() || "null".equalsIgnoreCase(text)) {
            return null;
        }
        return text;
    }

    private static void assertText(String row, String field, Object api, Object db) {
        String apiText = textOrNull(api);
        String dbText = textOrNull(db);
        if (apiText == null && dbText == null) {
            return;
        }
        if (apiText == null || !apiText.equals(dbText)) {
            throw new AssertionError(row + " " + field + " api=" + api + " db=" + db);
        }
    }

    private static void assertMoney(String row, String field, Object api, Object db) {
        if (textOrNull(api) == null && textOrNull(db) == null) {
            return;
        }
        if (textOrNull(api) == null || textOrNull(db) == null) {
            throw new AssertionError(row + " " + field + " api=" + api + " db=" + db);
        }
        BigDecimal difference = new BigDecimal(String.valueOf(api)).subtract(new BigDecimal(String.valueOf(db))).abs();
        if (difference.compareTo(new BigDecimal("0.01")) > 0) {
            throw new AssertionError(row + " " + field + " api=" + api + " db=" + db);
        }
    }
}