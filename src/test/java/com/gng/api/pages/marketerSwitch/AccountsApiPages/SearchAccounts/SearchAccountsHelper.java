package com.gng.api.pages.marketerSwitch.AccountsApiPages.SearchAccounts;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.AccountsPojo.SearchAccounts.SearchAccountsRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.marketerSwitch.AccountsApiSteps.SearchAccounts.SearchAccountsApiLabel;
import com.gng.api.util.ExcelReader;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.gng.api.constants.DBConstant.*;
import static com.gng.api.constants.TestConstant.CUSTOMER_DATA;
import static com.gng.api.constants.TestConstant.CUSTOMER_SHEET_NAME;
import static com.gng.api.steps.AesEncryption.AesEncryptionSteps.encryptData;

@Slf4j
public class SearchAccountsHelper {
    private final TestContext testContext;


    public SearchAccountsHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    SearchAccountsRequest preparePayload(SearchAccountsApiLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        String jsonFileName = apiLabel.equals(SearchAccountsApiLabel.search_accounts)
                ? SearchAccountsApiLabel.search_accounts.toString()
                : SearchAccountsApiLabel.search_accounts_mandatory.toString();
        return BasePage.deserializeJsonToPojo(jsonFileName, SearchAccountsRequest.class);
    }

    public void preparePayloadFromGetEligibleExternalConditions(SearchAccountsRequest payload, SearchAccountsApiLabel testCondition) {
        setParametersToEmpty(payload);
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setTransactionType(GlobalEnums.TransactionType.MKSW.getValue());
        setParametersFromGetEligiblePlansAndOffersResponse(payload, testCondition);

    }
    public void setParametersFromGetEligiblePlansAndOffersResponse(SearchAccountsRequest payload, SearchAccountsApiLabel testCondition){
        payload.setCustomerCode(testContext.getGetEligiblePlansAndOffersResponse().getData().getCustomerCode());
        payload.setPremisesCode(testContext.getGetEligiblePlansAndOffersResponse().getData().getPremisesCode());
    }

    public void preparePayloadForExternalConditions(SearchAccountsRequest payload, SearchAccountsApiLabel testCondition) {
        setParametersToEmpty(payload);
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setTransactionType(GlobalEnums.TransactionType.MKSW.getValue());

        switch (testCondition) {
            case GE_MRK_SW_RS_NEW_CC_YES_UC50_ALT_PATH_TC12 ->  loadCustomerData(payload, testCondition);
            default -> {
            }
        }
    }

    public void setParametersToEmpty(SearchAccountsRequest payload){
        payload.setCustomerCode("");
        payload.setCustomerLastName("");
        payload.setCustomerFirstName("");
        payload.setPremisesZipCode("");
        payload.setCustomerBusinessName("");
        payload.setPremisesCode("");
    }
    public void loadCustomerData(SearchAccountsRequest payload, SearchAccountsApiLabel testCondition){
        payload.setRequestID(FakerDataGenerator.generateString(10));
        Map<String, String> customerData = loadRowFromExcelToCustomerData(CUSTOMER_DATA, CUSTOMER_SHEET_NAME, testCondition);
        getCustomerAndPremiseDetails(payload, customerData, testCondition);
    }

    public void getCustomerAndPremiseDetails(SearchAccountsRequest payload, Map<String, String> data, SearchAccountsApiLabel testCondition ){

        switch (testCondition){
            case GE_MRK_SW_RS_NEW_CC_YES_UC50_ALT_PATH_TC12
                    -> {
                payload.setCustomerLastName(data.get("customerLastName"));
                payload.setCustomerFirstName(data.get("customerFirstName"));
            }
            default -> {
                payload.setCustomerLastName(data.get("customerLastName"));
                payload.setCustomerFirstName(data.get("customerFirstName"));
                payload.setAglcAccountNumber(data.get("aclcAccountNumber"));
                payload.setPremisesStreetNumber(data.get("premisesStreetNumber"));
                payload.setPremisesStreetName(data.get("premisesStreetName"));
                payload.setPremisesStreetSuffix(data.get("premisesStreetSuffix"));
                payload.setPremisesStreetPostDirection(data.get("premisesStreetPostDirection"));
                payload.setPremisesUnitType(data.get("premisesUnitType"));
                payload.setPremisesUnitNumber(data.get("premisesUnitNumber"));
                payload.setPremisesCity(data.get("premisesCity"));
                payload.setPremisesStateCode(data.get("premisesStateCode"));
                payload.setPremisesZipCode(data.get("premisesZipCode"));
            }
        }

        String ssn = data.get("SSN");
        if (ssn != null && !ssn.trim().isEmpty()) {
            payload.setSocialSecurityNumber(encryptData(data.get("SSN")));
        }
        String federalTaxId = data.get("federalTaxId");
        if (federalTaxId != null && !federalTaxId.trim().isEmpty()) {
            payload.setFederalTaxID(encryptData(data.get("federalTaxId")));
        }
    }

    public static <E extends Enum<E>> Map<String, String> loadRowFromExcelToCustomerData(
            String excelPath,
            String sheetName,
            E testLabel) {

        try {
            ExcelReader reader = new ExcelReader(excelPath);
            List<Map<String, String>> sheetData = reader.getSheetData(sheetName);

            return sheetData.stream()
                    .filter(row -> {
                        String condition = row.get("testCondition");
                        return condition != null && condition.contains(testLabel.name());
                    }).findFirst()
                    .orElseThrow(() -> new RuntimeException(
                            "No matching testConditions found containing: " + testLabel.name()));

        } catch (IOException e) {
            throw new RuntimeException("Failed to load data from Excel", e);
        }
    }
}
