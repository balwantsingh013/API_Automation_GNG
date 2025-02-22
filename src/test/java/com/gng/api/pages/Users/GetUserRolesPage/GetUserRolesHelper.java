package com.gng.api.pages.Users.GetUserRolesPage;

import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.Users.GetUserRoles.GetUserRolesRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.UsersApiSteps.GetUserRoles.GetUserRolesApiLabel;
import com.gng.api.util.CommonUtil;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

@Slf4j
public class GetUserRolesHelper {
    private final TestContext testContext;
    private final GetUserRolesApiPage apiPage; // Reference to GetUserRolesApiPage
    String activeUser;
    String encryptedPasswordMoreThan10Char = "pUsNrpKOUDBej9d5DYDxG0TBDRJhEYSz9hq05QdlDF8=";
    String encryptedPasswordLessThan7Char = "hysLdv98prkIGkXJvHhO8lDU3xG8i64KcXcgK2GZ6IQ=";
    String encrypted8CharPasswordWithSpecialChar = "FVGBjHF04+LgSXm1ULFcAhTDheMftquWq0VwfkLAEyo=";
    String validEncryptedPassword = "WRJpoTk4n5BLUNDAf4a2jzkuyjBcmgdgZ1JZdq7IEiE=";


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


    public void setRequestIDBasedOnTypeTCTC3_TC5(GetUserRolesRequest payload, GetUserRolesApiLabel requestID) {
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
        switch (loginID) {
            case WITHOUT_LOGIN_ID_AND_PASSWORD_TC6:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                String jsonPayload = CommonUtil.removeFieldsFromJson(payload, "loginID,password");
                testContext.setCustomRequestPayload(jsonPayload);
                log.info("Final request payload after removing loginID and password: {}", jsonPayload);
                break;
            case NULL_LOGIN_ID_AND_PASSWORD_TC6A:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(null);
                payload.setPassword(null);
                break;
            case WITHOUT_LOGIN_ID_TC7:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                String json = CommonUtil.removeFieldFromJson(payload, "loginID");
                testContext.setCustomRequestPayload(json);
                log.info("Final request payload after removing loginID: {}", json);
                break;
            case NULL_LOGIN_ID_TC7A:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(null);
                break;
            case LOGIN_ID_MORE_THAN_30_CHAR_TC8:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(FakerDataGenerator.generateAlphanumeric(31));
                break;
            case LOGIN_ID_WITH_SPECIAL_CHAR_TC9:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(FakerDataGenerator.generateAlphanumericWithSpecialChars(7));
                break;
        }
    }

    public void setPasswordBasedOnTypeTC10_TC12(GetUserRolesRequest payload, GetUserRolesApiLabel password) {
        switch (password) {
            case WITHOUT_PASSWORD_FIELD_TC10:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                String json = CommonUtil.removeFieldFromJson(payload, "password");
                testContext.setCustomRequestPayload(json);
                log.info("Final request payload after removing password: {}", json);
                break;
            case NULL_PASSWORD_TC10A:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setPassword(null);
                break;
            case UNENCRYPTED_PASSWORD_TC11:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setPassword(FakerDataGenerator.generateString(4));
                break;
            case ENCRYPTED_PASSWORD_MORE_THAN_10_CHAR_TC12:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setPassword(encryptedPasswordMoreThan10Char);
                break;
            case ENCRYPTED_PASSWORD_LESS_THAN_7_CHAR_TC12A:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setPassword(encryptedPasswordLessThan7Char);
                break;
            case ENCRYPTED_PASSWORD_WITH_8_CHAR_WITH_SPECIAL_CHAR_TC12B:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setPassword(encrypted8CharPasswordWithSpecialChar);
                break;
        }
    }

    public void validatePasswordNotMatchLoginIDInDB() {
        ApplicationContext.get().getDbAction().theInvalidPasswordMatchUpdateQuery();
    }

    public void validatePasswordNotMatchLoginID(GetUserRolesRequest payload) {
        payload.setRequestID(FakerDataGenerator.getRandomNumericString(9));
        payload.setLoginID("autotester");
        payload.setPassword("/La98bDE4x/vUobavr+1O9w3PaJHfN8jfuzJB3O1a2o=");

    }

    public void validateFailedCountAs4() {

        ApplicationContext.get().getDbAction().togetthefailedcountsandvalidateshouldbe4();
    }
        public void rollBackQuery() {
                ApplicationContext.get().getDbAction().rollBackChanges();
                System.out.println("Rollback query executed successfully.");
            }

    public void passwordNotMatchValue(GetUserRolesRequest payload) {
        payload.setRequestID(FakerDataGenerator.getRandomNumericString(6));
        payload.setLoginID("autotester");
        payload.setPassword("/La98bDE4x/vUobavr+1O9w3PaJHfN8jfuzJB3O1a2o=");
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
        switch (password) {
            case INVALID_LOGIN_ID_TC13:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(FakerDataGenerator.generateString(5));
                payload.setPassword(validEncryptedPassword);
                break;
            case PASSWORD_MISMATCH_WITH_LOGIN_ID_TC14:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                activeUser= ApplicationContext.get().getDbAction().getActiveUserID();
                payload.setLoginID(activeUser);
                payload.setPassword(validEncryptedPassword);
                break;
        }
    }

    public void validateDatabaseForMismatchCase()
    {
        String dbValidationResult = ApplicationContext.get().getDbAction().validateFailedLoginForSpecificUser(activeUser);
        Assert.assertEquals(dbValidationResult, "2");
    }
}
