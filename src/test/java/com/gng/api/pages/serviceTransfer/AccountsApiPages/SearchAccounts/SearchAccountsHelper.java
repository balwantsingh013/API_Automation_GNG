package com.gng.api.pages.serviceTransfer.AccountsApiPages.SearchAccounts;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.AccountsPojo.SearchAccounts.SearchAccountsRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.serviceTransfer.AccountsApiSteps.SearchAccounts.SearchAccountsApiLabel;
import com.gng.api.steps.turnOff.AccountsApiSteps.SearchAccounts.SearchAccountsTOffApiLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static com.gng.api.steps.AesEncryption.AesEncryptionSteps.encryptData;

@Slf4j
public class SearchAccountsHelper {
    private final TestContext testContext;
    public String validZipCode="30307";
    public String socialSecurityNumber= "666398181";
    public String premisesStreetNumber="442";
    public String premisesStreetPreDirection;
    public String premisesStreetName="CLIFTON";
    public String premisesStreetSuffix;
    public String premisesStreetPostDirection;
    public String premisesUnitType;
    public String premisesUnitNumber;
    public String premisesCity= "ATLANTA";
    public String premisesStateCode="GA";
    public String premisesZipCode;
    public String preisesUnitType="#";

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

    public void setParametersToEmpty(SearchAccountsRequest payload){
        payload.setCustomerCode("");
        payload.setCustomerLastName("");
        payload.setCustomerFirstName("");
        payload.setPremisesZipCode("");
        payload.setCustomerBusinessName("");
        payload.setPremisesCode("");
    }

    public void preparePayloadForNegativeTestConditions(SearchAccountsRequest payload, SearchAccountsApiLabel testCondition) {
        setParametersToEmpty(payload);
        payload.setTransactionType(GlobalEnums.TransactionType.TRANSFER.getValue());
        payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
        Map<String, Object> accountDetailsLastNameZipCode = ApplicationContext.get().getDbAction().getAccountDetails_LastNameZipCode();
        String lastName=accountDetailsLastNameZipCode.get("UZBENRO_DSM_LAST_NAME").toString();
        validZipCode = accountDetailsLastNameZipCode.get("UCRADDR_ZIP").toString();
        switch (testCondition) {
            case SEARCH_ACCOUNTS_NO_SEARCH_PARAMETERS_PROVIDED_TC_1:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                break;

            case SEARCH_ACCOUNTS_MISSING_REQUEST_ID_TC_2:
                payload.setRequestID("");
                break;

            case SEARCH_ACCOUNTS_INVALID_REQUEST_ID_LENGTH_TC_3:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(33));
                break;

            case SEARCH_ACCOUNTS_DUPLICATE_REQUEST_ID_TC_4:
                payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());
                break;

            case SEARCH_ACCOUNTS_MISSING_LOGIN_ID_TC_5:
                payload.setLoginID("");
                break;

            case SEARCH_ACCOUNTS_INVALID_LOGIN_ID_LENGTH_TC_6:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setLoginID(FakerDataGenerator.generateString(31));
                break;

            case SEARCH_ACCOUNTS_INVALID_LOGIN_ID_FORMAT_TC_7:
                payload.setLoginID(FakerDataGenerator.generateAlphanumericWithSpecialChars(6));
                break;

            case SEARCH_ACCOUNTS_INVALID_LOGIN_ID_TC_8:
                payload.setLoginID(FakerDataGenerator.generateString(6));
                payload.setCustomerLastName(FakerDataGenerator.generateString(5));
                payload.setPremisesZipCode(validZipCode);
                break;

            case SEARCH_ACCOUNTS_MISSING_CUSTOMER_CODE_TC_9:
                payload.setPremisesCode(FakerDataGenerator.generateDigits(6));
                break;

            case SEARCH_ACCOUNTS_MISSING_PREMISES_CODE_TC_10:
                payload.setCustomerCode(FakerDataGenerator.generateDigits(6));
                break;

            case SEARCH_ACCOUNTS_INVALID_CUSTOMER_CODE_LENGTH_TC_11:
                payload.setCustomerCode(FakerDataGenerator.generateDigits(10));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(6));
                break;

            case SEARCH_ACCOUNTS_INVALID_PREMISES_CODE_LENGTH_TC_12:
                payload.setCustomerCode(FakerDataGenerator.generateDigits(6));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(8));
                break;

            case SEARCH_ACCOUNTS_MISSING_TRANSACTION_TYPE_TC_13:
                payload.setTransactionType(null);
                break;

            case SEARCH_ACCOUNTS_INVALID_TRRANSACTION_TYPE_TC_14:
                payload.setTransactionType(FakerDataGenerator.generateUpperCaseString(4));
                break;

            case SEARCH_ACCOUNTS_INVALID_TRANSACTION_TYPR_LENGTH_TC_15:
                payload.setTransactionType(FakerDataGenerator.generateUpperCaseString(5));
                break;

            case SEARCH_ACCOUNTS_INVALID_BUSINESS_NAME_LENGTH_TC_16:
                payload.setCustomerBusinessName(FakerDataGenerator.generateString(61));
                break;

            case SEARCH_ACCOUNTS_INVALID_CUSTOMER_LAST_NAME_LENGTH_TC_17:
                payload.setCustomerLastName(FakerDataGenerator.generateString(61));
                break;

            case SEARCH_ACCOUNTS_MISSING_PREMISES_ZIP_CODE_TC_19:
                payload.setCustomerLastName(FakerDataGenerator.generateString(10));
                break;

            case SEARCH_ACCOUNTS_MISSING_TRANSACTION_TYPE_TC_20:
                payload.setCustomerLastName(lastName);
                payload.setPremisesZipCode(validZipCode);
                payload.setTransactionType(null);
                break;

            case SEARCH_ACCOUNTS_INVALID_FIRST_NAME_LENGTH_TC_21:
                payload.setCustomerFirstName(FakerDataGenerator.generateString(16));
                break;

            case SEARCH_ACCOUNTS_UNENCRYPTED_SSN_TC_22:
                payload.setSocialSecurityNumber(socialSecurityNumber);
                break;

            case SEARCH_ACCOUNTS_INVALID_SSN_LENGTH_TC_23:
                payload.setSocialSecurityNumber(encryptData(FakerDataGenerator.generateDigits(33)));
                break;

            case SEARCH_ACCOUNTS_TAX_ID_NOT_ALLOWED_TC_24:
                payload.setCustomerBusinessName(FakerDataGenerator.generateString(10));
                payload.setFederalTaxID(encryptData(FakerDataGenerator.generateDigits(9)));
                break;

            case SEARCH_ACCOUNTS_SSN_AND_TAX_ID_PROVIDED_TC_25:
                payload.setSocialSecurityNumber(encryptData(socialSecurityNumber));
                payload.setFederalTaxID(encryptData(FakerDataGenerator.generateDigits(9)));
                break;

            case SEARCH_ACCOUNTS_PHONE_NOT_ALLOWED_TC_26:
                payload.setCustomerLastName(lastName);
                payload.setPremisesZipCode(validZipCode);
                payload.setPhoneNumber(FakerDataGenerator.generateDigits(10));
                break;

            case SEARCH_ACCOUNTS_INVALID_AGLC_ACCOUNT_NO_LENGTH_TC_27:
                payload.setCustomerLastName(lastName);
                payload.setPremisesZipCode(validZipCode);
                payload.setAglcAccountNumber(FakerDataGenerator.generateDigits(21));
                break;

            case SEARCH_ACCOUNTS_NON_NUMERIC_AGLC_ACCOUNT_NO_TC_28:
                payload.setCustomerLastName(lastName);
                payload.setPremisesZipCode(validZipCode);
                payload.setAglcAccountNumber(FakerDataGenerator.generateAlphanumericWithSpecialChars(10));
                break;

            case SEARCH_ACCOUNTS_INVALID_STREET_NUMBER_LENGTH_TC_29:
                payload.setPremisesStreetName(premisesStreetName);
                payload.setPremisesStreetSuffix(premisesStreetSuffix);
                payload.setPremisesStreetPostDirection(premisesStreetPostDirection);
                payload.setPremisesCity(premisesCity);
                payload.setPremisesZipCode(validZipCode);
                payload.setPremisesStreetNumber(FakerDataGenerator.generateDigits(13));
                break;

            case SEARCH_ACCOUNTS_INVALID_STREET_PRE_DIR_LENGTH_TC_30:
                payload.setPremisesStreetName(premisesStreetName);
                payload.setPremisesStreetSuffix(premisesStreetSuffix);
                payload.setPremisesStreetPostDirection(premisesStreetPostDirection);
                payload.setPremisesCity(premisesCity);
                payload.setPremisesZipCode(validZipCode);
                payload.setPremisesStreetNumber(premisesStreetNumber);
                payload.setPremisesStreetPreDirection(FakerDataGenerator.generateString(3));
                break;


            case SEARCH_ACCOUNTS_INVALID_STREET_NAME_LENGTH_TC_31:
                payload.setPremisesStreetName(FakerDataGenerator.generateString(31));
                payload.setPremisesStreetSuffix(premisesStreetSuffix);
                payload.setPremisesStreetPostDirection(premisesStreetPostDirection);
                payload.setPremisesCity(premisesCity);
                payload.setPremisesZipCode(validZipCode);
                payload.setPremisesStreetNumber(premisesStreetNumber);
                break;

            default:
                payload.setRequestID(FakerDataGenerator.generateString(10));
        }
    }
}
