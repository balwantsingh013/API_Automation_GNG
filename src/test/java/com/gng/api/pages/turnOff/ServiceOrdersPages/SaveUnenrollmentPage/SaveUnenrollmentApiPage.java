package com.gng.api.pages.turnOff.ServiceOrdersPages.SaveUnenrollmentPage;

import com.gng.api.pages.BasePage;

import com.gng.api.pages.turnOff.ServiceOrdersPages.SaveUnenrollmentPage.SaveUnenrollmentHelper;
import com.gng.api.pojo.AccountsPojo.SearchAccounts.SearchAccountsRequest;
import com.gng.api.pojo.ServiceOrdersPojo.SaveUnenrollment.SaveUnenrollmentRequest;
import com.gng.api.pojo.ServiceOrdersPojo.SaveUnenrollment.SaveUnenrollmentResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.turnOff.ServiceOrdersSteps.SaveUnenrollment.SaveUnenrollmentApiLabel;
import com.gng.api.util.FakerDataGenerator;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;
import static com.gng.api.constants.ApiEndPoint.SAVE_UNENROLLMENT;
import static com.gng.api.constants.ApiEndPoint.SEARCH_ACCOUNTS;

public class SaveUnenrollmentApiPage extends BasePage {


    private final SaveUnenrollmentHelper helper;


    public SaveUnenrollmentApiPage(TestContext testContext) {
        super(testContext);
        this.helper = new SaveUnenrollmentHelper(testContext);
    }

    public void validateInvalidRequestIDCases(SaveUnenrollmentApiLabel apiLabel, SaveUnenrollmentApiLabel requestID) {
        SaveUnenrollmentRequest payload = helper.preparePayload(apiLabel);
        helper.setRequestIDBasedOnType(payload, requestID);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SAVE_UNENROLLMENT, 200);
        testContext.setResponse(response);
    }

    public void validateForActiveRAMVS(SaveUnenrollmentApiLabel apiLabel, String pricePlan, String sclsCode, String forwardingAddressIs, String type, String turnoffreason, String setEmail, String etcExists){
        SaveUnenrollmentRequest payload = helper.preparePayload(apiLabel);
        helper.setCustomerCodePremCodeAGLCServiceNo(payload, pricePlan, sclsCode);
        helper.setTurnOffReasonAndSubReason(payload, turnoffreason);
        helper.setForwardingAddressDetailsBasedOnType(payload, forwardingAddressIs, type);
        helper.setEmailAddress(payload,setEmail);
        helper.setEtcExists(payload,etcExists);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SAVE_UNENROLLMENT, 200);
        testContext.setResponse(response);
    }
}
