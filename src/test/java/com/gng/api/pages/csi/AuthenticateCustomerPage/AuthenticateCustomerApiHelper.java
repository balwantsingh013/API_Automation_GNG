package com.gng.api.pages.csi.AuthenticateCustomerPage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.AuthenticateCustomer.AuthenticateCustomerRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.AuthenticateCustomer.AuthenticateCustomerLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;

import java.util.Map;

@Slf4j
public class AuthenticateCustomerApiHelper {

    private final TestContext testContext;

    public AuthenticateCustomerApiHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    AuthenticateCustomerRequest preparePayload(AuthenticateCustomerLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        String jsonFileName = apiLabel.equals(AuthenticateCustomerLabel.authenticate_customer)
                ? AuthenticateCustomerLabel.authenticate_customer.toString()
                : AuthenticateCustomerLabel.authenticate_customer_mandatory.toString();
        return BasePage.deserializeJsonToPojo(jsonFileName, AuthenticateCustomerRequest.class);
    }

    public void preparePayloadForTestCondition(AuthenticateCustomerRequest payload, AuthenticateCustomerLabel testCondition) {
        Map<String, Object> accountInfo = null;
        switch (testCondition) {
            case TC_129__Negative__Missing_Request_ID:
                payload.setRequestID("");
                break;

            case TC_130__Negative__Invalid_Request_ID__Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(33));
                break;

            case TC_131__Negative__Duplicate_Request_ID:
                payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());
                break;

            case TC_132__Negative__Invalid_customerCode_Length:
                payload.setCustomerCode(FakerDataGenerator.generateDigits(10));
                break;

            case TC_133__Negative__Invalid_customerCode_Format:
                payload.setCustomerCode(FakerDataGenerator.generateAlphanumeric(6));
                break;

            case TC_134__Negative__Invalid_premisesCode_Length:
                payload.setPremisesCode(FakerDataGenerator.generateDigits(8));
                break;

            case TC_135__Negative__Invalid_premisesCode_Format:
                payload.setPremisesCode(FakerDataGenerator.generateAlphanumeric(6));
                break;

            case TC_136__Negative__Invalid_Last_or_Business_Name_Length:
                payload.setCustomerLastNameBusiness(FakerDataGenerator.generateString(61));
                break;

            case TC_137__Negative__Invalid_First_Name_Length:
                payload.setCustomerFirstName(FakerDataGenerator.generateString(16));
                break;

            case TC_138__Negative__Invalid_Last_Four_SSN_Length___Too_Long____:
                payload.setLastFourSocialSecurityNumber(FakerDataGenerator.generateDigits(5));
                break;

            case TC_139__Negative__Invalid_Last_Four_SSN_Length___Too_Short____:
                payload.setLastFourSocialSecurityNumber(FakerDataGenerator.generateDigits(3));
                break;

            case TC_140__Negative__Invalid_Federal_Tax_ID_Length___Too_Long____:
                payload.setFederalTaxID(FakerDataGenerator.generateDigits(10));
                break;

            case TC_141__Negative__Invalid_Federal_Tax_ID_Length___Too_Short____:
                payload.setFederalTaxID(FakerDataGenerator.generateDigits(8));
                break;

            case TC_142__Negative__Invalid_Email_Address_Format:
                payload.setEmailAddress(FakerDataGenerator.generateString(10));
                break;

            case TC_143__Negative__Invalid_Phone_Number_Length___Too_Long____:
                payload.setPhoneNumber(FakerDataGenerator.generateDigits(11));
                break;

            case TC_144__Negative__Invalid_Phone_Number_Length___Too_Short____:
                payload.setPhoneNumber(FakerDataGenerator.generateDigits(9));
                break;

            case TC_145__Negative__Invalid_Username_Format:
                payload.setUsername(FakerDataGenerator.generateAlphanumericWithSpecialChars(6));
                break;

            case TC_146__Negative__Inactive_Username:
                accountInfo = ApplicationContext.get().getDbAction("mariadb").getInactiveUser();
                payload.setUsername(accountInfo.get("user_name").toString());
                break;

            case TC_147__Negative__Invalid_Password_Format_Length___Too_Short____:
                payload.setPassword(FakerDataGenerator.generateString(7));
                break;

            case TC_148__Negative__Invalid_Password_Format_Length___Too_Long____:
                payload.setPassword(FakerDataGenerator.generateString(65));
                break;

            // Insufficient search criteria cases
            case TC_149__Negative__Insufficient_Search_Criteria:
            case TC_150__Negative__Last_4_SSN_____Password:
                payload.setLastFourSocialSecurityNumber(FakerDataGenerator.generateDigits(4));
                payload.setPassword(FakerDataGenerator.generateAlphanumeric(6));
                break;

            case TC_151__Negative__Last_4_SSN_____Username:
                payload.setLastFourSocialSecurityNumber(FakerDataGenerator.generateDigits(4));
                payload.setUsername(FakerDataGenerator.generateAlphanumeric(6));
                break;

            case TC_152__Negative__Last_4_SSN_____Customer_Code_____Premises_Code:
                payload.setLastFourSocialSecurityNumber(FakerDataGenerator.generateDigits(4));
                payload.setCustomerCode(FakerDataGenerator.generateDigits(6));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(6));
                break;

            case TC_153__Negative__Last_4_SSN_____Federal_Tax_ID_Number:
                payload.setLastFourSocialSecurityNumber(FakerDataGenerator.generateDigits(4));
                payload.setFederalTaxID(FakerDataGenerator.generateDigits(6));
                break;

            case TC_154__Negative__Password_____Federal_Tax_ID_Number:
                payload.setPassword(FakerDataGenerator.generateAlphanumeric(6));
                payload.setFederalTaxID(FakerDataGenerator.generateDigits(6));
                break;

            case TC_155__Negative__Customer_Last_Name_____Email_Address:
                payload.setCustomerLastNameBusiness(FakerDataGenerator.generateString(6));
                payload.setEmailAddress(FakerDataGenerator.generateEmail());
                break;

            case TC_159__Negative__Business_Name_____Email_Address:
                payload.setCustomerLastNameBusiness(FakerDataGenerator.generateString(6));
                payload.setEmailAddress(FakerDataGenerator.generateEmail());
                break;

            case TC_156__Negative__Customer_Last_Name_____Phone_Number:
                payload.setCustomerLastNameBusiness(FakerDataGenerator.generateString(6));
                payload.setPhoneNumber(FakerDataGenerator.generateDigits(10));
                break;

            case TC_160__Negative__Business_Name_____Phone_Number:
                payload.setCustomerLastNameBusiness(FakerDataGenerator.generateString(6));
                payload.setPhoneNumber(FakerDataGenerator.generateDigits(10));
                break;

            case TC_157__Negative__Customer_Last_Name_____Username:
                payload.setCustomerLastNameBusiness(FakerDataGenerator.generateString(6));
                payload.setUsername(FakerDataGenerator.generateString(6));
                break;

            case TC_161__Negative__Business_Name_____Username:
                payload.setCustomerLastNameBusiness(FakerDataGenerator.generateString(6));
                payload.setUsername(FakerDataGenerator.generateString(6));
                break;

            case TC_158__Negative__Customer_Last_Name_____Customer_Code_____Premises_Code:
                payload.setCustomerLastNameBusiness(FakerDataGenerator.generateString(6));
                payload.setCustomerCode(FakerDataGenerator.generateDigits(6));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(6));
                break;

            case TC_162__Negative__Business_Name_____Customer_Code_____Premises_Code:
                payload.setCustomerLastNameBusiness(FakerDataGenerator.generateString(6));
                payload.setCustomerCode(FakerDataGenerator.generateDigits(6));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(6));
                break;

            case TC_163__Negative__Email_Address_____Phone_Number:
                payload.setEmailAddress(FakerDataGenerator.generateEmail());
                payload.setPhoneNumber(FakerDataGenerator.generateDigits(10));
                break;

            case TC_164__Negative__Email_Address_____Username:
                payload.setEmailAddress(FakerDataGenerator.generateEmail());
                payload.setUsername(FakerDataGenerator.generateString(6));
                break;

            case TC_165__Negative__Email_Address_____Customer_Code_____Premises_Code:
                payload.setEmailAddress(FakerDataGenerator.generateEmail());
                payload.setCustomerCode(FakerDataGenerator.generateDigits(6));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(6));
                break;

            case TC_166__Negative__Email_Address_____Federal_Tax_ID_Number:
                payload.setEmailAddress(FakerDataGenerator.generateEmail());
                payload.setFederalTaxID(FakerDataGenerator.generateDigits(6));
                break;

            case TC_167__Negative__Phone_Number_____Username:
                payload.setPhoneNumber(FakerDataGenerator.generateDigits(10));
                payload.setUsername(FakerDataGenerator.generateString(6));
                break;

            case TC_168__Negative__Phone_Number_____Customer_Code_____Premises_Code:
                payload.setPhoneNumber(FakerDataGenerator.generateDigits(10));
                payload.setCustomerCode(FakerDataGenerator.generateDigits(6));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(6));
                break;

            case TC_169__Negative__Phone_Number_____Federal_Tax_ID_Number:
                payload.setPhoneNumber(FakerDataGenerator.generateDigits(10));
                payload.setFederalTaxID(FakerDataGenerator.generateDigits(6));
                break;

            case TC_170__Negative__Username_____Customer_Code_____Premises_Code:
                payload.setUsername(FakerDataGenerator.generateString(6));
                payload.setCustomerCode(FakerDataGenerator.generateDigits(6));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(6));
                break;

            case TC_171__Negative__Username_____Federal_Tax_ID_Number:
                payload.setUsername(FakerDataGenerator.generateString(6));
                payload.setFederalTaxID(FakerDataGenerator.generateDigits(6));
                break;

            case TC_172__Negative__Customer_Code_____Premises_Code_____Federal_Tax_ID_Number:
                payload.setCustomerCode(FakerDataGenerator.generateDigits(6));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(6));
                payload.setFederalTaxID(FakerDataGenerator.generateDigits(6));
                break;

            // Positive cases
            case TC_174__Positive__Last_4_SSN_____Customer_Last_Name:
                payload.setLastFourSocialSecurityNumber("1234");
                payload.setCustomerLastNameBusiness("Smith");
                break;

            case TC_175__Positive__Last_4_SSN_____Email_Address:
                payload.setLastFourSocialSecurityNumber("1234");
                payload.setEmailAddress("test@example.com");
                break;

            case TC_176__Positive__Last_4_SSN_____Phone_Number:
                payload.setLastFourSocialSecurityNumber("1234");
                payload.setPhoneNumber("1234567890");
                break;

            case TC_177__Positive__Password_____Username:
                payload.setPassword("password123");
                payload.setUsername("validUser");
                break;

            case TC_178__Positive__Password_____Customer_Last_Name:
                payload.setPassword("password123");
                payload.setCustomerLastNameBusiness("Smith");
                break;

            case TC_179__Positive__Password_____Business_Name:
                payload.setPassword("password123");
                payload.setCustomerLastNameBusiness("AcmeCorp");
                break;

            case TC_180__Positive__Password_____Customer_Code_____Premises_Code:
                payload.setPassword("password123");
                payload.setCustomerCode("123456789");
                payload.setPremisesCode("1234567");
                break;

            case TC_181__Positive__Password_____Email_Address:
                payload.setPassword("password123");
                payload.setEmailAddress("test@example.com");
                break;

            case TC_182__Positive__Password_____Phone_Number:
                payload.setPassword("password123");
                payload.setPhoneNumber("1234567890");
                break;

            case TC_183__Positive__Business_Name_____Federal_Tax_ID:
                payload.setCustomerLastNameBusiness("AcmeCorp");
                payload.setFederalTaxID("123456789");
                break;

            case TC_184__Positive__Login_ID_Saved:
                payload.setCustomerLastNameBusiness("AcmeCorp");
                payload.setFederalTaxID("123456789");
                payload.setLoginID("testLoginID");
                break;

            default:
                log.warn("Unhandled test condition: {}", testCondition);
                break;
        }
    }
}
