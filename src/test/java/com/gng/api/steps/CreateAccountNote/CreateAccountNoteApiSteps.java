package com.gng.api.steps.CreateAccountNote;

import com.gng.api.pages.CreateAccountNotePage.CreateAccountNoteApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CreateAccountNoteApiSteps {

    private final TestContext testContext;
    private final CreateAccountNoteApiPage createAccountNoteApiPage;

    public CreateAccountNoteApiSteps(TestContext testContext, CreateAccountNoteApiPage createAccountNoteApiPage) {
        this.testContext = testContext;
        this.createAccountNoteApiPage = createAccountNoteApiPage;
        testContext.setCreateAccountNoteApiPage(createAccountNoteApiPage);
    }

    @When("a request is made to the CreateAccountNote Api with Null NoteTypeCode")
    public void a_request_is_made_to_the_create_account_note_api_with_null_note_type_code(DataTable dataTable) {
        createAccountNoteApiPage.sendCreateAccountNoteRequestWithNullNoteTypeCode(dataTable);
    }

    @When("a request is made to the CreateAccountNote Api with Null CustomerCode")
    public void a_request_is_made_to_the_create_account_note_api_with_null_customer_code(DataTable dataTable) {
        createAccountNoteApiPage.sendCreateAccountNoteRequestWithNullCustomerCode(dataTable);
    }

    @When("a request is made to the CreateAccountNote Api with non-existent combination of CustomerCode and PremisesCode")
    public void a_request_is_made_to_the_create_account_note_api_with_non_existent_combination(DataTable dataTable) {
        createAccountNoteApiPage.sendCreateAccountNoteRequestWithNonExistentCombination(dataTable);
    }

    @When("a request is made to the CreateAccountNote Api with missing RequestID")
    public void a_request_is_made_to_the_create_account_note_api_with_missing_request_id(DataTable dataTable) {
        createAccountNoteApiPage.sendCreateAccountNoteRequestWithMissingRequestId(dataTable);
    }

    @When("a request is made to the CreateAccountNote Api with valid data")
    public void a_request_is_made_to_the_create_account_note_api_with_valid_data(DataTable dataTable) {
        createAccountNoteApiPage.sendCreateAccountNoteRequestWithValidData(dataTable);
    }

    @When("a request is made to the CreateAccountNote Api with {string} value is {string}")
    public void a_request_is_made_to_the_create_account_note_api_with_value_is(String param, String paramValue, DataTable dataTable) {
        createAccountNoteApiPage.sendCreateAccountNoteRequestWithParamValue(param, paramValue, dataTable);
    }

    @When("a request is made to the CreateAccountNote Api with Null NoteText")
    public void a_request_is_made_to_the_create_account_note_api_with_null_note_text(DataTable dataTable) {
        createAccountNoteApiPage.sendCreateAccountNoteRequestWithNullNoteText(dataTable);
    }

    @When("a request is made to the CreateAccountNote Api with Null Origin")
    public void a_request_is_made_to_the_create_account_note_api_with_null_origin(DataTable dataTable) {
        createAccountNoteApiPage.sendCreateAccountNoteRequestWithNullOrigin(dataTable);
    }

    @When("a request is made to the CreateAccountNote Api with Duplicate requestID")
    public void a_request_is_made_to_the_create_account_note_api_with_duplicate_request_id(DataTable dataTable) {
        createAccountNoteApiPage.sendCreateAccountNoteRequestWithDuplicateRequestId(dataTable);
    }

    @When("a request is made to the CreateAccountNote Api with Invalid Expiration Date Format")
    public void a_request_is_made_to_the_create_account_note_api_with_invalid_expiration_date_format(DataTable dataTable) {
        createAccountNoteApiPage.sendCreateAccountNoteRequestWithInvalidExpirationDate(dataTable);
    }

    @When("a request is made to the CreateAccountNote Api with non-existent combination of ServiceNumber and PremisesCode")
    public void a_request_is_made_to_the_create_account_note_api_with_non_existent_service_number_combination(DataTable dataTable) {
        createAccountNoteApiPage.sendCreateAccountNoteRequestWithNonExistentServiceNumber(dataTable);
    }

    @When("a request is made to the CreateAccountNote Api with Invalid ServiceNumber Format")
    public void a_request_is_made_to_the_create_account_note_api_with_invalid_service_number_format(DataTable dataTable) {
        createAccountNoteApiPage.sendCreateAccountNoteRequestWithInvalidServiceNumberFormat(dataTable);
    }

    @When("a request is made to the CreateAccountNote Api with non-existent NoteType")
    public void a_request_is_made_to_the_create_account_note_api_with_non_existent_note_type(DataTable dataTable) {
        createAccountNoteApiPage.sendCreateAccountNoteRequestWithNonExistentNoteType(dataTable);
    }

    @Then("verify response contains a valid NoteSequenceNumber")
    public void verify_response_contains_a_valid_note_sequence_number() {
        createAccountNoteApiPage.verifyValidNoteSequenceNumber();
    }

    @Then("verify NoteSequenceNumber match the value in the database")
    public void verify_note_sequence_number_match_the_value_in_the_database() {
        createAccountNoteApiPage.verifyNoteSequenceNumberInDatabase();
    }
}
