package com.gng.api.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pojo.envConfig.EnvConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;

/**
 * Preferences / PEW helpers for FTD05 VerifyAccount.
 * Token flow mirrors Postman {@code AuthenticatePreferences}.
 */
@Slf4j
@UtilityClass
public class PreferencesVerifyAccountUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Pattern AUTH_TOKEN_PATTERN = Pattern.compile(
            "<(?:\\w+:)?AuthenticationToken>(.*?)</(?:\\w+:)?AuthenticationToken>",
            Pattern.DOTALL);
    private static final String SOAP_ACTION =
            "http://vertexone.net/gng/vendorservices/IVertexTokenGeneratorService/GetAuthenticationToken";
    private static final String DEFAULT_TOKEN_URI =
            "https://token-uat1.gng.vertexna.net/VertexTokenGeneratorService.svc";

    private static volatile String cachedToken;

    public static String requirePreferencesBaseUri() {
        EnvConfig env = ApplicationContext.get().getEnvConfig();
        String base = env.getPreferencesBaseUri();
        if (base == null || base.isBlank()) {
            throw new IllegalStateException(
                    "preferencesBaseUri is not set. Expected Postman PreferencesBaseUrl "
                            + "(e.g. https://pref-uat1.gng.vertexna.net/) in envconfig-uat1.yml "
                            + "or -DpreferencesBaseUri=...");
        }
        return base.endsWith("/") ? base : base + "/";
    }

    /**
     * Returns PreferencesAuthToken: env override first, else SOAP AuthenticatePreferences (cached).
     */
    public static String requirePreferencesAuthToken() {
        EnvConfig env = ApplicationContext.get().getEnvConfig();
        String configured = env.getPreferencesAuthToken();
        if (configured != null && !configured.isBlank()) {
            return configured.trim();
        }
        String cached = cachedToken;
        if (cached != null && !cached.isBlank()) {
            return cached;
        }
        synchronized (PreferencesVerifyAccountUtil.class) {
            if (cachedToken != null && !cachedToken.isBlank()) {
                return cachedToken;
            }
            cachedToken = fetchPreferencesAuthToken(env);
            env.setPreferencesAuthToken(cachedToken);
            return cachedToken;
        }
    }

    public static void clearCachedPreferencesAuthToken() {
        cachedToken = null;
    }

    private static String fetchPreferencesAuthToken(EnvConfig env) {
        String tokenUri = firstNonBlank(env.getPreferencesTokenUri(), DEFAULT_TOKEN_URI);
        String username = firstNonBlank(env.getPreferencesTokenUsername(), "gagassave");
        String password = firstNonBlank(env.getPreferencesTokenPassword(), "gagsaveT0612");
        String vendorSource = firstNonBlank(env.getPreferencesVendorSource(), "GAGASSAVE");
        String application = firstNonBlank(env.getPreferencesApplication(), "Preferences");

        String soapBody = """
                <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" \
                xmlns:ven="http://vertexone.net/gng/vendorservices" \
                xmlns:dom="http://domain.vertexone.net">\
                <soapenv:Header/>\
                <soapenv:Body>\
                <ven:GetAuthenticationToken>\
                <ven:VendorTokenInfo>\
                <dom:Application>%s</dom:Application>\
                </ven:VendorTokenInfo>\
                <ven:VendorSource>%s</ven:VendorSource>\
                </ven:GetAuthenticationToken>\
                </soapenv:Body>\
                </soapenv:Envelope>
                """.formatted(application, vendorSource);

        String basic = Base64.getEncoder().encodeToString(
                (username + ":" + password).getBytes(StandardCharsets.UTF_8));

        log.info("Fetching PreferencesAuthToken via SOAP GetAuthenticationToken at {}", tokenUri);
        Response response = given()
                .relaxedHTTPSValidation()
                .header("Authorization", "Basic " + basic)
                .header("SOAPAction", SOAP_ACTION)
                .contentType("text/xml; charset=utf-8")
                .accept(ContentType.XML)
                .body(soapBody)
                .when()
                .post(tokenUri)
                .then()
                .extract()
                .response();

        if (response.statusCode() != 200) {
            throw new IllegalStateException(
                    "Preferences AuthenticatePreferences failed HTTP " + response.statusCode()
                            + ": " + response.asString());
        }

        String body = response.asString();
        Matcher matcher = AUTH_TOKEN_PATTERN.matcher(body);
        if (!matcher.find() || matcher.group(1) == null || matcher.group(1).isBlank()) {
            throw new IllegalStateException(
                    "AuthenticationToken not found in Preferences token response: " + body);
        }
        String token = matcher.group(1).trim();
        log.info("PreferencesAuthToken acquired (length={})", token.length());
        return token;
    }

    private static String firstNonBlank(String... values) {
        if (values == null) {
            return null;
        }
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
        }
        return null;
    }

    /**
     * Probe Preferences VerifyAccount. Returns normalized {@code data} node when ErrorCode=0.
     */
    public static java.util.Optional<ObjectNode> tryVerifyAccount(
            String customerCode, String premisesCode, String emailAddress) {
        if (customerCode == null || premisesCode == null || emailAddress == null
                || emailAddress.isBlank()) {
            return java.util.Optional.empty();
        }
        try {
            String token = requirePreferencesAuthToken();
            String baseUri = requirePreferencesBaseUri();
            ObjectNode inner = MAPPER.createObjectNode();
            inner.put("acctSearchType", "email");
            inner.put("actions", "VerifyAccount");
            inner.put("custCode", customerCode.trim());
            inner.put("premCode", premisesCode.trim());
            inner.put("emailAddress", emailAddress.trim());
            inner.put("Module", "PEW");
            inner.put("authenticationToken", token);
            inner.put("requestID", java.util.UUID.randomUUID().toString());

            ObjectNode envelope = MAPPER.createObjectNode();
            envelope.put("Request", MAPPER.writeValueAsString(inner));

            Response raw = given()
                    .relaxedHTTPSValidation()
                    .contentType(ContentType.JSON)
                    .body(envelope)
                    .when()
                    .post(baseUri + "api/requestbroker")
                    .then()
                    .extract()
                    .response();
            if (raw.statusCode() != 200) {
                log.debug("Preferences probe HTTP {} for {}/{}", raw.statusCode(), customerCode, premisesCode);
                return java.util.Optional.empty();
            }
            JsonNode normalized = MAPPER.readTree(normalizeBrokerResponseBody(raw.asString()));
            int errorCode = normalized.path("errorCode").asInt(-1);
            if (errorCode != 0 || normalized.path("data").isMissingNode() || normalized.path("data").isNull()) {
                log.debug("Preferences probe ErrorCode={} for {}/{} email={}",
                        errorCode, customerCode, premisesCode, emailAddress);
                return java.util.Optional.empty();
            }
            ObjectNode data = (ObjectNode) normalized.get("data");
            log.info("Preferences VerifyAccount probe OK for {}/{} billPresType={} corr={}",
                    customerCode, premisesCode,
                    text(data, "billPresType"), text(data, "correspondencePreference"));
            return java.util.Optional.of(data);
        } catch (Exception ex) {
            log.warn("Preferences probe failed for {}/{}: {}", customerCode, premisesCode, ex.getMessage());
            return java.util.Optional.empty();
        }
    }

    /**
     * Normalize Preferences broker body to WAPI-like camelCase envelope used by steps:
     * {@code { success, errorCode, errorMessage, requestID, data: { ... } }}.
     */
    public static String normalizeBrokerResponseBody(String rawBody) throws Exception {
        JsonNode root = MAPPER.readTree(rawBody);
        if (root.has("Response") && root.get("Response").isTextual()) {
            root = MAPPER.readTree(root.get("Response").asText());
        } else if (root.has("response") && root.get("response").isTextual()) {
            root = MAPPER.readTree(root.get("response").asText());
        }

        if (root.has("VerifyAccount") && root.get("VerifyAccount").isObject()) {
            return MAPPER.writeValueAsString(normalizePreferencesVerifyAccountEnvelope(root));
        }
        return MAPPER.writeValueAsString(toCamelCaseTree(root));
    }

    private static ObjectNode normalizePreferencesVerifyAccountEnvelope(JsonNode root) {
        JsonNode va = root.get("VerifyAccount");
        ObjectNode out = MAPPER.createObjectNode();

        boolean success = textOrBool(va, "Success", "success");
        int errorCode = intOrZero(va, "ErrorCode", "errorCode");
        // Validated=true only means broker accepted the call — Success/ErrorCode are authoritative
        out.put("success", success && errorCode == 0);
        out.put("errorCode", errorCode);
        putTextOrNull(out, "errorMessage", text(va, "ErrorMessage", "errorMessage"));
        putTextOrNull(out, "requestID", text(va, "RequestID", "requestID"));

        JsonNode dataNode = va.get("Data");
        if (dataNode != null && dataNode.isArray() && !dataNode.isEmpty()) {
            out.set("data", mapPreferencesAccountRow(dataNode.get(0)));
        } else if (dataNode != null && dataNode.isObject()) {
            out.set("data", mapPreferencesAccountRow(dataNode));
        } else {
            out.putNull("data");
        }
        return out;
    }

    private static ObjectNode mapPreferencesAccountRow(JsonNode row) {
        ObjectNode data = MAPPER.createObjectNode();
        putTextOrNull(data, "customerCode", text(row, "CustCode", "custCode", "CustomerCode"));
        putTextOrNull(data, "premisesCode", text(row, "PremCode", "premCode", "PremisesCode"));
        putTextOrNull(data, "accountStatus", text(row, "AcctStatus", "acctStatus", "AccountStatus"));
        putTextOrNull(data, "emailAddress", text(row, "EmailAddress", "emailAddress"));

        // Billing address: prefer Billing* fields; fall back to service Street*/Serv*
        putTextOrNull(data, "billingStreetNumber",
                text(row, "BillingStreetNum", "billingStreetNum", "StreetNum", "streetNum"));
        putTextOrNull(data, "billingStreetPreDirection",
                text(row, "BillingStreetPreDir", "billingStreetPreDir", "StreetPreDir", "streetPreDir"));
        putTextOrNull(data, "billingStreetName",
                text(row, "BillingStreetName", "billingStreetName", "StreetName", "streetName"));
        putTextOrNull(data, "billingCity",
                text(row, "BillingCity", "billingCity", "ServCity", "servCity"));
        String state = text(row, "BillingState", "billingState", "ServState", "servState");
        putTextOrNull(data, "billingState", state);
        putTextOrNull(data, "billingStateCode", state);
        String zip = text(row, "BillingZip", "billingZip", "ServZip", "servZip");
        putTextOrNull(data, "billingZip", zip);
        putTextOrNull(data, "billingZipCode", zip);
        putTextOrNull(data, "billingPoBox", text(row, "BillingPoBox", "billingPoBox"));

        putTextOrNull(data, "billPresType", text(row, "BillPresType", "billPresType"));
        putTextOrNull(data, "correspondencePreference",
                text(row, "CorrespondencePreference", "correspondencePreference"));
        putTextOrNull(data, "billDeliveryConfirmDate",
                normalizeYyyyMmDd(text(row, "PaperlessConfDate", "paperlessConfDate", "BillDeliveryConfirmDate")));
        putTextOrNull(data, "corrDeliveryConfirmDate",
                normalizeYyyyMmDd(text(row, "PaperlessCorrConfDate", "paperlessCorrConfDate", "CorrDeliveryConfirmDate")));
        return data;
    }

    private static String normalizeYyyyMmDd(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        String digits = value.replaceAll("\\D", "");
        if (digits.length() >= 8) {
            return digits.substring(0, 8);
        }
        return value.trim();
    }

    private static boolean textOrBool(JsonNode node, String... keys) {
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

    private static int intOrZero(JsonNode node, String... keys) {
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

    private static JsonNode toCamelCaseTree(JsonNode node) {
        if (node == null || node.isNull()) {
            return node;
        }
        if (node.isObject()) {
            ObjectNode out = MAPPER.createObjectNode();
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                out.set(toCamelCase(entry.getKey()), toCamelCaseTree(entry.getValue()));
            }
            return out;
        }
        if (node.isArray()) {
            var arr = MAPPER.createArrayNode();
            node.forEach(child -> arr.add(toCamelCaseTree(child)));
            return arr;
        }
        return node;
    }

    private static String toCamelCase(String key) {
        if (key == null || key.isBlank()) {
            return key;
        }
        if (Character.isLowerCase(key.charAt(0))) {
            return key;
        }
        return key.substring(0, 1).toLowerCase(Locale.ROOT) + key.substring(1);
    }
}
