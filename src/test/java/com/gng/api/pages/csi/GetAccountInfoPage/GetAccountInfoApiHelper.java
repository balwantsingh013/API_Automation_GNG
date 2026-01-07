package com.gng.api.pages.csi.GetAccountInfoPage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.GetAccountInfo.GetAccountInfoRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.GetAccountInfo.GetAccountInfoLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;

import java.util.Map;

@Slf4j
public class GetAccountInfoApiHelper {

    private final TestContext testContext;

    public GetAccountInfoApiHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    GetAccountInfoRequest preparePayload(GetAccountInfoLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        String jsonFileName = apiLabel.equals(GetAccountInfoLabel.csi_get_account_info)
                ? GetAccountInfoLabel.csi_get_account_info.toString()
                : GetAccountInfoLabel.csi_get_account_info_mandatory.toString();
        return BasePage.deserializeJsonToPojo(jsonFileName, GetAccountInfoRequest.class);
    }

    public void preparePayloadForTestCondition(GetAccountInfoRequest payload, GetAccountInfoLabel testCondition) {
        Map<String, Object> accountInfo = null;
        switch (testCondition) {
            case TC_185__Negative__Missing_Request_ID:
                payload.setRequestID("");
                break;

            case TC_186__Negative__Invalid_Request_ID__Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(33));
                break;

            case TC_187__Negative__Duplicate_Request_ID:
                payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());
                break;

            case TC_188__Negative__Invalid_customerCode_Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateDigits(10)); // >9 digits
                break;

            case TC_189__Negative__Invalid_customerCode_Format:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateAlphanumeric(6)); // not purely numeric
                break;

            case TC_190__Negative__Invalid_premisesCode_Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(8)); // >7 digits
                break;

            case TC_191__Negative__Invalid_premisesCode_Format:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPremisesCode(FakerDataGenerator.generateAlphanumeric(6)); // not purely numeric
                break;

            case TC_192__Negative__Invalid_customerCode:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(GlobalEnums.InvalidValues.INVALID_CUSTOMER_CODE.getValue());
                break;

            case TC_193__Negative__Invalid_premisesCode:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPremisesCode(GlobalEnums.InvalidValues.INVALID_PREMISES_CODE.getValue());
                break;

            case TC_194__Positive__Account_Info_Returned:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                payload.setCustomerCode(ApplicationContext.get().getDbAction("mariadb").getValidCustomerCode());
//                payload.setPremisesCode(ApplicationContext.get().getDbAction("mariadb").getValidPremisesCode());
                break;

            case TC_195__Positive__Login_ID_Saved:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                Map<String, Object> accountData = ApplicationContext.get().getDbAction().getActiveAccountWithoutNickname();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountData.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountData.get("UCRACCT_PREM_CODE").toString());
                payload.setLoginID(FakerDataGenerator.generateAlphanumeric(8));
                break;

            default:
                // Handle unknown test condition
                break;
        }
    }
}
