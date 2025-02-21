package com.gng.api.pages.Users.ResetPassword;

import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.pojo.Users.ResetPassword.ResetPasswordRequest;
import com.gng.api.steps.UsersApiSteps.ResetPassword.ResetPasswordApiLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

@Slf4j
public class ResetPasswordHelper {
    private final TestContext testContext;

    public ResetPasswordHelper(TestContext testContext) {
        this.testContext = testContext;
    }


    ResetPasswordRequest preparePayload(ResetPasswordApiLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        String jsonFileName = apiLabel.equals(ResetPasswordApiLabel.reset_password)
                ? ResetPasswordApiLabel.reset_password.toString()
                : ResetPasswordApiLabel.reset_password_mandatory.toString();
        return BasePage.deserializeJsonToPojo(jsonFileName, ResetPasswordRequest.class);
    }
    public void validatePasswordExpireDaysEntryInDB()
    {
        String expectedValue = passwordExpireDayValue();
        List<Map<String, Object>> passwordExpireDayValue = ApplicationContext.get().getDbAction().getPasswordExpireDaysValue();


        Assert.assertEquals(passwordExpireDayValue.size(), 1, "Exactly one entry should exist in the table.");

        Map<String, Object> entry = passwordExpireDayValue.get(0);

        String parmName = entry.get("UZRPSTO_PARM_NAME").toString();;
        String objectName =  entry.get("UZRPSTO_OBJECT").toString();;
        String value = entry.get("UZRPSTO_PARM_VALUE").toString();

        Assert.assertEquals(parmName, "PASSWORD_EXPIRE_DAYS", "Parameter name does not match.");
        Assert.assertEquals(objectName, "SPK_WEB_API", "Object name does not match.");
        Assert.assertEquals(value, expectedValue, "Value does not match the expected value.");
    }
    public String passwordExpireDayValue() {
        return "45";
    }


    public void setRequestIDBasedOnTypeTC21_TC23(ResetPasswordRequest payload, ResetPasswordApiLabel requestID) {
        switch (requestID) {
            case NULL_REQUEST_ID_TC21:
                payload.setRequestID(null);
                payload.setLoginID("test10965");
                payload.setOldPassword(FakerDataGenerator.generatePassword(5, 10, true));
                payload.setNewPassword(FakerDataGenerator.generatePassword(5, 10, true));
                break;
            case DUPLICATE_REQUEST_ID_TC22:
                payload.setRequestID("3BC00A0397B14F29A313280EE0110941");
                break;
            case LONG_REQUEST_ID_TC23:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(35));
                break;
            default:
                payload.setRequestID(FakerDataGenerator.generateString(10));
        }
    }

    public void setLoginIDBasedOnTypeTC24_TC28(ResetPasswordRequest payload, ResetPasswordApiLabel loginID) {
        switch (loginID) {
            case NULL_LOGIN_ID_AND_OLD_PASSWORD_TC24:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(null);
                payload.setOldPassword(null);
                payload.setNewPassword(FakerDataGenerator.generatePassword(5, 10, true));
                break;
            case NULL_LOGIN_ID_TC25:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(null);
                payload.setOldPassword(FakerDataGenerator.generatePassword(5, 10, true));
                payload.setNewPassword(FakerDataGenerator.generatePassword(5, 10, true));
                break;
            case MAX_LENGTH_LOGIN_ID_TC26:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(FakerDataGenerator.getRandomNumericString(35));
                payload.setOldPassword(FakerDataGenerator.generatePassword(5, 10, true));
                payload.setNewPassword(FakerDataGenerator.generatePassword(5, 10, true));
                break;
            case ALPHANUMERIC_LOGIN_ID_TC27:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(FakerDataGenerator.generateAlphanumeric(8));
                payload.setOldPassword(FakerDataGenerator.generatePassword(5, 10, true));
                payload.setNewPassword(FakerDataGenerator.generatePassword(5, 10, true));
                break;
            case INVALID_LOGIN_ID_NOT_IN_USER_TABLE_TC28:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("test109651");
                payload.setOldPassword(FakerDataGenerator.generatePassword(5, 10, true));
                payload.setNewPassword(FakerDataGenerator.generatePassword(5, 10, true));
                break;
            default:
                payload.setLoginID(FakerDataGenerator.generateLowerCaseString(10));
        }
    }

    public void setOldPasswordBasedOnTypeTC29_TC31(ResetPasswordRequest payload, ResetPasswordApiLabel oldPassword) {
        switch (oldPassword) {
            case NULL_OLD_PASSWORD_TC29:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("LoginID");
                payload.setOldPassword(null);
                payload.setNewPassword(FakerDataGenerator.generatePassword(5, 10, true));
                break;
            case INVALID_OLD_PASSWORD_FORMAT_NOT_ENCRYPTED_TC30:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("test10965");
                payload.setOldPassword("abcd12345");
                payload.setNewPassword("zH@Qvw/9IZjxL2ihIbS41B43z+w4bzQGZqZo7dG+gTw=");
                break;
            case INVALID_OLD_PASSWORD_FORMAT_ENCRYPTED_MAX_CHAR_TC31:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("test10965");
                payload.setOldPassword("xxP1GXFhuCOZsYwH6XQf1Cv9e7fCRsCEM18vwj12maA=");
                payload.setNewPassword("zH@Qvw/9IZjxL2ihIbS41B43z+w4bzQGZqZo7dG+gTw=");
                break;
            case INVALID_PASSWORD_FORMAT_ENCRYPTED_MIN_7_CHAR_TC31_1:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("test10965");
                payload.setOldPassword("A6Z+8doiFMIoawaFymc4109iHhIE32ntX4uiZI1kXjc=");
                payload.setNewPassword("zH@Qvw/9IZjxL2ihIbS41B43z+w4bzQGZqZo7dG+gTw=");
                break;
            default:
                payload.setOldPassword(FakerDataGenerator.generateLowerCaseString(10));
        }
    }

    public void setNewPasswordBasedOnTypeTC32_TC35(ResetPasswordRequest payload, ResetPasswordApiLabel newPassword) {
        switch (newPassword) {
            case NULL_NEW_PASSWORD_TC32:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("LoginID");
                payload.setOldPassword(FakerDataGenerator.generatePassword(5, 10, true));
                payload.setNewPassword(null);
                break;
            case INVALID_NEW_PASSWORD_FORMAT_NOT_ENCRYPTED_TC33:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("test10965");
                payload.setOldPassword(FakerDataGenerator.generatePassword(5, 10, true));
                payload.setNewPassword("abcd12345");
                break;
            case INVALID_NEW_PASSWORD_FORMAT_ENCRYPTED_MAX_CHAR_TC34:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("test10965");
                payload.setOldPassword(FakerDataGenerator.generatePassword(5, 10, true));
                payload.setNewPassword(FakerDataGenerator.generateAlphanumeric(35));
                break;
            case INVALID_PASSWORD_FORMAT_ENCRYPTED_MIN_7_CHAR_TC34_1:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("test10965");
                payload.setOldPassword(FakerDataGenerator.generatePassword(5, 10, true));
                payload.setNewPassword(FakerDataGenerator.generateAlphanumeric(7));
                break;
            case OLD_PASSWORD_NEW_PASSWORD_SAME_TC35:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("test10965");
                payload.setOldPassword("mpgejG3D10LlJivRkZGLTMQgQg0f7FGWaiVSkpflVrg=");
                payload.setNewPassword("mpgejG3D10LlJivRkZGLTMQgQg0f7FGWaiVSkpflVrg=");
                break;
            default:
                payload.setNewPassword(FakerDataGenerator.generateLowerCaseString(10));
        }
    }
}
