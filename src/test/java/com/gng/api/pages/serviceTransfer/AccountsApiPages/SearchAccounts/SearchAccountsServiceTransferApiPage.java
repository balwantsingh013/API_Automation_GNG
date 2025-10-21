package com.gng.api.pages.serviceTransfer.AccountsApiPages.SearchAccounts;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.AccountsPojo.SearchAccounts.SearchAccountsRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.serviceTransfer.AccountsApiSteps.SearchAccounts.SearchAccountsApiLabel;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.SEARCH_ACCOUNTS;

public class SearchAccountsServiceTransferApiPage extends BasePage {

    private final com.gng.api.pages.serviceTransfer.AccountsApiPages.SearchAccounts.SearchAccountsHelper helper;

    public SearchAccountsServiceTransferApiPage(TestContext testContext) {
        super(testContext);
        this.helper = new SearchAccountsHelper(testContext);
    }

    public void validateNegativeCases(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel testCondition){
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.preparePayloadForNegativeTestConditions(payload, testCondition);
        executeSearchAccountsRequest(payload);
    }

    private void executeSearchAccountsRequest(SearchAccountsRequest payload) {
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

}