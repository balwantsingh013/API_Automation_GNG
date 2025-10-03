package com.gng.api.pages.serviceTransfer.CommonPages.GetMarketerReferenceDataPage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CommonPojo.GetMerketerReferenceData.GetMarketerReferenceDataRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.serviceTransfer.Common.GetMarketerReferenceData.GetMarketerReferenceDataApiLabel;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.GET_MARKETER_REFERENCE_DATA;

public class GetMarketerReferenceDataApiPage extends BasePage {

    private final GetMarketerReferenceDataApiHelper helper;

    public GetMarketerReferenceDataApiPage(TestContext testContext) {
        super(testContext);
        this.helper = new GetMarketerReferenceDataApiHelper(testContext);
    }

    public void requestToGenerateMarketerReferenceDataWithInvalidTestCondition(GetMarketerReferenceDataApiLabel apiLabel, GetMarketerReferenceDataApiLabel testCondition){
        GetMarketerReferenceDataRequest payload=helper.preparePayload(apiLabel);
        helper.setRequestPayloadAsPerTestCondition(payload, testCondition);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_MARKETER_REFERENCE_DATA, 200);
        testContext.setResponse(response);
    }

    public void requestToGenerateMarketerReferenceDataWithValidTestCondition(GetMarketerReferenceDataApiLabel apiLabel){
        GetMarketerReferenceDataRequest payload=helper.preparePayload(apiLabel);
        helper.setRequestPayloadForValidTCs(payload);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_MARKETER_REFERENCE_DATA, 200);
        testContext.setResponse(response);
        storeMarketerReferenceData(response);
    }

    private void storeMarketerReferenceData(Response response) {
        testContext.setResponse(response);
        if (testContext.getMarketerReferenceData() == null) {
            testContext.setMarketerReferenceData(response.jsonPath().getLong("data.marketerReferenceData"));
        }
    }
}
