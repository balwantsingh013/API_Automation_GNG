package com.gng.api.pages.marketerSwitch.ServiceOrdersPages.GetEligiblePlansAndOffersPage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.request.GetEligiblePlansAndOffersRequest;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.response.GetEligiblePlansAndOffersResponse;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.response.Plans;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.marketerSwitch.ServiceOrdersSteps.GetEligiblePlansAndOffers.GetEligiblePlansAndOffersApiLabel;
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
import static com.gng.api.constants.TestConstant.CUSTOMER_DATA;
import static com.gng.api.constants.TestConstant.CUSTOMER_SHEET_NAME;
import static com.gng.api.steps.AesEncryption.AesEncryptionSteps.encryptData;

@Slf4j
public class GetEligiblePlansAndOffersHelper {

    private final TestContext testContext;

    public GetEligiblePlansAndOffersHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    GetEligiblePlansAndOffersRequest preparePayload(GetEligiblePlansAndOffersApiLabel apiLabel) {
        log.info("Preparing Market Switch payload for {}", apiLabel);
        String jsonFileName = apiLabel.equals(GetEligiblePlansAndOffersApiLabel.get_eligible_plans_and_offers)
                ? GetEligiblePlansAndOffersApiLabel.get_eligible_plans_and_offers.toString()
                : GetEligiblePlansAndOffersApiLabel.get_eligible_plans_and_offers_mandatory.toString();
        return BasePage.deserializeJsonToPojo(jsonFileName, GetEligiblePlansAndOffersRequest.class);
    }

    public void setSupportingDefaultParameters(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel testCondition) {
        payload.setRequestID(FakerDataGenerator.generateString(12));
        payload.setTransactionType(GlobalEnums.TransactionType.MKSW.getValue());
        payload.setReferralCode("");
        payload.setCreditCheckOption(YES.getValue());
        payload.setEnrollmentState(null);
        payload.setInitialCreditCheckCustomerCode(null);
        payload.setConfirmCreditCheck(null);
        payload.setSspParticipantCode(null);
        payload.setCommercialCreditCheckBusinessBIN(null);
        payload.setAglcAccountNumber(null);
        payload.setServiceTransferCurrentPricePlan(null);
        payload.setServiceTransferOfferRemainder(null);
        payload.setSeasonalSavingsProgramIndicator(false);
        payload.setMarketingPromotionCode("");
    }

    public void setParametersToSeedExternalDataBasedOnType(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel testCondition) {
        setSupportingDefaultParameters(payload, testCondition);
        switch (testCondition) {
            case SE_MRK_SW_ABLC_ACCOUNT_PROVIDED_TC_038 -> {
                setTheFieldToEmptyForCommercialScenarios(payload);
                loadCommercialData(payload, testCondition);
                payload.setCustomerType(COMMERCIAL.getValue());
                payload.setCreditCheckOption(YES.getValue());
                payload.setAglcAccountNumber(padAglcAccountNumber(FakerDataGenerator.getRandomNumericString(9)));
                payload.setSspParticipantCode(null);
            }
            default -> {
                payload.setCreditCheckOption(YES.getValue());
                payload.setAglcAccountNumber(padAglcAccountNumber(FakerDataGenerator.getRandomNumericString(9)));
                payload.setSspParticipantCode(null);
                payload.setAuthorizedBy(null);}
        }
    }

    public void setParametersToSeedExternalPositiveDataBasedOnType(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel testCondition) {
        setSupportingDefaultParameters(payload, testCondition);
        switch (testCondition) {
            case SE_MRK_SW_UPDATE_ENROLLMENT_GOOD_TC_43 -> {
                loadCustomerData(payload, testCondition);
                payload.setEnrollmentSource(GlobalEnums.EnrollmentSource.PHONECALL.getValue());
                payload.setCallerIDNotAvailable(Boolean.FALSE);
                payload.setCreditCheckOption(YES.getValue());
                payload.setAglcServiceLocationID(FakerDataGenerator.getRandomNumericString(9));
                payload.setAglcAccountNumber(padAglcAccountNumber(payload.getAglcServiceLocationID()));
                payload.setSspParticipantCode(null);
            }
            case SE_MRK_SW_DEPOSIT_PAID_NEW_FLOW_TC_44 -> {
                setTheFieldToEmptyForCommercialScenarios(payload);
                loadCommercialData(payload, testCondition);
                payload.setEnrollmentSource(GlobalEnums.EnrollmentSource.MAIL.getValue());
                payload.setCallerIDNotAvailable(Boolean.FALSE);
                payload.setCreditCheckOption(YES.getValue());
                payload.setAglcServiceLocationID(FakerDataGenerator.getRandomNumericString(9));
                payload.setAglcAccountNumber(padAglcAccountNumber(payload.getAglcServiceLocationID()));
                payload.setSspParticipantCode(null);
            }
            default -> {
                payload.setCreditCheckOption(YES.getValue());
                payload.setAglcAccountNumber(padAglcAccountNumber(FakerDataGenerator.getRandomNumericString(9)));
                payload.setSspParticipantCode(null);
                payload.setAuthorizedBy(null);}
        }
    }

    public void setSeedDataCustomerInformation(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel testCondition) {
        setSupportingDefaultParameters(payload, testCondition);
        switch (testCondition) {
            case GE_MRK_SW_RS_NEW_CC_YES_UC65_TC22,
                 GE_MRK_SW_RS_NEW_CC_YES_MULTI_PREMISES_MATCH_UC71_TC23,
                 GE_MRK_SW_RS_CRDS_CC_YES_DEPOSIT_BILLED_VALUE110_TC24 -> {
                loadCustomerData(payload, testCondition);
                payload.setCreditCheckOption(YES.getValue());
                payload.setAglcAccountNumber(padAglcAccountNumber(FakerDataGenerator.getRandomNumericString(9)));
                payload.setSspParticipantCode(null);
                payload.setAuthorizedBy(null);
            }

            case GE_MRK_SW_CM_CRDS_CC_YES_COMM_DEPOSIT_PROSPECT_UC73_TC30 -> {
                setTheFieldToEmptyForCommercialScenarios(payload);
                loadCommercialData(payload, testCondition);
                payload.setCustomerType(COMMERCIAL.getValue());
                payload.setCreditCheckOption(YES.getValue());
                payload.setConfirmCreditCheck(Boolean.TRUE);
                payload.setCommercialCreditCheckBusinessBIN(null);
                payload.setAglcAccountNumber(padAglcAccountNumber(payload.getAglcServiceLocationID()));

            }
            case GE_MRK_SW_CM_CRDS_CC_YES_PROMO_DEPOSIT_REQUIRED_VALUE210_CREDIT0_49_TC32 -> {
                setTheFieldToEmptyForCommercialScenarios(payload);
                loadCommercialData(payload, testCondition);
                payload.setCustomerType(COMMERCIAL.getValue());
                payload.setCreditCheckOption(YES.getValue());
                payload.setConfirmCreditCheck(Boolean.TRUE);
                payload.setCommercialCreditCheckBusinessBIN(null);
                payload.setAglcAccountNumber(padAglcAccountNumber(payload.getAglcServiceLocationID()));
                payload.setMarketingPromotionCode(GlobalEnums.MarketingPromotionCodes.PROMOTION_CODE_DEALS.getValue());
            }
            case GE_MRK_SW_CM_CRDS_CC_YES_DEPOSIT_REQUIRED_BUSINESS_NAME_POPULATED_UC42_TC33 -> {
                setTheFieldToEmptyForCommercialScenarios(payload);
                loadCommercialData(payload, testCondition);
                payload.setCustomerType(COMMERCIAL.getValue());
                payload.setCreditCheckOption(YES.getValue());
                payload.setConfirmCreditCheck(Boolean.FALSE);
                payload.setCommercialCreditCheckBusinessBIN(null);
                payload.setAglcAccountNumber(padAglcAccountNumber(payload.getAglcServiceLocationID()));
            }
        }
    }

    public void setParametersBasedOnType(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel testCondition) {
        setSupportingDefaultParameters(payload, testCondition);
        switch (testCondition) {
            case GE_MRK_SW_VAL_MISSING_TRANSACTION_TYPE_TC04 ->
                    payload.setTransactionType(null);

            case GE_MRK_SW_VAL_TRANSACTION_TYPE_MAX_LENGTH_TC05 ->
                    payload.setTransactionType(FakerDataGenerator.generateString(5)); // >4 chars

            case GE_MRK_SW_VAL_INVALID_TRANSACTION_TYPE_TC06 ->
                    payload.setTransactionType(GlobalEnums.InvalidValues.INVALID_TRANSACTION_TYPE.getValue());

            case GE_MRK_SW_VAL_MISSING_AGLC_ACCOUNT_NUMBER_TC07 -> {
                payload.setAglcAccountNumber(null);
            }

            case GE_MRK_SW_VAL_MISSING_AGLC_SERVICE_LOCATION_ID_TC08 -> {
                payload.setAglcServiceLocationID(null);
            }

            case GE_MRK_SW_RS_NEW_CC_YES_INVALID_SSN_FRAUD_ALERT_11114_TC09,
                 GE_MRK_SW_RS_NEW_CC_YES_NO_RECORD_CONFIRM_FALSE_11112_TC10,
                 GE_MRK_SW_RS_NEW_CC_YES_UC47_TC15 -> {
                loadCustomerData(payload, testCondition);
                payload.setCreditCheckOption(YES.getValue());
                payload.setConfirmCreditCheck(Boolean.FALSE);
            }

            case GE_MRK_SW_RS_NEW_CC_YES_NO_RECORD_CONTINUE_UC50_TC11,
                 GE_MRK_SW_RS_NEW_CC_YES_UC50_ALT_PATH_TC12,
                 GE_MRK_SW_RS_NEW_CC_YES_UC52_TC13,
                 GE_MRK_SW_RS_NEW_CC_YES_UC63_TC14 -> {
                loadCustomerData(payload, testCondition);
                payload.setCreditCheckOption(YES.getValue());
                payload.setConfirmCreditCheck(Boolean.TRUE);
            }

            case GE_MRK_SW_RS_NEW_CC_NO_DENIED_BY_CUSTOMER_TC16 -> {
                loadCustomerData(payload, testCondition);
                payload.setCreditCheckOption(NO.getValue());
                payload.setConfirmCreditCheck(Boolean.FALSE);
            }

            case GE_MRK_SW_RS_NEW_CC_YES_TIER1_VALUE100_CREDIT700_999_SSP_FALSE_TC17 -> {
                payload.setCreditCheckOption(YES.getValue());
                payload.setConfirmCreditCheck(Boolean.FALSE);
                payload.setAglcAccountNumber(FakerDataGenerator.getRandomNumericString(9));
                payload.setSeasonalSavingsProgramIndicator(false);
                payload.setAuthorizedBy("");
            }
            case GE_MRK_SW_RS_NEW_CC_YES_TIER2_VALUE101_CREDIT600_699_SSP_FALSE_TC18,
                 GE_MRK_SW_RS_NEW_CC_YES_TIER_UC45_TC19 -> {
                loadCustomerData(payload, testCondition);
                payload.setCreditCheckOption(YES.getValue());
                payload.setConfirmCreditCheck(Boolean.FALSE);
                payload.setSeasonalSavingsProgramIndicator(false);
                payload.setAuthorizedBy("");
            }

            case GE_MRK_SW_RS_NEW_CC_COMM_CREDIT_OPTION_9998_TC20 -> {
                loadCustomerData(payload, testCondition);
                payload.setSocialSecurityNumber(null);
                payload.setCreditCheckOption(COMMERCIAL_CREDIT_CHECK_REQUIRED.getValue());
                payload.setConfirmCreditCheck(Boolean.FALSE);
            }

            case GE_MRK_SW_RS_NEW_CC_YES_ACN_LANDLORD_BYPASS_CREDIT_TC21 -> {
                loadCustomerData(payload, testCondition);
                payload.setCreditCheckOption(ACN_LANDLORD.getValue());
                payload.setTenantLandlord(GlobalEnums.TenantOrLandlord.LANDLORD.getValue());
                payload.setConfirmCreditCheck(Boolean.FALSE);
                payload.setTransactionID(null);
                payload.setCustomerCode(null);
                payload.setPremisesCode(null);
                payload.setAuthorizedBy("");
            }

            case GE_MRK_SW_CM_NEW_CC_YES_UC53_TC25 -> {
                setTheFieldToEmptyForCommercialScenarios(payload);
                loadCommercialData(payload, testCondition);
                payload.setCustomerType(COMMERCIAL.getValue());
                payload.setCreditCheckOption(YES.getValue());
                payload.setConfirmCreditCheck(Boolean.FALSE);
                payload.setCommercialCreditCheckBusinessBIN(null);
                payload.setAglcAccountNumber(padAglcAccountNumber(payload.getAglcServiceLocationID()));
            }

            case GE_MRK_SW_CM_NEW_CC_YES_BIN_NULL_NO_MATCH_CONTINUE_TC26 -> {
                setTheFieldToEmptyForCommercialScenarios(payload);
                loadCommercialData(payload, testCondition);
                payload.setCustomerType(COMMERCIAL.getValue());
                payload.setCreditCheckOption(YES.getValue());
                payload.setConfirmCreditCheck(Boolean.TRUE);
                payload.setCommercialCreditCheckBusinessBIN(null);
            }

            case GE_MRK_SW_CM_NEW_CC_YES_BIN_NOT_NULL_SELECT_SIMILAR_BUSINESS_TC27 -> {
                setTheFieldToEmptyForCommercialScenarios(payload);
                loadCommercialData(payload, testCondition);
                payload.setCustomerType(COMMERCIAL.getValue());
                payload.setCreditCheckOption(YES.getValue());
                payload.setConfirmCreditCheck(Boolean.TRUE);
            }

            case GE_MRK_SW_CM_NEW_CC_YES_TIER_EXCELLENT_VALUE200_CREDIT50_100_TC28 -> {
                setTheFieldToEmptyForCommercialScenarios(payload);
                loadCommercialData(payload, testCondition);
                payload.setCustomerType(COMMERCIAL.getValue());
                payload.setCreditCheckOption(YES.getValue());
                payload.setConfirmCreditCheck(Boolean.FALSE);
                payload.setMarketingPromotionCode("");
                payload.setAglcAccountNumber(padAglcAccountNumber(payload.getAglcServiceLocationID()));
            }

            case GE_MRK_SW_CM_NEW_CC_YES_COMM_DEPOSIT_PROSPECT_UC72_TC29 -> {
                setTheFieldToEmptyForCommercialScenarios(payload);
                loadCommercialData(payload, testCondition);
                payload.setCustomerType(COMMERCIAL.getValue());
                payload.setCreditCheckOption(CREDIT_CHECK_NOT_REQUIRED.getValue());
                payload.setConfirmCreditCheck(Boolean.FALSE);
                payload.setAglcAccountNumber(padAglcAccountNumber(payload.getAglcServiceLocationID()));
            }

            case GE_MRK_SW_CM_CRDS_CC_YES_COMM_DEPOSIT_PROSPECT_UC72_TC31 -> {
                setTheFieldToEmptyForCommercialScenarios(payload);
                loadCommercialData(payload, testCondition);
                payload.setCustomerType(COMMERCIAL.getValue());
                payload.setEnrollmentState(null);
                payload.setCreditCheckOption(NO.getValue());
                payload.setConfirmCreditCheck(Boolean.FALSE);
            }

            case GE_MRK_SW_CM_CRDS_CC_YES_PROMO_DEPOSIT_REQUIRED_VALUE210_CREDIT0_49_TC32 -> {
                setTheFieldToEmptyForCommercialScenarios(payload);
                loadCommercialData(payload, testCondition);
                payload.setCustomerType(COMMERCIAL.getValue());
                payload.setEnrollmentSource(GlobalEnums.EnrollmentSource.FAX.getValue());
                payload.setMarketingPromotionCode(
                        GlobalEnums.MarketingPromotionCodes.PROMOTION_CODE_DEALS.getValue());
                payload.setCreditCheckOption(YES.getValue());
                payload.setEnrollmentState(GlobalEnums.EnrollMentState.INCL.getValue());
                payload.setConfirmCreditCheck(Boolean.FALSE);
            }

            case GE_MRK_SW_CM_CRDS_CC_YES_DEPOSIT_REQUIRED_BUSINESS_NAME_POPULATED_UC42_TC33 -> {
                setTheFieldToEmptyForCommercialScenarios(payload);
                loadCommercialData(payload, testCondition);
                payload.setCustomerType(COMMERCIAL.getValue());
                payload.setCreditCheckOption(YES.getValue());
                payload.setEnrollmentState(GlobalEnums.EnrollMentState.CRDS.getValue());
                payload.setConfirmCreditCheck(Boolean.FALSE);
            }
        }
    }

    public void setParametersSecondCallBasedOnType(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel testCondition) {
        setSupportingDefaultParameters(payload, testCondition);
        switch (testCondition) {
            case GE_MRK_SW_RS_NEW_CC_YES_UC65_TC22 -> {
                loadCustomerData(payload, testCondition);
                setParamsFromSearchAccountsResponse(payload, testCondition);
                payload.setAglcAccountNumber(testContext.getGetEligiblePlansAndOffersResponse().getData().getAglcAccountNumber());
                payload.setAglcServiceLocationID(testContext.getGetEligiblePlansAndOffersResponse().getData().getAglcServiceLocationID());
                payload.setCreditCheckOption(YES.getValue());
                payload.setEnrollmentState(GlobalEnums.EnrollMentState.INCL.getValue());
                payload.setAuthorizedBy(null);
            }
            case GE_MRK_SW_RS_NEW_CC_YES_MULTI_PREMISES_MATCH_UC71_TC23 -> {
                loadCustomerData(payload, testCondition);
                payload.setAglcAccountNumber(testContext.getGetEligiblePlansAndOffersResponse().getData().getAglcAccountNumber());
                payload.setAglcServiceLocationID(testContext.getGetEligiblePlansAndOffersResponse().getData().getAglcServiceLocationID());
                payload.setInitialCreditCheckCustomerCode(testContext.getGetEligiblePlansAndOffersResponse().getData().getCustomerCode());
                payload.setCreditCheckOption(MULTIPLE_PREMISES_OWNER.getValue());
                payload.setEnrollmentState(null);
                payload.setAuthorizedBy(null);
            }
            case GE_MRK_SW_RS_CRDS_CC_YES_DEPOSIT_BILLED_VALUE110_TC24 -> {
                loadCustomerData(payload, testCondition);
                setParamsFromSearchAccountsResponse(payload, testCondition);
                payload.setAglcAccountNumber(testContext.getGetEligiblePlansAndOffersResponse().getData().getAglcAccountNumber());
                payload.setAglcServiceLocationID(testContext.getGetEligiblePlansAndOffersResponse().getData().getAglcServiceLocationID());
                payload.setCreditCheckOption(YES.getValue());
                payload.setEnrollmentState(GlobalEnums.EnrollMentState.CRDS.getValue());
                payload.setAuthorizedBy(null);
            }
            case GE_MRK_SW_CM_CRDS_CC_YES_COMM_DEPOSIT_PROSPECT_UC73_TC30 -> {
                loadSecondCallCommercialData(payload, testCondition);
                loadCommercialDataFromResponse(payload, testCondition);
                payload.setCustomerType(COMMERCIAL.getValue());
                payload.setConfirmCreditCheck(Boolean.FALSE);
                payload.setCreditCheckOption(MULTIPLE_PREMISES_OWNER.getValue());
                payload.setAglcAccountNumber(testContext.getGetEligiblePlansAndOffersResponse().getData().getAglcAccountNumber());
                payload.setAglcServiceLocationID(testContext.getGetEligiblePlansAndOffersResponse().getData().getAglcServiceLocationID());
            }

            case GE_MRK_SW_CM_CRDS_CC_YES_PROMO_DEPOSIT_REQUIRED_VALUE210_CREDIT0_49_TC32,
                 GE_MRK_SW_CM_CRDS_CC_YES_DEPOSIT_REQUIRED_BUSINESS_NAME_POPULATED_UC42_TC33 -> {
                clearNameFields(payload);
                loadSecondCallCommercialData(payload, testCondition);
                setParamsFromSearchAccountsResponse(payload, testCondition);
                payload.setEnrollmentState(GlobalEnums.EnrollMentState.CRDS.getValue());
                payload.setCustomerType(COMMERCIAL.getValue());
                payload.setConfirmCreditCheck(Boolean.FALSE);
                payload.setCreditCheckOption(MULTIPLE_PREMISES_OWNER.getValue());
                payload.setAglcAccountNumber(testContext.getGetEligiblePlansAndOffersResponse().getData().getAglcAccountNumber());
                payload.setAglcServiceLocationID(testContext.getGetEligiblePlansAndOffersResponse().getData().getAglcServiceLocationID());
            }
        }
    }

    public void setTheFieldToEmptyForCommercialScenarios(GetEligiblePlansAndOffersRequest payload) {
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

    public void loadCommercialDataFromResponse(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel testCondition) {

        switch (testCondition) {
            case GE_MRK_SW_CM_NEW_CC_YES_BIN_NULL_NO_MATCH_CONTINUE_TC26,
                 GE_MRK_SW_CM_NEW_CC_YES_BIN_NOT_NULL_SELECT_SIMILAR_BUSINESS_TC27 -> {
                var data = testContext.getGetEligiblePlansAndOffersResponse()
                        .getData()
                        .getSimilarBusinesses()
                        .getFirst();
                payload.setCustomerBusinessName(data.getBusinessName());
                payload.setPremisesStreetName(data.getBusinessStreet());
                payload.setPremisesCity(data.getBusinessCity());
                payload.setPremisesStateCode(data.getBusinessState());
                payload.setPremisesZipCode(data.getBusinessZipCode());
                payload.setCommercialCreditCheckBusinessBIN(data.getBusinessBIN());
            }
            case GE_MRK_SW_CM_CRDS_CC_YES_COMM_DEPOSIT_PROSPECT_UC73_TC30 -> {
                var data = testContext.getGetEligiblePlansAndOffersResponse()
                        .getData();
                payload.setCustomerBusinessName(data.getCustomerBusinessName());
                payload.setInitialCreditCheckCustomerCode(testContext.getGetEligiblePlansAndOffersResponse()
                        .getData().getCustomerCode());
            }
            default -> {
            }
        }
    }

    public void loadCommercialData(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel testCondition) {
        Map<String, String> customerData =
                loadRowFromExcelToCustomerData(CUSTOMER_DATA, CUSTOMER_SHEET_NAME, testCondition);
        getCommercialCustomerAndPremiseDetails(payload, customerData, testCondition);
    }
    public void loadSecondCallCommercialData(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel testCondition) {
        Map<String, String> customerData =
                loadRowFromExcelToCustomerData(CUSTOMER_DATA, CUSTOMER_SHEET_NAME, testCondition);
        getSecondCallCommercialCustomerAndPremiseDetails(payload, customerData, testCondition);
    }

    private void getCommercialCustomerAndPremiseDetails(GetEligiblePlansAndOffersRequest payload, Map<String, String> data, GetEligiblePlansAndOffersApiLabel testCondition) {

        String federalTaxId = data.get("federalTaxId");
        if (federalTaxId != null && !federalTaxId.trim().isEmpty()) {
            payload.setFederalTaxID(encryptData(federalTaxId));
        }

        switch (testCondition) {
            case GE_MRK_SW_CM_CRDS_CC_YES_COMM_DEPOSIT_PROSPECT_UC73_TC30 -> {
                payload.setSocialSecurityNumber("");
                payload.setCustomerType(data.get("customerType"));
                payload.setCustomerBusinessName(data.get("customerLastName"));
                payload.setCommercialCreditCheckBusinessBIN(null);
                payload.setAglcServiceLocationID(FakerDataGenerator.getRandomNumericString(8));
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
            default -> {
                payload.setCustomerType(data.get("customerType"));
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
                payload.setCreditCheckBusinessName(data.get("creditCheckBusinessName"));
                payload.setAglcAccountNumber(FakerDataGenerator.getRandomNumericString(9));
            }
        }
    }

    private void getSecondCallCommercialCustomerAndPremiseDetails(GetEligiblePlansAndOffersRequest payload, Map<String, String> data, GetEligiblePlansAndOffersApiLabel testCondition) {
        String federalTaxId = data.get("federalTaxId");
        if (federalTaxId != null && !federalTaxId.trim().isEmpty()) {
            payload.setFederalTaxID(encryptData(federalTaxId));
        }

        switch (testCondition) {
            case GE_MRK_SW_CM_CRDS_CC_YES_COMM_DEPOSIT_PROSPECT_UC73_TC30 -> {
                payload.setCustomerLastName("");
                payload.setCustomerFirstName("");
                payload.setSocialSecurityNumber("");
                payload.setCustomerType(data.get("customerType"));
                payload.setCustomerBusinessName(data.get("customerLastName"));
                payload.setCommercialCreditCheckBusinessBIN(null);
                payload.setAglcServiceLocationID(data.get("aglcServiceLocationID"));
                payload.setInitialCreditCheckCustomerCode(
                        testContext.getGetEligiblePlansAndOffersResponse().getData().getCustomerCode());
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
                payload.setCreditCheckBusinessName(data.get("creditCheckBusinessName"));
                payload.setAglcAccountNumber(FakerDataGenerator.getRandomNumericString(9));
            }
        }
    }

    public void loadCustomerData(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel testCondition) {
        Map<String, String> customerData =
                loadRowFromExcelToCustomerData(CUSTOMER_DATA, CUSTOMER_SHEET_NAME, testCondition);
        getCustomerAndPremiseDetails(payload, customerData, testCondition);
    }

    public void getCustomerAndPremiseDetails(GetEligiblePlansAndOffersRequest payload,
                                             Map<String, String> data,
                                             GetEligiblePlansAndOffersApiLabel testCondition) {
        clearIds(payload);
        payload.setAcnStatusIndicator(get(data, "acnStatusIndicator"));
        payload.setAglcAccountNumber(get(data, "aglcAccountNumber"));
        payload.setAglcServiceLocationID(get(data, "aglcServiceLocationID"));
        payload.setPremisesCountyCode(get(data, "premisesCountyCode"));
        payload.setCallerID(get(data, "Phone"));
        applyPersonNames(payload, data);
        applyAddress(payload, data);

        switch (testCondition) {
            case GE_MRK_SW_RS_NEW_CC_YES_UC50_ALT_PATH_TC12,
                 GE_MRK_SW_RS_NEW_CC_YES_UC52_TC13 -> {
                payload.setAuthorizedBy("");
                payload.setGenerationCode(null);
                payload.setPremisesUnitType(null);
                payload.setPremisesUnitNumber(null);
                payload.setPremisesStreetPreDirection(null);
                payload.setCallerID(null);
                payload.setEmailAddress(null);
                payload.setAdditionalEnrollmentData(null);
                payload.setAglcAccountNumber(FakerDataGenerator.getRandomNumericString(9));

                String creditCheckCustomerCode = get(data, "CreditCheckCustomerCode");
                payload.setInitialCreditCheckCustomerCode(
                        creditCheckCustomerCode.isBlank()
                                ? null
                                : creditCheckCustomerCode
                );
            }

            default ->  payload.setAglcAccountNumber(FakerDataGenerator.getRandomNumericString(9));
        }

        encryptAndSetSSN(payload, data.get("SSN"));
        encryptAndSetFedTaxId(payload, data.get("federalTaxId"));
    }

    public void setParamsFromSearchAccountsResponse(GetEligiblePlansAndOffersRequest payload,
                                                    GetEligiblePlansAndOffersApiLabel testCondition) {
        String geTransactionIdRaw = String.valueOf(
                testContext.getGetEligiblePlansAndOffersResponse()
                        .getData()
                        .getTransactionID()
        );

        int geTransactionId = parseIntOrAssert(geTransactionIdRaw,
                "GetEligiblePlansAndOffersResponse.data.transactionID");

        var matchingAccount = testContext.getSearchAccountsResponse()
                .getData()
                .getAccounts()
                .stream()
                .filter(acct -> {
                    String acctTxRaw = String.valueOf(acct.getTransactionID());
                    int acctTxId = parseIntOrAssert(acctTxRaw,
                            "SearchAccountsResponse.data.accounts[].transactionID (customerCode=" + acct.getCustomerCode() + ")");
                    return acctTxId == geTransactionId;
                })
                .findFirst()
                .orElse(null);

        Assert.assertNotNull(matchingAccount,
                "No SearchAccounts account found for transactionId: " + geTransactionId
        );

        switch (testCondition) {
            case GE_MRK_SW_RS_NEW_CC_YES_UC65_TC22,
                 GE_MRK_SW_RS_CRDS_CC_YES_DEPOSIT_BILLED_VALUE110_TC24,
                 GE_MRK_SW_CM_CRDS_CC_YES_PROMO_DEPOSIT_REQUIRED_VALUE210_CREDIT0_49_TC32,
                 GE_MRK_SW_CM_CRDS_CC_YES_DEPOSIT_REQUIRED_BUSINESS_NAME_POPULATED_UC42_TC33 -> {
                payload.setCustomerCode(matchingAccount.getCustomerCode());
                payload.setPremisesCode(matchingAccount.getPremisesCode());
                payload.setAglcAccountNumber(matchingAccount.getAglcAccountNumber());
                payload.setTransactionID(geTransactionId);
            }
        }
    }

    private int parseIntOrAssert(String value, String fieldName) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            throw new AssertionError("Unable to parse int from " + fieldName + " value: [" + value + "]", ex);
        }
    }

    private static String get(Map<String, String> data, String key) {
        return Optional.ofNullable(data.get(key)).orElse("");
    }

    private static void clearIds(GetEligiblePlansAndOffersRequest payload) {
        payload.setCustomerCode(null);
        payload.setPremisesCode(null);
    }
    private static void clearNameFields(GetEligiblePlansAndOffersRequest payload) {
        payload.setCustomerLastName(null);
        payload.setCustomerMiddleName(null);
        payload.setCustomerFirstName(null);
        payload.setGenerationCode(null);
    }

    private static void applyPersonNames(GetEligiblePlansAndOffersRequest payload, Map<String, String> data) {
        payload.setCustomerLastName(get(data, "customerLastName"));
        payload.setCustomerMiddleName(get(data, "customerMiddleName"));
        payload.setCustomerFirstName(get(data, "customerFirstName"));
        payload.setGenerationCode(get(data, "generationCode"));
    }

    private static void applyAddress(GetEligiblePlansAndOffersRequest payload, Map<String, String> data) {
        payload.setPremisesStreetNumber(get(data, "premisesStreetNumber"));
        payload.setPremisesStreetName(get(data, "premisesStreetName"));
        payload.setPremisesStreetSuffix(get(data, "premisesStreetSuffix"));
        payload.setPremisesStreetPostDirection(get(data, "premisesStreetPostDirection"));
        payload.setPremisesUnitType(get(data, "premisesUnitType"));
        payload.setPremisesUnitNumber(get(data, "premisesUnitNumber"));
        payload.setPremisesCity(get(data, "premisesCity"));
        payload.setPremisesStateCode(get(data, "premisesStateCode"));
        payload.setPremisesZipCode(get(data, "premisesZipCode"));
    }

    private void encryptAndSetSSN(GetEligiblePlansAndOffersRequest payload, String ssnRaw) {
        if (ssnRaw != null && !ssnRaw.trim().isEmpty()) {
            payload.setSocialSecurityNumber(encryptData(ssnRaw));
        }
    }

    private void encryptAndSetFedTaxId(GetEligiblePlansAndOffersRequest payload, String fedTaxRaw) {
        if (fedTaxRaw != null && !fedTaxRaw.trim().isEmpty()) {
            payload.setFederalTaxID(encryptData(fedTaxRaw));
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

    public void verifyResidentialPlansReceivedAgainstDatabase() {
        Map<String, Object> controlNumberResult = ApplicationContext.get().getDbAction().getControlNumber();
        String controlNum = controlNumberResult.get("UZTCOTT_CONTROL_NUM").toString();
        List<Map<String, Object>> eligiblePlansList =
                ApplicationContext.get().getDbAction().getValidationPlansAndOffers(controlNum);
        GetEligiblePlansAndOffersResponse response = testContext.getGetEligiblePlansAndOffersResponse();

        for (Map<String, Object> eligiblePlan : eligiblePlansList) {
            comparePlanFields(eligiblePlan, response);
        }
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

    public static void comparePlanFields(Map<String, Object> eligiblePlan,
                                         GetEligiblePlansAndOffersResponse response) {
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
                Assert.assertEquals(apiPlanDescription, dbPlanDescription,
                        "Plan description mismatch for planCode: " + dbPlanCode);
                Assert.assertEquals(apiPromo1Code, dbPromo1Code,
                        "Promotion1 code mismatch for planCode: " + dbPlanCode);
                Assert.assertEquals(apiPromo1Desc, dbPromo1Desc,
                        "Promotion1 description mismatch for planCode: " + dbPlanCode);

                break;
            }
        }

        Assert.assertTrue(matchFound,
                "No matching planCode found in API response for: " + dbPlanCode);
    }

    public void verifyDisallowedPlansFor(GetEligiblePlansAndOffersApiLabel testCondition) {

        final List<String> ALL = Arrays.stream(GlobalEnums.PlanCode.values())
                .map(GlobalEnums.PlanCode::getValue)
                .toList();

        final Function<List<String>, List<String>> allExcept = allowed -> {
            Set<String> dis = new LinkedHashSet<>(ALL);
            allowed.forEach(dis::remove);
            return new ArrayList<>(dis);
        };

        final Supplier<List<String>> allPlans = () -> new ArrayList<>(ALL);

        List<String> standardPlans = List.of(
                GlobalEnums.PlanCode.RGB.getValue(),
                GlobalEnums.PlanCode.GPP.getValue(),
                GlobalEnums.PlanCode.M24.getValue(),
                GlobalEnums.PlanCode.M18.getValue(),
                GlobalEnums.PlanCode.RF6.getValue(),
                GlobalEnums.PlanCode.MVS.getValue(),
                GlobalEnums.PlanCode.CSV.getValue(),
                GlobalEnums.PlanCode.MI.getValue(),
                GlobalEnums.PlanCode.TRD.getValue(),
                GlobalEnums.PlanCode.PGB.getValue(),
                GlobalEnums.PlanCode.PRP.getValue()
        );

        List<String> prepayOnlyPlans = List.of(
                GlobalEnums.PlanCode.VML.getValue(),
                GlobalEnums.PlanCode.PGB.getValue(),
                GlobalEnums.PlanCode.PRP.getValue()
        );

        List<String> vsMvsMiTrdPgbPrp = List.of(
                GlobalEnums.PlanCode.MVS.getValue(),
                GlobalEnums.PlanCode.CSV.getValue(),
                GlobalEnums.PlanCode.MI.getValue(),
                GlobalEnums.PlanCode.TRD.getValue(),
                GlobalEnums.PlanCode.PGB.getValue(),
                GlobalEnums.PlanCode.PRP.getValue()
        );

        List<String> disallowed = switch (testCondition) {
            case GE_MRK_SW_RS_NEW_CC_YES_TIER1_VALUE100_CREDIT700_999_SSP_FALSE_TC17,
                 GE_MRK_SW_RS_NEW_CC_YES_TIER2_VALUE101_CREDIT600_699_SSP_FALSE_TC18,
                 GE_MRK_SW_RS_NEW_CC_COMM_CREDIT_OPTION_9998_TC20,
                 GE_MRK_SW_RS_NEW_CC_YES_MULTI_PREMISES_MATCH_UC71_TC23
                    -> allExcept.apply(standardPlans);

            case GE_MRK_SW_RS_NEW_CC_YES_TIER_UC45_TC19
                    -> allExcept.apply(prepayOnlyPlans);

            case GE_MRK_SW_RS_NEW_CC_YES_ACN_LANDLORD_BYPASS_CREDIT_TC21
                    -> allExcept.apply(List.of(
                    GlobalEnums.PlanCode.RGB.getValue(),
                    GlobalEnums.PlanCode.GPP.getValue(),
                    GlobalEnums.PlanCode.M24.getValue(),
                    GlobalEnums.PlanCode.M18.getValue(),
                    GlobalEnums.PlanCode.RF6.getValue(),
                    GlobalEnums.PlanCode.MVS.getValue(),
                    GlobalEnums.PlanCode.CSV.getValue(),
                    GlobalEnums.PlanCode.MI.getValue(),
                    GlobalEnums.PlanCode.TRD.getValue()
            ));

            case GE_MRK_SW_RS_NEW_CC_YES_UC65_TC22
                    -> allExcept.apply(vsMvsMiTrdPgbPrp);

            case GE_MRK_SW_RS_CRDS_CC_YES_DEPOSIT_BILLED_VALUE110_TC24
                    -> List.of(GlobalEnums.PlanCode.PGB.getValue());

            case GE_MRK_SW_RS_NEW_CC_YES_INVALID_SSN_FRAUD_ALERT_11114_TC09,
                 GE_MRK_SW_RS_NEW_CC_YES_NO_RECORD_CONFIRM_FALSE_11112_TC10,
                 GE_MRK_SW_RS_NEW_CC_NO_DENIED_BY_CUSTOMER_TC16,
                 GE_MRK_SW_CM_NEW_CC_YES_UC53_TC25
                    -> allPlans.get();

            default -> Collections.emptyList();
        };

        verifyPlansDoNotContainCodes(disallowed);
    }

    public void verifyEnrollmentRecord(GetEligiblePlansAndOffersApiLabel testCondition) {
        GlobalEnums.CreditScoreStatus expectedCreditScoreStatus = null;
        GlobalEnums.CreditScoreText expectedCreditScoreTextEnum = null;
        GlobalEnums.EnrollMentState expectedEnrollmentStateEnum;
        GlobalEnums.MarketingPromotionCodes expectedMarketingPromotionCodeEnum = null;
        GlobalEnums.PlanCode expectedPlanCodeEnum = null;
        Supplier<Map<String, Object>> enrollmentSupplier;

        switch (testCondition) {
            case GE_MRK_SW_RS_NEW_CC_YES_NO_RECORD_CONTINUE_UC50_TC11 -> {
                expectedEnrollmentStateEnum = GlobalEnums.EnrollMentState.INCL;
                expectedCreditScoreStatus = GlobalEnums.CreditScoreStatus.STATUS_TEXT;
                expectedCreditScoreTextEnum = GlobalEnums.CreditScoreText.MATCH_CODE_B;
                enrollmentSupplier = this::getEnrollmentRecordFromGetEligiblePlansAndOffersResponse;
            }

            case GE_MRK_SW_RS_NEW_CC_YES_UC50_ALT_PATH_TC12,
                 GE_MRK_SW_RS_NEW_CC_YES_UC52_TC13 -> {
                expectedEnrollmentStateEnum = GlobalEnums.EnrollMentState.PENDING_REVIEW;
                expectedCreditScoreStatus = GlobalEnums.CreditScoreStatus.STATUS_TEXT;
                expectedCreditScoreTextEnum = GlobalEnums.CreditScoreText.VERIFY_ID;
                enrollmentSupplier = () -> getEnrollmentRecordFromCustomerLastName(testCondition);
            }

            case GE_MRK_SW_RS_NEW_CC_YES_UC63_TC14 -> {
                verifyNoEnrollmentRecordCreated(testCondition);
                return;
            }

            case GE_MRK_SW_RS_NEW_CC_YES_UC47_TC15 -> {
                expectedEnrollmentStateEnum = GlobalEnums.EnrollMentState.BAD_CREDIT;
                enrollmentSupplier = () -> getEnrollmentRecordFromCustomerLastName(testCondition);
            }

            case GE_MRK_SW_RS_NEW_CC_NO_DENIED_BY_CUSTOMER_TC16 -> {
                expectedEnrollmentStateEnum = GlobalEnums.EnrollMentState.INCL;
                expectedCreditScoreStatus = GlobalEnums.CreditScoreStatus.REFUSE;
                expectedCreditScoreTextEnum = GlobalEnums.CreditScoreText.NOT_APPLICABLE;
                enrollmentSupplier = () -> getEnrollmentRecordFromCustomerLastName(testCondition);
            }

            case GE_MRK_SW_RS_NEW_CC_YES_TIER1_VALUE100_CREDIT700_999_SSP_FALSE_TC17,
                 GE_MRK_SW_RS_NEW_CC_YES_TIER2_VALUE101_CREDIT600_699_SSP_FALSE_TC18,
                 GE_MRK_SW_RS_NEW_CC_YES_TIER_UC45_TC19 -> {
                expectedEnrollmentStateEnum = GlobalEnums.EnrollMentState.INCL;
                expectedCreditScoreStatus = GlobalEnums.CreditScoreStatus.NUMBER;
                expectedCreditScoreTextEnum = GlobalEnums.CreditScoreText.MATCH_CODE_C;
                enrollmentSupplier = this::getEnrollmentRecordFromGetEligiblePlansAndOffersResponse;
            }

            case GE_MRK_SW_RS_NEW_CC_COMM_CREDIT_OPTION_9998_TC20,
                 GE_MRK_SW_RS_NEW_CC_YES_MULTI_PREMISES_MATCH_UC71_TC23 -> {
                expectedEnrollmentStateEnum = GlobalEnums.EnrollMentState.INCL;
                expectedCreditScoreStatus = GlobalEnums.CreditScoreStatus.NUMBER;
                enrollmentSupplier = this::getEnrollmentRecordFromGetEligiblePlansAndOffersResponse;
            }

            case GE_MRK_SW_RS_NEW_CC_YES_ACN_LANDLORD_BYPASS_CREDIT_TC21 -> {
                expectedEnrollmentStateEnum = GlobalEnums.EnrollMentState.INCL;
                expectedCreditScoreStatus = GlobalEnums.CreditScoreStatus.ACNL;
                expectedCreditScoreTextEnum = GlobalEnums.CreditScoreText.NOT_APPLICABLE;
                enrollmentSupplier = this::getEnrollmentRecordFromGetEligiblePlansAndOffersResponse;
            }

            case GE_MRK_SW_RS_NEW_CC_YES_UC65_TC22 -> {
                expectedEnrollmentStateEnum = GlobalEnums.EnrollMentState.INCL;
                expectedCreditScoreStatus = GlobalEnums.CreditScoreStatus.NUMBER;
                expectedCreditScoreTextEnum = GlobalEnums.CreditScoreText.MATCH_CODE_C;
                expectedMarketingPromotionCodeEnum =
                        GlobalEnums.MarketingPromotionCodes.PROMOTION_CODE_FIX_10_DOLLARS_FOR_12_MONTHS;
                expectedPlanCodeEnum = GlobalEnums.PlanCode.PGB;
                enrollmentSupplier = this::getEnrollmentRecordFromGetEligiblePlansAndOffersResponse;
            }

            default -> {
                return;
            }
        }

        Map<String, Object> enrollmentRecord = enrollmentSupplier.get();

        String expectedEnrollmentState = expectedEnrollmentStateEnum.getValue();
        String expectedStatus = expectedCreditScoreStatus == null ? null : expectedCreditScoreStatus.getValue();
        String expectedScoreText = expectedCreditScoreTextEnum == null ? null : expectedCreditScoreTextEnum.getValue();
        String expectedMarketingPromotionCodeText =
                expectedMarketingPromotionCodeEnum == null ? null : expectedMarketingPromotionCodeEnum.getValue();
        String expectedPlanCodeText =
                expectedPlanCodeEnum == null ? null : expectedPlanCodeEnum.getValue();

        String actualStatus = enrollmentRecord.get("UZBENRO_CRED_SCORE_STATUS") == null
                ? null
                : enrollmentRecord.get("UZBENRO_CRED_SCORE_STATUS").toString();

        String actualScoreText = enrollmentRecord.get("UZBENRO_CRED_SCORE_TEXT") == null
                ? null
                : enrollmentRecord.get("UZBENRO_CRED_SCORE_TEXT").toString();

        String actualEnrollmentStatus = enrollmentRecord.get("UZBENRO_ENRO_STATUS") == null
                ? null
                : enrollmentRecord.get("UZBENRO_ENRO_STATUS").toString();

        String actualMarketingPromotionCodeText = enrollmentRecord.get("UZBENRO_CAMPAIGN_ID") == null
                ? null
                : enrollmentRecord.get("UZBENRO_CAMPAIGN_ID").toString();

        String actualPlanCodeText = enrollmentRecord.get("UZBENRO_PRICE_PLAN") == null
                ? null
                : enrollmentRecord.get("UZBENRO_PRICE_PLAN").toString();

        Assert.assertEquals(
                actualEnrollmentStatus,
                expectedEnrollmentState,
                "Unexpected UZBENRO_ENRO_STATUS for condition: " + testCondition
        );

        if (expectedStatus != null) {
            Assert.assertEquals(
                    actualStatus,
                    expectedStatus,
                    "Unexpected UZBENRO_CRED_SCORE_STATUS for condition: " + testCondition
            );
        }

        if (expectedScoreText != null) {
            Assert.assertEquals(
                    actualScoreText,
                    expectedScoreText,
                    "Unexpected UZBENRO_CRED_SCORE_TEXT for condition: " + testCondition
            );
        }

        if (expectedMarketingPromotionCodeText != null) {
            Assert.assertEquals(
                    actualMarketingPromotionCodeText,
                    expectedMarketingPromotionCodeText,
                    "Unexpected UZBENRO_CAMPAIGN_ID (promo code) for condition: " + testCondition
            );
        }

        if (expectedPlanCodeText != null) {
            Assert.assertEquals(
                    actualPlanCodeText,
                    expectedPlanCodeText,
                    "Unexpected UZBENRO_PRICE_PLAN for condition: " + testCondition
            );
        }
    }

    private Map<String, Object> getEnrollmentRecordFromGetEligiblePlansAndOffersResponse() {
        if (testContext.getGetEligiblePlansAndOffersResponse() == null
                || testContext.getGetEligiblePlansAndOffersResponse().getData() == null) {
            throw new AssertionError("getEligiblePlansAndOffers response null for enrollment record verification");
        }

        String customerCode = testContext.getGetEligiblePlansAndOffersResponse()
                .getData()
                .getCustomerCode();

        return ApplicationContext.get()
                .getDbAction()
                .getEnrollmentRecordByCustomerCode(customerCode);
    }

    private Map<String, Object> getEnrollmentRecordFromSearchAccountsResponse() {
        if (testContext.getSearchAccountsResponse() == null
                || testContext.getSearchAccountsResponse().getData() == null) {
            throw new AssertionError("searchAccounts response null for enrollment record verification");
        }

        String customerCode = testContext.getSearchAccountsResponse()
                .getData()
                .getAccounts()
                .getLast()
                .getCustomerCode();

        return ApplicationContext.get()
                .getDbAction()
                .getEnrollmentRecordByCustomerCode(customerCode);
    }

    private Map<String, Object> getEnrollmentRecordFromCustomerLastName(GetEligiblePlansAndOffersApiLabel testCondition) {
        GetEligiblePlansAndOffersRequest payload = preparePayload(testCondition);
        loadCustomerData(payload, testCondition);

        String lastName = payload.getCustomerLastName();

        List<Map<String, Object>> records = ApplicationContext.get()
                .getDbAction()
                .getEnrollmentRecordsForMarketerSwitch(lastName);

        if (records == null || records.isEmpty()) {
            throw new AssertionError("No Enrollment record found for lastName=" + lastName
                    + " for condition: " + testCondition);
        }

        if (records.size() > 1) {
            log.warn("Multiple Enrollment records found for lastName={}, using the first one", lastName);
        }

        return records.getFirst();
    }

    private void verifyNoEnrollmentRecordCreated(GetEligiblePlansAndOffersApiLabel testCondition) {
        GetEligiblePlansAndOffersRequest payload = preparePayload(testCondition);
        loadCustomerData(payload, testCondition);

        List<Map<String, Object>> records = ApplicationContext.get()
                .getDbAction()
                .getEnrollmentRecordsForMarketerSwitch(payload.getCustomerLastName());

        int count = (records == null) ? 0 : records.size();
        Assert.assertEquals(
                count,
                0,
                "Expected no Enrollment record to be created for " + testCondition + ", but found " + count + " record(s)"
        );
    }

    private String padAglcAccountNumber(String value) {
        if (value == null) {
            return null;
        }

        int targetLength = 20;
        if (value.length() >= targetLength) {
            return value;
        }

        String sb = "0".repeat(targetLength - value.length()) +
                value;
        return sb;
    }

    private static String normalize(Object value) {
        return value == null ? "" : value.toString().trim();
    }
}
