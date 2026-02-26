package com.gng.api.pages.csi.GetBillHistoryPage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.GetBillHistory.GetBillHistoryRequest;
import com.gng.api.pojo.CSIPojo.GetBillHistory.GetBillHistoryResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.GetBillHistory.GetBillHistoryLabel;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.GET_BILL_HISTORY;

public class GetBillHistoryPage extends BasePage {

    private final GetBillHistoryApiHelper helper;

    public GetBillHistoryPage(TestContext testContext) {
        super(testContext);
        this.helper = new GetBillHistoryApiHelper(testContext);
    }

    public void validateResponseForTestConditions(GetBillHistoryLabel apiLabel,
                                                  GetBillHistoryLabel testCondition) {

        // Prepare base payload
        GetBillHistoryRequest payload = helper.preparePayload(apiLabel);

        // Apply test-condition-specific modifications
        helper.preparePayloadForTestCondition(payload, testCondition);

        // Set request spec with token
        setRequestSpecification(payload, testContext.getAuthToken());

        // Execute API call
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_BILL_HISTORY, 200);

        // Deserialize response
        GetBillHistoryResponse billingHistoryResponse =
                deserializeResponseToPojo(response, GetBillHistoryResponse.class);

        // Store in test context
        testContext.setGetBillHistoryResponse(billingHistoryResponse);
        testContext.setResponse(response);
    }
}
