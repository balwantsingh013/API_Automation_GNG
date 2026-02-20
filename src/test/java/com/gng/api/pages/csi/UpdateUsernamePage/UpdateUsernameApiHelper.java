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
                        .getUserAccountInfoNew(testContext.getCustomerCode()+testContext.getPremisesCode());

        Assert.assertNotNull(
                registeredAccount,
                "Expected account to be associated with a username, but query returned null"
        );
    }

    public void preparePayloadForTestCondition(UpdateUsernameRequest payload, UpdateUsernameLabel testCondition) {
        String newUsername="";
        List<Map<String, Object>> usersInfo=null;
        Map<String, Object> userInfo = null;
        if(testContext.getUsername() == null || testContext.getUsername().isEmpty()) {
             newUsername = FakerDataGenerator.generateAlphanumeric(8);
        }
        else{
             newUsername=testContext.getUsername();
        }
        payload.setRequestID(FakerDataGenerator.generateAlphanumeric(7));

        String custCode="";
        String premCode="";
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
                payload.setPassword("UAT2@CustomerPa");
                while (true) {
                    userInfo = ApplicationContext.get().getDbAction().getFinalAccountOnly2();
                    custCode = userInfo.get("UCRACCT_CUST_CODE").toString();
                    premCode = userInfo.get("UCRACCT_PREM_CODE").toString();
                    Map<String, Object> registeredAccount =
                            ApplicationContext.get().getDbAction("mariadb").getRegisteredAccount(custCode);
                    if (registeredAccount == null) {
                        userInfo = ApplicationContext.get().getDbAction().getUserAccountInfo(custCode);
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

            case TC_37__Negative__Account_username_already_linked:
                userInfo = ApplicationContext.get().getDbAction("mariadb").getActiveUsername2();
                payload.setUsername(userInfo.get("user_name").toString());
                while (true) {
                    userInfo = ApplicationContext.get().getDbAction().getFinalAccountOnly2();
                    custCode = userInfo.get("UCRACCT_CUST_CODE").toString();
                    premCode = userInfo.get("UCRACCT_PREM_CODE").toString();
                    Map<String, Object> registeredAccount =
                            ApplicationContext.get().getDbAction("mariadb").getRegisteredAccount(custCode+premCode);
                           if (registeredAccount != null) {
                                userInfo =
                                       ApplicationContext.get()
                                               .getDbAction("mariadb")
                                               .getUserAccountInfoNew(custCode+premCode);

                               ///userInfo = ApplicationContext.get().getDbAction().getUserAccountInfo(custCode);
                               payload.setCustomerCode(custCode);
                        payload.setPremisesCode(premCode);
                        testContext.setCustomerCode(custCode);
                        testContext.setPremisesCode(premCode);
                        break;
                    }
                }
                break;

            case TC_38__Negative__Invalid_credentials___Password____:
                userInfo = ApplicationContext.get().getDbAction().getCustPremCodeRSActiveETC();
                custCode = userInfo.get("UCRACCT_CUST_CODE").toString();
                premCode = userInfo.get("UCRACCT_PREM_CODE").toString();
                payload.setCustomerCode(custCode);
                payload.setPremisesCode(premCode);
                userInfo = ApplicationContext.get().getDbAction("mariadb").getUserAccountInfoNew(custCode+premCode);
                payload.setUsername(userInfo.get("user_name").toString());
                payload.setPassword(FakerDataGenerator.generateAlphanumeric(8));
                break;

            case TC_39__Negative__Invalid_credentials___Username_Inactive____:
                userInfo = ApplicationContext.get().getDbAction("mariadb").getUserAccountInfoNewInactive(custCode+premCode);
                payload.setUsername(userInfo.get("user_name").toString());
                payload.setPassword(FakerDataGenerator.generateAlphanumeric(8));
                String accountNo= userInfo.get("account_number").toString();
                custCode = accountNo.substring(0, 9);
                premCode = accountNo.substring(9);
                payload.setCustomerCode(custCode);
                payload.setPremisesCode(premCode);
                break;

            case TC_40__Negative__Missing_customerCode:
                payload.setCustomerCode("");
                break;

            case TC_41__Negative__Invalid_customerCode_Length:
                payload.setCustomerCode(FakerDataGenerator.generateDigits(10));
                break;

            case TC_42__Negative__Invalid_customerCode_Format:
                payload.setCustomerCode(FakerDataGenerator.generateAlphanumeric(6));
                break;

            case TC_43__Negative__Invalid_Account_Number:
                payload.setCustomerCode("9988776");
                userInfo = ApplicationContext.get().getDbAction().getUserAccountInfo("9988776");
                userInfo = ApplicationContext.get().getDbAction().getCustPremCodeRSActivePastDueRewards();
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_44__Negative__Missing_premisesCode:
                payload.setPremisesCode("");
                break;

            case TC_45__Negative__Invalid_premisesCode_Length:
                payload.setPremisesCode(FakerDataGenerator.generateDigits(8));
                break;

            case TC_46__Negative__Invalid_premisesCode_Format:
                payload.setPremisesCode("a"+FakerDataGenerator.generateAlphanumeric(5));
                break;

            // POSITIVE CASES

            case TC_285__Positive__Search_Order__Active_Name_order:
                newUsername= "sameNameSortA";
                payload.setUsername(newUsername);
                testContext.setUsername(newUsername);
                while (true) {
                    usersInfo = ApplicationContext.get().getDbAction().getActiveAccountWithSameName();
                    custCode = usersInfo.getFirst().get("UCBCUST_CUST_CODE").toString();
                    premCode = usersInfo.getFirst().get("UCRACCT_PREM_CODE").toString();
                    testContext.setFirstName(usersInfo.getFirst().get("UCBCUST_FIRST_NAME").toString());
                    Map<String, Object> registeredAccount =
                            ApplicationContext.get().getDbAction("mariadb").getRegisteredAccount2(custCode);
                    if (registeredAccount == null) {
                        userInfo = ApplicationContext.get().getDbAction("mariadb").getUserAccountInfoNew(custCode+premCode);
                        payload.setCustomerCode(custCode);
                        payload.setPremisesCode(premCode);
                        break;
                    }
                }
                break;

            case TC_285__Positive__Search_Order__Active_Name_order_2:
                newUsername= "sameNameSortA";
                payload.setUsername(newUsername);
                testContext.setUsername(newUsername);
                while (true) {
                    usersInfo = ApplicationContext.get().getDbAction().getActiveAccountWithSameName2(testContext.getFirstName());
                    custCode = usersInfo.getFirst().get("UCBCUST_CUST_CODE").toString();
                    premCode = usersInfo.getFirst().get("UCRACCT_PREM_CODE").toString();
                    Map<String, Object> registeredAccount =
                            ApplicationContext.get().getDbAction("mariadb").getRegisteredAccount2(custCode);
                    if (registeredAccount == null) {
                        userInfo = ApplicationContext.get().getDbAction("mariadb").getUserAccountInfoNew(custCode+premCode);
                        payload.setCustomerCode(custCode);
                        payload.setPremisesCode(premCode);
                        break;
                    }
                }
                break;

            case TC_47__Positive__Username_Available___Banner_Active____:
                payload.setUsername(newUsername);
                testContext.setUsername(newUsername);

                int attempts = 0;

                while (attempts < 20) {
                    attempts++;

                    userInfo = ApplicationContext.get().getDbAction().getActiveAccountOnly2();
                    custCode = userInfo.get("UCRACCT_CUST_CODE").toString();
                    premCode = userInfo.get("UCRACCT_PREM_CODE").toString();

                    Map<String, Object> registeredAccount =
                            ApplicationContext.get().getDbAction("mariadb").getRegisteredAccount2(custCode);

                    if (registeredAccount == null) {
                        userInfo = ApplicationContext.get().getDbAction("mariadb")
                                .getUserAccountInfoNew(custCode + premCode);

                        payload.setCustomerCode(custCode);
                        payload.setPremisesCode(premCode);

                        testContext.setCustomerCode(custCode);
                        testContext.setPremisesCode(premCode);
                        testContext.setCustomerCode1(custCode);

                        break;
                    }
                }

                // Optional: handle case where no valid account was found
                if (attempts >= 20) {
                    throw new RuntimeException("No unregistered ACTIVE account found after 20 attempts.");
                }

                break;


            case TC_48__Positive__Username_Available___Banner_New____:
                payload.setUsername(newUsername);
                testContext.setUsername(newUsername);

                int noOfattempts = 0;

                while (noOfattempts < 20) {
                    noOfattempts++;

                    userInfo = ApplicationContext.get().getDbAction().getNewAccountOnly2();
                    custCode = userInfo.get("UCRACCT_CUST_CODE").toString();
                    premCode = userInfo.get("UCRACCT_PREM_CODE").toString();

                    Map<String, Object> registeredAccount =
                            ApplicationContext.get().getDbAction("mariadb").getRegisteredAccount2(custCode);

                    if (registeredAccount == null) {
                        userInfo = ApplicationContext.get().getDbAction("mariadb")
                                .getUserAccountInfoNew(custCode + premCode);

                        payload.setCustomerCode(custCode);
                        payload.setPremisesCode(premCode);

                        testContext.setCustomerCode(custCode);
                        testContext.setPremisesCode(premCode);
                        testContext.setCustomerCode2(custCode);

                        break;
                    }
                }

                // Optional: handle case where no valid account was found
                if (noOfattempts >= 20) {
                    throw new RuntimeException("No unregistered account found after 20 attempts.");
                }

                break;


            case TC_49__Positive__Username_Available___Banner_Final____:
                payload.setUsername(newUsername);
                testContext.setUsername(newUsername);

                int retry = 0;

                while (retry < 20) {
                    retry++;

                    userInfo = ApplicationContext.get().getDbAction().getFinalAccountOnly2();
                    custCode = userInfo.get("UCRACCT_CUST_CODE").toString();
                    premCode = userInfo.get("UCRACCT_PREM_CODE").toString();

                    Map<String, Object> registeredAccount =
                            ApplicationContext.get().getDbAction("mariadb").getRegisteredAccount2(custCode);

                    if (registeredAccount == null) {
                        userInfo = ApplicationContext.get().getDbAction("mariadb")
                                .getUserAccountInfoNew(custCode + premCode);

                        payload.setCustomerCode(custCode);
                        payload.setPremisesCode(premCode);

                        testContext.setCustomerCode(custCode);
                        testContext.setPremisesCode(premCode);
                        testContext.setCustomerCode3(custCode);

                        break;
                    }
                }

                // Optional: fail if no valid account found
                if (retry >= 20) {
                    throw new RuntimeException("No unregistered FINAL account found after 20 attempts.");
                }

                break;


            case TC_50__Positive__Username_Available___Banner_Inactive____:
                payload.setUsername(newUsername);
                testContext.setUsername(newUsername);

                int retryCount = 0;

                while (retryCount < 20) {
                    retryCount++;

                    userInfo = ApplicationContext.get().getDbAction().getInactiveAccountOnly2();
                    custCode = userInfo.get("UCRACCT_CUST_CODE").toString();
                    premCode = userInfo.get("UCRACCT_PREM_CODE").toString();

                    Map<String, Object> registeredAccount =
                            ApplicationContext.get().getDbAction("mariadb").getRegisteredAccount2(custCode);

                    if (registeredAccount == null) {
                        userInfo = ApplicationContext.get().getDbAction("mariadb")
                                .getUserAccountInfoNew(custCode + premCode);

                        payload.setCustomerCode(custCode);
                        payload.setPremisesCode(premCode);

                        testContext.setCustomerCode(custCode);
                        testContext.setPremisesCode(premCode);
                        testContext.setCustomerCode4(custCode);

                        break;
                    }
                }

                // Optional: fail if no valid inactive account found
                if (retryCount >= 20) {
                    throw new RuntimeException("No unregistered INACTIVE account found after 20 attempts.");
                }

                break;



            case TC_51__Positive__Username_Active___Banner_Active____:
                userInfo = ApplicationContext.get().getDbAction("mariadb").getActiveUsernameFromOtherTable3();
                payload.setUsername(userInfo.get("user_name").toString());
                testContext.setUsername(userInfo.get("user_name").toString());
                while (true) {
                    userInfo = ApplicationContext.get().getDbAction().getActiveAccountOnly2();
                    custCode = userInfo.get("UCRACCT_CUST_CODE").toString();
                    premCode = userInfo.get("UCRACCT_PREM_CODE").toString();
                    Map<String, Object> registeredAccount =
                            ApplicationContext.get().getDbAction("mariadb").getRegisteredAccount(custCode);
                    if (registeredAccount == null) {
                        userInfo = ApplicationContext.get().getDbAction("mariadb").getUserAccountInfoNew(custCode+premCode);
                        payload.setCustomerCode(custCode);
                        payload.setPremisesCode(premCode);
                        testContext.setCustomerCode(custCode);
                        testContext.setPremisesCode(premCode);
                        testContext.setCustomerCode5(custCode);
                        break;
                    }
                }
                break;

            case TC_52__Positive__Username_Active___Banner_New____:
                userInfo = ApplicationContext.get().getDbAction("mariadb").getActiveUsernameFromOtherTable4();
                payload.setUsername(userInfo.get("user_name").toString());
                testContext.setUsername(userInfo.get("user_name").toString());
                while (true) {
                    userInfo = ApplicationContext.get().getDbAction().getNewAccountOnly2();
                    custCode = userInfo.get("UCRACCT_CUST_CODE").toString();
                    premCode = userInfo.get("UCRACCT_PREM_CODE").toString();
                    Map<String, Object> registeredAccount =
                            ApplicationContext.get().getDbAction("mariadb").getRegisteredAccount(custCode);
                    if (registeredAccount == null) {
                        userInfo = ApplicationContext.get().getDbAction("mariadb").getUserAccountInfoNew(custCode+premCode);
                        payload.setCustomerCode(custCode);
                        payload.setPremisesCode(premCode);
                        testContext.setCustomerCode(custCode);
                        testContext.setPremisesCode(premCode);
                        testContext.setCustomerCode6(custCode);
                        break;
                    }
                }
                break;


            case TC_53__Positive__Username_Active___Banner_Final____:
                userInfo = ApplicationContext.get().getDbAction("mariadb").getActiveUsernameFromOtherTable4();
                payload.setUsername(userInfo.get("user_name").toString());
                testContext.setUsername(userInfo.get("user_name").toString());
                while (true) {
                    userInfo = ApplicationContext.get().getDbAction().getFinalAccountOnly2();
                    custCode = userInfo.get("UCRACCT_CUST_CODE").toString();
                    premCode = userInfo.get("UCRACCT_PREM_CODE").toString();
                    Map<String, Object> registeredAccount =
                            ApplicationContext.get().getDbAction("mariadb").getRegisteredAccount(custCode);
                    if (registeredAccount == null) {
                        userInfo = ApplicationContext.get().getDbAction("mariadb").getUserAccountInfoNew(custCode+premCode);
                        payload.setCustomerCode(custCode);
                        payload.setPremisesCode(premCode);
                        testContext.setCustomerCode(custCode);
                        testContext.setPremisesCode(premCode);
                        testContext.setCustomerCode7(custCode);
                        break;
                    }
                }
                break;

            case TC_54__Positive__Username_Active___Banner_Inactive____:
                userInfo = ApplicationContext.get().getDbAction("mariadb").getActiveUsernameFromOtherTable5();
                payload.setUsername(userInfo.get("user_name").toString());
                testContext.setUsername(userInfo.get("user_name").toString());
                while (true) {
                    userInfo = ApplicationContext.get().getDbAction().getInactiveAccountOnly2();
                    custCode = userInfo.get("UCRACCT_CUST_CODE").toString();
                    premCode = userInfo.get("UCRACCT_PREM_CODE").toString();
                    Map<String, Object> registeredAccount =
                            ApplicationContext.get().getDbAction("mariadb").getRegisteredAccount(custCode);
                    if (registeredAccount == null) {
                        userInfo = ApplicationContext.get().getDbAction("mariadb").getUserAccountInfoNew(custCode+premCode);
                        payload.setCustomerCode(custCode);
                        payload.setPremisesCode(premCode);
                        testContext.setCustomerCode(custCode);
                        testContext.setPremisesCode(premCode);
                        testContext.setCustomerCode8(custCode);
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
