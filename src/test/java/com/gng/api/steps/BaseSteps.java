package com.gng.api.steps;

import com.gng.api.context.ApplicationContext;
import com.gng.api.pojo.CSIPojo.UpdateAccountNickname.UpdateAccountNicknameResponse;
import com.gng.api.pojo.TestContext.TestContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.testng.AssertJUnit.*;

@Slf4j
public class BaseSteps {
    private final TestContext testContext;

    @ParameterType("true|false")
    public Boolean booleanVal(String value) {
        return Boolean.valueOf(value);
    }

    public BaseSteps(TestContext testContext) {
        this.testContext = testContext;
    }

    @Then("verify response code of {string} Api is {int}")
    public void verify_response_code_of_api_is(String apiName, Integer statusCode) {
        verifyResponseCode(apiName, statusCode);
    }

    @And("response should have ErrorCode {int} and ErrorMessage {string}")
    public void responseShouldHaveErrorCodeAndErrorMessage(int errorCode, String errorMessage) {
        verifyErrorCodeAndMessage(errorCode, errorMessage);
    }

    @And("response should have a SSP eligible as {string} and {string}")
    public void responseShouldHaveWarningForSSPEligibility(String sspEligibility, String Warning){
        verifySSPEligibilityAndWarning(booleanVal(sspEligibility),Warning);
    }

    @And("the response should have the username status as {string}")
    public void verifyUsernameStatus(String status){
        validateUsernameStatus(status);
    }

    @And("the response should have data as {string}")
    public void validateDataFieldStep(String expectedData) {
        validateDataField(expectedData);
    }


    @And("response should have ErrorCode {int} and ErrorMessage {string} with Invalid State code {string}")
    public void responseShouldHaveErrorCodeAndErrorMessageWithInvalidStateCode(int errorCode, String errorMessage, String invalidStateCode) {
        verifyErrorCodeAndMessage(errorCode, errorMessage + " " + invalidStateCode);
    }

    @And("response should have ErrorCode {int} and ErrorMessage {string} with Invalid premise zip code {string}")
    public void responseShouldHaveErrorCodeAndErrorMessageWithInvalidPremiseZipCode(int errorCode, String errorMessage, String invalidPremiseZipCode) {
        verifyErrorCodeAndMessage(errorCode, errorMessage + " " + invalidPremiseZipCode);
    }

    @And("response should return numberOfMatches as {int}")
    public void responseShouldReturnNumberOfMatchesAs(int numberOfMatches) {
        verifyNumberOfMatches(numberOfMatches);
    }

    @And("response should have {string} to {string}")
    public void responseShouldHavePaymentFieldsAs(String field, String value){
        verifyPaymentFields(field,value);
    }

    @And("response should have {string} as {string}")
    public void enrollmentStateresponseShouldHaveFieldAs(String field, String value){
        verifyFieldInResponse(field, value);
    }

    @And("the response should confirm nickname status as {string}")
    public void responseShouldConfirmNicknameStatusAs(String expectedStatus) {
        UpdateAccountNicknameResponse response = testContext.getUpdateAccountNicknameResponse();
        String actualStatus = response.getData().getNicknameStatus();
        assertThat("Nickname status mismatch", actualStatus, is(expectedStatus));
    }

    @And("response should have {string} flag as {string}")
    public void responseShouldHaveFlagAs(String flag, String value){
        verifyFlagValueInResponse(flag, booleanVal(value));
    }

    @And("response should have {int} turnOffReasons")
    public void responseShouldHaveTurnOffReasons(int count) {
        verifyNumberOfTurnOffReasons(count);
    }

    @And("the response should contain the following turnOffReasons:")
    public void verifyTurnOffReasons(DataTable dataTable) {
        List<Map<String, String>> expectedReasons = dataTable.asMaps(String.class, String.class);
        validateTurnOffReasons(expectedReasons);
    }

    @And("the response should contain the following reasonForTurnOffAlerts:")
    public void verifyReasonForTurnOffAlerts(DataTable dataTable) {
        List<Map<String, String>> expectedAlerts = dataTable.asMaps(String.class, String.class);
        validateReasonForTurnOffAlerts(expectedAlerts);
    }

    @And("the response should contain the following plans:")
    public void verifyPlans(DataTable dataTable) {
        List<Map<String, String>> expectedPlans = dataTable.asMaps(String.class, String.class);
        validatePlans(expectedPlans);
    }

    @And("response should have the following roles")
    public void responseShouldHaveTheFollowingRoles(DataTable dataTable) {
        List<Map<String, String>> expectedRoles = dataTable.asMaps(String.class, String.class);
        verifyRoleDetails(expectedRoles);
    }

    @And("the response should have the rewards status as {string}")
    public void responseShouldHaveRewardsStatusAs(String status) {
        verifyRewardsStatusInResponse(status);
    }

    @And("the response should have success as {string}")
    public void responseShouldHaveSuccessAs(String success) {
        verifySuccess(Boolean.parseBoolean(success));
    }
    @And("response should have email existence as {string}")
    public void responseShouldHaveEmailExistenceAs(String expected) {
        verifyEmailExistence(expected);
    }

    @And("response should have partner promotions indicator as {string}")
    public void responseShouldHavePartnerPromotionsIndicatorAs(String expectedValue) {
        verifyPartnerPromotionsIndicator(expectedValue);
    }

    @And("response should have marketing offers indicator as {string}")
    public void responseShouldHaveMarketingOffersIndicatorAs(String expectedValue) {
        verifyMarketingOffersIndicator(expectedValue);
    }

    @And("response should have account billing reminder as {string}")
    public void responseShouldHaveAccountBillingReminderAs(String expectedValue) {
        verifyAccountBillingReminder(expectedValue);
    }

    @And("response should have phone number existence as {string}")
    public void responseShouldHavePhoneNumberExistenceAs(String expected) {
        verifyPhoneNumberExistence(expected);
    }

    @And("response should validate greener life rate as {string}")
    public void responseShouldValidateGreenerLifeRateAs(String expected) {
        verifyGreenerLifeRate(expected);
    }

    @And("response should have bill delivery option as {string}")
    public void responseShouldHaveBillDeliveryOptionAs(String expectedValue) {
        verifyBillDeliveryOption(expectedValue);
    }

    @And("response should have correspondence delivery option as {string}")
    public void responseShouldHaveCorrespondenceDeliveryOptionAs(String expectedValue) {
        verifyCorrespondenceDeliveryOption(expectedValue);
    }

    @And("response should have price plan count as {string}")
    public void responseShouldHavePricePlanCountAs(String expectedType) {
        verifyPricePlanCount(expectedType);
    }

    @And("response should validate guaranteed bill plan for {string}")
    public void responseShouldValidateGuaranteedBillPlanFor(String testCondition) {
        verifyGuaranteedBillPlan(testCondition);
    }

    @And("response should validate price protection guarantee plan for {string}")
    public void responseShouldValidatePriceProtectionGuaranteePlanFor(String testCondition) {
        verifyPriceProtectionGuaranteePlan(testCondition);
    }

    @And("response should validate rollover plan indicator for {string}")
    public void responseShouldValidateRolloverPlanIndicatorFor(String testCondition) {
        verifyRolloverPlanIndicator(testCondition);
    }

    @And("response should validate restricted plan indicator for {string}")
    public void responseShouldValidateRestrictedPlanIndicatorFor(String testCondition) {
        verifyRestrictedPlanIndicator(testCondition);
    }

    @And("response should validate discounts for {string}")
    public void responseShouldValidateDiscountsFor(String testCondition) {
        verifyDiscounts(testCondition);
    }

    @And("accounts should be sorted by accountStatus in order A, F, N, I")
    public void accountsShouldBeSortedByAccountStatus() {
        validateAccountStatusSorting();
    }

    @And("accounts should be alphabetically sorted by customerFirstName when status and last name match")
    public void accountsShouldBeAlphabeticallySortedByCustomerFirstName() {
        validateAlphabeticalSortingForMatchingStatusAndLastName();
    }

    @And("the response should have bankDraftStatus as {string}")
    public void validateBankDraftStatusStep(String expectedStatus) {
        validateBankDraftStatus(expectedStatus);
    }

    private void validateBankDraftStatus(String expectedStatus) {
        Response response = testContext.getResponse();

        String actualStatus = response.jsonPath().getString("data.bankDraftStatus");

        assertThat("bankDraftStatus value mismatch", actualStatus, equalTo(expectedStatus));
    }

    @And("perform the validation for {string}")
    public void performValidationForTestCondition(String testCondition) {
        validateBankDraftInfoFields(testCondition);
    }

    private void validateBankDraftInfoFields(String testCondition) {
        Response response = testContext.getResponse();

        switch (testCondition) {

            case "TC_16__Positive__BankRoutingNumber": {
                String routing = response.jsonPath().getString("data.bankDraftRoutingNumber");

                assertThat("bankDraftRoutingNumber should not be null", routing, notNullValue());
                assertThat("bankDraftRoutingNumber should be at least 4 characters long", routing.length() >= 4, equalTo(true));

                String last4 = routing.substring(routing.length() - 4);
                String maskedPart = routing.substring(0, routing.length() - 4);

                assertThat("bankDraftRoutingNumber last 4 digits should be numeric", last4.matches("\\d{4}"), equalTo(true));
                assertThat("bankDraftRoutingNumber should be masked except last 4 digits", maskedPart.matches("[*]+"), equalTo(true));
                break;
            }

            case "TC_17__Positive__BankAccountNumber": {
                String acct = response.jsonPath().getString("data.bankDraftAccountNumber");

                assertThat("bankDraftAccountNumber should not be null", acct, notNullValue());
                assertThat("bankDraftAccountNumber should be at least 4 characters long", acct.length() >= 4, equalTo(true));

                String last4 = acct.substring(acct.length() - 4);
                String maskedPart = acct.substring(0, acct.length() - 4);

                assertThat("bankDraftAccountNumber last 4 digits should be numeric", last4.matches("\\d{4}"), equalTo(true));
                assertThat("bankDraftAccountNumber should be masked except last 4 digits", maskedPart.matches("[*]+"), equalTo(true));
                break;
            }

            case "TC_18__Positive__BankAccountType_Checking": {
                String type = response.jsonPath().getString("data.bankDraftAccountType");
                assertThat("bankDraftAccountType mismatch", type, equalTo("CHECKING"));
                break;
            }

            case "TC_19__Positive__BankAccountType_Savings": {
                String type = response.jsonPath().getString("data.bankDraftAccountType");
                assertThat("bankDraftAccountType mismatch", type, equalTo("SAVINGS"));
                break;
            }

            case "TC_20__Positive__BankName": {
                String bankName = response.jsonPath().getString("data.bankName");
                assertThat("bankName should not be null", bankName, notNullValue());
                assertThat("bankName should not be empty", bankName.trim().isEmpty(), equalTo(false));
                break;
            }

            default:
                throw new IllegalArgumentException("Unknown test condition: " + testCondition);
        }
    }



    private void validateAlphabeticalSortingForMatchingStatusAndLastName() {
        Response response = testContext.getResponse();

        List<Map<String, Object>> accounts =
                response.jsonPath().getList("data.accounts");

        if (accounts == null || accounts.size() <= 1) {
            return; // nothing to validate
        }

        // Group accounts by (status + lastName)
        Map<String, List<Map<String, Object>>> grouped = accounts.stream()
                .collect(Collectors.groupingBy(acc ->
                        acc.get("accountStatus") + "|" +
                                acc.get("customerLastNameBusiness")
                ));

        for (Map.Entry<String, List<Map<String, Object>>> entry : grouped.entrySet()) {

            List<Map<String, Object>> group = entry.getValue();

            // Only validate groups with 2 or more accounts
            if (group.size() <= 1) {
                continue;
            }

            // Extract actual first names
            List<String> actualNames = group.stream()
                    .map(acc -> acc.get("customerFirstName").toString())
                    .collect(Collectors.toList());

            // Expected sorted list
            List<String> expectedNames = new ArrayList<>(actualNames);
            expectedNames.sort(String.CASE_INSENSITIVE_ORDER);

            // Human-readable assertion
            assertThat(
                    "Accounts with same status and last name are NOT sorted alphabetically by customerFirstName\n" +
                            "Group: " + entry.getKey() + "\n" +
                            "Expected alphabetical: " + expectedNames + "\n" +
                            "Actual:               " + actualNames,
                    actualNames,
                    equalTo(expectedNames)
            );
        }
    }


    private void validateAccountStatusSorting() {
        Response response = testContext.getResponse();

        List<Map<String, Object>> accounts =
                response.jsonPath().getList("data.accounts");

        if (accounts == null || accounts.size() <= 1) {
            return; // nothing to validate
        }

        // Required order
        List<String> requiredOrder = Arrays.asList("A", "F", "N", "I");

        // Map status → rank
        Map<String, Integer> statusRank = new HashMap<>();
        statusRank.put("A", 1);
        statusRank.put("F", 2);
        statusRank.put("N", 3);
        statusRank.put("I", 4);

        // Extract actual statuses from response
        List<String> actualStatuses = accounts.stream()
                .map(acc -> acc.get("accountStatus").toString())
                .collect(Collectors.toList());

        // Create expected sorted version
        List<String> expectedStatuses = new ArrayList<>(actualStatuses);
        expectedStatuses.sort(Comparator.comparingInt(statusRank::get));

        // Human-readable assertion
        assertThat(
                "Accounts are NOT sorted by accountStatus in order A, F, N, I\n" +
                        "Expected: " + expectedStatuses + "\n" +
                        "Actual:   " + actualStatuses,
                actualStatuses,
                equalTo(expectedStatuses)
        );
    }



    private void verifyDiscounts(String testCondition) {
        Response response = testContext.getResponse();

        List<Map<String, Object>> discounts = response.jsonPath().getList("data.discounts");
        Assert.assertNotNull(discounts, "discounts array should not be null");

        int count = discounts.size();

        boolean isSingle = testCondition.contains("TC_183");
        boolean isMultiple = testCondition.contains("TC_184");
        boolean isNone = testCondition.contains("TC_185");
        boolean isTransferable = testCondition.contains("TC_186");
        boolean isNonTransferable = testCondition.contains("TC_187");

        // --- COUNT VALIDATIONS ---
        if (isSingle) {
            Assert.assertEquals(count, 1, "Expected exactly 1 discount for single discount scenario");
            return;
        }

        if (isMultiple) {
            Assert.assertTrue(count > 1, "Expected multiple discounts for multiple discount scenario");
            return;
        }

        if (isNone) {
            Assert.assertEquals(count, 0, "Expected no discounts for no-discount scenario");
            return;
        }

        // --- TRANSFERABILITY VALIDATIONS ---
        boolean matchFound = false;

        for (Map<String, Object> discount : discounts) {
            Object indicator = discount.get("discountTransferabilityIndicator");
            String value = indicator == null ? "" : indicator.toString().trim();

            if (isTransferable && value.equalsIgnoreCase("Y")) {
                matchFound = true;
                break;
            }

            if (isNonTransferable && value.equalsIgnoreCase("N")) {
                matchFound = true;
                break;
            }
        }

        Assert.assertTrue(
                matchFound,
                "No discount matched expected transferability indicator for: " + testCondition
        );
    }

    private void verifyRestrictedPlanIndicator(String testCondition) {
        Response response = testContext.getResponse();

        List<Map<String, Object>> plans = response.jsonPath().getList("data.pricePlans");
        Assert.assertNotNull(plans, "pricePlans array should not be null");
        Assert.assertTrue(plans.size() > 0, "pricePlans array should contain at least one plan");

        boolean expectRestricted = testCondition.contains("TC_181"); // TC_181 = restricted expected

        boolean matchFound = false;

        for (Map<String, Object> plan : plans) {

            Object indicator = plan.get("restrictedPlanIndicator");
            String value = indicator == null ? "" : indicator.toString().trim();

            if (!expectRestricted) {
                // TC_180: Non-Restricted → expect "N"
                if (value.equalsIgnoreCase("N")) {
                    matchFound = true;
                    break;
                }
            } else {
                // TC_181: Restricted → expect "Y"
                if (value.equalsIgnoreCase("Y")) {
                    matchFound = true;
                    break;
                }
            }
        }

        Assert.assertTrue(
                matchFound,
                "No plan matched expected restricted plan indicator for: " + testCondition
        );
    }

    private void verifyRolloverPlanIndicator(String testCondition) {
        Response response = testContext.getResponse();

        List<Map<String, Object>> plans = response.jsonPath().getList("data.pricePlans");
        Assert.assertNotNull(plans, "pricePlans array should not be null");
        Assert.assertTrue(plans.size() > 0, "pricePlans array should contain at least one plan");

        boolean expectRollover = testCondition.contains("TC_179"); // TC_179 = rollover expected

        boolean matchFound = false;

        for (Map<String, Object> plan : plans) {

            Object indicator = plan.get("rolloverPlanIndicator");
            String value = indicator == null ? "" : indicator.toString().trim();

            if (!expectRollover) {
                // TC_178: No Rollover → expect "N"
                if (value.equalsIgnoreCase("N")) {
                    matchFound = true;
                    break;
                }
            } else {
                // TC_179: Rollover → expect "Y"
                if (value.equalsIgnoreCase("Y")) {
                    matchFound = true;
                    break;
                }
            }
        }

        Assert.assertTrue(
                matchFound,
                "No plan matched expected rollover indicator for: " + testCondition
        );
    }


    private void verifyPriceProtectionGuaranteePlan(String testCondition) {
        Response response = testContext.getResponse();

        List<Map<String, Object>> plans = response.jsonPath().getList("data.pricePlans");
        Assert.assertNotNull(plans, "pricePlans array should not be null");
        Assert.assertTrue(plans.size() > 0, "pricePlans array should contain at least one plan");

        boolean expectPPG = testCondition.contains("TC_177"); // TC_177 = PPG plan expected

        boolean matchFound = false;

        for (Map<String, Object> plan : plans) {

            Object fee = plan.get("priceProtectionGuaranteeFee");
            Object ceiling = plan.get("priceProtectionGuaranteeCeiling");

            boolean feeEmpty = isEmptyOrZero(fee);
            boolean ceilingEmpty = isEmptyOrZero(ceiling);

            if (!expectPPG) {
                // TC_176: No Price Protection Guarantee Plan
                if (feeEmpty && ceilingEmpty) {
                    matchFound = true;
                    break;
                }
            } else {
                // TC_177: Price Protection Guarantee Plan
                if (!feeEmpty && !ceilingEmpty) {
                    matchFound = true;
                    break;
                }
            }
        }

        Assert.assertTrue(
                matchFound,
                "No price plan matched the expected Price Protection Guarantee rules for: " + testCondition
        );
    }
    private boolean isEmptyOrZero(Object value) {
        if (value == null) return true;

        String str = String.valueOf(value).trim();
        if (str.isEmpty()) return true;

        try {
            double num = Double.parseDouble(str);
            return num == 0.0;
        } catch (NumberFormatException e) {
            return false;
        }
    }



    private void verifyGuaranteedBillPlan(String testCondition) {
        Response response = testContext.getResponse();

        List<Map<String, Object>> plans = response.jsonPath().getList("data.pricePlans");
        Assert.assertNotNull(plans, "pricePlans array should not be null");
        Assert.assertTrue(plans.size() > 0, "pricePlans array should contain at least one plan");

        boolean isGuaranteedPlan = testCondition.contains("TC_175");

        boolean matchFound = false;

        for (Map<String, Object> plan : plans) {

            Object gbpAmount = plan.get("planGBPAmount");
            Object thermPrice = plan.get("planThermPrice");
            Object serviceCharge = plan.get("planServiceCharge");

            if (!isGuaranteedPlan) {
                // TC_174: No Guaranteed Bill Plan
                if (gbpAmount == null &&
                        thermPrice != null &&
                        serviceCharge != null) {

                    matchFound = true;
                    break;
                }

            } else {
                // TC_175: Guaranteed Bill Plan
                boolean thermEmpty =
                        thermPrice == null ||
                                String.valueOf(thermPrice).trim().isEmpty() ||
                                String.valueOf(thermPrice).trim().equals("0.0") ||
                                String.valueOf(thermPrice).trim().equals("0");

                boolean serviceEmpty =
                        serviceCharge == null ||
                                String.valueOf(serviceCharge).trim().isEmpty() ||
                                String.valueOf(serviceCharge).trim().equals("0.0") ||
                                String.valueOf(serviceCharge).trim().equals("0");

                if (gbpAmount != null && thermEmpty && serviceEmpty) {
                    matchFound = true;
                    break;
                }
            }
        }

        Assert.assertTrue(
                matchFound,
                "No price plan matched the expected Guaranteed/Non-Guaranteed Bill Plan rules"
        );
    }


    private void verifyPricePlanCount(String expectedType) {
        Response response = testContext.getResponse();
        List<?> plans = response.jsonPath().getList("data.pricePlans");

        int count = (plans == null) ? 0 : plans.size();
        boolean isSingle = "single".equalsIgnoreCase(expectedType);
        boolean isMultiple = "multiple".equalsIgnoreCase(expectedType);

        boolean matches = false;

        if (isSingle) {
            matches = count == 1;
        } else if (isMultiple) {
            matches = count > 1;
        }

        assertThat(
                "Price plan count mismatch. Expected type: " + expectedType + ", Actual count: " + count,
                matches,
                is(true)
        );
    }


    private void verifyCorrespondenceDeliveryOption(String expectedValue) {
        Response response = testContext.getResponse();
        Object actualValue = response.jsonPath().get("data.correspondenceDeliveryOption");

        String actual = actualValue == null ? "null" : actualValue.toString().trim();
        String expected = expectedValue.trim();

        boolean matches = actual.equalsIgnoreCase(expected);

        assertThat(
                "Correspondence Delivery Option mismatch. Expected: " + expected + ", Actual: " + actual,
                matches,
                is(true)
        );
    }


    private void verifyBillDeliveryOption(String expectedValue) {
        Response response = testContext.getResponse();
        Object actualValue = response.jsonPath().get("data.billDeliveryOption");

        String actual = actualValue == null ? "null" : actualValue.toString().trim();
        String expected = expectedValue.trim();

        boolean matches = actual.equalsIgnoreCase(expected);

        assertThat(
                "Bill Delivery Option mismatch. Expected: " + expected + ", Actual: " + actual,
                matches,
                is(true)
        );
    }


    private void verifyGreenerLifeRate(String expected) {
        Response response = testContext.getResponse();
        Object value = response.jsonPath().get("data.greenerLifeRate");

        boolean shouldExist = Boolean.parseBoolean(expected);

        boolean exists = false;

        if (value != null) {
            String str = value.toString().trim();
            exists = !str.isEmpty();   // any non-empty string counts as existing
        }

        assertThat(
                "Greener Life Rate existence mismatch. Expected: " + shouldExist + ", Actual: " + exists,
                exists,
                is(shouldExist)
        );
    }




    private void verifyPhoneNumberExistence(String expected) {
        Response response = testContext.getResponse();
        String phone = response.jsonPath().getString("data.phoneNumber");

        boolean shouldExist = Boolean.parseBoolean(expected);
        boolean exists = phone != null && !phone.trim().isEmpty();

        assertThat(
                "Phone number existence mismatch. Expected: " + shouldExist + ", Actual: " + exists,
                exists,
                is(shouldExist)
        );
    }


    private void verifyAccountBillingReminder(String expectedValue) {
        Response response = testContext.getResponse();
        Object actualValue = response.jsonPath().get("data.accountBillingReminder");

        String actual = actualValue == null ? "null" : actualValue.toString().trim();
        String expected = expectedValue.trim();

        boolean matches = actual.equalsIgnoreCase(expected);

        assertThat(
                "Account Billing Reminder mismatch. Expected: " + expected + ", Actual: " + actual,
                matches,
                is(true)
        );
    }


    private void verifyMarketingOffersIndicator(String expectedValue) {
        Response response = testContext.getResponse();
        Object actualValue = response.jsonPath().get("data.marketingOffersIndicator");

        String actual = actualValue == null ? "null" : actualValue.toString().trim();
        String expected = expectedValue.trim();

        boolean matches = actual.equalsIgnoreCase(expected);

        assertThat(
                "Marketing Offers Indicator mismatch. Expected: " + expected + ", Actual: " + actual,
                matches,
                is(true)
        );
    }


    private void verifyPartnerPromotionsIndicator(String expectedValue) {
        Response response = testContext.getResponse();
        Object actualValue = response.jsonPath().get("data.partnerPromotionsIndicator");

        String actual = actualValue == null ? "null" : actualValue.toString().trim();
        String expected = expectedValue.trim();

        boolean matches = actual.equalsIgnoreCase(expected);

        assertThat(
                "Partner Promotions Indicator mismatch. Expected: " + expected + ", Actual: " + actual,
                matches,
                is(true)
        );
    }


    private void verifyEmailExistence(String expected) {
        Response response = testContext.getResponse();
        String email = response.jsonPath().getString("data.emailAddress");

        boolean shouldExist = Boolean.parseBoolean(expected);
        boolean exists = email != null && !email.trim().isEmpty();

        assertThat(
                "Email existence mismatch. Expected: " + shouldExist + ", Actual: " + exists,
                exists,
                is(shouldExist)
        );
    }

    private void validateDataField(String expectedDataValue) {
        Response response = testContext.getResponse();

        Object actualData = response.jsonPath().get("data");

        // Case 1: Expecting null
        if ("null".equalsIgnoreCase(expectedDataValue)) {
            assertThat("Expected data to be null but it was not",
                    actualData,
                    equalTo(null));
            return;
        }

        // Case 2: Expecting NOT null
        if ("notNull".equalsIgnoreCase(expectedDataValue)) {
            assertThat("Expected data to NOT be null but it was",
                    actualData != null,
                    equalTo(true));
            return;
        }

    }



    private void verifySuccess(boolean expectedSuccess) {
        Response response = testContext.getResponse();

        assertThat("Incorrect success flag returned",
                response.jsonPath().getBoolean("success"),
                equalTo(expectedSuccess));
    }

    private void verifyRewardsStatusInResponse(String expectedStatus) {
        Response response = testContext.getResponse();
        List<Map<String, Object>> rewards = response.jsonPath().getList("getAccountRewards.rewards");

        boolean matchFound = rewards != null && rewards.stream()
                .anyMatch(reward -> expectedStatus.equals(String.valueOf(reward.get("status"))));

        assertThat("Expected reward status not found: " + expectedStatus, matchFound, is(true));
    }

    private void validateUsernameStatus(String expectedStatus) {
        Response response = testContext.getResponse();

        if(!Objects.equals(expectedStatus, "null")) {
            // Validate usernameStatus at data.usernameStatus
            assertThat("Incorrect usernameStatus returned",
                    response.jsonPath().getString("data.usernameStatus"),
                    equalTo(expectedStatus));
        }
    }


    private void verifyRoleDetails(List<Map<String, String>> expectedRoles) {
        Response response = testContext.getResponse();
        List<Map<String, String>> actualRoles = response.jsonPath().getList("data.roles");

        // Step 1: Validate each expected role is present in the response
        for (Map<String, String> expectedRole : expectedRoles) {
            String expectedRoleID = expectedRole.get("roleID");
            String expectedRoleDescription = expectedRole.get("roleDescription");

            boolean roleFound = actualRoles.stream()
                    .anyMatch(actualRole -> expectedRoleID.equals(actualRole.get("roleID")) &&
                                            expectedRoleDescription.equals(actualRole.get("roleDescription")));

            assertThat("Expected role with ID '" + expectedRoleID + "' and description '" + expectedRoleDescription + "' not found", roleFound);
        }

        // Step 2: Validate role IDs match with database
        List<Map<String, Object>> dbRoleRecords = ApplicationContext.get().getDbAction().getUserRoleIDs("autotester");

        List<String> dbRoleIDs = dbRoleRecords.stream()
                .map(record -> Objects.toString(record.get("role_id"), "").trim())
                .filter(roleId -> !roleId.isEmpty())
                .toList();

        List<String> apiRoleIDs = actualRoles.stream()
                .map(role -> role.get("roleID"))
                .filter(Objects::nonNull)
                .map(String::trim)
                .toList();

        assertThat("Mismatch between role IDs from API and database",
                new HashSet<>(apiRoleIDs).equals(new HashSet<>(dbRoleIDs)));
    }



    private void validatePlans(List<Map<String, String>> expectedPlans) {
        Response response = testContext.getResponse();
        List<Map<String, Object>> actualPlans = response.jsonPath().getList("data.plans");

        for (Map<String, String> expected : expectedPlans) {
            String expectedCode = normalize(expected.get("planCode"));
            String expectedDesc = normalize(expected.get("planDescription"));

            boolean matchFound = actualPlans.stream().anyMatch(plan -> {
                String actualCode = normalize(plan.get("planCode"));
                String actualDesc = normalize(plan.get("planDescription"));
                return expectedCode.equals(actualCode) && expectedDesc.equals(actualDesc);
            });

            assertThat("Plan not found: code=" + expectedCode + ", description=" + expectedDesc, matchFound);
        }
    }

    private void validateReasonForTurnOffAlerts(List<Map<String, String>> expectedAlerts) {
        Response response = testContext.getResponse();
        List<Map<String, Object>> actualReasons = response.jsonPath().getList("data.turnOffReasons");

        for (Map<String, String> expected : expectedAlerts) {
            String reason = normalize(expected.get("reasonForTurnOff"));
            String expectedAlert = normalize(expected.get("reasonForTurnOffAlert"));

            String actualAlert = actualReasons.stream()
                    .filter(r -> reason.equals(normalize(r.get("reasonForTurnOff"))))
                    .map(r -> normalize(r.get("reasonForTurnOffAlert")))
                    .findFirst()
                    .orElse("");

            assertThat("Unexpected reasonForTurnOffAlert for: " + reason, actualAlert, equalTo(expectedAlert));
        }
    }

    private void verifyNumberOfTurnOffReasons(int count) {
        Response response = testContext.getResponse();
        assertThat("Unexpected number of turnOffReasons returned", response.jsonPath().getList("data.turnOffReasons").size(), equalTo(count));
    }

    private String normalize(Object value) {
        return Optional.ofNullable(value)
                .map(Object::toString)
                .map(s -> s.replaceAll("\\u00A0", " "))
                .map(String::trim)
                .orElse("");
    }

    private void validateTurnOffReasons(List<Map<String, String>> expectedReasons) {
        Response response = testContext.getResponse();
        List<Map<String, Object>> actualReasons = response.jsonPath().getList("data.turnOffReasons");

        for (Map<String, String> expected : expectedReasons) {
            String expectedReason = normalize(expected.get("reasonForTurnOff"));
            String expectedSubReason = normalize(expected.get("subReasonForTurnOff"));

            boolean matchFound = actualReasons.stream().anyMatch(actual -> {
                String actualReason = normalize(actual.get("reasonForTurnOff"));
                String actualSubReason = normalize(actual.get("subReasonForTurnOff"));
                return expectedReason.equals(actualReason) && expectedSubReason.equals(actualSubReason);
            });

            assertThat("Expected turnOffReason not found: " + expectedReason + " / " + expectedSubReason, matchFound);
        }
    }

    private void verifyFlagValueInResponse(String flag, Boolean expectedValue) {
        Response response = testContext.getResponse();

        List<Boolean> flagValues = response.jsonPath().getList("data.accounts." + flag, Boolean.class);
        assertThat("Expected at least one account with " + flag + " = " + expectedValue,
                flagValues, hasItem(expectedValue));
    }

    private void verifyFieldInResponse(String field, String value) {
        Response response = testContext.getResponse();
        List<Map<String, Object>> accounts = response.jsonPath().getList("data.accounts");

        boolean matchFound;

        if ("BANK".equalsIgnoreCase(value)) {
            matchFound = accounts.stream()
                    .anyMatch(account -> {
                        Object fieldValue = account.get(field);
                        return fieldValue != null && String.valueOf(fieldValue).toLowerCase().contains("bank");
                    });
        } else if (field.equals("aglcAccountNumber")) {
            matchFound = accounts.stream()
                    .anyMatch(account -> {
                        Object fieldValue = account.get(field);
                        return fieldValue != null && String.valueOf(fieldValue).contains("0000");
                    });
        } else if (field.equals("rewards")) {
            boolean expectedNonEmpty = Boolean.parseBoolean(value);
            matchFound = accounts.stream()
                    .anyMatch(account -> {
                        List<?> rewards = (List<?>) account.get("rewards");
                        return expectedNonEmpty ? rewards != null && !rewards.isEmpty()
                                : rewards == null || rewards.isEmpty();
                    });
        } else if (field.equals("activeDiscounts")) {
            matchFound = accounts.stream()
                    .anyMatch(account -> {
                        List<Map<String, Object>> discounts = (List<Map<String, Object>>) account.get("activeDiscounts");
                        if (discounts == null) return false;
                        return discounts.stream()
                                .anyMatch(discount -> value.equals(discount.get("activeDiscountTransferMessage")));
                    });
        } else {
            matchFound = accounts.stream()
                    .anyMatch(account -> value.equals(String.valueOf(account.get(field))));
        }

        assertThat("Expected value not found in any account for field: " + field, matchFound, is(true));
    }



    private void verifyResponseCode(String apiName, Integer statusCode) {
        assertThat("Invalid Response Code for API: " + apiName,
                testContext.getResponse().statusCode(),
                equalTo(statusCode));
    }

    private void verifyErrorCodeAndMessage(int errorCode, String errorMessage) {
        Response response = testContext.getResponse();
        String actualErrorMessage = response.jsonPath().getString("errorMessage");

        // Normalize expected message
        boolean containsPipe = errorMessage.contains("[PIPE]");
        boolean containsZipCode = errorMessage.contains("Invalid PremisesZipCode");
        boolean containsStateCode = errorMessage.contains("Invalid PremisesStateCode provided");
        String normalizedExpectedMessage = errorMessage.replace("[PIPE]", "|");

        // Validate error code
        assertThat("Incorrect ErrorCode returned",
                response.jsonPath().getInt("errorCode"),
                equalTo(errorCode));

        // Special handling when expected message is literal "null"
        if ("null".equalsIgnoreCase(normalizedExpectedMessage)) {
            assertThat("ErrorMessage should be null or \"null\"",
                    actualErrorMessage == null || "null".equalsIgnoreCase(actualErrorMessage));
            return;
        }

        // Conditional validation based on presence of [PIPE] or specific substrings
        if (containsPipe || containsZipCode || containsStateCode) {
            assertThat("ErrorMessage does not contain expected content",
                    actualErrorMessage,
                    containsString(normalizedExpectedMessage));
        } else {
            assertThat("Incorrect ErrorMessage returned",
                    actualErrorMessage,
                    equalTo(normalizedExpectedMessage));
        }
    }


    private void verifySSPEligibilityAndWarning(boolean sspEligibility, String warning) {
        Response response = testContext.getResponse();

        // Validate ssp Eligibility flag at data.sspEligible
        assertThat("Incorrect Eligibility flag returned",
                response.jsonPath().getBoolean("data.sspEligible"),
                equalTo(sspEligibility));

        // Validate the warning message at data.outMessage
        assertThat("Incorrect Warning message returned",
                response.jsonPath().getString("data.outMessage"),
                equalTo(warning));
    }


    private void verifyNumberOfMatches(int expectedMatches) {
        Response response = testContext.getResponse();

        assertThat("Unexpected number of matches returned",
                response.jsonPath().getInt("data.numberOfMatches"),
                equalTo(expectedMatches));
    }

    private void verifyPaymentFields(String field, String value){
        Response response = testContext.getResponse();
        switch (value.toLowerCase()) {
            case "exist" -> {
                assertTrue("Unexpected " + field + " amount",
                        response.jsonPath().getDouble("data.accounts[0]." + field) > 0);
            }
            case "not empty" -> {
                assertNotNull("Unexpected " + field + " is null", response.jsonPath().getString("data.accounts[0]." + field));
                assertFalse("Unexpected " + field + " is empty", response.jsonPath().getString("data.accounts[0]." + field).trim().isEmpty());
            }
            case "empty" ->{
                assertTrue("Unexpected " + field + " is empty", response.jsonPath().getString("data.accounts[0]." + field).trim().isEmpty());
            }
            case "null" -> {
                assertNull("Unexpected " + field + " is not null", response.jsonPath().getString("data.accounts[0]." + field));
            }
            case "not exist" -> {
                assertEquals("Unexpected " + field + " amount", 0.0,
                        response.jsonPath().getDouble("data.accounts[0]." + field));
            }
        }
    }
}
