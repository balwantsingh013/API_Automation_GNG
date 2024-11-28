package com.gng.api.pages.GetAccountInfoPage;

import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.pojo.accountinfo.GetAccountInfoRequest;
import com.gng.api.util.CommonUtil;
import org.assertj.core.api.SoftAssertions;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.gng.api.pages.GetAccountInfoPage.GetAccountInfoLabels.*;
import static com.gng.api.util.LogUtil.logError;
import static com.gng.api.util.LogUtil.logInfo;

public class GetAccountInfoHelper {
    private final TestContext testContext;

    public GetAccountInfoHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    public GetAccountInfoRequest createAndConfigureRequest(GetAccountInfoLabels apiLabel) {
        GetAccountInfoRequest request = new GetAccountInfoRequest();
        return getApiPayload(apiLabel, request);
    }

    public GetAccountInfoLabels getMissingParamApiLabel(String missingParam) {
        return switch (missingParam) {
            case "RequestID" -> MISSING_REQUEST_ID;
            case "PremisesCode" -> MISSING_PREM_CODE;
            case "CustomerCode" -> MISSING_CUSTOMER_CODE;
            default -> throw new IllegalStateException("Invalid missing param: " + missingParam);
        };
    }

    public GetAccountInfoLabels getInvalidLengthApiLabel(String param) {
        return switch (param) {
            case "PremCode" -> INVALID_PREM_CODE_LENGTH;
            case "CustomerCode" -> INVALID_CUSTOMER_CODE_LENGTH;
            default -> throw new IllegalStateException("Invalid param: " + param);
        };
    }

    public void verifyAccountInformationWithDatabase(Map<String, Object> accountInformationDB, Map<String, String> responseMap) {
        List<String> keysDB = accountInformationDB.keySet().stream().toList();
        SoftAssertions softAssert = new SoftAssertions();

        for (String key : keysDB) {
            try {
                softAssert.assertThat(responseMap.get(key)).isEqualTo(accountInformationDB.get(key));
            } catch (ClassCastException e) {
                logError(e.getMessage());
            }
        }
        softAssert.assertAll();
    }

    public GetAccountInfoRequest getApiPayload(GetAccountInfoLabels apiName, GetAccountInfoRequest request) {
        logInfo("Get Api Payload");
        return switch (apiName) {
            case HAPPY_FLOW -> buildHappyFlowPayload(request);
            case MISSING_REQUEST_ID -> buildMissingRequestIdPayload(request);
            case MISSING_PREM_CODE -> buildMissingPremCodePayload(request);
            case MISSING_CUSTOMER_CODE -> buildMissingCustomerCodePayload(request);
            case INVALID_CUSTOMER_CODE_LENGTH -> buildInvalidCustomerCodeLengthPayload(request);
            case INVALID_PREM_CODE_LENGTH -> buildInvalidPremCodeLengthPayload(request);
            case NONEXISTENT_CUST_PREM_CODE -> buildNonExistentCombinationPayload(request);
        };
    }

    private GetAccountInfoRequest buildHappyFlowPayload(GetAccountInfoRequest request) {
        request.setRequestID(UUID.randomUUID().toString());
        request.setCustomerCode(testContext.getCustomerCode());
        request.setPremisesCode(testContext.getPremisesCode());
        return request;
    }

    private GetAccountInfoRequest buildMissingRequestIdPayload(GetAccountInfoRequest request) {
        buildHappyFlowPayload(request);
        CommonUtil.nullifyFields(request, "requestID");
        return request;
    }

    private GetAccountInfoRequest buildMissingPremCodePayload(GetAccountInfoRequest request) {
        request.setRequestID(UUID.randomUUID().toString());
        request.setCustomerCode(testContext.getCustomerCode());
        CommonUtil.nullifyFields(request, "premisesCode");
        return request;
    }

    private GetAccountInfoRequest buildMissingCustomerCodePayload(GetAccountInfoRequest request) {
        request.setRequestID(UUID.randomUUID().toString());
        CommonUtil.nullifyFields(request, "customerCode");
        request.setPremisesCode(testContext.getPremisesCode());
        return request;
    }

    private GetAccountInfoRequest buildInvalidCustomerCodeLengthPayload(GetAccountInfoRequest request) {
        request.setRequestID(UUID.randomUUID().toString());
        request.setCustomerCode(CommonUtil.getRandomNumericString(10));
        request.setPremisesCode(testContext.getPremisesCode());
        return request;
    }

    private GetAccountInfoRequest buildInvalidPremCodeLengthPayload(GetAccountInfoRequest request) {
        request.setRequestID(UUID.randomUUID().toString());
        request.setCustomerCode(testContext.getCustomerCode());
        request.setPremisesCode(CommonUtil.getRandomNumericString(8));
        return request;
    }

    private GetAccountInfoRequest buildNonExistentCombinationPayload(GetAccountInfoRequest request) {
        request.setRequestID(UUID.randomUUID().toString());
        request.setCustomerCode(CommonUtil.getRandomNumericString(8));
        request.setPremisesCode(testContext.getPremisesCode());
        return request;
    }
}
