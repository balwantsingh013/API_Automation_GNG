package com.gng.api.pages.ServiceOrdersPages.GetEligiblePlansAndOffersPage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.request.GetEligiblePlansAndOffersRequest;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.response.GetEligiblePlansAndOffersResponse;
import com.gng.api.pojo.ServiceOrdersPojo.SaveEnrollment.SaveEnrollmentRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.ServiceOrdersSteps.GetEligiblePlansAndOffers.GetEligiblePlansAndOffersApiLabel;
import com.gng.api.steps.ServiceOrdersSteps.SaveEnrollment.SaveEnrollmentApiLabel;
import com.gng.api.util.FakerDataGenerator;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.GET_ELIGIBLE_PLANS_AND_OFFERS;
import static com.gng.api.constants.ApiEndPoint.SAVE_ENROLLMENT;

public class GetEligiblePlansAndOffersApiPage extends BasePage {

    private final GetEligiblePlansAndOffersHelper helper;

    public GetEligiblePlansAndOffersApiPage(TestContext testContext) {
        super(testContext);
        this.helper = new GetEligiblePlansAndOffersHelper(testContext);
    }
    public void sendGetEligiblePlansAndOffersRequest(GetEligiblePlansAndOffersApiLabel apiLabel) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        payload.setRequestID(FakerDataGenerator.generateString(10));
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        GetEligiblePlansAndOffersResponse getEligiblePlansAndOffersResponse = deserializeResponseToPojo(response, GetEligiblePlansAndOffersResponse.class);
        testContext.setGetEligiblePlansAndOffersResponse(getEligiblePlansAndOffersResponse);
    }
    public void validateInvalidRequestIDCases(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel requestID) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setRequestIDBasedOnType(payload, requestID);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }
    public void validateInvalidLoginIDCases(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel loginID) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setLoginIDBasedOnType(payload, loginID);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }
    public void validateInvalidTransactionTypeCases(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel transactionType) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setTransactionTypeBasedOnType(payload, transactionType);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }
    public void validateInvalidCustomerTypeCases(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel customerTYPE) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setCustomerTypeBasedOnType(payload, customerTYPE);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }
    public void validateInvalidEnrollmentSourcesCases(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel enrollmentSources) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setEnrollmentSourcesBasedOnType(payload, enrollmentSources);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }
    public void validateInvalidCustomerLastNameCases(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel customerLastName) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setCustomerLastNameBasedOnType(payload, customerLastName);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }



}
