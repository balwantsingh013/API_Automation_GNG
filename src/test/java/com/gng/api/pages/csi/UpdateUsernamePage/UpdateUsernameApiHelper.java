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

    public void validateUsername() {
        Map<String, Object> userInfo =
                ApplicationContext.get().getDbAction("mariadb")
                        .getDesiredUsername(testContext.getUsername());

        Assert.assertNotNull(userInfo, "Expected username lookup to return a result, but got null");
    }

    public void validateAccountAssociationWithUsername() {
        Map<String, Object> registeredAccount =
                ApplicationContext.get()
                        .getDbAction("mariadb")
                        .getRegisteredAccount(testContext.getCustomerCode());

        Assert.assertNotNull(
                registeredAccount,
                "Expected account to be associated with a username, but query returned null"
        );
    }

    public void preparePayloadForTestCondition(UpdateUsernameRequest payload, UpdateUsernameLabel testCondition) {

        Map<String, Object> userInfo = null;
        String newUsername = FakerDataGenerator.generateAlphanumeric(8);
        payload.setRequestID(FakerDataGenerator.generateAlphanumeric(7));

        switch (testCondition) {

            // NEGATIVE CASES

            case TC_26__Negative__Missing_Request_ID:
                payload.setRequestID("");
                break;

            case TC_27__Negative__Invalid_Request_ID__Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(33));
                break;

            case TC_28__Negative__Duplicate_Request_ID:
                payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());
                break;

            case TC_29__Negative__Missing_Username:
                payload.setUsername("");
                break;

            case TC_30__Negative__Invalid_Username__Inactive:
                userInfo = ApplicationContext.get().getDbAction("mariadb").getInactiveUser();
                payload.setUsername(userInfo.get("user_name").toString());
                payload.setPassword("UAT2@CustomerPass");
                while (true) {
                    userInfo = ApplicationContext.get().getDbAction().getFinalAccountOnly();
                    String custCode = userInfo.get("UCRACCT_CUST_CODE").toString();
                    String premCode = userInfo.get("UCRACCT_PREM_CODE").toString();
                    Map<String, Object> registeredAccount =
                            ApplicationContext.get().getDbAction("mariadb").getRegisteredAccount(custCode);
                    if (registeredAccount == null) {
                        payload.setCustomerCode(custCode);
                        payload.setPremisesCode(premCode);
                        testContext.setCustomerCode(custCode);
                        testContext.setPremisesCode(premCode);
                        break;
                    }
                }
                break;

            case TC_31__Negative__Invalid_Username_format__Length___Too_Short____:
                payload.setUsername(FakerDataGenerator.generateString(4));
                break;

            case TC_32__Negative__Invalid_Username_format__Length___Too_Long____:
                payload.setUsername(FakerDataGenerator.generateString(16));
                break;

            case TC_33__Negative__Invalid_Username_format__Alphanumeric:
                payload.setUsername(FakerDataGenerator.generateAlphanumericWithSpecialChars(6));
                break;

            case TC_34__Negative__Missing_Password:
                payload.setPassword("");
                break;

            case TC_35__Negative__Invalid_Password_Format__Length___Too_Short____:
                payload.setPassword(FakerDataGenerator.generateString(7));
                break;

            case TC_36__Negative__Invalid_Password_Format__Length___Too_Long____:
                payload.setPassword(FakerDataGenerator.generateString(65));
                break;

            case TC_37__Negative__Account_username_already_exists:
                userInfo = ApplicationContext.get().getDbAction("mariadb").getActiveUsername2();
                payload.setUsername(userInfo.get("user_name").toString());
                userInfo = ApplicationContext.get().getDbAction().getAccountWithoutNickname();
                payload.setCustomerCode(userInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_38__Negative__Invalid_credentials___Password____:
                userInfo = ApplicationContext.get().getDbAction().getCustPremCodeRSActiveETC();
                payload.setCustomerCode(userInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                userInfo = ApplicationContext.get().getDbAction("mariadb").getActiveUsername2();
                payload.setUsername(userInfo.get("user_name").toString());
                payload.setPassword(FakerDataGenerator.generateAlphanumeric(8));
                break;

            case TC_39__Negative__Invalid_credentials___Username_does_not_exist____:
                while (true) {
                    userInfo = ApplicationContext.get().getDbAction().getFinalAccountOnly();
                    String custCode = userInfo.get("UCRACCT_CUST_CODE").toString();
                    String premCode = userInfo.get("UCRACCT_PREM_CODE").toString();
                    Map<String, Object> registeredAccount =
                            ApplicationContext.get().getDbAction("mariadb").getRegisteredAccount(custCode);
                    if (registeredAccount == null) {
                        payload.setCustomerCode(custCode);
                        payload.setPremisesCode(premCode);
                        testContext.setCustomerCode(custCode);
                        testContext.setPremisesCode(premCode);
                        break;
                    }
                }
                userInfo = ApplicationContext.get().getDbAction("mariadb").getActiveUsername3();
                payload.setUsername(userInfo.get("user_name").toString());
                payload.setPassword(FakerDataGenerator.generateAlphanumeric(8));
                break;

            case TC_40__Negative__Invalid_credentials___Username_Inactive____:
                userInfo = ApplicationContext.get().getDbAction("mariadb").getInactiveUser();
                payload.setUsername(userInfo.get("user_name").toString());
                userInfo = ApplicationContext.get().getDbAction().getCustPremCodeRSActivePastDueRewards();
                payload.setCustomerCode(userInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_41__Negative__Missing_customerCode:
                payload.setCustomerCode("");
                break;

            case TC_42__Negative__Invalid_customerCode_Length:
                payload.setCustomerCode(FakerDataGenerator.generateDigits(10));
                break;

            case TC_43__Negative__Invalid_customerCode_Format:
                payload.setCustomerCode(FakerDataGenerator.generateAlphanumeric(6));
                break;

            case TC_44__Negative__Invalid_Account_number:
                payload.setCustomerCode("9988776");
                userInfo = ApplicationContext.get().getDbAction().getCustPremCodeRSActivePastDueRewards();
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_45__Negative__Missing_premisesCode:
                payload.setPremisesCode("");
                break;

            case TC_46__Negative__Invalid_premisesCode_Length:
                payload.setPremisesCode(FakerDataGenerator.generateDigits(8));
                break;

            case TC_47__Negative__Invalid_premisesCode_Format:
                payload.setPremisesCode(FakerDataGenerator.generateAlphanumeric(6));
                break;

            // POSITIVE CASES

            case TC_48__Positive__Username_Available___Banner_Active____:
                payload.setUsername(newUsername);
                testContext.setUsername(newUsername);
                while (true) {
                    userInfo = ApplicationContext.get().getDbAction().getActiveAccountOnly();
                    String custCode = userInfo.get("UCRACCT_CUST_CODE").toString();
                    String premCode = userInfo.get("UCRACCT_PREM_CODE").toString();
                    Map<String, Object> registeredAccount =
                            ApplicationContext.get().getDbAction("mariadb").getRegisteredAccount(custCode);
                    if (registeredAccount == null) {
                        payload.setCustomerCode(custCode);
                        payload.setPremisesCode(premCode);
                        testContext.setCustomerCode(custCode);
                        testContext.setPremisesCode(premCode);
                        break;
                    }
                }
                break;

            case TC_49__Positive__Username_Available___Banner_New____:
                payload.setUsername(newUsername);
                testContext.setUsername(newUsername);
                while (true) {
                    userInfo = ApplicationContext.get().getDbAction().getNewAccountOnly();
                    String custCode = userInfo.get("UCRACCT_CUST_CODE").toString();
                    String premCode = userInfo.get("UCRACCT_PREM_CODE").toString();
                    Map<String, Object> registeredAccount =
                            ApplicationContext.get().getDbAction("mariadb").getRegisteredAccount(custCode);
                    if (registeredAccount == null) {
                        payload.setCustomerCode(custCode);
                        payload.setPremisesCode(premCode);
                        testContext.setCustomerCode(custCode);
                        testContext.setPremisesCode(premCode);
                        break;
                    }
                }
                break;

            case TC_50__Positive__Username_Available___Banner_Final____:
                payload.setUsername(newUsername);
                testContext.setUsername(newUsername);
                while (true) {
                    userInfo = ApplicationContext.get().getDbAction().getFinalAccountOnly();
                    String custCode = userInfo.get("UCRACCT_CUST_CODE").toString();
                    String premCode = userInfo.get("UCRACCT_PREM_CODE").toString();
                    Map<String, Object> registeredAccount =
                            ApplicationContext.get().getDbAction("mariadb").getRegisteredAccount(custCode);
                    if (registeredAccount == null) {
                        payload.setCustomerCode(custCode);
                        payload.setPremisesCode(premCode);
                        testContext.setCustomerCode(custCode);
                        testContext.setPremisesCode(premCode);
                        break;
                    }
                }
                break;

            case TC_51__Positive__Username_Active___Banner_Active____:
                userInfo = ApplicationContext.get().getDbAction("mariadb").getActiveUsernameFromOtherTable();
                payload.setUsername(userInfo.get("user_name").toString());
                testContext.setUsername(userInfo.get("user_name").toString());
                while (true) {
                    userInfo = ApplicationContext.get().getDbAction().getActiveAccountOnly();
                    String custCode = userInfo.get("UCRACCT_CUST_CODE").toString();
                    String premCode = userInfo.get("UCRACCT_PREM_CODE").toString();
                    Map<String, Object> registeredAccount =
                            ApplicationContext.get().getDbAction("mariadb").getRegisteredAccount(custCode);
                    if (registeredAccount == null) {
                        payload.setCustomerCode(custCode);
                        payload.setPremisesCode(premCode);
                        testContext.setCustomerCode(custCode);
                        testContext.setPremisesCode(premCode);
                        break;
                    }
                }
                break;

            case TC_52__Positive__Username_Active___Banner_Final____:
                userInfo = ApplicationContext.get().getDbAction("mariadb").getActiveUsernameFromOtherTable();
                payload.setUsername(userInfo.get("user_name").toString());
                testContext.setUsername(userInfo.get("user_name").toString());
                while (true) {
                    userInfo = ApplicationContext.get().getDbAction().getFinalAccountOnly();
                    String custCode = userInfo.get("UCRACCT_CUST_CODE").toString();
                    String premCode = userInfo.get("UCRACCT_PREM_CODE").toString();
                    Map<String, Object> registeredAccount =
                            ApplicationContext.get().getDbAction("mariadb").getRegisteredAccount(custCode);
                    if (registeredAccount == null) {
                        payload.setCustomerCode(custCode);
                        payload.setPremisesCode(premCode);
                        testContext.setCustomerCode(custCode);
                        testContext.setPremisesCode(premCode);
                        break;
                    }
                }
                break;

            case TC_53__Positive__Username_Active___Banner_Inactive____:
                userInfo = ApplicationContext.get().getDbAction("mariadb").getActiveUsernameFromOtherTable();
                payload.setUsername(userInfo.get("user_name").toString());
                testContext.setUsername(userInfo.get("user_name").toString());
                while (true) {
                    userInfo = ApplicationContext.get().getDbAction().getInactiveAccountOnly();
                    String custCode = userInfo.get("UCRACCT_CUST_CODE").toString();
                    String premCode = userInfo.get("UCRACCT_PREM_CODE").toString();
                    Map<String, Object> registeredAccount =
                            ApplicationContext.get().getDbAction("mariadb").getRegisteredAccount(custCode);
                    if (registeredAccount == null) {
                        payload.setCustomerCode(custCode);
                        payload.setPremisesCode(premCode);
                        testContext.setCustomerCode(custCode);
                        testContext.setPremisesCode(premCode);
                        break;
                    }
                }
                break;

            default:
                log.warn("Unhandled test condition: {}", testCondition);
                break;
        }
    }
}
