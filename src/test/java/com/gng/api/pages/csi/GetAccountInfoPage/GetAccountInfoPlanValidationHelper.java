package com.gng.api.pages.csi.GetAccountInfoPage;

import io.restassured.response.Response;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

public final class GetAccountInfoPlanValidationHelper {

    private GetAccountInfoPlanValidationHelper() {
    }

    public static void validateRenewalIndicatorFormat(Response response) {
        Object indicator = response.jsonPath().get("data.planRenewalWindowIndicator");
        Assert.assertNotNull(indicator,
                "planRenewalWindowIndicator should be present in the response data");
        Assert.assertTrue(indicator instanceof String,
                "planRenewalWindowIndicator should be a String");
    }

    public static void validateRenewalIndicatorValue(Response response, String expectedValue) {
        Object indicator = response.jsonPath().get("data.planRenewalWindowIndicator");
        Assert.assertNotNull(indicator,
                "planRenewalWindowIndicator should be present in the response data");
        Assert.assertEquals(String.valueOf(indicator), expectedValue,
                "Expected planRenewalWindowIndicator = '" + expectedValue + "'");
    }

    public static void validateGuaranteedPlanPricingModel(Response response) {
        List<Map<String, Object>> plans = getPricePlans(response);
        boolean matchFound = false;

        for (Map<String, Object> plan : plans) {
            Object gbpAmount = plan.get("planGBPAmount");
            Object thermPrice = plan.get("planThermPrice");
            Object serviceCharge = plan.get("planServiceCharge");

            if (gbpAmount != null && isEmptyOrZero(thermPrice) && isEmptyOrZero(serviceCharge)) {
                matchFound = true;
                break;
            }
        }

        Assert.assertTrue(matchFound,
                "No price plan matched the expected Guaranteed Bill Plan pricing rules");

        List<Map<String, Object>> discounts = response.jsonPath().getList("data.discounts");
        if (discounts == null || discounts.isEmpty()) {
            return;
        }

        for (Map<String, Object> discount : discounts) {
            Object indicator = discount.get("discountTransferabilityIndicator");
            String value = indicator == null ? "" : indicator.toString().trim();
            Assert.assertEquals(value, "N",
                    "Expected non-transferable discount indicator 'N' but found '" + value + "'");
        }
    }

    public static void validateNonGuaranteedPlanPricingModel(Response response) {
        List<Map<String, Object>> plans = getPricePlans(response);
        boolean matchFound = false;

        for (Map<String, Object> plan : plans) {
            Object gbpAmount = plan.get("planGBPAmount");
            Object thermPrice = plan.get("planThermPrice");
            Object serviceCharge = plan.get("planServiceCharge");

            if (isEmptyOrZero(gbpAmount) && thermPrice != null && serviceCharge != null) {
                matchFound = true;
                break;
            }
        }

        Assert.assertTrue(matchFound,
                "No price plan matched the expected Non-Guaranteed Bill Plan pricing rules");
    }

    public static void validatePlanDescriptionFormat(Response response) {
        List<Map<String, Object>> plans = getPricePlans(response);
        for (int i = 0; i < plans.size(); i++) {
            Map<String, Object> plan = plans.get(i);
            Object description = plan.get("planDescription");
            Assert.assertNotNull(description,
                    "planDescription should be present for price plan index " + i);
            Assert.assertTrue(description instanceof String,
                    "planDescription should be a String for price plan index " + i);
        }
    }

    public static void validatePlanDescriptionValue(Response response) {
        List<Map<String, Object>> plans = getPricePlans(response);
        for (int i = 0; i < plans.size(); i++) {
            Map<String, Object> plan = plans.get(i);
            Object description = plan.get("planDescription");
            Assert.assertNotNull(description,
                    "planDescription should be present for price plan index " + i);
            Assert.assertFalse(description.toString().trim().isEmpty(),
                    "planDescription should be non-empty for price plan index " + i);
        }
    }

    private static List<Map<String, Object>> getPricePlans(Response response) {
        List<Map<String, Object>> plans = response.jsonPath().getList("data.pricePlans");
        Assert.assertNotNull(plans, "pricePlans array should not be null");
        Assert.assertFalse(plans.isEmpty(), "pricePlans array should contain at least one plan");
        return plans;
    }

    private static boolean isEmptyOrZero(Object value) {
        if (value == null) {
            return true;
        }

        String str = String.valueOf(value).trim();
        if (str.isEmpty()) {
            return true;
        }

        try {
            return Double.parseDouble(str) == 0.0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
