package com.gng.api.pages.csi.UpdateUsernamePage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.UpdateUsername.UpdateUsernameRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.UpdateUsername.UpdateUsernameLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;

import java.util.Map;

@Slf4j
public class UpdateUsernameApiHelper {

    private final TestContext testContext;

    public UpdateUsernameApiHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    UpdateUsernameRequest preparePayload(UpdateUsernameLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        String jsonFileName = apiLabel.equals(UpdateUsernameLabel.update_username)
                ? UpdateUsernameLabel.update_username.toString()
                : UpdateUsernameLabel.update_username_mandatory.toString();
        return BasePage.deserializeJsonToPojo(jsonFileName, UpdateUsernameRequest.class);
    }

    public void preparePayloadForTestCondition(UpdateUsernameRequest payload, UpdateUsernameLabel testCondition) {
        Map<String, Object> userInfo = null;
        switch (testCondition) {
            case TC_29__Negative__Missing_Request_ID:
                payload.setRequestID("");
                break;

            case TC_30__Negative__Invalid_Request_ID__Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(33));
                break;

            case TC_31__Negative__Duplicate_Request_ID:
                payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());
                break;

            case TC_32__Negative__Missing_Username:
                payload.setUsername("");
                break;

            case TC_33__Negative__Invalid_Username__Inactive:
                userInfo = ApplicationContext.get().getDbAction("mariadb").getInactiveUser();
                payload.setUsername(userInfo.get("user_name").toString());
                break;

            case TC_34__Negative__Invalid_Username_format__Length___Too_Short____:
                payload.setUsername(FakerDataGenerator.generateString(4));
                break;

            case TC_35__Negative__Invalid_Username_format__Length___Too_Long____:
                payload.setUsername(FakerDataGenerator.generateString(16));
                break;

            case TC_36__Negative__Invalid_Username_format__Alphanumeric:
                payload.setUsername(FakerDataGenerator.generateAlphanumericWithSpecialChars(6));
                break;

            case TC_37__Negative__Invalid_Username__Not_Found:
                payload.setUsername("NonExistentUser123");
                break;

            case TC_38__Negative__Missing_Password:
                payload.setPassword("");
                break;

            case TC_39__Negative__Invalid_Password_Format__Length___Too_Short____:
                payload.setPassword(FakerDataGenerator.generateString(7));
                break;

            case TC_40__Negative__Invalid_Password_Format__Length___Too_Long____:
                payload.setPassword(FakerDataGenerator.generateString(65));
                break;

            case TC_41__Negative__Invalid_Password_Format___Not_A_String____:
                payload.setPassword("12345678"); // numeric-like string
                break;

            case TC_42__Negative__Invalid_Password_Format__Policy_Requirement___s____:
                payload.setPassword("password"); // fails policy (no complexity)
                break;

            case TC_43__Negative__Account_username_already_exists:
                userInfo = ApplicationContext.get().getDbAction("mariadb").getActiveUsername();
                payload.setUsername(userInfo.get("user_name").toString());
                break;

            case TC_44__Negative__Password_does_not_match_username:
                userInfo = ApplicationContext.get().getDbAction("mariadb").getActiveUsername();
                payload.setUsername(userInfo.get("user_name").toString());
                payload.setPassword("WrongPassword123");
                break;

            case TC_45__Negative__Missing_customerCode:
                payload.setCustomerCode("");
                break;

            case TC_46__Negative__Invalid_customerCode_Length:
                payload.setCustomerCode(FakerDataGenerator.generateDigits(10));
                break;

            case TC_47__Negative__Invalid_customerCode_Format:
                payload.setCustomerCode(FakerDataGenerator.generateAlphanumeric(6));
                break;

            case TC_48__Negative__Invalid_customerCode:
                payload.setCustomerCode("999999999"); // not in Banner
                break;

            case TC_49__Negative__Missing_premisesCode:
                payload.setPremisesCode("");
                break;

            case TC_50__Negative__Invalid_premisesCode_Length:
                payload.setPremisesCode(FakerDataGenerator.generateDigits(8));
                break;

            case TC_51__Negative__Invalid_premisesCode_Format:
                payload.setPremisesCode(FakerDataGenerator.generateAlphanumeric(6));
                break;

            case TC_52__Negative__Invalid_premisesCode:
                payload.setPremisesCode("7777777"); // not in Banner
                break;

            case TC_53__Positive__Username_Available:
                String newUsername = FakerDataGenerator.generateAlphanumeric(8);
                payload.setUsername(newUsername);
                userInfo = ApplicationContext.get().getDbAction("mariadb").getUsername(newUsername);
                Assert.assertTrue(userInfo.isEmpty(), "Expected username to be available");
                break;

            case TC_54__Positive__Username_Active:
                userInfo = ApplicationContext.get().getDbAction("mariadb").getActiveUsername();
                payload.setUsername(userInfo.get("user_name").toString());
                break;

            case TC_55__Positive__Login_ID_Saved:
                payload.setLoginID("CSRLogin123");
                break;

            default:
                log.warn("Unhandled test condition: {}", testCondition);
                break;
        }
    }
}
