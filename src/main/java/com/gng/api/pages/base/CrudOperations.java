package com.gng.api.pages.base;

import com.gng.api.report.ExtentReportManager;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.*;

import static com.gng.api.spec.SetApiSpecification.getRequestSpec;
import static com.gng.api.util.LogUtil.logInfo;
import static io.restassured.RestAssured.given;

/**
 * Class containing CRUD operations method
 * Create, Read, Update, Delete
 */
@Slf4j
public class CrudOperations {

    protected static final String UNEXPECTED_VALUE = "Unexpected value: ";

    public Response sendRequest(String requestType, String uri, int expectedStatusCode) {
        ThreadLocal<Response> response = new ThreadLocal<>();
        logInfo("Send " + requestType + " Request");
        switch (requestType) {
            case HttpGet.METHOD_NAME -> {
                response.set(given().
                        when().spec(getRequestSpec()).get(uri).then().
                        extract().response());
                ExtentReportManager.addResponseDetailsToReport(response.get(), expectedStatusCode);
                response.get().then().statusCode(expectedStatusCode);
                return response.get();
            }
            case HttpPost.METHOD_NAME -> {
                response.set(given().
                        when().spec(getRequestSpec()).post(uri).then().
                        extract().response());
                ExtentReportManager.addResponseDetailsToReport(response.get(), expectedStatusCode);
                response.get().then().statusCode(expectedStatusCode);
                return response.get();
            }
            case HttpPut.METHOD_NAME -> {
                response.set(given().
                        when().spec(getRequestSpec()).put(uri).then().
                        extract().response());
                ExtentReportManager.addResponseDetailsToReport(response.get(), expectedStatusCode);
                response.get().then().statusCode(expectedStatusCode);
                return response.get();
            }
            case HttpPatch.METHOD_NAME -> {
                response.set(given().
                        when().spec(getRequestSpec()).patch(uri).then().
                        extract().response());
                ExtentReportManager.addResponseDetailsToReport(response.get(), expectedStatusCode);
                response.get().then().statusCode(expectedStatusCode);
                return response.get();
            }
            case HttpDelete.METHOD_NAME -> {
                response.set(given().
                        when().spec(getRequestSpec()).delete(uri).then().
                        extract().response());
                ExtentReportManager.addResponseDetailsToReport(response.get(), expectedStatusCode);
                response.get().then().statusCode(expectedStatusCode);
                return response.get();
            }
            default -> throw new IllegalStateException("Invalid RequestType Param: " + requestType);
        }
    }

}
