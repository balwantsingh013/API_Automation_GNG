package com.gng.api.pages.csi.UpdateBankDraftPage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.UpdateBankDraft.UpdateBankDraftRequest;
import com.gng.api.pojo.CSIPojo.UpdateBankDraft.UpdateBankDraftResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.UpdateBankDraft.UpdateBankDraftLabel;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.UPDATE_BANK_DRAFT;

public class UpdateBankDraftPage extends BasePage {

    private final UpdateBankDraftApiHelper helper;

    public UpdateBankDraftPage(TestContext testContext) {
        super(testContext);
        this.helper = new UpdateBankDraftApiHelper(testContext);
    }

    public void validateResponseForTestConditions(UpdateBankDraftLabel apiLabel,
                                                  UpdateBankDraftLabel testCondition) {

        // Prepare base payload
        UpdateBankDraftRequest payload = helper.preparePayload(apiLabel);

        // Apply test-condition-specific modifications
        helper.preparePayloadForTestCondition(payload, testCondition);

        // Set request specification with auth token
        setRequestSpecification(payload, testContext.getAuthToken());

        // Execute API call
        Response response = sendRequest(HttpPost.METHOD_NAME, UPDATE_BANK_DRAFT, 200);

        // Deserialize response
        UpdateBankDraftResponse updateBankDraftResponse =
                deserializeResponseToPojo(response, UpdateBankDraftResponse.class);

        // Store in test context
        testContext.setUpdateBankDraftResponse(updateBankDraftResponse);
        testContext.setResponse(response);
    }
}
