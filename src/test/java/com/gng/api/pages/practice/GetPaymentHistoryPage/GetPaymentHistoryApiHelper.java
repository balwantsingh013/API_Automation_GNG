package com.gng.api.pages.practice.GetPaymentHistoryPage;

import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.GetPaymentHistory.GetPaymentHistoryRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.practice.GetPaymentHistory.GetPaymentHistoryLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class GetPaymentHistoryApiHelper {

    private final TestContext testContext;

    public GetPaymentHistoryApiHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    GetPaymentHistoryRequest preparePayload(GetPaymentHistoryLabel apiLabel) {
        log.info("Preparing practice payload for {}", apiLabel);
        return BasePage.deserializeJsonToPojo(apiLabel.toString(), GetPaymentHistoryRequest.class);
    }

    public void preparePayloadForTestCondition(GetPaymentHistoryRequest payload,
                                               GetPaymentHistoryLabel testCondition) {
        Map<String, Object> accountInfo;
        switch (testCondition) {
            case TC_126__Negative__Missing_Request_ID:
                payload.setRequestID("");
                break;

            case TC_130__Negative__Invalid_customerCode_Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateDigits(12));
                break;

            case TC_141__Positive__Payment_Date_Format:
                accountInfo = ApplicationContext.get().getDbAction().getAccountWithPaymentHistory();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(10));
                payload.setCustomerCode(accountInfo.get("cust_code").toString());
                payload.setPremisesCode(accountInfo.get("prem_code").toString());
                payload.setNumberOfMonths("24");
                testContext.setCustomerCode(accountInfo.get("cust_code").toString());
                testContext.setPremisesCode(accountInfo.get("prem_code").toString());
                ApplicationContext.get().getDbAction().getPaymentHistory(
                        accountInfo.get("cust_code").toString(),
                        accountInfo.get("prem_code").toString());
                break;

            default:
                log.warn("No payload adjustment for test condition: {}", testCondition);
                break;
        }
    }
}
