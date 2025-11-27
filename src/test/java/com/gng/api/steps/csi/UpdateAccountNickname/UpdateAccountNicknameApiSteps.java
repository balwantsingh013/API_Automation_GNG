package com.gng.api.steps.csi.UpdateAccountNickname;

import com.gng.api.pages.csi.UpdateAccountNicknamePage.UpdateAccountNicknamePage;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.util.CommonUtil;
import com.gng.api.util.TestContextHolder;
import io.cucumber.java.Before;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import static com.gng.api.steps.csi.UpdateAccountNickname.UpdateAccountNicknameLabel.set_account_nickname;

@Slf4j
public class UpdateAccountNicknameApiSteps {

    @Before
    public void setupToken() {
        CommonUtil.silentlyGenerateAuthToken(); // No Allure logging
    }

    private final TestContext testContext;
    private final UpdateAccountNicknamePage setAccountNicknamePage;

    public UpdateAccountNicknameApiSteps(TestContext testContext, UpdateAccountNicknamePage setAccountNicknamePage) {
        this.testContext = testContext;
        this.setAccountNicknamePage = setAccountNicknamePage;
        testContext.setGetAccountInfoApiPage(setAccountNicknamePage);
        // Set globally for utility access
        TestContextHolder.set(testContext);
    }

    @When("a request is made to UpdateAccountNickname Api for {string}")
    public void a_request_is_made_to_the_SetAccountNickname_Api_with_test_condition(String testCondition) {
        CommonUtil.logTestDescriptionToReports(testCondition);
        setAccountNicknamePage.validateResponseForNegativeTestConditions(set_account_nickname, UpdateAccountNicknameLabel.valueOf(testCondition));
    }
}
