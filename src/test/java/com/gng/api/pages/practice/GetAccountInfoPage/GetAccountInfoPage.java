package com.gng.api.pages.practice.GetAccountInfoPage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.GetAccountInfo.GetAccountInfoRequest;
import com.gng.api.pojo.CSIPojo.GetAccountInfo.GetAccountInformationResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.practice.GetAccountInfo.GetAccountInfoLabel;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.CSI_GET_ACCOUNT_INFO;

public class GetAccountInfoPage extends BasePage {

    private final GetAccountInfoApiHelper helper;

    public GetAccountInfoPage(TestContext testContext) {
        super(testContext);
        this.helper = new GetAccountInfoApiHelper();
    }

    public void validateResponseForTestConditions(GetAccountInfoLabel apiLabel, GetAccountInfoLabel testCondition) {
        GetAccountInfoRequest payload = helper.preparePayload(apiLabel);
        helper.preparePayloadForTestCondition(payload, testCondition);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, CSI_GET_ACCOUNT_INFO, 200);
        GetAccountInformationResponse getAccountInfoResponse =
                deserializeResponseToPojo(response, GetAccountInformationResponse.class);
        testContext.setGetAccountInformationResponse(getAccountInfoResponse);
        testContext.setResponse(response);
    }
}
