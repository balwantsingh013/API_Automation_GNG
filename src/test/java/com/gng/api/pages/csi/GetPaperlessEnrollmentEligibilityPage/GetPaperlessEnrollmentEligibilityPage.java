package com.gng.api.pages.csi.GetPaperlessEnrollmentEligibilityPage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.GetPaperlessEnrollmentEligibility.GetPaperlessEnrollmentEligibilityRequest;
import com.gng.api.pojo.CSIPojo.GetPaperlessEnrollmentEligibility.GetPaperlessEnrollmentEligibilityResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.GetPaperlessEnrollmentEligibility.GetPaperlessEnrollmentEligibilityLabel;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.GET_PAPERLESS_ENROLLMENT_ELIGIBILITY;

public class GetPaperlessEnrollmentEligibilityPage extends BasePage {

    private final GetPaperlessEnrollmentEligibilityApiHelper helper;

    public GetPaperlessEnrollmentEligibilityPage(TestContext testContext) {
        super(testContext);
        this.helper = new GetPaperlessEnrollmentEligibilityApiHelper(testContext);
    }

    public void validateResponseForTestCondition(GetPaperlessEnrollmentEligibilityLabel apiLabel,
                                                 GetPaperlessEnrollmentEligibilityLabel testCondition) {
        GetPaperlessEnrollmentEligibilityRequest payload = helper.preparePayload(apiLabel);
        helper.preparePayloadForTestCondition(payload, testCondition);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_PAPERLESS_ENROLLMENT_ELIGIBILITY, 200);
        GetPaperlessEnrollmentEligibilityResponse getPaperlessEnrollmentEligibilityResponse =
                deserializeResponseToPojo(response, GetPaperlessEnrollmentEligibilityResponse.class);
        testContext.setGetPaperlessEnrollmentEligibilityResponse(getPaperlessEnrollmentEligibilityResponse);
        testContext.setResponse(response);
    }
}
