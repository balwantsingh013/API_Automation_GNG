package com.gng.api.pages;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.pojo.accountinfo.GetAccountInfoRequest;
import com.gng.api.pojo.accountinfo.GetAccountInfoResponse;
import com.gng.api.util.CommonUtil;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.gng.api.constants.TestConstant.UNEXPECTED_VALUE;
import static com.gng.api.context.ApplicationContext.getRequestSpec;
import static com.gng.api.util.LogUtil.logError;
import static com.gng.api.util.LogUtil.logInfo;
import static com.gng.api.constants.ApiLabel.*;

public class GetAccountInfoApiPage extends CommonUtil{

    private final TestContext testContext;

    public GetAccountInfoApiPage(TestContext testContext) {
        this.testContext = testContext;
    }

    public void setRequestSpecification(String apiName, GetAccountInfoRequest getAccountInfoRequest) {
        logInfo("Set Request Specification for :" + apiName);
        getRequestSpec().auth().oauth2(testContext.getAuthToken()).body(getAccountInfoRequest);
    }

    public GetAccountInfoRequest getApiPayload(String apiName, GetAccountInfoRequest getAccountInfoRequest) {
        logInfo("Get Api Payload");
        switch (apiName) {
            case GETACCOUNTINFO_HAPPYFLOW_API:
                getAccountInfoRequest.setRequestID(UUID.randomUUID().toString());
                getAccountInfoRequest.setCustomerCode(testContext.getCustomerCode());
                getAccountInfoRequest.setPremisesCode(testContext.getPremisesCode());
                return getAccountInfoRequest;
            case GETACCOUNTINFO_MISSINGREQUESTID_API:
                nullifyFields(getAccountInfoRequest, "requestID");
                getAccountInfoRequest.setCustomerCode(testContext.getCustomerCode());
                getAccountInfoRequest.setPremisesCode(testContext.getPremisesCode());
                return getAccountInfoRequest;
            case GETACCOUNTINFO_MISSINGPREMCODE_API:
                getAccountInfoRequest.setRequestID(UUID.randomUUID().toString());
                getAccountInfoRequest.setCustomerCode(testContext.getCustomerCode());
                nullifyFields(getAccountInfoRequest, "premisesCode");
                return getAccountInfoRequest;
            case GETACCOUNTINFO_MISSINGCUSTOMERCODE_API:
                getAccountInfoRequest.setRequestID(UUID.randomUUID().toString());
                nullifyFields(getAccountInfoRequest, "customerCode");
                getAccountInfoRequest.setPremisesCode(testContext.getPremisesCode());
                return getAccountInfoRequest;
            case GETACCOUNTINFO_INVALIDCUSTOMERCODELENGTH_API:
                getAccountInfoRequest.setRequestID(UUID.randomUUID().toString());
                getAccountInfoRequest.setCustomerCode(CommonUtil.getRandomNumericString(10));
                getAccountInfoRequest.setPremisesCode(testContext.getPremisesCode());
                return getAccountInfoRequest;
            case GETACCOUNTINFO_INVALIDPREMCODELENGTH_API:
                getAccountInfoRequest.setRequestID(UUID.randomUUID().toString());
                getAccountInfoRequest.setCustomerCode(testContext.getCustomerCode());
                getAccountInfoRequest.setPremisesCode(CommonUtil.getRandomNumericString(8));
                return getAccountInfoRequest;
            case GETACCOUNTINFO_NONEXISTENT_CUSTPREMCODE_API:
                getAccountInfoRequest.setRequestID(UUID.randomUUID().toString());
                getAccountInfoRequest.setCustomerCode(CommonUtil.getRandomNumericString(8));
                getAccountInfoRequest.setPremisesCode(testContext.getPremisesCode());
                return getAccountInfoRequest;
            default:
                throw new IllegalStateException(UNEXPECTED_VALUE + apiName);
        }
    }

    public GetAccountInfoRequest deserializeJsonToPojo(String apiName) {
        logInfo("Deserializing Json to Pojo");
        return null;
    }

    public GetAccountInfoResponse deserializeResponseToPojo(String apiName, Response response) {
        logInfo("Deserialize Response To Pojo");
        JsonFactory factory = JsonFactory.builder()
                .enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION)
                .build();
        ObjectMapper mapper = new ObjectMapper(factory);
        try {
            return switch (apiName) {
                case GETACCOUNTINFO_HAPPYFLOW_API ->
                        mapper.readValue(response.getBody().asString(), GetAccountInfoResponse.class);
                default -> throw new IllegalStateException(UNEXPECTED_VALUE + apiName);
            };
        } catch (JsonProcessingException e) {
            logError(e.getMessage());
        }
        return null;
    }

    public Map<String, String> getApiHeaders(String apiName) {
        logInfo("Get Api Headers");
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization", "Bearer " + testContext.getAuthToken());
        return requestHeaders;
    }

    public String getApiQueryParams(String apiName) {
        logInfo("Get Api Params");
        return null;
    }
}
