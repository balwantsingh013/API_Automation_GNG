package com.gng.api.pages.csi.SearchAccountsPage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.SearchAccounts.SearchAccountsRequestCSI;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.SearchAccounts.SearchAccountsLabelCSI;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class SearchAccountsApiHelperCSI {

    private final TestContext testContext;

    public SearchAccountsApiHelperCSI(TestContext testContext) {
        this.testContext = testContext;
    }

    public SearchAccountsRequestCSI preparePayload(SearchAccountsLabelCSI apiLabel) {
        log.info("Preparing payload for {}", apiLabel);

        String jsonFileName = SearchAccountsLabelCSI.search_accounts_csi.toString();
        return BasePage.deserializeJsonToPojo(jsonFileName, SearchAccountsRequestCSI.class);
    }

    public void preparePayloadForTestCondition(SearchAccountsRequestCSI payload, SearchAccountsLabelCSI testCondition) {

        Map<String, Object> userInfo;
        String accountNo="";
        String customerCode="";
        String premisesCode="";

        switch (testCondition) {

            // ---------------- EXISTING NEGATIVE CASES ----------------

            case TC_188__Negative__Missing_Request_ID:
                payload.setRequestID("");
                break;

            case TC_189__Negative__Invalid_Request_ID_Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(33));
                break;

            case TC_190__Negative__Duplicate_Request_ID:
                payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());
                break;

            case TC_191__Negative__Invalid_customerCode_Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateDigits(10));
                break;

            case TC_192__Negative__Invalid_customerCode_Format:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateAlphanumeric(7));
                break;

            case TC_193__Negative__Invalid_premisesCode_Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(8));
                break;

            case TC_194__Negative__Invalid_premisesCode_Format:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPremisesCode(FakerDataGenerator.generateAlphanumeric(6));
                break;

            case TC_195__Negative__Invalid_Last_or_Business_Name_Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerLastNameBusiness(FakerDataGenerator.generateString(61));
                break;

            case TC_196__Negative__Invalid_First_Name_Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerFirstName(FakerDataGenerator.generateString(16));
                break;

            case TC_197__Negative__Invalid_Last_Four_SSN_Length_Too_Long:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setLastFourSocialSecurityNumber(FakerDataGenerator.generateDigits(5));
                break;

            case TC_198__Negative__Invalid_Last_Four_SSN_Length_Too_Short:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setLastFourSocialSecurityNumber(FakerDataGenerator.generateDigits(3));
                break;

            case TC_199__Negative__Invalid_Federal_Tax_ID_Length_Too_Long:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setFederalTaxID(FakerDataGenerator.generateDigits(10));
                break;

            case TC_200__Negative__Invalid_Federal_Tax_ID_Length_Too_Short:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setFederalTaxID(FakerDataGenerator.generateDigits(8));
                break;

            case TC_201__Negative__Invalid_Email_Address_Format:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setEmailAddress(FakerDataGenerator.generateString(8));
                break;

            case TC_202__Negative__Invalid_Phone_Number_Length_Too_Long:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPhoneNumber(FakerDataGenerator.generateDigits(11));
                break;

            case TC_203__Negative__Invalid_Phone_Number_Length_Too_Short:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPhoneNumber(FakerDataGenerator.generateDigits(9));
                break;

            case TC_204__Negative__Invalid_Username_Format:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(FakerDataGenerator.generateAlphanumericWithSpecialChars(8));
                break;

            case TC_205__Negative__Inactive_Username:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                userInfo = ApplicationContext.get().getDbAction("mariadb").getInactiveUser();
                payload.setUsername(userInfo.get("user_name").toString());
                break;

            case TC_206__Negative__Invalid_Password_Format_Length_Too_Short:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPassword(FakerDataGenerator.generateString(5));
                break;

            case TC_207__Negative__Invalid_Password_Format_Length_Too_Long:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPassword(FakerDataGenerator.generateString(65));
                break;

            case TC_208__Negative__Last4SSN_____Password:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setLastFourSocialSecurityNumber(FakerDataGenerator.generateDigits(4));
                payload.setPassword(FakerDataGenerator.generateString(10));
                payload.setUsername(null);
                break;

            case TC_209__Negative__Last4SSN_____Username:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setLastFourSocialSecurityNumber(FakerDataGenerator.generateDigits(4));
                payload.setUsername(FakerDataGenerator.generateString(8));
                payload.setPassword(null);
                break;

            case TC_210__Negative__Last4SSN_____CustomerCode_____PremisesCode:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setLastFourSocialSecurityNumber(FakerDataGenerator.generateDigits(4));
                payload.setCustomerCode(FakerDataGenerator.generateDigits(9));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(7));
                payload.setUsername(null);
                payload.setPassword(null);
                break;

            case TC_211__Negative__Last4SSN_____FederalTaxID_Number:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setLastFourSocialSecurityNumber(FakerDataGenerator.generateDigits(4));
                payload.setFederalTaxID(FakerDataGenerator.generateDigits(9));
                payload.setUsername(null);
                payload.setPassword(null);
                break;

            case TC_212__Negative__Password_____FederalTaxID_Number:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPassword(FakerDataGenerator.generateString(10));
                payload.setFederalTaxID(FakerDataGenerator.generateDigits(9));
                payload.setUsername(null);
                break;

            case TC_213__Negative__CustomerLastName_____EmailAddress:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerLastNameBusiness(FakerDataGenerator.generateString(10));
                payload.setEmailAddress(FakerDataGenerator.generateEmail());
                payload.setUsername(null);
                payload.setPassword(null);
                break;

            case TC_214__Negative__CustomerLastName_____PhoneNumber:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerLastNameBusiness(FakerDataGenerator.generateString(10));
                payload.setPhoneNumber(FakerDataGenerator.generateDigits(10));
                payload.setUsername(null);
                payload.setPassword(null);
                break;

            case TC_215__Negative__CustomerLastName_____Username:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerLastNameBusiness(FakerDataGenerator.generateString(10));
                payload.setUsername(FakerDataGenerator.generateString(8));
                payload.setPassword(null);
                break;

            case TC_216__Negative__CustomerLastName_____CustomerCode_____PremisesCode:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerLastNameBusiness(FakerDataGenerator.generateString(10));
                payload.setCustomerCode(FakerDataGenerator.generateDigits(9));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(7));
                payload.setUsername(null);
                payload.setPassword(null);
                break;

            case TC_217__Negative__EmailAddress_____PhoneNumber:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setEmailAddress(FakerDataGenerator.generateEmail());
                payload.setPhoneNumber(FakerDataGenerator.generateDigits(10));
                payload.setUsername(null);
                payload.setPassword(null);
                break;

            case TC_218__Negative__EmailAddress_____Username:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setEmailAddress(FakerDataGenerator.generateEmail());
                payload.setUsername(FakerDataGenerator.generateString(8));
                payload.setPassword(null);
                break;


            case TC_219__Negative__EmailAddress_____CustomerCode_____PremisesCode:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setEmailAddress(FakerDataGenerator.generateEmail());
                payload.setCustomerCode(FakerDataGenerator.generateDigits(9));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(7));
                payload.setUsername(null);
                payload.setPassword(null);
                break;

            case TC_220__Negative__EmailAddress_____FederalTaxID_Number:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setEmailAddress(FakerDataGenerator.generateEmail());
                payload.setFederalTaxID(FakerDataGenerator.generateDigits(9));
                payload.setUsername(null);
                payload.setPassword(null);
                break;

            case TC_221__Negative__PhoneNumber_____Username:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPhoneNumber(FakerDataGenerator.generateDigits(10));
                payload.setUsername(FakerDataGenerator.generateString(8));
                payload.setPassword(null);
                break;

            case TC_222__Negative__PhoneNumber_____CustomerCode_____PremisesCode:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPhoneNumber(FakerDataGenerator.generateDigits(10));
                payload.setCustomerCode(FakerDataGenerator.generateDigits(9));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(7));
                payload.setUsername(null);
                payload.setPassword(null);
                break;

            case TC_223__Negative__PhoneNumber_____FederalTaxID_Number:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPhoneNumber(FakerDataGenerator.generateDigits(10));
                payload.setFederalTaxID(FakerDataGenerator.generateDigits(9));
                payload.setUsername(null);
                payload.setPassword(null);
                break;

            case TC_224__Negative__Username_____CustomerCode_____PremisesCode:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(FakerDataGenerator.generateString(8));
                payload.setCustomerCode(FakerDataGenerator.generateDigits(9));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(7));
                payload.setPassword(null);
                break;

            case TC_225__Negative__Username_____FederalTaxID_Number:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setUsername(FakerDataGenerator.generateString(8));
                payload.setFederalTaxID(FakerDataGenerator.generateDigits(9));
                payload.setPassword(null);
                break;

            case TC_226__Negative__CustomerCode_____PremisesCode_____FederalTaxID_Number:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateDigits(9));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(7));
                payload.setFederalTaxID(FakerDataGenerator.generateDigits(9));
                payload.setUsername(null);
                payload.setPassword(null);
                break;

            case TC_227__Negative__Too_Many_Matches:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setEmailAddress("Fernando.Waisfeld@vertexone.net");
                payload.setLastFourSocialSecurityNumber("7954");
                payload.setUsername(null);
                payload.setPassword(null);
                break;

            // ---------------- POSITIVE CASES (TC_231 - TC_233) ----------------

            case TC_228__Positive__Last4SSN_____CustomerLastName:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));

                userInfo =
                        ApplicationContext.get().getDbAction().getCustomerWithLastNameAndSSN();
                payload.setLastFourSocialSecurityNumber(userInfo.get("ucbcust_ssn_last_four").toString());
                payload.setCustomerLastNameBusiness(userInfo.get("ucbcust_last_name").toString());
                payload.setUsername(null);
                payload.setPassword(null);
                break;

            case TC_229__Positive__Last4SSN_____EmailAddress:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                // Fetch a valid customer with matching email + SSN
                userInfo =
                        ApplicationContext.get().getDbAction().getCustomerWithLastNameAndSSN();
                payload.setLastFourSocialSecurityNumber(userInfo.get("ucbcust_ssn_last_four").toString());

                payload.setEmailAddress(userInfo.get("GZBEMCP_EMAIL_ADDR").toString());
                payload.setUsername(null);
                payload.setPassword(null);
                break;

            case TC_230__Positive__Last4SSN_____PhoneNumber:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                // Fetch a valid customer with matching phone + SSN
                userInfo =
                        ApplicationContext.get().getDbAction().getCustomerWithLastNameAndSSN();
                String phoneNumber= userInfo.get("ucrtele_phone_area").toString()+userInfo.get("ucrtele_phone_number").toString();
                        payload.setPhoneNumber(phoneNumber);
                payload.setLastFourSocialSecurityNumber(userInfo.get("ucbcust_ssn_last_four").toString());
                payload.setUsername(null);
                payload.setPassword(null);
                break;


            case TC_231__Positive__Password_____Username:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                break;

            case TC_232__Positive__Password_____CustomerLastNameBusiness:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                userInfo =
                        ApplicationContext.get().getDbAction("mariadb").getRequiredAccountDetails();

                accountNo= userInfo.get("account_number").toString();
                customerCode = accountNo.substring(0, 9);
                premisesCode = accountNo.substring(9);

                userInfo =
                        ApplicationContext.get().getDbAction().getLastnameForCustomerCode(customerCode);


                payload.setCustomerLastNameBusiness(userInfo.get("ucbcust_last_name").toString());
                payload.setUsername(null);
                break;

            case TC_233__Positive__Password_____CustomerCode_____PremisesCode:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                userInfo =
                        ApplicationContext.get().getDbAction("mariadb").getRequiredAccountDetails();

                accountNo= userInfo.get("account_number").toString();
                customerCode = accountNo.substring(0, 9);
                premisesCode = accountNo.substring(9);
                payload.setCustomerCode(customerCode);
                payload.setPremisesCode(premisesCode);
                payload.setUsername(null);
                break;

            case TC_234__Positive__Password_____EmailAddress:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                payload.setEmailAddress("uzair.shafi@vertexone.ai");
               payload.setUsername(null);

                userInfo =
                        ApplicationContext.get().getDbAction("mariadb").getRequiredAccountDetails();

                accountNo= userInfo.get("account_number").toString();
                customerCode = accountNo.substring(0, 9);
                premisesCode = accountNo.substring(9);

                userInfo =
                        ApplicationContext.get().getDbAction().getEmailAddressFromCustCode(customerCode);

                payload.setEmailAddress(userInfo.get("GZBEMCP_EMAIL_ADDR").toString());
                break;

            case TC_235__Positive__Password_____PhoneNumber:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                userInfo =
                        ApplicationContext.get().getDbAction().getPhoneNumberFromDB();
                payload.setPhoneNumber(userInfo.get("phone_number").toString());
                payload.setPassword("UAT2@CustomerPass");
                payload.setUsername(null);
                break;

            case TC_236__Positive__BusinessName_____FederalTaxID:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerLastNameBusiness("WILSON WIRE & CABLE, INC");
                payload.setFederalTaxID("630854169");
                payload.setUsername(null);
                payload.setPassword(null);
                break;

            case TC_241__Positive__Username:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
//                payload.setCustomerCode("2370976");
//                payload.setPremisesCode("2392173");
                userInfo =
                        ApplicationContext.get().getDbAction("mariadb").getAccountNumber();
                String accNo = userInfo.get("account_number").toString();

                String ccode = accNo.substring(0, 9);          // first 9 characters
                String pcode = accNo.substring(9);             // everything after 9th character

                payload.setCustomerCode(ccode);
                payload.setPremisesCode(pcode);

                payload.setUsername(null);
                payload.setPassword("UAT2@CustomerPass");
                break;

            case TC_242__Positive__No_Username:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                while (true) {
                    userInfo = ApplicationContext.get().getDbAction().getInactiveAccountOnly2();
                    String custCode = userInfo.get("UCRACCT_CUST_CODE").toString();
                    String premCode = userInfo.get("UCRACCT_PREM_CODE").toString();
                    Map<String, Object> registeredAccount =
                            ApplicationContext.get().getDbAction("mariadb").getRegisteredAccount(custCode);
                    if (registeredAccount == null) {
                        userInfo = ApplicationContext.get().getDbAction().getUserAccountInfo(custCode);
                        payload.setCustomerCode(custCode);
                        payload.setPremisesCode(premCode);
                        break;
                    }
                }
                payload.setPassword("UAT2@CustomerPass");
                payload.setUsername(null);
                break;

            case TC_243__Positive__Nickname:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));

                userInfo = ApplicationContext.get().getDbAction().getAccountWithNickname();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(userInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setPassword("UAT2@CustomerPass");

                payload.setUsername(null);
                break;

            case TC_244__Positive__No_Nickname:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                userInfo = ApplicationContext.get().getDbAction().getAccountWithoutNickname();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(userInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setPassword("UAT2@CustomerPass");

                payload.setUsername(null);
                break;

            case TC_245__Positive__FirstName:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));

                // Fetch an account where firstName IS set
                userInfo =
                        ApplicationContext.get().getDbAction().getAccountWithFirstName();

                payload.setCustomerCode(userInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setPassword("UAT2@CustomerPass");

                payload.setUsername(null);
                break;

            case TC_246__Positive__No_FirstName:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));

                // Fetch an account where firstName is NOT set
               userInfo =
                        ApplicationContext.get().getDbAction().getAccountWithoutFirstName();

                payload.setCustomerCode(userInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setPassword("UAT2@CustomerPass");

                payload.setUsername(null);
                break;

            case TC_247__Positive__Residential:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));


                userInfo = ApplicationContext.get().getDbAction().getAccountWithNickname();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(userInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setPassword("UAT2@CustomerPass");

                payload.setUsername(null);

                break;

            case TC_248__Positive__Commercial:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));

                userInfo = ApplicationContext.get().getDbAction().getAccountWithoutNickname();
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(userInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setPassword("UAT2@CustomerPass");

                payload.setUsername(null);
//
            case TC_249__Positive__Industrial:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));

                // Fetch an Industrial account (customerType = 'IN')
                userInfo =
                        ApplicationContext.get().getDbAction().getIndustrialAccount();

                payload.setCustomerCode(userInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setPassword("UAT2@CustomerPass");

                payload.setUsername(null);
                break;

            case TC_250__Positive__Agriculture:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));

                userInfo =
                        ApplicationContext.get().getDbAction().getAgricultureAccount();

                payload.setCustomerCode(userInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setPassword("UAT2@CustomerPass");

                payload.setUsername(null);
                break;

            case TC_251__Positive__MultiFamily:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));

                // Fetch a Multi-Family account (customerType = 'MF')
                userInfo =
                        ApplicationContext.get().getDbAction().getMultiFamilyAccount();

                payload.setCustomerCode(userInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setPassword("UAT2@CustomerPass");

                payload.setUsername(null);
                break;


            case TC_252__Positive__Seasonal:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));

                // Fetch a Seasonal account (customerType = 'SE')
                userInfo =
                        ApplicationContext.get().getDbAction().getSeasonalAccount();

                payload.setCustomerCode(userInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setPassword("UAT2@CustomerPass");

                payload.setUsername(null);
                break;

            case TC_253__Positive__SeniorCitizen:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));

                // Fetch a Senior Citizen account (customerType = 'SR')
                userInfo =
                        ApplicationContext.get().getDbAction().getSeniorCitizenAccount();

                payload.setCustomerCode(userInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setPassword("UAT2@CustomerPass");

                payload.setUsername(null);
                break;

            case TC_254__Positive__PremisesAddress_StreetNumber:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));

                // Fetch an account where street number IS set
                userInfo =
                        ApplicationContext.get().getDbAction().getAccountWithStreetNumber();

                payload.setCustomerCode(userInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setPassword("UAT2@CustomerPass");

                payload.setUsername(null);
                break;

            case TC_255__Positive__PremisesAddress_No_StreetNumber:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));

                // Fetch an account where street number is NOT set
                userInfo =
                        ApplicationContext.get().getDbAction().getAccountWithoutStreetNumber();

                payload.setCustomerCode(userInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setPassword("UAT2@CustomerPass");

                payload.setUsername(null);
                break;

            case TC_256__Positive__PremisesAddress_StreetPreDirection:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));

                // Fetch an account where street pre-direction IS set
                userInfo =
                        ApplicationContext.get().getDbAction().getAccountWithStreetPreDirection();

                payload.setCustomerCode(userInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setPassword("UAT2@CustomerPass");

                payload.setUsername(null);
                break;

            case TC_257__Positive__PremisesAddress_No_StreetPreDirection:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));

                // Fetch an account where street pre-direction is NOT set
               userInfo =
                        ApplicationContext.get().getDbAction().getAccountWithoutStreetPreDirection();

                payload.setCustomerCode(userInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setPassword("UAT2@CustomerPass");

                payload.setUsername(null);
                break;

            case TC_258__Positive__PremisesAddress_StreetName:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));

                // Fetch an account where street name IS set
                userInfo =
                        ApplicationContext.get().getDbAction().getAccountWithStreetName();

                payload.setCustomerCode(userInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setPassword("UAT2@CustomerPass");

                payload.setUsername(null);
                break;

            case TC_259__Positive__PremisesAddress_No_StreetName:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));

                // Fetch an account where street name is NOT set
                userInfo =
                        ApplicationContext.get().getDbAction().getAccountWithoutStreetName();

                payload.setCustomerCode(userInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setPassword("UAT2@CustomerPass");

                payload.setUsername(null);
                break;

            case TC_260__Positive__PremisesAddress_StreetSuffix:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));

                // Fetch an account where street suffix IS set
                userInfo =
                        ApplicationContext.get().getDbAction().getAccountWithStreetSuffix();

                payload.setCustomerCode(userInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setPassword("UAT2@CustomerPass");

                payload.setUsername(null);
                break;

            case TC_261__Positive__PremisesAddress_No_StreetSuffix:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));

                // Fetch an account where street suffix is NOT set
                userInfo =
                        ApplicationContext.get().getDbAction().getAccountWithoutStreetSuffix();

                payload.setCustomerCode(userInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setPassword("UAT2@CustomerPass");

                payload.setUsername(null);
                break;

            case TC_262__Positive__PremisesAddress_StreetPostDirection:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));

                // Fetch an account where street post-direction IS set
                userInfo =
                        ApplicationContext.get().getDbAction().getAccountWithStreetPostDirection();

                payload.setCustomerCode(userInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setPassword("UAT2@CustomerPass");

                payload.setUsername(null);
                break;


            case TC_263__Positive__PremisesAddress_No_StreetPostDirection:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));

                userInfo =
                        ApplicationContext.get().getDbAction().getAccountWithoutStreetPostDirection();

                payload.setCustomerCode(userInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setPassword("UAT2@CustomerPass");

                payload.setUsername(null);
                break;

            case TC_264__Positive__PremisesAddress_UnitType:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(8));

                userInfo =
                        ApplicationContext.get().getDbAction().getAccountWithUnitType();

                payload.setCustomerCode(userInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setPassword("UAT2@CustomerPass");

                payload.setUsername(null);
                break;

            case TC_265__Positive__PremisesAddress_No_UnitType:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));

                // Fetch an account where unit type is NOT set
                userInfo =
                        ApplicationContext.get().getDbAction().getAccountWithoutUnitType();

                payload.setCustomerCode(userInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setPassword("UAT2@CustomerPass");

                payload.setUsername(null);
                break;

            case TC_266__Positive__PremisesAddress_UnitNumber:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));

                // Fetch an account where unit number IS set
                userInfo =
                        ApplicationContext.get().getDbAction().getAccountWithUnitNumber();

                payload.setCustomerCode(userInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setPassword("UAT2@CustomerPass");

                payload.setUsername(null);
                break;

            case TC_267__Positive__PremisesAddress_No_UnitNumber:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));

                // Fetch an account where unit number is NOT set
                userInfo =
                        ApplicationContext.get().getDbAction().getAccountWithoutUnitNumber();

                payload.setCustomerCode(userInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setPassword("UAT2@CustomerPass");

                payload.setUsername(null);
                break;

            case TC_268__Positive__PremisesAddress_City:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));

                // Fetch an account where city IS set
                userInfo =
                        ApplicationContext.get().getDbAction().getAccountWithCity();

                payload.setCustomerCode(userInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setPassword("UAT2@CustomerPass");

                payload.setUsername(null);
                break;

            case TC_269__Positive__PremisesAddress_No_City:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));

                // Fetch an account where city is NOT set
                userInfo =
                        ApplicationContext.get().getDbAction().getAccountWithoutCity();

                payload.setCustomerCode(userInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setPassword("UAT2@CustomerPass");

                payload.setUsername(null);
                break;

            case TC_270__Positive__PremisesAddress_State:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));

                // Fetch an account where state IS set
                userInfo =
                        ApplicationContext.get().getDbAction().getAccountWithState();

                payload.setCustomerCode(userInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setPassword("UAT2@CustomerPass");

                payload.setUsername(null);
                break;

            case TC_271__Positive__PremisesAddress_No_State:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));

                // Fetch an account where state is NOT set
                userInfo =
                        ApplicationContext.get().getDbAction().getAccountWithoutState();

                payload.setCustomerCode(userInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setPassword("UAT2@CustomerPass");

                payload.setUsername(null);
                break;

            case TC_272__Positive__PremisesAddress_ZipCode:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));

                // Fetch an account where zip code IS set
                userInfo =
                        ApplicationContext.get().getDbAction().getAccountWithZipCode();

                payload.setCustomerCode(userInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setPassword("UAT2@CustomerPass");

                payload.setUsername(null);
                break;

            case TC_273__Positive__PremisesAddress_No_ZipCode:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));

                // Fetch an account where zip code is NOT set
                userInfo =
                        ApplicationContext.get().getDbAction().getAccountWithoutZipCode();

                payload.setCustomerCode(userInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(userInfo.get("UCRACCT_PREM_CODE").toString());
                payload.setPassword("UAT2@CustomerPass");

                payload.setUsername(null);
                break;

            case TC_274__Positive__Search_Order__Active_______Final_______New_______Inactive,
                 TC_275__Positive__Search_Order__Active_______Final_______New,
                 TC_276__Positive__Search_Order__Active_______Final_______Inactive,
                 TC_277__Positive__Search_Order__Active_______Final,
                 TC_278__Positive__Search_Order__Active_______New_______Inactive,
                 TC_279__Positive__Search_Order__Active_______New,
                 TC_280__Positive__Search_Order__Active_______Inactive,
                 TC_281__Positive__Search_Order__Final_______New_______Inactive,
                 TC_282__Positive__Search_Order__Final_______New,
                 TC_283__Positive__Search_Order__Final_______Inactive,
                 TC_284__Positive__Search_Order__New_______Inactive,
                 TC_237__Positive__AccountStatus_Active,
                 TC_239__Positive__AccountStatus_Inactive,
                 TC_238__Positive__AccountStatus_Final,
                 TC_240__Positive__AccountStatus_New:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPassword("UAT2@CustomerPass");
                payload.setUsername(testContext.getUsername());
                break;

                case TC_285__Positive__Search_Order__Active_Name_order:
                    payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                    payload.setPassword("testActiveAccounts");
                    payload.setCustomerLastNameBusiness("BAILEY");
                    payload.setUsername(null);
                    break;

            case TC_286__Positive__Search_Order__Final_Name_order:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPassword("testFinalAccount1");
                payload.setCustomerLastNameBusiness("FRAZIER");
                payload.setUsername(null);
                break;

            case TC_287__Positive__Search_Order__New_Name_order:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPassword("NewStatusAcc");
                payload.setCustomerLastNameBusiness("ABBAS");
                payload.setUsername(null);
                break;

            case TC_288__Positive__Search_Order__Inactive_Name_order:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPassword("InactiveAccount");
                payload.setCustomerLastNameBusiness("ANDERSON");
                payload.setUsername(null);
                break;

            default:
                log.warn("Unhandled test condition: {}", testCondition);
                break;
        }
    }
}
