package com.gng.api.pages.marketerSwitch.AccountsApiPages.SearchAccounts;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.AccountsPojo.SearchAccounts.SearchAccountsRequest;
import com.gng.api.pojo.AccountsPojo.SearchAccounts.SearchAccountsResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.marketerSwitch.AccountsApiSteps.SearchAccounts.SearchAccountsApiLabel;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.SEARCH_ACCOUNTS;

public class SearchAccountsApiPage extends BasePage {

    private final SearchAccountsHelper helper;

    public SearchAccountsApiPage(TestContext testContext) {
        super(testContext);
        this.helper = new SearchAccountsHelper(testContext);
    }

    public void validatePositiveCases(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel testCondition){
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.preparePayloadForPositiveTestConditions(payload, testCondition);
        executeSearchAccountsRequest(payload);
    }

    public void validateExternalCases(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel testCondition){
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.preparePayloadForExternalConditions(payload, testCondition);
        executeSearchAccountsRequest(payload);
    }
    public void searchFromGetEligibleExternalCases(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel testCondition){
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.preparePayloadFromGetEligibleExternalConditions(payload, testCondition);
        executeSearchAccountsRequest(payload);
    }

    private void executeSearchAccountsRequest(SearchAccountsRequest payload) {
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        SearchAccountsResponse searchAccountsResponse= deserializeResponseToPojo(response,SearchAccountsResponse.class);
        testContext.setResponse(response);
        testContext.setSearchAccountsResponse(searchAccountsResponse);
    }
}
