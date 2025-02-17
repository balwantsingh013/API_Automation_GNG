package com.gng.api.pages.AccountsApiPages.SearchAccounts;


import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.AccountsPojo.SearchAccounts.SearchAccountsRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.AccountsApiSteps.SearchAccounts.SearchAccountsApiLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Slf4j
public class SearchAccountsHelper {
    private final TestContext testContext;
    public String premisesZipCode;
    public String customerLastName;
    public String customerFirstName;
    public String premisesCode;
    public String customerCode;
    public String aglcAccountNumber;
    public String premisesStreetNumber;
    public String premisesStreetPreDirection;
    public String premisesStreetName;
    public String premisesStreetSuffix;
    public String premisesStreetPostDirection;
    public String premisesUnitNumber;
    public String premisesUnitType;
    public String premisesCity;
    public String premisesStateCode;

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
                payload.setLoginID("autotester");
                payload.setTransactionType("TNON");
                break;
            case DUPLICATE_REQUEST_ID_TC43:
                payload.setRequestID("3BC00A0397B14F29A313280EE0110941");
                payload.setLoginID("autotester");
                break;
            case LONG_REQUEST_ID_TC44:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(35));
                payload.setTransactionType("TNON");
                payload.setLoginID("autotester");
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
                payload.setTransactionType("TNON");
                break;
            default:
                payload.setLoginID(FakerDataGenerator.generateLowerCaseString(10));
        }
    }

    public void setCustomerCodeBasedOnTypeTC49(SearchAccountsRequest payload, SearchAccountsApiLabel customerCode) {
        switch (customerCode) {
            case MAX_LENGTH_CUSTOMER_CODE_TC49:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("autotester");
                payload.setTransactionType("TNON");
                payload.setCustomerCode(FakerDataGenerator.generateLowerCaseString(15));
                break;
            default:
                payload.setCustomerCode(FakerDataGenerator.generateLowerCaseString(9));
        }
    }

    public void setPremisesCodeBasedOnTypeTC50(SearchAccountsRequest payload, SearchAccountsApiLabel premisesCode) {
        switch (premisesCode) {
            case MAX_LENGTH_PREMISES_CODE_TC50:
                payload.setRequestID(FakerDataGenerator.generateString(7));
                payload.setLoginID("autotester");
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
                payload.setLoginID("autotester");
                payload.setTransactionType(null);
                break;
            case INVALID_TRANSACTION_TYPE_TC52:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("autotester");
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
                payload.setLoginID("autotester");
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
                payload.setLoginID("autotester");
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
                payload.setLoginID("autotester");
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
                payload.setLoginID("autotester");
                payload.setSocialSecurityNumber(FakerDataGenerator.getRandomNumericString(9));
                break;
            case ENCRYPTED_SOCIAL_SECURITY_NUMBER_TC57:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("autotester");
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
                payload.setLoginID("autotester");
                payload.setFederalTaxID(FakerDataGenerator.getRandomNumericString(9));
                break;
            case ENCRYPTED_FEDERAL_TAX_ID_TC59:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("autotester");
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
                payload.setLoginID("autotester");
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
                payload.setLoginID("autotester");
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
                payload.setLoginID("autotester");
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
                payload.setLoginID("autotester");
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
                payload.setLoginID("autotester");
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
                payload.setLoginID("autotester");
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
                payload.setLoginID("autotester");
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
                payload.setLoginID("autotester");
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
                payload.setLoginID("autotester");
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
                payload.setLoginID("autotester");
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
                payload.setLoginID("autotester");
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
                payload.setLoginID("autotester");
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
                payload.setLoginID("autotester");
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
                payload.setLoginID("autotester");
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
                payload.setLoginID("autotester");
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
                payload.setLoginID("autotester");
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
                payload.setLoginID("autotester");
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
                payload.setLoginID("autotester");
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
                payload.setLoginID("autotester");
                payload.setTransactionType("TNON");
                payload.setPremisesStreetNumber("25");
                payload.setPremisesStreetName("");
                break;
            case PREMISES_STREET_PRE_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC79:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("autotester");
                payload.setTransactionType("TNON");
                payload.setPremisesStreetPreDirection("SE");
                payload.setPremisesStreetName("");
                break;
            case PREMISES_STREET_SUFFIX_PROVIDED_PREMISES_STREET_NAME_MISSING_TC80:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("autotester");
                payload.setTransactionType("TNON");
                payload.setPremisesStreetSuffix(FakerDataGenerator.generateUpperCaseString(2));
                payload.setPremisesStreetName("");
                break;
            case PREMISES_STREET_POST_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC81:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("autotester");
                payload.setPremisesStreetPostDirection(FakerDataGenerator.generateUpperCaseString(2));
                payload.setPremisesStreetName("");
                break;
            case PREMISES_STREET_NUMBER_PREMISES_STREET_PRE_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC82:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("autotester");
                payload.setPremisesStreetNumber(FakerDataGenerator.getRandomNumericString(2));
                payload.setPremisesStreetPreDirection("SW");
                payload.setPremisesStreetName("");
                break;
            case PREMISES_STREET_NUMBER_PREMISES_STREET_SUFFIX_PROVIDED_PREMISES_STREET_NAME_MISSING_TC83:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("autotester");
                payload.setPremisesStreetNumber(FakerDataGenerator.getRandomNumericString(2));
                payload.setPremisesStreetSuffix(FakerDataGenerator.generateUpperCaseString(2));
                payload.setPremisesStreetName("");
                break;
            case PREMISES_STREET_NUMBER_PREMISES_STREET_POST_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC84:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("autotester");
                payload.setPremisesStreetNumber(FakerDataGenerator.getRandomNumericString(2));
                payload.setPremisesStreetPostDirection(FakerDataGenerator.generateUpperCaseString(2));
                payload.setPremisesStreetName("");
                break;
            case PREMISES_STREET_PRE_DIRECTION_PREMISES_STREET_SUFFIX_PROVIDED_PREMISES_STREET_NAME_MISSING_TC85:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("autotester");
                payload.setPremisesStreetSuffix("RD");
                payload.setPremisesStreetPreDirection("SE");
                payload.setPremisesStreetName("");
                break;
            case PREMISES_STREET_PRE_DIRECTION_PREMISES_STREET_POST_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC86:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("autotester");
                payload.setPremisesStreetPostDirection("SE");
                payload.setPremisesStreetPreDirection("SW");
                payload.setPremisesStreetName("");
            case PREMISES_STREET_SUFFIX_PREMISES_STREET_POST_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC87:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("autotester");
                payload.setPremisesStreetPostDirection(FakerDataGenerator.generateUpperCaseString(2));
                payload.setPremisesStreetSuffix(FakerDataGenerator.generateUpperCaseString(2));
                payload.setPremisesStreetName("");
                break;
            case PREMISES_STREET_NUMBER_SUFFIX_PRE_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC88:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("autotester");
                payload.setPremisesStreetNumber(FakerDataGenerator.getRandomNumericString(2));
                payload.setPremisesStreetSuffix("RD");
                payload.setPremisesStreetPreDirection("SW");
                payload.setPremisesStreetName("");
                break;
            case PREMISES_STREET_NUMBER_SUFFIX_POST_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC89:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("autotester");
                payload.setPremisesStreetNumber(FakerDataGenerator.getRandomNumericString(2));
                payload.setPremisesStreetSuffix(FakerDataGenerator.generateUpperCaseString(2));
                payload.setPremisesStreetPostDirection(FakerDataGenerator.generateUpperCaseString(2));
                payload.setPremisesStreetName("");
                break;
            case PREMISES_PRE_DIRECTION_SUFFIX_POST_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC90:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("autotester");
                payload.setPremisesStreetPreDirection("SE");
                payload.setPremisesStreetSuffix("RD");
                payload.setPremisesStreetPostDirection("SW");
                payload.setPremisesStreetName("");
                break;
            case PREMISES_STREET_NUMBER_PRE_DIRECTION_POST_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC91:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("autotester");
                payload.setPremisesStreetNumber(FakerDataGenerator.getRandomNumericString(2));
                payload.setPremisesStreetPreDirection("SE");
                payload.setPremisesStreetPostDirection("SW");
                payload.setPremisesStreetName("");
                break;
            case PREMISES_STREET_NUMBER_SUFFIX_PRE_DIRECTION_POST_DIRECTION_PROVIDED_PREMISES_STREET_NAME_MISSING_TC92:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("autotester");
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
                payload.setLoginID("autotester");
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
                payload.setLoginID("autotester");
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
                payload.setLoginID("autotester");
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
                payload.setLoginID("autotester");
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
                payload.setLoginID("autotester");
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
                payload.setLoginID("autotester");
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
                payload.setLoginID("autotester");
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
                payload.setLoginID("autotester");
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
                payload.setLoginID("autotester");
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
                payload.setLoginID("autotester");
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
                payload.setLoginID("autotester");
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
                payload.setLoginID("autotester");
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
                payload.setLoginID("autotester");
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
                payload.setLoginID("autotester");
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


    public void setInvalidCustomerCode(SearchAccountsRequest payload) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setCustomerCode("5555555");
        payload.setPremisesCode("55555");
    }
    public void setCustomerCode(SearchAccountsRequest payload,List<Map<String, Object>> invalidCustomerCode) {
        payload.setCustomerCode("5555555");
        String a = (String) payload.getCustomerCode();
        System.out.println("Customer Code: " + a);

    }


    public void validateCustomerCodeInDB(String customerCode,SearchAccountsRequest payload) {

        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setCustomerCode("5555555");
        payload.setPremisesCode("55555");

        List<Map<String, Object>> invalidCustomerCodesFromDB = getInvalidCustomerCode(customerCode);

        if (invalidCustomerCodesFromDB.isEmpty()) {
            System.out.println("Validation failed: No matching record found for Customer Code in the database.");
        } else if (invalidCustomerCodesFromDB.size() > 1) {
            System.out.println("Validation failed: Multiple matching records found for Customer Code in the database.");
        } else {
            String dbCustomerCode = (String) invalidCustomerCodesFromDB.get(0).get("CustomerCode");

            if (customerCode.equals(dbCustomerCode)) {
                System.out.println("Validation passed: Customer Code matches the database value.");
            } else {
                System.out.println("Validation failed: Customer Code does not match the database value.");
                System.out.println("Expected: " + customerCode);
                System.out.println("Actual: " + dbCustomerCode);
            }
        }
    }
    public List<Map<String, Object>> getInvalidCustomerCode(String customerCode) {

        List<Map<String, Object>> results = new ArrayList<>();
        Map<String, Object> record = new HashMap<>();
        record.put("CustomerCode", customerCode);
        results.add(record);

        return results;
    }


    public void setLastNameFirstNameAndZiPBType(SearchAccountsRequest payload) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setPremisesZipCode("PremisesZipCode");
        payload.setCustomerLastName("CustomerLastName");
        payload.setCustomerFirstName("CustomerFirstName");
    }




    public void setLastNameFirstNameAndZiPBInDBType() {
        List<Map<String, Object>> customerData = ApplicationContext.get().getDbAction().lastNameFirstNameTC112Query();

        if (customerData != null && !customerData.isEmpty()) {
            Map<String, Object> data = customerData.get(0);

            premisesZipCode = data.get("premises_zip_code").toString();
            customerLastName = data.get("customer_last_name").toString();
            customerFirstName = data.get("customer_first_name").toString();
        } else {
            throw new RuntimeException("No customer data found in DB.");
        }
    }


    public void setaglcAccountNumberType(SearchAccountsRequest payload) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setAglcAccountNumber("aglcAccountNumber");

    }

    public void setAGLCAccountNumberETypeNoSSPInDBType() {
        List<Map<String, Object>> aglcAccNumber = ApplicationContext.get().getDbAction().aglcAccountNumberETypeNoSSPTC114Query();

        if (aglcAccNumber != null && !aglcAccNumber.isEmpty()) {
            Map<String, Object> data = aglcAccNumber.get(0);

            aglcAccountNumber = data.get("aglc_acc_number").toString();

        } else {
            throw new RuntimeException("No Aglc Account Number found in DB.");
        }
    }


    public void setCustomerDataETypeSSP(SearchAccountsRequest payload) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setPremisesStreetNumber("premisesStreetNumber");
        payload.setPremisesStreetPreDirection("premisesStreetPreDirection");
        payload.setPremisesStreetName("premisesStreetName");
        payload.setPremisesStreetSuffix("premisesStreetSuffix");
        payload.setPremisesStreetPostDirection("premisesStreetPostDirection");
        payload.setPremisesUnitNumber("premisesUnitNumber");
        payload.setPremisesUnitType("premisesUnitType");
        payload.setPremisesCity("premisesCity");
        payload.setPremisesStateCode("premisesStateCode");
        payload.setPremisesZipCode("premisesZipCode");

    }

    public void setCustomerDataETypeSSPInDBType() {
        List<Map<String, Object>> customerDataSSP = ApplicationContext.get().getDbAction().customerDataWithETypeTC115Query();

        if (customerDataSSP != null && !customerDataSSP.isEmpty()) {
            Map<String, Object> data = customerDataSSP.get(0);

            aglcAccountNumber = data.get("aglc_acc_number").toString();

        } else {
            throw new RuntimeException("No Aglc Account Number found in DB.");
        }
    }

    public void setAccountNumberSearchETypeNoSSPDB() {
        List<Map<String, Object>> accountNumberData = ApplicationContext.get().getDbAction().accountNumberSearchETypeNoSSPDBTC110Query();

        if (accountNumberData != null && !accountNumberData.isEmpty()) {
            Map<String, Object> data = accountNumberData.get(0);

            premisesCode = data.get("premises_code").toString();
            customerCode = data.get("customer_code").toString();
        } else {
            throw new RuntimeException("No account Number Data found in DB.");
        }
    }

    public void setAccountNumberSearchETypeNoSSP(SearchAccountsRequest payload ) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setPremisesCode("premisesCode");
        payload.setCustomerCode("customerCode");

    }

    public void setEnrollmentRecordsBasedOnTheProvidedPhoneNumber(SearchAccountsRequest payload) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setPhoneNumber("7704090713");
    }


    public void setAccountNumberSearchWithoutSSNBasedOnTypeTC109(SearchAccountsRequest payload) {
            payload.setRequestID(FakerDataGenerator.generateString(10));
            payload.setLoginID("autotester");
            payload.setCustomerCode("005801335");
            payload.setPremisesCode("5776499");
            payload.setTransactionType("TNON");

    }
    public void setLastNameAndZiPBType(SearchAccountsRequest payload) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setLoginID("autotester");
        payload.setCustomerLastName("STONE");
        payload.setPremisesZipCode("30132");

    }
    public void setSSPParticipantCodeBasedOnTypeTC121e(SearchAccountsRequest payload) {
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("autotester");
                payload.setCustomerCode("5801335");
                payload.setPremisesCode("5776499");
                payload.setTransactionType("TNON");


    }


}




























