package com.gng.api.pages.practice.GetPaymentHistoryPage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.GetPaymentHistory.GetPaymentHistoryRequest;
import com.gng.api.pojo.CSIPojo.GetPaymentHistory.GetPaymentHistoryResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.practice.GetPaymentHistory.GetPaymentHistoryLabel;
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
        GetPaymentHistoryRequest payload = helper.preparePayload(apiLabel);
        helper.preparePayloadForTestCondition(payload, testCondition);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_PAYMENT_HISTORY, 200);
        GetPaymentHistoryResponse getPaymentHistoryResponse =
                deserializeResponseToPojo(response, GetPaymentHistoryResponse.class);
        testContext.setGetPaymentHistoryResponse(getPaymentHistoryResponse);
        testContext.setResponse(response);
    }
}
