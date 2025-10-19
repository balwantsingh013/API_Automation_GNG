package com.gng.api.pages.turnOn.AccountsApiPages.SearchAccounts;


import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.AccountsPojo.SearchAccounts.SearchAccountsRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.turnOn.AccountsApiSteps.SearchAccounts.SearchAccountsApiLabel;
import com.gng.api.util.ExcelReader;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.gng.api.constants.DBConstant.UCRACCT_CUST_CODE;
import static com.gng.api.constants.DBConstant.UCRACCT_PREM_CODE;
import static com.gng.api.constants.GlobalEnums.TransactionType.TURN_ON;
import static com.gng.api.constants.TestConstant.*;
import static com.gng.api.steps.AesEncryption.AesEncryptionSteps.encryptData;
import static com.gng.api.steps.turnOn.AccountsApiSteps.SearchAccounts.SearchAccountsApiLabel.INACTIVE_UZBSSPP_STATUS_TC121E_2;


@Slf4j
public class SearchAccountsHelper {
    private final TestContext testContext;
    public String premisesCode;
    public String customerCode;

    public static final String USERNAME = "autotester";
    public static String ssn="666374706";

    public SearchAccountsHelper(TestContext testContext) {
        this.testContext = testContext;
    }


    SearchAccountsRequest preparePayload(SearchAccountsApiLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        String jsonFileName = apiLabel.equals(SearchAccountsApiLabel.search_accounts)
                ? SearchAccountsApiLabel.search_accounts.toString()
                : SearchAccountsApiLabel.search_accounts_mandatory.toString();
        return BasePage.deserializeJsonToPojo(jsonFileName, SearchAccountsRequest.class);
    }

    public void setRequestIDBasedOnTypeTestCondition(SearchAccountsRequest payload, SearchAccountsApiLabel testCondition) {
        switch (testCondition) {
            case NULL_REQUEST_ID_TC42:
                payload.setRequestID(null);
                payload.setLoginID(USERNAME);
                payload.setTransactionType(TURN_ON.getValue());
                break;
            case LONG_REQUEST_ID_TC43:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(35));
                payload.setLoginID(USERNAME);
                break;
            case DUPLICATE_REQUEST_ID_TC44:
                payload.setRequestID("3BC00A0397B14F29A313280EE0110941");
                payload.setTransactionType(TURN_ON.getValue());
                payload.setLoginID(USERNAME);
                break;
            default:
                payload.setRequestID(FakerDataGenerator.generateString(10));
        }
    }

    public void setLoginIDBasedOnTypeTC45_TC48(SearchAccountsRequest payload, SearchAccountsApiLabel loginID) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        switch (loginID) {
            case NULL_LOGIN_ID_TC45:
                payload.setLoginID("");
                payload.setTransactionType(TURN_ON.getValue());
                break;
            case ALPHANUMERIC_LOGIN_ID_TC47:
                payload.setLoginID(FakerDataGenerator.generateAlphanumericWithSpecialChars(8));
                break;
            case MAX_LENGTH_LOGIN_ID_TC46:
                payload.setLoginID(FakerDataGenerator.getRandomNumericString(35));
                break;
            case INVALID_LOGIN_ID_NOT_PRESENT_USER_TABLE_TC48:
                payload.setLoginID("dummy");
                payload.setCustomerLastName("Doe");
                payload.setTransactionType(TURN_ON.getValue());
                payload.setPremisesZipCode("30214");
                break;
            default:
                payload.setLoginID(FakerDataGenerator.generateLowerCaseString(10));
        }
    }

    public void setCustomerCodeBasedOnTypeTC49(SearchAccountsRequest payload, SearchAccountsApiLabel customerCode) {
        switch (customerCode) {
            case MAX_LENGTH_CUSTOMER_CODE_TC49:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setCustomerCode(FakerDataGenerator.generateLowerCaseString(15));
                break;
            default:
                payload.setCustomerCode(FakerDataGenerator.generateLowerCaseString(9));
        }
    }

    public void setPremisesCodeBasedOnTypeTC50(SearchAccountsRequest payload, SearchAccountsApiLabel premisesCode) {
        switch (premisesCode) {
            case MAX_LENGTH_PREMISES_CODE_TC50:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setPremisesCode(FakerDataGenerator.generateLowerCaseString(8));
                break;
            default:
                payload.setPremisesCode(FakerDataGenerator.generateLowerCaseString(7));
        }
    }

    public void setTransactionTypeBasedOnTypeTC51_TC52(SearchAccountsRequest payload, SearchAccountsApiLabel transactionType) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        switch (transactionType) {
            case MISSING_TRANSACTION_TYPE_TC51:
                payload.setLoginID(USERNAME);
                payload.setTransactionType(null);
                break;
            case INVALID_TRANSACTION_TYPE_TC52:
                payload.setLoginID(USERNAME);
                payload.setTransactionType("TNOF");
                break;
            default:
                payload.setTransactionType(FakerDataGenerator.generateUpperCaseString(4));
        }
    }

    public void setCustomerBusinessNameBasedOnTypeTC53(SearchAccountsRequest payload, SearchAccountsApiLabel customerBusinessName) {
        switch (customerBusinessName) {
            case MAX_LENGTH_BUSINESS_NAME_TC53:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setCustomerBusinessName(FakerDataGenerator.generateLowerCaseString(62));
                break;
            default:
                payload.setCustomerBusinessName(FakerDataGenerator.generateLowerCaseString(50));
        }
    }

    public void setLastNameBasedOnTypeTC54(SearchAccountsRequest payload, SearchAccountsApiLabel customerLastName) {
        switch (customerLastName) {
            case MAX_LENGTH_LAST_NAME_TC54:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setCustomerLastName(FakerDataGenerator.generateLowerCaseString(62));
                break;
            default:
                payload.setCustomerLastName(FakerDataGenerator.generateLowerCaseString(50));
        }
    }

    public void setFirstNameBasedOnTypeTC55(SearchAccountsRequest payload, SearchAccountsApiLabel customerFirstName) {
        switch (customerFirstName) {
            case MAX_LENGTH_FIRST_NAME_TC55:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setCustomerFirstName(FakerDataGenerator.generateLowerCaseString(17));
                break;
            default:
                payload.setCustomerFirstName(FakerDataGenerator.generateLowerCaseString(14));
        }
    }

    public void setSocialSecurityNumberBasedOnTypeTC56_TC57(SearchAccountsRequest payload, SearchAccountsApiLabel socialSecurityNumber) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setLoginID(USERNAME);
        switch (socialSecurityNumber) {
            case NOT_ENCRYPTED_SOCIAL_SECURITY_NUMBER_TC56:
                payload.setSocialSecurityNumber(FakerDataGenerator.getRandomNumericString(9));
                break;
            case ENCRYPTED_SOCIAL_SECURITY_NUMBER_TC57:
                payload.setSocialSecurityNumber(encryptData(FakerDataGenerator.generateAlphanumericWithSpecialChars(9)));
                break;
            default:
                payload.setSocialSecurityNumber(FakerDataGenerator.generateLowerCaseString(9));
        }
    }

    public void setFederalTaxIDBasedOnTypeTC58_TC59(SearchAccountsRequest payload, SearchAccountsApiLabel federalTaxID) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setLoginID(USERNAME);
        switch (federalTaxID) {
            case NOT_ENCRYPTED_FEDERAL_TAX_ID_TC58:
                payload.setFederalTaxID(FakerDataGenerator.getRandomNumericString(9));
                break;
            case ENCRYPTED_FEDERAL_TAX_ID_TC59:
                payload.setFederalTaxID(encryptData(FakerDataGenerator.generateAlphanumericWithSpecialChars(9)));
                break;
            default:
                payload.setFederalTaxID(FakerDataGenerator.generateLowerCaseString(9));
        }
    }

    public void setPhoneNumberBasedOnTypeTC60(SearchAccountsRequest payload, SearchAccountsApiLabel phoneNumber) {
        switch (phoneNumber) {
            case MAX_LENGTH_PHONE_NUMBER_TC60:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setPhoneNumber(FakerDataGenerator.getRandomNumericString(11));
                break;
            default:
                payload.setPhoneNumber(FakerDataGenerator.getRandomNumericString(10));
        }
    }


    public void setAGLCAccountNumberBasedOnTypeTC61(SearchAccountsRequest payload, SearchAccountsApiLabel aglcAccountNumber) {
        switch (aglcAccountNumber) {
            case MAX_LENGTH_AGLC_ACCOUNT_NUMBER_TC61:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setAglcAccountNumber(FakerDataGenerator.getRandomNumericString(21));
                break;
            default:
                payload.setAglcAccountNumber(FakerDataGenerator.getRandomNumericString(20));
        }
    }

    public void setPremisesStreetNumberBasedOnTypeTC62(SearchAccountsRequest payload, SearchAccountsApiLabel premisesStreetNumber) {
        switch (premisesStreetNumber) {
            case MAX_LENGTH_PREMISES_STREET_NUMBER_TC62:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setPremisesStreetNumber(FakerDataGenerator.getRandomNumericString(14));
                break;
            default:
                payload.setPremisesStreetNumber(FakerDataGenerator.getRandomNumericString(12));
        }
    }

    public void setPremisesStreetPreDirectionBasedOnTypeTC63(SearchAccountsRequest payload, SearchAccountsApiLabel premisesStreetPreDirection) {
        switch (premisesStreetPreDirection) {
            case MAX_LENGTH_PREMISES_STREET_PRE_DIRECTION_TC63:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setPremisesStreetPreDirection(FakerDataGenerator.generateUpperCaseString(3));
                break;
            default:
                payload.setPremisesStreetPreDirection(FakerDataGenerator.generateUpperCaseString(2));
        }
    }

    public void setPremisesStreetNameBasedOnTypeTC64(SearchAccountsRequest payload, SearchAccountsApiLabel premisesStreetName) {
        switch (premisesStreetName) {
            case MAX_LENGTH_PREMISES_STREET_NAME_TC64:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setPremisesStreetName(FakerDataGenerator.getRandomString(32));
                break;
            default:
                payload.setPremisesStreetName(FakerDataGenerator.getRandomString(30));
        }
    }

    public void setPremisesStreetSuffixBasedOnTypeTC65(SearchAccountsRequest payload, SearchAccountsApiLabel premisesStreetSuffix) {
        switch (premisesStreetSuffix) {
            case MAX_LENGTH_PREMISES_STREET_SUFFIX_TC65:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setPremisesStreetSuffix(FakerDataGenerator.getRandomString(32));
                break;
            default:
                payload.setPremisesStreetSuffix(FakerDataGenerator.getRandomString(30));
        }
    }

    public void setPremisesStreetPostDirectionBasedOnTypeTC66(SearchAccountsRequest payload, SearchAccountsApiLabel premisesStreetPostDirection) {
        switch (premisesStreetPostDirection) {
            case MAX_LENGTH_PREMISES_STREET_POST_DIRECTION_TC66:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setPremisesStreetPostDirection(FakerDataGenerator.generateUpperCaseString(3));
                break;
            default:
                payload.setPremisesStreetPostDirection(FakerDataGenerator.generateUpperCaseString(2));
        }
    }

    public void setPremisesUnitTypeBasedOnTypeTC67(SearchAccountsRequest payload, SearchAccountsApiLabel premisesUnitType) {
        switch (premisesUnitType) {
            case MAX_LENGTH_PREMISES_UNIT_TYPE_TC67:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setPremisesUnitType(FakerDataGenerator.generateUpperCaseString(7));
                break;
            default:
                payload.setPremisesUnitType(FakerDataGenerator.generateUpperCaseString(6));
        }
    }

    public void setPremisesUnitNumberBasedOnTypeTC68(SearchAccountsRequest payload, SearchAccountsApiLabel premisesUnitNumber) {
        switch (premisesUnitNumber) {
            case MAX_LENGTH_PREMISES_UNIT_NUMBER_TC68:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setPremisesUnitNumber(FakerDataGenerator.getRandomNumericString(7));
                break;
            default:
                payload.setPremisesUnitType(FakerDataGenerator.getRandomNumericString(6));
        }
    }

    public void setPremisesCityBasedOnTypeTC69(SearchAccountsRequest payload, SearchAccountsApiLabel premisesCity) {
        switch (premisesCity) {
            case MAX_LENGTH_PREMISES_CITY_TC69:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setPremisesCity(FakerDataGenerator.generateLowerCaseString(22));
                break;
            default:
                payload.setPremisesCity(FakerDataGenerator.generateLowerCaseString(20));
        }
    }

    public void setPremisesStateCodeBasedOnTypeTC70(SearchAccountsRequest payload, SearchAccountsApiLabel premisesStateCode, String invalidStateCode) {
        switch (premisesStateCode) {
            case MAX_LENGTH_PREMISES_STATE_CODE_TC70:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setTransactionType(TURN_ON.getValue());
                payload.setPremisesStateCode(invalidStateCode);
                break;
            default:
                payload.setPremisesStateCode(FakerDataGenerator.generateUpperCaseString(3));
        }
    }

    public void setPremisesZipCodeBasedOnTypeTC71(SearchAccountsRequest payload, String invalidPremiseZipCode, SearchAccountsApiLabel premisesZipCode) {
        switch (premisesZipCode) {
            case MAX_LENGTH_PREMISES_ZIP_CODE_TC71:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setTransactionType(TURN_ON.getValue());
                payload.setPremisesZipCode(invalidPremiseZipCode);
                break;
            default:
                payload.setPremisesZipCode(FakerDataGenerator.getRandomNumericString(5));
        }

    }

    public void setMissingSearchFieldsBasedOnType(SearchAccountsRequest payload, SearchAccountsApiLabel testCondition) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setLoginID(USERNAME);
        payload.setTransactionType(TURN_ON.getValue());
        payload.setAglcAccountNumber("");
        payload.setCustomerBusinessName("");
        payload.setCustomerLastName("");
        payload.setSocialSecurityNumber("");
        payload.setPremisesZipCode("");
        payload.setFederalTaxID("");
        payload.setPhoneNumber("");
        switch (testCondition) {
            case ONLY_PREMISES_CODE_PROVIDED_TC73:
                payload.setPremisesCode(FakerDataGenerator.getRandomNumericString(7));
                break;
            case ONLY_CUSTOMER_CODE_PROVIDED_TC74:
                payload.setCustomerCode(FakerDataGenerator.getRandomNumericString(7));
                break;
            case ONLY_CUSTOMER_FIRST_NAME_PROVIDED_TC75:
                payload.setCustomerFirstName(FakerDataGenerator.generateFirstName());
                break;
            case ONLY_CUSTOMER_LAST_NAME_PROVIDED_TC76:
                payload.setCustomerLastName(FakerDataGenerator.generateLastName());
                break;
            case SSN_PROVIDED_FEDERAL_TAX_ID_PROVIDE_TC77:
                payload.setSocialSecurityNumber("YUiEKcZGFmS59Uu1aacc2tTqSPYw9QYAv97gTfGyhaI=");
                payload.setFederalTaxID("YUiEKcZGFmS59Uu1aacc2tTqSPYw9QYAv97gTfGyhaI=");
                break;
            case ONLY_PREMISES_STREET_NUMBER_PROVIDED_TC78:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setPremisesStreetNumber("25");
                break;
            case ONLY_PREMISES_STREET_PRE_DIRECTION_PROVIDED_TC79:
                payload.setPremisesStreetPreDirection("SE");
                break;
            case ONLY_PREMISES_STREET_SUFFIX_PROVIDED_TC80:
                payload.setPremisesStreetSuffix(FakerDataGenerator.generateUpperCaseString(2));
                break;
            case PREMISES_STREET_POST_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC81:
                payload.setPremisesStreetPostDirection(FakerDataGenerator.generateUpperCaseString(2));
                break;
            case PREMISES_STREET_NUMBER_PREMISES_STREET_PRE_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC82:
                payload.setPremisesStreetNumber(FakerDataGenerator.getRandomNumericString(2));
                payload.setPremisesStreetPreDirection("SW");
            case PREMISES_STREET_NUMBER_PREMISES_STREET_SUFFIX_PROVIDED_PREMISES_STREET_NAME_MISSING_TC83:
                payload.setPremisesStreetNumber(FakerDataGenerator.getRandomNumericString(2));
                payload.setPremisesStreetSuffix(FakerDataGenerator.generateUpperCaseString(2));
                break;
            case PREMISES_STREET_NUMBER_PREMISES_STREET_POST_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC84:
                payload.setPremisesStreetNumber(FakerDataGenerator.getRandomNumericString(2));
                payload.setPremisesStreetPostDirection(FakerDataGenerator.generateUpperCaseString(2));
                break;
            case PREMISES_STREET_PRE_DIRECTION_PREMISES_STREET_SUFFIX_PROVIDED_PREMISES_STREET_NAME_MISSING_TC85:
                payload.setPremisesStreetSuffix("RD");
                payload.setPremisesStreetPreDirection("SE");
                break;
            case PREMISES_STREET_PRE_DIRECTION_PREMISES_STREET_POST_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC86:
                payload.setPremisesStreetPostDirection("SE");
                payload.setPremisesStreetPreDirection("SW");
            case PREMISES_STREET_SUFFIX_PREMISES_STREET_POST_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC87:
                payload.setPremisesStreetPostDirection(FakerDataGenerator.generateUpperCaseString(2));
                payload.setPremisesStreetSuffix(FakerDataGenerator.generateUpperCaseString(2));
                break;
            case PREMISES_STREET_NUMBER_SUFFIX_PRE_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC88:
                payload.setPremisesStreetNumber(FakerDataGenerator.getRandomNumericString(2));
                payload.setPremisesStreetSuffix("RD");
                payload.setPremisesStreetPreDirection("SW");
                break;
            case PREMISES_STREET_NUMBER_SUFFIX_POST_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC89:
                payload.setPremisesStreetNumber(FakerDataGenerator.getRandomNumericString(2));
                payload.setPremisesStreetSuffix(FakerDataGenerator.generateUpperCaseString(2));
                payload.setPremisesStreetPostDirection(FakerDataGenerator.generateUpperCaseString(2));
                break;
            case PREMISES_PRE_DIRECTION_SUFFIX_POST_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC90:
                payload.setPremisesStreetPreDirection("SE");
                payload.setPremisesStreetSuffix("RD");
                payload.setPremisesStreetPostDirection("SW");
                break;
            case PREMISES_STREET_NUMBER_PRE_DIRECTION_POST_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC91:
                payload.setPremisesStreetNumber(FakerDataGenerator.getRandomNumericString(2));
                payload.setPremisesStreetPreDirection("SE");
                payload.setPremisesStreetPostDirection("SW");
                break;
            case PREMISES_STREET_NUMBER_SUFFIX_PRE_DIRECTION_POST_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC92:
                payload.setPremisesStreetNumber(FakerDataGenerator.getRandomNumericString(2));
                payload.setPremisesStreetPreDirection("SE");
                payload.setPremisesStreetSuffix("RD");
                payload.setPremisesStreetPostDirection("SW");
                break;
            case PREMISES_STREET_NAME_PROVIDED_CITY_ZIP_STATE_CODE_MISSING_TC93:
                payload.setPremisesStreetName(FakerDataGenerator.generateString(3));
                break;
            case PREMISES_CITY_PROVIDED_STREET_NAME_ZIP_STATE_CODE_MISSING_TC94:
                payload.setPremisesCity(FakerDataGenerator.generateLowerCaseString(3));
                break;
            case PREMISES_STATE_CODE_PROVIDED_STREET_NAME_ZIP_CITY_MISSING_TC95:
                payload.setPremisesStateCode(FakerDataGenerator.generateUpperCaseString(3));
                break;
            case PREMISES_ZIP_CODE_PROVIDED_STREET_NAME_ZIP_STATE_MISSING_TC96:
                payload.setPremisesZipCode("30052");
                break;
            case PREMISES_STREET_NAME_CITY_PROVIDED_ZIP_STATE_MISSING_TC97:
                payload.setPremisesStreetName(FakerDataGenerator.generateString(3));
                payload.setPremisesCity(FakerDataGenerator.generateLowerCaseString(3));
                break;
            case PREMISES_STREET_NAME_STATE_PROVIDED_ZIP_CITY_MISSING_TC98:
                payload.setPremisesStreetName(FakerDataGenerator.generateString(3));
                payload.setPremisesStateCode(FakerDataGenerator.generateUpperCaseString(3));
                break;
            case PREMISES_STREET_NAME_ZIP_PROVIDED_STATE_CITY_MISSING_TC99:
                payload.setPremisesStreetName(FakerDataGenerator.generateLowerCaseString(10));
                payload.setPremisesZipCode("30052");
                break;
            case PREMISES_STATE_CITY_PROVIDED_STREET_NAME_ZIP_MISSING_TC100:
                payload.setPremisesStateCode(FakerDataGenerator.generateUpperCaseString(3));
                payload.setPremisesCity(FakerDataGenerator.generateLowerCaseString(3));
                break;
            case PREMISES_STATE_CITY_ZIP_PROVIDED_STREET_NAME_STATE_MISSING_TC101:
                payload.setPremisesCity(FakerDataGenerator.generateLowerCaseString(8));
                payload.setPremisesZipCode("30052");
                break;
            case PREMISES_STATE_ZIP_PROVIDED_STREET_NAME_CITY_MISSING_TC102:
                payload.setPremisesStateCode("GA");
                payload.setPremisesZipCode("30052");
                break;
            case PREMISES_STATE_STREET_NAME_CITY_PROVIDED_ZIP_MISSING_TC103:
                payload.setPremisesStreetName(FakerDataGenerator.generateString(3));
                payload.setPremisesCity(FakerDataGenerator.generateString(6));
                payload.setPremisesStateCode("GA");
                break;
            case PREMISES_STATE_STREET_NAME_PROVIDED_CITY_MISSING_TC104:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setPremisesStreetName(FakerDataGenerator.generateString(3));
                payload.setPremisesCity("");
                payload.setPremisesStateCode(FakerDataGenerator.generateUpperCaseString(2));
                payload.setPremisesZipCode("30052");
                break;
            case PREMISES_ZIP_STREET_NAME_CITY_PROVIDED_STATE_MISSING_TC105:
                payload.setPremisesStreetName(FakerDataGenerator.generateString(3));
                payload.setPremisesCity(FakerDataGenerator.generateString(6));
                payload.setPremisesZipCode("30052");
                break;
            case PREMISES_ZIP_STATE_CITY_PROVIDED_STREET_NAME_MISSING_TC106:
                payload.setPremisesCity(FakerDataGenerator.generateLowerCaseString(6));
                payload.setPremisesStateCode("GA");
                payload.setPremisesZipCode("30052");
                break;
            default:
                break;
        }
    }

    //positive test cases


    public void setInvalidCustomerCode(SearchAccountsRequest payload, String customerCode, String premisesCode) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setCustomerCode(customerCode);
        payload.setPremisesCode(premisesCode);
    }

    public void validateCustomerCodeAndPremisesCodeInDB(String customerCode, String premisesCode) {
        Map<String, Object> customerCodeInDatabase= ApplicationContext.get().getDbAction().searchForCustomerCodeAndPremisesCodeInDatabase(customerCode, premisesCode);
        Assert.assertTrue(customerCodeInDatabase.isEmpty());
    }

    public void verifyTheCountOfRecordsRetrivedFromDBIsMoreThan30(String customerBusinessName) {
        Long countOfRecordsBasedOnBusinessName= ApplicationContext.get().getDbAction().searchForCustomerBusinessNameInDatabase(customerBusinessName);
        Assert.assertTrue(countOfRecordsBasedOnBusinessName>30);
    }

    public void setLastNameFirstNameAndZiPBType(SearchAccountsRequest payload) {
        Map<String, Object> data = ApplicationContext.get().getDbAction().lastNameFirstNameTC112Query();

        String premisesZipCode = data.get("PREMISESZIPCODE").toString();
        String customerLastName = data.get("CUSTOMERLASTNAMEBUSINESS").toString();
        String customerFirstName = data.get("CUSTOMERFIRSTNAME").toString();
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setLoginID(USERNAME);
        payload.setPremisesZipCode(premisesZipCode);
        payload.setCustomerLastName(customerLastName);
        payload.setCustomerFirstName(customerFirstName);
    }

    public void setaglcAccountNumberType(SearchAccountsRequest payload) {
        Map<String, Object> data = ApplicationContext.get().getDbAction().aglcAccountNumberETypeNoSSPTC114Query();

        String aglcAccountNumber = data.get("aglcAccountNumber").toString();

        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setLoginID(USERNAME);
        payload.setAglcAccountNumber(aglcAccountNumber);
    }

    public void setCustomerDataETypeSSP(SearchAccountsRequest payload) {
        Map<String, Object> data = ApplicationContext.get().getDbAction().customerDataWithETypeTC115Query();

        String premisesStreetNumber = data.get("PREMISESSTREETNUMBER").toString();
        String premisesStreetPreDirection =
                data.get("PREMISESSTREETPREDIRECTION") != null
                        ? data.get("PREMISESSTREETPREDIRECTION").toString()
                        : "";
        String premisesStreetName = data.get("PREMISESSTREETNAME").toString();
        String premisesStreetSuffix = data.get("PREMISESSTREETSUFFIX").toString();
        String premisesStreetPostDirection =
                data.get("PREMISESSTREETPOSTDIRECTION") != null
                        ? data.get("PREMISESSTREETPOSTDIRECTION").toString()
                        : "";
        String premisesUnitType =
                data.get("PREMISESUNITTYPE") != null
                        ? data.get("PREMISESUNITTYPE").toString()
                        : "";
        String premisesCity = data.get("PREMISESCITY").toString();
        String premisesStateCode = data.get("PREMISESSTATECODE").toString();
        String premisesZipCode = data.get("PREMISESZIPCODE").toString();

        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setLoginID(USERNAME);
        payload.setPremisesStreetNumber(premisesStreetNumber);
        payload.setPremisesStreetPreDirection(premisesStreetPreDirection);
        payload.setPremisesStreetName(premisesStreetName);
        payload.setPremisesStreetSuffix(premisesStreetSuffix);
        payload.setPremisesStreetPostDirection(premisesStreetPostDirection);
        payload.setPremisesUnitType(premisesUnitType);
        payload.setPremisesCity(premisesCity);
        payload.setPremisesStateCode(premisesStateCode);
        payload.setPremisesZipCode(premisesZipCode);

    }

    public void setAccountNumberSearchETypeNoSSP(SearchAccountsRequest payload, String sspIndicator) {
        Map<String, Object> accountNumberData = ApplicationContext.get().getDbAction().accountNumberSearchETypeNoSSPDBTC110Query(sspIndicator);
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setCustomerCode(accountNumberData.get("UZBENRO_CUST_CODE").toString());
        payload.setPremisesCode(accountNumberData.get("UZBENRO_PREM_CODE").toString());
    }

    public void setEnrollmentRecordsBasedOnTheProvidedPhoneNumber(SearchAccountsRequest payload, SearchAccountsApiLabel testCondition) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        String telecode="";
        String status="";
        switch (testCondition) {
            case SEARCH_BASED_ON_PHONE_BI_A_TC_119_1 -> {
                telecode = "BI";
                status = "A";
            }
            case SEARCH_BASED_ON_PHONE_BI_I_TC_119_2 -> {
                telecode = "BI";
                status = "I";
            }
            case SEARCH_BASED_ON_PHONE_BU_A_TC_119_3 -> {
                telecode = "BU";
                status = "A";
            }
            case SEARCH_BASED_ON_PHONE_BU_I_TC_119_4 -> {
                telecode = "BU";
                status = "I";
            }
        }
        Map<String, Object>  phoneNumber = ApplicationContext.get().getDbAction().getPhoneNumber(telecode,status);
        payload.setPhoneNumber(phoneNumber.get("UCRTELE_PHONE_AREA").toString()+phoneNumber.get("UCRTELE_PHONE_NUMBER").toString());
    }

    public void setBusinessName(SearchAccountsRequest payload) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        Map<String, Object>  customerBusinessName = ApplicationContext.get().getDbAction().getCustomBusnsNm_ForActPenRewardCommercialAccount();
        payload.setCustomerBusinessName(customerBusinessName.get("UCBCUST_LAST_NAME").toString());
    }


    public void setWildcardSearch(SearchAccountsRequest payload) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        Map<String, Object>  lastNameFirstNameZip = ApplicationContext.get().getDbAction().getAccountDetails_ForResidentialOrSeniorResAccount("RS");
        payload.setPremisesZipCode(lastNameFirstNameZip.get("UCRADDR_ZIP").toString());
        payload.setCustomerLastName(lastNameFirstNameZip.get("UZBENRO_DSM_LAST_NAME").toString().substring(0, 3));
        payload.setCustomerFirstName(lastNameFirstNameZip.get("UZBENRO_DSM_FIRST_NAME").toString().substring(0, 2));
    }

    public void setWildcardSearchWithCity(SearchAccountsRequest payload){
        payload.setRequestID(FakerDataGenerator.generateString(10));
        Map<String, Object>  streetNameCityStateZipData = ApplicationContext.get().getDbAction().getStreetCityStateZipDetails();
        payload.setPremisesStreetName(streetNameCityStateZipData.get("UCRADDR_STREET_NAME").toString());
        payload.setPremisesCity(streetNameCityStateZipData.get("UCRADDR_CITY").toString().substring(0, 2));
        payload.setPremisesStateCode(streetNameCityStateZipData.get("UCRADDR_STAT_CODE").toString());
        payload.setPremisesZipCode(streetNameCityStateZipData.get("UCRADDR_ZIP").toString());
    }

    public void setPartialPayment(SearchAccountsRequest payload) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        Map<String, Object> accountNumberData = ApplicationContext.get().getDbAction().getAccountDetails_ForPastDueBalanceAndPartialPayment();
        payload.setCustomerCode(accountNumberData.get("GZBRTPP_CUST_CODE").toString());
        payload.setPremisesCode(accountNumberData.get("GZBRTPP_PREM_CODE").toString());
    }

    public void setFullPayment(SearchAccountsRequest payload) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setLoginID(USERNAME);
        payload.setPremisesCode("5198736");
        payload.setCustomerCode("5221058");
    }

    public void setPayloadForPrevSavedEnrollment(SearchAccountsRequest payload, SearchAccountsApiLabel testCondition){
        payload.setRequestID(FakerDataGenerator.generateString(10));
        String customerCode = testContext.getGetEligiblePlansAndOffersResponse().getData().getCustomerCode();
        String premisesCode = testContext.getGetEligiblePlansAndOffersResponse().getData().getPremisesCode();
        payload.setTransactionType(TURN_ON.getValue());
        payload.setCustomerCode(customerCode);
        payload.setPremisesCode(premisesCode);
    }

    public void setNoPayment(SearchAccountsRequest payload) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        Map<String, Object> accountNumberData = ApplicationContext.get().getDbAction().getAccountDetails_ForPastDueBalanceAndNoPayment();
        payload.setCustomerCode(accountNumberData.get("GZBRTPP_CUST_CODE").toString());
        payload.setPremisesCode(accountNumberData.get("GZBRTPP_PREM_CODE").toString());
    }

    public void setMultiplePayments(SearchAccountsRequest payload) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setLoginID(USERNAME);
        payload.setPremisesCode("5730863");
        payload.setCustomerCode("5549929");
    }


    public void setAccountNumberSearchWithoutSSPBasedOnTypeTC109(SearchAccountsRequest payload, String sspIndicator) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        Map<String, Object>  custPremCodeNoSSP = ApplicationContext.get().getDbAction().custCodePremCodeNoSSPAccount(sspIndicator);
        payload.setCustomerCode(custPremCodeNoSSP.get("UZBENRO_CUST_CODE").toString());
        payload.setPremisesCode(custPremCodeNoSSP.get("UZBENRO_PREM_CODE").toString());
    }

    public void setLastNameAndZiPBType(SearchAccountsRequest payload, String sspIndicator) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        Map<String, Object>  lastNameZipNoSSP = ApplicationContext.get().getDbAction().lastNameZipNoSSPAccount(sspIndicator);
        payload.setCustomerLastName(lastNameZipNoSSP.get("UZBENRO_DSM_LAST_NAME").toString());
        payload.setPremisesZipCode(lastNameZipNoSSP.get("UCRADDR_ZIP").toString());
    }

    public void setStreetNameCityStateZip(SearchAccountsRequest payload) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        Map<String, Object>  streetNameCityStateZipData = ApplicationContext.get().getDbAction().getStreetCityStateZipDetails();
        payload.setPremisesStreetName(streetNameCityStateZipData.get("UCRADDR_STREET_NAME").toString());
        payload.setPremisesCity(streetNameCityStateZipData.get("UCRADDR_CITY").toString());
        payload.setPremisesStateCode(streetNameCityStateZipData.get("UCRADDR_STAT_CODE").toString());
        payload.setPremisesZipCode(streetNameCityStateZipData.get("UCRADDR_ZIP").toString());
    }

    public void setAddressDetailsWithPreDirection(SearchAccountsRequest payload) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        Map<String, Object>  addressDetails = ApplicationContext.get().getDbAction().getAddressDetails();
        payload.setPremisesStreetName(addressDetails.get("UCRADDR_STREET_NAME").toString());
        payload.setPremisesCity(addressDetails.get("UCRADDR_CITY").toString());
        payload.setPremisesStateCode(addressDetails.get("UCRADDR_STAT_CODE").toString());
        payload.setPremisesZipCode(addressDetails.get("UCRADDR_ZIP").toString());
        payload.setPremisesStreetNumber(addressDetails.get("UCRADDR_STREET_NUMBER").toString());
        payload.setPremisesStreetNumber(addressDetails.get("UCRADDR_PDIR_CODE_PRE").toString());
        payload.setPremisesStreetNumber(addressDetails.get("UCRADDR_PDIR_CODE_POST").toString());
        payload.setPremisesStreetNumber(addressDetails.get("UCRADDR_SSFX_CODE").toString());
    }

    public void setReturnedRecordsExceedsPSTOValue(SearchAccountsRequest payload, String customerBusinessName) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setCustomerBusinessName(customerBusinessName);
    }

    public void setSSPParticipantCodeBasedOnTypeTC121e(SearchAccountsRequest payload, SearchAccountsApiLabel testCondition) {
        String status="A";
        if(testCondition.equals(INACTIVE_UZBSSPP_STATUS_TC121E_2)){
            status="I";
        }
        payload.setRequestID(FakerDataGenerator.generateString(10));
        Map<String, Object>  custPremCodeSSPParticipantCode = ApplicationContext.get().getDbAction().custCodePremCodeSSPParticipantCodeAccount(status);
        payload.setCustomerCode(custPremCodeSSPParticipantCode.get("UZRSSPA_CUST_CODE").toString());
        payload.setPremisesCode(custPremCodeSSPParticipantCode.get("UZRSSPA_PREM_CODE").toString());
    }

    public void setCustCodePremCodeBasedOnTypeTC121e3(SearchAccountsRequest payload) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        Map<String, Object>  custPremCodeNotInSSPParticipantTable = ApplicationContext.get().getDbAction().getCustCodePremCodeNotInSSPParticipantParentTable();
        payload.setCustomerCode(custPremCodeNotInSSPParticipantTable.get("UZRSSPA_CUST_CODE").toString());
        payload.setPremisesCode(custPremCodeNotInSSPParticipantTable.get("UZRSSPA_PREM_CODE").toString());
    }

    public void setValidSSNTC113(SearchAccountsRequest payload) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setSocialSecurityNumber(encryptData(ssn));
    }

    public void setExternalCasesParameters(SearchAccountsRequest payload, SearchAccountsApiLabel testCondition) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        switch (testCondition) {
            case ST_SE_INVALID_ENROLLMENT_STATUS_VALUE_TC234,
                 ST_SE_INVALID_ES_PAYMENT_CONFIRMATION_REQUIRED_TC235,
                 ST_SE_INVALID_PAYMENT_CONFIRMATION_TC236,
                 ST_SE_INVALID_SSP_PARTICIPANT_CODE_VALUE_TC237,
                 ST_SE_SSP_PARTICIPANT_CODE_NOT_REQUIRED_TC238,
                 ST_SE_MISSING_MARKETER_REFERENCE_CE_TRAN_TC239,
                 ST_SE_DUPLICATE_MARKETER_REFERENCE_DATA_TC240,
                 ST_SE_MARKETER_REFERENCE_DATA_INVALID_TYPE_TC242,
                 ST_SE_MARKETER_REFERENCE_DATA_TOO_LONG_TC243,
                 ST_SE_MARKETER_REFERENCE_DATA_TOO_SHORT_TC244,
                 ST_SE_CURRENT_MARKETER_CODE_PROVIDED_TC245,
                 ST_SE_REQUESTED_TURN_ON_DATE_PROVIDED_TC246,
                 ST_SE_SERVICE_TRANSFER_REWARD_BOOLEAN_ONLY_TC247,
                 ST_SE_CURRENT_PRICE_PLAN_FIXED_BOOLEAN_ONLY_TC248,
                 ST_SE_CURRENT_PRICE_PLAN_CEILING_BOOLEAN_ONLY_TC249,
                 ST_SE_CURRENT_PRICE_PLAN_APPLICABLE_FIXED_OR_CEILING_ONLY_TC250:
                payload.setLoginID(USERNAME);
                List<Map<String, Object>> activeCustomerData = ApplicationContext.get().getDbAction().getActiveCustomerWithServiceTransferEnrollment();
                payload.setCustomerCode(activeCustomerData.getFirst().get(UCRACCT_CUST_CODE).toString());
                payload.setPremisesCode(activeCustomerData.getFirst().get(UCRACCT_PREM_CODE).toString());
                payload.setTransactionType(GlobalEnums.TransactionType.TRANSFER.getValue());
                break;

            default:
                payload.setTransactionType(FakerDataGenerator.generateUpperCaseString(4));
        }
    }

    public void setPrepaySearchRequestParamsFromCustomerFile(SearchAccountsRequest payload, SearchAccountsApiLabel testCondition){
        payload.setRequestID(FakerDataGenerator.generateString(10));
        Map<String, String> customerData = loadRowFromExcelToCustomerData(CUSTOMER_DATA, CUSTOMER_SHEET_NAME, testCondition);
        getCustomerAndPremiseDetails(payload, customerData);
    }

    public void getCustomerAndPremiseDetails(SearchAccountsRequest payload, Map<String, String> data ){
        payload.setLoginID(data.get("loginID"));
        payload.setCustomerLastName(data.get("customerLastName"));
        payload.setCustomerFirstName(data.get("customerFirstName"));
        payload.setAglcAccountNumber(data.get("aclcAccountNumber"));
        payload.setPremisesStreetNumber(data.get("premisesStreetNumber"));
        payload.setPremisesStreetName(data.get("premisesStreetName"));
        payload.setPremisesStreetSuffix(data.get("premisesStreetSuffix"));
        payload.setPremisesStreetPostDirection(data.get("premisesStreetPostDirection"));
        payload.setPremisesUnitType(data.get("premisesUnitType"));
        payload.setPremisesUnitNumber(data.get("premisesUnitNumber"));
        payload.setPremisesCity(data.get("premisesCity"));
        payload.setPremisesStateCode(data.get("premisesStateCode"));
        payload.setPremisesZipCode(data.get("premisesZipCode"));

        String ssn = data.get("SSN");
        if (ssn != null && !ssn.trim().isEmpty()) {
            payload.setSocialSecurityNumber(encryptData(data.get("SSN")));
        }
        String federalTaxId = data.get("federalTaxId");
        if (federalTaxId != null && !federalTaxId.trim().isEmpty()) {
            payload.setSocialSecurityNumber(encryptData(data.get("federalTaxId")));
        }
    }
    public static <E extends Enum<E>> Map<String, String> loadRowFromExcelToCustomerData(
            String excelPath,
            String sheetName,
            E testLabel) {

        try {
            ExcelReader reader = new ExcelReader(excelPath);
            List<Map<String, String>> sheetData = reader.getSheetData(sheetName);

            return sheetData.stream()
                    .filter(row -> {
                        String condition = row.get("testCondition");
                        return condition != null && condition.contains(testLabel.name());
                    }).findFirst()
                    .orElseThrow(() -> new RuntimeException(
                            "No matching testConditions found containing: " + testLabel.name()));

        } catch (IOException e) {
            throw new RuntimeException("Failed to load data from Excel", e);
        }
    }
}




























