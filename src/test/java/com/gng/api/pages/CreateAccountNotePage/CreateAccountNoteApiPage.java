package com.gng.api.pages.CreateAccountNotePage;

import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.base.BasePage;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.pojo.createaccountnote.CreateAccountNoteRequest;
import io.cucumber.datatable.DataTable;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import java.util.List;
import java.util.Map;

import static com.gng.api.constants.ApiEndPoint.ACCOUNT_NOTE;
import static com.gng.api.constants.ApiLabel.*;
import static com.gng.api.constants.DBConstant.UCBACCT_CUST_CODE;
import static com.gng.api.constants.DBConstant.UCBACCT_PREM_CODE;
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
        setupAndExecuteRequest(dataTable, CREATEACCOUNTNOTE_HAPPYFLOW_API);
    }

    public void sendCreateAccountNoteRequestWithNullNoteTypeCode(DataTable dataTable) {
        setupAndExecuteRequest(dataTable, CREATEACCOUNTNOTE_NULLNOTETYPECODE_API);
    }

    public void sendCreateAccountNoteRequestWithNullCustomerCode(DataTable dataTable) {
        setupAndExecuteRequest(dataTable, CREATEACCOUNTNOTE_NULLCUSTOMERCODE_API);
    }

    public void sendCreateAccountNoteRequestWithNonExistentCombination(DataTable dataTable) {
        setupAndExecuteRequest(dataTable, GETACCOUNTINFO_NONEXISTENT_CUSTPREMCODE_API);
    }

    public void sendCreateAccountNoteRequestWithMissingRequestId(DataTable dataTable) {
        setupAndExecuteRequest(dataTable, CREATEACCOUNTNOTE_MISSINGREQUESTID_API);
    }

    public void sendCreateAccountNoteRequestWithParamValue(String param, String paramValue, DataTable dataTable) {
        String apiLabel = helper.getApiLabelForParam(param);
        setupAndExecuteRequest(dataTable, apiLabel);
    }

    public void sendCreateAccountNoteRequestWithNullNoteText(DataTable dataTable) {
        setupAndExecuteRequest(dataTable, CREATEACCOUNTNOTE_NULLNOTETEXT_API);
    }

    public void sendCreateAccountNoteRequestWithNullOrigin(DataTable dataTable) {
        setupAndExecuteRequest(dataTable, CREATEACCOUNTNOTE_NULLORIGIN_API);
    }

    public void sendCreateAccountNoteRequestWithDuplicateRequestId(DataTable dataTable) {
        List<Map<String, Object>> activeCustomerData = ApplicationContext.get().getDbAction().getActiveCustomerDetails();
        setCustomerAndPremisesCodes(activeCustomerData);
        helper.setupRequestData(dataTable);

        CreateAccountNoteRequest request = new CreateAccountNoteRequest();
        request = helper.getApiPayload(CREATEACCOUNTNOTE_HAPPYFLOW_API, request);

        setRequestSpecification(CREATEACCOUNTNOTE_HAPPYFLOW_API, request);
        executeRequest(HttpPost.METHOD_NAME, ACCOUNT_NOTE, 200);
        Response response = executeRequest(HttpPost.METHOD_NAME, ACCOUNT_NOTE, 200);
        testContext.setResponse(response);
    }

    public void sendCreateAccountNoteRequestWithInvalidExpirationDate(DataTable dataTable) {
        setupAndExecuteRequest(dataTable, CREATEACCOUNTNOTE_INVALIDEXPIRATIONDATE_API);
    }

    public void sendCreateAccountNoteRequestWithNonExistentServiceNumber(DataTable dataTable) {
        setupAndExecuteRequest(dataTable, CREATEACCOUNTNOTE_NONEXISTENT_SERVICENOPREMCODE_API);
    }

    public void sendCreateAccountNoteRequestWithInvalidServiceNumberFormat(DataTable dataTable) {
        setupAndExecuteRequest(dataTable, CREATEACCOUNTNOTE_INVALID_SERVICENOFORMAT_API);
    }

    public void sendCreateAccountNoteRequestWithNonExistentNoteType(DataTable dataTable) {
        setupAndExecuteRequest(dataTable, CREATEACCOUNTNOTE_NONEXISTENT_NOTETYPE_API);
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

    private void setupAndExecuteRequest(DataTable dataTable, String apiLabel) {
        List<Map<String, Object>> activeCustomerData = ApplicationContext.get().getDbAction().getActiveCustomerDetails();
        setCustomerAndPremisesCodes(activeCustomerData);
        helper.setupRequestData(dataTable);

        CreateAccountNoteRequest request = new CreateAccountNoteRequest();
        request = helper.getApiPayload(apiLabel, request);
        setRequestSpecification(apiLabel, request);
        Response response = executeRequest(HttpPost.METHOD_NAME, ACCOUNT_NOTE, 200);
        testContext.setResponse(response);
    }
}
