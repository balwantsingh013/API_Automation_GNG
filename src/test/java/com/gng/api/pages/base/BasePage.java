package com.gng.api.pages.base;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.util.CommonUtil;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gng.api.constants.DBConstant.UCRACCT_CUST_CODE;
import static com.gng.api.constants.DBConstant.UCRACCT_PREM_CODE;
import static com.gng.api.context.ApplicationContext.getRequestSpec;
import static com.gng.api.util.LogUtil.logError;
import static com.gng.api.util.LogUtil.logInfo;

public abstract class BasePage extends CommonUtil {
    protected final TestContext testContext;

    protected BasePage(TestContext testContext) {
        this.testContext = testContext;
    }

    protected void setRequestSpecification(String apiName, Object request) {
        logInfo("Set Request Specification for: " + apiName);
        getRequestSpec().auth().oauth2(testContext.getAuthToken()).body(request);
    }

    protected Map<String, String> getApiHeaders() {
        logInfo("Get Api Headers");
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization", "Bearer " + testContext.getAuthToken());
        return requestHeaders;
    }

    protected <T> T deserializeResponseToPojo(String apiName, Response response, Class<T> responseClass) {
        logInfo("Deserialize Response To Pojo");
        JsonFactory factory = JsonFactory.builder()
                .enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION)
                .build();
        ObjectMapper mapper = new ObjectMapper(factory);
        try {
            return mapper.readValue(response.getBody().asString(), responseClass);
        } catch (JsonProcessingException e) {
            logError(e.getMessage());
            return null;
        }
    }

    protected void setCustomerAndPremisesCodes(List<Map<String, Object>> activeCustomerData) {
        testContext.setCustomerCode(activeCustomerData.getFirst().get(UCRACCT_CUST_CODE).toString());
        testContext.setPremisesCode(activeCustomerData.getFirst().get(UCRACCT_PREM_CODE).toString());
    }

    protected Response executeRequest(String method, String endpoint, int expectedStatusCode) {
        return sendRequest(method, endpoint, expectedStatusCode);
    }
}

