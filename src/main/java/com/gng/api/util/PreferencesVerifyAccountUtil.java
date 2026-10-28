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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Base64;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;

/**
 * Preferences / PEW helpers for FTD05 VerifyAccount.
 * Token flow uses SOAP AuthenticatePreferences ({@code GetAuthenticationToken}).
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

    /**
     * Re-fetch the SOAP token after this long. The broker token outlives a single API call but
     * not a full suite run, and nothing in the flow detects an aged-out token — a proactive TTL
     * keeps long runs from failing every remaining Preferences TC on a stale token.
     */
    private static final long TOKEN_TTL_MILLIS = 15L * 60L * 1000L;

    private static volatile String cachedToken;
    private static volatile long cachedTokenAtMillis;

    public static String requirePreferencesBaseUri() {
        EnvConfig env = ApplicationContext.get().getEnvConfig();
        String base = env.getPreferencesBaseUri();
        if (base == null || base.isBlank()) {
            throw new IllegalStateException(
                    "preferencesBaseUri is not set. Expected PreferencesBaseUrl "
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
        // A token we published back to EnvConfig ourselves must still honour the TTL below;
        // only an operator-supplied PreferencesAuthToken short-circuits the refresh.
        if (configured != null && !configured.isBlank() && !configured.trim().equals(cachedToken)) {
            return configured.trim();
        }
        String cached = cachedToken;
        if (cached != null && !cached.isBlank() && !isTokenStale()) {
            return cached;
        }
        synchronized (PreferencesVerifyAccountUtil.class) {
            if (cachedToken != null && !cachedToken.isBlank() && !isTokenStale()) {
                return cachedToken;
            }
            cachedToken = fetchPreferencesAuthToken(env);
            cachedTokenAtMillis = System.currentTimeMillis();
            env.setPreferencesAuthToken(cachedToken);
            return cachedToken;
        }
    }

    private static boolean isTokenStale() {
        return System.currentTimeMillis() - cachedTokenAtMillis > TOKEN_TTL_MILLIS;
    }

    /**
     * Drops the cached token and fetches a fresh one. Used when a Preferences call comes back
     * HTTP 401/403, so an aged-out token costs one retry instead of the rest of the run.
     */
    public static String refreshPreferencesAuthToken() {
        synchronized (PreferencesVerifyAccountUtil.class) {
            clearCachedPreferencesAuthToken();
            EnvConfig env = ApplicationContext.get().getEnvConfig();
            env.setPreferencesAuthToken(null);
            return requirePreferencesAuthToken();
        }
    }

    public static void clearCachedPreferencesAuthToken() {
        cachedToken = null;
        cachedTokenAtMillis = 0L;
    }

    /** HTTP statuses that mean "token rejected", as opposed to a business ErrorCode. */
    public static boolean isAuthRejection(int statusCode) {
        return statusCode == 401 || statusCode == 403;
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
            String baseUri = requirePreferencesBaseUri();
            Response raw = postVerifyAccountProbe(
                    baseUri, requirePreferencesAuthToken(), customerCode, premisesCode, emailAddress);
            if (isAuthRejection(raw.statusCode())) {
                log.info("Preferences probe HTTP {} — refreshing auth token and retrying once",
                        raw.statusCode());
                raw = postVerifyAccountProbe(
                        baseUri, refreshPreferencesAuthToken(), customerCode, premisesCode, emailAddress);
            }
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

    private static Response postVerifyAccountProbe(String baseUri,
                                                   String token,
                                                   String customerCode,
                                                   String premisesCode,
                                                   String emailAddress) throws Exception {
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

        return given()
                .relaxedHTTPSValidation()
                .contentType(ContentType.JSON)
                .body(envelope)
                .when()
                .post(baseUri + "api/requestbroker")
                .then()
                .extract()
                .response();
    }

    /**
     * Probe WAPI Swagger {@code /Accounts/VerifyAccount}. Returns data when ErrorCode=0.
     */
    public static java.util.Optional<ObjectNode> trySwaggerVerifyAccount(
            String baseUri,
            String bearerToken,
            String customerCode,
            String premisesCode,
            String emailAddress) {
        if (baseUri == null || bearerToken == null || customerCode == null || premisesCode == null
                || emailAddress == null || emailAddress.isBlank()) {
            return java.util.Optional.empty();
        }
        try {
            String uri = baseUri.endsWith("/") ? baseUri.substring(0, baseUri.length() - 1) : baseUri;
            ObjectNode body = MAPPER.createObjectNode();
            body.put("requestID", java.util.UUID.randomUUID().toString().replace("-", ""));
            body.put("verifyType", "email");
            body.put("customerCode", customerCode.trim());
            body.put("premisesCode", premisesCode.trim());
            body.put("emailAddress", emailAddress.trim());
            body.put("origin", "Macquarium");
            body.put("loginID", "autotester");

            Response raw = given()
                    .relaxedHTTPSValidation()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + bearerToken.trim())
                    .body(body)
                    .when()
                    .post(uri + "/Accounts/VerifyAccount")
                    .then()
                    .extract()
                    .response();
            if (raw.statusCode() != 200) {
                log.debug("Swagger probe HTTP {} for {}/{}", raw.statusCode(), customerCode, premisesCode);
                return java.util.Optional.empty();
            }
            JsonNode root = MAPPER.readTree(raw.asString());
            int errorCode = root.path("errorCode").asInt(root.path("ErrorCode").asInt(-1));
            if (errorCode != 0) {
                log.debug("Swagger probe ErrorCode={} for {}/{} email={}",
                        errorCode, customerCode, premisesCode, emailAddress);
                return java.util.Optional.empty();
            }
            JsonNode data = root.path("data");
            if (data.isMissingNode() || data.isNull()) {
                return java.util.Optional.empty();
            }
            log.info("Swagger VerifyAccount probe OK for {}/{}", customerCode, premisesCode);
            return java.util.Optional.of((ObjectNode) data);
        } catch (Exception ex) {
            log.warn("Swagger probe failed for {}/{}: {}", customerCode, premisesCode, ex.getMessage());
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

        // Billing address: Billing* only. Do not fall back to service Street*/Serv*/Unit*
        // (null Billing* must stay blank for TC_228 GTBENRL source checks).
        putTextOrNull(data, "billingStreetNumber",
                text(row, "BillingStreetNum", "billingStreetNum", "BillingStreetNumber",
                        "billingStreetNumber"));
        putTextOrNull(data, "billingStreetPreDirection",
                text(row, "BillingStreetPreDir", "billingStreetPreDir", "BillingStreetPreDirection",
                        "billingStreetPreDirection"));
        putTextOrNull(data, "billingStreetName",
                text(row, "BillingStreetName", "billingStreetName"));
        putTextOrNull(data, "billingStreetSuffix",
                text(row, "BillingStreetSuffix", "billingStreetSuffix",
                        "BillingStreetSfx", "billingStreetSfx"));
        putTextOrNull(data, "billingStreetPostDirection",
                text(row, "BillingStreetPostDir", "billingStreetPostDir", "BillingStreetPostDirection",
                        "billingStreetPostDirection"));
        putTextOrNull(data, "billingUnitType",
                text(row, "BillingUnitType", "billingUnitType"));
        putTextOrNull(data, "billingUnitNumber",
                text(row, "BillingUnitNum", "billingUnitNum", "BillingUnitNumber", "billingUnitNumber"));
        putTextOrNull(data, "billingCity", text(row, "BillingCity", "billingCity"));
        String state = text(row, "BillingState", "billingState");
        putTextOrNull(data, "billingState", state);
        putTextOrNull(data, "billingStateCode", state);
        String zip = text(row, "BillingZip", "billingZip");
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
        String trimmed = value.trim()
                .replace('\u2013', '-')
                .replace('\u2014', '-')
                .replace('/', '-');
        if (trimmed.matches("\\d{8}")) {
            return trimmed;
        }
        String digits = trimmed.replaceAll("\\D", "");
        if (digits.length() >= 8 && !trimmed.matches("(?i).*[A-Z].*")) {
            return digits.substring(0, 8);
        }
        // Preferences PEW often returns Oracle-style dates (e.g. 15-MAR-26)
        String upper = trimmed.toUpperCase(Locale.ENGLISH);
        String[] patterns = {"d-MMM-yy", "dd-MMM-yy", "d-MMM-yyyy", "dd-MMM-yyyy",
                "yyyy-MM-dd", "M-d-yyyy", "MM-dd-yyyy"};
        for (String pattern : patterns) {
            try {
                java.text.SimpleDateFormat in =
                        new java.text.SimpleDateFormat(pattern, Locale.ENGLISH);
                in.setLenient(true);
                java.text.SimpleDateFormat out = new java.text.SimpleDateFormat("yyyyMMdd");
                return out.format(in.parse(upper));
            } catch (Exception ignored) {
                // try next pattern
            }
        }
        for (DateTimeFormatter formatter : new DateTimeFormatter[]{
                DateTimeFormatter.ofPattern("d-MMM-yy", Locale.ENGLISH),
                DateTimeFormatter.ofPattern("dd-MMM-yy", Locale.ENGLISH),
                DateTimeFormatter.ofPattern("d-MMM-yyyy", Locale.ENGLISH),
                DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH),
                DateTimeFormatter.ISO_LOCAL_DATE
        }) {
            try {
                return LocalDate.parse(upper, formatter)
                        .format(DateTimeFormatter.BASIC_ISO_DATE);
            } catch (DateTimeParseException ignored) {
                // try next pattern
            }
        }
        log.warn("Unable to normalize confirm date '{}' to YYYYMMDD", value);
        return trimmed;
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
