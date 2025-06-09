package com.gng.api.pages.turnOff.AccountsApiPages.SearchAccounts;

import com.gng.api.pages.BasePage;
import com.gng.api.pages.turnOff.AccountsApiPages.SearchAccounts.SearchAccountsHelper;
import com.gng.api.pojo.AccountsPojo.SearchAccounts.SearchAccountsRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.turnOn.AccountsApiSteps.SearchAccounts.SearchAccountsApiLabel;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.SEARCH_ACCOUNTS;

public class SearchAccountsApiPage extends BasePage {

    private final com.gng.api.pages.turnOff.AccountsApiPages.SearchAccounts.SearchAccountsHelper helper;

    public SearchAccountsApiPage(TestContext testContext) {
        super(testContext);
        this.helper = new SearchAccountsHelper(testContext);
    }

    public void validateResponseForInvalidAccountParamtersTC74(SearchAccountsApiLabel apiLabel) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.getCustomerFromDbAndPreparePayload(payload);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);

    }

    public void validateResponseForvalidAccountParamtersForTC75_76(SearchAccountsApiLabel apiLabel, String account_type) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.getCustomerFromDbAndPreparePayload1(payload, account_type);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);

    }


}
