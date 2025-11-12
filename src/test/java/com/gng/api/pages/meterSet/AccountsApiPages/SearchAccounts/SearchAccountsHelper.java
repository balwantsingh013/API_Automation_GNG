package com.gng.api.pages.meterSet.AccountsApiPages.SearchAccounts;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.AccountsPojo.SearchAccounts.SearchAccountsRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.meterSet.AccountsApiSteps.SearchAccounts.SearchAccountsApiLabel;
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

    public void preparePayloadForPositiveTestConditions(SearchAccountsRequest payload, SearchAccountsApiLabel testCondition) {
        setParametersToEmpty(payload);

        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setTransactionType(GlobalEnums.TransactionType.METER_SET.getValue());
        Map<String, Object> activeCustomerData = ApplicationContext.get().getDbAction().getActiveCustomerWithServiceTransferEnrollment();

        switch (testCondition) {
        case MS_SA_LAST_NAME_AND_ZIP_POSITIVE_TC001 -> {
            payload.setCustomerLastName(activeCustomerData.get(UCBCUST_LAST_NAME).toString());
            payload.setPremisesZipCode(activeCustomerData.get(UCBPREM_ZIPC_CODE).toString());
        }
        case MS_SA_FIRST_NAME_LAST_NAME_AND_ZIP_POSITIVE_TC002 -> {
            payload.setCustomerFirstName(activeCustomerData.get(UCBCUST_FIRST_NAME).toString());
            payload.setCustomerLastName(activeCustomerData.get(UCBCUST_LAST_NAME).toString());
            payload.setPremisesZipCode(activeCustomerData.get(UCBPREM_ZIPC_CODE).toString());
        }
            default ->
                payload.setRequestID(FakerDataGenerator.generateString(10));
        }
    }

    public void preparePayloadFromGetEligibleExternalConditions(SearchAccountsRequest payload, SearchAccountsApiLabel testCondition) {
        setParametersToEmpty(payload);
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setTransactionType(GlobalEnums.TransactionType.METER_SET.getValue());
        payload.setCustomerCode(testContext.getGetEligiblePlansAndOffersResponse().getData().getCustomerCode());
        payload.setPremisesCode(testContext.getGetEligiblePlansAndOffersResponse().getData().getPremisesCode());

        switch (testCondition) {
            case MS_GE_RS_ACN_LAND_BYPASS_CREDIT_TC_022,
                 MS_GE_RS_INCL_TIER_5_TC_023,
                 MS_RS_MULTIPLE_PREM_TC_024,
                 MS_RS_CRDS_ENROLLMENT_CREDIT_CHECK_TC_025,
                 MS_CM_CRDS_EN_CREDIT_CHECK_YES_COMM_DEP_PROSP_TC_033,
                 MS_CM_CRDS_EN_CREDIT_CHECK_YES_BUSINESS_NAME_TC_034-> {
                setParametersToEmpty(payload);
                setParametersFromGetEligiblePlansAndOffersResponse(payload, testCondition);
                payload.setTransactionType(GlobalEnums.TransactionType.METER_SET.getValue());
            }

            default -> {
            }
        }
    }
    public void setParametersFromGetEligiblePlansAndOffersResponse(SearchAccountsRequest payload, SearchAccountsApiLabel testCondition){
        payload.setCustomerCode(testContext.getGetEligiblePlansAndOffersResponse().getData().getCustomerCode());
        payload.setPremisesCode(testContext.getGetEligiblePlansAndOffersResponse().getData().getPremisesCode());
    }

    public void preparePayloadForExternalConditions(SearchAccountsRequest payload, SearchAccountsApiLabel testCondition) {
        setParametersToEmpty(payload);
        payload.setRequestID(FakerDataGenerator.generateString(10));
        payload.setTransactionType(GlobalEnums.TransactionType.METER_SET.getValue());
        Map<String, Object> activeCustomerData = null;

        switch (testCondition) {
            case MS_RS_NO_RECORD_FOUND_TC_013, MS_RS_VARIANT_TC_014-> {
                activeCustomerData = ApplicationContext.get().getDbAction().getCustomerInformationCreditScoreTextNoRecord();
                payload.setTransactionType(GlobalEnums.TransactionType.METER_SET.getValue());

                if (activeCustomerData == null) throw new AssertionError();
                payload.setCustomerCode(activeCustomerData.get(UZBENRO_CUST_CODE).toString());
                payload.setPremisesCode(activeCustomerData.get(UZBENRO_PREM_CODE).toString());
            }
            case MS_RS_CREDIT_FREEZE_TC_015 -> {
                activeCustomerData = ApplicationContext.get().getDbAction()
                        .getCustomerInformationCreditFreeze();

                if (activeCustomerData == null) throw new AssertionError();
                payload.setCustomerCode(activeCustomerData.get(UZBENRO_CUST_CODE).toString());
                payload.setPremisesCode(activeCustomerData.get(UZBENRO_PREM_CODE).toString());

            }
            case MS_RS_DENIAL_DUE_TC_016 -> {
                loadCustomerData(payload, testCondition);
                payload.setTransactionType(GlobalEnums.TransactionType.TURN_ON.getValue());
            }
            case MS_GE_RS_INCL_TIER_5_TC_023 -> {
                setParametersToEmpty(payload);
                setParametersFromGetEligiblePlansAndOffersResponse(payload, testCondition);
                payload.setTransactionType(GlobalEnums.TransactionType.METER_SET.getValue());

            }
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
            case MS_RS_DENIAL_DUE_TC_016
                    -> {
                payload.setCustomerLastName(data.get("customerLastName"));
                payload.setCustomerFirstName(data.get("customerFirstName"));
                payload.setAglcServiceLocationID(data.get("aglcServiceLocationID"));
                payload.setPremisesStreetNumber(data.get("premisesStreetNumber"));
                payload.setPremisesStreetName(data.get("premisesStreetName"));
                payload.setPremisesStreetSuffix(data.get("premisesStreetSuffix"));
                payload.setPremisesStreetPostDirection(data.get("premisesStreetPostDirection"));
                payload.setPremisesUnitType(data.get("premisesUnitType"));
                payload.setPremisesUnitNumber(data.get("premisesUnitNumber"));
                payload.setPremisesCity(data.get("premisesCity"));
                payload.setPremisesStateCode(data.get("premisesStateCode"));
                payload.setPremisesZipCode(data.get("premisesZipCode"));
                payload.setAglcAccountNumber(null);
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
