package com.gng.api.pages.csi.UpdateMailingAddressPage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.UpdateMailingAddress.UpdateMailingAddressRequest;
import com.gng.api.pojo.CSIPojo.UpdateMailingAddress.UpdateMailingAddressResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.UpdateMailingAddress.UpdateMailingAddressLabel;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.UPDATE_MAILING_ADDRESS;

public class UpdateMailingAddressPage extends BasePage {

    private final UpdateMailingAddressApiHelper helper;

    public UpdateMailingAddressPage(TestContext testContext) {
        super(testContext);
        this.helper = new UpdateMailingAddressApiHelper(testContext);
    }

    public void validateResponseForNegativeTestConditions(UpdateMailingAddressLabel apiLabel, UpdateMailingAddressLabel testCondition) {
        UpdateMailingAddressRequest payload = helper.preparePayload(apiLabel);
        helper.preparePayloadForTestCondition(payload, testCondition);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, UPDATE_MAILING_ADDRESS, 200);
        UpdateMailingAddressResponse updateMailingAddressResponse = deserializeResponseToPojo(response, UpdateMailingAddressResponse.class);
        testContext.setUpdateMailingAddressResponse(updateMailingAddressResponse);
        testContext.setResponse(response);
    }
}
