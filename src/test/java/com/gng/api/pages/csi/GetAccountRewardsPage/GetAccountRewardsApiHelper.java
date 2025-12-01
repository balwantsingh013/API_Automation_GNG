package com.gng.api.pages.csi.GetAccountRewardsPage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.GetAccountRewards.GetAccountRewardsRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.GetAccountRewards.GetAccountRewardsLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;

import java.util.Map;

@Slf4j
public class GetAccountRewardsApiHelper {

    private final TestContext testContext;

    public GetAccountRewardsApiHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    GetAccountRewardsRequest preparePayload(GetAccountRewardsLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        String jsonFileName = apiLabel.equals(GetAccountRewardsLabel.get_account_rewards)
                ? GetAccountRewardsLabel.get_account_rewards.toString()
                : GetAccountRewardsLabel.get_account_rewards_mandatory.toString();
        return BasePage.deserializeJsonToPojo(jsonFileName, GetAccountRewardsRequest.class);
    }

    public void preparePayloadForNegativeTestCondition(GetAccountRewardsRequest payload, GetAccountRewardsLabel testCondition) {
        Map<String, Object> accountData = null;
        switch (testCondition) {
            case TC_107__Negative__Missing_Request_ID:
                payload.setRequestID("");
                break;

            case TC_108__Negative__Invalid_Request_ID_Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(33));
                break;

            case TC_109__Negative__Duplicate_Request_ID:
                payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());
                break;

            case TC_110__Negative__Missing_customerCode:
                payload.setCustomerCode("");
                break;

            case TC_111__Negative__Invalid_customerCode_Length:
                payload.setCustomerCode(FakerDataGenerator.generateDigits(10));
                break;

            case TC_112__Negative__Invalid_customerCode_Format:
                payload.setCustomerCode(FakerDataGenerator.generateAlphanumericWithSpecialChars(6));
                break;

            case TC_113__Negative__Invalid_customerCode:
                payload.setCustomerCode("999999999"); // Non-existent
                break;

            case TC_114__Negative__Missing_premisesCode:
                payload.setPremisesCode("");
                break;

            case TC_115__Negative__Invalid_premisesCode_Length:
                payload.setPremisesCode(FakerDataGenerator.generateDigits(8));
                break;

            case TC_116__Negative__Invalid_premisesCode_Format:
                payload.setPremisesCode(FakerDataGenerator.generateAlphanumericWithSpecialChars(5));
                break;

            case TC_117__Negative__Invalid_premisesCode:
                payload.setPremisesCode("9999999"); // Non-existent
                break;

            case TC_118__Positive__Rewards_Response_Returned_with_Active_Rewards:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateDigits(9));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(7));
                // Optionally validate DB state for active rewards
                //accountData = ApplicationContext.get().getDbAction("mariadb").getActiveRewards();
                Assert.assertFalse(accountData.isEmpty(), "Expected active rewards data");
                break;

            case TC_119__Positive__Rewards_Response_Returned_with_Pending_Rewards:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateDigits(9));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(7));
                //accountData = ApplicationContext.get().getDbAction("mariadb").getPendingRewards();
                Assert.assertFalse(accountData.isEmpty(), "Expected pending rewards data");
                break;

            case TC_120__Positive__Rewards_Response_Returned_with_Active_and_Pending_Rewards:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateDigits(9));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(7));
                //accountData = ApplicationContext.get().getDbAction("mariadb").getMixedRewards();
                Assert.assertFalse(accountData.isEmpty(), "Expected mixed rewards data");
                break;

            case TC_121__Positive__Rewards_Response_Returned_with_NO_Rewards:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateDigits(9));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(7));
                //accountData = ApplicationContext.get().getDbAction("mariadb").getNoRewards();
                Assert.assertTrue(accountData.isEmpty(), "Expected no rewards data");
                break;

            case TC_122__Positive__Rewards_Response_Returned_with_Refer__a__Friend_Reward:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateDigits(9));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(7));
                //accountData = ApplicationContext.get().getDbAction("mariadb").getReferAFriendRewards();
                Assert.assertFalse(accountData.isEmpty(), "Expected refer-a-friend rewards data");
                break;

            default:
                log.warn("Unhandled test condition: {}", testCondition);
                break;
        }
    }
}
