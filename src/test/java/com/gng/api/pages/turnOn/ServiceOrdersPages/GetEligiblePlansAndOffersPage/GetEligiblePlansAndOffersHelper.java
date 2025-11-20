package com.gng.api.pages.turnOn.ServiceOrdersPages.GetEligiblePlansAndOffersPage;

import com.github.javafaker.Bool;
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
import static com.gng.api.constants.GlobalEnums.ACNorNACN.ACN;
import static com.gng.api.constants.GlobalEnums.BillingUnitType.KEY;
import static com.gng.api.constants.GlobalEnums.CreditCheckOption.*;
import static com.gng.api.constants.GlobalEnums.CustomerType.COMMERCIAL;
import static com.gng.api.constants.GlobalEnums.CustomerType.RESIDENTIAL;
import static com.gng.api.constants.GlobalEnums.EnrollMentState.CRDS;
import static com.gng.api.constants.GlobalEnums.EnrollMentState.INCL;
import static com.gng.api.constants.GlobalEnums.EnrollmentSource.*;
import static com.gng.api.constants.GlobalEnums.HomePhoneType.LANDLINE;
import static com.gng.api.constants.GlobalEnums.HomePhoneType.MOBILE;
import static com.gng.api.constants.GlobalEnums.InvalidValues.*;
import static com.gng.api.constants.GlobalEnums.MarketingPromotionCodes.*;
import static com.gng.api.constants.GlobalEnums.PromotionCode.DEALS;
import static com.gng.api.constants.GlobalEnums.TenantOrLandlord.LANDLORD;
import static com.gng.api.constants.GlobalEnums.TenantOrLandlord.TENANT;
import static com.gng.api.constants.GlobalEnums.TransactionType.*;
import static com.gng.api.constants.GlobalEnums.WorkPhoneType.BUSINESS;
import static com.gng.api.constants.TestConstant.*;
import static com.gng.api.steps.AesEncryption.AesEncryptionSteps.encryptData;
import static com.gng.api.steps.turnOn.ServiceOrdersSteps.GetEligiblePlansAndOffers.GetEligiblePlansAndOffersApiLabel.*;
import static com.gng.api.util.CommonUtil.nullifyFields;

@Slf4j
public class GetEligiblePlansAndOffersHelper {
    String billingAddressState="MH";
    String invalidLoginIdForACN="sys";
    String validZipCode="30309";
    String premisesUnitType="#";

    private final TestContext testContext;

    public GetEligiblePlansAndOffersHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    GetEligiblePlansAndOffersRequest preparePayload(GetEligiblePlansAndOffersApiLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        String jsonFileName = apiLabel.equals(get_eligible_plans_and_offers)
                ? get_eligible_plans_and_offers.toString()
                : get_eligible_plans_and_offers_mandatory.toString();
        return BasePage.deserializeJsonToPojo(jsonFileName, GetEligiblePlansAndOffersRequest.class);
    }


    public void setTransactionTypeBasedOnType(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel transactionType) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        switch (transactionType) {
            case NULL_TANSACTION_TYPE_TC_183:
                payload.setTransactionType(null);
                break;
            case EMPTY_TRANSACTION_TYPE:
                payload.setTransactionType("");
                break;
            case NUMERIC_TRANSACTION_TYPE:
                payload.setTransactionType(FakerDataGenerator.generateDigits(10));
                break;
            case MAX_LENGTH_VALIDATION_TRANSACTION_TYPE_TC_184:
                payload.setTransactionType(FakerDataGenerator.generateUpperCaseString(36));
                break;
            case LOWERCASE_TRANSACTION_TYPE:
                payload.setTransactionType(FakerDataGenerator.generateLowerCaseString(4));
                break;
            case WHITESPACE_CONTAINS_TRANSACTION_TYPE:
                payload.setTransactionType(TRANSACTION_TYPE_WITH_WHITESPACE.getValue());
                break;
            case ALPHANUMERIC_TRANSACTION_TYPE:
                payload.setTransactionType(FakerDataGenerator.generateAlphanumeric(2));
                break;
            default:
                payload.setTransactionType(FakerDataGenerator.generateUpperCaseString(4));
        }
    }

    public void setCustomerTypeBasedOnType(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel customerTYPE) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        switch (customerTYPE) {
            case EMPTY_CUSTOMER_TYPE:
                payload.setCustomerType("");
                break;
            case MIN_LENGTH_CUSTOMER_TYPE:
                payload.setCustomerType(FakerDataGenerator.getRandomString(3));
                break;
            case SPL_CHAR_CUSTOMER_TYPE:
                payload.setCustomerType(FakerDataGenerator.generateAlphanumericWithSpecialChars(3));
                break;
            case MAX_LENGTH_CUSTOMER_TYPE_TC_191:
                payload.setCustomerType(FakerDataGenerator.getRandomString(5));
                break;
            case NULL_CUSTOMER_TYPE_TC_190:
                payload.setCustomerType(null);
                break;
            case INVALID_VALUE_CUSTOMER_TYPE_TC_192:
                payload.setCustomerType(INVALID_CUSTOMER_TYPE.getValue());
                break;

            default:
                payload.setCustomerType(FakerDataGenerator.getRandomString(2));
        }
    }

    public void setCommercialPayloadForNegativeTCs(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel testCondition){
        setTheFieldToEmptyForCommercialScenarios(payload);
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setTransactionType(TURN_ON.getValue());
        payload.setCustomerType(COMMERCIAL.getValue());
        payload.setAglcServiceLocationID(FakerDataGenerator.generateDigits(9));
        Map<String, String> customerData = loadRowFromExcelToCustomerData(EXPERIAN_DATA, EXPERIAN_SHEET_NAME, testCondition);
        parseAddress(payload, customerData.getOrDefault("BUSINESS STREET ADDRESS", ""));
        populateCommonFields(payload, customerData);
    }
    public void setEnrollmentSourcesBasedOnType(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel enrollmentSources) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        switch (enrollmentSources) {
            case NULL_ENROLLMENT_SOURCES_TC_193:
                payload.setEnrollmentSource(null);
                break;
            case SPL_CHAR_ENROLLMENT_SOURCES:
                payload.setEnrollmentSource(FakerDataGenerator.generateAlphanumericWithSpecialChars(7));
                break;
            case EMPTY_ENROLLMENT_SOURCES:
                payload.setEnrollmentSource("");
                break;
            case NUMERIC_ENROLLMENT_SOURCES:
                payload.setEnrollmentSource(FakerDataGenerator.getRandomNumericString(5));
                break;
            case MAX_LENGTH_ENROLLMENT_SOURCES_TC_194:
                payload.setEnrollmentSource(FakerDataGenerator.generateString(36));
                break;
            case INVALID_VALUE_ENROLLMENT_SOURCE_195:
                payload.setEnrollmentSource(FakerDataGenerator.generateUpperCaseString(6));
                break;
            case INVALID_LENGTH_MARKETING_PROMOTION_CODE_196:
                payload.setMarketingPromotionCode(FakerDataGenerator.generateString(46));
                break;
            case INVALID_MARKETING_PROMOTION_CODE_FOR_ENROLLMENT_SOURCE_TC_197:
                payload.setMarketingPromotionCode(INNVALID_MARKETING_PROMOTION_CODE.getValue());
                payload.setEnrollmentSource(ALLCONNECT.getValue());
                break;
            case INVALID_MARKETING_PROMOTION_CODE_TC_198:
                payload.setMarketingPromotionCode(FakerDataGenerator.generateString(10));
                break;
            case INVALID_MARKETING_PROMOTION_CODE_FOR_RS_CUST_TYPE_TC_199:
                payload.setMarketingPromotionCode(PROMOTION_CODE_COMMERCIAL.getValue());
                payload.setCustomerType(RESIDENTIAL.getValue());
                break;
            case INVALID_MARKETING_PROMOTION_CODE_FOR_NEW_CUSTOMERS_TC_200:
                payload.setMarketingPromotionCode(PROMOTION_CODE_FOR_EXISTING_CUSTOMER.getValue());
                break;
            case EXPIRED_MARKETING_PROMOTION_CODE_TC_201:
                payload.setMarketingPromotionCode(EXPIRED_MARKETING_PROMOTION_CODE.getValue());
                break;
            case INVALID_MARKETING_PROMOTION_CODE_FOR_CM_CUST_TYPE_TC_202:
                setCommercialPayloadForNegativeTCs(payload, enrollmentSources);
                payload.setEnrollmentSource(FAX.getValue());
                payload.setMarketingPromotionCode(PROMOTION_CODE_RESIDENTIAL.getValue());
                payload.setAuthorizedBy(FakerDataGenerator.generateUpperCaseString(2));
                break;
            case FEDERAL_TAX_ID_INVALID_LENGTH_TC_225:
                setCommercialPayloadForNegativeTCs(payload, enrollmentSources);
                payload.setFederalTaxID(FakerDataGenerator.generateDigits(10));
                break;
            case NON_NUMERIC_FEDERAL_TAX_ID_TC_226:
                setCommercialPayloadForNegativeTCs(payload, enrollmentSources);
                payload.setFederalTaxID(FakerDataGenerator.generateString(9));
                break;
            case FEDERAL_TAX_ID_NULL_TC_227:
                setCommercialPayloadForNegativeTCs(payload, enrollmentSources);
                payload.setFederalTaxID(null);
                break;
            case FEDERAL_TAX_ID_MISSING_FOR_CREDIT_CHECK_COMM_TC_228:
                payload.setCustomerBusinessName(FakerDataGenerator.generateString(10));
                payload.setCreditCheckOption(COMMERCIAL_CREDIT_CHECK_REQUIRED.getValue());
                payload.setFederalTaxID(null);
                break;
            case EMAIL_ADDRESS_LENGTH_VALIDATION_TC_229:
                payload.setEmailAddress(FakerDataGenerator.generateString(101));
                break;
            case INVALID_EMAIL_FORMAT_TC_230:
                payload.setEmailAddress(FakerDataGenerator.generateString(5)+"."+FakerDataGenerator.generateString(5));
                break;
            case AGLC_ACCOUNT_NUMBER_LENGTH_VALIDATION_TC_231:
                payload.setAglcAccountNumber(FakerDataGenerator.generateDigits(21));
                break;
            case AGLC_ACCOUNT_NUMBER_NON_NUMERIC_TC_232:
                payload.setAglcAccountNumber(FakerDataGenerator.generateString(8));
                break;
            case INVALID_CUSTOMER_BUSINESS_NAME_LENGTH_TC_212:
                setCommercialPayloadForNegativeTCs(payload, enrollmentSources);
                payload.setCustomerBusinessName(FakerDataGenerator.generateString(61));
                break;
            case AUTHORIZED_BY_LENGTH_VALIDATION_TC_236:
                setCommercialPayloadForNegativeTCs(payload, enrollmentSources);
                payload.setAuthorizedBy(FakerDataGenerator.generateString(91));
                break;
            case AUTHORIZED_BY_NULL_TC_237:
                setCommercialPayloadForNegativeTCs(payload, enrollmentSources);
                payload.setAuthorizedBy(null);
                break;
            case REFERRAL_CODE_LENGTH_VALIDATION_TC_238:
                payload.setMarketingPromotionCode(INNVALID_MARKETING_PROMOTION_CODE.getValue());
                payload.setReferralCode(FakerDataGenerator.generateDigits(10));
                break;
            case REFERRAL_CODE_BEGINNING_WITH_A_LETTER_TC_239:
                payload.setMarketingPromotionCode(INNVALID_MARKETING_PROMOTION_CODE.getValue());
                payload.setReferralCode(FakerDataGenerator.generateDigits(9));
                break;
            case MARKETING_PROMOTION_CODE_MISSING_TC_241:
                payload.setMarketingPromotionCode(null);
                payload.setReferralCode(FakerDataGenerator.generateString(1)+FakerDataGenerator.generateDigits(8));
                break;
            case REFERRAL_CODE_ALPHANUMERIC_TC_240:
                payload.setMarketingPromotionCode(INNVALID_MARKETING_PROMOTION_CODE.getValue());
                payload.setReferralCode(FakerDataGenerator.generateAlphanumeric(9));
                break;
            case PREMISES_STREET_NUMBER_LENGTH_VALIDATION_TC_242:
                payload.setPremisesStreetNumber(FakerDataGenerator.generateDigits(13));
                break;
            case PREMISES_STREET_PRE_DIRECTION_LENGTH_VALIDATION_TC_243:
                payload.setPremisesStreetPreDirection(FakerDataGenerator.generateString(3));
                break;
            case INVALID_PREMISES_STREET_PRE_DIRECTION_TC_243_2:
                payload.setPremisesStreetPreDirection(INVALID_STREET_PREDIRECTION.getValue());
                break;
            case PREMISES_STREET_NAME_LENGTH_VALIDATION_TC_244:
                payload.setPremisesStreetName(FakerDataGenerator.generateString(31));
                break;
            case NULL_PREMISES_STREET_NAME_TC_245:
                payload.setPremisesStreetName(null);
                break;
            case PREMISES_STREET_SUFFIX_LENGTH_VALIDATION_TC_246:
                payload.setPremisesStreetSuffix(FakerDataGenerator.generateString(7));
                break;
            case INVALID_PREMISES_STREET_SUFFIX_TC_246A:
                payload.setPremisesStreetSuffix(INVALID_STREET_SUFFIX.getValue());
                break;
            case PREMISES_STREET_POST_DIRECTION_LENGTH_VALIDATION_TC_247:
                payload.setPremisesStreetPostDirection(FakerDataGenerator.generateString(3));
                break;
            case INVALID_PREMISES_STREET_POST_DIRECTION_TC_247A:
                payload.setPremisesStreetPostDirection(INVALID_STREET_POST_DIRECTION.getValue());
                break;
            case PREMISES_UNIT_TYPE_LENGTH_VALIDATION_TC_248:
                payload.setPremisesUnitType(FakerDataGenerator.generateString(7));
                break;
            case INVALID_PREMISES_UNIT_TYPE_TC_248A:
                payload.setPremisesUnitType(FakerDataGenerator.generateString(6));
                break;
            case PREMISES_UNIT_NUMBER_LENGTH_VALIDATION_TC_249:
                payload.setPremisesUnitNumber(FakerDataGenerator.generateDigits(7));
                break;
            case PREMISES_CITY_LENGTH_VALIDATION_TC_250:
                payload.setPremisesCity(FakerDataGenerator.generateString(21));
                break;
            case PREMISES_CITY_NULL_TC_251:
                payload.setPremisesCity(null);
                break;
            case PREMISES_STATE_CODE_LENGTH_VALIDATION_TC_252:
                payload.setPremisesStateCode(FakerDataGenerator.generateString(4));
                break;
            case NULL_PREMISES_STATE_CODE_TC_253:
                payload.setPremisesStateCode(null);
                break;
            case NULL_PREMISES_ZIP_CODE_TC_255:
                payload.setPremisesZipCode(null);
                break;
            case INVALID_BOOLEAN_VALUE_SEPARATE_BILLING_ADDRESS_TC_258:
                payload.setSeparateBillingAddress(FakerDataGenerator.generateString(1));
                break;
            case EMPTY_SEPARATE_BILLING_ADDRESS_TC_259:
                payload.setSeparateBillingAddress("");
                break;
            case BILLING_ADDRESS_TYPE_LENGTH_VALIDATION_TC_260:
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType(FakerDataGenerator.generateString(2));
                break;
            case NULL_BILLING_ADDRESS_TYPE_TC_261:
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType(null);
                break;
            case INVALID_BILLING_ADDRESS_TYPE_TC_262:
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType(INVALID_BILLING_ADDRESS_TYPE.getValue());
                break;
            case BILLING_STREET_NAME_LENGTH_VALIDATION_TC_263:
                payload.setBillingStreetName(FakerDataGenerator.generateString(31));
                break;
            case BILLING_STREET_NUMBER_LENGTH_VALIDATION_TC_264:
                payload.setBillingStreetNumber(FakerDataGenerator.generateDigits(13));
                break;
            case BILLING_STREET_PREDIRECTION_LENGTH_VALIDATION_TC_265:
                payload.setBillingStreetPreDirection(FakerDataGenerator.generateString(3));
                break;
            case INVALID_BILLING_STREET_PREDIRECTION_TC_266:
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType(GlobalEnums.AddressType.STREET.getValue());
                payload.setBillingStreetNumber(FakerDataGenerator.generateDigits(5));
                payload.setBillingStreetName(FakerDataGenerator.generateString(5));
                payload.setBillingStreetPreDirection(INVALID_BILLING_PRE_DIRECTION.getValue());
                break;
            case BILLING_STREET_SUFFIX_LENGTH_VALIDATION_TC_267:
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType(GlobalEnums.AddressType.STREET.getValue());
                payload.setBillingStreetNumber(FakerDataGenerator.generateDigits(5));
                payload.setBillingStreetName(FakerDataGenerator.generateString(5));
                payload.setBillingStreetSuffix(FakerDataGenerator.generateString(7));
                break;
            case INVALID_BILLING_STREET_SUFFIX_TC_268:
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType(GlobalEnums.AddressType.STREET.getValue());
                payload.setBillingStreetNumber(FakerDataGenerator.generateDigits(5));
                payload.setBillingStreetName(FakerDataGenerator.generateString(5));
                payload.setBillingStreetSuffix(INVALID_STREET_SUFFIX.getValue());
                break;
            case BILLING_STREET_POST_DIRECTION_LENGTH_VALIDATION_TC_269:
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType(GlobalEnums.AddressType.STREET.getValue());
                payload.setBillingStreetNumber(FakerDataGenerator.generateDigits(5));
                payload.setBillingStreetName(FakerDataGenerator.generateString(5));
                payload.setBillingStreetSuffix(FakerDataGenerator.generateString(6));
                payload.setBillingStreetPostDirection(FakerDataGenerator.generateString(3));
                break;
            case INVALID_BILLING_STREET_POST_DIRECTION_TC_270:
                payload.setSeparateBillingAddress(true);
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setBillingAddressType(GlobalEnums.AddressType.STREET.getValue());
                payload.setBillingStreetNumber(FakerDataGenerator.generateDigits(5));
                payload.setBillingStreetName(FakerDataGenerator.generateString(5));
                payload.setBillingStreetPostDirection(INVALID_STREET_POST_DIRECTION.getValue());
                break;
            case BILLING_UNIT_TYPE_LENGTH_VALIDATION_TC_271:
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType(GlobalEnums.AddressType.STREET.getValue());
                payload.setBillingStreetNumber(FakerDataGenerator.generateDigits(5));
                payload.setBillingStreetName(FakerDataGenerator.generateString(5));
                payload.setBillingUnitType(FakerDataGenerator.generateString(7));
                break;
            case INVALID_BILLING_UNIT_TYPE_TC_272:
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType(GlobalEnums.AddressType.STREET.getValue());
                payload.setBillingStreetNumber(FakerDataGenerator.generateDigits(5));
                payload.setBillingStreetName(FakerDataGenerator.generateString(5));
                payload.setBillingUnitType(INVALID_BILLING_UNIT_TYPE.getValue());
                break;
            case BILLING_UNIT_NUMBER_LENGTH_VALIDATION_TC_273:
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType(GlobalEnums.AddressType.STREET.getValue());
                payload.setBillingStreetNumber(FakerDataGenerator.generateDigits(5));
                payload.setBillingStreetName(FakerDataGenerator.generateString(5));
                payload.setBillingUnitType(KEY.getValue());
                payload.setBillingUnitNumber(FakerDataGenerator.generateDigits(7));
                break;
            case BILLING_RURAL_ROUTE_LENGTH_VALIDATION_TC_274:
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType(GlobalEnums.AddressType.RURAL.getValue());
                payload.setBillingRuralRoute(FakerDataGenerator.generateDigits(21));
                break;
            case NULL_BILLING_RURAL_ROUTE_TC_274A:
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType(GlobalEnums.AddressType.RURAL.getValue());
                payload.setBillingRuralRoute("");
                break;
            case BILLING_RURAL_ROUTE_NUMBER_LENGTH_VALIDATION_TC_275:
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType(GlobalEnums.AddressType.RURAL.getValue());
                payload.setBillingRuralRouteNumber(FakerDataGenerator.generateDigits(11));
                break;
            case NULL_BILLING_RURAL_ROUTE_NUMBER_TC_275A:
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType(GlobalEnums.AddressType.RURAL.getValue());
                payload.setBillingRuralRoute(FakerDataGenerator.generateString(8));
                payload.setBillingRuralRouteNumber(null);
                break;
            case BILLING_PO_BOX_LENGTH_VALIDATION_TC_276:
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType(GlobalEnums.AddressType.POBOX.getValue());
                payload.setBillingPOBox(FakerDataGenerator.generateDigits(11));
                break;
            case NULL_BILLING_PO_BOX_TC_276A:
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType(GlobalEnums.AddressType.POBOX.getValue());
                payload.setBillingPOBox("");
                break;
            case BILLING_ADDRESS_LINE_LENGTH_VALIDATION_TC_277:
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType(GlobalEnums.AddressType.STREET.getValue());
                payload.setBillingAddressLine2(FakerDataGenerator.generateString(31));
                break;
            case BILLING_ADDRESS_LINE_LENGTH_VALIDATION_FOR_RURAL_TC_277A:
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType(GlobalEnums.AddressType.RURAL.getValue());
                payload.setBillingRuralRouteNumber(FakerDataGenerator.generateDigits(8));
                payload.setBillingRuralRoute(FakerDataGenerator.generateString(8));
                payload.setBillingAddressLine2(FakerDataGenerator.generateString(31));
                break;
            case BILLLING_ADDRESS_LINE_LENGTH_VALIDATION_FOR_POBOX_TC_277B:
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType(GlobalEnums.AddressType.POBOX.getValue());
                payload.setBillingPOBox(FakerDataGenerator.generateDigits(3));
                payload.setBillingAddressLine2(FakerDataGenerator.generateString(31));
                break;
            case BILLING_CITY_LENGTH_VALIDATION_TC_278:
                payload.setSeparateBillingAddress(true);
                payload.setBillingCity(FakerDataGenerator.generateString(21));
                payload.setBillingAddressType(GlobalEnums.AddressType.STREET.getValue());
                break;
            case BILLING_CITY_LENGTH_VALIDATION_POBOX_TC_278B:
                payload.setSeparateBillingAddress(true);
                payload.setBillingCity(FakerDataGenerator.generateString(21));
                payload.setBillingAddressType(GlobalEnums.AddressType.POBOX.getValue());
                payload.setBillingPOBox(FakerDataGenerator.generateDigits(3));
                break;
            case BILLING_CITY_LENGTH_VALIDATION_RURAL_TC_278A:
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType(GlobalEnums.AddressType.RURAL.getValue());
                payload.setBillingRuralRouteNumber(FakerDataGenerator.generateDigits(8));
                payload.setBillingRuralRoute(FakerDataGenerator.generateString(8));
                payload.setBillingCity(FakerDataGenerator.generateString(21));
                break;
            case BILLING_STATE_CODE_LENGTH_VALIDATION_TC_279:
                payload.setSeparateBillingAddress(true);
                payload.setBillingCity(FakerDataGenerator.generateCity());
                payload.setBillingStateCode(FakerDataGenerator.generateString(4));
                payload.setBillingAddressType(GlobalEnums.AddressType.STREET.getValue());
                break;
            case Billing_STATE_CODE_LENGTH_VALIDATION_RURAL_TC_279A:
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType(GlobalEnums.AddressType.RURAL.getValue());
                payload.setBillingRuralRouteNumber(FakerDataGenerator.generateDigits(8));
                payload.setBillingRuralRoute(FakerDataGenerator.generateString(8));
                payload.setBillingCity(FakerDataGenerator.generateCity());
                payload.setBillingStateCode(FakerDataGenerator.generateString(4));
                break;
            case BILLING_STATE_CODE_LENGTH_VALIDATION_POBOX_TC_279B:
                payload.setSeparateBillingAddress(true);
                payload.setBillingCity(FakerDataGenerator.generateCity());
                payload.setBillingStateCode(FakerDataGenerator.generateString(4));
                payload.setBillingAddressType(GlobalEnums.AddressType.POBOX.getValue());
                payload.setBillingPOBox(FakerDataGenerator.generateDigits(3));
                break;
            case INVALID_BILLING_STATE_CODE_TC_280:
                payload.setSeparateBillingAddress(true);
                payload.setBillingCity(FakerDataGenerator.generateCity());
                payload.setBillingStateCode(INVALID_BILLING_STATE_CODE.getValue());
                payload.setBillingAddressType(GlobalEnums.AddressType.STREET.getValue());
                break;
            case INVALID_BILLING_STATE_CODE_RURAL_TC_280A:
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType(GlobalEnums.AddressType.RURAL.getValue());
                payload.setBillingRuralRouteNumber(FakerDataGenerator.generateDigits(8));
                payload.setBillingRuralRoute(FakerDataGenerator.generateString(8));
                payload.setBillingCity(FakerDataGenerator.generateCity());
                payload.setBillingStateCode(INVALID_BILLING_STATE_CODE.getValue());
                break;
            case INVALID_BILLING_STATE_CODE_POBOX_TC_280B:
                payload.setSeparateBillingAddress(true);
                payload.setBillingCity(FakerDataGenerator.generateCity());
                payload.setBillingStateCode(INVALID_BILLING_STATE_CODE.getValue());
                payload.setBillingAddressType(GlobalEnums.AddressType.POBOX.getValue());
                payload.setBillingPOBox(FakerDataGenerator.generateDigits(3));
                break;
            case INVALID_BILLING_ZIP_CODE_TC_281_1:
                payload.setSeparateBillingAddress(true);
                payload.setBillingStreetName(FakerDataGenerator.generateString(5));
                payload.setBillingCity(FakerDataGenerator.generateCity());
                payload.setBillingStateCode(billingAddressState);
                payload.setBillingZipCode(INVALID_ZIP_9_DIGIT.getValue());
                payload.setBillingAddressType(GlobalEnums.AddressType.STREET.getValue());
                break;
            case NULL_BILLING_ZIP_CODE_TC_281C:
                payload.setSeparateBillingAddress(true);
                payload.setBillingCity(FakerDataGenerator.generateCity());
                payload.setBillingStateCode(billingAddressState);
                payload.setBillingZipCode(null);
                payload.setBillingAddressType(GlobalEnums.AddressType.STREET.getValue());
                payload.setBillingStreetName(FakerDataGenerator.generateString(5));
                break;
            case NULL_BILLING_ZIP_CODE_RURAL_TC_281D:
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType(GlobalEnums.AddressType.RURAL.getValue());
                payload.setBillingRuralRouteNumber(FakerDataGenerator.generateDigits(8));
                payload.setBillingRuralRoute(FakerDataGenerator.generateString(8));
                payload.setBillingCity(FakerDataGenerator.generateCity());
                payload.setBillingStateCode(billingAddressState);
                payload.setBillingZipCode(null);
                break;
            case NULL_BILLING_ZIP_CODE_POBOX_TC_281E:
                payload.setSeparateBillingAddress(true);
                payload.setBillingCity(FakerDataGenerator.generateCity());
                payload.setBillingStateCode(billingAddressState);
                payload.setBillingAddressType(GlobalEnums.AddressType.POBOX.getValue());
                payload.setBillingZipCode(null);
                payload.setBillingPOBox(FakerDataGenerator.generateDigits(3));
                break;
            case BILLING_COUNTY_CODE_LENGTH_VALIDATION_TC_282:
                payload.setSeparateBillingAddress(true);
                payload.setBillingCity(FakerDataGenerator.generateCity());
                payload.setBillingStateCode(billingAddressState);
                payload.setBillingZipCode(FakerDataGenerator.generateDigits(5));
                payload.setBillingAddressType(GlobalEnums.AddressType.STREET.getValue());
                payload.setBillingStreetName(FakerDataGenerator.generateString(5));
                payload.setBillingCountyCode(FakerDataGenerator.generateAlphanumeric(6));
                break;

            case BILLING_COUNTY_CODE_RURAL_LENGTH_VALIDATION_TC_282A:
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType(GlobalEnums.AddressType.RURAL.getValue());
                payload.setBillingRuralRouteNumber(FakerDataGenerator.generateDigits(8));
                payload.setBillingRuralRoute(FakerDataGenerator.generateString(8));
                payload.setBillingCity(FakerDataGenerator.generateCity());
                payload.setBillingStateCode(billingAddressState);
                payload.setBillingZipCode(FakerDataGenerator.generateDigits(5));
                payload.setBillingCountyCode(FakerDataGenerator.generateAlphanumeric(6));
                break;

            case BILLING_COUNTY_CODE_POBOX_LENGTH_VALIDATION_TC_282B:
                payload.setSeparateBillingAddress(true);
                payload.setBillingCity(FakerDataGenerator.generateCity());
                payload.setBillingStateCode(billingAddressState);
                payload.setBillingAddressType(GlobalEnums.AddressType.POBOX.getValue());
                payload.setBillingZipCode(null);
                payload.setBillingPOBox(FakerDataGenerator.generateDigits(3));
                payload.setBillingCountyCode(FakerDataGenerator.generateAlphanumeric(6));
                break;

            case INVALID_BILLING_COUNTY_CODE_TC_283:
                payload.setSeparateBillingAddress(true);
                payload.setBillingCity(FakerDataGenerator.generateCity());
                payload.setBillingStateCode(billingAddressState);
                payload.setBillingZipCode(validZipCode);
                payload.setBillingAddressType(GlobalEnums.AddressType.STREET.getValue());
                payload.setBillingStreetName(FakerDataGenerator.generateString(5));
                payload.setBillingCountyCode(FakerDataGenerator.generateAlphanumeric(5));
                break;

            case INVALID_BILLING_COUNTY_CODE_RURAL_TC_283A:
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType(GlobalEnums.AddressType.RURAL.getValue());
                payload.setBillingRuralRouteNumber(FakerDataGenerator.generateDigits(8));
                payload.setBillingRuralRoute(FakerDataGenerator.generateString(8));
                payload.setBillingCity(FakerDataGenerator.generateCity());
                payload.setBillingStateCode(billingAddressState);
                payload.setBillingZipCode(null);
                payload.setBillingCountyCode(FakerDataGenerator.generateAlphanumeric(5));
                break;

            case INVALID_BILLING_POBOX_COUNTY_CODE_TC_283B:
                payload.setSeparateBillingAddress(true);
                payload.setBillingCity(FakerDataGenerator.generateCity());
                payload.setBillingStateCode(billingAddressState);
                payload.setBillingAddressType(GlobalEnums.AddressType.POBOX.getValue());
                payload.setBillingZipCode(null);
                payload.setBillingPOBox(FakerDataGenerator.generateDigits(3));
                payload.setBillingCountyCode(FakerDataGenerator.generateAlphanumeric(5));
                break;

            case INVALID_BILLING_ZIP_CODE_TC_281_2:
                payload.setSeparateBillingAddress(true);
                payload.setBillingCity(FakerDataGenerator.generateCity());
                payload.setBillingStateCode(billingAddressState);
                payload.setBillingZipCode(FakerDataGenerator.generateDigits(4));
                payload.setBillingAddressType(GlobalEnums.AddressType.STREET.getValue());
                break;
            case INVALID_BILLING_ZIP_CODE_POBOX_TC_281B_1:
                payload.setSeparateBillingAddress(true);
                payload.setBillingCity(FakerDataGenerator.generateCity());
                payload.setBillingStateCode(billingAddressState);
                payload.setBillingAddressType(GlobalEnums.AddressType.POBOX.getValue());
                payload.setBillingPOBox(FakerDataGenerator.generateDigits(3));
                payload.setBillingZipCode(FakerDataGenerator.generateDigits(4)+"-"+FakerDataGenerator.generateDigits(5));
                break;
            case INVALID_BILLING_ZIP_CODE_POBOX_TC_281B_2:
                payload.setSeparateBillingAddress(true);
                payload.setBillingCity(FakerDataGenerator.generateCity());
                payload.setBillingStateCode(billingAddressState);
                payload.setBillingAddressType(GlobalEnums.AddressType.POBOX.getValue());
                payload.setBillingPOBox(FakerDataGenerator.generateDigits(3));
                payload.setBillingZipCode(FakerDataGenerator.generateDigits(4));
                break;
            case INVALID_BILLING_ZIP_CODE_RURAL_TC_281A_2:
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType(GlobalEnums.AddressType.RURAL.getValue());
                payload.setBillingRuralRouteNumber(FakerDataGenerator.generateDigits(8));
                payload.setBillingRuralRoute(FakerDataGenerator.generateString(8));
                payload.setBillingCity(FakerDataGenerator.generateCity());
                payload.setBillingZipCode(FakerDataGenerator.generateDigits(4));
                payload.setBillingStateCode(billingAddressState);
                break;
            case INVALID_BILLING_ZIP_CODE_RURAL_TC_281A_1:
                payload.setSeparateBillingAddress(true);
                payload.setBillingAddressType(GlobalEnums.AddressType.RURAL.getValue());
                payload.setBillingRuralRouteNumber(FakerDataGenerator.generateDigits(8));
                payload.setBillingRuralRoute(FakerDataGenerator.generateString(8));
                payload.setBillingCity(FakerDataGenerator.generateCity());
                payload.setBillingZipCode(FakerDataGenerator.generateDigits(4)+"-"+FakerDataGenerator.generateDigits(5));
                payload.setBillingStateCode(billingAddressState);
                break;
            case PREMISES_COUNTY_CODE_LENGTH_VALIDATION_TC_256:
                payload.setPremisesCountyCode(FakerDataGenerator.generateString(6));
                break;
            case INVALID_PREMISES_COUNTY_CODE_TC_256A:
                payload.setPremisesCountyCode(INVALID_PREMISES_COUNTY_CODE.getValue());
                break;
            case NULL_PREMISES_COUNTY_CODE_TC_257:
                payload.setPremisesCountyCode(null);
                break;
            case PREMISES_ZIP_CODE_NOT_A_NUMERIC_VALUE_OF_LENGTH_5_TC_255A:
                payload.setPremisesZipCode(FakerDataGenerator.generateDigits(4));
                break;
            case PREMISES_ZIP_CODE_LENGTH_VALIDATION_TC_254:
                payload.setPremisesZipCode(FakerDataGenerator.generateDigits(11));
                break;
            case INVALID_PREMISES_ZIP_FORMAT_TC_255B:
                payload.setPremisesZipCode(FakerDataGenerator.generateDigits(4)+"-"+FakerDataGenerator.generateDigits(4));
                break;
            case NON_NUMERIC_PREMISES_ZIP_CODE_TC_255_2:
                payload.setPremisesZipCode(FakerDataGenerator.generateString(5));
                break;
            case INVALID_PREMISES_ZIP_CODE_TC_255C:
                payload.setPremisesZipCode(INVALID_ZIP_CODE.getValue());
                break;
            case INVALID_PREMISES_STATE_CODE_TC_253_2:
                payload.setPremisesStateCode(INVALID_PREMISE_STATE_CODE.getValue());
                break;
            case CUSTOMER_BUSINESS_NAME_NULL_TC_213:
                setCommercialPayloadForNegativeTCs(payload, enrollmentSources);
                payload.setCustomerBusinessName(null);
                break;
            case CUSTOMER_BUSINESS_NAME_COMM_CREDIT_CHECK_214:
                payload.setEnrollmentSource(MAIL.getValue());
                payload.setCustomerBusinessName(null);
                payload.setCreditCheckOption(COMMERCIAL_CREDIT_CHECK_REQUIRED.getValue());
                break;
            case CREDIT_CHECK_BUSINESS_NAME_LENGTH_VALIDATION_TC_215:
                payload.setEnrollmentSource(MAIL.getValue());
                payload.setCreditCheckBusinessName(FakerDataGenerator.generateString(61));
                break;
            case GENERATION_CODE_LENGTH_VALIDATION_TC_218:
                payload.setGenerationCode(FakerDataGenerator.generateString(4));
                break;
            case INVALID_GENERATION_CODE_TC_219:
                payload.setGenerationCode(INVALID_GENERATION_CODE.getValue());
                break;
            case CUSTOMER_MIDDLE_NAME_LENGTH_VALIDATION_TC_220:
                payload.setCustomerMiddleName(FakerDataGenerator.generateString(16));
                break;
            case CUSTOMER_FIRST_NAME_LENGTH_VALIDATION_TC_221:
                payload.setCustomerFirstName(FakerDataGenerator.generateString(16));
                break;
            case CUSTOMER_BUSINESS_NAME_NULL_TC_215A:
                setCommercialPayloadForNegativeTCs(payload, enrollmentSources);
                payload.setCreditCheckBusinessName(FakerDataGenerator.generateString(15));
                payload.setCustomerBusinessName(null);
                break;
            case INVALID_LENGTH_CUSTOMER_LAST_NAME_TC_216:
                payload.setCustomerLastName(FakerDataGenerator.generateString(61));
                break;
            case SSN_INVALID_LENGTH_TC_223:
                payload.setSocialSecurityNumber(encryptData(FakerDataGenerator.generateDigits(10)));
                break;
            case NON_NUMERIC_SSN_TC_224:
                payload.setSocialSecurityNumber(encryptData(FakerDataGenerator.generateString(9)));
                break;
            case CUSTOMER_FIRST_AND_MIDDLE_NAME_INVALID_LENGTH_TC_222:
                payload.setCustomerFirstName(FakerDataGenerator.generateString(16));
                payload.setCustomerMiddleName(FakerDataGenerator.generateString(16));
                break;
            case AGLC_SERVICE_LOCATION_ID_NULL_TC_233:
                payload.setAglcServiceLocationID(null);
                break;
            case AGLC_SERVICE_LOCATION_ID_LENGTH_VALIDATION_TC_234:
                payload.setAglcServiceLocationID(FakerDataGenerator.generateDigits(11));
                break;
            case NON_NUMERIC_AGLC_SERVICE_LOCATION_ID_TC_235:
                payload.setAglcServiceLocationID(FakerDataGenerator.generateString(9));
                break;
            case INVALID_MARKETING_PROMOTION_CODE_FOR_ENROLLMENT_SOURCE_TC_203:
                payload.setMarketingPromotionCode(PROMOTION_CODE_GREEN_LIFE.getValue());
                payload.setEnrollmentSource(MAIL.getValue());
                break;
            case INVALID_LENGTH_CALLER_ID_TC_204:
                payload.setCallerIDNotAvailable(false);
                payload.setCallerID(FakerDataGenerator.generateDigits(11));
                payload.setEnrollmentSource(PHONECALL.getValue());
                break;
            case NON_NUMERIC_CALLER_ID_TC_205:
                payload.setCallerIDNotAvailable(false);
                payload.setCallerID(FakerDataGenerator.generateString(5));
                payload.setEnrollmentSource(PHONECALL.getValue());
                break;
            case CALLER_ID_MISSING_TC_206:
                payload.setCallerIDNotAvailable(false);
                payload.setCallerID(null);
                payload.setEnrollmentSource(PHONECALL.getValue());
                break;
            case INVALID_VALUE_FOR_CALLER_ID_NOT_AVAILABLE_TC_207:
                payload.setCallerIDNotAvailable(FakerDataGenerator.generateString(3));
                payload.setCallerID(FakerDataGenerator.generateDigits(10));
                payload.setEnrollmentSource(PHONECALL.getValue());
                break;
            case CALLER_ID_NOT_AVAILABLE_TRUE_BUT_VALUE_IS_PROVIDED_TC_208:
                payload.setCallerIDNotAvailable(true);
                payload.setCallerID(FakerDataGenerator.generateDigits(10));
                payload.setEnrollmentSource(PHONECALL.getValue());
                break;
            case ADDITIONAL_ENROLLMENT_DATA_LENGTH_VALIDATION_TC_209:
                payload.setAdditionalEnrollmentData(FakerDataGenerator.generateString(31));
                break;
            case SSP_INDICATOR_NOT_PROVIDED_TC_210:
                payload.setSeasonalSavingsProgramIndicator(null);
                break;
            case INVALID_SSP_INDICATOR_VALUE_TC_211:
                payload.setSeasonalSavingsProgramIndicator(FakerDataGenerator.generateString(3));
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
        Map<String, String> customerData = loadRowFromExcelToCustomerData(EXPERIAN_DATA, EXPERIAN_SHEET_NAME, testCondition);
        parseAddress(payload, customerData.getOrDefault("BUSINESS STREET ADDRESS", ""));
        populateCommonFields(payload, customerData);

        switch (testCondition) {
            case COMMERCIAL_CREDIT_CHECK_YES_TC_339, COMMERCIAL_CREDIT_CHECK_YES_INCL_ENROLLMENT_TC_350:
                parseAddress(payload, customerData.getOrDefault("BUSINESS STREET ADDRESS", ""));
                break;

            case COMMERCIAL_CREDIT_CHECK_YES_NEW_ENROLLMENT_TC_340, COMMERCIAL_CREDIT_CHECK_YES_CRDS_ENROLLMENT_TC_350B:
                parseAddress(payload, customerData.getOrDefault("BUSINESS STREET ADDRESS", ""));
                payload.setEnrollmentSource(FAX.getValue());
                break;

            case COMMERCIAL_CREDIT_CHECK_SKIP_NEW_ENROLLMENT_TC_341:
                payload.setEnrollmentSource(WEB.getValue());
                payload.setCreditCheckOption(GlobalEnums.CreditCheckOption.CREDIT_CHECK_NOT_REQUIRED.getValue());
                break;

            case COMMERCIAL_CREDIT_CHECK_YES_NEW_ENROLLMENT_TC_342:
                payload.setEmailAddress(FakerDataGenerator.generateEmail());
                break;

            case COMMERCIAL_CREDIT_CHECK_YES_NEW_ENROLLMENT_TC_343:
                parseAddress(payload, customerData.getOrDefault("BUSINESS STREET ADDRESS", ""));
                payload.setEnrollmentSource(FAX.getValue());
                parseBillingAddress(payload, customerData.getOrDefault("BILLING ADDRESS", ""));
                parsePhoneDetails(payload, customerData.getOrDefault("PHONE NUMBER DETAILS", ""));
                payload.setSeasonalSavingsProgramIndicator(true);
                payload.setBillingCity(customerData.get("BILLING CITY"));
                payload.setBillingStateCode(customerData.get("BILLING STATE"));
                payload.setBillingZipCode(customerData.get("BILLING ZIP"));
                payload.setBillingCountyCode(customerData.get("BILLING COUNTRY CODE"));
                break;

            case COMMERCIAL_CREDIT_CHECK_SERV_TRANSFER_NEW_ENROLLMENT_TC_344:
                parseAddress(payload, customerData.getOrDefault("BUSINESS STREET ADDRESS", ""));
                payload.setEnrollmentSource(GNGHUB.getValue());
                payload.setCreditCheckOption(SERVICE_TRANSFER.getValue());
                break;

            case COMMERCIAL_CREDIT_CHECK_YES_NEW_ENROLLMENT_TC_346:
                parseAddress(payload, customerData.getOrDefault("BUSINESS STREET ADDRESS", ""));
                payload.setCreditCheckOption(NO.getValue());
                break;

            case COMMERCIAL_CREDIT_CHECK_MULT_NEW_ENROLLMENT_TC_345:
                parseAddress(payload, customerData.getOrDefault("BUSINESS STREET ADDRESS", ""));
                payload.setCreditCheckOption(MULTIPLE_PREMISES_OWNER.getValue());
                break;

            case COMMERCIAL_CREDIT_CHECK_YES_NEW_ENROLLMENT_TC_347:
                parseAddress(payload, customerData.getOrDefault("BUSINESS STREET ADDRESS", ""));
                break;

            case COMMERCIAL_CREDIT_CHECK_YES_NEW_ENROLLMENT_TC_348:
                payload.setMarketingPromotionCode(DEALS.getValue());
                parseAddressWithUnit(payload, customerData.getOrDefault("BUSINESS STREET ADDRESS", ""));
                payload.setEmailAddress(FakerDataGenerator.generateEmail());
                payload.setPremisesStreetSuffix("");
                break;

            case COMMERCIAL_CREDIT_CHECK_YES_NEW_ENROLLMENT_TC_349:
                parseAddressWithUnit(payload, customerData.getOrDefault("BUSINESS STREET ADDRESS", ""));
                payload.setEmailAddress(FakerDataGenerator.generateEmail());
                payload.setPremisesStreetSuffix("");
                payload.setPremisesUnitType(premisesUnitType);
                payload.setPremisesUnitNumber(FakerDataGenerator.generateDigits(3));
                break;

            case COMMERCIAL_CREDIT_CHECK_YES_CRDS_ENROLLMENT_TC_350E:
                parseAddress(payload, customerData.getOrDefault("BUSINESS STREET ADDRESS", ""));
                payload.setCreditCheckBusinessName(customerData.get("BUSINESS NAME"));
                break;
        }
    }

    public void setCustomerLastNameBasedOnType(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel customerLastName) {
        switch (customerLastName) {
//            case SPL_CHAR_CUSTOMER_LAST_NAME:
//                payload.setRequestID(FakerDataGenerator.generateString(10));
//                payload.setCustomerLastName(FakerDataGenerator.generateAlphanumericWithSpecialChars(2));
//                break;
            case EMPTY_CUSTOMER_LAST_NAME_TC_217:
                payload.setRequestID(FakerDataGenerator.generateString(10));
                payload.setCustomerLastName(null);
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
        payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
        switch (creditCheckOption) {
            case EMPTY_CREDIT_CHECK_OPTION_TC_312:
                payload.setCreditCheckOption("");
                break;
            case INVALID_CREDIT_CHECK_OPTION_TC_311:
                payload.setCreditCheckOption(INVALID_CREDIT_CHECK_OPTION.getValue());
                break;
            case MAX_LENGTH_CREDIT_CHECK_OPTION_TC_310:
                payload.setCreditCheckOption(FakerDataGenerator.getRandomString(33));
                break;
            default:
                payload.setCreditCheckOption(FakerDataGenerator.getRandomString(32));
        }
    }

    public void setInitialCreditCheckCustomerCodeBasedOnTypeTC313_317(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel initialCreditCheckCustomerCode) {
        payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
        switch (initialCreditCheckCustomerCode) {
            case EMPTY_INITIAL_CREDIT_CHECK_CUSTOMER_CODE_WITH_CREDIT_CHECK_OPTION_315:
                payload.setCreditCheckOption(MULTIPLE_PREMISES_OWNER.getValue());
                payload.setInitialCreditCheckCustomerCode(null);
                break;
            case MAX_LENGTH_INITIAL_CREDIT_CHECK_CUSTOMER_CODE_313:
                payload.setInitialCreditCheckCustomerCode(FakerDataGenerator.getRandomNumericString(10));
                break;
            case NONNUMERIC_INITIAL_CREDIT_CHECK_CUSTOMER_CODE_314:
                payload.setCreditCheckOption(YES.getValue());
                payload.setInitialCreditCheckCustomerCode(FakerDataGenerator.generateUpperCaseString(7));
                break;
            case INVALID_INITIAL_CREDIT_CHECK_CUSTOMER_CODE_NOT_PRESENT_IN_TABLE_316:
                payload.setCreditCheckOption(MULTIPLE_PREMISES_OWNER.getValue());
                payload.setInitialCreditCheckCustomerCode(INVALID_INITIAL_CREDIT_CHECK_CUSTOMER_CODE.getValue());
                break;
            case INVALID_INITIAL_CREDIT_CHECK_CUSTOMER_CODE_WITHOUT_CREDIT_SCORE_317:
                Map<String, String> customerData = loadRowFromExcelToCustomerData(CUSTOMER_DATA, CUSTOMER_SHEET_NAME, initialCreditCheckCustomerCode);
                payload.setCustomerLastName(customerData.get("customerLastName"));
                payload.setCreditCheckOption(MULTIPLE_PREMISES_OWNER.getValue());
                payload.setGenerationCode(null);
                payload.setCustomerFirstName(customerData.get("customerFirstName"));
                payload.setSocialSecurityNumber(encryptData(customerData.get("SSN")));
                payload.setInitialCreditCheckCustomerCode(customerData.get("CreditCheckCustomerCode"));
                break;
            default:

                payload.setInitialCreditCheckCustomerCode(FakerDataGenerator.getRandomString(8));
        }
    }

    public void setTenantLandlordBasedOnType(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel tenantLandlord) {
        switch (tenantLandlord) {
            case EMPTY_TENANT_LANDLORD:
                payload.setTenantLandlord(" ");
            case SPL_CHAR_TENANT_LANDLORD:
                payload.setTenantLandlord(FakerDataGenerator.generateAlphanumericWithSpecialChars(5));
                break;
            case MAX_LENGTH_TENANT_LANDLORD:
                payload.setTenantLandlord(FakerDataGenerator.getRandomString(4));
                break;
            case LOWERCASE_TENANT_LANDLORD:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
                payload.setTenantLandlord(FakerDataGenerator.generateAlphanumeric(3));
                break;
            case NUMERIC_TENANT_LANDLORD:
                payload.setTenantLandlord(FakerDataGenerator.generateAlphanumeric(3));
                break;

            default:
                payload.setTenantLandlord(FakerDataGenerator.getRandomString(1));
        }
    }

    public void setWorkPhoneNumberBasedOnType284_286(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel workphonenumber) {
        payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
        switch (workphonenumber) {
            case MIN_LENGTH_WORK_PHONE_NUMBER_TC_284:
                payload.setWorkPhoneNumber(FakerDataGenerator.getRandomNumericString(9));
                break;
            case ALPHANUMERIC_WORK_PHONE_NUMBER_TC_285:
                payload.setWorkPhoneNumber(FakerDataGenerator.generateAlphanumeric(10));
                break;
            case NULL_WORK_PHONE_NUMBER_WITH_VALID_WORK_PHONE_TYPE_TC_286:
                payload.setWorkPhoneType(LANDLINE.getValue());
                payload.setWorkPhoneNumber(null);
                break;
            default:
                payload.setWorkPhoneNumber(FakerDataGenerator.generatePhoneNumber());
        }
    }

    public void setWorkPhoneTypeBasedOnType287_290(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel workphonetype) {
        payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
        switch (workphonetype) {
            case NULL_WORK_PHONE_TYPE_WITH_VALID_WORK_PHONE_NUMBER_TC_287:
                payload.setWorkPhoneType("");
                payload.setWorkPhoneNumber(FakerDataGenerator.generateDigits(10));
                break;
            case MAX_LENGTH_WORK_EXTENSION_TYPE_TC_288:
                payload.setWorkPhoneExtension(FakerDataGenerator.getRandomNumericString(5));
                break;
            case WORK_PHONE_TYPE_PROVIDED_MAX_1_CHAR_TC_289:
                payload.setWorkPhoneType(BUSINESS.getValue());
                payload.setWorkPhoneNumber(FakerDataGenerator.generatePhoneNumber());
                payload.setWorkPhoneExtension(null);
                break;
            case INVALID_WORK_PHONE_TYPE_VALUE_TC_290:
                payload.setWorkPhoneType(INVALID_WORK_PHONE_TYPE.getValue());
                payload.setWorkPhoneNumber(FakerDataGenerator.generateDigits(10));
                payload.setWorkPhoneExtension(FakerDataGenerator.generateDigits(3));
                break;
            default:
                payload.setWorkPhoneType("L");
        }
    }

    public void setHomePhoneNumberBasedOnType291_293(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel homePhoneNumber) {
        payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
        switch (homePhoneNumber) {
            case HOME_PHONE_NUMBER_NOT_10_DIGIT_TC_291:
                payload.setHomePhoneNumber(FakerDataGenerator.generateDigits(11));
                break;
            case ALPHANUMERIC_HOME_PHONE_NUMBER_TC_292:
                payload.setHomePhoneNumber(FakerDataGenerator.generateAlphanumeric(10));
                break;
            case NULL_HOME_PHONE_NUMBER_WITH_VALID_HOME_PHONE_TYPE_TC_293:
                payload.setHomePhoneNumber(null);
                payload.setHomePhoneType(MOBILE.getValue());
                break;
            default:
                payload.setHomePhoneNumber(FakerDataGenerator.generateDigits(10));
        }
    }

    public void setHomePhoneTypeBasedOnType294_297(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel homephonetype) {
        payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
        switch (homephonetype) {
            case NULL_HOME_PHONE_TYPE_WITH_VALID_HOME_PHONE_NUMBER_294:
                payload.setHomePhoneType(null);
                payload.setHomePhoneNumber(FakerDataGenerator.generateDigits(10));
                break;
            case HOME_PHONE_TYPE_PROVIDED_MAX_1_CHAR_296:
                payload.setHomePhoneType(FakerDataGenerator.generateUpperCaseString(2));
                payload.setHomePhoneNumber(FakerDataGenerator.generateDigits(10));
                break;
            case INVALID_HOME_PHONE_TYPE_VALUE_297:
                payload.setHomePhoneType(INVALID_HOME_PHONE_TYPE.getValue());
                payload.setHomePhoneNumber(FakerDataGenerator.generateDigits(10));
                break;
            default:
                payload.setHomePhoneType(LANDLINE.getValue());
        }
    }

    public void setAcnStatusIndicatorBasedOnTypeTC298_307(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel acnStatusIndicator) {
        payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
        switch (acnStatusIndicator) {
            case EMPTY_ACN_STATUS_INDICATOR_WITH_TENANT_LANDLORD:
                payload.setAcnStatusIndicator("");
                payload.setTenantLandlord(TENANT.getValue());
                break;
            case INVALID_ACN_STATUS_INDICATOR_VALUE_NOT_PRESENT_IN_TABLE_TC_299:
                payload.setAcnStatusIndicator(FakerDataGenerator.generateString(4));
                break;
            case MAX_LENGTH_ACN_STATUS_INDICATOR_TC_298:
                payload.setAcnStatusIndicator(FakerDataGenerator.generateString(5));
                payload.setTenantLandlord(TENANT.getValue());
                break;
            case VALID_ACN_STATUS_INDICATOR_WITH_MAX_LENGTH_TENANT_LANDLORD_TC_301:
                payload.setAcnStatusIndicator(ACN.getValue());
                payload.setTenantLandlord(FakerDataGenerator.generateUpperCaseString(5));
                break;
            case INVALID_TENANT_LANDLORD_INDICATOR_TC_302:
                payload.setAcnStatusIndicator(ACN.getValue());
                payload.setTenantLandlord(INVALID_TENANT_LANDLORD.getValue());
                break;
            case VALID_ACN_STATUS_INDICATOR_WITH_NULL_TENANT_LANDLORD_TC_303:
                payload.setAcnStatusIndicator(ACN.getValue());
                payload.setTenantLandlord(null);
                break;
            case VALID_ACN_STATUS_INDICATOR_VALID_TENANT_LANDLORD_L_WITH_INVALID_USER_ROLE_TC_304:
                payload.setAcnStatusIndicator(ACN.getValue());
                payload.setTenantLandlord(LANDLORD.getValue());
                payload.setLoginID(invalidLoginIdForACN);
                break;
            case VALID_ACN_STATUS_INDICATOR_VALID_TENANT_LANDLORD_T_WITH_INVALID_USER_ROLE_TC_305:
                payload.setAcnStatusIndicator(ACN.getValue());
                payload.setTenantLandlord(TENANT.getValue());
                payload.setLoginID(invalidLoginIdForACN);
                break;
            case NULL_ACN_STATUS_INDICATOR_VALID_TENANT_LANDLORD_L_WITH_INVALID_USER_ROLE_TC_306:
                payload.setAcnStatusIndicator(null);
                payload.setTenantLandlord(LANDLORD.getValue());
                payload.setLoginID(invalidLoginIdForACN);
                break;
            case NULL_ACN_STATUS_INDICATOR_TC_300A:
                payload.setAcnStatusIndicator(null);
                payload.setTenantLandlord(LANDLORD.getValue());
                break;
            case NULL_ACN_STATUS_INDICATOR_TC_300B:
                payload.setAcnStatusIndicator(null);
                payload.setTenantLandlord(TENANT.getValue());
                break;
            case NULL_ACN_STATUS_INDICATOR_VALID_TENANT_LANDLORD_T_WITH_INVALID_USER_ROLE_TC_307:
                payload.setLoginID(invalidLoginIdForACN);
                payload.setAcnStatusIndicator(null);
                payload.setTenantLandlord(TENANT.getValue());
                break;

            default:
                payload.setAcnStatusIndicator(ACN.getValue());
        }

    }

    public void setCustomerPEWCPreferencesBasedOnTypeTC308_309(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel acnStatusIndicator) {
        payload.setRequestID(FakerDataGenerator.getRandomNumericString(10));
        switch (acnStatusIndicator) {
            case CUSTOMER_PEWC_PREFRENCES_VALUE_GOOD_WITH_OTHER_PARAM_NULL_TC_308:
                payload.setHomePhoneType(null);
                payload.setHomePhoneNumber(null);
                payload.setWorkPhoneExtension(null);
                payload.setWorkPhoneType(null);
                payload.setWorkPhoneNumber(null);
                payload.setCustomerPEWCPreferences(FakerDataGenerator.generateString(4));
                break;
            case CUSTOMER_PEWC_PREFRENCES_VALUE_TRUE_WITH_OTHER_PARAM_NULL_TC_309:
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
            case NULL_REQUEST_ID_TC_156:
                payload.setRequestID(null);
                break;
            case DUPLICATE_REQUEST_ID_TC_155:
                payload.setRequestID(DUPLICATE_REQUEST_ID.getValue());
                break;
            case LONG_REQUEST_ID_TC_157:
                payload.setRequestID(FakerDataGenerator.getRandomNumericString(35));
                break;

            default:
                payload.setRequestID(FakerDataGenerator.generateString(10));
        }
    }

    public void setLoginIDBasedOnTypeTC158_160B(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel loginID) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        switch (loginID) {
            case INVALID_LOGIN_ID_TC_160:
                payload.setLoginID(FakerDataGenerator.generateString(6));
                break;
            case NULL_LOGIN_ID_TC_158:
                payload.setLoginID("");
                break;
            case NON_NUMERIC_LOGIN_ID_TC_160B:
                payload.setLoginID(FakerDataGenerator.generateAlphanumericWithSpecialChars(9));
                break;
            case ALPHANUMERIC_LOGIN_ID_TC_160A:
                payload.setLoginID(FakerDataGenerator.generateAlphanumeric(8));
                break;
            case MAX_LENGTH_LOGIN_ID_TC_159:
                payload.setLoginID(FakerDataGenerator.getRandomNumericString(35));
                break;
            default:
                payload.setLoginID(FakerDataGenerator.generateLowerCaseString(10));
        }
    }

    public void setTransactionIDBasedOnTypeTC161_162(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel transactionID) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        switch (transactionID) {
            case NULL_TRANSACTION_ID_INCL_ENROLLMENT_STATE_TC_161:
                payload.setEnrollmentState(INCL.getValue());
                payload.setCustomerCode("5911661");
                payload.setPremisesCode("5886135");
                payload.setTransactionID(null);
                break;
            case NULL_TRANSACTION_ID_CRDS_ENROLLMENT_STATE_TC_162:
                payload.setEnrollmentState(CRDS.getValue());
                payload.setCustomerCode("5911662");
                payload.setPremisesCode("5886136");
                payload.setTransactionID(null);
                break;
            default:
                payload.setTransactionID(FakerDataGenerator.getRandomNumericString(10));
        }
    }

    public void setCustomerCodeBasedOnTypeTC163_164(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel customercode) throws IOException {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        switch (customercode) {
            case NULL_CUSTOMER_CODE_INCL_ENROLLMENT_STATE_TC_163:
                /*ExcelReader excelReader = new ExcelReader(EXPERIAN_DATA);
                List<Map<String, String>> testData = excelReader.getSheetData(EXPERIAN_SHEET_NAME);
                Map<String, String> rowData = testData.get(0); // 0-indexed, fetches the second data row
                log.info("BUSINESS NAME is: {}", rowData.get("BUSINESS NAME"));*/
                payload.setEnrollmentState(INCL.getValue());
                payload.setPremisesCode("5886135");
                payload.setTransactionID("234223459");
                payload.setCustomerCode(null);
                break;
            case NULL_CUSTOMER_CODE_CRDS_ENROLLMENT_STATE_TC_164:
                payload.setEnrollmentState(INCL.getValue());
                payload.setPremisesCode("5886136");
                payload.setTransactionID("234223459");
                payload.setCustomerCode(null);
                break;
            default:
                payload.setCustomerCode(FakerDataGenerator.getRandomNumericString(8));
        }
    }

    public void setPremisesCodeBasedOnTypeTC165_167(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel premisesCode) {
        payload.setRequestID(FakerDataGenerator.generateString(10));
        switch (premisesCode) {
            case NULL_PREMISES_CODE_INCL_ENROLLMENT_STATE_TC_165:
                payload.setEnrollmentState("INCL");
                payload.setTransactionID("234223459");
                payload.setCustomerCode("5911662");
                payload.setPremisesCode(null);
                break;
            case NULL_PREMISES_CODE_CRDS_ENROLLMENT_STATE_TC_166:
                payload.setEnrollmentState("CRDS");
                payload.setTransactionID("234223459");
                payload.setCustomerCode("5911662");
                payload.setPremisesCode(null);
                break;
            case VALID_PREMISES_CODE_NULL_ENROLLMENT_STATE_TC_167:
                payload.setTransactionID("234223459");
                payload.setCustomerCode("5911662");
                payload.setPremisesCode("5886135");
                payload.setEnrollmentState(null);
                break;

            case ENROLLMENT_STATE_CUST_CODE_PREM_CODE_NULL_TC_168:
                payload.setTransactionID("234223459");
                payload.setCustomerCode(null);
                payload.setPremisesCode(null);
                payload.setEnrollmentState(null);
                break;

            case ENROLLMENT_STATE_TRAN_ID_PREM_CODE_NULL_TC_169:
                payload.setTransactionID(null);
                payload.setCustomerCode("5911662");
                payload.setPremisesCode(null);
                payload.setEnrollmentState(null);
                break;

            case ENROLLMENT_STATE_TRAN_ID_CUST_CODE_NULL_TC_170:
                payload.setTransactionID(null);
                payload.setCustomerCode(null);
                payload.setPremisesCode("5886135");
                payload.setEnrollmentState(null);
                break;

            case ENROLLMENT_STATE_PREM_CODE_NULL_TC_171:
                 payload.setTransactionID("234223459");
                payload.setCustomerCode("5911662");
                payload.setPremisesCode(null);
                payload.setEnrollmentState(null);
                break;

            case ENROLLMENT_STATE_CUST_CODE_NULL_TC_172:
                payload.setTransactionID("234223459");
                payload.setCustomerCode(null);
                payload.setPremisesCode("5886135");
                payload.setEnrollmentState(null);
                break;

            case ENROLLMENT_STATE_TRAN_ID_NULL_TC_173:
                payload.setTransactionID(null);
                payload.setCustomerCode("5911662");
                payload.setPremisesCode("5886135");
                payload.setEnrollmentState(null);
                break;

            case INVALID_ENROLLMENT_STATE_TC_174:
                payload.setTransactionID("234223459");
                payload.setCustomerCode("5911662");
                payload.setPremisesCode("5886135");
                payload.setEnrollmentState("PVER");
                break;

            default:
                payload.setPremisesCode(FakerDataGenerator.getRandomNumericString(8));
        }
    }

    public void setInvalidReferralCodeTC238_241(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel referralcode) {
        switch (referralcode) {
            default:
                payload.setReferralCode(FakerDataGenerator.getRandomNumericString(10));
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
        payload.setRequestID(FakerDataGenerator.getRandomNumericString(8));
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
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_441:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_444:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_445:
                payload.setEnrollmentState(INCL.getValue());
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_453:
                payload.setEnrollmentState(INCL.getValue());
                payload.setAcnStatusIndicator(ACN.getValue());
                break;

            case INVALID_TRANSACTION_ID_ENROLLMENT_STATE_INCL_TC_175:
                payload.setTransactionID(FakerDataGenerator.generateDigits(6));
                setTheFieldToEmptyForCommercialScenarios(payload);
                payload.setEnrollmentState(INCL.getValue());
                break;

            case INVALID_TRANSACTION_ID_ENROLLMENT_STATE_CRDS_TC_176:
                payload.setTransactionID(FakerDataGenerator.generateDigits(6));
                payload.setEnrollmentState(CRDS.getValue());
                break;

            case INVALID_CUSTOMER_CODE_ENROLLMENT_STATE_INCL_TC_177:
            case INVALID_CUSTOMER_CODE_ENROLLMENT_STATE_INCL_TC_187:
                payload.setCustomerCode(FakerDataGenerator.generateDigits(8));
                setTheFieldToEmptyForCommercialScenarios(payload);
                payload.setEnrollmentState(INCL.getValue());
                break;

            case INVALID_CUSTOMER_CODE_ENROLLMENT_STATE_CRDS_TC_178:
                payload.setCustomerCode(FakerDataGenerator.generateDigits(8));
                setTheFieldToEmptyForCommercialScenarios(payload);
                payload.setEnrollmentState(CRDS.getValue());
                break;

            case INVALID_PREMISES_CODE_ENROLLMENT_STATE_INCL_TC_179:
            case INVALID_PREMISES_CODE_ENROLLMENT_STATE_INCL_TC_189:
                payload.setPremisesCode("9000000");
                setTheFieldToEmptyForCommercialScenarios(payload);
                payload.setEnrollmentState(INCL.getValue());
                break;

            case INVALID_PREMISES_CODE_ENROLLMENT_STATE_CRDS_TC_180:
                payload.setPremisesCode("9000000");
                setTheFieldToEmptyForCommercialScenarios(payload);
                payload.setEnrollmentState(CRDS.getValue());
                break;

            case INVALID_PREMISES_CODE_LENGTH_ENROLLMENT_STATE_INCL_TC_188:
                payload.setPremisesCode(FakerDataGenerator.generateDigits(8));
                setTheFieldToEmptyForCommercialScenarios(payload);
                payload.setEnrollmentState(INCL.getValue());
                break;

            case INVALID_PREMISES_CODE_NON_NUMERIC_ENROLLMENT_STATE_INCL_TC_188A:
                payload.setPremisesCode(FakerDataGenerator.generateString(8));
                setTheFieldToEmptyForCommercialScenarios(payload);
                payload.setEnrollmentState(INCL.getValue());
                break;

            case INVALID_COMBINATION_OF_CUSTOMER_AND_PREMISES_CODE_INCL_TC_181:
                payload.setCustomerCode(customerCode-1);
                setTheFieldToEmptyForCommercialScenarios(payload);
                payload.setEnrollmentState(INCL.getValue());
                break;

            case INVALID_COMBINATION_OF_CUSTOMER_AND_PREMISES_CODE_CRDS_TC_182:
                payload.setCustomerCode(customerCode-1);
                setTheFieldToEmptyForCommercialScenarios(payload);
                payload.setEnrollmentState(CRDS.getValue());
                break;

            case INVALID_LENGTH_CUSTOMER_CODE_ENROLLMENT_STATE_INCL_TC_186:
                payload.setCustomerCode(FakerDataGenerator.generateDigits(11));
                payload.setEnrollmentState(INCL.getValue());
                break;

            case INVALID_CUSTOMER_CODE_LESS_THAN_0_ENROLLMENT_STATE_INCL_TC_186A:
                payload.setCustomerCode(0.5);
                payload.setEnrollmentState(INCL.getValue());
                break;

            case INVALID_CUSTOMER_CODE_EMPTY_ENROLLMENT_STATE_INCL_TC_186B:
                payload.setCustomerCode("");
                payload.setEnrollmentState(INCL.getValue());
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_447:
                payload.setEnrollmentState(INCL.getValue());
                payload.setCustomerType(COMMERCIAL.getValue());
                setTheFieldToEmptyForCommercialScenarios(payload);
                String customerBusinessName= testContext.getSearchAccountsResponse().getData().getAccounts().getFirst().getCustomerBusinessName();
                payload.setCustomerBusinessName(customerBusinessName);
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_426:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_426_1:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_437:
                payload.setEnrollmentState(CRDS.getValue());
                break;

            case SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_478:
            case SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_480:
            case SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_482_2:
            case SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_483_2:
                payload.setEnrollmentState(INCL.getValue());
                payload.setCustomerCode(null);
                break;

            case SSP_VALIDATION_SSP_PARTICIPANT_CODE_MISSING_TC_490:
            case SSP_VALIDATION_SSP_PARTICIPANT_CODE_MISSING_TC_494_2:
                payload.setEnrollmentState(INCL.getValue());
                payload.setSspParticipantCode(null);
                transactionID=testContext.getSaveEnrollmentResponse().getData().getTransactionID();
                payload.setTransactionID(transactionID);
                payload.setSeasonalSavingsProgramIndicator(true);
                break;

            case SSP_VALIDATION_SSP_PARTICIPANT_CODE_MISSING_TC_492:
                payload.setSspParticipantCode(null);
                payload.setSeasonalSavingsProgramIndicator(true);
                payload.setEnrollmentState(INCL.getValue());
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
                Map<String, String> customerData = loadRowFromExcelToCustomerData(EXPERIAN_DATA, EXPERIAN_SHEET_NAME, testCondition);
                //Map<String, String> commercialCustomerData = allRowsOfCommercialCustomerData.get(5063);
                nullifyFields(payload,  "enrollmentState", "transactionID");
                payload.setSspParticipantCode(sspParticipantCode);
                payload.setSeasonalSavingsProgramIndicator(true);
                payload.setCustomerType(COMMERCIAL.getValue());
                setTheFieldToEmptyForCommercialScenarios(payload);
                payload.setFederalTaxID(encryptData(customerData.get("TAX-ID")));
                payload.setCustomerBusinessName(customerData.get("BUSINESS NAME"));
                nullifyFields(payload,  "enrollmentState", "transactionID");
                payload.setCreditCheckOption(YES.getValue());
                break;

            case SSP_VALIDATION_PREMISES_CODE_MISSING_TC_484:
            case SSP_VALIDATION_PREMISES_CODE_MISSING_TC_488_2:
                payload.setEnrollmentState(INCL.getValue());
                nullifyFields(payload,  "premisesCode", "sspParticipantCode");
                break;

            case SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_486:
                payload.setEnrollmentState(INCL.getValue());
                nullifyFields(payload,  "customerCode", "sspParticipantCode");
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_CE_TC_502,
                 GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_CE_TC_505,
                 GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_507:
                payload.setAglcServiceLocationID(testContext.getGetEligiblePlansAndOffersResponse().getData().getAglcServiceLocationID());
                payload.setAglcAccountNumber(testContext.getGetEligiblePlansAndOffersResponse().getData().getAglcAccountNumber());
                payload.setEnrollmentState(INCL.getValue());
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
        payload.setEnrollmentState(INCL.getValue());
        payload.setCustomerCode(selectedAccount.getCustomerCode());
        payload.setPremisesCode(selectedAccount.getPremisesCode());
        payload.setPremisesCity(selectedAccount.getPremisesCity());
        payload.setPremisesStateCode(selectedAccount.getPremisesStateCode());
        payload.setPremisesZipCode(selectedAccount.getPremisesZipCode());
        payload.setCustomerBusinessName(selectedAccount.getCustomerBusinessName());
    }


    public void preparePayloadBasedOnTC_EligiblePlansAndSaveEnrollment(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel testCondition) {
        payload.setRequestID(FakerDataGenerator.getRandomNumericString(6));
        Map<String, String> commercialCustomerData = null;
        Map<String, String> customerData = null;

        switch(testCondition) {
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_CE_TC_423:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_SI_TC_433:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_424:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_434:
            case INVALID_TRANSACTION_ID_ENROLLMENT_STATE_INCL_TC_175:
            case INVALID_CUSTOMER_CODE_ENROLLMENT_STATE_INCL_TC_177:
            case INVALID_CUSTOMER_CODE_ENROLLMENT_STATE_INCL_TC_187:
            case INVALID_PREMISES_CODE_ENROLLMENT_STATE_INCL_TC_179:
            case INVALID_PREMISES_CODE_LENGTH_ENROLLMENT_STATE_INCL_TC_188:
            case INVALID_COMBINATION_OF_CUSTOMER_AND_PREMISES_CODE_INCL_TC_181:
            case INVALID_LENGTH_CUSTOMER_CODE_ENROLLMENT_STATE_INCL_TC_186:
            case INVALID_CUSTOMER_CODE_LESS_THAN_0_ENROLLMENT_STATE_INCL_TC_186A:
            case INVALID_CUSTOMER_CODE_EMPTY_ENROLLMENT_STATE_INCL_TC_186B:
            case INVALID_PREMISES_CODE_NON_NUMERIC_ENROLLMENT_STATE_INCL_TC_188A:
            case INVALID_PREMISES_CODE_ENROLLMENT_STATE_INCL_TC_189:
                customerData = loadRowFromExcelToCustomerData(CUSTOMER_DATA, CUSTOMER_SHEET_NAME, testCondition);
                payload.setEnrollmentSource(PHONECALL.getValue());
                payload.setCreditCheckOption(YES.getValue());
                getCustomerDetails(payload, customerData);
                payload.setCallerID(FakerDataGenerator.generateDigits(10));
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_DP_TC_425:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_DR_TC_435:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_RD_TC_446:
                commercialCustomerData = loadRowFromExcelToCustomerData(EXPERIAN_DATA, EXPERIAN_SHEET_NAME, testCondition);
                payload.setCustomerType(COMMERCIAL.getValue());
                payload.setCreditCheckOption(YES.getValue());
                populateCommonFields(payload,commercialCustomerData);
                setTheFieldToEmptyForCommercialScenarios(payload);
                break;

            case NO_MATCHING_DATA_COMMERCIAL_TC_361, VALID_DATA_REENTERED_COMMERCIAL_TC_362:
                customerData = loadRowFromExcelToCustomerData(CUSTOMER_DATA, CUSTOMER_SHEET_NAME, testCondition);
                setTheFieldToEmptyForCommercialScenarios(payload);
                payload.setCustomerType(COMMERCIAL.getValue());
                payload.setCreditCheckOption(YES.getValue());
                payload.setFederalTaxID(encryptData(customerData.get("federalTaxId")));
                payload.setPremisesCity(customerData.get("premisesCity"));
                payload.setPremisesStateCode(customerData.get("premisesStateCode"));
                payload.setPremisesZipCode(customerData.get("premisesZipCode"));
                payload.setPremisesCountyCode(customerData.get("premisesCountyCode"));
                payload.setCustomerBusinessName(customerData.get("customerLastName"));
                break;

            case CREDIT_CHECK_BUSINESS_BIN_NOT_NULL_TC_262A:
                customerData = loadRowFromExcelToCustomerData(CUSTOMER_DATA, CUSTOMER_SHEET_NAME, testCondition);
                setTheFieldToEmptyForCommercialScenarios(payload);
                payload.setCustomerType(COMMERCIAL.getValue());
                payload.setCreditCheckOption(YES.getValue());
                payload.setFederalTaxID(encryptData(customerData.get("federalTaxId")));
                payload.setPremisesCity(customerData.get("premisesCity"));
                payload.setPremisesStateCode(customerData.get("premisesStateCode"));
                payload.setPremisesZipCode(customerData.get("premisesZipCode"));
                payload.setPremisesCountyCode(customerData.get("premisesCountyCode"));
                payload.setCustomerBusinessName(customerData.get("customerLastName"));
                payload.setCreditCheckBusinessName(customerData.get("customerLastName"));
                payload.setConfirmCreditCheck(true);
                payload.setCommercialCreditCheckBusinessBIN(customerData.get("Bin"));
                break;

            case SSP_VALIDATION_PREMISES_CODE_MISSING_TC_484:
            case SSP_VALIDATION_PREMISES_CODE_MISSING_TC_485:
            case SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_486:
            case SSP_VALIDATION_CUSTOMER_CODE_MISSING_TC_487, SSP_VALIDATION_PREMISES_CODE_MISSING_TC_488,
                 SSP_VALIDATION_SSP_PARTICIPANT_CODE_MISSING_TC_491,
                 GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_SI_TC_504,
                 GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_506,
                 GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_447:
                commercialCustomerData = loadRowFromExcelToCustomerData(EXPERIAN_DATA, EXPERIAN_SHEET_NAME, testCondition);
                payload.setCustomerType(COMMERCIAL.getValue());
                payload.setCreditCheckOption(YES.getValue());
                populateCommonFields(payload,commercialCustomerData);
                setTheFieldToEmptyForCommercialScenarios(payload);
                payload.setSeasonalSavingsProgramIndicator(true);
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_SF_TC_454:
                commercialCustomerData = loadRowFromExcelToCustomerData(EXPERIAN_DATA, EXPERIAN_SHEET_NAME, testCondition);
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
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_426:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_426_1:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_427:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_428:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_436:
            case INVALID_CUSTOMER_CODE_ENROLLMENT_STATE_CRDS_TC_178:
            case INVALID_PREMISES_CODE_ENROLLMENT_STATE_CRDS_TC_180:
            case INVALID_COMBINATION_OF_CUSTOMER_AND_PREMISES_CODE_CRDS_TC_182:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_440:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_437:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_441:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_444:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_445,
                 SAVE_ENROLLMENT_INVALID_SPLIT_FEE_VALUE_TC417a, INVALID_TRANSACTION_ID_ENROLLMENT_STATE_CRDS_TC_176:
                customerData = loadRowFromExcelToCustomerData(CUSTOMER_DATA, CUSTOMER_SHEET_NAME, testCondition);
                payload.setCreditCheckOption(YES.getValue());
                getCustomerDetails(payload,customerData);
                break;


            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_BD_TC_452:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_453:
                customerData = loadRowFromExcelToCustomerData(CUSTOMER_DATA, CUSTOMER_SHEET_NAME, testCondition);
                payload.setCreditCheckOption(YES.getValue());
                getCustomerDetails(payload,customerData);
                payload.setSeasonalSavingsProgramIndicator(false);
                payload.setAcnStatusIndicator(ACN.getValue());
                break;

            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_SF_TC_501:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_CE_TC_502:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_SI_TC_503:
            case GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_CE_TC_505,
                 GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_507,
                 SAVE_ENROLLMENT_INVALID_VALUE_BILLING_PLAN_PREPAY_TC406a,
                 SAVE_ENROLLMENT_INVALID_SSP_EMPTY_TC412,
                 SAVE_ENROLLMENT_INVALID_SSP_RESULT_VALUE_TC414,
                 SAVE_ENROLLMENT_INVALID_SSP_SPLIT_FEE_EMPTY_TC415,
                 SAVE_ENROLLMENT_INVALID_SSP_SPLIT_FEE_VALUE_TC416,
                 SAVE_ENROLLMENT_INVALID_AGLC_ACCOUNT_EMPTY_TC418,
                 SAVE_ENROLLMENT_INVALID_AGLC_ACCOUNT_MAX_LENGTH_TC419,
                 SAVE_ENROLLMENT_INVALID_AGLC_SERVICE_ORDER_EMPTY_TC420,
                 SAVE_ENROLLMENT_INVALID_SSP_PC_VALUE_TC422b:
                customerData = loadRowFromExcelToCustomerData(CUSTOMER_DATA, CUSTOMER_SHEET_NAME, testCondition);
                getCustomerAndPremiseDetails(payload, customerData);
                payload.setSeasonalSavingsProgramIndicator(true);
                break;

            default:
                payload.setCreditCheckOption(GlobalEnums.CreditCheckOption.CREDIT_CHECK_NOT_REQUIRED.getValue());
                customerData = loadRowFromExcelToCustomerData(CUSTOMER_DATA, CUSTOMER_SHEET_NAME, testCondition);
                getCustomerDetails(payload, customerData);
                payload.setTransactionType(TURN_ON.getValue());
                payload.setCustomerType(RESIDENTIAL.getValue());
                payload.setEnrollmentSource(MAIL.getValue());
                payload.setRequestID("1"+ FakerDataGenerator.generateDigits(6));
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

    public void setExternalRequestParams(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel testCondition) {
        Map<String, String> customerData = loadRowFromExcelToCustomerData(CUSTOMER_DATA, CUSTOMER_SHEET_NAME, testCondition);
        getCustomerAndPremiseDetails(payload, customerData);

        switch (testCondition)
        {
            case MS_RS_FRAUD_ALERT_INVALID_SSN_TC_010 -> {
                payload.setTransactionType(METER_SET.getValue());
                payload.setCreditCheckOption(YES.getValue());
                payload.setConfirmCreditCheck(Boolean.FALSE);
                payload.setAglcServiceLocationID(FakerDataGenerator.getRandomNumericString(8));
        }
            default -> throw new IllegalStateException("Unexpected value: " + testCondition);
        }

    }

    public void setRequestParamsWithoutPromotionCode(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel testCondition) {
        Map<String, String> customerData = loadRowFromExcelToCustomerData(CUSTOMER_DATA, CUSTOMER_SHEET_NAME, testCondition);
        getCustomerAndPremiseDetails(payload, customerData);
        if(testCondition==INVALID_TRANSACTION_ID_ENROLLMENT_STATE_CRDS_TC_176){
            payload.setTenantLandlord("T");
            payload.setCreditCheckOption("Yes");
            payload.setAcnStatusIndicator("NACN");
        }
        if(testCondition==SSP_VALIDATION_PAYMENT_CONFIRMATION_NUMBER_MISSING_TC_496){
            payload.setSeasonalSavingsProgramIndicator(true);
            payload.setTenantLandlord("T");
            payload.setCreditCheckOption("Yes");
            payload.setAcnStatusIndicator("NACN");
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

    public void validateAllTheEntriesInTablesForEligiblePlansAndOffers(GetEligiblePlansAndOffersApiLabel testCondition){
        if(! (testCondition== DECEASED_OR_NON_ISSUED_CUSTOMER_TC_355 || testCondition==DECEASED_OR_NON_ISSUED_CONFIRM_CREDIT_CHECK_CUSTOMER_TC_355A || testCondition==GET_ELIGIBLE_PLANS_AND_OFFERS_FROZEN_ACCOUNT_357 ||testCondition==NO_MATCH_FOUND_IN_EXPERIAN_TC_353 ||testCondition==NO_MATCH_FOUND_IN_EXPERIAN_TC_356)) {
            String customerCode = testContext.getGetEligiblePlansAndOffersResponse().getData().getCustomerCode();
            String premisesCode = testContext.getGetEligiblePlansAndOffersResponse().getData().getPremisesCode();
            String firstName = testContext.getGetEligiblePlansAndOffersResponse().getData().getCustomerFirstName();
            String lastName = testContext.getGetEligiblePlansAndOffersResponse().getData().getCustomerLastName();
            String zipCode = testContext.getGetEligiblePlansAndOffersResponse().getData().getPremisesZipCode();
            String aglcServiceLocationID = testContext.getGetEligiblePlansAndOffersResponse().getData().getAglcServiceLocationID();
            String streetNumber = testContext.getGetEligiblePlansAndOffersResponse().getData().getPremisesStreetNumber();
            String city = testContext.getGetEligiblePlansAndOffersResponse().getData().getPremisesCity();
            String state = testContext.getGetEligiblePlansAndOffersResponse().getData().getPremisesStateCode();

            Map<String, Object> enrollmentRecord = null;

            if (testCondition == NO_MATCH_PLAN_CODE_B_CONTINUE_ENROLLMENT_TC_354 ||
                    testCondition == NO_MATCH_INVALID_NAME_CONTINUE_ENROLLMENT_TC_354A ||
                    testCondition == LOW_CREDIT_SCORE_FOR_SSP_ENROLLMENT_TC_338B) {

                enrollmentRecord = ApplicationContext.get()
                        .getDbAction()
                        .validateAllTheTablesAfterGetEligiblePlansRequest(
                                customerCode,
                                premisesCode,
                                firstName,
                                lastName,
                                zipCode,
                                aglcServiceLocationID,
                                streetNumber,
                                city,
                                state
                        );
            }

            Assert.assertEquals(enrollmentRecord.get("UZBENRO_CUST_CODE").toString(), customerCode);
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
}
