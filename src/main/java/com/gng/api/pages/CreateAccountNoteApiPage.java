package com.gng.api.pages;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gng.api.context.TestContext;
import com.gng.api.pages.base.CrudOperations;
import com.gng.api.pages.base.IBaseApiStep;
import com.gng.api.pojo.createaccountnote.CreateAccountNoteRequest;
import com.gng.api.pojo.createaccountnote.CreateAccountNoteResponse;
import com.gng.api.util.FakerUtil;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.gng.api.constants.ApiLabel.*;
import static com.gng.api.spec.SetApiSpecification.getRequestSpec;
import static com.gng.api.util.CommonUtil.nullifyFields;
import static com.gng.api.util.LogUtil.logError;
import static com.gng.api.util.LogUtil.logInfo;

public class CreateAccountNoteApiPage extends CrudOperations implements IBaseApiStep<CreateAccountNoteRequest, CreateAccountNoteResponse> {

    private final TestContext testContext;

    public CreateAccountNoteApiPage(TestContext testContext) {
        this.testContext = testContext;
    }

    @Override
    public void setRequestSpecification(String apiName, CreateAccountNoteRequest createAccountNoteRequest) {
        logInfo("Set Request Specification for: " + apiName);
        getRequestSpec().auth().oauth2(testContext.getAuthToken()).body(createAccountNoteRequest);
    }

    @Override
    public CreateAccountNoteRequest getApiPayload(String apiName, CreateAccountNoteRequest createAccountNoteRequest) {
        logInfo("Get Api Payload");
        switch (apiName) {
            case CREATEACCOUNTNOTE_HAPPYFLOW_API:
                createAccountNoteRequest.setRequestID(UUID.randomUUID().toString());
                createAccountNoteRequest.setCustomerCode(testContext.getCustomerCode());
                createAccountNoteRequest.setPremisesCode(testContext.getPremisesCode());
                createAccountNoteRequest.setServiceNumber(testContext.getServiceNumber());
                createAccountNoteRequest.setNoteTypeCode(testContext.getNoteTypeCode());
                createAccountNoteRequest.setNoteText(testContext.getNoteText());
                createAccountNoteRequest.setOrigin(testContext.getOrigin());
             /* createAccountNoteRequest.setExpirationDate(testContext.getExpirationDate());
                createAccountNoteRequest.setSuspenseDate(testContext.getSuspenseDate());
                createAccountNoteRequest.setUserIDRemind(testContext.getUserIDRemind());*/
                return createAccountNoteRequest;
            case CREATEACCOUNTNOTE_MISSINGREQUESTID_API:
                nullifyFields(createAccountNoteRequest, "requestID");
                createAccountNoteRequest.setCustomerCode(testContext.getCustomerCode());
                createAccountNoteRequest.setPremisesCode(testContext.getPremisesCode());
                createAccountNoteRequest.setServiceNumber(testContext.getServiceNumber());
                createAccountNoteRequest.setNoteTypeCode(testContext.getNoteTypeCode());
                createAccountNoteRequest.setNoteText(testContext.getNoteText());
                createAccountNoteRequest.setOrigin(testContext.getOrigin());
                return createAccountNoteRequest;
            case GETACCOUNTINFO_MISSINGPREMCODE_API:
                createAccountNoteRequest.setRequestID(UUID.randomUUID().toString());
                createAccountNoteRequest.setCustomerCode(testContext.getCustomerCode());
                nullifyFields(createAccountNoteRequest, "premisesCode");
                return createAccountNoteRequest;
            case GETACCOUNTINFO_MISSINGCUSTOMERCODE_API:
                createAccountNoteRequest.setRequestID(UUID.randomUUID().toString());
                nullifyFields(createAccountNoteRequest, "customerCode");
                createAccountNoteRequest.setPremisesCode(testContext.getPremisesCode());
                return createAccountNoteRequest;
            case GETACCOUNTINFO_NONEXISTENT_CUSTPREMCODE_API:
                createAccountNoteRequest.setRequestID(UUID.randomUUID().toString());
                createAccountNoteRequest.setCustomerCode(FakerUtil.getRandomNumericString(8));
                createAccountNoteRequest.setPremisesCode(testContext.getPremisesCode());
                createAccountNoteRequest.setServiceNumber(testContext.getServiceNumber());
                createAccountNoteRequest.setNoteTypeCode(testContext.getNoteTypeCode());
                createAccountNoteRequest.setNoteText(testContext.getNoteText());
                createAccountNoteRequest.setOrigin(testContext.getOrigin());
                return createAccountNoteRequest;
            case CREATEACCOUNTNOTE_NULLCUSTOMERCODE_API:
                createAccountNoteRequest.setRequestID(UUID.randomUUID().toString());
                createAccountNoteRequest.setCustomerCode(null);
                createAccountNoteRequest.setPremisesCode(testContext.getPremisesCode());
                createAccountNoteRequest.setServiceNumber(testContext.getServiceNumber());
                createAccountNoteRequest.setNoteTypeCode(testContext.getNoteTypeCode());
                createAccountNoteRequest.setNoteText(testContext.getNoteText());
                createAccountNoteRequest.setOrigin(testContext.getOrigin());
                return createAccountNoteRequest;
            case CREATEACCOUNTNOTE_NULLNOTETYPECODE_API:
                createAccountNoteRequest.setRequestID(UUID.randomUUID().toString());
                createAccountNoteRequest.setCustomerCode(testContext.getCustomerCode());
                createAccountNoteRequest.setPremisesCode(testContext.getPremisesCode());
                createAccountNoteRequest.setServiceNumber(testContext.getServiceNumber());
                createAccountNoteRequest.setNoteTypeCode(null);
                createAccountNoteRequest.setNoteText(testContext.getNoteText());
                createAccountNoteRequest.setOrigin(testContext.getOrigin());
                return createAccountNoteRequest;
            case CREATEACCOUNTNOTE_NULLNOTETEXT_API:
                createAccountNoteRequest.setRequestID(UUID.randomUUID().toString());
                createAccountNoteRequest.setCustomerCode(testContext.getCustomerCode());
                createAccountNoteRequest.setPremisesCode(testContext.getPremisesCode());
                createAccountNoteRequest.setServiceNumber(testContext.getServiceNumber());
                createAccountNoteRequest.setNoteTypeCode(testContext.getNoteTypeCode());
                createAccountNoteRequest.setNoteText(null);
                createAccountNoteRequest.setOrigin(testContext.getOrigin());
                return createAccountNoteRequest;
            case CREATEACCOUNTNOTE_NULLORIGIN_API:
                createAccountNoteRequest.setRequestID(UUID.randomUUID().toString());
                createAccountNoteRequest.setCustomerCode(testContext.getCustomerCode());
                createAccountNoteRequest.setPremisesCode(testContext.getPremisesCode());
                createAccountNoteRequest.setServiceNumber(testContext.getServiceNumber());
                createAccountNoteRequest.setNoteTypeCode(testContext.getNoteTypeCode());
                createAccountNoteRequest.setNoteText(testContext.getNoteText());
                createAccountNoteRequest.setOrigin(null);
                return createAccountNoteRequest;
            case CREATEACCOUNTNOTE_INVALIDCUSTOMERCODELENGTH_API:
                createAccountNoteRequest.setRequestID(UUID.randomUUID().toString());
                createAccountNoteRequest.setCustomerCode(FakerUtil.getRandomNumericString(10));
                createAccountNoteRequest.setPremisesCode(testContext.getPremisesCode());
                createAccountNoteRequest.setServiceNumber(testContext.getServiceNumber());
                createAccountNoteRequest.setNoteTypeCode(testContext.getNoteTypeCode());
                createAccountNoteRequest.setNoteText(testContext.getNoteText());
                createAccountNoteRequest.setOrigin(testContext.getOrigin());
                return createAccountNoteRequest;
            case CREATEACCOUNTNOTE_INVALIDPREMCODELENGTH_API:
                createAccountNoteRequest.setRequestID(UUID.randomUUID().toString());
                createAccountNoteRequest.setCustomerCode(testContext.getCustomerCode());
                createAccountNoteRequest.setPremisesCode(FakerUtil.getRandomNumericString(8));
                createAccountNoteRequest.setServiceNumber(testContext.getServiceNumber());
                createAccountNoteRequest.setNoteTypeCode(testContext.getNoteTypeCode());
                createAccountNoteRequest.setNoteText(testContext.getNoteText());
                createAccountNoteRequest.setOrigin(testContext.getOrigin());
                return createAccountNoteRequest;
            case CREATEACCOUNTNOTE_INVALIDEXPIRATIONDATE_API:
                createAccountNoteRequest.setRequestID(UUID.randomUUID().toString());
                createAccountNoteRequest.setCustomerCode(testContext.getCustomerCode());
                createAccountNoteRequest.setPremisesCode(testContext.getPremisesCode());
                createAccountNoteRequest.setServiceNumber(testContext.getServiceNumber());
                createAccountNoteRequest.setNoteTypeCode(testContext.getNoteTypeCode());
                createAccountNoteRequest.setNoteText(testContext.getNoteText());
                createAccountNoteRequest.setOrigin(testContext.getOrigin());
                createAccountNoteRequest.setExpirationDate(testContext.getExpirationDate());
                return createAccountNoteRequest;
            case CREATEACCOUNTNOTE_NONEXISTENT_SERVICENOPREMCODE_API:
                createAccountNoteRequest.setRequestID(UUID.randomUUID().toString());
                createAccountNoteRequest.setCustomerCode(testContext.getCustomerCode());
                createAccountNoteRequest.setPremisesCode(testContext.getPremisesCode());
                createAccountNoteRequest.setServiceNumber(FakerUtil.getRandomNumericString(4));
                createAccountNoteRequest.setNoteTypeCode(testContext.getNoteTypeCode());
                createAccountNoteRequest.setNoteText(testContext.getNoteText());
                createAccountNoteRequest.setOrigin(testContext.getOrigin());
                return createAccountNoteRequest;
            case CREATEACCOUNTNOTE_INVALID_SERVICENOFORMAT_API:
                createAccountNoteRequest.setRequestID(UUID.randomUUID().toString());
                createAccountNoteRequest.setCustomerCode(testContext.getCustomerCode());
                createAccountNoteRequest.setPremisesCode(testContext.getPremisesCode());
                createAccountNoteRequest.setServiceNumber(FakerUtil.getRandomNumericString(5));
                createAccountNoteRequest.setNoteTypeCode(testContext.getNoteTypeCode());
                createAccountNoteRequest.setNoteText(testContext.getNoteText());
                createAccountNoteRequest.setOrigin(testContext.getOrigin());
                return createAccountNoteRequest;
            case CREATEACCOUNTNOTE_NONEXISTENT_NOTETYPE_API:
                createAccountNoteRequest.setRequestID(UUID.randomUUID().toString());
                createAccountNoteRequest.setCustomerCode(testContext.getCustomerCode());
                createAccountNoteRequest.setPremisesCode(testContext.getPremisesCode());
                createAccountNoteRequest.setServiceNumber(testContext.getServiceNumber());
                createAccountNoteRequest.setNoteTypeCode(FakerUtil.getRandomString(3));
                createAccountNoteRequest.setNoteText(testContext.getNoteText());
                createAccountNoteRequest.setOrigin(testContext.getOrigin());
                return createAccountNoteRequest;
            default:
                throw new IllegalStateException(UNEXPECTED_VALUE + apiName);
        }
    }

    @Override
    public CreateAccountNoteRequest deserializeJsonToPojo(String apiName) {
        logInfo("Deserializing Json to Pojo");
        return null;
    }

    @Override
    public CreateAccountNoteResponse deserializeResponseToPojo(String apiName, Response response) {
        logInfo("Deserialize Response To Pojo");
        JsonFactory factory = JsonFactory.builder()
                .enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION)
                .build();
        ObjectMapper mapper = new ObjectMapper(factory);
        try {
            return switch (apiName) {
                case GETACCOUNTINFO_HAPPYFLOW_API ->
                        mapper.readValue(response.getBody().asString(), CreateAccountNoteResponse.class);
                default -> throw new IllegalStateException(UNEXPECTED_VALUE + apiName);
            };
        } catch (JsonProcessingException e) {
            logError(e.getMessage());
        }
        return null;
    }

    @Override
    public Map<String, String> getApiHeaders(String apiName) {
        logInfo("Get Api Headers");
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization", "Bearer " + testContext.getAuthToken());
        return requestHeaders;
    }

    @Override
    public String getApiQueryParams(String apiName) {
        logInfo("Get Api Params");
        return null;
    }
}
