package com.gng.api.pages.CreateAccountNotePage;

import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.pojo.createaccountnote.CreateAccountNoteRequest;
import com.gng.api.util.CommonUtil;
import io.cucumber.datatable.DataTable;
import static com.gng.api.util.LogUtil.logInfo;
import static com.gng.api.constants.ApiLabel.*;
import static com.gng.api.constants.TestConstant.UNEXPECTED_VALUE;

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

    public String getApiLabelForParam(String param) {
        return switch (param) {
            case "PremisesCode" -> CREATEACCOUNTNOTE_INVALIDPREMCODELENGTH_API;
            case "CustomerCode" -> CREATEACCOUNTNOTE_INVALIDCUSTOMERCODELENGTH_API;
            default -> throw new IllegalStateException("Invalid param: " + param);
        };
    }

    public CreateAccountNoteRequest getApiPayload(String apiName, CreateAccountNoteRequest request) {
        logInfo("Get Api Payload");
        return switch (apiName) {
            case CREATEACCOUNTNOTE_HAPPYFLOW_API -> buildHappyFlowPayload(request);
            case CREATEACCOUNTNOTE_MISSINGREQUESTID_API -> buildMissingRequestIdPayload(request);
            case CREATEACCOUNTNOTE_NULLCUSTOMERCODE_API -> buildNullCustomerCodePayload(request);
            case CREATEACCOUNTNOTE_NULLNOTETYPECODE_API -> buildNullNoteTypeCodePayload(request);
            case CREATEACCOUNTNOTE_NULLNOTETEXT_API -> buildNullNoteTextPayload(request);
            case CREATEACCOUNTNOTE_NULLORIGIN_API -> buildNullOriginPayload(request);
            case CREATEACCOUNTNOTE_INVALIDCUSTOMERCODELENGTH_API -> buildInvalidCustomerCodeLengthPayload(request);
            case CREATEACCOUNTNOTE_INVALIDPREMCODELENGTH_API -> buildInvalidPremCodeLengthPayload(request);
            case CREATEACCOUNTNOTE_INVALIDEXPIRATIONDATE_API -> buildInvalidExpirationDatePayload(request);
            case CREATEACCOUNTNOTE_NONEXISTENT_SERVICENOPREMCODE_API -> buildNonExistentServiceNumberPayload(request);
            case CREATEACCOUNTNOTE_INVALID_SERVICENOFORMAT_API -> buildInvalidServiceNumberFormatPayload(request);
            case CREATEACCOUNTNOTE_NONEXISTENT_NOTETYPE_API -> buildNonExistentNoteTypePayload(request);
            case GETACCOUNTINFO_NONEXISTENT_CUSTPREMCODE_API -> buildNonExistentCustomerPremCodePayload(request);
            default -> throw new IllegalStateException(UNEXPECTED_VALUE + apiName);
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
        request = buildHappyFlowPayload(request);
        CommonUtil.nullifyFields(request, "requestID");
        return request;
    }

    private CreateAccountNoteRequest buildNullCustomerCodePayload(CreateAccountNoteRequest request) {
        request = buildHappyFlowPayload(request);
        request.setCustomerCode(null);
        return request;
    }

    private CreateAccountNoteRequest buildNullNoteTypeCodePayload(CreateAccountNoteRequest request) {
        request = buildHappyFlowPayload(request);
        request.setNoteTypeCode(null);
        return request;
    }

    private CreateAccountNoteRequest buildNullNoteTextPayload(CreateAccountNoteRequest request) {
        request = buildHappyFlowPayload(request);
        request.setNoteText(null);
        return request;
    }

    private CreateAccountNoteRequest buildNullOriginPayload(CreateAccountNoteRequest request) {
        request = buildHappyFlowPayload(request);
        request.setOrigin(null);
        return request;
    }

    private CreateAccountNoteRequest buildInvalidCustomerCodeLengthPayload(CreateAccountNoteRequest request) {
        request = buildHappyFlowPayload(request);
        request.setCustomerCode(CommonUtil.getRandomNumericString(10));
        return request;
    }

    private CreateAccountNoteRequest buildInvalidPremCodeLengthPayload(CreateAccountNoteRequest request) {
        request = buildHappyFlowPayload(request);
        request.setPremisesCode(CommonUtil.getRandomNumericString(8));
        return request;
    }

    private CreateAccountNoteRequest buildInvalidExpirationDatePayload(CreateAccountNoteRequest request) {
        request = buildHappyFlowPayload(request);
        request.setExpirationDate(testContext.getExpirationDate());
        return request;
    }

    private CreateAccountNoteRequest buildNonExistentServiceNumberPayload(CreateAccountNoteRequest request) {
        request = buildHappyFlowPayload(request);
        request.setServiceNumber(CommonUtil.getRandomNumericString(4));
        return request;
    }

    private CreateAccountNoteRequest buildInvalidServiceNumberFormatPayload(CreateAccountNoteRequest request) {
        request = buildHappyFlowPayload(request);
        request.setServiceNumber(CommonUtil.getRandomNumericString(5));
        return request;
    }

    private CreateAccountNoteRequest buildNonExistentNoteTypePayload(CreateAccountNoteRequest request) {
        request = buildHappyFlowPayload(request);
        request.setNoteTypeCode(CommonUtil.getRandomString(3));
        return request;
    }

    private CreateAccountNoteRequest buildNonExistentCustomerPremCodePayload(CreateAccountNoteRequest request) {
        request = buildHappyFlowPayload(request);
        request.setCustomerCode(CommonUtil.getRandomNumericString(8));
        return request;
    }
}
