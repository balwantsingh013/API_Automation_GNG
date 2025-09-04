package com.gng.api.pages.turnOn.ServiceOrdersPages.SaveEnrollmentPage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.pages.BasePage;

import com.gng.api.pojo.ServiceOrdersPojo.SaveEnrollment.SaveEnrollmentRequest;
import com.gng.api.pojo.ServiceOrdersPojo.SaveEnrollment.SaveEnrollmentResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.turnOn.ServiceOrdersSteps.SaveEnrollment.SaveEnrollmentApiLabel;
import com.gng.api.util.FakerDataGenerator;
import io.cucumber.datatable.DataTable;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.SAVE_ENROLLMENT;


public class SaveEnrollmentApiPage extends BasePage {

    private final SaveEnrollmentHelper helper;
    public SaveEnrollmentApiPage(TestContext testContext) {
        super(testContext);
        this.helper = new SaveEnrollmentHelper(testContext);
    }

    public void sendSaveEnrollmentRequest(SaveEnrollmentApiLabel apiLabel) {
        SaveEnrollmentRequest payload = helper.preparePayload(apiLabel);
        payload.setRequestID(FakerDataGenerator.generateString(10));
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SAVE_ENROLLMENT, 200);
        SaveEnrollmentResponse saveEnrollmentResponse = deserializeResponseToPojo(response, SaveEnrollmentResponse.class);
        testContext.setSaveEnrollmentResponse(saveEnrollmentResponse);
    }

    public void validateInvalidRequestIDCases(SaveEnrollmentApiLabel apiLabel, SaveEnrollmentApiLabel requestID) {
        SaveEnrollmentRequest payload = helper.preparePayload(apiLabel);
        helper.setRequestIDBasedOnType(payload, requestID);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SAVE_ENROLLMENT, 200);
        testContext.setResponse(response);
    }

        public void validateInvalidCustomerCodeCases(SaveEnrollmentApiLabel apiLabel, SaveEnrollmentApiLabel customerCODE)
    {
        SaveEnrollmentRequest payload = helper.preparePayload(apiLabel);
        helper.setCustomerCodeBasedOnType(payload, customerCODE);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SAVE_ENROLLMENT, 200);
        testContext.setResponse(response);
    }
    public void validateInvalidPremisesCodeCases(SaveEnrollmentApiLabel apiLabel, SaveEnrollmentApiLabel premisesCode) {
        SaveEnrollmentRequest payload = helper.preparePayload(apiLabel);
        helper.setPremisesCodeBasedOnType(payload, premisesCode);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SAVE_ENROLLMENT, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidTransactionIDCases(SaveEnrollmentApiLabel apiLabel, SaveEnrollmentApiLabel testCondition) {
        SaveEnrollmentRequest payload = helper.preparePayload(apiLabel);
        helper.setTransactionIDBasedOnType(payload, testCondition);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SAVE_ENROLLMENT, 200);
        testContext.setResponse(response);
    }
    public void validateInvalidTransactionTypeCases(SaveEnrollmentApiLabel apiLabel, SaveEnrollmentApiLabel transactionType) {
        SaveEnrollmentRequest payload = helper.preparePayload(apiLabel);
        helper.setTransactionTypeBasedOnType(payload, transactionType);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SAVE_ENROLLMENT, 200);
        testContext.setResponse(response);
    }
    public void validateInvalidPlanCodeCases(SaveEnrollmentApiLabel apiLabel, SaveEnrollmentApiLabel planCode) {
        SaveEnrollmentRequest payload = helper.preparePayload(apiLabel);
        helper.setPlanCodeBasedOnType(payload, planCode);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SAVE_ENROLLMENT, 200);
        testContext.setResponse(response);
    }
    public void validateInvalidLoginIDCases(SaveEnrollmentApiLabel apiLabel, SaveEnrollmentApiLabel loginID) {
        SaveEnrollmentRequest payload = helper.preparePayload(apiLabel);
        helper.setLoginIDBasedOnType(payload, loginID);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SAVE_ENROLLMENT, 200);
        testContext.setResponse(response);
    }
    public void validateInvalidEnrollmentStatusCases(SaveEnrollmentApiLabel apiLabel, SaveEnrollmentApiLabel enrollmentStatus) {
        SaveEnrollmentRequest payload = helper.preparePayload(apiLabel);
        helper.setEnrollmentStatusBasedOnType(payload, enrollmentStatus);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SAVE_ENROLLMENT, 200);
        testContext.setResponse(response);
    }
    public void validateInvalidBillingPlanCases(SaveEnrollmentApiLabel apiLabel, SaveEnrollmentApiLabel billingPlan) {
        SaveEnrollmentRequest payload = helper.preparePayload(apiLabel);
        helper.setBillingPlanBasedOnType(payload, billingPlan);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SAVE_ENROLLMENT, 200);
        testContext.setResponse(response);
    }

    public void validateSaveEnrollmentPositiveTCs(SaveEnrollmentApiLabel apiLabel,SaveEnrollmentApiLabel testCondition, String planCode, String promotionCode){
        SaveEnrollmentRequest payload = helper.preparePayload(apiLabel);
        helper.setSaveEnrollmentRequestParametersAsPerTestCondition(payload, testCondition, planCode, promotionCode);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SAVE_ENROLLMENT, 200);
        SaveEnrollmentResponse saveEnrollmentResponse = deserializeResponseToPojo(response, SaveEnrollmentResponse.class);
        testContext.setSaveEnrollmentResponse(saveEnrollmentResponse);
        testContext.setResponse(response);
    }

    public void validateCEForPreviouslySavedEnrollment(SaveEnrollmentApiLabel apiLabel,SaveEnrollmentApiLabel testCondition, String planCode, String promotionCode){
        SaveEnrollmentRequest payload = helper.preparePayload(apiLabel);
        helper.setSaveEnrollmentRequestForPreviouslySavedEnrollment(payload, testCondition, planCode, promotionCode);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SAVE_ENROLLMENT, 200);
        SaveEnrollmentResponse saveEnrollmentResponse = deserializeResponseToPojo(response, SaveEnrollmentResponse.class);
        testContext.setSaveEnrollmentResponse(saveEnrollmentResponse);
        testContext.setResponse(response);
    }

    public void validateEnrollmentAfterRequote(SaveEnrollmentApiLabel apiLabel,SaveEnrollmentApiLabel testCondition, String planCode){
        SaveEnrollmentRequest payload = helper.preparePayload(apiLabel);
        helper.setSaveEnrollmentAfterRequote(payload, testCondition, planCode);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SAVE_ENROLLMENT, 200);
        SaveEnrollmentResponse saveEnrollmentResponse = deserializeResponseToPojo(response, SaveEnrollmentResponse.class);
        testContext.setSaveEnrollmentResponse(saveEnrollmentResponse);
        testContext.setResponse(response);
    }

    public void performDatabaseValidationAfterEnrollment(DataTable dataTable){
        helper.databaseValidationPostEnrollment(dataTable);
    }

    public void validateSaveEnrollmentByPlanCode
            (SaveEnrollmentApiLabel apiLabel, GlobalEnums.PlanCode planCode, SaveEnrollmentApiLabel testCondition) {
        SaveEnrollmentRequest payload = helper.preparePayload(apiLabel);
        helper.setRequestParamsByPlanCode(payload, planCode, testCondition);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SAVE_ENROLLMENT, 200);
        testContext.setResponse(response);
        testContext.setSaveEnrollmentResponse(deserializeResponseToPojo(response, SaveEnrollmentResponse.class));
    }
}
