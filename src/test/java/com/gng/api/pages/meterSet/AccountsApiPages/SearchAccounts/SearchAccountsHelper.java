package com.gng.api.pages.meterSet.AccountsApiPages.SearchAccounts;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.AccountsPojo.SearchAccounts.SearchAccountsRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.meterSet.AccountsApiSteps.SearchAccounts.SearchAccountsApiLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static com.gng.api.constants.DBConstant.*;

@Slf4j
public class SearchAccountsHelper {
    private final TestContext testContext;


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

    public void preparePayloadForPositiveTestConditions(SearchAccountsRequest payload, SearchAccountsApiLabel testCondition) {
        setParametersToEmpty(payload);

        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setTransactionType(GlobalEnums.TransactionType.METER_SET.getValue());
        Map<String, Object> activeCustomerData = ApplicationContext.get().getDbAction().getActiveCustomerWithServiceTransferEnrollment();

        switch (testCondition) {
        case MS_SA_LAST_NAME_AND_ZIP_POSITIVE_TC001 -> {
            payload.setCustomerLastName(activeCustomerData.get(UCBCUST_LAST_NAME).toString());
            payload.setPremisesZipCode(activeCustomerData.get(UCBPREM_ZIPC_CODE).toString());
        }
        case MS_SA_FIRST_NAME_LAST_NAME_AND_ZIP_POSITIVE_TC002 -> {
            payload.setCustomerFirstName(activeCustomerData.get(UCBCUST_FIRST_NAME).toString());
            payload.setCustomerLastName(activeCustomerData.get(UCBCUST_LAST_NAME).toString());
            payload.setPremisesZipCode(activeCustomerData.get(UCBPREM_ZIPC_CODE).toString());
        }
            default ->
                payload.setRequestID(FakerDataGenerator.generateString(10));
        }
    }
    public void setParametersToEmpty(SearchAccountsRequest payload){
        payload.setCustomerCode("");
        payload.setCustomerLastName("");
        payload.setCustomerFirstName("");
        payload.setPremisesZipCode("");
        payload.setCustomerBusinessName("");
        payload.setPremisesCode("");
    }
}
