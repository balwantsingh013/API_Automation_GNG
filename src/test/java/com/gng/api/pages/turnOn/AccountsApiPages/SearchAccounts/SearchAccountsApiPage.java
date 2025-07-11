package com.gng.api.pages.turnOn.AccountsApiPages.SearchAccounts;


import com.gng.api.pages.BasePage;
import com.gng.api.pojo.AccountsPojo.SearchAccounts.SearchAccountsRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.turnOn.AccountsApiSteps.SearchAccounts.SearchAccountsApiLabel;
import io.restassured.response.Response;
import org.apache.http.client.methods.HttpPost;

import static com.gng.api.constants.ApiEndPoint.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public class SearchAccountsApiPage extends BasePage {
    private final SearchAccountsHelper helper;

    public SearchAccountsApiPage(TestContext testContext) {
        super(testContext);
        this.helper = new SearchAccountsHelper(testContext);
    }

    public void validateInvalidRequestIDCasesTC42_TC44(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel requestID) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setRequestIDBasedOnTypeTCTC42_TC44(payload, requestID);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidLoginIDCasesTC45_TC48(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel loginID) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setLoginIDBasedOnTypeTC45_TC48(payload, loginID);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidCustomerCodeCasesTC49(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel customerCode) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setCustomerCodeBasedOnTypeTC49(payload, customerCode);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidPremisesCodeCasesTC50(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel premisesCode) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setPremisesCodeBasedOnTypeTC50(payload, premisesCode);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidTransactionTypeCasesTC51_TC52(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel transactionType) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setTransactionTypeBasedOnTypeTC51_TC52(payload, transactionType);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidBusinessNameCasesTC53(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel customerBusinessName) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setCustomerBusinessNameBasedOnTypeTC53(payload, customerBusinessName);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidLastNameFormatCasesTC54(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel lastName) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setLastNameBasedOnTypeTC54(payload, lastName);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidFirstNameFormatCasesTC55(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel firstName) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setFirstNameBasedOnTypeTC55(payload, firstName);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidSocialSecurityNumberFormatCasesTC56_TC57(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel socialSecurityNumber) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setSocialSecurityNumberBasedOnTypeTC56_TC57(payload, socialSecurityNumber);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidFederalTaxIDFormatCasesTC58_TC59(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel federalTaxID) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setFederalTaxIDBasedOnTypeTC58_TC59(payload, federalTaxID);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidPhoneNumberFormatCasesTC60(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel phoneNumber) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setPhoneNumberBasedOnTypeTC60(payload, phoneNumber);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidAGLCAccountNumberFormatCasesTC61(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel aglcAccountNumber) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setAGLCAccountNumberBasedOnTypeTC61(payload, aglcAccountNumber);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidPremisesStreetNumberFormatCasesTC62(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel premisesStreetNumber) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setPremisesStreetNumberBasedOnTypeTC62(payload, premisesStreetNumber);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidPremisesStreetPreDirectionFormatCasesTC63(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel premisesStreetPreDirection) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setPremisesStreetPreDirectionBasedOnTypeTC63(payload, premisesStreetPreDirection);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidPremisesStreetNameFormatCasesTC64(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel premisesStreetName) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setPremisesStreetNameBasedOnTypeTC64(payload, premisesStreetName);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidPremisesStreetSuffixFormatCasesTC65(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel premisesStreetSuffix) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setPremisesStreetSuffixBasedOnTypeTC65(payload, premisesStreetSuffix);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidPremisesStreetPostDirectionFormatCasesTC66(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel premisesStreetPostDirection) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setPremisesStreetPostDirectionBasedOnTypeTC66(payload, premisesStreetPostDirection);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidPremisesUnitTypeFormatCasesTC67(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel premisesUnitType) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setPremisesUnitTypeBasedOnTypeTC67(payload, premisesUnitType);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidPremisesUnitNumberFormatCasesTC68(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel premisesUnitNumber) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setPremisesUnitNumberBasedOnTypeTC68(payload, premisesUnitNumber);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidPremisesCityFormatCasesTC69(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel premisesCity) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setPremisesCityBasedOnTypeTC69(payload, premisesCity);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidPremisesStateCodeFormatCasesTC70(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel premisesStateCode) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setPremisesStateCodeBasedOnTypeTC70(payload, premisesStateCode);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidPremisesZipCodeFormatCasesTC71(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel premisesZipCode) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setPremisesZipCodeBasedOnTypeTC71(payload, premisesZipCode);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateMissingSearchFieldsCasesTC72(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel missingSearchField) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setMissingSearchFieldsBasedOnTypeTC72(payload, missingSearchField);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidCustomerCodeCasesTC73(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel customerCode) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setCustomerCodeBasedOnTypeTC73(payload, customerCode);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidPremisesCodeCasesTC74(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel customerFirstName) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setPremisesCodeBasedOnTypeTC74(payload, customerFirstName);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidCustomerLastNameCasesTC75(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel customerLastName) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setCustomerLastNameBasedOnTypeTC75(payload, customerLastName);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidPremisesZipCodeCasesTC76(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel premisesZipCode) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setPremisesZipCodeBasedOnTypeTC76(payload, premisesZipCode);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidSSNAndFederalTaxIDCasesTC77(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel federalTaxID) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setSSNAndFederalTaxIDBasedOnTypeTC77(payload, federalTaxID);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateInvalidPremisesStreetNameCasesTC78_TC92(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel premisesStreetName) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setPremisesStreetNameBasedOnTypeTC78_TC92(payload, premisesStreetName);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateMissingZipCityAndStateAddressFieldsCasesTC93(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel premisesCity) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setMissingZipCityAndStateAddressFieldsBasedOnTypeTC93(payload, premisesCity);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateMissingZipStreetNameAndStateAddressFieldsCasesTC94(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel premisesZipCode) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setMissingZipStreetNameAndStateAddressFieldsBasedOnTypeTC94(payload, premisesZipCode);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateMissingZipStreetNameAndCityAddressFieldsCasesTC95(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel premisesZipCode) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setMissingZipStreetNameAndCityAddressFieldsBasedOnTypeTC95(payload, premisesZipCode);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateMissingStreetNameStateAndCityAddressFieldsCasesTC96(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel premisesZipCode) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setMissingStreetNameStateAndCityAddressFieldsBasedOnTypeTC96(payload, premisesZipCode);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateMissingStateAndZipCodeAddressFieldsCasesTC97(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel premisesStateCode) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setMissingStateAndZipCodeAddressFieldsBasedOnTypeTC97(payload, premisesStateCode);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateMissingCityAndZipCodeAddressFieldsCasesTC98(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel premisesStateCode) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setMissingCityAndZipCodeAddressFieldsBasedOnTypeTC98(payload, premisesStateCode);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateMissingStateAndCityAddressFieldsCasesTC99(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel premisesStateCode) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setMissingStateAndCityAddressFieldsBasedOnTypeTC99(payload, premisesStateCode);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateMissingStreetNameAndZipAddressFieldsCasesTC100(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel premisesZipCode) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setMissingStreetNameAndZipAddressFieldsBasedOnTypeTC100(payload, premisesZipCode);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateMissingStreetNameAndStateAddressFieldsCasesTC101(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel premisesStateCode) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setMissingStreetNameAndStateAddressFieldsBasedOnTypeTC101(payload, premisesStateCode);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateMissingStreetNameAndCityAddressFieldsCasesTC102(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel premisesCity) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setMissingStreetNameAndCityAddressFieldsBasedOnTypeTC102(payload, premisesCity);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateMissingZipAddressFieldsCasesTC103(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel premisesZipCode) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setMissingZipAddressFieldsBasedOnTypeTC103(payload, premisesZipCode);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateMissingCityAddressFieldsCasesTC104(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel premisesCity) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setMissingCityAddressFieldsBasedOnTypeTC104(payload, premisesCity);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateMissingStateCodeAddressFieldsCasesTC105(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel premisesStateCode) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setMissingStateCodeAddressFieldsBasedOnTypeTC105(payload, premisesStateCode);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateMissingStreetNameAddressFieldsCasesTC106(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel premisesStreetName) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setMissingStreetNameAddressFieldsBasedOnTypeTC106(payload, premisesStreetName);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateAccountNumberSearchWithInvalidCustomerCodePremiseCodeTC107(SearchAccountsApiLabel apiLabel, String customerCode, String premisesCode) {
        helper.validateCustomerCodeAndPremisesCodeInDB(customerCode,premisesCode);
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setInvalidCustomerCode(payload, customerCode, premisesCode);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateReturnedRecordsExceedsPSTOValueTC108(SearchAccountsApiLabel apiLabel, String customerBusinessName) {
        helper.verifyTheCountOfRecordsRetrivedFromDBIsMoreThan30(customerBusinessName);
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setReturnedRecordsExceedsPSTOValue(payload, customerBusinessName);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateAccountNumberSearchBTypeNoSSPOnTypeTC109(SearchAccountsApiLabel apiLabel, String sspIndicator) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setAccountNumberSearchWithoutSSPBasedOnTypeTC109(payload, sspIndicator);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateAccountNumberSearchETypeNoSSPOnTypeTC110(SearchAccountsApiLabel apiLabel, String sspIndicator) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setAccountNumberSearchETypeNoSSP(payload, sspIndicator);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }


    public void validateLastNameAndZiPBTypESSPBasedOnTypeTC111(SearchAccountsApiLabel apiLabel, String sspIndicator) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setLastNameAndZiPBType(payload, sspIndicator);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateLastNameAndZiPTC112(SearchAccountsApiLabel apiLabel) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setLastNameFirstNameAndZiPBType(payload);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);


    }

    public void validateAGLCAccountNumberETypeNoSSPTC114(SearchAccountsApiLabel apiLabel) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setaglcAccountNumberType(payload);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateCustomerDataETypeSSPTC115(SearchAccountsApiLabel apiLabel) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setCustomerDataETypeSSP(payload);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateStreetNameAndCityAndStateCodeAndZipCodeTC116(SearchAccountsApiLabel apiLabel) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setStreetNameCityStateZip(payload);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateNumberAndPreDirAndSuffixAndPostDirAndStreetNameAndCityAndStateCodeAndZipCodeTC117(SearchAccountsApiLabel apiLabel) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setAddressDetailsWithPreDirection(payload);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }


    public void validateEnrollmentRecordsBasedOnTheProvidedPhoneNumberTC119(SearchAccountsApiLabel apiLabel, SearchAccountsApiLabel testCondition) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setEnrollmentRecordsBasedOnTheProvidedPhoneNumber(payload, testCondition);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }


    public void validateBusinessNameTC120(SearchAccountsApiLabel apiLabel) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setBusinessName(payload);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateWildcardSearchTC121(SearchAccountsApiLabel apiLabel) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setWildcardSearch(payload);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateWildcardSearchWithCityTC121_2(SearchAccountsApiLabel apiLabel) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setWildcardSearchWithCity(payload);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validatePartialPaymentTC121a(SearchAccountsApiLabel apiLabel) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setPartialPayment(payload);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateFullPaymentTC121b(SearchAccountsApiLabel apiLabel) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setFullPayment(payload);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }
    public void validateNoPaymentTC121c(SearchAccountsApiLabel apiLabel) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setNoPayment(payload);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateMultiplePaymentTC121d(SearchAccountsApiLabel apiLabel) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setMultiplePayments(payload);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateSSPParticipantCodeBasedOnTypeTC121e(SearchAccountsApiLabel apiLabel,SearchAccountsApiLabel testCondition) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setSSPParticipantCodeBasedOnTypeTC121e(payload, testCondition);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void validateSSPParticipantCodeBasedOnTypeTC121e3(SearchAccountsApiLabel apiLabel) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setCustCodePremCodeBasedOnTypeTC121e3(payload);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }

    public void verifySearchAccountAPIWhenValidSSNIsPassed(SearchAccountsApiLabel apiLabel) {
        SearchAccountsRequest payload = helper.preparePayload(apiLabel);
        helper.setValidSSNTC113(payload);
        setRequestSpecification(payload, testContext.getAuthToken());
        Response response = sendRequest(HttpPost.METHOD_NAME, SEARCH_ACCOUNTS, 200);
        testContext.setResponse(response);
    }





}















