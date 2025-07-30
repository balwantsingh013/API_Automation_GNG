package com.gng.api.pages.turnOn.ServiceOrdersPages.GetEligiblePlansAndOffersPage;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.request.GetEligiblePlansAndOffersRequest;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.response.GetEligiblePlansAndOffersResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.pojo.shared.CustomerData;
import com.gng.api.steps.AesEncryption.AesEncryptionSteps;
import com.gng.api.steps.turnOn.ServiceOrdersSteps.GetEligiblePlansAndOffers.GetEligiblePlansAndOffersApiLabel;
import com.gng.api.util.ExcelReader;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.gng.api.constants.GlobalEnums.CreditCheckOption.*;
import static com.gng.api.constants.GlobalEnums.EnrollmentSource.*;
import static com.gng.api.constants.GlobalEnums.PromotionCode.DEALS;
import static com.gng.api.constants.TestConstant.*;
import static com.gng.api.steps.turnOn.ServiceOrdersSteps.GetEligiblePlansAndOffers.GetEligiblePlansAndOffersApiLabel.CREDIT_CHECK_MULTIPLE_PREMISES;
import static com.gng.api.steps.turnOn.ServiceOrdersSteps.GetEligiblePlansAndOffers.GetEligiblePlansAndOffersApiLabel.CREDIT_CHECK_YES;
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




    public void payloadBasedOnTCsCommercial(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel testCondition) {
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
                data = allRows.get(1946);
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

            case COMMERCIAL_CREDIT_CHECK_YES_NEW_ENROLLMENT_TC_347:
                data = allRows.get(184);
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
                data = allRows.get(4697);
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

                // Set credit check business name from a different row
                Map<String, String> creditCheckData = allRows.get(4912);
                payload.setCreditCheckBusinessName(creditCheckData.get("BUSINESS NAME"));
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

    public void mapDataFromExcel(CustomerData customerData, GetEligiblePlansAndOffersRequest payload) {
        Optional.ofNullable(customerData.getLoginID()).ifPresent(payload::setLoginID);
        Optional.ofNullable(customerData.getCustomerType()).ifPresent(payload::setCustomerType);
        Optional.ofNullable(customerData.getCustomerLastName()).ifPresent(payload::setCustomerLastName);
        Optional.ofNullable(customerData.getCustomerMiddleName()).ifPresent(payload::setCustomerMiddleName);
        Optional.ofNullable(customerData.getCustomerFirstName()).ifPresent(payload::setCustomerFirstName);
        Optional.ofNullable(customerData.getAglcServiceLocationID()).ifPresent(payload::setAglcServiceLocationID);
        Optional.ofNullable(customerData.getPremisesStreetNumber()).ifPresent(payload::setPremisesStreetNumber);
        Optional.ofNullable(customerData.getPremisesStreetPreDirection()).ifPresent(payload::setPremisesStreetPreDirection);
        Optional.ofNullable(customerData.getPremisesStreetName()).ifPresent(payload::setPremisesStreetName);
        Optional.ofNullable(customerData.getPremisesStreetSuffix()).ifPresent(payload::setPremisesStreetSuffix);
        Optional.ofNullable(customerData.getPremisesStreetPostDirection()).ifPresent(payload::setPremisesStreetPostDirection);
        Optional.ofNullable(customerData.getPremisesUnitType()).ifPresent(payload::setPremisesUnitType);
        Optional.ofNullable(customerData.getPremisesUnitNumber()).ifPresent(payload::setPremisesUnitNumber);
        Optional.ofNullable(customerData.getPremisesCity()).ifPresent(payload::setPremisesCity);
        Optional.ofNullable(customerData.getPremisesStateCode()).ifPresent(payload::setPremisesStateCode);
        Optional.ofNullable(customerData.getPremisesZipCode()).ifPresent(payload::setPremisesZipCode);
        Optional.ofNullable(customerData.getPremisesCountyCode()).ifPresent(payload::setPremisesCountyCode);
        Optional.ofNullable(customerData.getAcnStatusIndicator()).ifPresent(payload::setAcnStatusIndicator);
        Optional.ofNullable(customerData.getTenantLandlord()).ifPresent(payload::setTenantLandlord);
        Optional.ofNullable(customerData.getCreditCheckOption()).ifPresent(payload::setCreditCheckOption);
        Optional.of(customerData.getConfirmCreditCheck()).ifPresent(payload::setConfirmCreditCheck);
        Optional.of(customerData.getMarketingPromotionCode()).ifPresent(payload::setMarketingPromotionCode);
        Optional.ofNullable(customerData.getSocialSecurityNumber())
                .ifPresent(ssn -> payload.setSocialSecurityNumber(AesEncryptionSteps.encryptData(ssn)));
        Optional.ofNullable(customerData.getFederalTaxId())
                .ifPresent(fedTaxId -> payload.setFederalTaxID(AesEncryptionSteps.encryptData(fedTaxId)));
    }

    public void setRequestParams(GetEligiblePlansAndOffersRequest payload, CustomerData customerData,
                                 GetEligiblePlansAndOffersApiLabel testCondition) {
        if (customerData != null){
            mapDataFromExcel(customerData, payload);
        }
    }

    public List<GetEligiblePlansAndOffersResponse.Plan> getValidationEligiblePlansAndOffers() {
        String controlNum = ApplicationContext.get().getDbAction().getControlNumber();
        Object rawResult = ApplicationContext.get().getDbAction().getEligiblePlansAndOffersResult(controlNum);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return mapper.convertValue(
                rawResult,
                new TypeReference<>() {
                }
        );
    }

}



