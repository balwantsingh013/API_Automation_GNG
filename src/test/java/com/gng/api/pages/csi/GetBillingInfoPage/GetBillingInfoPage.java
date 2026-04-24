package com.gng.api.pages.csi.GetBillingInfoPage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.GetBillingInfo.GetBillingInfoRequest;
import com.gng.api.pojo.CSIPojo.GetBillingInfo.GetBillingInfoResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.GetBillingInfo.GetBillingInfoLabel;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.GET_BILLING_INFO;

public class GetBillingInfoPage extends BasePage {

    private final GetBillingInfoApiHelper helper;

    public GetBillingInfoPage(TestContext testContext) {
        super(testContext);
        this.helper = new GetBillingInfoApiHelper(testContext);
    }

    public void validateResponseForNegativeTestConditions(GetBillingInfoLabel apiLabel, GetBillingInfoLabel testCondition) {
        GetBillingInfoRequest payload = helper.preparePayload(apiLabel);
        helper.preparePayloadForTestCondition(payload, testCondition);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_BILLING_INFO, 200);
        GetBillingInfoResponse getBillingInfoResponse = deserializeResponseToPojo(response, GetBillingInfoResponse.class);
        testContext.setGetBillingInfoResponse(getBillingInfoResponse);
        testContext.setResponse(response);
    }

}