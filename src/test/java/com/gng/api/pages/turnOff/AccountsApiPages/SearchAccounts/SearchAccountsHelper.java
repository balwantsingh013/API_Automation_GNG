package com.gng.api.pages.turnOff.AccountsApiPages.SearchAccounts;

import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.AccountsPojo.SearchAccounts.SearchAccountsRequest;
import com.gng.api.pojo.TestContext.TestContext;
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

    public static final String USERNAME = "autotester";
    public static final String USERNAME_01 = "sys";
    public static final String TRANS_TYPE="TOFF";

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
        List<Map<String, Object>> AccountDetails_CustCode = ApplicationContext.get().getDbAction().getActiveCustomerDetails();
        String customerCode=AccountDetails_CustCode.getFirst().get("UCRACCT_CUST_CODE").toString();
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setLoginID(USERNAME_01);
        payload.setCustomerCode(customerCode);
        payload.setPremisesCode(FakerDataGenerator.generateDigits(7));
        payload.setTransactionType(TRANS_TYPE);

    }

    public void  getDetailsFromDbAndPreparePayloadForRMOrCM_customer(SearchAccountsRequest payload, String account_type) {
        Map<String, Object> AccountDetails_CustCodePremCode = ApplicationContext.get().getDbAction().getAccountDetails_ForResidentialOrCommercialAccount(account_type);
        String customerCode=AccountDetails_CustCodePremCode.get("UCRACCT_CUST_CODE").toString();
        String premisesCode=AccountDetails_CustCodePremCode.get("UCRACCT_PREM_CODE").toString();
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setLoginID(USERNAME_01);
        payload.setCustomerCode(customerCode);
        payload.setPremisesCode(premisesCode);
        payload.setTransactionType(TRANS_TYPE);

    }

    public void  getLastNameAndZipcodeFromDbAndPreparePayload(SearchAccountsRequest payload, String combination_status) {
        Map<String, Object> AccountDetails_LastNameZipcode = ApplicationContext.get().getDbAction().getAccountDetails_LastNameZipCode();
        String lastName=AccountDetails_LastNameZipcode.get("UZBENRO_DSM_LAST_NAME").toString();
        String zipCode=AccountDetails_LastNameZipcode.get("UCRADDR_ZIP").toString();
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setLoginID(USERNAME_01);
        payload.setCustomerLastName(lastName);
        payload.setTransactionType(TRANS_TYPE);
        if (combination_status.equalsIgnoreCase("InValid")) {
          payload.setPremisesZipCode(FakerDataGenerator.generateDigits(5));
        } else if (combination_status.equalsIgnoreCase("Valid")) {
            payload.setPremisesZipCode(zipCode);
        } else {
            throw new IllegalArgumentException("Invalid combination_status : " + combination_status);
        }

    }

    public void  getFirstNameLastNameAndZipcodeFromDbAndPreparePayload(SearchAccountsRequest payload, String account_type) {
        Map<String, Object> AccountDetails_LastNameZipcode = ApplicationContext.get().getDbAction().getAccountDetails_ForResidentialOrSeniorResAccount(account_type);
        String firstName=AccountDetails_LastNameZipcode.get("UZBENRO_DSM_FIRST_NAME").toString();
        String lastName=AccountDetails_LastNameZipcode.get("UZBENRO_DSM_LAST_NAME").toString();
        String zipCode=AccountDetails_LastNameZipcode.get("UCRADDR_ZIP").toString();
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setLoginID(USERNAME_01);
        payload.setCustomerFirstName(firstName);
        payload.setCustomerLastName(lastName);
        payload.setPremisesZipCode(zipCode);
        payload.setTransactionType(TRANS_TYPE);


    }

    public void  getCustomerBusinessNameFromDbAndPreparePayload(SearchAccountsRequest payload, String account_type) {
        Map<String, Object> AccountDetails_LastNameZipcode=null;
        if (account_type.equalsIgnoreCase("PastDueBalance")) {
             AccountDetails_LastNameZipcode = ApplicationContext.get().getDbAction().getAccountDetails_ForPastDueBalanceCommercialAccount(account_type);
        } else if (account_type.equalsIgnoreCase("SONP")) {
            AccountDetails_LastNameZipcode = ApplicationContext.get().getDbAction().getAccountDetails_ForSONPCommercialAccount(account_type);
        } else {
            throw new IllegalArgumentException("Invalid account type: " + account_type);
        }

        String customerBusinessName=AccountDetails_LastNameZipcode.get("UCBCUST_LAST_NAME").toString();
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setLoginID(USERNAME_01);
        payload.setTransactionType(TRANS_TYPE);
        payload.setCustomerBusinessName(customerBusinessName);


    }

}
