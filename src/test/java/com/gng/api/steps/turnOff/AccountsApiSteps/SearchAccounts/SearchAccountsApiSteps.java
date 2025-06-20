package com.gng.api.steps.turnOff.AccountsApiSteps.SearchAccounts;

import com.gng.api.pages.turnOff.AccountsApiPages.SearchAccounts.SearchAccountsApiPage;
import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import static com.gng.api.steps.turnOff.AccountsApiSteps.SearchAccounts.SearchAccountsTOffApiLabel.search_accounts;

@Slf4j
public class SearchAccountsApiSteps {

    private final TestContext testContext;
    private final SearchAccountsApiPage searchAccountsApiPage;

    public SearchAccountsApiSteps(TestContext testContext, SearchAccountsApiPage searchAccountsApiPage) {
        this.testContext = testContext;
        this.searchAccountsApiPage = searchAccountsApiPage;
        testContext.setGetAccountInfoApiPage(searchAccountsApiPage);
    }

    @When("a request is made to the SearchAccounts Api with Last Name And Zipcode And transactionType As TOFF With {string}")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_InValid_OR_Valid_Combination_Of_Last_Name_And_Zipcode_with_transactionType_As_TOFF(String combinationType) {
        searchAccountsApiPage.validateResponseForCombinationOfLastNameAndZipCodeForTC77_78(search_accounts, SearchAccountsTOffApiLabel.valueOf(combinationType));
    }

    @When("a request is made to the SearchAccounts Api with First Name, Last Name And Zipcode with transactionType As TOFF is provided For {string}")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_FirstName_LastName_And_Zipcode_with_transactionType_As_TOFF_is_input(String accountType) {
        searchAccountsApiPage.validateResponseForCombinationOfLastNameAndZipCode(search_accounts, SearchAccountsTOffApiLabel.valueOf(accountType));
    }

    @When("a request is made to the SearchAccounts Api with Valid CustomerBusinessName Parameter with transactionType As TOFF is provided For {string}")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_Valid_CustomerBusinessName_Parameter_with_transactionType_As_TOFF_is_input_For_Commercial_Active(String accountType) {
        searchAccountsApiPage.validateResponseForValidCustomerBusinessName(search_accounts, SearchAccountsTOffApiLabel.valueOf(accountType));
    }

    @When("a request is made to the SearchAccounts Api with Valid AGLC Account Number Parameter with transactionType As TOFF is input For Residential Active account")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_Valid_AGLC_Number_Parameter_with_TOFF_RS_Active_account() {
        searchAccountsApiPage.validateResponseForValidAGLCNumberTC103(search_accounts);
    }

    @When("a request is made to the SearchAccounts Api with Valid Address Details parameters with transactionType As TOFF is input For {string}")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_Valid_Address_Details_Parameters_with_TOFF_RS_Active_account(String testCondition) {
        searchAccountsApiPage.validateResponseForValidAddressDetailsTC104_105(search_accounts, SearchAccountsTOffApiLabel.valueOf(testCondition));
    }

    @When("a request is made to the SearchAccounts Api with Valid SSN Parameter with transactionType As TOFF is input For Active RS Account with Past Due Balance")
    public void a_request_is_made_to_the_SearchAccounts_Api_with_Valid_SSN_Parameter_with_TOFF_RS_Active_account(){
        searchAccountsApiPage.validateResponseForValidSSNTC89(search_accounts);
    }

    @When("a request is made to the SearchAccounts Api with Valid customer and premisesCode with TOFF for {string}")
    public void a_request_made_to_SearchAccounts_Api_Valid_customer_premisesCode(String testCondition){
        searchAccountsApiPage.validateResponseForValidCustomerANDPremiseCode(search_accounts,SearchAccountsTOffApiLabel.valueOf(testCondition));
    }
}
