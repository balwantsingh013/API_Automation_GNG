package com.gng.api.pages.turnOn.Users.ResetPassword;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pages.turnOn.Users.GetUserRolesPage.GetUserRolesApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.pojo.Users.ResetPassword.ResetPasswordRequest;
import com.gng.api.report.ExtentReportManager;
import com.gng.api.steps.AesEncryption.AesEncryptionSteps;
import com.gng.api.steps.turnOn.UsersApiSteps.ResetPassword.ResetPasswordApiLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import java.util.Map;

import static com.gng.api.steps.turnOn.UsersApiSteps.ResetPassword.ResetPasswordApiLabel.EXPIRED_PASSWORD_TC_40;
import static com.gng.api.util.CommonUtil.removeFieldFromJson;
import static com.gng.api.util.CommonUtil.removeFieldsFromJson;

@Slf4j
public class ResetPasswordHelper {
    private final TestContext testContext;
    String validEncryptedPassword;
    public static String valid_password="Password@2";
    public static String old_valid_password="Password@1";
    public static String loginId="autotester";
    public static String loginId2="autotester1";
    public static String loginId3="autotester2";
    public static String loginId4 ="sys";
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

    public void resetPasswordForExpiredPassword(ResetPasswordRequest payload, ResetPasswordApiLabel testCondition){
        if(testCondition.equals(EXPIRED_PASSWORD_TC_40)) {
            int rowUpdated = ApplicationContext.get().getDbAction().passwordExpiredUpdateQuery(loginId);
            Assert.assertEquals(rowUpdated, 1, "Expected exactly one row to be updated");
            Map<String, Object> isPasswordExpired = ApplicationContext.get().getDbAction().PasswordExpiredCheckQuery(loginId);
            Assert.assertEquals(isPasswordExpired.get("IS_EXPIRED"), "Y", "Password is not be expired");
            payload.setLoginID(loginId);
        }
        else{
            Map<String, Object> isPasswordExpired=ApplicationContext.get().getDbAction().PasswordExpiredCheckQuery(loginId3);
            Assert.assertEquals(isPasswordExpired.get("IS_EXPIRED"), "N", "Password should not be expired");
            payload.setLoginID(loginId3);
        }
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setNewPassword(AesEncryptionSteps.encryptData(valid_password));
        payload.setOldPassword(AesEncryptionSteps.encryptData(old_valid_password));
    }

    public void resetPasswordForLockedOutAccount(ResetPasswordRequest payload){
        Map<String, Object> isAccountLocked=ApplicationContext.get().getDbAction().checkUserLockStatusQuery();
        Assert.assertEquals(isAccountLocked.get("USER_LOCKED_IND"), "Y", "Account is not locked");
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setNewPassword(AesEncryptionSteps.encryptData(valid_password));
        payload.setOldPassword(AesEncryptionSteps.encryptData(old_valid_password));
        payload.setLoginID(loginId2);
    }

    public void changeThePasswordBackToOldPassword(ResetPasswordRequest payload, ResetPasswordApiLabel testCondition){
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setNewPassword(AesEncryptionSteps.encryptData(old_valid_password));
        payload.setOldPassword(AesEncryptionSteps.encryptData(valid_password));
        if(testCondition.equals(EXPIRED_PASSWORD_TC_40)) {
            payload.setLoginID(loginId);
        }
        else{
            payload.setLoginID(loginId3);
        }
    }

    public void verifyTheNumberOfFailedLogins(int count, ResetPasswordApiLabel testCondition){
        String user=resolveUser(testCondition,loginId, loginId3);
        int failedLoginsCount =  ApplicationContext.get().getDbAction().failedLoginsCount(user);
        Assert.assertEquals(failedLoginsCount,count);
    }

    public void verifyPasswordExpirationStatus(ResetPasswordApiLabel testCondition) {
        String user=resolveUser(testCondition,loginId, loginId3);
        boolean isUpdated = ApplicationContext.get().getDbAction().isPasswordExpirationUpdatedToSysdatePlus45(user);
        Assert.assertTrue( isUpdated, "Password expiration is not updated to SYSDATE + 45");
    }

    public void updateNumberOfFailedLogins(int count, ResetPasswordApiLabel testCondition){
        String user=resolveUser(testCondition,loginId, loginId3);
        ApplicationContext.get().getDbAction().updateFailedLoginsCount(count,user);
        int failedLoginsCount = ApplicationContext.get().getDbAction().failedLoginsCount(user);
        Assert.assertEquals(failedLoginsCount,count);
    }

    public String resolveUser(ResetPasswordApiLabel testCondition, String loginId, String loginId3) {
        if (testCondition.equals(EXPIRED_PASSWORD_TC_40)) {
            return loginId;
        } else {
            return loginId3;
        }
    }

    public void updateLockedOutIndicator(String lockedOutIndicator, int count, ResetPasswordApiLabel testCondition){
        switch(testCondition){
            case INVALID_PASSWORD_TEST_CONDITION:
                ApplicationContext.get().getDbAction().updateUserLockedStatus(lockedOutIndicator, count, loginId4);
                break;

            case VALID_PASSWORD_TEST_CONDITION:
                ApplicationContext.get().getDbAction().updateUserLockedStatus(lockedOutIndicator, count, loginId);
                break;

            case LOCKED_OUT_ACCOUNT_TC38:
                ApplicationContext.get().getDbAction().updateUserLockedStatus(lockedOutIndicator, count, loginId2);
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
                String encryptedMore12Char = AesEncryptionSteps.encryptData(FakerDataGenerator.generateString(12));
                log.info(encryptedMore12Char);
                ExtentReportManager.logInfoToReport(encryptedMore12Char);
                payload.setOldPassword(encryptedMore12Char);
                break;
            case ENCRYPTED_OLD_PASSWORD_LESS_THAN_7_CHAR_TC31A:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                String encryptedLess7Char = AesEncryptionSteps.encryptData(FakerDataGenerator.generateString(5));
                log.info(encryptedLess7Char);
                ExtentReportManager.logInfoToReport(encryptedLess7Char);
                payload.setOldPassword(encryptedLess7Char);
                break;
            case ENCRYPTED_OLD_PASSWORD_WITH_8_CHAR_WITH_SPECIAL_CHAR_TC31B:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                String encryptedSpecial = AesEncryptionSteps.encryptData(FakerDataGenerator.generateAlphanumericWithSpecialChars(8));
                log.info(encryptedSpecial);
                ExtentReportManager.logInfoToReport(encryptedSpecial);
                payload.setOldPassword(encryptedSpecial);
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
                String encryptedMore12Char = AesEncryptionSteps.encryptData(FakerDataGenerator.generateString(12));
                log.info(encryptedMore12Char);
                ExtentReportManager.logInfoToReport(encryptedMore12Char);
                payload.setNewPassword(encryptedMore12Char);
                break;
            case ENCRYPTED_NEW_PASSWORD_LESS_THAN_7_CHAR_TC34A:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                String encryptedLess7Char = AesEncryptionSteps.encryptData(FakerDataGenerator.generateString(5));
                log.info(encryptedLess7Char);
                ExtentReportManager.logInfoToReport(encryptedLess7Char);
                payload.setNewPassword(encryptedLess7Char);
                break;
            case ENCRYPTED_OLD_PASSWORD_WITH_8_CHAR_WITH_SPECIAL_CHAR_TC34B:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                String encryptedSpecial = AesEncryptionSteps.encryptData(GlobalEnums.InvalidValues.INVALID_PASSWORD.getValue());
                log.info(encryptedSpecial);
                ExtentReportManager.logInfoToReport(encryptedSpecial);
                payload.setNewPassword(encryptedSpecial);
                payload.setLoginID(loginId);
                payload.setOldPassword(old_valid_password);
                break;
            case OLD_PASSWORD_NEW_PASSWORD_SAME_TC35:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                String user =  ApplicationContext.get().getDbAction().getActiveUserID();
                payload.setLoginID(user);
                break;
        }
    }

    public String validatePasswordWithLoginIN(ResetPasswordRequest payload, ResetPasswordApiLabel oldPassword) {
        String user = null;
        switch (oldPassword) {
            case INCORRECT_PASSWORD_UNBLOCK_USER_TC36:
                user = ApplicationContext.get().getDbAction().getActiveUserID();
                payload.setLoginID(user);
                payload.setRequestID(FakerDataGenerator.generateString(10));
                validEncryptedPassword = AesEncryptionSteps.encryptData(FakerDataGenerator.generateString(7));
                payload.setOldPassword(validEncryptedPassword);
                break;
            case INCORRECT_PASSWORD_BLOCK_USER_TC37:
                user = ApplicationContext.get().getDbAction().extractUserFromDBWithFailedLogin3();

                if (user == null) {
                    user = ApplicationContext.get().getDbAction().getActiveUserID();
                    int count = ApplicationContext.get().getDbAction().updateUserToFailedAttempt3(user);

                    log.info("User not found with failed login attempts, retrieved active user: {}", user);
                    log.info("Updated user failed login attempt count: {}", count);

                    ExtentReportManager.logInfoToReport("User not found with failed login attempts, retrieved active user: " + user);
                    ExtentReportManager.logInfoToReport("Updated failed login attempt count: " + count);
                } else {
                    log.info("User found with failed login attempts: {}", user);
                    ExtentReportManager.logInfoToReport("User found with failed login attempts: " + user);
                }
                // Set payload details
                String requestID = FakerDataGenerator.getRandomNumericString(6);
                validEncryptedPassword = AesEncryptionSteps.encryptData(FakerDataGenerator.generateAlphanumeric(7));

                payload.setRequestID(requestID);
                payload.setLoginID(user);
                payload.setOldPassword(validEncryptedPassword);

                // Logging the assigned values
                log.info("Generated RequestID: {}", requestID);
                log.info("Generated Encrypted Password: {}", validEncryptedPassword);

                ExtentReportManager.logInfoToReport("Generated RequestID: " + requestID);
                ExtentReportManager.logInfoToReport("Generated Encrypted Password: " + validEncryptedPassword);
                break;
        }
        return user;
    }


    public void validateFailedCount(String user)
    {
        Map<String, Object> dbValidationResult = ApplicationContext.get().getDbAction().validateFailedLoginForSpecificUser(user);
        if (dbValidationResult.isEmpty()) {
            System.out.println("No data found for the given user.");
        } else {
            Object failedLoginsObj = dbValidationResult.get("FAILED_LOGINS");
            Object userLockedIndObj = dbValidationResult.get("USER_LOCKED_IND");
            int failedLogins = failedLoginsObj != null ? Integer.parseInt(failedLoginsObj.toString()) : -1;
            String userLockedInd = userLockedIndObj != null ? (userLockedIndObj.toString()) : "UNKNOWN";
            Assert.assertEquals(failedLogins, 4, "Mismatch in expected FAILED_LOGINS value");
            Assert.assertEquals(userLockedInd, "Y", "Mismatch in expected USER_LOCKED_IND value");
        }
    }
}
