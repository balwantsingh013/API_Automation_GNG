package com.gng.api.util;

import com.github.javafaker.Faker;
import com.gng.api.report.ExtentReportManager;
import io.restassured.response.Response;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.*;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.gng.api.config.LogConfig.logInfo;
import static com.gng.api.spec.SetApiSpecification.getRequestSpec;
import static io.restassured.RestAssured.given;


@Slf4j
public class CommonUtil {

    public static Faker faker;
    public static final String UNEXPECTED_VALUE = "Unexpected value: ";

    public Response sendRequest(String requestType, String uri, int expectedStatusCode) {
        logInfo("Sending " + requestType + " request to " + uri);

        try {
            // Validate the request type (optional)
            if (!isValidRequestType(requestType)) {
                throw new IllegalArgumentException("Invalid HTTP method: " + requestType);
            }

            // Make the request
            Response response = given()
                    .when()
                    .spec(getRequestSpec())
                    .request(requestType, uri)
                    .then()
                    .extract()
                    .response();

            // Log and validate the response
            ExtentReportManager.addResponseDetailsToReport(response, expectedStatusCode);
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

    /**
     * Use Reflection to Dynamically Nullify Specified Fields.
     * Traverses the class hierarchy to find and nullify the specified fields.
     *
     * @param object          The object whose fields need to be nullified.
     * @param fieldsToNullify The names of the fields to nullify.
     */
    public static void nullifyFields(Object object, String... fieldsToNullify) {
        if (object == null || fieldsToNullify == null) {
            log.warn("Object or fieldsToNullify is null. Skipping nullifyFields.");
            return;
        }

        Set<String> fieldsSet = new HashSet<>(Set.of(fieldsToNullify));

        try {
            Class<?> clazz = object.getClass();
            while (clazz != null) { // Traverse the class hierarchy
                for (Field field : clazz.getDeclaredFields()) {
                    if (fieldsSet.contains(field.getName())) {
                        field.setAccessible(true);
                        if (!field.getType().isPrimitive()) {
                            field.set(object, null);
                        } else {
                            log.warn("Cannot nullify primitive field: {}", field.getName());
                        }
                    }
                }
                clazz = clazz.getSuperclass(); // Move to superclass
            }
        } catch (IllegalAccessException e) {
            log.error("Failed to nullify field: {}", e.getMessage(), e);
        }
    }

    /**
     * Get the current date and time in a thread-safe manner.
     *
     * @return The current date and time formatted as dd-MM-yyyy-hhmmss.
     */
    public static String getCurrentDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HHmmss");
        return LocalDateTime.now().format(formatter);
    }


    private static Faker getFaker() {
        faker = Optional.ofNullable(faker).orElseGet(Faker::new);
        return faker;
    }

    public static String getRandomNumericString(int length) {
        String value = String.valueOf(getFaker().number().digits(length));
        log.info("Random Numeric String: {}", value);
        return value;
    }

    public static String getRandomString(int length) {
        String value = getFaker().lorem().characters(length);
        log.info("Random String: {}", value);
        return value;
    }
}
