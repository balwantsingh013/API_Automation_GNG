package com.gng.api.pages.turnOff.CommonPages.GetMarketerRefrenceDataPage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CommonPojo.GetMerketerReferenceData.GetMarketerReferenceDataRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.turnOff.Common.GetMarketerReferenceData.GetMarketerReferenceDataLabel;
import com.gng.api.util.FakerDataGenerator;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.GET_MARKETER_REFERENCE_DATA;

public class GetMarketerReferenceDataApiPage extends BasePage {

    private final GetMarketerReferenceDataHelper helper;

    public GetMarketerReferenceDataApiPage(TestContext testContext) {
        super(testContext);
        this.helper = new GetMarketerReferenceDataHelper(testContext);
    }

    public void requestToGenerateMarketerReferenceData(GetMarketerReferenceDataLabel apiLabel){
        GetMarketerReferenceDataRequest payload=helper.preparePayload(apiLabel);
        payload.setRequestID(FakerDataGenerator.generateString(10));
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_MARKETER_REFERENCE_DATA, 200);
        testContext.setResponse(response);
        storeMarketerReferenceData(response);
    }

    private void storeMarketerReferenceData(Response response) {
        testContext.setResponse(response);
        testContext.setMarketerReferenceData(response.jsonPath().getLong("data.marketerReferenceData"));
    }
}
