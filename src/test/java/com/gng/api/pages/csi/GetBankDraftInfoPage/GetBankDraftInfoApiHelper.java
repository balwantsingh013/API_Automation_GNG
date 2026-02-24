package com.gng.api.pages.csi.GetBankDraftInfoPage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.GetBankDraftInfo.GetBankDraftInfoRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.GetBankDraftInfo.GetBankDraftInfoLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class GetBankDraftInfoApiHelper {

    private final TestContext testContext;

    public GetBankDraftInfoApiHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    GetBankDraftInfoRequest preparePayload(GetBankDraftInfoLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);

        String jsonFileName = apiLabel.equals(GetBankDraftInfoLabel.get_bank_draft_info)
                ? GetBankDraftInfoLabel.get_bank_draft_info.toString()
                : GetBankDraftInfoLabel.get_bank_draft_info_mandatory.toString();

        return BasePage.deserializeJsonToPojo(jsonFileName, GetBankDraftInfoRequest.class);
    }

    public void preparePayloadForTestCondition(GetBankDraftInfoRequest payload,
                                               GetBankDraftInfoLabel testCondition) {

        Map<String, Object> accountInfo = null;

        switch (testCondition) {

            // ---------------- NEGATIVE TEST CASES ----------------

            case TC1__Negative__Missing_Request_ID:
                payload.setRequestID("");
                break;

            case TC_2__Negative__Invalid_Request_ID_Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(33));
                break;

            case TC3__Negative__Duplicate_Request_ID:
                payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());
                break;

            case TC_4__Negative__Missing_CustomerCode:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode("");
                break;

            case TC_5__Negative__Invalid_CustomerCode_Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateDigits(10));
                break;

            case TC_6__Negative__Invalid_CustomerCode_Format:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateAlphanumeric(6));
                break;

            case TC_7__Negative__Missing_PremisesCode:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPremisesCode("");
                break;

            case TC_8__Negative__Invalid_PremisesCode_Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(8));
                break;

            case TC_9__Negative__Invalid_PremisesCode_Format:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPremisesCode(FakerDataGenerator.generateAlphanumeric(6));
                break;

            case TC10__Negative__Invalid_Account_Number:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(7));

                payload.setCustomerCode("9988776");
                accountInfo = ApplicationContext.get().getDbAction().getUserAccountInfo("9988776");
                accountInfo = ApplicationContext.get().getDbAction().getCustPremCodeRSActivePastDueRewards();
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;


            // ---------------- POSITIVE TEST CASES ----------------

            case TC_11__Positive__No_BankDraft_Info:
                accountInfo = ApplicationContext.get().getDbAction().getAccountWithoutBankDraft();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("customer_code").toString());
                payload.setPremisesCode(accountInfo.get("premises_code").toString());
                break;

            case TC_12__Positive__BankDraftStatus_Active:
                accountInfo = ApplicationContext.get().getDbAction().getActiveBankDraftAccount();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("customer_code").toString());
                payload.setPremisesCode(accountInfo.get("premises_code").toString());
                break;

            case TC_13__Positive__BankDraftStatus_PreNotification:
                accountInfo = ApplicationContext.get().getDbAction().getPreNotificationBankDraftAccount();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("customer_code").toString());
                payload.setPremisesCode(accountInfo.get("premises_code").toString());
                break;

            case TC_14__Positive__BankDraftStatus_Canceled:
                accountInfo = ApplicationContext.get().getDbAction().getCanceledBankDraftAccount();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("customer_code").toString());
                payload.setPremisesCode(accountInfo.get("premises_code").toString());
                break;

            case TC_15__Positive__BankDraftStatus_Inactive:
                accountInfo = ApplicationContext.get().getDbAction().getInactiveBankDraftAccount();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("customer_code").toString());
                payload.setPremisesCode(accountInfo.get("premises_code").toString());
                break;

            case TC_16__Positive__BankRoutingNumber:
                accountInfo = ApplicationContext.get().getDbAction().getBankDraftWithRoutingNumber();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("customer_code").toString());
                payload.setPremisesCode(accountInfo.get("premises_code").toString());
                break;

            case TC_17__Positive__BankAccountNumber:
                accountInfo = ApplicationContext.get().getDbAction().getBankDraftWithAccountNumber();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("customer_code").toString());
                payload.setPremisesCode(accountInfo.get("premises_code").toString());
                String custCode= accountInfo.get("customer_code").toString();

                String premCode= accountInfo.get("premises_code").toString();
                accountInfo = ApplicationContext.get().getDbAction().updateAccountNo(custCode, premCode, "12345678");
                testContext.setBankAccountNo("****5678");
                break;

            case TC_18__Positive__BankAccountType_Checking:
                accountInfo = ApplicationContext.get().getDbAction().getBankDraftCheckingAccount();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("customer_code").toString());
                payload.setPremisesCode(accountInfo.get("premises_code").toString());
                break;

            case TC_19__Positive__BankAccountType_Savings:
                accountInfo = ApplicationContext.get().getDbAction().getBankDraftSavingsAccount();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("customer_code").toString());
                payload.setPremisesCode(accountInfo.get("premises_code").toString());
                break;

            case TC_20__Positive__BankName:
                accountInfo = ApplicationContext.get().getDbAction().getBankDraftWithBankName();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountInfo.get("customer_code").toString());
                payload.setPremisesCode(accountInfo.get("premises_code").toString());
                break;

            default:
                log.warn("Unhandled test condition: {}", testCondition);
                break;
        }
    }
}
