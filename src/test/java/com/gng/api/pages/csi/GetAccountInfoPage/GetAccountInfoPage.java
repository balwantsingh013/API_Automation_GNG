package com.gng.api.pages.csi.GetAccountInfoPage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.GetAccountInfo.GetAccountInfoRequest;
import com.gng.api.pojo.CSIPojo.GetAccountInfo.GetAccountInformationResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.GetAccountInfo.GetAccountInfoLabel;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.CSI_GET_ACCOUNT_INFO;

public class GetAccountInfoPage extends BasePage {

    private final com.gng.api.pages.csi.GetAccountInfoPage.GetAccountInfoApiHelper helper;

    public GetAccountInfoPage(TestContext testContext) {
        super(testContext);
        this.helper = new GetAccountInfoApiHelper(testContext);
    }

    public void validateResponseForTestConditions(GetAccountInfoLabel apiLabel, GetAccountInfoLabel testCondition) {
        // Prepare base payload
        GetAccountInfoRequest payload = helper.preparePayload(apiLabel);
        // Adjust payload for the specific test condition
        helper.preparePayloadForTestCondition(payload, testCondition);
        // Set request specification with auth token
        setRequestSpecification(payload, testContext.getAuthToken());
        // Send request
        Response response = sendRequest(HttpPost.METHOD_NAME, CSI_GET_ACCOUNT_INFO, 200);
        // Deserialize response
        GetAccountInformationResponse getAccountInfoResponse = deserializeResponseToPojo(response, GetAccountInformationResponse.class);
        // Store in context
        testContext.setGetAccountInformationResponse(getAccountInfoResponse);
        testContext.setResponse(response);
    }
}
