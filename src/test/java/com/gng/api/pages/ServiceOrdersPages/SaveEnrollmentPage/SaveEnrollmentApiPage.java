package com.gng.api.pages.ServiceOrdersPages.SaveEnrollmentPage;

import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.AccountsApiPages.GetAccountInfoPage.GetAccountInfoLabels;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.AccountsPojo.getAccountInfo.GetAccountInfoRequest;
import com.gng.api.pojo.ServiceOrdersPojo.SaveEnrollment.SaveEnrollmentResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.pojo.ServiceOrdersPojo.SaveEnrollment.SaveEnrollmentRequest;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import java.util.List;
import java.util.Map;

import static com.gng.api.constants.ApiEndPoint.GET_ACCOUNT_INFO;
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
            testContext.setSaveEnrollmentResponse(saveEnrollmentResponse);
        }
    public void sendSaveEnrollmentRequestForInvalidPromotionCode() {
        SaveEnrollmentRequest request = helper.createAndConfigureRequest(INVALID_PROMO_CODE_LENGTH);
        setRequestSpecification(INVALID_PROMO_CODE_LENGTH.getLabel(), request);
        Response response = sendRequest(HttpPost.METHOD_NAME, SAVE_ENROLLMENT, 200);
        SaveEnrollmentResponse saveEnrollmentResponse = deserializeResponseToPojo(
                INVALID_PROMO_CODE_LENGTH.getLabel(), response, SaveEnrollmentResponse.class);
        testContext.setSaveEnrollmentResponse(saveEnrollmentResponse);
    }
    public void sendSaveEnrollmentRequestForInvalidCustomerCode(String param) {
        SaveEnrollmentRequest request = helper.createAndConfigureRequest(INVALID_CUSTOMER_CODE_LENGTH);
        setRequestSpecification(INVALID_CUSTOMER_CODE_LENGTH.getLabel(), request);
        Response response = sendRequest(HttpPost.METHOD_NAME, SAVE_ENROLLMENT, 200);
        SaveEnrollmentResponse saveEnrollmentResponse = deserializeResponseToPojo(
                INVALID_CUSTOMER_CODE_LENGTH.getLabel(), response, SaveEnrollmentResponse.class);
        testContext.setResponse(response);
    }
    public void sendSaveEnrollmentRequestWithInvalidParam(String invalidParam) {
//        List<Map<String, Object>> activeCustomerData = ApplicationContext.get().getDbAction().getActiveCustomerDetails();
//        setCustomerAndPremisesCodes(activeCustomerData);

        SaveEnrollmentLabels apiLabel = helper.getInvalidParamApiLabel(invalidParam);
        SaveEnrollmentRequest request = helper.createAndConfigureRequest(apiLabel);
        setRequestSpecification(apiLabel.getLabel(), request);
        Response response = sendRequest(HttpPost.METHOD_NAME, SAVE_ENROLLMENT, 200);
        testContext.setResponse(response);
    }


    }







