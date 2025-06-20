package com.gng.api.pages.turnOff.CommonPages.GetReasonsForLeavingPage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CommonPojo.GetReasonsForLeaving.GetReasonsForLeavingRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.turnOff.Common.GetReasonsForLeaving.GetReasonsForLeavingLabel;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.GET_REASONS_FOR_LEAVING;
import static com.gng.api.constants.ApiEndPoint.SAVE_UNENROLLMENT;

public class GetReasonsForLeavingApiPage extends BasePage {

    private final GetReasonsForLeavingHelper helper;

    public GetReasonsForLeavingApiPage(TestContext testContext) {
        super(testContext);
        this.helper = new GetReasonsForLeavingHelper(testContext);
    }

    public void requestToGetReasonsForLeavingWithETCFlag(GetReasonsForLeavingLabel apiLabel, boolean etcExists){

        GetReasonsForLeavingRequest payload = helper.preparePayload(apiLabel);
        helper.setEtcExistsAndRequestID(payload,etcExists);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_REASONS_FOR_LEAVING, 200);
        testContext.setResponse(response);
    }
}
