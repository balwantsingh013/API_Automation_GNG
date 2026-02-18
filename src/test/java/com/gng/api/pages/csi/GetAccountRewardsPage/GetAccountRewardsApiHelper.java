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

    public void validateRewardInfo(GetAccountRewardsLabel testCondition){
        Map<String, Object> accountData = null;

        switch (testCondition) {

            case TC_139__Positive__Rewards_Response_Returned_with_Active_Rewards:
                accountData = ApplicationContext.get().getDbAction().getRewardDetails(testContext.getRewardId());
                break;

            case TC_140__Positive__Rewards_Response_Returned_with_Pending_Rewards:
            accountData = ApplicationContext.get().getDbAction().getRewardDetails(testContext.getRewardId());
            Assert.assertEquals(testContext.getGetAccountRewardsResponse().getData().getRewards().getFirst().getRemainingOccurrences(),"","Incorrect count for remainingOccurrences returned for Pending rewards");
            break;

            case TC_143__Positive__Active_Rewards_Response_Returned_with_RewardsRefer_A_Friend_Rewards,
                 TC_144__Positive__Pending_Rewards_Response_Returned_with_RewardsRefer_A_Friend_Rewards:
                accountData = ApplicationContext.get().getDbAction().getRewardDetails("2");
                break;

            case TC_141__Positive__Rewards_Response_Returned_with_Active_and_Pending_Rewards:
                accountData = ApplicationContext.get().getDbAction().getRewardDetails(testContext.getActiveRewardId());
                accountData = ApplicationContext.get().getDbAction().getRewardDetails(testContext.getPendingRewardId());
                break;

            default:
                break;
        }

    }

    public void preparePayloadForTestCondition(GetAccountRewardsRequest payload, GetAccountRewardsLabel testCondition) {

        Map<String, Object> accountData = null;
        payload.setRequestID(FakerDataGenerator.generateAlphanumeric(7));

        switch (testCondition) {

            case TC_129__Negative__Missing_Request_ID:
                payload.setRequestID("");
                break;

            case TC_130__Negative__Invalid_Request_ID_Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(33));
                break;

            case TC_131__Negative__Duplicate_Request_ID:
                payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());
                break;

            case TC_132__Negative__Missing_customerCode:
                payload.setCustomerCode("");
                break;

            case TC_133__Negative__Invalid_customerCode_Length:
                payload.setCustomerCode(FakerDataGenerator.generateDigits(10));
                break;

            case TC_134__Negative__Invalid_customerCode_Format:
                payload.setCustomerCode(FakerDataGenerator.generateAlphanumericWithSpecialChars(6));
                break;

            case TC_135__Negative__Invalid_Account_Number:
                payload.setCustomerCode("9988776");
                accountData = ApplicationContext.get().getDbAction().getUserAccountInfo("9988776");// Non-existent
                break;

            case TC_136__Negative__Missing_premisesCode:
                payload.setPremisesCode("");
                break;

            case TC_137__Negative__Invalid_premisesCode_Length:
                payload.setPremisesCode(FakerDataGenerator.generateDigits(8));
                break;

            case TC_138__Negative__Invalid_premisesCode_Format:
                payload.setPremisesCode(FakerDataGenerator.generateAlphanumericWithSpecialChars(5));
                break;

            case TC_139__Positive__Rewards_Response_Returned_with_Active_Rewards:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActiveRewards();
                Assert.assertNotNull(accountData, "Active rewards data should not be null");
                payload.setCustomerCode(accountData.get("GZBRWDS_CUST_CODE").toString());
                payload.setPremisesCode(accountData.get("GZBRWDS_PREM_CODE").toString());
                testContext.setRewardId(accountData.get("GZBRWDS_REWARD_ID").toString());
                break;

            case TC_140__Positive__Rewards_Response_Returned_with_Pending_Rewards:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getPendingRewards();
                Assert.assertNotNull(accountData, "Pending rewards data should not be null");
                payload.setCustomerCode(accountData.get("GZBPRWD_CUST_CODE").toString());
                payload.setPremisesCode(accountData.get("GZBPRWD_PREM_CODE").toString());
                testContext.setRewardId(accountData.get("GZBPRWD_REWARD_ID").toString());
                break;

            case TC_141__Positive__Rewards_Response_Returned_with_Active_and_Pending_Rewards:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getMixedRewards();
                Assert.assertNotNull(accountData, "Mixed rewards data should not be null");
                payload.setCustomerCode(accountData.get("GZBRWDS_CUST_CODE").toString());
                payload.setPremisesCode(accountData.get("GZBRWDS_PREM_CODE").toString());
                testContext.setActiveRewardId(accountData.get("GZBRWDS_REWARD_ID").toString());
                testContext.setPendingRewardId(accountData.get("GZBPRWD_REWARD_ID").toString());
                break;

            case TC_142__Positive__Rewards_Response_Returned_with_No_Rewards:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getNoRewards();
                Assert.assertNotNull(accountData, "No rewards data should not be null");
                payload.setCustomerCode(accountData.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountData.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_143__Positive__Active_Rewards_Response_Returned_with_RewardsRefer_A_Friend_Rewards:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActiveReferAFriendRewards();
                Assert.assertNotNull(accountData, "Refer-a-friend rewards data should not be null");
                payload.setCustomerCode(accountData.get("GZBRWDS_CUST_CODE").toString());
                payload.setPremisesCode(accountData.get("GZBRWDS_PREM_CODE").toString());
                break;

            case TC_144__Positive__Pending_Rewards_Response_Returned_with_RewardsRefer_A_Friend_Rewards:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getPendingReferAFriendRewards();
                Assert.assertNotNull(accountData, "Refer-a-friend rewards data should not be null");
                payload.setCustomerCode(accountData.get("GZBPRWD_CUST_CODE").toString());
                payload.setPremisesCode(accountData.get("GZBPRWD_PREM_CODE").toString());
                break;

            default:
                log.warn("Unhandled test condition: {}", testCondition);
                break;
        }
    }
}
