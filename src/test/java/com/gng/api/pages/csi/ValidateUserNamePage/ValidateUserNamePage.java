package com.gng.api.pages.csi.ValidateUserNamePage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.ValidateUsername.ValidateUsernameRequest;
import com.gng.api.pojo.CSIPojo.ValidateUsername.ValidateUsernameResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.ValidateUserName.ValidateUserNameLabel;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.VALIDATE_USERNAME;

public class ValidateUserNamePage extends BasePage{

    private final com.gng.api.pages.csi.ValidateUserNamePage.ValidateUserNameApiHelper helper;

    public ValidateUserNamePage(TestContext testContext) {
        super(testContext);
        this.helper = new ValidateUserNameApiHelper(testContext);
    }

    public void validateResponseForNegativeTestConditions(ValidateUserNameLabel apiLabel, ValidateUserNameLabel testCondition) {
        ValidateUsernameRequest payload = helper.preparePayload(apiLabel);
        helper.preparePayloadForTestCondition(payload, testCondition);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, VALIDATE_USERNAME, 200);
        ValidateUsernameResponse validateUsernameResponse = deserializeResponseToPojo(response, ValidateUsernameResponse.class);
        testContext.setValidateUsernameResponse(validateUsernameResponse);
        testContext.setResponse(response);
    }
}
