package com.gng.api.pages.csi.VerifyAccountPage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.VerifyAccount.PreferencesBrokerEnvelope;
import com.gng.api.pojo.CSIPojo.VerifyAccount.VerifyAccountRequest;
import com.gng.api.pojo.CSIPojo.VerifyAccount.VerifyAccountResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.report.DetailedExtentReportManager;
import com.gng.api.report.ExtentReportManager;
import com.gng.api.report.SimplifiedExtentReportManager;
import com.gng.api.steps.csi.VerifyAccount.VerifyAccountLabel;
import com.gng.api.util.PreferencesVerifyAccountUtil;
import io.restassured.builder.ResponseBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import static com.gng.api.constants.ApiEndPoint.VERIFY_ACCOUNT;
import static io.restassured.RestAssured.given;

@Slf4j
public class VerifyAccountPage extends BasePage {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final VerifyAccountApiHelper helper;

    public VerifyAccountPage(TestContext testContext) {
        super(testContext);
        this.helper = new VerifyAccountApiHelper(testContext);
    }

    public void validateResponseForTestCondition(VerifyAccountLabel apiLabel,
                                                 VerifyAccountLabel testCondition) {
        VerifyAccountRequest payload = helper.preparePayload(apiLabel);
        helper.preparePayloadForTestCondition(payload, testCondition);
        Response response = sendPreferencesVerifyAccount(payload);
        VerifyAccountResponse verifyAccountResponse =
                deserializeResponseToPojo(response, VerifyAccountResponse.class);
        testContext.setVerifyAccountResponse(verifyAccountResponse);
        testContext.setResponse(response);
    }

    /**
     * POST https://pref-uat1.../api/requestbroker with stringified Request
     * (actions=VerifyAccount, Module=PEW). Auth via PreferencesAuthToken inside Request.
     */
    private Response sendPreferencesVerifyAccount(VerifyAccountRequest inner) {
        String baseUri = PreferencesVerifyAccountUtil.requirePreferencesBaseUri();
        String url = baseUri + VERIFY_ACCOUNT;
        try {
            String requestJson = MAPPER.writeValueAsString(inner);
            PreferencesBrokerEnvelope envelope = PreferencesBrokerEnvelope.builder()
                    .request(requestJson)
                    .build();

            log.info("Sending Preferences VerifyAccount to {} (custCode={}, premCode={})",
                    url, inner.getCustCode(), inner.getPremCode());

            Response raw = given()
                    .relaxedHTTPSValidation()
                    .contentType(ContentType.JSON)
                    .body(envelope)
                    .when()
                    .post(url)
                    .then()
                    .extract()
                    .response();

            SimplifiedExtentReportManager.addRequestDetailsToReport(
                    given().contentType(ContentType.JSON).baseUri(baseUri).body(envelope));
            DetailedExtentReportManager.addRequestDetailsToReport(
                    given().contentType(ContentType.JSON).baseUri(baseUri).body(envelope));
            ExtentReportManager.addResponseDetailsToReport(raw, 200);
            DetailedExtentReportManager.addResponseDetailsToReport(raw, 200);

            raw.then().statusCode(200);

            String normalized = PreferencesVerifyAccountUtil.normalizeBrokerResponseBody(raw.asString());
            log.info("Normalized Preferences VerifyAccount response: {}", normalized);

            return new ResponseBuilder()
                    .clone(raw)
                    .setBody(normalized)
                    .build();
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Preferences VerifyAccount call failed for " + url + ": " + e.getMessage(), e);
        }
    }
}
