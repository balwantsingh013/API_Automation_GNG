package com.gng.api.pages.csi.GetUsageHistoryPage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.GetUsageHistory.GetUsageHistoryRequest;
import com.gng.api.pojo.CSIPojo.GetUsageHistory.GetUsageHistoryResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.GetUsageHistory.GetUsageHistoryLabel;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.GET_USAGE_HISTORY;

public class GetUsageHistoryPage extends BasePage {

    private final GetUsageHistoryApiHelper helper;

    public GetUsageHistoryPage(TestContext testContext) {
        super(testContext);
        this.helper = new GetUsageHistoryApiHelper(testContext);
    }

    public void validateResponseForTestCondition(GetUsageHistoryLabel apiLabel,
                                                 GetUsageHistoryLabel testCondition) {

        // Build base payload
        GetUsageHistoryRequest payload = helper.preparePayload(apiLabel);

        // Apply test-condition-specific modifications
        helper.preparePayloadForTestCondition(payload, testCondition);

        // Set request specification with token
        setRequestSpecification(payload, testContext.getAuthToken());

        // Execute API call
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_USAGE_HISTORY, 200);

        // Deserialize response
        GetUsageHistoryResponse getUsageHistoryResponse =
                deserializeResponseToPojo(response, GetUsageHistoryResponse.class);

        // Store in TestContext
        testContext.setGetUsageHistoryResponse(getUsageHistoryResponse);
        testContext.setResponse(response);
    }
}
