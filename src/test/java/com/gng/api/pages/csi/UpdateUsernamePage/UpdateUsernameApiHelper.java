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

import java.util.List;
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
        Map<String, Object> userNames = null;
        String newUsername = FakerDataGenerator.generateAlphanumeric(8);
        payload.setRequestID(FakerDataGenerator.generateAlphanumeric(7));

        switch (testCondition) {

            case TC_28__Negative__Missing_Request_ID:
                payload.setRequestID("");
                break;

            case TC_29__Negative__Invalid_Request_ID__Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(33));
                break;

            case TC_30__Negative__Duplicate_Request_ID:
                payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());
                break;

            case TC_31__Negative__Missing_Username:
                payload.setUsername("");
                break;

            case TC_32__Negative__Invalid_Username__Inactive:
                payload.setUsername("0000000000");
                payload.setPassword("i8J6WjjzyLz8");
                userInfo = ApplicationContext.get().getDbAction().getCustPremCodeRSActivePastDueRewards();
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setCustomerCode(userInfo.get("UCRACCT_CUST_CODE").toString());
                break;

            case TC_33__Negative__Invalid_Username__Inactive:
                userInfo = ApplicationContext.get().getDbAction("mariadb").getInactiveUser2();
                payload.setUsername(userInfo.get("user_name").toString());
                userInfo = ApplicationContext.get().getDbAction().getCustPremCodeRSActivePastDueRewards();
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setCustomerCode(userInfo.get("UCRACCT_CUST_CODE").toString());
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

            case TC_37__Negative__Missing_Password:
                payload.setPassword("");
                break;

            case TC_38__Negative__Invalid_Password_Format__Length___Too_Short____:
                payload.setPassword(FakerDataGenerator.generateString(7));
                break;

            case TC_39__Negative__Invalid_Password_Format__Length___Too_Long____:
                payload.setPassword(FakerDataGenerator.generateString(65));
                break;


            case TC_42__Negative__Account_username_already_exists:
                userInfo = ApplicationContext.get().getDbAction("mariadb").getActiveUsername();
                payload.setUsername(userInfo.get("user_name").toString());
                userInfo = ApplicationContext.get().getDbAction().getAccountWithoutNickname();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(userInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_43__Negative__Invalid_credentials___Password____:
                userInfo = ApplicationContext.get().getDbAction().getCustPremCodeRSActivePastDueRewards();
                payload.setCustomerCode(userInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                userInfo = ApplicationContext.get().getDbAction("mariadb").getActiveUsername2();
                payload.setUsername(userInfo.get("user_name").toString());
                payload.setPassword(FakerDataGenerator.generateAlphanumeric(8));
                break;

            case TC_44__Negative__Invalid_credentials___Username_does_not_exist____:
                userInfo = ApplicationContext.get().getDbAction().getCustPremCodeRSActivePastDueRewards();
                payload.setCustomerCode(userInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                userInfo = ApplicationContext.get().getDbAction("mariadb").getActiveUsername();
                payload.setUsername(userInfo.get("user_name").toString());
                payload.setPassword(FakerDataGenerator.generateAlphanumeric(8));
                break;

            case TC_45__Negative__Invalid_credentials___Username_Inactive____:
                userInfo = ApplicationContext.get().getDbAction("mariadb").getInactiveUser();
                payload.setUsername(userInfo.get("user_name").toString());
                userInfo = ApplicationContext.get().getDbAction().getCustPremCodeRSActivePastDueRewards();
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setCustomerCode(userInfo.get("UCRACCT_CUST_CODE").toString());
                break;

            case TC_46__Negative__Invalid_credentials___Username_Inactive____:
                userInfo = ApplicationContext.get().getDbAction("mariadb").getInactiveUser2();
                payload.setUsername(userInfo.get("user_name").toString());
                userInfo = ApplicationContext.get().getDbAction().getCustPremCodeRSActivePastDueRewards();
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setCustomerCode(userInfo.get("UCRACCT_CUST_CODE").toString());
                break;

            case TC_47__Negative__Missing_customerCode:
                payload.setCustomerCode("");
                break;


            case TC_48__Negative__Invalid_customerCode_Length:
                payload.setCustomerCode(FakerDataGenerator.generateDigits(10));
                break;

            case TC_49__Negative__Invalid_customerCode_Format:
                payload.setCustomerCode(FakerDataGenerator.generateAlphanumeric(6));
                break;

            case TC_50__Negative__Invalid_customerCode:
                String customerCode = "9988776";
                payload.setCustomerCode(customerCode);
                userInfo = ApplicationContext.get().getDbAction().getCustPremCodeRSActivePastDueRewards();
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                userInfo = ApplicationContext.get().getDbAction().getCustomerCode(customerCode);
                Assert.assertEquals(userInfo.size(), 0, "Expected customerCode to not exist in UCBCUST");
                break;

            case TC_51__Negative__Missing_premisesCode:
                payload.setPremisesCode("");
                break;

            case TC_52__Negative__Invalid_premisesCode_Length:
                payload.setPremisesCode(FakerDataGenerator.generateDigits(8));
                break;

            case TC_53__Negative__Invalid_premisesCode_Format:
                payload.setPremisesCode(FakerDataGenerator.generateAlphanumeric(6));
                break;


            case TC_54__Positive__Username_Available___Banner_Active____:
                payload.setUsername(newUsername);

                while (true) {
                    // Step 1: Oracle call — get random ACTIVE Banner account
                    userInfo = ApplicationContext.get().getDbAction().getActiveAccountOnly();

                    String custCode = userInfo.get("UCRACCT_CUST_CODE").toString();
                    String premCode = userInfo.get("UCRACCT_PREM_CODE").toString();

                    System.out.println("Oracle returned ACTIVE custCode=" + custCode + ", premCode=" + premCode);

                    // Step 2: MariaDB call — check if this account is already registered
                    Map<String, Object> registeredAccount =
                            ApplicationContext.get()
                                    .getDbAction("mariadb")
                                    .getRegisteredAccount(custCode);

                    System.out.println("MariaDB returned: " + registeredAccount);

                    // Step 3: If NOT found in MariaDB → valid account → break loop
                    if (registeredAccount == null) {
                        payload.setCustomerCode(custCode);
                        payload.setPremisesCode(premCode);
                        break;
                    }

                    // Otherwise loop again and fetch another random Oracle account
                    System.out.println("Active account exists in MariaDB, retrying...");
                }

                break;


            case TC_55__Positive__Username_Available___Banner_New____:
                payload.setUsername(newUsername);

                while (true) {
                    userInfo = ApplicationContext.get().getDbAction().getNewAccountOnly();

                    String custCode = userInfo.get("UCRACCT_CUST_CODE").toString();
                    String premCode = userInfo.get("UCRACCT_PREM_CODE").toString();

                    userInfo =
                            ApplicationContext.get()
                                    .getDbAction("mariadb")
                                    .getRegisteredAccount(custCode);

                    // If no row returned → account is NOT registered → break
                    if (userInfo == null) {
                        payload.setCustomerCode(custCode);
                        payload.setPremisesCode(premCode);
                        break;
                    }

                    // Otherwise try again
                }

                break;


            case TC_56__Positive__Username_Available___Banner_Final____:
                payload.setUsername(newUsername);

                while (true) {
                    // Step 1: Oracle call — get random FINAL Banner account
                    userInfo = ApplicationContext.get().getDbAction().getFinalAccountOnly();

                    String custCode = userInfo.get("UCRACCT_CUST_CODE").toString();
                    String premCode = userInfo.get("UCRACCT_PREM_CODE").toString();

                    System.out.println("Oracle returned FINAL custCode=" + custCode + ", premCode=" + premCode);

                    // Step 2: MariaDB call — check if this account is already registered
                    Map<String, Object> registeredAccount =
                            ApplicationContext.get()
                                    .getDbAction("mariadb")
                                    .getRegisteredAccount(custCode);

                    System.out.println("MariaDB returned: " + registeredAccount);

                    // Step 3: If NOT found in MariaDB → valid account → break loop
                    if (registeredAccount == null) {
                        payload.setCustomerCode(custCode);
                        payload.setPremisesCode(premCode);
                        break;
                    }

                    // Otherwise loop again and fetch another random Oracle account
                    System.out.println("Final account exists in MariaDB, retrying...");
                }

                break;


            case TC_57__Positive__Username_Active___Banner_Active____:

                // Step 1: Get an ACTIVE username from MariaDB
                userInfo = ApplicationContext.get().getDbAction("mariadb").getActiveUsername();
                payload.setUsername(userInfo.get("user_name").toString());

                // Step 2: Loop until we find an ACTIVE Banner account not registered in MariaDB
                while (true) {

                    // Oracle: get random ACTIVE Banner account
                    userInfo = ApplicationContext.get().getDbAction().getActiveAccountOnly();

                    String custCode = userInfo.get("UCRACCT_CUST_CODE").toString();
                    String premCode = userInfo.get("UCRACCT_PREM_CODE").toString();

                    System.out.println("Oracle returned ACTIVE custCode=" + custCode + ", premCode=" + premCode);

                    // MariaDB: check if this account is already registered
                    Map<String, Object> registeredAccount =
                            ApplicationContext.get()
                                    .getDbAction("mariadb")
                                    .getRegisteredAccount(custCode);

                    System.out.println("MariaDB returned: " + registeredAccount);

                    // If NOT found in MariaDB → valid account → break loop
                    if (registeredAccount == null) {
                        payload.setCustomerCode(custCode);
                        payload.setPremisesCode(premCode);
                        break;
                    }

                    // Otherwise loop again
                    System.out.println("Active account exists in MariaDB, retrying...");
                }
                break;

            case TC_58__Positive__Username_Active___Banner_Final____:

                // Step 1: Get an ACTIVE username from MariaDB
                userInfo = ApplicationContext.get().getDbAction("mariadb").getActiveUsername();
                payload.setUsername(userInfo.get("user_name").toString());

                // Step 2: Loop until we find a FINAL Banner account not registered in MariaDB
                while (true) {

                    // Oracle: get random FINAL Banner account
                    userInfo = ApplicationContext.get().getDbAction().getFinalAccountOnly();

                    String custCode = userInfo.get("UCRACCT_CUST_CODE").toString();
                    String premCode = userInfo.get("UCRACCT_PREM_CODE").toString();

                    System.out.println("Oracle returned FINAL custCode=" + custCode + ", premCode=" + premCode);

                    // MariaDB: check if this account is already registered
                    Map<String, Object> registeredAccount =
                            ApplicationContext.get()
                                    .getDbAction("mariadb")
                                    .getRegisteredAccount(custCode);

                    System.out.println("MariaDB returned: " + registeredAccount);

                    // If NOT found in MariaDB → valid account → break loop
                    if (registeredAccount == null) {
                        payload.setCustomerCode(custCode);
                        payload.setPremisesCode(premCode);
                        break;
                    }

                    // Otherwise loop again
                    System.out.println("Final account exists in MariaDB, retrying...");
                }

                break;

            case TC_59__Positive__Username_Active___Banner_Inactive____:

                // Step 1: Get an ACTIVE username from MariaDB
                userInfo = ApplicationContext.get().getDbAction("mariadb").getActiveUsername();
                payload.setUsername(userInfo.get("user_name").toString());

                // Step 2: Loop until we find an INACTIVE Banner account not registered in MariaDB
                while (true) {

                    // Oracle: get random INACTIVE Banner account
                    userInfo = ApplicationContext.get().getDbAction().getInactiveAccountOnly();

                    String custCode = userInfo.get("UCRACCT_CUST_CODE").toString();
                    String premCode = userInfo.get("UCRACCT_PREM_CODE").toString();

                    System.out.println("Oracle returned INACTIVE custCode=" + custCode + ", premCode=" + premCode);

                    // MariaDB: check if this account is already registered
                    Map<String, Object> registeredAccount =
                            ApplicationContext.get()
                                    .getDbAction("mariadb")
                                    .getRegisteredAccount(custCode);

                    System.out.println("MariaDB returned: " + registeredAccount);

                    // If NOT found in MariaDB → valid account → break loop
                    if (registeredAccount == null) {
                        payload.setCustomerCode(custCode);
                        payload.setPremisesCode(premCode);
                        break;
                    }

                    // Otherwise loop again
                    System.out.println("Inactive account exists in MariaDB, retrying...");
                }

                break;




            default:
                log.warn("Unhandled test condition: {}", testCondition);
                break;
        }
    }
}
