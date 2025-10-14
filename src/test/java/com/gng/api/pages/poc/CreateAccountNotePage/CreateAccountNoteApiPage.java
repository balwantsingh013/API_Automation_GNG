package com.gng.api.pages.poc.CreateAccountNotePage;

import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.AccountsPojo.createAccountNote.CreateAccountNoteResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.pojo.AccountsPojo.createAccountNote.CreateAccountNoteRequest;
import com.gng.api.steps.poc.CreateAccountNote.CreateAccountNoteApiLabel;
import io.cucumber.datatable.DataTable;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import java.util.List;
import java.util.Map;

import static com.gng.api.constants.ApiEndPoint.CREATE_ACCOUNT_NOTE;
import static com.gng.api.constants.DBConstant.UCBACCT_CUST_CODE;
import static com.gng.api.constants.DBConstant.UCBACCT_PREM_CODE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CreateAccountNoteApiPage extends BasePage {
    private final CreateAccountNoteHelper helper;

    public CreateAccountNoteApiPage(TestContext testContext) {
        super(testContext);
        this.helper = new CreateAccountNoteHelper(testContext);
    }

    public void validateInvalidParametersCases(CreateAccountNoteApiLabel apiLabel, CreateAccountNoteApiLabel testCondition) {
        CreateAccountNoteRequest payload = helper.preparePayload(apiLabel);
        helper.setParametersBasedOnTypeNegative(payload, testCondition);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, CREATE_ACCOUNT_NOTE, 200);
        CreateAccountNoteResponse pojo = deserializeResponseToPojo(response, CreateAccountNoteResponse.class);
        testContext.setCreateAccountNoteResponse(pojo);
        testContext.setResponse(response);
        testContext.setCustomerCode(payload.getCustomerCode());
        testContext.setPremisesCode(payload.getPremisesCode());
    }

    public void validatePositiveTestConditions(CreateAccountNoteApiLabel apiLabel, String noteText, CreateAccountNoteApiLabel testCondition) {
        CreateAccountNoteRequest payload = helper.preparePayload(apiLabel);
        helper.setParametersBasedOnTypePositive(payload, noteText, testCondition);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, CREATE_ACCOUNT_NOTE, 200);
        CreateAccountNoteResponse pojo = deserializeResponseToPojo(response, CreateAccountNoteResponse.class);
        testContext.setCreateAccountNoteResponse(pojo);
        testContext.setResponse(response);
        testContext.setCustomerCode(payload.getCustomerCode());
        testContext.setPremisesCode(payload.getPremisesCode());
    }


    public void verifyNoteCreatedWithCorrectLines(String noteText, String testCondition){
        helper.verifyNoteCreatedWithCorrectLines(noteText, testCondition);
    }

    public void verifyNoteSequenceNumberInDatabase() {
        String noteSequenceNumberDB = testContext.getCreateAccountNoteResponse().getData().getNoteSequenceNumber();
        List<Map<String, Object>> noteSequenceNumberData = ApplicationContext.get().getDbAction()
                .getNoteSequenceNumber(noteSequenceNumberDB);


        String custCodeDB = noteSequenceNumberData.getFirst().get(UCBACCT_CUST_CODE).toString();
        String premCodeDB = noteSequenceNumberData.getFirst().get(UCBACCT_PREM_CODE).toString();

        assertThat("noteSequenceNumber in Response and DB doesn't match",
                noteSequenceNumberDB, equalTo(noteSequenceNumberDB));
        assertThat("CustCode in Response and DB doesn't match",
                custCodeDB, equalTo(testContext.getCustomerCode()));
        assertThat("PremCode in Response and DB doesn't match",
                premCodeDB, equalTo(testContext.getPremisesCode()));
    }
}
