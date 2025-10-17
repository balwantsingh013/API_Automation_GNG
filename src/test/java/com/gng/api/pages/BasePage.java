package com.gng.api.pages;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.report.DetailedExtentReportManager;
import com.gng.api.report.SimplifiedExtentReportManager;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gng.api.constants.DBConstant.UCRACCT_CUST_CODE;
import static com.gng.api.constants.DBConstant.UCRACCT_PREM_CODE;
import static com.gng.api.constants.TestConstant.JSON;
import static com.gng.api.constants.TestConstant.PATH_PAYLOAD;
import static com.gng.api.context.ApplicationContext.getRequestSpec;
import static com.gng.api.util.LogUtil.logError;
import static com.gng.api.util.LogUtil.logInfo;
import static io.restassured.RestAssured.given;

@Slf4j
public abstract class BasePage {
    protected final TestContext testContext;

    protected BasePage(TestContext testContext) {
        this.testContext = testContext;
    }

    protected void setRequestSpecification(String apiName, Object request) {
        logInfo("Set Request Specification for: " + apiName);
        getRequestSpec().auth().oauth2(testContext.getAuthToken()).body(request);
    }

    // Set Request Specification
    protected <T> void setRequestSpecification(T payload, String token) {
        log.info("Setting Request Specification");
        // Use modified payload if available, otherwise use the default payload
        Object finalPayload = (testContext.getCustomRequestPayload() != null)
                ? testContext.getCustomRequestPayload()
                : payload;

        getRequestSpec()
                .headers(getApiHeaders())
                .body(finalPayload)
                .auth()
                .oauth2(token);
    }

    protected Map<String, String> getApiHeaders() {
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

    public static <T> T deserializeJsonToPojo(String apiName, Class<T> clazz) {
        return deserializeJson(apiName, clazz);
    }

    private static <T> T deserializeJson(String apiName, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper(JsonFactory.builder().enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION).build());
        try {
            String path = PATH_PAYLOAD + apiName + "." + JSON;
            return mapper.readValue(new File(path), clazz);
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize JSON file: " + apiName, e);
        }
    }

    // Deserialize Response to POJO
    protected <T> T deserializeResponseToPojo(Response response, Class<T> clazz) {
        log.info("Deserializing API Response to POJO");
        ObjectMapper mapper = new ObjectMapper(JsonFactory.builder()
                .enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION)
                .build());

        try {
            String responseBody = response.getBody().asString();
            log.info("Response Body: {}", responseBody);

            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(responseBody, clazz);
        } catch (IOException e) {
            log.error("Response Deserialization Failed. Response Status: {}, Class: {}",
                    response.getStatusCode(), clazz.getSimpleName());
            throw new RuntimeException(String.format("Failed to deserialize response to %s. Error: %s",
                    clazz.getSimpleName(), e.getMessage()), e);
        }
    }

    protected void setCustomerAndPremisesCodes(List<Map<String, Object>> activeCustomerData) {
        testContext.setCustomerCode(activeCustomerData.getFirst().get(UCRACCT_CUST_CODE).toString());
        testContext.setPremisesCode(activeCustomerData.getFirst().get(UCRACCT_PREM_CODE).toString());
    }

    public Response sendRequest(String requestType, String uri, int expectedStatusCode) {
        logInfo("Sending " + requestType + " request to " + uri);

        try {
            // Validate the request type (optional)
            if (!isValidRequestType(requestType)) {
                throw new IllegalArgumentException("Invalid HTTP method: " + requestType);
            }

            // Add request details to report BEFORE making request
            SimplifiedExtentReportManager.addRequestDetailsToReport(getRequestSpec());
            DetailedExtentReportManager.addRequestDetailsToReport(getRequestSpec());

            // Make the request
            Response response = given()
                    .when()
                    .spec(getRequestSpec())
                    .request(requestType, uri)
                    .then()
                    .extract()
                    .response();

            // Add response details to report AFTER receiving response
            SimplifiedExtentReportManager.addResponseDetailsToReport(response, expectedStatusCode);
            DetailedExtentReportManager.addResponseDetailsToReport(response, expectedStatusCode);

            // Validate status code
            response.then().statusCode(expectedStatusCode);

            logInfo(requestType + " request to " + uri + " completed successfully.");
            return response;
        } catch (Exception e) {
            log.error("Error during {} request to {}", requestType, uri, e);
            throw e;
        }
    }

    /**
     * Validates the request type against allowed HTTP methods.
     *
     * @param requestType the HTTP method
     * @return true if valid, false otherwise
     */
    private boolean isValidRequestType(String requestType) {
        return switch (requestType) {
            case HttpGet.METHOD_NAME, HttpPost.METHOD_NAME, HttpPut.METHOD_NAME,
                 HttpPatch.METHOD_NAME, HttpDelete.METHOD_NAME -> true;
            default -> false;
        };
    }
}