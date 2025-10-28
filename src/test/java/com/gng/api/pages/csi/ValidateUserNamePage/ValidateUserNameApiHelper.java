package com.gng.api.pages.csi.ValidateUserNamePage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.ValidateUsername.ValidateUsernameRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.ValidateUserName.ValidateUserNameLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;

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
        switch (testCondition) {
            case Missing_request_id_TC_1:
                payload.setRequestID("");
                break;

            case Length_of_request_id_larger_than_32_TC_2:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(33));
                break;

            case Invalid_request_id_format_TC_3:
                payload.setRequestID(FakerDataGenerator.generateAlphanumericWithSpecialChars(3));
                break;

            case Duplicate_request_id_TC_4:
                payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());
                break;

            case Missing_username_TC_5:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername("");
                break;

            case Length_of_username_smaller_than_5_TC_6:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(FakerDataGenerator.generateString(4));
                break;

            case Length_of_username_greater_than_15_TC_7:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(FakerDataGenerator.generateString(16));
                break;

            case Username_not_alphanumeric_TC_8:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(FakerDataGenerator.generateAlphanumericWithSpecialChars(6));
                break;

            default:
                // Handle unknown test condition
                break;
        }

    }

}

