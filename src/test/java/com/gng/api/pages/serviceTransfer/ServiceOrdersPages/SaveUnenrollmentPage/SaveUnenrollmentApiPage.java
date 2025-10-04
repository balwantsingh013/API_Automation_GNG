package com.gng.api.pages.serviceTransfer.ServiceOrdersPages.SaveUnenrollmentPage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.ServiceOrdersPojo.SaveUnenrollment.SaveUnenrollmentRequest;
import com.gng.api.pojo.ServiceOrdersPojo.SaveUnenrollment.SaveUnenrollmentResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.serviceTransfer.ServiceOrdersSteps.SaveUnenrollment.SaveUnenrollmentApiLabel;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.SAVE_UNENROLLMENT;

public class SaveUnenrollmentApiPage extends BasePage {

    private final SaveUnenrollmentHelper helper;

    public SaveUnenrollmentApiPage(TestContext testContext) {
        super(testContext);
        this.helper = new SaveUnenrollmentHelper(testContext);
    }

    public void validateInvalidParametersCases(SaveUnenrollmentApiLabel apiLabel, SaveUnenrollmentApiLabel testCondition) {
        SaveUnenrollmentRequest payload = helper.preparePayload(apiLabel);
        helper.setParametersBasedOnTypeForServiceTransfer(payload, testCondition);
     //   helper.setMarketerReferenceData(payload, testContext.getMarketerReferenceData());
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SAVE_UNENROLLMENT, 200);
        SaveUnenrollmentResponse pojo = deserializeResponseToPojo(response, SaveUnenrollmentResponse.class);
        testContext.setSaveUnenrollmentResponse(pojo);
        testContext.setResponse(response);
    }
}
