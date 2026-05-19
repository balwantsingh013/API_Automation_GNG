package com.gng.api.pages.csi.UpdatePaperlessCommunicationsPage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.UpdatePaperlessCommunications.UpdatePaperlessCommunicationsRequest;
import com.gng.api.pojo.CSIPojo.UpdatePaperlessCommunications.UpdatePaperlessCommunicationsResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.UpdatePaperlessCommunications.UpdatePaperlessCommunicationsLabel;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.UPDATE_PAPERLESS_COMMUNICATIONS;

public class UpdatePaperlessCommunicationsPage extends BasePage {

    private final UpdatePaperlessCommunicationsApiHelper helper;

    public UpdatePaperlessCommunicationsPage(TestContext testContext) {
        super(testContext);
        this.helper = new UpdatePaperlessCommunicationsApiHelper(testContext);
    }

    public void validateResponseForNegativeTestConditions(UpdatePaperlessCommunicationsLabel apiLabel, UpdatePaperlessCommunicationsLabel testCondition) {
        UpdatePaperlessCommunicationsRequest payload = helper.preparePayload(apiLabel);
        helper.preparePayloadForTestCondition(payload, testCondition);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, UPDATE_PAPERLESS_COMMUNICATIONS, 200);
        UpdatePaperlessCommunicationsResponse updatePaperlessCommunicationsResponse = deserializeResponseToPojo(response, UpdatePaperlessCommunicationsResponse.class);
        testContext.setUpdatePaperlessCommunicationsResponse(updatePaperlessCommunicationsResponse);
        testContext.setResponse(response);
    }

}