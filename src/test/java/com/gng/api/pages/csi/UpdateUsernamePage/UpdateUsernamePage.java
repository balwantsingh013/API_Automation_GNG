package com.gng.api.pages.csi.UpdateUsernamePage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.UpdateUsername.UpdateUsernameRequest;
import com.gng.api.pojo.CSIPojo.UpdateUsername.UpdateUsernameResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.UpdateUsername.UpdateUsernameLabel;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.UPDATE_USERNAME;

public class UpdateUsernamePage extends BasePage {

    private final com.gng.api.pages.csi.UpdateUsernamePage.UpdateUsernameApiHelper helper;

    public UpdateUsernamePage(TestContext testContext) {
        super(testContext);
        this.helper = new UpdateUsernameApiHelper(testContext);
    }

    public void validateResponseForTestConditions(UpdateUsernameLabel apiLabel, UpdateUsernameLabel testCondition) {
        UpdateUsernameRequest payload = helper.preparePayload(apiLabel);
        helper.preparePayloadForTestCondition(payload, testCondition);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, UPDATE_USERNAME, 200);
        UpdateUsernameResponse updateUsernameResponse = deserializeResponseToPojo(response, UpdateUsernameResponse.class);
        testContext.setUpdateUsernameResponse(updateUsernameResponse);
        testContext.setResponse(response);
    }

    public void validateIfUSernameIsPresent(){
        helper.validateUsername();
    }

    public void validateAccountAssociation(){
        helper.validateAccountAssociationWithUsername();
    }
}
