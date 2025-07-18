package com.gng.api.pages.turnOn.ServiceOrdersPages.GetDefaultPlansAndOffersPage;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.ServiceOrdersPojo.GetDefaultPlansAndOffers.GetDefaultPlansAndOffersRequest;
import com.gng.api.pojo.ServiceOrdersPojo.GetDefaultPlansAndOffers.GetDefaultPlansAndOffersResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.turnOn.GetDefaultPlansAndOffers.GetDefaultPlansAndOffersApiLabel;
import com.gng.api.util.FakerDataGenerator;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;
import static com.gng.api.constants.ApiEndPoint.GET_DEFAULT_PLANS_AND_OFFERS;

public class GetDefaultPlansAndOffersApiPage extends BasePage {

    private final GetDefaultPlansAndOffersHelper helper;

    public GetDefaultPlansAndOffersApiPage(TestContext testContext) {
        super(testContext);
        this.helper = new GetDefaultPlansAndOffersHelper(testContext);
    }

    public void setRequestParams(GetDefaultPlansAndOffersApiLabel apiLabel, GetDefaultPlansAndOffersApiLabel accountType, GetDefaultPlansAndOffersApiLabel promotionCode,
                                 GetDefaultPlansAndOffersApiLabel enrollmentSource, GetDefaultPlansAndOffersApiLabel testCondition) {

        GetDefaultPlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        payload.setRequestID(FakerDataGenerator.generateString(10));
        helper.setRequestParams(payload, accountType, promotionCode, enrollmentSource, testCondition);

        setRequestSpecification(payload, testContext.getAuthToken());
        Response offersResponse = sendRequest(HttpPost.METHOD_NAME, GET_DEFAULT_PLANS_AND_OFFERS, 200);
        GetDefaultPlansAndOffersResponse getDefaultPlansAndOffersResponse = deserializeResponseToPojo(offersResponse, GetDefaultPlansAndOffersResponse.class);
        testContext.setGetDefaultPlansAndOffersResponse(getDefaultPlansAndOffersResponse);
        testContext.setResponse(offersResponse);
    }
}
