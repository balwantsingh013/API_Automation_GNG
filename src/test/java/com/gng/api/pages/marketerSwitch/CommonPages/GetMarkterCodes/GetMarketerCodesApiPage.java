package com.gng.api.pages.marketerSwitch.CommonPages.GetMarkterCodes;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CommonPojo.GetMarketerCodes.request.GetMarketerCodesRequest;
import com.gng.api.pojo.CommonPojo.GetMarketerCodes.response.GetMarketerCodesResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.marketerSwitch.Common.GetMarketerCodes.GetMarketerCodesApiLabel;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.GET_MARKETER_CODES;

public class GetMarketerCodesApiPage extends BasePage {
    private final GetMarketerCodesHelper helper;

    public GetMarketerCodesApiPage(TestContext testContext) {
        super(testContext);
        this.helper = new GetMarketerCodesHelper(testContext);
    }

    public void validateNegativeTestConditions(GetMarketerCodesApiLabel apiLabel, GetMarketerCodesApiLabel testCondition) {
        GetMarketerCodesRequest payload = helper.preparePayload(apiLabel);
        helper.setParametersBasedOnType(payload, testCondition);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_MARKETER_CODES, 200);
        GetMarketerCodesResponse getMarketerCodesResponse = deserializeResponseToPojo(response, GetMarketerCodesResponse.class);
        testContext.setGetMarketerCodesResponse(getMarketerCodesResponse);
        testContext.setResponse(response);
    }

    public void validatePositiveTestConditions(GetMarketerCodesApiLabel apiLabel, GetMarketerCodesApiLabel testCondition) {
        GetMarketerCodesRequest payload = helper.preparePayload(apiLabel);
        helper.setParametersBasedOnType(payload, testCondition);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_MARKETER_CODES, 200);
        GetMarketerCodesResponse getMarketerCodesResponse = deserializeResponseToPojo(response, GetMarketerCodesResponse.class);
        testContext.setGetMarketerCodesResponse(getMarketerCodesResponse);
        testContext.setResponse(response);
    }

}
