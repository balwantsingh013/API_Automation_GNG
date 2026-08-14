package com.gng.api.pages.practice.GetAccountInfoPage;

import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.GetAccountInfo.GetAccountInfoRequest;
import com.gng.api.steps.practice.GetAccountInfo.GetAccountInfoLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class GetAccountInfoApiHelper {

    GetAccountInfoRequest preparePayload(GetAccountInfoLabel apiLabel) {
        log.info("Preparing practice payload for {}", apiLabel);
        return BasePage.deserializeJsonToPojo(apiLabel.toString(), GetAccountInfoRequest.class);
    }

    public void preparePayloadForTestCondition(GetAccountInfoRequest payload, GetAccountInfoLabel testCondition) {
        Map<String, Object> accountInfo;
        switch (testCondition) {
            case TC_142__Negative__Missing_Request_ID:
                payload.setRequestID("");
                break;

            case TC_145__Negative__Invalid_customerCode_Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateDigits(10));
                break;

            case TC_150__Positive__Account_Info_Returned___Email_Address____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetails();
                payload.setCustomerCode(accountInfo.get("GZBEMCP_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_151__Positive__Account_Info_Returned___No_Email_Address____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC150();
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_152__Positive__Account_Info_Returned___Partner_Promotions_Indicator__equals_Y____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC151();
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            default:
                log.warn("No payload adjustment for test condition: {}", testCondition);
                break;
        }
    }
}
