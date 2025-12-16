package com.gng.api.steps.csi.GetAccountInfo;

import com.gng.api.pages.csi.GetAccountInfoPage.GetAccountInfoPage;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.util.CommonUtil;
import com.gng.api.util.TestContextHolder;
import io.cucumber.java.Before;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

import static com.gng.api.steps.csi.GetAccountInfo.GetAccountInfoLabel.csi_get_account_info;

@Slf4j
public class GetAccountInfoApiSteps {

    @Before
    public void setupToken() {
        CommonUtil.silentlyGenerateAuthToken(); // No Allure logging
    }

    private final TestContext testContext;
    private final GetAccountInfoPage getAccountInfoPage;

    public GetAccountInfoApiSteps(TestContext testContext, GetAccountInfoPage getAccountInfoPage) {
        this.testContext = testContext;
        this.getAccountInfoPage = getAccountInfoPage;
        testContext.setGetAccountInfoApiPage(getAccountInfoPage);
        // Set globally for utility access
        TestContextHolder.set(testContext);
    }

    @When("a request is made to GetAccountInfo Api for {string}")
    public void a_request_is_made_to_the_GetAccountInfo_Api_with_test_condition(String testCondition) {
        CommonUtil.logTestDescriptionToReports(testCondition);
        getAccountInfoPage.validateResponseForTestConditions(csi_get_account_info, GetAccountInfoLabel.valueOf(testCondition));
    }
}
