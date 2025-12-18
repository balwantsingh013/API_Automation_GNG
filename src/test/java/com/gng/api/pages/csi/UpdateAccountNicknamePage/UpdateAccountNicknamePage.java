package com.gng.api.pages.csi.UpdateAccountNicknamePage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.UpdateAccountNickname.UpdateAccountNicknameRequest;
import com.gng.api.pojo.CSIPojo.UpdateAccountNickname.UpdateAccountNicknameResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.UpdateAccountNickname.UpdateAccountNicknameLabel;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.UPDATE_ACCOUNT_NICKNAME;

public class UpdateAccountNicknamePage extends BasePage {

    private final UpdateAccountNicknameApiHelper helper;

    public UpdateAccountNicknamePage(TestContext testContext) {
        super(testContext);
        this.helper = new UpdateAccountNicknameApiHelper(testContext);
    }

    public void validateResponseForNegativeTestConditions(UpdateAccountNicknameLabel apiLabel, UpdateAccountNicknameLabel testCondition) {
        UpdateAccountNicknameRequest payload = helper.preparePayload(apiLabel);
        helper.preparePayloadForTestCondition(payload, testCondition);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, UPDATE_ACCOUNT_NICKNAME, 200);
        UpdateAccountNicknameResponse setAccountNicknameResponse = deserializeResponseToPojo(response, UpdateAccountNicknameResponse.class);
        testContext.setUpdateAccountNicknameResponse(setAccountNicknameResponse);
        testContext.setResponse(response);
    }
}
