package com.gng.api.pages.ServiceOrdersPages.SaveEnrollmentPage;

import com.gng.api.pages.BasePage;

import com.gng.api.pojo.ServiceOrdersPojo.SaveEnrollment.SaveEnrollmentRequest;
import com.gng.api.pojo.ServiceOrdersPojo.SaveEnrollment.SaveEnrollmentResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.ServiceOrdersSteps.SaveEnrollment.SaveEnrollmentApiLabel;
import com.gng.api.util.FakerDataGenerator;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.SAVE_ENROLLMENT;


public class SaveEnrollmentApiPage extends BasePage {

    private final SaveEnrollmentHelper helper;

    public SaveEnrollmentApiPage(TestContext testContext) {
        super(testContext);
        this.helper = new SaveEnrollmentHelper(testContext);
    }

    public void sendSaveEnrollmentRequest(SaveEnrollmentApiLabel apiLabel)
    {
        SaveEnrollmentRequest payload = helper.preparePayload(apiLabel);
        payload.setRequestID(FakerDataGenerator.generateString(10));
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SAVE_ENROLLMENT, 200);
        SaveEnrollmentResponse saveEnrollmentResponse = deserializeResponseToPojo(response, SaveEnrollmentResponse.class);
        testContext.setSaveEnrollmentResponse(saveEnrollmentResponse);
    }

    public void validateInvalidRequestIDCases(SaveEnrollmentApiLabel apiLabel, SaveEnrollmentApiLabel requestID)
    {
        SaveEnrollmentRequest payload = helper.preparePayload(apiLabel);
        switch (requestID) {
            case EMPTY_REQUEST_ID:
                payload.setRequestID("");
                break;
            case DUPLICATE_REQUEST_ID:
                payload.setRequestID("123");
                break;
            case SPECIAL_CHARS_REQUEST_ID:
                payload.setRequestID(FakerDataGenerator.generateAlphanumericWithSpecialChars(10));
                break;
            case LONG_REQUEST_ID:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(200));
                break;
            case UNICODE_CHARS_REQUEST_ID:
                payload.setRequestID(FakerDataGenerator.generateUnicode());
                break;
            default:
                payload.setRequestID(FakerDataGenerator.generateString(10));
        }

        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SAVE_ENROLLMENT, 200);
        testContext.setResponse(response);
    }

}
