package com.gng.api.steps.AesEncryption;

import com.gng.api.constants.ApiEndPoint;
import com.gng.api.context.ApplicationContext;
import com.gng.api.report.ExtentReportManager;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AesEncryptionSteps {

    @Step("Encrypting data using AES Encryption API")
    public static String encryptData(String data) {
        return processAesRequest(data, ApiEndPoint.AES_ENCRYPTION, "Encryption");
    }

    @Step("Decrypting data using AES Decryption API")
    public static String decryptData(String encryptedData) {
        return processAesRequest(encryptedData, ApiEndPoint.AES_DECRYPTION, "Decryption");
    }

    /**
     * Common method to handle AES encryption & decryption API calls.
     */
    private static String processAesRequest(String data, String endPoint, String action) {
        log.info("{} data using AES {} API...", action, action);

        String uri = ApplicationContext.get().getEnvConfig().getAesBaseUri() + endPoint;

        Map<String, String> payload = new HashMap<>();
        payload.put("DataToEncryptOrDecrypt", data);

        // Logging request details
        ExtentReportManager.logInfoToReport(action + " Request - URI: " + uri);
        ExtentReportManager.logInfoToReport("Payload: " + payload);

        Response response = RestAssured.given()
                .relaxedHTTPSValidation()
                .contentType(ContentType.JSON)
                .body(payload)
                .post(uri)
                .then()
                .extract()
                .response();

        // Log API request and response details
        ExtentReportManager.addRequestDetailsToReport(RestAssured.given().contentType(ContentType.JSON).body(payload));
        ExtentReportManager.addResponseDetailsToReport(response, 200);

        if (response.getStatusCode() == 200) {
            String resultData = response.jsonPath().getString("encryptedDecryptedData");

            if (resultData == null || resultData.isEmpty()) {
                log.error("{} failed: Response does not contain 'encryptedDecryptedData'", action);
                String errorMessage = action + " API response does not contain 'encryptedDecryptedData'. Response: " + response.getBody().asString();

                // Log error details
                ExtentReportManager.logErrorToReport(errorMessage);
                attachToAllureReport(action + " Error", errorMessage);

                throw new RuntimeException(errorMessage);
            }

            log.info("{} successful. Result Data: {}", action, resultData);

            // Log success details
            ExtentReportManager.logInfoToReport(action + " Successful. Result Data: " + resultData);
            attachToAllureReport(action + " Response", resultData);

            return resultData;
        } else {
            log.error("{} failed with status code: {}", action, response.getStatusCode());
            String errorMessage = action + " API call failed: " + response.getBody().asString();

            // Log failure details
            ExtentReportManager.logErrorToReport(errorMessage);
            attachToAllureReport(action + " Error", errorMessage);

            throw new RuntimeException(errorMessage);
        }
    }

    @Step("{title}")
    public static void attachToAllureReport(String title, String content) {
        if (content == null || content.isEmpty()) {
            log.warn("Skipping Allure attachment: {} (content is null or empty)", title);
            return;
        }
        io.qameta.allure.Allure.addAttachment(title, content);
    }
}
