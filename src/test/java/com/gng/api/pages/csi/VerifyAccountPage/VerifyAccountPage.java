package com.gng.api.pages.csi.VerifyAccountPage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.VerifyAccount.PreferencesBrokerEnvelope;
import com.gng.api.pojo.CSIPojo.VerifyAccount.SwaggerVerifyAccountRequest;
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
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.VERIFY_ACCOUNT;
import static com.gng.api.constants.ApiEndPoint.VERIFY_ACCOUNT_SWAGGER;
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
        Response response;
        if (VerifyAccountApiHelper.isSwaggerTestCondition(testCondition)) {
            SwaggerVerifyAccountRequest payload = helper.prepareSwaggerPayload();
            helper.prepareSwaggerPayloadForTestCondition(payload, testCondition);
            response = sendSwaggerVerifyAccount(payload);
        } else {
            VerifyAccountRequest payload = helper.preparePreferencesPayload(apiLabel);
            helper.preparePreferencesPayloadForTestCondition(payload, testCondition);
            response = sendPreferencesVerifyAccount(payload);
        }
        VerifyAccountResponse verifyAccountResponse =
                deserializeResponseToPojo(response, VerifyAccountResponse.class);
        testContext.setVerifyAccountResponse(verifyAccountResponse);
        testContext.setResponse(response);
    }

    /** POST WAPI {@code /Accounts/VerifyAccount} with Bearer token (FTD05 TC_206–227). */
    private Response sendSwaggerVerifyAccount(SwaggerVerifyAccountRequest payload) {
        setRequestSpecification(payload, testContext.getAuthToken());
        Response raw = sendRequest(HttpPost.METHOD_NAME, VERIFY_ACCOUNT_SWAGGER, 200);
        try {
            String normalized = normalizeSwaggerResponseBody(raw.asString());
            log.info("Normalized Swagger VerifyAccount response: {}", normalized);
            return new ResponseBuilder()
                    .clone(raw)
                    .setBody(normalized)
                    .build();
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to normalize Swagger VerifyAccount response: " + e.getMessage(), e);
        }
    }

    /**
     * POST Preferences {@code api/requestbroker} with stringified Request
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

    /**
     * Normalize WAPI VerifyAccount body to camelCase with a single {@code data} object.
     * Maps billing* when present; falls back to premises* only when billing* is absent
     * (some builds expose mailing under premises* naming).
     */
    static String normalizeSwaggerResponseBody(String rawBody) throws Exception {
        JsonNode root = MAPPER.readTree(rawBody);
        ObjectNode out = MAPPER.createObjectNode();

        boolean success = bool(root, "success", "Success");
        int errorCode = intVal(root, "errorCode", "ErrorCode");
        out.put("success", success && errorCode == 0);
        out.put("errorCode", errorCode);
        putTextOrNull(out, "errorMessage", text(root, "errorMessage", "ErrorMessage"));
        putTextOrNull(out, "requestID", text(root, "requestID", "RequestID"));

        JsonNode dataNode = root.get("data");
        if (dataNode == null || dataNode.isNull()) {
            dataNode = root.get("Data");
        }
        if (dataNode != null && dataNode.isArray() && !dataNode.isEmpty()) {
            out.set("data", mapSwaggerAccountRow(dataNode.get(0)));
        } else if (dataNode != null && dataNode.isObject()) {
            out.set("data", mapSwaggerAccountRow(dataNode));
        } else {
            out.putNull("data");
        }
        return MAPPER.writeValueAsString(out);
    }

    private static ObjectNode mapSwaggerAccountRow(JsonNode row) {
        ObjectNode data = MAPPER.createObjectNode();
        putTextOrNull(data, "customerCode", text(row, "customerCode", "CustomerCode"));
        putTextOrNull(data, "premisesCode", text(row, "premisesCode", "PremisesCode"));
        putTextOrNull(data, "accountStatus", text(row, "accountStatus", "AccountStatus"));
        putTextOrNull(data, "emailAddress", text(row, "emailAddress", "EmailAddress"));

        putBilling(data, row, "billingStreetNumber",
                "billingStreetNumber", "BillingStreetNumber",
                "premisesStreetNumber", "PremisesStreetNumber");
        putBilling(data, row, "billingStreetPreDirection",
                "billingStreetPreDirection", "BillingStreetPreDirection",
                "premisesStreetPreDirection", "PremisesStreetPreDirection");
        putBilling(data, row, "billingStreetName",
                "billingStreetName", "BillingStreetName",
                "premisesStreetName", "PremisesStreetName");
        putBilling(data, row, "billingStreetSuffix",
                "billingStreetSuffix", "BillingStreetSuffix",
                "premisesStreetSuffix", "PremisesStreetSuffix");
        putBilling(data, row, "billingStreetPostDirection",
                "billingStreetPostDirection", "BillingStreetPostDirection",
                "premisesStreetPostDirection", "PremisesStreetPostDirection");
        putBilling(data, row, "billingUnitType",
                "billingUnitType", "BillingUnitType",
                "premisesUnitType", "PremisesUnitType");
        putBilling(data, row, "billingUnitNumber",
                "billingUnitNumber", "BillingUnitNumber",
                "premisesUnitNumber", "PremisesUnitNumber");
        putBilling(data, row, "billingCity",
                "billingCity", "BillingCity",
                "premisesCity", "PremisesCity");
        String state = firstText(row,
                "billingState", "BillingState", "billingStateCode", "BillingStateCode",
                "premisesState", "PremisesState", "premisesStateCode", "PremisesStateCode");
        putTextOrNull(data, "billingState", state);
        putTextOrNull(data, "billingStateCode", state);
        String zip = firstText(row,
                "billingZip", "BillingZip", "billingZipCode", "BillingZipCode",
                "premisesZip", "PremisesZip", "premisesZipCode", "PremisesZipCode");
        putTextOrNull(data, "billingZip", zip);
        putTextOrNull(data, "billingZipCode", zip);
        putBilling(data, row, "billingPoBox",
                "billingPoBox", "BillingPoBox",
                "premisesPoBox", "PremisesPoBox");

        putTextOrNull(data, "billPresType", text(row, "billPresType", "BillPresType"));
        putTextOrNull(data, "correspondencePreference",
                text(row, "correspondencePreference", "CorrespondencePreference"));
        putTextOrNull(data, "billDeliveryConfirmDate",
                text(row, "billDeliveryConfirmDate", "BillDeliveryConfirmDate"));
        putTextOrNull(data, "corrDeliveryConfirmDate",
                text(row, "corrDeliveryConfirmDate", "CorrDeliveryConfirmDate"));
        return data;
    }

    private static void putBilling(ObjectNode data, JsonNode row, String target, String... keys) {
        putTextOrNull(data, target, firstText(row, keys));
    }

    private static String firstText(JsonNode node, String... keys) {
        return text(node, keys);
    }

    private static boolean bool(JsonNode node, String... keys) {
        for (String key : keys) {
            JsonNode v = node.get(key);
            if (v != null && !v.isNull()) {
                if (v.isBoolean()) {
                    return v.asBoolean();
                }
                String s = v.asText();
                if ("true".equalsIgnoreCase(s) || "1".equals(s)) {
                    return true;
                }
                if ("false".equalsIgnoreCase(s) || "0".equals(s)) {
                    return false;
                }
            }
        }
        return false;
    }

    private static int intVal(JsonNode node, String... keys) {
        for (String key : keys) {
            JsonNode v = node.get(key);
            if (v != null && !v.isNull() && v.isNumber()) {
                return v.asInt();
            }
            if (v != null && !v.isNull() && v.isTextual()) {
                try {
                    return Integer.parseInt(v.asText().trim());
                } catch (NumberFormatException ignored) {
                    // continue
                }
            }
        }
        return 0;
    }

    private static String text(JsonNode node, String... keys) {
        for (String key : keys) {
            JsonNode v = node.get(key);
            if (v != null && !v.isNull() && !v.asText().isBlank()) {
                return v.asText().trim();
            }
        }
        return null;
    }

    private static void putTextOrNull(ObjectNode target, String field, String value) {
        if (value == null || value.isBlank()) {
            target.putNull(field);
        } else {
            target.put(field, value);
        }
    }
}
