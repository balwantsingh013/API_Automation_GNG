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

    public void verifyPrepayPlanRequote(GetPrepayPlansRequoteApiLabel apiLabel, GetPrepayPlansRequoteApiLabel testCondition) {
        configureQuote();
        GetPrepayPlansRequoteRequest payload = helper.preparePayload(apiLabel);
        payload.setRequestID(FakerDataGenerator.generateString(10));

        helper.setRequestParams(payload);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_PREPAY_PLANS_REQUOTE, 200);
        GetPrepayPlansRequoteResponse getPrepayPlansRequoteResponse = deserializeResponseToPojo(response, GetPrepayPlansRequoteResponse.class);

        testContext.setGetPrepayPlansRequoteResponse(getPrepayPlansRequoteResponse);
        testContext.setResponse(response);
    }

    public void configureQuote() {
        helper.expirePrepayQuoteIfOpenDateInFuture(testContext.getSearchAccountsResponse(), testContext.getGetEligiblePlansAndOffersResponse().getData().getCustomerCode());
    }

    public void verifyResponsePlans(GlobalEnums.PlanCode planCode){
        helper.verifyResidentialPrepayPlansReceivedAgainstDatabase(planCode);
    }

    public void validateInvalidRequestIDCases(GetPrepayPlansRequoteApiLabel payloadType, GetPrepayPlansRequoteApiLabel testCondition) {
        GetPrepayPlansRequoteRequest payload = helper.preparePayload(payloadType);
        helper.setRequestIDBasedOnTestCondition(payload, testCondition);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_PREPAY_PLANS_REQUOTE, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidLoginIDCases(GetPrepayPlansRequoteApiLabel payloadType, GetPrepayPlansRequoteApiLabel testCondition) {
        GetPrepayPlansRequoteRequest payload = helper.preparePayload(payloadType);
        helper.setLoginIDBasedOnTestCondition(payload, testCondition);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_PREPAY_PLANS_REQUOTE, 200);
        testContext.setResponse(response);
    }

    public void validateNegativeTestCases(GetPrepayPlansRequoteApiLabel payloadType, GetPrepayPlansRequoteApiLabel testCondition) {
        GetPrepayPlansRequoteRequest payload = helper.preparePayload(payloadType);
        helper.setNegativeRequestParamsBasedOnTestCondition(payload, testCondition);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_PREPAY_PLANS_REQUOTE, 200);
        testContext.setResponse(response);
    }

    public void validateNegativePrepayPlanRequoteCompleteFlow(GetPrepayPlansRequoteApiLabel payloadType, GetPrepayPlansRequoteApiLabel testCondition) {
        GetPrepayPlansRequoteRequest payload = helper.preparePayload(payloadType);
        payload.setRequestID(FakerDataGenerator.generateString(10));

        helper.setRequestParams(payload);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_PREPAY_PLANS_REQUOTE, 200);
        GetPrepayPlansRequoteResponse getPrepayPlansRequoteResponse = deserializeResponseToPojo(response, GetPrepayPlansRequoteResponse.class);

        testContext.setGetPrepayPlansRequoteResponse(getPrepayPlansRequoteResponse);
        testContext.setResponse(response);
    }
}
