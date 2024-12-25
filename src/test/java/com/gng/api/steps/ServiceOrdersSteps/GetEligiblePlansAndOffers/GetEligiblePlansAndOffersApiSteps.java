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
        getEligiblePlansAndOffersApiPage.validateInvalidPremisesStreetType(get_eligible_plans_and_offers, GetEligiblePlansAndOffersApiLabel.valueOf(premisesStreetNumber));
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
    @When("a request is made to the GetEligiblePlansAndOffers Api with premises {string} City")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_premises_city (String premisesCity)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidPremisesCityCases(get_eligible_plans_and_offers_mandatory, GetEligiblePlansAndOffersApiLabel.valueOf(premisesCity));
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with premises {string} Zipcode")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_premises_zip_code (String premisesZipCode)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidPremisesZipCodeCases(get_eligible_plans_and_offers_mandatory, GetEligiblePlansAndOffersApiLabel.valueOf(premisesZipCode));
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with premises {string} Countycode")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_premises_county_code (String premisesCountyCode)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidPremisesCountyCodeCases(get_eligible_plans_and_offers_mandatory, GetEligiblePlansAndOffersApiLabel.valueOf(premisesCountyCode));
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with premises {string} Statecode")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_premises_state_code (String premisesStateCode)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidPremisesStateCodeCases(get_eligible_plans_and_offers_mandatory, GetEligiblePlansAndOffersApiLabel.valueOf(premisesStateCode));
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with credit {string} Checkoption")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_credit_check_option (String creditCheckOption)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidCreditCheckOptionCases(get_eligible_plans_and_offers_mandatory, GetEligiblePlansAndOffersApiLabel.valueOf(creditCheckOption));
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with tenant {string} Landlord")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_tenant_landlord (String tenantLandlord)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidTenantLandlordCases(get_eligible_plans_and_offers_mandatory, GetEligiblePlansAndOffersApiLabel.valueOf(tenantLandlord));
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with separateBilling {string} Address")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_separateBilling_address (String separateBillingAddress)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidSeparateBillingAddressCases(get_eligible_plans_and_offers_mandatory, GetEligiblePlansAndOffersApiLabel.valueOf(separateBillingAddress));
    }
    @When("a request is made to the GetEligiblePlansAndOffers Api with acnStatus {string} Indicator")
    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_acnStatus_indicator (String acnStatusIndicator)
    {
        getEligiblePlansAndOffersApiPage.validateInvalidAcnStatusIndicatorCases(get_eligible_plans_and_offers_mandatory, GetEligiblePlansAndOffersApiLabel.valueOf(acnStatusIndicator));
    }
//    @When("a request is made to the GetEligiblePlansAndOffers Api with aglcService {string} LocationID")
//    public void a_request_is_made_to_the_GetEligiblePlansAndOffers_Api_with_aglcService_LocationID (String aglcServiceLocationID)
//    {
//        getEligiblePlansAndOffersApiPage.validateInvalidAglcServiceLocationIDCases(get_eligible_plans_and_offers_mandatory, GetEligiblePlansAndOffersApiLabel.valueOf(aglcServiceLocationID));
//    }
}
