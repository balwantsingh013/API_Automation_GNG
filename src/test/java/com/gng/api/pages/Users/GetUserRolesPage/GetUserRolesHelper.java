package com.gng.api.pages.Users.GetUserRolesPage;

import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.Users.GetUserRoles.GetUserRolesRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.UsersApiSteps.GetUserRoles.GetUserRolesApiLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;

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
                payload.setLoginID("sys");
                payload.setPassword(FakerDataGenerator.generatePassword(5, 10, true));
                break;
            case DUPLICATE_REQUEST_ID_TC4:
                payload.setRequestID("3BC00A0397B14F29A313280EE0110941");
                break;
            case LONG_REQUEST_ID_TC5:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(35));
                break;

            default:
                payload.setRequestID(FakerDataGenerator.generateString(10));
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

    public void validatePasswordNotMatchLoginIDInDB(GetUserRolesRequest payload) {
        try {
            String expectedValue = passwordNotMatchValue();
            List<Map<String, Object>> passwordNotMatchValue =
                    ApplicationContext.get().getDbAction().theInvalidPasswordMatchUpdateQuery();
            Assert.assertEquals(passwordNotMatchValue.size(), 1, "Password does not match the login ID.");

            payload.setPassword("/La98bDE4x/vUobavr+1O9w3PaJHfN8jfuzJB3O1a2o=");

            String expectedValue1 = failedLoginCount();
            List<Map<String, Object>> failedLoginCount =
                    ApplicationContext.get().getDbAction().togetthefailedcountsandvalidateshouldbe4();
            Assert.assertEquals(failedLoginCount.size(), 1, "Failed login count is incorrect.");

        } catch (Exception e) {
            System.err.println(STR."An error occurred during validation: \{e.getMessage()}");
            e.printStackTrace();
        } finally {
            try {
                ApplicationContext.get().getDbAction().rollBackChanges();
                System.out.println("Rollback query executed successfully.");
            } catch (Exception e) {
                System.err.println(STR."An unexpected error occurred during rollback: \{e.getMessage()}");
                e.printStackTrace();
            }
        }
    }

    public String passwordNotMatchValue() {
        return "/La98bDE4x/vUobavr+1O9w3PaJHfN8jfuzJB3O1a2o=";
    }

    public String failedLoginCount() {
        return "4";
    }

    public void validatePasswordExpiredInDB() {
        try {
            ApplicationContext.get().getDbAction().PasswordExpiredUpdateQuery();

            List<Map<String, Object>> result = ApplicationContext.get().getDbAction().PasswordExpiredCheckQuery();

            boolean isPasswordExpired = result.get(0).get("is_expired").equals("Y");
            Assert.assertTrue(isPasswordExpired, "Password should be marked as expired in the database.");

            List<Map<String, Object>> failedLoginResult = ApplicationContext.get().getDbAction().failedLoginCountQuery();
            int failedLoginCount = Integer.parseInt(failedLoginResult.get(0).get("failed_logins").toString());

            Assert.assertEquals(failedLoginCount, 0, "Failed login count should be reset to 0.");

        } catch (Exception e) {
            System.err.println(STR."An error occurred during validation: \{e.getMessage()}");
            e.printStackTrace();
        } finally {
            try {
                ApplicationContext.get().getDbAction().rollBackQueryForPasswordExpired();
                System.out.println("Rollback query executed successfully.");
            } catch (Exception e) {
                System.err.println(STR."An unexpected error occurred during rollback: \{e.getMessage()}");
                e.printStackTrace();
            }
        }
    }
    public void validateLockedOutLoginIDInDB(GetUserRolesRequest payload) {
        try {
            ApplicationContext.get().getDbAction().updateUserLockStatusQuery();

            List<Map<String, Object>> lockStatusResult = ApplicationContext.get().getDbAction().checkUserLockStatusQuery();
            boolean isUserLocked = lockStatusResult.get(0).get("user_locked_ind").equals("Y");
            Assert.assertTrue(isUserLocked, "User should be locked in the database.");

            payload.setPassword("gQ0diQBSDMqsYPNevswiPuZ2/W8R5B4rD3JoxskVGbc=");

            List<Map<String, Object>> failedLoginResult = ApplicationContext.get().getDbAction().failedUserLockCountQuery();
            int failedLoginCount = Integer.parseInt(failedLoginResult.get(0).get("failed_logins").toString());
            Assert.assertEquals(failedLoginCount, 4, "Failed login count should be 4.");

        } catch (Exception e) {
            System.err.println(STR."An error occurred during validation: \{e.getMessage()}");
            e.printStackTrace();
        } finally {
            try {
                ApplicationContext.get().getDbAction().rollBackUserLockStatusQuery();
                System.out.println("Rollback query executed successfully.");
            } catch (Exception e) {
                System.err.println(STR."An unexpected error occurred during rollback: \{e.getMessage()}");
                e.printStackTrace();
            }
        }

    }

//    public void validateSuccessfulResponse(GetUserRolesRequest payload) {
//        try {
//            ApplicationContext.get().getDbAction().updateTheFailedLoginsQuery();
//
//
//            payload.setLoginID("autotester");
//            payload.setPassword("gQ0diQBSDMqsYPNevswiPuZ2/W8R5B4rD3JoxskVGbc=");
//
//            List<Map<String, Object>> roles = response.getRoles();
//
//            Assert.assertNotNull(roles, "Roles array should not be null.");
//            Assert.assertTrue(roles.size() > 0, "Roles array should have at least one role.");
//
//            List<Map<String, Object>> dbRoleCountResult = ApplicationContext.get().getDbAction().RoleCountQuery();
//            int dbRoleCount = Integer.parseInt(dbRoleCountResult.get(0).get("count(*)").toString());
//            int apiRoleCount = roles.size();
//
//            Assert.assertEquals(apiRoleCount, dbRoleCount, "Role count from API should match the DB count.");
//
//
//            List<Map<String, Object>> failedLoginResult = ApplicationContext.get().getDbAction().FailedCountUserRoleQuery();
//            int failedLoginCount = Integer.parseInt(failedLoginResult.get(0).get("failed_logins").toString());
//            Assert.assertEquals(failedLoginCount, 0, "Failed login count should be 0 after the API call.");
//
//        } catch (Exception e) {
//            System.err.println(STR."An error occurred during validation: \{e.getMessage()}");
//            e.printStackTrace();
//        } finally {
//            try {
//                ApplicationContext.get().getDbAction().rollbackCountUserRoleQuery();
//                System.out.println("Rollback query executed successfully.");
//            } catch (Exception e) {
//                System.err.println(STR."An unexpected error occurred during rollback: \{e.getMessage()}");
//                e.printStackTrace();
//            }
//        }
//    }


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
