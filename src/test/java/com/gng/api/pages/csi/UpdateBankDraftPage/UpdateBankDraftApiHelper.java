package com.gng.api.pages.csi.UpdateBankDraftPage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.UpdateBankDraft.UpdateBankDraftRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.UpdateBankDraft.UpdateBankDraftLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class UpdateBankDraftApiHelper {

    private final TestContext testContext;

    public UpdateBankDraftApiHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    UpdateBankDraftRequest preparePayload(UpdateBankDraftLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);

        String jsonFileName = apiLabel.equals(UpdateBankDraftLabel.update_bank_draft)
                ? UpdateBankDraftLabel.update_bank_draft.toString()
                : UpdateBankDraftLabel.update_bank_draft_mandatory.toString();

        return BasePage.deserializeJsonToPojo(jsonFileName, UpdateBankDraftRequest.class);
    }

    public void preparePayloadForTestCondition(UpdateBankDraftRequest payload,
                                               UpdateBankDraftLabel testCondition) {

        Map<String, Object> accountInfo = null;

        switch (testCondition) {

            // ---------------- NEGATIVE TEST CASES ----------------

            case TC_50__Negative__Missing_Request_ID:
                payload.setRequestID("");
                break;

            case TC_51__Negative__Invalid_Request_ID_Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(33));
                break;

            case TC_52__Negative__Duplicate_Request_ID:
                payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());
                break;

            case TC_53__Negative__Missing_customerCode:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode("");
                break;

            case TC_54__Negative__Invalid_customerCode_Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateDigits(10));
                break;

            case TC_55__Negative__Invalid_customerCode_Format:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateAlphanumeric(6));
                break;

            case TC_56__Negative__Missing_premisesCode:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPremisesCode("");
                break;

            case TC_57__Negative__Invalid_premisesCode_Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(8));
                break;

            case TC_58__Negative__Invalid_premisesCode_Format:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPremisesCode(FakerDataGenerator.generateAlphanumeric(6));
                break;

            case TC_59__Negative__Invalid_Account_Number:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(7));

                payload.setCustomerCode("9988776");
                accountInfo = ApplicationContext.get().getDbAction().getUserAccountInfo("9988776");
                accountInfo = ApplicationContext.get().getDbAction().getCustPremCodeRSActivePastDueRewards();
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_60__Negative__Missing_Bank_Account_Type:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setBankDraftAccountType("");
                break;

            case TC_61__Negative__Invalid_Bank_Account_Type_Format:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setBankDraftAccountType("X");
                break;

            case TC_62__Negative__Missing_Routing_Number:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setBankDraftRoutingNumber("");
                break;

            case TC_63__Negative__Invalid_Routing_Number_Format:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setBankDraftRoutingNumber("ABC123XYZ");
                break;

            case TC_64__Negative__Invalid_Routing_Number_Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setBankDraftRoutingNumber(FakerDataGenerator.generateDigits(10));
                break;

            case TC_65__Negative__Invalid_Routing_Number:
                accountInfo = ApplicationContext.get().getDbAction().getActiveBankDraftAccount();
                payload.setCustomerCode(accountInfo.get("customer_code").toString());
                payload.setPremisesCode(accountInfo.get("premises_code").toString());
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setBankDraftRoutingNumber("999999999");
                accountInfo = ApplicationContext.get().getDbAction().getUserAccountWithRoutingNo("999999999");
                break;

            case TC_66__Negative__Missing_Bank_Account_Number:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setBankDraftAccountNumber("");
                break;

            case TC_67__Negative__Invalid_Bank_Account_Number_Format:
                accountInfo = ApplicationContext.get().getDbAction().getActiveBankDraftAccount();
                String customerCode=accountInfo.get("customer_code").toString();
                payload.setCustomerCode(customerCode);
                String premisesCode=accountInfo.get("premises_code").toString();
                payload.setPremisesCode(premisesCode);
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setBankDraftAccountNumber("ABC123XYZ");
                accountInfo = ApplicationContext.get().getDbAction().getRoutingNo(customerCode, premisesCode);
                payload.setBankDraftRoutingNumber(accountInfo.get("ROUTING_NUMBER").toString());
                break;

            case TC_68__Negative__Invalid_Bank_Account_Number_Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setBankDraftAccountNumber(FakerDataGenerator.generateDigits(70));
                break;

            case TC_69__Negative__No_Active_ABD_Configured:
                accountInfo = ApplicationContext.get().getDbAction().getAccountWithoutBankDraft();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("customer_code").toString());
                payload.setPremisesCode(accountInfo.get("premises_code").toString());
                break;

            case TC_70__Negative__Invalid_Service_Number:
                accountInfo = ApplicationContext.get().getDbAction().getActiveBankDraftAccountNoService();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("customer_code").toString());
                payload.setPremisesCode(accountInfo.get("premises_code").toString());
                break;

            case TC_71__Negative__Active_Payment_Arrangement:
                accountInfo = ApplicationContext.get().getDbAction().getAccountWithActivePaymentArrangement();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("customer_code").toString());
                payload.setPremisesCode(accountInfo.get("premises_code").toString());
                payload.setBankDraftRoutingNumber(accountInfo.get("BANKDRAFTROUTINGNUMBER").toString());
                break;

            case TC_72__Negative__Account_Ineligible_Future_Payment:
//                accountInfo = ApplicationContext.get().getDbAction().getAccountWithFuturePaymentArrangement();
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                payload.setCustomerCode(accountInfo.get("customer_code").toString());
//                payload.setPremisesCode(accountInfo.get("premises_code").toString());
                break;

            case TC_73__Negative__No_Update_Required:
                accountInfo = ApplicationContext.get().getDbAction().getAccountWithActiveNoPaymentArrangement();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("customer_code").toString());
                payload.setPremisesCode(accountInfo.get("premises_code").toString());
                payload.setBankDraftRoutingNumber(accountInfo.get("BANKDRAFTROUTINGNUMBER").toString());
                break;


            // ---------------- POSITIVE TEST CASES ----------------

            case TC_74__Positive__Active_Checking_Account_Updated:
                accountInfo = ApplicationContext.get().getDbAction().getBankDraftCheckingAccount2();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("customer_code").toString());
                payload.setPremisesCode(accountInfo.get("premises_code").toString());
                payload.setBankDraftAccountType("C");
                payload.setBankDraftRoutingNumber("123456789");
                break;

            case TC_75__Positive__Active_Savings_Account_Updated:
                accountInfo = ApplicationContext.get().getDbAction().getBankDraftSavingsAccount2();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("customer_code").toString());
                payload.setPremisesCode(accountInfo.get("premises_code").toString());
                payload.setBankDraftAccountType("S");
                payload.setBankDraftRoutingNumber("123456789");
                break;

            case TC_76__Positive__Prenotification_Checking_Updated:
                accountInfo = ApplicationContext.get().getDbAction().getPreNotificationBankDraftAccount();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("customer_code").toString());
                payload.setPremisesCode(accountInfo.get("premises_code").toString());
                payload.setBankDraftRoutingNumber("123456789");
                payload.setBankDraftAccountType("P");
                break;

            case TC_77__Positive__Prenotification_Savings_Updated:
//                accountInfo = ApplicationContext.get().getDbAction().getPrenoteABDSavingsAccount();
//                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                payload.setCustomerCode(accountInfo.get("customer_code").toString());
//                payload.setPremisesCode(accountInfo.get("premises_code").toString());
                payload.setBankDraftAccountType("S");
                break;

            case TC_78__Positive__Login_ID_Saved:
                accountInfo = ApplicationContext.get().getDbAction().getAccountWithActiveNoPaymentArrangement();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("customer_code").toString());
                payload.setPremisesCode(accountInfo.get("premises_code").toString());
                payload.setLoginID("TESTLOGIN123");
                break;

            default:
                log.warn("Unhandled test condition: {}", testCondition);
                break;
        }
    }
}
