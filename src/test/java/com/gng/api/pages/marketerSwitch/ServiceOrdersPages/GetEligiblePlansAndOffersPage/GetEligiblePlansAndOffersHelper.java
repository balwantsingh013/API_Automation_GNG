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
                 GE_MRK_SW_RS_NEW_CC_YES_NO_RECORD_CONFIRM_FALSE_11112_TC10, GE_MRK_SW_RS_NEW_CC_YES_UC47_TC15 -> {
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

            case GE_MRK_SW_RS_NEW_CC_COMM_CREDIT_OPTION_9998_ALL_PLANS_TC20 -> {
                loadCustomerData(payload, testCondition);
                payload.setCreditCheckOption(COMMERCIAL_CREDIT_CHECK_REQUIRED.getValue());
                payload.setConfirmCreditCheck(Boolean.FALSE);
                //payload.setAglcAccountNumber("");
            }

            case GE_MRK_SW_RS_NEW_CC_YES_ACN_LANDLORD_BYPASS_CREDIT_TC21 -> {
                loadCustomerData(payload, testCondition);
                payload.setCreditCheckOption(ACN_LANDLORD.getValue());
                payload.setTenantLandlord(GlobalEnums.TenantOrLandlord.LANDLORD.getValue());
                payload.setConfirmCreditCheck(Boolean.FALSE);
                payload.setTransactionID(null);
                payload.setCustomerCode(null);
                payload.setPremisesCode(null);
                payload.setAglcAccountNumber("");
                payload.setAuthorizedBy("");
            }

            case GE_MRK_SW_RS_NEW_CC_YES_UC65_TC22 -> {
                loadCustomerData(payload, testCondition);
                payload.setCreditCheckOption(YES.getValue());
                payload.setConfirmCreditCheck(Boolean.FALSE);

                 payload.setSeasonalSavingsProgramIndicator(true);
            }

            case GE_MRK_SW_RS_NEW_CC_YES_MULTI_PREMISES_MATCH_UC71_TC23 -> {
                loadCustomerData(payload, testCondition);
                payload.setCreditCheckOption(MULTIPLE_PREMISES_OWNER.getValue());
                payload.setConfirmCreditCheck(Boolean.FALSE);
                if (testContext.getGetEligiblePlansAndOffersResponse() != null
                        && testContext.getGetEligiblePlansAndOffersResponse().getData() != null) {
                    payload.setInitialCreditCheckCustomerCode(
                            testContext.getGetEligiblePlansAndOffersResponse()
                                    .getData()
                                    .getCustomerCode());
                }
            }

            case GE_MRK_SW_RS_CRDS_CC_YES_DEPOSIT_BILLED_VALUE110_TC24 -> {
                loadCustomerData(payload, testCondition);
                payload.setCreditCheckOption(YES.getValue());
                payload.setConfirmCreditCheck(Boolean.FALSE);
                payload.setEnrollmentState(GlobalEnums.EnrollMentState.CRDS.getValue());
                if (testContext.getGetEligiblePlansAndOffersResponse() != null
                        && testContext.getGetEligiblePlansAndOffersResponse().getData() != null) {
                    payload.setTransactionID(
                            testContext.getGetEligiblePlansAndOffersResponse()
                                    .getData()
                                    .getTransactionID());
                }
            }

            case GE_MRK_SW_CM_NEW_CC_YES_UC53_TC25, GE_MRK_SW_CM_NEW_CC_YES_BIN_NULL_NO_MATCH_CONTINUE_TC26 -> {
                setTheFieldToEmptyForCommercialScenarios(payload);
                loadCommercialData(payload, testCondition);
                payload.setCustomerType(COMMERCIAL.getValue());
                payload.setCreditCheckOption(YES.getValue());
                payload.setConfirmCreditCheck(Boolean.FALSE);
                payload.setCommercialCreditCheckBusinessBIN(null);
            }

            case GE_MRK_SW_CM_NEW_CC_YES_BIN_NOT_NULL_SELECT_SIMILAR_BUSINESS_TC27 -> {
                setTheFieldToEmptyForCommercialScenarios(payload);
                loadCommercialDataFromResponse(payload, testCondition);
                loadCommercialData(payload, testCondition);
                payload.setCustomerType(COMMERCIAL.getValue());
                payload.setCreditCheckOption(YES.getValue());
                payload.setConfirmCreditCheck(Boolean.TRUE);
            }

            case GE_MRK_SW_CM_NEW_CC_YES_TIER_EXCELLENT_VALUE200_CREDIT50_100_TC28 -> {
                loadCommercialData(payload, testCondition);
                payload.setCustomerType(COMMERCIAL.getValue());
                payload.setCreditCheckOption(YES.getValue());
                payload.setConfirmCreditCheck(Boolean.FALSE);
                payload.setMarketingPromotionCode("");
            }

            case GE_MRK_SW_CM_NEW_CC_YES_COMM_DEPOSIT_PROSPECT_UC72_TC29 -> {
                loadCommercialData(payload, testCondition);
                payload.setCustomerType(COMMERCIAL.getValue());
                payload.setCreditCheckOption(YES.getValue());
                payload.setConfirmCreditCheck(Boolean.FALSE);
            }

            case GE_MRK_SW_CM_CRDS_CC_YES_COMM_DEPOSIT_PROSPECT_UC73_TC30,
                 GE_MRK_SW_CM_CRDS_CC_YES_COMM_DEPOSIT_PROSPECT_UC72_TC31 -> {
                setTheFieldToEmptyForCommercialScenarios(payload);
                loadCommercialData(payload, testCondition);
                payload.setCustomerType(COMMERCIAL.getValue());
                payload.setEnrollmentState(GlobalEnums.EnrollMentState.CRDS.getValue());
                payload.setCreditCheckOption(YES.getValue());
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

            case get_eligible_plans_and_offers,
                 get_eligible_plans_and_offers_mandatory -> {
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
            case GE_MRK_SW_CM_NEW_CC_YES_BIN_NOT_NULL_SELECT_SIMILAR_BUSINESS_TC27 -> {
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
            default -> {
            }
        }
    }

    public void loadCommercialData(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel testCondition) {
        Map<String, String> customerData =
                loadRowFromExcelToCustomerData(CUSTOMER_DATA, CUSTOMER_SHEET_NAME, testCondition);
        getCommercialCustomerAndPremiseDetails(payload, customerData, testCondition);
    }

    private void getCommercialCustomerAndPremiseDetails(GetEligiblePlansAndOffersRequest payload, Map<String, String> data, GetEligiblePlansAndOffersApiLabel testCondition) {

        String federalTaxId = data.get("federalTaxId");
        if (federalTaxId != null && !federalTaxId.trim().isEmpty()) {
            payload.setFederalTaxID(encryptData(federalTaxId));
        }

        switch (testCondition) {

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

    public void loadCustomerData(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel testCondition) {
        Map<String, String> customerData = loadRowFromExcelToCustomerData(CUSTOMER_DATA, CUSTOMER_SHEET_NAME, testCondition);
        getCustomerAndPremiseDetails(payload, customerData, testCondition);
    }

    public void getCustomerAndPremiseDetails(GetEligiblePlansAndOffersRequest payload, Map<String, String> data, GetEligiblePlansAndOffersApiLabel testCondition) {
        clearIds(payload);
        payload.setAcnStatusIndicator(get(data, "acnStatusIndicator"));
        payload.setAglcAccountNumber(get(data, "aglcAccountNumber"));
        payload.setAglcServiceLocationID(get(data, "aglcServiceLocationID"));
        payload.setPremisesCountyCode(get(data, "premisesCountyCode"));
        payload.setCustomerMiddleName(get(data, "customerMiddleName"));

        applyPersonNames(payload, data);
        applyAddress(payload, data);

        switch (testCondition) {
            case GE_MRK_SW_RS_NEW_CC_YES_INVALID_SSN_FRAUD_ALERT_11114_TC09,
                 GE_MRK_SW_RS_NEW_CC_YES_NO_RECORD_CONFIRM_FALSE_11112_TC10 -> payload.setAglcAccountNumber(FakerDataGenerator.getRandomNumericString(9));
            case GE_MRK_SW_RS_NEW_CC_YES_UC50_ALT_PATH_TC12, GE_MRK_SW_RS_NEW_CC_YES_UC52_TC13 -> {
                payload.setAuthorizedBy("");
                payload.setGenerationCode(null);
                payload.setPremisesUnitType(null);
                payload.setPremisesUnitNumber(null);
                payload.setPremisesStreetPreDirection(null);
                payload.setCallerID(null);
                payload.setEmailAddress(null);
                payload.setAdditionalEnrollmentData(null);
                payload.setAglcAccountNumber(FakerDataGenerator.getRandomNumericString(9));
            }
            default -> {
                payload.setAglcAccountNumber(FakerDataGenerator.getRandomNumericString(9));
            }
        }

        encryptAndSetSSN(payload, data.get("SSN"));
        encryptAndSetFedTaxId(payload, data.get("federalTaxId"));
    }

    private static String get(Map<String, String> data, String key) {
        return Optional.ofNullable(data.get(key)).orElse("");
    }

    private static void clearIds(GetEligiblePlansAndOffersRequest payload) {
        payload.setCustomerCode(null);
        payload.setPremisesCode(null);
    }

    private static void applyPersonNames(GetEligiblePlansAndOffersRequest payload,  Map<String, String> data) {
        payload.setCustomerLastName(get(data, "customerLastName"));
        payload.setCustomerFirstName(get(data, "customerFirstName"));
        payload.setGenerationCode(get(data, "generationCode"));
    }

    private static void applyAddress(GetEligiblePlansAndOffersRequest payload,  Map<String, String> data) {
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
        List<Map<String, Object>> eligiblePlansList = ApplicationContext.get().getDbAction().getValidationPlansAndOffers(controlNum);
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
                Assert.assertEquals(apiPlanDescription, dbPlanDescription,
                        "Plan description mismatch for planCode: " + dbPlanCode);
                Assert.assertEquals(apiPromo1Code, dbPromo1Code,
                        "Promotion1 code mismatch for planCode: " + dbPlanCode);
                Assert.assertEquals(apiPromo1Desc, dbPromo1Desc,
                        "Promotion1 description mismatch for planCode: " + dbPlanCode);

                break;
            }
        }

        Assert.assertTrue(matchFound,"No matching planCode found in API response for: " + dbPlanCode);
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

        List<String> disallowed = switch (testCondition) {

            case GE_MRK_SW_RS_NEW_CC_YES_TIER1_VALUE100_CREDIT700_999_SSP_FALSE_TC17,
                 GE_MRK_SW_RS_NEW_CC_YES_TIER2_VALUE101_CREDIT600_699_SSP_FALSE_TC18->
                List.of(GlobalEnums.PlanCode.GB6.getValue(),
                        GlobalEnums.PlanCode.CGB.getValue(),
                        GlobalEnums.PlanCode.CCV.getValue(),
                        GlobalEnums.PlanCode.CVS.getValue()
                );

            case GE_MRK_SW_RS_NEW_CC_YES_TIER_UC45_TC19 ->
                    allExcept.apply(List.of(
                            GlobalEnums.PlanCode.VML.getValue(),
                            GlobalEnums.PlanCode.PGB.getValue(),
                            GlobalEnums.PlanCode.PRP.getValue()
                    ));

            case GE_MRK_SW_RS_NEW_CC_COMM_CREDIT_OPTION_9998_ALL_PLANS_TC20 ->
                    allExcept.apply(List.of(
                            GlobalEnums.PlanCode.RGB.getValue(),
                            GlobalEnums.PlanCode.GPP.getValue(),
                            GlobalEnums.PlanCode.TWENTY_FOUR_M.getValue(),
                            GlobalEnums.PlanCode.EIGHTEEN_M.getValue(),
                            GlobalEnums.PlanCode.RF6.getValue(),
                            GlobalEnums.PlanCode.MVS.getValue(),
                            GlobalEnums.PlanCode.CSV.getValue(),
                            GlobalEnums.PlanCode.MI.getValue(),
                            GlobalEnums.PlanCode.TRD.getValue(),
                            GlobalEnums.PlanCode.PGB.getValue(),
                            GlobalEnums.PlanCode.PRP.getValue()
                    ));

            case GE_MRK_SW_RS_NEW_CC_YES_ACN_LANDLORD_BYPASS_CREDIT_TC21,
                 GE_MRK_SW_RS_NEW_CC_YES_UC65_TC22,
                 GE_MRK_SW_RS_NEW_CC_YES_MULTI_PREMISES_MATCH_UC71_TC23
                    -> List.of(
                    GlobalEnums.PlanCode.GB6.getValue(),
                    GlobalEnums.PlanCode.RGB.getValue(),
                    GlobalEnums.PlanCode.PGB.getValue()
            );

            case GE_MRK_SW_RS_CRDS_CC_YES_DEPOSIT_BILLED_VALUE110_TC24 ->
                    List.of(GlobalEnums.PlanCode.PGB.getValue());

            case GE_MRK_SW_CM_NEW_CC_YES_BIN_NOT_NULL_SELECT_SIMILAR_BUSINESS_TC27,
                 GE_MRK_SW_CM_NEW_CC_YES_TIER_EXCELLENT_VALUE200_CREDIT50_100_TC28,
                 GE_MRK_SW_CM_NEW_CC_YES_COMM_DEPOSIT_PROSPECT_UC72_TC29,
                 GE_MRK_SW_CM_CRDS_CC_YES_COMM_DEPOSIT_PROSPECT_UC73_TC30
                    -> List.of(GlobalEnums.PlanCode.CGB.getValue());

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
                expectedEnrollmentStateEnum = GlobalEnums.EnrollMentState.PENDINGREVIEW;
                expectedCreditScoreStatus = GlobalEnums.CreditScoreStatus.STATUS_TEXT;
                expectedCreditScoreTextEnum = GlobalEnums.CreditScoreText.VERIFY_ID;
                //enrollmentSupplier = this::getEnrollmentRecordFromSearchAccountsResponse;
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

                //enrollmentSupplier = () -> getEnrollmentRecordFromCustomerLastName(testCondition);
            }

            case GE_MRK_SW_RS_NEW_CC_COMM_CREDIT_OPTION_9998_ALL_PLANS_TC20 -> {
                expectedEnrollmentStateEnum = GlobalEnums.EnrollMentState.INCL;
                expectedCreditScoreStatus = GlobalEnums.CreditScoreStatus.NUMBER;
                enrollmentSupplier = this::getEnrollmentRecordFromGetEligiblePlansAndOffersResponse;

            }

            default -> { return; }
        }

        Map<String, Object> enrollmentRecord = enrollmentSupplier.get();

        String expectedEnrollmentState = expectedEnrollmentStateEnum.getValue();
        String expectedStatus = expectedCreditScoreStatus == null ? null : expectedCreditScoreStatus.getValue();
        String expectedScoreText = expectedCreditScoreTextEnum == null ? null : expectedCreditScoreTextEnum.getValue();

        String actualStatus = enrollmentRecord.get("UZBENRO_CRED_SCORE_STATUS") == null
                ? null
                : enrollmentRecord.get("UZBENRO_CRED_SCORE_STATUS").toString();

        String actualScoreText = enrollmentRecord.get("UZBENRO_CRED_SCORE_TEXT") == null
                ? null
                : enrollmentRecord.get("UZBENRO_CRED_SCORE_TEXT").toString();

        String actualEnrollmentStatus = enrollmentRecord.get("UZBENRO_ENRO_STATUS") == null
                ? null
                : enrollmentRecord.get("UZBENRO_ENRO_STATUS").toString();

        // Always assert enrollment state
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
            // optional: tighten this if you expect exactly one
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

    private static String normalize(Object value) {
        return value == null ? "" : value.toString().trim();
    }
}
