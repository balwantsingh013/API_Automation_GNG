package com.gng.api.steps.csi.UpdatePassword;

import com.gng.api.pages.csi.UpdatePasswordPage.UpdatePasswordPage;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.util.CommonUtil;
import com.gng.api.util.TestContextHolder;
import io.cucumber.java.Before;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import static com.gng.api.steps.csi.UpdatePassword.UpdatePasswordLabel.update_password;

@Slf4j
public class UpdatePasswordApiSteps {

    @Before
    public void setupToken() {
        CommonUtil.silentlyGenerateAuthToken(); // No Allure logging
    }

    private final TestContext testContext;
    private final UpdatePasswordPage updatePasswordPage;

    public UpdatePasswordApiSteps(TestContext testContext, UpdatePasswordPage updatePasswordPage) {
        this.testContext = testContext;
        this.updatePasswordPage = updatePasswordPage;
        testContext.setGetAccountInfoApiPage(updatePasswordPage);
        // Set globally for utility access
        TestContextHolder.set(testContext);
    }

    @When("a request is made to UpdatePassword Api for {string}")
    public void a_request_is_made_to_the_UpdatePassword_Api_with_test_condition(String testCondition) {
        CommonUtil.logTestDescriptionToReports(testCondition);
        updatePasswordPage.validateResponseForNegativeTestConditions(update_password, UpdatePasswordLabel.valueOf(testCondition));
    }

    @When("change the password back to the old one")
    public void rollBackPassword(){
        updatePasswordPage.rollbackPasswordToPreviousOne();
    }

    @When("verify if the new password is different from the old password")
    public void verify_if_the_password_is_updated(){
        updatePasswordPage.verifyUpdatedPassword();
    }

}
