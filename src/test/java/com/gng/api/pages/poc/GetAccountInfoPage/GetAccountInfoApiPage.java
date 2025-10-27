package com.gng.api.pages.poc.GetAccountInfoPage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.pojo.AccountsPojo.getAccountInfo.GetAccountInfoRequest;
import com.gng.api.pojo.AccountsPojo.getAccountInfo.GetAccountInfoResponse;
import com.gng.api.steps.poc.GetAccountInfo.GetAccountInfoApiLabel;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;
import static com.gng.api.constants.ApiEndPoint.GET_ACCOUNT_INFO;

public class GetAccountInfoApiPage extends BasePage {
    private final GetAccountInfoHelper helper;

    public GetAccountInfoApiPage(TestContext testContext) {
        super(testContext);
        this.helper = new GetAccountInfoHelper(testContext);
    }

    public void verifyAccountInformationWithDatabase() {
        helper.verifyAccountInformationWithDatabase();
    }

    public void validateInvalidParameters(GetAccountInfoApiLabel apiLabel, GetAccountInfoApiLabel testCondition) {
        GetAccountInfoRequest payload = helper.preparePayload(apiLabel);
        helper.setParametersBasedOnTypeNegative(payload, testCondition);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ACCOUNT_INFO, 200);
        GetAccountInfoResponse pojo = deserializeResponseToPojo(response, GetAccountInfoResponse.class);
        testContext.setGetAccountInfoResponse(pojo);
        testContext.setResponse(response);
    }

    public void validatePositiveConditions(GetAccountInfoApiLabel apiLabel, GetAccountInfoApiLabel testCondition) {
        GetAccountInfoRequest payload = helper.preparePayload(apiLabel);
        helper.setParametersBasedOnTypePositive(payload, testCondition);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ACCOUNT_INFO, 200);
        GetAccountInfoResponse pojo = deserializeResponseToPojo(response, GetAccountInfoResponse.class);
        testContext.setGetAccountInfoResponse(pojo);
        testContext.setResponse(response);
    }
}
