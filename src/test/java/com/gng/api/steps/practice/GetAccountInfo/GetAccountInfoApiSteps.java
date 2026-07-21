package com.gng.api.steps.practice.GetAccountInfo;

import com.gng.api.pages.practice.GetAccountInfoPage.GetAccountInfoPage;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.util.CommonUtil;
import com.gng.api.util.TestContextHolder;
import io.cucumber.java.Before;
import io.cucumber.java.en.When;

import static com.gng.api.steps.practice.GetAccountInfo.GetAccountInfoLabel.practice_get_account_info;

public class GetAccountInfoApiSteps {

    @Before
    public void setupToken() {
        CommonUtil.silentlyGenerateAuthToken();
    }

    private final GetAccountInfoPage getAccountInfoPage;

    public GetAccountInfoApiSteps(TestContext testContext, GetAccountInfoPage getAccountInfoPage) {
        this.getAccountInfoPage = getAccountInfoPage;
        testContext.setGetAccountInfoApiPage(getAccountInfoPage);
        TestContextHolder.set(testContext);
    }

    @When("a practice request is made to GetAccountInfo Api for {string}")
    public void a_practice_request_is_made_to_get_account_info_api(String testCondition) {
        CommonUtil.logTestDescriptionToReports(testCondition);
        getAccountInfoPage.validateResponseForTestConditions(
                practice_get_account_info,
                GetAccountInfoLabel.valueOf(testCondition));
    }
}
