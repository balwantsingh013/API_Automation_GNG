package com.gng.api.pages.marketerSwitch.ServiceOrdersPages.GetDefaultPlansAndOffersPage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.ServiceOrdersPojo.GetDefaultPlansAndOffers.GetDefaultPlansAndOffersRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.marketerSwitch.ServiceOrdersSteps.GetDefaultPlansAndOffers.GetDefaultPlansAndOffersApiLabel;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.GET_DEFAULT_PLANS_AND_OFFERS;

public class GetDefaultPlansAndOffersApiPage extends BasePage {

    private final GetDefaultPlansAndOffersHelper helper;

    public GetDefaultPlansAndOffersApiPage(TestContext testContext) {
        super(testContext);
        this.helper = new GetDefaultPlansAndOffersHelper(testContext);
    }

    public void validateNegativeConditions(GetDefaultPlansAndOffersApiLabel payloadType, GetDefaultPlansAndOffersApiLabel testCondition) {
        GetDefaultPlansAndOffersRequest payload = helper.preparePayload(payloadType);
        helper.setNegativeRequestParams(payload, testCondition);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_DEFAULT_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void verifyResponsePlans(){
        helper.verifyResidentialDefaultPlansReceivedAgainstDatabase();
    }
}
