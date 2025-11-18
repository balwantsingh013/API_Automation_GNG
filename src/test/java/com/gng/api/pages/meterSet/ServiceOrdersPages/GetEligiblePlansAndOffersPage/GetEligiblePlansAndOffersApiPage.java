package com.gng.api.pages.meterSet.ServiceOrdersPages.GetEligiblePlansAndOffersPage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.request.GetEligiblePlansAndOffersRequest;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.response.GetEligiblePlansAndOffersResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.meterSet.ServiceOrdersSteps.GetEligiblePlansAndOffers.GetEligiblePlansAndOffersApiLabel;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import java.util.ArrayList;

import static com.gng.api.constants.ApiEndPoint.GET_ELIGIBLE_PLANS_AND_OFFERS;

public class GetEligiblePlansAndOffersApiPage extends BasePage {
    private final GetEligiblePlansAndOffersHelper helper;

    public GetEligiblePlansAndOffersApiPage(TestContext testContext) {
        super(testContext);
        this.helper = new GetEligiblePlansAndOffersHelper(testContext);
    }

    public void seedNegativeTestConditions(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel testCondition) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setParametersToSeedDataBasedOnType(payload, testCondition);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        GetEligiblePlansAndOffersResponse getEligiblePlansAndOffersResponse = deserializeResponseToPojo(response, GetEligiblePlansAndOffersResponse.class);
        testContext.setGetEligiblePlansAndOffersResponse(getEligiblePlansAndOffersResponse);
        testContext.setResponse(response);
    }

    public void seedCustomerFileSourceTestConditions(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel testCondition) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setSeedDataCustomerInformation(payload, testCondition);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        GetEligiblePlansAndOffersResponse getEligiblePlansAndOffersResponse = deserializeResponseToPojo(response, GetEligiblePlansAndOffersResponse.class);
        testContext.setGetEligiblePlansAndOffersResponse(getEligiblePlansAndOffersResponse);
        testContext.setResponse(response);
    }

    public void validateNegativeTestConditions(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel testCondition) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setParametersBasedOnType(payload, testCondition);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        GetEligiblePlansAndOffersResponse getEligiblePlansAndOffersResponse = deserializeResponseToPojo(response, GetEligiblePlansAndOffersResponse.class);
        testContext.setGetEligiblePlansAndOffersResponse(getEligiblePlansAndOffersResponse);
        testContext.setResponse(response);
    }

    public void validatePositiveTestConditions(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel testCondition) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setParametersBasedOnType(payload, testCondition);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        GetEligiblePlansAndOffersResponse getEligiblePlansAndOffersResponse = deserializeResponseToPojo(response, GetEligiblePlansAndOffersResponse.class);
        testContext.setGetEligiblePlansAndOffersResponse(getEligiblePlansAndOffersResponse);
        testContext.setResponse(response);
    }

    public void verifyResponsePlans(){
        helper.verifyResidentialPlansReceivedAgainstDatabase();
    }

    public void verifyResponseDoesNotContainPlans(GetEligiblePlansAndOffersApiLabel testCondition) {
        helper.verifyDisallowedPlansFor(testCondition);
    }
}
