package com.gng.api.pages.turnOff.AccountsApiPages.SearchAccounts;

import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.AccountsPojo.SearchAccounts.SearchAccountsRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.turnOff.AccountsApiSteps.SearchAccounts.SearchAccountsTOffApiLabel;
import com.gng.api.steps.turnOn.AccountsApiSteps.SearchAccounts.SearchAccountsApiLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class SearchAccountsHelper {

    private final TestContext testContext;
    public String premisesCode;
    public String customerCode;
    public String lastName;
    public String zipCode;
    public String firstName;
    public String customerBusinessName;
    public static final String TRANS_TYPE_TOFF="TOFF";

    public SearchAccountsHelper(TestContext testContext) {
        this.testContext = testContext;
    }


    SearchAccountsRequest preparePayload(SearchAccountsApiLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        String jsonFileName = apiLabel.equals(SearchAccountsApiLabel.search_accounts)
                ? SearchAccountsApiLabel.search_accounts.toString()
                : SearchAccountsApiLabel.search_accounts_mandatory.toString();
        return BasePage.deserializeJsonToPojo(jsonFileName, SearchAccountsRequest.class);
    }

    public void  getCustomerDetialsFromDbAndPreparePayload(SearchAccountsRequest payload) {
        List<Map<String, Object>> accountDetailsCustCode = ApplicationContext.get().getDbAction().getActiveCustomerDetails();
        customerCode=accountDetailsCustCode.getFirst().get("UCRACCT_CUST_CODE").toString();
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setCustomerCode(customerCode);
        payload.setPremisesCode(FakerDataGenerator.generateDigits(7));
        payload.setTransactionType(TRANS_TYPE_TOFF);

    }

    public void  getDetailsFromDbAndPreparePayloadForRMOrCM_customer(SearchAccountsRequest payload, SearchAccountsTOffApiLabel accountType) {
       String account_Type=null;
        switch(accountType){
            case RESIDENTIAL_VALID_ACTIVE_ACCOUNT_TC75:
                account_Type="RS";
            case COMMERCIAL_VALID_ACTIVE_ACCOUNT_TC76:
                account_Type="CM";
        }
        Map<String, Object> accountDetailsCustCode = ApplicationContext.get().getDbAction().getAccountDetails_ForResidentialOrCommercialAccount(account_Type);
        customerCode=accountDetailsCustCode.get("UCRACCT_CUST_CODE").toString();
        premisesCode=accountDetailsCustCode.get("UCRACCT_PREM_CODE").toString();
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setCustomerCode(customerCode);
        payload.setPremisesCode(premisesCode);
        payload.setTransactionType(TRANS_TYPE_TOFF);

    }

    public void  getLastNameAndZipcodeFromDbAndPreparePayload(SearchAccountsRequest payload, SearchAccountsTOffApiLabel combinationType) {
        Map<String, Object> accountDetailsLastNameZipCode = ApplicationContext.get().getDbAction().getAccountDetails_LastNameZipCode();
        lastName=accountDetailsLastNameZipCode.get("UZBENRO_DSM_LAST_NAME").toString();
        zipCode=accountDetailsLastNameZipCode.get("UCRADDR_ZIP").toString();
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setCustomerLastName(lastName);
        payload.setTransactionType(TRANS_TYPE_TOFF);
        switch(combinationType){
            case INVALID_COMBINATION_OF_LASTNAME_ZIPCODE_TC77:
                payload.setPremisesZipCode(FakerDataGenerator.generateDigits(5));
            case VALID_COMBINATION_OF_LASTNAME_ZIPCODE_TC78:
                payload.setPremisesZipCode(zipCode);
        }
    }

    public void  getFirstNameLastNameAndZipcodeFromDbAndPreparePayload(SearchAccountsRequest payload, SearchAccountsTOffApiLabel accountType) {
        String account_Type=null;
        switch(accountType){
            case RESIDENTIAL_VALID_ACTIVE_ACCOUNT_TC79:
                account_Type="RS";
            case SENIOR_RESIDENTIAL_VALID_ACTIVE_ACCOUNT_TC80:
                account_Type="SR";
        }
        Map<String, Object> accountDetailsLastNameZipCode = ApplicationContext.get().getDbAction().getAccountDetails_ForResidentialOrSeniorResAccount(account_Type);
        firstName=accountDetailsLastNameZipCode.get("UZBENRO_DSM_FIRST_NAME").toString();
        lastName=accountDetailsLastNameZipCode.get("UZBENRO_DSM_LAST_NAME").toString();
        zipCode=accountDetailsLastNameZipCode.get("UCRADDR_ZIP").toString();
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setCustomerFirstName(firstName);
        payload.setCustomerLastName(lastName);
        payload.setPremisesZipCode(zipCode);
        payload.setTransactionType(TRANS_TYPE_TOFF);


    }

    public void  getCustomerBusinessNameFromDbAndPreparePayload(SearchAccountsRequest payload, SearchAccountsTOffApiLabel accountType) {
        Map<String, Object> accountDetailsLastNameZipCode=null;
        switch(accountType){
            case COMMERCIAL_VALID_ACTIVE_PASTDUEBALANCE_ACCOUNT_TC81:
                accountDetailsLastNameZipCode = ApplicationContext.get().getDbAction().getAccountDetails_ForPastDueBalanceCommercialAccount();
            case COMMERCIAL_VALID_ACTIVE_SONP_ACCOUNT_TC82:
                accountDetailsLastNameZipCode = ApplicationContext.get().getDbAction().getAccountDetails_ForSONPCommercialAccount();
        }
        customerBusinessName=accountDetailsLastNameZipCode.get("UCBCUST_LAST_NAME").toString();
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setTransactionType(TRANS_TYPE_TOFF);
        payload.setCustomerBusinessName(customerBusinessName);
    }

    public void  getCustomerBusinessNameFromDbAndPreparePayloadTC_83(SearchAccountsRequest payload) {
        Map<String, Object> accountDetailsCustomerBsnName = ApplicationContext.get().getDbAction().getCustomBusnsNm_ForActPenRewardCommercialAccount();
        customerBusinessName=accountDetailsCustomerBsnName.get("UCBCUST_LAST_NAME").toString();
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setTransactionType(TRANS_TYPE_TOFF);
        payload.setCustomerBusinessName(customerBusinessName);
    }

    public void  getCustomerPremisesCodeFromDbAndPreparePayloadTC_68(SearchAccountsRequest payload) {
        Map<String, Object> custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeCMInactiveMetered();
        customerCode=custPremCode.get("UCRACCT_CUST_CODE").toString();
        premisesCode=custPremCode.get("UCRACCT_PREM_CODE").toString();
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setTransactionType(TRANS_TYPE_TOFF);
        payload.setCustomerCode(customerCode);
        payload.setPremisesCode(premisesCode);
    }

    public void  getCustomerPremisesCodeFromDbAndPreparePayloadTC_69(SearchAccountsRequest payload) {
        Map<String, Object> custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeCMInactiveBadDebt();
        customerCode=custPremCode.get("UABBDBT_CUST_CODE").toString();
        premisesCode=custPremCode.get("UABBDBT_PREM_CODE").toString();
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setTransactionType(TRANS_TYPE_TOFF);
        payload.setCustomerCode(customerCode);
        payload.setPremisesCode(premisesCode);
    }

    public void  getCustomerPremisesCodeFromDbAndPreparePayloadTC_70(SearchAccountsRequest payload) {
        Map<String, Object> custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeCMInactiveSONP();
        customerCode=custPremCode.get("UABBDBT_CUST_CODE").toString();
        premisesCode=custPremCode.get("UABBDBT_PREM_CODE").toString();
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setTransactionType(TRANS_TYPE_TOFF);
        payload.setCustomerCode(customerCode);
        payload.setPremisesCode(premisesCode);
    }

    public void  getCustomerPremisesCodeFromDbAndPreparePayloadTC_71(SearchAccountsRequest payload) {
        Map<String, Object> custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeCMInactiveNonMetered();
        customerCode=custPremCode.get("UCRACCT_CUST_CODE").toString();
        premisesCode=custPremCode.get("UCRACCT_PREM_CODE").toString();
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setTransactionType(TRANS_TYPE_TOFF);
        payload.setCustomerCode(customerCode);
        payload.setPremisesCode(premisesCode);
    }

    public void  getCustomerPremisesCodeFromDbAndPreparePayloadTC_72(SearchAccountsRequest payload) {
        Map<String, Object> custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeCMNewBakrupcy();
        customerCode=custPremCode.get("UCRACCT_CUST_CODE").toString();
        premisesCode=custPremCode.get("UCRACCT_PREM_CODE").toString();
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setTransactionType(TRANS_TYPE_TOFF);
        payload.setCustomerCode(customerCode);
        payload.setPremisesCode(premisesCode);
    }

    public void  getCustomerPremisesCodeFromDbAndPreparePayloadTC_73(SearchAccountsRequest payload) {
        Map<String, Object> custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeCMInactiveBakrupcy();
        customerCode=custPremCode.get("UCRACCT_CUST_CODE").toString();
        premisesCode=custPremCode.get("UCRACCT_PREM_CODE").toString();
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setTransactionType(TRANS_TYPE_TOFF);
        payload.setCustomerCode(customerCode);
        payload.setPremisesCode(premisesCode);
    }

    public void getCustomerPremisesCodeFromDbAndPreparePayload(SearchAccountsRequest payload,SearchAccountsTOffApiLabel testCondition) {
        Map<String, Object> custPremCode=null;
        switch (testCondition) {
            case ACTIVE_RS_NEW_NON_METERED_TC_56:
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeRSActiveNonMeteredAccount();
                customerCode=custPremCode.get("UCRACCT_CUST_CODE").toString();
                premisesCode=custPremCode.get("UCRACCT_PREM_CODE").toString();
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setTransactionType(TRANS_TYPE_TOFF);
                payload.setCustomerCode(customerCode);
                payload.setPremisesCode(premisesCode);
                break;

            case ACTIVE_MSB_ACCOUNT_TC_57:
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeMSBAccount();
                customerCode=custPremCode.get("UCRACCT_CUST_CODE").toString();
                premisesCode=custPremCode.get("UCRACCT_PREM_CODE").toString();
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setTransactionType(TRANS_TYPE_TOFF);
                payload.setCustomerCode(customerCode);
                payload.setPremisesCode(premisesCode);
                break;

            case ACTIVE_RS_NEW_NON_METERED_TC_58:
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeActiveNonMeteredAccount();
                customerCode=custPremCode.get("UCRACCT_CUST_CODE").toString();
                premisesCode=custPremCode.get("UCRACCT_PREM_CODE").toString();
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setTransactionType(TRANS_TYPE_TOFF);
                payload.setCustomerCode(customerCode);
                payload.setPremisesCode(premisesCode);
                break;

            case INACTIVE_RS_METERED_TC_59:
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeInactiveMeteredAccount();
                customerCode=custPremCode.get("UCRACCT_CUST_CODE").toString();
                premisesCode=custPremCode.get("UCRACCT_PREM_CODE").toString();
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setTransactionType(TRANS_TYPE_TOFF);
                payload.setCustomerCode(customerCode);
                payload.setPremisesCode(premisesCode);
                break;

            case INACTIVE_BAD_DEBT_BALANCE_TC_60:
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeInactiveAccWithBadDebt();
                customerCode=custPremCode.get("UABOPEN_CUST_CODE").toString();
                premisesCode=custPremCode.get("UABOPEN_PREM_CODE").toString();
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setTransactionType(TRANS_TYPE_TOFF);
                payload.setCustomerCode(customerCode);
                payload.setPremisesCode(premisesCode);
                break;

            case RS_INACTIVE_WITH_SONP_TC_61:
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeInactiveAccWithSONP();
                customerCode=custPremCode.get("UCRACCT_CUST_CODE").toString();
                premisesCode=custPremCode.get("UCRACCT_PREM_CODE").toString();
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setTransactionType(TRANS_TYPE_TOFF);
                payload.setCustomerCode(customerCode);
                payload.setPremisesCode(premisesCode);
                break;

            case RS_INACTIVE_NON_METER_TC_62:
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeInactiveNonMeteredAccount();
                customerCode=custPremCode.get("UCRACCT_CUST_CODE").toString();
                premisesCode=custPremCode.get("UCRACCT_PREM_CODE").toString();
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setTransactionType(TRANS_TYPE_TOFF);
                payload.setCustomerCode(customerCode);
                payload.setPremisesCode(premisesCode);
                break;

            case RS_NEW_BANKRUPCY_TC_63:
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeResidentialNewBankrupcy();
                customerCode=custPremCode.get("UCRACCT_CUST_CODE").toString();
                premisesCode=custPremCode.get("UCRACCT_PREM_CODE").toString();
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setTransactionType(TRANS_TYPE_TOFF);
                payload.setCustomerCode(customerCode);
                payload.setPremisesCode(premisesCode);
                break;

            case RS_INACTIVE_BANKRUPCY_TC_64:
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeResidentialInactiveBankrupcy();
                customerCode=custPremCode.get("UCRACCT_CUST_CODE").toString();
                premisesCode=custPremCode.get("UCRACCT_PREM_CODE").toString();
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setTransactionType(TRANS_TYPE_TOFF);
                payload.setCustomerCode(customerCode);
                payload.setPremisesCode(premisesCode);
                break;

            case CM_NEW_NON_METERED_TC_65:
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeCMNewNonMetered();
                customerCode=custPremCode.get("UCRACCT_CUST_CODE").toString();
                premisesCode=custPremCode.get("UCRACCT_PREM_CODE").toString();
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setTransactionType(TRANS_TYPE_TOFF);
                payload.setCustomerCode(customerCode);
                payload.setPremisesCode(premisesCode);
                break;

            case CM_ACTIVE_NON_METERED_TC_67:
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeCMActiveNonMetered();
                customerCode=custPremCode.get("UCRACCT_CUST_CODE").toString();
                premisesCode=custPremCode.get("UCRACCT_PREM_CODE").toString();
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setTransactionType(TRANS_TYPE_TOFF);
                payload.setCustomerCode(customerCode);
                payload.setPremisesCode(premisesCode);
        }
    }




}
