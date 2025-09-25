package com.gng.api.report;

import io.qameta.allure.Allure;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class AllureRestAssuredFilter implements Filter {

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext context) {
        // Capture request body only if not auth token
        if (!requestSpec.getURI().contains("/Authentication/GetToken")) {
            String requestBuilder = "Request Body: " +
                    (requestSpec.getBody() != null ? requestSpec.getBody().toString() : "No Body");
            Allure.addAttachment("Request", new ByteArrayInputStream(requestBuilder.getBytes(StandardCharsets.UTF_8)));
        }

        // Proceed with the request
        Response response = context.next(requestSpec, responseSpec);

        // Capture response details only if not auth token
        if (!requestSpec.getURI().contains("/Authentication/GetToken")) {
            String responseBuilder = "Response Status Code: " + response.getStatusCode() + "\n" +
                    "Response Body: " + response.getBody().asString();
            Allure.addAttachment("Response", new ByteArrayInputStream(responseBuilder.getBytes(StandardCharsets.UTF_8)));
        }

        return response;
    }
}
