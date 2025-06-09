package com.gng.api.pages.turnOff.AccountsApiPages.SearchAccounts;

import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.AccountsPojo.SearchAccounts.SearchAccountsRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.turnOn.AccountsApiSteps.SearchAccounts.SearchAccountsApiLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;

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

    public void  getCustomerFromDbAndPreparePayload(SearchAccountsRequest payload) {
        Map<String, Object> AccountDetails_CustCode = ApplicationContext.get().getDbAction().getAccountDetails_CustCode();
        String customerCode=AccountDetails_CustCode.get("UCRACCT_CUST_CODE").toString();
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setLoginID(USERNAME_01);
        payload.setCustomerCode(customerCode);
        payload.setPremisesCode(FakerDataGenerator.generateDigits(7));
        payload.setTransactionType(TRANS_TYPE);

    }

    public void  getCustomerFromDbAndPreparePayload1(SearchAccountsRequest payload, String account_type) {
        Map<String, Object> AccountDetails_CustCode = ApplicationContext.get().getDbAction().getAccountDetails_ForResOrCommAccount(account_type);
        String customerCode=AccountDetails_CustCode.get("UCRACCT_CUST_CODE").toString();
        String premisesCode=AccountDetails_CustCode.get("UCRACCT_PREM_CODE").toString();
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setLoginID(USERNAME_01);
        payload.setCustomerCode(customerCode);
        payload.setPremisesCode(premisesCode);
        payload.setTransactionType(TRANS_TYPE);

    }

}
