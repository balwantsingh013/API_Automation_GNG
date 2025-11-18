package com.gng.api.steps.turnOn.AccountsApiSteps.SearchAccounts;

import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.pages.turnOn.AccountsApiPages.SearchAccounts.SearchAccountsApiPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

import static com.gng.api.steps.turnOn.AccountsApiSteps.SearchAccounts.SearchAccountsApiLabel.search_accounts;
import static com.gng.api.steps.turnOn.AccountsApiSteps.SearchAccounts.SearchAccountsApiLabel.search_accounts_mandatory;

@Slf4j
public class SearchAccountsApiSteps {

    private final TestContext testContext;
    private final SearchAccountsApiPage searchAccountsApiPage;

    public SearchAccountsApiSteps(TestContext testContext, SearchAccountsApiPage searchAccountsApiPage) {
        this.testContext = testContext;
        this.searchAccountsApiPage = searchAccountsApiPage;
        testContext.setGetAccountInfoApiPage(searchAccountsApiPage);
    }

    @When("a request is made to the SearchAccounts Api with {string} condition")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_condition(String testCondition) {
        searchAccountsApiPage.validateInvalidRequestIDCases(search_accounts, SearchAccountsApiLabel.valueOf(testCondition));
    }

    @When("a request is made to the SearchAccounts Api with {string}TC45_TC48")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC45_TC48(String loginID) {
        searchAccountsApiPage.validateInvalidLoginIDCasesTC45_TC48(search_accounts, SearchAccountsApiLabel.valueOf(loginID));
    }

    @When("a request is made to the SearchAccounts Api with {string}TC49")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC49(String customerCode) {
        searchAccountsApiPage.validateInvalidCustomerCodeCasesTC49(search_accounts, SearchAccountsApiLabel.valueOf(customerCode));
    }

    @When("a request is made to the SearchAccounts Api with {string}TC50")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC50(String premisesCode) {
        searchAccountsApiPage.validateInvalidPremisesCodeCasesTC50(search_accounts, SearchAccountsApiLabel.valueOf(premisesCode));
    }

    @When("a request is made to the SearchAccounts Api with {string}TC51_TC52")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC51_TC52(String transactionType) {
        searchAccountsApiPage.validateInvalidTransactionTypeCasesTC51_TC52(search_accounts, SearchAccountsApiLabel.valueOf(transactionType));
    }

    @When("a request is made to the SearchAccounts Api with {string}TC53")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC53(String businessName) {
        searchAccountsApiPage.validateInvalidBusinessNameCasesTC53(search_accounts, SearchAccountsApiLabel.valueOf(businessName));
    }

    @When("a request is made to the SearchAccounts Api with {string}TC54")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC54(String lastName) {
        searchAccountsApiPage.validateInvalidLastNameFormatCasesTC54(search_accounts, SearchAccountsApiLabel.valueOf(lastName));
    }

    @When("a request is made to the SearchAccounts Api with {string}TC55")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC55(String firstName) {
        searchAccountsApiPage.validateInvalidFirstNameFormatCasesTC55(search_accounts, SearchAccountsApiLabel.valueOf(firstName));
    }

    @When("a request is made to the SearchAccounts Api with {string}TC56_TC57")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC56_TC57(String socialSecurityNumber) {
        searchAccountsApiPage.validateInvalidSocialSecurityNumberFormatCasesTC56_TC57(search_accounts, SearchAccountsApiLabel.valueOf(socialSecurityNumber));
    }

    @When("a request is made to the SearchAccounts Api with {string}TC58_TC59")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC58_TC59(String federalTaxID) {
        searchAccountsApiPage.validateInvalidFederalTaxIDFormatCasesTC58_TC59(search_accounts, SearchAccountsApiLabel.valueOf(federalTaxID));
    }

    @When("a request is made to the SearchAccounts Api with {string}TC60")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC60(String phoneNumber) {
        searchAccountsApiPage.validateInvalidPhoneNumberFormatCasesTC60(search_accounts, SearchAccountsApiLabel.valueOf(phoneNumber));
    }

    @When("a request is made to the SearchAccounts Api with {string}TC61")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC61(String aglcAccountNumber) {
        searchAccountsApiPage.validateInvalidAGLCAccountNumberFormatCasesTC61(search_accounts, SearchAccountsApiLabel.valueOf(aglcAccountNumber));
    }

    @When("a request is made to the SearchAccounts Api with {string}TC62")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC62(String premisesStreetNumber) {
        searchAccountsApiPage.validateInvalidPremisesStreetNumberFormatCasesTC62(search_accounts, SearchAccountsApiLabel.valueOf(premisesStreetNumber));
    }

    @When("a request is made to the SearchAccounts Api with {string}TC63")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC63(String premisesStreetPreDirection) {
        searchAccountsApiPage.validateInvalidPremisesStreetPreDirectionFormatCasesTC63(search_accounts, SearchAccountsApiLabel.valueOf(premisesStreetPreDirection));
    }

    @When("a request is made to the SearchAccounts Api with {string}TC64")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC64(String premisesStreetName) {
        searchAccountsApiPage.validateInvalidPremisesStreetNameFormatCasesTC64(search_accounts, SearchAccountsApiLabel.valueOf(premisesStreetName));
    }

    @When("a request is made to the SearchAccounts Api with {string}TC65")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC65(String premisesStreetSuffix) {
        searchAccountsApiPage.validateInvalidPremisesStreetSuffixFormatCasesTC65(search_accounts, SearchAccountsApiLabel.valueOf(premisesStreetSuffix));
    }

    @When("a request is made to the SearchAccounts Api with {string}TC66")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC66(String premisesStreetPostDirection) {
        searchAccountsApiPage.validateInvalidPremisesStreetPostDirectionFormatCasesTC66(search_accounts, SearchAccountsApiLabel.valueOf(premisesStreetPostDirection));
    }

    @When("a request is made to the SearchAccounts Api with {string}TC67")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC67(String premisesUnitType) {
        searchAccountsApiPage.validateInvalidPremisesUnitTypeFormatCasesTC67(search_accounts, SearchAccountsApiLabel.valueOf(premisesUnitType));
    }

    @When("a request is made to the SearchAccounts Api with {string}TC68")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC68(String premisesUnitNumber) {
        searchAccountsApiPage.validateInvalidPremisesUnitNumberFormatCasesTC68(search_accounts, SearchAccountsApiLabel.valueOf(premisesUnitNumber));
    }

    @When("a request is made to the SearchAccounts Api with {string}TC69")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC69(String premisesCity) {
        searchAccountsApiPage.validateInvalidPremisesCityFormatCasesTC69(search_accounts, SearchAccountsApiLabel.valueOf(premisesCity));
    }

    @When("a request is made to the SearchAccounts Api with {string}TC70 and {string} invalid state code")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC70(String premisesStateCode, String invalidStateCode) {
        searchAccountsApiPage.validateInvalidPremisesStateCodeFormatCasesTC70(search_accounts, SearchAccountsApiLabel.valueOf(premisesStateCode), invalidStateCode);
    }

    @When("a request is made to the SearchAccounts Api with {string} invalid premises zip code for {string} condition")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC71(String invalidPremisesZipCode, String testCondition) {
        searchAccountsApiPage.validateInvalidPremisesZipCodeFormatCasesTC71(search_accounts, invalidPremisesZipCode, SearchAccountsApiLabel.valueOf(testCondition));
    }

    @When("a request is made to the SearchAccounts Api with invalid search parameters for {string} condition")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_invalid_search_parameters(String testCondition) {
        searchAccountsApiPage.validateMissingSearchFieldsCases(search_accounts, SearchAccountsApiLabel.valueOf(testCondition));
    }

    @When("a request is made to the SearchAccounts Api with customer code {string} and premises code {string} that do not exist in database TC_107")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC107(String customerCode, String premisesCode) {
        searchAccountsApiPage.validateAccountNumberSearchWithInvalidCustomerCodePremiseCodeTC107(search_accounts, customerCode, premisesCode);
    }

    @When("a request is made to the SearchAccounts Api with customer business name as {string} returned records exceeds the PSTO value TC_108")
    public void a_request_is_made_to_the_SearchAccounts_Api_returned_records_exceeds_the_PSTO_value_TC108(String customerBusinessName) {
        searchAccountsApiPage.validateReturnedRecordsExceedsPSTOValueTC108(search_accounts, customerBusinessName);
    }

    @When("a request is made to the SearchAccounts Api for Account Number with B Type No SSP {string} TC_109")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_Account_Number_Search_B_Type_No_SSP_TC109(String sspIndicator) {
        searchAccountsApiPage.validateAccountNumberSearchBTypeNoSSPOnTypeTC109(search_accounts, sspIndicator);
    }

    @When("a request is made to the SearchAccounts Api for Account Number with E Type No SSP {string} TC_110")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_Account_Number_Search_E_Type_No_SSP_TC110(String sspIndicator) {
        searchAccountsApiPage.validateAccountNumberSearchETypeNoSSPOnTypeTC110(search_accounts,sspIndicator);
    }

    @When("a request is made to the SearchAccounts Api with SSP {string} TC_111")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_SSP_Based_TC111(String sspIndicator) {
        searchAccountsApiPage.validateLastNameAndZiPBTypESSPBasedOnTypeTC111(search_accounts, sspIndicator);
    }

    @When("a request is made to the SearchAccounts Api with First Name & Last Name & Zip - E Type, SSP TC_112")
    public void a_request_is_made_to_the_SearchAccounts_First_Name_And_Last_Name_And_Zip_E_Type_SSP_TC112() {
        searchAccountsApiPage.validateLastNameAndZiPTC112(search_accounts);
    }

    @When("a request is made to the SearchAccounts Api with AGLC Account Number E Type No SSP TC_114")
    public void a_request_is_made_to_the_SearchAccounts_AGLC_Account_Number_E_Type_No_SSP_TC114() {
        searchAccountsApiPage.validateAGLCAccountNumberETypeNoSSPTC114(search_accounts);
    }

    @When("a request is made to the SearchAccounts Api with Customer Data E Type SSP TC_115")
    public void a_request_is_made_to_the_SearchAccounts_Customer_Data_E_Type_SSP_TC115() {
        searchAccountsApiPage.validateCustomerDataETypeSSPTC115(search_accounts);
    }

    @When("a request is made to the SearchAccounts Api with Street Name And City And State Code And Zip Code TC_116")
    public void a_request_is_made_to_the_SearchAccounts_Street_Name_And_City_And_State_Code_And_Zip_Code_TC116() {
        searchAccountsApiPage.validateStreetNameAndCityAndStateCodeAndZipCodeTC116(search_accounts);
    }

    @When("a request is made to the SearchAccounts Api with Number And PreDir And Suffix And PostDir And Street Name And City And State Code And Zip Code TC_117")
    public void a_request_is_made_to_the_SearchAccounts_Number_And_PreDir_And_Suffix_And_PostDir_And_Street_Name_And_City_And_State_Code_And_Zip_Code_TC117() {
        searchAccountsApiPage.validateNumberAndPreDirAndSuffixAndPostDirAndStreetNameAndCityAndStateCodeAndZipCodeTC117(search_accounts);
    }


    @When("a request is made to the SearchAccounts Api {string}")
    public void a_request_is_made_to_the_SearchAccounts_Api_Based_on_phone_numberTC119(String testCondition) {
        searchAccountsApiPage.validateEnrollmentRecordsBasedOnTheProvidedPhoneNumberTC119(search_accounts, SearchAccountsApiLabel.valueOf(testCondition));
    }
    @When("a request is made to the SearchAccounts Api with Business Name TC_120")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_with_Business_Name_TC_120() {
        searchAccountsApiPage.validateBusinessNameTC120(search_accounts);
    }

    @When("a request is made to the SearchAccounts Api with Wildcard Search TC_121_1")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_with_Wildcard_Search_TC_121() {
        searchAccountsApiPage.validateWildcardSearchTC121(search_accounts);
    }

    @When("a request is made to the SearchAccounts Api with Wildcard Search TC_121_2")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_with_Wildcard_Search_TC_121_2() {
        searchAccountsApiPage.validateWildcardSearchWithCityTC121_2(search_accounts);
    }

    @When("a request is made to the SearchAccounts Api with Account details that have Partial Payment TC_121a")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_with_Partial_Payment_TC_121a() {
        searchAccountsApiPage.validatePartialPaymentTC121a(search_accounts);
    }

    @When("a request is made to the SearchAccountsApi for {string}")
    public void a_request_to_search_accounts_for_prev_saved_enrollment(String testCondition){
        searchAccountsApiPage.validateSearchAccountsForPrevSavedEnrollment(search_accounts, SearchAccountsApiLabel.valueOf(testCondition));
    }


    @When("a request is made to the SearchAccounts Api with Full Payment TC_121b")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_with_Full_Payment_TC_121b() {
        searchAccountsApiPage.validateFullPaymentTC121b(search_accounts);
    }

    @When("a request is made to the SearchAccounts Api with Account details that have No Payment TC_121c")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_with_No_Payment_TC_121c() {
        searchAccountsApiPage.validateNoPaymentTC121c(search_accounts);
    }

    @When("a request is made to the SearchAccounts Api with Multiple Payment TC_121d")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_with_Multiple_Payment_TC_121d() {
        searchAccountsApiPage.validateMultiplePaymentTC121d(search_accounts_mandatory);
    }

    @When("a request is made to the SearchAccounts Api with an SSP Participant Code for {string}")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_an_SSP_Participant_Code_TC121e(String testCondition) {
        searchAccountsApiPage.validateSSPParticipantCodeBasedOnTypeTC121e(search_accounts, SearchAccountsApiLabel.valueOf(testCondition));
    }

    @When("a request is made to the SearchAccounts Api with account that does not exist in SSP Participant parent table TC_112e_3")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_an_not_in_ssp_parent_table_TC121e3() {
        searchAccountsApiPage.validateSSPParticipantCodeBasedOnTypeTC121e3(search_accounts);
    }

    @When("a request is made to the SearchAccounts Api TurnOn with a valid SSN")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_valid_ssn() {
        searchAccountsApiPage.verifySearchAccountAPIWhenValidSSNIsPassed(search_accounts);
    }

    @Given("a prepay transaction is returned from searchAccounts api for {string}")
    public void validTransactionExists(String testCondition) {
        searchAccountsApiPage.verifyPrepayTransactionIdExists(SearchAccountsApiLabel.search_accounts, SearchAccountsApiLabel.valueOf(testCondition));
    }

}
