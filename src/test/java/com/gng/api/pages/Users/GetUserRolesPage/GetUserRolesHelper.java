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

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class GetUserRolesHelper {
    private final TestContext testContext;

    public GetUserRolesHelper(TestContext testContext) {
        this.testContext = testContext;
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
                payload.setRequestID("3BC00A0397B14F29A313280EE0110941");
                break;
            case LONG_REQUEST_ID_TC5:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(50));
                break;
            case NO_REQUEST_ID_TC3A:
                String jsonPayload = CommonUtil.removeFieldFromJson(payload, "requestID");
                testContext.setCustomRequestPayload(jsonPayload);
                log.info("Final request payload after removing requestID: {}", jsonPayload);
        }
    }


    public void setLoginIDBasedOnTypeTC6_TC9(GetUserRolesRequest payload, GetUserRolesApiLabel loginID) {
        switch (loginID) {
            case NULL_LOGIN_ID_AND_PASSWORD_TC6:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("");
                payload.setPassword("");
                break;
            case NULL_LOGIN_ID_TC7:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("");
                payload.setPassword(FakerDataGenerator.generatePassword(5, 10, true));
                break;
            case ALPHANUMERIC_LOGIN_ID_TC8:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(FakerDataGenerator.generateAlphanumeric(8));
                payload.setPassword(FakerDataGenerator.generatePassword(5, 10, true));
                break;
            case MAX_LENGTH_LOGIN_ID_TC9:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(FakerDataGenerator.getRandomNumericString(35));
                payload.setPassword(FakerDataGenerator.generatePassword(5, 10, true));
                break;
            default:
                payload.setLoginID(FakerDataGenerator.generateLowerCaseString(10));
        }
    }

    public void setPasswordBasedOnTypeTC10_TC12(GetUserRolesRequest payload, GetUserRolesApiLabel password) {
        switch (password) {
            case NULL_PASSWORD_TC10:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("LoginID");
                payload.setPassword(null);
                break;
            case INVALID_PASSWORD_FORMAT_NOT_ENCRYPTED_TC11:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("test10965");
                payload.setPassword("abcd");
                break;
            case INVALID_PASSWORD_FORMAT_ENCRYPTED_10_CHAR_TC12:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("test10965");
                payload.setPassword(FakerDataGenerator.generateAlphanumeric(15));
                break;
            case INVALID_PASSWORD_FORMAT_ENCRYPTED_7_CHAR_TC12_1:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("test10965");
                payload.setPassword(FakerDataGenerator.generateAlphanumeric(7));
                break;
            default:
                payload.setPassword(FakerDataGenerator.generateLowerCaseString(10));
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
                payload.setLoginID("dummy");
                payload.setPassword("gQ0diQBSDMqsYPNevswiPuZ2/W8R5B4rD3JoxskVGbc=");
                break;
            case PASSWORD_MISMATCH_WITH_LOGIN_ID_TC14:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("autotester");
                payload.setPassword("/La98bDE4x/vUobavr+1O9w3PaJHfN8jfuzJB3O1a2o=");
                break;
            case LOCKED_LOGIN_ID_TC17:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("autotester");
                payload.setPassword(FakerDataGenerator.generateAlphanumeric(10));
                break;
            default:
                payload.setPassword(FakerDataGenerator.generateLowerCaseString(10));
        }
    }
}
