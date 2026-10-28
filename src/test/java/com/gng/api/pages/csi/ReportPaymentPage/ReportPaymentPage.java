package com.gng.api.pages.csi.ReportPaymentPage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.ReportPayment.ReportPaymentRequest;
import com.gng.api.pojo.CSIPojo.ReportPayment.ReportPaymentResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.ReportPayment.ReportPaymentLabel;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.REPORT_PAYMENT;

public class ReportPaymentPage extends BasePage {

    private final ReportPaymentApiHelper helper;

    public ReportPaymentPage(TestContext testContext) {
        super(testContext);
        this.helper = new ReportPaymentApiHelper(testContext);
    }

    public void validateResponseForTestConditions(ReportPaymentLabel apiLabel,
                                                  ReportPaymentLabel testCondition) {
        ReportPaymentRequest payload = helper.preparePayload(apiLabel);
        helper.preparePayloadForTestCondition(payload, testCondition);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, REPORT_PAYMENT, 200);
        ReportPaymentResponse reportPaymentResponse =
                deserializeResponseToPojo(response, ReportPaymentResponse.class);
        testContext.setReportPaymentResponse(reportPaymentResponse);
        testContext.setResponse(response);
    }
}
