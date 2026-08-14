package com.gng.api.pages.csi.GetAccountInfoPage;

import com.gng.api.constants.GlobalEnums;
import com.gng.api.context.ApplicationContext;
import com.gng.api.pages.BasePage;
import com.gng.api.pages.csi.GetPaperlessEnrollmentEligibilityPage.GetPaperlessEnrollmentEligibilitySetupHelper;
import com.gng.api.pojo.CSIPojo.GetAccountInfo.GetAccountInfoRequest;
import com.gng.api.pojo.TestContext.TestContext;
import com.gng.api.report.DualReportManager;
import com.gng.api.steps.csi.GetAccountInfo.GetAccountInfoLabel;
import com.gng.api.util.FakerDataGenerator;
import com.gng.api.util.VerifyAccountEvidenceUtil;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.gng.api.constants.ApiEndPoint.CSI_GET_ACCOUNT_INFO;

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
        Map<String, Object> accountInfo;
        switch (testCondition) {
            case TC_142__Negative__Missing_Request_ID:
                payload.setRequestID("");
                break;

            case TC_143__Negative__Invalid_Request_ID__Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(33));
                break;

            case TC_144__Negative__Duplicate_Request_ID:
                payload.setRequestID(GlobalEnums.InvalidValues.DUPLICATE_REQUEST_ID.getValue());
                break;

            case TC_145__Negative__Invalid_customerCode_Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateDigits(10)); // >9 digits
                break;

            case TC_146__Negative__Invalid_customerCode_Format:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode(FakerDataGenerator.generateAlphanumeric(6)); // not purely numeric
                break;

            case TC_147__Negative__Invalid_premisesCode_Length:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPremisesCode(FakerDataGenerator.generateDigits(8)); // >7 digits
                break;

            case TC_148__Negative__Invalid_premisesCode_Format:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setPremisesCode(FakerDataGenerator.generateAlphanumeric(6)); // not purely numeric
                break;

            case TC_149__Negative__Invalid_Account_Number:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                payload.setCustomerCode("9988776");
                break;

            case TC_150__Positive__Account_Info_Returned___Email_Address____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetails();
                payload.setCustomerCode(accountInfo.get("GZBEMCP_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_151__Positive__Account_Info_Returned___No_Email_Address____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC150();
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_152__Positive__Account_Info_Returned___Partner_Promotions_Indicator__equals_Y____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC151();
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_153__Positive__Account_Info_Returned___Partner_Promotions_Indicator__equals_N____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC152();
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_154__Positive__Account_Info_Returned___Partner_Promotions_Indicator_is_null____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC153();
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_155__Positive__Account_Info_Returned___Marketing_Offers_Indicator__equals_Y____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC154();
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_156__Positive__Account_Info_Returned___Marketing_Offers_Indicator__equals_N____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC155();
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_157__Positive__Account_Info_Returned___Marketing_Offers_Indicator_is_null____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC156();
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_158__Positive__Account_Info_Returned___Account__and_Billing_Reminder__equals_Y____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC157();
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_159__Positive__Account_Info_Returned___Account__and_Billing_Reminder__equals_N____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC158();
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_160__Positive__Account_Info_Returned___Account__and_Billing_Reminder_is_null____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC159();
                payload.setCustomerCode(accountInfo.get("UCRACCT_CUST_CODE").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_161__Positive__Account_Info_Returned___Primary_Phone_is_Home____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC160();
                payload.setCustomerCode(accountInfo.get("ucrtele_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_162__Positive__Account_Info_Returned___Primary_Phone_is_Work____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC161();
                payload.setCustomerCode(accountInfo.get("ucrtele_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_163__Positive__Account_Info_Returned___Primary_Phone_is_both_Home_and_Work____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC162();
                payload.setCustomerCode(accountInfo.get("ucrtele_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_164__Positive__Account_Info_Returned___No_Phone_Number____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC163();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_165__Positive__Account_Info_Returned___Greener_Life_Rate____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC164();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_166__Positive__Account_Info_Returned___No_Greener_Life_Rate____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC165();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_167__Positive__Account_Info_Returned___Bill_Delivery_Option_is_Paper____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC166();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_168__Positive__Account_Info_Returned___Bill_Delivery_Option_is_Electronic____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC167();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_169__Positive__Account_Info_Returned___Bill_Delivery_Option_is_Fiserv_E__Bill____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC168();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_170__Positive__Account_Info_Returned___Bill_Delivery_Option_is_null____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC169();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_13__Positive__Account_Info_Returned___Bill_Delivery_Option_is_Initiated____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = resolveInitiatedBillDeliveryAccount();
                payload.setCustomerCode(accountInfo.get("customerCode").toString());
                payload.setPremisesCode(accountInfo.get("premisesCode").toString());
                break;

            case TC_171__Positive__Account_Info_Returned___Correspondence_Delivery_Option_is_Paper____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC170();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_172__Positive__Account_Info_Returned___Correspondence_Delivery_Option_is_Electronic____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC171();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_173__Positive__Account_Info_Returned___Correspondence_Delivery_Option_is_null____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC172();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_14__Positive__Account_Info_Returned___Correspondence_Delivery_Option_is_Initiated____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = resolveInitiatedCorrespondenceDeliveryAccount();
                payload.setCustomerCode(accountInfo.get("customerCode").toString());
                payload.setPremisesCode(accountInfo.get("premisesCode").toString());
                break;

            case TC_174__Positive__Account_Info_Returned___Single_Price_Plan____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC173();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_175__Positive__Account_Info_Returned___Multiple_Price_Plans____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC174();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_176__Positive__Account_Info_Returned___No_Guaranteed_Bill_Plan____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC175();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_177__Positive__Account_Info_Returned___Guaranteed_Bill_Plan____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC176();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_178__Positive__Account_Info_Returned___No_Price_Protection_Guarantee_Plan____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC177();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_179__Positive__Account_Info_Returned___Price_Protection_Guarantee_Plan____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC178();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_180__Positive__Account_Info_Returned___No_Rollover___ACR______Plans____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC179();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_181__Positive__Account_Info_Returned___Rollover___ACR______Plans____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC180();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_182__Positive__Account_Info_Returned___Non__Restricted_Plans____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC181();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_183__Positive__Account_Info_Returned___Single_Discount____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC183();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_184__Positive__Account_Info_Returned___Multiple_Discounts____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC184();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_185__Positive__Account_Info_Returned___No_Discounts____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC185();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_186__Positive__Account_Info_Returned___Transferable_Discount____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC184();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_187__Positive__Account_Info_Returned___Non__Transferable_Discount____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = ApplicationContext.get().getDbAction().getAccountDetailsTC187();
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("UCRACCT_PREM_CODE").toString());
                break;

            case TC_1__Positive__Account_Info_Returned___Renewal_Indicator_Format____:
            case TC_2__Positive__Account_Info_Returned___Renewal_Indicator_equals_Y____:
            case TC_3__Positive__Account_Info_Returned___Renewal_Indicator_equals_N____:
            case TC_4__Positive__Account_Info_Returned___Renewal_Indicator_equals_Dash____:
            case TC_5__Positive__Account_Info_Returned___Guaranteed_Plan_Pricing_Model____:
            case TC_6__Positive__Account_Info_Returned___Non__Guaranteed_Plan_Pricing_Model____:
            case TC_7__Positive__Account_Info_Returned___Plan_Description_Format____:
            case TC_8__Positive__Account_Info_Returned___Plan_Description_Value____:
                payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
                accountInfo = getAccountDetailsForPlanTestCondition(testCondition);
                payload.setCustomerCode(accountInfo.get("ucracct_cust_code").toString());
                payload.setPremisesCode(accountInfo.get("ucracct_prem_code").toString());
                break;

            default:
                // Handle unknown test condition
                break;
        }
    }

    private Map<String, Object> getAccountDetailsForPlanTestCondition(GetAccountInfoLabel testCondition) {
        return switch (testCondition) {
            case TC_1__Positive__Account_Info_Returned___Renewal_Indicator_Format____ ->
                    ApplicationContext.get().getDbAction().getAccountDetailsTC1();
            case TC_2__Positive__Account_Info_Returned___Renewal_Indicator_equals_Y____ ->
                    resolveRenewalIndicatorAccount(
                            "TC_2",
                            "Y",
                            ApplicationContext.get().getDbAction().listAccountDetailsTC2Candidates());
            case TC_3__Positive__Account_Info_Returned___Renewal_Indicator_equals_N____ ->
                    resolveRenewalIndicatorAccount(
                            "TC_3",
                            "N",
                            ApplicationContext.get().getDbAction().listAccountDetailsTC3Candidates());
            case TC_4__Positive__Account_Info_Returned___Renewal_Indicator_equals_Dash____ ->
                    resolveRenewalIndicatorAccount(
                            "TC_4",
                            "-",
                            ApplicationContext.get().getDbAction().listAccountDetailsTC4Candidates());
            case TC_5__Positive__Account_Info_Returned___Guaranteed_Plan_Pricing_Model____ ->
                    resolveGuaranteedPlanAccount();
            case TC_6__Positive__Account_Info_Returned___Non__Guaranteed_Plan_Pricing_Model____ ->
                    ApplicationContext.get().getDbAction().getAccountDetailsTC6();
            case TC_7__Positive__Account_Info_Returned___Plan_Description_Format____ ->
                    ApplicationContext.get().getDbAction().getAccountDetailsTC7();
            case TC_8__Positive__Account_Info_Returned___Plan_Description_Value____ ->
                    ApplicationContext.get().getDbAction().getAccountDetailsTC8();
            default -> throw new IllegalArgumentException("Unsupported plan test condition: " + testCondition);
        };
    }

    /**
     * Probes DB candidates until GetAccountInfo returns the expected planRenewalWindowIndicator.
     * Avoids flakes from single-row DB picks whose live indicator has drifted.
     * Extent report logs only the selected account (probe attempts stay in debug logs).
     */
    private Map<String, Object> resolveRenewalIndicatorAccount(
            String tcId, String expectedIndicator, List<Map<String, Object>> candidates) {
        if (candidates == null || candidates.isEmpty()) {
            throw new IllegalStateException(
                    tcId + ": no DB candidates to probe for planRenewalWindowIndicator="
                            + expectedIndicator);
        }

        for (Map<String, Object> candidate : candidates) {
            String customerCode = stringVal(candidate, "ucracct_cust_code");
            String premisesCode = stringVal(candidate, "ucracct_prem_code");
            if (customerCode == null || premisesCode == null) {
                continue;
            }
            String indicator = probePlanRenewalWindowIndicator(customerCode, premisesCode);
            log.debug("{} probe {}/{} planRenewalWindowIndicator={}",
                    tcId, customerCode, premisesCode, indicator);
            if (expectedIndicator.equalsIgnoreCase(indicator)) {
                DualReportManager.logInfo(tcId + ": selected account with renewal indicator "
                        + expectedIndicator + " " + customerCode + "/" + premisesCode);
                Map<String, Object> selected = new LinkedHashMap<>(candidate);
                selected.put("ucracct_cust_code", customerCode);
                selected.put("ucracct_prem_code", premisesCode);
                return selected;
            }
        }

        throw new IllegalStateException(
                tcId + ": no candidate returned planRenewalWindowIndicator=" + expectedIndicator
                        + " after probing " + candidates.size()
                        + " accounts. UAT may lack matching plan data.");
    }

    /**
     * Probes Guaranteed Bill candidates until API returns Guaranteed pricing and, when discounts
     * are present, discountTransferabilityIndicator = N for every discount (FTD).
     * Falls back to Guaranteed without discount when no non-transferable discount account exists.
     * Extent report logs only the selected account.
     */
    private Map<String, Object> resolveGuaranteedPlanAccount() {
        Map<String, Object> withNonTransferable = probeGuaranteedAccount(
                "TC_5 PRICEPRO+discount",
                ApplicationContext.get().getDbAction().listAccountDetailsTC5PriceProCandidates(),
                true);
        if (withNonTransferable != null) {
            return withNonTransferable;
        }

        log.debug("TC_5: no Guaranteed+PRICEPRO candidate with non-transferable discounts; "
                + "trying Guaranteed + any discount");
        Map<String, Object> withAnyDiscount = probeGuaranteedAccount(
                "TC_5 any-discount",
                ApplicationContext.get().getDbAction().listAccountDetailsTC5AnyDiscountCandidates(),
                true);
        if (withAnyDiscount != null) {
            return withAnyDiscount;
        }

        log.debug("TC_5: no Guaranteed+discount account returned transferability N; "
                + "falling back to Guaranteed without discount");
        Map<String, Object> withoutDiscount =
                ApplicationContext.get().getDbAction().getAccountDetailsTC5WithoutDiscount();
        String customerCode = stringVal(withoutDiscount, "ucracct_cust_code");
        String premisesCode = stringVal(withoutDiscount, "ucracct_prem_code");
        Response response = callGetAccountInfo(customerCode, premisesCode);
        if (response == null || !hasGuaranteedPricing(response)) {
            throw new IllegalStateException(
                    "TC_5: Guaranteed-without-discount fallback did not return Guaranteed pricing for "
                            + customerCode + "/" + premisesCode);
        }
        DualReportManager.logInfo("TC_5: selected Guaranteed account WITHOUT discount "
                + customerCode + "/" + premisesCode);
        return withoutDiscount;
    }

    private Map<String, Object> probeGuaranteedAccount(
            String label, List<Map<String, Object>> candidates, boolean requireNonTransferableWhenDiscounted) {
        if (candidates == null || candidates.isEmpty()) {
            log.debug("{}: no DB candidates", label);
            return null;
        }
        for (Map<String, Object> candidate : candidates) {
            String customerCode = stringVal(candidate, "ucracct_cust_code");
            String premisesCode = stringVal(candidate, "ucracct_prem_code");
            if (customerCode == null || premisesCode == null) {
                continue;
            }
            Response response = callGetAccountInfo(customerCode, premisesCode);
            if (response == null || response.jsonPath().getInt("errorCode") != 0) {
                log.debug("{} probe {}/{} skipped (API error)", label, customerCode, premisesCode);
                continue;
            }
            boolean guaranteed = hasGuaranteedPricing(response);
            boolean discountsOk = discountsMatchNonTransferableRule(
                    response, requireNonTransferableWhenDiscounted);
            log.debug("{} probe {}/{} guaranteedPricing={} discountsOk={} discounts={}",
                    label, customerCode, premisesCode, guaranteed, discountsOk,
                    response.jsonPath().getList("data.discounts"));
            if (guaranteed && discountsOk) {
                List<Map<String, Object>> discounts = response.jsonPath().getList("data.discounts");
                DualReportManager.logInfo("TC_5: selected Guaranteed account " + customerCode + "/"
                        + premisesCode
                        + (discounts == null || discounts.isEmpty()
                        ? " (no discounts)"
                        : " with non-transferable discount(s)"));
                Map<String, Object> selected = new LinkedHashMap<>(candidate);
                selected.put("ucracct_cust_code", customerCode);
                selected.put("ucracct_prem_code", premisesCode);
                return selected;
            }
        }
        return null;
    }

    private Response callGetAccountInfo(String customerCode, String premisesCode) {
        GetAccountInfoRequest payload = preparePayload(GetAccountInfoLabel.csi_get_account_info);
        payload.setRequestID(FakerDataGenerator.generateAlphanumeric(6));
        payload.setCustomerCode(customerCode);
        payload.setPremisesCode(premisesCode);

        String uri = ApplicationContext.get().getEnvConfig().getBaseUri() + CSI_GET_ACCOUNT_INFO;
        return io.restassured.RestAssured.given()
                .relaxedHTTPSValidation()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + testContext.getAuthToken())
                .body(payload)
                .post(uri);
    }

    private boolean hasGuaranteedPricing(Response response) {
        List<Map<String, Object>> plans = response.jsonPath().getList("data.pricePlans");
        if (plans == null) {
            return false;
        }
        for (Map<String, Object> plan : plans) {
            Object gbpAmount = plan.get("planGBPAmount");
            Object thermPrice = plan.get("planThermPrice");
            Object serviceCharge = plan.get("planServiceCharge");
            if (gbpAmount != null && !String.valueOf(gbpAmount).trim().isEmpty()
                    && !"null".equalsIgnoreCase(String.valueOf(gbpAmount).trim())
                    && isEmptyOrZero(thermPrice) && isEmptyOrZero(serviceCharge)) {
                return true;
            }
        }
        return false;
    }

    private boolean discountsMatchNonTransferableRule(
            Response response, boolean requireNonTransferableWhenDiscounted) {
        List<Map<String, Object>> discounts = response.jsonPath().getList("data.discounts");
        if (discounts == null || discounts.isEmpty()) {
            return true;
        }
        if (!requireNonTransferableWhenDiscounted) {
            return true;
        }
        for (Map<String, Object> discount : discounts) {
            Object indicator = discount.get("discountTransferabilityIndicator");
            String value = indicator == null ? "" : indicator.toString().trim();
            if (!"N".equalsIgnoreCase(value)) {
                return false;
            }
        }
        return true;
    }

    private boolean isEmptyOrZero(Object value) {
        if (value == null) {
            return true;
        }
        String str = String.valueOf(value).trim();
        if (str.isEmpty() || "null".equalsIgnoreCase(str)) {
            return true;
        }
        try {
            return Double.parseDouble(str) == 0.0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String probePlanRenewalWindowIndicator(String customerCode, String premisesCode) {
        Response response = callGetAccountInfo(customerCode, premisesCode);
        if (response == null || response.jsonPath().getInt("errorCode") != 0) {
            return null;
        }
        String indicator = response.jsonPath().getString("data.planRenewalWindowIndicator");
        return indicator == null ? "" : indicator.trim();
    }

    private String stringVal(Map<String, Object> row, String key) {
        if (row == null || key == null) {
            return null;
        }
        Object value = row.get(key);
        if (value == null) {
            for (Map.Entry<String, Object> entry : row.entrySet()) {
                if (entry.getKey() != null && entry.getKey().equalsIgnoreCase(key)) {
                    value = entry.getValue();
                    break;
                }
            }
        }
        if (value == null) {
            return null;
        }
        String text = value.toString().trim();
        return text.isEmpty() ? null : text;
    }

    private Map<String, Object> resolveInitiatedBillDeliveryAccount() {
        Map<String, Object> account = new GetPaperlessEnrollmentEligibilitySetupHelper(testContext)
                .ensureActivePendingBillEnrollment();
        account = normalizeCustomerPremisesKeys(account);
        VerifyAccountEvidenceUtil.captureInitiatedDeliveryInProgressEvidence(
                account.get("customerCode").toString(),
                account.get("premisesCode").toString(),
                true,
                "TC_13 GetAccountInfo billDeliveryOption=I");
        return account;
    }

    private Map<String, Object> resolveInitiatedCorrespondenceDeliveryAccount() {
        Map<String, Object> account = new GetPaperlessEnrollmentEligibilitySetupHelper(testContext)
                .ensureActivePendingCorrEnrollment();
        account = normalizeCustomerPremisesKeys(account);
        VerifyAccountEvidenceUtil.captureInitiatedDeliveryInProgressEvidence(
                account.get("customerCode").toString(),
                account.get("premisesCode").toString(),
                false,
                "TC_14 GetAccountInfo correspondenceDeliveryOption=I");
        return account;
    }

    private Map<String, Object> normalizeCustomerPremisesKeys(Map<String, Object> account) {
        if (account == null) {
            throw new IllegalStateException("No account found for initiated paperless delivery option");
        }
        Object customer = firstPresent(account, "customerCode", "CUSTOMERCODE", "ucracct_cust_code", "UCRACCT_CUST_CODE");
        Object premises = firstPresent(account, "premisesCode", "PREMISESCODE", "ucracct_prem_code", "UCRACCT_PREM_CODE");
        if (customer == null || premises == null) {
            throw new IllegalStateException("Account missing customerCode/premisesCode: " + account.keySet());
        }
        account.put("customerCode", customer.toString());
        account.put("premisesCode", premises.toString());
        return account;
    }

    private Object firstPresent(Map<String, Object> account, String... keys) {
        for (String key : keys) {
            if (account.containsKey(key) && account.get(key) != null) {
                return account.get(key);
            }
        }
        return null;
    }
}
