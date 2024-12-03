package com.gng.api.pages.ServiceOrdersPages.SaveEnrollmentPage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.ServiceOrdersPojo.SaveEnrollment.SaveEnrollmentResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.pojo.ServiceOrdersPojo.SaveEnrollment.SaveEnrollmentRequest;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.SAVE_ENROLLMENT;
import static com.gng.api.pages.ServiceOrdersPages.SaveEnrollmentPage.SaveEnrollmentLabels.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


public class SaveEnrollmentApiPage extends BasePage {
    private final SaveEnrollmentHelper helper;
    private Response response;


        public SaveEnrollmentApiPage(TestContext testContext) {
            super(testContext);
            this.helper = new SaveEnrollmentHelper(testContext);
        }

        public void sendSaveEnrollmentRequest() {
            SaveEnrollmentRequest request = helper.createAndConfigureRequest(HAPPY_FLOW_);
            setRequestSpecification(HAPPY_FLOW_.getLabel(), request);
            Response response = sendRequest(HttpPost.METHOD_NAME, SAVE_ENROLLMENT, 200);
            SaveEnrollmentResponse saveEnrollmentResponse = deserializeResponseToPojo(
                    HAPPY_FLOW_.getLabel(), response, SaveEnrollmentResponse.class);

            processResponse(saveEnrollmentResponse);
        }

        private void processResponse(SaveEnrollmentResponse response) {
            System.out.println("Response received: " + response);
        }

        public void sendSaveEnrollmentRequestWithMissingParam(String missingParam) {
            SaveEnrollmentLabels apiLabel = helper.getMissingParamApiLabel(missingParam);
            SaveEnrollmentRequest request = helper.createAndConfigureRequest(apiLabel);
            setRequestSpecification(apiLabel.getLabel(), request);
            Response response = sendRequest(HttpPost.METHOD_NAME, SAVE_ENROLLMENT, 200);
            testContext.setResponse(response);
        }

        private void setupAndExecuteRequest(SaveEnrollmentLabels apiLabel) {
            SaveEnrollmentRequest request = new SaveEnrollmentRequest();
            request = helper.getApiPayload(apiLabel, request);
            setRequestSpecification(apiLabel.getLabel(), request);
            sendRequest(HttpPost.METHOD_NAME, SAVE_ENROLLMENT, 200);
        }

        public void sendSaveEnrollmentRequestWithInvalidCustomerCode( ) {
            setupAndExecuteRequest( INVALID_CUSTOMER_CODE);
        }
        public void sendSaveEnrollmentRequestWithInvalidPromotionCode( ) {
            setupAndExecuteRequest( INVALID_PROMO_CODE_LENGTH);
        }
        public void sendSaveEnrollmentRequestWithHappyFlow() {
            setupAndExecuteRequest(HAPPY_FLOW_);
        }

    public void verifyResponseCodeForInvalidPromotionCode(String param, int statusCode) {
        assertThat("Invalid ServiceOrderAPI Response Code for invalid " + param,
                response.getStatusCode(), equalTo(statusCode));
    }
    public void verifyResponseCodeForInvalidCustomerCode(String param, int statusCode) {
        assertThat("Invalid ServiceOrderAPI Response Code for invalid " + param,
                response.getStatusCode(), equalTo(statusCode));
    }
    }







