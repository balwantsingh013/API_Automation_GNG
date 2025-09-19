package com.gng.api.pages.turnOff.AccountsApiPages.SearchAccounts;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.AccountsPojo.SearchAccounts.SearchAccountsRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.turnOff.AccountsApiSteps.SearchAccounts.SearchAccountsTOffApiLabel;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.SEARCH_ACCOUNTS;

public class SearchAccountsApiPage extends BasePage {

    private final com.gng.api.pages.turnOff.AccountsApiPages.SearchAccounts.SearchAccountsHelper helper;

    public SearchAccountsApiPage(TestContext testContext) {
        super(testContext);
        this.helper = new SearchAccountsHelper(testContext);
    }

    public void validateResponseForCombinationOfLastNameAndZipCodeForTC77_78(SearchAccountsTOffApiLabel apiLabel, SearchAccountsTOffApiLabel combinationType) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.getLastNameAndZipcodeFromDbAndPreparePayload(payload, combinationType);
        executeSearchAccountsRequest(payload);
    }

    public void validateResponseForCombinationOfLastNameAndZipCode(SearchAccountsTOffApiLabel apiLabel, SearchAccountsTOffApiLabel accountType) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.getFirstNameLastNameAndZipcodeFromDbAndPreparePayload(payload, accountType);
        executeSearchAccountsRequest(payload);
    }

    public void validateResponseForValidCustomerBusinessName(SearchAccountsTOffApiLabel apiLabel, SearchAccountsTOffApiLabel accountType) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.getCustomerBusinessNameFromDbAndPreparePayload(payload, accountType);
        executeSearchAccountsRequest(payload);
    }

    public void validateResponseForValidAGLCNumberTC103(SearchAccountsTOffApiLabel apiLabel){
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.getAGLCNumberFromDbAndPreparePayloadTC_103(payload);
        executeSearchAccountsRequest(payload);
    }

    public void validateResponseForValidAddressDetailsTC104_105(SearchAccountsTOffApiLabel apiLabel, SearchAccountsTOffApiLabel testCondition){
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.getAddressDetailsFromDbAndPreparePayloadTC_104(payload, testCondition);
        executeSearchAccountsRequest(payload);
    }

    public void validateResponseForInvalidRequestID(SearchAccountsTOffApiLabel apiLabel, SearchAccountsTOffApiLabel testCondition){
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.preparePayloadForInvalidRequestId(payload, testCondition);
        executeSearchAccountsRequest(payload);
    }

    public void validateResponseForInvalidLoginID(SearchAccountsTOffApiLabel apiLabel, SearchAccountsTOffApiLabel testCondition){
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.preparePayloadForInvalidLoginId(payload, testCondition);
        executeSearchAccountsRequest(payload);
    }

    public void validateResponseForInvalidField(SearchAccountsTOffApiLabel apiLabel, SearchAccountsTOffApiLabel testCondition){
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.preparePayloadForInvalidField(payload, testCondition);
        executeSearchAccountsRequest(payload);
    }

    public void validateResponseForValidSSNTC89(SearchAccountsTOffApiLabel apiLabel){
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.getSSNFromDbAndPreparePayloadTC_89(payload);
        executeSearchAccountsRequest(payload);
    }

    public void validateResponseForValidCustomerANDPremiseCode(SearchAccountsTOffApiLabel apiLabel, SearchAccountsTOffApiLabel testCondition){
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.getCustomerPremisesCodeFromDbAndPreparePayload(payload, testCondition);
        executeSearchAccountsRequest(payload);
    }

    private void executeSearchAccountsRequest(SearchAccountsRequest payload) {
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }
}
