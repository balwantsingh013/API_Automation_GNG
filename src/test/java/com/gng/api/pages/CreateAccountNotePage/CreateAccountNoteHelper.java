package com.gng.api.pages.CreateAccountNotePage;

import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.pojo.createaccountnote.CreateAccountNoteRequest;
import com.gng.api.util.CommonUtil;
import io.cucumber.datatable.DataTable;
import static com.gng.api.util.LogUtil.logInfo;
import static com.gng.api.pages.CreateAccountNotePage.CreateAccountNoteLabels.*;

import java.util.Map;
import java.util.UUID;

public class CreateAccountNoteHelper {
    private final TestContext testContext;

    public CreateAccountNoteHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    public void setupRequestData(DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        setTestContextData(data);
    }

    public CreateAccountNoteLabels getApiLabelForParam(String param) {
        return switch (param) {
            case "PremisesCode" -> INVALID_PREM_CODE_LENGTH;
            case "CustomerCode" -> INVALID_CUSTOMER_CODE_LENGTH;
            default -> throw new IllegalStateException("Invalid param: " + param);
        };
    }

    public CreateAccountNoteRequest getApiPayload(CreateAccountNoteLabels apiName, CreateAccountNoteRequest request) {
        logInfo("Get Api Payload");
        return switch (apiName) {
            case HAPPY_FLOW -> buildHappyFlowPayload(request);
            case MISSING_REQUEST_ID -> buildMissingRequestIdPayload(request);
            case NULL_CUSTOMER_CODE -> buildNullCustomerCodePayload(request);
            case NULL_NOTE_TYPE_CODE -> buildNullNoteTypeCodePayload(request);
            case NULL_NOTE_TEXT -> buildNullNoteTextPayload(request);
            case NULL_ORIGIN -> buildNullOriginPayload(request);
            case INVALID_CUSTOMER_CODE_LENGTH -> buildInvalidCustomerCodeLengthPayload(request);
            case INVALID_PREM_CODE_LENGTH -> buildInvalidPremCodeLengthPayload(request);
            case INVALID_EXPIRATION_DATE -> buildInvalidExpirationDatePayload(request);
            case NONEXISTENT_SERVICE_NO_PREM_CODE -> buildNonExistentServiceNumberPayload(request);
            case INVALID_SERVICE_NO_FORMAT -> buildInvalidServiceNumberFormatPayload(request);
            case NONEXISTENT_NOTE_TYPE -> buildNonExistentNoteTypePayload(request);
            case NONEXISTENT_CUST_PREM_CODE -> buildNonExistentCustomerPremCodePayload(request);
        };
    }

    private void setTestContextData(Map<String, String> data) {
        testContext.setServiceNumber(data.get("serviceNumber"));
        testContext.setNoteTypeCode(data.get("noteTypeCode"));
        testContext.setNoteText(data.get("noteText"));
        testContext.setOrigin(data.get("origin"));
    }

    private CreateAccountNoteRequest buildHappyFlowPayload(CreateAccountNoteRequest request) {
        request.setRequestID(UUID.randomUUID().toString());
        request.setCustomerCode(testContext.getCustomerCode());
        request.setPremisesCode(testContext.getPremisesCode());
        request.setServiceNumber(testContext.getServiceNumber());
        request.setNoteTypeCode(testContext.getNoteTypeCode());
        request.setNoteText(testContext.getNoteText());
        request.setOrigin(testContext.getOrigin());
        return request;
    }

    private CreateAccountNoteRequest buildMissingRequestIdPayload(CreateAccountNoteRequest request) {
        buildHappyFlowPayload(request);
        CommonUtil.nullifyFields(request, "requestID");
        return request;
    }

    private CreateAccountNoteRequest buildNullCustomerCodePayload(CreateAccountNoteRequest request) {
        buildHappyFlowPayload(request);
        request.setCustomerCode(null);
        return request;
    }

    private CreateAccountNoteRequest buildNullNoteTypeCodePayload(CreateAccountNoteRequest request) {
        buildHappyFlowPayload(request);
        request.setNoteTypeCode(null);
        return request;
    }

    private CreateAccountNoteRequest buildNullNoteTextPayload(CreateAccountNoteRequest request) {
        buildHappyFlowPayload(request);
        request.setNoteText(null);
        return request;
    }

    private CreateAccountNoteRequest buildNullOriginPayload(CreateAccountNoteRequest request) {
        buildHappyFlowPayload(request);
        request.setOrigin(null);
        return request;
    }

    private CreateAccountNoteRequest buildInvalidCustomerCodeLengthPayload(CreateAccountNoteRequest request) {
        buildHappyFlowPayload(request);
        request.setCustomerCode(CommonUtil.getRandomNumericString(10));
        return request;
    }

    private CreateAccountNoteRequest buildInvalidPremCodeLengthPayload(CreateAccountNoteRequest request) {
        buildHappyFlowPayload(request);
        request.setPremisesCode(CommonUtil.getRandomNumericString(8));
        return request;
    }

    private CreateAccountNoteRequest buildInvalidExpirationDatePayload(CreateAccountNoteRequest request) {
        buildHappyFlowPayload(request);
        request.setExpirationDate(testContext.getExpirationDate());
        return request;
    }

    private CreateAccountNoteRequest buildNonExistentServiceNumberPayload(CreateAccountNoteRequest request) {
        buildHappyFlowPayload(request);
        request.setServiceNumber(CommonUtil.getRandomNumericString(4));
        return request;
    }

    private CreateAccountNoteRequest buildInvalidServiceNumberFormatPayload(CreateAccountNoteRequest request) {
        buildHappyFlowPayload(request);
        request.setServiceNumber(CommonUtil.getRandomNumericString(5));
        return request;
    }

    private CreateAccountNoteRequest buildNonExistentNoteTypePayload(CreateAccountNoteRequest request) {
        buildHappyFlowPayload(request);
        request.setNoteTypeCode(CommonUtil.getRandomString(3));
        return request;
    }

    private CreateAccountNoteRequest buildNonExistentCustomerPremCodePayload(CreateAccountNoteRequest request) {
        buildHappyFlowPayload(request);
        request.setCustomerCode(CommonUtil.getRandomNumericString(8));
        return request;
    }
}
