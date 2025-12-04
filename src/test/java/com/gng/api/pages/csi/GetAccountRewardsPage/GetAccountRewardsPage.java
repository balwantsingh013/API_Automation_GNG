package com.gng.api.pages.csi.GetAccountRewardsPage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.GetAccountRewards.GetAccountRewardsRequest;
import com.gng.api.pojo.CSIPojo.GetAccountRewards.GetAccountRewardsResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.GetAccountRewards.GetAccountRewardsLabel;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.GET_ACCOUNT_REWARDS;

public class GetAccountRewardsPage extends BasePage {

    private final com.gng.api.pages.csi.GetAccountRewardsPage.GetAccountRewardsApiHelper helper;

    public GetAccountRewardsPage(TestContext testContext) {
        super(testContext);
        this.helper = new GetAccountRewardsApiHelper(testContext);
    }

    public void validateResponseForNegativeTestConditions(GetAccountRewardsLabel apiLabel, GetAccountRewardsLabel testCondition) {
        GetAccountRewardsRequest payload = helper.preparePayload(apiLabel);
        helper.preparePayloadForNegativeTestCondition(payload, testCondition);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ACCOUNT_REWARDS, 200);
        GetAccountRewardsResponse getAccountRewardsResponse = deserializeResponseToPojo(response, GetAccountRewardsResponse.class);
        testContext.setGetAccountRewardsResponse(getAccountRewardsResponse);
        testContext.setResponse(response);
    }
}
