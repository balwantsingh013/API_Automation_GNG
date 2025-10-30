package com.gng.api.steps.csi.ValidateUserName;

import com.gng.api.pages.csi.ValidateUserNamePage.ValidateUserNamePage;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.util.CommonUtil;
import com.gng.api.util.TestContextHolder;
import io.cucumber.java.Before;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import static com.gng.api.steps.csi.ValidateUserName.ValidateUserNameLabel.validate_username;

@Slf4j
public class ValidateUserNameApiSteps {

    @Before
    public void setupToken() {
        CommonUtil.silentlyGenerateAuthToken(); // No Allure logging
    }

    private final TestContext testContext;
    private final ValidateUserNamePage validateUserNamePage;


    public ValidateUserNameApiSteps(TestContext testContext, ValidateUserNamePage validateUserNamePage) {
        this.testContext = testContext;
        this.validateUserNamePage = validateUserNamePage;
        testContext.setGetAccountInfoApiPage(validateUserNamePage);
        // Set globally for utility access
        TestContextHolder.set(testContext);
    }

    @When("a request is made to validateUsername Api for {string}")
    public void a_request_is_made_to_the_validateUsername_Api_with_invalid_values(String testCondition) {
        CommonUtil.logTestDescriptionToReports(testCondition);
        validateUserNamePage.validateResponseForNegativeTestConditions(validate_username, ValidateUserNameLabel.valueOf(testCondition));
    }

}