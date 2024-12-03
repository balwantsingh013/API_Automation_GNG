package com.gng.api.pages.ServiceOrdersPages.SaveEnrollmentPage;


import com.gng.api.pojo.ServiceOrdersPojo.SaveEnrollment.SaveEnrollmentRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.util.CommonUtil;

import static com.gng.api.pages.ServiceOrdersPages.SaveEnrollmentPage.SaveEnrollmentLabels.INVALID_PROMO_CODE_LENGTH;
import static com.gng.api.util.LogUtil.logInfo;

public class SaveEnrollmentHelper {
    private final TestContext testContext;


    public SaveEnrollmentHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    public SaveEnrollmentRequest createAndConfigureRequest(SaveEnrollmentLabels apiLabel) {
        SaveEnrollmentRequest request = new SaveEnrollmentRequest();
        return getApiPayload(apiLabel, request);
    }


    public SaveEnrollmentRequest getApiPayload(SaveEnrollmentLabels apiName, SaveEnrollmentRequest request) {
        logInfo("Get Api Payload");
        return switch (apiName) {
            case HAPPY_FLOW_ -> buildHappyFlowPayload(request);
            case INVALID_CUSTOMER_CODE -> buildInvalidCustomerCodePayload(request);
            case MISSING_REQUEST_ID -> buildMissingRequestIdPayload(request);
            case INVALID_PROMO_CODE_LENGTH -> buildInvalidPromoCodeLengthPayload(request);


            default -> throw new IllegalStateException("Unexpected value: " + apiName);
        };

    }

    private SaveEnrollmentRequest buildHappyFlowPayload(SaveEnrollmentRequest request) {
        request.setLoginID("dkinnaird");
        request.setTransactionID(1416);
        request.setTransactionType("MKSW");
        request.setCustomerCode(12345);
        request.setPremisesCode("5914107");
        request.setPlanCode("MVS");
        request.setEnrollmentStatus("SI");
        request.setSplitConnectionFeeIndicator(false);
        return request;
    }

    private SaveEnrollmentRequest buildInvalidCustomerCodePayload(SaveEnrollmentRequest request) {
        buildHappyFlowPayload(request);
        request.setCustomerCode(1234567891);
        return request;
    }

    private SaveEnrollmentRequest buildMissingRequestIdPayload(SaveEnrollmentRequest request) {
        buildHappyFlowPayload(request);
        CommonUtil.nullifyFields(request, "requestID");
        return request;
    }

    private SaveEnrollmentRequest buildInvalidPromoCodeLengthPayload(SaveEnrollmentRequest request) {
        buildHappyFlowPayload(request);
        request.setPromotionCode(CommonUtil.getRandomNumericString(10));
        return request;
    }


    public SaveEnrollmentLabels getMissingParamApiLabel(String missingParam) {
        return switch (missingParam) {
            case "InvalidPromoCodeLength" -> INVALID_PROMO_CODE_LENGTH;
            default -> throw new IllegalStateException("Invalid missing param: " + missingParam);
        };
    }

}






