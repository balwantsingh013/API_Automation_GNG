package com.gng.api.pages.AccountsApiPages.GetAccountInfoPage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.pojo.AccountsPojo.getAccountInfo.GetAccountInfoRequest;
import com.gng.api.pojo.AccountsPojo.getAccountInfo.GetAccountInfoResponse;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import java.util.List;
import java.util.Map;

import static com.gng.api.constants.ApiEndPoint.GET_ACCOUNT_INFO;
import static com.gng.api.pages.AccountsApiPages.GetAccountInfoPage.GetAccountInfoLabels.*;
import static com.gng.api.util.LogUtil.logInfo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class GetAccountInfoApiPage extends BasePage {
    private final GetAccountInfoHelper helper;

    public GetAccountInfoApiPage(TestContext testContext) {
        super(testContext);
        this.helper = new GetAccountInfoHelper(testContext);
    }

    public void sendGetAccountInfoRequest() {
        List<Map<String, Object>> activeCustomerData = ApplicationContext.get().getDbAction().getActiveCustomerDetails();
        setCustomerAndPremisesCodes(activeCustomerData);

        GetAccountInfoRequest request = helper.createAndConfigureRequest(HAPPY_FLOW);
        setRequestSpecification(HAPPY_FLOW.getLabel(), request);
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ACCOUNT_INFO, 200);
        GetAccountInfoResponse getAccountInfoResponse = deserializeResponseToPojo(
                HAPPY_FLOW.getLabel(), response, GetAccountInfoResponse.class);
        testContext.setGetAccountInfoResponse(getAccountInfoResponse);
    }

    public void sendGetAccountInfoRequestWithMissingParam(String missingParam) {
        List<Map<String, Object>> activeCustomerData = ApplicationContext.get().getDbAction().getActiveCustomerDetails();
        setCustomerAndPremisesCodes(activeCustomerData);

        GetAccountInfoLabels apiLabel = helper.getMissingParamApiLabel(missingParam);
        GetAccountInfoRequest request = helper.createAndConfigureRequest(apiLabel);
        setRequestSpecification(apiLabel.getLabel(), request);
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ACCOUNT_INFO, 200);
        testContext.setResponse(response);
    }

    public void sendGetAccountInfoRequestWithInvalidParamLength(String param) {
        List<Map<String, Object>> activeCustomerData = ApplicationContext.get().getDbAction().getActiveCustomerDetails();
        setCustomerAndPremisesCodes(activeCustomerData);

        GetAccountInfoLabels apiLabel = helper.getInvalidLengthApiLabel(param);
        GetAccountInfoRequest request = helper.createAndConfigureRequest(apiLabel);
        setRequestSpecification(apiLabel.getLabel(), request);
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ACCOUNT_INFO, 200);
        testContext.setResponse(response);
    }

    public void sendGetAccountInfoRequestWithDuplicateRequestId() {
        List<Map<String, Object>> activeCustomerData = ApplicationContext.get().getDbAction().getActiveCustomerDetails();
        setCustomerAndPremisesCodes(activeCustomerData);

        GetAccountInfoRequest request = helper.createAndConfigureRequest(HAPPY_FLOW);
        setRequestSpecification(HAPPY_FLOW.getLabel(), request);
        sendRequest(HttpPost.METHOD_NAME, GET_ACCOUNT_INFO, 200);
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ACCOUNT_INFO, 200);
        testContext.setResponse(response);
    }

    public void sendGetAccountInfoRequestWithNonExistentCombination() {
        List<Map<String, Object>> activeCustomerData = ApplicationContext.get().getDbAction().getActiveCustomerDetails();
        setCustomerAndPremisesCodes(activeCustomerData);

        GetAccountInfoRequest request = helper.createAndConfigureRequest(NONEXISTENT_CUST_PREM_CODE);
        setRequestSpecification(NONEXISTENT_CUST_PREM_CODE.getLabel(), request);
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ACCOUNT_INFO, 200);
        testContext.setResponse(response);
    }

    public void verifyResponseCodeForMissingParam(String missingParam, int statusCode) {
        assertThat("Invalid AccountInfoApi Response Code for missing " + missingParam,
                testContext.getResponse().statusCode(), equalTo(statusCode));
    }

    public void verifyResponseCodeForInvalidParamLength(String param, int statusCode) {
        assertThat("Invalid AccountInfoApi Response Code for invalid " + param,
                testContext.getResponse().statusCode(), equalTo(statusCode));
    }

    public void verifyAccountInformationWithDatabase() {
        Map<String, Object> accountInformationDB = ApplicationContext.get().getDbAction()
                .getAccountInformationHappyFlow().getFirst();
        GetAccountInfoResponse response = testContext.getGetAccountInfoResponse();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> responseMap = mapper.convertValue(response.getData(), new TypeReference<Map<String, Object>>() {});

        logInfo("Database Values: {}"+ accountInformationDB);
        logInfo("Response Values: {}"+ responseMap);

        helper.verifyAccountInformationWithDatabase(accountInformationDB, responseMap);
    }

}
