package com.gng.api.pages.csi.UpdatePasswordPage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.UpdatePassword.UpdatePasswordRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.UpdatePassword.UpdatePasswordLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;

import java.util.Map;

@Slf4j
public class UpdatePasswordApiHelper {

    private final TestContext testContext;

    public UpdatePasswordApiHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    UpdatePasswordRequest preparePayload(UpdatePasswordLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        String jsonFileName = apiLabel.equals(UpdatePasswordLabel.update_password)
                ? UpdatePasswordLabel.update_password.toString()
                : UpdatePasswordLabel.update_password_mandatory.toString();
        return BasePage.deserializeJsonToPojo(jsonFileName, UpdatePasswordRequest.class);
    }

    public void preparePayloadForTestCondition(UpdatePasswordRequest payload, UpdatePasswordLabel testCondition) {
        Map<String, Object> userNames = null;

        switch (testCondition) {
            case TC_14__Negative__Missing_Request_ID:
                payload.setRequestID("");
                break;

            case TC_15__Negative__Invalid_Request_ID__Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(33));
                break;

            case TC_16__Negative__Duplicate_Request_ID:
                payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());
                break;

            case TC_17__Negative__Missing_Username:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername("");
                break;

            case TC_18__Negative__Invalid_Username_format__Length___Too_Short____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(FakerDataGenerator.generateString(4));
                break;

            case TC_19__Negative__Invalid_Username_format__Length___Too_Long____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(FakerDataGenerator.generateString(16));
                break;

            case TC_20__Negative__Invalid_Username_format__Alphanumeric:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(FakerDataGenerator.generateAlphanumericWithSpecialChars(6));
                break;

            case TC_21__Negative__Invalid_Username__Not_Found:
                String username = FakerDataGenerator.generateAlphanumeric(6);
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(username);
                userNames = ApplicationContext.get().getDbAction("mariadb").getUsername(username);
                Assert.assertTrue(userNames.isEmpty(), "Expected userNames map to be empty");
                break;

            case TC_22__Negative__Invalid_Username__Inactive:
                userNames = ApplicationContext.get().getDbAction("mariadb").getInactiveUser();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(userNames.get("user_name").toString());
                break;

            case TC_23__Negative__Missing_Password:
                userNames = ApplicationContext.get().getDbAction("mariadb").getActiveUsername();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(userNames.get("user_name").toString());
                payload.setPassword("");
                break;

            case TC_24__Negative__Invalid_Password_Format__Length___Too_Short____:
                userNames = ApplicationContext.get().getDbAction("mariadb").getActiveUsername();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(userNames.get("user_name").toString());
                payload.setPassword(FakerDataGenerator.generateString(7));
                break;

            case TC_25__Negative__Invalid_Password_Format__Length___Too_Long____:
                userNames = ApplicationContext.get().getDbAction("mariadb").getActiveUsername();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(userNames.get("user_name").toString());
                payload.setPassword(FakerDataGenerator.generateString(65));
                break;

            case TC_26__Negative__Invalid_Password__Reused_Password:
                userNames = ApplicationContext.get().getDbAction("mariadb").getActiveUsername();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(userNames.get("user_name").toString());
                //payload.setPassword(GlobalEnums.InvalidValues.REUSED_PASSWORD.getValue());
                break;

            case TC_27__Positive__Password__Updated:
                userNames = ApplicationContext.get().getDbAction("mariadb").getActiveUsernameFromOtherTable();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(userNames.get("user_name").toString());
                payload.setPassword(FakerDataGenerator.generateAlphanumeric(12)); // valid new password
                break;

            case TC_28__Positive__LoginID_Saved:
                userNames = ApplicationContext.get().getDbAction("mariadb").getActiveUsernameFromOtherTable();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(userNames.get("user_name").toString());
                payload.setPassword(FakerDataGenerator.generateAlphanumeric(12));
                payload.setLoginID("CSRLogin123");
                break;

            default:
                log.warn("Unhandled test condition: {}", testCondition);
                break;
        }
    }
}
