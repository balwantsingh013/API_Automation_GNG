package com.gng.api.pages.csi.SetAccountNicknamePage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.SetAccountNickname.SetAccountNicknameRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.SetAccountNickname.SetAccountNicknameLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;

import java.util.Map;

@Slf4j
public class SetAccountNicknameApiHelper {

    private final TestContext testContext;

    public SetAccountNicknameApiHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    SetAccountNicknameRequest preparePayload(SetAccountNicknameLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        String jsonFileName = apiLabel.equals(SetAccountNicknameLabel.set_account_nickname)
                ? SetAccountNicknameLabel.set_account_nickname.toString()
                : SetAccountNicknameLabel.set_account_nickname_mandatory.toString();
        return BasePage.deserializeJsonToPojo(jsonFileName, SetAccountNicknameRequest.class);
    }

    public void preparePayloadForNegativeTestCondition(SetAccountNicknameRequest payload, SetAccountNicknameLabel testCondition) {
        Map<String, Object> accountData = null;

        switch (testCondition) {
            case Missing_request_id_TC_1:
                payload.setRequestID("");
                break;

            case Length_of_request_id_larger_than_32_TC_2:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(33));
                break;

            case Duplicate_request_id_TC_3:
                payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());
                break;

            case Missing_customerCode_TC_4:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode("");
                payload.setPremisesCode(FakerDataGenerator.generateDigits(7));
                payload.setNickname(FakerDataGenerator.generateString(10));
                break;

            case CustomerCode_length_greater_than_9_TC_5:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateDigits(10));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(7));
                payload.setNickname(FakerDataGenerator.generateString(10));
                break;

            case CustomerCode_not_a_string_TC_6:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode("123456789"); // Simulated numeric string
                payload.setPremisesCode(FakerDataGenerator.generateDigits(7));
                payload.setNickname(FakerDataGenerator.generateString(10));
                break;

            case CustomerCode_not_found_in_UCBCUST_TC_7:
                String customerCode = FakerDataGenerator.generateDigits(9);
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(customerCode);
                payload.setPremisesCode(FakerDataGenerator.generateDigits(7));
                payload.setNickname(FakerDataGenerator.generateString(10));
                //accountData = ApplicationContext.get().getDbAction("banner").getCustomerCode(customerCode);
                //Assert.assertTrue(accountData.isEmpty(), "Expected customerCode to not exist in UCBCUST");
                break;

            case Missing_premisesCode_TC_8:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateDigits(9));
                payload.setPremisesCode("");
                payload.setNickname(FakerDataGenerator.generateString(10));
                break;

            case PremisesCode_length_greater_than_7_TC_9:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateDigits(9));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(8));
                payload.setNickname(FakerDataGenerator.generateString(10));
                break;

            case PremisesCode_not_a_string_TC_10:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateDigits(9));
                payload.setPremisesCode("1234567"); // Simulated numeric string
                payload.setNickname(FakerDataGenerator.generateString(10));
                break;

            case PremisesCode_not_found_in_UCBPREM_TC_11:
                String premisesCode = FakerDataGenerator.generateDigits(7);
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateDigits(9));
                payload.setPremisesCode(premisesCode);
                payload.setNickname(FakerDataGenerator.generateString(10));
                //accountData = ApplicationContext.get().getDbAction("banner").getPremisesCode(premisesCode);
                Assert.assertTrue(accountData.isEmpty(), "Expected premisesCode to not exist in UCBPREM");
                break;

            case Nickname_not_allowed_for_new_account_TC_12:
                //accountData = ApplicationContext.get().getDbAction("banner").getNewStatusAccount();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountData.get("customer_code").toString());
                payload.setPremisesCode(accountData.get("premises_code").toString());
                payload.setNickname(FakerDataGenerator.generateString(10));
                break;

            case Nickname_already_exists_for_account_TC_13:
                //accountData = ApplicationContext.get().getDbAction("banner").getAccountWithNickname();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountData.get("customer_code").toString());
                payload.setPremisesCode(accountData.get("premises_code").toString());
                payload.setNickname(accountData.get("nickname").toString());
                break;

            case Nickname_missing_for_existing_account_TC_14:
                //accountData = ApplicationContext.get().getDbAction("banner").getAccountWithoutNickname();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountData.get("customer_code").toString());
                payload.setPremisesCode(accountData.get("premises_code").toString());
                payload.setNickname("");
                break;

            case Valid_account_with_new_nickname_TC_15:
                //accountData = ApplicationContext.get().getDbAction("banner").getAccountWithoutNickname();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(accountData.get("customer_code").toString());
                payload.setPremisesCode(accountData.get("premises_code").toString());
                payload.setNickname(FakerDataGenerator.generateString(10));
                break;

            default:
                // Handle unknown test condition
                break;
        }
    }
}
