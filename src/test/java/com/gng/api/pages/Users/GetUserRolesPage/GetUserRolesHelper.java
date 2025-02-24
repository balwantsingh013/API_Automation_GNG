package com.gng.api.pages.Users.GetUserRolesPage;

import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.Users.GetUserRoles.GetUserRolesRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.report.ExtentReportManager;
import com.gng.api.steps.AesEncryption.AesEncryptionSteps;
import com.gng.api.steps.UsersApiSteps.GetUserRoles.GetUserRolesApiLabel;
import com.gng.api.util.CommonUtil;
import com.gng.api.util.FakerDataGenerator;
import io.qameta.allure.Allure;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

import static com.gng.api.steps.BaseSteps.*;

@Slf4j
public class GetUserRolesHelper {
    private final TestContext testContext;
    private final GetUserRolesApiPage apiPage; // Reference to GetUserRolesApiPage
    String user;

    public GetUserRolesHelper(TestContext testContext, GetUserRolesApiPage apiPage) {
        this.testContext = testContext;
        this.apiPage = apiPage; // Store reference
    }


    GetUserRolesRequest preparePayload(GetUserRolesApiLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        String jsonFileName = apiLabel.equals(GetUserRolesApiLabel.get_user_roles)
                ? GetUserRolesApiLabel.get_user_roles.toString()
                : GetUserRolesApiLabel.get_user_roles_mandatory.toString();
        return BasePage.deserializeJsonToPojo(jsonFileName, GetUserRolesRequest.class);
    }

    public void validate_UZBPSTO_OBJECT_Value() {
        log.info("Validating UZBPSTO_OBJECT value from DB");
        ExtentReportManager.logInfoToReport("Validating UZBPSTO_OBJECT value from DB");
        Allure.step("Validating UZBPSTO_OBJECT value from DB");

        String objectValue = ApplicationContext.get().getDbAction().select_UZBPSTO_OBJECT_Value();
        log.info("Expected: SPK_WEB_API, Actual: {}", objectValue);
        Assert.assertEquals(objectValue, "SPK_WEB_API");

        ExtentReportManager.logInfoToReport("UZBPSTO_OBJECT validation successful!");
        Allure.step("UZBPSTO_OBJECT validation successful!");
    }

    public void validate_UZRPSTO_PARM_NAME_Value() {
        log.info("Validating UZRPSTO_PARM_NAME value from DB");
        ExtentReportManager.logInfoToReport("Validating UZRPSTO_PARM_NAME value from DB");
        Allure.step("Validating UZRPSTO_PARM_NAME value from DB");

        Map<String, Object> result = ApplicationContext.get().getDbAction().select_UZRPSTO_PARM_NAME_Value();
        Object obj = result.get("UZRPSTO_OBJECT");
        Object paramValue = result.get("UZRPSTO_PARM_VALUE");
        String failedLogins = obj != null ? obj.toString() : "UNKNOWN";
        int param = paramValue != null ? Integer.parseInt(paramValue.toString()) : -1;

        log.info("Expected: SPK_WEB_API, Actual: {}", failedLogins);
        log.info("Expected: 4, Actual: {}", param);

        Assert.assertEquals(failedLogins, "SPK_WEB_API");
        Assert.assertEquals(param, 4);

        ExtentReportManager.logInfoToReport("UZRPSTO_PARM_NAME validation successful!");
        Allure.step("UZRPSTO_PARM_NAME validation successful!");
    }


    public void setRequestIDBasedOnTypeTCTC3_TC5(GetUserRolesRequest payload, GetUserRolesApiLabel requestID) {
        user = ApplicationContext.get().getDbAction().getActiveUserID();
        payload.setLoginID(user);

        switch (requestID) {
            case NULL_REQUEST_ID_TC3:
                payload.setRequestID(null);
                break;
            case DUPLICATE_REQUEST_ID_TC4:
                payload.setRequestID(apiPage.getOrGenerateRequestID());
                break;
            case LONG_REQUEST_ID_TC5:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(33));
                break;
            case NO_REQUEST_ID_TC3A:
                String jsonPayload = CommonUtil.removeFieldFromJson(payload, "requestID");
                testContext.setCustomRequestPayload(jsonPayload);
                log.info("Final request payload after removing requestID: {}", jsonPayload);
        }
    }



    public void setLoginIDBasedOnTypeTC6_TC9(GetUserRolesRequest payload, GetUserRolesApiLabel loginID) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        switch (loginID) {
            case WITHOUT_LOGIN_ID_AND_PASSWORD_TC6:
                String jsonPayload = CommonUtil.removeFieldsFromJson(payload, "loginID,password");
                testContext.setCustomRequestPayload(jsonPayload);
                log.info("Final request payload after removing loginID and password: {}", jsonPayload);
                break;
            case NULL_LOGIN_ID_AND_PASSWORD_TC6A:
                payload.setLoginID(null);
                payload.setPassword(null);
                break;
            case WITHOUT_LOGIN_ID_TC7:
                String json = CommonUtil.removeFieldFromJson(payload, "loginID");
                testContext.setCustomRequestPayload(json);
                log.info("Final request payload after removing loginID: {}", json);
                break;
            case NULL_LOGIN_ID_TC7A:
                payload.setLoginID(null);
                break;
            case LOGIN_ID_MORE_THAN_30_CHAR_TC8:
                payload.setLoginID(FakerDataGenerator.generateAlphanumeric(31));
                break;
            case LOGIN_ID_WITH_SPECIAL_CHAR_TC9:
                payload.setLoginID(FakerDataGenerator.generateAlphanumericWithSpecialChars(7));
                break;
        }
    }

    public void setPasswordBasedOnTypeTC10_TC12(GetUserRolesRequest payload, GetUserRolesApiLabel password) {
        user = ApplicationContext.get().getDbAction().getActiveUserID();
        payload.setLoginID(user);
        payload.setRequestID(FakerDataGenerator.generateString(10));

        switch (password) {
            case WITHOUT_PASSWORD_FIELD_TC10:
                String json = CommonUtil.removeFieldFromJson(payload, "password");
                testContext.setCustomRequestPayload(json);
                log.info("Final request payload after removing password: {}", json);
                break;
            case NULL_PASSWORD_TC10A:
                payload.setPassword(null);
                break;
            case UNENCRYPTED_PASSWORD_TC11:
                payload.setPassword(FakerDataGenerator.generateString(4));
                break;
            case ENCRYPTED_PASSWORD_MORE_THAN_10_CHAR_TC12:
                String encryptedMore12Char = AesEncryptionSteps.encryptData(FakerDataGenerator.generateString(12));
                log.info(encryptedMore12Char);
                ExtentReportManager.logInfoToReport(encryptedMore12Char);
                payload.setPassword(encryptedMore12Char);
                break;
            case ENCRYPTED_PASSWORD_LESS_THAN_7_CHAR_TC12A:
                String encryptedLess7Char = AesEncryptionSteps.encryptData(FakerDataGenerator.generateString(5));
                log.info(encryptedLess7Char);
                ExtentReportManager.logInfoToReport(encryptedLess7Char);
                payload.setPassword(encryptedLess7Char);
                break;
            case ENCRYPTED_PASSWORD_WITH_8_CHAR_WITH_SPECIAL_CHAR_TC12B:
                String encryptedSpecial = AesEncryptionSteps.encryptData(FakerDataGenerator.generateAlphanumericWithSpecialChars(8));
                log.info(encryptedSpecial);
                ExtentReportManager.logInfoToReport(encryptedSpecial);
                payload.setPassword(encryptedSpecial);
                break;
        }
    }

    public void extractTheUserFromDB(GetUserRolesRequest payload) {
        user = ApplicationContext.get().getDbAction().extractUserFromDBWithFailedLogin3();
        if(user==null)
        {
          user =  ApplicationContext.get().getDbAction().getActiveUserID();
          int count= ApplicationContext.get().getDbAction().updateUserToFailedAttempt3(user);
          System.out.println(count);
          payload.setRequestID(FakerDataGenerator.getRandomNumericString(6));
          payload.setLoginID(user);
          String validEncryptedPassword = AesEncryptionSteps.encryptData(FakerDataGenerator.generateString(7));
          payload.setPassword(validEncryptedPassword);
        }
        else
        {
            payload.setRequestID(FakerDataGenerator.getRandomNumericString(6));
            payload.setLoginID(user);
            String validEncryptedPassword = AesEncryptionSteps.encryptData(FakerDataGenerator.generateString(7));
            payload.setPassword(validEncryptedPassword);
        }
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
            Assert.assertEquals(failedLogins, 4, "Mismatch in expected FAILED_LOGINS value");
            Assert.assertEquals(userLockedInd, "Y", "Mismatch in expected USER_LOCKED_IND value");
        }
    }

    public void validatePasswordNotMatchLoginID(GetUserRolesRequest payload) {
        payload.setRequestID(FakerDataGenerator.getRandomNumericString(9));
        payload.setLoginID("autotester");
        payload.setPassword("/La98bDE4x/vUobavr+1O9w3PaJHfN8jfuzJB3O1a2o=");

    }


        public void rollBackQuery() {
                ApplicationContext.get().getDbAction().rollBackChanges();
                System.out.println("Rollback query executed successfully.");
            }

    public void passwordNotMatchValue(GetUserRolesRequest payload) {

    }


    public void validatePasswordCredentials(GetUserRolesRequest payload) {
        payload.setRequestID(FakerDataGenerator.getRandomNumericString(6));
        payload.setLoginID("autotester");
        payload.setPassword("gQ0diQBSDMqsYPNevswiPuZ2/W8R5B4rD3JoxskVGbc=");

    }


    public void validatePasswordExpiredInDB() {

        ApplicationContext.get().getDbAction().PasswordExpiredUpdateQuery();
    }


    public void rollbackDatabaseQuery() {
        ApplicationContext.get().getDbAction().rollBackQueryForPasswordExpired();
        System.out.println("Rollback query executed successfully.");

    }


    public void validateLockedOutLoginIDInDBUpdateQuery() {

        ApplicationContext.get().getDbAction().updateUserLockStatusQuery();
    }

    public void validateLockedOutLoginIDInDBRollBackQuery() {

                ApplicationContext.get().getDbAction().rollBackUserLockStatusQuery();
                System.out.println("Rollback query executed successfully.");
            }

    public void updateFailedLoginsQuery() {

        ApplicationContext.get().getDbAction().updateTheFailedLoginQuery();
    }

    public void validateRolesCount() {
        List<Map<String, Object>> dbRoleCountResult = ApplicationContext.get().getDbAction().RoleCountQuery();
    }


    public void validateFailedCountUserRoles() {
            List<Map<String, Object>> failedLoginResult = ApplicationContext.get().getDbAction().FailedCountUserRoleQuery();

        }
    public void validateFailedCountUserRollback() {

        ApplicationContext.get().getDbAction().rollbackCountUserRoleQuery();
                System.out.println("Rollback query executed successfully.");


    }

    public void setTestConditionBasedOnTypeTC13_TC14(GetUserRolesRequest payload, GetUserRolesApiLabel password) {
        payload.setRequestID(FakerDataGenerator.generateString(10));

        switch (password) {
            case INVALID_LOGIN_ID_TC13:
                payload.setLoginID(FakerDataGenerator.generateString(5));
                break;
            case PASSWORD_MISMATCH_WITH_LOGIN_ID_TC14:
                user= ApplicationContext.get().getDbAction().getActiveUserID();
                payload.setLoginID(user);
                break;
        }
    }

    public void validateDatabaseForMismatchCase()
    {
        Map<String, Object> dbValidationResult = ApplicationContext.get().getDbAction().validateFailedLoginForSpecificUser(user);
        Object failedLoginObj= dbValidationResult.get("FAILED_LOGINS");
        int failedLogins = failedLoginObj != null ? Integer.parseInt(failedLoginObj.toString()) : -1;
        Assert.assertEquals(failedLogins, 2);
    }
}
