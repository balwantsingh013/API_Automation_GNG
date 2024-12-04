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

}
