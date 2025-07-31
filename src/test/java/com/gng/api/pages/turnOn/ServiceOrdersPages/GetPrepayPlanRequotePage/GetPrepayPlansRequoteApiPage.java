package com.gng.api.pages.turnOn.ServiceOrdersPages.GetPrepayPlanRequotePage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.ServiceOrdersPojo.GetPrepayPlansRequote.GetPrepayPlansRequoteRequest;
import com.gng.api.pojo.ServiceOrdersPojo.GetPrepayPlansRequote.GetPrepayPlansRequoteResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.turnOn.ServiceOrdersSteps.GetPrepayPlanRequote.GetPrepayPlansRequoteApiLabel;
import com.gng.api.util.FakerDataGenerator;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.GET_PREPAY_PLANS_REQUOTE;

public class GetPrepayPlansRequoteApiPage extends BasePage {

    private final GetPrepayPlansRequoteHelper helper;

    public GetPrepayPlansRequoteApiPage(TestContext testContext) {
        super(testContext);
        this.helper = new GetPrepayPlansRequoteHelper(testContext);
    }

    public void setRequestParams(GetPrepayPlansRequoteApiLabel apiLabel, GetPrepayPlansRequoteApiLabel testCondition) {


        GetPrepayPlansRequoteRequest payload = helper.preparePayload(apiLabel);
        payload.setRequestID(FakerDataGenerator.generateString(10));
        helper.setRequestParams(payload, testCondition);

        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_PREPAY_PLANS_REQUOTE, 200);
        GetPrepayPlansRequoteResponse getPrepayPlansRequoteResponse = deserializeResponseToPojo(response, GetPrepayPlansRequoteResponse.class);
        //testContext.setGetValidationPrepayPlansRequote(helper.getOriginalPayload());
        testContext.setGetPrepayPlansRequoteResponse(getPrepayPlansRequoteResponse);
        testContext.setResponse(response);
    }
}
