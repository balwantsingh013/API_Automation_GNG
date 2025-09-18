package com.gng.api.pages.turnOff.AccountsApiPages.SearchAccounts;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.AccountsPojo.SearchAccounts.SearchAccountsRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.turnOff.AccountsApiSteps.SearchAccounts.SearchAccountsTOffApiLabel;
import com.gng.api.steps.turnOn.AccountsApiSteps.SearchAccounts.SearchAccountsApiLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Map;
import static com.gng.api.steps.AesEncryption.AesEncryptionSteps.encryptData;
import static com.gng.api.steps.turnOff.AccountsApiSteps.SearchAccounts.SearchAccountsTOffApiLabel.VALID_CUST_CODE_INVALID_PREM_CODE_TC_74;

@Slf4j
public class SearchAccountsHelper {

    private final TestContext testContext;
    public String premisesCode;
    public String customerCode;
    public String lastName;
    public String zipCode;
    public String firstName;
    public String customerBusinessName;
    public String socialSecurityNumber= "666398181";
    public static final String TRANS_TYPE_TOFF="TOFF";
    public String aglcAccountNumber;
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
    public String validZipCode="30307";
    public String preisesUnitType="#";

    public SearchAccountsHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    SearchAccountsRequest preparePayload(SearchAccountsTOffApiLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        String jsonFileName = apiLabel.equals(SearchAccountsTOffApiLabel.search_accounts)
                ? SearchAccountsApiLabel.search_accounts.toString()
                : SearchAccountsApiLabel.search_accounts_mandatory.toString();
        return BasePage.deserializeJsonToPojo(jsonFileName, SearchAccountsRequest.class);
    }

    private void setTransactionTypeAndRequestId(SearchAccountsRequest payload) {
        payload.setTransactionType(TRANS_TYPE_TOFF);
        payload.setRequestID(FakerDataGenerator.generateString(10));
    }

    public void  getLastNameAndZipcodeFromDbAndPreparePayload(SearchAccountsRequest payload, SearchAccountsTOffApiLabel combinationType) {
        Map<String, Object> accountDetailsLastNameZipCode = ApplicationContext.get().getDbAction().getAccountDetails_LastNameZipCode();
        lastName=accountDetailsLastNameZipCode.get("UZBENRO_DSM_LAST_NAME").toString();
        zipCode=accountDetailsLastNameZipCode.get("UCRADDR_ZIP").toString();
        payload.setCustomerLastName(lastName);
        setTransactionTypeAndRequestId(payload);
        switch(combinationType){
            case INVALID_COMBINATION_OF_LASTNAME_ZIPCODE_TC_77:
                payload.setCustomerLastName(FakerDataGenerator.generateLowerCaseString(5));
                payload.setPremisesZipCode(FakerDataGenerator.generateDigits(5));
                break;
            case VALID_COMBINATION_OF_LASTNAME_ZIPCODE_TC_78:
                payload.setPremisesZipCode(zipCode);
        }
    }

    public void  getFirstNameLastNameAndZipcodeFromDbAndPreparePayload(SearchAccountsRequest payload, SearchAccountsTOffApiLabel accountType) {
        String account_Type=null;
        switch(accountType){
            case RESIDENTIAL_VALID_ACTIVE_ACCOUNT_TC_79:
                account_Type="RS";
                break;
            case SENIOR_RESIDENTIAL_VALID_ACTIVE_ACCOUNT_TC_80:
                account_Type="SR";
        }
        Map<String, Object> accountDetailsLastNameZipCode = ApplicationContext.get().getDbAction().getAccountDetails_ForResidentialOrSeniorResAccount(account_Type);
        firstName=accountDetailsLastNameZipCode.get("UZBENRO_DSM_FIRST_NAME").toString();
        lastName=accountDetailsLastNameZipCode.get("UZBENRO_DSM_LAST_NAME").toString();
        zipCode=accountDetailsLastNameZipCode.get("UCRADDR_ZIP").toString();
        payload.setCustomerFirstName(firstName);
        payload.setCustomerLastName(lastName);
        payload.setPremisesZipCode(zipCode);
        setTransactionTypeAndRequestId(payload);
    }

    public void  getCustomerBusinessNameFromDbAndPreparePayload(SearchAccountsRequest payload, SearchAccountsTOffApiLabel accountType) {
        Map<String, Object> validCustomerBusinessName=null;
        switch(accountType){
            case COMMERCIAL_VALID_ACTIVE_PASTDUEBALANCE_ACCOUNT_TC_81:
                validCustomerBusinessName = ApplicationContext.get().getDbAction().getAccountDetails_ForPastDueBalanceCommercialAccount();
                break;

            case COMMERCIAL_VALID_ACTIVE_SONP_ACCOUNT_TC_82:
                validCustomerBusinessName = ApplicationContext.get().getDbAction().getAccountDetails_ForSONPCommercialAccount();
                break;

            case COMMERCIAL_VALID_ACTIVE_PENDING_REWARDS_TC_83:
                validCustomerBusinessName = ApplicationContext.get().getDbAction().getCustomBusnsNm_ForActPenRewardCommercialAccount();
                break;

            case COMMERCIAL_VALID_ACTIVE_ETC_TC_85:
                validCustomerBusinessName = ApplicationContext.get().getDbAction().getCustomerBusinessNameCMActiveETC();
                break;

            case COMMERCIAL_VALID_ACTIVE_NO_ETC_TC_86:
                validCustomerBusinessName = ApplicationContext.get().getDbAction().getCustomerBusinessNameCMActiveNoETC();
                break;

            case COMMERCIAL_VALID_ACTIVE_PRICE_PLAN_CCV_TC_87:
                validCustomerBusinessName = ApplicationContext.get().getDbAction().getCustomerBusinessNameCMActiveCCV();
                break;

            case COMMERCIAL_VALID_FINAL_ACCOUNT_TC_88:
                validCustomerBusinessName = ApplicationContext.get().getDbAction().getCustomerBusinessNameCMFinalAccount();
                break;
        }
        customerBusinessName=validCustomerBusinessName.get("UCBCUST_LAST_NAME").toString();
        setTransactionTypeAndRequestId(payload);
        payload.setCustomerBusinessName(customerBusinessName);
    }

    public void setParametersToEmpty(SearchAccountsRequest payload){
        payload.setCustomerCode("");
        payload.setCustomerLastName("");
        payload.setCustomerFirstName("");
        payload.setPremisesZipCode("");
        payload.setCustomerBusinessName("");
        payload.setPremisesCode("");
    }

    public void getAGLCNumberFromDbAndPreparePayloadTC_103(SearchAccountsRequest payload) {
        Map<String, Object> aglcNumber = ApplicationContext.get().getDbAction().getAGLCNumberRSActiveAccount();
        aglcAccountNumber=aglcNumber.get("GTBTRNH_AGLC_ACCT_NBR").toString();
        setTransactionTypeAndRequestId(payload);
        payload.setAglcAccountNumber(aglcAccountNumber);
    }

    public void preparePayloadForInvalidRequestId(SearchAccountsRequest payload, SearchAccountsTOffApiLabel testCondition){
        payload.setTransactionType(GlobalEnums.TransactionType.TURN_OFF.getValue());
        switch (testCondition) {
            case SEARCH_ACCOUNTS_MISSING_REQUEST_ID_TC_12:
                payload.setRequestID(null);
                break;
            case SEARCH_ACCOUNTS_INVALID_REQUEST_ID_LENGTH_TC_13:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(33));
                break;
            case SEARCH_ACCOUNTS_DUPLICATE_REQUEST_ID_TC_14:
                payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());
                break;
            default:
                payload.setRequestID(FakerDataGenerator.generateString(10));
        }
    }

    public void preparePayloadForInvalidField(SearchAccountsRequest payload, SearchAccountsTOffApiLabel testCondition) {
        payload.setTransactionType(GlobalEnums.TransactionType.TURN_OFF.getValue());
        payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
        setParametersToEmpty(payload);
        switch (testCondition) {
            case SEARCH_ACCOUNTS_MISSING_CUSTOMER_CODE_TC_19:
                payload.setPremisesCode(FakerDataGenerator.generateDigits(6));
                break;

            case SEARCH_ACCOUNTS_MISSING_PREMISES_CODE_TC_20:
                payload.setCustomerCode(FakerDataGenerator.generateDigits(6));
                break;

            case INVALID_LENGTH_CUSTOMER_CODE_TC_21:
                payload.setCustomerCode("1"+FakerDataGenerator.generateDigits(9));
                break;

            case INVALID_LENGTH_PREMISES_CODE_TC_22:
                payload.setPremisesCode("1"+FakerDataGenerator.generateDigits(7));
                break;

            case MISSING_TRANSACTION_TYPE_TC_23:
                payload.setTransactionType("");
                break;

            case INVALID_TRANSACTION_TYPE_TC_24:
                payload.setTransactionType(FakerDataGenerator.generateUpperCaseString(4));
                break;

            case INVALID_TRANSACTION_TYPE_LENGTH_TC_25:
                payload.setTransactionType(FakerDataGenerator.generateUpperCaseString(5));
                break;

            case INVALID_CUSTOMER_BUSINESS_NAME_LENGTH_TC_26:
                payload.setCustomerBusinessName(FakerDataGenerator.generateString(61));
                break;

            case INVALID_LAST_NAME_LENGTH_TC_27:
                payload.setCustomerLastName(FakerDataGenerator.generateString(61));
                break;

            case INVALID_LASTNAME_AND_ZIP_COMBINATION_TC_28:
                payload.setCustomerLastName(FakerDataGenerator.generateString(5));
                payload.setPremisesZipCode(validZipCode);
                break;

            case MISSING_ZIP_CODE_TC_29:
                payload.setCustomerLastName(FakerDataGenerator.generateString(5));
                payload.setPremisesZipCode("");
                break;

            case MISSING_TRANSACTION_TYPE_LAST_NAME_AND_ZIP_GIVEN_TC_30:
                payload.setCustomerLastName(FakerDataGenerator.generateString(5));
                payload.setPremisesZipCode(validZipCode);
                payload.setTransactionType("");
                break;

            case INVALID_FIRST_NAME_LENGTH_TC_31:
                payload.setCustomerFirstName(FakerDataGenerator.generateString(16));
                break;

            case UNENCRYPTED_SSN_TC_32:
                payload.setSocialSecurityNumber(socialSecurityNumber);
                break;

            case INVALID_SSN_LENGTH_TC_33:
                payload.setSocialSecurityNumber("1"+FakerDataGenerator.generateDigits(32));
                break;

            case FEDERAL_TAX_ID_NOT_ALLOWED_TC_34:
                payload.setFederalTaxID(encryptData(FakerDataGenerator.generateDigits(9)));
                payload.setCustomerBusinessName(FakerDataGenerator.generateString(10));
                break;

            case SSN_AND_FEDERAL_TAX_ID_PROVIDED_TC_35:
                payload.setSocialSecurityNumber(encryptData(socialSecurityNumber));
                payload.setFederalTaxID(encryptData(FakerDataGenerator.generateDigits(9)));
                break;

            case INVALID_PARAMETER_PROVIDED_PHONE_NUMBER_TC_36:
                payload.setCustomerLastName(FakerDataGenerator.generateLastName());
                payload.setPhoneNumber(FakerDataGenerator.generateDigits(10));
                break;

            case INVALID_AGLC_ACCOUNT_NUMBER_LENGTH_TC_37:
                payload.setAglcAccountNumber(FakerDataGenerator.generateDigits(21));
                payload.setCustomerLastName(FakerDataGenerator.generateLastName());
                payload.setPremisesZipCode(validZipCode);
                break;

            case INVALID_AGLC_ACCOUNT_NUMBER_LENGTH_BUSINESS_NAME_GIVEN_TC_38:
                payload.setAglcAccountNumber(FakerDataGenerator.generateDigits(21));
                payload.setCustomerBusinessName(FakerDataGenerator.generateLastName());
                break;

            case NON_NUMERIC_AGLC_ACCOUNT_NUMBER_TC_39:
                payload.setAglcAccountNumber(FakerDataGenerator.generateAlphanumericWithSpecialChars(8));
                break;

            case INVALID_PREMISES_STREET_NUMBER_LENGTH_TC_40:
                setAddressDetails(payload);
                payload.setPremisesStreetNumber("1"+FakerDataGenerator.generateDigits(12));
                break;

            case INVALID_STREET_PREDIRECTION_LENGTH_TC_41:
                setAddressDetails(payload);
                payload.setPremisesStreetPreDirection(FakerDataGenerator.generateUpperCaseString(3));
                break;

            case INVALID_PREMISES_STREET_NAME_LENGTH_TC_42:
                setAddressDetails(payload);
                payload.setPremisesStreetName(FakerDataGenerator.generateString(31));
                break;

            case MISSING_PREMISES_STREET_NAME_TC_43:
                setAddressDetails(payload);
                payload.setPremisesStreetName(null);
                break;

            case INVALID_STREET_SUFFIX_LENGTH_TC_44:
                setAddressDetails(payload);
                payload.setPremisesStreetSuffix(FakerDataGenerator.generateUpperCaseString(7));
                break;

            case INVALID_PREMISES_STREET_POST_DIRECTION_LENGTH_TC_45:
                setAddressDetails(payload);
                payload.setPremisesStreetPostDirection(FakerDataGenerator.generateUpperCaseString(3));
                break;

            case INVALID_UNIT_TYPE_LENGTH_TC_46:
                setAddressDetails(payload);
                payload.setPremisesUnitType(FakerDataGenerator.generateAlphanumericWithSpecialChars(7));
                break;

            case PREMISES_UNIT_TYPE_MISSING_TC_47:
                setAddressDetails(payload);
                payload.setPremisesUnitType(null);
                payload.setPremisesUnitNumber(FakerDataGenerator.generateDigits(3));
                break;

            case INVALID_PREMISES_UNIT_NUMBER_FORMAT_TC_48:
                setAddressDetails(payload);
                payload.setPremisesUnitType(preisesUnitType);
                payload.setPremisesUnitNumber("1"+FakerDataGenerator.generateDigits(7));
                break;

            case MISSING_UNIT_NUMBER_TC_49:
                setAddressDetails(payload);
                payload.setPremisesUnitType(preisesUnitType);
                payload.setPremisesUnitNumber(null);
                break;

            case INVALID_PREMISES_CITY_LENGTH_TC_50:
                setAddressDetails(payload);
                payload.setPremisesCity(FakerDataGenerator.generateString(21));
                break;

            case MISSING_PREMISES_CITY_TC_51:
                setAddressDetails(payload);
                payload.setPremisesCity(null);
                break;

            case INVALID_STATE_CODE_LENGTH_TC_52:
                setAddressDetails(payload);
                payload.setPremisesStateCode(FakerDataGenerator.generateUpperCaseString(4));
                break;

            case MISSING_STATE_CODE_TC_53:
                setAddressDetails(payload);
                payload.setPremisesStateCode(null);
                break;

            case INVALID_ZIP_CODE_LENGTH_TC_54A:
                setAddressDetails(payload);
                payload.setPremisesZipCode(FakerDataGenerator.generateDigits(7));
                break;

            case INVALID_ZIP_CODE_LENGTH_LESS_THAN_5_TC_54B:
                setAddressDetails(payload);
                payload.setPremisesZipCode(FakerDataGenerator.generateDigits(4));
                break;

            case MISSING_ZIP_CODE_TC_55:
                setAddressDetails(payload);
                payload.setPremisesZipCode(null);
                break;


            default:

        }
    }

    public void setAddressDetails(SearchAccountsRequest payload){
        payload.setPremisesStreetNumber(premisesStreetNumber);
        payload.setPremisesStreetName(premisesStreetName);
        payload.setPremisesCity(premisesCity);
        payload.setPremisesStateCode(premisesStateCode);
        payload.setPremisesZipCode(validZipCode);

    }
            public void preparePayloadForInvalidLoginId(SearchAccountsRequest payload, SearchAccountsTOffApiLabel testCondition) {
        payload.setTransactionType(GlobalEnums.TransactionType.TURN_OFF.getValue());
        payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
        switch (testCondition) {
            case SEARCH_ACCOUNTS_MISSING_LOGIN_ID_TC_15:
                payload.setLoginID(null);
                break;

            case SEARCH_ACCOUNTS_INVALID_LOGIN_ID_TC_16:
                payload.setLoginID(FakerDataGenerator.generateAlphanumeric(31));
                break;

            case SEARCH_ACCOUNTS_NON_ALPHANUMERIC_ID_TC_17:
                payload.setLoginID(FakerDataGenerator.generateAlphanumericWithSpecialChars(8));
                break;

            case SEARCH_ACCOUNT_LOGIN_ID_NOT_IN_USERS_TABLE_TC_18:
                payload.setLoginID(GlobalEnums.InvalidValues.INVALID_LOGIN_ID.getValue());
                break;
        }
    }

    public void getAddressDetailsFromDbAndPreparePayloadTC_104(SearchAccountsRequest payload, SearchAccountsTOffApiLabel testCondition) {
        Map<String, Object> addressDetails = ApplicationContext.get().getDbAction().getAddressDetailsRSActiveAccount();

        switch(testCondition){
            case ALL_PREMISES_FIELDS_TC_104:
                premisesStreetNumber=addressDetails.get("UCRADDR_STREET_NUMBER").toString();
                premisesStreetPreDirection=addressDetails.get("UCRADDR_PDIR_CODE_PRE").toString();
                premisesStreetSuffix=addressDetails.get("UCRADDR_SSFX_CODE").toString();
                premisesStreetPostDirection=addressDetails.get("UCRADDR_PDIR_CODE_POST").toString();
                premisesUnitType=addressDetails.get("UCRADDR_UTYP_CODE").toString();
                premisesUnitNumber=addressDetails.get("UCRADDR_UNIT").toString();

                payload.setPremisesStreetNumber(premisesStreetNumber);
                payload.setPremisesStreetPreDirection(premisesStreetPreDirection);
                payload.setPremisesStreetSuffix(premisesStreetSuffix);
                payload.setPremisesStreetPostDirection(premisesStreetPostDirection);
                payload.setPremisesUnitType(premisesUnitType);
                payload.setPremisesUnitNumber(premisesUnitNumber);
                break;

            case PREMISES_STREET_NAME_CITY_STATE_ZIP_TC_105:
                //no changes need to be made other than common fields below
                break;
        }

        premisesStreetName=addressDetails.get("UCRADDR_STREET_NAME").toString();
        premisesCity=addressDetails.get("UCRADDR_CITY").toString();
        premisesStateCode=addressDetails.get("UCRADDR_STAT_CODE").toString();
        premisesZipCode=addressDetails.get("UCRADDR_ZIP").toString();

        payload.setPremisesStreetName(premisesStreetName);
        payload.setPremisesCity(premisesCity);
        payload.setPremisesStateCode(premisesStateCode);
        payload.setPremisesZipCode(premisesZipCode);
        setTransactionTypeAndRequestId(payload);
    }

    public void getSSNFromDbAndPreparePayloadTC_89(SearchAccountsRequest payload) {
        setTransactionTypeAndRequestId(payload);
        payload.setSocialSecurityNumber(encryptData(socialSecurityNumber));
    }

    public void getCustomerPremisesCodeFromDbAndPreparePayload(SearchAccountsRequest payload,SearchAccountsTOffApiLabel testCondition) {
        Map<String, Object> custPremCode=null;
        String account_Type=null;
        List<Map<String, Object>> accountDetailsCustCode=null;
        String SSpIndicator;
        String customerType="";

        switch (testCondition) {
            case ACTIVE_RS_NEW_NON_METERED_TC_56:
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeRSActiveNonMeteredAccount();
                break;

            case ACTIVE_MSB_ACCOUNT_TC_57:
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeMSBAccount();
                break;

            case ACTIVE_RS_NEW_NON_METERED_TC_58:
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeActiveNonMeteredAccount();
                break;

            case INACTIVE_RS_METERED_TC_59:
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeInactiveMeteredAccount();
                break;

            case INACTIVE_BAD_DEBT_BALANCE_TC_60:
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeInactiveAccWithBadDebt();
                break;

            case RS_INACTIVE_WITH_SONP_TC_61:
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeInactiveAccWithSONP();
                break;

            case RS_INACTIVE_NON_METER_TC_62:
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeInactiveNonMeteredAccount();
                break;

            case RS_NEW_BANKRUPCY_TC_63:
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeResidentialNewBankrupcy();
                break;

            case RS_INACTIVE_BANKRUPCY_TC_64:
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeResidentialInactiveBankrupcy();
                break;

            case CM_NEW_NON_METERED_TC_65:
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeCMNewNonMetered();
                break;

            case CM_ACTIVE_NON_METERED_TC_67:
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeCMActiveNonMetered();
                break;

            case CM_INACTIVE_METERED_TC_68:
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeCMInactiveMetered();
                break;

            case CM_INACTIVE_BAD_DEBT_TC_69:
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeCMInactiveBadDebt();
                break;

            case CM_INACTIVE_SONP_TC_70:
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeCMInactiveSONP();
                break;

            case CM_INACTIVE_NON_METERED_TC_71:
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeCMInactiveNonMetered();
                break;

            case CM_NEW_BANKRUPCY_TC_72:
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeCMNewBakrupcy();
                break;

            case CM_INACTIVE_BANKRUPCY_TC_73:
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeCMInactiveBakrupcy();
                break;

            case VALID_CUST_CODE_INVALID_PREM_CODE_TC_74:
                accountDetailsCustCode = ApplicationContext.get().getDbAction().getActiveCustomerDetails();
                break;

            case RS_ACTIVE_ACCOUNT_TC_75:
                account_Type="RS";
                custPremCode = ApplicationContext.get().getDbAction().getAccountDetails_ForResidentialOrCommercialAccount(account_Type);
                break;

            case CM_ACTIVE_ACCOUNT_TC_76:
                account_Type="CM";
                custPremCode = ApplicationContext.get().getDbAction().getAccountDetails_ForResidentialOrCommercialAccount(account_Type);
                break;

            case RS_ACTIVE_SONP_NON_MASTER_TC_91:
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeRSSONPNonMaster();
                break;

            case RS_ACTIVE_PENDING_REWARDS_TC_92:
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeRSActivePendingRewards();
                break;

            case RS_ACTIVE_UNAPPLIED_DEPOSIT_TC_93:
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeRSActiveUnappliedDeposit();
                break;

            case RS_ACTIVE_NO_UNAPPLIED_DEPOSIT_TC_94:
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeRSActiveNoUnappliedDeposit();
                break;

            case RS_ACTIVE_PGB_PRICE_PLAN_EXP_DATE_TC_95:
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeRSActivePGBExpirationDate();
                break;

            case RS_ACTIVE_MKT_PRICE_PLAN_NO_EXP_DATE_TC_96:
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeRSActiveMKTNoExpirationDate();
                break;

            case RS_ACTIVE_ETC_TC_97:
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeRSActiveETC();
                break;

            case RS_ACTIVE_ETC_RGB_PRICE_PLAN_TC_98:
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeRSActiveETCRGB();
                break;

            case RS_ACTIVE_GREENER_LIFE_TC_99:
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeRSActiveGreenerLife();
                break;

            case RS_ACTIVE_CSV_PRICE_PLAN_TC_100:
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeRSActiveCSV();
                break;

            case RS_FINAL_ACCOUNT_TC_101:
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeRSFinal();
                break;

            case RS_ACTIVE_ACCOUNT_TC_102:
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeRSActive();
                break;

            case SSP_ACCOUNT_WITH_ETC_TC_105A:
                SSpIndicator="Y";
                customerType="RS";
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeSSPAccountwithETC(SSpIndicator, customerType);
                break;

            case SSP_ACCOUNT_WITHOUT_ETC_TC_105B:
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeSSPAccountiWithoutETCTC105B();
                break;

            case NON_SSP_ACCOUNT_WITH_ETC_TC_105C:
                SSpIndicator="N";
                customerType="CM";
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeSSPAccountwithETC(SSpIndicator, customerType);
                break;

            case NON_SSP_ACCOUNT_WITHOUT_ETC_TC_105D:
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeSSPAccountiWithoutETC();
                break;
        }

        switch(testCondition){
            case INACTIVE_BAD_DEBT_BALANCE_TC_60:
                customerCode=custPremCode.get("UABOPEN_CUST_CODE").toString();
                premisesCode=custPremCode.get("UABOPEN_PREM_CODE").toString();
                break;

            case CM_INACTIVE_BAD_DEBT_TC_69:
                customerCode=custPremCode.get("UABBDBT_CUST_CODE").toString();
                premisesCode=custPremCode.get("UABBDBT_PREM_CODE").toString();
                break;

            case VALID_CUST_CODE_INVALID_PREM_CODE_TC_74:
                customerCode=accountDetailsCustCode.getFirst().get("UCRACCT_CUST_CODE").toString();
                setTransactionTypeAndRequestId(payload);
                payload.setCustomerCode(customerCode);
                payload.setPremisesCode(FakerDataGenerator.generateDigits(7));
                break;

            case RS_ACTIVE_SONP_NON_MASTER_TC_91:
            case RS_ACTIVE_UNAPPLIED_DEPOSIT_TC_93:
            case RS_ACTIVE_NO_UNAPPLIED_DEPOSIT_TC_94:
            case RS_ACTIVE_PGB_PRICE_PLAN_EXP_DATE_TC_95:
            case RS_ACTIVE_MKT_PRICE_PLAN_NO_EXP_DATE_TC_96:
                customerCode=custPremCode.get("UCRSCMP_CUST_CODE").toString();
                premisesCode=custPremCode.get("UCRSCMP_PREM_CODE").toString();
                break;

            case SSP_ACCOUNT_WITHOUT_ETC_TC_105B:
            case NON_SSP_ACCOUNT_WITHOUT_ETC_TC_105D:
                customerCode=custPremCode.get("UZBENRO_CUST_CODE").toString();
                premisesCode=custPremCode.get("UZBENRO_PREM_CODE").toString();
                break;

            default:
                customerCode=custPremCode.get("UCRACCT_CUST_CODE").toString();
                premisesCode=custPremCode.get("UCRACCT_PREM_CODE").toString();
        }

        if(!(testCondition.equals(VALID_CUST_CODE_INVALID_PREM_CODE_TC_74))){
            setTransactionTypeAndRequestId(payload);
            payload.setCustomerCode(customerCode);
            payload.setPremisesCode(premisesCode);
        }
    }
}
