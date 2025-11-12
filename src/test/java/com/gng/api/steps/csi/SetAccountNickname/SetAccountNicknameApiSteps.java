package com.gng.api.steps.csi.SetAccountNickname;

import com.gng.api.pages.csi.SetAccountNicknamePage.SetAccountNicknamePage;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.util.CommonUtil;
import com.gng.api.util.TestContextHolder;
import io.cucumber.java.Before;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import static com.gng.api.steps.csi.SetAccountNickname.SetAccountNicknameLabel.set_account_nickname;

@Slf4j
public class SetAccountNicknameApiSteps {

    @Before
    public void setupToken() {
        CommonUtil.silentlyGenerateAuthToken(); // No Allure logging
    }

    private final TestContext testContext;
    private final SetAccountNicknamePage setAccountNicknamePage;

    public SetAccountNicknameApiSteps(TestContext testContext, SetAccountNicknamePage setAccountNicknamePage) {
        this.testContext = testContext;
        this.setAccountNicknamePage = setAccountNicknamePage;
        testContext.setGetAccountInfoApiPage(setAccountNicknamePage);
        // Set globally for utility access
        TestContextHolder.set(testContext);
    }

    @When("a request is made to SetAccountNickname Api for {string}")
    public void a_request_is_made_to_the_SetAccountNickname_Api_with_test_condition(String testCondition) {
        CommonUtil.logTestDescriptionToReports(testCondition);
        setAccountNicknamePage.validateResponseForNegativeTestConditions(set_account_nickname, SetAccountNicknameLabel.valueOf(testCondition));
    }
}
