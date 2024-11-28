package com.gng.api.pages.GetAccountInfoPage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.base.BasePage;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.pojo.accountinfo.GetAccountInfoRequest;
import com.gng.api.pojo.accountinfo.GetAccountInfoResponse;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import java.util.List;
import java.util.Map;

import static com.gng.api.constants.ApiEndPoint.ACCOUNT_INFO;
import static com.gng.api.constants.ApiLabel.*;
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

        GetAccountInfoRequest request = helper.createAndConfigureRequest(GETACCOUNTINFO_HAPPYFLOW_API);
        setRequestSpecification(GETACCOUNTINFO_HAPPYFLOW_API, request);
        Response response = executeRequest(HttpPost.METHOD_NAME, ACCOUNT_INFO, 200);
        GetAccountInfoResponse getAccountInfoResponse = deserializeResponseToPojo(
                GETACCOUNTINFO_HAPPYFLOW_API, response, GetAccountInfoResponse.class);
        testContext.setGetAccountInfoResponse(getAccountInfoResponse);
    }

    public void sendGetAccountInfoRequestWithMissingParam(String missingParam) {
        List<Map<String, Object>> activeCustomerData = ApplicationContext.get().getDbAction().getActiveCustomerDetails();
        setCustomerAndPremisesCodes(activeCustomerData);

        String apiLabel = helper.getMissingParamApiLabel(missingParam);
        GetAccountInfoRequest request = helper.createAndConfigureRequest(apiLabel);
        setRequestSpecification(apiLabel, request);
        Response response = executeRequest(HttpPost.METHOD_NAME, ACCOUNT_INFO, 200);
        testContext.setResponse(response);
    }

    public void sendGetAccountInfoRequestWithInvalidParamLength(String param) {
        List<Map<String, Object>> activeCustomerData = ApplicationContext.get().getDbAction().getActiveCustomerDetails();
        setCustomerAndPremisesCodes(activeCustomerData);

        String apiLabel = helper.getInvalidLengthApiLabel(param);
        GetAccountInfoRequest request = helper.createAndConfigureRequest(apiLabel);
        setRequestSpecification(apiLabel, request);
        Response response = executeRequest(HttpPost.METHOD_NAME, ACCOUNT_INFO, 200);
        testContext.setResponse(response);
    }

    public void sendGetAccountInfoRequestWithDuplicateRequestId() {
        List<Map<String, Object>> activeCustomerData = ApplicationContext.get().getDbAction().getActiveCustomerDetails();
        setCustomerAndPremisesCodes(activeCustomerData);

        GetAccountInfoRequest request = helper.createAndConfigureRequest(GETACCOUNTINFO_HAPPYFLOW_API);
        setRequestSpecification(GETACCOUNTINFO_HAPPYFLOW_API, request);
        executeRequest(HttpPost.METHOD_NAME, ACCOUNT_INFO, 200);
        Response response = executeRequest(HttpPost.METHOD_NAME, ACCOUNT_INFO, 200);
        testContext.setResponse(response);
    }

    public void sendGetAccountInfoRequestWithNonExistentCombination() {
        List<Map<String, Object>> activeCustomerData = ApplicationContext.get().getDbAction().getActiveCustomerDetails();
        setCustomerAndPremisesCodes(activeCustomerData);

        GetAccountInfoRequest request = helper.createAndConfigureRequest(GETACCOUNTINFO_NONEXISTENT_CUSTPREMCODE_API);
        setRequestSpecification(GETACCOUNTINFO_NONEXISTENT_CUSTPREMCODE_API, request);
        Response response = executeRequest(HttpPost.METHOD_NAME, ACCOUNT_INFO, 200);
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
        Map<String, String> responseMap = mapper.convertValue(response.getData(), Map.class);

        helper.verifyAccountInformationWithDatabase(accountInformationDB, responseMap);
    }
}
