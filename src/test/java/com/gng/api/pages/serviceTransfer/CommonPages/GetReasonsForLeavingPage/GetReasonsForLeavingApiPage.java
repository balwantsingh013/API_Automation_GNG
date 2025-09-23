package com.gng.api.pages.serviceTransfer.CommonPages.GetReasonsForLeavingPage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CommonPojo.GetReasonsForLeaving.GetReasonsForLeavingRequest;
import com.gng.api.pojo.CommonPojo.GetReasonsForLeaving.Response.GetReasonsForLeavingResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.serviceTransfer.Common.GetReasonsForLeaving.GetReasonsForLeavingApiLabel;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.GET_REASONS_FOR_LEAVING;

public class GetReasonsForLeavingApiPage extends BasePage {


    private final GetReasonsForLeavingHelper helper;


    public GetReasonsForLeavingApiPage(TestContext testContext) {
        super(testContext);
        this.helper = new GetReasonsForLeavingHelper(testContext);
    }

    public void validateInvalidRequestParameters(GetReasonsForLeavingApiLabel apiLabel, GetReasonsForLeavingApiLabel requestID) {
        GetReasonsForLeavingRequest payload = helper.preparePayload(apiLabel);
        helper.setParametersBasedOnType(payload, requestID);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_REASONS_FOR_LEAVING, 200);
        GetReasonsForLeavingResponse getReasonsForLeavingResponse = deserializeResponseToPojo(response, GetReasonsForLeavingResponse.class);
        testContext.setGetReasonsForLeavingResponse(getReasonsForLeavingResponse);
        testContext.setResponse(response);
    }

    public void validRequestByTestCondition(GetReasonsForLeavingApiLabel apiLabel, GetReasonsForLeavingApiLabel requestID) {
        GetReasonsForLeavingRequest payload = helper.preparePayload(apiLabel);
        helper.setParametersBasedOnType(payload, requestID);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_REASONS_FOR_LEAVING, 200);
        GetReasonsForLeavingResponse getReasonsForLeavingResponse = deserializeResponseToPojo(response, GetReasonsForLeavingResponse.class);
        testContext.setGetReasonsForLeavingResponse(getReasonsForLeavingResponse);
        testContext.setResponse(response);
    }

    public void verifyGetReasonsForLeavingResponse(String turnOffReason) {
       helper.validateSingleTurnOffReason(turnOffReason);
    }
}
