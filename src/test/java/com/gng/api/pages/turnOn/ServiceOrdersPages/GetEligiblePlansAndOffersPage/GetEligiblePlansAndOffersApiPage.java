package com.gng.api.pages.turnOn.ServiceOrdersPages.GetEligiblePlansAndOffersPage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.request.GetEligiblePlansAndOffersRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.turnOn.ServiceOrdersSteps.GetEligiblePlansAndOffers.GetEligiblePlansAndOffersApiLabel;
import com.gng.api.util.FakerDataGenerator;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;
import java.io.IOException;
import static com.gng.api.constants.ApiEndPoint.GET_ELIGIBLE_PLANS_AND_OFFERS;
import static com.gng.api.steps.AesEncryption.AesEncryptionSteps.encryptData;

public class GetEligiblePlansAndOffersApiPage extends BasePage {

    private final GetEligiblePlansAndOffersHelper helper;
    public static String ssn_tc_318 = "666252963";
    public static String ssn_tc_319 = "666495180";
    public static String ssn_tc_320 = "666066117";
    public static String ssn_tc_321 = "666206220";
    public static String ssn_tc_322 = "666314835";
    public static String ssn_tc_323 = "666266076";
    public static String ssn_tc_324 = "666621606";
    public static String ssn_tc_325 = "666085827";
    public static String federal_tax_id_326 = "132581802";
    public static String ssn_tc_327 = "666435795";
    public static String ssn_tc_328 = "666182004";

    public GetEligiblePlansAndOffersApiPage(TestContext testContext) {
        super(testContext);
        this.helper = new GetEligiblePlansAndOffersHelper(testContext);
    }

    public void sendGetEligiblePlansAndOffersRequestWithPromotionCodeAsNull(GetEligiblePlansAndOffersApiLabel apiLabel) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setMarketingPromotionCode(null);
        payload.setCreditCheckOption("yes");
        payload.setSocialSecurityNumber(encryptData(ssn_tc_318));
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void sendGetEligiblePlansAndOffersRequestWithNoPromotionCode(GetEligiblePlansAndOffersApiLabel apiLabel) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setSocialSecurityNumber(encryptData(ssn_tc_319));
        payload.setEnrollmentSource("PHONE CALL");
        payload.setCallerID("9123545042");
        payload.setCreditCheckOption("yes");
        payload.setCustomerLastName("POLIZZI");
        payload.setCustomerMiddleName("N");
        payload.setCustomerFirstName("DEBRA");
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void sendGetEligiblePlansAndOffersRequestWithNoPromotionCodeTC320(GetEligiblePlansAndOffersApiLabel apiLabel) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setCreditCheckOption("yes");
        payload.setCustomerLastName("BLOCK");
        payload.setCustomerMiddleName("E");
        payload.setCustomerFirstName("EUGENE");
        payload.setHomePhoneNumber("4165245244");
        payload.setHomePhoneType("M");
        payload.setSocialSecurityNumber(encryptData(ssn_tc_320));
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void sendGetEligiblePlansAndOffersRequestWithPromotionCode(GetEligiblePlansAndOffersApiLabel apiLabel) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setEnrollmentSource("PHONE CALL");
        payload.setMarketingPromotionCode("AAA");
        payload.setCallerID("4164965244");
        payload.setCreditCheckOption("yes");
        payload.setSocialSecurityNumber(encryptData(ssn_tc_321));
        payload.setCustomerLastName("FISCHER");
        payload.setCustomerMiddleName("L");
        payload.setCustomerFirstName("BERT");
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void sendGetEligiblePlansAndOffersRequestWithNoPromotionCodeTC322(GetEligiblePlansAndOffersApiLabel apiLabel) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setCreditCheckOption("yes");
        payload.setBillingRuralRoute("RR");
        payload.setBillingAddressType("R");
        payload.setBillingCity("WARRENTON");
        payload.setBillingZipCode("30828");
        payload.setSocialSecurityNumber(encryptData(ssn_tc_322));
        payload.setCustomerLastName("BLUE");
        payload.setCustomerMiddleName(null);
        payload.setCustomerFirstName("RILEY");
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void sendGetEligiblePlansAndOffersRequestWithNoPromotionCodeTC323(GetEligiblePlansAndOffersApiLabel apiLabel) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setCreditCheckOption("yes");
        payload.setSocialSecurityNumber(encryptData(ssn_tc_323));
        payload.setCustomerLastName("FRANKLIN");
        payload.setCustomerMiddleName("M");
        payload.setCustomerFirstName("LILY");
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void sendGetEligiblePlansAndOffersRequestWithNoPromotionCodeTC324(GetEligiblePlansAndOffersApiLabel apiLabel) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setCreditCheckOption("yes");
        payload.setSocialSecurityNumber(encryptData(ssn_tc_324));
        payload.setCustomerLastName("BUTLER");
        payload.setCustomerMiddleName("C");
        payload.setCustomerFirstName("CLAUDIA");
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void sendGetEligiblePlansAndOffersRequestWithNoPromotionCodeTC325(GetEligiblePlansAndOffersApiLabel apiLabel) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setCreditCheckOption("ServTransfer");
        payload.setSocialSecurityNumber(encryptData(ssn_tc_325));
        payload.setCustomerLastName("MCPHERRON");
        payload.setCustomerMiddleName(null);
        payload.setCustomerFirstName("CRAIG");
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void sendGetEligiblePlansAndOffersRequestWithNoPromotionCodeTC326(GetEligiblePlansAndOffersApiLabel apiLabel) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setCreditCheckOption("Comm");
        payload.setSocialSecurityNumber("");
        payload.setFederalTaxID(encryptData(federal_tax_id_326));
        payload.setCustomerLastName("NAGRA VISION ATLANT");
        payload.setCustomerMiddleName(null);
        payload.setCustomerFirstName(null);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void sendGetEligiblePlansAndOffersRequestWithPromotionCodeTC327(GetEligiblePlansAndOffersApiLabel apiLabel) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setLoginID("ACNCSR");
        payload.setCreditCheckOption("yes");
        payload.setMarketingPromotionCode("AAA");
        payload.setAcnStatusIndicator("ACN");
        payload.setTenantLandlord("L");
        payload.setSocialSecurityNumber(encryptData(ssn_tc_327));
        payload.setCustomerLastName("GRAW");
        payload.setCustomerMiddleName(null);
        payload.setCustomerFirstName("LAURA");
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void sendGetEligiblePlansAndOffersRequestWithPromotionCodeTC328(GetEligiblePlansAndOffersApiLabel apiLabel) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setLoginID("ACNCSR");
        payload.setCreditCheckOption("yes");
        payload.setMarketingPromotionCode("AAA");
        payload.setAcnStatusIndicator("ACN");
        payload.setTenantLandlord("L");
        payload.setSocialSecurityNumber(encryptData(ssn_tc_328));
        payload.setCustomerLastName("CHAPPELL");
        payload.setCustomerMiddleName(null);
        payload.setCustomerFirstName("TERESA");
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void sendGetEligiblePlansAndOffersRequestWithNoPromotionCodeTC339(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel testCondition){
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setTransactionType("TNON");
        payload.setCustomerType("CM");
        payload.setAuthorizedBy("MM");
        payload.setSeasonalSavingsProgramIndicator(false);
        helper.payloadBasedOnTC339(payload, testCondition);
        payload.setCustomerFirstName(null);
        payload.setCustomerLastName(null);
        payload.setAglcServiceLocationID(FakerDataGenerator.generateDigits(9));
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidRequestIDCasesTC155_157(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel requestID) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setRequestIDBasedOnTypeTC155_157(payload, requestID);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidLoginIDCasesTC158_160B(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel loginID) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setLoginIDBasedOnTypeTC158_160B(payload, loginID);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidTransactionIDCasesTC161_162(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel transactionID) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setTransactionIDBasedOnTypeTC161_162(payload, transactionID);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidCustomerCodeCasesTC163_164(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel customercode) throws IOException {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setCustomerCodeBasedOnTypeTC163_164(payload, customercode);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidPremisesCodeCasesTC165_167(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel premisesCode) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setPremisesCodeBasedOnTypeTC165_167(payload, premisesCode);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidEnrollmentStateCasesTC168_182(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel premisesCode) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setPremisesCodeBasedOnTypeTC165_167(payload, premisesCode);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidTestConditionTC237(GetEligiblePlansAndOffersApiLabel apiLabel ) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setInvalidTestConditionTC237(payload);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidReferralCode(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel referralCode) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setInvalidReferralCodeTC238_241(payload, referralCode);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidPremisesStreetNumberTC242(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel premisesStreetNumber) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setInvalidPremisesStreetNumberTC242(payload, premisesStreetNumber);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidPremisesStreetPreDirectionTC243(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel premisesStreetPreDirection) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setInvalidPremisesStreetPreDirectionTypeTC243(payload, premisesStreetPreDirection);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidPremisesStreetNameTC244_245(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel premisesStreetName) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setInvalidPremisesStreetNameTypeTC244_245(payload, premisesStreetName);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidPremisesStreetSuffixTC246_246a(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel premisesStreetSuffix) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setInvalidPremisesStreetSuffixTypeTC246_246a(payload, premisesStreetSuffix);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidPremisesStreetPostDirectionTC247_247a(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel premisesStreetPostDirection) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setInvalidPremisesStreetPostDirectionTypeTC247_247a(payload, premisesStreetPostDirection);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidPremisesUnitTypeTC248_248a(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel premisesUnitType) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setInvalidPremisesUnitTypeTC248_248a(payload, premisesUnitType);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidPremisesUnitNumberTC249(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel premisesUnitNumber) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setInvalidPremisesUnitNumberTC249(payload, premisesUnitNumber);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidPremisesUnitNumberTC250_251(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel premisesCity) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setInvalidPremisesCityTC250(payload, premisesCity);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidPremisesStateCodeCases252_253(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel premisesStateCode) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setPremisesStateCodeBasedOnType252_253(payload, premisesStateCode);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidPremisesZipCodeCases254_255C(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel premisesZipCode) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setPremisesZipCodeBasedOnType254_255c(payload, premisesZipCode);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidPremisesCountyCodeCases256_257(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel premisesCountyCode) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setPremisesCountyCodeBasedOnType256_257(payload, premisesCountyCode);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidSeparateBillingAddressCases258_283b(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel separateBillingAddress) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setSeparateBillingAddressBasedOnType258_283b(payload, separateBillingAddress);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidWorkPhoneNumberTC284_286(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel workphonenumber) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setWorkPhoneNumberBasedOnType284_286(payload, workphonenumber);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidWorkPhoneTypeTC287_290(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel workphonetype) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setWorkPhoneTypeBasedOnType287_290(payload, workphonetype);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidHomePhoneNumberTC291_293(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel homePhoneNumber) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setHomePhoneNumberBasedOnType291_293(payload, homePhoneNumber);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidHomePhoneTypeTC294_297(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel homePhoneType) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setHomePhoneTypeBasedOnType294_297(payload, homePhoneType);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidAcnStatusIndicatorCasesTC298_307(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel acnStatusIndicator) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setAcnStatusIndicatorBasedOnTypeTC298_307(payload, acnStatusIndicator);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidCustomerPEWCPreferencesCasesTC308_309(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel acnStatusIndicator) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setCustomerPEWCPreferencesBasedOnTypeTC308_309(payload, acnStatusIndicator);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidCreditCheckOptionCases310_312(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel creditCheckOption) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setCreditCheckOptionBasedOnTypeTC310_312(payload, creditCheckOption);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void validateInitialCreditCheckCustomerCodeCases313_317(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel initialCreditCheckCustomerCode) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setInitialCreditCheckCustomerCodeBasedOnTypeTC313_317(payload, initialCreditCheckCustomerCode);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidTransactionTypeCases(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel transactionType) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setTransactionTypeBasedOnType(payload, transactionType);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidCustomerTypeCases(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel customerTYPE) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setCustomerTypeBasedOnType(payload, customerTYPE);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidEnrollmentSourcesCases(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel enrollmentSources) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setEnrollmentSourcesBasedOnType(payload, enrollmentSources);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidCustomerLastNameCases(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel customerLastName) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setCustomerLastNameBasedOnType(payload, customerLastName);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }


    public void validateInvalidTenantLandlordCases(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel tenantLandlord) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setTenantLandlordBasedOnType(payload, tenantLandlord);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }
    public void validateTestCondition25(GetEligiblePlansAndOffersApiLabel apiLabel) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setTestCondition25(payload);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);

    }
    public void validateTestCondition26(GetEligiblePlansAndOffersApiLabel apiLabel) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setTestCondition26(payload);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);

    }
    public void validateTestCondition27(GetEligiblePlansAndOffersApiLabel apiLabel) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setTestCondition27(payload);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);

    }
    public void validateTestCondition28(GetEligiblePlansAndOffersApiLabel apiLabel) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setTestCondition28(payload);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);

    }
    public void validateTestCondition29(GetEligiblePlansAndOffersApiLabel apiLabel) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setTestCondition29(payload);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);

    }
    public void validateTestCondition30(GetEligiblePlansAndOffersApiLabel apiLabel) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setTestCondition30(payload);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);

    }
    public void validateTestCondition31(GetEligiblePlansAndOffersApiLabel apiLabel) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setTestCondition31(payload);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);

    }

    public void validateTestConditionRSTC11UC50(GetEligiblePlansAndOffersApiLabel apiLabel) throws IOException {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setTestConditionRSTC11UC50(payload);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);

    }

}
