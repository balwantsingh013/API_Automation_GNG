package com.gng.api.pages.csi.UpdateAccountNicknamePage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.UpdateAccountNickname.UpdateAccountNicknameRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.UpdateAccountNickname.UpdateAccountNicknameLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;

import java.util.Map;

@Slf4j
public class UpdateAccountNicknameApiHelper {

    private final TestContext testContext;

    public UpdateAccountNicknameApiHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    UpdateAccountNicknameRequest preparePayload(UpdateAccountNicknameLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        String jsonFileName = apiLabel.equals(UpdateAccountNicknameLabel.update_account_nickname)
                ? UpdateAccountNicknameLabel.update_account_nickname.toString()
                : UpdateAccountNicknameLabel.set_account_nickname_mandatory.toString();
        return BasePage.deserializeJsonToPojo(jsonFileName, UpdateAccountNicknameRequest.class);
    }

    public void preparePayloadForTestCondition(UpdateAccountNicknameRequest payload, UpdateAccountNicknameLabel testCondition) {
        Map<String, Object> accountData = null;

        switch (testCondition) {
            case TC_56__Negative__Missing_Request_ID:
                payload.setRequestID("");
                break;

            case TC_57__Negative__Invalid_Request_ID__Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(33));
                break;

            case TC_58__Negative__Duplicate_Request_ID:
                payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());
                break;

            case TC_59__Negative__Missing_CustomerCode:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode("");
                payload.setPremisesCode(FakerDataGenerator.generateDigits(6));
                payload.setNickname(FakerDataGenerator.generateString(7));
                break;

            case TC_60__Negative__Invalid_CustomerCode_Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateDigits(10));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(6));
                payload.setNickname(FakerDataGenerator.generateString(10));
                break;

            case TC_61__Negative__Invalid_CustomerCode_Format__Not_String:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateString(5));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(6));
                payload.setNickname(FakerDataGenerator.generateString(10));
                break;

            case TC_62__Negative__Invalid_CustomerCode__Not_Found:
                String customerCode = "9" + FakerDataGenerator.generateDigits(8);
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(customerCode);
                accountData = ApplicationContext.get().getDbAction().selectPremisesCode();
                payload.setPremisesCode(accountData.get("ucbprem_code").toString());
                payload.setNickname(FakerDataGenerator.generateString(7));
                accountData = ApplicationContext.get().getDbAction().getCustomerCode(customerCode);
                Assert.assertEquals(accountData.size(), 0, "Expected customerCode to not exist in UCBCUST");
                break;

            case TC_63__Negative__Missing_PremisesCode:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateDigits(9));
                payload.setPremisesCode("");
                payload.setNickname(FakerDataGenerator.generateString(10));
                break;

            case TC_64__Negative__Invalid_PremisesCode_Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateDigits(7));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(8));
                payload.setNickname(FakerDataGenerator.generateString(7));
                break;

            case TC_65__Negative__Invalid_PremisesCode_Format__Not_String:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateDigits(9));
                payload.setPremisesCode(FakerDataGenerator.generateAlphanumeric(6));
                payload.setNickname(FakerDataGenerator.generateString(10));
                break;

            case TC_66__Negative__Invalid_PremisesCode__Not_Found:
                String premisesCode = "9" + FakerDataGenerator.generateDigits(6);
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().selectCustCode();
                payload.setCustomerCode(accountData.get("ucbcust_cust_code").toString());
                payload.setPremisesCode(premisesCode);
                payload.setNickname(FakerDataGenerator.generateString(10));
                accountData = ApplicationContext.get().getDbAction().getPremisesCode(premisesCode);
                Assert.assertEquals(accountData.size(), 0, "Expected premisesCode to not exist in UCBPREM");
                break;

            case TC_67__Negative__Nickname_Not_Allowed_For_New_Account:
                accountData = ApplicationContext.get().getDbAction().getNewStatusAccount();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountData.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountData.get("ucracct_prem_code").toString());
                payload.setNickname(FakerDataGenerator.generateString(10));
                break;

            case TC_68__Negative__Nickname_Already_Exists:
                accountData = ApplicationContext.get().getDbAction().getAccountWithNickname();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountData.get("OCSACNM_CUST_CODE").toString());
                payload.setPremisesCode(accountData.get("OCSACNM_PREM_CODE").toString());
                payload.setNickname(FakerDataGenerator.generateString(7));
                break;

            case TC_69__Negative__Nickname_Missing:
                accountData = ApplicationContext.get().getDbAction().getAccountWithoutNickname();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountData.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountData.get("UCRACCT_PREM_CODE").toString());
                payload.setNickname("");
                break;

            case TC_70__Positive__Nickname_Updated:
                accountData = ApplicationContext.get().getDbAction().getAccountWithoutNickname();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountData.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountData.get("UCRACCT_PREM_CODE").toString());
                payload.setNickname(FakerDataGenerator.generateString(7));
                break;

            case TC_71__Positive__LoginID_Saved:
                accountData = ApplicationContext.get().getDbAction().getAccountWithoutNickname();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountData.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountData.get("UCRACCT_PREM_CODE").toString());
                payload.setNickname(FakerDataGenerator.generateString(7));
                payload.setLoginID("CSRLogin123");
                break;

            default:
                log.warn("Unhandled test condition: {}", testCondition);
                break;
        }
    }
}
