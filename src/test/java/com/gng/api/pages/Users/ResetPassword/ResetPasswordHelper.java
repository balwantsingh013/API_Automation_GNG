package com.gng.api.pages.Users.ResetPassword;

import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pages.Users.GetUserRolesPage.GetUserRolesApiPage;
import com.gng.api.pages.Users.GetUserRolesPage.GetUserRolesHelper;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.pojo.Users.ResetPassword.ResetPasswordRequest;
import com.gng.api.steps.UsersApiSteps.ResetPassword.ResetPasswordApiLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

import static com.gng.api.util.CommonUtil.removeFieldFromJson;
import static com.gng.api.util.CommonUtil.removeFieldsFromJson;

@Slf4j
public class ResetPasswordHelper {
    private final TestContext testContext;
    String user;
    public ResetPasswordHelper(TestContext testContext) {
        this.testContext = testContext;
    }


    ResetPasswordRequest preparePayload(ResetPasswordApiLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        String jsonFileName = apiLabel.equals(ResetPasswordApiLabel.reset_password)
                ? ResetPasswordApiLabel.reset_password.toString()
                : ResetPasswordApiLabel.reset_password_mandatory.toString();
        return BasePage.deserializeJsonToPojo(jsonFileName, ResetPasswordRequest.class);
    }
    public void validatePasswordExpireDaysEntryInDB() {
        Map<String, Object> passwordExpireDayValue = ApplicationContext.get().getDbAction().getPasswordExpireDaysValue();
        Object expireValue = passwordExpireDayValue.get("UZRPSTO_PARM_VALUE");
        int value = expireValue != null ? Integer.parseInt(expireValue.toString()) : -1;
        Assert.assertEquals(value, 45);
    }


    public void setRequestIDBasedOnTypeTC21_TC23(ResetPasswordRequest payload, ResetPasswordApiLabel requestID) {
        switch (requestID) {
            case WITHOUT_REQUEST_ID_TC21:
                String jsonPayload= removeFieldFromJson(payload,"requestID");
                testContext.setCustomRequestPayload(jsonPayload);
                log.info("Final request payload after removing requestID: {}", jsonPayload);
                break;
            case NULL_REQUEST_ID_TC21A:
                payload.setRequestID(null);
                break;
            case DUPLICATE_REQUEST_ID_TC22:
                GetUserRolesApiPage getUserRolesApiPage = new GetUserRolesApiPage(testContext);
                payload.setRequestID(getUserRolesApiPage.getOrGenerateRequestID());
                break;
            case LONG_REQUEST_ID_TC23:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(35));
                break;
        }
    }

    public void setLoginIDBasedOnTypeTC24_TC28(ResetPasswordRequest payload, ResetPasswordApiLabel loginID) {
        switch (loginID) {
            case WITHOUT_LOGIN_ID_AND_OLD_PASSWORD_TC24:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                String jsonPayload= removeFieldsFromJson(payload,"loginID,oldPassword");
                testContext.setCustomRequestPayload(jsonPayload);
                log.info("Final request payload after removing loginID and oldPassword: {}", jsonPayload);
                break;
            case NULL_LOGIN_ID_AND_OLD_PASSWORD_TC24A:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(null);
                payload.setOldPassword(null);
                break;
            case WITHOUT_LOGIN_ID_TC25:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                String jsonPay= removeFieldFromJson(payload,"loginID");
                testContext.setCustomRequestPayload(jsonPay);
                log.info("Final request payload after removing loginID: {}", jsonPay);
                break;
            case NULL_LOGIN_ID_TC25A:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(null);
                break;
            case MAX_LENGTH_LOGIN_ID_TC26:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(FakerDataGenerator.getRandomNumericString(35));
                break;
            case SPECIAL_CHAR_LOGIN_ID_TC27:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(FakerDataGenerator.generateAlphanumericWithSpecialChars(10));
                break;
            case INVALID_LOGIN_ID_NOT_IN_USER_TABLE_TC28:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(FakerDataGenerator.generateString(6));
                break;
        }
    }

    public void setOldPasswordBasedOnTypeTC29_TC31(ResetPasswordRequest payload, ResetPasswordApiLabel oldPassword) {
        switch (oldPassword) {
            case WITHOUT_OLD_PASSWORD_TC29:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                String updatedPayload= removeFieldFromJson(payload,"oldPassword");
                testContext.setCustomRequestPayload(updatedPayload);
                log.info("Final request payload after removing oldPassword: {}", updatedPayload);
                break;
            case NULL_OLD_PASSWORD_TC29A:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setOldPassword(null);
                break;
            case UNENCRYPTED_OLD_PASSWORD_TC30:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                String activeUser =  ApplicationContext.get().getDbAction().getActiveUserID();
                payload.setLoginID(activeUser);
                payload.setOldPassword(FakerDataGenerator.generateString(7));
                break;
            case ENCRYPTED_OLD_PASSWORD_MORE_THAN_10_CHAR_TC31:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setOldPassword(GetUserRolesHelper.encryptedPasswordMoreThan10Char);
                break;
            case ENCRYPTED_OLD_PASSWORD_LESS_THAN_7_CHAR_TC31A:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setOldPassword(GetUserRolesHelper.encryptedPasswordLessThan7Char);
                break;
            case ENCRYPTED_OLD_PASSWORD_WITH_8_CHAR_WITH_SPECIAL_CHAR_TC31B:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setOldPassword(GetUserRolesHelper.encrypted8CharPasswordWithSpecialChar);
                break;
        }
    }

    public void setNewPasswordBasedOnTypeTC32_TC35(ResetPasswordRequest payload, ResetPasswordApiLabel newPassword) {
        switch (newPassword) {
            case WITHOUT_NEW_PASSWORD_TC32:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                String updatedPayload= removeFieldFromJson(payload,"newPassword");
                testContext.setCustomRequestPayload(updatedPayload);
                log.info("Final request payload after removing newPassword: {}", updatedPayload);
                break;
            case NULL_NEW_PASSWORD_TC32A:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setNewPassword(null);
                break;
            case UNENCRYPTED_NEW_PASSWORD_TC33:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                String activeUser =  ApplicationContext.get().getDbAction().getActiveUserID();
                payload.setLoginID(activeUser);
                payload.setNewPassword(FakerDataGenerator.generateString(7));
                break;
            case ENCRYPTED_NEW_PASSWORD_MORE_THAN_10_CHAR_TC34:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setNewPassword(GetUserRolesHelper.encryptedPasswordMoreThan10Char);
                break;
            case ENCRYPTED_NEW_PASSWORD_LESS_THAN_7_CHAR_TC34A:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setNewPassword(GetUserRolesHelper.encryptedPasswordLessThan7Char);
                break;
            case ENCRYPTED_OLD_PASSWORD_WITH_8_CHAR_WITH_SPECIAL_CHAR_TC34B:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setNewPassword(GetUserRolesHelper.encrypted8CharPasswordWithSpecialChar);
                break;
            case OLD_PASSWORD_NEW_PASSWORD_SAME_TC35:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                String user =  ApplicationContext.get().getDbAction().getActiveUserID();
                payload.setLoginID(user);
                break;
        }
    }

    public void validatePasswordWithLoginIN(ResetPasswordRequest payload)
    {
        user = ApplicationContext.get().getDbAction().getActiveUserID();
        payload.setLoginID(user);
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setOldPassword(GetUserRolesHelper.validEncryptedPassword);
    }


    public void validateFailedCount()
    {
        Map<String, Object> dbValidationResult = ApplicationContext.get().getDbAction().validateFailedLoginForSpecificUser(user);
        if (dbValidationResult.isEmpty()) {
            System.out.println("No data found for the given user.");
        } else {
            Object failedLoginsObj = dbValidationResult.get("FAILED_LOGINS");
            Object userLockedIndObj = dbValidationResult.get("USER_LOCKED_IND");
            int failedLogins = failedLoginsObj != null ? Integer.parseInt(failedLoginsObj.toString()) : -1;
            String userLockedInd = userLockedIndObj != null ? (userLockedIndObj.toString()) : "UNKNOWN";
            Assert.assertEquals(failedLogins, 2, "Mismatch in expected FAILED_LOGINS value");
            Assert.assertEquals(userLockedInd, "N", "Mismatch in expected USER_LOCKED_IND value");
        }
    }
}
