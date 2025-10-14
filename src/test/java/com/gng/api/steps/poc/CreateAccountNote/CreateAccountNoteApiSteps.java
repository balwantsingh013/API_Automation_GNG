package com.gng.api.steps.poc.CreateAccountNote;
import com.gng.api.pages.poc.CreateAccountNotePage.CreateAccountNoteApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

import static com.gng.api.steps.poc.CreateAccountNote.CreateAccountNoteApiLabel.create_account_note;

@Slf4j
public class CreateAccountNoteApiSteps {

    private final TestContext testContext;
    private final CreateAccountNoteApiPage createAccountNoteApiPage;

    public CreateAccountNoteApiSteps(TestContext testContext, CreateAccountNoteApiPage createAccountNoteApiPage) {
        this.testContext = testContext;
        this.createAccountNoteApiPage = createAccountNoteApiPage;
        testContext.setCreateAccountNoteApiPage(createAccountNoteApiPage);
    }

    @Then("verify NoteSequenceNumber match the value in the database")
    public void verify_note_sequence_number_match_the_value_in_the_database() {
        createAccountNoteApiPage.verifyNoteSequenceNumberInDatabase();
    }

    @When("a request is made to the CreateBannerNotes Api with invalid parameters for {string} condition")
    public void a_request_is_made_to_the_create_account_note_api_with_invalid_parameters(String testCondition) {
        createAccountNoteApiPage.validateInvalidParametersCases(create_account_note, CreateAccountNoteApiLabel.valueOf(testCondition));
    }

    @When("a request is made to the CreateBannerNotes Api with valid parameters {string} noteText for {string} condition")
    public void a_request_is_made_to_CreateBannerNotesApi_With_Valid_Parameters_For_Condition(String noteText, String testCondition) {
        createAccountNoteApiPage.validatePositiveTestConditions(create_account_note, noteText, CreateAccountNoteApiLabel.valueOf(testCondition));

    }

    @Then("verify banner note is created successfully with {string} noteText for {string} condition")
    public void verify_note_created_successfully(String noteText, String testCondition) {
        createAccountNoteApiPage.verifyNoteCreatedWithCorrectLines(noteText, testCondition);
    }
}
