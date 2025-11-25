package com.gng.api.pages.serviceTransfer.ServiceOrdersPages.GetEligiblePlansAndOffersPage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.request.GetEligiblePlansAndOffersRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.serviceTransfer.ServiceOrdersSteps.GetEligiblePlansAndOffers.GetEligiblePlansAndOffersApiLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static com.gng.api.constants.DBConstant.UCRACCT_CUST_CODE;

import static com.gng.api.constants.GlobalEnums.CreditCheckOption.SERVICE_TRANSFER;


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
        payload.setTransactionType(GlobalEnums.TransactionType.TRANSFER.getValue());
        payload.setReferralCode(null);
        payload.setCreditCheckOption(SERVICE_TRANSFER.getValue());
        payload.setEnrollmentState(null);
        payload.setInitialCreditCheckCustomerCode(null);
        payload.setConfirmCreditCheck(null);
        payload.setSspParticipantCode(null);
        payload.setCommercialCreditCheckBusinessBIN(null);
        payload.setServiceTransferCurrentPricePlan(false);
        payload.setServiceTransferOfferRemainder(false);
        payload.setServiceTransferReward(null);

        switch (testCondition){
            case ST_GE_ENROLLMENT_STATE_INVALID_FOR_TRAN_NEG_TC198,
                 ST_GE_ST_CURRENT_TRUE_NOT_TRAN_NEG_TC217,
                 ST_GE_ST_OFFER_REMAINDER_TRUE_NOT_TRAN_NEG_TC220 -> {
                    setNamesFromSearchAccountsResponse(payload);
                    clearNewCustomerRequestFields(payload);
                    setRequestParamsForTransferFromSearchAccountsResponse(payload, testCondition);
            }
            case ST_GE_ST_CURRENT_TRUE_PLAN_NOT_FIXED_OR_CEILING_NEG_TC215A -> {
                    clearNewCustomerRequestFields(payload);
                    setRequestParamsForTransferFromSearchAccountsResponse(payload, testCondition);
            }
            default -> {
                    Map<String, Object> uzrrcotRecord = ApplicationContext.get().getDbAction().getLatestUZRRCOTRecord();
                    payload.setCustomerCode(uzrrcotRecord.get("UZRRCOT_CUST_CODE"));
                    payload.setPremisesCode(uzrrcotRecord.get("UZRRCOT_PREM_CODE"));
            }
        }
    }

    public void setParametersBasedOnTypeNegative(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel testCondition) {
        setSupportingDefaultParameters(payload, testCondition);

        switch (testCondition) {
            case ST_GE_MISSING_REQUEST_ID_NEG_TC188 -> payload.setRequestID(null);
            case ST_GE_INVALID_REQUEST_ID_LENGTH_NEG_TC189 -> payload.setRequestID(FakerDataGenerator.generateString(65));
            case ST_GE_DUPLICATE_REQUEST_ID_NEG_TC190 -> payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());

            case ST_GE_MISSING_LOGIN_ID_NEG_TC191 -> payload.setLoginID(null);
            case ST_GE_INVALID_LOGIN_ID_LENGTH_NEG_TC192 -> payload.setLoginID(FakerDataGenerator.generateString(31));
            case ST_GE_INVALID_LOGIN_ID_FORMAT_NEG_TC193 -> payload.setLoginID(FakerDataGenerator.generateAlphanumericWithSpecialChars(6));
            case ST_GE_INVALID_LOGIN_ID_NOT_FOUND_NEG_TC194 -> payload.setLoginID(FakerDataGenerator.getRandomString(5));

            case ST_GE_MISSING_TRANSACTION_TYPE_NEG_TC195 -> payload.setTransactionType(null);
            case ST_GE_INVALID_TRANSACTION_TYPE_LENGTH_NEG_TC196 -> payload.setTransactionType(FakerDataGenerator.generateString(5)); // >4
            case ST_GE_INVALID_TRANSACTION_TYPE_VALUE_NEG_TC197 -> payload.setTransactionType(GlobalEnums.InvalidValues.INVALID_TRANSACTION_TYPE.getValue());

            case ST_GE_ENROLLMENT_STATE_INVALID_FOR_TRAN_NEG_TC198 -> {
                payload.setEnrollmentState(GlobalEnums.EnrollMentState.INCL.getValue());
                payload.setTransactionID(null);
                payload.setServiceTransferReward(null);
            }

            case ST_GE_MISSING_CUSTOMER_CODE_NEG_TC199 -> payload.setCustomerCode(null);
            case ST_GE_INVALID_CUSTOMER_CODE_LENGTH_NEG_TC200 -> payload.setCustomerCode(FakerDataGenerator.getRandomNumericString(12)); // >9
            case ST_GE_INVALID_CUSTOMER_CODE_FORMAT_NEG_TC201 -> payload.setCustomerCode(FakerDataGenerator.getRandomString(5));
            case ST_GE_INVALID_CUSTOMER_CODE_NOT_FOUND_NEG_TC202 -> payload.setCustomerCode(GlobalEnums.InvalidValues.INVALID_CUSTOMER_CODE.getValue());

            case ST_GE_MISSING_PREMISES_CODE_NEG_TC203 -> payload.setPremisesCode(null);
            case ST_GE_INVALID_PREMISES_CODE_LENGTH_NEG_TC204 -> payload.setPremisesCode(FakerDataGenerator.getRandomNumericString(9)); // >7
            case ST_GE_INVALID_PREMISES_CODE_NOT_FOUND_NEG_TC205 -> payload.setPremisesCode(GlobalEnums.InvalidValues.INVALID_PREMISES_CODE.getValue());

            case ST_GE_INVALID_ACCOUNT_COMBINATION_NEG_TC206 -> {
                Map<String, Object> activeCustomerData = ApplicationContext.get().getDbAction().getActiveCustomerWithServiceTransferEnrollment();
                payload.setCustomerCode(activeCustomerData.get(UCRACCT_CUST_CODE).toString());
            }
            case ST_GE_REFERRAL_CODE_NOT_REQUIRED_FOR_TRAN_NEG_TC207 -> {
                payload.setTransactionType(GlobalEnums.TransactionType.TRANSFER.getValue());
                payload.setReferralCode(FakerDataGenerator.generateString(4));
            }
            case ST_GE_INVALID_CREDIT_CHECK_OPTION_LENGTH_NEG_TC208 -> payload.setCreditCheckOption(FakerDataGenerator.generateString(33));
            case ST_GE_INVALID_CREDIT_CHECK_OPTION_VALUE_NEG_TC209 -> payload.setCreditCheckOption(FakerDataGenerator.generateString(3));
            case ST_GE_MISSING_CREDIT_CHECK_OPTION_NEG_TC210 -> payload.setCreditCheckOption(null);

            case ST_GE_INITIAL_CREDIT_CHECK_CUST_CODE_NOT_REQUIRED_NEG_TC211 -> {
                payload.setTransactionType(GlobalEnums.TransactionType.TRANSFER.getValue());
                payload.setInitialCreditCheckCustomerCode(FakerDataGenerator.getRandomNumericString(6));
            }
            case ST_GE_CONFIRM_CREDIT_CHECK_NOT_REQUIRED_NEG_TC212 -> {
                payload.setTransactionType(GlobalEnums.TransactionType.TRANSFER.getValue());
                payload.setConfirmCreditCheck(Boolean.TRUE);
            }
            case ST_GE_SSP_PARTICIPANT_CODE_NOT_REQUIRED_NEG_TC213 -> {
                payload.setTransactionType(GlobalEnums.TransactionType.TRANSFER.getValue());
                payload.setSspParticipantCode(FakerDataGenerator.getRandomNumericString(6));
            }
            case ST_GE_COMMERCIAL_CREDIT_CHECK_BIN_NOT_REQUIRED_NEG_TC214 -> {
                payload.setTransactionType(GlobalEnums.TransactionType.TRANSFER.getValue());
                payload.setCommercialCreditCheckBusinessBIN(FakerDataGenerator.getRandomNumericString(6));
            }
            case ST_GE_ST_CURRENT_TRUE_PLAN_NOT_FIXED_OR_CEILING_NEG_TC215A -> {
                clearNewCustomerRequestFields(payload);
                setRequestParamsForTransferFromSearchAccountsResponse(payload, testCondition);
                payload.setServiceTransferCurrentPricePlan(Boolean.TRUE);
            }
            case ST_GE_ST_CURRENT_FLAG_MISSING_WHEN_PLAN_FIXED_OR_CEILING_NEG_TC216 -> {
                payload.setTransactionType(GlobalEnums.TransactionType.TRANSFER.getValue());
                payload.setServiceTransferCurrentPricePlan(null);
            }
            case ST_GE_ST_CURRENT_TRUE_NOT_TRAN_NEG_TC217 -> {
                clearCustomerPremisesTransaction(payload);
                payload.setTransactionType(GlobalEnums.TransactionType.TURN_ON.getValue());
                payload.setServiceTransferCurrentPricePlan(Boolean.TRUE);
            }
            case ST_GE_ST_OFFER_REMAINDER_TRUE_NOT_TRAN_NEG_TC220 ->   payload.setServiceTransferOfferRemainder(Boolean.TRUE);

            default -> { }
        }
    }

    public void setParametersBasedOnTypeForExternalCasesNegative(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel testCondition) {
        payload.setRequestID(FakerDataGenerator.getRandomNumericString(12));
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
                 ST_SE_CURRENT_PRICE_PLAN_CEILING_BOOLEAN_ONLY_TC249,
                 ST_SE_CURRENT_PRICE_PLAN_APPLICABLE_FIXED_OR_CEILING_ONLY_TC250 -> {
                clearNewCustomerRequestFields(payload);
                setRequestParamsForTransferFromSearchAccountsResponse(payload, testCondition);
                payload.setServiceTransferReward(false);
                payload.setServiceTransferOfferRemainder(false);
                payload.setServiceTransferCurrentPricePlan(false);
            }
            case ST_GE_ENROLLMENT_STATE_INVALID_FOR_TRAN_NEG_TC198 ->{
                clearNewCustomerRequestFields(payload);
                setRequestParamsForTransferFromSearchAccountsResponse(payload, testCondition);
                payload.setServiceTransferReward(false);
                payload.setServiceTransferOfferRemainder(false);
                payload.setServiceTransferCurrentPricePlan(false);
                payload.setEnrollmentState(GlobalEnums.EnrollMentState.INCL.getValue());
            }

            case ACN_RS_REMAINS_ON_TIER_1_TC_251 -> {
                clearNewCustomerRequestFields(payload);
                setRequestParamsForTransferFromSearchAccountsResponse(payload, testCondition);
                payload.setServiceTransferOfferRemainder(true);
                payload.setServiceTransferCurrentPricePlan(true);
                payload.setEnrollmentState(null);
                payload.setAcnStatusIndicator("ACN");
            }

            case ACN_RS_REMAINS_ON_TIER_1_NACN_TC_252 -> {
                clearNewCustomerRequestFields(payload);
                setRequestParamsForTransferFromSearchAccountsResponse(payload, testCondition);
                payload.setServiceTransferOfferRemainder(true);
                payload.setServiceTransferCurrentPricePlan(true);
                payload.setEnrollmentState(null);
                payload.setAcnStatusIndicator("NACN");
            }

            case ACN_RS_TC_271, ACN_RS_TC_269,ACN_RS_TC_253, ACN_RS_TC_255, ACN_RS_TC_259, ACN_RS_TC_263, ACN_RS_TC_265, ACN_RS_TC_268 -> {
                clearNewCustomerRequestFields(payload);
                setRequestParamsForTransferFromSearchAccountsResponse(payload, testCondition);
                payload.setServiceTransferOfferRemainder(false);
                payload.setServiceTransferCurrentPricePlan(false);
                payload.setEnrollmentState(null);
                payload.setAcnStatusIndicator("ACN");
            }

            case NACN_RS_TC_270, NACN_RS_TC_267, NACN_RS_TC_266, NACN_RS_TC_254, NACN_RS_TC_256, NACN_SR_TC_258, NACN_RS_TC_260, NACN_SR_TC_262, NACN_RS_TC_264 -> {
                clearNewCustomerRequestFields(payload);
                setRequestParamsForTransferFromSearchAccountsResponse(payload, testCondition);
                payload.setServiceTransferOfferRemainder(false);
                payload.setServiceTransferCurrentPricePlan(false);
                payload.setEnrollmentState(null);
                payload.setAcnStatusIndicator("NACN");
            }

            default -> { }
        }
    }

    public void setParametersSeedingDataBasedOnTypeNegative(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel testCondition) {

        switch (testCondition) {
            case ST_SE_CURRENT_PRICE_PLAN_APPLICABLE_FIXED_OR_CEILING_ONLY_TC250 -> {
                clearNewCustomerRequestFields(payload);
                setRequestParamsForTransferFromSearchAccountsResponse(payload, testCondition);
                payload.setServiceTransferOfferRemainder(false);
                payload.setServiceTransferReward(false);
                payload.setServiceTransferCurrentPricePlan(false);
            }
            default ->  {}
        }
    }

    public void setRequestParamsForTransferFromSearchAccountsResponse(GetEligiblePlansAndOffersRequest payload, GetEligiblePlansAndOffersApiLabel testCondition) {
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
        String aglcAcct = acct.getAglcAccountNumber();

        payload.setAglcAccountNumber(aglcAcct);
        String last9 = aglcAcct.substring(Math.max(0, aglcAcct.length() - 9));
        payload.setAglcServiceLocationID(last9);

        payload.setCreditCheckOption(SERVICE_TRANSFER.getValue());
        payload.setTransactionType(GlobalEnums.TransactionType.TRANSFER.getValue());
        payload.setCustomerCode(testContext.getSearchAccountsResponse().getData().getAccounts().getFirst().getCustomerCode());
        payload.setPremisesCode(testContext.getSearchAccountsResponse().getData().getAccounts().getFirst().getPremisesCode());

    }

    public void setNamesFromSearchAccountsResponse(GetEligiblePlansAndOffersRequest payload){
        payload.setCustomerFirstName(testContext.getSearchAccountsResponse().getData().getAccounts().getFirst().getCustomerFirstName());
        payload.setCustomerMiddleName(testContext.getSearchAccountsResponse().getData().getAccounts().getFirst().getCustomerMiddleName());
        payload.setCustomerLastName(testContext.getSearchAccountsResponse().getData().getAccounts().getFirst().getCustomerLastName());
    }

    public void clearCustomerPremisesTransaction(GetEligiblePlansAndOffersRequest payload){
        payload.setCustomerCode(null);
        payload.setPremisesCode(null);
        payload.setTransactionID(null);
    }

    public void clearNewCustomerRequestFields(GetEligiblePlansAndOffersRequest payload){
        payload.setSocialSecurityNumber(null);
        payload.setConfirmCreditCheck(null);
        payload.setAglcServiceLocationID(null);
        payload.setPremisesStreetNumber(null);
        payload.setPremisesStreetPreDirection(null);
        payload.setPremisesStreetName(null);
        payload.setPremisesStreetSuffix(null);
        payload.setPremisesStreetPostDirection(null);
        payload.setPremisesUnitType(null);
        payload.setPremisesUnitNumber(null);
        payload.setPremisesCity(null);
        payload.setPremisesStateCode(null);
        payload.setPremisesZipCode(null);
        payload.setPremisesCountyCode(null);
        payload.setBillingStreetNumber(null);
        payload.setBillingStreetPreDirection(null);
        payload.setBillingStreetName(null);
        payload.setBillingStreetSuffix(null);
        payload.setBillingStreetPostDirection(null);
        payload.setBillingUnitType(null);
        payload.setBillingUnitNumber(null);
        payload.setBillingAddressLine2(null);
        payload.setBillingCity(null);
        payload.setBillingStateCode(null);
        payload.setBillingZipCode(null);
        payload.setBillingCountyCode(null);
        payload.setMarketingPromotionCode(null);
        payload.setReferralCode(null);
        payload.setEmailAddress(null);
        payload.setHomePhoneNumber(null);
        payload.setWorkPhoneNumber(null);
    }

}
