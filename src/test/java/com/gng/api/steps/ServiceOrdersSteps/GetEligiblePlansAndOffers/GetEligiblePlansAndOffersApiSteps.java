package com.gng.api.steps.ServiceOrdersSteps.GetEligiblePlansAndOffers;


import com.gng.api.pages.ServiceOrdersPages.GetEligiblePlansAndOffersPage.GetEligiblePlansAndOffersApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.ServiceOrdersSteps.SaveEnrollment.SaveEnrollmentApiLabel;
import io.cucumber.java.en.When;

import static com.gng.api.steps.ServiceOrdersSteps.GetEligiblePlansAndOffers.GetEligiblePlansAndOffersApiLabel.*;
import static com.gng.api.steps.ServiceOrdersSteps.SaveEnrollment.SaveEnrollmentApiLabel.save_enrollment_mandatory;

public class GetEligiblePlansAndOffersApiSteps {

    private final TestContext testContext;
    private final GetEligiblePlansAndOffersApiPage getEligiblePlansAndOffersApiPage;

    public GetEligiblePlansAndOffersApiSteps(TestContext testContext, GetEligiblePlansAndOffersApiPage getEligiblePlansAndOffersApiPage ) {
        this.testContext = testContext;
        this.getEligiblePlansAndOffersApiPage = getEligiblePlansAndOffersApiPage;
        testContext.setGetEligiblePlansAndOffersApiPage(getEligiblePlansAndOffersApiPage);
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with customer type residential credit check as yes and promotion code as null TC_318_UC 39")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_customer_type_residential_credit_check_as_yes_and_promotion_code_as_null_TC_318_UC_39() {
        getEligiblePlansAndOffersApiPage.sendGetEligiblePlansAndOffersRequestWithPromotionCodeAsNull(get_eligible_plans_and_offers);
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with customer type residential credit check as yes with no promotion code  TC_319_UC 44")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_customer_type_residential_credit_check_as_yes_with_no_promotion_code_TC_319_UC_44() {
        getEligiblePlansAndOffersApiPage.sendGetEligiblePlansAndOffersRequestWithNoPromotionCode(get_eligible_plans_and_offers);
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with customer type residential credit check as yes yes with no promotion code  TC_320_UC 46")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_customer_type_residential_credit_check_as_yes_with_no_promotion_codeTC_320_UC_46() {
        getEligiblePlansAndOffersApiPage.sendGetEligiblePlansAndOffersRequestWithNoPromotionCodeTC320(get_eligible_plans_and_offers);
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with customer type residential credit check as yes with  promotion code  TC_321_UC 64")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_customer_type_residential_credit_check_as_yes_with_promotion_code_TC_321_UC_64() {
        getEligiblePlansAndOffersApiPage.sendGetEligiblePlansAndOffersRequestWithPromotionCode(get_eligible_plans_and_offers);
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with customer type residential credit check as yes with no promotion code  TC_322_UC 45")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_customer_type_residential_credit_check_as_yes_with_no_promotion_code_TC_322_UC_45() {
        getEligiblePlansAndOffersApiPage.sendGetEligiblePlansAndOffersRequestWithNoPromotionCodeTC322(get_eligible_plans_and_offers);
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with customer type residential credit check as yes with no promotion code  TC_323_UC NA")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_customer_type_residential_credit_check_as_yes_with_no_promotion_code_TC_323_UC_NA() {
        getEligiblePlansAndOffersApiPage.sendGetEligiblePlansAndOffersRequestWithNoPromotionCodeTC323(get_eligible_plans_and_offers);
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with customer type residential credit check as yes with no promotion code  TC_324_UC NA")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_customer_type_residential_credit_check_as_yes_with_no_promotion_code_TC_324_UC_NA() {
        getEligiblePlansAndOffersApiPage.sendGetEligiblePlansAndOffersRequestWithNoPromotionCodeTC324(get_eligible_plans_and_offers);
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with customer type residential credit check as Service Transfer with no promotion code  TC_325_UC 55")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_customer_type_residential_credit_check_as_Service_Transfer_with_no_promotion_code_TC_325_UC_55() {
        getEligiblePlansAndOffersApiPage.sendGetEligiblePlansAndOffersRequestWithNoPromotionCodeTC325(get_eligible_plans_and_offers);
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with customer type residential credit check as Comm with no promotion code  TC_326_UC 56")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_customer_type_residential_credit_check_as_Comm_with_no_promotion_code_TC_326_UC_56() {
        getEligiblePlansAndOffersApiPage.sendGetEligiblePlansAndOffersRequestWithNoPromotionCodeTC326(get_eligible_plans_and_offers);
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with customer type residential credit check as yes with  promotion code  TC_327_UC 40")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_customer_type_residential_credit_check_as_yes_with_promotion_code_TC_327_UC_40() {
        getEligiblePlansAndOffersApiPage.sendGetEligiblePlansAndOffersRequestWithPromotionCodeTC327(get_eligible_plans_and_offers);
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with customer type residential credit check as yes with no promotion code  TC_328_UC NA")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_customer_type_residential_credit_check_as_yes_with_no_promotion_code_TC_328_UC_NA() {
        getEligiblePlansAndOffersApiPage.sendGetEligiblePlansAndOffersRequestWithPromotionCodeTC328(get_eligible_plans_and_offers);
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with {string}")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with(String requestID)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidRequestIDCases(get_eligible_plans_and_offers_mandatory, GetEligiblePlansAndOffersApiLabel.valueOf(requestID));
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with {string} Type")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_test_Condition_Type(String testCondition)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidTestCondition(get_eligible_plans_and_offers_mandatory, GetEligiblePlansAndOffersApiLabel.valueOf(testCondition));
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with {string} code Type")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_invalid_referral_code_Type(String referralCode)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidReferralCode(get_eligible_plans_and_offers, GetEligiblePlansAndOffersApiLabel.valueOf(referralCode));
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with {string}  Type")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_invalid_premises_street_Type(String premisesStreetNumber)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidPremisesStreetNumberTC242(get_eligible_plans_and_offers, GetEligiblePlansAndOffersApiLabel.valueOf(premisesStreetNumber));
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with {string} test cases 243")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_invalid_premises_street_Direction_test_case_243(String premisesStreetPreDirection)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidPremisesStreetPreDirectionTC243(get_eligible_plans_and_offers, GetEligiblePlansAndOffersApiLabel.valueOf(premisesStreetPreDirection));
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with {string} test cases 244_245")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_invalid_premises_street_name_test_case_244_245(String premisesStreetName)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidPremisesStreetNameTC244_245(get_eligible_plans_and_offers, GetEligiblePlansAndOffersApiLabel.valueOf(premisesStreetName));
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with {string} test cases 246_246a")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_invalid_premises_street_suffix_test_case_246_246a(String premisesStreetSuffix)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidPremisesStreetSuffixTC246_246a(get_eligible_plans_and_offers, GetEligiblePlansAndOffersApiLabel.valueOf(premisesStreetSuffix));
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with {string} test cases 247_247a")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_invalid_premises_street_post_Direction_test_case_247_247a(String premisesStreetPostDirection)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidPremisesStreetPostDirectionTC247_247a(get_eligible_plans_and_offers, GetEligiblePlansAndOffersApiLabel.valueOf(premisesStreetPostDirection));
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with {string} test cases 248_248a")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_invalid_premises_unit_type_test_case_248_248a(String premisesUnitType)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidPremisesUnitTypeTC248_248a(get_eligible_plans_and_offers, GetEligiblePlansAndOffersApiLabel.valueOf(premisesUnitType));
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with {string} test cases 249")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_invalid_premises_unit_number_test_case_249(String premisesUnitNumber)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidPremisesUnitNumberTC249(get_eligible_plans_and_offers, GetEligiblePlansAndOffersApiLabel.valueOf(premisesUnitNumber));
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with {string} test cases 250_251")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_invalid_premises_city_test_case_250_251(String premisesCity)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidPremisesUnitNumberTC250_251(get_eligible_plans_and_offers, GetEligiblePlansAndOffersApiLabel.valueOf(premisesCity));
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with premises {string} Statecode 252_253")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_premises_state_code_252_253 (String premisesStateCode)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidPremisesStateCodeCases252_253(get_eligible_plans_and_offers_mandatory, GetEligiblePlansAndOffersApiLabel.valueOf(premisesStateCode));
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with premises {string} Zipcode254_255c")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_premises_zip_code_254_255c (String premisesZipCode)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidPremisesZipCodeCases254_255C(get_eligible_plans_and_offers_mandatory, GetEligiblePlansAndOffersApiLabel.valueOf(premisesZipCode));
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with premises {string} Countycode256_257")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_premises_county_code_256_257 (String premisesCountyCode)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidPremisesCountyCodeCases256_257(get_eligible_plans_and_offers_mandatory, GetEligiblePlansAndOffersApiLabel.valueOf(premisesCountyCode));
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with separateBilling {string} Address258_283b")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_separateBilling_address_258_283b (String separateBillingAddress)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidSeparateBillingAddressCases258_283b(get_eligible_plans_and_offers_mandatory, GetEligiblePlansAndOffersApiLabel.valueOf(separateBillingAddress));
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with workPhone {string} Number284_286")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_workPhone_number_284_286 (String workPhoneNumber)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidWorkPhoneNumberTC284_286(get_eligible_plans_and_offers_mandatory, GetEligiblePlansAndOffersApiLabel.valueOf(workPhoneNumber));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with WorkPhone {string} Type287_290")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_workPhone_type_287_290 (String workPhoneType)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidWorkPhoneTypeTC287_290(get_eligible_plans_and_offers, GetEligiblePlansAndOffersApiLabel.valueOf(workPhoneType));
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with HomePhone {string} Number291_293")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_homePhone_number_291_293 (String homePhoneNumber)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidHomePhoneNumberTC291_293(get_eligible_plans_and_offers, GetEligiblePlansAndOffersApiLabel.valueOf(homePhoneNumber));
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with HomePhone {string} Type294_297")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_homePhone_type_294_297 (String homePhoneType)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidHomePhoneTypeTC294_297(get_eligible_plans_and_offers, GetEligiblePlansAndOffersApiLabel.valueOf(homePhoneType));
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with acnStatus {string} IndicatorTC298_307")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_acnStatus_indicator_TC_298_307 (String acnStatusIndicator)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidAcnStatusIndicatorCasesTC298_307(get_eligible_plans_and_offers_mandatory, GetEligiblePlansAndOffersApiLabel.valueOf(acnStatusIndicator));
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with CustomerPEWC {string} PreferencesTC308_310")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_CustomerPEWC_Preferences_TC_308_310 (String customerPEWCPreferences)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidCustomerPEWCPreferencesCasesTC308_309(get_eligible_plans_and_offers_mandatory, GetEligiblePlansAndOffersApiLabel.valueOf(customerPEWCPreferences));
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with credit {string} CheckoptionTC310_312")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_credit_check_option_TC_310_312 (String creditCheckOption)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidCreditCheckOptionCases310_312(get_eligible_plans_and_offers_mandatory, GetEligiblePlansAndOffersApiLabel.valueOf(creditCheckOption));
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with InitialCreditCheck {string} CustomerCodeTC313_315")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_Initial_Credit_Check_CustomerCode_TC_313_315 (String initialCreditCheckCustomerCode)
    {
        getEligiblePlansAndOffersApiPage.validateInitialCreditCheckCustomerCodeCases313_315(get_eligible_plans_and_offers_mandatory, GetEligiblePlansAndOffersApiLabel.valueOf(initialCreditCheckCustomerCode));
    }









    @When("a request is made to the GetEligiblePlansAndOffers Api with login {string} ID")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_login_ID (String loginID)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidLoginIDCases(get_eligible_plans_and_offers_mandatory, GetEligiblePlansAndOffersApiLabel.valueOf(loginID));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with  transaction {string} Type")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_transaction_type (String transactionType)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidTransactionTypeCases(get_eligible_plans_and_offers_mandatory, GetEligiblePlansAndOffersApiLabel.valueOf(transactionType));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with  customer {string} Type")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_with_customer(String customerTYPE)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidCustomerTypeCases(get_eligible_plans_and_offers_mandatory, GetEligiblePlansAndOffersApiLabel.valueOf(customerTYPE));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with enrollment {string} Sources")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_enrollment_sources (String enrollmentSources)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidEnrollmentSourcesCases(get_eligible_plans_and_offers_mandatory, GetEligiblePlansAndOffersApiLabel.valueOf(enrollmentSources));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with customer {string} LastName")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_customer_last_name (String customerLastName)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidCustomerLastNameCases(get_eligible_plans_and_offers_mandatory, GetEligiblePlansAndOffersApiLabel.valueOf(customerLastName));
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with customer {string} FirstName")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_customer_first_name (String customerFirstName)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidCustomerFirstNameCases(get_eligible_plans_and_offers_mandatory, GetEligiblePlansAndOffersApiLabel.valueOf(customerFirstName));
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with seasonal savings program {string} Indicator")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_seasonal_savings_program_indicator(String seasonalSavingsProgramIndicator)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidSeasonalSavingsProgramIndicatorCases(get_eligible_plans_and_offers_mandatory, GetEligiblePlansAndOffersApiLabel.valueOf(seasonalSavingsProgramIndicator));
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with premises {string} StreetName")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_premises_street_name (String premisesStreetName)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidPremisesStreetNameCases(get_eligible_plans_and_offers_mandatory, GetEligiblePlansAndOffersApiLabel.valueOf(premisesStreetName));
    }

    @When("a request is made to the GetEligiblePlansAndOffers Api with tenant {string} Landlord")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_tenant_landlord (String tenantLandlord)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidTenantLandlordCases(get_eligible_plans_and_offers_mandatory, GetEligiblePlansAndOffersApiLabel.valueOf(tenantLandlord));
    }


//    @When("a request is made to the GetEligiblePlansAndOffers Api with aglcService {string} LocationID")
//    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_aglcService_LocationID (String aglcServiceLocationID)
//    {
//        getEligiblePlansAndOffersApiPage.validateInvalidAglcServiceLocationIDCases(get_eligible_plans_and_offers_mandatory, GetEligiblePlansAndOffersApiLabel.valueOf(aglcServiceLocationID));
//    }
}
