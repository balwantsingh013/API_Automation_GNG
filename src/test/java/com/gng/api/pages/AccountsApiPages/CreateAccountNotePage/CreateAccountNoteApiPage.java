package com.gng.api.pages.AccountsApiPages.CreateAccountNotePage;

import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.pojo.AccountsPojo.createAccountNote.CreateAccountNoteRequest;
import io.cucumber.datatable.DataTable;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import java.util.List;
import java.util.Map;

import static com.gng.api.constants.ApiEndPoint.CREATE_ACCOUNT_NOTE;
import static com.gng.api.constants.DBConstant.UCBACCT_CUST_CODE;
import static com.gng.api.constants.DBConstant.UCBACCT_PREM_CODE;
import static com.gng.api.pages.AccountsApiPages.CreateAccountNotePage.CreateAccountNoteLabels.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CreateAccountNoteApiPage extends BasePage {
    private final CreateAccountNoteHelper helper;

    public CreateAccountNoteApiPage(TestContext testContext) {
        super(testContext);
        this.helper = new CreateAccountNoteHelper(testContext);
    }

    public void sendCreateAccountNoteRequestWithValidData(DataTable dataTable) {
        setupAndExecuteRequest(dataTable, HAPPY_FLOW);
    }

    public void sendCreateAccountNoteRequestWithNullNoteTypeCode(DataTable dataTable) {
        setupAndExecuteRequest(dataTable, NULL_NOTE_TYPE_CODE);
    }

    public void sendCreateAccountNoteRequestWithNullCustomerCode(DataTable dataTable) {
        setupAndExecuteRequest(dataTable, NULL_CUSTOMER_CODE);
    }

    public void sendCreateAccountNoteRequestWithNonExistentCombination(DataTable dataTable) {
        setupAndExecuteRequest(dataTable, NONEXISTENT_CUST_PREM_CODE);
    }

    public void sendCreateAccountNoteRequestWithMissingRequestId(DataTable dataTable) {
        setupAndExecuteRequest(dataTable, MISSING_REQUEST_ID);
    }

    public void sendCreateAccountNoteRequestWithParamValue(String param, String paramValue, DataTable dataTable) {
        CreateAccountNoteLabels apiLabel = helper.getApiLabelForParam(param);
        setupAndExecuteRequest(dataTable, apiLabel);
    }

    public void sendCreateAccountNoteRequestWithNullNoteText(DataTable dataTable) {
        setupAndExecuteRequest(dataTable, NULL_NOTE_TEXT);
    }

    public void sendCreateAccountNoteRequestWithNullOrigin(DataTable dataTable) {
        setupAndExecuteRequest(dataTable, NULL_ORIGIN);
    }

    public void sendCreateAccountNoteRequestWithDuplicateRequestId(DataTable dataTable) {
        List<Map<String, Object>> activeCustomerData = ApplicationContext.get().getDbAction().getActiveCustomerDetails();
        setCustomerAndPremisesCodes(activeCustomerData);
        helper.setupRequestData(dataTable);

        CreateAccountNoteRequest request = new CreateAccountNoteRequest();
        request = helper.getApiPayload(HAPPY_FLOW, request);

        setRequestSpecification(HAPPY_FLOW.getLabel(), request);
        sendRequest(HttpPost.METHOD_NAME, CREATE_ACCOUNT_NOTE, 200);
        Response response = sendRequest(HttpPost.METHOD_NAME, CREATE_ACCOUNT_NOTE, 200);
        testContext.setResponse(response);
    }

    public void sendCreateAccountNoteRequestWithInvalidExpirationDate(DataTable dataTable) {
        setupAndExecuteRequest(dataTable, INVALID_EXPIRATION_DATE);
    }

    public void sendCreateAccountNoteRequestWithNonExistentServiceNumber(DataTable dataTable) {
        setupAndExecuteRequest(dataTable, NONEXISTENT_SERVICE_NO_PREM_CODE);
    }

    public void sendCreateAccountNoteRequestWithInvalidServiceNumberFormat(DataTable dataTable) {
        setupAndExecuteRequest(dataTable, INVALID_SERVICE_NO_FORMAT);
    }

    public void sendCreateAccountNoteRequestWithNonExistentNoteType(DataTable dataTable) {
        setupAndExecuteRequest(dataTable, NONEXISTENT_NOTE_TYPE);
    }

    public void verifyValidNoteSequenceNumber() {
        testContext.setNoteSequenceNumber(testContext.getResponse().jsonPath().getString("data.noteSequenceNumber"));
        assertThat("noteSequenceNumber is Null", testContext.getNoteSequenceNumber(), notNullValue());
    }

    public void verifyNoteSequenceNumberInDatabase() {
        List<Map<String, Object>> noteSequenceNumberData = ApplicationContext.get().getDbAction()
                .getNoteSequenceNumber(testContext.getNoteSequenceNumber());

        String noteSequenceNumberDB = testContext.getResponse().jsonPath().getString("data.noteSequenceNumber");
        String custCodeDB = noteSequenceNumberData.getFirst().get(UCBACCT_CUST_CODE).toString();
        String premCodeDB = noteSequenceNumberData.getFirst().get(UCBACCT_PREM_CODE).toString();

        assertThat("noteSequenceNumber in Response and DB doesn't match",
                noteSequenceNumberDB, equalTo(testContext.getNoteSequenceNumber()));
        assertThat("CustCode in Response and DB doesn't match",
                custCodeDB, equalTo(testContext.getCustomerCode()));
        assertThat("PremCode in Response and DB doesn't match",
                premCodeDB, equalTo(testContext.getPremisesCode()));
    }

    private void setupAndExecuteRequest(DataTable dataTable, CreateAccountNoteLabels apiLabel) {
        List<Map<String, Object>> activeCustomerData = ApplicationContext.get().getDbAction().getActiveCustomerDetails();
        setCustomerAndPremisesCodes(activeCustomerData);
        helper.setupRequestData(dataTable);

        CreateAccountNoteRequest request = new CreateAccountNoteRequest();
        request = helper.getApiPayload(apiLabel, request);
        setRequestSpecification(apiLabel.getLabel(), request);
        Response response = sendRequest(HttpPost.METHOD_NAME, CREATE_ACCOUNT_NOTE, 200);
        testContext.setResponse(response);
    }
}
