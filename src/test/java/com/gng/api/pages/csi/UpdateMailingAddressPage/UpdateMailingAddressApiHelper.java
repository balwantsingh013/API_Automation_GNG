package com.gng.api.pages.csi.UpdateMailingAddressPage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.UpdateMailingAddress.UpdateMailingAddressRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.UpdateMailingAddress.UpdateMailingAddressLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

@Slf4j
public class UpdateMailingAddressApiHelper {

    private final TestContext testContext;

    // Variables for hardcoded values
    private static final String DUPLICATE_REQUEST_ID = GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue();
    private static final String NON_EXISTENT_CUSTOMER_CODE_PREFIX = "9";
    private static final String NON_EXISTENT_PREMISES_CODE_PREFIX = "9";
    private static final String STREET_PRE_DIRECTION_TOO_LONG = "NORTH";
    private static final String STREET_PRE_DIRECTION_INVALID = "XX";
    private static final String STREET_SUFFIX_INVALID = "ZZZ";
    private static final String STREET_POST_DIRECTION_TOO_LONG = "WESTSIDE";
    private static final String STREET_POST_DIRECTION_INVALID = "XX";
    private static final String CITY_INVALID_COMBINATION = "Atlanta";
    private static final String ZIPCODE_INVALID_COMBINATION = "30043";
    private static final String COUNTY_CODE_INVALID = "999";
    private static final String DELIVERY_POINT_INVALID = "ABC";
    private static final String CARRIER_ROUTE_INVALID = "ABCDE";
    private static final String STREET_NAME_VALID = "Main";
    private static final String STREET_NUMBER_VALID = "102";
    private static final String CITY_VALID = "PRESTON";
    private static final String ZIPCODE_VALID = "31824";
    private static final String POBOX_VALID = "400";
    private static final String RURAL_ROUTE_VALID = "RR 8";
    private static final String STREET_NAME_TOO_MANY = "Main St";
    private static final String POBOX_TOO_MANY = "123";
    private static final String RURAL_ROUTE_TOO_MANY = "RR 5";
    private static final String ZIPCODE_INVALID = "99799";

    public UpdateMailingAddressApiHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    UpdateMailingAddressRequest preparePayload(UpdateMailingAddressLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        String jsonFileName = apiLabel.equals(UpdateMailingAddressLabel.update_mailing_address)
                ? UpdateMailingAddressLabel.update_mailing_address.toString()
                : UpdateMailingAddressLabel.update_mailing_address_mandatory.toString();
        return BasePage.deserializeJsonToPojo(jsonFileName, UpdateMailingAddressRequest.class);
    }

    public void validateIfLoginIdIsSaved() {
        String expectedLoginId = testContext.getLoginId();
        Map<String, Object> accountData = ApplicationContext.get().getDbAction().getStoredLoginId(expectedLoginId);
        Assert.assertNotNull(accountData, "Expected GZRAPIL row for loginID but got null: " + expectedLoginId);
        Assert.assertEquals(accountData.get("gzrapil_login_id").toString(), expectedLoginId);
    }

    public void databaseValidationsForPositiveTCs(UpdateMailingAddressLabel testCondition){
        Map<String, Object> accountData=null;
        String customerCode= testContext.getCustomerCode();

        switch (testCondition) {
            case TC_117__Positive__Valid_Street_Address___Minimum_parameters_and_No_Existing_Address____,
                 TC_118__Positive__Valid_Street_Address___Maximum_parameters_and_No_Existing_Address____,
                 TC_119__Positive__Valid_Street_Address___Mixed_parameters_and_No_Existing_Address____:
                accountData = ApplicationContext.get().getDbAction().performDatabaseValidationsTC113(customerCode);
                Assert.assertNotNull(accountData, "accountData should not be null");
                break;


            case TC_126__Positive__Valid_PO_Box_Address:
             accountData = ApplicationContext.get().getDbAction().performDatabaseValidations2(customerCode);
            Assert.assertEquals(accountData.get("UCRADDR_STREET_LINE2").toString(),"PO BOX "+POBOX_VALID);
            break;

            case TC_127__Positive__Valid_Rural_Route_Address:
                accountData = ApplicationContext.get().getDbAction().performDatabaseValidations2(customerCode);
                Assert.assertEquals(accountData.get("UCRADDR_STREET_LINE2").toString(),RURAL_ROUTE_VALID);
                break;

            case TC_120__Positive__Valid_Street_Address___Minimum_parameters_and_Existing_Address_and_Same_Day____,
                 TC_121__Positive__Valid_Street_Address___Maximum_parameters_and_Existing_Address_and_Same_Day____,
                 TC_122__Positive__Valid_Street_Address___Mixed_parameters_and_Existing_Address_and_Same_Day____:
            accountData = ApplicationContext.get().getDbAction().performDatabaseValidations2(customerCode);
            break;


            case TC_123__Positive__Valid_Street_Address___Minimum_parameters_and_Existing_Address_and_Different_Day____,
                 TC_124__Positive__Valid_Street_Address___Maximum_parameters_and_Existing_Address_and_Different_Day____,
                 TC_125__Positive__Valid_Street_Address___Mixed_parameters_and_Existing_Address_and_Different_Day____:
                accountData = ApplicationContext.get().getDbAction().performDatabaseValidationsTC119(customerCode);
                Assert.assertNotNull(accountData, "accountData should not be null");
                accountData = ApplicationContext.get().getDbAction().performDatabaseValidationsTC119_2(customerCode);
                Assert.assertNotNull(accountData, "accountData should not be null");
                break;
        }


    }

    public void preparePayloadForTestCondition(UpdateMailingAddressRequest payload, UpdateMailingAddressLabel testCondition) {
        Map<String, Object> dbValues = null;
        Map<String, Object> accountData=null;
        List<Map<String, Object>> accountsData=null;
        payload.setRequestID(FakerDataGenerator.generateAlphanumeric(7));
        String customerCode="";
        switch (testCondition) {
            case TC_80__Negative__Missing_Request_ID:
                payload.setRequestID("");
                break;

            case TC_81__Negative__Invalid_Request_ID_Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(33));
                break;

            case TC_82__Negative__Duplicate_Request_ID:
                payload.setRequestID(DUPLICATE_REQUEST_ID);
                break;

            case TC_83__Negative__Missing_customerCode:
                payload.setCustomerCode("");
                break;

            case TC_84__Negative__Invalid_customerCode_Length:
                payload.setCustomerCode(FakerDataGenerator.generateDigits(10));
                break;

            case TC_85__Negative__Invalid_customerCode_Format:
                payload.setCustomerCode(FakerDataGenerator.generateAlphanumericWithSpecialChars(6));
                break;

            case TC_86__Negative__Invalid_Account_Number:
                payload.setCustomerCode("9988776");
                Map<String, Object> userInfo = ApplicationContext.get().getDbAction().getUserAccountInfo("9988776");
                payload.setCustomerCode("9988776");
                payload.setPremisesCode(FakerDataGenerator.generateDigits(6));
                break;

            case TC_87__Negative__Missing_premisesCode:
                payload.setPremisesCode("");
                break;

            case TC_88__Negative__Invalid_premisesCode_Length:
                payload.setPremisesCode(FakerDataGenerator.generateDigits(8));
                break;

            case TC_89__Negative__Invalid_premisesCode_Format:
                payload.setPremisesCode(FakerDataGenerator.generateAlphanumericWithSpecialChars(5));
                break;

            case TC_90__Negative__Invalid_Address_Fields_Missing:
                payload.setStreetName("");
                payload.setPoBox("");
                payload.setRuralRoute("");
                break;

            case TC_91__Negative__Invalid_Address_Fields_Too_Many:
                payload.setStreetName(STREET_NAME_TOO_MANY);
                payload.setPoBox(POBOX_TOO_MANY);
                payload.setRuralRoute(RURAL_ROUTE_TOO_MANY);
                break;

            case TC_92__Negative__Invalid_Street_Number_Length:
                payload.setStreetNumber(FakerDataGenerator.generateAlphanumeric(13));
                break;

            case TC_93__Negative__Invalid_Street_Pre_Direction_Length:
                payload.setStreetPreDirection(STREET_PRE_DIRECTION_TOO_LONG);
                break;

            case TC_94__Negative__Invalid_Street_Pre_Direction:
                accountData = ApplicationContext.get().getDbAction().getAccountWithoutAddress();
                payload.setCustomerCode(accountData.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountData.get("UCRACCT_PREM_CODE").toString());
                payload.setStreetPreDirection(STREET_PRE_DIRECTION_INVALID);
                accountsData = ApplicationContext.get().getDbAction().verifyThePredirection();
                break;

            case TC_95__Negative__Invalid_Street_Name_Length:
                payload.setStreetName(FakerDataGenerator.generateString(31));
                break;

            case TC_96__Negative__Missing_StreetName:
                payload.setStreetName("");
                break;

            case TC_97__Negative__Invalid_StreetSuffix_Length:
                payload.setStreetSuffix(FakerDataGenerator.generateString(7));
                break;

            case TC_98__Negative__Invalid_StreetSuffix:
                accountData = ApplicationContext.get().getDbAction().getAccountWithoutAddress();
                payload.setCustomerCode(accountData.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountData.get("UCRACCT_PREM_CODE").toString());
                payload.setStreetSuffix(STREET_SUFFIX_INVALID);
                accountsData = ApplicationContext.get().getDbAction().verifyTheStreetSuffix();
                break;

            case TC_99__Negative__Invalid_Street_Post_Direction_Length:
                payload.setStreetPostDirection(STREET_POST_DIRECTION_TOO_LONG);
                break;

            case TC_100__Negative__Invalid_Street_Post_Direction:
                accountData = ApplicationContext.get().getDbAction().getAccountWithoutAddress();
                payload.setCustomerCode(accountData.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountData.get("UCRACCT_PREM_CODE").toString());
                payload.setStreetPostDirection(STREET_POST_DIRECTION_INVALID);
                accountsData = ApplicationContext.get().getDbAction().verifyThePredirection();
                break;

            case TC_101__Negative__Invalid_Unit_Type_Format:
                payload.setUnitType(FakerDataGenerator.generateString(7));
                break;

            case TC_102__Negative__Invalid_Unit_Type:
                String unitType=FakerDataGenerator.generateString(5);
                accountData = ApplicationContext.get().getDbAction().getAccountWithoutAddress();
                payload.setCustomerCode(accountData.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountData.get("UCRACCT_PREM_CODE").toString());
                payload.setUnitType(unitType);
                accountsData = ApplicationContext.get().getDbAction().verifyTheUnitType();
                break;

            case TC_103__Negative__Invalid_Unit_Number_Format:
                payload.setUnitNumber(FakerDataGenerator.generateDigits(7));
                break;

            case TC_104__Negative__Invalid_City_Length:
                payload.setCity(FakerDataGenerator.generateString(21));
                break;

            case TC_105__Negative__Missing_City:
                payload.setCity("");
                break;

            case TC_106__Negative__Invalid_Zip_Code_Format_Length:
                payload.setZipCode(FakerDataGenerator.generateDigits(11));
                break;

            case TC_107__Negative__Missing_Zip_Code:
                payload.setZipCode("");
                break;

            case TC_108__Negative__Invalid_Zip_Code:
                accountData = ApplicationContext.get().getDbAction().getAccountWithoutAddress();
                payload.setCustomerCode(accountData.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountData.get("UCRACCT_PREM_CODE").toString());
                payload.setZipCode(ZIPCODE_INVALID);
                accountData = ApplicationContext.get().getDbAction().verifyTheZipCode(ZIPCODE_INVALID);
                break;

            case TC_109__Negative__Invalid_City_and_Zip_Code_Combination:
                accountData = ApplicationContext.get().getDbAction().getAccountWithoutAddress();
                payload.setCustomerCode(accountData.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountData.get("UCRACCT_PREM_CODE").toString());
                payload.setCity(CITY_INVALID_COMBINATION);
                payload.setZipCode(ZIPCODE_INVALID_COMBINATION);
                accountData = ApplicationContext.get().getDbAction().verifyTheZipCodeAncCity(ZIPCODE_INVALID_COMBINATION, CITY_INVALID_COMBINATION);
                break;

            case TC_110__Negative__Invalid_Delivery_Point_Format:
                payload.setDeliveryPoint(DELIVERY_POINT_INVALID);
                break;

            case TC_111__Negative__Invalid_Carrier_Route_Length:
                payload.setCarrierRoute(CARRIER_ROUTE_INVALID);
                break;

            case TC_112__Negative__Invalid_Attention_To_Length:
                payload.setAttentionTo(FakerDataGenerator.generateString(31));
                break;

            case TC_113__Negative__Invalid_Additional_Address_Line_Length:
                payload.setAdditionalAddressLine(FakerDataGenerator.generateString(31));
                break;

            case TC_114__Negative__Invalid_Rural_Route_Length:
                payload.setRuralRoute(FakerDataGenerator.generateString(31));
                payload.setStreetName("");
                payload.setCity(CITY_VALID);
                payload.setZipCode(ZIPCODE_VALID);
                accountData = ApplicationContext.get().getDbAction().getAccountWithoutAddress();
                customerCode=accountData.get("UCRACCT_CUST_CODE").toString();
                payload.setCustomerCode(accountData.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountData.get("UCRACCT_PREM_CODE").toString());
                testContext.setCustomerCode(accountData.get("UCRACCT_CUST_CODE").toString());
                break;

            case TC_115__Negative__Invalid_PO_Box_Length:
                payload.setPoBox(FakerDataGenerator.generateDigits(24));
                payload.setStreetName("");
                payload.setCity(CITY_VALID);
                payload.setZipCode(ZIPCODE_VALID);
                accountData = ApplicationContext.get().getDbAction().getAccountWithoutAddress();
                customerCode=accountData.get("UCRACCT_CUST_CODE").toString();
                payload.setCustomerCode(accountData.get("UCRACCT_CUST_CODE").toString());
                testContext.setCustomerCode(accountData.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountData.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_116__Negative__Invalid_PO_Box_Format:
                payload.setPoBox("PO BOX 400");
                payload.setStreetName("");
                payload.setCity(CITY_VALID);
                payload.setZipCode(ZIPCODE_VALID);
                accountData = ApplicationContext.get().getDbAction().getAccountWithoutAddress();
                customerCode=accountData.get("UCRACCT_CUST_CODE").toString();
                payload.setCustomerCode(accountData.get("UCRACCT_CUST_CODE").toString());
                testContext.setCustomerCode(accountData.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountData.get("UCRACCT_PREM_CODE").toString());
                break;


            case TC_117__Positive__Valid_Street_Address___Minimum_parameters_and_No_Existing_Address____:
                accountData = ApplicationContext.get().getDbAction().getAccountWithoutAddress();
                testContext.setCustomerCode(accountData.get("UCRACCT_CUST_CODE").toString());
                payload.setCustomerCode(accountData.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountData.get("UCRACCT_PREM_CODE").toString());
                payload.setStreetName(STREET_NAME_VALID);
                payload.setCity(CITY_VALID);
                payload.setZipCode(ZIPCODE_VALID);
                break;

            case TC_118__Positive__Valid_Street_Address___Maximum_parameters_and_No_Existing_Address____:
                accountData = ApplicationContext.get().getDbAction().getAccountWithoutAddress();
                testContext.setCustomerCode(accountData.get("UCRACCT_CUST_CODE").toString());
                payload.setCustomerCode(accountData.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountData.get("UCRACCT_PREM_CODE").toString());
                payload.setStreetName(STREET_NAME_VALID);
                payload.setCity(CITY_VALID);
                payload.setZipCode(ZIPCODE_VALID);
                payload.setStreetNumber(STREET_NUMBER_VALID);
                payload.setStreetPreDirection("N");
                payload.setStreetPostDirection("NW");
                payload.setUnitType("APT");
                payload.setUnitNumber("12B");
                payload.setDeliveryPoint("12");
                payload.setCarrierRoute("3400");
                payload.setAttentionTo("JOHN DOE");
                payload.setAdditionalAddressLine("BUILDING 5");
                break;

            case TC_119__Positive__Valid_Street_Address___Mixed_parameters_and_No_Existing_Address____:
                accountData = ApplicationContext.get().getDbAction().getAccountWithoutAddress();
                testContext.setCustomerCode(accountData.get("UCRACCT_CUST_CODE").toString());
                payload.setCustomerCode(accountData.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountData.get("UCRACCT_PREM_CODE").toString());
                payload.setStreetName(STREET_NAME_VALID);
                payload.setZipCode(ZIPCODE_VALID);
                payload.setCity(CITY_VALID);
                payload.setStreetNumber(STREET_NUMBER_VALID);
                break;

            case TC_120__Positive__Valid_Street_Address___Minimum_parameters_and_Existing_Address_and_Same_Day____:
                accountData = ApplicationContext.get().getDbAction().getAccountWithAddressSameDay();
                customerCode=accountData.get("UCRACCT_CUST_CODE").toString();
                testContext.setCustomerCode(customerCode);
                payload.setCustomerCode(customerCode);
                payload.setPremisesCode(accountData.get("UCRACCT_PREM_CODE").toString());
                payload.setStreetName(STREET_NAME_VALID);
                payload.setCity(CITY_VALID);
                payload.setZipCode(ZIPCODE_VALID);
                accountData = ApplicationContext.get().getDbAction().performDatabaseValidationsTC116(customerCode);
                break;

            case TC_121__Positive__Valid_Street_Address___Maximum_parameters_and_Existing_Address_and_Same_Day____:
                accountData = ApplicationContext.get().getDbAction().getAccountWithAddressSameDay();
                customerCode=accountData.get("UCRACCT_CUST_CODE").toString();
                testContext.setCustomerCode(customerCode);
                payload.setCustomerCode(customerCode);
                payload.setPremisesCode(accountData.get("UCRACCT_PREM_CODE").toString());
                payload.setStreetName(STREET_NAME_VALID);
                payload.setCity(CITY_VALID);
                payload.setZipCode(ZIPCODE_VALID);
                payload.setStreetNumber(STREET_NUMBER_VALID);
                payload.setStreetPreDirection("N");
                payload.setStreetPostDirection("NW");
                payload.setUnitType("APT");
                payload.setUnitNumber("12B");
                payload.setDeliveryPoint("12");
                payload.setCarrierRoute("3400");
                payload.setAttentionTo("JOHN DOE");
                payload.setAdditionalAddressLine("BUILDING 5");
                accountData = ApplicationContext.get().getDbAction().performDatabaseValidationsTC116(customerCode);
                break;

            case TC_122__Positive__Valid_Street_Address___Mixed_parameters_and_Existing_Address_and_Same_Day____:
                accountData = ApplicationContext.get().getDbAction().getAccountWithAddressSameDay();
                customerCode=accountData.get("UCRACCT_CUST_CODE").toString();
                testContext.setCustomerCode(customerCode);
                payload.setCustomerCode(customerCode);
                payload.setPremisesCode(accountData.get("UCRACCT_PREM_CODE").toString());
                payload.setStreetName(STREET_NAME_VALID);
                payload.setZipCode(ZIPCODE_VALID);
                payload.setCity(CITY_VALID);
                payload.setStreetNumber(STREET_NUMBER_VALID);
                accountData = ApplicationContext.get().getDbAction().performDatabaseValidationsTC116(customerCode);
                break;

            case TC_123__Positive__Valid_Street_Address___Minimum_parameters_and_Existing_Address_and_Different_Day____:
                accountData = ApplicationContext.get().getDbAction().getAccountWithAddressDifferentDay();
                testContext.setCustomerCode(accountData.get("UCRACCT_CUST_CODE").toString());
                payload.setCustomerCode(accountData.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountData.get("UCRACCT_PREM_CODE").toString());
                payload.setStreetName(STREET_NAME_VALID);
                payload.setCity(CITY_VALID);
                payload.setZipCode(ZIPCODE_VALID);
                accountData = ApplicationContext.get().getDbAction().performDatabaseValidationsTC119(testContext.getCustomerCode());
                break;

            case TC_124__Positive__Valid_Street_Address___Maximum_parameters_and_Existing_Address_and_Different_Day____:
                accountData = ApplicationContext.get().getDbAction().getAccountWithAddressDifferentDay();
                String customerCode1="";
                testContext.setCustomerCode(accountData.get("UCRACCT_CUST_CODE").toString());
                customerCode1=accountData.get("UCRACCT_CUST_CODE").toString();
                payload.setCustomerCode(accountData.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountData.get("UCRACCT_PREM_CODE").toString());
                payload.setStreetName(STREET_NAME_VALID);
                payload.setCity(CITY_VALID);
                payload.setZipCode(ZIPCODE_VALID);
                payload.setStreetNumber(STREET_NUMBER_VALID);
                payload.setStreetPreDirection("N");
                payload.setStreetPostDirection("NW");
                payload.setUnitType("APT");
                payload.setUnitNumber("12B");
                payload.setDeliveryPoint("12");
                payload.setCarrierRoute("3400");
                payload.setAttentionTo("JOHN DOE");
                payload.setAdditionalAddressLine("BUILDING 5");
                accountData = ApplicationContext.get().getDbAction().performDatabaseValidationsTC119(customerCode1);
                break;

            case TC_125__Positive__Valid_Street_Address___Mixed_parameters_and_Existing_Address_and_Different_Day____:
                accountData = ApplicationContext.get().getDbAction().getAccountWithAddressDifferentDay();
                testContext.setCustomerCode(accountData.get("UCRACCT_CUST_CODE").toString());
                customerCode=accountData.get("UCRACCT_CUST_CODE").toString();
                payload.setCustomerCode(accountData.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountData.get("UCRACCT_PREM_CODE").toString());
                payload.setStreetName(STREET_NAME_VALID);
                payload.setZipCode(ZIPCODE_VALID);
                payload.setCity(CITY_VALID);
                payload.setStreetNumber(STREET_NUMBER_VALID);
                accountData = ApplicationContext.get().getDbAction().performDatabaseValidationsTC119(customerCode);
                break;

            case TC_126__Positive__Valid_PO_Box_Address:
                payload.setPoBox(POBOX_VALID);
                payload.setStreetName("");
                payload.setCity(CITY_VALID);
                payload.setZipCode(ZIPCODE_VALID);
                accountData = ApplicationContext.get().getDbAction().getAccountWithoutAddress();
                customerCode=accountData.get("UCRACCT_CUST_CODE").toString();
                payload.setCustomerCode(accountData.get("UCRACCT_CUST_CODE").toString());
                testContext.setCustomerCode(accountData.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountData.get("UCRACCT_PREM_CODE").toString());
                accountData = ApplicationContext.get().getDbAction().performDatabaseValidationsTC119(customerCode);
                break;

            case TC_127__Positive__Valid_Rural_Route_Address:
                payload.setRuralRoute(RURAL_ROUTE_VALID);
                payload.setStreetName("");
                payload.setCity(CITY_VALID);
                payload.setZipCode(ZIPCODE_VALID);
                accountData = ApplicationContext.get().getDbAction().getAccountWithoutAddress();
                customerCode=accountData.get("UCRACCT_CUST_CODE").toString();
                payload.setCustomerCode(accountData.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountData.get("UCRACCT_PREM_CODE").toString());
                testContext.setCustomerCode(accountData.get("UCRACCT_CUST_CODE").toString());
                accountData = ApplicationContext.get().getDbAction().performDatabaseValidationsTC119(customerCode);
                break;

            case TC_128__Positive__LoginID_Saved:
                payload.setStreetName(STREET_NAME_VALID);
                payload.setStreetNumber(STREET_NUMBER_VALID);
                payload.setCity(CITY_VALID);
                payload.setZipCode(ZIPCODE_VALID);
                String loginID=FakerDataGenerator.generateString(6);
                payload.setLoginID(loginID);
                testContext.setLoginId(loginID);
                accountData = ApplicationContext.get().getDbAction().getAccountWithoutAddress();
                payload.setCustomerCode(accountData.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountData.get("UCRACCT_PREM_CODE").toString());
                break;

            default:
                log.warn("Unhandled test condition: {}", testCondition);
                break;
        }
    }
}
