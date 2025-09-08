package com.gng.api.pages.turnOn.ServiceOrdersPages.GetEligiblePlansAndOffersPage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.request.GetEligiblePlansAndOffersRequest;
import com.gng.api.pojo.ServiceOrdersPojo.GetEligiblePlansAndOffers.response.GetEligiblePlansAndOffersResponse;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.turnOn.ServiceOrdersSteps.GetEligiblePlansAndOffers.GetEligiblePlansAndOffersApiLabel;
import com.gng.api.util.FakerDataGenerator;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;
import java.io.IOException;

import static com.gng.api.constants.ApiEndPoint.GET_ELIGIBLE_PLANS_AND_OFFERS;
import static com.gng.api.steps.turnOn.ServiceOrdersSteps.GetEligiblePlansAndOffers.GetEligiblePlansAndOffersApiLabel.GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_427;
import static com.gng.api.steps.turnOn.ServiceOrdersSteps.GetEligiblePlansAndOffers.GetEligiblePlansAndOffersApiLabel.GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_428;

public class GetEligiblePlansAndOffersApiPage extends BasePage {
    private final GetEligiblePlansAndOffersHelper helper;

    public GetEligiblePlansAndOffersApiPage(TestContext testContext) {
        super(testContext);
        this.helper = new GetEligiblePlansAndOffersHelper(testContext);
    }

    public void sendGetEligiblePlansAndOffersRequestCommercial(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel testCondition){
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.payloadBasedOnTCsCommercial(payload, testCondition);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        testContext.setResponse(response);
        GetEligiblePlansAndOffersResponse getEligiblePlansAndOffersResponse = deserializeResponseToPojo(response, GetEligiblePlansAndOffersResponse.class);
        testContext.setGetEligiblePlansAndOffersResponse(getEligiblePlansAndOffersResponse);
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

    public void validateInvalidReferralCode(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel referralCode) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.setInvalidReferralCodeTC238_241(payload, referralCode);
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

    public void sendGetEligiblePlansAndOffersRequest(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel testCondition) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        helper.preparePayloadBasedOnTC_EligiblePlansAndSaveEnrollment(payload,testCondition);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        GetEligiblePlansAndOffersResponse getEligiblePlansAndOffersResponse = deserializeResponseToPojo(response, GetEligiblePlansAndOffersResponse.class);
        testContext.setGetEligiblePlansAndOffersResponse(getEligiblePlansAndOffersResponse);
        testContext.setResponse(response);
        }

        public void sendGetEligiblePlansAndOffersPrevSavedIncompleteEnrollment(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel testCondition){
        if(!testCondition.equals(GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_427)&&!testCondition.equals(GET_ELIGIBLE_PLANS_AND_OFFERS_SAVE_ENROLLMENT_PREV_SAVED_TC_428)) {
            GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
            helper.preparePayloadForPreviouslySavedIncompleteEnrollment(payload, testCondition);
            setRequestSpecification(payload, testContext.getAuthToken());
            Response response = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
            GetEligiblePlansAndOffersResponse getEligiblePlansAndOffersResponse = deserializeResponseToPojo(response, GetEligiblePlansAndOffersResponse.class);
            testContext.setGetEligiblePlansAndOffersResponse(getEligiblePlansAndOffersResponse);
            testContext.setResponse(response);
        }
        }
    public void validatePositiveTestConditionsPromotionCodeFromExcelData(GetEligiblePlansAndOffersApiLabel apiLabel, GlobalEnums.PromotionCode promotionCode, GetEligiblePlansAndOffersApiLabel testCondition) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        payload.setRequestID(FakerDataGenerator.generateString(10));
        helper.setRequestParams(payload, promotionCode, testCondition);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response offersResponse = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        GetEligiblePlansAndOffersResponse getEligiblePlansAndOffersResponse = deserializeResponseToPojo(offersResponse, GetEligiblePlansAndOffersResponse.class);
        testContext.setGetEligiblePlansAndOffersResponse(getEligiblePlansAndOffersResponse);
        testContext.setResponse(offersResponse);
    }
    public void validatePositiveWithNoPromotionCodeTestConditionsFromExcelData(GetEligiblePlansAndOffersApiLabel apiLabel, GetEligiblePlansAndOffersApiLabel testCondition) {
        GetEligiblePlansAndOffersRequest payload = helper.preparePayload(apiLabel);
        payload.setRequestID(FakerDataGenerator.generateString(10));
        helper.setRequoteRequestParams(payload, testCondition);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response offersResponse = sendRequest(HttpPost.METHOD_NAME, GET_ELIGIBLE_PLANS_AND_OFFERS, 200);
        GetEligiblePlansAndOffersResponse getEligiblePlansAndOffersResponse = deserializeResponseToPojo(offersResponse, GetEligiblePlansAndOffersResponse.class);
        testContext.setGetEligiblePlansAndOffersResponse(getEligiblePlansAndOffersResponse);
        testContext.setResponse(offersResponse);
    }

    public void verifyResponsePlans(){
        helper.verifyResidentialPlansReceivedAgainstDatabase();
    }
    public void verifyResponsePlansContainsPlan(GlobalEnums.PlanCode planCode){
        helper.verifyPlanReturned(planCode);
    }

}
