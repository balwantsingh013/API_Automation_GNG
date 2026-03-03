package com.gng.api.pages.csi.UpdatePasswordPage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.UpdatePassword.UpdatePasswordRequest;
import com.gng.api.pojo.CSIPojo.UpdatePassword.UpdatePasswordResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.UpdatePassword.UpdatePasswordLabel;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.UPDATE_PASSWORD;

public class UpdatePasswordPage extends BasePage {

    private final UpdatePasswordApiHelper helper;

    public UpdatePasswordPage(TestContext testContext) {
        super(testContext);
        this.helper = new UpdatePasswordApiHelper(testContext);
    }

    public void validateResponseForNegativeTestConditions(UpdatePasswordLabel apiLabel, UpdatePasswordLabel testCondition) {
        UpdatePasswordRequest payload = helper.preparePayload(apiLabel);
        helper.preparePayloadForTestCondition(payload, testCondition);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, UPDATE_PASSWORD, 200);
        UpdatePasswordResponse updatePasswordResponse = deserializeResponseToPojo(response, UpdatePasswordResponse.class);
        testContext.setUpdatePasswordResponse(updatePasswordResponse);
        testContext.setResponse(response);
    }

    public void rollbackPasswordToPreviousOne(){
        helper.changePasswordWithQuery();
    }

    public void verifyUpdatedPassword(){
        helper.validateIfPasswordIsChanged();
    }
}
