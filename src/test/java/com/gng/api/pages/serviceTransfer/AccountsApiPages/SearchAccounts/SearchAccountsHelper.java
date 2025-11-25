package com.gng.api.pages.serviceTransfer.AccountsApiPages.SearchAccounts;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.AccountsPojo.SearchAccounts.SearchAccountsRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.serviceTransfer.AccountsApiSteps.SearchAccounts.SearchAccountsApiLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

import static com.gng.api.constants.DBConstant.UCRACCT_CUST_CODE;
import static com.gng.api.constants.DBConstant.UCRACCT_PREM_CODE;
import static com.gng.api.steps.AesEncryption.AesEncryptionSteps.encryptData;
import static com.gng.api.steps.serviceTransfer.ServiceOrdersSteps.GetEligiblePlansAndOffers.GetEligiblePlansAndOffersApiLabel.*;
import static com.gng.api.steps.serviceTransfer.ServiceOrdersSteps.SaveEnrollment.SaveEnrollmentApiLabel.*;

@Slf4j
public class SearchAccountsHelper {
    private final TestContext testContext;
    public String validZipCode="30307";
    public String socialSecurityNumber= "666398181";
    public String premisesStreetNumber="442";
    public String premisesStreetName="CLIFTON";
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

    public void setAddressDetails(SearchAccountsRequest payload){
        payload.setPremisesStreetNumber(premisesStreetNumber);
        payload.setPremisesStreetName(premisesStreetName);
        payload.setPremisesCity(premisesCity);
        payload.setPremisesStateCode(premisesStateCode);
        payload.setPremisesZipCode(validZipCode);

    }

    public void fetchRecordFromDBForTestConditions(SearchAccountsApiLabel testCondition){
        Map<String, Object> validCustomerDetails=null;
        switch (testCondition) {
            case ACN_RS_REMAINS_ON_TIER_1_TC_251:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeResidentialTier1();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case ACN_RS_REMAINS_ON_TIER_1_NACN_TC_252:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeResidentialTier1NACN();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case ACN_RS_TC_253:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeResidentialACNRS();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case NACN_RS_TC_254:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC254();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case ACN_RS_TC_255:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC255();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case NACN_RS_TC_256:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC256();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case NACN_SR_TC_258:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC258();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case ACN_RS_TC_259:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC259();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case NACN_RS_TC_260:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC260();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case NACN_SR_TC_262:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC262();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case ACN_RS_TC_263:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC263();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case NACN_RS_TC_264:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC264();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case ACN_RS_TC_265:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC265();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case NACN_RS_TC_266:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC266();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case NACN_RS_TC_267:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC267();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case ACN_RS_TC_268:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC268();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case ACN_RS_TC_269:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC269();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case NACN_RS_TC_270:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC270();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case ACN_RS_TC_271:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC271();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case NACN_RS_TC_273:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC273();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case ACN_RS_TC_274:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC274();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case ACN_RS_TC_275:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC275();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case NACN_RS_TC_276:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC276();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case NACN_RS_TC_277:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC277();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case ACN_RS_TC_278:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC278();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case NACN_RS_TC_279:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC279();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case NACN_RS_TC_280:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC280();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case ACN_CM_TC_281:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC281();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case NACN_CM_TC_282:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC282();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case NACN_CM_TC_283:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC283();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case ACN_CM_TC_284:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC284();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case NACN_RS_TC_285:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC285();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case NACN_RS_TC_286:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC286();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case NACN_CM_TC_287:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC287();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case NACN_CM_TC_288:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC288();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case NACN_RS_TC_289:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC289();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case NACN_RS_TC_290:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC290();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case NACN_RS_TC_291:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC291();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case NACN_RS_TC_292:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC292();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case NACN_RS_TC_293:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC293();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case NACN_RS_TC_294:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC294();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case NACN_RS_TC_295:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC295();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case NACN_RS_TC_296:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC296();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case NACN_RS_TC_297:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC297();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case ACN_RS_TC_298:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC298();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case NACN_RS_TC_300:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC300();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;

            case NACN_CM_TC_301:
                validCustomerDetails = ApplicationContext.get().getDbAction().getCustPremCodeTC301();
                testContext.setCustomerCode(validCustomerDetails.get("UZBENRO_CUST_CODE").toString());
                testContext.setPremisesCode(validCustomerDetails.get("UZBENRO_PREM_CODE").toString());
                break;


        }

    }

    public void preparePayloadForNegativeTestConditions(SearchAccountsRequest payload, SearchAccountsApiLabel testCondition) {
        setParametersToEmpty(payload);
        Map<String, Object> validCustomerBusinessDetails = null;
        payload.setTransactionType(GlobalEnums.TransactionType.TRANSFER.getValue());
        payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
        Map<String, Object> accountDetailsLastNameZipCode = ApplicationContext.get().getDbAction().getAccountDetails_LastNameZipCode();
        String lastName=accountDetailsLastNameZipCode.get("UZBENRO_DSM_LAST_NAME").toString();
        validZipCode = accountDetailsLastNameZipCode.get("UCRADDR_ZIP").toString();
        switch (testCondition) {
            case NO_SEARCH_PARAMETERS_PROVIDED_TC_1:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                break;

            case MISSING_REQUEST_ID_TC_2:
                payload.setRequestID("");
                break;

            case INVALID_REQUEST_ID_LENGTH_TC_3:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(33));
                break;

            case DUPLICATE_REQUEST_ID_TC_4:
                payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());
                break;

            case MISSING_LOGIN_ID_TC_5:
                payload.setLoginID("");
                break;

            case INVALID_LOGIN_ID_LENGTH_TC_6:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setLoginID(FakerDataGenerator.generateString(31));
                break;

            case INVALID_LOGIN_ID_FORMAT_TC_7:
                payload.setLoginID(FakerDataGenerator.generateAlphanumericWithSpecialChars(6));
                break;

            case INVALID_LOGIN_ID_TC_8:
                payload.setLoginID(FakerDataGenerator.generateString(6));
                payload.setCustomerLastName(FakerDataGenerator.generateString(5));
                payload.setPremisesZipCode(validZipCode);
                break;

            case MISSING_CUSTOMER_CODE_TC_9:
                payload.setPremisesCode(FakerDataGenerator.generateDigits(6));
                break;

            case MISSING_PREMISES_CODE_TC_10:
                payload.setCustomerCode(FakerDataGenerator.generateDigits(6));
                break;

            case INVALID_CUSTOMER_CODE_LENGTH_TC_11:
                payload.setCustomerCode(FakerDataGenerator.generateDigits(10));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(6));
                break;

            case  INVALID_PREMISES_CODE_LENGTH_TC_12:
                payload.setCustomerCode(FakerDataGenerator.generateDigits(6));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(8));
                break;

            case MISSING_TRANSACTION_TYPE_TC_13:
                payload.setTransactionType(null);
                break;

            case INVALID_TRANSACTION_TYPE_TC_14:
                payload.setTransactionType(FakerDataGenerator.generateUpperCaseString(4));
                break;

            case INVALID_TRANSACTION_TYPE_LENGTH_TC_15:
                payload.setTransactionType(FakerDataGenerator.generateUpperCaseString(5));
                break;

            case INVALID_BUSINESS_NAME_LENGTH_TC_16:
                payload.setCustomerBusinessName(FakerDataGenerator.generateString(61));
                break;

            case INVALID_CUSTOMER_LAST_NAME_LENGTH_TC_17:
                payload.setCustomerLastName(FakerDataGenerator.generateString(61));
                break;

            case MISSING_PREMISES_ZIP_CODE_TC_19:
                payload.setCustomerLastName(FakerDataGenerator.generateString(10));
                break;

            case MISSING_TRANSACTION_TYPE_TC_20:
                payload.setCustomerLastName(lastName);
                payload.setPremisesZipCode(validZipCode);
                payload.setTransactionType(null);
                break;

            case INVALID_FIRST_NAME_LENGTH_TC_21:
                payload.setCustomerFirstName(FakerDataGenerator.generateString(16));
                break;

            case UNENCRYPTED_SSN_TC_22:
                payload.setSocialSecurityNumber(socialSecurityNumber);
                break;

            case INVALID_SSN_LENGTH_TC_23:
                payload.setSocialSecurityNumber(encryptData(FakerDataGenerator.generateDigits(33)));
                break;

            case TAX_ID_NOT_ALLOWED_TC_24:
                payload.setCustomerBusinessName(FakerDataGenerator.generateString(10));
                payload.setFederalTaxID(encryptData(FakerDataGenerator.generateDigits(9)));
                break;

            case SSN_AND_TAX_ID_PROVIDED_TC_25:
                payload.setSocialSecurityNumber(encryptData(socialSecurityNumber));
                payload.setFederalTaxID(encryptData(FakerDataGenerator.generateDigits(9)));
                break;

            case PHONE_NOT_ALLOWED_TC_26:
                payload.setCustomerLastName(lastName);
                payload.setPremisesZipCode(validZipCode);
                payload.setPhoneNumber(FakerDataGenerator.generateDigits(10));
                break;

            case INVALID_AGLC_ACCOUNT_NO_LENGTH_TC_27:
                payload.setCustomerLastName(lastName);
                payload.setPremisesZipCode(validZipCode);
                payload.setAglcAccountNumber(FakerDataGenerator.generateDigits(21));
                break;

            case NON_NUMERIC_AGLC_ACCOUNT_NO_TC_28:
                payload.setCustomerLastName(lastName);
                payload.setPremisesZipCode(validZipCode);
                payload.setAglcAccountNumber(FakerDataGenerator.generateAlphanumericWithSpecialChars(10));
                break;

            case INVALID_STREET_NUMBER_LENGTH_TC_29:
                setAddressDetails(payload);
                payload.setPremisesStreetNumber(FakerDataGenerator.generateDigits(13));
                break;

            case INVALID_STREET_PRE_DIR_LENGTH_TC_30:
                setAddressDetails(payload);
                payload.setPremisesStreetPreDirection(FakerDataGenerator.generateString(3));
                break;


            case INVALID_STREET_NAME_LENGTH_TC_31:
                setAddressDetails(payload);
                payload.setPremisesStreetName(FakerDataGenerator.generateString(31));
                break;

            case MISSING_PREMISES_STREET_NAME_TC_32:
                setAddressDetails(payload);
                payload.setPremisesStreetName(null);
                break;

            case INVALID_STREET_SUFFIX_LENGTH_TC_33:
                setAddressDetails(payload);
                payload.setPremisesStreetSuffix(FakerDataGenerator.generateUpperCaseString(7));
                break;

            case INVALID_PREMISES_STREET_POST_DIRECTION_LENGTH_TC_34:
                setAddressDetails(payload);
                payload.setPremisesStreetPostDirection(FakerDataGenerator.generateUpperCaseString(3));
                break;

            case INVALID_UNIT_TYPE_LENGTH_TC_35:
                setAddressDetails(payload);
                payload.setPremisesUnitType(FakerDataGenerator.generateAlphanumericWithSpecialChars(7));
                break;

            case PREMISES_UNIT_TYPE_MISSING_TC_36:
                setAddressDetails(payload);
                payload.setPremisesUnitType(null);
                payload.setPremisesUnitNumber(FakerDataGenerator.generateDigits(3));
                break;

            case INVALID_PREMISES_UNIT_NUMBER_FORMAT_TC_37:
                setAddressDetails(payload);
                payload.setPremisesUnitType(preisesUnitType);
                payload.setPremisesUnitNumber("1"+FakerDataGenerator.generateDigits(7));
                break;

            case MISSING_UNIT_NUMBER_TC_38:
                setAddressDetails(payload);
                payload.setPremisesUnitType(preisesUnitType);
                payload.setPremisesUnitNumber(null);
                break;

            case INVALID_PREMISES_CITY_LENGTH_TC_39:
                setAddressDetails(payload);
                payload.setPremisesCity(FakerDataGenerator.generateString(21));
                break;

            case MISSING_PREMISES_CITY_TC_40:
                setAddressDetails(payload);
                payload.setPremisesCity(null);
                break;

            case INVALID_STATE_CODE_LENGTH_TC_41:
                setAddressDetails(payload);
                payload.setPremisesStateCode(FakerDataGenerator.generateUpperCaseString(4));
                break;

            case MISSING_STATE_CODE_TC_42:
                setAddressDetails(payload);
                payload.setPremisesStateCode(null);
                break;

            case INVALID_ZIP_CODE_LENGTH_TC_43:
                setAddressDetails(payload);
                payload.setPremisesZipCode(FakerDataGenerator.generateDigits(7));
                break;

            case INVALID_ZIP_CODE_LENGTH_LESS_THAN_5_TC_44:
                setAddressDetails(payload);
                payload.setPremisesZipCode(FakerDataGenerator.generateDigits(4));
                break;

            case MISSING_ZIP_CODE_TC_45:
                setAddressDetails(payload);
                payload.setPremisesZipCode(null);
                break;

            case INVALID_LAST_NAME_ZIP_COMBINATION_TC_18:
            case INVALID_RS_LAST_NAME_ZIP_COMBINATION_TC_47:
                payload.setPremisesZipCode(validZipCode);
                payload.setCustomerLastName(FakerDataGenerator.generateString(5));
                break;

            case INVALID_CUSTOMER_PREMISES_CODE_COMBINATION_TC_46:
                payload.setCustomerCode(FakerDataGenerator.generateDigits(6));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(6));
                break;

            case VALID_LAST_NAME_ZIP_COMBINATION_TC_48:
                payload.setPremisesZipCode(validZipCode);
                payload.setCustomerLastName(lastName);
                break;

            case NO_MATCHING_CUSTOMER_BUSINESS_NAME_TC_49:
                payload.setCustomerBusinessName(FakerDataGenerator.generateString(6));
                break;

            case VALID_CUSTOMER_BUSINESS_NAME_TC_50:
                validCustomerBusinessDetails = ApplicationContext.get().getDbAction().getCustomerBusinessNameCMActiveNoETC();
                payload.setCustomerBusinessName(validCustomerBusinessDetails.get("UCBCUST_LAST_NAME").toString());
                break;

            case  INACTIVE_UNAPPLIED_DEPOSIT_TC_51:
                validCustomerBusinessDetails = ApplicationContext.get().getDbAction().getCustPremCodeRSInactiveUnappliedDeposit();
                payload.setCustomerCode(validCustomerBusinessDetails.get("UCRSCMP_CUST_CODE").toString());
                payload.setPremisesCode(validCustomerBusinessDetails.get("UCRSCMP_PREM_CODE").toString());
                break;

            case  INACTIVE_PAST_DUE_TC_52:
                validCustomerBusinessDetails = ApplicationContext.get().getDbAction().getCustPremCodeRSInactivePastDue();
                payload.setCustomerCode(validCustomerBusinessDetails.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(validCustomerBusinessDetails.get("UCRACCT_PREM_CODE").toString());
                break;

            case RS_INACTIVE_BANKRUPCY_TC_53:
                validCustomerBusinessDetails = ApplicationContext.get().getDbAction().getCustPremCodeResidentialInactiveBankrupcy();
                payload.setCustomerCode(validCustomerBusinessDetails.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(validCustomerBusinessDetails.get("UCRACCT_PREM_CODE").toString());
                break;

            case RS_INACTIVE_BAD_DEBT_BALANCE_TC_54:
            case RS_INACTIVE_BAD_DEBT_BALANCE_TC_55:
                validCustomerBusinessDetails = ApplicationContext.get().getDbAction().getCustPremCodeInactiveAccWithBadDebt();
                payload.setCustomerCode(validCustomerBusinessDetails.get("UABOPEN_CUST_CODE").toString());
                payload.setPremisesCode(validCustomerBusinessDetails.get("UABOPEN_PREM_CODE").toString());
                break;

            case  NEW_UNAPPLIED_DEPOSIT_TC_57:
                validCustomerBusinessDetails = ApplicationContext.get().getDbAction().getCustPremCodeRSNewUnappliedDeposit();
                payload.setCustomerCode(validCustomerBusinessDetails.get("UCRSCMP_CUST_CODE").toString());
                payload.setPremisesCode(validCustomerBusinessDetails.get("UCRSCMP_PREM_CODE").toString());
                break;

            case RS_NEW_BANKRUPCY_TC_58:
                validCustomerBusinessDetails = ApplicationContext.get().getDbAction().getCustPremCodeResidentialNewBankrupcy();
                payload.setCustomerCode(validCustomerBusinessDetails.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(validCustomerBusinessDetails.get("UCRACCT_PREM_CODE").toString());
                break;

            case FINAL_NO_SONP_ACTIVE_PENDING_REWARDS_TC_59:
                validCustomerBusinessDetails = ApplicationContext.get().getDbAction().getCustPremCodeRSFinalPendingRewards();
                payload.setCustomerCode(validCustomerBusinessDetails.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(validCustomerBusinessDetails.get("UCRACCT_PREM_CODE").toString());
                break;

            case  FINAL_UNAPPLIED_DEPOSIT_TC_60:
                validCustomerBusinessDetails = ApplicationContext.get().getDbAction().getCustPremCodeRSFinalUnappliedDeposit();
                payload.setCustomerCode(validCustomerBusinessDetails.get("UCRSCMP_CUST_CODE").toString());
                payload.setPremisesCode(validCustomerBusinessDetails.get("UCRSCMP_PREM_CODE").toString());
                break;

            case  ACTIVE_PAST_DUE_TC_61:
                validCustomerBusinessDetails = ApplicationContext.get().getDbAction().getCustPremCodeRSActivePastDue();
                payload.setCustomerCode(validCustomerBusinessDetails.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(validCustomerBusinessDetails.get("UCRACCT_PREM_CODE").toString());
                break;

            case  ACTIVE_PAST_DUE_REWARDS_TC_63:
                validCustomerBusinessDetails = ApplicationContext.get().getDbAction().getCustPremCodeRSActivePastDueRewards();
                payload.setCustomerCode(validCustomerBusinessDetails.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(validCustomerBusinessDetails.get("UCRACCT_PREM_CODE").toString());
                break;

            case  ACTIVE_UNAPPLIED_DEPOSIT_TC_64:
                validCustomerBusinessDetails = ApplicationContext.get().getDbAction().getCustPremCodeRSActiveUnappliedDepositCSV();
                payload.setCustomerCode(validCustomerBusinessDetails.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(validCustomerBusinessDetails.get("UCRACCT_PREM_CODE").toString());
                break;

            case  ACTIVE_SONP_PAST_DUE_TC_68:
                validCustomerBusinessDetails = ApplicationContext.get().getDbAction().getCustPremCodeRSActiveDiscount();
                payload.setCustomerCode(validCustomerBusinessDetails.get("UCRSCMP_CUST_CODE").toString());
                payload.setPremisesCode(validCustomerBusinessDetails.get("UCRSCMP_PREM_CODE").toString());
                break;

            case  ACTIVE_GREENER_LIFE_SONP_TC_69:
                validCustomerBusinessDetails = ApplicationContext.get().getDbAction().getCustPremCodeRSActiveSONP();
                payload.setCustomerCode(validCustomerBusinessDetails.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(validCustomerBusinessDetails.get("UCRACCT_PREM_CODE").toString());
                break;

            case  SONP_UNAPPLIEDDEPOSIT_TC_70:
                validCustomerBusinessDetails = ApplicationContext.get().getDbAction().getCustPremCodeRSActiveSONPUnappliedDeposit();
                payload.setCustomerCode(validCustomerBusinessDetails.get("GTBTRNH_CUST_CODE").toString());
                payload.setPremisesCode(validCustomerBusinessDetails.get("GTBTRNH_PREM_CODE").toString());
                break;

            case DISCOUNTS_WITH_RESTRICTIONS_TC_65:
                validCustomerBusinessDetails = ApplicationContext.get().getDbAction().getCustPremCodeDiscountWithRestrictions();
                payload.setCustomerCode(validCustomerBusinessDetails.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(validCustomerBusinessDetails.get("UCRACCT_PREM_CODE").toString());
                break;

            case TRANSFERABLE_DISCOUNTS_TC_66:
                validCustomerBusinessDetails = ApplicationContext.get().getDbAction().getCustPremCodeTransferableDiscount();
                payload.setCustomerCode(validCustomerBusinessDetails.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(validCustomerBusinessDetails.get("UCRACCT_PREM_CODE").toString());
                break;

            case MULTIPLE_DISCOUNTS_TC_67:
                validCustomerBusinessDetails = ApplicationContext.get().getDbAction().getCustPremCodeMultipleDiscount();
                payload.setCustomerCode(validCustomerBusinessDetails.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(validCustomerBusinessDetails.get("UCRACCT_PREM_CODE").toString());
                break;

            case ACTIVE_SONP_WITH_REWARDS_TC_71:
                validCustomerBusinessDetails = ApplicationContext.get().getDbAction().getCustPremCodeRSActiveSONPRewards();
                payload.setCustomerCode(validCustomerBusinessDetails.get("GTBTRNH_CUST_CODE").toString());
                payload.setPremisesCode(validCustomerBusinessDetails.get("GTBTRNH_PREM_CODE").toString());
                break;

            case SONP_DISCOUNT_NO_RESTRICTION_TC_72:
                validCustomerBusinessDetails = ApplicationContext.get().getDbAction().getCustPremCodeRSActiveSONPDiscounts();
                payload.setCustomerCode(validCustomerBusinessDetails.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(validCustomerBusinessDetails.get("UCRACCT_PREM_CODE").toString());
                break;

            case SONP_DISCOUNT_WITH_RESTRICTIONS_TC_73:
                validCustomerBusinessDetails = ApplicationContext.get().getDbAction().getCustPremCodeRSActiveSONPDiscountsRestrictions();
                payload.setCustomerCode(validCustomerBusinessDetails.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(validCustomerBusinessDetails.get("UCRACCT_PREM_CODE").toString());
                break;

            case TRANSFERABLE_DISCOUNTS_SONP_TC_74:
                validCustomerBusinessDetails = ApplicationContext.get().getDbAction().getCustPremCodeRSActiveSONPDiscountsMultiple();
                payload.setCustomerCode(validCustomerBusinessDetails.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(validCustomerBusinessDetails.get("UCRACCT_PREM_CODE").toString());
                break;

            case MULTIPLE_DISCOUNTS_TC_SONP_TC_75:
                validCustomerBusinessDetails = ApplicationContext.get().getDbAction().getCustPremCodeRSActiveSONPDiscountsTransfersble();
                payload.setCustomerCode(validCustomerBusinessDetails.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(validCustomerBusinessDetails.get("UCRACCT_PREM_CODE").toString());
                break;

            case  ACTIVE_GREENER_LIFE_NO_SONP_TC_62:
                validCustomerBusinessDetails = ApplicationContext.get().getDbAction().getCustPremCodeRSActiveGreenerLifeNoSONP();
                payload.setCustomerCode(validCustomerBusinessDetails.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(validCustomerBusinessDetails.get("UCRACCT_PREM_CODE").toString());
                break;

            case ACN_RS_REMAINS_ON_TIER_1_TC_251:
            case ACN_RS_REMAINS_ON_TIER_1_NACN_TC_252:
            case ACN_RS_TC_253:
            case NACN_RS_TC_254:
            case ACN_RS_TC_255:
            case NACN_RS_TC_256:
            case NACN_SR_TC_258:
            case ACN_RS_TC_259:
            case NACN_RS_TC_260:
            case NACN_SR_TC_262:
            case ACN_RS_TC_263:
            case NACN_RS_TC_264:
            case ACN_RS_TC_265:
            case NACN_RS_TC_266:
            case NACN_RS_TC_267:
            case ACN_RS_TC_268:
            case ACN_RS_TC_269:
            case NACN_RS_TC_270:
            case ACN_RS_TC_271:
                payload.setPremisesCode(testContext.getPremisesCode());
                payload.setCustomerCode(testContext.getCustomerCode());
                break;

            default:
                payload.setRequestID(FakerDataGenerator.generateString(10));
        }
    }


    public void setExternalCasesParameters(SearchAccountsRequest payload, SearchAccountsApiLabel testCondition) {
        Map<String, Object> customerData;
        payload.setTransactionType(GlobalEnums.TransactionType.TRANSFER.getValue());
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
                 ST_SE_CURRENT_PRICE_PLAN_CEILING_BOOLEAN_ONLY_TC249:

                Map<String, Object> activeCustomerData = ApplicationContext.get().getDbAction().getActiveCustomerWithServiceTransferEnrollment();
                payload.setCustomerCode(activeCustomerData.get(UCRACCT_CUST_CODE).toString());
                payload.setPremisesCode(activeCustomerData.get(UCRACCT_PREM_CODE).toString());
                payload.setTransactionType(GlobalEnums.TransactionType.TRANSFER.getValue());
                break;
            case ST_SE_CURRENT_PRICE_PLAN_APPLICABLE_FIXED_OR_CEILING_ONLY_TC250:
                customerData = ApplicationContext.get().getDbAction()
                        .getTenantCustomerInformationByStatusAndPlanType(GlobalEnums.AccountStatus.ACTIVE.getValue(), GlobalEnums.PlanCode.MVS.getValue());
                setCustomerInfo(payload, customerData);
                break;
            case
                ST_GE_ST_CURRENT_TRUE_PLAN_NOT_FIXED_OR_CEILING_NEG_TC215A,
                ST_GE_ST_CURRENT_TRUE_NOT_TRAN_NEG_TC217:
                customerData = ApplicationContext.get().getDbAction()
                        .getTenantCustomerInformationByStatusAndPlanType(GlobalEnums.AccountStatus.ACTIVE.getValue(), GlobalEnums.PlanCode.RGB.getValue());
                setCustomerInfo(payload, customerData);
                break;

            case  ST_GE_ENROLLMENT_STATE_INVALID_FOR_TRAN_NEG_TC198,
                  ST_GE_ST_OFFER_REMAINDER_TRUE_NOT_TRAN_NEG_TC220:
                customerData = ApplicationContext.get().getDbAction()
                        .getTenantCustomerInformationByStatusAndPlanType(GlobalEnums.AccountStatus.ACTIVE.getValue(), GlobalEnums.PlanCode.PGB.getValue());
                setCustomerInfo(payload, customerData);
                break;
        }
    }

    private void setCustomerInfo(SearchAccountsRequest payload, Map<String, Object> row) {
        payload.setPremisesCode(row.get(UCRACCT_PREM_CODE).toString());
        payload.setCustomerCode(row.get(UCRACCT_CUST_CODE).toString());
    }
}
