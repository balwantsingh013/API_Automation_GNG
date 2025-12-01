package com.gng.api.steps.csi.GetAccountRewards;

import com.gng.api.pages.csi.GetAccountRewardsPage.GetAccountRewardsPage;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.util.CommonUtil;
import com.gng.api.util.TestContextHolder;
import io.cucumber.java.Before;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

import static com.gng.api.steps.csi.GetAccountRewards.GetAccountRewardsLabel.get_account_rewards;

@Slf4j
public class GetAccountRewardsApiSteps {

    @Before
    public void setupToken() {
        CommonUtil.silentlyGenerateAuthToken(); // No Allure logging
    }

    private final TestContext testContext;
    private final GetAccountRewardsPage getAccountRewardsPage;

    public GetAccountRewardsApiSteps(TestContext testContext, GetAccountRewardsPage getAccountRewardsPage) {
        this.testContext = testContext;
        this.getAccountRewardsPage = getAccountRewardsPage;
        testContext.setGetAccountInfoApiPage(getAccountRewardsPage);
        // Set globally for utility access
        TestContextHolder.set(testContext);
    }

    @When("a request is made to GetAccountRewards Api for {string}")
    public void a_request_is_made_to_the_GetAccountRewards_Api_with_invalid_values(String testCondition) {
        CommonUtil.logTestDescriptionToReports(testCondition);
        getAccountRewardsPage.validateResponseForNegativeTestConditions(
                get_account_rewards,
                GetAccountRewardsLabel.valueOf(testCondition)
        );
    }
}
