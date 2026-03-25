package com.gng.api.pages.csi.GetPaymentArrangementInfoPage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pojo.CSIPojo.GetPaymentArrangementInfo.GetPaymentArrangementInfoRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.steps.csi.GetPaymentArrangementInfo.GetPaymentArrangementInfoLabel;
import com.gng.api.util.FakerDataGenerator;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;

import java.util.Map;

@Slf4j
public class GetPaymentArrangementInfoApiHelper {

    private final TestContext testContext;

    public GetPaymentArrangementInfoApiHelper(TestContext testContext) {
        this.testContext = testContext;
    }

    GetPaymentArrangementInfoRequest preparePayload(GetPaymentArrangementInfoLabel apiLabel) {
        log.info("Preparing payload for {}", apiLabel);
        String jsonFileName = apiLabel.equals(GetPaymentArrangementInfoLabel.get_payment_arrangement_info)
                ? GetPaymentArrangementInfoLabel.get_payment_arrangement_info.toString()
                : GetPaymentArrangementInfoLabel.get_payment_arrangement_info_mandatory.toString();

        return BasePage.deserializeJsonToPojo(jsonFileName, GetPaymentArrangementInfoRequest.class);
    }

    public void preparePayloadForTestCondition(GetPaymentArrangementInfoRequest payload,
                                               GetPaymentArrangementInfoLabel testCondition) {

        Map<String, Object> accountData = null;

        switch (testCondition) {

            // ---------------- NEGATIVE TEST CASES ----------------

            case TC_156__Negative__Missing_Request_ID:
                payload.setRequestID("");
                break;

            case TC_157__Negative__Invalid_Request_ID_Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(33));
                break;

            case TC_158__Negative__Duplicate_Request_ID:
                payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());
                break;

            case TC_159__Negative__Missing_customerCode:
                payload.setCustomerCode("");
                break;

            case TC_160__Negative__Invalid_customerCode_Length:
                payload.setCustomerCode(FakerDataGenerator.generateDigits(10));
                break;

            case TC_161__Negative__Invalid_customerCode_Format:
                payload.setCustomerCode(FakerDataGenerator.generateAlphanumeric(6));
                break;

            case TC_162__Negative__Missing_premisesCode:
                payload.setPremisesCode("");
                break;

            case TC_163__Negative__Invalid_premisesCode_Length:
                payload.setPremisesCode(FakerDataGenerator.generateDigits(8));
                break;

            case TC_164__Negative__Invalid_premisesCode_Format:
                payload.setPremisesCode(FakerDataGenerator.generateAlphanumeric(5));
                break;

            case TC_165__Negative__Invalid_Account_Number:
                payload.setCustomerCode("999999999");
                payload.setPremisesCode("9999999");
                break;


            // ---------------- POSITIVE TEST CASES ----------------

            case TC_166__Positive__No_Payment_Arrangement:
            case TC_167__Positive__Has_Inactive_Payment_Arrangement:
            case TC_168__Positive__Has_Active_Payment_Arrangement:
            case TC_169__Positive__Payment_Arrangement_Number_Format:
            case TC_170__Positive__Payment_Arrangement_Type_Code_Format:
            case TC_171__Positive__Payment_Arrangement_Type_Code_Format:
            case TC_172__Positive__Payment_Arrangement_Total_Amount_Format:
            case TC_173__Positive__Payment_Arrangement_Date_Created_Format:
            case TC_174__Positive__Payment_Arrangement_Number_Of_Installments_Format:
            case TC_175__Positive__Payment_Arrangement_Amount_Due_Format:
            case TC_176__Positive__Payment_Arrangement_Balance_Format:
            case TC_177__Positive__Payment_Arrangement_Date_Due_Format:
            case TC_178__Positive__Payment_Arrangement_Date_Paid_Format:
            case TC_179__Positive__Payment_Arrangement_Number_Of_Installments_GT_1:

                // Fetch valid Banner account for all positive tests
               // accountData = ApplicationContext.get().getDbAction("banner").getValidPaymentArrangementAccount();

                Assert.assertNotNull(accountData, "Expected valid Banner account data");

                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(10));
                payload.setCustomerCode(accountData.get("customer_code").toString());
                payload.setPremisesCode(accountData.get("premises_code").toString());
                break;


            default:
                log.warn("Unhandled test condition: {}", testCondition);
                break;
        }
    }
}
