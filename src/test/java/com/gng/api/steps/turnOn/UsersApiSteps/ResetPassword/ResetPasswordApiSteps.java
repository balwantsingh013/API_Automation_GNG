package com.gng.api.steps.turnOn.UsersApiSteps.ResetPassword;

import com.gng.api.pages.turnOn.Users.ResetPassword.ResetPasswordApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.java.en.When;

import static com.gng.api.steps.turnOn.UsersApiSteps.ResetPassword.ResetPasswordApiLabel.reset_password;

public class ResetPasswordApiSteps {
    private final TestContext testContext;
    private final ResetPasswordApiPage resetPasswordApiPage;

    public ResetPasswordApiSteps(TestContext testContext, ResetPasswordApiPage resetPasswordApiPage) {
        this.testContext = testContext;
        this.resetPasswordApiPage = resetPasswordApiPage;
        testContext.setResetPasswordApiPage(resetPasswordApiPage);
    }
    @When("a request is made to the validate UZRPSTO_PARM_NAME value in DB TC20")
    public void a_request_is_made_to_validate_UZRPSTO_PARM_NAME_Value_TC20() {
        resetPasswordApiPage.validateUserTable_UZRPSTO_PARM_NAME_ValueTC20();
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

    @When("a request is made to validate oldPassword {string} doesn't match with LoginID TC36")
    public void a_request_is_made_to_validate_password_does_not_match_with_LoginID_TC36(String oldPassword)
    {
        resetPasswordApiPage.validatePasswordDoesNotMatchWithLoginID(reset_password, ResetPasswordApiLabel.valueOf(oldPassword));
    }

    @When("a request is made to the ResetPassword Api with {string}")
    public void a_request_is_made_to_reset_password_for_expired_password(String testCondition)
    {
        resetPasswordApiPage.validateResetPasswordWithExpiredPassword(reset_password, ResetPasswordApiLabel.valueOf(testCondition));
    }

    @When("a request is made to the ResetPassword Api with locked out account details TC38")
    public void a_request_is_made_to_reset_password_for_locked_out_account()
    {
        resetPasswordApiPage.validateResetPasswordForLockedOutAccount(reset_password);
    }

    @When("a request is made to the ResetPassword Api to set the old password again for {string}")
    public void a_request_is_made_to_reset_password_to_set_old_password_again(String testCondition) {
        resetPasswordApiPage.validateResetPassword(reset_password, ResetPasswordApiLabel.valueOf(testCondition));
    }

    @When("verify if the failed login count is updated to {int} for {string}")
    public void verify_the_failed_login_count(int count, String testCondition){
        resetPasswordApiPage.verifyTheFailedLoginCount(count, ResetPasswordApiLabel.valueOf(testCondition));
    }

    @When("verify if the password expiration is updated to Sysdate plus 45 days for {string}")
    public void verify_password_expiration_updated(String testCondition) {
        resetPasswordApiPage.verifyPasswordExpirationUpdated(ResetPasswordApiLabel.valueOf(testCondition));
    }

    @When("update the failed login count to {int} for {string}")
    public void update_the_failed_login_count(int count, String testCondition){
        resetPasswordApiPage.updateTheFailedLoginCount(count, ResetPasswordApiLabel.valueOf(testCondition));
    }

    @When("update the locked indicator to {string} and failed logins to {int} for {string}")
    public void update_the_locked_indicaor(String lockedOutIndicator, int count, String testCondition){
        resetPasswordApiPage.updateTheLockedIndicator(lockedOutIndicator,count, ResetPasswordApiLabel.valueOf(testCondition));
    }

}
