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

    public void preparePayloadForNegativeTestCondition(UpdatePasswordRequest payload, UpdatePasswordLabel testCondition) {
        Map<String, Object> userNames = null;

        switch (testCondition) {
            case Missing_Request_id_TC_1:
                payload.setRequestID("");
                break;

            case Length_of_Request_id_larger_than_32_TC_2:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(33));
                break;

            case Duplicate_Request_id_TC_3:
                payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());
                break;

            case Missing_Username_TC_4:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername("");
                break;

            case Length_of_Username_smaller_than_5_TC_5:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(FakerDataGenerator.generateString(4));
                break;

            case Length_of_Username_greater_than_15_TC_6:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(FakerDataGenerator.generateString(16));
                break;

            case Username_not_Alphanumeric_TC_7:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(FakerDataGenerator.generateAlphanumericWithSpecialChars(6));
                break;

            case Username_not_found_TC_8:
                String username = FakerDataGenerator.generateAlphanumeric(6);
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(username);
                userNames = ApplicationContext.get().getDbAction("mariadb").getUsername(username);
                Assert.assertTrue(userNames.isEmpty(), "Expected userNames map to be empty");
                break;

            case Inactive_username_TC_9:
                userNames = ApplicationContext.get().getDbAction("mariadb").getInactiveUser();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(userNames.get("user_name").toString());
                break;

            case Missing_password_TC_10:
                userNames = ApplicationContext.get().getDbAction("mariadb").getActiveUsername();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(userNames.get("user_name").toString());
                payload.setPassword("");
                break;

            case Password_shorter_than_8_characters_TC_11:
                userNames = ApplicationContext.get().getDbAction("mariadb").getActiveUsername();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(userNames.get("user_name").toString());
                payload.setPassword(FakerDataGenerator.generateString(7));
                break;

            case Password_longer_than_64_characters_TC_12:
                userNames = ApplicationContext.get().getDbAction("mariadb").getActiveUsername();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(userNames.get("user_name").toString());
                payload.setPassword(FakerDataGenerator.generateString(65));
                break;

            case Password_not_a_string_TC_13:
                userNames = ApplicationContext.get().getDbAction("mariadb").getActiveUsernameFromOtherTable();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(userNames.get("user_name").toString());
                payload.setPassword(FakerDataGenerator.generateDigits(8));
                break;

            case Password_does_not_conform_to_policy_TC_14:
                userNames = ApplicationContext.get().getDbAction("mariadb").getActiveUsernameFromOtherTable();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(userNames.get("user_name").toString());
                payload.setPassword("SHORT"); // Weak password
                break;

            case Password_matches_current_password_TC_15:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                break;

            case Valid_username_and_password_TC_16:
                userNames = ApplicationContext.get().getDbAction("mariadb").getActiveUsernameFromOtherTable();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(userNames.get("user_name").toString());
                payload.setPassword(FakerDataGenerator.generateAlphanumeric(9)); // Weak password
                break;

            default:
                // Handle unknown test condition
                break;
        }
    }

}
