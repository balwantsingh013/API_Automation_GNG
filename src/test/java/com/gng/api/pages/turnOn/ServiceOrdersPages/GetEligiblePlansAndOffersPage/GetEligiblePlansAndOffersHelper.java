package com.gng.api.pages.turnOn.ServiceOrdersPages.GetEligiblePlansAndOffersPage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.AccountsPojo.SearchAccounts.Account;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.request.GetEligiblePlansAndOffersRequest;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.response.GetEligiblePlansAndOffersResponse;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.response.Plans;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.turnOn.ServiceOrdersSteps.GetEligiblePlansAndOffers.GetEligiblePlansAndOffersApiLabel;
import com.gng.api.util.ExcelReader;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import static com.gng.api.constants.GlobalEnums.CreditCheckOption.*;
import static com.gng.api.constants.GlobalEnums.CustomerType.COMMERCIAL;
import static com.gng.api.constants.GlobalEnums.CustomerType.RESIDENTIAL;
import static com.gng.api.constants.GlobalEnums.EnrollmentSource.*;
import static com.gng.api.constants.GlobalEnums.PromotionCode.DEALS;
import static com.gng.api.constants.GlobalEnums.TransactionType.TURN_ON;
import static com.gng.api.constants.TestConstant.*;
import static com.gng.api.steps.AesEncryption.AesEncryptionSteps.encryptData;
import static com.gng.api.util.CommonUtil.nullifyFields;

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


    public void setTransactionTypeBasedOnType(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel transactionType) {
        switch (transactionType) {
            case EMPTY_TRANSACTION_TYPE:
                payload.setTransactionType("");
                break;
            case NUMERIC_TRANSACTION_TYPE:
                payload.setTransactionType(FakerDataGenerator.generateDigits(10));
                break;
            case UPPERCASE_TRANSACTION_TYPE:
                payload.setTransactionType(FakerDataGenerator.generateUpperCaseString(36));
                break;
            case LOWERCASE_TRANSACTION_TYPE:
                payload.setTransactionType(FakerDataGenerator.generateLowerCaseString(4));
                break;
            case WHITESPACE_CONTAINS_TRANSACTION_TYPE:
                payload.setTransactionType("DF HJ");
                break;
            case ALPHANUMERIC_TRANSACTION_TYPE:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setTransactionType(FakerDataGenerator.generateAlphanumeric(2));
                break;
            default:
                payload.setTransactionType(FakerDataGenerator.generateUpperCaseString(4));
        }
    }

    public void setCustomerTypeBasedOnType(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel customerTYPE) {
        switch (customerTYPE) {
            case EMPTY_CUSTOMER_TYPE:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setCustomerType("");
                break;
            case MIN_LENGTH_CUSTOMER_TYPE:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setCustomerType(FakerDataGenerator.getRandomString(3));
                break;
            case SPL_CHAR_CUSTOMER_TYPE:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setCustomerType(FakerDataGenerator.generateAlphanumericWithSpecialChars(3));
                break;
            case MAX_LENGTH_CUSTOMER_TYPE:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setCustomerType(FakerDataGenerator.getRandomString(5));
                break;

            default:
                payload.setCustomerType(FakerDataGenerator.getRandomString(2));
        }
    }

    public void setEnrollmentSourcesBasedOnType(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel enrollmentSources) {
        switch (enrollmentSources) {
            case SPL_CHAR_ENROLLMENT_SOURCES:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setEnrollmentSource(FakerDataGenerator.generateAlphanumericWithSpecialChars(7));
                break;
            case EMPTY_ENROLLMENT_SOURCES:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setEnrollmentSource("");
                break;
            case NUMERIC_ENROLLMENT_SOURCES:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setEnrollmentSource(FakerDataGenerator.getRandomNumericString(5));
                break;
            case MAX_LENGTH_ENROLLMENT_SOURCES:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setEnrollmentSource(FakerDataGenerator.getRandomNumericString(38));
                break;

            default:
                payload.setEnrollmentSource(FakerDataGenerator.generateUpperCaseString(35));
        }

    }

    private void parseAddress(GetEligiblePlansAndOffersRequest payload, String fullAddress) {
        if (fullAddress == null || fullAddress.isEmpty()) return;

        String[] parts = fullAddress.trim().split("\\s+");

        String streetNumber = (parts.length >= 1) ? parts[0] : "";
        String streetSuffix = "";
        String postDirection = "";
        String streetName = "";
        String premisesUnitType = "";
        String premisesUnitNumber = "";

        List<String> unitTypes = Arrays.asList("STE", "SUITE", "APT", "UNIT", "FL", "RM");
        List<String> directions = Arrays.asList("N", "S", "E", "W", "NE", "NW", "SE", "SW");

        int unitIndex = -1;
        for (int i = 0; i < parts.length; i++) {
            if (unitTypes.contains(parts[i].toUpperCase())) {
                unitIndex = i;
                break;
            }
        }

        int suffixStart = (unitIndex == -1) ? parts.length : unitIndex;

        // Capture directional suffix (e.g., BLVD SE)
        if (suffixStart >= 3) {
            streetSuffix = parts[suffixStart - 2];
            postDirection = parts[suffixStart - 1];

            if (!directions.contains(postDirection.toUpperCase())) {
                streetSuffix = parts[suffixStart - 1];
                postDirection = "";
            }

            streetName = String.join(" ", Arrays.copyOfRange(parts, 1, suffixStart - (postDirection.isEmpty() ? 1 : 2)));
        } else if (suffixStart == 3) {
            streetName = parts[1];
            streetSuffix = parts[2];
        } else if (suffixStart == 2) {
            streetName = parts[1];
        }

        if (unitIndex != -1 && unitIndex + 1 < parts.length) {
            premisesUnitType = parts[unitIndex];
            premisesUnitNumber = String.join(" ", Arrays.copyOfRange(parts, unitIndex + 1, parts.length));
        }

        payload.setPremisesStreetNumber(streetNumber);
        payload.setPremisesStreetName(streetName);
        payload.setPremisesStreetSuffix(streetSuffix);
        payload.setPremisesStreetPostDirection(postDirection);
        payload.setPremisesUnitType(premisesUnitType);
        payload.setPremisesUnitNumber(premisesUnitNumber);
    }


    private void populateCommonFields(GetEligiblePlansAndOffersRequest payload, Map<String, String> data) {
        parseAddress(payload, data.getOrDefault("BUSINESS STREET ADDRESS", ""));

        payload.setPremisesCity(data.getOrDefault("BUSINESS CITY", ""));
        payload.setPremisesStateCode(data.getOrDefault("BUSINESS STATE", ""));
        payload.setPremisesZipCode(data.getOrDefault("BUSINESS ZIP", ""));
        payload.setCustomerBusinessName(data.get("BUSINESS NAME"));
        payload.setFederalTaxID(encryptData(data.get("TAX-ID")));
    }


    private void parseBillingAddress(GetEligiblePlansAndOffersRequest payload, String billingAddress) {
        if (billingAddress == null || billingAddress.isEmpty()) return;

        String[] parts = billingAddress.trim().split("\\s+");

        String addressType   = (parts.length >= 1) ? parts[0] : "";
        String streetNumber  = (parts.length >= 2) ? parts[1] : "";
        String streetName    = (parts.length >= 3) ? parts[2] : "";
        String streetSuffix  = (parts.length >= 4) ? parts[3] : "";

        payload.setBillingAddressType(addressType);
        payload.setPremisesStreetNumber(streetNumber);
        payload.setPremisesStreetName(streetName);
        payload.setPremisesStreetSuffix(streetSuffix);
    }

    private void parsePhoneDetails(GetEligiblePlansAndOffersRequest payload, String phoneDetails) {
        if (phoneDetails == null || phoneDetails.isEmpty()) return;

        String[] parts = phoneDetails.trim().split("\\s+");

        String phoneType     = (parts.length >= 1) ? parts[0] : "";
        String phoneExt      = (parts.length >= 2) ? parts[1] : "";
        String phoneNumber   = (parts.length >= 3) ? parts[2] : "";

        payload.setWorkPhoneType(phoneType);
        payload.setWorkPhoneExtension(phoneExt);
        payload.setWorkPhoneNumber(phoneNumber);
    }

    private void parseAddressWithUnit(GetEligiblePlansAndOffersRequest payload, String fullAddress) {
        if (fullAddress == null || fullAddress.isEmpty()) return;

        String[] parts = fullAddress.trim().split("\\s+");

        String streetNumber = (parts.length >= 1) ? parts[0] : "";
        String streetName = (parts.length >= 2) ? parts[1] : "";
        String streetSuffix = (parts.length >= 3) ? parts[2] : "";

        String postDirection = (parts.length >= 4 && parts[3].matches("^(N|S|E|W|NE|NW|SE|SW)$")) ? parts[3] : "";

        String premisesUnitType = (parts.length >= 5 && !postDirection.isEmpty()) ? parts[4] :
                (parts.length >= 4 && postDirection.isEmpty()) ? parts[3] : "";

        String premisesUnitNumber = (parts.length >= 6 && !postDirection.isEmpty()) ? parts[5] :
                (parts.length >= 5 && postDirection.isEmpty()) ? parts[4] : "";

        payload.setPremisesStreetNumber(streetNumber);
        payload.setPremisesStreetName(streetName);
        payload.setPremisesStreetSuffix(streetSuffix);
        payload.setPremisesStreetPostDirection(postDirection);
        payload.setPremisesUnitType(premisesUnitType);
        payload.setPremisesUnitNumber(premisesUnitNumber);
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

    public void payloadBasedOnTCsCommercial(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel testCondition) {
        setTheFieldToEmptyForCommercialScenarios(payload);
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setTransactionType(TURN_ON.getValue());
        payload.setCustomerType(COMMERCIAL.getValue());
        payload.setAglcServiceLocationID(FakerDataGenerator.generateDigits(9));
        ExcelReader excelReader;
        try {
            excelReader = new ExcelReader(EXPERIAN_DATA);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Map<String, String>> allRows = excelReader.getSheetData(EXPERIAN_SHEET_NAME);
        Map<String, String> data = null;

        switch (testCondition) {
            case COMMERCIAL_CREDIT_CHECK_YES_TC_339:
                data = allRows.get(5107);
                parseAddress(payload, data.getOrDefault("BUSINESS STREET ADDRESS", ""));
                populateCommonFields(payload, data);
                break;

            case COMMERCIAL_CREDIT_CHECK_YES_NEW_ENROLLMENT_TC_340:
                data = allRows.get(4399);
                parseAddress(payload, data.getOrDefault("BUSINESS STREET ADDRESS", ""));
                populateCommonFields(payload, data);
                payload.setEnrollmentSource(FAX.getValue());
                break;

            case COMMERCIAL_CREDIT_CHECK_SKIP_NEW_ENROLLMENT_TC_341:
                data = allRows.get(4638);
                populateCommonFields(payload, data);
                payload.setEnrollmentSource(WEB.getValue());
                payload.setCreditCheckOption(CREDIT_CHECK_NOT_REQUIRED.getValue());
                break;

            case COMMERCIAL_CREDIT_CHECK_YES_NEW_ENROLLMENT_TC_342:
                data = allRows.get(9612);
                populateCommonFields(payload, data);
                payload.setEmailAddress(FakerDataGenerator.generateEmail());
                break;

            case COMMERCIAL_CREDIT_CHECK_YES_NEW_ENROLLMENT_TC_343:
                data = allRows.get(4766);
                parseAddress(payload, data.getOrDefault("BUSINESS STREET ADDRESS", ""));
                populateCommonFields(payload, data);
                payload.setEnrollmentSource(FAX.getValue());
                parseBillingAddress(payload, data.getOrDefault("BILLING ADDRESS", ""));
                parsePhoneDetails(payload, data.getOrDefault("PHONE NUMBER DETAILS", ""));
                payload.setSeasonalSavingsProgramIndicator(true);
                payload.setBillingCity(data.get("BILLING CITY"));
                payload.setBillingStateCode(data.get("BILLING STATE"));
                payload.setBillingZipCode(data.get("BILLING ZIP"));
                payload.setBillingCountyCode(data.get("BILLING COUNTRY CODE"));
                break;

            case COMMERCIAL_CREDIT_CHECK_SERV_TRANSFER_NEW_ENROLLMENT_TC_344:
                data = allRows.get(4613);
                parseAddress(payload, data.getOrDefault("BUSINESS STREET ADDRESS", ""));
                populateCommonFields(payload, data);
                payload.setEnrollmentSource(GNGHUB.getValue());
                payload.setCreditCheckOption(SERVICE_TRANSFER.getValue());
                break;

            case COMMERCIAL_CREDIT_CHECK_YES_NEW_ENROLLMENT_TC_346:
                data = allRows.get(4861);
                parseAddress(payload, data.getOrDefault("BUSINESS STREET ADDRESS", ""));
                populateCommonFields(payload, data);
                payload.setCreditCheckOption(NO.getValue());
                break;

            case COMMERCIAL_CREDIT_CHECK_MULT_NEW_ENROLLMENT_TC_345:
                data = allRows.get(31);
                parseAddress(payload, data.getOrDefault("BUSINESS STREET ADDRESS", ""));
                populateCommonFields(payload, data);
                payload.setCreditCheckOption(MULTIPLE_PREMISES_OWNER.getValue());
                break;

            case COMMERCIAL_CREDIT_CHECK_YES_NEW_ENROLLMENT_TC_347:
                data = allRows.get(1064);
                parseAddress(payload, data.getOrDefault("BUSINESS STREET ADDRESS", ""));
                populateCommonFields(payload, data);
                break;

            case COMMERCIAL_CREDIT_CHECK_YES_NEW_ENROLLMENT_TC_348:
                data = allRows.get(2508);
                payload.setMarketingPromotionCode(DEALS.getValue());
                parseAddressWithUnit(payload, data.getOrDefault("BUSINESS STREET ADDRESS", ""));
                populateCommonFields(payload, data);
                payload.setEmailAddress(FakerDataGenerator.generateEmail());
                break;

            case COMMERCIAL_CREDIT_CHECK_YES_NEW_ENROLLMENT_TC_349:
                data = allRows.get(4499);
                parseAddressWithUnit(payload, data.getOrDefault("BUSINESS STREET ADDRESS", ""));
                populateCommonFields(payload, data);
                payload.setEmailAddress(FakerDataGenerator.generateEmail());
                break;

            case COMMERCIAL_CREDIT_CHECK_YES_INCL_ENROLLMENT_TC_350:
                data = allRows.get(4695);
                parseAddress(payload, data.getOrDefault("BUSINESS STREET ADDRESS", ""));
                populateCommonFields(payload, data);
                break;

            case COMMERCIAL_CREDIT_CHECK_YES_CRDS_ENROLLMENT_TC_350B:
                data = allRows.get(4459);
                parseAddress(payload, data.getOrDefault("BUSINESS STREET ADDRESS", ""));
                populateCommonFields(payload, data);
                payload.setEnrollmentSource(FAX.getValue());
                break;

            case COMMERCIAL_CREDIT_CHECK_YES_CRDS_ENROLLMENT_TC_350E:
                data = allRows.get(5093);
                parseAddress(payload, data.getOrDefault("BUSINESS STREET ADDRESS", ""));
                populateCommonFields(payload, data);
                payload.setCreditCheckBusinessName(data.get("BUSINESS NAME"));
                break;
        }
    }

    public void setCustomerLastNameBasedOnType(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel customerLastName) {
        switch (customerLastName) {
//            case SPL_CHAR_CUSTOMER_LAST_NAME:
//                payload.setRequestID(FakerDataGenerator.generateString(10));
//                payload.setCustomerLastName(FakerDataGenerator.generateAlphanumericWithSpecialChars(2));
//                break;
            case EMPTY_CUSTOMER_LAST_NAME:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setCustomerLastName("");
                break;
//            case LOWERCASE_CUSTOMER_LAST_NAME:
//                payload.setRequestID(FakerDataGenerator.generateString(10));
//                payload.setCustomerLastName(FakerDataGenerator.generateLowerCaseString(2));
//                break;
//            case MAX_LENGTH_CUSTOMER_LAST_NAME:
//                payload.setRequestID(FakerDataGenerator.generateString(10));
//                payload.setCustomerLastName(FakerDataGenerator.getRandomString(10));
//                break;
//            case NUMERIC_CUSTOMER_LAST_NAME:
//                payload.setRequestID(FakerDataGenerator.generateString(10));
//                payload.setCustomerLastName(FakerDataGenerator.getRandomNumericString(5));
//                break;

            default:
                payload.setCustomerLastName(FakerDataGenerator.generateUpperCaseString(2));
        }

    }


    public void setCreditCheckOptionBasedOnTypeTC310_312(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel creditCheckOption) {
        switch (creditCheckOption) {
            case EMPTY_CREDIT_CHECK_OPTION:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setCreditCheckOption("");
                break;
            case INVALID_CREDIT_CHECK_OPTION:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setCreditCheckOption("MAYBE");
                break;
            case MAX_LENGTH_CREDIT_CHECK_OPTION:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setCreditCheckOption(FakerDataGenerator.getRandomString(33));
                break;
            default:
                payload.setCreditCheckOption(FakerDataGenerator.getRandomString(32));
        }
    }

    public void setInitialCreditCheckCustomerCodeBasedOnTypeTC313_317(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel initialCreditCheckCustomerCode) {
        switch (initialCreditCheckCustomerCode) {
            case EMPTY_INITIAL_CREDIT_CHECK_CUSTOMER_CODE_WITH_CREDIT_CHECK_OPTION_315:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setCreditCheckOption("MULT");
                payload.setInitialCreditCheckCustomerCode(null);
                break;
            case MAX_LENGTH_INITIAL_CREDIT_CHECK_CUSTOMER_CODE_313:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setInitialCreditCheckCustomerCode(FakerDataGenerator.getRandomNumericString(10));
                break;
            case NONNUMERIC_INITIAL_CREDIT_CHECK_CUSTOMER_CODE_314:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setCreditCheckOption("Yes");
                payload.setInitialCreditCheckCustomerCode("ABCDEFG");
                break;
            case INVALID_INITIAL_CREDIT_CHECK_CUSTOMER_CODE_NOT_PRESENT_IN_TABLE_316:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setCreditCheckOption("MULT");
                payload.setInitialCreditCheckCustomerCode("1234588");
                break;
            case INVALID_INITIAL_CREDIT_CHECK_CUSTOMER_CODE_WITHOUT_CREDIT_SCORE_317:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setCustomerLastName("BANKER");
                payload.setCreditCheckOption("MULT");
                payload.setGenerationCode(null);
                payload.setCustomerFirstName("ROBERT");
                payload.setSocialSecurityNumber("lduHv2sf3IiZv3cL6Lh7G4/uhyw8Fu6tKHdn12qCxyM=");
                payload.setInitialCreditCheckCustomerCode("5908691");
            default:

                payload.setInitialCreditCheckCustomerCode(FakerDataGenerator.getRandomString(8));
        }
    }


    public void setTenantLandlordBasedOnType(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel tenantLandlord) {
        switch (tenantLandlord) {
            case EMPTY_TENANT_LANDLORD:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setTenantLandlord(" ");
            case SPL_CHAR_TENANT_LANDLORD:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setTenantLandlord(FakerDataGenerator.generateAlphanumericWithSpecialChars(5));
                break;
            case MAX_LENGTH_TENANT_LANDLORD:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setTenantLandlord(FakerDataGenerator.getRandomString(4));
                break;
            case LOWERCASE_TENANT_LANDLORD:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setTenantLandlord(FakerDataGenerator.generateAlphanumeric(3));
                break;
            case NUMERIC_TENANT_LANDLORD:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setTenantLandlord(FakerDataGenerator.generateAlphanumeric(3));
                break;

            default:
                payload.setTenantLandlord(FakerDataGenerator.getRandomString(1));
        }
    }

    public void setSeparateBillingAddressBasedOnType258_283b(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel separateBillingAddress) {
        switch (separateBillingAddress) {
            case EMPTY_SEPARATE_BILLING_ADDRESS:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress("");
                break;
            case MIN_LENGTH_SEPARATE_BILLING_ADDRESS:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(FakerDataGenerator.generateLowerCaseString(1));
                payload.setBillingStreetPreDirection(null);
                break;
            case SEPARATE_BILLING_ADDRESS_BILLING_ADD_TYPE_PROVIDED:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType(FakerDataGenerator.generateUpperCaseString(5));
                break;
            case SEPARATE_BILLING_ADDRESS_BILLING_ADD_TYPE_MISSING:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType(null);
                break;
            case SEPARATE_BILLING_ADDRESS_WITH_INVALID_BILLING_ADD_TYPE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType("C");
                break;
            case INVALID_SEPARATE_BILLING_ADDRESS_WITH_VALID_BILLING_ADD_TYPE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType("S");
                payload.setBillingStreetName(FakerDataGenerator.generateUpperCaseString(31));
                payload.setBillingCity("ATLANTA");
                payload.setBillingStateCode("GA");
                payload.setBillingZipCode("30542");
                payload.setBillingCountyCode("T207");
                break;
            case VALID_BILLING_ADDRESS_TYPE_WITH_INVALID_BILLING_STREET_NUMBER:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType("S");
                payload.setBillingStreetName("TREE-PARK");
                payload.setBillingCity("ATLANTA");
                payload.setBillingStateCode("GA");
                payload.setBillingZipCode("30542");
                payload.setBillingCountyCode("T207");
                payload.setBillingStreetNumber(FakerDataGenerator.getRandomNumericString(13));
                break;
            case VALID_BILLING_ADDRESS_TYPE_WITH_MAX_LENGTH_BILLING_STREET_PRE_DIRECTION:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType("S");
                payload.setBillingStreetName("TREE-PARK");
                payload.setBillingCity("ATLANTA");
                payload.setBillingStateCode("GA");
                payload.setBillingZipCode("30542");
                payload.setBillingCountyCode("T207");
                payload.setBillingStreetPreDirection("SOUTH");
                payload.setBillingStreetNumber("123456789123");
                break;
            case VALID_BILLING_ADDRESS_TYPE_WITH_INVALID_BILLING_STREET_PRE_DIRECTION:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType("S");
                payload.setBillingStreetName("TREE-PARK");
                payload.setBillingCity("ATLANTA");
                payload.setBillingStateCode("GA");
                payload.setBillingZipCode("30542");
                payload.setBillingCountyCode("T207");
                payload.setBillingStreetPreDirection("EN");
                payload.setBillingStreetNumber("123456789123");
                break;
            case VALID_BILLING_ADDRESS_TYPE_WITH_MAX_LENGTH_BILLING_STREET_SUFFIX:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType("S");
                payload.setBillingStreetName("TREE-PARK");
                payload.setBillingCity("ATLANTA");
                payload.setBillingStateCode("GA");
                payload.setBillingZipCode("30542");
                payload.setBillingCountyCode("T207");
                payload.setBillingStreetPreDirection("NE");
                payload.setWorkPhoneNumber(null);
                payload.setBillingStreetSuffix(FakerDataGenerator.generateUpperCaseString(7));
                payload.setBillingStreetNumber(FakerDataGenerator.getRandomNumericString(12));
                break;
            case VALID_BILLING_ADDRESS_TYPE_WITH_INVALID_BILLING_STREET_SUFFIX:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType("S");
                payload.setBillingStreetName("TREE-PARK");
                payload.setBillingCity("ATLANTA");
                payload.setBillingStateCode("GA");
                payload.setBillingZipCode("30542");
                payload.setBillingCountyCode("T207");
                payload.setBillingStreetPreDirection("NE");
                payload.setBillingStreetSuffix(FakerDataGenerator.generateUpperCaseString(3));
                payload.setBillingStreetNumber(FakerDataGenerator.getRandomNumericString(12));
                break;
            case VALID_BILLING_ADDRESS_TYPE_WITH_MAX_LENGTH_BILLING_STREET_POST_DIRECTION:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType("S");
                payload.setBillingStreetName("TREE-PARK");
                payload.setBillingCity("FLOWERY BRANCH");
                payload.setBillingStateCode("GA");
                payload.setBillingZipCode("30542");
                payload.setBillingCountyCode("T207");
                payload.setBillingStreetPreDirection(null);
                payload.setBillingStreetSuffix("CIR");
                payload.setBillingStreetPostDirection(FakerDataGenerator.generateUpperCaseString(3));
                payload.setBillingStreetNumber(FakerDataGenerator.getRandomNumericString(12));
                break;
            case VALID_BILLING_ADDRESS_TYPE_WITH_INVALID_BILLING_STREET_POST_DIRECTION:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType("S");
                payload.setBillingStreetName("TREE-PARK");
                payload.setBillingCity("ATLANTA");
                payload.setBillingStateCode("GA");
                payload.setBillingZipCode("30542");
                payload.setBillingCountyCode("T207");
                payload.setBillingStreetPreDirection(null);
                payload.setWorkPhoneNumber(null);
                payload.setBillingStreetSuffix("CIR");
                payload.setBillingStreetPostDirection("EN");
                payload.setBillingStreetNumber(FakerDataGenerator.getRandomNumericString(12));
                break;
            case VALID_BILLING_ADDRESS_TYPE_WITH_MAX_LENGTH_BILLING_UNIT_TYPE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType("S");
                payload.setBillingStreetName("TREE-PARK");
                payload.setBillingCity("ATLANTA");
                payload.setBillingStateCode("GA");
                payload.setBillingZipCode("30542");
                payload.setBillingCountyCode("T207");
                payload.setBillingStreetPreDirection("NE");
                payload.setWorkPhoneNumber(null);
                payload.setBillingStreetSuffix("CIR");
                payload.setBillingStreetPostDirection("NE");
                payload.setBillingUnitType(FakerDataGenerator.getRandomNumericString(7));
                payload.setBillingStreetNumber(FakerDataGenerator.getRandomNumericString(12));
                break;
            case VALID_BILLING_ADDRESS_TYPE_WITH_INVALID_BILLING_UNIT_TYPE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType("S");
                payload.setBillingStreetName("TREE-PARK");
                payload.setBillingCity("ATLANTA");
                payload.setBillingStateCode("GA");
                payload.setBillingZipCode("30542");
                payload.setBillingCountyCode("T207");
                payload.setBillingStreetPreDirection("NE");
                payload.setWorkPhoneNumber(null);
                payload.setBillingStreetSuffix("CIR");
                payload.setBillingStreetPostDirection("NE");
                payload.setBillingUnitType("KE");
                payload.setBillingStreetNumber(FakerDataGenerator.getRandomNumericString(12));
                break;
            case VALID_BILLING_ADDRESS_TYPE_WITH_MAX_LENGTH_BILLING_UNIT_NUMBER:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType("S");
                payload.setBillingStreetName("TREE-PARK");
                payload.setBillingCity("ATLANTA");
                payload.setBillingStateCode("GA");
                payload.setBillingZipCode("30542");
                payload.setBillingCountyCode("T207");
                payload.setBillingStreetPreDirection("NE");
                payload.setBillingStreetSuffix("CIR");
                payload.setBillingStreetPostDirection("NE");
                payload.setBillingUnitType("KEY");
                payload.setBillingUnitNumber(FakerDataGenerator.getRandomNumericString(7));
                payload.setBillingStreetNumber(FakerDataGenerator.getRandomNumericString(12));
                break;
            case VALID_BILLING_ADDRESS_TYPE_WITH_MAX_LENGTH_BILLING_RURAL_ROUTE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType("R");
                payload.setBillingStreetName("TREE-PARK");
                payload.setBillingCity("ATLANTA");
                payload.setBillingStateCode("GA");
                payload.setBillingZipCode("30542");
                payload.setBillingCountyCode("T207");
                payload.setBillingRuralRoute(FakerDataGenerator.getRandomNumericString(21));
                payload.setBillingStreetNumber(FakerDataGenerator.getRandomNumericString(12));
                break;
            case VALID_BILLING_ADDRESS_TYPE_WITH_EMPTY_BILLING_RURAL_ROUTE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType("R");
                payload.setBillingCity("ATLANTA");
                payload.setBillingStateCode("GA");
                payload.setBillingZipCode("30542");
                payload.setBillingCountyCode("T207");
                payload.setLoginID("sys");
                payload.setCustomerPEWCPreferences(true);
                payload.setBillingRuralRoute(null);
                break;
            case VALID_BILLING_ADDRESS_TYPE_WITH_MAX_LENGTH_BILLING_RURAL_ROUTE_NUMBER:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType("R");
                payload.setBillingRuralRoute("RR");
                payload.setBillingRuralRouteNumber(FakerDataGenerator.getRandomNumericString(11));
                break;
            case VALID_BILLING_ADDRESS_TYPE_WITH_NULL_BILLING_RURAL_ROUTE_NUMBER:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType("R");
                payload.setBillingCity("FLOWERY BRANCH");
                payload.setBillingStateCode("GA");
                payload.setBillingZipCode("30542");
                payload.setBillingCountyCode("T207");
                payload.setLoginID("sys");
                payload.setBillingRuralRoute("RR");
                payload.setBillingRuralRouteNumber(null);
                break;
            case VALID_BILLING_ADDRESS_TYPE_WITH_MAX_LENGTH_BILLING_PO_BOX:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType("P");
                payload.setBillingRuralRoute("RR");
                payload.setBillingCity("FLOWERY BRANCH");
                payload.setBillingStateCode("GA");
                payload.setBillingZipCode("30542");
                payload.setBillingCountyCode("T207");
                payload.setBillingPOBox(FakerDataGenerator.getRandomNumericString(12));
                payload.setBillingStreetNumber(FakerDataGenerator.getRandomNumericString(12));
                break;
            case VALID_BILLING_ADDRESS_TYPE_WITH_NULL_BILLING_PO_BOX:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType("P");
                payload.setBillingCity("FLOWERY BRANCH");
                payload.setBillingStateCode("GA");
                payload.setBillingZipCode("30542");
                payload.setBillingCountyCode("T207");
                payload.setBillingPOBox(null);
                break;
            case VALID_BILLING_ADDRESS_TYPE_WITH_MAX_LENGTH_BILLING_ADDRESS_LINE2_WITH_BILLING_RURAL_ROUTE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType("S");
                payload.setBillingCity("ATLANTA");
                payload.setBillingRuralRoute("RR");
                payload.setBillingRuralRouteNumber("2A");
                payload.setBillingStateCode("GA");
                payload.setBillingZipCode("30542");
                payload.setBillingAddressLine2(FakerDataGenerator.generateUpperCaseString(31));
                break;
            case VALID_BILLING_ADDRESS_TYPE_WITH_MAX_LENGTH_BILLING_ADDRESS_LINE2:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType("S");
                payload.setBillingRuralRoute("RR");
                payload.setBillingCity("FLOWERY BRANCH");
                payload.setBillingStateCode("GA");
                payload.setBillingZipCode("30542");
                payload.setBillingCountyCode("T207");
                payload.setBillingAddressLine2(FakerDataGenerator.generateUpperCaseString(31));
                break;

            case VALID_BILLING_ADDRESS_TYPE_WITH_MAX_LENGTH_BILLING_ADDRESS_LINE2_WITH_BILLING_PO_BOX:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType("P");
                payload.setBillingRuralRoute("RR");
                payload.setBillingCity("FLOWERY BRANCH");
                payload.setBillingPOBox("204");
                payload.setBillingStateCode("GA");
                payload.setBillingZipCode("30542");
                payload.setBillingAddressLine2(FakerDataGenerator.generateUpperCaseString(31));
                break;
            case VALID_BILLING_ADDRESS_TYPE_S_WITH_MAX_LENGTH_BILLING_CITY:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType("S");
                payload.setBillingStreetNumber("1234");
                payload.setBillingStreetPreDirection(null);
                payload.setBillingStreetName("TREE-PARK");
                payload.setBillingStateCode("GA");
                payload.setBillingZipCode("30542");
                payload.setBillingCity(FakerDataGenerator.generateUpperCaseString(21));
                break;
            case VALID_BILLING_ADDRESS_TYPE_R_WITH_MAX_LENGTH_BILLING_CITY_WITH_BILLING_RURAL_ROUTE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType("R");
                payload.setBillingRuralRoute("RR");
                payload.setBillingRuralRouteNumber("123");
                payload.setBillingStateCode("GA");
                payload.setBillingZipCode("30542");
                payload.setBillingCity(FakerDataGenerator.generateUpperCaseString(21));
                break;
            case VALID_BILLING_ADDRESS_TYPE_P_WITH_MAX_LENGTH_BILLING_CITY_WITH_BILLING_PO_BOX:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType("P");
                payload.setBillingPOBox("2A");
                payload.setBillingAddressLine2(null);
                payload.setBillingStateCode("GA");
                payload.setBillingZipCode("30542");
                payload.setBillingCountyCode("T207");
                payload.setBillingCity(FakerDataGenerator.generateUpperCaseString(21));
                break;
            case VALID_BILLING_ADDRESS_TYPE_R_WITH_MAX_LENGTH_BILLING_STATE_CODE_WITH_BILLING_RURAL_ROUTE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType("R");
                payload.setBillingRuralRoute("RR");
                payload.setBillingRuralRouteNumber("123");
                payload.setBillingCity("FLOWERY BRANCH");
                payload.setBillingZipCode("30542");
                payload.setBillingStateCode("GAGA");

                break;
            case VALID_BILLING_ADDRESS_TYPE_P_WITH_MAX_LENGTH_BILLING_STATE_CODE_WITH_BILLING_PO_BOX:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType("P");
                payload.setBillingPOBox("2A");
                payload.setBillingAddressLine2(null);
                payload.setBillingCity("FLOWERY BRANCH");
                payload.setBillingZipCode("30542");
                payload.setBillingCountyCode("T207");
                payload.setBillingStateCode("GAGA");
                break;
            case VALID_BILLING_ADDRESS_TYPE_S_WITH_INVALID_BILLING_STATE_CODE_NOT_PRESENT_IN_TABLE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType("S");
                payload.setBillingAddressLine2(null);
                payload.setBillingStreetName("TREE-PARK");
                payload.setBillingStreetNumber("1234");
                payload.setBillingCity("ATLANTA");
                payload.setBillingZipCode("30542");
                payload.setBillingCountyCode("T207");
                payload.setBillingStateCode("BEE");
                break;
            case VALID_BILLING_ADDRESS_TYPE_R_WITH_INVALID_BILLING_STATE_CODE_NOT_PRESENT_IN_TABLE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType("R");
                payload.setBillingCity("FLOWERY BRANCH");
                payload.setBillingZipCode("30542");
                payload.setBillingCountyCode("T207");
                payload.setBillingStateCode("AAA");
                payload.setBillingRuralRouteNumber("1234");
                payload.setBillingRuralRoute("RR");
                break;
            case VALID_BILLING_ADDRESS_TYPE_P_WITH_INVALID_BILLING_STATE_CODE_NOT_PRESENT_IN_TABLE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType("P");
                payload.setBillingPOBox("2A");
                payload.setBillingCity("FLOWERY BRANCH");
                payload.setBillingZipCode("30542");
                payload.setBillingStateCode("AAA");
                break;
            case VALID_BILLING_ADDRESS_TYPE_S_WITH_INVALID_BILLING_ZIP_CODE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType("S");
                payload.setBillingStreetName("TREE-PARK");
                payload.setBillingStreetPreDirection(null);
                payload.setBillingCity("ATLANTA");
                payload.setBillingZipCode("1234-12345");
                payload.setBillingStateCode("GA");
                payload.setBillingCountyCode("T207");
                break;
            case VALID_BILLING_ADDRESS_TYPE_S_WITH_MIN_LENGTH_BILLING_ZIP_CODE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType("S");
                payload.setBillingStreetName("TREE-PARK");
                payload.setBillingStreetPreDirection(null);
                payload.setBillingCity("ATLANTA");
                payload.setBillingZipCode("1234");
                payload.setBillingStateCode("GA");
                payload.setBillingCountyCode("T207");
                break;
            case VALID_BILLING_ADDRESS_TYPE_R_WITH_INVALID_BILLING_ZIP_CODE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType("R");
                payload.setBillingRuralRoute("RR");
                payload.setBillingRuralRouteNumber("1234");
                payload.setBillingStreetName("TREE-PARK");
                payload.setBillingStreetPreDirection(null);
                payload.setBillingCity("FLOWERY BRANCH");
                payload.setBillingZipCode("1234-5897");
                payload.setBillingStateCode("GA");
                payload.setBillingCountyCode("T207");
                break;
            case VALID_BILLING_ADDRESS_TYPE_R_WITH_MIN_LENGTH_BILLING_ZIP_CODE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType("R");
                payload.setBillingRuralRoute("RR");
                payload.setBillingRuralRouteNumber("1234");
                payload.setBillingStreetName("TREE-PARK");
                payload.setBillingStreetPreDirection(null);
                payload.setBillingCity("FLOWERY BRANCH");
                payload.setBillingZipCode("1234");
                payload.setBillingStateCode("GA");
                payload.setBillingCountyCode("T207");
                break;
            case VALID_BILLING_ADDRESS_TYPE_P_WITH_INVALID_BILLING_ZIP_CODE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType("P");
                payload.setBillingRuralRoute("RR");
                payload.setBillingRuralRouteNumber("1234");
                payload.setBillingStreetName("TREE-PARK");
                payload.setBillingStreetPreDirection(null);
                payload.setBillingCity("FLOWERY BRANCH");
                payload.setBillingZipCode("3054-30542");
                payload.setBillingStateCode("GA");
                payload.setBillingPOBox("2A");
                payload.setBillingCountyCode("T207");
                break;
            case VALID_BILLING_ADDRESS_TYPE_P_WITH_MIN_LENGTH_BILLING_ZIP_CODE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType("S");
                payload.setBillingRuralRoute("RR");
                payload.setBillingRuralRouteNumber("1234");
                payload.setBillingStreetName("TREE-PARK");
                payload.setBillingStreetPreDirection(null);
                payload.setBillingCity("FLOWERY BRANCH");
                payload.setBillingZipCode("3054");
                payload.setBillingStateCode("GA");
                payload.setBillingPOBox("2A");
                payload.setBillingCountyCode("T207");
                break;
            case VALID_BILLING_ADDRESS_TYPE_S_WITH_BILLING_ZIP_CODE_MISSING_281c:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType("S");
                payload.setBillingStreetName("TREE-PARK");
                payload.setBillingStreetNumber("1308");
                payload.setBillingStreetName("TREE-PARK");
                payload.setBillingStreetSuffix("CIR");
                payload.setBillingCity("FLOWERY BRANCH");
                payload.setBillingZipCode(null);
                payload.setBillingPOBox("2A");
                break;
            case VALID_BILLING_ADDRESS_TYPE_P_WITH_BILLING_ZIP_CODE_MISSING_281e:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType("P");
                payload.setBillingCity("FLOWERY BRANCH");
                payload.setBillingZipCode(null);
                payload.setBillingPOBox("2");
                break;
            case VALID_BILLING_ADDRESS_TYPE_S_WITH_MAX_LENGTH_BILLING_COUNTY_CODE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType("S");
                payload.setBillingStreetNumber("1308");
                payload.setBillingStreetPreDirection(null);
                payload.setBillingStreetName("TREE-PARK");
                payload.setBillingStreetSuffix("CIR");
                payload.setBillingCity("FLOWERY BRANCH");
                payload.setBillingStateCode("GA");
                payload.setBillingZipCode("30542");
                payload.setBillingCountyCode("T12345");
                break;
            case VALID_BILLING_ADDRESS_TYPE_R_WITH_MAX_LENGTH_BILLING_COUNTY_CODE_BILLING_RURAL_ROUTE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType("R");
                payload.setBillingRuralRoute("RR");
                payload.setBillingRuralRouteNumber("2A");
                payload.setBillingStateCode("GA");
                payload.setBillingCity("FLOWERY BRANCH");
                payload.setBillingZipCode(null);
                payload.setBillingCountyCode("T207T207");
                break;
            case VALID_BILLING_ADDRESS_TYPE_P_WITH_MAX_LENGTH_BILLING_COUNTY_CODE_WITH_BILLING_PO_BOX:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setLoginID("sys");
                payload.setBillingAddressType("P");
                payload.setBillingPOBox("2A");
                payload.setBillingAddressLine2(null);
                payload.setBillingCity("FLOWERY BRANCH");
                payload.setBillingStateCode("GA");
                payload.setBillingZipCode(null);
                payload.setBillingCountyCode("T20789");
                break;
            case VALID_BILLING_ADDRESS_TYPE_S_WITH_BILLING_COUNTY_CODE_NOT_PRESENT_IN_TABLE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType("S");
                payload.setLoginID("sys");
                payload.setBillingStreetNumber("1308");
                payload.setBillingStreetPreDirection(null);
                payload.setBillingStreetName("TREE-PARK");
                payload.setBillingStreetSuffix("CIR");
                payload.setBillingCity("FLOWERY BRANCH");
                payload.setBillingStateCode("GA");
                payload.setBillingZipCode("30542");
                payload.setBillingCountyCode("T207T207");
                break;
            case VALID_BILLING_ADDRESS_TYPE_R_WITH_BILLING_COUNTY_CODE_NOT_PRESENT_IN_TABLE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType("R");
                payload.setBillingRuralRouteNumber("2A");
                payload.setBillingPOBox(null);
                payload.setBillingAddressLine2(null);
                payload.setBillingCity("FLOWERY BRANCH");
                payload.setBillingStateCode("GA");
                payload.setBillingZipCode("30542");
                payload.setBillingCountyCode("T20");
                break;
            case VALID_BILLING_ADDRESS_TYPE_P_WITH_BILLING_COUNTY_CODE_NOT_PRESENT_IN_TABLE_WITH_BILLING_PO_BOX:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType("P");
                payload.setBillingPOBox("2A");
                payload.setBillingAddressLine2(null);
                payload.setBillingCity("FLOWERY BRANCH");
                payload.setBillingStateCode("GA");
                payload.setBillingZipCode("30542");
                payload.setBillingCountyCode("T20");
                break;
            default:
                payload.setSeparateBillingAddress(Boolean.getBoolean(String.valueOf(true)));
        }
    }

    public void setWorkPhoneNumberBasedOnType284_286(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel workphonenumber) {
        switch (workphonenumber) {
            case MIN_LENGTH_WORK_PHONE_NUMBER:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setWorkPhoneNumber(FakerDataGenerator.getRandomNumericString(9));
                break;
            case ALPHANUMERIC_WORK_PHONE_NUMBER:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setWorkPhoneNumber(FakerDataGenerator.generateAlphanumeric(10));
                break;
            case NULL_WORK_PHONE_NUMBER_WITH_VALID_WORK_PHONE_TYPE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setWorkPhoneType("home");
                payload.setWorkPhoneNumber("");
                payload.setBillingRuralRouteNumber(null);
                payload.setBillingRuralRoute(null);
                break;

            default:
                payload.setWorkPhoneNumber(FakerDataGenerator.generatePhoneNumber());
        }
    }

    public void setWorkPhoneTypeBasedOnType287_290(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel workphonetype) {
        switch (workphonetype) {

            case NULL_WORK_PHONE_TYPE_WITH_VALID_WORK_PHONE_NUMBER:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setWorkPhoneType("");
                payload.setWorkPhoneNumber("4165945244");
                break;
            case MAX_LENGTH_WORK_EXTENSION_TYPE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setWorkPhoneExtension(FakerDataGenerator.getRandomNumericString(5));
                break;
            case WORK_PHONE_TYPE_PROVIDED_MAX_1_CHAR:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setWorkPhoneType("BUSINESS");
                payload.setWorkPhoneNumber(FakerDataGenerator.generatePhoneNumber());
                payload.setWorkPhoneExtension(null);
                break;
            case INVALID_WORK_PHONE_TYPE_VALUE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setWorkPhoneType("C");
                payload.setWorkPhoneNumber("4165945244");
                payload.setWorkPhoneExtension("354");
                break;
            default:
                payload.setWorkPhoneType("L");
        }
    }

    public void setHomePhoneNumberBasedOnType291_293(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel homePhoneNumber) {
        switch (homePhoneNumber) {

            case MIN_LENGTH_HOME_PHONE_NUMBER:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setHomePhoneNumber("123456789");
                payload.setBillingRuralRoute(null);
                payload.setBillingRuralRouteNumber(null);
                break;
            case ALPHANUMERIC_HOME_PHONE_NUMBER:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setHomePhoneNumber(FakerDataGenerator.generateAlphanumeric(9));
                payload.setBillingRuralRoute(null);
                payload.setBillingRuralRouteNumber(null);
                break;
            case NULL_HOME_PHONE_NUMBER_WITH_VALID_HOME_PHONE_TYPE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setBillingRuralRoute(null);
                payload.setBillingRuralRouteNumber(null);
                payload.setHomePhoneNumber(null);
                payload.setHomePhoneType("M");
                break;
            default:
                payload.setHomePhoneNumber("41649653133");
        }
    }

    public void setHomePhoneTypeBasedOnType294_297(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel homephonetype) {
        switch (homephonetype) {

            case NULL_HOME_PHONE_TYPE_WITH_VALID_HOME_PHONE_NUMBER_294:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setHomePhoneType(null);
                payload.setHomePhoneNumber("4164965313");
                break;
            case HOME_PHONE_TYPE_PROVIDED_MAX_1_CHAR_296:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setHomePhoneType("LL");
                payload.setHomePhoneNumber("4164965313");
                break;
            case INVALID_HOME_PHONE_TYPE_VALUE_297:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setHomePhoneType("B");
                payload.setHomePhoneNumber("4164965313");
            default:
                payload.setHomePhoneType("L");
        }
    }


    public void setAcnStatusIndicatorBasedOnTypeTC298_307(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel acnStatusIndicator) {
        switch (acnStatusIndicator) {
            case EMPTY_ACN_STATUS_INDICATOR_WITH_TENANT_LANDLORD:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setAcnStatusIndicator("");
                payload.setTenantLandlord("T");
                break;
            case INVALID_ACN_STATUS_INDICATOR_VALUE_NOT_PRESENT_IN_TABLE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setAcnStatusIndicator("ABCD");
                break;
            case MAX_LENGTH_ACN_STATUS_INDICATOR:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setAcnStatusIndicator("NACNN");
                payload.setTenantLandlord("T");
                break;
            case VALID_ACN_STATUS_INDICATOR_WITH_MAX_LENGTH_TENANT_LANDLORD:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setAcnStatusIndicator("ACN");
                payload.setTenantLandlord(FakerDataGenerator.generateUpperCaseString(5));
                break;
            case VALID_ACN_STATUS_INDICATOR_WITH_NULL_TENANT_LANDLORD:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setLoginID("acncsr");
                payload.setAcnStatusIndicator("ACN");
                payload.setBillingStreetPostDirection(null);
                payload.setTenantLandlord(null);
                break;
            case VALID_ACN_STATUS_INDICATOR_VALID_TENANT_LANDLORD_L_WITH_INVALID_USER_ROLE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setAcnStatusIndicator("ACN");
                payload.setTenantLandlord("L");
                payload.setLoginID("sys");
                break;
            case VALID_ACN_STATUS_INDICATOR_VALID_TENANT_LANDLORD_T_WITH_INVALID_USER_ROLE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setAcnStatusIndicator("ACN");
                payload.setTenantLandlord("T");
                payload.setLoginID("sys");
                break;
            case NULL_ACN_STATUS_INDICATOR_VALID_TENANT_LANDLORD_L_WITH_INVALID_USER_ROLE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setAcnStatusIndicator(null);
                payload.setTenantLandlord("L");
                payload.setLoginID("sys");
                break;
            case NULL_ACN_STATUS_INDICATOR_VALID_TENANT_LANDLORD_T_WITH_INVALID_USER_ROLE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setLoginID("sys");
                payload.setAcnStatusIndicator(null);
                payload.setTenantLandlord("T");
                break;

            default:
                payload.setAcnStatusIndicator("ACN");
        }

    }

    public void setCustomerPEWCPreferencesBasedOnTypeTC308_309(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel acnStatusIndicator) {
        switch (acnStatusIndicator) {
            case CUSTOMER_PEWC_PREFRENCES_VALUE_GOOD_WITH_OTHER_PARAM_NULL:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setHomePhoneType(null);
                payload.setHomePhoneNumber(null);
                payload.setWorkPhoneExtension(null);
                payload.setWorkPhoneType(null);
                payload.setWorkPhoneNumber(null);
                payload.setCustomerPEWCPreferences("good");

                break;
            case CUSTOMER_PEWC_PREFRENCES_VALUE_TRUE_WITH_OTHER_PARAM_NULL:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setHomePhoneType(null);
                payload.setHomePhoneNumber(null);
                payload.setWorkPhoneExtension(null);
                payload.setWorkPhoneType(null);
                payload.setWorkPhoneNumber(null);
                payload.setCustomerPEWCPreferences(true);

                break;
            default:
                payload.setCustomerPEWCPreferences(true);

        }
    }

    public void setRequestIDBasedOnTypeTC155_157(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel requestID) {
        switch (requestID) {
            case NULL_REQUEST_ID:
                payload.setRequestID(null);
                break;
            case DUPLICATE_REQUEST_ID:
                payload.setRequestID("3BC00A0397B14F29A313280EE0110941");
                break;
            case LONG_REQUEST_ID:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(35));
                break;

            default:
                payload.setRequestID(FakerDataGenerator.generateString(10));
        }
    }

    public void setLoginIDBasedOnTypeTC158_160B(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel loginID) {
        switch (loginID) {
            case INVALID_LOGIN_ID:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("kfletcher123");
                break;
            case NULL_LOGIN_ID:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("");
                break;
            case NON_NUMERIC_LOGIN_ID:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(FakerDataGenerator.generateAlphanumericWithSpecialChars(9));
                break;
            case ALPHANUMERIC_LOGIN_ID:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(FakerDataGenerator.generateAlphanumeric(8));
                break;
            case MAX_LENGTH_LOGIN_ID:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(FakerDataGenerator.getRandomNumericString(35));
                break;
            default:
                payload.setLoginID(FakerDataGenerator.generateLowerCaseString(10));
        }
    }

    public void setTransactionIDBasedOnTypeTC161_162(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel transactionID) {
        switch (transactionID) {
            case NULL_TRANSACTION_ID_INCL_ENROLLMENT_STATE:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setEnrollmentState("INCL");
                payload.setCustomerCode("5911661");
                payload.setPremisesCode("5886135");
                payload.setTransactionID(null);
                break;
            case NULL_TRANSACTION_ID_CRDS_ENROLLMENT_STATE:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setEnrollmentState("CRDS");
                payload.setCustomerCode("5911662");
                payload.setPremisesCode("5886136");
                payload.setTransactionID(null);
                break;
            default:
                payload.setTransactionID(FakerDataGenerator.getRandomNumericString(10));
        }
    }

    public void setCustomerCodeBasedOnTypeTC163_164(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel customercode) throws IOException {
        switch (customercode) {
            case NULL_CUSTOMER_CODE_INCL_ENROLLMENT_STATE:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                /*ExcelReader excelReader = new ExcelReader(EXPERIAN_DATA);
                List<Map<String, String>> testData = excelReader.getSheetData(EXPERIAN_SHEET_NAME);
                Map<String, String> rowData = testData.get(0); // 0-indexed, fetches the second data row
                log.info("BUSINESS NAME is: {}", rowData.get("BUSINESS NAME"));*/
                payload.setEnrollmentState("INCL");
                payload.setPremisesCode("5886135");
                payload.setTransactionID("234223459");
                payload.setCustomerCode(null);
                break;
            case NULL_CUSTOMER_CODE_CRDS_ENROLLMENT_STATE:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setEnrollmentState("INCL");
                payload.setPremisesCode("5886136");
                payload.setTransactionID("234223459");
                payload.setCustomerCode(null);
                break;
            default:
                payload.setCustomerCode(FakerDataGenerator.getRandomNumericString(8));
        }
    }

    public void setPremisesCodeBasedOnTypeTC165_167(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel premisesCode) {
        switch (premisesCode) {
            case NULL_PREMISES_CODE_INCL_ENROLLMENT_STATE:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setEnrollmentState("INCL");
                payload.setTransactionID("234223459");
                payload.setCustomerCode("5911662");
                payload.setPremisesCode(null);
                break;
            case NULL_PREMISES_CODE_CRDS_ENROLLMENT_STATE:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setEnrollmentState("CRDS");
                payload.setTransactionID("234223459");
                payload.setCustomerCode("5911662");
                payload.setPremisesCode(null);
                break;
            case VALID_PREMISES_CODE_NULL_ENROLLMENT_STATE:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setEnrollmentState("CRDS");
                payload.setTransactionID("234223459");
                payload.setCustomerCode("5911662");
                payload.setPremisesCode("5886135");
                payload.setEnrollmentState(null);
                break;
            default:
                payload.setPremisesCode(FakerDataGenerator.getRandomNumericString(8));
        }
    }


    public void setInvalidTestConditionTC237(GetEligiblePlansAndOffersRequest payload) {
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setAuthorizedBy(null);
                payload.setCustomerType("CM");
                payload.setLoginID("sys");
                payload.setFederalTaxID("f45uBGDqZKPL34H0Fx01ETGmXhUlI6VyORn/aD0/IYg=");

    }

    public void setInvalidReferralCodeTC238_241(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel referralcode) {
        switch (referralcode) {
            case MAX_REFERRAL_CODE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setMarketingPromotionCode("AAA");
                payload.setCustomerType("CM");
                payload.setAuthorizedBy("MM");
                payload.setReferralCode(FakerDataGenerator.getRandomNumericString(14));
                break;
            case NONNUMERIC_REFERRAL_CODE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setMarketingPromotionCode("AAA");
                payload.setCustomerType("CM");
                payload.setAuthorizedBy("MM");
                payload.setFederalTaxID("f45uBGDqZKPL34H0Fx01ETGmXhUlI6VyORn/aD0/IYg=");
                payload.setReferralCode(FakerDataGenerator.getRandomNumericString(5));
                break;
            case ALPHANUMERIC_REFERRAL_CODE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setMarketingPromotionCode("AAA");
                payload.setCustomerType("CM");
                payload.setAuthorizedBy("MM");
                payload.setFederalTaxID("f45uBGDqZKPL34H0Fx01ETGmXhUlI6VyORn/aD0/IYg=");
                payload.setReferralCode(FakerDataGenerator.generateAlphanumeric(5));
                break;
            case VALID_REFERRAL_CODE_WITH_MISSING_MARKETING_CODE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setMarketingPromotionCode("");
                payload.setCustomerType("CM");
                payload.setAuthorizedBy("MM");
                payload.setFederalTaxID("f45uBGDqZKPL34H0Fx01ETGmXhUlI6VyORn/aD0/IYg=");
                payload.setReferralCode(FakerDataGenerator.getRandomNumericString(9));
                break;
            default:
                payload.setReferralCode(FakerDataGenerator.getRandomNumericString(10));
        }
    }


    public void setInvalidPremisesStreetNumberTC242(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel premisesStreetNumber) {
        switch (premisesStreetNumber) {
            case MAX_PREMISES_STREET_NUMBER:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setPremisesStreetNumber(FakerDataGenerator.getRandomNumericString(13));
                break;
            default:
                payload.setPremisesStreetNumber(FakerDataGenerator.getRandomNumericString(12));
        }
    }

    public void setInvalidPremisesStreetPreDirectionTypeTC243(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel premisesStreetPreDirection) {
        switch (premisesStreetPreDirection) {
            case MAX_PREMISES_STREET_PRE_DIRECTION:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setPremisesStreetPreDirection(FakerDataGenerator.getRandomNumericString(4));
                break;
            case INVALID_PREMISES_STREET_PRE_DIRECTION_EQUAL_TO_TWO:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setPremisesStreetPreDirection(FakerDataGenerator.generateUpperCaseString(2));
                break;
            default:
                payload.setPremisesStreetPreDirection(FakerDataGenerator.getRandomNumericString(2));
        }
    }

    public void setInvalidPremisesStreetNameTypeTC244_245(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel premisesStreetName) {
        switch (premisesStreetName) {
            case MAX_PREMISES_STREET_NAME:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setPremisesStreetName(FakerDataGenerator.generateUpperCaseString(32));
                break;
            case NULL_PREMISES_STREET_NAME:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setPremisesStreetName(null);
                break;
            default:
                payload.setPremisesStreetName(FakerDataGenerator.getRandomNumericString(30));
        }
    }

    public void setInvalidPremisesStreetSuffixTypeTC246_246a(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel premisesStreetSuffix) {
        switch (premisesStreetSuffix) {
            case MAX_PREMISES_STREET_SUFFIX:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setPremisesStreetSuffix(FakerDataGenerator.getRandomNumericString(7));
                break;
            case INVALID_PREMISES_STREET_SUFFIX:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setPremisesStreetSuffix(FakerDataGenerator.generateUpperCaseString(4));
                break;
            default:
                payload.setPremisesStreetSuffix(FakerDataGenerator.getRandomNumericString(6));
        }
    }

    public void setInvalidPremisesStreetPostDirectionTypeTC247_247a(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel premisesStreetPostDirection) {
        switch (premisesStreetPostDirection) {
            case MAX_PREMISES_STREET_POST_DIRECTION:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setPremisesStreetPostDirection(FakerDataGenerator.generateUpperCaseString(3));
                break;
            case INVALID_PREMISES_STREET_POST_DIRECTION_EQUAL_TO_TWO:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setPremisesStreetPostDirection(FakerDataGenerator.generateUpperCaseString(2));
                break;
            default:
                payload.setPremisesStreetPostDirection(FakerDataGenerator.getRandomNumericString(2));
        }
    }

    public void setInvalidPremisesUnitTypeTC248_248a(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel premisesUnitType) {
        switch (premisesUnitType) {
            case MAX_PREMISES_UNIT_TYPE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setPremisesUnitType(FakerDataGenerator.generateUpperCaseString(7));
                break;
            case INVALID_PREMISES_UNIT_TYPE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setPremisesUnitType(FakerDataGenerator.generateUpperCaseString(5));
                break;
            default:
                payload.setPremisesUnitType(FakerDataGenerator.getRandomNumericString(6));
        }
    }

    public void setInvalidPremisesUnitNumberTC249(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel premisesUnitNumber) {
        switch (premisesUnitNumber) {
            case MAX_PREMISES_UNIT_NUMBER:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setPremisesUnitNumber(FakerDataGenerator.generateUpperCaseString(7));
                break;
            default:
                payload.setPremisesUnitNumber(FakerDataGenerator.getRandomNumericString(6));

        }
    }

    public void setInvalidPremisesCityTC250(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel premisescity) {
        switch (premisescity) {
            case MAX_PREMISES_CITY:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setPremisesCity(FakerDataGenerator.generateUpperCaseString(21));
                break;
            case NULL_PREMISES_CITY:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setPremisesCity(null);
                break;
            default:
                payload.setPremisesCity(FakerDataGenerator.getRandomString(20));

        }
    }

    public void setPremisesStateCodeBasedOnType252_253(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel premisesStateCode) {
        switch (premisesStateCode) {
            case EMPTY_PREMISES_STATE_CODE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setPremisesStateCode("");
            case MAX_LENGTH_PREMISES_STATE_CODE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setPremisesStateCode(FakerDataGenerator.getRandomString(4));
                break;
            case INVALID_PREMISES_STATE_CODE_EQUAL_TO_TWO:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setPremisesStateCode("YU");
                break;
            default:
                payload.setPremisesStateCode(FakerDataGenerator.getRandomString(3));
        }
    }

    public void setPremisesZipCodeBasedOnType254_255c(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel premisesZipCode) {
        switch (premisesZipCode) {
            case EMPTY_PREMISES_ZIP_CODE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setPremisesZipCode("");
            case LOWERCASE_PREMISES_ZIP_CODE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setPremisesZipCode(FakerDataGenerator.generateLowerCaseString(5));
                break;
            case MAX_LENGTH_PREMISES_ZIP_CODE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setPremisesZipCode(FakerDataGenerator.getRandomNumericString(11));
                break;
            case INVALID_PREMISES_ZIP_FORMAT_CODE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setPremisesZipCode("3054-00000");
                break;
            case NUMERIC_PREMISES_ZIP_CODE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setPremisesZipCode("31111");
                break;
            case INVALID_PREMISES_ZIP_CODE_RANDOM_STRING:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setPremisesZipCode(FakerDataGenerator.getRandomNumericString(4));
                break;
            default:
                payload.setPremisesZipCode(FakerDataGenerator.getRandomString(10));
        }
    }

    public void setPremisesCountyCodeBasedOnType256_257(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel premisesCountyCode) {
        switch (premisesCountyCode) {
            case EMPTY_PREMISES_COUNTY_CODE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setPremisesCountyCode("");
            case MIN_LENGTH_PREMISES_COUNTY_CODE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setPremisesCountyCode(FakerDataGenerator.getRandomString(1));
                break;
            case ALPHANUMERIC_PREMISES_COUNTY_CODE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setPremisesCountyCode(FakerDataGenerator.generateAlphanumeric(5));
                break;
            default:
                payload.setPremisesCountyCode(FakerDataGenerator.getRandomString(5));
        }
    }
    public void setTestCondition25(GetEligiblePlansAndOffersRequest payload) {
        payload.setRequestID(FakerDataGenerator.getRandomNumericString(6));
        payload.setLoginID("sys");
        payload.setTransactionType("MKSW");
        payload.setCustomerLastName(null);
        payload.setCustomerFirstName(null);
        payload.setCustomerBusinessName("MORGAN");
        payload.setAglcAccountNumber("00000000000633305101");
        payload.setAglcServiceLocationID("633305101");
        payload.setCreditCheckOption("yes");
        payload.setConfirmCreditCheck(false);

    }
    public void setTestCondition26(GetEligiblePlansAndOffersRequest payload) {
        payload.setRequestID(FakerDataGenerator.getRandomNumericString(6));
        payload.setLoginID("sys");
        payload.setTransactionType("MKSW");
        payload.setCustomerBusinessName("MORGAN");
        payload.setCreditCheckOption("yes");
        payload.setConfirmCreditCheck(true);

    }
    public void setTestCondition27(GetEligiblePlansAndOffersRequest payload) {
        payload.setRequestID(FakerDataGenerator.getRandomNumericString(6));
        payload.setLoginID("sys");
        payload.setTransactionType("MKSW");
        payload.setCustomerBusinessName("MORGAN");
        payload.setCreditCheckBusinessName("MORGAN TRAILER MFG CO");
        payload.setCreditCheckOption("yes");
        payload.setConfirmCreditCheck(true);
        payload.setCommercialCreditCheckBusinessBIN("716441998");

    }
    public void setTestCondition28(GetEligiblePlansAndOffersRequest payload) {
        payload.setRequestID(FakerDataGenerator.getRandomNumericString(6));
        payload.setLoginID("sys");
        payload.setTransactionType("MKSW");
        payload.setCustomerBusinessName("MORGAN");
        payload.setCreditCheckOption("yes");
        payload.setConfirmCreditCheck(true);

    }
    public void setTestCondition29(GetEligiblePlansAndOffersRequest payload) {
        payload.setRequestID(FakerDataGenerator.getRandomNumericString(6));
        payload.setLoginID("sys");
        payload.setTransactionType("MKSW");
        payload.setCustomerBusinessName("MORGAN");
        payload.setCreditCheckOption("yes");
        payload.setConfirmCreditCheck(true);

    }
    public void setTestCondition30(GetEligiblePlansAndOffersRequest payload) {
        payload.setRequestID(FakerDataGenerator.getRandomNumericString(6));
        payload.setLoginID("sys");
        payload.setTransactionType("MKSW");
        payload.setCustomerBusinessName("MORGAN");
        payload.setCreditCheckOption("yes");
        payload.setConfirmCreditCheck(true);

    }
    public void setTestCondition31(GetEligiblePlansAndOffersRequest payload) {
        payload.setRequestID(FakerDataGenerator.getRandomNumericString(6));
        payload.setLoginID("sys");
        payload.setTransactionType("MKSW");
        payload.setCustomerBusinessName("MORGAN");
        payload.setCreditCheckOption("yes");
        payload.setConfirmCreditCheck(true);

    }
    public void setTestConditionRSTC11UC50(GetEligiblePlansAndOffersRequest payload) throws IOException {
        payload.setRequestID(FakerDataGenerator.getRandomNumericString(6));
        payload.setLoginID("sys");
        payload.setTransactionType("MKSW");
 ExcelReader excelReader = new ExcelReader(CUSTOMER_DATA);
                List<Map<String, String>> testData = excelReader.getSheetData(CUSTOMER_SHEET_NAME);
                Map<String, String> rowData = testData.get(3);
        payload.setCustomerLastName(rowData.get("customerLastName"));
        payload.setCustomerFirstName(rowData.get("customerFirstName"));
        payload.setAglcAccountNumber(rowData.get("aglcAccountNumber"));
        payload.setAglcServiceLocationID(rowData.get("aglcServiceLocationID"));
        payload.setPremisesStreetNumber(rowData.get("premisesStreetNumber"));
        payload.setPremisesStreetName(rowData.get("premisesStreetName"));
        payload.setPremisesStreetSuffix(rowData.get("premisesStreetSuffix"));
        payload.setPremisesUnitType(rowData.get("premisesUnitType"));
        payload.setPremisesUnitNumber(rowData.get("premisesUnitNumber"));
        payload.setPremisesCity(rowData.get("premisesCity"));
        payload.setPremisesStateCode(rowData.get("premisesStateCode"));
        payload.setPremisesZipCode(rowData.get("premisesZipCode"));
        payload.setPremisesCountyCode(rowData.get("premisesCountyCode"));
        payload.setSeparateBillingAddress(rowData.get("separateBillingAddress"));
        payload.setAcnStatusIndicator(rowData.get("acnStatusIndicator"));
        payload.setCustomerPEWCPreferences(rowData.get("customerPEWCPreferences"));
        payload.setCreditCheckOption(rowData.get("creditCheckOption"));
        payload.setConfirmCreditCheck(Boolean.parseBoolean(rowData.get("confirmCreditCheck")));
        payload.setTenantLandlord(rowData.get("tenantLandlord"));
        payload.setPremisesStreetPostDirection(rowData.get("premisesStreetPostDirection"));
    }

    public void getCustomerDetails(GetEligiblePlansAndOffersRequest payload, Map<String, String> data ){
        payload.setCustomerLastName(data.get("customerLastName"));
        payload.setCustomerFirstName(data.get("customerFirstName"));
        payload.setAglcAccountNumber(FakerDataGenerator.generateDigits(8));
        payload.setAglcServiceLocationID(data.get("aglcServiceLocationID"));
        payload.setPremisesStreetNumber(data.get("premisesStreetNumber"));
        payload.setPremisesStreetName(data.get("premisesStreetName"));
        payload.setPremisesStreetSuffix(data.get("premisesStreetSuffix"));
        payload.setPremisesCity(data.get("premisesCity"));
        payload.setPremisesStateCode(data.get("premisesStateCode"));
        payload.setPremisesZipCode(data.get("premisesZipCode"));
        payload.setPremisesCountyCode(data.get("premisesCountyCode"));
        String ssn = data.get("SSN");
        if (ssn != null && !ssn.trim().isEmpty()) {
            payload.setSocialSecurityNumber(encryptData(data.get("SSN")));
        }
    }

    public void setTheFieldToEmptyForCommercialScenarios(GetEligiblePlansAndOffersRequest payload){
        payload.setSocialSecurityNumber("");
        payload.setCustomerFirstName("");
        payload.setCustomerMiddleName("");
        payload.setCustomerLastName("");
    }

    public void preparePayloadForPreviouslySavedIncompleteEnrollment(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel testCondition){
        int customerCode = Integer.parseInt(testContext.getSearchAccountsResponse().getData().getAccounts().getFirst().getCustomerCode());
        String premisesCode = testContext.getSearchAccountsResponse().getData().getAccounts().getFirst().getPremisesCode();
        int transactionID = testContext.getSearchAccountsResponse().getData().getAccounts().getFirst().getTransactionID();
        String sspParticipantCode= testContext.getSearchAccountsResponse().getData().getAccounts().getFirst().getSspParticipantCode();
        payload.setTransactionType(TURN_ON.getValue());
        payload.setCustomerCode(customerCode);
        payload.setPremisesCode(premisesCode);
        payload.setTransactionID(transactionID);
        payload.setRequestID(FakerDataGenerator.getRandomNumericString(6));
        setTheFieldToEmptyForCommercialScenarios(payload);
        payload.setCustomerFirstName(testContext.getSearchAccountsResponse().getData().getAccounts().getFirst().getCustomerFirstName());
        payload.setCustomerLastName(testContext.getSearchAccountsResponse().getData().getAccounts().getFirst().getCustomerLastName());

        switch(testCondition){
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_424:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_434:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_436:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_440:
            case SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_479:
            case SSP_VALIDATION_PREMISES_CODE_MISSING_TC_492:
                payload.setEnrollmentState(GlobalEnums.EnrollMentState.INCL.getValue());
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_426:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_426_1:
                payload.setEnrollmentState(GlobalEnums.EnrollMentState.CRDS.getValue());
                break;

            case SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_478:
            case SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_480:
            case SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_482_2:
            case SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_483_2:
                payload.setEnrollmentState(GlobalEnums.EnrollMentState.INCL.getValue());
                payload.setCustomerCode(null);
                break;

            case SSP_VALIDATION_SSP_PARTICIPANT_CODE_MISSING_TC_490:
            case SSP_VALIDATION_SSP_PARTICIPANT_CODE_MISSING_TC_494_2:
                payload.setEnrollmentState(GlobalEnums.EnrollMentState.INCL.getValue());
                payload.setSspParticipantCode(null);
                transactionID=testContext.getSaveEnrollmentResponse().getData().getTransactionID();
                payload.setTransactionID(transactionID);
                payload.setSeasonalSavingsProgramIndicator(true);
                break;

            case SSP_VALIDATION_SSP_PARTICIPANT_CODE_MISSING_TC_492:
                payload.setSspParticipantCode(null);
                payload.setSeasonalSavingsProgramIndicator(true);
                payload.setEnrollmentState(GlobalEnums.EnrollMentState.INCL.getValue());
                break;

            case SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_482:
            case SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_483:
            case SSP_VALIDATION_SSP_PARTICIPANT_CODE_MISSING_TC_495:
            case SSP_VALIDATION_SSP_PARTICIPANT_CODE_MISSING_TC_494,
                 SSP_VALIDATION_PAYMENT_CONFIRMATION_NUMBER_MISSING_TC_496:
                nullifyFields(payload,  "enrollmentState", "transactionID");
                payload.setSspParticipantCode(sspParticipantCode);
                payload.setSeasonalSavingsProgramIndicator(true);
                break;

            case SSP_VALIDATION_PREMISES_CODE_MISSING_TC_488:
                ExcelReader excelReaderCommercialCustomerData = null;
                try {
                    excelReaderCommercialCustomerData = new ExcelReader(EXPERIAN_DATA);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                List<Map<String, String>> allRowsOfCommercialCustomerData = excelReaderCommercialCustomerData.getSheetData(EXPERIAN_SHEET_NAME);
                Map<String, String> commercialCustomerData = allRowsOfCommercialCustomerData.get(5063);
                nullifyFields(payload,  "enrollmentState", "transactionID");
                payload.setSspParticipantCode(sspParticipantCode);
                payload.setSeasonalSavingsProgramIndicator(true);
                payload.setCustomerType(COMMERCIAL.getValue());
                setTheFieldToEmptyForCommercialScenarios(payload);
                payload.setFederalTaxID(encryptData(commercialCustomerData.get("TAX-ID")));
                payload.setCustomerBusinessName(commercialCustomerData.get("BUSINESS NAME"));
                nullifyFields(payload,  "enrollmentState", "transactionID");
                payload.setCreditCheckOption("Yes");
                break;

            case SSP_VALIDATION_PREMISES_CODE_MISSING_TC_484:
            case SSP_VALIDATION_PREMISES_CODE_MISSING_TC_488_2:
                payload.setEnrollmentState(GlobalEnums.EnrollMentState.INCL.getValue());
                nullifyFields(payload,  "premisesCode", "sspParticipantCode");
                break;

            case SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_486:
                payload.setEnrollmentState(GlobalEnums.EnrollMentState.INCL.getValue());
                nullifyFields(payload,  "customerCode", "sspParticipantCode");
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_CE_TC_502,
                 GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_CE_TC_505,
                 GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_507:
                payload.setAglcServiceLocationID(testContext.getGetEligiblePlansAndOffersResponse().getData().getAglcServiceLocationID());
                payload.setAglcAccountNumber(testContext.getGetEligiblePlansAndOffersResponse().getData().getAglcAccountNumber());
                payload.setEnrollmentState(GlobalEnums.EnrollMentState.INCL.getValue());
 payload.setSeasonalSavingsProgramIndicator(true);
                break;
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_506:
                payload.setCustomerType(COMMERCIAL.getValue());
                payload.setCreditCheckOption(YES.getValue());
                setTheFieldToEmptyForCommercialScenarios(payload);
                payload.setAglcServiceLocationID(testContext.getGetEligiblePlansAndOffersResponse().getData().getAglcServiceLocationID());
              payload.setSeasonalSavingsProgramIndicator(true);
                populateCommonCommercialFieldsFromSearchAccountsResponse(payload, transactionID);
                break;
            default:
                break;
        }
    }
    private void populateCommonCommercialFieldsFromSearchAccountsResponse(GetEligiblePlansAndOffersRequest payload, int transactionID) {

        Account selectedAccount = testContext.getSearchAccountsResponse()
                .getData()
                .getAccounts()
                .stream()
                .filter(account -> account.getTransactionID() == transactionID)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Transaction ID not found"));

        payload.setCustomerBusinessName(selectedAccount.getCustomerBusinessName());
        payload.setEnrollmentState(GlobalEnums.EnrollMentState.INCL.getValue());
        payload.setCustomerCode(selectedAccount.getCustomerCode());
        payload.setPremisesCode(selectedAccount.getPremisesCode());
        payload.setPremisesCity(selectedAccount.getPremisesCity());
        payload.setPremisesStateCode(selectedAccount.getPremisesStateCode());
        payload.setPremisesZipCode(selectedAccount.getPremisesZipCode());
        payload.setCustomerBusinessName(selectedAccount.getCustomerBusinessName());
    }

    public void preparePayloadForPreviouslySavedIncompleteEnrollmentByTransactionId (GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel testCondition, int transactionID){

        Account selectedAccount = testContext.getSearchAccountsResponse()
                .getData()
                .getAccounts()
                .stream()
                .filter(account -> account.getTransactionID() == transactionID)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Transaction ID not found"));

        payload.setTransactionType(TURN_ON.getValue());
        payload.setCustomerCode(selectedAccount.getCustomerCode());
        payload.setPremisesCode(selectedAccount.getPremisesCode());
        payload.setTransactionID(selectedAccount.getTransactionID());
        payload.setRequestID(FakerDataGenerator.getRandomNumericString(6));
       // setTheFieldToEmptyForCommercialScenarios(payload);
        payload.setCustomerFirstName(selectedAccount.getCustomerFirstName());
        payload.setCustomerLastName(selectedAccount.getCustomerLastName());

        switch(testCondition){
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_424:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_434:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_436:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_440:
                payload.setEnrollmentState(GlobalEnums.EnrollMentState.INCL.getValue());
                break;
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_426:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_426_1:
                payload.setEnrollmentState(GlobalEnums.EnrollMentState.CRDS.getValue());
                break;
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_CE_TC_502,
                 GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_CE_TC_505,
                 GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_506,
                 GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_507:
                payload.setSeasonalSavingsProgramIndicator(true);

                break;
            default:
                break;
        }
    }

    public void preparePayloadBasedOnTC_EligiblePlansAndSaveEnrollment(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel testCondition) {
        payload.setRequestID(FakerDataGenerator.getRandomNumericString(6));
        ExcelReader excelReaderResidentialCustomerData = null;
        try {
            excelReaderResidentialCustomerData = new ExcelReader(CUSTOMER_DATA);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ExcelReader excelReaderCommercialCustomerData = null;
        try {
            excelReaderCommercialCustomerData = new ExcelReader(EXPERIAN_DATA);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Map<String, String>> allRowsOfCommercialCustomerData = excelReaderCommercialCustomerData.getSheetData(EXPERIAN_SHEET_NAME);
        Map<String, String> commercialCustomerData = allRowsOfCommercialCustomerData.get(5063);


        List<Map<String, String>> allRowsOfCustomerData = excelReaderResidentialCustomerData.getSheetData(CUSTOMER_SHEET_NAME);
        Map<String, String> customerData = allRowsOfCustomerData.get(30);
        switch(testCondition) {
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_CE_TC_423:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_SI_TC_433:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_424:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_434:
                payload.setEnrollmentSource(PHONECALL.getValue());
                payload.setCreditCheckOption(YES.getValue());
                getCustomerDetails(payload, customerData);
                payload.setCallerID(FakerDataGenerator.generateDigits(10));
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_DP_TC_425:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_DR_TC_435:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_RD_TC_446:
                payload.setCustomerType(COMMERCIAL.getValue());
                payload.setCreditCheckOption(YES.getValue());
                populateCommonFields(payload,commercialCustomerData);
                setTheFieldToEmptyForCommercialScenarios(payload);
                break;

            case SSP_VALIDATION_PREMISES_CODE_MISSING_TC_484:
            case SSP_VALIDATION_PREMISES_CODE_MISSING_TC_485:
            case SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_486:
            case SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_487, SSP_VALIDATION_PREMISES_CODE_MISSING_TC_488,
                 SSP_VALIDATION_SSP_PARTICIPANT_CODE_MISSING_TC_491:
                payload.setCustomerType(COMMERCIAL.getValue());
                payload.setCreditCheckOption(YES.getValue());
                populateCommonFields(payload,commercialCustomerData);
                setTheFieldToEmptyForCommercialScenarios(payload);
                payload.setSeasonalSavingsProgramIndicator(true);
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_SF_TC_454:
                payload.setSeasonalSavingsProgramIndicator(true);
                payload.setCustomerType(COMMERCIAL.getValue());
                payload.setCreditCheckOption(YES.getValue());
                populateCommonFields(payload,commercialCustomerData);
                setTheFieldToEmptyForCommercialScenarios(payload);
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PC_TC_431:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PC_TC_432:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PR_TC_438:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PR_TC_439:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_RP_TC_442:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_RP_TC_443:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_BD_TC_452:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_426:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_426_1:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_427:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_428:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_436:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_440:
                customerData = allRowsOfCustomerData.get(31);
                payload.setCreditCheckOption(YES.getValue());
                getCustomerDetails(payload,customerData);
                break;
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_SF_TC_501:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_CE_TC_502:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_SI_TC_503:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_CE_TC_505,
                 GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_507:
                customerData = loadRowFromExcelToCustomerData(CUSTOMER_DATA, CUSTOMER_SHEET_NAME, testCondition);
                getCustomerAndPremiseDetails(payload, customerData);
                payload.setSeasonalSavingsProgramIndicator(true);
                break;
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_SI_TC_504,
                 GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_506:
                payload.setCustomerType(COMMERCIAL.getValue());
                payload.setCreditCheckOption(YES.getValue());
                populateCommonFields(payload,commercialCustomerData);
                setTheFieldToEmptyForCommercialScenarios(payload);
                payload.setSeasonalSavingsProgramIndicator(true);
                break;
            default:
                payload.setCreditCheckOption(CREDIT_CHECK_NOT_REQUIRED.getValue());
                getCustomerDetails(payload, customerData);
                payload.setTransactionType(TURN_ON.getValue());
                payload.setCustomerType(RESIDENTIAL.getValue());
                payload.setEnrollmentSource(MAIL.getValue());
                break;
        }
    }

    public void getCustomerAndPremiseDetails(GetEligiblePlansAndOffersRequest payload, Map<String, String> data ){
        payload.setLoginID(data.get("loginID"));
        payload.setCustomerType(data.get("customerType"));
        payload.setCustomerLastName(data.get("customerLastName"));
        payload.setCustomerFirstName(data.get("customerFirstName"));
        payload.setAglcServiceLocationID(data.get("aglcServiceLocationID"));
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
        payload.setAcnStatusIndicator(data.get("acnStatusIndicator"));
        payload.setTenantLandlord(data.get("tenantLandlord"));
        payload.setCreditCheckOption(data.get("creditCheckOption"));
        payload.setConfirmCreditCheck(Boolean.parseBoolean(data.get("confirmCreditCheck")));
        payload.setSeasonalSavingsProgramIndicator(Boolean.parseBoolean(data.get("SSPStatusIndicator")));

        String ssn = data.get("SSN");
        if (ssn != null && !ssn.trim().isEmpty()) {
            payload.setSocialSecurityNumber(encryptData(data.get("SSN")));
        }
        String federalTaxId = data.get("federalTaxId");
        if (federalTaxId != null && !federalTaxId.trim().isEmpty()) {
            payload.setFederalTaxID(encryptData(data.get("federalTaxId")));
        }
    }

    public void setRequestParams(GetEligiblePlansAndOffersRequest payload, GlobalEnums.PromotionCode promotionCode,
                                 GetEligiblePlansAndOffersApiLabel testCondition) {
        Map<String, String> customerData = loadRowFromExcelToCustomerData(CUSTOMER_DATA, CUSTOMER_SHEET_NAME, testCondition);
        getCustomerAndPremiseDetails(payload, customerData);
        payload.setMarketingPromotionCode(promotionCode.getValue());
        if(testCondition.toString().contains("SSP")&& !(testCondition.toString().equals("SSP_FALSE_ALLOWED_FOR_ACN_TC_477"))){
            payload.setSeasonalSavingsProgramIndicator(true);
        }
    }

    public void setRequoteRequestParams(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel testCondition) {
        Map<String, String> customerData = loadRowFromExcelToCustomerData(CUSTOMER_DATA, CUSTOMER_SHEET_NAME, testCondition);
        getCustomerAndPremiseDetails(payload, customerData);
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
}
