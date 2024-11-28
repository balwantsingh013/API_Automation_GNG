package com.gng.api.pages.GetAccountInfoPage;

import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.pojo.accountinfo.GetAccountInfoRequest;
import com.gng.api.util.CommonUtil;
import org.assertj.core.api.SoftAssertions;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.gng.api.constants.ApiLabel.*;
import static com.gng.api.constants.TestConstant.UNEXPECTED_VALUE;
import static com.gng.api.util.LogUtil.logError;
import static com.gng.api.util.LogUtil.logInfo;

public class GetAccountInfoHelper {
    private final TestContext testContext;

    public GetAccountInfoHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    public GetAccountInfoRequest createAndConfigureRequest(String apiLabel) {
        GetAccountInfoRequest request = new GetAccountInfoRequest();
        return getApiPayload(apiLabel, request);
    }

    public String getMissingParamApiLabel(String missingParam) {
        return switch (missingParam) {
            case "RequestID" -> GETACCOUNTINFO_MISSINGREQUESTID_API;
            case "PremisesCode" -> GETACCOUNTINFO_MISSINGPREMCODE_API;
            case "CustomerCode" -> GETACCOUNTINFO_MISSINGCUSTOMERCODE_API;
            default -> throw new IllegalStateException("Invalid missing param: " + missingParam);
        };
    }

    public String getInvalidLengthApiLabel(String param) {
        return switch (param) {
            case "PremCode" -> GETACCOUNTINFO_INVALIDPREMCODELENGTH_API;
            case "CustomerCode" -> GETACCOUNTINFO_INVALIDCUSTOMERCODELENGTH_API;
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

    public GetAccountInfoRequest getApiPayload(String apiName, GetAccountInfoRequest request) {
        logInfo("Get Api Payload");
        return switch (apiName) {
            case GETACCOUNTINFO_HAPPYFLOW_API -> buildHappyFlowPayload(request);
            case GETACCOUNTINFO_MISSINGREQUESTID_API -> buildMissingRequestIdPayload(request);
            case GETACCOUNTINFO_MISSINGPREMCODE_API -> buildMissingPremCodePayload(request);
            case GETACCOUNTINFO_MISSINGCUSTOMERCODE_API -> buildMissingCustomerCodePayload(request);
            case GETACCOUNTINFO_INVALIDCUSTOMERCODELENGTH_API -> buildInvalidCustomerCodeLengthPayload(request);
            case GETACCOUNTINFO_INVALIDPREMCODELENGTH_API -> buildInvalidPremCodeLengthPayload(request);
            case GETACCOUNTINFO_NONEXISTENT_CUSTPREMCODE_API -> buildNonExistentCombinationPayload(request);
            default -> throw new IllegalStateException(UNEXPECTED_VALUE + apiName);
        };
    }

    private GetAccountInfoRequest buildHappyFlowPayload(GetAccountInfoRequest request) {
        request.setRequestID(UUID.randomUUID().toString());
        request.setCustomerCode(testContext.getCustomerCode());
        request.setPremisesCode(testContext.getPremisesCode());
        return request;
    }

    private GetAccountInfoRequest buildMissingRequestIdPayload(GetAccountInfoRequest request) {
        request = buildHappyFlowPayload(request);
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

