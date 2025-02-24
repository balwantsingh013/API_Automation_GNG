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
        log.info("Encrypting data using AES Encryption API...");

        String uri = ApplicationContext.get().getEnvConfig().getAesBaseUri() + ApiEndPoint.AES_ENCRYPTION;

        Map<String, String> payload = new HashMap<>();
        payload.put("DataToEncryptOrDecrypt", data);

        // Logging request to Extent Reports
        ExtentReportManager.logInfoToReport("Encryption Request - URI: " + uri);
        ExtentReportManager.logInfoToReport("Payload: " + payload);

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(payload)
                .post(uri)
                .then()
                .extract()
                .response();

        // Log API request details
        ExtentReportManager.addRequestDetailsToReport(RestAssured.given().contentType(ContentType.JSON).body(payload));
        ExtentReportManager.addResponseDetailsToReport(response, 200);

        if (response.getStatusCode() == 200) {
            String encryptedData = response.jsonPath().getString("encryptedDecryptedData");

            if (encryptedData == null || encryptedData.isEmpty()) {
                log.error("Encryption failed: Response does not contain 'encryptedData'");
                String errorMessage = "Encryption API response does not contain 'encryptedData'. Response: " + response.getBody().asString();

                // Log error to Extent Report
                ExtentReportManager.logErrorToReport(errorMessage);
                attachToAllureReport("Encryption Error", errorMessage);

                throw new RuntimeException(errorMessage);
            }

            log.info("Encryption successful. Encrypted Data: " + encryptedData);

            // Log response details to Extent and Allure
            ExtentReportManager.logInfoToReport("Encryption Successful. Encrypted Data: " + encryptedData);
            attachToAllureReport("Encryption Response", encryptedData);

            return encryptedData;
        } else {
            log.error("Encryption failed with status code: " + response.getStatusCode());
            String errorMessage = "Encryption API call failed: " + response.getBody().asString();

            // Log failure to Extent and Allure
            ExtentReportManager.logErrorToReport(errorMessage);
            attachToAllureReport("Encryption Error", errorMessage);

            throw new RuntimeException(errorMessage);
        }
    }

    @Step("{title}")
    public static void attachToAllureReport(String title, String content) {
        if (content == null || content.isEmpty()) {
            log.warn("Skipping Allure attachment: " + title + " (content is null or empty)");
            return;
        }
        io.qameta.allure.Allure.addAttachment(title, content);
    }
}
