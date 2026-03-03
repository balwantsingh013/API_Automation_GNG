package com.gng.api.steps.csi.UpdateUsername;

import com.gng.api.pages.csi.UpdateUsernamePage.UpdateUsernamePage;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.util.CommonUtil;
import com.gng.api.util.TestContextHolder;
import io.cucumber.java.Before;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

import static com.gng.api.steps.csi.UpdateUsername.UpdateUsernameLabel.update_username;

@Slf4j
public class UpdateUsernameApiSteps {

    @Before
    public void setupToken() {
        CommonUtil.silentlyGenerateAuthToken(); // No Allure logging
    }

    private final TestContext testContext;
    private final UpdateUsernamePage updateUsernamePage;

    public UpdateUsernameApiSteps(TestContext testContext, UpdateUsernamePage updateUsernamePage) {
        this.testContext = testContext;
        this.updateUsernamePage = updateUsernamePage;
        testContext.setUpdateUsernameApiPage(updateUsernamePage);
        // Set globally for utility access
        TestContextHolder.set(testContext);
    }

    @When("a request is made to UpdateUsername Api for {string}")
    public void a_request_is_made_to_the_UpdateUsername_Api_with_test_condition(String testCondition) {
        CommonUtil.logTestDescriptionToReports(testCondition);
        updateUsernamePage.validateResponseForTestConditions(
                update_username,
                UpdateUsernameLabel.valueOf(testCondition)
        );
    }

    @When("verify if the username is present")
    public void verify_if_username_is_present(){
        updateUsernamePage.validateIfUSernameIsPresent();
        }

    @When("verify if the account is accosiated with the username")
    public void verify_account_association(){
        updateUsernamePage.validateAccountAssociation();
    }
}
