package com.gng.api.pages.csi.GetPaymentArrangementInfoPage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.GetPaymentArrangementInfo.GetPaymentArrangementInfoRequest;
import com.gng.api.pojo.CSIPojo.GetPaymentArrangementInfo.GetPaymentArrangementInfoResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.GetPaymentArrangementInfo.GetPaymentArrangementInfoLabel;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.GET_PAYMENT_ARRANGEMENT_INFO;

public class GetPaymentArrangementInfoPage extends BasePage {

    private final GetPaymentArrangementInfoApiHelper helper;

    public GetPaymentArrangementInfoPage(TestContext testContext) {
        super(testContext);
        this.helper = new GetPaymentArrangementInfoApiHelper(testContext);
    }

    public void validateResponseForTestCondition(GetPaymentArrangementInfoLabel apiLabel,
                                                 GetPaymentArrangementInfoLabel testCondition) {

        // Build base payload
        GetPaymentArrangementInfoRequest payload = helper.preparePayload(apiLabel);

        // Apply test-condition-specific modifications
        helper.preparePayloadForTestCondition(payload, testCondition);

        // Set request specification with token
        setRequestSpecification(payload, testContext.getAuthToken());

        // Execute API call
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_PAYMENT_ARRANGEMENT_INFO, 200);

        // Deserialize response
        GetPaymentArrangementInfoResponse getPaymentArrangementInfoResponse =
                deserializeResponseToPojo(response, GetPaymentArrangementInfoResponse.class);

        // Store in TestContext
        testContext.setGetPaymentArrangementInfoResponse(getPaymentArrangementInfoResponse);
        testContext.setResponse(response);
    }
}
