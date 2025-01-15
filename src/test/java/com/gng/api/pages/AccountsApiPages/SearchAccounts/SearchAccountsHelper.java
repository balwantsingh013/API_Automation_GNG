package com.gng.api.pages.AccountsApiPages.SearchAccounts;


import com.gng.api.pages.BasePage;
import com.gng.api.pojo.AccountsPojo.SearchAccounts.SearchAccountsRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.pojo.Users.GetUserRoles.GetUserRolesRequest;
import com.gng.api.steps.AccountsApiSteps.SearchAccounts.SearchAccountsApiLabel;
import com.gng.api.steps.UsersApiSteps.GetUserRoles.GetUserRolesApiLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;

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
    public void setRequestIDBasedOnTypeTCTC42_TC44(SearchAccountsRequest payload, SearchAccountsApiLabel requestID) {
        switch (requestID) {
            case NULL_REQUEST_ID_TC42:
                payload.setRequestID(null);
                payload.setLoginID("test10965");
                break;
            case DUPLICATE_REQUEST_ID_TC43:
                payload.setRequestID("3BC00A0397B14F29A313280EE0110941");
                break;
            case LONG_REQUEST_ID_TC44:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(35));
                break;
            default:
                payload.setRequestID(FakerDataGenerator.generateString(10));
        }
    }
    public void setLoginIDBasedOnTypeTC45_TC48(SearchAccountsRequest payload, SearchAccountsApiLabel loginID) {
        switch (loginID) {
            case NULL_LOGIN_ID_TC45:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("");
                break;
            case ALPHANUMERIC_LOGIN_ID_TC47:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(FakerDataGenerator.generateAlphanumeric(8));
                break;
            case MAX_LENGTH_LOGIN_ID_TC46:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(FakerDataGenerator.getRandomNumericString(35));
                break;
            case INVALID_LOGIN_ID_NOT_PRESENT_USER_TABLE_TC48:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("dummy");
                break;
            default:
                payload.setLoginID(FakerDataGenerator.generateLowerCaseString(10));
        }
    }
    public void setCustomerCodeBasedOnTypeTC49(SearchAccountsRequest payload, SearchAccountsApiLabel customerCode) {
        switch (customerCode) {
            case MAX_LENGTH_CUSTOMER_CODE_TC49:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("test10965");
                payload.setCustomerCode(FakerDataGenerator.generateLowerCaseString(15));
                break;
            default:
                payload.setCustomerCode(FakerDataGenerator.generateLowerCaseString(9));
        }
        }
    public void setPremisesCodeBasedOnTypeTC50(SearchAccountsRequest payload, SearchAccountsApiLabel premisesCode) {
        switch (premisesCode) {
            case MAX_LENGTH_PREMISES_CODE_TC50:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("test10965");
                payload.setPremisesCode(FakerDataGenerator.generateLowerCaseString(8));
                break;
            default:
                payload.setPremisesCode(FakerDataGenerator.generateLowerCaseString(7));
        }
    }
    public void setTransactionTypeBasedOnTypeTC51_TC52(SearchAccountsRequest payload, SearchAccountsApiLabel transactionType) {
        switch (transactionType) {
            case MISSING_TRANSACTION_TYPE_TC51:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("test10965");
                payload.setTransactionID(null);
                break;
            case INVALID_TRANSACTION_TYPE_TC52:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("test10965");
                payload.setTransactionID("TNOF");
                break;
            default:
                payload.setTransactionType(FakerDataGenerator.generateUpperCaseString(4));
        }
    }
    public void setBusinessCodeBasedOnTypeTC53(SearchAccountsRequest payload, SearchAccountsApiLabel businessName) {
        switch (businessName) {
            case MAX_LENGTH_BUSINESS_NAME_TC53:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("test10965");
                payload.setcustomerBusinessName(FakerDataGenerator.generateLowerCaseString(62));
                break;
            default:
                payload.setcustomerBusinessName(FakerDataGenerator.generateLowerCaseString(50));
        }
    }


}
