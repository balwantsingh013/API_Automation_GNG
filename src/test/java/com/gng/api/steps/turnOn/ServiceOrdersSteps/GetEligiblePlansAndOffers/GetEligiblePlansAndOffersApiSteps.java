package com.gng.api.steps.turnOn.ServiceOrdersSteps.GetEligiblePlansAndOffers;


import com.gng.api.constants.GlobalEnums;
import com.gng.api.pages.turnOn.ServiceOrdersPages.GetEligiblePlansAndOffersPage.GetEligiblePlansAndOffersApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.IOException;

import static com.gng.api.steps.turnOn.ServiceOrdersSteps.GetEligiblePlansAndOffers.GetEligiblePlansAndOffersApiLabel.*;

public class GetEligiblePlansAndOffersApiSteps {

    private final TestContext testContext;
    private final GetEligiblePlansAndOffersApiPage getEligiblePlansAndOffersApiPage;

    public GetEligiblePlansAndOffersApiSteps(TestContext testContext, GetEligiblePlansAndOffersApiPage getEligiblePlansAndOffersApiPage) {
        this.testContext = testContext;
        this.getEligiblePlansAndOffersApiPage = getEligiblePlansAndOffersApiPage;
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with customer type commercial for {string}")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_customer_type_commercial_credit_check_as_yes_with_no_promotion_code_TC_339(String testCondition) {
        getEligiblePlansAndOffersApiPage.sendGetEligiblePlansAndOffersRequestCommercial(get_eligible_plans_and_offers, valueOf(testCondition));
    }

    @When("verify the plans in received in response")
    public void verify_plans_received_in_response(){
        getEligiblePlansAndOffersApiPage.verifyPlansReceivedInResponse();
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with {string}TC155_157")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_TC155_157(String requestID) {
        getEligiblePlansAndOffersApiPage.validateInvalidRequestIDCasesTC155_157(get_eligible_plans_and_offers_mandatory, valueOf(requestID));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with login {string} ID TC158_160b")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_login_ID_TC158_160b(String loginID) {
        getEligiblePlansAndOffersApiPage.validateInvalidLoginIDCasesTC158_160B(get_eligible_plans_and_offers_mandatory, valueOf(loginID));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with transaction {string} ID TC161_162")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_transaction_ID_TC161_162(String transactionID) {
        getEligiblePlansAndOffersApiPage.validateInvalidTransactionIDCasesTC161_162(get_eligible_plans_and_offers_mandatory, valueOf(transactionID));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with customer {string} code TC163_164")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_customer_code_TC163_164(String customerCode) throws IOException {
        getEligiblePlansAndOffersApiPage.validateInvalidCustomerCodeCasesTC163_164(get_eligible_plans_and_offers_mandatory, valueOf(customerCode));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with premises {string} code TC165_167")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_premises_code_TC165_167(String premisesCode) {
        getEligiblePlansAndOffersApiPage.validateInvalidPremisesCodeCasesTC165_167(get_eligible_plans_and_offers_mandatory, valueOf(premisesCode));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with enrollment {string} state TC168_182")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_enrollment_state_TC168_182(String enrollmentState) {
        getEligiblePlansAndOffersApiPage.validateInvalidEnrollmentStateCasesTC168_182(get_eligible_plans_and_offers_mandatory, valueOf(enrollmentState));
    }


    @When("a request is made to the GetEligiblePlansAndOffers Api with null Authorised Type")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_null_Authorised_Type( ) {
        getEligiblePlansAndOffersApiPage.validateInvalidTestConditionTC237(get_eligible_plans_and_offers);
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with {string} code Type")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_invalid_referral_code_Type(String referralCode) {
        getEligiblePlansAndOffersApiPage.validateInvalidReferralCode(get_eligible_plans_and_offers, valueOf(referralCode));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with {string}  Type")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_invalid_premises_street_Type(String premisesStreetNumber) {
        getEligiblePlansAndOffersApiPage.validateInvalidPremisesStreetNumberTC242(get_eligible_plans_and_offers, valueOf(premisesStreetNumber));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with {string} test cases 243")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_invalid_premises_street_Direction_test_case_243(String premisesStreetPreDirection) {
        getEligiblePlansAndOffersApiPage.validateInvalidPremisesStreetPreDirectionTC243(get_eligible_plans_and_offers, valueOf(premisesStreetPreDirection));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with {string} test cases 244_245")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_invalid_premises_street_name_test_case_244_245(String premisesStreetName) {
        getEligiblePlansAndOffersApiPage.validateInvalidPremisesStreetNameTC244_245(get_eligible_plans_and_offers, valueOf(premisesStreetName));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with {string} test cases 246_246a")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_invalid_premises_street_suffix_test_case_246_246a(String premisesStreetSuffix) {
        getEligiblePlansAndOffersApiPage.validateInvalidPremisesStreetSuffixTC246_246a(get_eligible_plans_and_offers, valueOf(premisesStreetSuffix));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with {string} test cases 247_247a")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_invalid_premises_street_post_Direction_test_case_247_247a(String premisesStreetPostDirection) {
        getEligiblePlansAndOffersApiPage.validateInvalidPremisesStreetPostDirectionTC247_247a(get_eligible_plans_and_offers, valueOf(premisesStreetPostDirection));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with {string} test cases 248_248a")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_invalid_premises_unit_type_test_case_248_248a(String premisesUnitType) {
        getEligiblePlansAndOffersApiPage.validateInvalidPremisesUnitTypeTC248_248a(get_eligible_plans_and_offers, valueOf(premisesUnitType));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with {string} test cases 249")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_invalid_premises_unit_number_test_case_249(String premisesUnitNumber) {
        getEligiblePlansAndOffersApiPage.validateInvalidPremisesUnitNumberTC249(get_eligible_plans_and_offers, valueOf(premisesUnitNumber));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with {string} test cases 250_251")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_invalid_premises_city_test_case_250_251(String premisesCity) {
        getEligiblePlansAndOffersApiPage.validateInvalidPremisesUnitNumberTC250_251(get_eligible_plans_and_offers, valueOf(premisesCity));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with premises {string} Statecode 252_253")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_premises_state_code_252_253(String premisesStateCode) {
        getEligiblePlansAndOffersApiPage.validateInvalidPremisesStateCodeCases252_253(get_eligible_plans_and_offers, valueOf(premisesStateCode));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with premises {string} Zipcode254_255c")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_premises_zip_code_254_255c(String premisesZipCode) {
        getEligiblePlansAndOffersApiPage.validateInvalidPremisesZipCodeCases254_255C(get_eligible_plans_and_offers, valueOf(premisesZipCode));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with premises {string} Countycode256_257")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_premises_county_code_256_257(String premisesCountyCode) {
        getEligiblePlansAndOffersApiPage.validateInvalidPremisesCountyCodeCases256_257(get_eligible_plans_and_offers, valueOf(premisesCountyCode));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with separateBilling {string} Address258_283b")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_separateBilling_address_258_283b(String separateBillingAddress) {
        getEligiblePlansAndOffersApiPage.validateInvalidSeparateBillingAddressCases258_283b(get_eligible_plans_and_offers, valueOf(separateBillingAddress));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with workPhone {string} Number284_286")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_workPhone_number_284_286(String workPhoneNumber) {
        getEligiblePlansAndOffersApiPage.validateInvalidWorkPhoneNumberTC284_286(get_eligible_plans_and_offers, valueOf(workPhoneNumber));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with WorkPhone {string} Type287_290")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_workPhone_type_287_290(String workPhoneType) {
        getEligiblePlansAndOffersApiPage.validateInvalidWorkPhoneTypeTC287_290(get_eligible_plans_and_offers, valueOf(workPhoneType));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with HomePhone {string} Number291_293")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_homePhone_number_291_293(String homePhoneNumber) {
        getEligiblePlansAndOffersApiPage.validateInvalidHomePhoneNumberTC291_293(get_eligible_plans_and_offers, valueOf(homePhoneNumber));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with HomePhone {string} Type294_297")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_homePhone_type_294_297(String homePhoneType) {
        getEligiblePlansAndOffersApiPage.validateInvalidHomePhoneTypeTC294_297(get_eligible_plans_and_offers, valueOf(homePhoneType));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with acnStatus {string} IndicatorTC298_307")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_acnStatus_indicator_TC_298_307(String acnStatusIndicator) {
        getEligiblePlansAndOffersApiPage.validateInvalidAcnStatusIndicatorCasesTC298_307(get_eligible_plans_and_offers, valueOf(acnStatusIndicator));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with CustomerPEWC {string} PreferencesTC308_310")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_CustomerPEWC_Preferences_TC_308_310(String customerPEWCPreferences) {
        getEligiblePlansAndOffersApiPage.validateInvalidCustomerPEWCPreferencesCasesTC308_309(get_eligible_plans_and_offers, valueOf(customerPEWCPreferences));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with credit {string} CheckoptionTC310_312")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_credit_check_option_TC_310_312(String creditCheckOption) {
        getEligiblePlansAndOffersApiPage.validateInvalidCreditCheckOptionCases310_312(get_eligible_plans_and_offers, valueOf(creditCheckOption));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with InitialCreditCheck {string} CustomerCodeTC313_317")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_Initial_Credit_Check_CustomerCode_TC_313_317(String initialCreditCheckCustomerCode) {
        getEligiblePlansAndOffersApiPage.validateInitialCreditCheckCustomerCodeCases313_317(get_eligible_plans_and_offers, valueOf(initialCreditCheckCustomerCode));
    }


    @When("a request is made to the GetEligiblePlansAndOffers Api with  transaction {string} Type")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_transaction_type(String transactionType) {
        getEligiblePlansAndOffersApiPage.validateInvalidTransactionTypeCases(get_eligible_plans_and_offers_mandatory, valueOf(transactionType));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with  customer {string} Type")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_with_customer(String customerTYPE) {
        getEligiblePlansAndOffersApiPage.validateInvalidCustomerTypeCases(get_eligible_plans_and_offers_mandatory, valueOf(customerTYPE));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with enrollment {string} Sources")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_enrollment_sources(String enrollmentSources) {
        getEligiblePlansAndOffersApiPage.validateInvalidEnrollmentSourcesCases(get_eligible_plans_and_offers_mandatory, valueOf(enrollmentSources));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with customer {string} LastName")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_customer_last_name(String customerLastName) {
        getEligiblePlansAndOffersApiPage.validateInvalidCustomerLastNameCases(get_eligible_plans_and_offers_mandatory, valueOf(customerLastName));
    }


    @When("a request is made to the GetEligiblePlansAndOffers Api with tenant {string} Landlord")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_tenant_landlord(String tenantLandlord) {
        getEligiblePlansAndOffersApiPage.validateInvalidTenantLandlordCases(get_eligible_plans_and_offers_mandatory, valueOf(tenantLandlord));
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with Commercial marketer switch  TC_25")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_Commercial_marketer_switch_TC_25() {
        getEligiblePlansAndOffersApiPage.validateTestCondition25(get_eligible_plans_and_offers_mandatory);
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with Commercial marketer switch  TC_26")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_Commercial_marketer_switch_TC_26() {
        getEligiblePlansAndOffersApiPage.validateTestCondition26(get_eligible_plans_and_offers_mandatory);
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with Commercial marketer switch  TC_27")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_Commercial_marketer_switch_TC_27() {
        getEligiblePlansAndOffersApiPage.validateTestCondition27(get_eligible_plans_and_offers_mandatory);
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with Commercial marketer switch  TC_28")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_Commercial_marketer_switch_TC_28() {
        getEligiblePlansAndOffersApiPage.validateTestCondition28(get_eligible_plans_and_offers_mandatory);
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with Commercial marketer switch  TC_29")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_Commercial_marketer_switch_TC_29() {
        getEligiblePlansAndOffersApiPage.validateTestCondition29(get_eligible_plans_and_offers_mandatory);
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with Commercial marketer switch  TC_30")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_Commercial_marketer_switch_TC_30() {
        getEligiblePlansAndOffersApiPage.validateTestCondition30(get_eligible_plans_and_offers_mandatory);
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with Commercial marketer switch  TC_31")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_Commercial_marketer_switch_TC_31() {
        getEligiblePlansAndOffersApiPage.validateTestCondition31(get_eligible_plans_and_offers_mandatory);
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with Residential marketer switch  RSTC11UC50")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_Residential_marketer_switch_RSTC11UC50() throws IOException {
        getEligiblePlansAndOffersApiPage.validateTestConditionRSTC11UC50(get_eligible_plans_and_offers);
    }

    @When("a request is made to the GetEligiblePlansAndOffers for a {string}")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_customer_type_residential_enrollment_source_phone_call(String testCondition) {
        getEligiblePlansAndOffersApiPage.sendGetEligiblePlansAndOffersRequest(get_eligible_plans_and_offers, valueOf(testCondition));
    }

    @When("a request is made to the GetEligiblePlansAndOffers for previously saved incomplete enrollment {string}")
    public void a_request_to_get_eligible_plans_and_offfers_for_incomplete_enrollment(String testCondition){
        getEligiblePlansAndOffersApiPage.sendGetEligiblePlansAndOffersPrevSavedIncompleteEnrollment(get_eligible_plans_and_offers, valueOf(testCondition));
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with {string} promotionCode for {string} condition")
    public void PositiveEligiblePlansAndOffersWithPromotionCodeApi(String promotionCode, String testCondition) {
        getEligiblePlansAndOffersApiPage.validatePositiveTestConditionsPromotionCodeFromExcelData(get_eligible_plans_and_offers, GlobalEnums.PromotionCode.valueOf(promotionCode), valueOf(testCondition));
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api for {string} condition")
    public void PositiveEligiblePlansAndOffersApi(String testCondition) {
        getEligiblePlansAndOffersApiPage.validatePositiveWithNoPromotionCodeTestConditionsFromExcelData(get_eligible_plans_and_offers, valueOf(testCondition));
    }
    @Then("the response should contain the expected plans")
    public void verifyEligibleResponsePlans() {
        getEligiblePlansAndOffersApiPage.verifyResponsePlans();
    }
    @Then("the response plans should contains a {string} plan")
    public void verifyEligiblePlansAndOffersContainPlanCode(String planCode) {
        getEligiblePlansAndOffersApiPage.verifyResponsePlansContainsPlan(GlobalEnums.PlanCode.valueOf(planCode));
    }
}
