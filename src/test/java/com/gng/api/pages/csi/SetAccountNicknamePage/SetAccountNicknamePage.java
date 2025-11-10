package com.gng.api.pages.csi.SetAccountNicknamePage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.SetAccountNickname.SetAccountNicknameRequest;
import com.gng.api.pojo.CSIPojo.SetAccountNickname.SetAccountNicknameResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.SetAccountNickname.SetAccountNicknameLabel;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.SET_ACCOUNT_NICKNAME;

public class SetAccountNicknamePage extends BasePage {

    private final SetAccountNicknameApiHelper helper;

    public SetAccountNicknamePage(TestContext testContext) {
        super(testContext);
        this.helper = new SetAccountNicknameApiHelper(testContext);
    }

    public void validateResponseForNegativeTestConditions(SetAccountNicknameLabel apiLabel, SetAccountNicknameLabel testCondition) {
        SetAccountNicknameRequest payload = helper.preparePayload(apiLabel);
        helper.preparePayloadForNegativeTestCondition(payload, testCondition);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SET_ACCOUNT_NICKNAME, 200);
        SetAccountNicknameResponse setAccountNicknameResponse = deserializeResponseToPojo(response, SetAccountNicknameResponse.class);
        testContext.setSetAccountNicknameResponse(setAccountNicknameResponse);
        testContext.setResponse(response);
    }
}
