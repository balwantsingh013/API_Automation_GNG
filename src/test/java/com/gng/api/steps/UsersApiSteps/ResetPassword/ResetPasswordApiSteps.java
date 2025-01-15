package com.gng.api.steps.UsersApiSteps.ResetPassword;

import com.gng.api.pages.Users.ResetPassword.ResetPasswordApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.java.en.When;

import static com.gng.api.steps.UsersApiSteps.ResetPassword.ResetPasswordApiLabel.reset_password;

public class ResetPasswordApiSteps {
    private final TestContext testContext;
    private final ResetPasswordApiPage resetPasswordApiPage;

    public ResetPasswordApiSteps(TestContext testContext, ResetPasswordApiPage resetPasswordApiPage) {
        this.testContext = testContext;
        this.resetPasswordApiPage = resetPasswordApiPage;
        testContext.setResetPasswordApiPage(resetPasswordApiPage);
    }
    @When("a request is made to the ResetPassword Api with {string}TC21_TC23")
    public void a_request_is_made_to_the_ResetPassword_Api_with_TC21_TC23(String requestID) {
        resetPasswordApiPage.validateInvalidRequestIDCasesTC21_TC23(reset_password, ResetPasswordApiLabel.valueOf(requestID));
    }
    @When("a request is made to the ResetPassword Api with {string}TC24_TC28")
    public void a_request_is_made_to_the_ResetPassword_Api_with_TC24_TC28(String loginID) {
        resetPasswordApiPage.validateInvalidLoginIDCasesTC24_TC28(reset_password, ResetPasswordApiLabel.valueOf(loginID));
    }
    @When("a request is made to the ResetPassword Api with {string}TC29_TC31")
    public void a_request_is_made_to_the_ResetPassword_Api_with_TC29_TC31(String oldPassword) {
        resetPasswordApiPage.validateInvalidOldPasswordCasesTC29_TC31(reset_password, ResetPasswordApiLabel.valueOf(oldPassword));
    }
    @When("a request is made to the ResetPassword Api with {string}TC32_TC35")
    public void a_request_is_made_to_the_ResetPassword_Api_with_TC32_TC35(String newPassword) {
        resetPasswordApiPage.validateInvalidNewPasswordCasesTC32_TC35(reset_password, ResetPasswordApiLabel.valueOf(newPassword));
    }


}
