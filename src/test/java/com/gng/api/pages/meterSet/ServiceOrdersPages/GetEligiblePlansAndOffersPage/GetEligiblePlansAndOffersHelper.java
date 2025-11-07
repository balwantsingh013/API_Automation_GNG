package com.gng.api.pages.meterSet.ServiceOrdersPages.GetEligiblePlansAndOffersPage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.request.GetEligiblePlansAndOffersRequest;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.response.DataResult;
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
import java.util.stream.Collectors;

import static com.gng.api.constants.DBConstant.UCBCUST_FIRST_NAME;
import static com.gng.api.constants.DBConstant.UCBCUST_LAST_NAME;
import static com.gng.api.constants.GlobalEnums.CreditCheckOption.*;
import static com.gng.api.constants.GlobalEnums.TransactionType.METER_SET;
import static com.gng.api.constants.GlobalEnums.TransactionType.TURN_ON;
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
                payload.setCustomerType(GlobalEnums.CustomerType.COMMERCIAL.getValue());
                payload.setConfirmCreditCheck(false);
                payload.setCommercialCreditCheckBusinessBIN(null);
            }

            case MS_CM_NO_MATCH_CONFIRM_RETURNS_PLANS_TC_027 -> {
                payload.setCustomerType(GlobalEnums.CustomerType.COMMERCIAL.getValue());
                payload.setConfirmCreditCheck(true);
                payload.setCommercialCreditCheckBusinessBIN(null);
            }

            case MS_CM_VARIANT_TC_028, MS_CM_VARIANT_TC_029, MS_CM_VARIANT_TC_030,
                 MS_CM_VARIANT_TC_031, MS_CM_VARIANT_TC_032, MS_CM_VARIANT_TC_033,
                 MS_CM_VARIANT_TC_034 -> {
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
//                payload.setTransactionType(METER_SET.getValue());
                payload.setCreditCheckOption(YES.getValue());
                payload.setConfirmCreditCheck(Boolean.FALSE);
            }

            case MS_RS_NO_MATCH_NO_CONFIRM_TC_011 -> {
                loadCustomerData(payload, testCondition);
                payload.setConfirmCreditCheck(Boolean.FALSE);
            }

            case MS_RS_NO_RECORD_FOUND_CONFIRM_PRP_ONLY_TC_012 -> {
                loadCustomerData(payload, testCondition);
                payload.setCustomerType(GlobalEnums.CustomerType.RESIDENTIAL.getValue());
                payload.setConfirmCreditCheck(Boolean.TRUE);
            }

            case MS_RS_NO_RECORD_FOUND_TC_013 -> {
                //loadCustomerData(payload, testCondition);
                setRequestParamsForTransferFromSearchAccountsResponse(payload, testCondition);
                setInvalidCombinationCustomerData(payload, testCondition);

                payload.setConfirmCreditCheck(Boolean.TRUE);
                payload.setCustomerCode(null);
                payload.setPremisesCode(null);
                payload.setAcnStatusIndicator(testContext.getGetEligiblePlansAndOffersResponse().getData().getAcnStatusIndicator());
                payload.setAuthorizedBy("");
//                payload.setCustomerCode(testContext.getGetEligiblePlansAndOffersResponse().getData().getCustomerCode());
//                payload.setPremisesCode(testContext.getGetEligiblePlansAndOffersResponse().getData().getPremisesCode());
            }

            case  MS_RS_CREDIT_FREEZE_TC_015 -> {
                loadCustomerData(payload, testCondition);
                payload.setConfirmCreditCheck(Boolean.TRUE);
                payload.setCustomerCode(null);
                payload.setPremisesCode(null);
            }

            case   MS_RS_DENIAL_DUE_TC_016 -> {
                loadCustomerData(payload, testCondition);
                payload.setSocialSecurityNumber("acfkXTY3/STA6XzuMulKpo1gvgTPjLmi0+i4zdsrLMs=");
//                setRequestParamsForTransferFromSearchAccountsResponse(payload, testCondition);
                payload.setConfirmCreditCheck(Boolean.FALSE);
                payload.setCustomerCode(null);
                payload.setPremisesCode(null);
            }

            case   MS_RS_CUSTOMER_NO_AUTH_TC_017 -> {
                loadCustomerData(payload, testCondition);
//                setRequestParamsForTransferFromSearchAccountsResponse(payload, testCondition);
                payload.setConfirmCreditCheck(Boolean.FALSE);
                payload.setCreditCheckOption(GlobalEnums.CreditCheckOption.NO.getValue());
                payload.setCustomerCode(null);
                payload.setPremisesCode(null);
            }
            case MS_GE_TIER_1_NE_TC_018, MS_GE_TIER_2_NE_TC_019, MS_GE_TIER_9_NE_TC_020 -> {
                loadCustomerData(payload, testCondition);
                payload.setConfirmCreditCheck(Boolean.FALSE);
                payload.setCreditCheckOption(YES.getValue());
                payload.setCustomerCode(null);
                payload.setPremisesCode(null);
                payload.setAuthorizedBy("TIMOTHY LUCH");

            }
            case MS_GE_RS_COMM_VS_150_TC_021 -> {
                loadCustomerData(payload, testCondition);
                payload.setConfirmCreditCheck(Boolean.FALSE);
                payload.setCreditCheckOption(COMMERCIAL_CREDIT_CHECK_REQUIRED.getValue());
                payload.setCustomerCode(null);
                payload.setPremisesCode(null);
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

            case MS_RS_VARIANT_TC_014 -> {
                setRequestParamsForTransferFromSearchAccountsResponse(payload, testCondition);
                setInvalidCombinationCustomerData(payload, testCondition);
                payload.setConfirmCreditCheck(Boolean.TRUE);
                payload.setCreditCheckOption(YES.getValue());
                payload.setCustomerCode(null);
                payload.setPremisesCode(null);
            }

            // ==================== COMMERCIAL (TC-026..034) ====================
            case MS_CM_NO_MATCH_INITIAL_TC_026 -> {
                payload.setCustomerType(GlobalEnums.CustomerType.COMMERCIAL.getValue());
                payload.setConfirmCreditCheck(false);
                payload.setCommercialCreditCheckBusinessBIN(null);
            }

            case MS_CM_NO_MATCH_CONFIRM_RETURNS_PLANS_TC_027 -> {
                payload.setCustomerType(GlobalEnums.CustomerType.COMMERCIAL.getValue());
                payload.setConfirmCreditCheck(true);
                payload.setCommercialCreditCheckBusinessBIN(null);
            }

            case MS_CM_VARIANT_TC_028, MS_CM_VARIANT_TC_029, MS_CM_VARIANT_TC_030,
                 MS_CM_VARIANT_TC_031, MS_CM_VARIANT_TC_032, MS_CM_VARIANT_TC_033,
                 MS_CM_VARIANT_TC_034 -> {
                payload.setCustomerType(GlobalEnums.CustomerType.COMMERCIAL.getValue());

            }

            case get_eligible_plans_and_offers, get_eligible_plans_and_offers_mandatory -> {
            }
        }
    }

    public void loadCustomerData(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel testCondition){
        Map<String, String> customerData = loadRowFromExcelToCustomerData(CUSTOMER_DATA, CUSTOMER_SHEET_NAME, testCondition);
        getCustomerAndPremiseDetails(payload, customerData, testCondition);
    }

    public void getCustomerAndPremiseDetails(GetEligiblePlansAndOffersRequest payload, Map<String, String> data, GetEligiblePlansAndOffersApiLabel testCondition ){
        payload.setCustomerLastName(data.get("customerLastName"));
        payload.setCustomerMiddleName(data.get("customerMiddleName"));
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

        switch (testCondition){
            case MS_RS_NO_MATCH_NO_CONFIRM_TC_011 -> {
                payload.setAglcAccountNumber(data.get("aclcAccountNumber"));

            }
            case MS_RS_DENIAL_DUE_TC_016 -> {
                payload.setAglcServiceLocationID(data.get("aglcServiceLocationID"));
                payload.setAglcAccountNumber(null);
            }

            case MS_GE_RS_INCL_TIER_5_TC_023, MS_RS_CRDS_ENROLLMENT_CREDIT_CHECK_TC_025 -> {
                payload.setAcnStatusIndicator(data.get("acnStatusIndicator"));
                payload.setAglcServiceLocationID(data.get("aglcServiceLocationID"));
                payload.setCustomerCode(null);
                payload.setPremisesCode(null);
                payload.setSspParticipantCode(null);
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
                //payload.setTransactionType(METER_SET.getValue());
                payload.setSeasonalSavingsProgramIndicator(false);
                payload.setMarketingPromotionCode(null);
                payload.setAuthorizedBy(null);
                payload.setAglcAccountNumber("");
                //payload.setTenantLandlord(GlobalEnums.TenantOrLandlord.LANDLORD.getValue());
            }
            case
                 MS_GE_RS_INCL_TIER_5_TC_023 -> {
                loadCustomerData(payload, testCondition);
                payload.setConfirmCreditCheck(Boolean.FALSE);
                payload.setCreditCheckOption(YES.getValue());
                payload.setTransactionType(METER_SET.getValue());
                payload.setSeasonalSavingsProgramIndicator(false);
                payload.setCustomerMiddleName("B"); // remove?
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
            case MS_RS_CRDS_ENROLLMENT_CREDIT_CHECK_TC_025 -> {
                loadCustomerData(payload, testCondition);
                payload.setConfirmCreditCheck(Boolean.FALSE);
                payload.setCreditCheckOption(YES.getValue());
                payload.setTransactionType(METER_SET.getValue());
            }
        }
    }


    public void setInvalidCombinationCustomerData(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel testCondition) {
        Map<String, Object> customerData;
        switch (testCondition) {
            case MS_RS_NO_RECORD_FOUND_TC_013,
                 MS_RS_VARIANT_TC_014,
                 MS_RS_DENIAL_DUE_TC_016,
                 MS_RS_CUSTOMER_NO_AUTH_TC_017,
                 MS_GE_TIER_9_NE_TC_020,
                 MS_GE_RS_COMM_VS_150_TC_021,
                 MS_GE_RS_ACN_LAND_BYPASS_CREDIT_TC_022,
                 MS_RS_CRDS_ENROLLMENT_CREDIT_CHECK_TC_025:
                 customerData = ApplicationContext.get().getDbAction().getCustomerInformationCreditScoreTextNoRecord();
                payload.setCustomerFirstName(customerData.get(UCBCUST_FIRST_NAME).toString());
                payload.setCustomerLastName(customerData.get(UCBCUST_LAST_NAME).toString());
                break;
        }
    }

    public void setSeedlingParamsFromSearchAccountsResponse(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel testCondition) {
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

        var acct = testContext.getSearchAccountsResponse().getData().getAccounts().getFirst();
        switch (testCondition){
               case MS_RS_NO_RECORD_FOUND_TC_013, MS_RS_CREDIT_FREEZE_TC_015 -> {
                   payload.setAglcServiceLocationID(acct.getAglcServiceLocationID());

               }
            default -> {}
        }


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
        var acct = testContext.getSearchAccountsResponse().getData().getAccounts().getFirst();
        Optional.ofNullable(testContext.getGetEligiblePlansAndOffersResponse())
                .map(GetEligiblePlansAndOffersResponse::getData)
                .map(DataResult::getAglcServiceLocationID)
                .ifPresent(payload::setAglcServiceLocationID);

        payload.setCustomerCode(searchResponseAcct.getCustomerCode());
        payload.setPremisesCode(searchResponseAcct.getPremisesCode());

        switch (testCondition) {
            case
                 MS_GE_RS_INCL_TIER_5_TC_023, MS_RS_CRDS_ENROLLMENT_CREDIT_CHECK_TC_025 -> {
                payload.setTransactionID(testContext.getGetEligiblePlansAndOffersResponse().getData().getTransactionID());

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

    public void verifyDisallowedPlansFor(GetEligiblePlansAndOffersApiLabel testCondition) {
        List<String> disallowed =
            switch (testCondition) {
            case MS_GE_TIER_1_NE_TC_018, MS_GE_TIER_2_NE_TC_019, MS_GE_TIER_9_NE_TC_020, MS_GE_RS_COMM_VS_150_TC_021,
                 MS_GE_RS_ACN_LAND_BYPASS_CREDIT_TC_022, MS_GE_RS_INCL_TIER_5_TC_023, MS_RS_MULTIPLE_PREM_TC_024 ->
                    List.of(GlobalEnums.PlanCode.GB6.getValue(), GlobalEnums.PlanCode.RGB.getValue(), GlobalEnums.PlanCode.PGB.getValue());

            case MS_RS_CRDS_ENROLLMENT_CREDIT_CHECK_TC_025 -> List.of(GlobalEnums.PlanCode.PGB.getValue());
            default -> Collections.emptyList();
        };
        verifyPlansDoNotContainCodes(disallowed);
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

    public void validateAllTheEntriesInTablesForEligiblePlansAndOffers(GetEligiblePlansAndOffersApiLabel testCondition){
        String customerCode = testContext.getGetEligiblePlansAndOffersResponse().getData().getCustomerCode();
        String premisesCode = testContext.getGetEligiblePlansAndOffersResponse().getData().getPremisesCode();
        String firstName= testContext.getGetEligiblePlansAndOffersResponse().getData().getCustomerFirstName();
        String lastName= testContext.getGetEligiblePlansAndOffersResponse().getData().getCustomerLastName();
        String zipCode= testContext.getGetEligiblePlansAndOffersResponse().getData().getPremisesZipCode();
        String aglcServiceLocationID= testContext.getGetEligiblePlansAndOffersResponse().getData().getAglcServiceLocationID();
        String streetNumber= testContext.getGetEligiblePlansAndOffersResponse().getData().getPremisesStreetNumber();
        String city= testContext.getGetEligiblePlansAndOffersResponse().getData().getPremisesCity();
        String state= testContext.getGetEligiblePlansAndOffersResponse().getData().getPremisesStateCode();
        Map<String, Object> enrollmentRecord= null;

//        if (testCondition = null) {
//
//            enrollmentRecord = ApplicationContext.get()
//                    .getDbAction()
//                    .validateAllTheTablesAfterGetEligiblePlansRequest(
//                            customerCode,
//                            premisesCode,
//                            firstName,
//                            lastName,
//                            zipCode,
//                            aglcServiceLocationID,
//                            streetNumber,
//                            city,
//                            state
//                    );
//        }

        Assert.assertEquals(enrollmentRecord.get("UZBENRO_CUST_CODE").toString(), customerCode);
    }



}
