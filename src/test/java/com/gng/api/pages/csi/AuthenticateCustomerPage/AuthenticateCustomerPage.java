package com.gng.api.pages.csi.AuthenticateCustomerPage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.AuthenticateCustomer.AuthenticateCustomerRequest;
import com.gng.api.pojo.CSIPojo.AuthenticateCustomer.AuthenticateCustomerResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.AuthenticateCustomer.AuthenticateCustomerLabel;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.AUTHENTICATE_CUSTOMER;

public class AuthenticateCustomerPage extends BasePage {

    private final com.gng.api.pages.csi.AuthenticateCustomerPage.AuthenticateCustomerApiHelper helper;

    public AuthenticateCustomerPage(TestContext testContext) {
        super(testContext);
        this.helper = new AuthenticateCustomerApiHelper(testContext);
    }

    public void validateResponseForTestConditions(AuthenticateCustomerLabel apiLabel, AuthenticateCustomerLabel testCondition) {
        AuthenticateCustomerRequest payload = helper.preparePayload(apiLabel);
        helper.preparePayloadForTestCondition(payload, testCondition);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, AUTHENTICATE_CUSTOMER, 200);
        AuthenticateCustomerResponse authenticateCustomerResponse = deserializeResponseToPojo(response, AuthenticateCustomerResponse.class);
        testContext.setAuthenticateCustomerResponse(authenticateCustomerResponse);
        testContext.setResponse(response);
    }
}
