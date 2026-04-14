package com.gng.api.pages.csi.GetBillingInfoPage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.GetBillingInfo.GetBillingInfoRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.GetBillingInfo.GetBillingInfoLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

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

            case TC_001__Negative__Missing_Request_ID:
                payload.setRequestID("");
                break;

            case TC_002__Negative__Invalid_Request_ID_Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(33));
                break;

            case TC_003__Negative__Duplicate_Request_ID:
                payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());
                break;

            case TC_004__Negative__Missing_customerCode:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode("");
                break;

            case TC_005__Negative__Invalid_customerCode_Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateDigits(10));
                break;

            case TC_006__Negative__Invalid_customerCode_Format:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateString(6));
                break;

            case TC_007__Negative__Missing_premisesCode:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPremisesCode("");
                break;

            case TC_008__Negative__Invalid_premisesCode_Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(8));
                break;

            case TC_009__Negative__Invalid_premisesCode_Format:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPremisesCode(FakerDataGenerator.generateString(6));
                break;

            case TC_010__Negative__Invalid_Account_Number__Invalid_Account__:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode("9988776");
                Map<String, Object> userInfo = ApplicationContext.get().getDbAction().getUserAccountInfo("9988776");
                payload.setCustomerCode("9988776");
                payload.setPremisesCode(FakerDataGenerator.generateDigits(6));
                break;

            case TC_11__Negative__Invalid_Account_Number__New_Account__:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getNewAccountOnly();
                payload.setCustomerCode(accountData.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountData.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_12__Negative__Invalid_Account_Number__Invalid_Customer_Account__:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getNonExistentUcbcustCustomerAndPremisesCode();
                payload.setCustomerCode(accountData.get("customer_code").toString());
                payload.setPremisesCode(accountData.get("premises_code").toString());
                break;

            case TC_13__Negative__Invalid_Account_Number__Invalid_Enrollment_Account__:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getNonExistentUzbenroCustomerAndPremisesCode();
                payload.setCustomerCode(accountData.get("customer_code").toString());
                payload.setPremisesCode(accountData.get("premises_code").toString());
                break;

            case TC_14__Negative__Invalid_Account_Number__Invalid_Pending_Enrollment_Account__:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getNonExistentGtbenroCustomerAndPremisesCode();
                payload.setCustomerCode(accountData.get("customer_code").toString());
                payload.setPremisesCode(accountData.get("premises_code").toString());
                break;


            // ---------------- POSITIVE TEST CASES ----------------
//
//            case TC_15__Positive__Valid_Account_with_No_Billing_History:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getAccountWithNoBillingHistory();
//                payload.setCustomerCode(accountData.get("customer_code").toString());
//                payload.setPremisesCode(accountData.get("premises_code").toString());
//                break;
//
//            case TC_16__Positive__Valid_Account_with_More_Than_1_Month_Billing_History:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getAccountWithMultipleMonthsBillingHistory();
//                payload.setCustomerCode(accountData.get("customer_code").toString());
//                payload.setPremisesCode(accountData.get("premises_code").toString());
//                break;
//
//            case TC_17__Positive__Billing_Info_Bill_Date_Format:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getAccountWithBillingHistory();
//                payload.setCustomerCode(accountData.get("customer_code").toString());
//                payload.setPremisesCode(accountData.get("premises_code").toString());
//                break;
//
//            case TC_18__Positive__Billing_Info_Bill_From_Date_Format:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getAccountWithBillingHistory();
//                payload.setCustomerCode(accountData.get("customer_code").toString());
//                payload.setPremisesCode(accountData.get("premises_code").toString());
//                break;
//
//            case TC_19__Positive__Billing_Info_Bill_To_Date_Format:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getAccountWithBillingHistory();
//                payload.setCustomerCode(accountData.get("customer_code").toString());
//                payload.setPremisesCode(accountData.get("premises_code").toString());
//                break;
//
//            case TC_20__Positive__Billing_Info_Past_Due_Amount_Format__Past_Due_Amount_Greater_Than_0__:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getAccountWithPastDueAmountGreaterThanZero();
//                payload.setCustomerCode(accountData.get("customer_code").toString());
//                payload.setPremisesCode(accountData.get("premises_code").toString());
//                break;
//
//            case TC_21__Positive__Billing_Info_Past_Due_Amount_Format__Past_Due_Amount_Equals_0__:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getAccountWithNoPastDueAmount();
//                payload.setCustomerCode(accountData.get("customer_code").toString());
//                payload.setPremisesCode(accountData.get("premises_code").toString());
//                break;
//
//            case TC_22__Positive__Billing_Info_Past_Due_Date_Format:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getAccountWithBillingHistory();
//                payload.setCustomerCode(accountData.get("customer_code").toString());
//                payload.setPremisesCode(accountData.get("premises_code").toString());
//                break;
//
//            case TC_23__Positive__Billing_Info_Previous_Bill_Amount_Format__Previous_Bill_Amount_Greater_Than_0__:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getAccountWithPreviousBillAmountGreaterThanZero();
//                payload.setCustomerCode(accountData.get("customer_code").toString());
//                payload.setPremisesCode(accountData.get("premises_code").toString());
//                break;
//
//            case TC_24__Positive__Billing_Info_Previous_Bill_Amount_Format__Previous_Bill_Amount_Equals_0__:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getAccountWithNoPreviousBillAmount();
//                payload.setCustomerCode(accountData.get("customer_code").toString());
//                payload.setPremisesCode(accountData.get("premises_code").toString());
//                break;
//
//            case TC_25__Positive__Billing_Info_Payments_Applied_Amount_Format__Payments_Applied_Amount_Greater_Than_0__:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getAccountWithPaymentsAppliedGreaterThanZero();
//                payload.setCustomerCode(accountData.get("customer_code").toString());
//                payload.setPremisesCode(accountData.get("premises_code").toString());
//                break;
//
//            case TC_26__Positive__Billing_Info_Payments_Applied_Amount_Format__Payments_Applied_Amount_Equals_0__:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getAccountWithNoPaymentsApplied();
//                payload.setCustomerCode(accountData.get("customer_code").toString());
//                payload.setPremisesCode(accountData.get("premises_code").toString());
//                break;
//
//            case TC_27__Positive__Balance_Brought_Forward_Greater_Than_0_Format:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getAccountWithBalanceBroughtForwardGreaterThanZero();
//                payload.setCustomerCode(accountData.get("customer_code").toString());
//                payload.setPremisesCode(accountData.get("premises_code").toString());
//                break;
//
//            case TC_28__Positive__Balance_Brought_Forward_Equals_0_Format:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getAccountWithZeroBalanceBroughtForward();
//                payload.setCustomerCode(accountData.get("customer_code").toString());
//                payload.setPremisesCode(accountData.get("premises_code").toString());
//                break;
//
//            case TC_29__Positive__Balance_Brought_Forward_Less_Than_0_Format:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getAccountWithBalanceBroughtForwardLessThanZero();
//                payload.setCustomerCode(accountData.get("customer_code").toString());
//                payload.setPremisesCode(accountData.get("premises_code").toString());
//                break;
//
//            case TC_30__Positive__Current_Charges_Format:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getAccountWithCurrentCharges();
//                payload.setCustomerCode(accountData.get("customer_code").toString());
//                payload.setPremisesCode(accountData.get("premises_code").toString());
//                break;
//
//            case TC_31__Positive__Total_Amount_Due_Format:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getAccountWithTotalAmountDue();
//                payload.setCustomerCode(accountData.get("customer_code").toString());
//                payload.setPremisesCode(accountData.get("premises_code").toString());
//                break;
//
//            case TC_32__Positive__Bill_Due_Date_Format:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getAccountWithBillingHistory();
//                payload.setCustomerCode(accountData.get("customer_code").toString());
//                payload.setPremisesCode(accountData.get("premises_code").toString());
//                break;
//
//            case TC_33__Positive__Payments_Since_Last_Bill_Greater_Than_0_Format:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getAccountWithPaymentsSinceLastBillGreaterThanZero();
//                payload.setCustomerCode(accountData.get("customer_code").toString());
//                payload.setPremisesCode(accountData.get("premises_code").toString());
//                break;
//
//            case TC_34__Positive__Payments_Since_Last_Bill_Equals_0_Format:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getAccountWithNoPaymentsSinceLastBill();
//                payload.setCustomerCode(accountData.get("customer_code").toString());
//                payload.setPremisesCode(accountData.get("premises_code").toString());
//                break;
//
//            case TC_35__Positive__Current_Balance_Format:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getAccountWithCurrentBalance();
//                payload.setCustomerCode(accountData.get("customer_code").toString());
//                payload.setPremisesCode(accountData.get("premises_code").toString());
//                break;
//
//            case TC_36__Positive__Budget_Billing_Amount_not_Budget_Billing_Account_Format:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getAccountNotOnBudgetBillingPlan();
//                payload.setCustomerCode(accountData.get("customer_code").toString());
//                payload.setPremisesCode(accountData.get("premises_code").toString());
//                break;
//
//            case TC_37__Positive__Budget_Billing_TotalVariance_not_Budget_Billing_Account_Format:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getAccountNotOnBudgetBillingPlan();
//                payload.setCustomerCode(accountData.get("customer_code").toString());
//                payload.setPremisesCode(accountData.get("premises_code").toString());
//                break;
//
//            case TC_38__Positive__Budget_Billing_Start_Date_not_Budget_Billing_Account_Format:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getAccountNotOnBudgetBillingPlan();
//                payload.setCustomerCode(accountData.get("customer_code").toString());
//                payload.setPremisesCode(accountData.get("premises_code").toString());
//                break;
//
//            case TC_39__Positive__Budget_Billing_Amount_Budget_Billing_Account_Format:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getAccountOnBudgetBillingPlan();
//                payload.setCustomerCode(accountData.get("customer_code").toString());
//                payload.setPremisesCode(accountData.get("premises_code").toString());
//                break;
//
//            case TC_40__Positive__Budget_Billing_TotalVariance_Budget_Billing_Account_Format:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getAccountOnBudgetBillingPlan();
//                payload.setCustomerCode(accountData.get("customer_code").toString());
//                payload.setPremisesCode(accountData.get("premises_code").toString());
//                break;
//
//            case TC_41__Positive__Budget_Billing_Start_Date_Budget_Billing_Account_Format:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getAccountOnBudgetBillingPlan();
//                payload.setCustomerCode(accountData.get("customer_code").toString());
//                payload.setPremisesCode(accountData.get("premises_code").toString());
//                break;
//
//            case TC_42__Positive__No_Bill_Line_Items:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getAccountWithNoBillLineItems();
//                payload.setCustomerCode(accountData.get("customer_code").toString());
//                payload.setPremisesCode(accountData.get("premises_code").toString());
//                break;
//
//            case TC_43__Positive__Bill_Line_Items_Code_Format:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getAccountWithBillLineItems();
//                payload.setCustomerCode(accountData.get("customer_code").toString());
//                payload.setPremisesCode(accountData.get("premises_code").toString());
//                break;
//
//            case TC_44__Positive__Bill_Line_Items_Description_Format:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getAccountWithBillLineItems();
//                payload.setCustomerCode(accountData.get("customer_code").toString());
//                payload.setPremisesCode(accountData.get("premises_code").toString());
//                break;
//
//            case TC_45__Positive__Bill_Line_Items_Amount_Format:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getAccountWithBillLineItems();
//                payload.setCustomerCode(accountData.get("customer_code").toString());
//                payload.setPremisesCode(accountData.get("premises_code").toString());
//                break;
//
//            case TC_46__Positive__Bill_Line_Items_Category_Format:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getAccountWithBillLineItems();
//                payload.setCustomerCode(accountData.get("customer_code").toString());
//                payload.setPremisesCode(accountData.get("premises_code").toString());
//                break;
//
//            case TC_47__Positive__Bill_Line_Items_Reward_Order:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getAccountWithRewardLineItems();
//                payload.setCustomerCode(accountData.get("customer_code").toString());
//                payload.setPremisesCode(accountData.get("premises_code").toString());
//                break;
//
//            case TC_48__Positive__Bill_Line_Items_Base_Charge_Order:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getAccountWithBaseChargeLineItems();
//                payload.setCustomerCode(accountData.get("customer_code").toString());
//                payload.setPremisesCode(accountData.get("premises_code").toString());
//                break;
//
//            case TC_49__Positive__Bill_Line_Items_Customer_Service_Charge_Order:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getAccountWithCustomerServiceChargeLineItems();
//                payload.setCustomerCode(accountData.get("customer_code").toString());
//                payload.setPremisesCode(accountData.get("premises_code").toString());
//                break;
//
//            case TC_50__Positive__Bill_Line_Items_Natural_Gas_Charge_Order:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getAccountWithNaturalGasChargeLineItems();
//                payload.setCustomerCode(accountData.get("customer_code").toString());
//                payload.setPremisesCode(accountData.get("premises_code").toString());
//                break;
//
//            case TC_51__Positive__Bill_Line_Items_Bill_Guarantee_Order:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getAccountWithBillGuaranteeLineItems();
//                payload.setCustomerCode(accountData.get("customer_code").toString());
//                payload.setPremisesCode(accountData.get("premises_code").toString());
//                break;
//
//            case TC_52__Positive__Bill_Line_Items_Interstate_Pipeline_Capacity_Charge_Order:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getAccountWithInterstatePipelineCapacityLineItems();
//                payload.setCustomerCode(accountData.get("customer_code").toString());
//                payload.setPremisesCode(accountData.get("premises_code").toString());
//                break;
//
//            case TC_53__Positive__Bill_Line_Items_Promotional_Discount_Charge_Order:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getAccountWithPromotionalDiscountLineItems();
//                payload.setCustomerCode(accountData.get("customer_code").toString());
//                payload.setPremisesCode(accountData.get("premises_code").toString());
//                break;
//
//            case TC_54__Positive__Bill_Line_Items_Miscellaneous_Charge_Order:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getAccountWithMiscellaneousChargeLineItems();
//                payload.setCustomerCode(accountData.get("customer_code").toString());
//                payload.setPremisesCode(accountData.get("premises_code").toString());
//                break;
//
//            case TC_55__Positive__Bill_Line_Items_Miscellaneous_Credit_Order:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getAccountWithMiscellaneousCreditLineItems();
//                payload.setCustomerCode(accountData.get("customer_code").toString());
//                payload.setPremisesCode(accountData.get("premises_code").toString());
//                break;
//
//            case TC_56__Positive__Bill_Line_Items_Taxes_Order:
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                accountData = ApplicationContext.get().getDbAction("banner").getAccountWithTaxLineItems();
//                payload.setCustomerCode(accountData.get("customer_code").toString());
//                payload.setPremisesCode(accountData.get("premises_code").toString());
//                break;

            default:
                log.warn("Unhandled test condition: {}", testCondition);
                break;
        }
    }
}