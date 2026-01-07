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

    public void preparePayloadForTestCondition(GetAccountRewardsRequest payload, GetAccountRewardsLabel testCondition) {
        Map<String, Object> accountData = null;
        payload.setRequestID(FakerDataGenerator.generateAlphanumeric(7));
        switch (testCondition) {
            // Negative flows
            case TC_112__Negative__Missing_Request_ID:
                payload.setRequestID("");
                break;

            case TC_113__Negative__Invalid_Request_ID_Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(33));
                break;

            case TC_114__Negative__Duplicate_Request_ID:
                payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());
                break;

            case TC_115__Negative__Missing_customerCode:
                payload.setCustomerCode("");
                break;

            case TC_116__Negative__Invalid_customerCode_Length:
                payload.setCustomerCode(FakerDataGenerator.generateDigits(10));
                break;

            case TC_117__Negative__Invalid_customerCode_Format:
                payload.setCustomerCode(FakerDataGenerator.generateAlphanumericWithSpecialChars(6));
                break;

            case TC_118__Negative__Invalid_customerCode:
                payload.setCustomerCode("999999999"); // Non-existent
                break;

            case TC_119__Negative__Missing_premisesCode:
                payload.setPremisesCode("");
                break;

            case TC_120__Negative__Invalid_premisesCode_Length:
                payload.setPremisesCode(FakerDataGenerator.generateDigits(8));
                break;

            case TC_121__Negative__Invalid_premisesCode_Format:
                payload.setPremisesCode(FakerDataGenerator.generateAlphanumericWithSpecialChars(5));
                break;

            case TC_122__Negative__Invalid_premisesCode:
                payload.setPremisesCode("9999999"); // Non-existent
                break;

            case TC_123__Positive__Active_Rewards:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getActiveRewards();
                payload.setCustomerCode(accountData.get("GZBRWDS_CUST_CODE").toString());
                payload.setPremisesCode(accountData.get("GZBRWDS_PREM_CODE").toString());
                break;

            case TC_124__Positive__Pending_Rewards:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getPendingRewards();
                payload.setCustomerCode(accountData.get("GZBRWDS_CUST_CODE").toString());
                payload.setPremisesCode(accountData.get("GZBRWDS_PREM_CODE").toString());
                break;

            case TC_125__Positive__Active_and_Pending_Rewards:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateDigits(9));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(7));
                // accountData = ApplicationContext.get().getDbAction("mariadb").getMixedRewards();
                Assert.assertFalse(accountData.isEmpty(), "Expected mixed rewards data");
                break;

            case TC_126__Positive__No_Rewards:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountData = ApplicationContext.get().getDbAction().getNoRewards();
                payload.setCustomerCode(accountData.get("GZBRWDS_CUST_CODE").toString());
                payload.setPremisesCode(accountData.get("GZBRWDS_PREM_CODE").toString());
                break;

            case TC_127__Positive__Refer_A_Friend_Rewards:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateDigits(9));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(7));
                // accountData = ApplicationContext.get().getDbAction("mariadb").getReferAFriendRewards();
                Assert.assertFalse(accountData.isEmpty(), "Expected refer-a-friend rewards data");
                break;

            case TC_128__Positive__LoginID_Saved:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateDigits(9));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(7));
                payload.setLoginID("CSRLogin123");
                break;

            default:
                log.warn("Unhandled test condition: {}", testCondition);
                break;
        }
    }
}
