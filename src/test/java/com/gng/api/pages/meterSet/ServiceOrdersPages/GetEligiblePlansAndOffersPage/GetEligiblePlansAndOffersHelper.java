package com.gng.api.pages.meterSet.ServiceOrdersPages.GetEligiblePlansAndOffersPage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.request.GetEligiblePlansAndOffersRequest;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.response.GetEligiblePlansAndOffersResponse;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.response.Plans;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.meterSet.ServiceOrdersSteps.GetEligiblePlansAndOffers.GetEligiblePlansAndOffersApiLabel;
import com.gng.api.util.ExcelReader;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.gng.api.constants.GlobalEnums.CreditCheckOption.*;
import static com.gng.api.constants.GlobalEnums.CustomerType.COMMERCIAL;
import static com.gng.api.constants.GlobalEnums.TransactionType.METER_SET;
import static com.gng.api.constants.GlobalEnums.TransactionType.TURN_ON;
import static com.gng.api.constants.TestConstant.*;
import static com.gng.api.steps.AesEncryption.AesEncryptionSteps.encryptData;

@Slf4j
public class GetEligiblePlansAndOffersHelper {

    private final TestContext testContext;

    public GetEligiblePlansAndOffersHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    GetEligiblePlansAndOffersRequest preparePayload(GetEligiblePlansAndOffersApiLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        String jsonFileName = apiLabel.equals(GetEligiblePlansAndOffersApiLabel.get_eligible_plans_and_offers)
                ? GetEligiblePlansAndOffersApiLabel.get_eligible_plans_and_offers.toString()
                : GetEligiblePlansAndOffersApiLabel.get_eligible_plans_and_offers_mandatory.toString();
        return BasePage.deserializeJsonToPojo(jsonFileName, GetEligiblePlansAndOffersRequest.class);
    }

    public void setSupportingDefaultParameters(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel testCondition) {
        payload.setRequestID(FakerDataGenerator.generateString(12));
        payload.setTransactionType(GlobalEnums.TransactionType.METER_SET.getValue()); // "SETM"
        payload.setReferralCode("");
        payload.setCreditCheckOption(GlobalEnums.CreditCheckOption.YES.getValue());
        payload.setEnrollmentState(null);
        payload.setInitialCreditCheckCustomerCode(null);
        payload.setConfirmCreditCheck(null);
        payload.setSspParticipantCode(null);
        payload.setCommercialCreditCheckBusinessBIN(null);

        // SETM requires null/empty AGLC
        payload.setAglcAccountNumber(null);

       // payload.setAglcServiceLocationID(FakerDataGenerator.getRandomNumericString(8));
        payload.setServiceTransferCurrentPricePlan(null);
        payload.setServiceTransferOfferRemainder(null);
        payload.setSeasonalSavingsProgramIndicator(false);
        payload.setMarketingPromotionCode("");
    }

    public void setParametersToSeedDataBasedOnType(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel testCondition) {

        setSupportingDefaultParameters(payload, testCondition);

        switch (testCondition) {

            case MS_RS_NO_RECORD_FOUND_CONFIRM_PRP_ONLY_TC_012 -> {
                loadCustomerData(payload, testCondition);
                payload.setConfirmCreditCheck(Boolean.TRUE);
            }

            case MS_RS_NO_RECORD_FOUND_TC_013 -> {
                //loadCustomerData(payload, testCondition);

                setSeedlingParamsFromSearchAccountsResponse(payload, testCondition);
                payload.setConfirmCreditCheck(Boolean.FALSE);
                payload.setCreditCheckOption(YES.getValue());

                payload.setAglcServiceLocationID(FakerDataGenerator.getRandomNumericString(8));
            }
            case  MS_RS_VARIANT_TC_014,
                 MS_RS_CUSTOMER_NO_AUTH_TC_017, MS_GE_TIER_1_NE_TC_018,
                 MS_GE_TIER_2_NE_TC_019, MS_GE_TIER_9_NE_TC_020, MS_GE_RS_COMM_VS_150_TC_021,
                 MS_GE_RS_ACN_LAND_BYPASS_CREDIT_TC_022,
                 MS_RS_CRDS_ENROLLMENT_CREDIT_CHECK_TC_025, MS_RS_CREDIT_FREEZE_TC_015 -> {
                    //remove 25?
                setSeedlingParamsFromSearchAccountsResponse(payload, testCondition);
                payload.setConfirmCreditCheck(Boolean.FALSE);
                payload.setCreditCheckOption(YES.getValue());
            }
            case MS_RS_DENIAL_DUE_TC_016 -> {
                loadCustomerData(payload, testCondition);
                payload.setConfirmCreditCheck(Boolean.FALSE);
                payload.setCreditCheckOption(YES.getValue());
                payload.setTransactionType(TURN_ON.getValue());
            }

            case MS_CM_NO_MATCH_INITIAL_TC_026 -> {
                setTheFieldToEmptyForCommercialScenarios(payload);
                loadCommercialData(payload, testCondition);
                payload.setCustomerType(COMMERCIAL.getValue());
                payload.setCreditCheckOption(YES.getValue());
                payload.setCustomerType(GlobalEnums.CustomerType.COMMERCIAL.getValue());
                payload.setConfirmCreditCheck(false);
                payload.setCommercialCreditCheckBusinessBIN(null);
            }

            case MS_CM_NO_MATCH_CONFIRM_RETURNS_PLANS_TC_027 -> {
                payload.setCustomerType(GlobalEnums.CustomerType.COMMERCIAL.getValue());
                payload.setConfirmCreditCheck(true);
                payload.setCommercialCreditCheckBusinessBIN(null);
            }

            case MS_CM_NO_MATCH_WITH_BIN_CONFIRM_RETURNS_PLANS_TC_028 -> {
                setTheFieldToEmptyForCommercialScenarios(payload);
                loadCommercialData(payload, testCondition);
                payload.setCustomerType(COMMERCIAL.getValue());
                payload.setCreditCheckOption(YES.getValue());
                payload.setConfirmCreditCheck(Boolean.FALSE);
                payload.setCommercialCreditCheckBusinessBIN(null);
                payload.setAglcServiceLocationID(FakerDataGenerator.getRandomNumericString(9));
            }

            case  MS_CM_CREDIT_MULTIPLE_CONFIRM_FALSE_NO_CGB_TC_031 -> {
                loadCustomerData(payload, testCondition);
                payload.setCustomerType(GlobalEnums.CustomerType.COMMERCIAL.getValue());
                payload.setConfirmCreditCheck(true);
                payload.setCommercialCreditCheckBusinessBIN(null);
            }

            case
                    MS_CM_NEW_EN_CREDIT_CHECK_NO_NO_PERMISSIONS_TC_032,
                    MS_CM_CRDS_EN_CREDIT_CHECK_YES_BUSINESS_NAME_TC_034 -> {
                payload.setCustomerType(GlobalEnums.CustomerType.COMMERCIAL.getValue());
            }
            case get_eligible_plans_and_offers, get_eligible_plans_and_offers_mandatory -> {
            }
        }
    }

    public void setParametersBasedOnType(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel testCondition) {

        setSupportingDefaultParameters(payload, testCondition);

        switch (testCondition) {

            case MS_GE_MISSING_TRANSACTION_TYPE_TC_006 ->
                    payload.setTransactionType(null);

            case MS_GE_TRANSACTION_TYPE_MAX_LENGTH_TC_007 ->
                    payload.setTransactionType(FakerDataGenerator.generateString(5)); // >4

            case MS_GE_TRANSACTION_TYPE_INVALID_TC_008 ->
                    payload.setTransactionType(GlobalEnums.InvalidValues.INVALID_TRANSACTION_TYPE.getValue());

            case MS_GE_AGLC_ACCOUNT_PROVIDED_TC_009 ->
                    payload.setAglcAccountNumber(FakerDataGenerator.getRandomNumericString(9));

            case MS_RS_FRAUD_ALERT_INVALID_SSN_TC_010 -> {
                loadCustomerData(payload, testCondition);
                payload.setCreditCheckOption(YES.getValue());
                payload.setConfirmCreditCheck(Boolean.FALSE);
            }

            case MS_RS_NO_MATCH_NO_CONFIRM_TC_011, MS_RS_DENIAL_DUE_TC_016, MS_RS_CREDIT_FREEZE_TC_015 -> {
                loadCustomerData(payload, testCondition);
                payload.setConfirmCreditCheck(Boolean.FALSE);
            }
            case MS_RS_VARIANT_TC_014 -> {
                loadCustomerData(payload, testCondition);
                payload.setConfirmCreditCheck(Boolean.TRUE);
                payload.setCreditCheckOption(YES.getValue());
                payload.setCustomerCode(null);
            }

            case MS_RS_NO_RECORD_FOUND_CONFIRM_PRP_ONLY_TC_012 -> {
                loadCustomerData(payload, testCondition);
                payload.setCustomerType(GlobalEnums.CustomerType.RESIDENTIAL.getValue());
                payload.setConfirmCreditCheck(Boolean.TRUE);
            }

            case MS_RS_NO_RECORD_FOUND_TC_013 -> {
                loadCustomerData(payload, testCondition);
                payload.setConfirmCreditCheck(Boolean.TRUE);
                payload.setAuthorizedBy("");
            }

            case   MS_RS_CUSTOMER_NO_AUTH_TC_017 -> {
                loadCustomerData(payload, testCondition);
                payload.setConfirmCreditCheck(Boolean.FALSE);
                payload.setCreditCheckOption(GlobalEnums.CreditCheckOption.NO.getValue());
            }
            case MS_GE_TIER_1_NE_TC_018, MS_GE_TIER_2_NE_TC_019, MS_GE_TIER_9_NE_TC_020 -> {
                loadCustomerData(payload, testCondition);
                payload.setConfirmCreditCheck(Boolean.FALSE);
                payload.setCreditCheckOption(YES.getValue());
                payload.setAuthorizedBy("");

            }
            case MS_GE_RS_COMM_VS_150_TC_021 -> {
                loadCustomerData(payload, testCondition);
                payload.setConfirmCreditCheck(Boolean.FALSE);
                payload.setCreditCheckOption(COMMERCIAL_CREDIT_CHECK_REQUIRED.getValue());
                payload.setAglcAccountNumber("");
            }

            case MS_GE_RS_ACN_LAND_BYPASS_CREDIT_TC_022 -> {
                loadCustomerData(payload, testCondition);
                setRequestParamsForTransferFromSearchAccountsResponse(payload, testCondition);
                payload.setConfirmCreditCheck(Boolean.FALSE);
                payload.setCreditCheckOption(ACN_LANDLORD.getValue());
                payload.setTenantLandlord(GlobalEnums.TenantOrLandlord.LANDLORD.getValue());
                payload.setTransactionID(null);
                payload.setCustomerCode(null);
                payload.setPremisesCode(null);
                payload.setAglcAccountNumber("");
                payload.setAuthorizedBy("");
            }

            case MS_GE_RS_INCL_TIER_5_TC_023, MS_RS_CRDS_ENROLLMENT_CREDIT_CHECK_TC_025-> {
                loadCustomerData(payload, testCondition);
                setRequestParamsForTransferFromSearchAccountsResponse(payload, testCondition);
                payload.setEnrollmentState(GlobalEnums.EnrollMentState.INCL.getValue());
                payload.setConfirmCreditCheck(Boolean.FALSE);
                payload.setCreditCheckOption(YES.getValue());
            }

            case MS_RS_MULTIPLE_PREM_TC_024 -> {
                loadCustomerData(payload, testCondition);
                payload.setCreditCheckOption(MULTIPLE_PREMISES_OWNER.getValue());
                payload.setInitialCreditCheckCustomerCode(testContext.getSearchAccountsResponse()
                        .getData().getAccounts().getFirst().getCustomerCode());
            }

            case MS_CM_NO_MATCH_INITIAL_TC_026 -> {
                setTheFieldToEmptyForCommercialScenarios(payload);
                loadCustomerData(payload, testCondition);
                payload.setCustomerType(COMMERCIAL.getValue());
                payload.setCreditCheckOption(YES.getValue());
                payload.setConfirmCreditCheck(Boolean.FALSE);
                payload.setCommercialCreditCheckBusinessBIN(null);
            }

            case MS_CM_NO_MATCH_CONFIRM_RETURNS_PLANS_TC_027 -> {
                setTheFieldToEmptyForCommercialScenarios(payload);
                loadCommercialData(payload, testCondition);
                payload.setCustomerType(COMMERCIAL.getValue());
                payload.setConfirmCreditCheck(Boolean.TRUE);
                payload.setCommercialCreditCheckBusinessBIN("");
            }
            case MS_CM_NO_MATCH_WITH_BIN_CONFIRM_RETURNS_PLANS_TC_028 -> {
                setTheFieldToEmptyForCommercialScenarios(payload);
                loadCommercialDataFromResponse(payload, testCondition);

                //load aglc and countycode
                loadCustomerData(payload, testCondition);
                payload.setCustomerType(COMMERCIAL.getValue());
                payload.setConfirmCreditCheck(Boolean.TRUE);
            }

            case MS_CM_EXCELLENT_CREDIT_CGB_NOT_OFFERED_TC_029 -> {
                setTheFieldToEmptyForCommercialScenarios(payload);
                loadCommercialData(payload, testCondition);
                payload.setCustomerType(COMMERCIAL.getValue());
                payload.setConfirmCreditCheck(Boolean.FALSE);
            }

            case MS_CM_CREDIT_SKIP_COM_BY_PASS_TC_030 -> {
                setTheFieldToEmptyForCommercialScenarios(payload);
                loadCommercialData(payload, testCondition);
                payload.setCustomerType(COMMERCIAL.getValue());
                payload.setConfirmCreditCheck(Boolean.FALSE);
                payload.setCreditCheckOption(CREDIT_CHECK_NOT_REQUIRED.getValue());
            }

            case MS_CM_CREDIT_MULTIPLE_CONFIRM_FALSE_NO_CGB_TC_031 -> {
                //setTheFieldToEmptyForCommercialScenarios(payload);
                loadCommercialData(payload, testCondition);
                payload.setCustomerType(COMMERCIAL.getValue());
                payload.setConfirmCreditCheck(Boolean.FALSE);
                payload.setCreditCheckOption(MULTIPLE_PREMISES_OWNER.getValue());
            }

            case MS_CM_NEW_EN_CREDIT_CHECK_NO_NO_PERMISSIONS_TC_032 -> {
                setTheFieldToEmptyForCommercialScenarios(payload);
                loadCommercialData(payload, testCondition);
                payload.setCustomerType(COMMERCIAL.getValue());
                payload.setConfirmCreditCheck(Boolean.FALSE);
                payload.setCreditCheckOption(NO.getValue());
            }

            case MS_CM_CRDS_EN_CREDIT_CHECK_YES_COMM_DEP_PROSP_TC_033,
                 MS_CM_CRDS_EN_CREDIT_CHECK_YES_BUSINESS_NAME_TC_034-> {
                loadCustomerData(payload, testCondition);
                setRequestParamsForTransferFromSearchAccountsResponse(payload, testCondition);
                payload.setEnrollmentState(GlobalEnums.EnrollMentState.INCL.getValue());
                payload.setConfirmCreditCheck(Boolean.FALSE);
                payload.setCreditCheckOption(YES.getValue());
                payload.setCustomerType(COMMERCIAL.getValue());
            }

        }
    }
    public void setTheFieldToEmptyForCommercialScenarios(GetEligiblePlansAndOffersRequest payload){
        payload.setCustomerFirstName("");
        payload.setCustomerMiddleName("");
        payload.setCustomerLastName("");
        payload.setSocialSecurityNumber("");
        payload.setCustomerBusinessName("");
        payload.setCustomerFirstName("");
        payload.setAglcAccountNumber("");
        payload.setPremisesStreetNumber("");
        payload.setPremisesStreetName("");
        payload.setPremisesStreetSuffix("");
        payload.setPremisesStreetPostDirection("");
        payload.setPremisesUnitType("");
        payload.setPremisesUnitNumber("");
        payload.setPremisesCity("");
        payload.setPremisesStateCode("");
        payload.setPremisesZipCode("");
        payload.setPremisesCountyCode("");
        payload.setCommercialCreditCheckBusinessBIN("");
    }

    public void loadCommercialDataFromResponse(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel testCondition){

        switch (testCondition){
            case MS_CM_NO_MATCH_WITH_BIN_CONFIRM_RETURNS_PLANS_TC_028 ->{
                var data = testContext.getGetEligiblePlansAndOffersResponse().getData().getSimilarBusinesses().getFirst();
                payload.setCustomerBusinessName(data.getBusinessName());
                payload.setPremisesStreetName(data.getBusinessStreet());
                payload.setPremisesCity(data.getBusinessCity());
                payload.setPremisesStateCode(data.getBusinessState());
                payload.setPremisesZipCode(data.getBusinessZipCode());
                payload.setCommercialCreditCheckBusinessBIN(data.getBusinessBIN());
            }
        }
    }

    public void loadCommercialData(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel testCondition){
        Map<String, String> customerData = loadRowFromExcelToCustomerData(CUSTOMER_DATA, CUSTOMER_SHEET_NAME, testCondition);
        getCommercialCustomerAndPremiseDetails(payload, customerData, testCondition);
    }

    private void getCommercialCustomerAndPremiseDetails(GetEligiblePlansAndOffersRequest payload, Map<String, String> data, GetEligiblePlansAndOffersApiLabel testCondition) {
        String federalTaxId = data.get("federalTaxId");
        if (federalTaxId != null && !federalTaxId.trim().isEmpty()) {
            payload.setFederalTaxID(encryptData(data.get("federalTaxId")));
        }
        switch (testCondition){
//            case MS_CM_NO_MATCH_INITIAL_TC_026, MS_CM_NO_MATCH_CONFIRM_RETURNS_PLANS_TC_027,
//                 MS_CM_NO_MATCH_WITH_BIN_CONFIRM_RETURNS_PLANS_TC_028, MS_CM_EXCELLENT_CREDIT_CGB_NOT_OFFERED_TC_029,
//                 MS_CM_CREDIT_SKIP_COM_BY_PASS_TC_030, MS_CM_NEW_EN_CREDIT_CHECK_NO_NO_PERMISSIONS_TC_032,
//                 MS_CM_CRDS_EN_CREDIT_CHECK_YES_COMM_DEP_PROSP_TC_033, MS_CM_CRDS_EN_CREDIT_CHECK_YES_BUSINESS_NAME_TC_034,
//                 MS_SE_CM_R_ENROLLMENT_STATUS_DP_TC_046, MS_SE_CM_R_ENROLLMENT_STATUS_SI_TC_049, MS_SE_RS_NA_ENROLLMENT_STATUS_RP_PRP_TC_050A -> {
//                payload.setCustomerBusinessName(data.get("customerLastName"));
//                payload.setAglcAccountNumber(data.get("aclcAccountNumber"));
//                payload.setPremisesStreetNumber(data.get("premisesStreetNumber"));
//                payload.setPremisesStreetName(data.get("premisesStreetName"));
//                payload.setPremisesStreetSuffix(data.get("premisesStreetSuffix"));
//                payload.setPremisesStreetPostDirection(data.get("premisesStreetPostDirection"));
//                payload.setPremisesUnitType(data.get("premisesUnitType"));
//                payload.setPremisesUnitNumber(data.get("premisesUnitNumber"));
//                payload.setPremisesCity(data.get("premisesCity"));
//                payload.setPremisesStateCode(data.get("premisesStateCode"));
//                payload.setPremisesZipCode(data.get("premisesZipCode"));
//                payload.setPremisesCountyCode(data.get("premisesCountyCode"));
//                payload.setCommercialCreditCheckBusinessBIN(data.get("Bin"));
//                payload.setAglcServiceLocationID(data.get("aglcServiceLocationID"));
//            }
            case MS_CM_CREDIT_MULTIPLE_CONFIRM_FALSE_NO_CGB_TC_031 -> {
                payload.setCustomerLastName("");
                payload.setCustomerFirstName("");
                payload.setSocialSecurityNumber("");
                payload.setCustomerType(data.get("customerType"));
                payload.setCustomerBusinessName(data.get("customerLastName"));
                payload.setCommercialCreditCheckBusinessBIN(null);
                payload.setAglcServiceLocationID(FakerDataGenerator.getRandomNumericString(8));
                payload.setInitialCreditCheckCustomerCode(testContext.getGetEligiblePlansAndOffersResponse().getData().getCustomerCode());
            }
            default -> {
                payload.setCustomerBusinessName(data.get("customerLastName"));
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
                payload.setPremisesCountyCode(data.get("premisesCountyCode"));
                payload.setCommercialCreditCheckBusinessBIN(data.get("Bin"));
                payload.setAglcServiceLocationID(data.get("aglcServiceLocationID"));
            }
        }
    }

    public void loadCustomerData(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel testCondition){
        Map<String, String> customerData = loadRowFromExcelToCustomerData(CUSTOMER_DATA, CUSTOMER_SHEET_NAME, testCondition);
        getCustomerAndPremiseDetails(payload, customerData, testCondition);
    }
    public void getCustomerAndPremiseDetails(GetEligiblePlansAndOffersRequest payload, Map<String, String> data, GetEligiblePlansAndOffersApiLabel testCondition ){
        switch (testCondition) {
            case MS_RS_NO_MATCH_NO_CONFIRM_TC_011, MS_RS_NO_RECORD_FOUND_CONFIRM_PRP_ONLY_TC_012-> {
                payload.setCustomerLastName(data.get("customerLastName"));
                payload.setGenerationCode(data.get("generationCode"));
                payload.setCustomerMiddleName(data.get("customerMiddleName"));
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
            }
            case MS_RS_NO_RECORD_FOUND_TC_013-> {
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
            }
            case MS_CM_NO_MATCH_INITIAL_TC_026, MS_CM_NO_MATCH_CONFIRM_RETURNS_PLANS_TC_027
                 -> {
                payload.setCustomerBusinessName(data.get("customerLastName"));
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
                payload.setPremisesCountyCode(data.get("premisesCountyCode"));
                payload.setCommercialCreditCheckBusinessBIN(data.get("Bin"));
                payload.setAglcServiceLocationID(data.get("aglcServiceLocationID"));

            }
            case MS_CM_NO_MATCH_WITH_BIN_CONFIRM_RETURNS_PLANS_TC_028 -> {
                payload.setAglcServiceLocationID(data.get("aglcServiceLocationID"));
                payload.setPremisesCountyCode(data.get("premisesCountyCode"));
            }
            case  MS_CM_CREDIT_MULTIPLE_CONFIRM_FALSE_NO_CGB_TC_031, MS_SE_CM_R_ENROLLMENT_STATUS_SI_TC_049 -> {
                payload.setCustomerType(data.get("customerType"));
                payload.setSocialSecurityNumber("");
                payload.setCustomerBusinessName(data.get("customerLastName"));
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
                payload.setPremisesCountyCode(data.get("premisesCountyCode"));
                payload.setCommercialCreditCheckBusinessBIN(data.get("Bin"));
            }
            default -> {
                payload.setCustomerLastName(data.get("customerLastName"));
                payload.setCustomerFirstName(data.get("customerFirstName"));
                payload.setPremisesStreetNumber(data.get("premisesStreetNumber"));
                payload.setPremisesStreetName(data.get("premisesStreetName"));
                payload.setPremisesStreetSuffix(data.get("premisesStreetSuffix"));
                payload.setPremisesStreetPostDirection(data.get("premisesStreetPostDirection"));
                payload.setPremisesUnitType(data.get("premisesUnitType"));
                payload.setPremisesUnitNumber(data.get("premisesUnitNumber"));
                payload.setPremisesCity(data.get("premisesCity"));
                payload.setPremisesStateCode(data.get("premisesStateCode"));
                payload.setPremisesZipCode(data.get("premisesZipCode"));
                payload.setAcnStatusIndicator(data.get("acnStatusIndicator"));
                payload.setCustomerCode(null);
                payload.setPremisesCode(null);
                payload.setAglcAccountNumber("");
                payload.setAglcServiceLocationID(data.get("aglcServiceLocationID"));
                payload.setPremisesCountyCode(data.get("premisesCountyCode"));
                payload.setCustomerMiddleName(data.get("customerMiddleName"));
            }
        }

        String ssn = data.get("SSN");
        if (ssn != null && !ssn.trim().isEmpty()) {
            payload.setSocialSecurityNumber(encryptData(data.get("SSN")));
        }
        String federalTaxId = data.get("federalTaxId");
        if (federalTaxId != null && !federalTaxId.trim().isEmpty()) {
            payload.setFederalTaxID(encryptData(data.get("federalTaxId")));
        }
    }

    public static <E extends Enum<E>> Map<String, String> loadRowFromExcelToCustomerData(String excelPath, String sheetName, E testLabel) {

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

    public void setSeedDataCustomerInformation(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel testCondition) {
        payload.setRequestID(FakerDataGenerator.getRandomString(12));
        payload.setTransactionType(METER_SET.getValue());
        Map<String, Object> customerData;
        switch (testCondition) {
            case MS_GE_RS_COMM_VS_150_TC_021 -> {
                loadCustomerData(payload, testCondition);
                payload.setConfirmCreditCheck(Boolean.TRUE);
            }

            case MS_GE_RS_ACN_LAND_BYPASS_CREDIT_TC_022 -> {
                loadCustomerData(payload, testCondition);
                payload.setConfirmCreditCheck(Boolean.TRUE);
                payload.setCreditCheckOption(YES.getValue());
                payload.setSeasonalSavingsProgramIndicator(false);
                payload.setMarketingPromotionCode(null);
                payload.setAuthorizedBy(null);
                payload.setAglcAccountNumber("");
            }
            case
                 MS_GE_RS_INCL_TIER_5_TC_023 -> {
                loadCustomerData(payload, testCondition);
                payload.setConfirmCreditCheck(Boolean.FALSE);
                payload.setCreditCheckOption(YES.getValue());
                payload.setTransactionType(METER_SET.getValue());
                payload.setSeasonalSavingsProgramIndicator(false);
                payload.setCustomerMiddleName(FakerDataGenerator.generateString(1));
                //needed below?
                payload.setMarketingPromotionCode(null);
                payload.setAuthorizedBy(null);
                payload.setAglcAccountNumber("");
            }
            case MS_RS_MULTIPLE_PREM_TC_024 -> {
                loadCustomerData(payload, GetEligiblePlansAndOffersApiLabel.MS_GE_RS_INCL_TIER_5_TC_023);
                payload.setConfirmCreditCheck(Boolean.FALSE);
                payload.setCreditCheckOption(YES.getValue());
                payload.setTransactionType(METER_SET.getValue());
            }
            case MS_RS_CRDS_ENROLLMENT_CREDIT_CHECK_TC_025, MS_SE_RS_R_ENROLLMENT_STATUS_PC_REQUOTE_TC_047,
                 MS_SE_RS_PRP_ENROLLMENT_STATUS_DB_TC_048,
                 MS_SE_RS_R_ENROLLMENT_STATUS_DR_TC_050, MS_SE_RS_NA_ENROLLMENT_STATUS_RP_PRP_TC_050A,
                 MS_SE_RS_PRP_ENROLLMENT_STATUS_PR_TC_051 -> {
                loadCustomerData(payload, testCondition);
                payload.setConfirmCreditCheck(Boolean.FALSE);
                payload.setCreditCheckOption(YES.getValue());
                payload.setTransactionType(METER_SET.getValue());
            }
            case MS_CM_CRDS_EN_CREDIT_CHECK_YES_COMM_DEP_PROSP_TC_033,
                 MS_CM_CRDS_EN_CREDIT_CHECK_YES_BUSINESS_NAME_TC_034,
                 MS_SE_CM_R_ENROLLMENT_STATUS_DP_TC_046, MS_SE_CM_R_ENROLLMENT_STATUS_SI_TC_049 -> {
                setTheFieldToEmptyForCommercialScenarios(payload);
                loadCommercialData(payload, testCondition);
                payload.setCustomerType(COMMERCIAL.getValue());
                payload.setConfirmCreditCheck(Boolean.FALSE);
                payload.setCreditCheckOption(YES.getValue());
            }
            case MS_SE_CM_R_ENROLLMENT_STATUS_RD_PROMO_TC_053-> {
                setTheFieldToEmptyForCommercialScenarios(payload);
                loadCommercialData(payload, testCondition);
                payload.setCustomerType(COMMERCIAL.getValue());
                payload.setConfirmCreditCheck(Boolean.FALSE);
                payload.setCreditCheckOption(YES.getValue());
                payload.setMarketingPromotionCode(GlobalEnums.MarketingPromotionCodes.PROMOTION_CODE_DEALS.getValue());
                payload.setCommercialCreditCheckBusinessBIN(null);
                payload.setAuthorizedBy("");
            }
            case MS_SE_RS_PRP_ENROLLMENT_STATUS_CP_TC_054 -> {
                loadCustomerData(payload, testCondition);
                payload.setConfirmCreditCheck(Boolean.FALSE);
                payload.setCreditCheckOption(YES.getValue());
                payload.setTransactionType(METER_SET.getValue());
                payload.setMarketingPromotionCode(GlobalEnums.MarketingPromotionCodes.PROMOTION_CODE_DEALS.getValue());
            }
        }
    }

    public void setSeedlingParamsFromSearchAccountsResponse(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel testCondition) {
        payload.setPremisesStreetName(testContext.getSearchAccountsResponse().getData().getAccounts().getFirst().getPremisesStreetName());
        payload.setPremisesStreetSuffix(testContext.getSearchAccountsResponse().getData().getAccounts().getFirst().getPremisesStreetSuffix());
        payload.setPremisesStreetNumber(testContext.getSearchAccountsResponse().getData().getAccounts().getFirst().getPremisesStreetNumber());
        payload.setPremisesCity(testContext.getSearchAccountsResponse().getData().getAccounts().getFirst().getPremisesCity());
        payload.setPremisesZipCode(testContext.getSearchAccountsResponse().getData().getAccounts().getFirst().getPremisesZipCode());
        payload.setPremisesStateCode(testContext.getSearchAccountsResponse().getData().getAccounts().getFirst().getPremisesStateCode());

        String countyCode = testContext.getSearchAccountsResponse().getData().getAccounts().getFirst().getPremisesCountyCode();
        String first4 = countyCode.substring(0, 4);
        payload.setPremisesCountyCode(first4);

        payload.setCustomerCode(null);
        payload.setPremisesCode(null);

    }

    public void setRequestParamsForTransferFromSearchAccountsResponse(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel testCondition) {
        var searchResponseAcct = testContext.getSearchAccountsResponse().getData().getAccounts().getFirst();

        payload.setPremisesStreetName(testContext.getSearchAccountsResponse().getData().getAccounts().getFirst().getPremisesStreetName());
        payload.setPremisesStreetSuffix(testContext.getSearchAccountsResponse().getData().getAccounts().getFirst().getPremisesStreetSuffix());
        payload.setPremisesStreetNumber(testContext.getSearchAccountsResponse().getData().getAccounts().getFirst().getPremisesStreetNumber());
        payload.setPremisesCity(testContext.getSearchAccountsResponse().getData().getAccounts().getFirst().getPremisesCity());
        payload.setPremisesZipCode(testContext.getSearchAccountsResponse().getData().getAccounts().getFirst().getPremisesZipCode());
        payload.setPremisesStateCode(testContext.getSearchAccountsResponse().getData().getAccounts().getFirst().getPremisesStateCode());

        String countyCode = testContext.getSearchAccountsResponse().getData().getAccounts().getFirst().getPremisesCountyCode();
        String first4 = countyCode.substring(0, 4);
        payload.setPremisesCountyCode(first4);

        payload.setCustomerCode(searchResponseAcct.getCustomerCode());
        payload.setPremisesCode(searchResponseAcct.getPremisesCode());

        switch (testCondition) {
            case MS_GE_RS_INCL_TIER_5_TC_023, MS_RS_CRDS_ENROLLMENT_CREDIT_CHECK_TC_025 ->
                payload.setTransactionID(testContext.getGetEligiblePlansAndOffersResponse().getData().getTransactionID());

            case MS_CM_CRDS_EN_CREDIT_CHECK_YES_COMM_DEP_PROSP_TC_033, MS_CM_CRDS_EN_CREDIT_CHECK_YES_BUSINESS_NAME_TC_034 -> {
                payload.setTransactionID(testContext.getGetEligiblePlansAndOffersResponse().getData().getTransactionID());
                payload.setEnrollmentState(GlobalEnums.EnrollMentState.CRDS.getValue());
            }
        }
    }

    public void verifyResidentialPlansReceivedAgainstDatabase() {
        Map<String, Object> controlNumberResult = ApplicationContext.get().getDbAction().getControlNumber();
        String controlNum = controlNumberResult.get("UZTCOTT_CONTROL_NUM").toString();
        List<Map<String, Object>> eligiblePlansList = ApplicationContext.get().getDbAction().getValidationPlansAndOffers(controlNum);
        GetEligiblePlansAndOffersResponse response = testContext.getGetEligiblePlansAndOffersResponse();

        for (Map<String, Object> eligiblePlan : eligiblePlansList) {
            comparePlanFields(eligiblePlan, response);
        }
    }

    public void verifyPlanReturned(GlobalEnums.PlanCode planCode) {
        List<Plans> plans = testContext.getGetEligiblePlansAndOffersResponse()
                .getData()
                .getPlans()
                .stream()
                .filter(p -> Objects.equals(p.getPlanCode(), planCode.getValue()))
                .toList();

        plans.stream()
                .findFirst()
                .orElseThrow(() ->
                        new AssertionError("Plan not found: " + planCode.getValue()));
    }


    public void verifyPlansDoNotContainCodes(Collection<String> disallowedCodes) {
        if (disallowedCodes == null || disallowedCodes.isEmpty()) return;

        Set<String> dis = disallowedCodes.stream()
                .filter(Objects::nonNull)
                .map(s -> s.trim().toUpperCase(Locale.ROOT))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        List<String> offending = testContext.getGetEligiblePlansAndOffersResponse()
                .getData()
                .getPlans()
                .stream()
                .map(Plans::getPlanCode)
                .filter(Objects::nonNull)
                .map(s -> s.toUpperCase(Locale.ROOT))
                .filter(dis::contains)
                .toList();

        if (!offending.isEmpty()) {
            throw new AssertionError("Disallowed plans present: " + offending);
        }
    }

    public static void comparePlanFields(Map<String, Object> eligiblePlan, GetEligiblePlansAndOffersResponse response) {
        String dbPlanCode = String.valueOf(eligiblePlan.get("planCode")).trim();
        String dbPlanDescription = String.valueOf(eligiblePlan.get("planDescription")).trim();
        String dbPromo1Code = normalize(eligiblePlan.get("promotion1Code"));
        String dbPromo1Desc = normalize(eligiblePlan.get("promotion1Description"));

        List<Plans> plans = response.getData().getPlans();
        boolean matchFound = false;

        for (Plans plan : plans) {
            if (plan.getPlanCode() != null && plan.getPlanCode().trim().equals(dbPlanCode)) {
                matchFound = true;

                String apiPlanCode = plan.getPlanCode().trim();
                String apiPlanDescription = normalize(plan.getPlanDescription());
                String apiPromo1Code = normalize(plan.getPromotion1Code());
                String apiPromo1Desc = normalize(plan.getPromotion1Description());

                Assert.assertEquals(apiPlanCode, dbPlanCode, "Plan code mismatch");
                Assert.assertEquals(apiPlanDescription, dbPlanDescription, "Plan description mismatch for planCode: " + dbPlanCode);
                Assert.assertEquals(apiPromo1Code, dbPromo1Code, "Promotion1 code mismatch for planCode: " + dbPlanCode);
                Assert.assertEquals(apiPromo1Desc, dbPromo1Desc, "Promotion1 description mismatch for planCode: " + dbPlanCode);

                break;
            }
        }

        Assert.assertTrue(matchFound, "No matching planCode found in API response for: " + dbPlanCode);
    }

    private static String normalize(Object value) {
        return value == null ? "" : value.toString().trim();
    }
    public void verifyDisallowedPlansFor(GetEligiblePlansAndOffersApiLabel testCondition) {

        final List<String> ALL = Arrays.stream(GlobalEnums.PlanCode.values())
                .map(GlobalEnums.PlanCode::getValue)
                .toList();

        final Function<List<String>, List<String>> allExcept = allowed -> {
            Set<String> dis = new LinkedHashSet<>(ALL);
            dis.removeAll(allowed);
            return new ArrayList<>(dis);
        };
        final Supplier<List<String>> allPlans = () -> new ArrayList<>(ALL);

        List<String> disallowed = switch (testCondition) {
            case MS_GE_TIER_1_NE_TC_018,
                 MS_GE_TIER_2_NE_TC_019,
                 MS_GE_TIER_9_NE_TC_020,
                 MS_GE_RS_COMM_VS_150_TC_021,
                 MS_GE_RS_ACN_LAND_BYPASS_CREDIT_TC_022,
                 MS_GE_RS_INCL_TIER_5_TC_023,
                 MS_RS_MULTIPLE_PREM_TC_024
                    -> List.of(
                    GlobalEnums.PlanCode.GB6.getValue(),
                    GlobalEnums.PlanCode.RGB.getValue(),
                    GlobalEnums.PlanCode.PGB.getValue()
            );

            case MS_RS_CRDS_ENROLLMENT_CREDIT_CHECK_TC_025 -> List.of(GlobalEnums.PlanCode.PGB.getValue());

            case MS_CM_NO_MATCH_WITH_BIN_CONFIRM_RETURNS_PLANS_TC_028,
                 MS_CM_EXCELLENT_CREDIT_CGB_NOT_OFFERED_TC_029,
                 MS_CM_CREDIT_SKIP_COM_BY_PASS_TC_030,
                 MS_CM_CREDIT_MULTIPLE_CONFIRM_FALSE_NO_CGB_TC_031 -> List.of(GlobalEnums.PlanCode.CGB.getValue());

            case MS_RS_FRAUD_ALERT_INVALID_SSN_TC_010, MS_RS_NO_MATCH_NO_CONFIRM_TC_011, MS_RS_NO_RECORD_FOUND_TC_013,
                 MS_RS_VARIANT_TC_014, MS_RS_CREDIT_FREEZE_TC_015, MS_RS_DENIAL_DUE_TC_016,
                 MS_RS_CUSTOMER_NO_AUTH_TC_017, MS_CM_NO_MATCH_INITIAL_TC_026 -> allPlans.get();

            case MS_RS_NO_RECORD_FOUND_CONFIRM_PRP_ONLY_TC_012 -> allExcept.apply(List.of(GlobalEnums.PlanCode.PRP.getValue()));

            default -> Collections.emptyList();
        };
        verifyPlansDoNotContainCodes(disallowed);
    }
}
