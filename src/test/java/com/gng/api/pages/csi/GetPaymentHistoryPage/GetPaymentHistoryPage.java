package com.gng.api.pages.csi.GetPaymentHistoryPage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.GetPaymentHistory.GetPaymentHistoryRequest;
import com.gng.api.pojo.CSIPojo.GetPaymentHistory.GetPaymentHistoryResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.GetPaymentHistory.GetPaymentHistoryLabel;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.GET_PAYMENT_HISTORY;

public class GetPaymentHistoryPage extends BasePage {

    private final GetPaymentHistoryApiHelper helper;

    public GetPaymentHistoryPage(TestContext testContext) {
        super(testContext);
        this.helper = new GetPaymentHistoryApiHelper(testContext);
    }

    public void validateResponseForTestCondition(GetPaymentHistoryLabel apiLabel,
                                                 GetPaymentHistoryLabel testCondition) {

        // Build base payload
        GetPaymentHistoryRequest payload = helper.preparePayload(apiLabel);

        // Apply test-condition-specific modifications
        helper.preparePayloadForTestCondition(payload, testCondition);

        // Set request specification with token
        setRequestSpecification(payload, testContext.getAuthToken());

        // Execute API call
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_PAYMENT_HISTORY, 200);

        // Deserialize response
        GetPaymentHistoryResponse getPaymentHistoryResponse =
                deserializeResponseToPojo(response, GetPaymentHistoryResponse.class);

        // Store in TestContext
        testContext.setGetPaymentHistoryResponse(getPaymentHistoryResponse);
        testContext.setResponse(response);
    }
}
