package com.gng.api.pages.csi.UpdateMailingAddressPage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.UpdateMailingAddress.UpdateMailingAddressRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.UpdateMailingAddress.UpdateMailingAddressLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;

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
    private static final String STREET_NUMBER_VALID = "123";
    private static final String CITY_VALID = "Lawrenceville";
    private static final String ZIPCODE_VALID = "30043";
    private static final String POBOX_VALID = "PO Box 456";
    private static final String RURAL_ROUTE_VALID = "RR 7";
    private static final String STREET_NAME_TOO_MANY = "Main St";
    private static final String POBOX_TOO_MANY = "123";
    private static final String RURAL_ROUTE_TOO_MANY = "RR 5";
    private static final String ZIPCODE_INVALID = "99999";

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

    public void preparePayloadForNegativeTestCondition(UpdateMailingAddressRequest payload, UpdateMailingAddressLabel testCondition) {
        Map<String, Object> dbValues = null;
        payload.setRequestID(FakerDataGenerator.generateAlphanumeric(7));
        switch (testCondition) {
            case TC_68__Negative__Missing_Request_ID:
                payload.setRequestID("");
                break;

            case TC_69__Negative__Invalid_Request_ID__Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(33));
                break;

            case TC_70__Negative__Duplicate_Request_ID:
                payload.setRequestID(DUPLICATE_REQUEST_ID);
                break;

            case TC_71__Negative__Missing_customerCode:
                payload.setCustomerCode("");
                break;

            case TC_72__Negative__Invalid_customerCode__Length:
                payload.setCustomerCode(FakerDataGenerator.generateDigits(10));
                break;

            case TC_73__Negative__Invalid_customerCode__Format:
                payload.setCustomerCode(FakerDataGenerator.generateAlphanumericWithSpecialChars(6));
                break;

            case TC_74__Negative__Invalid_customerCode:
                payload.setCustomerCode(NON_EXISTENT_CUSTOMER_CODE_PREFIX + FakerDataGenerator.generateDigits(8));
                break;

            case TC_75__Negative__Missing_premisesCode:
                payload.setPremisesCode("");
                break;

            case TC_76__Negative__Invalid_premisesCode__Length:
                payload.setPremisesCode(FakerDataGenerator.generateDigits(8));
                break;

            case TC_77__Negative__Invalid_premisesCode__Format:
                payload.setPremisesCode(FakerDataGenerator.generateAlphanumericWithSpecialChars(5));
                break;

            case TC_78__Negative__Invalid_premisesCode:
                payload.setPremisesCode(NON_EXISTENT_PREMISES_CODE_PREFIX + FakerDataGenerator.generateDigits(5));
                break;

            case TC_79__Negative__Invalid_Address_Fields___Missing____:
                payload.setStreetName("");
                payload.setPoBox("");
                payload.setRuralRoute("");
                break;

            case TC_80__Negative__Invalid_Address_Fields___Too_Many____:
                payload.setStreetName(STREET_NAME_TOO_MANY);
                payload.setPoBox(POBOX_TOO_MANY);
                payload.setRuralRoute(RURAL_ROUTE_TOO_MANY);
                break;

            case TC_81__Negative__Invalid_Street_Number__Length:
                payload.setStreetNumber(FakerDataGenerator.generateAlphanumeric(13));
                break;

            case TC_82__Negative__Invalid_Street_Pre__Direction__Length:
                payload.setStreetPreDirection(STREET_PRE_DIRECTION_TOO_LONG);
                break;

            case TC_83__Negative__Invalid_Street_Pre__Direction:
                payload.setStreetPreDirection(STREET_PRE_DIRECTION_INVALID);
                break;

            case TC_84__Negative__Invalid_Street_Name__Length:
                payload.setStreetName(FakerDataGenerator.generateString(31));
                break;

            case TC_85__Negative__Missing_StreetName:
                payload.setStreetName("");
                break;

            case TC_86__Negative__Invalid_StreetSuffix__Length:
                payload.setStreetSuffix(FakerDataGenerator.generateString(7));
                break;

            case TC_87__Negative__Invalid_StreetSuffix:
                payload.setStreetSuffix(STREET_SUFFIX_INVALID);
                break;

            case TC_88__Negative__Invalid_Street_Post__Direction__Length:
                payload.setStreetPostDirection(STREET_POST_DIRECTION_TOO_LONG);
                break;

            case TC_89__Negative__Invalid_Street_Post__Direction:
                payload.setStreetPostDirection(STREET_POST_DIRECTION_INVALID);
                break;

            case TC_90__Negative__Invalid_Unit_Type__Format:
                payload.setUnitType(FakerDataGenerator.generateString(7));
                break;

            case TC_91__Negative__Missing_Unit_Type:
                payload.setUnitType(FakerDataGenerator.generateString(5));
                break;

            case TC_92__Negative__Invalid_Unit_Number__Format:
                payload.setUnitNumber(FakerDataGenerator.generateString(7));
                break;

            case TC_93__Negative__Invalid_City__Length:
                payload.setCity(FakerDataGenerator.generateString(21));
                break;

            case TC_94__Negative__Missing_City:
                payload.setCity("");
                break;

            case TC_95__Negative__Invalid_Zip_Code__Format___Length__10____:
                payload.setZipCode(FakerDataGenerator.generateDigits(11));
                break;

            case TC_96__Negative__Missing_Zip_Code:
                payload.setZipCode("");
                break;

            case TC_97__Negative__Invalid_Zip_Code:
                payload.setZipCode(ZIPCODE_INVALID);
                break;

            case TC_98__Negative__Invalid_City_and_Zip_Code__Combination:
                payload.setCity(CITY_INVALID_COMBINATION);
                payload.setZipCode(ZIPCODE_INVALID_COMBINATION);
                break;

            case TC_99__Negative__Invalid_County_Code:
                payload.setCountyCode(COUNTY_CODE_INVALID);
                break;

            case TC_100__Negative__Invalid_Delivery_Point__Format:
                payload.setDeliveryPoint(DELIVERY_POINT_INVALID);
                break;

            case TC_101__Negative__Invalid_Carrier_Route__Length:
                payload.setCarrierRoute(CARRIER_ROUTE_INVALID);
                break;

            case TC_102__Negative__Invalid_Attention_To__Length:
                payload.setAttentionTo(FakerDataGenerator.generateString(31));
                break;

            case TC_103__Negative__Invalid_Additional_Address_Line__Length:
                payload.setAdditionalAddressLine(FakerDataGenerator.generateString(31));
                break;

            case TC_104__Positive__Valid_Street_Address:
                payload.setStreetName(STREET_NAME_VALID);
                payload.setStreetNumber(STREET_NUMBER_VALID);
                payload.setCity(CITY_VALID);
                payload.setZipCode(ZIPCODE_VALID);
                break;

            case TC_105__Positive__Valid_PO_Box_Address:
                payload.setPoBox(POBOX_VALID);
                payload.setStreetName("");
                payload.setCity(CITY_VALID);
                payload.setZipCode(ZIPCODE_VALID);
                break;

            case TC_106__Positive__Valid_Rural_Route_Address:
                payload.setRuralRoute(RURAL_ROUTE_VALID);
                payload.setStreetName("");
                payload.setCity(CITY_VALID);
                payload.setZipCode(ZIPCODE_VALID);
                break;

            default:
                log.warn("Unhandled test condition: {}", testCondition);
                break;
        }
    }
}
