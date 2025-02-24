package com.gng.api.pages.Users.ResetPassword;

import com.gng.api.pages.BasePage;
import com.gng.api.pages.Users.GetUserRolesPage.GetUserRolesHelper;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.pojo.Users.GetUserRoles.GetUserRolesRequest;
import com.gng.api.pojo.Users.ResetPassword.ResetPasswordRequest;
import com.gng.api.steps.UsersApiSteps.ResetPassword.ResetPasswordApiLabel;
import com.gng.api.util.FakerDataGenerator;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.GET_USER_ROLES;
import static com.gng.api.constants.ApiEndPoint.RESET_PASSWORD;


public class ResetPasswordApiPage   extends BasePage  {
    private final ResetPasswordHelper helper;

    public ResetPasswordApiPage(TestContext testContext) {
        super(testContext);
        this.helper = new ResetPasswordHelper(testContext);
    }
    public void validateUserTable_UZRPSTO_PARM_NAME_ValueTC20() {
        helper.validatePasswordExpireDaysEntryInDB();
    }

    public void validateInvalidRequestIDCasesTC21_TC23(ResetPasswordApiLabel apiLabel, ResetPasswordApiLabel requestID) {
        ResetPasswordRequest payload = helper.preparePayload(apiLabel);
        helper.setRequestIDBasedOnTypeTC21_TC23(payload, requestID);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, RESET_PASSWORD, 200);
        testContext.setResponse(response);
    }
    public void validateInvalidLoginIDCasesTC24_TC28(ResetPasswordApiLabel apiLabel, ResetPasswordApiLabel loginID) {
        ResetPasswordRequest payload = helper.preparePayload(apiLabel);
        helper.setLoginIDBasedOnTypeTC24_TC28(payload, loginID);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, RESET_PASSWORD, 200);
        testContext.setResponse(response);
    }
    public void validateInvalidOldPasswordCasesTC29_TC31(ResetPasswordApiLabel apiLabel, ResetPasswordApiLabel oldPassword) {
        ResetPasswordRequest payload = helper.preparePayload(apiLabel);
        helper.setOldPasswordBasedOnTypeTC29_TC31(payload, oldPassword);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, RESET_PASSWORD, 200);
        testContext.setResponse(response);
    }
    public void validateInvalidNewPasswordCasesTC32_TC35(ResetPasswordApiLabel apiLabel, ResetPasswordApiLabel newPassword) {
        ResetPasswordRequest payload = helper.preparePayload(apiLabel);
        helper.setNewPasswordBasedOnTypeTC32_TC35(payload, newPassword);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, RESET_PASSWORD, 200);
        testContext.setResponse(response);
    }


    public void validatePasswordDoesNotMatchWithLoginID(ResetPasswordApiLabel apiLabel)
    {
        ResetPasswordRequest payload = helper.preparePayload(apiLabel);
        helper.validatePasswordWithLoginIN(payload);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, RESET_PASSWORD, 200);
        testContext.setResponse(response);
        helper.validateFailedCount();
    }


}
