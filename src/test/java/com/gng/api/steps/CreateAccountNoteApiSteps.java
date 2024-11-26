package com.gng.api.steps;

import com.gng.api.context.RunContext;
import com.gng.api.context.TestContext;
import com.gng.api.pages.CreateAccountNoteApiPage;
import com.gng.api.pojo.createaccountnote.CreateAccountNoteRequest;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpPost;

import java.util.List;
import java.util.Map;

import static com.gng.api.config.LogConfig.logError;
import static com.gng.api.constants.ApiEndPoint.ACCOUNT_NOTE;
import static com.gng.api.constants.ApiLabel.*;
import static com.gng.api.constants.DBConstant.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Slf4j
public class CreateAccountNoteApiSteps  {

    private final TestContext testContext;

    public CreateAccountNoteApiSteps(TestContext testContext, CreateAccountNoteApiPage createAccountNoteApiPage) {
        super();
        this.testContext = testContext;
        testContext.setCreateAccountNoteApiPage(createAccountNoteApiPage);
    }

    @When("a request is made to the CreateAccountNote Api with Null NoteTypeCode")
    public void a_request_is_made_to_the_create_account_note_api_with_null_note_type_code(DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        List<Map<String, Object>> activeCustomerData = RunContext.get().getDbAction().getActiveCustomerDetails();
        testContext.setCustomerCode(activeCustomerData.getFirst().get(UCRACCT_CUST_CODE).toString());
        testContext.setPremisesCode(activeCustomerData.getFirst().get(UCRACCT_PREM_CODE).toString());
        testContext.setServiceNumber(data.get("serviceNumber"));
        testContext.setNoteText(data.get("noteText"));
        testContext.setOrigin(data.get("origin"));
        CreateAccountNoteRequest createAccountNoteRequest = new CreateAccountNoteRequest();
        createAccountNoteRequest = testContext.getCreateAccountNoteApiPage().getApiPayload(CREATEACCOUNTNOTE_NULLNOTETYPECODE_API, createAccountNoteRequest);
        testContext.getCreateAccountNoteApiPage().setRequestSpecification(CREATEACCOUNTNOTE_NULLNOTETYPECODE_API, createAccountNoteRequest);
        Response response = testContext.getCreateAccountNoteApiPage().sendRequest(HttpPost.METHOD_NAME, ACCOUNT_NOTE, 200);
        testContext.setResponse(response);
    }

    @When("a request is made to the CreateAccountNote Api with Null CustomerCode")
    public void a_request_is_made_to_the_create_account_note_api_with_null_customer_code(DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        List<Map<String, Object>> activeCustomerData = RunContext.get().getDbAction().getActiveCustomerDetails();
        testContext.setPremisesCode(activeCustomerData.getFirst().get(UCRACCT_PREM_CODE).toString());
        testContext.setServiceNumber(data.get("serviceNumber"));
        testContext.setNoteTypeCode(data.get("noteTypeCode"));
        testContext.setNoteText(data.get("noteText"));
        testContext.setOrigin(data.get("origin"));
        CreateAccountNoteRequest createAccountNoteRequest = new CreateAccountNoteRequest();
        createAccountNoteRequest = testContext.getCreateAccountNoteApiPage().getApiPayload(CREATEACCOUNTNOTE_NULLCUSTOMERCODE_API, createAccountNoteRequest);
        testContext.getCreateAccountNoteApiPage().setRequestSpecification(CREATEACCOUNTNOTE_NULLCUSTOMERCODE_API, createAccountNoteRequest);
        Response response = testContext.getCreateAccountNoteApiPage().sendRequest(HttpPost.METHOD_NAME, ACCOUNT_NOTE, 200);
        testContext.setResponse(response);
    }

    @When("a request is made to the CreateAccountNote Api with non-existent combination of CustomerCode and PremisesCode")
    public void a_request_is_made_to_the_create_account_note_api_with_non_existent_combination_of_customer_code_and_premises_code(DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        List<Map<String, Object>> activeCustomerData = RunContext.get().getDbAction().getActiveCustomerDetails();
        testContext.setCustomerCode(activeCustomerData.getFirst().get(UCRACCT_CUST_CODE).toString());
        testContext.setPremisesCode(activeCustomerData.getFirst().get(UCRACCT_PREM_CODE).toString());
        testContext.setServiceNumber(data.get("serviceNumber"));
        testContext.setNoteTypeCode(data.get("noteTypeCode"));
        testContext.setNoteText(data.get("noteText"));
        testContext.setOrigin(data.get("origin"));
        CreateAccountNoteRequest createAccountNoteRequest = new CreateAccountNoteRequest();
        createAccountNoteRequest = testContext.getCreateAccountNoteApiPage().getApiPayload(GETACCOUNTINFO_NONEXISTENT_CUSTPREMCODE_API, createAccountNoteRequest);
        testContext.getCreateAccountNoteApiPage().setRequestSpecification(GETACCOUNTINFO_NONEXISTENT_CUSTPREMCODE_API, createAccountNoteRequest);
        Response response = testContext.getCreateAccountNoteApiPage().sendRequest(HttpPost.METHOD_NAME, ACCOUNT_NOTE, 200);
        testContext.setResponse(response);
    }

    @When("a request is made to the CreateAccountNote Api with missing RequestID")
    public void a_request_is_made_to_the_create_account_note_api_with_missing_request_id(DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        List<Map<String, Object>> activeCustomerData = RunContext.get().getDbAction().getActiveCustomerDetails();
        testContext.setCustomerCode(activeCustomerData.getFirst().get(UCRACCT_CUST_CODE).toString());
        testContext.setPremisesCode(activeCustomerData.getFirst().get(UCRACCT_PREM_CODE).toString());
        testContext.setServiceNumber(data.get("serviceNumber"));
        testContext.setNoteTypeCode(data.get("noteTypeCode"));
        testContext.setNoteText(data.get("noteText"));
        testContext.setOrigin(data.get("origin"));
        CreateAccountNoteRequest createAccountNoteRequest = new CreateAccountNoteRequest();
        createAccountNoteRequest = testContext.getCreateAccountNoteApiPage().getApiPayload(CREATEACCOUNTNOTE_MISSINGREQUESTID_API, createAccountNoteRequest);
        testContext.getCreateAccountNoteApiPage().setRequestSpecification(CREATEACCOUNTNOTE_MISSINGREQUESTID_API, createAccountNoteRequest);
        Response response = testContext.getCreateAccountNoteApiPage().sendRequest(HttpPost.METHOD_NAME, ACCOUNT_NOTE, 200);
        testContext.setResponse(response);
    }

    @When("a request is made to the CreateAccountNote Api with valid data")
    public void a_request_is_made_to_the_create_account_note_api_with_valid_data(DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        List<Map<String, Object>> activeCustomerData = RunContext.get().getDbAction().getActiveCustomerDetails();
        testContext.setCustomerCode(activeCustomerData.getFirst().get(UCRACCT_CUST_CODE).toString());
        testContext.setPremisesCode(activeCustomerData.getFirst().get(UCRACCT_PREM_CODE).toString());
        testContext.setServiceNumber(data.get("serviceNumber"));
        testContext.setNoteTypeCode(data.get("noteTypeCode"));
        testContext.setNoteText(data.get("noteText"));
        testContext.setOrigin(data.get("origin"));
        CreateAccountNoteRequest createAccountNoteRequest = new CreateAccountNoteRequest();
        createAccountNoteRequest = testContext.getCreateAccountNoteApiPage().getApiPayload(CREATEACCOUNTNOTE_HAPPYFLOW_API, createAccountNoteRequest);
        testContext.getCreateAccountNoteApiPage().setRequestSpecification(CREATEACCOUNTNOTE_HAPPYFLOW_API, createAccountNoteRequest);
        Response response = testContext.getCreateAccountNoteApiPage().sendRequest(HttpPost.METHOD_NAME, ACCOUNT_NOTE, 200);
        testContext.setResponse(response);
    }

    @Then("verify response contains a valid NoteSequenceNumber")
    public void verify_response_contains_a_valid_note_sequence_number() {
        testContext.setNoteSequenceNumber(testContext.getResponse().jsonPath().getString("data.noteSequenceNumber"));
        assertThat("noteSequenceNumber is Null", testContext.getNoteSequenceNumber(), notNullValue());
    }

    @Then("verify NoteSequenceNumber match the value in the database")
    public void verify_note_sequence_number_match_the_value_in_the_database() {
        List<Map<String, Object>> noteSequenceNumberData = RunContext.get().getDbAction().getNoteSequenceNumber(testContext.getNoteSequenceNumber());
        testContext.setNoteSequenceNumber(noteSequenceNumberData.getFirst().get(UCBACCT_SEQ_NUMBER).toString());
        String noteSequenceNumberDB = testContext.getResponse().jsonPath().getString("data.noteSequenceNumber");
        String custCodeDB = noteSequenceNumberData.getFirst().get(UCBACCT_CUST_CODE).toString();
        String premCodeDB = noteSequenceNumberData.getFirst().get(UCBACCT_PREM_CODE).toString();
        assertThat("noteSequenceNumber in Response and DB doesn't match", noteSequenceNumberDB, equalTo(testContext.getNoteSequenceNumber()));
        assertThat("CustCode in Response and DB doesn't match", custCodeDB, equalTo(testContext.getCustomerCode()));
        assertThat("PremCode in Response and DB doesn't match", premCodeDB, equalTo(testContext.getPremisesCode()));
    }

    @When("a request is made to the CreateAccountNote Api with {string} value is {string}")
    public void a_request_is_made_to_the_create_account_note_api_with_value_is(String param, String paramValue, DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        List<Map<String, Object>> activeCustomerData = RunContext.get().getDbAction().getActiveCustomerDetails();
        testContext.setServiceNumber(data.get("serviceNumber"));
        testContext.setNoteTypeCode(data.get("noteTypeCode"));
        testContext.setNoteText(data.get("noteText"));
        testContext.setOrigin(data.get("origin"));
        CreateAccountNoteRequest createAccountNoteRequest = new CreateAccountNoteRequest();
        switch (param) {
            case "PremisesCode":
                testContext.setCustomerCode(activeCustomerData.getFirst().get(UCRACCT_CUST_CODE).toString());
                createAccountNoteRequest = testContext.getCreateAccountNoteApiPage().getApiPayload(CREATEACCOUNTNOTE_INVALIDPREMCODELENGTH_API, createAccountNoteRequest);
                testContext.getCreateAccountNoteApiPage().setRequestSpecification(CREATEACCOUNTNOTE_INVALIDPREMCODELENGTH_API, createAccountNoteRequest);
                break;
            case "CustomerCode":
                testContext.setPremisesCode(activeCustomerData.getFirst().get(UCRACCT_PREM_CODE).toString());
                createAccountNoteRequest = testContext.getCreateAccountNoteApiPage().getApiPayload(CREATEACCOUNTNOTE_INVALIDCUSTOMERCODELENGTH_API, createAccountNoteRequest);
                testContext.getCreateAccountNoteApiPage().setRequestSpecification(CREATEACCOUNTNOTE_INVALIDCUSTOMERCODELENGTH_API, createAccountNoteRequest);
                break;
            default:
                logError("Invalid Param Passed to Switch Case: " + param);
        }
        Response response = testContext.getCreateAccountNoteApiPage().sendRequest(HttpPost.METHOD_NAME, ACCOUNT_NOTE, 200);
        testContext.setResponse(response);
    }

    @When("a request is made to the CreateAccountNote Api with Null NoteText")
    public void a_request_is_made_to_the_create_account_note_api_with_null_note_text(DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        List<Map<String, Object>> activeCustomerData = RunContext.get().getDbAction().getActiveCustomerDetails();
        testContext.setCustomerCode(activeCustomerData.getFirst().get(UCRACCT_CUST_CODE).toString());
        testContext.setPremisesCode(activeCustomerData.getFirst().get(UCRACCT_PREM_CODE).toString());
        testContext.setServiceNumber(data.get("serviceNumber"));
        testContext.setNoteTypeCode(data.get("noteTypeCode"));
        testContext.setOrigin(data.get("origin"));
        CreateAccountNoteRequest createAccountNoteRequest = new CreateAccountNoteRequest();
        createAccountNoteRequest = testContext.getCreateAccountNoteApiPage().getApiPayload(CREATEACCOUNTNOTE_NULLNOTETEXT_API, createAccountNoteRequest);
        testContext.getCreateAccountNoteApiPage().setRequestSpecification(CREATEACCOUNTNOTE_NULLNOTETEXT_API, createAccountNoteRequest);
        Response response = testContext.getCreateAccountNoteApiPage().sendRequest(HttpPost.METHOD_NAME, ACCOUNT_NOTE, 200);
        testContext.setResponse(response);
    }

    @When("a request is made to the CreateAccountNote Api with Null Origin")
    public void a_request_is_made_to_the_create_account_note_api_with_null_origin(DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        List<Map<String, Object>> activeCustomerData = RunContext.get().getDbAction().getActiveCustomerDetails();
        testContext.setCustomerCode(activeCustomerData.getFirst().get(UCRACCT_CUST_CODE).toString());
        testContext.setPremisesCode(activeCustomerData.getFirst().get(UCRACCT_PREM_CODE).toString());
        testContext.setServiceNumber(data.get("serviceNumber"));
        testContext.setNoteTypeCode(data.get("noteTypeCode"));
        testContext.setNoteText(data.get("noteText"));
        CreateAccountNoteRequest createAccountNoteRequest = new CreateAccountNoteRequest();
        createAccountNoteRequest = testContext.getCreateAccountNoteApiPage().getApiPayload(CREATEACCOUNTNOTE_NULLORIGIN_API, createAccountNoteRequest);
        testContext.getCreateAccountNoteApiPage().setRequestSpecification(CREATEACCOUNTNOTE_NULLORIGIN_API, createAccountNoteRequest);
        Response response = testContext.getCreateAccountNoteApiPage().sendRequest(HttpPost.METHOD_NAME, ACCOUNT_NOTE, 200);
        testContext.setResponse(response);
    }

    @When("a request is made to the CreateAccountNote Api with Duplicate requestID")
    public void a_request_is_made_to_the_create_account_note_api_with_duplicate_request_id(DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        List<Map<String, Object>> activeCustomerData = RunContext.get().getDbAction().getActiveCustomerDetails();
        testContext.setCustomerCode(activeCustomerData.getFirst().get(UCRACCT_CUST_CODE).toString());
        testContext.setPremisesCode(activeCustomerData.getFirst().get(UCRACCT_PREM_CODE).toString());
        testContext.setServiceNumber(data.get("serviceNumber"));
        testContext.setNoteTypeCode(data.get("noteTypeCode"));
        testContext.setNoteText(data.get("noteText"));
        testContext.setOrigin(data.get("origin"));
        CreateAccountNoteRequest createAccountNoteRequest = new CreateAccountNoteRequest();
        createAccountNoteRequest = testContext.getCreateAccountNoteApiPage().getApiPayload(CREATEACCOUNTNOTE_HAPPYFLOW_API, createAccountNoteRequest);
        testContext.getCreateAccountNoteApiPage().setRequestSpecification(CREATEACCOUNTNOTE_HAPPYFLOW_API, createAccountNoteRequest);
        testContext.getCreateAccountNoteApiPage().sendRequest(HttpPost.METHOD_NAME, ACCOUNT_NOTE, 200);
        Response response = testContext.getCreateAccountNoteApiPage().sendRequest(HttpPost.METHOD_NAME, ACCOUNT_NOTE, 200);
        testContext.setResponse(response);
    }

    @When("a request is made to the CreateAccountNote Api with Invalid Expiration Date Format")
    public void a_request_is_made_to_the_create_account_note_api_with_invalid_expiration_date_format(DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        List<Map<String, Object>> activeCustomerData = RunContext.get().getDbAction().getActiveCustomerDetails();
        testContext.setCustomerCode(activeCustomerData.getFirst().get(UCRACCT_CUST_CODE).toString());
        testContext.setPremisesCode(activeCustomerData.getFirst().get(UCRACCT_PREM_CODE).toString());
        testContext.setServiceNumber(data.get("serviceNumber"));
        testContext.setNoteTypeCode(data.get("noteTypeCode"));
        testContext.setNoteText(data.get("noteText"));
        testContext.setOrigin(data.get("origin"));
        testContext.setExpirationDate(data.get("expirationDate"));
        CreateAccountNoteRequest createAccountNoteRequest = new CreateAccountNoteRequest();
        createAccountNoteRequest = testContext.getCreateAccountNoteApiPage().getApiPayload(CREATEACCOUNTNOTE_INVALIDEXPIRATIONDATE_API, createAccountNoteRequest);
        testContext.getCreateAccountNoteApiPage().setRequestSpecification(CREATEACCOUNTNOTE_INVALIDEXPIRATIONDATE_API, createAccountNoteRequest);
        Response response = testContext.getCreateAccountNoteApiPage().sendRequest(HttpPost.METHOD_NAME, ACCOUNT_NOTE, 200);
        testContext.setResponse(response);
    }

    @When("a request is made to the CreateAccountNote Api with non-existent combination of ServiceNumber and PremisesCode")
    public void a_request_is_made_to_the_create_account_note_api_with_non_existent_combination_of_service_number_and_premises_code(DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        List<Map<String, Object>> activeCustomerData = RunContext.get().getDbAction().getActiveCustomerDetails();
        testContext.setCustomerCode(activeCustomerData.getFirst().get(UCRACCT_CUST_CODE).toString());
        testContext.setPremisesCode(activeCustomerData.getFirst().get(UCRACCT_PREM_CODE).toString());
        testContext.setNoteTypeCode(data.get("noteTypeCode"));
        testContext.setNoteText(data.get("noteText"));
        testContext.setOrigin(data.get("origin"));
        CreateAccountNoteRequest createAccountNoteRequest = new CreateAccountNoteRequest();
        createAccountNoteRequest = testContext.getCreateAccountNoteApiPage().getApiPayload(CREATEACCOUNTNOTE_NONEXISTENT_SERVICENOPREMCODE_API, createAccountNoteRequest);
        testContext.getCreateAccountNoteApiPage().setRequestSpecification(CREATEACCOUNTNOTE_NONEXISTENT_SERVICENOPREMCODE_API, createAccountNoteRequest);
        Response response = testContext.getCreateAccountNoteApiPage().sendRequest(HttpPost.METHOD_NAME, ACCOUNT_NOTE, 200);
        testContext.setResponse(response);
    }

    @When("a request is made to the CreateAccountNote Api with Invalid ServiceNumber Format")
    public void a_request_is_made_to_the_create_account_note_api_with_invalid_service_number_format(DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        List<Map<String, Object>> activeCustomerData = RunContext.get().getDbAction().getActiveCustomerDetails();
        testContext.setCustomerCode(activeCustomerData.getFirst().get(UCRACCT_CUST_CODE).toString());
        testContext.setPremisesCode(activeCustomerData.getFirst().get(UCRACCT_PREM_CODE).toString());
        testContext.setNoteTypeCode(data.get("noteTypeCode"));
        testContext.setNoteText(data.get("noteText"));
        testContext.setOrigin(data.get("origin"));
        CreateAccountNoteRequest createAccountNoteRequest = new CreateAccountNoteRequest();
        createAccountNoteRequest = testContext.getCreateAccountNoteApiPage().getApiPayload(CREATEACCOUNTNOTE_INVALID_SERVICENOFORMAT_API, createAccountNoteRequest);
        testContext.getCreateAccountNoteApiPage().setRequestSpecification(CREATEACCOUNTNOTE_INVALID_SERVICENOFORMAT_API, createAccountNoteRequest);
        Response response = testContext.getCreateAccountNoteApiPage().sendRequest(HttpPost.METHOD_NAME, ACCOUNT_NOTE, 200);
        testContext.setResponse(response);
    }

    @When("a request is made to the CreateAccountNote Api with non-existent NoteType")
    public void a_request_is_made_to_the_create_account_note_api_with_non_existent_note_type(DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        List<Map<String, Object>> activeCustomerData = RunContext.get().getDbAction().getActiveCustomerDetails();
        testContext.setCustomerCode(activeCustomerData.getFirst().get(UCRACCT_CUST_CODE).toString());
        testContext.setPremisesCode(activeCustomerData.getFirst().get(UCRACCT_PREM_CODE).toString());
        testContext.setServiceNumber(activeCustomerData.getFirst().get(UCRSERV_NUM).toString());
        testContext.setNoteText(data.get("noteText"));
        testContext.setOrigin(data.get("origin"));
        CreateAccountNoteRequest createAccountNoteRequest = new CreateAccountNoteRequest();
        createAccountNoteRequest = testContext.getCreateAccountNoteApiPage().getApiPayload(CREATEACCOUNTNOTE_NONEXISTENT_NOTETYPE_API, createAccountNoteRequest);
        testContext.getCreateAccountNoteApiPage().setRequestSpecification(CREATEACCOUNTNOTE_NONEXISTENT_NOTETYPE_API, createAccountNoteRequest);
        Response response = testContext.getCreateAccountNoteApiPage().sendRequest(HttpPost.METHOD_NAME, ACCOUNT_NOTE, 200);
        testContext.setResponse(response);
    }

}
