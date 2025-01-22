package com.gng.api.steps.AccountsApiSteps.SearchAccounts;

import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.pages.AccountsApiPages.SearchAccounts.SearchAccountsApiPage;
import com.gng.api.steps.UsersApiSteps.GetUserRoles.GetUserRolesApiLabel;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

import static com.gng.api.steps.AccountsApiSteps.SearchAccounts.SearchAccountsApiLabel.search_accounts;
import static com.gng.api.steps.AccountsApiSteps.SearchAccounts.SearchAccountsApiLabel.search_accounts_mandatory;
import static com.gng.api.steps.ServiceOrdersSteps.SaveEnrollment.SaveEnrollmentApiLabel.save_enrollment;
import static com.gng.api.steps.UsersApiSteps.GetUserRoles.GetUserRolesApiLabel.get_user_roles;

@Slf4j
    public class SearchAccountsApiSteps {

        private final TestContext testContext;
        private final SearchAccountsApiPage searchAccountsApiPage;

        public SearchAccountsApiSteps(TestContext testContext, SearchAccountsApiPage searchAccountsApiPage) {
            this.testContext = testContext;
            this.searchAccountsApiPage = searchAccountsApiPage;
            testContext.setGetAccountInfoApiPage(searchAccountsApiPage);
        }
    @When("a request is made to the SearchAccounts Api with {string}TC42_TC44")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC42_TC44(String requestID) {
        searchAccountsApiPage.validateInvalidRequestIDCasesTC42_TC44(search_accounts,SearchAccountsApiLabel.valueOf(requestID));
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
    @When("a request is made to the SearchAccounts Api with {string}TC70")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC70(String premisesStateCode) {
        searchAccountsApiPage.validateInvalidPremisesStateCodeFormatCasesTC70(search_accounts, SearchAccountsApiLabel.valueOf(premisesStateCode));
    }
    @When("a request is made to the SearchAccounts Api with {string}TC71")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC71(String premisesZipCode) {
        searchAccountsApiPage.validateInvalidPremisesZipCodeFormatCasesTC71(search_accounts, SearchAccountsApiLabel.valueOf(premisesZipCode));
    }
//    @When("a request is made to the SearchAccounts Api with {string}TC72")
//    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC72(String missingSearchField) {
//        searchAccountsApiPage.validateMissingSearchFieldsCasesTC72(search_accounts, SearchAccountsApiLabel.valueOf(missingSearchField));
//    }
@When("a request is made to the SearchAccounts Api with {string}TC73")
public void a_request_is_made_to_the_SearchAccounts_Api_with_TC73(String customerCode) {
    searchAccountsApiPage.validateInvalidCustomerCodeCasesTC73(search_accounts, SearchAccountsApiLabel.valueOf(customerCode));
}
    @When("a request is made to the SearchAccounts Api with {string}TC74")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC74(String premisesCode) {
        searchAccountsApiPage.validateInvalidPremisesCodeCasesTC74(search_accounts, SearchAccountsApiLabel.valueOf(premisesCode));
    }
    @When("a request is made to the SearchAccounts Api with {string}TC75")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC75(String customerLastName) {
        searchAccountsApiPage.validateInvalidCustomerLastNameCasesTC75(search_accounts, SearchAccountsApiLabel.valueOf(customerLastName));
    }
    @When("a request is made to the SearchAccounts Api with {string}TC76")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC76(String premisesZipCode) {
        searchAccountsApiPage.validateInvalidPremisesZipCodeCasesTC76(search_accounts, SearchAccountsApiLabel.valueOf(premisesZipCode));
    }
    @When("a request is made to the SearchAccounts Api with {string}TC77")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC77(String federalTaxID) {
        searchAccountsApiPage.validateInvalidSSNAndFederalTaxIDCasesTC77(search_accounts, SearchAccountsApiLabel.valueOf(federalTaxID));
    }
    @When("a request is made to the SearchAccounts Api with {string}TC78_TC92")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC78_TC92(String premisesStreetName) {
        searchAccountsApiPage.validateInvalidPremisesStreetNameCasesTC78_TC92(search_accounts, SearchAccountsApiLabel.valueOf(premisesStreetName));
    }
    @When("a request is made to the SearchAccounts Api with {string}TC93")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC93(String premisesCity) {
        searchAccountsApiPage.validateMissingZipCityAndStateAddressFieldsCasesTC93(search_accounts, SearchAccountsApiLabel.valueOf(premisesCity));
    }

    @When("a request is made to the SearchAccounts Api with {string}TC94")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC94(String premisesZipCode) {
        searchAccountsApiPage.validateMissingZipStreetNameAndStateAddressFieldsCasesTC94(search_accounts, SearchAccountsApiLabel.valueOf(premisesZipCode));
    }
    @When("a request is made to the SearchAccounts Api with {string}TC95")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC95(String premisesZipCode) {
        searchAccountsApiPage.validateMissingZipStreetNameAndCityAddressFieldsCasesTC95(search_accounts, SearchAccountsApiLabel.valueOf(premisesZipCode));
    }
    @When("a request is made to the SearchAccounts Api with {string}TC96")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC96(String premisesZipCode) {
        searchAccountsApiPage.validateMissingStreetNameStateAndCityAddressFieldsCasesTC96(search_accounts, SearchAccountsApiLabel.valueOf(premisesZipCode));
    }
    @When("a request is made to the SearchAccounts Api with {string}TC97")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC97(String premisesStateCode) {
        searchAccountsApiPage.validateMissingStateAndZipCodeAddressFieldsCasesTC97(search_accounts, SearchAccountsApiLabel.valueOf(premisesStateCode));
    }
    @When("a request is made to the SearchAccounts Api with {string}TC98")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC98(String premisesStateCode) {
        searchAccountsApiPage.validateMissingCityAndZipCodeAddressFieldsCasesTC98(search_accounts, SearchAccountsApiLabel.valueOf(premisesStateCode));
    }
    @When("a request is made to the SearchAccounts Api with {string}TC99")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC99(String premisesStateCode) {
        searchAccountsApiPage.validateMissingStateAndCityAddressFieldsCasesTC99(search_accounts, SearchAccountsApiLabel.valueOf(premisesStateCode));
    }
    @When("a request is made to the SearchAccounts Api with {string}TC100")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC100(String premisesZipCode) {
        searchAccountsApiPage.validateMissingStreetNameAndZipAddressFieldsCasesTC100(search_accounts,SearchAccountsApiLabel.valueOf(premisesZipCode));
    }
    @When("a request is made to the SearchAccounts Api with {string}TC101")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC101(String premisesStateCode) {
        searchAccountsApiPage.validateMissingStreetNameAndStateAddressFieldsCasesTC101(search_accounts,SearchAccountsApiLabel.valueOf(premisesStateCode));
    }
    @When("a request is made to the SearchAccounts Api with {string}TC102")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC102(String premisesCity) {
        searchAccountsApiPage.validateMissingStreetNameAndCityAddressFieldsCasesTC102(search_accounts,SearchAccountsApiLabel.valueOf(premisesCity));
    }
    @When("a request is made to the SearchAccounts Api with {string}TC103")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC103(String premisesZipCode) {
        searchAccountsApiPage.validateMissingZipAddressFieldsCasesTC103(search_accounts,SearchAccountsApiLabel.valueOf(premisesZipCode));
    }
    @When("a request is made to the SearchAccounts Api with {string}TC104")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC104(String premisesCity) {
        searchAccountsApiPage.validateMissingCityAddressFieldsCasesTC104(search_accounts,SearchAccountsApiLabel.valueOf(premisesCity));
    }
    @When("a request is made to the SearchAccounts Api with {string}TC105")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC105(String premisesStateCode) {
        searchAccountsApiPage.validateMissingStateCodeAddressFieldsCasesTC105(search_accounts,SearchAccountsApiLabel.valueOf(premisesStateCode));
    }
    @When("a request is made to the SearchAccounts Api with {string}TC106")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC106(String premisesStreetName) {
        searchAccountsApiPage.validateMissingStreetNameAddressFieldsCasesTC106(search_accounts,SearchAccountsApiLabel.valueOf(premisesStreetName));
    }
    @When("a request is made to the SearchAccounts Api with an invalid Customer Code TC_107")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_TC107() {
        searchAccountsApiPage.validateAccountNumberSearchWithInvalidCustomerCodeBasedOnTypeTC107(search_accounts);
    }

    @When("a request is made to the SearchAccounts Api  TC_109")
    public void a_request_is_made_to_the_get_SearchAccounts_TC109() {
        searchAccountsApiPage.validateAccountNumberSearchWithoutSSNBasedOnTypeTC109(search_accounts_mandatory);
    }





}
