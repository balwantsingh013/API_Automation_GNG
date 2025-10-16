package com.gng.api.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pojo.TestContext.TestContext;
import io.restassured.response.Response;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import static io.restassured.RestAssured.given;
import static com.gng.api.util.LogUtil.logError;

@Slf4j
public class CommonUtil {

    private CommonUtil()
    {

    }

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

    public static String getCurrentDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HHmmss");
        return LocalDateTime.now().format(formatter);
    }

    public static void silentlyGenerateAuthToken() {
        try {
            ApplicationContext runContext = ApplicationContext.get();
            runContext.setAuthApiPayload();
            Response response = executeAuthRequest(runContext);
            storeAuthToken(response);
        } catch (Exception e) {
            handleException(e);
        }
    }

    public static Response executeAuthRequest(ApplicationContext runContext) {
        Response response = given()
                .relaxedHTTPSValidation()
                .contentType(ContentType.JSON)
                .baseUri(runContext.getEnvConfig().getBaseUri())
                .body(runContext.getAuthPayload())
                .post(runContext.getEnvConfig().getAuthUri())
                .then().extract().response();

        // Store response in shared TestContext
        TestContextHolder.get().setResponse(response);
        return response;
    }


    public static void storeAuthToken(Response response) {
        TestContext context = TestContextHolder.get();
        context.setResponse(response);
        context.setAuthToken(response.jsonPath().getString("token"));
    }

    public static void handleException(Exception e) {
        TestContext context = TestContextHolder.get();
        logError("Error occurred: " + e.getMessage());
        String responseDetails = context.getResponse() != null ? context.getResponse().prettyPrint() : "No response received";
        throw new IllegalStateException(e.getMessage() + "\n" + responseDetails, e);
    }


    public static String getCurrentDateTimeFormatted() {
        // Format: dd-MM-yyyy HH:mm:ss (e.g., 10-07-2025 15:42:10)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }


    public static String removeFieldFromJson(Object object, String fieldToRemove) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            // Convert the object to a Map
            Map<String, Object> objectMap = mapper.convertValue(object, Map.class);

            // Remove the field dynamically
            objectMap.remove(fieldToRemove);

            // Convert back to JSON string
            return mapper.writeValueAsString(objectMap);
        } catch (Exception e) {
            log.error("Failed to remove field from JSON: {}", e.getMessage(), e);
            return null;
        }
    }

    public static String removeFieldsFromJson(Object object, String fieldsToRemove) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            // Convert the object to a Map
            Map<String, Object> objectMap = mapper.convertValue(object, Map.class);

            // Remove multiple fields dynamically
            Arrays.stream(fieldsToRemove.split(","))
                    .map(String::trim)  // Trim spaces to handle "field1, field2"
                    .forEach(objectMap::remove);

            // Convert back to JSON string
            return mapper.writeValueAsString(objectMap);
        } catch (Exception e) {
            log.error("Failed to remove fields from JSON: {}", e.getMessage(), e);
            return null;
        }
    }

}

