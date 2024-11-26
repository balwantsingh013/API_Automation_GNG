package com.gng.api.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gng.api.context.RunContext;
import com.gng.api.context.TestContext;
import com.gng.api.pages.GetAccountInfoApiPage;
import com.gng.api.pojo.accountinfo.GetAccountInfoRequest;
import com.gng.api.pojo.accountinfo.GetAccountInfoResponse;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpPost;
import org.assertj.core.api.SoftAssertions;

import java.util.List;
import java.util.Map;

import static com.gng.api.config.LogConfig.logError;
import static com.gng.api.constants.ApiEndPoint.ACCOUNT_INFO;
import static com.gng.api.constants.ApiLabel.*;
import static com.gng.api.constants.DBConstant.UCRACCT_CUST_CODE;
import static com.gng.api.constants.DBConstant.UCRACCT_PREM_CODE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Slf4j
public class GetAccountInfoApiSteps {

    private final TestContext testContext;

    public GetAccountInfoApiSteps(TestContext testContext, GetAccountInfoApiPage getAccountInfoApiPage) {
        super();
        this.testContext = testContext;
        testContext.setGetAccountInfoApiPage(getAccountInfoApiPage);
    }

    @When("a request is made to the GetAccountInfo Api")
    public void a_request_is_made_to_the_get_account_info_api() {
        List<Map<String, Object>> activeCustomerData = RunContext.get().getDbAction().getActiveCustomerDetails();
        testContext.setCustomerCode(activeCustomerData.getFirst().get(UCRACCT_CUST_CODE).toString());
        testContext.setPremisesCode(activeCustomerData.getFirst().get(UCRACCT_PREM_CODE).toString());
        GetAccountInfoRequest getAccountInfoRequest = new GetAccountInfoRequest();
        getAccountInfoRequest = testContext.getGetAccountInfoApiPage().getApiPayload(GETACCOUNTINFO_HAPPYFLOW_API, getAccountInfoRequest);
        testContext.getGetAccountInfoApiPage().setRequestSpecification(GETACCOUNTINFO_HAPPYFLOW_API, getAccountInfoRequest);
        Response response = testContext.getGetAccountInfoApiPage().sendRequest(HttpPost.METHOD_NAME, ACCOUNT_INFO, 200);
        GetAccountInfoResponse getAccountInfoResponse = testContext.getGetAccountInfoApiPage().deserializeResponseToPojo(GETACCOUNTINFO_HAPPYFLOW_API, response);
        testContext.setGetAccountInfoResponse(getAccountInfoResponse);
    }

    @When("a request is made to the GetAccountInfo Api with missing param {string}")
    public void a_request_is_made_to_the_get_account_info_api_with_missing_param(String missingParam) {
        //Get Data from DB
        List<Map<String, Object>> activeCustomerData = RunContext.get().getDbAction().getActiveCustomerDetails();
        testContext.setCustomerCode(activeCustomerData.getFirst().get(UCRACCT_CUST_CODE).toString());
        testContext.setPremisesCode(activeCustomerData.getFirst().get(UCRACCT_PREM_CODE).toString());
        //Create Request Payload
        GetAccountInfoRequest getAccountInfoRequest = new GetAccountInfoRequest();
        switch (missingParam) {
            case "RequestID":
                getAccountInfoRequest = testContext.getGetAccountInfoApiPage().getApiPayload(GETACCOUNTINFO_MISSINGREQUESTID_API, getAccountInfoRequest);
                testContext.getGetAccountInfoApiPage().setRequestSpecification(GETACCOUNTINFO_MISSINGREQUESTID_API, getAccountInfoRequest);
                break;
            case "PremisesCode":
                getAccountInfoRequest = testContext.getGetAccountInfoApiPage().getApiPayload(GETACCOUNTINFO_MISSINGPREMCODE_API, getAccountInfoRequest);
                testContext.getGetAccountInfoApiPage().setRequestSpecification(GETACCOUNTINFO_MISSINGPREMCODE_API, getAccountInfoRequest);
                break;
            case "CustomerCode":
                getAccountInfoRequest = testContext.getGetAccountInfoApiPage().getApiPayload(GETACCOUNTINFO_MISSINGCUSTOMERCODE_API, getAccountInfoRequest);
                testContext.getGetAccountInfoApiPage().setRequestSpecification(GETACCOUNTINFO_MISSINGCUSTOMERCODE_API, getAccountInfoRequest);
                break;
            default:
                logError("Invalid Param Passed to Switch Case: " + missingParam);

        }
        Response response = testContext.getGetAccountInfoApiPage().sendRequest(HttpPost.METHOD_NAME, ACCOUNT_INFO, 200);
        testContext.setResponse(response);
    }

    @Then("verify response code of GetAccountInfo Api with missing param {string} is {int}")
    public void verify_response_code_of_getAccountInfoApi_with_missing_param_is(String missingParam, int statusCode) {
        assertThat("Invalid AccountInfoApi Response Code for missing " + missingParam, testContext.getResponse().statusCode(), equalTo(statusCode));
    }

    @When("a request is made to the GetAccountInfo Api with invalid param {string} length")
    public void a_request_is_made_to_the_get_account_info_api_with_invalid_param_length(String param) {
        List<Map<String, Object>> activeCustomerData = RunContext.get().getDbAction().getActiveCustomerDetails();
        testContext.setCustomerCode(activeCustomerData.getFirst().get(UCRACCT_CUST_CODE).toString());
        testContext.setPremisesCode(activeCustomerData.getFirst().get(UCRACCT_PREM_CODE).toString());
        GetAccountInfoRequest getAccountInfoRequest = new GetAccountInfoRequest();
        switch (param) {
            case "PremCode":
                getAccountInfoRequest = testContext.getGetAccountInfoApiPage().getApiPayload(GETACCOUNTINFO_INVALIDPREMCODELENGTH_API, getAccountInfoRequest);
                testContext.getGetAccountInfoApiPage().setRequestSpecification(GETACCOUNTINFO_INVALIDPREMCODELENGTH_API, getAccountInfoRequest);
                break;
            case "CustomerCode":
                getAccountInfoRequest = testContext.getGetAccountInfoApiPage().getApiPayload(GETACCOUNTINFO_INVALIDCUSTOMERCODELENGTH_API, getAccountInfoRequest);
                testContext.getGetAccountInfoApiPage().setRequestSpecification(GETACCOUNTINFO_INVALIDCUSTOMERCODELENGTH_API, getAccountInfoRequest);
                break;
            default:
                logError("Invalid Param Passed to Switch Case: " + param);
        }
        Response response = testContext.getGetAccountInfoApiPage().sendRequest(HttpPost.METHOD_NAME, ACCOUNT_INFO, 200);
        testContext.setResponse(response);
    }

    @Then("verify response code of GetAccountInfo Api with invalid param {string} length is {int}")
    public void verify_response_code_of_get_account_info_api_with_invalid_param_length_is(String param, Integer statusCode) {
        assertThat("Invalid AccountInfoApi Response Code for missing " + param, testContext.getResponse().statusCode(), equalTo(statusCode));
    }

    @Then("verify the account information in the response should match the information in the database")
    public void verify_the_account_information_in_the_response_should_match_the_information_in_the_database() {
        Map<String, Object> accountInformationDB = RunContext.get().getDbAction().getAccountInformationHappyFlow().getFirst();
        GetAccountInfoResponse response = testContext.getGetAccountInfoResponse();
        List<String> keysDB = accountInformationDB.keySet().stream().toList();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> responseMap = mapper.convertValue(response.getData(), Map.class);
        SoftAssertions softAssert = new SoftAssertions();
        for (String key : keysDB) {
            try {
                softAssert.assertThat(responseMap.get(key)).isEqualTo(accountInformationDB.get(key));
            } catch (ClassCastException e) {
                log.error(e.getMessage());
            }
            //.withFailMessage("Expected: " + accountInformationDB.get(key).toString() + "\nActual: " + responseMap.get(key))
        }
        softAssert.assertAll();
    }

    @When("a request is made to the GetAccountInfo Api with duplicate requestID")
    public void a_request_is_made_to_the_get_account_info_api_with_duplicate_request_id() {
        List<Map<String, Object>> activeCustomerData = RunContext.get().getDbAction().getActiveCustomerDetails();
        testContext.setCustomerCode(activeCustomerData.getFirst().get(UCRACCT_CUST_CODE).toString());
        testContext.setPremisesCode(activeCustomerData.getFirst().get(UCRACCT_PREM_CODE).toString());
        GetAccountInfoRequest getAccountInfoRequest = new GetAccountInfoRequest();
        getAccountInfoRequest = testContext.getGetAccountInfoApiPage().getApiPayload(GETACCOUNTINFO_HAPPYFLOW_API, getAccountInfoRequest);
        testContext.getGetAccountInfoApiPage().setRequestSpecification(GETACCOUNTINFO_HAPPYFLOW_API, getAccountInfoRequest);
        testContext.getGetAccountInfoApiPage().sendRequest(HttpPost.METHOD_NAME, ACCOUNT_INFO, 200);
        Response response = testContext.getGetAccountInfoApiPage().sendRequest(HttpPost.METHOD_NAME, ACCOUNT_INFO, 200);
        testContext.setResponse(response);
    }


    @When("a request is made to the GetAccountInfo Api with non-existent combination of custCode and premCode")
    public void a_request_is_made_to_the_get_account_info_api_with_non_existent_combination_of_cust_code_and_prem_code() {
        List<Map<String, Object>> activeCustomerData = RunContext.get().getDbAction().getActiveCustomerDetails();
        testContext.setCustomerCode(activeCustomerData.getFirst().get(UCRACCT_CUST_CODE).toString());
        testContext.setPremisesCode(activeCustomerData.getFirst().get(UCRACCT_PREM_CODE).toString());
        GetAccountInfoRequest getAccountInfoRequest = new GetAccountInfoRequest();
        getAccountInfoRequest = testContext.getGetAccountInfoApiPage().getApiPayload(GETACCOUNTINFO_NONEXISTENT_CUSTPREMCODE_API, getAccountInfoRequest);
        testContext.getGetAccountInfoApiPage().setRequestSpecification(GETACCOUNTINFO_NONEXISTENT_CUSTPREMCODE_API, getAccountInfoRequest);
        Response response = testContext.getGetAccountInfoApiPage().sendRequest(HttpPost.METHOD_NAME, ACCOUNT_INFO, 200);
        testContext.setResponse(response);
    }
}
