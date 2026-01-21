package com.gng.api.pages.csi.GetAccountInfoPage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.GetAccountInfo.GetAccountInfoRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.GetAccountInfo.GetAccountInfoLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;

import java.util.Map;

@Slf4j
public class GetAccountInfoApiHelper {

    private final TestContext testContext;

    public GetAccountInfoApiHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    GetAccountInfoRequest preparePayload(GetAccountInfoLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        String jsonFileName = apiLabel.equals(GetAccountInfoLabel.csi_get_account_info)
                ? GetAccountInfoLabel.csi_get_account_info.toString()
                : GetAccountInfoLabel.csi_get_account_info_mandatory.toString();
        return BasePage.deserializeJsonToPojo(jsonFileName, GetAccountInfoRequest.class);
    }

    public void preparePayloadForTestCondition(GetAccountInfoRequest payload, GetAccountInfoLabel testCondition) {
        Map<String, Object> accountInfo = null;
        switch (testCondition) {
            case TC_140__Negative__Missing_Request_ID:
                payload.setRequestID("");
                break;

            case TC_141__Negative__Invalid_Request_ID__Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(33));
                break;

            case TC_142__Negative__Duplicate_Request_ID:
                payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());
                break;

            case TC_143__Negative__Invalid_customerCode_Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateDigits(10)); // >9 digits
                break;

            case TC_144__Negative__Invalid_customerCode_Format:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateAlphanumeric(6)); // not purely numeric
                break;

            case TC_145__Negative__Invalid_premisesCode_Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(8)); // >7 digits
                break;

            case TC_146__Negative__Invalid_premisesCode_Format:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPremisesCode(FakerDataGenerator.generateAlphanumeric(6)); // not purely numeric
                break;

            case TC_147__Negative__Invalid_Account_Number:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode("9999999");
                break;

            case TC_148__Positive__Account_Info_Returned___Email_Address____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetails();
                payload.setCustomerCode(accountInfo.get("GZBEMCP_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_149__Positive__Account_Info_Returned___No_Email_Address____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC150();
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_150__Positive__Account_Info_Returned___Partner_Promotions_Indicator__equals_Y____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC151();
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_151__Positive__Account_Info_Returned___Partner_Promotions_Indicator__equals_N____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC152();
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_152__Positive__Account_Info_Returned___Partner_Promotions_Indicator_is_null____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC153();
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_153__Positive__Account_Info_Returned___Marketing_Offers_Indicator__equals_Y____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC154();
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_154__Positive__Account_Info_Returned___Marketing_Offers_Indicator__equals_N____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC155();
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_155__Positive__Account_Info_Returned___Marketing_Offers_Indicator_is_null____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC156();
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_156__Positive__Account_Info_Returned___Account__and_Billing_Reminder__equals_Y____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC157();
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_157__Positive__Account_Info_Returned___Account__and_Billing_Reminder__equals_N____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC158();
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_158__Positive__Account_Info_Returned___Account__and_Billing_Reminder_is_null____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC159();
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_159__Positive__Account_Info_Returned___Primary_Phone_is_Home____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC160();
                payload.setCustomerCode(accountInfo.get("ucrtele_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_160__Positive__Account_Info_Returned___Primary_Phone_is_Work____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC161();
                payload.setCustomerCode(accountInfo.get("ucrtele_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_161__Positive__Account_Info_Returned___Primary_Phone_is_both_Home_and_Work____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC162();
                payload.setCustomerCode(accountInfo.get("ucrtele_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_162__Positive__Account_Info_Returned___No_Phone_Number____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC163();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_163__Positive__Account_Info_Returned___Greener_Life_Rate____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC164();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_164__Positive__Account_Info_Returned___No_Greener_Life_Rate____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC165();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_165__Positive__Account_Info_Returned___Bill_Delivery_Option_is_Paper____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC166();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_166__Positive__Account_Info_Returned___Bill_Delivery_Option_is_Electronic____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC167();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_167__Positive__Account_Info_Returned___Bill_Delivery_Option_is_Fiserv_E__Bill____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC168();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_168__Positive__Account_Info_Returned___Bill_Delivery_Option_is_null____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC169();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_169__Positive__Account_Info_Returned___Correspondence_Delivery_Option_is_Paper____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC170();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_170__Positive__Account_Info_Returned___Correspondence_Delivery_Option_is_Electronic____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC171();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_171__Positive__Account_Info_Returned___Correspondence_Delivery_Option_is_null____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC172();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_172__Positive__Account_Info_Returned___Single_Price_Plan____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC173();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_173__Positive__Account_Info_Returned___Multiple_Price_Plans____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC174();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_174__Positive__Account_Info_Returned___No_Guaranteed_Bill_Plan____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC175();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_175__Positive__Account_Info_Returned___Guaranteed_Bill_Plan____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC176();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_176__Positive__Account_Info_Returned___No_Price_Protection_Guarantee_Plan____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC177();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_177__Positive__Account_Info_Returned___Price_Protection_Guarantee_Plan____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC178();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_178__Positive__Account_Info_Returned___No_Rollover___ACR__Plans____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC179();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_179__Positive__Account_Info_Returned___Rollover___ACR__Plans____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC180();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_180__Positive__Account_Info_Returned___Non__Restricted_Plans____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC181();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_181__Positive__Account_Info_Returned___Restricted_Plans____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC182();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_182__Positive__Account_Info_Returned___Single_Discount____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC183();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_183__Positive__Account_Info_Returned___Multiple_Discounts____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC184();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_184__Positive__Account_Info_Returned___No_Discounts____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC185();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_185__Positive__Account_Info_Returned___Transferable_Discount____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC186();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_186__Positive__Account_Info_Returned___Non__Transferable_Discount____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC187();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;


            default:
                // Handle unknown test condition
                break;
        }
    }
}
