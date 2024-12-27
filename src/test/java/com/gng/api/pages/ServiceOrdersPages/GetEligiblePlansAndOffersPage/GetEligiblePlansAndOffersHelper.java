package com.gng.api.pages.ServiceOrdersPages.GetEligiblePlansAndOffersPage;


import com.gng.api.pages.BasePage;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.request.GetEligiblePlansAndOffersRequest;
import com.gng.api.pojo.ServiceOrdersPojo.SaveEnrollment.SaveEnrollmentRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.ServiceOrdersSteps.GetEligiblePlansAndOffers.GetEligiblePlansAndOffersApiLabel;
import com.gng.api.steps.ServiceOrdersSteps.SaveEnrollment.SaveEnrollmentApiLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;

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

    public void setRequestIDBasedOnType(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel requestID) {
        switch (requestID) {
            case EMPTY_REQUEST_ID:
                payload.setRequestID("");
                break;
            case DUPLICATE_REQUEST_ID:
                payload.setRequestID("123");
                break;
            case LONG_REQUEST_ID:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(200));
                break;
            case UNICODE_CHARS_REQUEST_ID:
                payload.setRequestID(FakerDataGenerator.generateUnicode());
                break;
            default:
                payload.setRequestID(FakerDataGenerator.generateString(10));
        }
    }

    public void setLoginIDBasedOnType(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel loginID) {
        switch (loginID) {
            case MIN_LENGTH_LOGIN_ID:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(FakerDataGenerator.getRandomNumericString(1));
                break;
            case SPECIAL_CHARS_LOGIN_ID:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(FakerDataGenerator.generateAlphanumericWithSpecialChars(7));
                break;
            case EMPTY_LOGIN_ID:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID("");
                break;
            case UPPERCASE_LOGIN_ID:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setLoginID(FakerDataGenerator.generateUpperCaseString(9));
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
            case NUMERIC_CUSTOMER_TYPE:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setCustomerCode(FakerDataGenerator.getRandomNumericString(4));
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

    public void setCustomerFirstNameBasedOnType(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel customerFirstName) {
        switch (customerFirstName) {
            case EMPTY_CUSTOMER_FIRST_NAME:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setCustomerFirstName("");
                break;
            default:
                payload.setCustomerLastName(FakerDataGenerator.generateUpperCaseString(2));
        }
    }

    public void setSeasonalSavingsProgramIndicatorBasedOnType(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel seasonalSavingsProgramIndicator) {
        switch (seasonalSavingsProgramIndicator) {
            case NULL_SEASONAL_SAVINGS_PROGRAM_INDICATOR:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setSeasonalSavingsProgramIndicator(null);
                break;
            default:
                payload.setSeasonalSavingsProgramIndicator(Boolean.getBoolean(String.valueOf(true)));
        }
    }

    public void setPremisesStreetNameBasedOnType(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel premisesStreetName) {
        switch (premisesStreetName) {
            case EMPTY_PREMISES_STREET_NAME:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setPremisesStreetName("");
            default:
                payload.setPremisesStreetName(FakerDataGenerator.getRandomString(5));
        }
    }



    public void setCreditCheckOptionBasedOnType(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel creditCheckOption) {
        switch (creditCheckOption) {
            case EMPTY_CREDIT_CHECK_OPTION:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setCreditCheckOption(" ");
            case SPL_CHAR_CREDIT_CHECK_OPTION:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setCreditCheckOption(FakerDataGenerator.generateAlphanumericWithSpecialChars(5));
                break;
            case MAX_LENGTH_CREDIT_CHECK_OPTION:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setCreditCheckOption(FakerDataGenerator.getRandomString(4));
                break;
            case ALPHANUMERIC_CREDIT_CHECK_OPTION:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setCreditCheckOption(FakerDataGenerator.generateAlphanumeric(3));
                break;
            case NUMERIC_CREDIT_CHECK_OPTION:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setCreditCheckOption(FakerDataGenerator.getRandomNumericString(3));
                break;
            default:
                payload.setCreditCheckOption(FakerDataGenerator.getRandomString(3));
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
                payload.setSeparateBillingAddress(FakerDataGenerator.getRandomString(1));
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
                payload.setPremisesStreetPreDirection("SOUTH");
                payload.setBillingStreetNumber(FakerDataGenerator.getRandomNumericString(12));
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
                payload.setPremisesStreetPreDirection("EN");
                payload.setBillingStreetNumber(FakerDataGenerator.getRandomNumericString(12));
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
                payload.setPremisesStreetPreDirection("NE");
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
                payload.setPremisesStreetPreDirection("NE");
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
                payload.setPremisesStreetPreDirection(null);
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
                payload.setPremisesStreetPreDirection(null);
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
                payload.setPremisesStreetPreDirection("NE");
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
                payload.setPremisesStreetPreDirection("NE");
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
                payload.setPremisesStreetPreDirection("NE");
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
                payload.setBillingStreetName("TREE-PARK");
                payload.setBillingCity("ATLANTA");
                payload.setBillingStateCode("GA");
                payload.setBillingZipCode("30542");
                payload.setBillingCountyCode("T207");
                payload.setBillingRuralRoute(null);
                payload.setBillingStreetNumber(FakerDataGenerator.getRandomNumericString(12));
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
                payload.setBillingAddressType("P");
                payload.setBillingRuralRoute("RR");
                payload.setBillingCity("FLOWERY BRANCH");
                payload.setBillingStateCode("GA");
                payload.setBillingZipCode("30542");
                payload.setBillingCountyCode("T207");
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
                payload.setBillingRuralRoute("RR");
                payload.setBillingCity("FLOWERY BRANCH");
                payload.setBillingStateCode("GA");
                payload.setBillingZipCode("30542");
                payload.setBillingCountyCode("T207");
                payload.setBillingPOBox(null);
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





                
            default:
                payload.setSeparateBillingAddress(Boolean.getBoolean(String.valueOf(true)));
        }
    }

    public void setAcnStatusIndicatorBasedOnType(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel acnStatusIndicator) {
        switch (acnStatusIndicator) {
            case EMPTY_ACN_STATUS_INDICATOR:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setAcnStatusIndicator(" ");
            case SPL_CHAR_ACN_STATUS_INDICATOR:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setAcnStatusIndicator(FakerDataGenerator.generateAlphanumericWithSpecialChars(5));
                break;
            case MAX_LENGTH_ACN_STATUS_INDICATOR:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setAcnStatusIndicator(FakerDataGenerator.getRandomString(4));
                break;
            case ALPHANUMERIC_ACN_STATUS_INDICATOR:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setAcnStatusIndicator(FakerDataGenerator.generateAlphanumeric(4));
                break;

            default:
                payload.setAcnStatusIndicator(FakerDataGenerator.getRandomString(3));
        }

    }

    public void setInvalidTestCondition(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel authorizedBy) {
        switch (authorizedBy) {
            case NULL_AUTHORIZED_BY:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setAuthorizedBy("");
                payload.setCustomerType("CM");
                payload.setFederalTaxID("f45uBGDqZKPL34H0Fx01ETGmXhUlI6VyORn/aD0/IYg=");
            default:
                payload.setAuthorizedBy(FakerDataGenerator.generateString(9));

        }
    }

    public void setInvalidReferralCodeTC238_241(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel referralcode) {
        switch (referralcode) {
            case MAX_REFERRAL_CODE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setMarketingPromotionCode("AAA");
                payload.setCustomerType("CM");
                payload.setAuthorizedBy("MM");
                payload.setFederalTaxID("f45uBGDqZKPL34H0Fx01ETGmXhUlI6VyORn/aD0/IYg=");
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
    public void setPremisesZipCodeBasedOnType254_254c(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel premisesZipCode) {
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
                payload.setPremisesZipCode(FakerDataGenerator.getRandomString(11));
                break;
            case INVALID_PREMISES_ZIP_FORMAT_CODE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setPremisesZipCode("3054-00000");
                break;
            case NUMERIC_PREMISES_ZIP_CODE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setPremisesZipCode(FakerDataGenerator.getRandomString(5));
                break;
            case INVALID_PREMISES_ZIP_CODE_RANDOM_STRING:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setPremisesZipCode(FakerDataGenerator.getRandomString(4));
                break;
            default:
                payload.setPremisesZipCode(FakerDataGenerator.getRandomString(8));
        }
    }

    public void setPremisesCountyCodeBasedOnType256_257(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel premisesCountyCode) {
        switch (premisesCountyCode) {
            case EMPTY_PREMISES_COUNTY_CODE:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setPremisesCountyCode(" ");
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

}



