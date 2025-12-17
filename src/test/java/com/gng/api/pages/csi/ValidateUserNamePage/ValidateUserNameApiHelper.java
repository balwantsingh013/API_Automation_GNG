package com.gng.api.pages.csi.ValidateUserNamePage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.ValidateUsername.ValidateUsernameRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.ValidateUserName.ValidateUserNameLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;

import java.util.Map;

@Slf4j
public class ValidateUserNameApiHelper {

    private final TestContext testContext;

    public ValidateUserNameApiHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    ValidateUsernameRequest preparePayload(ValidateUserNameLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        String jsonFileName = apiLabel.equals(ValidateUserNameLabel.validate_username)
                ? ValidateUserNameLabel.validate_username.toString()
                : ValidateUserNameLabel.validate_username_mandatory.toString();
        return BasePage.deserializeJsonToPojo(jsonFileName, ValidateUsernameRequest.class);
    }

    public void preparePayloadForTestCondition(ValidateUsernameRequest payload, ValidateUserNameLabel testCondition) {
        Map<String, Object> userNames = null;
        switch (testCondition) {
            case TC_1__Negative__Missing_Request_ID:
                payload.setRequestID("");
                break;

            case TC_2__Negative__Invalid_Request_ID__Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(33));
                break;

            case TC_3__Negative__Duplicate_Request_ID:
                payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());
                break;

            case TC_4__Negative__Missing_Username:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername("");
                break;

            case TC_5__Negative__Invalid_Username_format__Length___Too_Short____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(FakerDataGenerator.generateString(4));
                break;

            case TC_6__Negative__Invalid_Username_format__Length___Too_Long____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(FakerDataGenerator.generateString(16));
                break;

            case TC_7__Negative__Invalid_Username_format__Alphanumeric:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(FakerDataGenerator.generateAlphanumericWithSpecialChars(6));
                break;

            case TC_9__Positive__Username_Available:
                String username = FakerDataGenerator.generateAlphanumeric(6);
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(username);
                userNames = ApplicationContext.get().getDbAction("mariadb").getUsername(username);
                Assert.assertTrue(userNames.isEmpty(), "Expected userNames map to be empty");
                break;

            case TC_8__Positive__Username_Available:
                userNames = ApplicationContext.get().getDbAction("mariadb").getActiveUsername();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(userNames.get("user_name").toString());
                break;

            case TC_10__Positive__Username_Active__users_table:
                userNames = ApplicationContext.get().getDbAction("mariadb").getActiveUsernameFromOtherTable();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(userNames.get("user_name").toString());
                break;

            case TC_11__Positive__Username_Active__custadv_pending_registrations_table:
                userNames = ApplicationContext.get().getDbAction("mariadb").getActiveUsernameFromOtherTable2();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(userNames.get("user_name").toString());
                break;

            case TC_12__Positive__Username_Inactive:
                userNames = ApplicationContext.get().getDbAction("mariadb").getInactiveUser();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(userNames.get("user_name").toString());
                break;

            case TC_13__Positive__LoginID_Saved:
                payload.setLoginID("CSRLogin123");
                userNames = ApplicationContext.get().getDbAction("mariadb").getActiveUsernameFromOtherTable();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(userNames.get("user_name").toString());
                break;

            default:
                log.warn("Unhandled test condition: {}", testCondition);
                break;
        }
    }
}
