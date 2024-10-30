package com.gng.api.spec;

import com.gng.api.context.RunContext;
import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SetApiSpecification {

    private static final ThreadLocal<RequestSpecification> requestSpecification = new ThreadLocal<>();

    private SetApiSpecification() {
    }

    public static void setRequestSpec() {
        log.info("Setting Request Specification");
        requestSpecification.set(RestAssured.given().baseUri(RunContext.get().getEnvConfig().getBaseUri()).contentType(ContentType.JSON));
    }

    public static RequestSpecification getRequestSpec() {
        return requestSpecification.get();
    }

    public static void setResponseSpec() {
        log.info("Setting Response Specification");
        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        RestAssured.responseSpecification = responseSpecBuilder.build();
    }

    public static void removeRequestSpec() {
        requestSpecification.remove();
    }
}
