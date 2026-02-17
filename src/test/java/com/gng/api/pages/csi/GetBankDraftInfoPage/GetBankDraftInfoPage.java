package com.gng.api.pages.csi.GetBankDraftInfoPage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.GetBankDraftInfo.GetBankDraftInfoRequest;
import com.gng.api.pojo.CSIPojo.GetBankDraftInfo.GetBankDraftInfoResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.GetBankDraftInfo.GetBankDraftInfoLabel;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.GET_BANK_DRAFT_INFO;

public class GetBankDraftInfoPage extends BasePage {

    private final GetBankDraftInfoApiHelper helper;

    public GetBankDraftInfoPage(TestContext testContext) {
        super(testContext);
        this.helper = new GetBankDraftInfoApiHelper(testContext);
    }

    public void validateResponseForTestConditions(GetBankDraftInfoLabel apiLabel,
                                                  GetBankDraftInfoLabel testCondition) {

        // Prepare base payload
        GetBankDraftInfoRequest payload = helper.preparePayload(apiLabel);

        // Apply test-condition-specific modifications
        helper.preparePayloadForTestCondition(payload, testCondition);

        // Set request specification with auth token
        setRequestSpecification(payload, testContext.getAuthToken());

        // Execute API call
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_BANK_DRAFT_INFO, 200);

        // Deserialize response
        GetBankDraftInfoResponse getBankDraftInfoResponse =
                deserializeResponseToPojo(response, GetBankDraftInfoResponse.class);

        // Store in test context
        testContext.setGetBankDraftInfoResponse(getBankDraftInfoResponse);
        testContext.setResponse(response);
    }
}
