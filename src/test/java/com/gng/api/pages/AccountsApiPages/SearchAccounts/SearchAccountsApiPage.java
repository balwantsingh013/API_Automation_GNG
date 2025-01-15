package com.gng.api.pages.AccountsApiPages.SearchAccounts;


import com.gng.api.pages.BasePage;
import com.gng.api.pojo.AccountsPojo.SearchAccounts.SearchAccountsRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.pojo.Users.GetUserRoles.GetUserRolesRequest;
import com.gng.api.steps.AccountsApiSteps.SearchAccounts.SearchAccountsApiLabel;
import com.gng.api.steps.UsersApiSteps.GetUserRoles.GetUserRolesApiLabel;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.GET_USER_ROLES;
import static com.gng.api.constants.ApiEndPoint.SEARCH_ACCOUNTS;
import static org.hamcrest.MatcherAssert.assertThat;

public class SearchAccountsApiPage extends BasePage {
    private final SearchAccountsHelper helper;

    public SearchAccountsApiPage(TestContext testContext) {
        super(testContext);
        this.helper = new SearchAccountsHelper(testContext);
    }
    public void validateInvalidRequestIDCasesTC42_TC44(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel requestID) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setRequestIDBasedOnTypeTCTC42_TC44(payload, requestID);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }
    public void validateInvalidLoginIDCasesTC45_TC48(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel loginID) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setLoginIDBasedOnTypeTC45_TC48(payload, loginID);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }
    public void validateInvalidCustomerCodeCasesTC49(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel customerCode) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setCustomerCodeBasedOnTypeTC49(payload, customerCode);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }
    public void validateInvalidPremisesCodeCasesTC50(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel premisesCode) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setPremisesCodeBasedOnTypeTC50(payload, premisesCode);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidTransactionTypeCasesTC51_TC52(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel transactionType) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setTransactionTypeBasedOnTypeTC51_TC52(payload, transactionType);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }
    public void validateInvalidBusinessNameCasesTC53(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel businessName) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setBusinessCodeBasedOnTypeTC53(payload, businessName);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

}
