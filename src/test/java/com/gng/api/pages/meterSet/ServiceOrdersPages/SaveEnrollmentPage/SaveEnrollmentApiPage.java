package com.gng.api.pages.meterSet.ServiceOrdersPages.SaveEnrollmentPage;



import com.gng.api.pages.BasePage;
import com.gng.api.pages.meterSet.ServiceOrdersPages.SaveEnrollmentPage.SaveEnrollmentHelper;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.pojo.ServiceOrdersPojo.SaveEnrollment.SaveEnrollmentRequest;
import com.gng.api.pojo.ServiceOrdersPojo.SaveEnrollment.SaveEnrollmentResponse;
import com.gng.api.steps.meterSet.ServiceOrdersSteps.SaveEnrollment.SaveEnrollmentApiLabel;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.SAVE_ENROLLMENT;

public class SaveEnrollmentApiPage extends BasePage {
    private final SaveEnrollmentHelper helper;

    public SaveEnrollmentApiPage(TestContext testContext) {
        super(testContext);
        this.helper = new SaveEnrollmentHelper(testContext);
    }

    public void validateExternalConditions(SaveEnrollmentApiLabel apiLabel, SaveEnrollmentApiLabel testCondition) {
        SaveEnrollmentRequest payload = helper.preparePayload(apiLabel);
        helper.setParametersBasedOnTypeExternal(payload, testCondition);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SAVE_ENROLLMENT, 200);
        SaveEnrollmentResponse pojo = deserializeResponseToPojo(response, SaveEnrollmentResponse.class);
        testContext.setSaveEnrollmentResponse(pojo);
        testContext.setResponse(response);
    }

    public void validateSecondCallExternalConditions(SaveEnrollmentApiLabel apiLabel, SaveEnrollmentApiLabel testCondition) {
        SaveEnrollmentRequest payload = helper.preparePayload(apiLabel);
        helper.setParametersSecondCallTypeExternal(payload, testCondition);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SAVE_ENROLLMENT, 200);
        SaveEnrollmentResponse pojo = deserializeResponseToPojo(response, SaveEnrollmentResponse.class);
        testContext.setSaveEnrollmentResponse(pojo);
        testContext.setResponse(response);
    }
}
