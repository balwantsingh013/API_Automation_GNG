package com.gng.api.pages.ServiceOrdersPages.GetEligiblePlansAndOffersPage;

import com.gng.api.pages.BasePage;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.request.GetEligiblePlansAndOffersRequest;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.response.GetEligiblePlansAndOffersResponse;
import com.gng.api.pojo.ServiceOrdersPojo.SaveEnrollment.SaveEnrollmentRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.ServiceOrdersSteps.GetEligiblePlansAndOffers.GetEligiblePlansAndOffersApiLabel;
import com.gng.api.steps.ServiceOrdersSteps.SaveEnrollment.SaveEnrollmentApiLabel;
import com.gng.api.util.FakerDataGenerator;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.GET_ELIGIBLE_PLANS_AND_OFFERS;
import static com.gng.api.constants.ApiEndPoint.SAVE_ENROLLMENT;

public class GetEligiblePlansAndOffersApiPage extends BasePage {

    private final GetEligiblePlansAndOffersHelper helper;

    public GetEligiblePlansAndOffersApiPage(TestContext testContext) {
        super(testContext);
        this.helper = new GetEligiblePlansAndOffersHelper(testContext);
    }
    public void sendGetEligiblePlansAndOffersRequestWithPromotionCodeAsNull(GetEligiblePlansAndOffersApiLabel apiLabel) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setMarketingPromotionCode(null);
        payload.setCreditCheckOption("yes");
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        GetEligiblePlansAndOffersResponse getEligiblePlansAndOffersResponse = deserializeResponseToPojo(response, GetEligiblePlansAndOffersResponse.class);
        testContext.setGetEligiblePlansAndOffersResponse(getEligiblePlansAndOffersResponse);
    }
    public void sendGetEligiblePlansAndOffersRequestWithNoPromotionCode(GetEligiblePlansAndOffersApiLabel apiLabel) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setCreditCheckOption("yes");
        payload.setCustomerLastName("BLOCK");
        payload.setCustomerMiddleName("E");
        payload.setCustomerFirstName("EUGENE");
        payload.setHomePhoneNumber("4165245244");
        payload.setHomePhoneType("M");
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        GetEligiblePlansAndOffersResponse getEligiblePlansAndOffersResponse = deserializeResponseToPojo(response, GetEligiblePlansAndOffersResponse.class);
        testContext.setGetEligiblePlansAndOffersResponse(getEligiblePlansAndOffersResponse);
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
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        GetEligiblePlansAndOffersResponse getEligiblePlansAndOffersResponse = deserializeResponseToPojo(response, GetEligiblePlansAndOffersResponse.class);
        testContext.setGetEligiblePlansAndOffersResponse(getEligiblePlansAndOffersResponse);
    }
    public void sendGetEligiblePlansAndOffersRequestWithPromotionCode(GetEligiblePlansAndOffersApiLabel apiLabel) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setEnrollmentSource("PHONE CALL");
        payload.setMarketingPromotionCode("AAA");
        payload.setCallerID("4164965244");
        payload.setCreditCheckOption("yes");
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        GetEligiblePlansAndOffersResponse getEligiblePlansAndOffersResponse = deserializeResponseToPojo(response, GetEligiblePlansAndOffersResponse.class);
        testContext.setGetEligiblePlansAndOffersResponse(getEligiblePlansAndOffersResponse);
    }
    public void sendGetEligiblePlansAndOffersRequestWithNoPromotionCodeTC322(GetEligiblePlansAndOffersApiLabel apiLabel) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setCreditCheckOption("yes");
        payload.setBillingRuralRoute("RR");
        payload.setBillingAddressType("R");
        payload.setBillingCity("WARRENTON");
        payload.setBillingZipCode("30828");
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        GetEligiblePlansAndOffersResponse getEligiblePlansAndOffersResponse = deserializeResponseToPojo(response, GetEligiblePlansAndOffersResponse.class);
        testContext.setGetEligiblePlansAndOffersResponse(getEligiblePlansAndOffersResponse);
    }
    public void sendGetEligiblePlansAndOffersRequestWithNoPromotionCodeTC323(GetEligiblePlansAndOffersApiLabel apiLabel) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setCreditCheckOption("yes");
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        GetEligiblePlansAndOffersResponse getEligiblePlansAndOffersResponse = deserializeResponseToPojo(response, GetEligiblePlansAndOffersResponse.class);
        testContext.setGetEligiblePlansAndOffersResponse(getEligiblePlansAndOffersResponse);
    }
    public void sendGetEligiblePlansAndOffersRequestWithNoPromotionCodeTC324(GetEligiblePlansAndOffersApiLabel apiLabel) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setCreditCheckOption("yes");
//        payload.setCustomerCode("5912124");
//        payload.setPremisesCode("5886598");
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        GetEligiblePlansAndOffersResponse getEligiblePlansAndOffersResponse = deserializeResponseToPojo(response, GetEligiblePlansAndOffersResponse.class);
        testContext.setGetEligiblePlansAndOffersResponse(getEligiblePlansAndOffersResponse);
    }
    public void sendGetEligiblePlansAndOffersRequestWithNoPromotionCodeTC325(GetEligiblePlansAndOffersApiLabel apiLabel) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setCreditCheckOption("Service Transfer");
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        GetEligiblePlansAndOffersResponse getEligiblePlansAndOffersResponse = deserializeResponseToPojo(response, GetEligiblePlansAndOffersResponse.class);
        testContext.setGetEligiblePlansAndOffersResponse(getEligiblePlansAndOffersResponse);
    }
    public void sendGetEligiblePlansAndOffersRequestWithNoPromotionCodeTC326(GetEligiblePlansAndOffersApiLabel apiLabel) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setCreditCheckOption("Comm");
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        GetEligiblePlansAndOffersResponse getEligiblePlansAndOffersResponse = deserializeResponseToPojo(response, GetEligiblePlansAndOffersResponse.class);
        testContext.setGetEligiblePlansAndOffersResponse(getEligiblePlansAndOffersResponse);
    }
    public void sendGetEligiblePlansAndOffersRequestWithPromotionCodeTC327(GetEligiblePlansAndOffersApiLabel apiLabel) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setLoginID("ACNCSR");
        payload.setCreditCheckOption("yes");
        payload.setMarketingPromotionCode("AAA");
        payload.setAcnStatusIndicator("ACN");
        payload.setTenantLandlord("L");
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        GetEligiblePlansAndOffersResponse getEligiblePlansAndOffersResponse = deserializeResponseToPojo(response, GetEligiblePlansAndOffersResponse.class);
        testContext.setGetEligiblePlansAndOffersResponse(getEligiblePlansAndOffersResponse);
    }
    public void sendGetEligiblePlansAndOffersRequestWithPromotionCodeTC328(GetEligiblePlansAndOffersApiLabel apiLabel) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setLoginID("ACNCSR");
        payload.setCreditCheckOption("yes");
        payload.setMarketingPromotionCode("AAA");
        payload.setAcnStatusIndicator("ACN");
        payload.setTenantLandlord("L");
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        GetEligiblePlansAndOffersResponse getEligiblePlansAndOffersResponse = deserializeResponseToPojo(response, GetEligiblePlansAndOffersResponse.class);
        testContext.setGetEligiblePlansAndOffersResponse(getEligiblePlansAndOffersResponse);
    }

    public void validateInvalidRequestIDCases(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel requestID) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setRequestIDBasedOnType(payload, requestID);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }
    public void validateInvalidTestCondition(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel testCondition) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setInvalidTestCondition(payload, testCondition);
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
    public void validateInvalidPremisesStreetType(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel referralCode) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setInvalidPremisesStreetTypeTC242_241(payload, referralCode);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }
    public void validateInvalidLoginIDCases(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel loginID) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setLoginIDBasedOnType(payload, loginID);
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
    public void validateInvalidCustomerFirstNameCases(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel customerFirstName) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setCustomerFirstNameBasedOnType(payload, customerFirstName);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidSeasonalSavingsProgramIndicatorCases(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel seasonalSavingsProgramIndicator) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setSeasonalSavingsProgramIndicatorBasedOnType(payload, seasonalSavingsProgramIndicator);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidPremisesStreetNameCases(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel premisesStreetName) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setPremisesStreetNameBasedOnType(payload, premisesStreetName);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidPremisesCityCases(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel premisesCity) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setPremisesCityBasedOnType(payload, premisesCity);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidPremisesZipCodeCases(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel premisesZipCode) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setPremisesZipCodeBasedOnType(payload, premisesZipCode);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }
    public void validateInvalidPremisesCountyCodeCases(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel premisesCountyCode) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setPremisesCountyCodeBasedOnType(payload, premisesCountyCode);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }
    public void validateInvalidPremisesStateCodeCases(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel premisesStateCode) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setPremisesStateCodeBasedOnType(payload, premisesStateCode);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidCreditCheckOptionCases(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel creditCheckOption) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setCreditCheckOptionBasedOnType(payload, creditCheckOption);
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
    public void validateInvalidSeparateBillingAddressCases(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel separateBillingAddress) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setSeparateBillingAddressBasedOnType(payload, separateBillingAddress);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }
    public void validateInvalidAcnStatusIndicatorCases(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel acnStatusIndicator) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setAcnStatusIndicatorBasedOnType(payload, acnStatusIndicator);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
    }
//    public void validateInvalidAglcServiceLocationIDCases(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel aglcServiceLocationID) {
//        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
//        helper.setAglcServiceLocationIDBasedOnType(payload, aglcServiceLocationID);
//        setRequestSpecification(payload, testContext.getAuthToken());
//        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
//        testContext.setResponse(response);
//    }
}
