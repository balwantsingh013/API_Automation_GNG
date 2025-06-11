package com.gng.api.pages.turnOff.AccountsApiPages.SearchAccounts;

import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.AccountsPojo.SearchAccounts.SearchAccountsRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.turnOff.AccountsApiSteps.SearchAccounts.SearchAccountsApiLabel;
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

    public static final String USERNAME = "autotester";
    public static final String LOGINID_SYS = "sys";
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

    public void  getDetailsFromDbAndPreparePayloadForRMOrCM_customer(SearchAccountsRequest payload, SearchAccountsApiLabel requestID) {
       String accountType=null;
        switch(requestID){
            case RESIDENTIAL_ACTIVE_ACCOUNT_TC75:
                accountType="RS";
            case COMMERCIAL_ACTIVE_ACCOUNT_TC76:
                accountType="CM";
        }
        Map<String, Object> accountDetailsCustCode = ApplicationContext.get().getDbAction().getAccountDetails_ForResidentialOrCommercialAccount(accountType);
        customerCode=accountDetailsCustCode.get("UCRACCT_CUST_CODE").toString();
        premisesCode=accountDetailsCustCode.get("UCRACCT_PREM_CODE").toString();
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setCustomerCode(customerCode);
        payload.setPremisesCode(premisesCode);
        payload.setTransactionType(TRANS_TYPE_TOFF);

    }

    public void  getLastNameAndZipcodeFromDbAndPreparePayload(SearchAccountsRequest payload, SearchAccountsApiLabel request_id) {
        Map<String, Object> accountDetailsLastNameZipCode = ApplicationContext.get().getDbAction().getAccountDetails_LastNameZipCode();
        lastName=accountDetailsLastNameZipCode.get("UZBENRO_DSM_LAST_NAME").toString();
        zipCode=accountDetailsLastNameZipCode.get("UCRADDR_ZIP").toString();
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setCustomerLastName(lastName);
        payload.setTransactionType(TRANS_TYPE_TOFF);
        switch(request_id){
            case INVALID_COMBINATION_TC77:
                payload.setPremisesZipCode(FakerDataGenerator.generateDigits(5));
            case VALID_COMBINATION_TC78:
                payload.setPremisesZipCode(zipCode);
        }
    }

    public void  getFirstNameLastNameAndZipcodeFromDbAndPreparePayload(SearchAccountsRequest payload, SearchAccountsApiLabel request_id) {
        String accountType=null;
        switch(request_id){
            case RESIDENTIAL_ACCOUNT_TC79:
                accountType="RS";
            case SENIOR_RESIDENTIAL_ACCOUNT_TC80:
                accountType="SR";
        }
        Map<String, Object> accountDetailsLastNameZipCode = ApplicationContext.get().getDbAction().getAccountDetails_ForResidentialOrSeniorResAccount(accountType);
        firstName=accountDetailsLastNameZipCode.get("UZBENRO_DSM_FIRST_NAME").toString();
        lastName=accountDetailsLastNameZipCode.get("UZBENRO_DSM_LAST_NAME").toString();
        zipCode=accountDetailsLastNameZipCode.get("UCRADDR_ZIP").toString();
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setCustomerFirstName(firstName);
        payload.setCustomerLastName(lastName);
        payload.setPremisesZipCode(zipCode);
        payload.setTransactionType(TRANS_TYPE_TOFF);


    }

    public void  getCustomerBusinessNameFromDbAndPreparePayload(SearchAccountsRequest payload, SearchAccountsApiLabel request_id) {
        Map<String, Object> accountDetailsLastNameZipCode=null;
        switch(request_id){
            case PASTDUEBALANCE_ACCOUNT_TC81:
                accountDetailsLastNameZipCode = ApplicationContext.get().getDbAction().getAccountDetails_ForPastDueBalanceCommercialAccount();
            case SONP_ACCOUNT_TC82:
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





}
