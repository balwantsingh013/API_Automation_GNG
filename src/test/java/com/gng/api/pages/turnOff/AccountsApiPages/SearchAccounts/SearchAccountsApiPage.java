package com.gng.api.pages.turnOff.AccountsApiPages.SearchAccounts;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.AccountsPojo.SearchAccounts.SearchAccountsRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.turnOff.AccountsApiSteps.SearchAccounts.SearchAccountsTOffApiLabel;
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


    public void validateResponseForCombinationOfLastNameAndZipCodeForTC77_78(SearchAccountsApiLabel apiLabel, SearchAccountsTOffApiLabel combinationType) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.getLastNameAndZipcodeFromDbAndPreparePayload(payload, combinationType);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);

    }

    public void validateResponseForCombinationOfLastNameAndZipCodeForTC79_80(SearchAccountsApiLabel apiLabel, SearchAccountsTOffApiLabel accountType) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.getFirstNameLastNameAndZipcodeFromDbAndPreparePayload(payload, accountType);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);

    }

    public void validateResponseForValidCustomerBusinessNameTC81_82(SearchAccountsApiLabel apiLabel, SearchAccountsTOffApiLabel accountType) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.getCustomerBusinessNameFromDbAndPreparePayload(payload, accountType);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);

    }

    public void validateResponseForValidCustomerBusinessNameTC83(SearchAccountsApiLabel apiLabel) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.getCustomerBusinessNameFromDbAndPreparePayloadTC_83(payload);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateResponseForValidCustomerANDPremiseCode(SearchAccountsApiLabel apiLabel, SearchAccountsTOffApiLabel testCondition){
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.getCustomerPremisesCodeFromDbAndPreparePayload(payload, testCondition);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }
}
