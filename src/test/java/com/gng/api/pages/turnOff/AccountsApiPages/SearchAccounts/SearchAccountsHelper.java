package com.gng.api.pages.turnOff.AccountsApiPages.SearchAccounts;

import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.AccountsPojo.SearchAccounts.SearchAccountsRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.AesEncryption.AesEncryptionSteps;
import com.gng.api.steps.turnOff.AccountsApiSteps.SearchAccounts.SearchAccountsTOffApiLabel;
import com.gng.api.steps.turnOn.AccountsApiSteps.SearchAccounts.SearchAccountsApiLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

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
    public String socialSecurityNumber;
    public static final String TRANS_TYPE_TOFF="TOFF";
    public String aglcAccountNumber;
    public String premisesStreetNumber;
    public String premisesStreetPreDirection;
    public String premisesStreetName;
    public String premisesStreetSuffix;
    public String premisesStreetPostDirection;
    public String premisesUnitType;
    public String premisesUnitNumber;
    public String premisesCity;
    public String premisesStateCode;
    public String premisesZipCode;

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

    public void  getLastNameAndZipcodeFromDbAndPreparePayload(SearchAccountsRequest payload, SearchAccountsTOffApiLabel combinationType) {
        Map<String, Object> accountDetailsLastNameZipCode = ApplicationContext.get().getDbAction().getAccountDetails_LastNameZipCode();
        lastName=accountDetailsLastNameZipCode.get("UZBENRO_DSM_LAST_NAME").toString();
        zipCode=accountDetailsLastNameZipCode.get("UCRADDR_ZIP").toString();
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setCustomerLastName(lastName);
        payload.setTransactionType(TRANS_TYPE_TOFF);
        switch(combinationType){
            case INVALID_COMBINATION_OF_LASTNAME_ZIPCODE_TC77:
                payload.setPremisesZipCode(FakerDataGenerator.generateDigits(5));
            case VALID_COMBINATION_OF_LASTNAME_ZIPCODE_TC78:
                payload.setPremisesZipCode(zipCode);
        }
    }

    public void  getFirstNameLastNameAndZipcodeFromDbAndPreparePayload(SearchAccountsRequest payload, SearchAccountsTOffApiLabel accountType) {
        String account_Type=null;
        switch(accountType){
            case RESIDENTIAL_VALID_ACTIVE_ACCOUNT_TC79:
                account_Type="RS";
            case SENIOR_RESIDENTIAL_VALID_ACTIVE_ACCOUNT_TC80:
                account_Type="SR";
        }
        Map<String, Object> accountDetailsLastNameZipCode = ApplicationContext.get().getDbAction().getAccountDetails_ForResidentialOrSeniorResAccount(account_Type);
        firstName=accountDetailsLastNameZipCode.get("UZBENRO_DSM_FIRST_NAME").toString();
        lastName=accountDetailsLastNameZipCode.get("UZBENRO_DSM_LAST_NAME").toString();
        zipCode=accountDetailsLastNameZipCode.get("UCRADDR_ZIP").toString();
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setCustomerFirstName(firstName);
        payload.setCustomerLastName(lastName);
        payload.setPremisesZipCode(zipCode);
        payload.setTransactionType(TRANS_TYPE_TOFF);


    }

    public void  getCustomerBusinessNameFromDbAndPreparePayload(SearchAccountsRequest payload, SearchAccountsTOffApiLabel accountType) {
        Map<String, Object> accountDetailsLastNameZipCode=null;
        switch(accountType){
            case COMMERCIAL_VALID_ACTIVE_PASTDUEBALANCE_ACCOUNT_TC81:
                accountDetailsLastNameZipCode = ApplicationContext.get().getDbAction().getAccountDetails_ForPastDueBalanceCommercialAccount();
            case COMMERCIAL_VALID_ACTIVE_SONP_ACCOUNT_TC82:
                accountDetailsLastNameZipCode = ApplicationContext.get().getDbAction().getAccountDetails_ForSONPCommercialAccount();
        }
        customerBusinessName=accountDetailsLastNameZipCode.get("UCBCUST_LAST_NAME").toString();
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setTransactionType(TRANS_TYPE_TOFF);
        payload.setCustomerBusinessName(customerBusinessName);
    }

    public void  getCustomerBusinessNameFromDbAndPreparePayloadTC_83(SearchAccountsRequest payload) {
        Map<String, Object> accountDetailsCustomerBsnName = ApplicationContext.get().getDbAction().getCustomBusnsNm_ForActPenRewardCommercialAccount();
        customerBusinessName=accountDetailsCustomerBsnName.get("UCBCUST_LAST_NAME").toString();
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setTransactionType(TRANS_TYPE_TOFF);
        payload.setCustomerBusinessName(customerBusinessName);
    }

    public void getCustomerBusinessNameFromDbAndPreparePayloadTC_85(SearchAccountsRequest payload) {
        Map<String, Object> accountDetailsCustomerBsnName = ApplicationContext.get().getDbAction().getCustomerBusinessNameCMActiveETC();
        customerBusinessName=accountDetailsCustomerBsnName.get("UCBCUST_LAST_NAME").toString();
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setTransactionType(TRANS_TYPE_TOFF);
        payload.setCustomerBusinessName(customerBusinessName);
    }

    public void getCustomerBusinessNameFromDbAndPreparePayloadTC_86(SearchAccountsRequest payload) {
        Map<String, Object> accountDetailsCustomerBsnName = ApplicationContext.get().getDbAction().getCustomerBusinessNameCMActiveNoETC();
        customerBusinessName=accountDetailsCustomerBsnName.get("UCBCUST_LAST_NAME").toString();
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setTransactionType(TRANS_TYPE_TOFF);
        payload.setCustomerBusinessName(customerBusinessName);
    }

    public void getCustomerBusinessNameFromDbAndPreparePayloadTC_87(SearchAccountsRequest payload) {
        Map<String, Object> accountDetailsCustomerBsnName = ApplicationContext.get().getDbAction().getCustomerBusinessNameCMActiveCCV();
        customerBusinessName=accountDetailsCustomerBsnName.get("UCBCUST_LAST_NAME").toString();
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setTransactionType(TRANS_TYPE_TOFF);
        payload.setCustomerBusinessName(customerBusinessName);
    }

    public void getCustomerBusinessNameFromDbAndPreparePayloadTC_88(SearchAccountsRequest payload) {
        Map<String, Object> accountDetailsCustomerBsnName = ApplicationContext.get().getDbAction().getCustomerBusinessNameCMFinalAccount();
        customerBusinessName=accountDetailsCustomerBsnName.get("UCBCUST_LAST_NAME").toString();
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setTransactionType(TRANS_TYPE_TOFF);
        payload.setCustomerBusinessName(customerBusinessName);
    }

    public void getAGLCNumberFromDbAndPreparePayloadTC_103(SearchAccountsRequest payload) {
        Map<String, Object> aglcNumber = ApplicationContext.get().getDbAction().getAGLCNumberRSActiveAccount();
        aglcAccountNumber=aglcNumber.get("GTBTRNH_AGLC_ACCT_NBR").toString();
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setTransactionType(TRANS_TYPE_TOFF);
        payload.setAglcAccountNumber(aglcAccountNumber);
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
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setTransactionType(TRANS_TYPE_TOFF);
    }

    public void getSSNFromDbAndPreparePayloadTC_89(SearchAccountsRequest payload) {
        Map<String, Object> accountDetailsCustomerSSN = ApplicationContext.get().getDbAction().getCustomerSSNActiveRSAccount();
        String UnencryptedSSN=accountDetailsCustomerSSN.get("UCBCUST_SSN").toString();
        socialSecurityNumber= AesEncryptionSteps.encryptData(UnencryptedSSN);
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setTransactionType(TRANS_TYPE_TOFF);
        payload.setSocialSecurityNumber(socialSecurityNumber);
    }

    public void getCustomerPremisesCodeFromDbAndPreparePayload(SearchAccountsRequest payload,SearchAccountsTOffApiLabel testCondition) {
        Map<String, Object> custPremCode=null;
        String account_Type=null;
        List<Map<String, Object>> accountDetailsCustCode=null;
        String SSpIndicator;

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
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeSSPAccountwithETC(SSpIndicator);
                break;

            case SSP_ACCOUNT_WITHOUT_ETC_105B:
                SSpIndicator="Y";
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeSSPAccountiWithoutETC(SSpIndicator);
                break;

            case NON_SSP_ACCOUNT_WITH_ETC_105C:
                SSpIndicator="N";
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeSSPAccountwithETC(SSpIndicator);
                break;

            case NON_SSP_ACCOUNT_WITHOUT_ETC_105D:
                SSpIndicator="N";
                custPremCode = ApplicationContext.get().getDbAction().getCustPremCodeSSPAccountiWithoutETC(SSpIndicator);
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
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setCustomerCode(customerCode);
                payload.setPremisesCode(FakerDataGenerator.generateDigits(7));
                payload.setTransactionType(TRANS_TYPE_TOFF);
                break;

            case RS_ACTIVE_SONP_NON_MASTER_TC_91:
            case RS_ACTIVE_UNAPPLIED_DEPOSIT_TC_93:
            case RS_ACTIVE_NO_UNAPPLIED_DEPOSIT_TC_94:
            case RS_ACTIVE_PGB_PRICE_PLAN_EXP_DATE_TC_95:
            case RS_ACTIVE_MKT_PRICE_PLAN_NO_EXP_DATE_TC_96:
                customerCode=custPremCode.get("UCRSCMP_CUST_CODE").toString();
                premisesCode=custPremCode.get("UCRSCMP_PREM_CODE").toString();
                break;

            case SSP_ACCOUNT_WITHOUT_ETC_105B:
            case NON_SSP_ACCOUNT_WITHOUT_ETC_105D:
                customerCode=custPremCode.get("UZBENRO_CUST_CODE").toString();
                premisesCode=custPremCode.get("UZBENRO_PREM_CODE").toString();
                break;

            default:
                customerCode=custPremCode.get("UCRACCT_CUST_CODE").toString();
                premisesCode=custPremCode.get("UCRACCT_PREM_CODE").toString();
        }

        if(!(testCondition.equals(VALID_CUST_CODE_INVALID_PREM_CODE_TC_74))){
            payload.setRequestID(FakerDataGenerator.generateString(10));
            payload.setTransactionType(TRANS_TYPE_TOFF);
            payload.setCustomerCode(customerCode);
            payload.setPremisesCode(premisesCode);
        }
    }

}
