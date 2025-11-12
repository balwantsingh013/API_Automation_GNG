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

    public void preparePayloadForNegativeTestCondition(ValidateUsernameRequest payload, ValidateUserNameLabel testCondition) {
        Map<String, Object> userNames= null;
        switch (testCondition) {
            case Missing_request_id_TC_1:
                payload.setRequestID("");
                break;

            case Length_of_request_id_larger_than_32_TC_2:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(33));
                break;

            case Duplicate_request_id_TC_3:
                payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());
                break;

            case Missing_username_TC_4:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername("");
                break;

            case Length_of_username_smaller_than_5_TC_5:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(FakerDataGenerator.generateString(4));
                break;

            case Length_of_username_greater_than_15_TC_6:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(FakerDataGenerator.generateString(16));
                break;

            case Username_not_alphanumeric_TC_7:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(FakerDataGenerator.generateAlphanumericWithSpecialChars(6));
                break;

            case Username_does_not_exist_in_mariadb_TC_9:
                String username=FakerDataGenerator.generateAlphanumeric(6);
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(username);
                userNames = ApplicationContext.get().getDbAction("mariadb").getUsername(username);
                Assert.assertTrue(userNames.isEmpty(), "Expected userNames map to be empty");
                break;

            case Username_exists_in_mariadb_AVAILABLE_TC_8:
                userNames = ApplicationContext.get().getDbAction("mariadb").getActiveUsername();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(userNames.get("user_name").toString());
                break;

            case Username_exists_in_mariadb__ACTIVE_TC_10:
                userNames = ApplicationContext.get().getDbAction("mariadb").getActiveUsernameFromOtherTable();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(userNames.get("user_name").toString());
                break;

            case Inactive_username_exists_in_mariadb_TC_12:
                userNames = ApplicationContext.get().getDbAction("mariadb").getInactiveUser();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(userNames.get("user_name").toString());
                break;

            case Username_exists_in_custadv_pending_registrations_table_TC_11:
                userNames = ApplicationContext.get().getDbAction("mariadb").getActiveUsernameFromOtherTable2();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(userNames.get("user_name").toString());
                break;

            default:
                // Handle unknown test condition
                break;
        }

    }

}

