package com.gng.api.context;

import com.gng.api.pojo.auth.AuthPayload;
import com.gng.api.pojo.envConfig.EnvConfig;
import com.gng.api.db.DBAction;
import com.gng.api.db.DBConnection;
import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.*;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;

import static com.gng.api.constants.TestConstant.PATH_CONFIG;

@Getter
@Slf4j
public class ApplicationContext {
    private static final ThreadLocal<RequestSpecification> requestSpecification = new ThreadLocal<>();

    private static final String DEFAULT_ENV = "qa";
    private static final String ENV_PROPERTY = "env";
    private static ApplicationContext instance;

    private AuthPayload authPayload;
    private EnvConfig envConfig;
    private DBAction dbAction;

    public ApplicationContext() {
        loadEnvConfig();
    }

    public static ApplicationContext get() {
        if (instance == null) {
            instance = new ApplicationContext();
        }
        return instance;
    }

    public String getEnvironment() {
        return System.getProperty(ENV_PROPERTY, DEFAULT_ENV).toLowerCase();
    }

    public String getEnvConfigFile() {
        return PATH_CONFIG + "envconfig-" + getEnvironment() + ".yml";
    }

    public void loadEnvConfig() {
        try (FileInputStream inputStream = new FileInputStream(getEnvConfigFile())) {
            envConfig = new Yaml().loadAs(inputStream, EnvConfig.class);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read config file: " + getEnvConfigFile(), e);
        }
        log.debug("Loaded environment configuration: {}", envConfig);
    }

    public void setAuthApiPayload() {
        authPayload = new AuthPayload();
        authPayload.setVendorId(envConfig.getVendorId());
        authPayload.setVendorSecret(envConfig.getVendorSecret());
    }

    public DBAction getDbAction() {
        if (dbAction == null) {
            dbAction = DBConnection.dbConnection().createDatabaseConnection(envConfig);
        }
        return dbAction;
    }

    public static void setRequestSpec() {
        log.info("Setting Request Specification");
        requestSpecification.set(RestAssured.given().baseUri(ApplicationContext.get().getEnvConfig().getBaseUri()).contentType(ContentType.JSON));
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

    public Response sendRequest(String requestType, String uri, int expectedStatusCode) {
        log.info("Sending {} request to {}", requestType, uri);
        try {
            // Validate the request type
            if (!isValidRequestType(requestType)) {
                throw new IllegalArgumentException("Invalid HTTP method: " + requestType);
            }

            // Make the request
            Response response = RestAssured
                    .given()
                    .spec(getRequestSpec())
                    .when()
                    .request(requestType, uri)
                    .then()
                    .extract()
                    .response();

            // Log and validate response
            response.then().statusCode(expectedStatusCode);
            log.info("{} request to {} completed successfully.", requestType, uri);
            return response;
        } catch (Exception e) {
            log.error("Error during {} request to {}", requestType, uri, e);
            throw e;
        }
    }

    private boolean isValidRequestType(String requestType) {
        return switch (requestType) {
            case HttpGet.METHOD_NAME, HttpPost.METHOD_NAME, HttpPut.METHOD_NAME,
                 HttpPatch.METHOD_NAME, HttpDelete.METHOD_NAME -> true;
            default -> false;
        };
    }



}
