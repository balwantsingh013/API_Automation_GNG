package com.gng.api.pages.turnOn.AccountsApiPages.SearchAccounts;


import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.AccountsPojo.SearchAccounts.SearchAccountsRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.turnOn.AccountsApiSteps.SearchAccounts.SearchAccountsApiLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

import static com.gng.api.steps.AesEncryption.AesEncryptionSteps.encryptData;
import static com.gng.api.steps.turnOn.AccountsApiSteps.SearchAccounts.SearchAccountsApiLabel.INACTIVE_UZBSSPP_STATUS_TC121E_2;


@Slf4j
public class SearchAccountsHelper {
    private final TestContext testContext;
    public String premisesCode;
    public String customerCode;

    public static final String USERNAME = "autotester";
    public static String ssn="Password@1";

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

    public void setRequestIDBasedOnTypeTCTC42_TC44(SearchAccountsRequest payload, SearchAccountsApiLabel requestID) {
        switch (requestID) {
            case NULL_REQUEST_ID_TC42:
                payload.setRequestID(null);
                payload.setLoginID(USERNAME);
                payload.setTransactionType("TNON");
                break;
            case DUPLICATE_REQUEST_ID_TC43:
                payload.setRequestID("3BC00A0397B14F29A313280EE0110941");
                payload.setLoginID(USERNAME);
                break;
            case LONG_REQUEST_ID_TC44:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(35));
                payload.setTransactionType("TNON");
                payload.setLoginID(USERNAME);
                break;
            default:
                payload.setRequestID(FakerDataGenerator.generateString(10));
        }
    }

    public void setLoginIDBasedOnTypeTC45_TC48(SearchAccountsRequest payload, SearchAccountsApiLabel loginID) {
        switch (loginID) {
            case NULL_LOGIN_ID_TC45:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("");
                payload.setTransactionType("TNON");
                break;
            case ALPHANUMERIC_LOGIN_ID_TC47:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(FakerDataGenerator.generateAlphanumericWithSpecialChars(8));
                break;
            case MAX_LENGTH_LOGIN_ID_TC46:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(FakerDataGenerator.getRandomNumericString(35));
                break;
            case INVALID_LOGIN_ID_NOT_PRESENT_USER_TABLE_TC48:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("dummy");
                payload.setCustomerLastName("Doe");
                payload.setTransactionType("TNON");
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
        switch (transactionType) {
            case MISSING_TRANSACTION_TYPE_TC51:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setTransactionType(null);
                break;
            case INVALID_TRANSACTION_TYPE_TC52:
                payload.setRequestID(FakerDataGenerator.generateString(10));
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
        switch (socialSecurityNumber) {
            case NOT_ENCRYPTED_SOCIAL_SECURITY_NUMBER_TC56:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setSocialSecurityNumber(FakerDataGenerator.getRandomNumericString(9));
                break;
            case ENCRYPTED_SOCIAL_SECURITY_NUMBER_TC57:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setSocialSecurityNumber(FakerDataGenerator.generateAlphanumericWithSpecialChars(12));
                break;

            default:
                payload.setSocialSecurityNumber(FakerDataGenerator.generateLowerCaseString(9));
        }
    }

    public void setFederalTaxIDBasedOnTypeTC58_TC59(SearchAccountsRequest payload, SearchAccountsApiLabel federalTaxID) {
        switch (federalTaxID) {
            case NOT_ENCRYPTED_FEDERAL_TAX_ID_TC58:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setFederalTaxID(FakerDataGenerator.getRandomNumericString(9));
                break;
            case ENCRYPTED_FEDERAL_TAX_ID_TC59:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setFederalTaxID(FakerDataGenerator.generateAlphanumericWithSpecialChars(12));
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

    public void setPremisesStateCodeBasedOnTypeTC70(SearchAccountsRequest payload, SearchAccountsApiLabel premisesStateCode) {
        switch (premisesStateCode) {
            case MAX_LENGTH_PREMISES_STATE_CODE_TC70:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setTransactionType("TNON");
                payload.setPremisesStateCode(FakerDataGenerator.generateUpperCaseString(4));
                break;
            default:
                payload.setPremisesStateCode(FakerDataGenerator.generateUpperCaseString(3));
        }
    }

    public void setPremisesZipCodeBasedOnTypeTC71(SearchAccountsRequest payload, SearchAccountsApiLabel premisesZipCode) {
        switch (premisesZipCode) {
            case MAX_LENGTH_PREMISES_ZIP_CODE_TC71:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setTransactionType("TNON");
                payload.setPremisesZipCode(FakerDataGenerator.getRandomNumericString(6));
                break;
            default:
                payload.setPremisesZipCode(FakerDataGenerator.getRandomNumericString(5));
        }

    }

    public void setMissingSearchFieldsBasedOnTypeTC72(SearchAccountsRequest payload, SearchAccountsApiLabel missingSearchField) {
        switch (missingSearchField) {
            case MAX_LENGTH_PREMISES_ZIP_CODE_TC71:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setTransactionType("TNON");
                payload.setAglcAccountNumber("");
                payload.setCustomerBusinessName("");
                payload.setCustomerLastName("");
                payload.setPremisesZipCode("");
                payload.setSocialSecurityNumber("");
                payload.setPremisesZipCode("");
                payload.setFederalTaxID("");
                payload.setPhoneNumber("");
                break;
            default:
                payload.setPremisesZipCode(FakerDataGenerator.getRandomNumericString(5));
        }

    }

    public void setCustomerCodeBasedOnTypeTC73(SearchAccountsRequest payload, SearchAccountsApiLabel customerCode) {
        switch (customerCode) {
            case CUSTOMER_CODE_PROVIDED_PREMISES_CODE_MISSING_TC73:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setPremisesCode(FakerDataGenerator.getRandomNumericString(7));
                payload.setCustomerCode("");
                break;
            default:
                payload.setCustomerCode(FakerDataGenerator.getRandomNumericString(5));
        }

    }

    public void setPremisesCodeBasedOnTypeTC74(SearchAccountsRequest payload, SearchAccountsApiLabel premisesCode) {
        switch (premisesCode) {
            case CUSTOMER_CODE_MISSING_PREMISES_CODE_PROVIDED_TC74:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setPremisesCode("");
                payload.setCustomerCode(FakerDataGenerator.getRandomNumericString(7));
                break;
            default:
                payload.setPremisesCode(FakerDataGenerator.getRandomNumericString(7));
        }
    }

    public void setCustomerLastNameBasedOnTypeTC75(SearchAccountsRequest payload, SearchAccountsApiLabel customerLastName) {
        switch (customerLastName) {
            case CUSTOMER_FIRST_NAME_PROVIDED_CUSTOMER_LAST_NAME_MISSING_TC75:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setCustomerFirstName(FakerDataGenerator.generateFirstName());
                payload.setCustomerLastName("");
                break;
            default:
                payload.setCustomerLastName(FakerDataGenerator.generateLastName());
        }
    }

    public void setPremisesZipCodeBasedOnTypeTC76(SearchAccountsRequest payload, SearchAccountsApiLabel premisesZipCode) {
        switch (premisesZipCode) {
            case CUSTOMER_LAST_NAME_PROVIDED_PREMISES_ZIP_MISSING_TC76:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setCustomerLastName(FakerDataGenerator.generateLastName());
                payload.setPremisesZipCode("");
                break;
            default:
                payload.setPremisesZipCode(FakerDataGenerator.generateZipCode());
        }
    }

    public void setSSNAndFederalTaxIDBasedOnTypeTC77(SearchAccountsRequest payload, SearchAccountsApiLabel federalTaxID) {
        switch (federalTaxID) {
            case SSN_PROVIDED_FEDERAL_TAX_ID_PROVIDE_TC77:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setTransactionType("TNON");
                payload.setLoginID(USERNAME);
                payload.setSocialSecurityNumber("YUiEKcZGFmS59Uu1aacc2tTqSPYw9QYAv97gTfGyhaI=");
                payload.setFederalTaxID("YUiEKcZGFmS59Uu1aacc2tTqSPYw9QYAv97gTfGyhaI=");
                break;
            default:
                payload.setFederalTaxID(FakerDataGenerator.generateAlphanumeric(10));
        }
    }

    public void setPremisesStreetNameBasedOnTypeTC78_TC92(SearchAccountsRequest payload, SearchAccountsApiLabel premisesStreetName) {
        switch (premisesStreetName) {
            case PREMISES_STREET_NUMBER_PROVIDED_PREMISES_STREET_NAME_MISSING_TC78:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setTransactionType("TNON");
                payload.setPremisesStreetNumber("25");
                payload.setPremisesStreetName("");
                break;
            case PREMISES_STREET_PRE_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC79:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setTransactionType("TNON");
                payload.setPremisesStreetPreDirection("SE");
                payload.setPremisesStreetName("");
                break;
            case PREMISES_STREET_SUFFIX_PROVIDED_PREMISES_STREET_NAME_MISSING_TC80:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setTransactionType("TNON");
                payload.setPremisesStreetSuffix(FakerDataGenerator.generateUpperCaseString(2));
                payload.setPremisesStreetName("");
                break;
            case PREMISES_STREET_POST_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC81:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setPremisesStreetPostDirection(FakerDataGenerator.generateUpperCaseString(2));
                payload.setPremisesStreetName("");
                break;
            case PREMISES_STREET_NUMBER_PREMISES_STREET_PRE_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC82:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setPremisesStreetNumber(FakerDataGenerator.getRandomNumericString(2));
                payload.setPremisesStreetPreDirection("SW");
                payload.setPremisesStreetName("");
                break;
            case PREMISES_STREET_NUMBER_PREMISES_STREET_SUFFIX_PROVIDED_PREMISES_STREET_NAME_MISSING_TC83:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setPremisesStreetNumber(FakerDataGenerator.getRandomNumericString(2));
                payload.setPremisesStreetSuffix(FakerDataGenerator.generateUpperCaseString(2));
                payload.setPremisesStreetName("");
                break;
            case PREMISES_STREET_NUMBER_PREMISES_STREET_POST_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC84:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setPremisesStreetNumber(FakerDataGenerator.getRandomNumericString(2));
                payload.setPremisesStreetPostDirection(FakerDataGenerator.generateUpperCaseString(2));
                payload.setPremisesStreetName("");
                break;
            case PREMISES_STREET_PRE_DIRECTION_PREMISES_STREET_SUFFIX_PROVIDED_PREMISES_STREET_NAME_MISSING_TC85:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setPremisesStreetSuffix("RD");
                payload.setPremisesStreetPreDirection("SE");
                payload.setPremisesStreetName("");
                break;
            case PREMISES_STREET_PRE_DIRECTION_PREMISES_STREET_POST_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC86:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setPremisesStreetPostDirection("SE");
                payload.setPremisesStreetPreDirection("SW");
                payload.setPremisesStreetName("");
            case PREMISES_STREET_SUFFIX_PREMISES_STREET_POST_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC87:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setPremisesStreetPostDirection(FakerDataGenerator.generateUpperCaseString(2));
                payload.setPremisesStreetSuffix(FakerDataGenerator.generateUpperCaseString(2));
                payload.setPremisesStreetName("");
                break;
            case PREMISES_STREET_NUMBER_SUFFIX_PRE_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC88:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setPremisesStreetNumber(FakerDataGenerator.getRandomNumericString(2));
                payload.setPremisesStreetSuffix("RD");
                payload.setPremisesStreetPreDirection("SW");
                payload.setPremisesStreetName("");
                break;
            case PREMISES_STREET_NUMBER_SUFFIX_POST_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC89:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setPremisesStreetNumber(FakerDataGenerator.getRandomNumericString(2));
                payload.setPremisesStreetSuffix(FakerDataGenerator.generateUpperCaseString(2));
                payload.setPremisesStreetPostDirection(FakerDataGenerator.generateUpperCaseString(2));
                payload.setPremisesStreetName("");
                break;
            case PREMISES_PRE_DIRECTION_SUFFIX_POST_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC90:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setPremisesStreetPreDirection("SE");
                payload.setPremisesStreetSuffix("RD");
                payload.setPremisesStreetPostDirection("SW");
                payload.setPremisesStreetName("");
                break;
            case PREMISES_STREET_NUMBER_PRE_DIRECTION_POST_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC91:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setPremisesStreetNumber(FakerDataGenerator.getRandomNumericString(2));
                payload.setPremisesStreetPreDirection("SE");
                payload.setPremisesStreetPostDirection("SW");
                payload.setPremisesStreetName("");
                break;
            case PREMISES_STREET_NUMBER_SUFFIX_PRE_DIRECTION_POST_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC92:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setPremisesStreetNumber(FakerDataGenerator.getRandomNumericString(2));
                payload.setPremisesStreetPreDirection("SE");
                payload.setPremisesStreetSuffix("RD");
                payload.setPremisesStreetPostDirection("SW");
                payload.setPremisesStreetName(null);
                break;
            default:
                payload.setPremisesStreetName(FakerDataGenerator.generateLowerCaseString(10));
        }
    }

    public void setMissingZipCityAndStateAddressFieldsBasedOnTypeTC93(SearchAccountsRequest payload, SearchAccountsApiLabel premisesCity) {
        switch (premisesCity) {
            case PREMISES_STREET_NAME_PROVIDED_CITY_ZIP_STATE_CODE_MISSING_TC93:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setPremisesStreetName(FakerDataGenerator.generateString(3));
                payload.setPremisesZipCode("");
                payload.setPremisesCity("");
                payload.setPremisesStateCode("");
                break;
            default:
                payload.setPremisesCity(FakerDataGenerator.generateLowerCaseString(7));
        }
    }

    public void setMissingZipStreetNameAndStateAddressFieldsBasedOnTypeTC94(SearchAccountsRequest payload, SearchAccountsApiLabel premisesZipCode) {
        switch (premisesZipCode) {
            case PREMISES_CITY_PROVIDED_STREET_NAME_ZIP_STATE_CODE_MISSING_TC94:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setPremisesCity(FakerDataGenerator.generateLowerCaseString(3));
                payload.setPremisesZipCode("");
                payload.setPremisesStreetName("");
                payload.setPremisesStateCode("");
                break;
            default:
                payload.setPremisesZipCode(FakerDataGenerator.generateLowerCaseString(7));
        }
    }

    public void setMissingZipStreetNameAndCityAddressFieldsBasedOnTypeTC95(SearchAccountsRequest payload, SearchAccountsApiLabel premisesZipCode) {
        switch (premisesZipCode) {
            case PREMISES_STATE_CODE_PROVIDED_STREET_NAME_ZIP_CITY_MISSING_TC95:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setPremisesStateCode(FakerDataGenerator.generateUpperCaseString(3));
                payload.setPremisesZipCode("");
                payload.setPremisesStreetName("");
                payload.setPremisesCity("");
                break;
            default:
                payload.setPremisesZipCode(FakerDataGenerator.generateLowerCaseString(7));
        }
    }

    public void setMissingStreetNameStateAndCityAddressFieldsBasedOnTypeTC96(SearchAccountsRequest payload, SearchAccountsApiLabel premisesStateCode) {
        switch (premisesStateCode) {
            case PREMISES_ZIP_CODE_PROVIDED_STREET_NAME_ZIP_STATE_MISSING_TC96:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setTransactionType("TNON");
                payload.setPremisesZipCode("30052");
                payload.setPremisesStateCode(null);
                payload.setPremisesStreetName(null);
                payload.setPremisesCity(null);
                break;
            default:
                payload.setPremisesStateCode(FakerDataGenerator.generateUpperCaseString(3));
        }
    }

    public void setMissingStateAndZipCodeAddressFieldsBasedOnTypeTC97(SearchAccountsRequest payload, SearchAccountsApiLabel premisesStateCode) {
        switch (premisesStateCode) {
            case PREMISES_STREET_NAME_CITY_PROVIDED_ZIP_STATE_MISSING_TC97:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setPremisesStreetName(FakerDataGenerator.generateString(3));
                payload.setPremisesCity(FakerDataGenerator.generateLowerCaseString(3));
                payload.setPremisesStateCode("");
                payload.setPremisesZipCode("");
                break;
            default:
                payload.setPremisesStateCode(FakerDataGenerator.generateUpperCaseString(3));
        }
    }

    public void setMissingCityAndZipCodeAddressFieldsBasedOnTypeTC98(SearchAccountsRequest payload, SearchAccountsApiLabel premisesStateCode) {
        switch (premisesStateCode) {
            case PREMISES_STREET_NAME_STATE_PROVIDED_ZIP_CITY_MISSING_TC98:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setPremisesStreetName(FakerDataGenerator.generateString(3));
                payload.setPremisesStateCode(FakerDataGenerator.generateUpperCaseString(3));
                payload.setPremisesCity(null);
                payload.setPremisesZipCode(null);
                break;
            default:
                payload.setPremisesStateCode(FakerDataGenerator.generateUpperCaseString(3));
        }
    }

    public void setMissingStateAndCityAddressFieldsBasedOnTypeTC99(SearchAccountsRequest payload, SearchAccountsApiLabel premisesStateCode) {
        switch (premisesStateCode) {
            case PREMISES_STREET_NAME_ZIP_PROVIDED_STATE_CITY_MISSING_TC99:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setPremisesStreetName(FakerDataGenerator.generateLowerCaseString(10));
                payload.setPremisesZipCode("30052");
                payload.setPremisesStateCode(null);
                payload.setPremisesCity(null);
                break;
            default:
                payload.setPremisesStateCode(FakerDataGenerator.generateUpperCaseString(3));
        }
    }

    public void setMissingStreetNameAndZipAddressFieldsBasedOnTypeTC100(SearchAccountsRequest payload, SearchAccountsApiLabel premisesZipCode) {
        switch (premisesZipCode) {
            case PREMISES_STATE_CITY_PROVIDED_STREET_NAME_ZIP_MISSING_TC100:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setPremisesStreetName(null);
                payload.setPremisesStateCode(FakerDataGenerator.generateUpperCaseString(3));
                payload.setPremisesCity(FakerDataGenerator.generateLowerCaseString(3));
                payload.setPremisesZipCode(null);
                break;
            default:
                payload.setPremisesZipCode(FakerDataGenerator.generateZipCode());
        }
    }

    public void setMissingStreetNameAndStateAddressFieldsBasedOnTypeTC101(SearchAccountsRequest payload, SearchAccountsApiLabel premisesStateCode) {
        switch (premisesStateCode) {
            case PREMISES_STATE_CITY_ZIP_PROVIDED_STREET_NAME_STATE_MISSING_TC101:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setPremisesCity(FakerDataGenerator.generateLowerCaseString(8));
                payload.setPremisesZipCode("30052");
                break;
            default:
                payload.setPremisesStateCode(FakerDataGenerator.generateUpperCaseString(3));
        }
    }

    public void setMissingStreetNameAndCityAddressFieldsBasedOnTypeTC102(SearchAccountsRequest payload, SearchAccountsApiLabel premisesCity) {
        switch (premisesCity) {
            case PREMISES_STATE_ZIP_PROVIDED_STREET_NAME_CITY_MISSING_TC102:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setPremisesStateCode("GA");
                payload.setPremisesZipCode("30052");
                break;
            default:
                payload.setPremisesCity(FakerDataGenerator.generateLowerCaseString(3));
        }
    }

    public void setMissingZipAddressFieldsBasedOnTypeTC103(SearchAccountsRequest payload, SearchAccountsApiLabel premisesZipCode) {
        switch (premisesZipCode) {
            case PREMISES_STATE_STREET_NAME_CITY_PROVIDED_ZIP_MISSING_TC103:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setPremisesStreetName(FakerDataGenerator.generateString(3));
                payload.setPremisesCity(FakerDataGenerator.generateString(6));
                payload.setPremisesStateCode("GA");
                payload.setPremisesZipCode("");
                break;
            default:
                payload.setPremisesZipCode(FakerDataGenerator.generateZipCode());
        }
    }

    public void setMissingCityAddressFieldsBasedOnTypeTC104(SearchAccountsRequest payload, SearchAccountsApiLabel premisesCity) {
        switch (premisesCity) {
            case PREMISES_STATE_STREET_NAME_PROVIDED_CITY_MISSING_TC104:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setPremisesStreetName(FakerDataGenerator.generateString(3));
                payload.setPremisesCity("");
                payload.setPremisesStateCode(FakerDataGenerator.generateUpperCaseString(3));
                payload.setPremisesZipCode("30052");
                break;
            default:
                payload.setPremisesCity(FakerDataGenerator.generateLowerCaseString(3));
        }
    }

    public void setMissingStateCodeAddressFieldsBasedOnTypeTC105(SearchAccountsRequest payload, SearchAccountsApiLabel premisesStateCode) {
        switch (premisesStateCode) {
            case PREMISES_ZIP_STREET_NAME_CITY_PROVIDED_STATE_MISSING_TC105:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setPremisesStreetName(FakerDataGenerator.generateString(3));
                payload.setPremisesCity(FakerDataGenerator.generateString(6));
                payload.setPremisesStateCode("");
                payload.setPremisesZipCode("30052");
                break;
            default:
                payload.setPremisesStateCode(FakerDataGenerator.generateUpperCaseString(3));
        }
    }

    public void setMissingStreetNameAddressFieldsBasedOnTypeTC106(SearchAccountsRequest payload, SearchAccountsApiLabel premisesStreetName) {
        switch (premisesStreetName) {
            case PREMISES_ZIP_STATE_CITY_PROVIDED_STREET_NAME_MISSING_TC106:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(USERNAME);
                payload.setPremisesStreetName("");
                payload.setPremisesCity(FakerDataGenerator.generateLowerCaseString(6));
                payload.setPremisesStateCode("GA");
                payload.setPremisesZipCode("30052");
                break;
            default:
                payload.setPremisesStreetName(FakerDataGenerator.generateString(3));
        }
    }


    //positive test cases


    public void setInvalidCustomerCode(SearchAccountsRequest payload, String customerCode, String premisesCode) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setCustomerCode(customerCode);
        payload.setPremisesCode(premisesCode);
    }

    public void validateCustomerCodeAndPremisesCodeInDB(String customerCode, String premisesCode) {
        Map<String, Object> customerCodeInDatabase= ApplicationContext.get().getDbAction().searchForCustomerCodeAndPremisesCodeInDatabase(customerCode, premisesCode);;
        Assert.assertTrue(customerCodeInDatabase.isEmpty());
    }

    public void verifyTheCountOfRecordsRetrivedFromDBIsMoreThan30(String customerBusinessName) {
        Long countOfRecordsBasedOnBusinessName= ApplicationContext.get().getDbAction().searchForCustomerBusinessNameInDatabase(customerBusinessName);;
        Assert.assertTrue(countOfRecordsBasedOnBusinessName>30);
    }

    public void setLastNameFirstNameAndZiPBType(SearchAccountsRequest payload) {
        List<Map<String, Object>> customerData = ApplicationContext.get().getDbAction().lastNameFirstNameTC112Query();
        Map<String, Object> data = customerData.get(0);

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
        List<Map<String, Object>> aglcAccNumber = ApplicationContext.get().getDbAction().aglcAccountNumberETypeNoSSPTC114Query();
        Map<String, Object> data = aglcAccNumber.get(0);


        String aglcAccountNumber = data.get("uzbenro_old_acct_num").toString();

        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setLoginID(USERNAME);
        payload.setAglcAccountNumber(aglcAccountNumber);

    }


    public void setCustomerDataETypeSSP(SearchAccountsRequest payload) {
        Map<String, Object> data = ApplicationContext.get().getDbAction().customerDataWithETypeTC115Query();


        String premisesStreetNumber = data.get("UCRADDR_STREET_NUMBER").toString();
        String premisesStreetPreDirection = data.get("UCRADDR_PDIR_CODE_PRE").toString();
        String premisesStreetName = data.get("UCRADDR_STREET_NAME").toString();
        String premisesStreetSuffix = data.get("UCRADDR_SSFX_CODE").toString();
        String premisesStreetPostDirection = data.get("UCRADDR_PDIR_CODE_POST").toString();
        String premisesUnitType = data.get("UCRADDR_UNIT").toString();
        String premisesCity = data.get("UCRADDR_CITY").toString();
        String premisesStateCode = data.get("UCRADDR_STAT_CODE").toString();
        String premisesZipCode = data.get("UCRADDR_ZIP").toString();

        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setLoginID(USERNAME);
        payload.setPremisesStreetNumber(premisesStreetNumber);
        payload.setPremisesStreetPreDirection(premisesStreetPreDirection);
        payload.setPremisesStreetName(premisesStreetName);
        payload.setPremisesStreetSuffix(premisesStreetSuffix);
        payload.setPremisesStreetPostDirection(premisesStreetPostDirection);
       // payload.setPremisesUnitNumber(premisesUnitNumber);
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

    public void setEnrollmentRecordsBasedOnTheProvidedPhoneNumber(SearchAccountsRequest payload) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        Map<String, Object>  phoneNumber = ApplicationContext.get().getDbAction().getPhoneNumber();
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
}




























