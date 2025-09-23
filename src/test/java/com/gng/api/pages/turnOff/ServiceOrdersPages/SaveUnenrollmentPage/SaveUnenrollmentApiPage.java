package com.gng.api.pages.turnOff.ServiceOrdersPages.SaveUnenrollmentPage;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.ServiceOrdersPojo.SaveUnenrollment.SaveUnenrollmentRequest;
import com.gng.api.pojo.ServiceOrdersPojo.SaveUnenrollment.SaveUnenrollmentResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.turnOff.ServiceOrdersSteps.SaveUnenrollment.SaveUnenrollmentApiLabel;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;
import static com.gng.api.constants.ApiEndPoint.SAVE_UNENROLLMENT;


public class SaveUnenrollmentApiPage extends BasePage {

    private final SaveUnenrollmentHelper helper;

    public SaveUnenrollmentApiPage(TestContext testContext) {
        super(testContext);
        this.helper = new SaveUnenrollmentHelper(testContext);
    }

    public void validateInvalidRequestAndLoginIDCases(SaveUnenrollmentApiLabel apiLabel, SaveUnenrollmentApiLabel requestID) {
        SaveUnenrollmentRequest payload = helper.preparePayload(apiLabel);
        helper.setRequestAndLoginIDBasedOnType(payload, requestID);
        helper.setMarketerReferenceData(payload, testContext.getMarketerReferenceData());
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SAVE_UNENROLLMENT, 200);
        SaveUnenrollmentResponse saveUnenrollmentResponse = deserializeResponseToPojo(response, SaveUnenrollmentResponse.class);
        testContext.setSaveUnenrollmentResponse(saveUnenrollmentResponse);
        testContext.setResponse(response);
    }

    public void validateInvalidParametersCases(SaveUnenrollmentApiLabel apiLabel, SaveUnenrollmentApiLabel requestID) {
        SaveUnenrollmentRequest payload = helper.preparePayload(apiLabel);
        helper.setParametersBasedOnType(payload, requestID);
        helper.setMarketerReferenceData(payload, testContext.getMarketerReferenceData());
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SAVE_UNENROLLMENT, 200);
        SaveUnenrollmentResponse saveUnenrollmentResponse = deserializeResponseToPojo(response, SaveUnenrollmentResponse.class);
        testContext.setSaveUnenrollmentResponse(saveUnenrollmentResponse);
        testContext.setResponse(response);
    }

    public void validateForActiveRAMVS(SaveUnenrollmentApiLabel apiLabel, String pricePlan, String sclsCode, SaveUnenrollmentApiLabel forwardingAddressIs, SaveUnenrollmentApiLabel type, SaveUnenrollmentApiLabel testCondition, Boolean setEmail, Boolean etcExists){
        SaveUnenrollmentRequest payload = helper.preparePayload(apiLabel);
        helper.setCustomerCodePremCodeAGLCServiceNo(payload, pricePlan, sclsCode, testCondition);
        helper.setTurnOffReasonAndSubReason(payload, testCondition);
        helper.setForwardingAddressDetailsBasedOnType(payload, forwardingAddressIs, type);
        helper.setEmailAddress(payload,setEmail);
        helper.setEtcExists(payload,etcExists);
        helper.setMarketerReferenceData(payload, testContext.getMarketerReferenceData());
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SAVE_UNENROLLMENT, 200);
        testContext.setResponse(response);
    }
}
